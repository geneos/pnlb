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
import org.compiere.model.MLocator;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MTransaction;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.eevolution.tools.UtilProcess;

/**
 *
 * @author santiago
 */
public class ClosePurchaseOrders extends SvrProcess{

    /**
     * Porcentaje que tiene que haber recibido de la cantidad ordenada inicial
     */
    public BigDecimal porcentaje = new BigDecimal(95);

    /**
     * Porcentaje, de lo recibido, que tiene que haber en el deposito M_Locator_ID
     */
    public BigDecimal porcentajeAlmacen = new BigDecimal(95);


    /**
     * Ubicacion en la que deben estar las partidas recibidas para poder cerrar.
     */
    public int M_locator_ID = 1000258;

    public BigDecimal getAlmacenado(int M_Product_ID, int M_Locator_ID, int M_AttributeSetInstance_ID){
        BigDecimal qty = BigDecimal.ZERO;
        String sql = "select sum(qtyonhand) from m_storage st" +
                     " join m_locator loc on loc.m_locator_id = st.m_locator_id " +
                     " join m_warehouse wh on wh.m_warehouse_id = loc.m_warehouse_id " +
                     " where m_product_id = "+M_Product_ID+
                     " and wh.value = 'AprobadoFamatina'"+
                     " and M_AttributeSetInstance_ID = "+M_AttributeSetInstance_ID;
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        ResultSet rs;
        try{
            rs = ps.executeQuery();
            if (rs.next())
                qty = rs.getBigDecimal(1);
            rs.close();
            ps.close();
        }
        catch(Exception e){

        }
        if (qty==null)
            qty = BigDecimal.ZERO;
        return qty;
    }


    private Integer[] getOrdenes(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        String sql = "select c_order_id from c_order where docstatus = 'CO' and issotrx = 'N'";
        //String sql = "select c_order_id from c_order where documentno = '17191'";
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());

        ResultSet rs;
        try{
            rs = ps.executeQuery();
            while (rs.next()){
                list.add(rs.getInt(1));
            }
            rs.close();
            ps.close();
        }
        catch(Exception e){

        }
        Integer[] ordenes = new Integer[list.size()];
        list.toArray(ordenes);
        return ordenes;
    }


    private boolean superaPorcentajeRecibido(BigDecimal qtyOrdered, BigDecimal qtyDelivered){
        boolean supera = false;
        //Obtengo el porcentaje de la cantidad ordenada
        qtyOrdered = porcentaje.multiply(qtyOrdered).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
        return (qtyDelivered.compareTo(qtyOrdered)>=0);
    }

    /**
     * Metodo que comprueba que exista el porcentaje requerido de la cantidad dada (qty)
     * en la ubicacion por defecto de la clase (Aprobado Famatina U.)
     * @param qty
     * @param M_AttributeSetInstance_ID
     * @param M_Product_ID
     * @return
     */
    private boolean superaPorcentajeAlmacen(BigDecimal qty, int M_Product_ID, int C_OrderLine_ID) throws SQLException{
        BigDecimal limiteInferior = porcentajeAlmacen.multiply(qty).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
        if (getCantidadAprobadoPartidasLineaOC(C_OrderLine_ID).compareTo(limiteInferior)>=0)
            return true;
        else
            return false;
    }

    @Override
    protected void prepare() {
        
    }

    @Override
    protected String doIt() throws Exception {
       System.out.println("Nueva clase: 20/05/2010");
        /**
         * Proceso que cierra todas las ordenes de compra completas que recibieron en todas sus lineas
         * mas del 95% de la cantidad ingresada y que adem√°s esa cantidad recibida est√° en mas de un 95%
         * en Aprobado. 
         */
        Integer ordenes[] = getOrdenes();
        boolean supera;
        vaciarTabla();
        for (int i=0;i<ordenes.length;i++){
            supera = true;
            //Obtengo la orden de compra con posibilidades de cerrarse
            MOrder order = new MOrder(getCtx(), ordenes[i].intValue(), get_TrxName());
            System.out.println("Orden de compra n∫: "+order.getDocumentNo());
            if (order.getDocumentNo().equals("800734"))
                System.out.println("Orden de compra n∫: "+order.getDocumentNo());
            //Obtengo las lineas
            MOrderLine[] orderLines = order.getLines();
            //Para cada una de las lineas chequeo que TODAS superen el porcentaje
            String mje = "";
            for (int j=0;j<orderLines.length&&supera;j++){
                //Si supera porcentaje y ademas est√° en Aprobado
                if (!superaPorcentajeRecibido(orderLines[j].getQtyEntered(), orderLines[j].getQtyDelivered())){
                    //order.setDescription("NO Deberia Cerrarse, no se entrego mas del "+porcentaje+"%");
                    mje = "No recibio mas del "+porcentaje+"% de la Cantidad Ordenada inicial";
                    System.out.println(mje);
                    //order.save();
                    supera = false;
                }
                else if (!superaPorcentajeAlmacen(orderLines[j].getQtyDelivered(), orderLines[j].getM_Product_ID(), orderLines[j].getC_OrderLine_ID())){
                    
                    /** BISion - 20/05/2010 - Santiago Iba√±ez
                     * Modificacion realizada porque si no hay cantidad suuficiente en Aprobado hay que ver si no se
                     * utilizo para produccion (Ej surtir una orden) o se entrego a clientes.
                     */
                    BigDecimal qtyAprobado = getCantidadAprobadoPartidasLineaOC(orderLines[j].getC_OrderLine_ID());
                    BigDecimal qtyEntregadaClientes = getCantidadTipoTransaccion(orderLines[j].getM_AttributeSetInstance_ID(), MTransaction.MOVEMENTTYPE_CustomerShipment, M_locator_ID);
                    BigDecimal qtyEntregadaProduccion = getCantidadTipoTransaccion(orderLines[j].getM_AttributeSetInstance_ID(), MTransaction.MOVEMENTTYPE_Production_,M_locator_ID);
                    //Si la cantidad en aprobado + cantidad en produccion + cantidad entregada a clientes
                    BigDecimal limiteInferior = porcentajeAlmacen.multiply(orderLines[j].getQtyDelivered()).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
                    if (!(qtyAprobado.add(qtyEntregadaClientes).add(qtyEntregadaProduccion).compareTo(limiteInferior)>=0)){
                    // fin modificacion BISion
                        MLocator loc = new MLocator(getCtx(), M_locator_ID, get_TrxName());
                        mje = "No hay en "+loc.getValue()+" mas del "+porcentajeAlmacen+"% de la partida recibida.";
                        System.out.println(mje);
                        //order.save();
                        supera = false;
                    }
                }
            }
            if (supera){
                order.setDescription("Cerrada automaticamente");
                order.save();
                System.out.println("Cerrando Orden: "+order.getDocumentNo()+"...");
                order.closeIt();
            }
            //Si no superaba alguna condicion entonces no se cierra la orden
            else{
                insertarFila(order.getDocumentNo(), order.getDateOrdered(), mje);
            }
        }
        UtilProcess.initViewer("Ordenes No Cerradas Automaticamente",getAD_PInstance_ID(),getProcessInfo());
        return "ok";
    }

    private void vaciarTabla(){
        try {
            String sql = "delete from T_ORDENES_NOCERRADAS";
            PreparedStatement ps = DB.prepareStatement(sql, null);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("No se pudo vaciar T_ORDENES_NOCERRADAS: "+ex.getMessage());
        }
    }

    private void insertarFila(String DocumentNo, Timestamp DateOrdered, String motivo){
        try {
            String sql = "insert into T_ORDENES_NOCERRADAS values (?,?,?,?,?,?,?)";
            PreparedStatement ps = DB.prepareStatement(sql, null);
            ps.setInt(1, getAD_Client_ID());
            ps.setInt(2, Env.getAD_Org_ID(getCtx()));
            ps.setString(3, "Y");
            ps.setInt(4, getAD_PInstance_ID());
            ps.setString(5, DocumentNo);
            ps.setTimestamp(6, DateOrdered);
            ps.setString(7, motivo);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("No se pudo ingresar en T_ORDENES_NOCERRADAS: "+ex.getMessage());
        }
    }

    /** BISion - 17/03/2010 - Santiago IbaÒez
     * Metodo que retorna la cantidad existente en Aprobado de todas las partidas
     * que se utilizaron para recibir/devolver en la linea de la OC dada.
     * @param C_OrderLine_ID
     * @return
     * @throws java.sql.SQLException
     */
    private BigDecimal getCantidadAprobadoPartidasLineaOC(int C_OrderLine_ID) throws SQLException{
        BigDecimal qty = BigDecimal.ZERO;
    String sql = "select sum(qtyonhand) from m_storage where m_attributesetinstance_id in " +
                "(select il.m_attributesetinstance_id " +
                "from m_inoutline il " +
                "join m_inout i on i.m_inout_id = il.m_inout_id " +
                "where il.c_orderline_id = ? and i.docstatus = 'CO') " +
                "and m_locator_id = "+M_locator_ID+" and qtyonhand > 0 ";

        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        ps.setInt(1, C_OrderLine_ID);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            qty = rs.getBigDecimal(1);
        rs.close();
        ps.close();
        if (qty==null)
            qty = BigDecimal.ZERO;
        return qty;
    }

    /**
     * Metodo que retorna la cantidad que se entrego a clientes. Busca las trx
     * asociadas a la partida dada cuyo tipo de movimiento sea C-
     * @param M_AtrtributeSetInstance_ID
     * @return
     */
    public BigDecimal getCantidadTipoTransaccion(int M_AtrtributeSetInstance_ID, String movementType, int M_Locator_ID){
        String sql = "select nvl(sum(movementqty),0) from m_transaction where m_attributesetinstance_id = ? and movementtype = ?";
        BigDecimal qty = null;
        try {
            PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
            ps.setInt(1, M_AtrtributeSetInstance_ID);
            ps.setString(2, movementType);
            ResultSet rs = ps.executeQuery();
            qty = BigDecimal.ZERO;
            if (rs.next()) {
                qty = qty.add(rs.getBigDecimal(1));
            }
            rs.close();
            ps.close();
        } catch (SQLException sQLException) {
            System.out.println("No se pudo obtener la cantidad entregada a clientes: "+sQLException);
        }
        return qty.negate();
    }

}
