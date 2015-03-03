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
import java.util.logging.Level;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.wf.MWFNode;
import org.compiere.wf.MWFNodeNext;
import org.compiere.wf.MWorkflow;
import org.eevolution.model.MMPCOrder;
import org.eevolution.model.MMPCOrderNode;
import org.eevolution.model.MMPCOrderNodeNext;
import org.eevolution.model.MMPCOrderWorkflow;

/**
 *
 * @author santiago
 */
public class ActualizarTransicionesNodos extends SvrProcess{

    @Override
    protected void prepare() {
        
    }

    @Override
    protected String doIt() throws Exception {
        String sqlOrders = "select distinct o.documentno, n.mpc_order_id from mpc_order_nodenext n "+
                            " join mpc_order o on o.mpc_order_id = n.mpc_order_id "+
                            " where mpc_order_next_id = 0 and o.docstatus in ('CO','AP','DR') order by n.mpc_order_id";
        PreparedStatement psOrders = DB.prepareStatement(sqlOrders, get_TrxName());
        ResultSet rsOrders = psOrders.executeQuery();
        while (rsOrders.next()){
            System.out.println("Actualizando Nodos de la Orden: "+rsOrders.getInt(1));
            actualizarTransiciones(rsOrders.getInt(2));
        }
        rsOrders.close();
        psOrders.close();
        return "ok";
    }

    private void actualizarTransiciones(int MPC_Order_ID) throws SQLException{
        //Obtengo los transiciones a corregir
        String sql = "select mpc_order_nodenext_id, mpc_order_node_id from mpc_order_nodenext where mpc_order_id = "+MPC_Order_ID;
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            //Transicion a corregir
            MMPCOrderNodeNext next = new MMPCOrderNodeNext(getCtx(), rs.getInt(1), get_TrxName());
            //Obtengo el nodo para saber de que workflow estoy tratando
            MMPCOrderNode nodo = new MMPCOrderNode(getCtx(), next.getMPC_Order_Node_ID(), get_TrxName());
            String sqlNext = "select mpc_order_node_id from mpc_order_node where ad_wf_node_id = "+next.getAD_WF_Next_ID()+" and mpc_order_workflow_id = "+nodo.getMPC_Order_Workflow_ID();
            PreparedStatement psNext = DB.prepareStatement(sqlNext, get_TrxName());
            ResultSet rsNext = psNext.executeQuery();
            if (rsNext.next()){
                int MPC_ORDER_NEXT_ID = rsNext.getInt(1);
                next.setMPC_Order_Next_ID(MPC_ORDER_NEXT_ID);
                next.save();
            }
            rsNext.close();
            psNext.close();
        }
        rs.close();
        ps.close();
    }

    private int getMPCOrderWorkflow(int MPC_Order_ID) throws SQLException{
        String sql = "select mpc_order_workflow_id from mpc_order_workflow where mpc_order_id = "+MPC_Order_ID;
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        ResultSet rs = ps.executeQuery();
        int id = -1;
        if (rs.next()){
            id = rs.getInt(1);
        }
        rs.close();
        ps.close();
        return id;
    }
}
