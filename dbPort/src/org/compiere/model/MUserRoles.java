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
 *	User Roles Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MUserRoles.java,v 1.8 2005/10/26 00:38:17 jjanke Exp $
 */
public class MUserRoles extends X_AD_User_Roles
{
	/**
	 * 	Get User Roles Of Role
	 *	@param ctx context
	 *	@param AD_Role_ID role
	 *	@return array of user roles
	 */
	public static MUserRoles[] getOfRole (Properties ctx, int AD_Role_ID)
	{
		String sql = "SELECT * FROM AD_User_Roles WHERE AD_Role_ID=?";
		ArrayList<MUserRoles> list = new ArrayList<MUserRoles>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_Role_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MUserRoles (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getOfRole", e);
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
		MUserRoles[] retValue = new MUserRoles[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfRole

	/**
	 * 	Get User Roles Of User
	 *	@param ctx context
	 *	@param AD_User_ID role
	 *	@return array of user roles
	 */
	public static MUserRoles[] getOfUser (Properties ctx, int AD_User_ID)
	{
		String sql = "SELECT * FROM AD_User_Roles WHERE AD_User_ID=?";
		ArrayList<MUserRoles> list = new ArrayList<MUserRoles>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_User_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MUserRoles (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getOfUser", e);
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
		MUserRoles[] retValue = new MUserRoles[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfUser

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MUserRoles.class);

	
	/**************************************************************************
	 * 	Persistence Constructor
	 *	@param ctx context
	 *	@param ignored invalid
	 */
	public MUserRoles (Properties ctx, int ignored, String trxName)
	{
		super (ctx, ignored, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MUserRoles

	/**
	 * 	Load constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MUserRoles (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MUserRoles

	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param AD_User_ID user
	 *	@param AD_Role_ID role
	 */
	public MUserRoles (Properties ctx, int AD_User_ID, int AD_Role_ID, String trxName)
	{
		this (ctx, 0, trxName);
		setAD_User_ID(AD_User_ID);
		setAD_Role_ID(AD_Role_ID);
	}	//	MUserRoles
	
	
}	//	MUserRoles
