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

import java.sql.*;
import java.util.*;
import org.compiere.util.*;

/**
 * 	Recurring Model
 *
 *	@author Jorg Janke
 *	@version $Id: MRecurring.java,v 1.15 2005/10/26 00:37:41 jjanke Exp $
 */
public class MRecurring extends X_C_Recurring
{
	public MRecurring (Properties ctx, int C_Recurring_ID, String trxName)
	{
		super (ctx, C_Recurring_ID, trxName);
		if (C_Recurring_ID == 0)
		{
		//	setC_Recurring_ID (0);		//	PK
			setDateNextRun (new Timestamp(System.currentTimeMillis()));
			setFrequencyType (FREQUENCYTYPE_Monthly);
			setFrequency(1);
		//	setName (null);
		//	setRecurringType (null);
			setRunsMax (1);
			setRunsRemaining (0);
		}
	}	//	MRecurring

	public MRecurring (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRecurring

	/**
	 *	String representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MRecurring[")
			.append(get_ID()).append(",").append(getName());
		if (getRecurringType().equals(MRecurring.RECURRINGTYPE_Order))
			sb.append(",C_Order_ID=").append(getC_Order_ID());
		else if (getRecurringType().equals(MRecurring.RECURRINGTYPE_Invoice))
			sb.append(",C_Invoice_ID=").append(getC_Invoice_ID());
		else if (getRecurringType().equals(MRecurring.RECURRINGTYPE_Project))
			sb.append(",C_Project_ID=").append(getC_Project_ID());
		else if (getRecurringType().equals(MRecurring.RECURRINGTYPE_GLJournal))
			sb.append(",GL_JournalBatch_ID=").append(getGL_JournalBatch_ID());
		sb.append(",Fequency=").append(getFrequencyType()).append("*").append(getFrequency());
		sb.append("]");
		return sb.toString();
	}	//	toString

	
	/**************************************************************************
	 * 	Execute Run.
	 *	@return clear text info
	 */
	public String executeRun()
	{
		Timestamp dateDoc = getDateNextRun();
		if (!calculateRuns())
			throw new IllegalStateException ("No Runs Left");

		//	log
		MRecurringRun run = new MRecurringRun (getCtx(), this);
		String msg = "@Created@ ";


		//	Copy
		if (getRecurringType().equals(MRecurring.RECURRINGTYPE_Order))
		{
			MOrder from = new MOrder (getCtx(), getC_Order_ID(), get_TrxName());
			MOrder order = MOrder.copyFrom (from, dateDoc, 
				from.getC_DocType_ID(), from.isSOTrx(), false, false, get_TrxName());
			run.setC_Order_ID(order.getC_Order_ID());
			msg += order.getDocumentNo();
		}
		else if (getRecurringType().equals(MRecurring.RECURRINGTYPE_Invoice))
		{
			MInvoice from = new MInvoice (getCtx(), getC_Invoice_ID(), get_TrxName());
			MInvoice invoice = MInvoice.copyFrom (from, dateDoc, 
				from.getC_DocType_ID(), from.isSOTrx(), false, get_TrxName(), false);
			run.setC_Invoice_ID(invoice.getC_Invoice_ID());
			msg += invoice.getDocumentNo();
		}
		else if (getRecurringType().equals(MRecurring.RECURRINGTYPE_Project))
		{
			MProject project = MProject.copyFrom (getCtx(), getC_Project_ID(), dateDoc, get_TrxName());
			run.setC_Project_ID(project.getC_Project_ID());
			msg += project.getValue();
		}
		else if (getRecurringType().equals(MRecurring.RECURRINGTYPE_GLJournal))
		{
			MJournalBatch journal = MJournalBatch.copyFrom (getCtx(), getGL_JournalBatch_ID(), dateDoc, get_TrxName());
			run.setGL_JournalBatch_ID(journal.getGL_JournalBatch_ID());
			msg += journal.getDocumentNo();
		}
		else
			return "Invalid @RecurringType@ = " + getRecurringType();
		run.save(get_TrxName());

		//
		setDateLastRun (run.getUpdated());
		setRunsRemaining (getRunsRemaining()-1);
		setDateNextRun();
		save(get_TrxName());
		return msg;
	}	//	execureRun

	/**
	 *	Calculate & set remaining Runs
	 *	@return true if runs left
	 */
	private boolean calculateRuns()
	{
		String sql = "SELECT COUNT(*) FROM C_Recurring_Run WHERE C_Recurring_ID=?";
		int current = DB.getSQLValue(get_TrxName(), sql, getC_Recurring_ID());
		int remaining = getRunsMax() - current;
		setRunsRemaining(remaining);
		save();
		return remaining > 0;
	}	//	calculateRuns

	/**
	 *	Calculate next run date
	 */
	private void setDateNextRun()
	{
		if (getFrequency() < 1)
			setFrequency(1);
		int frequency = getFrequency();
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateNextRun());
		//
		if (getFrequencyType().equals(FREQUENCYTYPE_Daily))
			cal.add(Calendar.DAY_OF_YEAR, frequency);
		else if (getFrequencyType().equals(FREQUENCYTYPE_Weekly))
			cal.add(Calendar.WEEK_OF_YEAR, frequency);
		else if (getFrequencyType().equals(FREQUENCYTYPE_Monthly))
			cal.add(Calendar.MONTH, frequency);
		else if (getFrequencyType().equals(FREQUENCYTYPE_Quarterly))
			cal.add(Calendar.MONTH, 3*frequency);
		Timestamp next = new Timestamp (cal.getTimeInMillis());
		setDateNextRun(next);
	}	//	setDateNextRun

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		String rt = getRecurringType();
		if (rt == null)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "RecurringType"));
			return false;
		}
		if (rt.equals(MRecurring.RECURRINGTYPE_Order)
			&& getC_Order_ID() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "C_Order_ID"));
			return false;
		}
		if (rt.equals(MRecurring.RECURRINGTYPE_Invoice)
			&& getC_Invoice_ID() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "C_Invoice_ID"));
			return false;
		}
		if (rt.equals(MRecurring.RECURRINGTYPE_GLJournal)
			&& getGL_JournalBatch_ID() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "GL_JournalBatch_ID"));
			return false;
		}
		if (rt.equals(MRecurring.RECURRINGTYPE_Project)
			&& getC_Project_ID() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "C_Project_ID"));
			return false;
		}
		return true;
	}	//	beforeSave
	
}	//	MRecurring
