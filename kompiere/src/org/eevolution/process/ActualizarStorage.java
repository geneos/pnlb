/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *
 * @author santiago
 */
public class ActualizarStorage extends SvrProcess{
    
    private void getProductosErroneos(){
        //USar la cantidad de las transacciones para actualizar onHand
    }

    @Override
    protected void prepare() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String doIt() throws Exception {
        String sql = "select trx.m_product_id, "+
                     " trx.m_attributesetinstance_id, "+
                     " trx.m_locator_id, "+
                     " sum(trx.movementqty), "+
                     " s.qtyonhand "+
                     " from m_transaction trx "+
                     " join m_storage s on s.m_product_id = trx.m_product_id and s.m_attributesetinstance_id = trx.m_attributesetinstance_id and s.m_locator_id = trx.m_locator_id "+
                     " group by trx.m_product_id, trx.m_attributesetinstance_id, trx.m_locator_id,s.qtyonhand "+
                     " having s.qtyonhand <> sum(trx.movementqty) ";
        PreparedStatement ps = DB.prepareStatement(sql, null);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            //Id del producto
            int M_Product_ID = rs.getInt(1);
            BigDecimal qtyOnHand = rs.getBigDecimal(4);
            System.out.println("Actualizando Storage del producto: "+M_Product_ID+" en Qty: "+qtyOnHand);
            String sqlUpdate = "update m_storage set qtyonhand = ? where m_product_id = ? and m_attributesetinstance_id = ? and m_locator_id = ?";
            PreparedStatement psUpdate = DB.prepareStatement(sqlUpdate, null);
            ps.setBigDecimal(1, qtyOnHand);
            ps.setInt(2, M_Product_ID);
            ps.setInt(3, rs.getInt(3));
            ps.setInt(4, rs.getInt(2));
            psUpdate.executeUpdate();
        }
        return "ok";
    }

}
