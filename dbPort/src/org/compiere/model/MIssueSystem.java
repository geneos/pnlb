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
 * 	Issue System Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MIssueSystem.java,v 1.1 2006/01/03 02:40:04 jjanke Exp $
 */
public class MIssueSystem extends X_R_IssueSystem
{
	/**
	 * 	Get/Set System
	 *	@param issue issue
	 *	@return system
	 */
	static public MIssueSystem get (MIssue issue)
	{
		if (issue.getDBAddress() == null)
			return null;
		MIssueSystem system = null;
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM R_IssueSystem WHERE DBAddress=?";
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setString (1, issue.getDBAddress());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				system = new MIssueSystem(issue.getCtx(), rs, null);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
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
		//	New
		if (system == null)
		{
			system = new MIssueSystem(issue.getCtx(), 0, null);
			system.setDBAddress(issue.getDBAddress());
			system.setA_Asset_ID(issue.getA_Asset_ID());
		}
		system.setSystemStatus(issue.getSystemStatus());
		system.setStatisticsInfo(issue.getStatisticsInfo());
		system.setProfileInfo(issue.getProfileInfo());
		if (issue.getA_Asset_ID() != 0 
			&& system.getA_Asset_ID() != issue.getA_Asset_ID())
			system.setA_Asset_ID(issue.getA_Asset_ID());
		//
		if (!system.save())
			return null;
		
		//	Set 
		issue.setR_IssueSystem_ID(system.getR_IssueSystem_ID());
		if (system.getA_Asset_ID() != 0)
			issue.setA_Asset_ID(system.getA_Asset_ID());
		return system;
	}	//	get
	
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (MIssueSystem.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_IssueSystem_ID id
	 *	@param trxName trx
	 */
	public MIssueSystem (Properties ctx, int R_IssueSystem_ID, String trxName)
	{
		super (ctx, R_IssueSystem_ID, trxName);
	}	//	MIssueSystem

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MIssueSystem (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MIssueSystem
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MIssueSystem[");
		sb.append(get_ID())
			.append ("-").append (getDBAddress())
			.append(",A_Asset_ID=").append(getA_Asset_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
}	//	MIssueSystem
