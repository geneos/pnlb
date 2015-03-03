/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *
 * @author Galleguita
 */
public class EliminarAlmacenes  extends SvrProcess {

	private int m_locator_ID1 = 0;
        private int m_locator_ID2 = 0;
        private int m_locator_ID3 = 0;
        private int m_locator_ID4 = 0;
        private MInventory m_inventory = null;
        private int M_Warehouse_ID = 0;


	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
                    
                    String name = para[i].getParameterName();
                    
                    if (para[i].getParameter() == null)
                            ;			
                    else if (name.equals("M_Locator_ID")){
                        //Como el nombre es el mismo para los 3 me fijo cual encuentra primero
                        if (i==0 && para[i].getParameter().equals("Y"))
                            //Almacen Salidas U. 
                            m_locator_ID1 = 1000296;
                        else if (i==1 && para[i].getParameter().equals("Y"))
                            //Almacen Control de Calidad
                            m_locator_ID2 = 1000278;
                        else if (i==2 && para[i].getParameter().equals("Y"))
                            //Almacen Destrucciones
                            m_locator_ID3 = 1000281;
                        else if (i==3 && para[i].getParameter().equals("Y"))
                            //Almacen Ajuste de Stock
                            m_locator_ID4 = 1000297;
                    } 
                }
	}

	@Override
	protected String doIt() throws Exception {
                String sqlStorage = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                int m_product_id = 0;
                int m_attributesetinstance_id = 0;
                if (m_locator_ID1 != 0){
                    sqlStorage = " SELECT m_product_id, m_attributesetinstance_id,qtyonhand "+
                                 " FROM m_storage "+
                                 " WHERE m_attributesetinstance_id != 0 and qtyonhand != 0 and m_locator_id ="+m_locator_ID1;
                    ps = DB.prepareStatement(sqlStorage, get_TrxName());
                    rs = ps.executeQuery();
                    //Buscamos el ID de M_Warehouse
                    String sqlWarehouse = " SELECT m_warehouse_id "+
                                 " FROM m_locator "+
                                 " WHERE m_locator_id ="+m_locator_ID1;
                    PreparedStatement psWH = DB.prepareStatement(sqlWarehouse, get_TrxName());
                    ResultSet rsWH = psWH.executeQuery();
                    if (rsWH.next()){
                        M_Warehouse_ID = rsWH.getInt(1);
                    }
                    psWH.close();
                    rsWH.close();
                    while (rs.next()){
                        m_product_id = rs.getInt(1);
                        m_attributesetinstance_id = rs.getInt(2);
                        m_inventory = new MInventory (getCtx(), 0, get_TrxName());
                        m_inventory.setDescription("Ajuste en almacen Salidas U.");
                        m_inventory.setM_Warehouse_ID(M_Warehouse_ID);
                        m_inventory.setC_DocType_ID(1000150);
                        BigDecimal QtyOnHand = Env.ZERO;
                        BigDecimal QtyOnBook = rs.getBigDecimal(3);
                        if (!m_inventory.save())
                        {
                                log.log(Level.SEVERE, "Inventory not saved");
                                break;
                        }
                        MInventoryLine line = new MInventoryLine (m_inventory, m_locator_ID1,m_product_id, m_attributesetinstance_id,QtyOnBook,QtyOnHand);		
                        if (!line.save()){
                            log.log(Level.SEVERE, "No se pudo guardar la linea del Ajuste de inventario ID: "+ line.getM_InventoryLine_ID());
                        }
                        m_inventory.completeIt();
                    }
                    ps.close();
                    rs.close();
                }
                if (m_locator_ID2 != 0){
                    sqlStorage = " SELECT m_product_id, m_attributesetinstance_id ,qtyonhand "+
                                 " FROM m_storage "+
                                 " WHERE m_attributesetinstance_id != 0  and qtyonhand != 0 and m_locator_id ="+m_locator_ID2;
                    ps = DB.prepareStatement(sqlStorage, get_TrxName());
                    rs = ps.executeQuery();
                    //Buscamos el ID de M_Warehouse
                    String sqlWarehouse = " SELECT m_warehouse_id "+
                                 " FROM m_locator "+
                                 " WHERE m_locator_id ="+m_locator_ID2;
                    PreparedStatement psWH = DB.prepareStatement(sqlWarehouse, get_TrxName());
                    ResultSet rsWH = psWH.executeQuery();
                    if (rsWH.next()){
                        M_Warehouse_ID = rsWH.getInt(1);
                    }
                    psWH.close();
                    rsWH.close();
                    while (rs.next()){
                        m_product_id = rs.getInt(1);
                        m_attributesetinstance_id = rs.getInt(2);
                        m_inventory = new MInventory (getCtx(), 0, get_TrxName());
                        m_inventory.setDescription("Ajuste en almacen Control de Calidad");
                        m_inventory.setM_Warehouse_ID(M_Warehouse_ID);
                        m_inventory.setC_DocType_ID(1000150);
                        BigDecimal QtyOnHand = Env.ZERO;
                        BigDecimal QtyOnBook = rs.getBigDecimal(3);
                        if (!m_inventory.save())
                        {
                                log.log(Level.SEVERE, "Inventory not saved");
                                break;
                        }
                        MInventoryLine line = new MInventoryLine (m_inventory, m_locator_ID2,m_product_id, m_attributesetinstance_id,QtyOnBook,QtyOnHand);		
                        if (!line.save()){
                            log.log(Level.SEVERE, "No se pudo guardar la linea del Ajuste de inventario ID: "+ line.getM_InventoryLine_ID());
                        }
                        m_inventory.completeIt();
                    }
                    ps.close();
                    rs.close();
                }
                if (m_locator_ID3 != 0){
                    sqlStorage = " SELECT m_product_id, m_attributesetinstance_id,qtyonhand  "+
                                 " FROM m_storage "+
                                 " WHERE m_attributesetinstance_id != 0  and qtyonhand != 0 and m_locator_id ="+m_locator_ID3;
                    ps = DB.prepareStatement(sqlStorage, get_TrxName());
                    rs = ps.executeQuery();
                    //Buscamos el ID de M_Warehouse
                    String sqlWarehouse = " SELECT m_warehouse_id "+
                                 " FROM m_locator "+
                                 " WHERE m_locator_id ="+m_locator_ID3;
                    PreparedStatement psWH = DB.prepareStatement(sqlWarehouse, get_TrxName());
                    ResultSet rsWH = psWH.executeQuery();
                    if (rsWH.next()){
                        M_Warehouse_ID = rsWH.getInt(1);
                    }
                    psWH.close();
                    rsWH.close();
                    while (rs.next()){
                        m_product_id = rs.getInt(1);
                        m_attributesetinstance_id = rs.getInt(2);
                        m_inventory = new MInventory (getCtx(), 0, get_TrxName());
                        m_inventory.setDescription("Ajuste en almacen Destrucciones");
                        m_inventory.setM_Warehouse_ID(M_Warehouse_ID);
                        m_inventory.setC_DocType_ID(1000150);
                        BigDecimal QtyOnHand = Env.ZERO;
                        BigDecimal QtyOnBook = rs.getBigDecimal(3);
                        if (!m_inventory.save())
                        {
                                log.log(Level.SEVERE, "Inventory not saved");
                                break;
                        }
                        MInventoryLine line = new MInventoryLine (m_inventory, m_locator_ID3,m_product_id, m_attributesetinstance_id,QtyOnBook,QtyOnHand);
                        if (!line.save()){
                            log.log(Level.SEVERE, "No se pudo guardar la linea del Ajuste de inventario ID: "+ line.getM_InventoryLine_ID());
                        }
                        m_inventory.completeIt();
                    }
                    ps.close();
                    rs.close();
                }
                if (m_locator_ID4 != 0){
                    sqlStorage = " SELECT m_product_id, m_attributesetinstance_id ,qtyonhand "+
                                 " FROM m_storage "+
                                 " WHERE m_attributesetinstance_id != 0  and qtyonhand != 0 and m_locator_id ="+m_locator_ID4;
                    ps = DB.prepareStatement(sqlStorage, get_TrxName());
                    rs = ps.executeQuery();
                    //Buscamos el ID de M_Warehouse
                    String sqlWarehouse = " SELECT m_warehouse_id "+
                                 " FROM m_locator "+
                                 " WHERE m_locator_id ="+m_locator_ID4;
                    PreparedStatement psWH = DB.prepareStatement(sqlWarehouse, get_TrxName());
                    ResultSet rsWH = psWH.executeQuery();
                    if (rsWH.next()){
                        M_Warehouse_ID = rsWH.getInt(1);
                    }
                    psWH.close();
                    rsWH.close();
                    while (rs.next()){
                        m_product_id = rs.getInt(1);
                        m_attributesetinstance_id = rs.getInt(2);
                        m_inventory = new MInventory (getCtx(), 0, get_TrxName());
                        m_inventory.setDescription("Ajuste en almacen Ajuste de Stock");
                        m_inventory.setM_Warehouse_ID(M_Warehouse_ID);
                        m_inventory.setC_DocType_ID(1000150);
                        BigDecimal QtyOnHand = Env.ZERO;
                        BigDecimal QtyOnBook = rs.getBigDecimal(3);
                        if (!m_inventory.save())
                        {
                                log.log(Level.SEVERE, "Inventory not saved");
                                break;
                        }
                        MInventoryLine line = new MInventoryLine (m_inventory, m_locator_ID4,m_product_id, m_attributesetinstance_id,QtyOnBook,QtyOnHand);		
                        if (!line.save()){
                            log.log(Level.SEVERE, "No se pudo guardar la linea del Ajuste de inventario ID: "+ line.getM_InventoryLine_ID());
                        }
                        m_inventory.completeIt();
                    }
                    ps.close();
                    rs.close();
                }
		return "ok";
                 

	}

}
