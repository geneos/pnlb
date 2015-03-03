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
package org.compiere.wf;

import java.sql.*;
import java.util.*;
import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Workflow Processor Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MWorkflowProcessor.java,v 1.10 2005/11/10 18:15:48 jjanke Exp $
 */
public class MWorkflowProcessor extends X_AD_WorkflowProcessor
	implements CompiereProcessor
{
	/**
	 * 	Get Active
	 *	@param ctx context
	 *	@return active processors
	 */
	public static MWorkflowProcessor[] getActive (Properties ctx)
	{
		ArrayList<MWorkflowProcessor> list = new ArrayList<MWorkflowProcessor>();
		String sql = "SELECT * FROM AD_WorkflowProcessor WHERE IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MWorkflowProcessor (ctx, rs, null));
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
		MWorkflowProcessor[] retValue = new MWorkflowProcessor[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getActive
	
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MWorkflowProcessor.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_WorkflowProcessor_ID id
	 */
	public MWorkflowProcessor (Properties ctx, int AD_WorkflowProcessor_ID, String trxName)
	{
		super (ctx, AD_WorkflowProcessor_ID, trxName);
	}	//	MWorkflowProcessor

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MWorkflowProcessor (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MWorkflowProcessor

	/**
	 * 	Get Server ID
	 *	@return id
	 */
	public String getServerID ()
	{
		return "WorkflowProcessor" + get_ID();
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
		ArrayList<MWorkflowProcessorLog> list = new ArrayList<MWorkflowProcessorLog>();
		String sql = "SELECT * "
			+ "FROM AD_WorkflowProcessorLog "
			+ "WHERE AD_WorkflowProcessor_ID=? " 
			+ "ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getAD_WorkflowProcessor_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MWorkflowProcessorLog (getCtx(), rs, get_TrxName()));
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
		MWorkflowProcessorLog[] retValue = new MWorkflowProcessorLog[list.size ()];
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
		String sql = "DELETE AD_WorkflowProcessorLog "
			+ "WHERE AD_WorkflowProcessor_ID=" + getAD_WorkflowProcessor_ID() 
			+ " AND (Created+" + getKeepLogDays() + ") < SysDate";
		int no = DB.executeUpdate(sql, get_TrxName());
		return 0;
	}	//	deleteLog

}	//	MWorkflowProcessor
