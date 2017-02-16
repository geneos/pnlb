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
import org.compiere.model.MZYNMODELTABLE;
import org.compiere.util.DB;

public class CalloutZynNames extends CalloutEngine {

    /*
     * Esta actualizacion ahora se realiza al guardar un registro del tipo MZYNMODELTABLE
     * MZYNMODELTABLE.beforeSave();
     */
    @Deprecated
    public String tableName(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {

        if (isCalloutActive() || value == null) {
            return "";
        }

        Integer zynModelTableID = (Integer) value;
        if (zynModelTableID == null || zynModelTableID.intValue() == 0) {
            return "";
        }
        setCalloutActive(true);
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT tablename FROM ad_table WHERE ad_table_id = ?";
        try {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, zynModelTableID.intValue());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                mTab.setValue("AD_TABLE_NAME", rs.getString(1));
            }
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

     /*
     * Esta actualizacion ahora se realiza al guardar un registro del tipo MZYNMODELTABLE
     * MZYNMODELCOLUMN.beforeSave();
     */
    @Deprecated
    public String columnName(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {

        if (isCalloutActive() || value == null) {
            return "";
        }

        Integer zynColumnTableID = (Integer) value;
        if (zynColumnTableID == null || zynColumnTableID.intValue() == 0) {
            return "";
        }
        setCalloutActive(true);

        String sql = "SELECT columnname FROM ad_column WHERE ad_column_id = ?";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, zynColumnTableID.intValue());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                mTab.setValue("AD_COLUMN_NAME", rs.getString(1));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql, e);
            return e.getLocalizedMessage();
        } finally {
            setCalloutActive(false);
        }
        return "";
    }
}
