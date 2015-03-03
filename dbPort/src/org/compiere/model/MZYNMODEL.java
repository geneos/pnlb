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

//import org.compiere.model.X_MPC_Order;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  ZYNModel
 *	
 *
 *  @author José Fantasia - Zynnia
 *  @version $Id: ZYNModel.java,v 1.0 Septiembre 2010
 */
public class MZYNMODEL extends X_ZYN_MODEL {

	/**	
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_Product_Costing_ID id
	 */
	public MZYNMODEL(Properties ctx, int ZYN_MODEL_ID, String trxName) {
		super(ctx, ZYN_MODEL_ID, trxName);

	}

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MZYNMODEL(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/*
	 *  Este método obtiene las columnas que son parámetros de un modelo
	 *
	 *  @author José Fantasia - Zynnia
	 *  @version $Id: ZYNModel.java,v 1.0 Septiembre 2010
	 */
	public List<String> getParameters() {

		ArrayList<String> listRet = new ArrayList<String>();
		for (String tableID : getIdTablesWithParameters()) {
			int id = Integer.parseInt(tableID);
//			System.out.println(id);
			listRet.addAll(MZYNMODELCOLUMN.getColumnParameterIDs(id));
		}
		return listRet;
	}

	public String getNameOfMainTable() {
		String sql = "SELECT ad_table_name "
				   + "FROM zyn_model_table "
				   + "WHERE isactive = 'Y' AND main = 'Y' AND zyn_model_id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String tableName = "";
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, this.getZYN_MODEL_ID());
			//
			rs = pstmt.executeQuery();
			if (rs.next()) {
				tableName = rs.getString(1);
			} else {
				log.log(Level.SEVERE, "Column Not Found - AD_Column_ID={0}", this.getZYN_MODEL_ID());
			}
		} catch (SQLException ex) {
			log.log(Level.SEVERE, "create", ex);
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
		return tableName;
	}

	private ArrayList<Integer> getRelationsIDs() {
		String sql = "SELECT zyn_model_fk_id FROM zyn_model_fk WHERE zyn_model_id = ? AND ISACTIVE = 'Y' ORDER BY zyn_model_fk_id asc";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Integer> ret = new ArrayList<Integer>();
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, this.getZYN_MODEL_ID());
			//
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ret.add(rs.getBigDecimal(1).intValue());
			} 
		} catch (SQLException ex) {
			log.log(Level.SEVERE, "create", ex);
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

	public ArrayList<MZYNMODELFK> getRelations() {
		ArrayList<MZYNMODELFK> relations = new ArrayList<MZYNMODELFK>();
		for(Integer relationID : getRelationsIDs()) {
			MZYNMODELFK relation = new MZYNMODELFK(Env.getCtx(), relationID, null);
			relations.add(relation);
		}
		return relations;
	}

	public List<String> getIdTablesWithParameters() {
		String sql = "SELECT DISTINCT(mt.zyn_model_table_id) FROM zyn_model_table mt "
				+ "INNER JOIN zyn_model_column mc ON (mt.zyn_model_table_id = mc.zyn_model_table_id) "
				+ "WHERE mt.isactive = 'Y' AND mc.isactive = 'Y' AND mc.isparameter = 'Y' AND mt.zyn_model_id = ?";
		ArrayList<String> tablesIds = new ArrayList<String>();
		PreparedStatement pstmtobl = null;
		ResultSet rsobl = null;
		try {

			pstmtobl = DB.prepareStatement(sql, null);
			pstmtobl.setInt(1, this.getZYN_MODEL_ID());
			rsobl = pstmtobl.executeQuery();
			while (rsobl.next()) {
				tablesIds.add(rsobl.getString(1));
			}
			pstmtobl.close();
			rsobl.close();

		} catch (Exception ex) {
			log.log(Level.SEVERE, "error getIdTablesWithParameters ", ex);
		} finally {
			if (rsobl != null) {
				try {
					rsobl.close();
				} catch (SQLException ex) {
					log.log(Level.SEVERE, null, ex);
				}
			}

			if (pstmtobl != null) {
				try {
					pstmtobl.close();
				} catch (SQLException ex) {
					log.log(Level.SEVERE, null, ex);
				}
				pstmtobl = null;
			}
		}
		return tablesIds;
	}

}	//	ZYNModel

