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
 * 	Callout for Allocate Payments
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutPaymentAllocate.java,v 1.2 2005/11/06 01:17:27 jjanke Exp $
 */
public class CalloutPaymentAllocate extends CalloutEngine
{
	/**
	 *  Payment_Invoice.
	 *  when Invoice selected
	 *  - set InvoiceAmt = invoiceOpen
	 *  	- DiscountAmt = C_Invoice_Discount (ID, DateTrx)
	 *   	- Amount = invoiceOpen (ID) - Discount
	 * 		- WriteOffAmt,OverUnderAmt = 0
	 */
	public String invoice (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer C_Invoice_ID = (Integer)value;
		if (isCalloutActive()		//	assuming it is resetting value
			|| C_Invoice_ID == null || C_Invoice_ID.intValue() == 0)
			return "";

		//	Check Payment
		int C_Payment_ID = Env.getContextAsInt(ctx, WindowNo, "C_Payment_ID");
		MPayment payment = new MPayment (ctx, C_Payment_ID, null);
		if (payment.getC_Charge_ID() != 0 || payment.getC_Invoice_ID() != 0 
			|| payment.getC_Order_ID() != 0)
			return Msg.getMsg(ctx, "PaymentIsAllocated");
		
		setCalloutActive(true);
		//
		mTab.setValue("DiscountAmt", Env.ZERO);
		mTab.setValue("WriteOffAmt", Env.ZERO);
		mTab.setValue("OverUnderAmt", Env.ZERO);

		int C_InvoicePaySchedule_ID = 0;
		if (Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "C_Invoice_ID") == C_Invoice_ID.intValue()
			&& Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "C_InvoicePaySchedule_ID") != 0)
			C_InvoicePaySchedule_ID = Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "C_InvoicePaySchedule_ID");

		//  Payment Date
		Timestamp ts = Env.getContextAsDate(ctx, WindowNo, "DateTrx");
		//
		String sql = "SELECT C_BPartner_ID,C_Currency_ID,"		//	1..2
			+ " invoiceOpen(C_Invoice_ID, ?),"					//	3		#1
			+ " invoiceDiscount(C_Invoice_ID,?,?), IsSOTrx "	//	4..5	#2/3
			+ "FROM C_Invoice WHERE C_Invoice_ID=?";			//			#4
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_InvoicePaySchedule_ID);
			pstmt.setTimestamp(2, ts);
			pstmt.setInt(3, C_InvoicePaySchedule_ID);
			pstmt.setInt(4, C_Invoice_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
			//	mTab.setValue("C_BPartner_ID", new Integer(rs.getInt(1)));
			//	int C_Currency_ID = rs.getInt(2);					//	Set Invoice Currency
			//	mTab.setValue("C_Currency_ID", new Integer(C_Currency_ID));
				//
				BigDecimal InvoiceOpen = rs.getBigDecimal(3);		//	Set Invoice OPen Amount
				if (InvoiceOpen == null)
					InvoiceOpen = Env.ZERO;
				BigDecimal DiscountAmt = rs.getBigDecimal(4);		//	Set Discount Amt
				if (DiscountAmt == null)
					DiscountAmt = Env.ZERO;
				mTab.setValue("InvoiceAmt", InvoiceOpen);
				mTab.setValue("Amount", InvoiceOpen.subtract(DiscountAmt));
				mTab.setValue("DiscountAmt", DiscountAmt);
				//  reset as dependent fields get reset
				Env.setContext(ctx, WindowNo, "C_Invoice_ID", C_Invoice_ID.toString());
				mTab.setValue("C_Invoice_ID", C_Invoice_ID);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			setCalloutActive(false);
			return e.getLocalizedMessage();
		}

		setCalloutActive(false);
		return "";
	}	//	invoice

	/**
	 *  Payment_Amounts.
	 *	Change of:
	 *		- IsOverUnderPayment -> set OverUnderAmt to 0
	 *		- C_Currency_ID, C_ConvesionRate_ID -> convert all
	 *		- PayAmt, DiscountAmt, WriteOffAmt, OverUnderAmt -> PayAmt
	 *			make sure that add up to InvoiceOpenAmt
	 */
	public String amounts (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
		//	No Invoice
		int C_Invoice_ID = Env.getContextAsInt(ctx, WindowNo, "C_Invoice_ID");
		if (C_Invoice_ID == 0)
			return "";
		setCalloutActive(true);
		//	Get Info from Tab
		BigDecimal Amount = (BigDecimal)mTab.getValue("Amount");
		BigDecimal DiscountAmt = (BigDecimal)mTab.getValue("DiscountAmt");
		BigDecimal WriteOffAmt = (BigDecimal)mTab.getValue("WriteOffAmt");
		BigDecimal OverUnderAmt = (BigDecimal)mTab.getValue("OverUnderAmt");
		BigDecimal InvoiceAmt = (BigDecimal)mTab.getValue("InvoiceAmt");
		log.fine("Amt=" + Amount + ", Discount=" + DiscountAmt
			+ ", WriteOff=" + WriteOffAmt + ", OverUnder=" + OverUnderAmt
			+ ", Invoice=" + InvoiceAmt);

		//	Changed Column
		String colName = mField.getColumnName();
		//  PayAmt - calculate write off
		if (colName.equals("Amount"))
		{
			
			OverUnderAmt = InvoiceAmt.subtract(Amount).subtract(DiscountAmt).subtract(WriteOffAmt);
			mTab.setValue("OverUnderAmt", OverUnderAmt);
			
			//WriteOffAmt = InvoiceAmt.subtract(Amount).subtract(DiscountAmt).subtract(OverUnderAmt);
			//mTab.setValue("WriteOffAmt", WriteOffAmt);
		}
		else    //  calculate Amount
		{
			Amount = InvoiceAmt.subtract(DiscountAmt).subtract(WriteOffAmt).subtract(OverUnderAmt);
			mTab.setValue("Amount", Amount);
		}

		setCalloutActive(false);
		return "";
	}	//	amounts

	
}	//	CalloutPaymentAllocate
