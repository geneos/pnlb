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
 *	Dunning Run Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDunningRunLine.java,v 1.7 2005/09/28 01:34:03 jjanke Exp $
 */
public class MDunningRunLine extends X_C_DunningRunLine
{
	/**
	 * 	Standarc Constructor
	 *	@param ctx ctx
	 *	@param C_DunningRunLine_ID id
	 */
	public MDunningRunLine (Properties ctx, int C_DunningRunLine_ID, String trxName)
	{
		super (ctx, C_DunningRunLine_ID, trxName);
		if (C_DunningRunLine_ID == 0)
		{
			setAmt (Env.ZERO);
			setOpenAmt(Env.ZERO);
			setConvertedAmt (Env.ZERO);
			setFeeAmt (Env.ZERO);
			setInterestAmt (Env.ZERO);
			setTotalAmt (Env.ZERO);
			setDaysDue (0);
			setTimesDunned (0);
			setIsInDispute(false);
			setProcessed (false);
		}
	}	//	MDunningRunLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MDunningRunLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MDunningRunLine

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 */
	public MDunningRunLine (MDunningRunEntry parent)
	{
		this(parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setC_DunningRunEntry_ID(parent.getC_DunningRunEntry_ID());
		//
		m_parent = parent;
		m_C_CurrencyTo_ID = parent.getC_Currency_ID();
	}	//	MDunningRunLine

	private MDunningRunEntry	m_parent = null;
	private MInvoice			m_invoice = null;
	private MPayment			m_payment = null;
	private int					m_C_CurrencyFrom_ID = 0;
	private int					m_C_CurrencyTo_ID = 0;
	
	/**
	 * 	Get Parent 
	 *	@return parent
	 */
	public MDunningRunEntry getParent()
	{
		if (m_parent == null)
			m_parent = new MDunningRunEntry (getCtx(), getC_DunningRunEntry_ID(), get_TrxName());
		return m_parent;
	}	//	getParent
	
	/**
	 * 	Get Invoice
	 *	@return Returns the invoice.
	 */
	public MInvoice getInvoice ()
	{
		if (getC_Invoice_ID() == 0)
			m_invoice = null;
		else if (m_invoice == null)
			m_invoice = new MInvoice (getCtx(), getC_Invoice_ID(), get_TrxName());
		return m_invoice;
	}	//	getInvoice
	
	/**
	 * 	Set Invoice
	 *	@param invoice The invoice to set.
	 */
	public void setInvoice (MInvoice invoice)
	{
		m_invoice = invoice;
		if (invoice != null)
		{
			m_C_CurrencyFrom_ID = invoice.getC_Currency_ID();
			setAmt(invoice.getGrandTotal());
			setOpenAmt(getAmt());	//	not correct
			setConvertedAmt (MConversionRate.convert(getCtx(), getOpenAmt(), 
				getC_CurrencyFrom_ID(), getC_CurrencyTo_ID(), getAD_Client_ID(), getAD_Org_ID()));
		}
		else
		{
			m_C_CurrencyFrom_ID = 0;
			setAmt(Env.ZERO);
			setOpenAmt(Env.ZERO);
			setConvertedAmt(Env.ZERO);
		}
	}	//	setInvoice
	
	/**
	 * 	Set Invoice
	 *	@param C_Invoice_ID
	 *	@param C_Currency_ID
	 *	@param GrandTotal 
	 *	@param Open
	 *	@param DaysDue
	 *	@param IsInDispute 
	 *	@param TimesDunned
	 *	@param DaysAfterLast not used
	 */
	public void setInvoice (int C_Invoice_ID, int C_Currency_ID, 
		BigDecimal GrandTotal, BigDecimal Open, 
		int DaysDue, boolean IsInDispute, 
		int TimesDunned, int DaysAfterLast)
	{
		setC_Invoice_ID(C_Invoice_ID);
		m_C_CurrencyFrom_ID = C_Currency_ID;
		setAmt (GrandTotal);
		setOpenAmt (Open);
		setConvertedAmt (MConversionRate.convert(getCtx(), getOpenAmt(), 
			C_Currency_ID, getC_CurrencyTo_ID(), getAD_Client_ID(), getAD_Org_ID()));
		setIsInDispute(IsInDispute);
		setDaysDue(DaysDue);
		setTimesDunned(TimesDunned);
	}	//	setInvoice
	
	
	/**
	 * 	Get Payment
	 *	@return Returns the payment.
	 */
	public MPayment getPayment ()
	{
		if (getC_Payment_ID() == 0)
			m_payment = null;
		else if (m_payment == null)
			m_payment = new MPayment (getCtx(), getC_Payment_ID(), get_TrxName());
		return m_payment;
	}	//	getPayment
	
	/**
	 * 	Set Payment
	 *
	public void setPayment (MPayment payment)
	{
		m_payment = payment;
		if (payment != null)
		{
			m_C_CurrencyFrom_ID = payment.getC_Currency_ID();
			setAmt(payment.getPayAmt());	//	need to reverse
			setOpenAmt(getAmt());	//	not correct
			setConvertedAmt (MConversionRate.convert(getCtx(), getOpenAmt(), 
				getC_CurrencyFrom_ID(), getC_CurrencyTo_ID(), getAD_Client_ID(), getAD_Org_ID()));
		}
		else
		{
			m_C_CurrencyFrom_ID = 0;
			setAmt(Env.ZERO);
			setConvertedAmt(Env.ZERO);
		}
	}	//	setPayment
	
	/**
	 * 	Set Payment
	 *	@param C_Payment_ID
	 *	@param C_Currency_ID
	 *	@param PayAmt
	 *	@param OpenAmt
	 */
	public void setPayment (int C_Payment_ID, int C_Currency_ID, 
		BigDecimal PayAmt, BigDecimal OpenAmt)
	{
		setC_Payment_ID(C_Payment_ID);
		m_C_CurrencyFrom_ID = C_Currency_ID;
		setAmt (PayAmt);
		setOpenAmt (OpenAmt);
		setConvertedAmt (MConversionRate.convert(getCtx(), getOpenAmt(), 
			C_Currency_ID, getC_CurrencyTo_ID(), getAD_Client_ID(), getAD_Org_ID()));
	}	//	setPayment

	
	/**
	 * 	Get Currency From (Invoice/Payment)
	 *	@return Returns the Currency From
	 */
	public int getC_CurrencyFrom_ID ()
	{
		if (m_C_CurrencyFrom_ID == 0)
		{
			if (getC_Invoice_ID() != 0)
				m_C_CurrencyFrom_ID = getInvoice().getC_Currency_ID();
			else if (getC_Payment_ID() != 0)
				m_C_CurrencyFrom_ID = getPayment().getC_Currency_ID();
		}
		return m_C_CurrencyFrom_ID;
	}	//	getC_CurrencyFrom_ID
	
	/**
	 * 	Get Currency To from Parent
	 *	@return Returns the Currency To
	 */
	public int getC_CurrencyTo_ID ()
	{
		if (m_C_CurrencyTo_ID == 0)
			m_C_CurrencyTo_ID = getParent().getC_Currency_ID();
		return m_C_CurrencyTo_ID;
	}	//	getC_CurrencyTo_ID
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Set Amt
		if (getC_Invoice_ID() == 0 && getC_Payment_ID() == 0)
		{
			setAmt(Env.ZERO);
			setOpenAmt(Env.ZERO);
		}
		//	Converted Amt
		if (Env.ZERO.compareTo(getOpenAmt()) == 0)
			setConvertedAmt (Env.ZERO);
		else if (Env.ZERO.compareTo(getConvertedAmt()) == 0)
			setConvertedAmt (MConversionRate.convert(getCtx(), getOpenAmt(), 
				getC_CurrencyFrom_ID(), getC_CurrencyTo_ID(), getAD_Client_ID(), getAD_Org_ID()));
		//	Total
		setTotalAmt(getConvertedAmt().add(getFeeAmt()).add(getInterestAmt()));
		//
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
		updateEntry();
		return success;
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		updateEntry();
		return success;
	}	//	afterDelete
	
	/**
	 * 	Update Entry.
	 *	Calculate/update Amt/Qty 
	 */
	private void updateEntry()
	{
		String sql = "UPDATE C_DunningRunEntry e "
			+ "SET (Amt,Qty)=(SELECT SUM(Amt),COUNT(*) FROM C_DunningRunLine l "
				+ "WHERE e.C_DunningRunEntry_ID=l.C_DunningRunEntry_ID) "
			+ "WHERE C_DunningRunEntry_ID=" + getC_DunningRunEntry_ID();
		DB.executeUpdate(sql, get_TrxName());
	}	//	updateEntry
	
}	//	MDunningRunLine
