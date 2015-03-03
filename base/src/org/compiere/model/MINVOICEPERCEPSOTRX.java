/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import org.compiere.util.*;
/**
 * @author daniel
 */
@SuppressWarnings("serial")
public class MINVOICEPERCEPSOTRX extends X_C_INVOICEPERCEP_SOTRX{
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_Action_Permission_ID id
	 */
	public MINVOICEPERCEPSOTRX (Properties ctx, int C_INVOICEPERCEP_SOTRX_ID, String trxName)
	{
		super (ctx, C_INVOICEPERCEP_SOTRX_ID, trxName);
	}	//	MActionPermission

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MINVOICEPERCEPSOTRX (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MActionPermission
	
	public static BigDecimal getPercepcionesAmount(int C_Invoice_ID)
	{
		String sql = "SELECT COALESCE(SUM(MONTO),0) " +
					" FROM C_INVOICEPERCEP_SOTRX " +
					" WHERE C_Invoice_ID = ?" +
					" GROUP BY C_Invoice_ID";
		try 
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setLong(1, C_Invoice_ID);
			
			ResultSet rs = pstmt.executeQuery();  
			
			if (rs.next())
				return rs.getBigDecimal(1);
		
		}
		catch(SQLException e){
			e.printStackTrace();
			return BigDecimal.ZERO;
		}
		
		return BigDecimal.ZERO;
	}
	
}
