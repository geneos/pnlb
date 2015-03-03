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
 *	Private Access
 *	
 *  @author Jorg Janke
 *  @version $Id: MPrivateAccess.java,v 1.10 2005/10/26 00:38:15 jjanke Exp $
 */
public class MPrivateAccess extends X_AD_Private_Access
{
	/**
	 * 	Load Pricate Access
	 *	@param ctx context 
	 *	@param AD_User_ID user
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@return access or null if not found
	 */
	public static MPrivateAccess get (Properties ctx, int AD_User_ID, int AD_Table_ID, int Record_ID)
	{
		MPrivateAccess retValue = null;
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM AD_Private_Access WHERE AD_User_ID=? AND AD_Table_ID=? AND Record_ID=?";
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_User_ID);
			pstmt.setInt(2, AD_Table_ID);
			pstmt.setInt(3, Record_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MPrivateAccess (ctx, rs, null); 
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "MPrivateAccess", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return retValue;
	}	//	get

	/**
	 * 	Get Where Clause of Locked Records for Table
	 *	@param AD_Table_ID table
	 *	@param AD_User_ID user requesting info
	 *	@return "<>1" or " NOT IN (1,2)" or null
	 */
	public static String getLockedRecordWhere (int AD_Table_ID, int AD_User_ID)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		PreparedStatement pstmt = null;
		String sql = "SELECT Record_ID FROM AD_Private_Access WHERE AD_Table_ID=? AND AD_User_ID<>? AND IsActive='Y'";
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Table_ID);
			pstmt.setInt(2, AD_User_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new Integer(rs.getInt(1))); 
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		//
		if (list.size() == 0)
			return null;
		if (list.size() == 1)
			return "<>" + list.get(0);
		//
		StringBuffer sb = new StringBuffer(" NOT IN(");
		for (int i = 0; i < list.size(); i++)
		{
			if (i > 0)
				sb.append(",");
			sb.append(list.get(i));
		}
		sb.append(")");
		return sb.toString();
	}	//	get

	
	/**	Logger					*/
	private static CLogger		s_log = CLogger.getCLogger(MPrivateAccess.class);
	
	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MPrivateAccess (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MPrivateAccess

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPrivateAccess(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPrivateAccess

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param AD_User_ID user
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 */
	public MPrivateAccess (Properties ctx, int AD_User_ID, int AD_Table_ID, int Record_ID)
	{
		super(ctx, 0, null);
		setAD_User_ID (AD_User_ID);
		setAD_Table_ID (AD_Table_ID);
		setRecord_ID (Record_ID);
	}	//	MPrivateAccess

}	//	MPrivateAccess
