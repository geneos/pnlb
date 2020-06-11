/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;
import org.compiere.util.*;

/**
 *  MProductAcctConfig
 *
 *	@author Jorg Janke
 *	@version $Id: MLot.java,v 1.10 2005/10/21 15:39:39 jjanke Exp $
 */
public class MPRODUCTACCTCONFIG extends X_M_PRODUCT_ACCT_CONFIG
{
	/**	Logger					*/
	private static CLogger		s_log = CLogger.getCLogger(MPRODUCTACCTCONFIG.class);


    public static MPRODUCTACCTCONFIG loadConfigForProduct(MProduct prod, CLogger log) {
        MPRODUCTACCTCONFIG config = null;
        String sql =
                "SELECT *  from M_Product_Acct_Config"
                + " WHERE regexp_substr(value,'[A-Za-z]+', 1, 1) = regexp_substr('"+prod.getValue()+"','[A-Za-z]+', 1, 1)";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql.toString(), prod.get_TrxName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                config = new MPRODUCTACCTCONFIG( prod.getCtx(),rs, prod.get_TrxName());
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "loadConfigForProduct - " + sql, e);
        }
        return config;
    }

    
    /**
     * 	Before Save
     *	@param newRecord new
     *	@return true
     */
    protected boolean beforeSave(boolean newRecord) {
        //	Check Not Duplicated Key
       
        String sql = "SELECT 1 from m_product_acct_config where "
                + " lower(VALUE) = lower('"+getValue()+"')"
                + " and m_product_acct_config_id != " + getM_PRODUCT_ACCT_CONFIG_ID();
        PreparedStatement ps = DB.prepareStatement(sql, null);
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null,"Por favor cree una configuración contable con un prefijo distinto", "Error: ya existe una configuración para el prefijo ingresado", JOptionPane.ERROR_MESSAGE);      
                return false;
            }
        } catch (SQLException ex) {
            s_log.log(Level.SEVERE, sql, ex);
        }

        return true;
    }	//	beforeSave
    
                /**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param M_Product_Acct_Config_ID ID
	 */
	public MPRODUCTACCTCONFIG (Properties ctx, int M_Product_Acct_Config_ID, String trxName)
	{
		super (ctx, M_Product_Acct_Config_ID, trxName);

	}	//	MProductAcctConfig

	/**
	 * 	Load Constructor
	 * 	@param ctx context
	 * 	@param rs result set
	 */
	public MPRODUCTACCTCONFIG (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProductAcctConfig

}	//	MProductAcctConfig
