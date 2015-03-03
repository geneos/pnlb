/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 * 	Request Status Model
 *  @author Jorg Janke
 *  @version $Id: MStatus.java,v 1.9 2005/11/14 02:10:53 jjanke Exp $
 */
public class MStatus extends X_R_Status
{
	/**
	 * 	Get Request Status (cached)
	 *	@param ctx context
	 *	@param R_Status_ID id
	 *	@return Request Status or null
	 */
	public static MStatus get (Properties ctx, int R_Status_ID)
	{
		if (R_Status_ID == 0)
			return null;
		Integer key = new Integer (R_Status_ID);
		MStatus retValue = (MStatus)s_cache.get(key);
		if (retValue == null)
		{
			retValue = new MStatus (ctx, R_Status_ID, null);
			s_cache.put(key, retValue);
		}
		return retValue;
	}	//	get

	/**
	 * 	Get Default Request Status
	 *	@param ctx context
	 *	@return Request Type
	 */
	public static MStatus getDefault (Properties ctx)
	{
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		Integer key = new Integer(AD_Client_ID);
		MStatus retValue = (MStatus)s_cacheDefault.get(key);
		if (retValue != null)
			return retValue;
		//	Get New
		String sql = "SELECT * FROM R_Status "
			+ "WHERE AD_Client_ID=? AND IsActive='Y' AND IsDefault='Y' "
			+ "ORDER BY Value";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MStatus (ctx, rs, null);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		if (retValue != null)
			s_cacheDefault.put(key, retValue);
		return retValue;
	}	//	getDefault

	/**
	 * 	Get Closed Status
	 *	@param ctx context
	 *	@return Request Type
	 */
	public static MStatus[] getClosed (Properties ctx)
	{
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		String sql = "SELECT * FROM R_Status "
			+ "WHERE AD_Client_ID=? AND IsActive='Y' AND IsClosed='Y' "
			+ "ORDER BY Value";
		ArrayList<MStatus> list = new ArrayList<MStatus>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				list.add(new MStatus (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		MStatus[] retValue = new MStatus[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	get

	
	/** Static Logger					*/
	private static CLogger s_log = CLogger.getCLogger(MStatus.class);
	/**	Cache							*/
	static private CCache<Integer,MStatus> s_cache
		= new CCache<Integer,MStatus> ("R_Status", 10);
	/**	Default Cache (Key=Client)		*/
	static private CCache<Integer,MStatus> s_cacheDefault
		= new CCache<Integer,MStatus>("R_Status", 2);


	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param R_Status_ID is
	 *	@param trxName trx
	 */
	public MStatus (Properties ctx, int R_Status_ID, String trxName)
	{
		super (ctx, R_Status_ID, trxName);
		if (R_Status_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setIsClosed (false);	// N
			setIsDefault (false);
			setIsFinalClose (false);	// N
			setIsOpen (false);
			setIsWebCanUpdate (true);
		}
	}	//	MStatus

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MStatus (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MStatus
	

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (isOpen() && isClosed())
			setIsClosed(false);
		if (isFinalClose() && !isClosed())
			setIsFinalClose(false);
		//
		if (!isWebCanUpdate() && getUpdate_Status_ID() != 0)
			setUpdate_Status_ID(0);
		if (getTimeoutDays() == 0 && getNext_Status_ID() != 0)
			setNext_Status_ID(0);
		//
		return true;
	}	//	beforeSave
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MStatus[");
		sb.append(get_ID()).append("-").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MStatus
