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
 *  Report Column Set Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReportColumnSet.java,v 1.12 2005/10/26 00:37:42 jjanke Exp $
 */
public class MReportColumnSet extends X_PA_ReportColumnSet
{
	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param PA_ReportColumnSet_ID id
	 */
	public MReportColumnSet (Properties ctx, int PA_ReportColumnSet_ID, String trxName)
	{
		super (ctx, PA_ReportColumnSet_ID, trxName);
		if (PA_ReportColumnSet_ID == 0)
		{
		}
		else
			loadColumns();
	}	//	MReportColumnSet

	/** Contained Columns		*/
	private MReportColumn[]	m_columns = null;

	/**
	 *	Load contained columns
	 */
	private void loadColumns()
	{
		ArrayList<MReportColumn> list = new ArrayList<MReportColumn>();
		String sql = "SELECT * FROM PA_ReportColumn WHERE PA_ReportColumnSet_ID=? AND IsActive='Y' ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getPA_ReportColumnSet_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MReportColumn (getCtx(), rs, null));
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
		m_columns = new MReportColumn[list.size()];
		list.toArray(m_columns);
		log.finest("ID=" + getPA_ReportColumnSet_ID() 
			+ " - Size=" + list.size());
	}	//	loadColumns

	/**
	 * 	Get Columns
	 *	@return columns
	 */
	public MReportColumn[] getColumns()
	{
		return m_columns;
	}	//	getColumns

	/**
	 * 	List Info
	 */
	public void list()
	{
		System.out.println(toString());
		if (m_columns == null)
			return;
		for (int i = 0; i < m_columns.length; i++)
			System.out.println("- " + m_columns[i].toString());
	}	//	list

	/*************************************************************************/

	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MReportColumnSet[")
			.append(get_ID()).append(" - ").append(getName())
			.append ("]");
		return sb.toString ();
	}

}	//	MReportColumnSet
