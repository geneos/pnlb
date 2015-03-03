/*******************************************************************************
 * The contents of this file are subject to the Compiere License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License. The
 * Original Code is Compiere ERP & CRM Business Solution The Initial Developer
 * of the Original Code is Jorg Janke and ComPiere, Inc. Portions created by
 * Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts created by ComPiere
 * are Copyright (C) ComPiere, Inc.; All Rights Reserved. Contributor(s):
 * ______________________________________.
 ******************************************************************************/
package org.compiere.util;

import java.io.*;
import java.sql.*;
import java.util.logging.*;

/**
 * 	Compiere Console Logger
 *	@author Jorg Janke
 *	@version $Id: CLogConsole.java,v 1.2 2005/01/22 22:01:23 jjanke Exp $
 */
public class CLogConsole extends Handler
{
	/**
	 * 	Get Console Handler
	 *	@param create create if not exists
	 *	@return console hander or null
	 */
	public static CLogConsole get (boolean create)
	{
		if (s_console == null && create)
			s_console = new CLogConsole();
		return s_console;
	}	//	get
	
	private static CLogConsole	s_console = null;
	
	/**
	 *	Constructor
	 */
	public CLogConsole ()
	{
		if (s_console == null)
			s_console = this;
		else
			reportError("Console Handler exists already", 
				new IllegalStateException("Existing Handler"), 
				ErrorManager.GENERIC_FAILURE);
		initialize();
	}	// CLogConsole

	/**	Printed header			*/
    private boolean 	m_doneHeader = false;
    /** Normal Writer			*/
    private PrintWriter	m_writerOut = null;
    /** Error Writer			*/
    private PrintWriter	m_writerErr = null;

    /**
     * 	Initialize
     */
    private void initialize()
    {
    //	System.out.println("CLogConsole.initialize");
		//	Set Writers
		String encoding = getEncoding();
		if (encoding != null)
		{
		    try 
		    {
		    	m_writerOut = new PrintWriter(new OutputStreamWriter(System.out, encoding));
		    	m_writerErr = new PrintWriter(new OutputStreamWriter(System.err, encoding));
		    }
		    catch (UnsupportedEncodingException ex) 
		    {
				reportError ("Opening encoded Writers", ex, ErrorManager.OPEN_FAILURE);
		    }
		}
		if (m_writerOut == null)
			m_writerOut = new PrintWriter(System.out);
		if (m_writerErr == null)
			m_writerErr = new PrintWriter(System.err);
		
    	//	Foratting
		setFormatter(CLogFormatter.get());
		//	Default Level
		setLevel(Level.INFO);
		//	Filter
		setFilter(CLogFilter.get());
		//
    }	//	initialize
	
    /**
     * 	Set Encoding
     *	@param encoding encoding
     *	@throws SecurityException
     *	@throws java.io.UnsupportedEncodingException
     */
    public void setEncoding (String encoding)
		throws SecurityException, java.io.UnsupportedEncodingException
	{
		super.setEncoding (encoding);
		// Replace the current writer with a writer for the new encoding.
		flush ();
		initialize();
	}	//	setEncoding

    
	/**
	 * 	Set Level
	 *	@see java.util.logging.Handler#setLevel(java.util.logging.Level)
	 *	@param newLevel new Level
	 *	@throws java.lang.SecurityException
	 */
	public synchronized void setLevel (Level newLevel)
		throws SecurityException
	{
		if (newLevel == null)
			return;
		super.setLevel (newLevel);
		boolean enableJDBC = newLevel == Level.FINEST;
		if (enableJDBC)
			DriverManager.setLogWriter(m_writerOut);	//	lists Statements
		else
			DriverManager.setLogWriter(null);
	}	//	setLevel
    
	/**
	 *	Publish
	 *	@see java.util.logging.Handler#publish(java.util.logging.LogRecord)
	 *	@param record log record
	 */
	public void publish (LogRecord record)
	{
		if (!isLoggable (record) || m_writerOut == null)
			return;
		
		//	Format
		String msg = null;
		try
		{
			msg = getFormatter().format (record);
		}
		catch (Exception ex)
		{
			reportError ("formatting", ex, ErrorManager.FORMAT_FAILURE);
			return;
		}
		//	Output
		try
		{
			if (!m_doneHeader)
			{
				m_writerOut.write (getFormatter().getHead (this));
				m_doneHeader = true;
			}
			if (record.getLevel() == Level.SEVERE 
				|| record.getLevel() == Level.WARNING)
			{
				flush();
				m_writerErr.write (msg);
				flush();
			}
			else
			{
				m_writerOut.write (msg);
				m_writerOut.flush();
			}
		}
		catch (Exception ex)
		{
			reportError ("writing", ex, ErrorManager.WRITE_FAILURE);
		}
	}	// publish

	/**
	 *	Flush
	 *	@see java.util.logging.Handler#flush()
	 */
	public void flush ()
	{
		try
		{
			if (m_writerOut != null)
				m_writerOut.flush();
		}
		catch (Exception ex)
		{
			reportError ("flush out", ex, ErrorManager.FLUSH_FAILURE);
		}
		try
		{
			if (m_writerErr != null)
				m_writerErr.flush();
		}
		catch (Exception ex)
		{
			reportError ("flush err", ex, ErrorManager.FLUSH_FAILURE);
		}
	}	// flush

	/**
	 * Close
	 * 
	 * @see java.util.logging.Handler#close()
	 * @throws SecurityException
	 */
	public void close () throws SecurityException
	{
		if (m_writerOut == null)
			return;

		//	Write Tail
		try
		{
			if (!m_doneHeader)
				m_writerOut.write (getFormatter().getHead(this));
			//
			m_writerOut.write (getFormatter().getTail(this));
		}
		catch (Exception ex)
		{
			reportError ("tail", ex, ErrorManager.WRITE_FAILURE);
		}
		//
		flush();
		//	Close
		try
		{
			m_writerOut.close();
		}
		catch (Exception ex)
		{
			reportError ("close out", ex, ErrorManager.CLOSE_FAILURE);
		}
		m_writerOut = null;
		try
		{
			m_writerErr.close();
		}
		catch (Exception ex)
		{
			reportError ("close err", ex, ErrorManager.CLOSE_FAILURE);
		}
		m_writerErr = null;
	}	// close
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("CLogConsole[");
		sb.append("Level=").append(getLevel())
			.append ("]");
		return sb.toString ();
	} //	toString

} // CLogConsole
