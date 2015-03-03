/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class MZYNCONDITION extends X_ZYN_CONDITION {

	private MZYNFILTER filter;

	private String tableName;

	private String columnName;

	private String condition;

	private String valueCnd;

	private String negate;

	public MZYNCONDITION (Properties ctx, int ZYN_CONDITION_ID, String trxName) {
		super(ctx, ZYN_CONDITION_ID, trxName);
		innerBuildObjects();
	}
	
	public MZYNCONDITION (Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		innerBuildObjects();
	}

	public final void innerBuildObjects() {
		String sql = "SELECT cnt.value, cn.negate, tbl.ad_table_name, col.ad_column_name, cn.value, cn.zyn_filter_id "
				+ "FROM zyn_condition cn "
				+ "LEFT OUTER JOIN zyn_condition_type cnt ON (cn.zyn_condition_type_id = cnt.zyn_condition_type_id) "
				+ "LEFT OUTER JOIN zyn_model_table tbl ON (cn.zyn_model_table_id = tbl.zyn_model_table_id) "
				+ "LEFT OUTER JOIN zyn_model_column col ON (cn.zyn_model_column_id = col.zyn_model_column_id) "
				+ "WHERE cn.isactive = 'Y' AND cn.zyn_condition_id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			int zynCondId = this.getZYN_CONDITION_ID();
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, zynCondId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int zynFilterId = rs.getInt(6);
				if (zynFilterId > 0) {
					this.filter = new MZYNFILTER(Env.getCtx(), zynFilterId, null);
				} else {
					this.condition = rs.getString(1);
					this.negate = (rs.getString(2).equals("Y") ? " NOT " : "" );
					this.tableName = rs.getString(3);
					this.columnName = rs.getString(4);
					this.valueCnd = rs.getString(5);
				}
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
		if(isFilterCondition()) {
			sqlQueryBuilder.append(getFilter().getSQLQuery());
		} else {
			if (isNegation()) {
				sqlQueryBuilder.append(getNegate()).append("( ");
			}
			sqlQueryBuilder.append(getTableName()).append(".").append(getColumnName()).append(" ");
			sqlQueryBuilder.append(getCondition()).append(" ");
			if (getCondition().equals("LIKE")) {
				sqlQueryBuilder.append("'%").append(getValueCnd()).append("%'");
			} else {
				try {
					BigDecimal number = new BigDecimal(getValueCnd());
					sqlQueryBuilder.append(getValueCnd());
				} catch (NumberFormatException ex) {
					sqlQueryBuilder.append("'").append(getValueCnd()).append("'");
				}			
			}	
			if (isNegation()) {
				sqlQueryBuilder.append(") ");
			}
		}
		return sqlQueryBuilder.toString();
	}

	public String getColumnName() {
		return columnName;
	}

	public String getCondition() {
		return condition;
	}

	public MZYNFILTER getFilter() {
		return filter;
	}

	public String getTableName() {
		return tableName;
	}

	public String getValueCnd() {
		return valueCnd;
	}

	public String getNegate() {
		return negate;
	}

	public boolean isNegation() {
		return !negate.equals("");
	}

	public boolean isFilterCondition() {
		return filter != null;
	}
}
