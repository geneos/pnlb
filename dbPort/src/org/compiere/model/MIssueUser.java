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
 * 	Issue User Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MIssueUser.java,v 1.1 2006/01/03 02:40:04 jjanke Exp $
 */
public class MIssueUser extends X_R_IssueUser
{
	/**
	 * 	Get/Set User for Issue
	 *	@param issue issue
	 *	@return User
	 */
	static public MIssueUser get (MIssue issue)
	{
		if (issue.getUserName() == null)
			return null;
		MIssueUser user = null;
		//	Find Issue User
		String sql = "SELECT * FROM R_IssueUser WHERE UserName=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setString (1, issue.getUserName());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				user = new MIssueUser (issue.getCtx(), rs, null);
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
		if (user == null)
		{
			user = new MIssueUser(issue.getCtx(), 0, null);
			user.setUserName(issue.getUserName());
			user.setAD_User_ID();
			if (!user.save())
				return null;
		}
		
		issue.setR_IssueUser_ID(user.getR_IssueUser_ID());
		return user;
	}	//	MIssueUser
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MIssueUser.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_IssueUser_ID id
	 *	@param trxName trx
	 */
	public MIssueUser (Properties ctx, int R_IssueUser_ID, String trxName)
	{
		super (ctx, R_IssueUser_ID, trxName);
	}	//	MIssueUser

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MIssueUser (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MIssueUser
	
	
	/**
	 * 	Set AD_User_ID
	 */
	public void setAD_User_ID ()
	{
		int AD_User_ID = DB.getSQLValue(null, 
			"SELECT AD_User_ID FROM AD_User WHERE EMail=?", getUserName());
		if (AD_User_ID != 0)
			super.setAD_User_ID (AD_User_ID);
	}	//	setAD_User_ID
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MIssueUser[");
		sb.append (get_ID())
			.append ("-").append(getUserName())
			.append(",AD_User_ID=").append(getAD_User_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
}	//	MIssueUser
