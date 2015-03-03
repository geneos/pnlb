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
 *  IvaCompras
 *
 *	@author Vit4B
 *	@fecha 08/11/2007 Version para actualizar la base de datos con los reservados.
 *
 */


public class IvaCompras extends CalloutEngine {
    
        int indneg = 0;
        
        public void executeUpdateValues(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) throws Exception {

            
            updateAllBomlines();
            
            
            

            String sqlGeneral = "select " 
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
                    + "and ord.docstatus = 'CO' order by bomline.m_product_id";


            //cntu = DB.executeUpdate(sqldel,null);

            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sqlGeneral,null);
                ResultSet rsobl = pstmtobl.executeQuery();
                //
                while (rsobl.next()) {

                    //System.out.println("***** entra primer sql del producto " + rsobl.getInt(2) + " con cantidad " + rsobl.getInt(1));
                    

                    int M_Product_ID = rsobl.getInt(2);                
                    int M_Locator_ID = rsobl.getInt(5);

                    setZeroReserved(M_Product_ID, M_Locator_ID);                
                    //BigDecimal requiered = getRequiered(M_Product_ID);
                    //BigDecimal movimientos = getSurtimientosDevoluciones(M_Product_ID);
                    
                    // Los movimientos se registran con signo cambiado por eso lo sumo surtimientos van como -
                    
                    //BigDecimal reserved = requiered.add(movimientos);
                    
                    BigDecimal reserved = getReservedBomlines(M_Product_ID, M_Locator_ID);
                    
                    
                    
                    
                    
                    setReserved(M_Product_ID, M_Locator_ID, reserved);
                    
                }
                
                pstmtobl.close();
                rsobl.close();

            } catch (SQLException enode) {
            }
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
        
        private boolean setZeroReserved(int M_Product_ID, int M_Locator_ID) {

            String sqlGeneral = "update m_storage set qtyreserved = 0 where m_product_id = " + M_Product_ID + " and m_locator_id = " + M_Locator_ID;
            int res = DB.executeUpdate(sqlGeneral,null);
            
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
                    + "having ord.docstatus = 'CO' "
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

        private boolean setReserved(int M_Product_ID, int M_Locator_ID, BigDecimal reserved) {
            
            if(reserved.compareTo(Env.ZERO) == -1)
            {
                //System.out.println("Reservado negativo por surtimientos mayores a requeridos producto " + M_Product_ID);
                reserved = Env.ZERO;
            }
            
            String sql = "update m_storage set qtyreserved = " + reserved.toString() + " where m_attributesetinstance_id = 0 and m_product_id = " + M_Product_ID + " and m_locator_id = " + M_Locator_ID;
            int res = DB.executeUpdate(sql,null);
            
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

        private boolean updateAllBomlines() {
            
            String sql = "select " 
                    + "distinct(bomline.m_product_id) "
                    + "from mpc_order_bomline bomline "
                    + "inner join mpc_order ord on (ord.mpc_order_id = bomline.mpc_order_id) "                    
                    + "where ord.docstatus = 'CO' ";
            
            
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

        private BigDecimal getReservedBomlines(int M_Product_ID, int M_Locator_ID) {
            
    
            String sql = "select sum(bomline.qtyreserved), " 
            + "ord.docstatus, "
            + "ord.m_warehouse_id, "
            + "bomline.m_product_id "
            + "from mpc_order_bomline bomline "
            + "inner join mpc_order ord on (ord.mpc_order_id = bomline.mpc_order_id) "
            + "group by ord.docstatus, ord.m_warehouse_id, bomline.m_product_id "
            + "having ord.docstatus = 'CO' and ord.m_warehouse_id = 1000028 and bomline.m_product_id = " + M_Product_ID;
            
            
            
            
            
            
            
            BigDecimal totalReservado = Env.ZERO;

            try {

                PreparedStatement pstmtobl = DB.prepareStatement(sql,null);
                ResultSet rsobl = pstmtobl.executeQuery();

                
                
                while (rsobl.next()) {
                    totalReservado = rsobl.getBigDecimal(1);
                }
                
                pstmtobl.close();
                rsobl.close();

            } catch (SQLException enode) {
            }
            
            return totalReservado;
        }

}