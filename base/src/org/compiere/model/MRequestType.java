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
 *	Request Type Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRequestType.java,v 1.14 2005/12/17 19:55:33 jjanke Exp $
 */
public class MRequestType extends X_R_RequestType
{
	/**
	 * 	Get Request Type (cached)
	 *	@param ctx context
	 *	@param R_RequestType_ID id
	 *	@return Request Type
	 */
	public static MRequestType get (Properties ctx, int R_RequestType_ID)
	{
		Integer key = new Integer (R_RequestType_ID);
		MRequestType retValue = (MRequestType)s_cache.get(key);
		if (retValue == null)
		{
			retValue = new MRequestType (ctx, R_RequestType_ID, null);
			s_cache.put(key, retValue);
		}
		return retValue;
	}	//	get

	/** Static Logger					*/
	private static CLogger s_log = CLogger.getCLogger(MRequestType.class);
	/**	Cache							*/
	static private CCache<Integer,MRequestType> s_cache = new CCache<Integer,MRequestType>("R_RequestType", 10);

	/**
	 * 	Get Default Request Type
	 *	@param ctx context
	 *	@return Request Type
	 */
	public static MRequestType getDefault (Properties ctx)
	{
		MRequestType retValue = null;
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		String sql = "SELECT * FROM R_RequestType "
			+ "WHERE AD_Client_ID IN (0,11) "
			+ "ORDER BY IsDefault DESC, AD_Client_ID DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MRequestType (ctx, rs, null);
				if (!retValue.isDefault())
					retValue = null;
			}
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
		return retValue;
	}	//	get


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_RequestType_ID id
	 */
	public MRequestType (Properties ctx, int R_RequestType_ID, String trxName)
	{
		super(ctx, R_RequestType_ID, trxName);
		if (R_RequestType_ID == 0)
		{
		//	setR_RequestType_ID (0);
		//	setName (null);
			setDueDateTolerance (7);
			setIsDefault (false);
			setIsEMailWhenDue (false);
			setIsEMailWhenOverdue (false);
			setIsSelfService (true);	// Y
			setAutoDueDateDays(0);
			setConfidentialType(CONFIDENTIALTYPE_CustomerConfidential);
			setIsAutoChangeRequest(false);
			setIsConfidentialInfo(false);
		}	
	}	//	MRequestType

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRequestType(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRequestType

	/** Next time stats to be created		*/
	private long m_nextStats = 0;
	
	private int m_openNo = 0;
	private int m_totalNo = 0;
	private int m_new30No = 0;
	private int m_closed30No = 0;
	
	/**
	 * 	Update Statistics
	 */
	private synchronized void updateStatistics()
	{
		if (System.currentTimeMillis() < m_nextStats)
			return;
		
		String sql = "SELECT "
			+ "(SELECT COUNT(*) FROM R_Request r"
			+ " INNER JOIN R_Status s ON (r.R_Status_ID=s.R_Status_ID AND s.IsOpen='Y') "
			+ "WHERE r.R_RequestType_ID=x.R_RequestType_ID) AS OpenNo, "
			+ "(SELECT COUNT(*) FROM R_Request r "
			+ "WHERE r.R_RequestType_ID=x.R_RequestType_ID) AS TotalNo, "
			+ "(SELECT COUNT(*) FROM R_Request r "
			+ "WHERE r.R_RequestType_ID=x.R_RequestType_ID AND Created>SysDate-30) AS New30No, "
			+ "(SELECT COUNT(*) FROM R_Request r"
			+ " INNER JOIN R_Status s ON (r.R_Status_ID=s.R_Status_ID AND s.IsClosed='Y') "
			+ "WHERE r.R_RequestType_ID=x.R_RequestType_ID AND r.Updated>SysDate-30) AS Closed30No "
			//
			+ "FROM R_RequestType x WHERE R_RequestType_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, getR_RequestType_ID());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				m_openNo = rs.getInt(1);
				m_totalNo = rs.getInt(2);
				m_new30No = rs.getInt(3);
				m_closed30No = rs.getInt(4);
			}
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
		
		m_nextStats = System.currentTimeMillis() + 3600000;		//	every hour
	}	//	updateStatistics
	
	/**
	 * 	Get Total No of requests of type
	 *	@return no
	 */
	public int getTotalNo()
	{
		updateStatistics();
		return m_totalNo;
	}

	/**
	 * 	Get Open No of requests of type
	 *	@return no
	 */
	public int getOpenNo()
	{
		updateStatistics();
		return m_openNo;
	}

	/**
	 * 	Get Closed in last 30 days of type
	 *	@return no
	 */
	public int getClosed30No()
	{
		updateStatistics();
		return m_closed30No;
	}
	
	/**
	 * 	Get New in the last 30 days of type
	 *	@return no
	 */
	public int getNew30No()
	{
		updateStatistics();
		return m_new30No;
	}
	
	/**
	 * 	Get Requests of Type
	 *	@param selfService self service
	 *	@param C_BPartner_ID id or 0 for public
	 *	@return array of requests
	 */
	public MRequest[] getRequests (boolean selfService, int C_BPartner_ID)
	{
		String sql = "SELECT * FROM R_Request WHERE R_RequestType_ID=?";
		if (selfService)
			sql += " AND IsSelfService='Y'";
		if (C_BPartner_ID == 0)
			sql += " AND ConfidentialType='A'";
		else
			sql += " AND (ConfidentialType='A' OR C_BPartner_ID=" + C_BPartner_ID + ")";
		sql += " ORDER BY DocumentNo DESC";
		//
		ArrayList<MRequest> list = new ArrayList<MRequest>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, getR_RequestType_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRequest (getCtx(), rs, null));
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
		
		MRequest[] retValue = new MRequest[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getRequests
	
	/**
	 * 	Get public Requests of Type
	 *	@return array of requests
	 */
	public MRequest[] getRequests ()
	{
		return getRequests(true, 0);
	}	//	getRequests
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MRequestType[");
		sb.append(get_ID()).append("-").append(getName())
			.append ("]");
		return sb.toString();
	}	//	toString
	
}	//	MRequestType
