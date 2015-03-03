/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import java.awt.Window;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MLanguage;
import org.compiere.model.MLocator;
import org.compiere.model.MQuery;
import org.compiere.model.MSequence;
import org.compiere.model.MTab;
import org.compiere.model.MTabVO;
import org.compiere.model.MTransaction;
import org.compiere.model.MWarehouse;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Trx;
import org.eevolution.model.MMPCOrder;

/** BISion - 06/04/2009 - Santiago Ibañez
 * Clase que crea un remito automaticamente desde un Movimiento de materiales
 * ingresado como parametro.
 * @author santiago
 */
public class GenerateRemitosFromManufactura extends SvrProcess {

    //Orden de manufactura desde la cual se crea el Remito
    private int MPC_Order_ID;
    //Transaccion global
    private String trxName;
    //Deposito del Remito
    private int M_Warehouse_ID;
    //Socio del Negocio del Remito
    private int C_BPartner_ID;
    //Fecha de la cabecera del Remito
    private Timestamp fecha;
    private boolean existenTransacciones = false;

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
            if (name.equals("MPC_Order_ID")) {
                MPC_Order_ID = ((BigDecimal) para[i].getParameter()).intValue();
            }
            else if (name.equals("C_BPartner_ID"))
                C_BPartner_ID = ((BigDecimal) para[i].getParameter()).intValue();
            else if (name.equals("M_Warehouse_ID"))
                M_Warehouse_ID = ((BigDecimal) para[i].getParameter()).intValue();
            else if (name.equals("MovementDate"))
                fecha = ((Timestamp) para[i].getParameter());
            else {
                log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
            }
        }
    }

    protected String doIt() {
        trxName = get_TrxName();
        MMPCOrder order = new MMPCOrder(getCtx(), MPC_Order_ID, trxName);
        if (!order.getDocStatus().equals(MMPCOrder.DOCSTATUS_Completed))
            JOptionPane.showMessageDialog(null,"No se genero ningun remito porque la orden de manufactura debe estar completa", "Orden no completa", JOptionPane.ERROR_MESSAGE);
        else{
            //cantidad de lineas de la orden de manufactura
            int cantidadLineas = getTransacciones().length;
            //Creo un remito cada filasMaximasRemito
            filaMovimiento = 0;
            int CantidadRemitos = cantidadLineas % filasMaximasRemito == 0 ? cantidadLineas/filasMaximasRemito : (cantidadLineas/filasMaximasRemito)+1;
            System.out.println("Se va(n) a crear "+CantidadRemitos+" Remito(s).");
            remitos = new ArrayList<Integer>();
            for (int i=1;i<=CantidadRemitos;i++)
                crearRemito(order.getDocumentNo(),i);


            if (existenTransacciones){
                //Comiteo los cambios para que la ventana pueda mostrar ya los datos
                Trx.get(trxName, false).commit();
                abrirVentanaRemitos(getRemitoWindow());
            }
        }
        return "OK";
    }

    /** BISion - 13/05/2009 - Santiago Ibañez
     * Metodo que crea la cabecera del Remito
     * @param date
     * @param OMno
     */
    private void crearRemito(String OMno, int i){
        //Creo un remito
        System.out.println("Creando Remito...");
        //Obtengo el ID del tipo de documento cuyo nombre sea Remito
        int C_DocType_ID = getDocTypeByName("Remito Famatina");
        MInOut remito = new MInOut(getCtx(), 0, trxName);
        //Obtengo el proximo numero de documento a asignar para el remito
        String docNo = getProximoID(C_DocType_ID);
        remito.setDocumentNo(docNo);
        //remito.setDescription("Creado desde la Orden de Manufactura nº: "+OMno);
        remito.setM_Warehouse_ID(M_Warehouse_ID);
        remito.setMovementType(MInOut.MOVEMENTTYPE_CustomerShipment);
        remito.setBPartner(new MBPartner(getCtx(), C_BPartner_ID, trxName));
        remito.setMovementDate(fecha);
        remito.setMPC_Order_ID(MPC_Order_ID);
        if (C_DocType_ID==0){
            System.out.println("No se encontro el DocType correspondiente a Remitos");
            throw new RuntimeException("fallo");
        }
        remito.setC_DocType_ID(C_DocType_ID);
        remito.save(trxName);
        crearLineas(remito.getM_InOut_ID(),i);
        remitos.add(remito.getM_InOut_ID());

        /**
         * Comiteo los cambios para que la ventana tenga datos para mostrar si
         * se encontraron transacciones, sino hago rollback
         */
        /*if (existenTransacciones){
            Trx.get(trxName, false).commit();
            //Muestro la Ventana de Remitos
            JOptionPane.showMessageDialog(null, "Se ha creado el remito "+remito.getDocumentNo(), "Remito creado", JOptionPane.INFORMATION_MESSAGE);
            AWindow frame = new AWindow();
            MQuery query = new MQuery("M_InOut");
            query = query.getEqualQuery("M_InOut_ID",remito.getM_InOut_ID());
            int AD_Window_ID = getRemitoWindow();
            if (AD_Window_ID!=0){
                frame.initWindow(AD_Window_ID, query);
                AEnv.showCenterScreen(frame);
            }
            else{
                JOptionPane.showMessageDialog(null, "No se puede abrir la ventana de Remitos", "Error Ventana", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            Trx.get(trxName, false).rollback();
            JOptionPane.showMessageDialog(null, "No se creo el Remito dado que no existen surtimientos para la Orden nº: "+OMno, "Remito no creado", JOptionPane.INFORMATION_MESSAGE);
        }*/
        
    }

    /** BISion - 13/05/2009 - Santiago Ibañez
     * Método que crea cada una de las lineas del Remito
     * @param M_InOut_ID Remito al que van a pertencer las líneas
     */
    private void crearLineas(int M_InOut_ID, int cantRemitos){        
        //Obtengo todos los surtimientos para la OM
        MTransaction trxs[] = getTransacciones();
        //Para cada una de las transacciones obtenidas creo una linea de Remito
        for (int i=filaMovimiento;i<cantRemitos*filasMaximasRemito&&i<trxs.length;i++){
            //Como existe al menos una transaccion indico que ya puedo crear el remito
            existenTransacciones = true;
            //Creo la linea del Remito
            MInOutLine line = new MInOutLine(getCtx(), 0, trxName);
            //Producto
            line.setM_Product_ID(trxs[i].getM_Product_ID(), true);
            //Partida
            line.setM_AttributeSetInstance_ID(trxs[i].getM_AttributeSetInstance_ID());
            //Cantidad, la niego porque las entregas se registran como negativas
            line.setQty(trxs[i].getMovementQty().negate());
            //La Ubicacion es la ubicacion defecto del deposito dado
            int M_Locator_ID = MWarehouse.get(getCtx(), M_Warehouse_ID).getDefaultLocator().getM_Locator_ID();
            line.setM_Locator_ID(M_Locator_ID);
            //Remito padre
            line.setM_InOut_ID(M_InOut_ID);
            //guardo los cambios
            line.save(trxName);
            System.out.println("Linea Creada de Remito: "+line.getLine()+" Cantidad: "+line.getQtyEntered());
            filaMovimiento++;
        }
    }

    /** BISion - 13/05/2009 - Santiago Ibañez
     * Método que retorna todas las transacciones de surtimientos para una OM
     * dada cuya cantidad de movimiento sea menor a cero para no tomar en cuenta las
     * devoluciones.
     * @return
     */
    private MTransaction[] getTransacciones(){
        ArrayList<MTransaction> transacciones = new ArrayList<MTransaction>();
        MTransaction trxs[] = new MTransaction[transacciones.size()];
        PreparedStatement ps = DB.prepareStatement("SELECT M_TRANSACTION_ID FROM M_TRANSACTION WHERE MPC_ORDER_ID = ? AND MOVEMENTTYPE = ? AND MOVEMENTQTY < 0", trxName);
        try{
            ps.setInt(1, MPC_Order_ID);
            //ps.setInt(2, MPC_Order_BOMLine_ID);
            ps.setString(2, MTransaction.MOVEMENTTYPE_Production_);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                MTransaction trx = new MTransaction(getCtx(), rs.getInt(1), trxName);
                transacciones.add(trx);
            }
            rs.close();
            rs = null;
            ps.close();
            ps = null;
        }
        catch(Exception ex){
            System.out.println("No se pudieron obtener las transacciones para la OM.");
            System.out.println(ex.getMessage());
        }
        return transacciones.toArray(trxs);
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
     /** BISion - 10/06/2009 - Santiago Ibañez
     * Metodo que abre en una unica ventana de Remitos, todos los creados por este proceso
     */
    public void abrirVentanaRemitos(int AD_Window_ID){
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
        frame.initWindow(AD_Window_ID, query);
        AEnv.showCenterScreen(frame);
    }
}
