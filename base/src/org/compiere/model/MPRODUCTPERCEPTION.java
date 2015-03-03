/*
 * MPAYMENTRET.java
 *
 * Created on January 28, 2008, 2:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.compiere.model;

import java.math.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *
 * @author Daniel Gini
 * 2009-08-28
 */
@SuppressWarnings("serial")
public class MPRODUCTPERCEPTION extends X_M_PRODUCT_PERCEPTION {
    
   /**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_Measure_ID id
	 *	@param trxName trx
	 */
	public MPRODUCTPERCEPTION (Properties ctx, int M_Product_Perception_ID, String trxName)
	{
		super (ctx, M_Product_Perception_ID, trxName);
	}	
        
	public static BigDecimal getAlicuotaMonotributo(int M_Product_ID, int C_Jurisdiccion_ID)
	{
		try {
			String sql =  
				" SELECT M_PRODUCT_PERCEPTION_ID FROM M_Product_Perception " +
				" WHERE M_Product_ID = ? AND C_JURISDICCION_ID = ? ";
			PreparedStatement pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,M_Product_ID);
			pstmt.setInt(2,C_Jurisdiccion_ID);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				MPRODUCTPERCEPTION prodPercep = new MPRODUCTPERCEPTION(Env.getCtx(),rs.getInt(1),null);
				return prodPercep.getAlicuotaMT();
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	public static BigDecimal getAlicuotaRespInscripto(int M_Product_ID, int C_Jurisdiccion_ID)
	{
		try {
			String sql =  
				" SELECT M_PRODUCT_PERCEPTION_ID FROM M_Product_Perception " +
				" WHERE M_Product_ID = ? AND C_JURISDICCION_ID = ? ";
			PreparedStatement pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,M_Product_ID);
			pstmt.setInt(2,C_Jurisdiccion_ID);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				MPRODUCTPERCEPTION prodPercep = new MPRODUCTPERCEPTION(Env.getCtx(),rs.getInt(1),null);
				return prodPercep.getAlicuotaRI();
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
}
