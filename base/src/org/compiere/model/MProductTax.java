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
 * 2009-09-14
 */
@SuppressWarnings("serial")
public class MProductTax extends X_M_Product_Tax {
    
   /**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_Measure_ID id
	 *	@param trxName trx
	 */
	public MProductTax (Properties ctx, int M_Product_Tax_ID, String trxName)
	{
		super (ctx, M_Product_Tax_ID, trxName);
	}	
        
	/**
	 * ORDEN:
	 * (3)
	 * PRODUCTO - COMPROBANTE - CLIENTE
	 * (2) 
	 * PRODUCTO - COMPROBANTE
	 * COMPROBANTE - CLIENTE
	 * PRODUCTO - CLIENTE
	 * (1)
	 * COMPROBANTE
	 * PRODUCTO
	 * CLIENTE
	 * (0)
	 * PREDETERMINADO
	 */
	
	/**
	 * @param M_Product_ID
	 * @param C_BPartner_ID
	 * @param C_DocType_ID
	 * @return C_Tax_ID
	 */
	public static int getCondicionIVA(int M_Product_ID, int C_BPartner_ID, int C_Jurisdiccion_ID)
	{
		try {
			
			String sql;
			PreparedStatement pstmt;
			ResultSet rs;
			
			MBPartner bpartner = new MBPartner(Env.getCtx(),C_BPartner_ID,null);
			
		//	if (bpartner.getCondicionIVACode().equals(MBPartner.CIVA_EXPORTACION))
				
		//	else
		//		if (bpartner.getCondicionIVACode().equals(MBPartner.CIVA_EXENTO))
					
			
			/* 
			 * 	CONDICION IVA
			 * 	Combinaciones: 
			 * 	(4)		CONDICIONIVA - JURISDICCION - PRODUCTO - CLIENTE
			 * 
			 * 	(3)		CONDICIONIVA - JURISDICCION - PRODUCTO
			 * 			CONDICIONIVA - JURISDICCION - CLIENTE
			 * 			CONDICIONIVA - PRODUCTO - CLIENTE
			 * 			
			 * 	(2)		CONDICIONIVA - JURISDICCION
			 * 			CONDICIONIVA - PRODUCTO
			 * 			CONDICIONIVA - CLIENTE
			 *
			 *  (1)		CONDICIONIVA
			 *  
			 */
			//CONDICIONIVA - JURISDICCION - PRODUCTO - CLIENTE
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID = ? AND C_JURISDICCION_ID = ? " +
				"	AND M_PRODUCT_ID = ? AND C_BPARTNER_ID = ?";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,bpartner.getCONDICIONIVA_ID());
			pstmt.setInt(4,C_Jurisdiccion_ID);
			pstmt.setInt(5,M_Product_ID);
			pstmt.setInt(6,C_BPartner_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			//CONDICIONIVA - JURISDICCION - PRODUCTO
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID = ? AND C_JURISDICCION_ID = ? " +
				"	AND M_PRODUCT_ID = ? AND C_BPARTNER_ID IS NULL";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,bpartner.getCONDICIONIVA_ID());
			pstmt.setInt(4,C_Jurisdiccion_ID);
			pstmt.setInt(5,M_Product_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			//CONDICIONIVA - JURISDICCION - CLIENTE
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID = ? AND C_JURISDICCION_ID = ? " +
				"	AND M_PRODUCT_ID IS NULL AND C_BPARTNER_ID = ?";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,bpartner.getCONDICIONIVA_ID());
			pstmt.setInt(4,C_Jurisdiccion_ID);
			pstmt.setInt(5,C_BPartner_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			//CONDICIONIVA - PRODUCTO - CLIENTE
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID = ? AND C_JURISDICCION_ID IS NULL " +
				"	AND M_PRODUCT_ID = ? AND C_BPARTNER_ID = ?";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,bpartner.getCONDICIONIVA_ID());
			pstmt.setInt(4,M_Product_ID);
			pstmt.setInt(5,C_BPartner_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			//CONDICIONIVA - JURISDICCION
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID = ? AND C_JURISDICCION_ID = ? " +
				"	AND M_PRODUCT_ID IS NULL AND C_BPARTNER_ID IS NULL";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,bpartner.getCONDICIONIVA_ID());
			pstmt.setInt(4,C_Jurisdiccion_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			//CONDICIONIVA - PRODUCTO
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID = ? AND C_JURISDICCION_ID IS NULL " +
				"	AND M_PRODUCT_ID = ? AND C_BPARTNER_ID IS NULL";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,bpartner.getCONDICIONIVA_ID());
			pstmt.setInt(4,M_Product_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			//CONDICIONIVA - CLIENTE
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID = ? AND C_JURISDICCION_ID IS NULL " +
				"	AND M_PRODUCT_ID IS NULL AND C_BPARTNER_ID = ?";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,bpartner.getCONDICIONIVA_ID());
			pstmt.setInt(4,C_BPartner_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			//CONDICIONIVA
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID = ? AND C_JURISDICCION_ID IS NULL " +
				"	AND M_PRODUCT_ID IS NULL AND C_BPARTNER_ID IS NULL";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,bpartner.getCONDICIONIVA_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			
			/* 
			 * 	JURISDICCION
			 * 	Combinaciones: 
			 * 	(3)		JURISDICCION - PRODUCTO - CLIENTE
			 * 
			 * 	(2)		JURISDICCION - PRODUCTO
			 * 			JURISDICCION - CLIENTE
			 * 			
			 * 	(1)		JURISDICCION
			 *  
			 */
			//JURISDICCION - PRODUCTO - CLIENTE
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID IS NULL AND C_JURISDICCION_ID = ? " +
				"	AND M_PRODUCT_ID = ? AND C_BPARTNER_ID = ?";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,C_Jurisdiccion_ID);
			pstmt.setInt(4,M_Product_ID);
			pstmt.setInt(5,C_BPartner_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			//JURISDICCION - PRODUCTO
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID IS NULL AND C_JURISDICCION_ID = ? " +
				"	AND M_PRODUCT_ID = ? AND C_BPARTNER_ID IS NULL";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,C_Jurisdiccion_ID);
			pstmt.setInt(4,M_Product_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			//JURISDICCION - CLIENTE
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID IS NULL AND C_JURISDICCION_ID = ? " +
				"	AND M_PRODUCT_ID IS NULL AND C_BPARTNER_ID = ?";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,C_Jurisdiccion_ID);
			pstmt.setInt(4,C_BPartner_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			// JURISDICCION
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID IS NULL AND C_JURISDICCION_ID = ? " +
				"	AND M_PRODUCT_ID IS NULL AND C_BPARTNER_ID IS NULL";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,C_Jurisdiccion_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			
			/* 
			 * 	PRODUCTO
			 * 	Combinaciones: 
			 * 	(2)		PRODUCTO - CLIENTE
			 * 
			 * 	(1)		PRODUCTO
			 * 
			 */
			//PRODUCTO - CLIENTE
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID IS NULL AND C_JURISDICCION_ID IS NULL " +
				"	AND M_PRODUCT_ID = ? AND C_BPARTNER_ID = ?";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,M_Product_ID);
			pstmt.setInt(4,C_BPartner_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			//PRODUCTO
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID IS NULL AND C_JURISDICCION_ID IS NULL " +
				"	AND M_PRODUCT_ID = ? AND C_BPARTNER_ID IS NULL";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,M_Product_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			
			/* 
			 * 	CLIENTE
			 * 	Combinaciones: 
			 * 	(1)		CLIENTE
			 * 
			 */
			//CLIENTE
			sql =  
				" SELECT M_Product_Tax_ID, C_Tax_ID FROM M_Product_Tax " +
				" WHERE AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'" +
				"   AND CONDICIONIVA_ID IS NULL AND C_JURISDICCION_ID IS NULL " +
				"	AND M_PRODUCT_ID IS NULL AND C_BPARTNER_ID = ?";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(3,C_BPartner_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(2);
			
			/* 
			 * 	Combinaciones: 
			 * 	(0)		PREDETERMINADA
			 */
			//PREDETERMINADO
			sql =  
				" SELECT C_Tax_ID FROM C_Tax " +
				" WHERE ISDEFAULT = 'Y' AND AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND ISACTIVE='Y'";
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Env.getAD_Org_ID(Env.getCtx()));
			pstmt.setInt(2,Env.getAD_Client_ID(Env.getCtx()));
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		
		return 0;
	}
	
}
