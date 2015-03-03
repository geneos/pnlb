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
 * 	Issue Project (and Asset Link)
 *	
 *  @author Jorg Janke
 *  @version $Id: MIssueProject.java,v 1.1 2006/01/03 02:40:05 jjanke Exp $
 */
public class MIssueProject extends X_R_IssueProject
{
	/**
	 * 	Get/Set Project
	 *	@param issue issue
	 *	@return project
	 */
	static public MIssueProject get (MIssue issue)
	{
		if (issue.getName() == null)
			return null;
		MIssueProject pj = null;
		String sql = "SELECT * FROM R_IssueProject WHERE Name=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setString (1, issue.getName());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				pj = new MIssueProject(issue.getCtx(), rs, null);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
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
		if (pj == null)
		{
			pj = new MIssueProject(issue.getCtx(), 0, null);
			pj.setName(issue.getName());
			pj.setA_Asset_ID(issue);
		}
		pj.setSystemStatus(issue.getSystemStatus());
		pj.setStatisticsInfo(issue.getStatisticsInfo());
		pj.setProfileInfo(issue.getProfileInfo());
		if (!pj.save())
			return null;
		
		//	Set 
		issue.setR_IssueProject_ID(pj.getR_IssueProject_ID());
		if (pj.getA_Asset_ID() != 0)
			issue.setA_Asset_ID(pj.getA_Asset_ID());
		return pj;
	}	//	get
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MIssueProject.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_IssueProject_ID id
	 *	@param trxName trx
	 */
	public MIssueProject (Properties ctx, int R_IssueProject_ID, String trxName)
	{
		super (ctx, R_IssueProject_ID, trxName);
	}	//	MIssueProject

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MIssueProject (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MIssueProject
	
	/**
	 * 	Set A_Asset_ID
	 *	@param issue issue
	 */
	public void setA_Asset_ID (MIssue issue)
	{
		int A_Asset_ID = 0;
		String sql = "SELECT * FROM A_Asset a "
			+ "WHERE EXISTS (SELECT * FROM A_Asset_Group ag "	//	Tracking Assets
				+ "WHERE a.A_Asset_Group_ID=ag.A_Asset_Group_ID AND ag.IsTrackIssues='Y')"
			+ " AND EXISTS (SELECT * FROM AD_User u "
				+ "WHERE (a.C_BPartner_ID=u.C_BPartner_ID OR a.C_BPartnerSR_ID=u.C_BPartner_ID)"
				+ " AND u.EMail=?)"					//	#1 EMail
			+ " AND (SerNo IS NULL OR SerNo=?)";	//	#2 Name
		
		
		
		
		super.setA_Asset_ID (A_Asset_ID);
	}	//	setA_Asset_ID
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MIssueProject[");
		sb.append (get_ID())
			.append ("-").append (getName())
			.append(",A_Asset_ID=").append(getA_Asset_ID())
			.append(",C_Project_ID=").append(getC_Project_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MIssueProject
