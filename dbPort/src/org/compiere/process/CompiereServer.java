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

import java.sql.*;

import java.util.logging.*;
import org.compiere.util.*;
import org.compiere.model.*;


/**
 *	Compiere Server Base
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereServer.java,v 1.4 2005/03/11 20:29:05 jjanke Exp $
 */
public abstract class CompiereServer extends Thread
{
	/**
	 * 	CompiereServer
	 *	@param name server name
	 */
	public CompiereServer (String name)
	{
		super (s_threadGroup, name);
	}	//	CompiereServer

	/**	Thread Group			*/
	private static ThreadGroup	s_threadGroup = new ThreadGroup("CompiereServer");
	
	/**	Logger					*/
	protected CLogger			log = CLogger.getCLogger(getClass());
	/** Working Status			*/
	private volatile boolean 	m_working = false; 
	/** Working Count			*/
	private int	 				m_count = 0; 
	/** Poll Count				*/
	private int	 				m_pollCount = 0; 
	/** Working Time (ms)		*/
	private volatile int	 	m_time = 0; 
	/** Work Start				*/
	private volatile long	 	m_start = 0; 
	/** Last Work Start			*/
	private volatile long	 	m_lastStart = 0; 
	/**	Sleep Seconds			*/
	private int					m_sleepSeconds = 10;  
	/**	Processor Instance		*/
	protected PO				p_processor = null;
	/** Server can continue		*/
	private boolean 			m_canContinue = true;

	
	/**
	 * 	Is Working
	 *	@return true if working
	 */
	public boolean isWorking()
	{
		return m_working;
	}	//	isWorking
	
	/**
	 * 	Get Poll Count
	 *	@return number of polls
	 */
	public int getPollCount()
	{
		return m_pollCount;
	}	//	getPollCount

	/**
	 * 	Get Work Count
	 *	@return number of work runs
	 */
	public int getWorkCount()
	{
		return m_count;
	}	//	getWorkCount
	
	/**
	 * 	Get Working Time
	 *	@return working time in ms
	 */
	public int getWorkTime()
	{
		return m_time;
	}	//	getWorkTime
	
	/**
	 * 	Get Start of Server
	 *	@return start of server
	 */
	public Timestamp getStart()
	{
		if (m_start == 0)
			return null;
		return new Timestamp (m_start);
	}	//	getStart
	
	/**
	 * 	Get Last Start of Server
	 *	@return last start of server
	 */
	public Timestamp getLastStart()
	{
		if (m_lastStart == 0)
			return null;
		return new Timestamp (m_lastStart);
	}	//	getLastStart

	/**
	 * 	Get Sleep Seconds
	 * 	@return sleep seconds
	 */
	public int getSleepSeconds ()
	{
		return m_sleepSeconds;
	}	//	getSleepSeconds

	/**
	 * 	Set Sleep Seconds
	 *	@param sleepSeconds sleep seconds
	 */
	public void setSleepSeconds (int sleepSeconds)
	{
		m_sleepSeconds = sleepSeconds;
	}	//	setSleepSeconds
	
	/**
	 * 	Set Server Processor
	 *	@param processor processor
	 */
	public void setProcessor (PO processor)
	{
		p_processor = processor;
		setName(getProcessorName());
	}	//	setProcessor

	
	
	/**
	 * 	Statistics
	 *	@return info
	 */
	public String getStatistics ()
	{
		StringBuffer sb = new StringBuffer ();
		sb.append("Alive=").append(isAlive())
			.append(", Start=").append(getStart())
			.append(", WorkCount=").append(getWorkCount())
			.append(", WorkTime=").append(getWorkTime())
			.append(", PollCount=").append(getPollCount())
			.append(", Working=").append(isWorking())
			.append(", Last=").append(getLastStart())
		//	.append(", SleepSec=").append(getSleepSeconds())
		;
		return sb.toString ();
	}	//	toString

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("CompiereServer[");
		sb.append(getStatistics()).append ("]");
		return sb.toString ();
	}	//	toString
	
	/**************************************************************************
	 * 	Run - Do the Work
	 * @see java.lang.Runnable#run()
	 */
	public final void run ()
	{
		if (m_start == 0)
			m_start = System.currentTimeMillis();
		
		m_canContinue = true;
		while (m_canContinue)
		{
			if (isInterrupted())
				return;
			
			/********************/
			m_lastStart = System.currentTimeMillis();
			m_working = true;
			try
			{
				m_pollCount++;
				if (canDoWork())
				{
					m_canContinue = doWork();
					m_count++;
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "run", e);
			}
			m_working = false;
			long end = System.currentTimeMillis();
			m_time += (end - m_lastStart);
			/********************/

			if (isInterrupted())
				return;

			try
			{
				log.fine("sleeping ... " + m_sleepSeconds);
				sleep (m_sleepSeconds*1000);
			}
			catch (InterruptedException e1)
			{
				log.warning("run - " + e1.getLocalizedMessage());
				return;
			}
		}	//	while
	}	//	run
	
	/**
	 * 	Get Processor Name
	 * 	@return Processor Name
	 */
	public abstract String getProcessorName();

	/**
	 * 	Is there work for the Worker?
	 * 	@return true if doWork should be called
	 */
	public abstract boolean canDoWork();
	
	/**
	 * 	Worker - do the work
	 * 	@return true if worker can continue
	 */
	public abstract boolean doWork();
	

}	//	CompiereServer
