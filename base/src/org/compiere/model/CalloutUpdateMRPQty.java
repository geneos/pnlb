/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;


import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.DB;

/**
 *
 * @author santiago
 */
public class CalloutUpdateMRPQty extends CalloutEngine {

public String update(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){
    //Obtengo los registros MRP con qty <> a cantidad - cantidad entregada de las OC
    System.out.println("Actualizando MRP para lineas de compra...");
    String sql = "select mrp.mpc_mrp_id, ol.qtyentered-ol.qtydelivered from mpc_mrp mrp"+
                 " join c_orderline ol on ol.c_orderline_id = mrp.c_orderline_id"+
                 " where (case when ol.qtyentered-ol.qtydelivered > 0 then ol.qtyentered-ol.qtydelivered else 0 end) <> mrp.qty and mrp.c_orderline_id is not null";
    System.out.println("Actualizando MRP para lineas de formulas...");
    actualizarMRPQty(sql);
    sql = "select mrp.mpc_mrp_id, ol.qtyrequiered-ol.qtydelivered from mpc_mrp mrp"+
          " join mpc_order_bomline ol on ol.mpc_order_bomline_id = mrp.mpc_order_bomline_id"+
          " where (case when ol.qtyrequiered-ol.qtydelivered > 0 then ol.qtyrequiered-ol.qtydelivered else 0 end) <> mrp.qty and mrp.mpc_order_bomline_id is not null";
    System.out.println("Actualizando MRP para ordenes de producción...");
    actualizarMRPQty(sql);
    sql = "select mrp.mpc_mrp_id, o.qtyentered-o.qtydelivered  from mpc_mrp mrp "+
          " join mpc_order o on o.mpc_order_id = mrp.mpc_order_id "+
          " where (case when o.qtyentered-o.qtydelivered < 0 then 0 else o.qtyentered-o.qtydelivered end) <> mrp.qty and mrp.mpc_order_id is not null and mrp.mpc_order_bomline_id is null ";

    return "";
    
}
    private void actualizarMRPQty(String sql){
        try{
            PreparedStatement ps = DB.prepareStatement(sql, null);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                System.out.println("Actualizando el registro MRP: "+rs.getInt(1));
                String sqlUpdate = "update mpc_mrp set qty = ? where mpc_mrp_id = ?";
                PreparedStatement psUpdate = DB.prepareStatement(sqlUpdate, null);
                BigDecimal diff = rs.getBigDecimal(2);
                diff = diff.signum()==-1 ? BigDecimal.ZERO : diff;
                psUpdate.setBigDecimal(1, diff);
                psUpdate.setInt(2 , rs.getInt(1));
                psUpdate.executeUpdate();
                psUpdate.close();
            }
            rs.close();
            ps.close();
        }
        catch(Exception e){
            System.out.println("Error al actualizar el registro MRP");
        }
    }
}
