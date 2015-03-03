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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Payment Selection Callouts
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutPaySelection.java,v 1.10 2005/11/06 01:17:27 jjanke Exp $
 */
public class CalloutPaySelection extends CalloutEngine
{
	/**
	 *	Payment Selection Line - Payment Amount.
	 *		- called from C_PaySelectionLine.PayAmt
	 *		- update DifferenceAmt
	 */
	public String payAmt (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		//	get invoice info
		Integer ii = (Integer)mTab.getValue("C_Invoice_ID");
		if (ii == null)
			return "";
		int C_Invoice_ID = ii.intValue();
		if (C_Invoice_ID == 0)
			return "";
		//
		BigDecimal OpenAmt = (BigDecimal)mTab.getValue("OpenAmt");
		BigDecimal PayAmt = (BigDecimal)mTab.getValue("PayAmt");
		BigDecimal DiscountAmt = (BigDecimal)mTab.getValue("DiscountAmt");
		setCalloutActive(true);
		BigDecimal DifferenceAmt = OpenAmt.subtract(PayAmt).subtract(DiscountAmt);
		log.fine(" - OpenAmt=" + OpenAmt + " - PayAmt=" + PayAmt
			+ ", Discount=" + DiscountAmt + ", Difference=" + DifferenceAmt);
		
		mTab.setValue("DifferenceAmt", DifferenceAmt);

		setCalloutActive(false);
		return "";
	}	//	PaySel_PayAmt

	/**
	 *	Payment Selection Line - Invoice.
	 *		- called from C_PaySelectionLine.C_Invoice_ID
	 *		- update PayAmt & DifferenceAmt
	 */
	public String invoice (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		//	get value
		int C_Invoice_ID = ((Integer)value).intValue();
		if (C_Invoice_ID == 0)
			return "";
		int C_BankAccount_ID = Env.getContextAsInt(ctx, WindowNo, "C_BankAccount_ID");
		Timestamp PayDate = Env.getContextAsDate(ctx, "PayDate");
		if (PayDate == null)
			PayDate = new Timestamp(System.currentTimeMillis());
		setCalloutActive(true);

		BigDecimal OpenAmt = Env.ZERO;
		BigDecimal DiscountAmt = Env.ZERO;
		Boolean IsSOTrx = Boolean.FALSE;
		String sql = "SELECT currencyConvert(invoiceOpen(i.C_Invoice_ID, 0), i.C_Currency_ID,"
				+ "ba.C_Currency_ID, i.DateInvoiced, i.C_ConversionType_ID, i.AD_Client_ID, i.AD_Org_ID),"
			+ " paymentTermDiscount(i.GrandTotal,i.C_Currency_ID,i.C_PaymentTerm_ID,i.DateInvoiced, ?), i.IsSOTrx "
			+ "FROM C_Invoice_v i, C_BankAccount ba "
			+ "WHERE i.C_Invoice_ID=? AND ba.C_BankAccount_ID=?";	//	#1..2
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Invoice_ID);
			pstmt.setInt(2, C_BankAccount_ID);
			pstmt.setTimestamp(3, PayDate);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				OpenAmt = rs.getBigDecimal(1);
				DiscountAmt = rs.getBigDecimal(2);
				IsSOTrx = new Boolean ("Y".equals(rs.getString(3)));
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		log.fine(" - OpenAmt=" + OpenAmt + " (Invoice=" + C_Invoice_ID + ",BankAcct=" + C_BankAccount_ID + ")");
		mTab.setValue("OpenAmt", OpenAmt);
		mTab.setValue("PayAmt", OpenAmt.subtract(DiscountAmt));
		mTab.setValue("DiscountAmt", DiscountAmt);
		mTab.setValue("DifferenceAmt", Env.ZERO);
		mTab.setValue("IsSOTrx", IsSOTrx);

		setCalloutActive(false);
		return "";
	}	//	PaySel_Invoice

	
}	//	CalloutPaySelection
