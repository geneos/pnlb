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
import org.compiere.model.MTab;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *  UpdateOrdered
 *
 *	@author Vit4B
 *	@fecha 02/07/2007 Version para actualizar la base de datos con los ordenados.
 *
 */


public class UpdateOrdered extends CalloutEngine {
    
        int indneg = 0;
        
        public void executeUpdateValuesLines(int MPC_Order_ID) {

            
            
                        
            String sqlGeneral = "select mpc_order_bomline_id, qtyreserved, qtydelivered, m_product_id "
                    + "from mpc_order_bomline "
                    + "where mpc_order_id = " + MPC_Order_ID;


            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sqlGeneral,null);
                ResultSet rsobl = pstmtobl.executeQuery();
                
                while (rsobl.next()) {

                    int Bomline_ID = rsobl.getInt(1);
                    int qtyReserved = rsobl.getInt(2);
                    int qtyDelivered = rsobl.getInt(3);
                    int M_Product_ID = rsobl.getInt(4);
                    
                    boolean res = false;
                    
                    //res = updateOrderedValueOrderLine(Bomline_ID,0);
                    //if(!res)
                    //   System.out.println("Error en la actualización");

                    res = updateReservedValueOrderLine(Bomline_ID,0);
                    if(!res)
                        System.out.println("Error en la actualización");

                    
                    updateReservedValueStorage(M_Product_ID,qtyReserved);
                    
                    

                }
                
                pstmtobl.close();
                rsobl.close();

            } catch (SQLException enode) {
            }
        }
        

         
        public void executeUpdateValues(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) throws Exception {

            
            updateAllBomlines();
                        
            String sqlGeneral = "select mpc_order_id, m_product_id, m_attributesetinstance_id, qtyentered, qtydelivered, docstatus "
                    + "from mpc_order "
                    + "where docstatus = 'CO' or  docstatus = 'CL'";


            //cntu = DB.executeUpdate(sqldel,null);

            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sqlGeneral,null);
                ResultSet rsobl = pstmtobl.executeQuery();
                
                while (rsobl.next()) {

                    int MPC_Order_ID = rsobl.getInt(1);                
                    int M_Product_ID = rsobl.getInt(2);                
                    int M_Attributesetinstance_id = rsobl.getInt(3);
                    
                    int qtyEntered = rsobl.getInt(4);
                    int qtyDelivered = rsobl.getInt(5);
                    String docstatus = rsobl.getString(6);
                    
                    //int difference = qtyEntered - qtyDelivered;
                    
                    int difference = qtyEntered;
                    
                    String tipoMovimiento = "P+";
                    
                    int receipt = getMovimientos(MPC_Order_ID, M_Product_ID, tipoMovimiento);
                    
                    boolean res = false;
                    
                    if(difference > receipt)
                        res = updateOrderedValue(M_Product_ID,receipt);
                    else
                        res = updateOrderedValue(M_Product_ID,difference);
                    
                    if(!res)
                        System.out.println("Error en la actualización");

                    if(docstatus.equals("CL"))
                    {
                        
                        res = updateOrderedValueOrder(MPC_Order_ID,0);
                        if(!res)
                            System.out.println("Error en la actualización");

                        res = updateReservedValueOrder(MPC_Order_ID,0);
                        if(!res)
                            
                            System.out.println("Error en la actualización");
                        
                        
                        
                        int valor = difference - receipt;
                        
                        if(valor > 0)
                            res = updateOrderedValue(M_Product_ID,valor);
                    
                        if(!res)
                        System.out.println("Error en la actualización");                      
                        
                        executeUpdateValuesLines(MPC_Order_ID);
                        
                    }
                    
                    
                }
                
                pstmtobl.close();
                rsobl.close();

            } catch (SQLException enode) {
            }
            
            
            
            
            
        }
        

        private int getMovimientos(int MPC_Order_ID, int M_Product_ID, String tipoMovimiento) {
            
            String sqlGeneral = "select sum(movementqty) "
                    + "from m_transaction "
                    + "where mpc_order_id = " + MPC_Order_ID 
                    + " and m_product_id = " + M_Product_ID
                    + " and movementtype = '" + tipoMovimiento + "'";


            int qty = 0;
                
            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sqlGeneral,null);
                ResultSet rsobl = pstmtobl.executeQuery();
                
                while (rsobl.next()) {

                     qty = rsobl.getInt(1);

                }
                
                pstmtobl.close();
                rsobl.close();

            } catch (SQLException enode) {
            }            
            
            
            return qty;
        }

        private boolean updateOrderedValue(int M_Product_id, int receipt) {
            
            
            int value = getValueOrderedInStorage(M_Product_id);
            
            int difference = value - receipt;
            String sql = "";
            
            if(difference < 0)
                sql = "update m_storage set qtyordered = 0 where m_product_id = " + M_Product_id + " and m_locator_id = 1000258 and m_attributesetinstance_id = 0";
            
            else
                sql = "update m_storage set qtyordered = " + difference + " where m_product_id = " + M_Product_id + " and m_locator_id = 1000258 and m_attributesetinstance_id = 0";
                
            int res = DB.executeUpdate(sql,null);
            
            if(res != -1)
                return true;
            else
                return false;
        
        }

        private int getValueOrderedInStorage(int M_Product_id) {
            String sqlGeneral = "select qtyordered "
                    + "from m_storage "
                    + "where m_product_id = " + M_Product_id
                    + " and m_attributesetinstance_id = 0 and m_locator_id = 1000258";


            int qty = 0;
                
            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sqlGeneral,null);
                ResultSet rsobl = pstmtobl.executeQuery();
                
                while (rsobl.next()) {

                     qty = rsobl.getInt(1);

                }
                
                pstmtobl.close();
                rsobl.close();

            } catch (SQLException enode) {
            }            
            
            
            return qty;
        }
        
        private int getValueReservedInStorage(int M_Product_id) {
            String sqlGeneral = "select qtyreserved "
                    + "from m_storage "
                    + "where m_product_id = " + M_Product_id
                    + " and m_attributesetinstance_id = 0 and m_locator_id = 1000258";


            int qty = 0;
                
            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sqlGeneral,null);
                ResultSet rsobl = pstmtobl.executeQuery();
                
                while (rsobl.next()) {

                     qty = rsobl.getInt(1);

                }
                
                pstmtobl.close();
                rsobl.close();

            } catch (SQLException enode) {
            }            
            
            
            return qty;
        }

        private boolean updateReservedValueStorage(int M_Product_ID, int qtyReserved) {
            
            
            int value = getValueReservedInStorage(M_Product_ID);
            
            int difference = value - qtyReserved;
            
            String sql = "update m_storage set qtyreserved = " + difference + " where m_product_id = " + M_Product_ID + " and m_locator_id = 1000258 and m_attributesetinstance_id = 0";
                
            int res = DB.executeUpdate(sql,null);
            
            if(res != -1)
                return true;
            else
                return false;
            
        }

        private boolean updateOrderedValueOrder(int MPC_Order_ID, int value) {
            
            String sql = "update mpc_order set qtyordered = " + value + " where mpc_order_id = " + MPC_Order_ID;
                
            int res = DB.executeUpdate(sql,null);
            
            if(res != -1)
                return true;
            else
                return false;
            
        }

        private boolean updateReservedValueOrder(int MPC_Order_ID, int value) {
            
            String sql = "update mpc_order set qtyreserved = " + value + " where mpc_order_id = " + MPC_Order_ID;
                
            int res = DB.executeUpdate(sql,null);
            
            if(res != -1)
                return true;
            else
                return false;
          
        }
        
        private boolean updateOrderedValueOrderLine(int MPC_Orderline_ID, int value) {
            
            String sql = "update mpc_order_bomline set qtyordered = " + value + " where mpc_order_bomline_id = " + MPC_Orderline_ID;
                
            int res = DB.executeUpdate(sql,null);
            
            if(res != -1)
                return true;
            else
                return false;
            
        }

        private boolean updateReservedValueOrderLine(int MPC_Orderline_ID, int value) {
            
            String sql = "update mpc_order_bomline set qtyreserved = " + value + " where mpc_order_bomline_id = " + MPC_Orderline_ID;
                
            int res = DB.executeUpdate(sql,null);
            
            if(res != -1)
                return true;
            else
                return false;
          
        }
            
        
        private boolean updateAllBomlines() {
            
            String sql = "select " 
                    + "distinct(bomline.m_product_id) "
                    + "from mpc_order_bomline bomline "
                    + "inner join mpc_order ord on (ord.mpc_order_id = bomline.mpc_order_id) "                    
                    + "where ord.docstatus = 'CL' ";
            
            
            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sql,null);
                ResultSet rsobl = pstmtobl.executeQuery();
                BigDecimal cantidad = Env.ZERO;
                int producto = 0;
                
                
                while (rsobl.next()) {

                    producto = rsobl.getInt(1);
                    boolean res = updateBomlines(producto);
                    
                    if(!res)
                    {
                        System.out.println("falla actualizacion de " + producto);
                        return false;
                    }
                        
                    
                    //System.out.println("updateBomlines " + producto);
                    
                    
                    
                }
                
                pstmtobl.close();
                rsobl.close();

            } catch (SQLException enode) {
            }
            
            return true;

        }
        
        
        private boolean updateBomlines(int M_Product_ID) {

            
            String sql = "select "
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
                    + "having ord.docstatus = 'CO' "
                    + "and trx.m_product_id = " + M_Product_ID + " "
                    + "and trx.movementtype = 'P-' "
                    + "order by trx.mpc_order_id ";

            

            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sql,null);
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
                    
                    if(reserved.compareTo(Env.ZERO) == -1)
                    {
                        //System.out.println("Reservado negativo " + indneg + " producto " + producto);
                        indneg++;
                        reserved = Env.ZERO;
                    }
                    
                    String sqlUpdate = "update mpc_order_bomline set qtyreserved = " + reserved + " where mpc_order_bomline_id = " + bomline;
                    int res = DB.executeUpdate(sqlUpdate,null);
            
                    if(res == -1)
                    {
                        System.out.println("update falla para c_order_bomline = " + bomline);
                        return false;
                    }
                        
                    
                    System.out.println("update all bomline del producto " + producto + " set qtyreserved = " + reserved + " where mpc_order_bomline_id = " + bomline);
                    
                    
                }
                
                pstmtobl.close();
                rsobl.close();

            } catch (SQLException enode) {
            }
            
            return true;
        }
   

}