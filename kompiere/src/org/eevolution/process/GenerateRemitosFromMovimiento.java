/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MLocator;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MQuery;
import org.compiere.model.MSequence;
import org.compiere.model.MWarehouse;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Trx;

/** BISion - 06/04/2009 - Santiago Ibañez
 * Clase que crea un remito automaticamente desde un Movimiento de materiales
 * ingresado como parametro.
 * @author santiago
 */
public class GenerateRemitosFromMovimiento extends SvrProcess {

    private int M_Movement_ID;
    private String trxName;
    private int M_Warehouse_ID;
    private int C_BPartner_ID;
    private Timestamp fecha;
    private BigDecimal valorizacion;
    //Determina la cantidad máxima de filas que puede tener un remito
    //Si se excede este valor se debe generar un remito nuevo
    private static int filasMaximasRemito = 8;

    //variable sobre la cual se itera en las lineas del movimiento
    private int filaMovimiento=0;

    //este arreglo mantiene los i de los remitos que se van creando para utilizarlo en el filtro que muestra la ventana
    private ArrayList<Integer> remitos;

    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (name.equals("C_BPartner_ID"))
                C_BPartner_ID = ((BigDecimal) para[i].getParameter()).intValue();
            else if (name.equals("MovementDate"))
                fecha = ((Timestamp) para[i].getParameter());
            else {
                log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
            }
        }
    }

    protected String doIt() {
        trxName = get_TrxName();
        MMovement mov = new MMovement(getCtx(), M_Movement_ID, trxName);

        //si no se seteo ninguna fecha se toma la fecha del movimiento
        fecha = fecha==null ? mov.getMovementDate(): fecha;

        //BISion - 02/06/2009 - Santiago Ibañez
        //Se comento porque ahora no se toma en cuenta el almacen de las lineas
        //Antes de crear el remito, chequeo que haya alguna línea cuya ubicacion destino
        //este incluida en el almacen ingresado como parametro
        //if (!existeLineaEnAlmacen())
        //    JOptionPane.showMessageDialog(null,"No se ha creado el Remito.\nEl movimiento "+mov.getDocumentNo()+" no tiene lineas en el almacen "+almacen.getName(), "No se creo el Remito", JOptionPane.INFORMATION_MESSAGE);
        //else

        //cantidad de lineas del movimiento
        int cantidadLineas = mov.getLines(true).length;
        //Creo un remito cada filasMaximasRemito
        filaMovimiento = 0;
        int CantidadRemitos = cantidadLineas % filasMaximasRemito == 0 ? cantidadLineas/filasMaximasRemito : (cantidadLineas/filasMaximasRemito)+1;
        System.out.println("Se va(n) a crear "+CantidadRemitos+" Remito(s).");
        remitos = new ArrayList<Integer>();
        for (int i=1;i<=CantidadRemitos;i++)
            crearRemito(mov.getDocumentNo(),i);

        //Comiteo los cambios para que la ventana pueda mostrar ya los datos
        Trx.get(trxName, false).commit();
        
        abrirVentanaRemitos();
        return "ok";
    }

    /** BISion - 03/06/2009 - Santiago Ibañez
     * Metodo que abre en una unica ventana de Remitos, todos los creados por este proceso
     */
    public void abrirVentanaRemitos(){
        String sqlWhere = new String();
        //Armo el query con todos los remitos creados
        Integer rem[] = new Integer[remitos.size()];
        remitos.toArray(rem);
        sqlWhere+= "M_InOut_ID = "+rem[0];
        for (int i=1;i<rem.length;i++){
            sqlWhere+= " OR M_InOut_ID = "+rem[i];
        }

        AWindow frame = new AWindow();
        MQuery query = new MQuery("M_InOut");
        //query = query.getEqualQuery("M_InOut_ID",remito.getM_InOut_ID());
        query.addRestriction(sqlWhere);
        frame.initWindow(getRemitoWindow(), query);
        AEnv.showCenterScreen(frame);
    }

    private void crearRemito(String MovNo,int cantRemitos){
        //Creo un remito
        System.out.println("Creando Remito...");
        //Obtengo el ID del tipo de documento cuyo nombre sea Remito
        int C_DocType_ID;// = getDocTypeByName("Remito%");
        MInOut remito = new MInOut(getCtx(), 0, trxName);

        //Obtengo el deposito de la primer linea del movimiento
        M_Warehouse_ID = getAlmacenPrimeraLinea();
        
        /**BISion - 23/06/2010 - Santiago Ibañez
         * Modificacion realizada para incorporar los nuevos tipos de Remitos
         */
        MLocator loc = new MLocator(getCtx(), getUbicacionPrimeraLinea(), get_TrxName());
        if (loc.getValue().equals("Expo. Andreani Loma Hermosa U.") || 
        		loc.getValue().equals("Andreani Loma Hermosa U. ")) 
            C_DocType_ID = getDocTypeByName("Remito Loma Hermosa");
        else if (loc.getValue().equals("Andreani MP Aprobado") ||
        		loc.getValue().equals("Andreani MP Cuarentena") ||
        		loc.getValue().equals("Andreani MP Rechazado"))
            C_DocType_ID = getDocTypeByName("Remito Florida");
        //Para cualquier otro deposito tomo como defecto Remito Famatina
        else
            C_DocType_ID = getDocTypeByName("Remito Famatina");
        // fin modificacion BISion
        String docNo = getProximoID(C_DocType_ID);
        remito.setDocumentNo(docNo);
        //remito.setDescription("Creado desde el Movimiento de Materiales: "+MovNo);
        remito.setM_Warehouse_ID(M_Warehouse_ID);
        remito.setMovementType(MInOut.MOVEMENTTYPE_CustomerShipment);
        remito.setBPartner(new MBPartner(getCtx(), C_BPartner_ID, trxName));
        remito.setMovementDate(fecha);
        remito.setM_Movement_ID(M_Movement_ID);
        if (C_DocType_ID==0){
            System.out.println("No se encontro el DocType correspondiente a Remitos");
            throw new RuntimeException("fallo");
        }
        remito.setC_DocType_ID(C_DocType_ID);
        remito.setVALORDECLARADO(valorizacion);
        /*
         * 09-06-2011 Camarzana Mariano
         * Se setea el remito con IsSOTrx = true debido a que estos tipos de documento
         * se visualizan desde la ventana Remito (Cliente) 
         */
        remito.setIsSOTrx(true);
        remito.save(trxName);
        crearLineas(remito.getM_InOut_ID(),cantRemitos);
        
        remitos.add(remito.getM_InOut_ID());
        //JOptionPane.showMessageDialog(null, "Se ha creado el remito "+remito.getDocumentNo(), "Remito creado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void crearLineas(int M_InOut_ID,int cantRemitos){
        MMovement mov = new MMovement(getCtx(),M_Movement_ID, trxName);
        //Para cada linea del movimiento genero una linea en el recibo
        MMovementLine[] lineas = mov.getLines(true);
        for (int i=filaMovimiento;i<cantRemitos*filasMaximasRemito&&i<lineas.length;i++){
            //Considero esta linea si su ubicacion destino esta incluida en el almacen como parametro
            //BISion - 02/06/2009 - Santiago Ibañez
            //se comento el if para no considerar almacenes
            //if (!perteneceAlmacen(lineas[i].getM_LocatorTo_ID()))
                //continue;
            MInOutLine line = new MInOutLine(getCtx(), 0, trxName);
            line.setM_Product_ID(lineas[i].getM_Locator_ID());
            //Producto
            line.setM_Product_ID(lineas[i].getM_Product_ID(), true);
            //Partida
            line.setM_AttributeSetInstance_ID(lineas[i].getM_AttributeSetInstance_ID());
            //Cantidad
            line.setQty(lineas[i].getMovementQty());
            //Ubicacion deposito
            
            //07-06-2011 Camarzana Mariano Cambiado por la ubicacion de origen
            //line.setM_Locator_ID(lineas[i].getM_LocatorTo_ID());
            
            line.setM_Locator_ID(lineas[i].getM_Locator_ID());
            //ID recibo
            line.setM_InOut_ID(M_InOut_ID);
            //guardo los cambios
            line.save(trxName);
            System.out.println("Linea Creada de Remito: "+line.getLine()+" Cantidad: "+line.getQtyEntered());
            filaMovimiento++;
        }
    }
    
    /** BISion - 11/05/2009 - Santiago Ibañez
     * Metodo que dado el C_DocType_ID obtiene el proximo numero de secuencia a asignar
     * con el formato XXXX-XXXXXXXX
     * @param C_DocType_ID
     * @return
     */
    private String getProximoID(int C_DocType_ID){
        //Obtengo el DocType del Remito
        MDocType docType = new MDocType(getCtx(), C_DocType_ID, trxName);
        //Obtengo la secuencia asociada al DocType anterior
        MSequence sec = new MSequence(getCtx(), docType.getDocNoSequence_ID(),null);
        //Prefijo por ahora le pongo 0000
        String prefijo = "0000";
        String sufijo ="00000000";
        String sufix;
        String secuenciaCompleta = (MSequence.getDocumentNo(C_DocType_ID, trxName)).toString();
        try{
            prefijo = secuenciaCompleta;
            prefijo = prefijo.substring(0,4);
            prefijo += "-";

            sufix = secuenciaCompleta;
            //Le saco los primeros 4 caracteres del prefijo
            sufix = sufix.substring(4, sufix.length());
            sufijo += sufix;
            //Tomo los ultimos 8 caracteres
            sufijo = sufijo.substring(sufijo.length()-8,sufijo.length());
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Controle la Secuencia de los Remitos", "Secuencia erronea", JOptionPane.INFORMATION_MESSAGE);
        }

        //sufijo += sufix.append(MSequence.getDocumentNo(C_DocType_ID, trxName)).toString();
        return prefijo+sufijo;

    }

    private boolean perteneceAlmacen(int M_Locator_ID){
        MLocator loc = new MLocator(getCtx(), M_Locator_ID, trxName);
        return loc.getM_Warehouse_ID() == M_Warehouse_ID;
    }

    /**
     * BISion - 14/04/2009 - Santiago Ibañez
     * Metodo creado para retornar el DocType correspondiente al nombre dado
     * @param name
     * @return
     */
    private int getDocTypeByName(String name){
        int id=0;
        String sql = "SELECT C_DocType_ID FROM C_DocType WHERE name like '"+name+"'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                id = rs.getInt(1);
        }
        catch(SQLException ex){
            System.out.println("No se pudo obtener DocType Asociado");
        }
        return id;
    }

    /** BISion - 11/05/2009 - Santiago Ibañez
     * Metodo que comprueba si existe al menos una línea del movimiento dado
     * cuya ubicacion destino esté incluida en el almacen dado como parametro
     * @return
     */
    private boolean existeLineaEnAlmacen(){
        boolean ret = false;
        ///Obtengo el Movimiento ingresado como parametro
        MMovement mov = new MMovement(getCtx(),M_Movement_ID, trxName);
        //Para cada linea del movimiento genero una linea en el recibo
        MMovementLine[] lineas = mov.getLines(true);
        for (int i=0;i<lineas.length&&!ret;i++){
            if (perteneceAlmacen(lineas[i].getM_LocatorTo_ID()))
                ret = true;
        }
        return ret;
    }

    /**
     * BISion - 02/06/2009 - Santiago Ibañez
     * Método que retorna el deposito destino de la primer linea del movimiento
     * @return
     */
    private int getAlmacenPrimeraLinea(){
        //Obtengo el Movimiento ingresado como parametro
        MMovement mov = new MMovement(getCtx(),M_Movement_ID, trxName);
        //Para cada linea del movimiento genero una linea en el recibo
        MMovementLine[] lineas = mov.getLines(true);
        //retorno el almacen de la primer línea
        MLocator loc = new MLocator(getCtx(), lineas[0].getM_Locator_ID(), trxName);
        return loc.getM_Warehouse_ID();
    }

    /** BISion - 23/06/2010 - Santiago Ibañez
     * Método que retorna la ubicacion en la primera linea para
     * saber que tipo de remito crear
     * @return
     */
    private int getUbicacionPrimeraLinea(){
        //Obtengo el Movimiento ingresado como parametro
        MMovement mov = new MMovement(getCtx(),M_Movement_ID, trxName);
        //Para cada linea del movimiento genero una linea en el recibo
        MMovementLine[] lineas = mov.getLines(true);
        return lineas[0].getM_Locator_ID();
    }

    /** BISion - 02/06/2009 - Santiago Ibañez
     * Metodo que setea fuera del proceso el numero de Movimiento
     * @param M_Movement_ID
     * @param valorizacion
     */
    public void setParamFromOut(int M_Movement_ID, BigDecimal valorizacion){
        this.M_Movement_ID = M_Movement_ID;
        this.valorizacion = valorizacion;
    }
    /**
     * Metodo que retorna el id de la ventana de Remitos
     * @return
     */
    private int getRemitoWindow(){
        //Obtengo del tab traducido cuyo nombre sea Remito
        String sql = "select tab.ad_window_id " +
                " from ad_tab_trl tt" +
                " join ad_tab tab on (tt.ad_tab_id = tab.ad_tab_id)" +
                " where tt.name like 'Remito' and tt.ad_language = 'es_MX'";
        PreparedStatement ps = DB.prepareStatement(sql, trxName);
        try{
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);
            rs.close();
            rs = null;
            ps.close();
            ps = null;
        }
        catch(Exception ex){
            System.out.println("No se pudo obtener la ventana de Remito: "+ex.getMessage());
        }
        return 0;
    }
}
