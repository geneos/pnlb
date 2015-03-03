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
package org.compiere.report;

import java.sql.*;
import org.compiere.util.*;

/**
 *  Financial Report Periods
 *
 *  @author Jorg Janke
 *  @version $Id: FinReportPeriod.java,v 1.7 2005/03/11 20:26:09 jjanke Exp $
 */
public class FinReportPeriod
{
	/**
	 *	Constructor
	 * 	@param C_Period_ID period
	 * 	@param Name name
	 * 	@param StartDate period start date
	 * 	@param EndDate period end date
	 * 	@param YearStartDate year start date
	*/
	public FinReportPeriod (int C_Period_ID, String Name, Timestamp StartDate, Timestamp EndDate,
		Timestamp YearStartDate)
	{
		m_C_Period_ID = C_Period_ID;
		m_Name = Name;
		m_StartDate = StartDate;
		m_EndDate = EndDate;
		m_YearStartDate = YearStartDate;
	}	//

	private int 		m_C_Period_ID;
	private String 		m_Name;
	private Timestamp 	m_StartDate;
	private Timestamp 	m_EndDate;
	private Timestamp 	m_YearStartDate;


	/**
	 * 	Get Period Info
	 * 	@return BETWEEN start AND end
	 */
	public String getPeriodWhere ()
	{
		StringBuffer sql = new StringBuffer ("BETWEEN ");
		sql.append(DB.TO_DATE(m_StartDate))
			.append(" AND ")
			.append(DB.TO_DATE(m_EndDate));
		return sql.toString();
	}	//	getPeriodWhere

	/**
	 * 	Get Year Info
	 * 	@return BETWEEN start AND end
	 */
	public String getYearWhere ()
	{
		StringBuffer sql = new StringBuffer ("BETWEEN ");
		sql.append(DB.TO_DATE(m_YearStartDate))
			  .append(" AND ")
			  .append(DB.TO_DATE(m_EndDate));
		return sql.toString();
	}	//	getPeriodWhere

	/**
	 * 	Get Total Info
	 * 	@return <= end
	 */
	public String getTotalWhere ()
	{
		StringBuffer sql = new StringBuffer ("<= ");
		sql.append(DB.TO_DATE(m_EndDate));
		return sql.toString();
	}	//	getPeriodWhere

	/**
	 * 	Is date in period
	 * 	@param date date
	 * 	@return true if in period
	 */
	public boolean inPeriod (Timestamp date)
	{
		if (date == null)
			return false;
		if (date.before(m_StartDate))
			return false;
		if (date.after(m_EndDate))
			return false;
		return true;
	}	//	inPeriod


	public int getC_Period_ID()
	{
		return m_C_Period_ID;
	}
	public Timestamp getEndDate()
	{
		return m_EndDate;
	}
	public String getName()
	{
		return m_Name;
	}
	public Timestamp getStartDate()
	{
		return m_StartDate;
	}
	public Timestamp getYearStartDate()
	{
		return m_YearStartDate;
	}

}	//	FinReportPeriod
