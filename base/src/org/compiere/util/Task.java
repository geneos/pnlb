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
package org.compiere.util;

import java.io.*;
import java.util.logging.*;

/**
 *  Execute OS Task
 *
 *  @author     Jorg Janke
 *  @version    $Id: Task.java,v 1.8 2005/07/18 03:47:43 jjanke Exp $
 */
public class Task extends Thread
{
	/**
	 *  Create Process with cmd
	 *  @param cmd o/s command
	 */
	public Task (String cmd)
	{
		m_cmd = cmd;
	}   //  Task

	private String          m_cmd;
	private Process         m_child = null;

	private StringBuffer    m_out = new StringBuffer();
	private StringBuffer    m_err = new StringBuffer();

	/** The Output Stream of process        */
	private InputStream     m_outStream;
	/** The Error Output Stream of process  */
	private InputStream     m_errStream;
	/** The Input Stream of process         */
	private OutputStream    m_inStream;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(Task.class);
	
	/** Read Out                            */
	private Thread          m_outReader = new Thread()
	{
		public void run()
		{
			log.fine("outReader");
			try
			{
				int c;
				while ((c = m_outStream.read()) != -1 && !isInterrupted())
				{
			//		System.out.print((char)c);
					m_out.append((char)c);
				}
				m_outStream.close();
			}
			catch (IOException ioe)
			{
				log.log(Level.SEVERE, "outReader", ioe);
			}
			log.fine("outReader - done");
		}   //  run
	};   //  m_outReader

	/** Read Out                            */
	private Thread          m_errReader = new Thread()
	{
		public void run()
		{
			log.fine("errReader");
			try
			{
				int c;
				while ((c = m_errStream.read()) != -1 && !isInterrupted())
				{
			//		System.err.print((char)c);
					m_err.append((char)c);
				}
				m_errStream.close();
			}
			catch (IOException ioe)
			{
				log.log(Level.SEVERE, "errReader", ioe);
			}
			log.fine("errReader - done");
		}   //  run
	};   //  m_errReader


	/**
	 *  Execute it
	 */
	public void run()
	{
		log.info(m_cmd);
		try
		{
			m_child = Runtime.getRuntime().exec(m_cmd);
			//
			m_outStream = m_child.getInputStream();
			m_errStream = m_child.getErrorStream();
			m_inStream = m_child.getOutputStream();
			//
			if (checkInterrupted())
				return;
			m_outReader.start();
			m_errReader.start();
			//
			try
			{
				if (checkInterrupted())
					return;
				m_errReader.join();
				if (checkInterrupted())
					return;
				m_outReader.join();
				if (checkInterrupted())
					return;
				m_child.waitFor();
			}
			catch (InterruptedException ie)
			{
				log.log(Level.INFO, "(ie) - " + ie);
			}
			//  ExitValue
			try
			{
				if (m_child != null)
					log.fine("run - ExitValue=" + m_child.exitValue());
			}
			catch (Exception e) {}
			log.config("done");
		}
		catch (IOException ioe)
		{
			log.log(Level.SEVERE, "(ioe)", ioe);
		}
	}   //  run

	/**
	 *  Check if interrupted
	 *  @return true if interrupted
	 */
	private boolean checkInterrupted()
	{
		if (isInterrupted())
		{
			log.config("interrupted");
			//  interrupt child processes
			if (m_child != null)
				m_child.destroy();
			m_child = null;
			if (m_outReader != null && m_outReader.isAlive())
				m_outReader.interrupt();
			m_outReader = null;
			if (m_errReader != null && m_errReader.isAlive())
				m_errReader.interrupt();
			m_errReader = null;
			//  close Streams
			if (m_inStream != null)
				try {   m_inStream.close();     } catch (Exception e) {}
			m_inStream = null;
			if (m_outStream != null)
				try {   m_outStream.close();    } catch (Exception e) {}
			m_outStream = null;
			if (m_errStream != null)
				try {   m_errStream.close();    } catch (Exception e) {}
			m_errStream = null;
			//
			return true;
		}
		return false;
	}   //  checkInterrupted

	/**
	 *  Get Out Info
	 *  @return StringBuffer
	 */
	public StringBuffer getOut()
	{
		return m_out;
	}   //  getOut

	/**
	 *  Get Err Info
	 *  @return StringBuffer
	 */
	public StringBuffer getErr()
	{
		return m_err;
	}   //  getErr

	/**
	 *  Get The process input stream - i.e. we output to it
	 *  @return OutputStream
	 */
	public OutputStream getInStream()
	{
		return m_inStream;
	}   //  getInStream
	
}   //  Task
