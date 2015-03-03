/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import org.compiere.model.MOrder;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *
 * @author Galleguita
 */
public class AbrirOrdenesDeCompraEspecificas extends SvrProcess {

        private String valueOC = "";

	@Override
	protected void prepare() {
                ProcessInfoParameter[] para = getParameter();
                valueOC = para[0].getParameterName();
	}

	@Override
	protected String doIt() throws Exception {
		String Ordenes[] = {valueOC};
		for (int i = 0; i<Ordenes.length;i++){
                        String sqlOrden = "select C_order_ID From C_Order Where documentno like '"+Ordenes[i]+"'";
                        PreparedStatement ps = DB.prepareStatement(sqlOrden, get_TrxName());
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()){
                            MOrder orden = new MOrder(getCtx(), rs.getInt(1), get_TrxName());
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
                               System.out.println("La Orden no existe: "+ Ordenes[i]);
		}
		return "ok";

	}

}