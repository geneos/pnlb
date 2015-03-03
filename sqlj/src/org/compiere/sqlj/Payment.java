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
package org.compiere.sqlj;

import java.math.*;
import java.sql.*;


/**
 *	SQLJ Payment related Functions
 *	
 *  @author Jorg Janke
 *  @version $Id: Payment.java,v 1.4 2005/10/08 02:04:26 jjanke Exp $
 */
public class Payment
{
	/**
	 * 	Get allocated Payment amount.
	 * 	- paymentAllocated
	 *	@param p_C_Payment_ID payment
	 *	@param p_C_Currency_ID currency
	 *	@return allocated amount
	 *	@throws SQLException
	 */
	public static BigDecimal allocated (int p_C_Payment_ID, int p_C_Currency_ID)
		throws SQLException
	{
		//	Charge - nothing available
		String sql = "SELECT C_Charge_ID "
			+ "FROM C_Payment "
			+ "WHERE C_Payment_ID=?";
		int C_Charge_ID = Compiere.getSQLValue(sql, p_C_Payment_ID);
		if (C_Charge_ID > 0)
			return Compiere.ZERO;

		int C_ConversionType_ID = 0;
		
		//	Calculate Allocated Amount
		BigDecimal allocatedAmt = getAllocatedAmt(p_C_Payment_ID, 
			p_C_Currency_ID, C_ConversionType_ID);
		
		//	Round
		return Currency.round(allocatedAmt, p_C_Currency_ID, null);
	}	//	allocated

	/**
	 * 	Get available Payment amount in payment currency
	 *	@param p_C_Payment_ID payment
	 *	@return available amt
	 *	@throws SQLException
	 */
	public static BigDecimal available (int p_C_Payment_ID)
		throws SQLException
	{
		if (p_C_Payment_ID == 0)
			return null;
		//
		int C_Currency_ID = 0;
		int C_ConversionType_ID = 0;
		BigDecimal PayAmt = null;
		int C_Charge_ID = 0;
		//
		String sql = "SELECT C_Currency_ID, C_ConversionType_ID, PayAmt, C_Charge_ID "
			+ "FROM C_Payment_v "	//	corrected for AP/AR
			+ "WHERE C_Payment_ID=?";
		PreparedStatement pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_C_Payment_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
		{
			C_Currency_ID = rs.getInt(1);
			C_ConversionType_ID = rs.getInt(2);
			PayAmt = rs.getBigDecimal(3);
			C_Charge_ID = rs.getInt(4);
		}
		rs.close();
		pstmt.close();
		//	Not found
		if (PayAmt == null)
			return null;
		//	Charge - nothing available
		if (C_Charge_ID != 0)
			return Compiere.ZERO;
		
		//	Calculate Allocated Amount
		BigDecimal allocatedAmt = getAllocatedAmt(p_C_Payment_ID, 
			C_Currency_ID, C_ConversionType_ID);
		
		BigDecimal available = PayAmt.subtract(allocatedAmt); 
		
		//	Round
		return Currency.round(available, C_Currency_ID, null);
	}	//	available

	/**
	 * 	Get Allocated Amt
	 *	@param p_C_Payment_ID payment
	 *	@param p_C_Currency_ID currency
	 *	@param p_C_ConversionType_ID conversion type
	 *	@return allocated amount in currency
	 *	@throws SQLException
	 */
	static BigDecimal getAllocatedAmt(int p_C_Payment_ID, 
		int p_C_Currency_ID, int p_C_ConversionType_ID)
		throws SQLException
	{
		//	Calculate Allocated Amount
		BigDecimal allocatedAmt = Compiere.ZERO;
		String sql = "SELECT a.AD_Client_ID, a.AD_Org_ID, al.Amount, a.C_Currency_ID, a.DateTrx "
			+ "FROM C_AllocationLine al "
			+ " INNER JOIN C_AllocationHdr a ON (al.C_AllocationHdr_ID=a.C_AllocationHdr_ID) "
			+ "WHERE al.C_Payment_ID=?"
			+ " AND a.IsActive='Y'";
		//	AND al.C_Invoice_ID IS NOT NULL;
		PreparedStatement pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_C_Payment_ID);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next())
		{
			int AD_Client_ID = rs.getInt(1);
			int AD_Org_ID = rs.getInt(2);
			BigDecimal amount = rs.getBigDecimal(3);
			int C_CurrencyFrom_ID  = rs.getInt(4);
			Timestamp DateTrx = rs.getTimestamp(5);
			//
			BigDecimal allocation = Currency.convert(amount, //.multiply(MultiplierAP),
				C_CurrencyFrom_ID, p_C_Currency_ID, DateTrx,p_C_ConversionType_ID, 
				AD_Client_ID, AD_Org_ID); 
			if (allocation != null)
				allocatedAmt = allocatedAmt.add(allocation);
		}
		rs.close();
		pstmt.close();
		//
		return allocatedAmt;
	}	//	getAllocatedAmt
	
}	//	Payment
