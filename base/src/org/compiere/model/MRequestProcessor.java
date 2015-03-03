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
 *	Request Processor Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRequestProcessor.java,v 1.13 2005/11/14 02:10:52 jjanke Exp $
 */
public class MRequestProcessor extends X_R_RequestProcessor 
	implements CompiereProcessor
{
	/**
	 * 	Get Active Request Processors
	 *	@param ctx context
	 *	@return array of Request 
	 */
	public static MRequestProcessor[] getActive (Properties ctx)
	{
		ArrayList<MRequestProcessor> list = new ArrayList<MRequestProcessor>();
		String sql = "SELECT * FROM R_RequestProcessor WHERE IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRequestProcessor (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
		MRequestProcessor[] retValue = new MRequestProcessor[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getActive
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MRequestProcessor.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_RequestProcessor_ID id
	 */
	public MRequestProcessor (Properties ctx, int R_RequestProcessor_ID, String trxName)
	{
		super (ctx, R_RequestProcessor_ID, trxName);
		if (R_RequestProcessor_ID == 0)
		{
		//	setName (null);
			setFrequencyType (FREQUENCYTYPE_Day);
			setFrequency (0);
			setKeepLogDays (7);
			setOverdueAlertDays (0);
			setOverdueAssignDays (0);
			setRemindDays (0);
		//	setSupervisor_ID (0);
		}	
	}	//	MRequestProcessor

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRequestProcessor (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRequestProcessor

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param Supervisor_ID Supervisor
	 */
	public MRequestProcessor (MClient parent, int Supervisor_ID)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setName (parent.getName() + " - " 
			+ Msg.translate(getCtx(), "R_RequestProcessor_ID"));
		setSupervisor_ID (Supervisor_ID);
	}	//	MRequestProcessor
	
	
	/**	The Lines						*/
	private MRequestProcessorRoute[]	m_routes = null;

	/**
	 * 	Get Routes
	 *	@param reload reload data
	 *	@return array of routes
	 */
	public MRequestProcessorRoute[] getRoutes (boolean reload)
	{
		if (m_routes != null && !reload)
			return m_routes;
		
		String sql = "SELECT * FROM R_RequestProcessor_Route WHERE R_RequestProcessor_ID=? ORDER BY SeqNo";
		ArrayList<MRequestProcessorRoute> list = new ArrayList<MRequestProcessorRoute>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getR_RequestProcessor_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRequestProcessorRoute (getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		//
		m_routes = new MRequestProcessorRoute[list.size ()];
		list.toArray (m_routes);
		return m_routes;
	}	//	getRoutes
	
	/**
	 * 	Get Logs
	 *	@return Array of Logs
	 */
	public CompiereProcessorLog[] getLogs()
	{
		ArrayList<MRequestProcessorLog> list = new ArrayList<MRequestProcessorLog>();
		String sql = "SELECT * "
			+ "FROM R_RequestProcessorLog "
			+ "WHERE R_RequestProcessor_ID=? " 
			+ "ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getR_RequestProcessor_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRequestProcessorLog (getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		MRequestProcessorLog[] retValue = new MRequestProcessorLog[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getLogs
	
	/**
	 * 	Delete old Request Log
	 *	@return number of records
	 */
	public int deleteLog()
	{
		if (getKeepLogDays() < 1)
			return 0;
		String sql = "DELETE R_RequestProcessorLog "
			+ "WHERE R_RequestProcessor_ID=" + getR_RequestProcessor_ID() 
			+ " AND (Created+" + getKeepLogDays() + ") < SysDate";
		int no = DB.executeUpdate(sql, get_TrxName());
		return 0;
	}	//	deleteLog
	
	/**
	 * 	Get the date Next run
	 * 	@param requery requery database
	 * 	@return date next run
	 */
	public Timestamp getDateNextRun (boolean requery)
	{
		if (requery)
			load(get_TrxName());
		return getDateNextRun();
	}	//	getDateNextRun

	/**
	 * 	Get Unique ID
	 *	@return Unique ID
	 */
	public String getServerID()
	{
		return "RequestProcessor" + get_ID();
	}	//	getServerID
	
}	//	MRequestProcessor
