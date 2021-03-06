/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import org.compiere.db.CConnection;
import org.compiere.model.MNote;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/** BISion - 13/08/2009 - Santiago Iba�ez
 * Esta clase tiene como objetivo implementar el proceso que importa desde una
 * BD origen seteada en la tabla AD_CONNECTION y actualiza la BD actual.
 * @author santiago
 */
public class ImportarPartidas extends SvrProcess{
    
     // TIPOS SQL
    private static String NUMBER_TYPE = "NUMBER";
    private static String DATE_TYPE = "DATE";

    private BigDecimal M_Locator_ID;

    private ArrayList<String> columnas;
    private ArrayList<String> tipos;
    private int colCount;

    private String tableName = "M_STORAGE";
    //Nombre de la clave primaria de la tabla tableName
    private String tableNamePK;

    //Este
    private String mensajeError;

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
            if (name.equals("M_Locator_ID"))
            	M_Locator_ID = (BigDecimal) para[i].getParameter();
            else {
                //log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
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
        //Si la tabla en cuestion es M_Storage no uso existeClave extranjera sino existeStorage
        boolean exist;
        if (tableName.toUpperCase().equals("M_STORAGE"))
            exist = existeStorage(rs.getInt("M_Locator_ID"),rs.getInt("M_AttributeSetInstance_ID"), rs.getInt("M_Product_ID"));
        else
            exist = existeClaveExtranjera(tableName, tableNamePK, valorPK);
        if (!exist){
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
                            process.ID = rs.getInt(i);
                            process.setTableName(tabla);
                            process.startProcess(getCtx(), this.getProcessInfo(), null);
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
        }
        PreparedStatement ps = getDatosAImportar();
        ResultSet rs = ps.executeQuery();
        String sql = getInsertSatement();
        //Mientras haya almacenamientos...
        while (rs.next()){
            if (ID==0)
                System.out.println("Comenzando a crear Storage: ");
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

        while (rsFk.next()){
            foreignKeys.put(rsFk.getString("FK_NAME"), true);
            foreignKeysTabla.put(rsFk.getString("FK_NAME"),rsFk.getString("PKTABLE_NAME"));
            foreignKeysColumnaRef.put(rsFk.getString("FK_NAME"), rsFk.getString("PKCOLUMN_NAME"));
            foreignKeysColumna.put(rsFk.getString("FKCOLUMN_NAME"),rsFk.getString("FK_NAME"));
            columnaForeignKey.put(rsFk.getString("FK_NAME"),rsFk.getString("FKCOLUMN_NAME"));
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
        if (valor == 1012415)
            System.out.println("ACA!!");
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
    /**
     * Metodo para comprobar si existe un Storage determinado. No se puede utilizar
     * existeClaveExtranjera porque la clave de la tabla M_Storage está formada por 3 columnas
     * @param loc M_Locator_ID
     * @param part M_AttributeSetInstance_ID
     * @param prod M_Product_ID
     * @return
     */
    private boolean existeStorage(int loc, int part, int prod){
        boolean ret = false;
        try{
            System.out.println("En: existeStorage()");
            String sql = "select * from M_Storage where M_Locator_ID = "+loc+" and M_AttributeSetInstance_ID = "+part+" and M_product_ID = "+prod;
            PreparedStatement ps = conexionInicial.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs=null;
                rs = ps.executeQuery();
            if (rs.next())
                ret = true;
            rs.close();
            ps.close();
            rs = null;
            ps = null;
            System.out.println("Saliendo de: existeClaveExtranjera()");
            return ret;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
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
        if (tableName.equals("M_STORAGE")){
        	sql +=" where M_Locator_ID = ? and qtyonhand <> 0";
            ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ps.setBigDecimal(1, M_Locator_ID);
        }
        //Puede ser una unica fila en particular
        else if (ID!=0){
            sql+= " where "+tableNamePK+" = "+ID;
            ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        }
        else{
        	PreparedStatement ps2 = DB.prepareStatement("select condicion from AD_IMPORTACION WHERE tabla = '"+tableName+"'",null);
        	ResultSet rs = ps2.executeQuery();
        	if (rs.next())
        		sql += " "+rs.getString(1);
        	ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        }
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