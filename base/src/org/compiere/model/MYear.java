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
import java.text.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.process.*;
import org.compiere.util.*;


/**
 *	Year Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MYear.java,v 1.9 2005/09/28 01:34:03 jjanke Exp $
 */
public class MYear extends X_C_Year
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Year_ID id
	 */
	public MYear (Properties ctx, int C_Year_ID, String trxName)
	{
		super (ctx, C_Year_ID, trxName);
		if (C_Year_ID == 0)
		{
		//	setC_Calendar_ID (0);
		//	setYear (null);
			setProcessing (false);	// N
		}		
	}	//	MYear

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MYear (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MYear
	
	/**
	 * 	Parent Constructor
	 *	@param calendar parent
	 */
	public MYear (MCalendar calendar)
	{
		this (calendar.getCtx(), 0, calendar.get_TrxName());
		setClientOrg(calendar);
		setC_Calendar_ID(calendar.getC_Calendar_ID());
		setYear();
	}	//	MYear
	
	
	/**
	 * 	Set current Year
	 */
	private void setYear ()
	{
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		String Year = String.valueOf(cal.get(Calendar.YEAR));
		super.setYear (Year);
	}	//	setYear
	
	/**
	 * 	Get Year As Int
	 *	@return year as int or 0
	 */
	public int getYearAsInt()
	{
		try
		{
			return Integer.parseInt(getYear());
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getYearAsInt - " + e.toString());
		}
		return 0;
	}	//	getYearAsInt
	
	/**
	 * 	Get last two characters of year
	 *	@return 01
	 */
	public String getYY()
	{
		String year = getYear();
		return year.substring(2, 4);
	}	//	getYY
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MYear[");
		sb.append(get_ID()).append("-")
			.append(getYear())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getYearAsInt() == 0)
			return false;
		return true;
	}	//	beforeSave
	
	/**
	 * 	Create 12 Standard (Jan-Dec) Periods.
	 * 	Creates also Period Control from DocType.
	 * 	@see DocumentTypeVerify#createPeriodControls(Properties, int, SvrProcess, String) 
	 *	@return true if created
	 */
	public boolean createStdPeriods(Locale locale)
	{
		if (locale == null)
		{
			MClient client = MClient.get(getCtx());
			locale = client.getLocale();
		}
		
		if (locale == null && Language.getLoginLanguage() != null)
			locale = Language.getLoginLanguage().getLocale();
		if (locale == null)
			locale = Env.getLanguage(getCtx()).getLocale();
		//
		DateFormatSymbols symbols = new DateFormatSymbols(locale);
		String[] months = symbols.getShortMonths();
		//
		int year = getYearAsInt();
		GregorianCalendar cal = new GregorianCalendar(locale);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//
		for (int month = 0; month < 12; month++)
		{
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			Timestamp start = new Timestamp(cal.getTimeInMillis());
			String name = months[month] + "-" + getYY();
			System.out.print("Create Period: " + name);
			//
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_YEAR, -1);
			Timestamp end = new Timestamp(cal.getTimeInMillis());
			//
			MPeriod period = new MPeriod (this, month+1, name, start, end);
			if (!period.save(get_TrxName()))	//	Creates Period Control
				return false;
		}
		return true;
	}	//	createStdPeriods
	
}	//	MYear
