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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Process Instance Model
 *
 *  @author Jorg Janke
 *  @version $Id: MPInstance.java,v 1.18 2005/11/14 02:11:18 jjanke Exp $
 */
public class MPInstance extends X_AD_PInstance
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_PInstance_ID instance or 0
	 */
	public MPInstance (Properties ctx, int AD_PInstance_ID, String ignored)
	{
		super (ctx, AD_PInstance_ID, null);
		//	New Process
		if (AD_PInstance_ID == 0)
		{
		//	setAD_Process_ID (0);	//	parent
		//	setRecord_ID (0);
			setIsProcessing (false);
		}
	}	//	MPInstance

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPInstance (Properties ctx, ResultSet rs, String ignored)
	{
		super(ctx, rs, null);
	}	//	MPInstance

	/**
	 * 	Create Process Instance from Process and create parameters
	 *	@param process process
	 *	@param Record_ID Record
	 */
	public MPInstance (MProcess process, int Record_ID)
	{
		this (process.getCtx(), 0, null);
		setAD_Process_ID (process.getAD_Process_ID());
		setRecord_ID (Record_ID);
		setAD_User_ID(Env.getAD_User_ID(process.getCtx()));
		if (!save())		//	need to save for parameters
			throw new IllegalArgumentException ("Cannot Save");
		//	Set Parameter Base Info
		MProcessPara[] para = process.getParameters();
		for (int i = 0; i < para.length; i++)
		{
			MPInstancePara pip = new MPInstancePara (this, para[i].getSeqNo());
			pip.setParameterName(para[i].getColumnName());
			pip.setInfo(para[i].getName());
			pip.save();
		}
	}	//	MPInstance

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param AD_Process_ID Process ID
	 *	@param Record_ID record
	 */
	public MPInstance (Properties ctx, int AD_Process_ID, int Record_ID)
	{
		this(ctx, 0, null);
		setAD_Process_ID (AD_Process_ID);
		setRecord_ID (Record_ID);
		setAD_User_ID(Env.getAD_User_ID(ctx));
		setIsProcessing (false);
	}	//	MPInstance
	

	/**	Parameters						*/
	private MPInstancePara[]		m_parameters = null;

	/**
	 * 	Get Parameters
	 *	@return parameter array
	 */
	public MPInstancePara[] getParameters()
	{
		if (m_parameters != null)
			return m_parameters;
		ArrayList<MPInstancePara> list = new ArrayList<MPInstancePara>();
		//
		String sql = "SELECT * FROM AD_PInstance_Para WHERE AD_PInstance_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, getAD_PInstance_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				list.add(new MPInstancePara(getCtx(), rs, null));
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		//
		m_parameters = new MPInstancePara[list.size()];
		list.toArray(m_parameters);
		return m_parameters;
	}	//	getParameters


	/**	Log Entries					*/
	private ArrayList<MPInstanceLog>	m_log	= new ArrayList<MPInstanceLog>();

	/**
	 *	Get Logs
	 *	@return array of logs
	 */
	public MPInstanceLog[] getLog()
	{
		//	load it from DB
		m_log.clear();
		String sql = "SELECT * FROM AD_PInstance_Log WHERE AD_PInstance_ID=? ORDER BY Log_ID";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, getAD_PInstance_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				m_log.add(new MPInstanceLog(rs));
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}

		MPInstanceLog[] retValue = new MPInstanceLog[m_log.size()];
		m_log.toArray(retValue);
		return retValue;
	}	//	getLog

	/**
	 *	@param P_Date date
	 *	@param P_ID id
	 *	@param P_Number number
	 *	@param P_Msg msg
	 */
	public void addLog (Timestamp P_Date, int P_ID, BigDecimal P_Number, String P_Msg)
	{
		MPInstanceLog logEntry = new MPInstanceLog (getAD_PInstance_ID(), m_log.size()+1,
			P_Date, P_ID, P_Number, P_Msg);
		m_log.add(logEntry);
		//	save it to DB ?
	//	log.save();
	}	//	addLog

	
	/**
	 * 	Set AD_Process_ID.
	 * 	Check Role if process can be performed
	 *	@param AD_Process_ID process
	 */
	public void setAD_Process_ID (int AD_Process_ID)
	{
		int AD_Role_ID = Env.getAD_Role_ID(getCtx());
		if (AD_Role_ID != 0)
		{
			MRole role = MRole.get(getCtx(), AD_Role_ID);
			Boolean access = role.getProcessAccess(AD_Process_ID);
			if (access == null || !access.booleanValue())
				throw new IllegalAccessError("Cannot access Process with " + role.getName());
		}
		super.setAD_Process_ID (AD_Process_ID);
	}	//	setAD_Process_ID

	/**
	 * 	String Representation
	 *	@see java.lang.Object#toString()
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPInstance[")
			.append (get_ID())
			.append(",OK=").append(isOK());
		String msg = getErrorMsg();
		if (msg != null && msg.length() > 0)
			sb.append(msg);
		sb.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Dump Log
	 */
	public void log()
	{
		log.info(toString());
		MPInstanceLog[] pil = getLog();
		for (int i = 0; i < pil.length; i++)
			log.info(i + "=" + pil[i]);
	}	//	log


	public static final int		RESULT_OK = 1;
	public static final int		RESULT_ERROR = 0;

	/**
	 * 	Is it OK
	 *	@return Result == OK
	 */
	public boolean isOK()
	{
		return getResult() == RESULT_OK;
	}	//	isOK
	
	/**
	 * 	Set Result
	 *	@param ok 
	 */
	public void setResult (boolean ok)
	{
		super.setResult (ok ? RESULT_OK : RESULT_ERROR);
	}	//	setResult
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Update Statistics
		if (!newRecord 
			&& !isProcessing()
			&& is_ValueChanged("IsProcessing"))
		{
			long ms = System.currentTimeMillis() - getCreated().getTime();
			int seconds = (int)(ms / 1000);
			if (seconds < 1)
				seconds = 1;
			MProcess prc = MProcess.get(getCtx(), getAD_Process_ID());
			prc.addStatistics(seconds);
			if (prc.get_ID() != 0 && prc.save())
				log.fine("afterSave - Process Statistics updated Sec=" + seconds);
			else
				log.warning("afterSave - Process Statistics not updated");
		}
		return success;
	}	//	afterSave
	
}	//	MPInstance