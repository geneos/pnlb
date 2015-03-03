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
import org.compiere.model.MRequisition;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.TimeUtil;
import org.eevolution.model.MMPCProductPlanning;
import org.eevolution.model.MRequisitionLine;

/**
 * Clase que modela el proceso de actualizar las fechas de inicio programada y fecha prometida
 * de los registros mrp asociados a requisiciones.
 * @author santiago
 */
public class UpdateMRPRequisicionesCO extends SvrProcess{

    @Override
    protected void prepare() {
        return;
    }

    @Override
    protected String doIt() throws Exception {
        String sql= "select * from mpc_mrp where (docstatus='CO' or docstatus='IP') and typemrp = 'POR'";
        PreparedStatement ps = DB.prepareStatement(sql, null);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            int M_Product_ID = rs.getInt("M_Product_ID");
            System.out.println("Actualizando producto: "+M_Product_ID);
            //Obtengo el tiempo de transferencia
            BigDecimal TiempoTransferencia = BigDecimal.ZERO;
            BigDecimal TiempoEntregaPrometido = BigDecimal.ZERO;
            MRequisitionLine rl = new MRequisitionLine(getCtx(), rs.getInt("M_RequisitionLine_ID"), null);
            MRequisition r =  new MRequisition(getCtx(), rl.getM_Requisition_ID(), null);
            //Obtengo el tiempo de entrega prometido
            MMPCProductPlanning plan = getMMPCProductPlanning(M_Product_ID);
            if (plan!=null){
                TiempoEntregaPrometido = plan.getDeliveryTime_Promised();
                TiempoTransferencia = plan.getTransfertTime();
            }
            //Fecha inicio programada
            Timestamp fip = r.getDateRequired();
            //Fecha prometida
            Timestamp fp = TimeUtil.addDays(fip, TiempoEntregaPrometido.add(TiempoTransferencia).intValue());
            int MPC_MRP_ID = rs.getInt("MPC_MRP_ID");
            try{
                String sqlUpdate = "UPDATE MPC_MRP SET datestartschedule = ?, datepromised = ? where mpc_mrp_id = ?";
                PreparedStatement psUpdate = DB.prepareStatement(sqlUpdate, null);
                psUpdate.setTimestamp(1, fip);
                psUpdate.setTimestamp(2, fp);
                psUpdate.setInt(3, MPC_MRP_ID);
                psUpdate.executeUpdate();
                psUpdate.close();
            }
            catch(Exception e){
                System.out.println("No se pudo actualizar registro MRP : "+MPC_MRP_ID);
            }
            //Actualizo el registro MPC_MRP
        }
        rs.close();
        ps.close();
        return "ok";
    }

    private MMPCProductPlanning getMMPCProductPlanning(int M_Product_ID) throws SQLException{
        String sql = "select mpc_product_planning_id from mpc_product_planning where isactive = 'Y' and m_product_id = ?";
        PreparedStatement ps = DB.prepareStatement(sql, null);
        ps.setInt(1, M_Product_ID);
        ResultSet rs = ps.executeQuery();
        int id=0;
        if (rs.next()){
            id = rs.getInt(1);
            MMPCProductPlanning plan = new MMPCProductPlanning(getCtx(), id, null);
            return plan;
        }
        return null;
    }


}
