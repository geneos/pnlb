/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.util.DB;

/**
 *
 * @author santiago
 */
public class CalloutMovementLineLocations extends CalloutEngine{
    public String locations(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		int idMov = (Integer) mTab.getValue("M_Movement_ID");
        MMovement mov = new MMovement(ctx, idMov, null);
		MDocType docType = new MDocType(ctx, mov.getC_DocType_ID(), null);
        if (docType.getName().equals("Movimiento por Aprobacion de Producto Terminado")){
            mTab.setValue("M_LocatorTo_ID", getLocation("Aprobado Famatina U."));
            mTab.setValue("M_Locator_ID", getLocation("Cuarentena Famatina U."));
        }
        else if (docType.getName().equals("Transfer a Distribuidor")){
            mTab.setValue("M_LocatorTo_ID", getLocation("Dep. Andreani U."));
            mTab.setValue("M_Locator_ID", getLocation("Aprobado Famatina U."));
        }
        else{
            mTab.setValue("M_LocatorTo_ID", null);
            mTab.setValue("M_Locator_ID", null);
        }
        return "";
	}	//	docType

    private int getLocation(String name){
        int id = 0;
        try {
            String sql = "select m_locator_id from m_locator where value like '" + name + "%'";
            PreparedStatement ps = DB.prepareStatement(sql, null);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(CalloutMovementLineLocations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
}
