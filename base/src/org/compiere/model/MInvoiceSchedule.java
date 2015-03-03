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
 *	Invoice Schedule Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInvoiceSchedule.java,v 1.7 2005/10/14 00:44:31 jjanke Exp $
 */
public class MInvoiceSchedule extends X_C_InvoiceSchedule
{
	/**
	 * 	Get MInvoiceSchedule from Cache
	 *	@param ctx context
	 *	@param C_InvoiceSchedule_ID id
	 *	@return MInvoiceSchedule
	 */
	public static MInvoiceSchedule get (Properties ctx, int C_InvoiceSchedule_ID, String trxName)
	{
		Integer key = new Integer (C_InvoiceSchedule_ID);
		MInvoiceSchedule retValue = (MInvoiceSchedule) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MInvoiceSchedule (ctx, C_InvoiceSchedule_ID, trxName);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,MInvoiceSchedule>	s_cache	= new CCache<Integer,MInvoiceSchedule>("C_InvoiceSchedule", 5);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_InvoiceSchedule_ID id
	 */
	public MInvoiceSchedule (Properties ctx, int C_InvoiceSchedule_ID, String trxName)
	{
		super (ctx, C_InvoiceSchedule_ID, trxName);
	}	//	MInvoiceSchedule

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MInvoiceSchedule (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInvoiceSchedule
	
	/**
	 * 	Can I send Invoice
	 *	@return true if I can send Invoice
	 */
	public boolean canInvoice (Timestamp xDate, BigDecimal orderAmt)
	{
		//	Amount
		if (isAmount() && getAmt() != null && orderAmt != null 
			&& orderAmt.compareTo(getAmt()) >= 0)
			return true;
		
		//	Daily
		if (INVOICEFREQUENCY_Daily.equals(getInvoiceFrequency()))
			return true;

		//	Remove time
		xDate = TimeUtil.getDay(xDate);
		Calendar today = TimeUtil.getToday();
		
		//	Weekly
		if (INVOICEFREQUENCY_Weekly.equals(getInvoiceFrequency()))
		{
			Calendar cutoff = TimeUtil.getToday();
			cutoff.set(Calendar.DAY_OF_WEEK, getCalendarDay(getInvoiceWeekDayCutoff()));
			if (cutoff.after(today))
				cutoff.add(Calendar.DAY_OF_YEAR, -7);
			Timestamp cutoffDate = new Timestamp (cutoff.getTimeInMillis());
			log.fine("canInvoice - Date=" + xDate + " > Cutoff=" + cutoffDate 
				+ " - " + xDate.after(cutoffDate));
			if (xDate.after(cutoffDate))
				return false;
			//
			Calendar invoice = TimeUtil.getToday();
			invoice.set(Calendar.DAY_OF_WEEK, getCalendarDay(getInvoiceWeekDay()));
			if (invoice.after(today))
				invoice.add(Calendar.DAY_OF_YEAR, -7);
			Timestamp invoiceDate = new Timestamp (invoice.getTimeInMillis());
			log.fine("canInvoice - Date=" + xDate + " > Invoice=" + invoiceDate 
				+ " - " + xDate.after(invoiceDate));
			if (xDate.after(invoiceDate))
				return false;
			return true;
		}
		
		//	Monthly
		if (INVOICEFREQUENCY_Monthly.equals(getInvoiceFrequency())
			|| INVOICEFREQUENCY_TwiceMonthly.equals(getInvoiceFrequency()))
		{
			if (getInvoiceDayCutoff() > 0)
			{
				Calendar cutoff = TimeUtil.getToday();
				cutoff.set(Calendar.DAY_OF_MONTH, getInvoiceDayCutoff());
				if (cutoff.after(today))
					cutoff.add(Calendar.MONTH, -1);
				Timestamp cutoffDate = new Timestamp (cutoff.getTimeInMillis());
				log.fine("canInvoice - Date=" + xDate + " > Cutoff=" + cutoffDate 
					+ " - " + xDate.after(cutoffDate));
				if (xDate.after(cutoffDate))
					return false;
			}
			Calendar invoice = TimeUtil.getToday();
			invoice.set(Calendar.DAY_OF_MONTH, getInvoiceDay());
			if (invoice.after(today))
				invoice.add(Calendar.MONTH, -1);
			Timestamp invoiceDate = new Timestamp (invoice.getTimeInMillis());
			log.fine("canInvoice - Date=" + xDate + " > Invoice=" + invoiceDate 
				+ " - " + xDate.after(invoiceDate));
			if (xDate.after(invoiceDate))
				return false;
			return true;
		}

		//	Bi-Monthly (+15)
		if (INVOICEFREQUENCY_TwiceMonthly.equals(getInvoiceFrequency()))
		{
			if (getInvoiceDayCutoff() > 0)
			{
				Calendar cutoff = TimeUtil.getToday();
				cutoff.set(Calendar.DAY_OF_MONTH, getInvoiceDayCutoff() +15);
				if (cutoff.after(today))
					cutoff.add(Calendar.MONTH, -1);
				Timestamp cutoffDate = new Timestamp (cutoff.getTimeInMillis());
				if (xDate.after(cutoffDate))
					return false;
			}
			Calendar invoice = TimeUtil.getToday();
			invoice.set(Calendar.DAY_OF_MONTH, getInvoiceDay() +15);
			if (invoice.after(today))
				invoice.add(Calendar.MONTH, -1);
			Timestamp invoiceDate = new Timestamp (invoice.getTimeInMillis());
			if (xDate.after(invoiceDate))
				return false;
			return true;
		}
		return false;
	}	//	canInvoice
	
	/**
	 * 	Convert to Calendar day
	 *	@param day Invoice Week Day
	 *	@return day
	 */
	private int getCalendarDay (String day)
	{
		if (INVOICEWEEKDAY_Friday.equals(day))
			return Calendar.FRIDAY;
		if (INVOICEWEEKDAY_Saturday.equals(day))
			return Calendar.SATURDAY;
		if (INVOICEWEEKDAY_Sunday.equals(day))
			return Calendar.SUNDAY;
		if (INVOICEWEEKDAY_Monday.equals(day))
			return Calendar.MONDAY;
		if (INVOICEWEEKDAY_Tuesday.equals(day))
			return Calendar.TUESDAY;
		if (INVOICEWEEKDAY_Wednesday.equals(day))
			return Calendar.WEDNESDAY;
	//	if (INVOICEWEEKDAY_Thursday.equals(day))
		return Calendar.THURSDAY;
	}	//	getCalendarDay
	
}	//	MInvoiceSchedule
