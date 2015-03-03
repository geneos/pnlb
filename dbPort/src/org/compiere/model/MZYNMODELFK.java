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
import org.compiere.util.DB;

/**
 *
 * @author Zynnia Software
 */
public class MZYNMODELFK extends X_ZYN_MODEL_FK {
	private String relationName;
	private String firstTableName;
	private String firstColumName;
	private String secondTableName;
	private String secondColumName;

	public MZYNMODELFK(Properties ctx, int ZYN_MODEL_FK_ID, String trxName) {
		super(ctx, ZYN_MODEL_FK_ID, trxName);
		generateTextReferences();
	}

	/** Load Constructor */
	public MZYNMODELFK(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		generateTextReferences();
	}

	private void generateTextReferences() {
		String sql = "SELECT relt.value, tbl1.ad_table_name, col1.ad_column_name, tbl2.ad_table_name, col2.ad_column_name "
				   + "FROM zyn_model_fk rel "
				   + "INNER JOIN zyn_model_fk_type relt ON (rel.zyn_model_fk_type_id = relt.zyn_model_fk_type_id) "
				   + "INNER JOIN zyn_model_table tbl1 ON(rel.zyn_model_table1_id = tbl1.zyn_model_table_id) "
				   + "INNER JOIN zyn_model_column col1 ON(rel.zyn_model_column1_id = col1.zyn_model_column_id) "
				   + "INNER JOIN zyn_model_table tbl2 ON(rel.zyn_model_table2_id = tbl2.zyn_model_table_id) "
				   + "INNER JOIN zyn_model_column  col2 ON (rel.zyn_model_column2_id = col2.zyn_model_column_id) "
				   + "WHERE rel.zyn_model_fk_id = ?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, this.getZYN_MODEL_FK_ID());
			//
			rs = pstmt.executeQuery();
			if (rs.next()) {
				relationName = rs.getString(1);
				firstTableName = rs.getString(2);
				firstColumName = rs.getString(3);
				secondTableName = rs.getString(4);
				secondColumName = rs.getString(5);
			} else {
				log.log(Level.SEVERE, "Relation Not Found - ZYN_MODEL_FK_ID={0}", this.getZYN_MODEL_FK_ID());
			}
		} catch (SQLException ex) {
			log.log(Level.SEVERE, "generateTextReferences", ex);
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

	public String getFirstColumName() {
		return firstColumName;
	}

	public String getFirstTableName() {
		return firstTableName;
	}

	public String getRelationName() {
		return relationName;
	}

	public String getSecondColumName() {
		return secondColumName;
	}

	public String getSecondTableName() {
		return secondTableName;
	}

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

    protected boolean beforeSave (boolean newRecord)
	{

        /*
         *  Validar que las tablas del JOIN sean diferentes y
         *  que las columnas del JOIN sean del mismo tipo.
         *
         */

        if(this.getZYN_MODEL_TABLE1_ID() == this.getZYN_MODEL_TABLE2_ID())
            return false;

        int columnId1 = this.getZYN_MODEL_COLUMN1_ID();
        MZYNMODELCOLUMN modcol1 = new MZYNMODELCOLUMN(this.getCtx(), columnId1, null);
        X_AD_Column column1 = new X_AD_Column(this.getCtx(), modcol1.getAD_Column_ID(), null);

        int columnId2 = this.getZYN_MODEL_COLUMN2_ID();
        MZYNMODELCOLUMN modcol2 = new MZYNMODELCOLUMN(this.getCtx(), columnId2, null);
        X_AD_Column column2 = new X_AD_Column(this.getCtx(), modcol2.getAD_Column_ID(), null);
	/*
	* 2/05/2013 Maria Jesus
	* Agregamos para que valide tambien que un ID se puede referenciar con un Product Attribute. 
	*/
        /*
         *  21/10 Maria Jesus
         *  Agregamos para que valide tambien que un tipo String puede referenciar con un tipo List
         *
         */
        if(column1.getAD_Reference_ID() == column2.getAD_Reference_ID())
            return true;
        else if((column1.getAD_Reference_ID() == 13 || column1.getAD_Reference_ID() == 18 || column1.getAD_Reference_ID() == 19 || column1.getAD_Reference_ID() == 30 || column1.getAD_Reference_ID() == 35 || column1.getAD_Reference_ID() == 17) && (column2.getAD_Reference_ID() == 13 || column2.getAD_Reference_ID() == 18 || column2.getAD_Reference_ID() == 19 || column2.getAD_Reference_ID() == 30 || column2.getAD_Reference_ID() == 35 || column2.getAD_Reference_ID() == 17  || column2.getAD_Reference_ID() == 10))
            return true;


        return false;

    }

}
