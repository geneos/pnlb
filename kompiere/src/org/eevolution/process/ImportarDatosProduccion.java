/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import org.compiere.process.SvrProcess;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import org.compiere.db.CConnection;
import org.compiere.model.MNote;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Env;
/*import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;
import org.compiere.db.CConnection;
import org.compiere.db.CConnectionDialog;
import org.compiere.db.CompiereDatabase;
import org.compiere.install.ConfigurationData;
import org.compiere.install.ConfigurationPanel;
import org.compiere.interfaces.Status;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Trx;
import org.eevolution.model.MOrder;*/

/**
 *
 * @author santiago
 */
public class ImportarDatosProduccion extends SvrProcess{

    private Timestamp from;
    private Timestamp to;

    // TIPOS SQL
    private static String STRING_TYPE = "STRING";
    private static String NUMBER_TYPE = "NUMBER";
    private static String DATE_TYPE = "DATE";
    private static String CHAR_TYPE = "CHAR";
    private static String NVARCHAR2_TYPE = "NVARCHAR2";
    private static String VARCHAR_TYPE = "VARCHAR";

    private ArrayList<String> columnas;
    private ArrayList<String> tipos;
    private int colCount;

    private String tableName = "C_ORDER";
    //Nombre de la clave primaria de la tabla tableName
    private String tableNamePK;

    //Este
    private String mensajeError;

    private boolean issotrx;
    private String docType;

    //Guarda pares Clave extranjera y un boolean que indica si existe o no el registro referenciado
    private HashMap foreignKeys;
    //Guarda pares Clave extranjera y tabla a la que hace referencia
    private HashMap foreignKeysTabla;
    //Guarda pares columna de la tabla que la referencia y Clave extranjera
    private HashMap foreignKeysColumna;
    //Guarda pares Clave extranjera y columna que es clave primaria en la tabla referenciada
    private HashMap foreignKeysColumnaRef;
    //Guarda claves Clave extranjera y columna de la tabla que la referencia (inverso a foreigKeyColumna)
    private HashMap columnaForeignKey;

    //Conexión donde se van a insertar los datos (Destino)
    private Connection conexionInicial;

    //Coenxion de la BD desde donde se van a obtener los datos (Origen)
    private Connection conexionFuente;

    //Identificador de una fila en particular que se quiera importar
    public int ID=0;

    private String host;
    private int port;
    private String bd;
    private String uid;
    private String pass;

    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (name.equals("DateOrdered")){
                from = ((Timestamp) para[i].getParameter());
                to = ((Timestamp) para[i].getParameter_To());
            }
            else if (name.equals("Name")){
                tableName = (String) para[i].getParameter();
            }
            else {
                log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
            }
        }
    }

    protected String doIt() throws Exception{
        System.out.println("***********************"+tableName+"***********************");
        //Obtengo la conexion de lectura escritura de la BD
        PreparedStatement ps = DB.prepareStatement("select * from AD_Connection",null);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
        	host = rs.getString("Host");
            pass = rs.getString("pass");
            port = rs.getInt("port");
            bd = rs.getString("bd");
            uid = rs.getString("uid");
        }
        ps.close();
        rs.close();
        ps = null;
        rs = null;
        conexionInicial = DB.getConnectionRW();
        //Obtengo la conexion desde donde voy a extraer las OC.
        CConnection c=CConnection.get("Oracle", host, port, bd, uid, pass);
        conexionFuente = c.getConnection(true, Connection.TRANSACTION_READ_COMMITTED);
        procesar(tableName);
        if (this.ID==0){
	        procesar("C_ORDERLINE");
	        procesar("M_INOUT");
	        procesar("M_INOUTLINE");
	        /*try{
		        ps = DB.prepareStatement("select tabla from AD_IMPORTACION", null);
		        rs = ps.executeQuery();
		        while(rs.next()){
		        	procesar(rs.getString(1));
		        }
	        }
	        catch(SQLException ex){
	        }*/
        }
        return "ok";
    }

    private void procesar(String tableName) throws SQLException{
        System.out.println("En: procesar()");
        columnas = new ArrayList<String>();
        tipos = new ArrayList<String>();
        foreignKeys = new HashMap();
        foreignKeysTabla = new HashMap();
        foreignKeysColumna = new HashMap();
        foreignKeysColumnaRef = new HashMap();
        columnaForeignKey = new HashMap();
        this.tableName = tableName;
        this.tableNamePK = getPrimaryKey(tableName);
        cargarTabla();
        System.out.println("Saliendo de: procesar()");
    }

    private String getInsertSatement(){
        System.out.println("En: getInsertSatement()");
        String sqlIns = "insert into "+tableName+"(";
        for (int i=0;i<colCount-1;i++){
            sqlIns+= columnas.get(i)+", ";
        }
        if (colCount>0)
            sqlIns+= columnas.get(colCount-1)+")";

        sqlIns+= " values(";
        for (int i=0;i<colCount-1;i++){
            sqlIns+= "?,";
        }
        sqlIns+="?)";
        System.out.println("Saliendo de: getInsertSatement()");
        return sqlIns;
    }

    private void cargarFila(String sql,ResultSet rs) throws SQLException{
        //Cargo la fila solo si aun no existe
        int valorPK;
        if (tableNamePK==null||tableNamePK.equals(""))
        	valorPK=0;
        else
        	valorPK = rs.getInt(tableNamePK);
        if (!existeClaveExtranjera(tableName, tableNamePK, valorPK)){
            System.out.println("En: cargarFila()");
            PreparedStatement ps = conexionInicial.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            for (int i=1;i<=colCount;i++){
                //Primero chequeo si es clave extranjera
                if (foreignKeysColumna.containsKey(columnas.get(i-1))){
                    String tabla = columnas.get(i-1);
                    String fk = (String) foreignKeysColumna.get(columnas.get(i-1));
                    tabla = (String) foreignKeysTabla.get(fk);
                    String colPK = (String) foreignKeysColumnaRef.get(fk);
                    if (!colPK.equals("AD_LANGUAGE")){
                        if (rs.getInt(i)!=0 && !existeClaveExtranjera(tabla, colPK, rs.getInt(i))){
                            ImportarDatosProduccion process = new ImportarDatosProduccion();
                            process.setTableName(tabla);
                            process.ID = rs.getInt(i);
                            process.startProcess(getCtx(), this.getProcessInfo(), null);
                            //foreignKeys.put(fk,false);
                        }
                    }
                }
                if (tipos.get(i-1).equals(NUMBER_TYPE))
                    ps.setBigDecimal(i, rs.getBigDecimal(i));
                else if (tipos.get(i-1).equals(DATE_TYPE))
                    ps.setDate(i, rs.getDate(i));
                else
                    ps.setString(i, rs.getString(i));
            }
            try{
                if (valorPK==1013281)
                    System.out.println("ACA");
                ps.executeUpdate();
                System.out.println("Documento Creado: "+tableName+"ID = "+ID);
                MNote note = new MNote(getCtx(), null,Env.getAD_User_ID(getCtx()), Env.getAD_Client_ID(getCtx()), Env.getAD_Org_ID(getCtx()), null);
                note.setTextMsg("Se ha creado "+getNombreTabla(tableName)+" - "+getValorIdentificatorio(tableName,tableNamePK,valorPK));
                note.setReference("Proceso de Importacion - Fila Importada");
                /*
                 *  03/06/2013 Maria Jesus
                 *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                 */
                if (!note.save()){
                    log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ "Se ha creado "+getNombreTabla(tableName)+" - "+getValorIdentificatorio(tableName,tableNamePK,valorPK));
                }
            }
            catch(SQLException ex){
                System.out.println("NO SE CREO"+tableName+" excepcion: "+ex.getMessage());
                MNote note = new MNote(getCtx(), null,Env.getAD_User_ID(getCtx()), Env.getAD_Client_ID(getCtx()), Env.getAD_Org_ID(getCtx()), null);
                Set existenciaFK = foreignKeys.entrySet();
                Iterator it = existenciaFK.iterator();
                String mje="";
                while (it.hasNext()){
                    String map = ((Object) it.next()).toString();
                    String fk = map.substring(0, map.lastIndexOf("="));
                    String valor = map.substring(map.indexOf("=")+1, map.length());
                    boolean existe = valor .equals("true") ? true : false;
                    if (!existe && rs.getInt((String) columnaForeignKey.get(fk))!=0){
                        String nombre = getNombreTabla((String)foreignKeysTabla.get(fk));
                        mje+="\n"+nombre+" = "+getValorIdentificatorio((String)foreignKeysTabla.get(fk), (String) foreignKeysColumnaRef.get(fk), rs.getInt((String) foreignKeysColumnaRef.get(fk)));
                    }
                }
                note.setTextMsg(getNombreTabla(tableName)+": "+getValorIdentificatorio(tableName,tableNamePK,valorPK)+" no creada. Es necesario crear antes los siguientes elementos:"+mje);
                /*
                 *  03/06/2013 Maria Jesus
                 *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                 */
                if (!note.save()){
                    log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ getNombreTabla(tableName)+": "+getValorIdentificatorio(tableName,tableNamePK,valorPK)+" no creada. Es necesario crear antes los siguientes elementos:"+mje);
                }
            }
            ps.close();
            ps = null;
        }
        System.out.println("Saliendo de: cargarFila()");
        //
    }

    private void cargarTabla() throws SQLException{
        System.out.println("En: cargarTabla()");
        try{
            cargarMetadatos();
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
            //throw new SQLException();
        }
        PreparedStatement ps = getDatosAImportar();
        ResultSet rs = ps.executeQuery();
        String sql = getInsertSatement();
        //Mientras haya ordenes de compra
        while (rs.next()){
            if (ID==0)
                System.out.println("Comenzando a crear Orden de Compra numero: ");
            cargarFila(sql, rs);
        }
        rs.close();
        ps.close();
        rs = null;
        ps = null;
        System.out.println("Saliendo de: cargarTabla()");
    }

    private void cargarMetadatos() throws SQLException{
        System.out.println("En: cargarMetadatos()");
        //Vacio todas las hashtables
        foreignKeys.clear();
        foreignKeysTabla.clear();
        foreignKeysColumnaRef.clear();
        foreignKeysColumna.clear();
        columnaForeignKey.clear();
        columnas.clear();
        tipos.clear();
        String sql = "select * from "+tableName;
        PreparedStatement ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData metadata = rs.getMetaData();

        // OBTENGO LAS FOREIGN KEYS
        DatabaseMetaData DBmetadata = conexionInicial.getMetaData();
        ResultSet rsFk = DBmetadata.getImportedKeys(null,null,tableName);
        int j=0;

        //Agregado para documentacion
        /*int tabID = 180;
        String sqlWind = "select upper(col.columnname), ft.name from ad_field f "+
                         " join ad_field_trl ft on f.ad_field_id = ft.ad_field_id"+
                         " join ad_column col on col.ad_column_id = f.ad_column_id"+
                         " join ad_tab t on t.ad_tab_id = f.ad_tab_id"+
                         " where t.ad_tab_id = "+tabID;
        PreparedStatement pstmt = conexionInicial.prepareStatement(sqlWind, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet resset = pstmt.executeQuery();
        HashMap fields = new HashMap();
        while(resset.next()){
            fields.put(resset.getString(1),resset.getString(2));
        }*/
        while (rsFk.next()){
            foreignKeys.put(rsFk.getString("FK_NAME"), true);
            foreignKeysTabla.put(rsFk.getString("FK_NAME"),rsFk.getString("PKTABLE_NAME"));
            foreignKeysColumnaRef.put(rsFk.getString("FK_NAME"), rsFk.getString("PKCOLUMN_NAME"));
            foreignKeysColumna.put(rsFk.getString("FKCOLUMN_NAME"),rsFk.getString("FK_NAME"));
            columnaForeignKey.put(rsFk.getString("FK_NAME"),rsFk.getString("FKCOLUMN_NAME"));
            //System.out.println(" "+j+1);
            /*System.out.println("PKTABLE: "+rsFk.getString("PKTABLE_NAME"));
            System.out.println("PKCOLUMN_NAME: "+rsFk.getString("PKCOLUMN_NAME"));
            System.out.println("FK_NAME: "+rsFk.getString("FK_NAME"));
            System.out.println("FKTABLE_NAME: "+rsFk.getString("FKTABLE_NAME"));
            System.out.println("FKCOLUMN_NAME: "+rsFk.getString("FKCOLUMN_NAME"));*/
            //if (fields.get(rsFk.getString("FKCOLUMN_NAME"))!=null)
                //System.out.println(fields.get(rsFk.getString("FKCOLUMN_NAME"))+"("+rsFk.getString("PKTABLE_NAME")+")");
            j++;
        }
        int cant = foreignKeys.size();

        colCount = metadata.getColumnCount();
        for (int i=1;i<=colCount ; i++){
            String columnName = metadata.getColumnName(i);
            columnas.add(columnName);
            String tipo = metadata.getColumnTypeName(i);
            tipos.add(tipo);
        }
        rsFk.close();
        rs.close();
        System.out.println("Saliendo de: cargarMetadatos()");
    }

    /** BISion - 06/07/2009 - Santiago Ibañez
     * Método que retorna el nombre semantico de una tabla dada. Si la tabla no tiene
     * traduccion entonces retorna el campo Name de la tabla dada.
     * @param tableName
     * @return
     * @throws java.sql.SQLException
     */
    public String getNombreTabla(String tableName) throws SQLException{
        System.out.println("En: getNombreTabla()");
        /*String sql = "select wt.name, wt2.name from ad_table t"
                    +"left join ad_window w on w.ad_window_id = t.ad_window_id"
                    +"left join ad_window w2 on w2.ad_window_id = t.po_window_id"
                    +"left join ad_window_trl wt on wt.ad_window_id = w.ad_window_id"
                    +"left join ad_window_trl wt2 on wt2.ad_window_id = w2.ad_window_id"
                    +"left where upper(t.tablename) like ?";*/
        String sql = "select tt.name,t.name from ad_table t" +
                     " left join ad_table_trl tt on t.ad_table_id = tt.ad_table_id " +
                     " where upper(t.tablename) like ?";
        PreparedStatement ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ps.setString(1, tableName);
        ResultSet rs = ps.executeQuery();
        String nombre = null;
        if (rs.next()){
            nombre = rs.getString(1);
            //Si la tabla no tiene traduccion entonces utilizo el campo Name de la tabla
            if (nombre==null || nombre.equals(""))
                nombre = rs.getString(2);
        }
        rs.close();
        ps.close();
        rs = null;
        ps = null;
        System.out.println("Saliendo de: getNombreTabla()");
        return nombre;
    }

    /** BISIon - 06/07/2009 - Santiago Ibañez
     * Metodo que retorna si existe el registro referenciado por la clave extranjera
     * @param tabla
     * @param valor
     * @return
     * @throws java.sql.SQLException
     */
    public boolean existeClaveExtranjera(String tabla, String colPK, int valor) throws SQLException{
        if (colPK==null||colPK.equals("")){
        	return false;
        }
    	try{
            System.out.println("En: existeClaveExtranjera()"+tabla+" "+colPK +" "+ valor);
            boolean ret = false;
            String sql = "select * from "+tabla+" where "+colPK +" = " +valor;
            PreparedStatement ps = conexionInicial.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs=null;
                rs = ps.executeQuery();
            if (rs.next())
                ret = true;
            rs.close();
            ps.close();
            rs = null;
            ps = null;
            System.out.println("Saliendo de: existeClaveExtranjera()"+colPK +" "+ valor);
            return ret;
        }
        catch(Exception e){
            System.out.println("");
            return false;
        }
    }

    /** BISion - 06/07/2009 - Santiago Ibañez
     * Metodo que retorna las columnas que identifican una tabla dada. Se utiliza para
     * saber que seleccionar de una tabla para que la informacion sea entendida por el usuario
     * dado que desconoce del uso de los id propios de la tabla.
     * @param tableName tabla sobre la cual se quieren obtener las columnas que la identifican
     * @return
     * @throws java.sql.SQLException
     */
    private String getColumnasIdentificatorias(String tableName) throws SQLException{
        System.out.println("En: getColumnasIdentificatorias()");
        String sql = "select col.columnname from ad_column col" +
                     " join ad_table tab on tab.ad_table_id = col.ad_table_id" +
                     " where upper(tab.tablename) = ? and col.isidentifier = 'Y'";
        PreparedStatement ps = conexionInicial.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ps.setString(1,tableName);
        ResultSet rs = ps.executeQuery();
        String ret="";
        while (rs.next()){
            ret+=rs.getString(1);
            ret+=" || ' - ' || ";
        }
        if (!ret.equals(""))
        	ret = ret.substring(0,ret.length()-13);
        rs.close();
        ps.close();
        rs = null;
        ps = null;
        System.out.println("Saliendo de: getColumnasIdentificatorias()");
        return ret;
    }

    /** BISion - 07/07/2009 - Santiago Ibañez
     * Metodo que devuelve el valor de aquellas columnas que identifican a la tabla.
     * Este metodo es usado para setear el mensaje de aviso para que sea comprendido por el usuario.
     * @param tableName
     * @param pk
     * @param valuePK
     * @return
     * @throws java.sql.SQLException
     */
    private String getValorIdentificatorio(String tableName, String pk, int valuePK) throws SQLException{
        if (pk==null||pk.equals(""))
        	return "";
    	System.out.println("En: getValorIdentificatorio()");
        String sql = "select "+getColumnasIdentificatorias(tableName)+" from "+tableName+" where "+pk+" = "+valuePK;
        PreparedStatement ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        String ret="";
        if (rs.next()){
            ret = rs.getString(1);
        }
        rs.close();
        ps.close();
        rs = null;
        ps = null;
        System.out.println("Saliendo de: getValorIdentificatorio()");
        return ret;
    }

    /** BISion - 07/07/2009 - Santiago Ibañez
     * Dada una tabla me retorna el nombre de la columna que es calve primaria
     * @param tableName
     * @return
     * @throws java.sql.SQLException
     */
    private String getPrimaryKey(String tableName) throws SQLException{
        System.out.println("En: getPrimaryKey()");
        String sql = "select columnname from ad_column col" +
                     " join ad_table t on t.ad_table_id = col.ad_table_id " +
                     " where upper(t.tablename) like ? and col.iskey = 'Y'";
        PreparedStatement ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ps.setString(1, tableName);
        ResultSet rs = ps.executeQuery();
        String ret="";
        while (rs.next()){
            ret = rs.getString(1);
            if (ret.endsWith("_ID"))
                break;
        }
        rs.close();
        ps.close();
        rs = null;
        ps = null;
        System.out.println("Saliendo de: getPrimaryKey()");
        return ret;
    }

    /** BISion - 10/07/2009 - Santiago Ibañez
     * Retorna una Consulta preparada con los datos a importar de acuerdo a la tabla seteada
     * @return
     * @throws java.sql.SQLException
     */
    private PreparedStatement getDatosAImportar() throws SQLException{
        System.out.println("En: getDatosAImportar()");
        String sql="SELECT * FROM "+tableName +" t";
        PreparedStatement ps=null;
        if (ID!=0){
            sql+= " where "+tableNamePK+" = "+ID;
            ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        }
        // Las ordenes de compra a considerar deben estar entre el rango de fechas especificado
        else if (tableName.equals("C_ORDER")){
            sql +=" WHERE issotrx = 'N' and dateordered between ? and ? ";
            ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ps.setTimestamp(1, from);
            ps.setTimestamp(2, to);
        }
        // Las recepciones de materiales de terceros deben estar entre el rango de fechas especificado
        else if (tableName.equals("M_INOUT")){
            sql +=" WHERE movementdate between ? and ? and (c_doctype_id = 1000142 or c_doctype_id = 1000143)";
            ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ps.setTimestamp(1, from);
            ps.setTimestamp(2, to);
        }
        // Las líneas de orden de compra deben pertenecer a las ordenes de compra ya importadas
        else if (tableName.equals("C_ORDERLINE")){
            sql +=" where t.c_order_id in (select o.c_order_id from c_order o WHERE o.issotrx = 'N' and o.dateordered between ? and ?)";
            ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ps.setTimestamp(1, from);
            ps.setTimestamp(2, to);
        }
        //Las lineas de las recepciones de 3ros deben pertenecer a las recepciones ya importadas
        else if (tableName.equals("M_INOUTLINE")){
            sql +=" where t.m_inout_id in (select m.m_inout_id from m_inout m where m.movementdate between ? and ? and (m.c_doctype_id = 1000142 or m.c_doctype_id = 1000143))";
            ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ps.setTimestamp(1, from);
            ps.setTimestamp(2, to);
        }
        //Puede ser una unica fila en particular
        /*if (ID!=0){
            sql+= " where "+tableNamePK+" = "+ID;
            ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        }
        else{
        	PreparedStatement ps2 = DB.prepareStatement("select condicion from AD_IMPORTACION WHERE tabla = '"+tableName+"'",null);
        	ResultSet rs = ps2.executeQuery();
        	if (rs.next())
        		sql += " "+rs.getString(1);
        	ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        }*/
        System.out.println("Saliendo de: getDatosAImportar()");
        return ps;
    }

    public void setTableName(String t){
        tableName = t;
    }

    public String getMensaje(){
        return mensajeError;
    }

    public void setMensaje(String m){
        mensajeError = m;
    }
}
