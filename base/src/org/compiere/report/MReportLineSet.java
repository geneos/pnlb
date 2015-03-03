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
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Report Line Set Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReportLineSet.java,v 1.13 2005/11/14 02:10:53 jjanke Exp $
 */
public class MReportLineSet extends X_PA_ReportLineSet
{
	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param PA_ReportLineSet_ID id
	 */
	public MReportLineSet (Properties ctx, int PA_ReportLineSet_ID, String trxName)
	{
		super (ctx, PA_ReportLineSet_ID, trxName);
		if (PA_ReportLineSet_ID == 0)
		{
		}
		else
			loadLines();
	}	//	MReportLineSet

	/**	Contained Lines			*/
	private MReportLine[]	m_lines = null;

	/**
	 * 	Load Lines
	 */
	private void loadLines()
	{
		ArrayList<MReportLine> list = new ArrayList<MReportLine>();
		String sql = "SELECT * FROM PA_ReportLine "
			+ "WHERE PA_ReportLineSet_ID=? AND IsActive='Y' "
			+ "ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getPA_ReportLineSet_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MReportLine (getCtx(), rs, get_TrxName()));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		//
		m_lines = new MReportLine[list.size()];
		list.toArray(m_lines);
		log.finest("ID=" + getPA_ReportLineSet_ID()
			+ " - Size=" + list.size());
	}	//	loadColumns

	/**
	 * 	Get Liness
	 *	@return array of lines
	 */
	public MReportLine[] getLiness()
	{
		return m_lines;
	}	//	getLines

	/**
	 * 	List Info
	 */
	public void list()
	{
		System.out.println(toString());
		if (m_lines == null)
			return;
		for (int i = 0; i < m_lines.length; i++)
			m_lines[i].list();
	}	//	list


	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MReportLineSet[")
			.append(get_ID()).append(" - ").append(getName())
			.append ("]");
		return sb.toString ();
	}

}	//	MReportLineSet
