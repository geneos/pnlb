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
 *	Payment Term Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPaymentTerm.java,v 1.14 2005/10/14 00:44:31 jjanke Exp $
 */
public class MPaymentTerm extends X_C_PaymentTerm
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_PaymentTerm_ID id
	 */
	public MPaymentTerm(Properties ctx, int C_PaymentTerm_ID, String trxName)
	{
		super(ctx, C_PaymentTerm_ID, trxName);
		if (C_PaymentTerm_ID == 0)
		{
			setAfterDelivery (false);
			setNetDays (0);
			setDiscount (Env.ZERO);
			setDiscount2 (Env.ZERO);
			setDiscountDays (0);
			setDiscountDays2 (0);
			setGraceDays (0);
			setIsDueFixed (false);
			setIsValid (false);
		}	}	//	MPaymentTerm

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPaymentTerm(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPaymentTerm

	/** 100									*/
	private final static BigDecimal		HUNDRED = new BigDecimal(100);

	/**	Payment Schedule children			*/
	private MPaySchedule[]				m_schedule;

	/**
	 * 	Get Payment Schedule
	 * 	@param requery if true re-query
	 *	@return array of schedule
	 */
	public MPaySchedule[] getSchedule (boolean requery)
	{
		if (m_schedule != null && !requery)
			return m_schedule;
		String sql = "SELECT * FROM C_PaySchedule WHERE C_PaymentTerm_ID=? ORDER BY NetDays";
		ArrayList<MPaySchedule> list = new ArrayList<MPaySchedule>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_PaymentTerm_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MPaySchedule ps = new MPaySchedule(getCtx(), rs, get_TrxName());
				ps.setParent(this);
				list.add (ps);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getSchedule", e); 
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		
		m_schedule = new MPaySchedule[list.size()];
		list.toArray(m_schedule);
		return m_schedule;
	}	//	getSchedule

	/**
	 * 	Validate Payment Term & Schedule
	 *	@return Validation Message @OK@ or error
	 */
	public String validate()
	{
		getSchedule(true);
		if (m_schedule.length == 0)
		{
			setIsValid(true);
			return "@OK@";
		}
		if (m_schedule.length == 1)
		{
			setIsValid(false);
			return "@Invalid@ @Count@ # = 1 (@C_PaySchedule_ID@)";
		}
		
		//	Add up
		BigDecimal total = Env.ZERO;
		for (int i = 0; i < m_schedule.length; i++)
		{
			BigDecimal percent = m_schedule[i].getPercentage();
			if (percent != null)
				total = total.add(percent);
		}
		boolean valid = total.compareTo(HUNDRED) == 0;
		setIsValid (valid);
		for (int i = 0; i < m_schedule.length; i++)
		{
			if (m_schedule[i].isValid() != valid)
			{
				m_schedule[i].setIsValid(valid);
				m_schedule[i].save();
			}
		}
		String msg = "@OK@";
		if (!valid)
			msg = "@Total@ = " + total + " - @Difference@ = " + HUNDRED.subtract(total); 
		return Msg.parseTranslation(getCtx(), msg);
	}	//	validate


	/*************************************************************************
	 * 	Apply Payment Term to Invoice -
	 *	@param C_Invoice_ID invoice
	 *	@return true if payment schedule is valid
	 */
	public boolean apply (int C_Invoice_ID)
	{
		MInvoice invoice = new MInvoice (getCtx(), C_Invoice_ID, get_TrxName());
		if (invoice == null || invoice.get_ID() == 0)
		{
			log.log(Level.SEVERE, "apply - Not valid C_Invoice_ID=" + C_Invoice_ID);
			return false;
		}
		return apply (invoice);
	}	//	apply
	
	/**
	 * 	Apply Payment Term to Invoice
	 *	@param invoice invoice
	 *	@return true if payment schedule is valid
	 */
	public boolean apply (MInvoice invoice)
	{
		if (invoice == null || invoice.get_ID() == 0)
		{
			log.log(Level.SEVERE, "No valid invoice - " + invoice);
			return false;
		}

		if (!isValid())
			return applyNoSchedule (invoice);
		//
		getSchedule(true);
		if (m_schedule.length <= 1)
			return applyNoSchedule (invoice);
		else	//	only if valid
			return applySchedule(invoice);		
	}	//	apply

	/**
	 * 	Apply Payment Term without schedule to Invoice
	 *	@param invoice invoice
	 *	@return false as no payment schedule
	 */
	private boolean applyNoSchedule (MInvoice invoice)
	{
		deleteInvoicePaySchedule (invoice.getC_Invoice_ID(), invoice.get_TrxName());
		//	updateInvoice
		if (invoice.getC_PaymentTerm_ID() != getC_PaymentTerm_ID())
			invoice.setC_PaymentTerm_ID(getC_PaymentTerm_ID());
		if (invoice.isPayScheduleValid())
			invoice.setIsPayScheduleValid(false);
		return false;
	}	//	applyNoSchedule

	/**
	 * 	Apply Payment Term with schedule to Invoice
	 *	@param invoice invoice
	 *	@return true if payment schedule is valid
	 */
	private boolean applySchedule (MInvoice invoice)
	{
		deleteInvoicePaySchedule (invoice.getC_Invoice_ID(), invoice.get_TrxName());
		//	Create Schedule
		MInvoicePaySchedule ips = null;
		BigDecimal remainder = invoice.getGrandTotal();
		for (int i = 0; i < m_schedule.length; i++)
		{
			ips = new MInvoicePaySchedule (invoice, m_schedule[i]);
			ips.save(invoice.get_TrxName());
			log.fine(ips.toString());
			remainder = remainder.subtract(ips.getDueAmt());
		}	//	for all schedules
		//	Remainder - update last
		if (remainder.compareTo(Env.ZERO) != 0 && ips != null)
		{
			ips.setDueAmt(ips.getDueAmt().add(remainder));
			ips.save(invoice.get_TrxName());
			log.fine("Remainder=" + remainder + " - " + ips);
		}
		
		//	updateInvoice
		if (invoice.getC_PaymentTerm_ID() != getC_PaymentTerm_ID())
			invoice.setC_PaymentTerm_ID(getC_PaymentTerm_ID());
		return invoice.validatePaySchedule();
	}	//	applySchedule

	/**
	 * 	Delete existing Invoice Payment Schedule
	 *	@param C_Invoice_ID id
	 */
	private void deleteInvoicePaySchedule (int C_Invoice_ID, String trxName)
	{
		String sql = "DELETE C_InvoicePaySchedule WHERE C_Invoice_ID=" + C_Invoice_ID;
		int no = DB.executeUpdate(sql, trxName);
		log.fine("C_Invoice_ID=" + C_Invoice_ID + " - #" + no);
	}	//	deleteInvoicePaySchedule

	
	/**************************************************************************
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPaymentTerm[");
		sb.append(get_ID()).append("-").append(getName())
			.append(",Valid=").append(isValid())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (isDueFixed())
		{
			int dd = getFixMonthDay();
			if (dd < 1 || dd > 31)
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@Invalid@ @FixMonthDay@"));
				return false;
			}
			dd = getFixMonthCutoff();
			if (dd < 1 || dd > 31)
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@Invalid@ @FixMonthCutoff@"));
				return false;
			}
		}
		
		if (!newRecord || !isValid())
			validate();
		return true;
	}	//	beforeSave
	
}	//	MPaymentTerm
