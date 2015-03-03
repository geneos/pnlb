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
 * 	Operating Task Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MTask.java,v 1.2 2005/09/19 04:49:45 jjanke Exp $
 */
public class MTask extends X_AD_Task
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Task_ID id
	 *	@param trxName trx
	 */
	public MTask (Properties ctx, int AD_Task_ID, String trxName)
	{
		super (ctx, AD_Task_ID, trxName);
	}	//	MTask

	/**
	 * 	Load Cosntructor
	 *	@param ctx ctx
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MTask (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MTask
	
	/**	Actual Task			*/
	private Task m_task = null;
	
	/**
	 * 	Execute Task and wait
	 *	@return execution info
	 */
	public String execute()
	{
		String cmd = Msg.parseTranslation(Env.getCtx(), getOS_Command()).trim();
		if (cmd == null || cmd.equals(""))
			return "Cannot execute '" + getOS_Command() + "'";
		//
		if (isServerProcess())
			return executeRemote(cmd);
		return executeLocal(cmd);
	}	//	execute
	
	/**
	 * 	Execute Task locally and wait
	 * 	@param cmd command
	 *	@return execution info
	 */
	public String executeLocal(String cmd)
	{
		log.config(cmd);
		if (m_task != null && m_task.isAlive())
			m_task.interrupt();

		m_task = new Task(cmd);
		m_task.start();

		StringBuffer sb = new StringBuffer();
		while (true)
		{
			//  Give it a bit of time
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException ioe)
			{
				log.log(Level.SEVERE, cmd, ioe);
			}
			//  Info to user
			sb.append(m_task.getOut())
				.append("\n-----------\n")
				.append(m_task.getErr())
				.append("\n-----------");

			//  Are we done?
			if (!m_task.isAlive())
				break;
		}
		log.config("done");
		return sb.toString();
	}	//	executeLocal
	
	/**
	 * 	Execute Task locally and wait
	 * 	@param cmd command
	 *	@return execution info
	 */
	public String executeRemote(String cmd)
	{
		log.config(cmd);
		return "Remote:\n";
	}	//	executeRemote
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MTask[");
		sb.append(get_ID())
			.append("-").append(getName())
			.append(";Server=").append(isServerProcess())
			.append(";").append(getOS_Command())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MTask
