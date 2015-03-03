/*
 * MPAYMENTRET.java
 *
 * Created on January 28, 2008, 2:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *
 * @author Daniel Gini
 * 2009-09-25
 */
@SuppressWarnings("serial")
public class MProductDiscount extends X_M_Product_Discount {
    
   /**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_Measure_ID id
	 *	@param trxName trx
	 */
	public MProductDiscount (Properties ctx, int M_Product_Tax_ID, String trxName)
	{
		super (ctx, M_Product_Tax_ID, trxName);
	}	
        
	/**
	 * ORDEN:
	 * (2)
	 * PRODUCTO - CLIENTE
	 * (1) 
	 * PRODUCTO
	 */
	
	/**
	 * @param M_Product_ID
	 * @param C_BPartner_ID
	 */
	public static MProductDiscount get(int M_Product_ID, int C_BPartner_ID)
	{
		
		try {
			
			//	PRODUCTO - CLIENTE
			String sql =
				" SELECT M_Product_Discount_ID FROM M_Product_Discount " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"	AND M_PRODUCT_ID = ? AND C_BPARTNER_ID = ?";
			PreparedStatement pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,M_Product_ID);
			pstmt.setInt(4,C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return new MProductDiscount (Env.getCtx(),rs.getInt(1),null);
			
			//	PRODUCTO
			sql =
				" SELECT M_Product_Discount_ID FROM M_Product_Discount " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"	AND M_PRODUCT_ID = ? AND C_BPARTNER_ID is NULL";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,M_Product_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return new MProductDiscount (Env.getCtx(),rs.getInt(1),null);
		
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
}
