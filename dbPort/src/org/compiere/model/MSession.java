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

import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Session Model.
 *	Maintained in AMenu.
 *	
 *  @author Jorg Janke
 *  @version $Id: MSession.java,v 1.15 2005/10/26 00:38:17 jjanke Exp $
 */
public class MSession extends X_AD_Session
{
	/**
	 * 	Get existing or create local session
	 *	@param ctx context
	 *	@param createNew create if not found
	 *	@return session session
	 */
	public static MSession get (Properties ctx, boolean createNew)
	{
		int AD_Session_ID = Env.getContextAsInt(ctx, "#AD_Session_ID");
		MSession session = null;
		if (AD_Session_ID > 0)
			session = (MSession)s_sessions.get(new Integer(AD_Session_ID));
		if (session == null && createNew)
		{
			session = new MSession (ctx, null);	//	local session
			session.save();
			AD_Session_ID = session.getAD_Session_ID();
			Env.setContext (ctx, "#AD_Session_ID", AD_Session_ID);
			s_sessions.put (new Integer(AD_Session_ID), session);
		}	
		return session;
	}	//	get
	
	/**
	 * 	Get existing or create remote session
	 *	@param ctx context
	 */
	public static MSession get (Properties ctx, String Remote_Addr, String Remote_Host, String WebSession)
	{
		int AD_Session_ID = Env.getContextAsInt(ctx, "#AD_Session_ID");
		MSession session = null;
		if (AD_Session_ID > 0)
			session = (MSession)s_sessions.get(new Integer(AD_Session_ID));
		if (session == null)
		{
			session = new MSession (ctx, Remote_Addr, Remote_Host, WebSession, null);	//	remote session
			session.save();
			AD_Session_ID = session.getAD_Session_ID();
			Env.setContext(ctx, "#AD_Session_ID", AD_Session_ID);
			s_sessions.put(new Integer(AD_Session_ID), session);
		}	
		return session;
	}	//	get

	/**	Sessions					*/
	private static CCache<Integer, MSession> s_sessions = Ini.isClient() 
		? new CCache<Integer, MSession>("AD_Session_ID", 1, 0)		//	one client session 
		: new CCache<Integer, MSession>("AD_Session_ID", 30, 0);	//	no time-out	
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Session_ID id
	 */
	public MSession (Properties ctx, int AD_Session_ID, String trxName)
	{
		super(ctx, AD_Session_ID, trxName);
		if (AD_Session_ID == 0)
		{
			setProcessed (false);
		}
	}	//	MSession

	/**
	 * 	Load Costructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MSession(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MSession

	/**
	 * 	New (remote) Constructor
	 *	@param ctx context
	 */
	public MSession (Properties ctx, String Remote_Addr, String Remote_Host, String WebSession, String trxName)
	{
		this (ctx, 0, trxName);
		if (Remote_Addr != null)
			setRemote_Addr(Remote_Addr);
		if (Remote_Host != null)
			setRemote_Host(Remote_Host);
		if (WebSession != null)
			setWebSession(WebSession);
	}	//	MSession

	/**
	 * 	New (local) Constructor
	 *	@param ctx context
	 */
	public MSession (Properties ctx, String trxName)
	{
		this (ctx, 0, trxName);
		try
		{
			InetAddress lh = InetAddress.getLocalHost();
			setRemote_Addr(lh.getHostAddress());
			setRemote_Host(lh.getHostName());
		}
		catch (UnknownHostException e)
		{
			log.log(Level.SEVERE, "MSession - No Local Host", e);
		}
	}	//	MSession

	/**	Web Store Session		*/
	private boolean		m_webStoreSession = false;
	
	/**
	 * 	Is it a Web Store Session
	 *	@return Returns true if Web Store Session.
	 */
	public boolean isWebStoreSession ()
	{
		return m_webStoreSession;
	}	//	isWebStoreSession
	
	/**
	 * 	Set Web Store Session
	 *	@param webStoreSession The webStoreSession to set.
	 */
	public void setWebStoreSession (boolean webStoreSession)
	{
		m_webStoreSession = webStoreSession;
	}	//	setWebStoreSession
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MSession[")
			.append(getAD_Session_ID())
			.append(",AD_User_ID=").append(getCreatedBy())
			.append(",").append(getCreated())
			.append(",Remote=").append(getRemote_Addr());
		String s = getRemote_Host();
		if (s != null && s.length() > 0)
			sb.append(",").append(s);
		if (m_webStoreSession)
			sb.append(",WebStoreSession");
		sb.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Session Logout
	 */
	public void logout()
	{
		setProcessed(true);
		save();
		s_sessions.remove(new Integer(getAD_Session_ID()));
		log.info("logout - " + TimeUtil.formatElapsed(getCreated(), getUpdated()));
	}	//	logout


	/**
	 * 	Create Change Log only if table is logged
	 * 	@param TrxName transaction name
	 *	@param AD_ChangeLog_ID 0 for new change log
	 *	@param AD_Table_ID table
	 *	@param AD_Column_ID column
	 *	@param Record_ID record
	 *	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 *	@param OldValue old
	 *	@param NewValue new
	 *	@return saved change log or null
	 */
	public MChangeLog changeLog (
		String TrxName, int AD_ChangeLog_ID,
		int AD_Table_ID, int AD_Column_ID, int Record_ID,
		int AD_Client_ID, int AD_Org_ID,
		Object OldValue, Object NewValue)
	{
		//	Null handling
		if (OldValue == null && NewValue == null)
			return null;
		//	Equal Value
		if (OldValue != null && NewValue != null && OldValue.equals(NewValue))
			return null;

		//	No Log
		if (AD_Column_ID == 6652 
			|| AD_Column_ID == 6653)	//	AD_Process.Statistics_
			return null;
		
		//	Role Logging
		MRole role = MRole.getDefault(getCtx(), false);
		//	Do we need to log
		if (m_webStoreSession						//	log if WebStore
			|| MChangeLog.isLogged(AD_Table_ID)		//	im/explicit log
			|| (role != null && role.isChangeLog()))//	Role Logging
			;
		else
			return null;
		//
		log.finest("AD_ChangeLog_ID=" + AD_ChangeLog_ID
				+ ", AD_Session_ID=" + getAD_Session_ID()
				+ ", AD_Table_ID=" + AD_Table_ID + ", AD_Column_ID=" + AD_Column_ID
				+ ": " + OldValue + " -> " + NewValue);
		boolean success = false;
		
		try
		{
			MChangeLog cl = new MChangeLog(getCtx(), 
				AD_ChangeLog_ID, TrxName, getAD_Session_ID(),
				AD_Table_ID, AD_Column_ID, Record_ID, AD_Client_ID, AD_Org_ID,
				OldValue, NewValue);
			if (cl.save())
				return cl;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "AD_ChangeLog_ID=" + AD_ChangeLog_ID
				+ ", AD_Session_ID=" + getAD_Session_ID()
				+ ", AD_Table_ID=" + AD_Table_ID + ", AD_Column_ID=" + AD_Column_ID, e);
			return null;
		}
		log.log(Level.SEVERE, "AD_ChangeLog_ID=" + AD_ChangeLog_ID
			+ ", AD_Session_ID=" + getAD_Session_ID()
			+ ", AD_Table_ID=" + AD_Table_ID + ", AD_Column_ID=" + AD_Column_ID);
		return null;
	}	//	changeLog

}	//	MSession

