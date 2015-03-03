/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
/*
 *  21/03/2013 Maria Jesus
 *  Modificacion de org.compiere.model.MOrder a 
 *  org.eevolution.model.MOrder para que luego del save() haga el 
 *  afterSave() que es el que modifica el docStatus en la tabla
 *  MPC_MRP y de este modo no aparezcan en Revisión a Detalle.
 */
import org.eevolution.model.MOrder;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;


/**
 * 
 * @author santiago
 */
public class CerrarOrdenesCompra extends SvrProcess {

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
			if (!orden.getDocStatus().equals(orden.STATUS_Closed))
				if (orden.closeIt()) {
					System.out.println("Cerrando orden: "
							+ orden.getDocumentNo());
					i++;
				} else
					System.out.println("No se pudo cerrar la Orden: "
							+ orden.getDocumentNo());
			else
				System.out.println("La Orden "
						+ orden.getDocumentNo() + "ya se encuentra en estado cerrado");
		}
		System.out.println("Cantidad de Ordenes: " + i);
		rs.close();
		ps.close();
		return "ok";

	}

	private String getSQLOrdenes() {
		if (from != null && to != null)
			return "select c_order_id from c_order where issotrx = 'N' and dateordered between ? and ?";
		else if (from != null)
			return "select c_order_id from c_order where issotrx = 'N' and dateordered >= ?";
		else if (to != null)
			return "select c_order_id from c_order where issotrx = 'N' and dateordered <= ?";
		else
			return "select c_order_id from c_order where issotrx = 'N'";
	}

}
