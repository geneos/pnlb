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
 *	Accounting Processor Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAcctProcessor.java,v 1.10 2005/11/06 01:17:27 jjanke Exp $
 */
public class MAcctProcessor extends X_C_AcctProcessor
	implements CompiereProcessor
{
	/**
	 * 	Get Active
	 *	@param ctx context
	 *	@return active processors
	 */
	public static MAcctProcessor[] getActive (Properties ctx)
	{
		ArrayList<MAcctProcessor> list = new ArrayList<MAcctProcessor>();
		String sql = "SELECT * FROM C_AcctProcessor WHERE IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MAcctProcessor (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getActive", e);
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
		MAcctProcessor[] retValue = new MAcctProcessor[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getActive

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MAcctProcessor.class);

	
	/**
	 * 	Standard Construvtor
	 *	@param ctx context
	 *	@param C_AcctProcessor_ID id
	 */
	public MAcctProcessor (Properties ctx, int C_AcctProcessor_ID, String trxName)
	{
		super (ctx, C_AcctProcessor_ID, trxName);
		if (C_AcctProcessor_ID == 0)
		{
		//	setName (null);
		//	setSupervisor_ID (0);
			setFrequencyType (FREQUENCYTYPE_Hour);
			setFrequency (1);
			setKeepLogDays (7);	// 7
		}	
	}	//	MAcctProcessor

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAcctProcessor (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAcctProcessor

	/**
	 * 	Parent Constructor
	 *	@param client parent
	 *	@param Supervisor_ID admin
	 */
	public MAcctProcessor (MClient client, int Supervisor_ID)
	{
		this (client.getCtx(), 0, client.get_TrxName());
		setClientOrg(client);
		setName (client.getName() + " - " 
			+ Msg.translate(getCtx(), "C_AcctProcessor_ID"));
		setSupervisor_ID (Supervisor_ID);
	}	//	MAcctProcessor
	
	
	
	/**
	 * 	Get Server ID
	 *	@return id
	 */
	public String getServerID ()
	{
		return "AcctProcessor" + get_ID();
	}	//	getServerID

	/**
	 * 	Get Date Next Run
	 *	@param requery requery
	 *	@return date next run
	 */
	public Timestamp getDateNextRun (boolean requery)
	{
		if (requery)
			load(get_TrxName());
		return getDateNextRun();
	}	//	getDateNextRun

	/**
	 * 	Get Logs
	 *	@return logs
	 */
	public CompiereProcessorLog[] getLogs ()
	{
		ArrayList<MAcctProcessorLog> list = new ArrayList<MAcctProcessorLog>();
		String sql = "SELECT * "
			+ "FROM C_AcctProcessorLog "
			+ "WHERE C_AcctProcessor_ID=? " 
			+ "ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_AcctProcessor_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MAcctProcessorLog (getCtx(), rs, get_TrxName()));
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
		MAcctProcessorLog[] retValue = new MAcctProcessorLog[list.size ()];
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
		String sql = "DELETE C_AcctProcessorLog "
			+ "WHERE C_AcctProcessor_ID=" + getC_AcctProcessor_ID() 
			+ " AND (Created+" + getKeepLogDays() + ") < SysDate";
		int no = DB.executeUpdate(sql, get_TrxName());
		return 0;
	}	//	deleteLog

}	//	MAcctProcessor
