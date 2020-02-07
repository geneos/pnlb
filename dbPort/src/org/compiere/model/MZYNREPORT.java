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
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  ZYNReport
 *	
 *
 *  @author José Fantasia - Zynnia
 *  @version $Id: ZYNReport.java,v 1.0 Septiembre 2010
 */
public class MZYNREPORT extends X_ZYN_REPORT {

	/**	
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_Product_Costing_ID id
	 */
	public MZYNREPORT(Properties ctx, int ZYN_REPORT_ID, String trxName) {
		super(ctx, ZYN_REPORT_ID, trxName);
	}

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MZYNREPORT(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public ArrayList<FieldDynamicReport> getFieldsForReport() {
		String sql = "SELECT tbl.ad_table_name, col.ad_column_name, vi.issum, vi.istransp, tbl.ad_table_id, vi.name, vi.isorderby, vi.orderview, col.ad_column_id "
				+ "FROM zyn_view vi "
				+ "INNER JOIN zyn_model_column col ON (vi.zyn_model_column_id = col.zyn_model_column_id) "
				+ "INNER JOIN zyn_model_table tbl ON (vi.zyn_model_table_id = tbl.zyn_model_table_id) "
				+ "WHERE col.isactive = 'Y' AND tbl.isactive = 'Y' AND vi.zyn_report_id = ? "
                                + "ORDER BY vi.orderview ";
		ArrayList<FieldDynamicReport> ret = new ArrayList<FieldDynamicReport>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			pstmt = DB.prepareStatement(sql, null);
			int repID = this.getZYN_REPORT_ID();
			pstmt.setInt(1, repID);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				FieldDynamicReport pair = new FieldDynamicReport(rs.getBigDecimal(5), rs.getString(6), rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(7), rs.getInt(8),rs.getInt(9));
				ret.add(pair);
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

	public MZYNCONDITION getMainContidition() {
		String sql = "SELECT zyn_condition_id "
				   + "FROM zyn_condition "
				   + "WHERE isactive = 'Y' AND main = 'Y' AND zyn_report_id = ? "
				   + "AND rownum <= 1";
		MZYNCONDITION cnd = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			int reportID = this.getZYN_REPORT_ID();
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, reportID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				cnd = new MZYNCONDITION(Env.getCtx(), rs.getBigDecimal(1).intValue(), null);
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
		return cnd;
	}

	public ArrayList<FieldDynamicReport> getFieldsForOrder() {
		String sql = "SELECT tbl.ad_table_name, col.ad_column_name, vi.issum, vi.istransp, tbl.ad_table_id, vi.name, vi.isorderby, vi.orderview, col.AD_Column_ID "
				+ "FROM zyn_view vi "
				+ "INNER JOIN zyn_model_column col ON (vi.zyn_model_column_id = col.zyn_model_column_id) "
				+ "INNER JOIN zyn_model_table tbl ON (vi.zyn_model_table_id = tbl.zyn_model_table_id) "
				+ "WHERE col.isactive = 'Y' AND tbl.isactive = 'Y' AND vi.isorderby = 'Y' AND vi.zyn_report_id = ? "
				+ "ORDER BY vi.orderview ASC";
		ArrayList<FieldDynamicReport> ret = new ArrayList<FieldDynamicReport>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, this.getZYN_REPORT_ID());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				FieldDynamicReport pair = new FieldDynamicReport(rs.getBigDecimal(5), rs.getString(6), rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(7), rs.getInt(8),rs.getInt(9));
				ret.add(pair);
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

        /*
         *  Verifico si alguno de los campos es un lik a un subreporte.
         */
        
        public boolean hasSubreport() {

            boolean flag = false;

            String sql = "SELECT * "
                            + "FROM zyn_view "
                            + "WHERE zyn_report_id = ? AND VIEWTYPE = 'L'";

            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {

                    pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, this.getZYN_REPORT_ID());
                    rs = pstmt.executeQuery();

                    while (rs.next()) {
                            flag = true;
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



            return flag;
        }



        public ArrayList<MZYNVIEWCALC> getColumnsCalc() {


                String sql = "SELECT ZYN_VIEW_CALC_ID "
				+ "FROM ZYN_VIEW_CALC vi "
				+ "WHERE ZYN_REPORT_ID = ?";

                ArrayList<MZYNVIEWCALC> ret = new ArrayList<MZYNVIEWCALC>();

                PreparedStatement pstmt = null;

                ResultSet rs = null;

                try {

			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, this.getZYN_REPORT_ID());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				MZYNVIEWCALC col = new MZYNVIEWCALC(this.getCtx(),rs.getInt("ZYN_VIEW_CALC_ID"),null);
				ret.add(col);
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


}	//	ZYNReport
