/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author Galleguita
 */
public class EliminarStock  extends SvrProcess {

	private String m_locator_ID = null;
	private String m_product_category_ID = null;
	private MInventory m_inventory = null;
	private int M_Warehouse_ID = 0;


	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
                    
                    String name = para[i].getParameterName();
                    if (name.equals("M_Locator_ID")){
                        m_locator_ID = (para[i].getParameter().toString());
                    }
                    else if (name.equals("M_Product_Category_ID")){
                            m_product_category_ID = (para[i].getParameter().toString());
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
                
                ResultSet rsProduct = getProduct();
                
                if(m_product_category_ID != null && m_locator_ID != null){
                    while (rsProduct.next()){
			m_product_id = rsProduct.getInt(1);
                        sqlStorage = " SELECT m_attributesetinstance_id,qtyonhand,m_locator_id "+
                                     " FROM m_storage "+
                                     " WHERE qtyonhand != 0 and m_locator_id ="+m_locator_ID +
                                     " and m_product_id = "+ m_product_id;
                        ps = DB.prepareStatement(sqlStorage, get_TrxName());
                        rs = ps.executeQuery();
                        
                        while (rs.next()){
                                //Buscamos el ID de M_Warehouse
                            String sqlWarehouse = " SELECT m_warehouse_id "+
                                         " FROM m_locator "+
                                         " WHERE m_locator_id ="+m_locator_ID;
                            PreparedStatement psWH = DB.prepareStatement(sqlWarehouse, get_TrxName());
                            ResultSet rsWH = psWH.executeQuery();
                            if (rsWH.next()){
                                M_Warehouse_ID = rsWH.getInt(1);
                            }
                            psWH.close();
                            rsWH.close();
                            m_attributesetinstance_id = rs.getInt(1);
                            if (m_attributesetinstance_id != 0){
                                m_inventory = new MInventory (getCtx(), 0, get_TrxName());
                                m_inventory.setDescription("Ajuste de Stock con Categoria de Producto y Almacen");
                                m_inventory.setM_Warehouse_ID(M_Warehouse_ID);
                                m_inventory.setC_DocType_ID(1000150);
                                BigDecimal QtyOnHand = Env.ZERO;
                                BigDecimal QtyOnBook = rs.getBigDecimal(2);
                                if (!m_inventory.save())
                                {
                                        log.log(Level.SEVERE, "Inventory not saved");
                                        break;
                                }
                                MInventoryLine line = new MInventoryLine(m_inventory, rs.getInt(3),m_product_id, m_attributesetinstance_id,QtyOnBook,QtyOnHand);
                                if (!line.save()){
                                    log.log(Level.SEVERE, "No se pudo guardar la linea del Ajuste de inventario ID: "+ line.getM_InventoryLine_ID());
                                }
                                m_inventory.setDocStatus(m_inventory.completeIt());
                            }
                            else
                            {
                                String updateQtyOnHand = "UPDATE M_Storage SET qtyonhand = 0 WHERE m_product_id = "+m_product_id+
                                                         " and m_attributesetinstance_id = 0 and m_locator_id = "+m_locator_ID;
                                PreparedStatement pstmtUpdate = DB.prepareStatement(updateQtyOnHand, get_TrxName());
                                ResultSet rsUpdated = pstmtUpdate.executeQuery();
                                rsUpdated.close();
                                pstmtUpdate.close();
                            }
                        }
                        ps.close();
                        rs.close();
                    }
                }else if (m_product_category_ID != null && m_locator_ID == null){
                        while (rsProduct.next()){
                            m_product_id = rsProduct.getInt(1);
                            sqlStorage = " SELECT m_locator_id, m_attributesetinstance_id,qtyonhand "+
                                                     " FROM m_storage "+
                                                     " WHERE qtyonhand != 0 and m_product_id = "+ m_product_id;
                            ps = DB.prepareStatement(sqlStorage, get_TrxName());
                            rs = ps.executeQuery();
                            
                            while (rs.next()){
                                        //Buscamos el ID de M_Warehouse
                                    String sqlWarehouse = " SELECT m_warehouse_id "+
                                                             " FROM m_locator "+
                                                             " WHERE m_locator_id ="+rs.getInt(1);
                                    PreparedStatement psWH = DB.prepareStatement(sqlWarehouse, get_TrxName());
                                    ResultSet rsWH = psWH.executeQuery();
                                    if (rsWH.next()){
                                       M_Warehouse_ID = rsWH.getInt(1);
                                    }
                                    psWH.close();
                                    rsWH.close();
                                    m_attributesetinstance_id = rs.getInt(2);
                                    if (m_attributesetinstance_id != 0){
                                        m_inventory = new MInventory (getCtx(), 0, get_TrxName());
                                        m_inventory.setDescription("Ajuste de Stock con Categoria de Producto");
                                        m_inventory.setM_Warehouse_ID(M_Warehouse_ID);
                                        m_inventory.setC_DocType_ID(1000150);
                                        BigDecimal QtyOnHand = Env.ZERO;
                                        BigDecimal QtyOnBook = rs.getBigDecimal(3);
                                        if (!m_inventory.save())
                                        {
                                                        log.log(Level.SEVERE, "Inventory not saved");
                                                        break;
                                        }
                                        MInventoryLine line = new MInventoryLine(m_inventory, rs.getInt(1),m_product_id, m_attributesetinstance_id,QtyOnBook,QtyOnHand);
                                        if (!line.save()){
                                                log.log(Level.SEVERE, "No se pudo guardar la linea del Ajuste de inventario ID: "+ line.getM_InventoryLine_ID());
                                        }
                                        m_inventory.setDocStatus(m_inventory.completeIt());
                                    }
                                    else
                                    {
                                        String updateQtyOnHand = "UPDATE M_Storage SET qtyonhand = 0 WHERE m_product_id = "+m_product_id+
                                                                 " and m_attributesetinstance_id = 0 and m_locator_id = "+rs.getInt(1);
                                        PreparedStatement pstmtUpdate = DB.prepareStatement(updateQtyOnHand, get_TrxName());
                                        ResultSet rsUpdated = pstmtUpdate.executeQuery();
                                        rsUpdated.close();
                                        pstmtUpdate.close();
                                    }
                            }
                            ps.close();
                            rs.close();
                        }
                    }else if (m_product_category_ID == null && m_locator_ID != null){
                                sqlStorage = " SELECT m_product_id, m_attributesetinstance_id,qtyonhand,m_locator_id "+
                                                         " FROM m_storage "+
                                                         " WHERE m_attributesetinstance_id != 0 and qtyonhand != 0 and m_locator_id ="+m_locator_ID;
                                ps = DB.prepareStatement(sqlStorage, get_TrxName());
                                rs = ps.executeQuery();
                                //Buscamos el ID de M_Warehouse
                                String sqlWarehouse = " SELECT m_warehouse_id "+
                                                         " FROM m_locator "+
                                                         " WHERE m_locator_id ="+m_locator_ID;
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
                                        if (m_attributesetinstance_id != 0){
                                            m_inventory = new MInventory (getCtx(), 0, get_TrxName());
                                            m_inventory.setDescription("Ajuste de Stock con Almacen");
                                            m_inventory.setM_Warehouse_ID(M_Warehouse_ID);
                                            m_inventory.setC_DocType_ID(1000150);
                                            BigDecimal QtyOnHand = Env.ZERO;
                                            BigDecimal QtyOnBook = rs.getBigDecimal(3);
                                            if (!m_inventory.save())
                                            {
                                                            log.log(Level.SEVERE, "Inventory not saved");
                                                            break;
                                            }
                                            MInventoryLine line = new MInventoryLine(m_inventory, rs.getInt(4),m_product_id, m_attributesetinstance_id,QtyOnBook,QtyOnHand);
                                            if (!line.save()){
                                                    log.log(Level.SEVERE, "No se pudo guardar la linea del Ajuste de inventario ID: "+ line.getM_InventoryLine_ID());
                                            }
                                            m_inventory.setDocStatus(m_inventory.completeIt());
                                        }
                                        else
                                        {
                                            String updateQtyOnHand = "UPDATE M_Storage SET qtyonhand = 0 WHERE m_product_id = "+m_product_id+
                                                                     " and m_attributesetinstance_id = 0 and m_locator_id = "+m_locator_ID;
                                            PreparedStatement pstmtUpdate = DB.prepareStatement(updateQtyOnHand, get_TrxName());
                                            ResultSet rsUpdated = pstmtUpdate.executeQuery();
                                            rsUpdated.close();
                                            pstmtUpdate.close();
                                        }
                                }
                                ps.close();
                                rs.close();
                            }
		return "ok";
	}

    private ResultSet getProduct() throws SQLException {
        if (m_product_category_ID != null){
            String sqlCatProduct = "SELECT M_PRODUCT_ID FROM M_PRODUCT WHERE M_PRODUCT_CATEGORY_ID = " + m_product_category_ID;
            PreparedStatement ps = DB.prepareStatement(sqlCatProduct, get_TrxName());
            ResultSet rsProduct = ps.executeQuery();
            return rsProduct;
        }
        return null;
    }
}