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

import org.compiere.util.CLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *  MZYNMODELCOLUMN
 *	
 *
 *  @author José Fantasia - Zynnia
 *  @version $Id: MZYNMODELCOLUMN.java,v 1.0 Septiembre 2010
 */
public class MZYNMODELCOLUMN extends X_ZYN_MODEL_COLUMN {

    private String completeColumName = "";

    /**	
     * 	Default Constructor
     *	@param ctx context
     *	@param M_Product_Costing_ID id
     */
    public MZYNMODELCOLUMN(Properties ctx, int ZYN_MODEL_COLUMN_ID, String trxName) {
        super(ctx, ZYN_MODEL_COLUMN_ID, trxName);

    }

    /**
     * 	Load Constructor
     *	@param ctx context
     *	@param rs result set
     */
    public MZYNMODELCOLUMN(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public static List getColumnParameterIDs(int table) {
        String sql = "SELECT zyn_model_column_id FROM zyn_model_column "
                + "WHERE zyn_model_table_id = ? AND isparameter = 'Y' AND isactive = 'Y'";

        ArrayList<String> ret = new ArrayList<String>();
        CLogger log = CLogger.getCLogger(MZYNMODELCOLUMN.class);
        PreparedStatement pstmtobl = null;
        ResultSet rsobl = null;
        try {
            pstmtobl = DB.prepareStatement(sql, null);
            pstmtobl.setInt(1, table);
            rsobl = pstmtobl.executeQuery();
            while (rsobl.next()) {
                ret.add(rsobl.getString(1));
            }
            rsobl.close();
            pstmtobl.close();
        } catch (Exception ex) {
            CLogger.getCLogger(MZYNMODELCOLUMN.class).log(Level.SEVERE, "Error getColumnParameterIDs", ex);
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
        return ret;
    }

    public String getAD_TABLE_NAME() {
        String sql = "SELECT ad_table_name FROM zyn_model_table WHERE zyn_model_table_id = ?";
        String name = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, this.getZYN_MODEL_TABLE_ID());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                name = rs.getString(1);
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
        return name;
    }

    public String getCompleteNameForQuery() {
        if (completeColumName.equals("")) {
            if (isPARAMETER() && getZYN_PARAMETER_REF_ID() > 0) {
                MZYNMODELCOLUMN zModelColumn = new MZYNMODELCOLUMN(Env.getCtx(), getZYN_PARAMETER_REF_ID(), null);
                completeColumName = zModelColumn.getCompleteNameForQuery();
            } else {
                StringBuilder sqlQueryBuilder = new StringBuilder();

                MColumn col = new MColumn(Env.getCtx(), getAD_Column_ID(), null);
                                                
                if (col.getColumnSQL() != null && !col.getColumnSQL().equals("")){
                    sqlQueryBuilder.append(col.getColumnSQL());
                }
                else {
                    sqlQueryBuilder.append(getAD_TABLE_NAME());
                    sqlQueryBuilder.append(".").append(getAD_COLUMN_NAME());
                }
  
                completeColumName = sqlQueryBuilder.toString();
            }
        }
        return completeColumName;
    }

    public String getCompleteColumnNameReferenced() {
        String sql = "SELECT ad_table_name FROM zyn_model_table WHERE zyn_model_table_id = ?";
        String name = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, this.getZYN_MODEL_TABLE_ID());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                name = rs.getString(1);
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
        return name;
    }

    /**
     * 	Before Save
     *	@param newRecord new
     *	@return true or false
     */
    protected boolean beforeSave(boolean newRecord) {
       
        /*
         * Se actualiza nombre de la columna para posterior armado de query
         */
        String sql = "SELECT columnname FROM ad_column WHERE ad_column_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, getAD_Column_ID());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                setAD_COLUMN_NAME(rs.getString(1));
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql, e);
            e.printStackTrace();
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

        /*
         *  Valido solo si la columna es considerada como parámetro de lo
         *  contrario lo omito.
         *
         */
        if (this.isPARAMETER()) {

            /*
             *  Valida el tipo de parámetro con el campo de modo que se eviten
             *  errores por incompatibilidades.
             *
             *  Los que tengan un tipo de parámetro con identificador deben
             *  tener campos cuyo nombre tenga sufijo _ID
             *
             */
            int parameterTypeId = this.getZYN_PARAMETER_TYPE_ID();
            X_ZYN_PARAMETER_TYPE parameterType = new X_ZYN_PARAMETER_TYPE(this.getCtx(), parameterTypeId, null);

            /**
             * Es el caso en el que el parametro no exista
             */
            if (parameterTypeId <= 0) {
                log.saveError("FillMandatory", Msg.getElement(getCtx(), "ZYN_PARAMETER_TYPE_ID"));
                System.out.println("no seleccionaste parámetro");
                return false;
            }

            if (parameterType.isPARAMETERIDENTIFIER()) {
                int columnModelId = this.getZYN_MODEL_COLUMN_ID();
                X_ZYN_MODEL_COLUMN columnModel = new X_ZYN_MODEL_COLUMN(this.getCtx(), columnModelId, null);
                int columnId = columnModel.getAD_Column_ID();
                X_AD_Column column = new X_AD_Column(this.getCtx(), columnId, null);

                /*
                 *  La verificación no debe ser column.isKey() ya que cualquier campo clave
                 *  extranjera que queremos tomar como buscador _ID no sería considerado
                 *  ya que no es clave en la tabla sino clave extranjera.
                 * 
                 *  Debemos de considerar el campo AD_Reference_ID dela columna.
                 * 
                 *  Zynnia 27/05/2012
                 *  JF
                 * 
                 * 
                 */

                int refID = column.getAD_Reference_ID();

                if (refID == 30 || // 30 = Search
                        refID == 13 || // 13 = ID
                        refID == 17 || // 17 = List
                        refID == 18 || // 18 = Table
                        refID == 19) // 19 = Table Direct
                {
                    return true;
                } else {
                    return false;
                }

            }

            /*
             *  Valida el tipo de parámetro con el campo de modo que se eviten
             *  errores por incompatibilidades.
             *
             *  Valida si el campo es tipo fecha
             *
             */
            if (parameterType.getName().contains("Fecha")) {
                int columnModelId = this.getZYN_MODEL_COLUMN_ID();
                X_ZYN_MODEL_COLUMN columnModel = new X_ZYN_MODEL_COLUMN(this.getCtx(), columnModelId, null);
                int columnId = columnModel.getAD_Column_ID();
                X_AD_Column column = new X_AD_Column(this.getCtx(), columnId, null);

                int refID = column.getAD_Reference_ID();

                if (refID == 15 || // 15 = Date
                        refID == 16) // 16 = Date + Time
                {
                    return true;
                } else {
                    return false;
                }

            }

            /*
             *  Valida el tipo de parámetro con el campo de modo que se eviten
             *  errores por incompatibilidades.
             *
             *  Valida si el campo es tipo texto
             *
             */
            if (parameterType.getName().contains("Texto")) {
                int columnModelId = this.getZYN_MODEL_COLUMN_ID();
                X_ZYN_MODEL_COLUMN columnModel = new X_ZYN_MODEL_COLUMN(this.getCtx(), columnModelId, null);
                int columnId = columnModel.getAD_Column_ID();
                X_AD_Column column = new X_AD_Column(this.getCtx(), columnId, null);

                int refID = column.getAD_Reference_ID();

                if (refID == 10 || // 10 = String
                        refID == 14 || // 14 = Text
                        refID == 34 || // 34 = Memo
                        refID == 36 || // 36 = Text Long
                        refID == 17) // 17 = List
                {
                    return true;
                } else {
                    return false;
                }

            }

            /*
             *  Valida el tipo de parámetro con el campo de modo que se eviten
             *  errores por incompatibilidades.
             *
             *  Valida si el campo es tipo Lista Fija
             *
             */
            if (parameterType.getName().contains("Lista Fija")) {
                int columnModelId = this.getZYN_MODEL_COLUMN_ID();
                X_ZYN_MODEL_COLUMN columnModel = new X_ZYN_MODEL_COLUMN(this.getCtx(), columnModelId, null);
                int columnId = columnModel.getAD_Column_ID();
                X_AD_Column column = new X_AD_Column(this.getCtx(), columnId, null);

                int refID = column.getAD_Reference_ID();

                if (refID == 17) // 17 = List					
                {
                    return true;
                } else {
                    return false;
                }
            }


            /*
             *  Valida el tipo de parámetro con el campo de modo que se eviten
             *  errores por incompatibilidades.
             *
             *  Valida si el campo es tipo cantidad (numérico)
             *
             *  Verificar comportamiento ára campos enteros (Integer - Number)
             * 
             */
            if (parameterType.getName().contains("Cantidad")) {
                int columnModelId = this.getZYN_MODEL_COLUMN_ID();
                X_ZYN_MODEL_COLUMN columnModel = new X_ZYN_MODEL_COLUMN(this.getCtx(), columnModelId, null);
                int columnId = columnModel.getAD_Column_ID();
                X_AD_Column column = new X_AD_Column(this.getCtx(), columnId, null);

                int refID = column.getAD_Reference_ID();

                if (refID == 29 || // 29 = Quantity
                        refID == 22 || // 22 = Number
                        refID == 11 || // 11 = Integer
                        refID == 12 || // 12 = Ammount
                        refID == 37) // 37 = Cost + Price
                {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}	//	ZynModelColumn

