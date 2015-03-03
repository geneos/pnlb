/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;



import java.util.Properties;
import org.compiere.util.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *  ZynModelTable
 *	
 *
 *  @author José Fantasia - Zynnia
 *  @version $Id: ZynModelTable.java,v 1.0 Septiembre 2010
 */
public class MZYNMODELTABLE extends X_ZYN_MODEL_TABLE {

	/**	
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_Product_Costing_ID id
	 */
	public MZYNMODELTABLE(Properties ctx, int ZYN_MODEL_TABLE_ID, String trxName) {
		super(ctx, ZYN_MODEL_TABLE_ID, trxName);

	}

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MZYNMODELTABLE(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	
	protected boolean beforeSave(boolean newRecord) {
		//	New Address invalidates verification
		/*
        boolean newStateMain = this.isMAIN();
		int zynModelID = this.getZYN_MODEL_ID();
		int zynModelTableID = this.getZYN_MODEL_TABLE_ID();

		log.log(Level.INFO, "zynModelID {0} zynModelTableID {1}", new Object[]{zynModelID, zynModelTableID});
		String sql = "SELECT COUNT(zyn_model_table_id) FROM zyn_model_table "
				   + "WHERE main = 'Y' AND zyn_model_id = ? AND zyn_model_table_id <> ?";
		int cant = this.getCantTablasWith(sql, zynModelID, zynModelTableID);

		if (newStateMain && cant > 0) {
			if (ADialog.ask(0, null, "WarningOnlyOneMain")) {
				sql = "UPDATE zyn_model_table SET main = 'N' WHERE zyn_model_id = ?";
				Object[] params = {zynModelID};
				DB.executeUpdate(sql, params, false, null);
				return true;
			}
			return false;
		} else if (!newStateMain && cant <= 0) {
			//log.saveError("ErrorSaveMain", Msg.getElement(getCtx(), "FieldLength"));
			log.saveError("ErrorSaveMain", "");
			return false;
		}
        */
		return true;
	}	//	beforeSave

    
	private int getCantTablasWith(String sql, int zynModelID, int zynModelTableID) {
		log.log(Level.INFO, "sql {0}", sql);
		int returnValue = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, zynModelID);
			pstmt.setInt(2, zynModelTableID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				returnValue = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
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
		return returnValue;
	}

	public ArrayList<String> getKeyFields() {
		String sql = "SELECT col.name "
				   + "FROM ad_column col "
				   + "WHERE col.iskey = 'Y' AND col.ad_table_id = ?";
		ArrayList<String> ret = new ArrayList<String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, this.getZYN_MODEL_TABLE_ID());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ret.add(rs.getString(1));
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
		return ret;
	}
}	//	ZynModelTable

