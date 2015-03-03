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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.model.CalloutEngine;
import org.compiere.model.MField;
import org.compiere.model.MLocator;
import org.compiere.model.MProduct;
import org.compiere.model.MTab;
import org.compiere.model.MWarehouse;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.eevolution.model.MMPCOrder;
import org.eevolution.model.MMPCOrderBOMLine;


/**
 *  TestVit4b
 *
 *	@author Vit4B
 *	@fecha 02/07/2007 Version para actualizar la base de datos con los reservados.
 *
 */


public class TestVit4b extends SvrProcess {
    
        int indneg = 0;
        
        public void executeUpdateValues() throws Exception {

            System.out.println("ACTUALIZACION DE RESERVADOS");
            /** BISion - 24/11/2008 - Santiago Iba�ez
             * Agregado soporte transaccional
             */
            boolean fallo = true;
            Trx trx = Trx.get(Trx.createTrxName("UpdateBOMLines"), true);
            /**
             * Actualiza la cantidad reservada de las líneas de acuerdo a las
             * transacciones
             */
            try{
                setZeroReservado(trx.getTrxName());
                setDepositoCorrecto(trx.getTrxName());
                updateAllBomlines(trx.getTrxName());
                trx.commit();
                trx.close();
            }
            catch(Exception e){
                e.printStackTrace();
                //se deshacen los cambios sobre la BD.
                trx.rollback();
                trx.close();
                fallo = true;
            }
                
            /* BISion - 10/02/2009 - Santiago Ibañez
             * - Pongo en cero cantidad reservada para OM 'cerradas'.
             * - No se asigna transaccion alguna ya que cualquier cambio
             *   realizado no sera necesario deshacer ante la ocurrencia
             *   de alguna excepcion.
             * - Actualizo el almacenamiento de cada uno de los productos
             *   involucrados en las lineas.
             * */
            /*trx = Trx.get(Trx.createTrxName("UpdateBOMLinesCL"), true);
            try{
                updateClosedBomLines(null);
                //trx.commit();
                //trx.close();
             }
             catch(Exception ex){
                ex.printStackTrace();
                //se deshacen los cambios sobre la BD.
                //trx.rollback();
                //trx.close();
                fallo = true;
             }
             trx = Trx.get(Trx.createTrxName("UpdateBOMLinesCO"), true);
             try{
                /**
                 * Actualiza los reservados de las OM 'completas'
                 * y actualiza los alamacenmaientos de los productos
                 * involucrados en cada una de las líneas.
                 */
                    /*String sqlGeneral = "select o.m_product_id,sum(qtyreserved) as ord from mpc_order o where o.docstatus = 'CO' or docstatus = 'CL' "+
                                        " group by o.m_product_id having sum(qtyreserved) <>"+
                                        " (select qtyreserved from m_storage "+
                                        " where m_attributesetinstance_id=0 "+
                                        " and m_locator_id = 1000258 "+
                                        " and m_product_id = o.m_Product_id)";*/


                    /*String sqlGeneral = "select "
                    + "sum(bomline.qtyreserved), "
                    + "bomline.m_product_id, "
                    + "mstorage.m_attributesetinstance_id, "
                    + "mstorage.qtyreserved, "
                    + "mstorage.m_locator_id, "
                    + "ord.docstatus "
                        + "from mpc_order_bomline bomline "
                        + "inner join mpc_order ord on (bomline.mpc_order_id = ord.mpc_order_id) "
                        + "inner join m_storage mstorage on (mstorage.m_product_id = bomline.m_product_id) "
                        + "group by "
                        + "bomline.m_product_id, "
                        + "mstorage.m_attributesetinstance_id, "
                        + "mstorage.qtyreserved, "
                        + "mstorage.m_locator_id, "
                        + "ord.docstatus "
                        + "having mstorage.m_attributesetinstance_id = 0 "
                        + "and sum(bomline.qtyreserved) <> mstorage.qtyreserved "
                        + "and mstorage.m_locator_id = 1000258 "
                        + "and ord.docstatus = 'CO' order by bomline.m_product_id";*/

                    //cntu = DB.executeUpdate(sqldel,null);
                    /*PreparedStatement pstmtobl = DB.prepareStatement(sqlGeneral,trx.getTrxName());
                    ResultSet rsobl = pstmtobl.executeQuery();
                    //
                    while (rsobl.next()) {
                        //System.out.println("***** entra primer sql del producto " + rsobl.getInt(2) + " con cantidad " + rsobl.getInt(1));
                        int M_Product_ID = rsobl.getInt(1);
                        int M_Locator_ID = 1000258;
                        setZeroReserved(M_Product_ID, M_Locator_ID,null);
                        //BigDecimal requiered = getRequiered(M_Product_ID);
                        //BigDecimal movimientos = getSurtimientosDevoluciones(M_Product_ID);
                        // Los movimientos se registran con signo cambiado por eso lo sumo surtimientos van como -
                        //BigDecimal reserved = requiered.add(movimientos);
                        BigDecimal reserved = getReservedBomlines(M_Product_ID, M_Locator_ID,null);
                        setReserved(M_Product_ID, M_Locator_ID, reserved,null);
                    }
                    pstmtobl.close();
                    rsobl.close();
                    //se hacen efectivos los cambios sobre la BD
                    //trx.commit();
                    //trx.close();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                    //se deshacen los cambios sobre la BD.
                    //trx.rollback();
                    //trx.close();
                    fallo = true;
                }*/
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
            int res = DB.executeUpdate(sqlGeneral,trxName);
            
            if(res != -1)
                return true;
            else
                return false;
            
        }

        private BigDecimal getRequiered(int M_Product_ID) {
            
            
            String sql = "select " 
            + "sum(bomline.qtyrequiered), "
            + "bomline.m_product_id, "
            + "ord.docstatus "
                    + "from mpc_order_bomline bomline "
                    + "inner join mpc_order ord on (bomline.mpc_order_id = ord.mpc_order_id) "
                    + "group by "
                    + "bomline.m_product_id, "
                    + "ord.docstatus "
                    + "having ord.docstatus = 'CO' "
                    + "and bomline.m_product_id = " + M_Product_ID;


            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sql,null);
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
        
        

        private boolean updateBomlines(int M_Product_ID, String trxName) throws Exception{

            BigDecimal reservadoGlobal = BigDecimal.ZERO;

            //Obtengo todas las Lineas de formulas en las que el producto es el M_Product_ID dado
            String sql = "select bom.mpc_order_bomline_id,bom.qtyreserved,bom.qtyrequiered from mpc_order_bomline bom "+
                         " join mpc_order o on (o.mpc_order_id = bom.mpc_order_id)"+
                         " where (o.docstatus = 'CO') and bom.m_product_id = "+M_Product_ID;



            /*String sql = "select bom.m_product_id,bom.mpc_order_bomline_id, bom.qtyrequiered,"+
                         " (select nvl(sum(trx.movementqty),0) from m_transaction trx where trx.mpc_order_bomline_id = bom.mpc_order_bomline_id and trx.movementtype='P-')"+
                         " from mpc_order_bomline bom"+
                         " join mpc_order o on (o.mpc_order_id = bom.mpc_order_id)"+
                         " where bom.m_product_id = "+ M_Product_ID +" and (o.docstatus = 'CO' or o.docstatus='CL')";*/

            //Obtengo todas las transacciones del producto dado agrupadas por lineas de ordenes
            /*String sql = "select "
            + "sum(trx.movementqty), "
            + "trx.mpc_order_id, "
            + "trx.movementtype, "
            + "ord.docstatus, "
            + "trx.m_product_id, "
            + "ordbom.mpc_order_bomline_id, "
            + "ordbom.qtyrequiered "
                    + "from m_transaction trx "
                    + "inner join mpc_order_bomline ordbom on (trx.mpc_order_bomline_id = ordbom.mpc_order_bomline_id) "
                    + "inner join mpc_order ord on (ordbom.mpc_order_id = ord.mpc_order_id) "
                    + "group by "
                    + "trx.mpc_order_id, "
                    + "trx.movementtype, "
                    + "ord.docstatus, "
                    + "trx.m_product_id, "
                    + "ordbom.mpc_order_bomline_id, "
                    + "ordbom.qtyrequiered "
                    + "having (ord.docstatus = 'CO')"
                    + "and trx.m_product_id = " + M_Product_ID + " "
                    + "and trx.movementtype = 'P-' "
                    + "order by trx.mpc_order_id ";*/
            //try {

                PreparedStatement pstmtobl = DB.prepareStatement(sql,trxName);
                ResultSet rsobl = pstmtobl.executeQuery();
                BigDecimal cantidad = Env.ZERO;
                int producto = 0;
                int bomline = 0;
                BigDecimal requiered = Env.ZERO;

                BigDecimal reserved = Env.ZERO;

                while (rsobl.next()) {
                    //suma de las transacciones
                    
                    bomline = rsobl.getInt(1);
                    requiered = rsobl.getBigDecimal(3);

                    if (M_Product_ID==1006422)
                        System.out.println("ACA");
                    cantidad = getSumaTransacciones(bomline, M_Product_ID,trxName);
                    
                    // la cantidad se recupera como negativa signo en el cual es cargada en m_storage
                    if (cantidad==null)
                        System.out.println("ACA");
                    reserved = requiered.add(cantidad);
                    
                    if(reserved.compareTo(Env.ZERO) == -1)
                    {
                        //System.out.println("Reservado negativo " + indneg + " producto " + producto);
                        indneg++;
                        reserved = Env.ZERO;
                    }
                    /** BISion - 24/04/2009 - Santiago Iba�ez
                     * Chequeo para que no supere la cantidad requerida
                     */
                     if(reserved.compareTo(requiered) == 1)
                    {
                        reserved = requiered;
                    }

                    //acumulo la cantidad reservada
                    reservadoGlobal= reservadoGlobal.add(reserved);

                    String sqlUpdate = "update mpc_order_bomline set qtyreserved = " + reserved + ", qtydelivered = "+cantidad.negate()+" where mpc_order_bomline_id = " + bomline;
                    int res = DB.executeUpdate(sqlUpdate,trxName);
                    if(res == -1)
                    {
                        System.out.println("update falla para c_order_bomline = " + bomline);
                        return false;
                    }
                        
                    
                    System.out.println("update all bomline del producto " + M_Product_ID + " set qtyreserved = " + reserved + " where mpc_order_bomline_id = " + bomline);
                    
                    
                }
                //Actualizo el MStorage del producto dado
                int M_Locator_ID =  1000258;
                //Obtengo el producto para saber si es comprado o no
                //MProduct prod = new MProduct(Env.getCtx(), M_Product_ID, trxName);
                //Si el producto es comprado entonces actualizo el storage de
                /*if (prod.isPurchased())
                    M_Locator_ID = 1000277;
                else
                    M_Locator_ID = 1000258;
                */
                /** BISion - 15/01/2009 - Santiago Iba�ez
                 * COM-PAN-REQ-14.002.01
                 */
                //String sqlUpdate = "update m_storage set qtyreserved = " + reservadoGlobal + " where m_attributesetinstance_id = 0 and m_locator_id = "+M_Locator_ID+" and m_product_id = "+M_Product_ID;
                
                /*
                 * 20-05-2011 Camarzana Mariano
                 * Modificado el almacen por el que se utiliza ahora Dep. de Terceros Aprobado
                 */
                /*String sqlUpdate = " update m_storage set qtyreserved = "+reservadoGlobal+
                            " where "+
                            " m_locator_id in "+
                            " (select loc.m_locator_id from m_locator loc "+
                            " join m_warehouse wh on wh.m_warehouse_id = loc.m_warehouse_id "+
                            " where wh.value = 'AprobadoFamatina') "+
                            " and m_product_id = "+M_Product_ID+
                            " and m_attributesetinstance_id = 0 ";*/

                //Obtengo el almacen correspondiente a la orden de manufactura
                MWarehouse wh = MWarehouse.get(Env.getCtx(), 1000032);
                //Obtengo la ubicacion por defecto del almacen
                MLocator locator = new MLocator(Env.getCtx(),wh.getDefaultLocator().getM_Locator_ID(),null);
				
				String sqlUpdate = " update m_storage set qtyreserved = "+reservadoGlobal+
                " where "+
                " m_locator_id = " + locator.get_ID() +
                " and m_product_id = "+M_Product_ID+
                " and m_attributesetinstance_id = 0 ";


				
				int res = DB.executeUpdate(sqlUpdate,trxName);
                if(res == -1)
                {
                    System.out.println("update del storage falla para producto = " + M_Product_ID);
                    return false;
                }

                pstmtobl.close();
                rsobl.close();

            //} catch (SQLException enode) {
            //}
            
            return true;
        }
        
        /** BISion - 29/04/2009 - Santiago Ibañez
         * Metodo que retorna la suma de trx de tipo P- para una linea de orden
         * dada
         * @param MPC_Order_BomLine_ID
         * @param trxName
         * @return
         */
        private BigDecimal getSumaTransacciones(int MPC_Order_BomLine_ID, int M_Product_ID,String trxName){
            BigDecimal trx = BigDecimal.ZERO;
            String sql = "select sum(trx.movementqty) from m_transaction trx where trx.mpc_order_bomline_id = "+MPC_Order_BomLine_ID +" and trx.movementtype='P-' and m_product_id = "+M_Product_ID;
            PreparedStatement psTrx = DB.prepareStatement(sql, trxName);
            try{
                ResultSet rs = psTrx.executeQuery();
                if (rs.next()){
                    trx = rs.getBigDecimal(1);
                    if (trx==null)
                        trx = BigDecimal.ZERO;
                }
                psTrx.close();
                rs.close();
            }
            catch(Exception e){
                System.out.println("No se pudo obtener la suma de Trx para la MPC_Order_BomLine_ID = "+MPC_Order_BomLine_ID);
            }
            return trx;
        }

        private BigDecimal getSurtimientosDevoluciones(int M_Product_ID) {
            String sql = "select " 
            + "sum(trx.movementqty), "
            + "trx.m_product_id, "                    
            + "ord.docstatus, "
            + "trx.movementtype "
                    + "from m_transaction trx "
                    + "inner join mpc_order ord on (trx.mpc_order_id = ord.mpc_order_id) "
                    + "group by "
                    + "trx.m_product_id, "
                    + "ord.docstatus, "
                    + "trx.movementtype "
                    + "having (ord.docstatus = 'CO')"
                    + "and trx.m_product_id = " + M_Product_ID + " "
                    + "and trx.movementtype = 'P-'";


            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sql,null);
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
            
            if(reserved.compareTo(Env.ZERO) == -1)
            {
                //System.out.println("Reservado negativo por surtimientos mayores a requeridos producto " + M_Product_ID);
                reserved = Env.ZERO;
            }
            
            String sql = "update m_storage set qtyreserved = " + reserved.toString() + " where m_attributesetinstance_id = 0 and m_product_id = " + M_Product_ID + " and m_locator_id = " + M_Locator_ID;
            int res = DB.executeUpdate(sql,trxName);
            
            if(res != -1)
            {
                System.out.println("update m_storage set qtyreserved = " + reserved.toString() + " where m_attributesetinstance_id = 0 and m_product_id = " + M_Product_ID + " and m_locator_id = " + M_Locator_ID);
                return true;
            }
            else
            {
                System.out.println("update falla para producto = " + M_Product_ID);
                return false;
            }
                
        }

        private boolean updateAllBomlines(String trxName) throws Exception{
            
            //Obtengo todos los productos involucrados en lineas de ordenes completas

            String sql = "select "
                    + "distinct(bomline.m_product_id) "
                    + "from mpc_order_bomline bomline "
                    + "inner join mpc_order ord on (ord.mpc_order_id = bomline.mpc_order_id) "                    
                    + "where ord.docstatus = 'CO'";
            
            
                /*
                 * BISion - 10/02/2009 - Santiago Iba�ez
                 * La actualizacion de las lineas se realiza en el momento
                 * trx Local, dado que si se produce alguna excepcion los
                 * cambios hechos no es necesario deshacerlos
                */
                PreparedStatement pstmtobl = DB.prepareStatement(sql,trxName);
                ResultSet rsobl = pstmtobl.executeQuery();
                BigDecimal cantidad = Env.ZERO;
                int producto = 0;
                
                
                while (rsobl.next()) {
                    producto = rsobl.getInt(1);
                    int i=0;
                    if (producto == 1004900)
                        i++;
                    System.out.println("Producto: "+producto);
                    boolean res = updateBomlines(producto,trxName);
                    
                    if(!res)
                    {
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

        /**
         * Metodo que retorna para un producto dado la suma de cada una de las
         * cantidades reservadas en las lineas de formulas de ordenes de
         * produccion completas
         * @param M_Product_ID
         * @param M_Locator_ID
         * @param trxName
         * @return
         * @throws java.lang.Exception
         */
        private BigDecimal getReservedBomlines(int M_Product_ID, int M_Locator_ID, String trxName) throws Exception {
            
            String sql = "select sum(bomline.qtyreserved), " 
            + "ord.docstatus, "
            + "ord.m_warehouse_id, "
            + "bomline.m_product_id "
            + "from mpc_order_bomline bomline "
            + "inner join mpc_order ord on (ord.mpc_order_id = bomline.mpc_order_id) "
            + "group by ord.docstatus, ord.m_warehouse_id, bomline.m_product_id "
            + "having ord.docstatus = 'CO' and ord.m_warehouse_id = 1000028 and bomline.m_product_id = " + M_Product_ID;
            
            
            BigDecimal totalReservado = Env.ZERO;

            //try {

                PreparedStatement pstmtobl = DB.prepareStatement(sql,trxName);
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

        /** BISion - 10/02/2009 - Santiago Ibañez
         * Método que pone en cero la cantidad reservada de aquellas lineas
         * de formula de ordenes de manufactura en estado cerrado
         */
        private void updateClosedBomLines(String trxName) throws Exception{
            //Obtengo cada una de las líneas de ordenes de manufactura cerradas
            String sql = "select "
                    + "bomline.mpc_order_bomline_id "
                    + "from mpc_order_bomline bomline "
                    + "inner join mpc_order ord on (ord.mpc_order_id = bomline.mpc_order_id) "
                    + "where ord.docstatus = 'CL'";
             PreparedStatement pstmtobl = DB.prepareStatement(sql,trxName);
             ResultSet rsobl = pstmtobl.executeQuery();
             int MPC_Order_BOMLine_ID = 0;
             //Para cada una de las lineas...
             while (rsobl.next()) {
                MPC_Order_BOMLine_ID = rsobl.getInt(1);
                //Obtengo la línea
                MMPCOrderBOMLine bom = new MMPCOrderBOMLine(Env.getCtx(),MPC_Order_BOMLine_ID,trxName);
                //Obtengo la orden de manufactura a la que pertenece
                MMPCOrder order = new MMPCOrder(Env.getCtx(),bom.getMPC_Order_ID(),trxName);
                //Obtengo el almacen correspondiente a la orden de manufactura
                MWarehouse wh = MWarehouse.get(Env.getCtx(), order.getM_Warehouse_ID());
                //Obtengo la ubicacion por defecto del almacen
				int M_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
                //Seteo la cantidad reservada en cero
                bom.setQtyReserved(BigDecimal.ZERO);
                //Seteo el almacenamiento en cero
                setZeroReserved(bom.getM_Product_ID(),M_Locator_ID,trxName);
                if (!bom.save())
                    System.out.print("En la linea "+MPC_Order_BOMLine_ID+" no se pudo poner en cero la cantidad reservada");
                else
                    System.out.println("update all closed bomlines set qtyreserved = 0 where mpc_order_bomline_id = " + MPC_Order_BOMLine_ID);
             }

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
            /*//Actualizo las Ordenes de Manufactura completas o cerradas
            sql = "update mpc_order set m_warehouse_id = 1000028 where docstatus = 'CL' or docstatus = 'CO'";
            ps = DB.prepareStatement(sql, trxName);
            ps.executeUpdate();
            ps.close();*/
            
            /*
             * 20-05-2011 Camarzana Mariano
             * Modificado el almacen que es el que se utiliza ahora Dep. de Terceros Aprobado
             */
            //Actualizo las Ordenes de Manufactura completas o cerradas
            sql = "update mpc_order set m_warehouse_id = 1000032 where docstatus = 'CL' or docstatus = 'CO'";
            ps = DB.prepareStatement(sql, trxName);
            ps.executeUpdate();
            ps.close();

        }

        /**
         * BISion - 24/04/2009 - Santiago Iba�ez
         * Metodo que pone en cero los reservados de las ordenes cerradas y
         * del resrvado global en las ubicaciones adecuadas.
         * @param trxName
         * @throws java.lang.Exception
         */
        private void setZeroReservado(String trxName) throws Exception{

            //Pongo en 0 el ordenado para las ordenes de compra cerradas
            String sql = "update c_orderline ol set ol.qtyreserved = 0 " +
                         " where ol.c_order_id in (select c_order_id " +
                         " from c_order where docstatus = 'CL')";

            PreparedStatement ps = DB.prepareStatement(sql, trxName);
            ps.executeUpdate();
            ps.close();

           //Pongo en 0 el reservado en el Storage para todos los productos comprados
           //en los depositos diferentes a Cuarentena
           sql = "update m_storage set qtyreserved = 0 " +
                 " where m_attributesetinstance_id = 0 " +
                 " and m_product_id in " +
                 " (select m_product_id from m_product where ispurchased = 'Y')";
           ps = DB.prepareStatement(sql, trxName);
           ps.executeUpdate();
           ps.close();

           //Pongo en 0 el reservado para las ordenes de manufactura cerradas
           sql = "update mpc_order set qtyreserved = 0 where docstatus = 'CL'";
           ps = DB.prepareStatement(sql, trxName);
           ps.executeUpdate();
           ps.close();

           //Pongo en 0 el reservado para las lineas de las ordenes de manufactura cerradas
           sql = "update mpc_order_bomline set qtyreserved = 0 where mpc_order_id in (select mpc_order_id from mpc_order where docstatus = 'CL')";
           ps = DB.prepareStatement(sql, trxName);
           ps.executeUpdate();
           ps.close();

           //Pongo en 0 el ordenado en el Storage para todos los productos elaborados
           //en todos los depositos (No discrimino aprobado del resto ya que inicialmente
           //los pongo en cero a todos y despues reestabelezco el reservado solo para los depositos de Aprobado
           sql ="update m_storage set qtyreserved = 0 where m_attributesetinstance_id = 0 " +
                " and m_product_id in (select m_product_id from m_product where isbom = 'Y')";
           ps = DB.prepareStatement(sql, trxName);
           ps.executeUpdate();
           ps.close();
        }

    @Override
    protected void prepare() {
    }

    @Override
    protected String doIt() throws Exception {
        /*executeUpdateValues();*/
        //validarInvoice();
        
        try
            {
                //1- Actualizcion de Reservados
                MRP mrp = new MRP();
                if (! mrp.executeUpdateValues())
                     throw new RuntimeException("No se pudo completar la actualización de reservados.");                 
            }		
        catch (Exception e)
		{
                    //ser� capturada por Svr para deshacer la transacci�n.
                    throw e;
		}
        
        return  "ok";
    }

    private void validarInvoice() throws FileNotFoundException {
        
        File f = new File("sql.txt");
        BufferedReader entrada = new BufferedReader(new FileReader(f));
        String sql;
        if(f.exists())
            try {
                
                sql = entrada.readLine();
                
                PreparedStatement pstmtobl = DB.prepareStatement(sql,null);
                ResultSet rsobl;
                try {
                    rsobl = pstmtobl.executeQuery();
                    if(rsobl.next()) {
                        System.out.println("OK " + sql);
                    } else {
                        System.out.println(sql);
                    }
                    pstmtobl.close();
                    rsobl.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TestVit4b.class.getName()).log(Level.SEVERE, null, ex);
                }
                                
            } catch (IOException ex) {
                Logger.getLogger(TestVit4b.class.getName()).log(Level.SEVERE, null, ex);
            }  
        
        
    }
}