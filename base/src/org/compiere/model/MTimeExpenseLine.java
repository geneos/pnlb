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
 * 	Time + Expense Line Model
 *
 *	@author Jorg Janke
 *	@version $Id: MTimeExpenseLine.java,v 1.8 2005/03/11 20:26:04 jjanke Exp $
 */
public class MTimeExpenseLine extends X_S_TimeExpenseLine
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param S_TimeExpenseLine_ID id
	 */
	public MTimeExpenseLine (Properties ctx, int S_TimeExpenseLine_ID, String trxName)
	{
		super (ctx, S_TimeExpenseLine_ID, trxName);
		if (S_TimeExpenseLine_ID == 0)
		{
		//	setS_TimeExpenseLine_ID (0);		//	PK
		//	setS_TimeExpense_ID (0);			//	Parent
			setQty(Env.ONE);
			setQtyInvoiced(Env.ZERO);
			setQtyReimbursed(Env.ZERO);
			//
			setExpenseAmt(Env.ZERO);
			setConvertedAmt(Env.ZERO);
			setPriceReimbursed(Env.ZERO);
			setInvoicePrice(Env.ZERO);
			setPriceInvoiced(Env.ZERO);
			//
			setDateExpense (new Timestamp(System.currentTimeMillis()));
			setIsInvoiced (false);
			setIsTimeReport (false);
			setLine (10);
			setProcessed(false);
		}
	}	//	MTimeExpenseLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MTimeExpenseLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MTimeExpenseLine

	/**	Currency of Report			*/
	private int m_C_Currency_Report_ID = 0;

	
	/**
	 * 	Get Qty Invoiced
	 *	@return entered or qty
	 */
	public BigDecimal getQtyInvoiced ()
	{
		BigDecimal bd = super.getQtyInvoiced ();
		if (Env.ZERO.compareTo(bd) == 0)
			return getQty();
		return bd;
	}	//	getQtyInvoiced

	/**
	 * 	Get Qty Reimbursed
	 *	@return entered or qty
	 */
	public BigDecimal getQtyReimbursed ()
	{
		BigDecimal bd = super.getQtyReimbursed ();
		if (Env.ZERO.compareTo(bd) == 0)
			return getQty();
		return bd;
	}	//	getQtyReimbursed
	
	
	/**
	 * 	Get Price Invoiced
	 *	@return entered or invoice price
	 */
	public BigDecimal getPriceInvoiced ()
	{
		BigDecimal bd = super.getPriceInvoiced ();
		if (Env.ZERO.compareTo(bd) == 0)
			return getInvoicePrice();
		return bd;
	}	//	getPriceInvoiced
	
	/**
	 * 	Get Price Reimbursed
	 *	@return entered or converted amt
	 */
	public BigDecimal getPriceReimbursed ()
	{
		BigDecimal bd = super.getPriceReimbursed ();
		if (Env.ZERO.compareTo(bd) == 0)
			return getConvertedAmt();
		return bd;
	}	//	getPriceReimbursed
	
	
	/**
	 * 	Get Approval Amt
	 *	@return qty * converted amt
	 */
	public BigDecimal getApprovalAmt()
	{
		return getQty().multiply(getConvertedAmt());
	}	//	getApprovalAmt
	
	
	/**
	 * 	Get C_Currency_ID of Report (Price List)
	 *	@return currency
	 */
	public int getC_Currency_Report_ID()
	{
		if (m_C_Currency_Report_ID != 0)
			return m_C_Currency_Report_ID;
		//	Get it from header
		MTimeExpense te = new MTimeExpense (getCtx(), getS_TimeExpense_ID(), get_TrxName());
		m_C_Currency_Report_ID = te.getC_Currency_ID();
		return m_C_Currency_Report_ID;
	}	//	getC_Currency_Report_ID

	/**
	 * 	Set C_Currency_ID of Report (Price List)
	 *	@param C_Currency_ID currency
	 */
	protected void setC_Currency_Report_ID (int C_Currency_ID)
	{
		m_C_Currency_Report_ID = C_Currency_ID;
	}	//	getC_Currency_Report_ID

	
	/**
	 * 	Before Save.
	 * 	Calculate converted amt
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Calculate Converted Amount
		if (newRecord || is_ValueChanged("ExpenseAmt") || is_ValueChanged("C_Currency_ID"))
		{
			if (getC_Currency_ID() == getC_Currency_Report_ID())
				setConvertedAmt(getExpenseAmt());
			else
			{
				setConvertedAmt(MConversionRate.convert (getCtx(),
					getExpenseAmt(), getC_Currency_ID(), getC_Currency_Report_ID(), 
					getDateExpense(), 0, getAD_Client_ID(), getAD_Org_ID()) );
			}
		}
		if (isTimeReport())
		{
			setExpenseAmt(Env.ZERO);
			setConvertedAmt(Env.ZERO);
		}
		return true;
	}	//	beforeSave
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		updateHeader();
		return success;
	}	//	afterSave
	
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		updateHeader();
		return success;
	}	//	afterDelete
	
	/**
	 * 	Update Header.
	 * 	Set Approved Amount
	 */
	private void updateHeader()
	{
		String sql = "UPDATE S_TimeExpense te"
			+ " SET ApprovalAmt = "
				+ "(SELECT SUM(Qty*ConvertedAmt) FROM S_TimeExpenseLine tel "
				+ "WHERE te.S_TimeExpense_ID=tel.S_TimeExpense_ID) "
			+ "WHERE S_TimeExpense_ID=" + getS_TimeExpense_ID();
		int no = DB.executeUpdate(sql, get_TrxName());
	}	//	updateHeader
	
}	//	MTimeExpenseLine
