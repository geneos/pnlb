/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import org.eevolution.model.MOrder;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *
 * @author Galleguita
 */
public class AbrirOrdenesCompra extends SvrProcess {

	private Timestamp from = null;

	private Timestamp to = null;

	@Override
	protected void prepare() {
		ProcessInfoParameter parametros[] = getParameter();
		for (int i = 0; i < parametros.length; i++) {
			if (parametros[i].getParameterName().equals("DateOrdered")) {
				from = (Timestamp) parametros[i].getParameter();
				to = (Timestamp) parametros[i].getParameter_To();
			}

		}
	}

	@Override
	protected String doIt() throws Exception {
		String sqlOrdenes = getSQLOrdenes();
		PreparedStatement ps = DB.prepareStatement(sqlOrdenes, get_TrxName());
		if (from != null) {
			ps.setTimestamp(1, from);
			if (to != null)
				ps.setTimestamp(2, to);
		} else if (to != null)
			ps.setTimestamp(1, to);
		ResultSet rs = ps.executeQuery();
		int i = 0;
		while (rs.next()) {
			MOrder orden = new MOrder(getCtx(), rs.getInt(1), get_TrxName());
                        if (noProcesar(orden.getDocumentNo())){
                            if (orden.getDocStatus().equals(orden.STATUS_Closed))
                                    if (orden.uncloseIt()) {
                                            System.out.println("Abriendo orden: "
                                                            + orden.getDocumentNo());
                                            i++;
                                    } else
                                            System.out.println("No se pudo revertir el cierre la Orden: "
                                                            + orden.getDocumentNo());
                            else
                                    System.out.println("La Orden "
                                                    + orden.getDocumentNo() + "ya se encuentra en estado completo");
                        }
                        else
                        {
                            if(!orden.revertirQtyLostSales()){
                                    System.out.println("No se pudo revertir las cantidades de la Orden: "+ orden.getDocumentNo());
                            }
                            else{
                                    System.out.println("La Orden "+ orden.getDocumentNo() + "ya se encuentra en estado borrador");
                            }
                                
                        }
		}
		rs.close();
		ps.close();
		return "ok";

	}

	private String getSQLOrdenes() {
		if (from != null && to != null)
			return "select c_order_id from c_order where issotrx = 'N' and dateordered between ? and ? and docstatus = 'CL' ";
		else if (from != null)
			return "select c_order_id from c_order where issotrx = 'N' and dateordered >= ? and docstatus = 'CL' ";
		else if (to != null)
			return "select c_order_id from c_order where issotrx = 'N' and dateordered <= ? and docstatus = 'CL' ";
		else
			return "select c_order_id from c_order where issotrx = 'N'";
	}
        
        private boolean noProcesar(String DocumentNo) {
            String ordenes[] = {"26287","26286","26289","26278","26284","26274","26272","26275","26188","26179","26294","26290","26291","26292","26288","26281"};
            for (int i = 0; i<ordenes.length;i++){
                if (ordenes[i].equals(DocumentNo)){
                    return false;
                }
            }
            return true;
	}

}