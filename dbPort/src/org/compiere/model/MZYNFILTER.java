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
import org.compiere.model.X_ZYN_FILTER;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class MZYNFILTER extends X_ZYN_FILTER {

	private MZYNCONDITION rightCondition;

	private MZYNCONDITION leftCondition;

	private String operator;

	public MZYNFILTER(Properties ctx, int ZYN_FILTER_ID, String trxName) {
		super (ctx, ZYN_FILTER_ID, trxName);
		innerBuildObjects();
	}

	public MZYNFILTER(Properties ctx, ResultSet rs, String trxName) {
		super (ctx, rs, trxName);
		innerBuildObjects();
	}

	public final void innerBuildObjects() {
		String sql = "SELECT value "
				   + "FROM zyn_filter_type "
				   + "WHERE isactive = 'Y' AND zyn_filter_type_id = ? ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, this.getZYN_FILTER_TYPE_ID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.leftCondition = new MZYNCONDITION(Env.getCtx(), this.getZYN_CONDITION1_ID(), null);
				this.rightCondition = new MZYNCONDITION(Env.getCtx(), this.getZYN_CONDITION2_ID(), null);
				this.operator = rs.getString(1);
			}
		} catch (Exception ex) {
			log.log(Level.SEVERE, null, ex);

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
		}
	}

	public String getSQLQuery() {
		StringBuilder sqlQueryBuilder = new StringBuilder();
		sqlQueryBuilder.append(" (").append(getLeftCondition().getSQLQuery()).append(" ");
		sqlQueryBuilder.append(getOperator()).append(getRightCondition().getSQLQuery()).append(") ");
		return sqlQueryBuilder.toString();
	}

	public MZYNCONDITION getLeftCondition() {
		return leftCondition;
	}

	public String getOperator() {
		return operator;
	}

	public MZYNCONDITION getRightCondition() {
		return rightCondition;
	}
}
