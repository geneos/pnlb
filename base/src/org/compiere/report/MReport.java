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
import java.util.*;
import org.compiere.model.*;


/**
 *  Report Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReport.java,v 1.12 2005/09/19 04:49:48 jjanke Exp $
 */
public class MReport extends X_PA_Report
{
	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param PA_Report_ID id
	 */
	public MReport (Properties ctx, int PA_Report_ID, String trxName)
	{
		super (ctx, PA_Report_ID, trxName);
		if (PA_Report_ID == 0)
		{
		//	setName (null);
		//	setPA_ReportLineSet_ID (0);
		//	setPA_ReportColumnSet_ID (0);
			setListSources(false);
			setListTrx(false);
		}
		else
		{
			m_columnSet = new MReportColumnSet (ctx, getPA_ReportColumnSet_ID(), trxName);
			m_lineSet = new MReportLineSet (ctx, getPA_ReportLineSet_ID(), trxName);
		}
	}	//	MReport

	/** 
	 * 	Load Constructor 
	 */
	public MReport (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
		m_columnSet = new MReportColumnSet (ctx, getPA_ReportColumnSet_ID(), trxName);
		m_lineSet = new MReportLineSet (ctx, getPA_ReportLineSet_ID(), trxName);
	}	//	MReport

	private MReportColumnSet	m_columnSet = null;
	private MReportLineSet		m_lineSet = null;

	/**
	 * 	List Info
	 */
	public void list()
	{
		System.out.println(toString());
		if (m_columnSet != null)
			m_columnSet.list();
		System.out.println();
		if (m_lineSet != null)
			m_lineSet.list();
	}	//	dump

	/**
	 * 	Get Where Clause for Report
	 * 	@return Where Clause for Report
	 */
	public String getWhereClause()
	{
		//	AD_Client indirectly via AcctSchema
		StringBuffer sb = new StringBuffer();
		//	Mandatory 	AcctSchema
		sb.append("C_AcctSchema_ID=").append(getC_AcctSchema_ID());
		//
		return sb.toString();
	}	//	getWhereClause

	/*************************************************************************/

	/**
	 * 	String Representation
	 * 	@return Info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MReport[")
			.append(get_ID()).append(" - ").append(getName());
		if (getDescription() != null)
			sb.append("(").append(getDescription()).append(")");
		sb.append(" - C_AcctSchema_ID=").append(getC_AcctSchema_ID())
			.append(", C_Calendar_ID=").append(getC_Calendar_ID());
		sb.append ("]");
		return sb.toString ();
	}	//	toString


	public MReportColumnSet	getColumnSet()
	{
		return m_columnSet;
	}

	public MReportLineSet getLineSet()
	{
		return m_lineSet;
	}

}	//	MReport
