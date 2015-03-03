/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/
package org.eevolution.process;

import java.math.*;
import java.sql.*;
import java.util.Properties;
import org.compiere.model.CalloutEngine;
import org.compiere.model.MField;
import org.compiere.model.MOrderLine;
import org.compiere.model.MStorage;
import org.compiere.model.MTab;
import org.compiere.process.DocAction;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.eevolution.model.MMPCOrder;

/**
 *  TestVit4b
 *
 *	@author Vit4B
 *	@fecha 02/07/2007 Version para actualizar la base de datos con los reservados.
 *
 */
public class UpdateOrdenadosVit4b extends CalloutEngine {

    int indneg = 0;

    public void executeUpdateValues(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) throws Exception {




        /** BISion - 24/11/2008 - Santiago Ibañez
         * Agregado soporte transaccional
         */
        Trx trx = Trx.get(Trx.createTrxName("ActOrdIns"), true);

        // Actualizacion de ordenados de insumos
        System.out.println("ACTUALIZACION DE ORDENADOS v2.0");

        try {

            setZeroOrdered(trx.getTrxName());
            setDepositoCorrecto(trx.getTrxName());
            trx.commit();
            //Selecciono los productos que tienen mal el almacenamiento
            
            /**
             * Modificado porque no consideraba todos los productos con el ordenado mal
             */
            String sqlGeneral ="select ol.m_product_id,sum(ol.qtyordered) as ord from c_orderline ol"+
                               " join c_order o on (o.c_order_id = ol.c_Order_id)"+
                               " where (o.docstatus = 'CO' or docstatus = 'CL' )"+
                               " group by ol.m_product_id"+
                               " having sum(qtyordered) <> (select qtyordered from m_storage where m_attributesetinstance_id=0 and m_locator_id = 1000277 and m_product_id = ol.m_Product_id)";

            PreparedStatement pstmtobl = DB.prepareStatement(sqlGeneral, trx.getTrxName());
            ResultSet rsobl = pstmtobl.executeQuery();

            //PARA CADA UNO DE LOS PRODUCTOS QUE TENIAN MAL EL ORDENADO...
            while (rsobl.next()) {
                //Cantidad ordenada a actualizar para el Storage del producto
                BigDecimal ordenadoStorage = BigDecimal.ZERO;

                int M_Product_ID = rsobl.getInt(1);
                System.out.println("Producto: "+M_Product_ID);
                int i = 0;
                //OBSERVAR ESTOS CASOS
                if (/*M_Product_ID==1009161||M_Product_ID==1007291||M_Product_ID==1007026||*/M_Product_ID == 1005554) {
                    i++;
                }
                /**
                 * Obtengo todas las lineas de ordenes de compra completas
                 * para un producto dado (uno que tenia mal el ordenado)
                 */
                String sqlProducto = "select " + "orderline.c_orderline_id " + "from c_orderline orderline " + "inner join c_order corder on (corder.c_order_id = orderline.c_order_id)" + "where corder.docstatus = 'CO' " + "and orderline.m_product_id = " + M_Product_ID;



                PreparedStatement stmProducto = DB.prepareStatement(sqlProducto, trx.getTrxName());
                ResultSet resProducto = stmProducto.executeQuery();

                //Para cada linea de orden de compra obtenida obtengo las transacciones asociadas
                while (resProducto.next()) {
                    int C_OrderLine_ID = resProducto.getInt(1);
                    String sqlTrxProducto = "select " +
                            " nvl(sum(trx.movementqty),0)" +
                            " from m_transaction trx" +
                            " inner join m_inoutline iline on (iline.m_inoutline_id = trx.m_inoutline_id)" +
                            " group by" +
                            " trx.m_product_id," +
                            " iline.c_orderline_id," +
                            " trx.movementtype" +
                            " having iline.c_orderline_id = " + C_OrderLine_ID +
                            " and trx.movementtype = 'V+'";



                    PreparedStatement stmTrxProducto = DB.prepareStatement(sqlTrxProducto, trx.getTrxName());
                    ResultSet resTrxProducto = stmTrxProducto.executeQuery();

                    BigDecimal sumaTrx = BigDecimal.ZERO;
                    while (resTrxProducto.next()) {
                        //Obtengo la suma de las transacciones
                        sumaTrx = resTrxProducto.getBigDecimal(1);
                    //Obtengo el ordenado de la linea de compra (que deberia ser igual a line.getQtyOrdered())
                    //*BigDecimal ordenado = linea.getQtyEntered().subtract(linea.getQtyDelivered());
                    }

                    //Obtengo la linea de la orden de compra
                    MOrderLine linea = new MOrderLine(Env.getCtx(), C_OrderLine_ID, trx.getTrxName());
                    resTrxProducto.close();
                    stmTrxProducto.close();

                    //fin modificacion

                    BigDecimal ordenado = sumaTrx;

                    //Si hay mas devoluciones (no deberia pasar...)
                    if (sumaTrx.signum() == -1) {
                        ordenado = linea.getQtyEntered();
                    } //Si hay mas recepciones
                    else if (sumaTrx.signum() == 1) {
                        //Decremento el ordenado
                        ordenado = linea.getQtyEntered().subtract(sumaTrx);
                    } //Se compensan las recepciones con las devoluciones o no existen transacciones
                    else {
                        ordenado = linea.getQtyEntered();
                    }

                    //Chequeo los limites del ordenado entre [0, cantidad ingresada de la linea]
                    if (ordenado.signum() == -1) {
                        ordenado = BigDecimal.ZERO;
                    } else if (ordenado.compareTo(linea.getQtyEntered()) > 0) {
                        ordenado = linea.getQtyEntered();
                    }
                    //Acumulo el ordenado del Storage
                    ordenadoStorage = ordenadoStorage.add(ordenado);

                    //ACTUALIZACION VIA OBJETOS/SQL DE LA LINEA DE OC
                    //Como algunas no las actualiza entonces se hara via SQL

                    /** BISion - 01/04/2009 - Santiago Ibañez
                     * Modificacion realizada para actualizar la cantidad
                     * entregada dado que existian ordenes de compra con cantidad
                     * entregada mayor a cero pero con ninguna transaccion ¿?
                     */
                    BigDecimal entregado;
                    //Si la suma de todas las transacciones es negativa entonces la cantidad entregada es 0
                    if (sumaTrx.signum() == -1) //linea.setQtyDelivered(BigDecimal.ZERO);
                    {
                        entregado = BigDecimal.ZERO;
                    } else {
                        entregado = sumaTrx;
                    }
                    String updateOrderLine = "update C_OrderLine set qtydelivered = " + entregado + ", qtyordered = " + ordenado + ", m_warehouse_id = 1000042 where c_orderline_id = " + linea.getC_OrderLine_ID();
                    DB.executeUpdate(updateOrderLine, trx.getTrxName());
                //fin modificacion BISion
                }
                //se hacen efectivos los cambios sobre la BD
                trx.commit();
                //trx.close();
                resProducto.close();
                stmProducto.close();
                //Actualizo el Storage para el producto
                        /*String sqlWhere = " Where M_Product_ID = "+M_Product_ID+" AND M_AttributeSetInstance_ID = 0 AND M_Locator_ID = 1000277";
                PreparedStatement updateStorage = DB.prepareStatement("UPDATE M_Storage SET QtyOrdered = ?"+sqlWhere,trx.getTrxName());
                updateStorage.setBigDecimal(1, ordenadoStorage);
                updateStorage.executeUpdate();*/

                /** BISion - 31/03/2009 - Santiago Ibañez
                 * Pongo en cero las cantidades ordenadas para el producto en
                 * todos los storages diferentes a Cuarentena Famatina
                 */
                String sql = "update M_Storage set qtyordered = 0 where m_Product_id = " + M_Product_ID + " and m_attributesetinstance_id = 0";
                int k = DB.executeUpdate(sql, trx.getTrxName());
                if (k == -1) {
                    System.out.println("No se pudo actualizar M_Storage para el producto: " + M_Product_ID + " y la partida 0.");
                }
                //fin modificacion BISion

                MStorage ms = MStorage.get(Env.getCtx(), 1000277, M_Product_ID, 0, trx.getTrxName());
                ms.setQtyOrdered(ordenadoStorage);
                ms.save();
                //se hacen efectivos los cambios sobre la BD
                trx.commit();
            //trx.close();
            }
            rsobl.close();
            pstmtobl.close();

            //se hacen efectivos los cambios sobre la BD
            trx.commit();
            trx.close();

        } catch (Exception e) {
            e.printStackTrace();
            //se deshacen los cambios sobre la BD.
            trx.rollback();
            trx.close();
        }

        // Actualizacion de ordenados de elaborados
        trx = Trx.get(Trx.createTrxName("ActOrdElab"), true);

        /**
         * Como primera medida se pone en 0 el ordenado correspondiente
         * a las ordenes de manufactura cerradas.
         */
         //DB.executeUpdate("update mpc_order set qtyordered = 0 where docstatus = 'CL'", trx.getTrxName());
         //trx.commit();

         try {
            DB.executeUpdate("update mpc_order set qtyordered = 0 where docstatus = 'CL'", trx.getTrxName());
            trx.commit();
            String sqlGeneral = "select o.m_product_id,sum(qtyordered) as ord " +
                                " from mpc_order o where o.docstatus = 'CO' or docstatus = 'CL' " +
                                " group by o.m_product_id having sum(qtyordered) <> " +
                                " (select qtyordered from m_storage where m_attributesetinstance_id=0 and m_locator_id = 1000258 and m_product_id = o.m_Product_id)";


            PreparedStatement pstmtobl = DB.prepareStatement(sqlGeneral, trx.getTrxName());
            ResultSet rsobl = pstmtobl.executeQuery();
            while (rsobl.next()) {

                int M_Product_ID = rsobl.getInt(1);
                System.out.println("Producto con cantidad ordenada erronea: "+M_Product_ID);
                //Obtengo todas las ordenes de manufactura
                String sqlOM = "select mpc_order_id from mpc_order where docstatus = 'CO' and m_Product_id = " + M_Product_ID;
                PreparedStatement psOM = DB.prepareStatement(sqlOM, trx.getTrxName());
                ResultSet rsOM = psOM.executeQuery();
                BigDecimal qtyStorage = Env.ZERO;
                while (rsOM.next()) {
                    int MPC_Order_ID = rsOM.getInt(1);
                    MMPCOrder order = new MMPCOrder(ctx, MPC_Order_ID, trx.getTrxName());
                    System.out.println("    Orden de Manufactura: "+order.toString());
                    //Obtengo todas las transacciones asociadas a la OM dada
                    String sqlProducto = "select " + 
                                         " nvl(sum(trx.movementqty),0)" +
                                         " from m_transaction trx " +
                                         " where trx.mpc_order_id = " + MPC_Order_ID +
                                         " and trx.m_product_id = " + M_Product_ID +
                                         " and trx.movementtype = 'P+'";
                    PreparedStatement stmProducto = DB.prepareStatement(sqlProducto, trx.getTrxName());
                    ResultSet resProducto = stmProducto.executeQuery();
                    BigDecimal sumaTrx = BigDecimal.ZERO;
                    while (resProducto.next()) {
                        /*
                         * tengo las transacciones por orden,
                         * ahora resta actualizar la orden segun lo siguiente:
                         * ordenado = cantidad de la orden
                         * ordenado -= sum(trx.movementqty) para tipo de movimiento P+
                         * storage += ordenado de la orden en cuestion
                         */
                        sumaTrx = resProducto.getBigDecimal(1);
                    }
                    resProducto.close();
                    stmProducto.close();
                    BigDecimal ordenado;
                    BigDecimal entregado = BigDecimal.ZERO;
                    //Si se entrego mas de lo que se habia ordenado
                    if (sumaTrx.compareTo(order.getQtyEntered()) > 0) {
                        ordenado = BigDecimal.ZERO;
                        entregado = sumaTrx;
                    } //Si hay mas devoluciones (nunca deberia ocurrir)
                    else if (sumaTrx.signum() == -1) {
                        ordenado = order.getQtyEntered();
                        entregado = BigDecimal.ZERO;
                    } else {
                        //El ordenado para esta OM es la cantidad ingresada menos las sumatoria de las Trx
                        ordenado = order.getQtyEntered().subtract(sumaTrx);
                        entregado = sumaTrx;
                    }
                    
                    //Actualizo la cantidad entregada

                    order.setQtyDelivered(sumaTrx);

                    qtyStorage = qtyStorage.add(ordenado);
                    order.setQtyOrdered(ordenado);
                    DB.executeUpdate("update mpc_order set qtyordered = "+order.getQtyOrdered()+",qtydelivered = "+order.getQtyDelivered()+" where mpc_order_id = "+order.getMPC_Order_ID(), trx.getTrxName());
                    //se hacen efectivos los cambios sobre la BD
                    trx.commit();
                }
                //ACTUALIZO EL STORAGE
                String sql = "update M_Storage set qtyordered = 0 where m_Product_id = " + M_Product_ID + " and m_attributesetinstance_id = 0";
                DB.executeUpdate(sql, trx.getTrxName());
                rsOM.close();
                psOM.close();
                MStorage ms = MStorage.get(ctx, 1000258, M_Product_ID, 0, trx.getTrxName());
                ms.setQtyOrdered(qtyStorage);
                ms.save();
                trx.commit();
            }
            rsobl.close();
            pstmtobl.close();
            //se hacen efectivos los cambios sobre la BD
            trx.commit();
            
        } catch (Exception e) {
            e.printStackTrace();
            //se deshacen los cambios sobre la BD.
            trx.rollback();
            trx.close();
        }

        /** BISion - 01/04/2009 - Santiago Ibañez
         * Por ultimo actualizo las OM poniendo el ordenado como la diferencia entre
         * Cantidad y Cantidad entregada ya que la primer consulta puede haber obviado
         * OM de este tipo
         */
        DB.executeUpdate("update MPC_Order set qtyordered = (case when qtyentered-qtydelivered <0 then 0 else qtyentered-qtydelivered end) where docstatus = 'CO' and (case when qtyentered-qtydelivered <0 then 0 else qtyentered-qtydelivered end) <> qtyordered", trx.getTrxName());
        //trx.commit();
        //trx.close();
        if (Trx.get(trx.getTrxName(), false).commit()){
            trx.close();
        }
        else
        {
            Trx.get(trx.getTrxName(), false).rollback();
            trx.close();
        }
    }


    private void setZeroOrdered(String trxName) throws Exception{

        //Pongo en 0 el ordenado para las ordenes de compra cerradas
        String sql = "update c_orderline ol set ol.qtyordered = 0 " +
                     " where ol.c_order_id in (select c_order_id " +
                     " from c_order where docstatus = 'CL')";

        PreparedStatement ps = DB.prepareStatement(sql, trxName);
        ps.executeUpdate();
        ps.close();

       //Pongo en 0 el ordenado en el Sotrage para todos los productos comprados
       //en los depositos diferentes a Cuarentena
       sql = "update m_storage set qtyordered = 0 " +
             " where m_attributesetinstance_id = 0 " +
             " and m_locator_id <> 1000277 " +
             " and m_product_id in " +
             " (select m_product_id from m_product where ispurchased = 'Y')";
       ps = DB.prepareStatement(sql, trxName);
       ps.executeUpdate();
       ps.close();
       
       //Pongo en 0 el ordenado para las ordenes de compra cerradas
       sql = "update mpc_order set qtyordered = 0 where docstatus = 'CL'";


       //Pongo en 0 el ordenado en el Sotrage para todos los productos elaborados
       //en los depositos diferentes a Aprobado
       sql ="update m_storage set qtyordered = 0 where m_attributesetinstance_id = 0 " +
            " and m_locator_id <> 1000258 and m_product_id in (select m_product_id from m_product where isbom = 'Y')";
       ps = DB.prepareStatement(sql, trxName);
       ps.executeUpdate();
       ps.close();
    }

    /** BISion - 23/04/2009 - Santiago Ibañez
     * Metodo que actualiza:
     *  Las OC y sus lineas seteandole el deposito de Cuarentena
     *  Las OM seteandole el deposito de aprobado.
     * @param trxName
     * @throws java.lang.Exception
     */
    private void setDepositoCorrecto(String trxName) throws Exception{
        //Actualizo las ordenes de compra completas o cerradas
        String sql = "update c_order set m_warehouse_id = 1000042 where docstatus = 'CO' or docstatus = 'CL'";
        PreparedStatement ps = DB.prepareStatement(sql, trxName);
        ps.executeUpdate();
        ps.close();
        //Actualizo las lineas de las ordenes de compra
        sql = "update c_orderline ol set ol.m_warehouse_id = 1000042 where ol.c_order_id in " +
              "(select c_order_id from c_order where docstatus = 'CO' or docstatus = 'CL')";
        ps = DB.prepareStatement(sql, trxName);
        ps.executeUpdate();
        ps.close();        
        //Actualizo las Ordenes de Manufactura completas o cerradas
        /*
         * 03-01-2011 Camarzana Mariano
         * Comentado debido a que no debe modificar el almacen de las ordenes
         */
        /*sql = "update mpc_order set m_warehouse_id = 1000028 where docstatus = 'CL' or docstatus = 'CO'";
        ps = DB.prepareStatement(sql, trxName);
        ps.executeUpdate();
        ps.close();*/
        /*
         * 19-05-2011 Camarzana Mariano
         * Anteriormente se empleaba Aprobado Famatina por eso se seteaban los almacenes con el id = 1000028 que
         * actualmente se emplea departamento de terceros aprobado, debido a esto se seteara con id = 1000032
         */
       /* sql = "update mpc_order set m_warehouse_id = 1000032 where docstatus = 'CL' or docstatus = 'CO'";
        ps = DB.prepareStatement(sql, trxName);
        ps.executeUpdate();
        ps.close();*/
    }
    /**
     *  Start

    public static void main(String[] args){
    try {
    TestVit4b.executeUpdateValues();
    } catch (Exception ex) {
    ex.printStackTrace();
    }
    }
     */
    private boolean setZeroReserved(int M_Product_ID, int M_Locator_ID, String trxName) {
        String sqlGeneral = "update m_storage set qtyreserved = 0 where m_product_id = " + M_Product_ID + " and m_locator_id = " + M_Locator_ID;
        int res = DB.executeUpdate(sqlGeneral, trxName);

        if (res != -1) {
            return true;
        } else {
            return false;
        }

    }

    private BigDecimal getRequiered(int M_Product_ID) {


        String sql = "select " + "sum(bomline.qtyrequiered), " + "bomline.m_product_id, " + "ord.docstatus " + "from mpc_order_bomline bomline " + "inner join mpc_order ord on (bomline.mpc_order_id = ord.mpc_order_id) " + "group by " + "bomline.m_product_id, " + "ord.docstatus " + "having ord.docstatus = 'CO' " + "and bomline.m_product_id = " + M_Product_ID;


        try {

            PreparedStatement pstmtobl = DB.prepareStatement(sql, null);
            ResultSet rsobl = pstmtobl.executeQuery();
            BigDecimal total = Env.ZERO;

            while (rsobl.next()) {

                //System.out.println("Requerido para " + M_Product_ID + " la cantidad " +rsobl.getInt(1));
                total = rsobl.getBigDecimal(1);

            }

            pstmtobl.close();
            rsobl.close();
            return total;

        } catch (SQLException enode) {
        }

        return Env.ZERO;
    }

    private boolean updateBomlines(int M_Product_ID, String trxName) throws Exception {


        String sql = "select " + "sum(trx.movementqty), " + "trx.mpc_order_id, " + "trx.movementtype, " + "ord.docstatus, " + "trx.m_product_id, " + "ordbom.mpc_order_bomline_id, " + "ordbom.qtyrequiered " + "from m_transaction trx " + "inner join mpc_order_bomline ordbom on (trx.mpc_order_bomline_id = ordbom.mpc_order_bomline_id) " + "inner join mpc_order ord on (ordbom.mpc_order_id = ord.mpc_order_id) " + "group by " + "trx.mpc_order_id, " + "trx.movementtype, " + "ord.docstatus, " + "trx.m_product_id, " + "ordbom.mpc_order_bomline_id, " + "ordbom.qtyrequiered " + "having (ord.docstatus = 'CO' or ord.docstatus = 'CL')" + "and trx.m_product_id = " + M_Product_ID + " " + "and trx.movementtype = 'P-' " + "order by trx.mpc_order_id ";
        //try {

        PreparedStatement pstmtobl = DB.prepareStatement(sql, trxName);
        ResultSet rsobl = pstmtobl.executeQuery();
        BigDecimal cantidad = Env.ZERO;
        int producto = 0;
        int bomline = 0;
        BigDecimal requiered = Env.ZERO;

        BigDecimal reserved = Env.ZERO;

        while (rsobl.next()) {

            cantidad = rsobl.getBigDecimal(1);
            producto = rsobl.getInt(5);
            bomline = rsobl.getInt(6);
            requiered = rsobl.getBigDecimal(7);

            // la cantidad se recupera como negativa signo en el cual es cargada en m_storage

            reserved = requiered.add(cantidad);

            if (reserved.compareTo(Env.ZERO) == -1) {
                //System.out.println("Reservado negativo " + indneg + " producto " + producto);
                indneg++;
                reserved = Env.ZERO;
            }

            String sqlUpdate = "update mpc_order_bomline set qtyreserved = " + reserved + " where mpc_order_bomline_id = " + bomline;
            int res = DB.executeUpdate(sqlUpdate, trxName);
            if (res == -1) {
                System.out.println("update falla para c_order_bomline = " + bomline);
                return false;
            }


            System.out.println("update all bomline del producto " + producto + " set qtyreserved = " + reserved + " where mpc_order_bomline_id = " + bomline);


        }

        pstmtobl.close();
        rsobl.close();

        //} catch (SQLException enode) {
        //}

        return true;
    }

    private BigDecimal getSurtimientosDevoluciones(int M_Product_ID) {
        String sql = "select " + "sum(trx.movementqty), " + "trx.m_product_id, " + "ord.docstatus, " + "trx.movementtype " + "from m_transaction trx " + "inner join mpc_order ord on (trx.mpc_order_id = ord.mpc_order_id) " + "group by " + "trx.m_product_id, " + "ord.docstatus, " + "trx.movementtype " + "having (ord.docstatus = 'CO' or ord.docstatus = 'CL')" + "and trx.m_product_id = " + M_Product_ID + " " + "and trx.movementtype = 'P-'";


        try {

            PreparedStatement pstmtobl = DB.prepareStatement(sql, null);
            ResultSet rsobl = pstmtobl.executeQuery();
            BigDecimal total = Env.ZERO;

            while (rsobl.next()) {

                //System.out.println("Obtiene para " + M_Product_ID + " la cantidad " +rsobl.getInt(1));
                total = rsobl.getBigDecimal(1);

            }

            pstmtobl.close();
            rsobl.close();
            return total;

        } catch (SQLException enode) {
        }

        return Env.ZERO;
    }

    private boolean setReserved(int M_Product_ID, int M_Locator_ID, BigDecimal reserved, String trxName) {

        if (reserved.compareTo(Env.ZERO) == -1) {
            //System.out.println("Reservado negativo por surtimientos mayores a requeridos producto " + M_Product_ID);
            reserved = Env.ZERO;
        }

        String sql = "update m_storage set qtyreserved = " + reserved.toString() + " where m_attributesetinstance_id = 0 and m_product_id = " + M_Product_ID + " and m_locator_id = " + M_Locator_ID;
        int res = DB.executeUpdate(sql, trxName);

        if (res != -1) {
            System.out.println("update m_storage set qtyreserved = " + reserved.toString() + " where m_attributesetinstance_id = 0 and m_product_id = " + M_Product_ID + " and m_locator_id = " + M_Locator_ID);
            return true;
        } else {
            System.out.println("update falla para producto = " + M_Product_ID);
            return false;
        }

    }

    private boolean updateAllBomlines(String trxName) throws Exception {

        String sql = "select " + "distinct(bomline.m_product_id) " + "from mpc_order_bomline bomline " + "inner join mpc_order ord on (ord.mpc_order_id = bomline.mpc_order_id) " + "where ord.docstatus = 'CO' or ord.docstatus = 'CL'";



        PreparedStatement pstmtobl = DB.prepareStatement(sql, trxName);
        ResultSet rsobl = pstmtobl.executeQuery();
        BigDecimal cantidad = Env.ZERO;
        int producto = 0;


        while (rsobl.next()) {

            producto = rsobl.getInt(1);
            boolean res = updateBomlines(producto, trxName);

            if (!res) {
                System.out.println("falla actualizacion de " + producto);
                return false;
            }


        //System.out.println("updateBomlines " + producto);



        }

        pstmtobl.close();
        rsobl.close();

        //} catch (Exception enode) {
        //}

        return true;

    }

    private BigDecimal getReservedBomlines(int M_Product_ID, int M_Locator_ID, String trxName) throws Exception {

        String sql = "select sum(bomline.qtyreserved), " + "ord.docstatus, " + "ord.m_warehouse_id, " + "bomline.m_product_id " + "from mpc_order_bomline bomline " + "inner join mpc_order ord on (ord.mpc_order_id = bomline.mpc_order_id) " + "group by ord.docstatus, ord.m_warehouse_id, bomline.m_product_id " + "having (ord.docstatus = 'CO' or ord.docstatus = 'CL') and ord.m_warehouse_id = 1000028 and bomline.m_product_id = " + M_Product_ID;


        BigDecimal totalReservado = Env.ZERO;

        //try {

        PreparedStatement pstmtobl = DB.prepareStatement(sql, trxName);
        ResultSet rsobl = pstmtobl.executeQuery();



        while (rsobl.next()) {
            totalReservado = rsobl.getBigDecimal(1);
        }

        pstmtobl.close();
        rsobl.close();

        //} catch (SQLException enode) {
        //}

        return totalReservado;
    }
}