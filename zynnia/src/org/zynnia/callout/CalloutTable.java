/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.callout;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.model.CalloutEngine;
import org.compiere.model.MField;
import org.compiere.model.MTab;
import org.compiere.util.DB;

/**
 *
 * @author Zynnia Software
 */

public class CalloutTable extends CalloutEngine {

	public String isMain(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {

		if (isCalloutActive() || value == null) {
			return "";
		}
		
        String modelID = mTab.getValue("ZYN_MODEL_ID").toString();

        setCalloutActive(true);
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT MAIN FROM ZYN_MODEL_TABLE WHERE ZYN_MODEL_ID = " + modelID;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
                mField.setValue(false, false);
				return "Ya existe una tabla como Principal";
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					log.log(Level.SEVERE, null, ex);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException ex) {
					log.log(Level.SEVERE, null, ex);
				}
				pstmt = null;
			}
			setCalloutActive(false);
		}
		return "";
	}

}
