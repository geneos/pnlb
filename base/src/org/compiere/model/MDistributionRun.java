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
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Distribution Run Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDistributionRun.java,v 1.6 2005/09/24 01:52:52 jjanke Exp $
 */
public class MDistributionRun extends X_M_DistributionRun
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_DistributionRun_ID id
	 */
	public MDistributionRun (Properties ctx, int M_DistributionRun_ID, String trxName)
	{
		super (ctx, M_DistributionRun_ID, trxName);
	}	//	MDistributionRun

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MDistributionRun (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MDistributionRun
	
	/**	 Cached Lines					*/
	private MDistributionRunLine[] 	m_lines = null;
	
	/**
	 * 	Get active, non zero lines
	 *	@param reload true if reload
	 *	@return lines
	 */
	public MDistributionRunLine[] getLines (boolean reload)
	{
		if (!reload && m_lines != null)
			return m_lines;
		//
		String sql = "SELECT * FROM M_DistributionRunLine "
			+ "WHERE M_DistributionRun_ID=? AND IsActive='Y' AND TotalQty IS NOT NULL AND TotalQty<> 0 ORDER BY Line";
		ArrayList<MDistributionRunLine> list = new ArrayList<MDistributionRunLine>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getM_DistributionRun_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MDistributionRunLine(getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getLines", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		m_lines = new MDistributionRunLine[list.size()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines
	
}	//	MDistributionRun
