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
import org.compiere.util.*;

/**
 *	Aging Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAging.java,v 1.9 2005/05/17 05:29:53 jjanke Exp $
 */
public class MAging extends X_T_Aging
{
	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param AD_PInstance_ID instance
	 *	@param C_BPartner_ID bpartner
	 *	@param C_Currency_ID currency
	 *	@param C_Invoice_ID invoice
	 *	@param C_BP_Group_ID group
	 *	@param DueDate due date
	 *	@param IsSOTrx SO Trx
	 */
	public MAging (Properties ctx, int AD_PInstance_ID, int C_BPartner_ID, 
		int C_Currency_ID, int C_Invoice_ID, int C_InvoicePaySchedule_ID,
		int C_BP_Group_ID, Timestamp DueDate, boolean IsSOTrx, String trxName)
	{
		super(ctx, 0, trxName);
		setAD_PInstance_ID (AD_PInstance_ID);
		setC_BPartner_ID (C_BPartner_ID);
		setC_Currency_ID (C_Currency_ID);
		setC_Invoice_ID (C_Invoice_ID);
		setC_InvoicePaySchedule_ID(C_InvoicePaySchedule_ID);
		setIsListInvoices(C_Invoice_ID != 0);
		//
		setC_BP_Group_ID (C_BP_Group_ID);
		if (DueDate == null)
			setDueDate (new Timestamp(System.currentTimeMillis()));
		else
			setDueDate(DueDate);
		setIsSOTrx (IsSOTrx);
		//
		setDueAmt (Env.ZERO);
		setDue0 (Env.ZERO);
		setDue0_7 (Env.ZERO);
		setDue0_30 (Env.ZERO);
		setDue1_7 (Env.ZERO);
		setDue31_60 (Env.ZERO);
		setDue31_Plus (Env.ZERO);
		setDue61_90 (Env.ZERO);
		setDue61_Plus (Env.ZERO);
		setDue8_30 (Env.ZERO);
		setDue91_Plus (Env.ZERO);
		//
		setPastDueAmt (Env.ZERO);
		setPastDue1_7 (Env.ZERO);
		setPastDue1_30 (Env.ZERO);
		setPastDue31_60 (Env.ZERO);
		setPastDue31_Plus (Env.ZERO);
		setPastDue61_90 (Env.ZERO);
		setPastDue61_Plus (Env.ZERO);
		setPastDue8_30 (Env.ZERO);
		setPastDue91_Plus (Env.ZERO);
		//
		setOpenAmt(Env.ZERO);
		setInvoicedAmt(Env.ZERO);
	}	//	MAging

	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAging (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAging

	/**
	 * 	Add Amount to Buckets
	 *	@param daysDue positive due - negative not due
	 *	@param invoicedAmt invoiced amount
	 *	@param openAmt open amount
	 */
	public void add (int daysDue, BigDecimal invoicedAmt, BigDecimal openAmt)
	{
		if (invoicedAmt == null)
			invoicedAmt = Env.ZERO;
		if (openAmt == null)
			openAmt = Env.ZERO;
		setInvoicedAmt(getInvoicedAmt().add(invoicedAmt));
		setOpenAmt(getOpenAmt().add(openAmt));
		
		BigDecimal amt = openAmt;
		
		//	Not due - negative
		if (daysDue <= 0)
		{
			setDueAmt (getDueAmt().add(amt));
			if (daysDue == 0)
				setDue0 (getDue0().add(amt));
				
			if (daysDue >= -7)
				setDue0_7 (getDue0_7().add(amt));
				
			if (daysDue >= -30)
				setDue0_30 (getDue0_30().add(amt));
				
			if (daysDue <= -1 && daysDue >= -7)
				setDue1_7 (getDue1_7().add(amt));
				
			if (daysDue <= -8 && daysDue >= -30)
				setDue8_30 (getDue8_30().add(amt));
				
			if (daysDue <= -31 && daysDue >= -60)
				setDue31_60 (getDue31_60().add(amt));
				
			if (daysDue <= -31)
				setDue31_Plus (getDue31_Plus().add(amt));
				
			if (daysDue <= -61 && daysDue >= -90)
				setDue61_90 (getDue61_90().add(amt));
				
			if (daysDue <= -61)
				setDue61_Plus (getDue61_Plus().add(amt));
				
			if (daysDue <= -91)
				setDue91_Plus (getDue91_Plus().add(amt));
		}
		else	//	Due = positive (> 1)
		{
			setPastDueAmt (getPastDueAmt().add(amt));
			if (daysDue <= 7)
				setPastDue1_7 (getPastDue1_7().add(amt));
				
			if (daysDue <= 30)
				setPastDue1_30 (getPastDue1_30().add(amt));
				
			if (daysDue >= 8 && daysDue <= 30)
				setPastDue8_30 (getPastDue8_30().add(amt));
			
			if (daysDue >= 31 && daysDue <= 60)
				setPastDue31_60 (getPastDue31_60().add(amt));
				
			if (daysDue >= 31)
				setPastDue31_Plus (getPastDue31_Plus().add(amt));
			
			if (daysDue >= 61 && daysDue <= 90)
				setPastDue61_90 (getPastDue61_90().add(amt));
				
			if (daysDue >= 61)
				setPastDue61_Plus (getPastDue61_Plus().add(amt));
				
			if (daysDue >= 91)
				setPastDue91_Plus (getPastDue91_Plus().add(amt));
		}
	}	//	add

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MAging[");
		sb.append("AD_PInstance_ID=").append(getAD_PInstance_ID())
			.append(",C_BPartner_ID=").append(getC_BPartner_ID())
			.append(",C_Currency_ID=").append(getC_Currency_ID())
			.append(",C_Invoice_ID=").append(getC_Invoice_ID());
		sb.append("]");
		return sb.toString();
	} //	toString

}	//	MAging
