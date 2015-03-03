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
package org.compiere.process;

import java.util.logging.*;
import org.compiere.model.*;

/**
 *	Compiere Service.
 *	Instanciates and Controls the Compiere Server, 
 *	which actually does the work in separate thread
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereService.java,v 1.4 2005/03/11 20:29:06 jjanke Exp $
 */
public class CompiereService extends StateEngine
{
	/**
	 * 	Compiere Service
	 * 	@param processor Processor instance
	 *	@param serverClass server class
	 */
	public CompiereService (PO processor, Class serverClass)
	{
		super ();
		m_processor = processor;
		m_serverClass = serverClass;
	}	//	CompiereServer

	/**	Compiere Server(s)			*/
	private CompiereServer	m_server = null;
	/**	Compiere Server	Class		*/
	private Class			m_serverClass = null;
	/** Compiere Server Processor Instance	*/ 
	private PO				m_processor = null;
	

	/**
	 * 	Get Compier Server
	 *	@return Compiere Server
	 */
	public CompiereServer getCompierServer()
	{
		getState();
		return m_server;
	}	//	getCompiereServer
	
	/**
	 * 	Get/Check State
	 *	@return state
	 */
	public String getState ()
	{
		if (isRunning())
		{
			if (m_server == null || !m_server.isAlive())
				terminate();
		}
		return super.getState ();
	}	//	getState

	/**
	 * 	Start: not started -> running
	 *	@return true if set to running
	 */
	public boolean start()
	{
		if (!super.start())
			return false;
		
		boolean ok = false;
		try
		{
			m_server = (CompiereServer)m_serverClass.newInstance();
			m_server.setProcessor (m_processor);
			m_server.start();
			ok = true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "start", e);
			ok = false;
		}
		if (!ok)
			return abort();
		log.info("start - " + ok);
		getState();
		return ok;
	}	//	start

	/**
	 * 	Resume: suspended -> running
	 *	@return true if set to sunning
	 */
	public boolean resume()
	{
		if (!super.resume())
			return false;
		
		boolean ok = false;
		try
		{
			m_server = (CompiereServer)m_serverClass.newInstance();
			m_server.setProcessor (m_processor);
			m_server.start();
			ok = true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "resume", e);
			ok = false;
		}
		if (!ok)
			return abort();
		log.info("resume - " + ok);
		getState();
		return ok;
	}	//	resume
	
	
	/**
	 * 	Complete: running -> completed
	 *	@return true if set to completed
	 */
	public boolean complete()
	{
		if (!super.complete())
			return false;
		
		boolean ok = false;
		if (m_server != null && m_server.isAlive())
		{
			try
			{
				m_server.interrupt();
				m_server.join();
				ok = true;
			}
			catch (Exception e)
			{
				return abort();
			}
		}
		log.info("complete - " + ok);
		return ok;
	}	//	complete

	/**
	 * 	Suspend: running -> suspended
	 *	@return true if suspended
	 */
	public boolean suspend()
	{
		if (!super.suspend())
			return false;
		
		boolean ok = false;
		if (m_server != null && m_server.isAlive())
		{
			try
			{
				m_server.interrupt();
				m_server.join();
				ok = true;
			}
			catch (Exception e)
			{
				return abort();
			}
		}
		log.info("suspend - " + ok);
		return ok;
	}	//	suspend
	

	/**
	 * 	Abort: open -> aborted
	 *	@return true if set to aborted
	 */
	public boolean abort()	//	raises CannotStop, NotRunning
	{
		if (super.abort())
		{
			if (m_server != null && m_server.isAlive())
			{
				try
				{
					m_server.interrupt();
				}
				catch (Exception e)
				{
				}
			}
			log.info("abort - done");
			return true;
		}
		return false;
	}	//	abort
	
	/**
	 * 	Terminate (System Error): open -> terminated
	 *	@return true if set to terminated
	 */
	public boolean terminate()
	{
		if (super.terminate())
		{
			if (m_server != null && m_server.isAlive())
			{
				try
				{
					m_server.interrupt();
				}
				catch (Exception e)
				{
				}
			}
			log.info("terminate - done");
			return true;
		}
		return false;
	}	//	terminate
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("CompiereService[");
		sb.append(getStateInfo())
			.append(" - ").append(m_server);
		sb.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	CompiereService
