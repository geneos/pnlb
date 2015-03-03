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
 *  TestVit4b
 *
 *	@author Vit4B
 *	@fecha 02/07/2007 Version para actualizar la base de datos con los reservados.
 *
 */


public class UpdateDataOrder extends CalloutEngine {
    
        int indneg = 0;
        
        public void executeUpdateValues(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) throws Exception {

            
            updateMStorageOrdererZero();
            
            updateMStorageOrdererDifference();

            updateOrderOrdererDifference();
            
            updateOrderLinesZero();
            
        }
            
            

        private boolean updateMStorageOrdererZero() {
            
            String sql = "update m_storage set qtyordered = 0 where m_product_id in (select m_product_id from m_product where ispurchased = 'N' and isbom = 'Y')";
            int res = DB.executeUpdate(sql,null);
            
            if(res != -1)
                return true;
            else
                return false;
       
        }

        private void updateMStorageOrdererDifference() {

            String sqlGeneral = "select mpc_order_id, m_product_id, "
                    + "m_attributesetinstance_id, qtyentered, qtydelivered, docstatus "
                    + "from mpc_order "
                    + "where docstatus = 'CO'";



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
                    
                    
                    String tipoMovimiento = "P+";
                    
                    int receipt = getMovimientos(MPC_Order_ID, M_Product_ID, tipoMovimiento);
                    
                    int difference = qtyEntered - receipt;
                    
                    
                    
                    boolean res = false;
                    
                    /**
                     *      Actualizo el ordenado en el registro de m_storage correspondient a M_Product_ID
                     *      en el deposito m_locator_id = 1000258 para la instancia m_attributesetinstance_id = 0
                     *
                     *
                     */
                    
                    
                    if(difference > 0)
                    {
                        res = updateOrderedValue(M_Product_ID,difference);
                    
                        if(!res)
                            System.out.println("Error en la actualización");

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
        
        
        private boolean updateOrderedValue(int M_Product_id, int ordered) {
            
            
            int actual = getValueOrderedInStorage(M_Product_id);
            
            int value = actual + ordered;
            String sql = "";
            
            sql = "update m_storage set qtyordered = " + value + " where m_product_id = " + M_Product_id + " and m_locator_id = 1000258 and m_attributesetinstance_id = 0";
            
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

        private void updateOrderOrdererDifference() {

            String sqlGeneral = "select mpc_order_id, m_product_id, "
                    + "m_attributesetinstance_id, qtyentered, qtydelivered, docstatus "
                    + "from mpc_order "
                    + "where docstatus = 'CO'";



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
                    
                    
                    String tipoMovimiento = "P+";
                    
                    int receipt = getMovimientos(MPC_Order_ID, M_Product_ID, tipoMovimiento);
                    
                    int difference = qtyEntered - receipt;
                    
                    
                    
                    boolean res = false;
                    
                    if(difference > 0)
                    {
                        res = updateOrderedValueOrder(MPC_Order_ID,difference);
                        res = updateReservedValueOrder(MPC_Order_ID,difference);
                        res = updateDeliveredValueOrder(MPC_Order_ID,receipt);
                        
                        System.out.println("Diferencia " + difference + " para MPC_Order_ID " + MPC_Order_ID);                    

                        if(!res)
                            System.out.println("Error en la actualización");                    
                    }
                    else
                    {
                        res = updateOrderedValueOrder(MPC_Order_ID,0);
                        res = updateReservedValueOrder(MPC_Order_ID,0);
                        res = updateDeliveredValueOrder(MPC_Order_ID,receipt);
                        
                        System.out.println("Diferencia " + difference + " para MPC_Order_ID " + MPC_Order_ID);                    

                        if(!res)
                            System.out.println("Error en la actualización");                    
                    }
                        
                    


                   
                    
                }
                
                pstmtobl.close();
                rsobl.close();

            } catch (SQLException enode) {
            }
            
            
            
            
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
        
        
        private boolean updateDeliveredValueOrder(int MPC_Order_ID, int value) {
            
            String sql = "update mpc_order set qtydelivered = " + value + " where mpc_order_id = " + MPC_Order_ID;
                
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

        private boolean updateReservedValueOrderLine(int MPC_Order_ID, int value) {
            
            String sql = "update mpc_order_bomline set qtyreserved = " + value + " where mpc_order_id = " + MPC_Order_ID;
                
            int res = DB.executeUpdate(sql,null);
            
            if(res != -1)
                return true;
            else
                return false;
          
        }

        private void updateOrderLinesZero() {

            String sqlGeneral = "select mpc_order_id from mpc_order where docstatus = 'CL' order by documentno";



            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sqlGeneral,null);
                ResultSet rsobl = pstmtobl.executeQuery();
                
                while (rsobl.next()) {

                    int MPC_Order_ID = rsobl.getInt(1);
                    //updateOrderedValueOrderLine(MPC_Order_ID, 0);
                    updateReservedValueOrderLine(MPC_Order_ID, 0);
                    
                }
                
                pstmtobl.close();
                rsobl.close();

            } catch (SQLException enode) {
            }
            
        }



}