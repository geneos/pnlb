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

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;

/**
 *	Client Error Buffer
 *	
 *  @author Jorg Janke
 *  @version $Id: CLogErrorBuffer.java,v 1.6 2006/01/03 02:40:37 jjanke Exp $
 */
public class CLogErrorBuffer extends Handler
{
	/**
	 * 	Get Client Log Handler
	 *	@param create create if not exists
	 * 	@return handler
	 */
	public static CLogErrorBuffer get(boolean create)
	{
		if (s_handler == null && create)
			s_handler = new CLogErrorBuffer();
		return s_handler;
	}	//	get

	/**	Appender				*/
	private static CLogErrorBuffer	s_handler;

	
	/**************************************************************************
	 * 	Constructor
	 */
	public CLogErrorBuffer ()
	{
		if (s_handler == null)
			s_handler = this;
		else
			reportError("Error Handler exists already", 
				new IllegalStateException("Existing Handler"), 
				ErrorManager.GENERIC_FAILURE);
		initialize();
	}	//	CLogErrorBuffer

	/** Error Buffer Size			*/
	private static final int		ERROR_SIZE = 20;
	/**	The Error Buffer			*/
	private LinkedList<LogRecord> 	m_errors = new LinkedList<LogRecord>();
	/**	The Error Buffer History	*/
	private LinkedList<LogRecord[]>	m_history = new LinkedList<LogRecord[]>();

	/** Log Size					*/
	private static final int		LOG_SIZE = 100;
	/**	The Log Buffer				*/
	private LinkedList<LogRecord>	m_logs = new LinkedList<LogRecord>();
	/**	Issue Error					*/
	private volatile boolean		m_issueError = true;
	
    /**
     * 	Initialize
     */
    private void initialize()
    {
    //	System.out.println("CLogConsole.initialize");

    	//	Foratting
		setFormatter(CLogFormatter.get());
		//	Default Level
		super.setLevel(Level.INFO);
		//	Filter
		setFilter(CLogFilter.get());
    }	//	initialize

    
	/**
	 *	Set Level.
	 *	Ignore OFF - and higer then FINE
	 *	@see java.util.logging.Handler#setLevel(java.util.logging.Level)
	 *	@param newLevel ignored
	 *	@throws java.lang.SecurityException
	 */
	public synchronized void setLevel (Level newLevel)
		throws SecurityException
	{
		if (newLevel == null)
			return;
		if (newLevel == Level.OFF)
			super.setLevel(Level.SEVERE);
		else if (newLevel == Level.ALL || newLevel == Level.FINEST || newLevel == Level.FINER)
			super.setLevel(Level.FINE);
		else
			super.setLevel(newLevel);
	}	//	SetLevel
    
	/**
	 *	Publish
	 *	@see java.util.logging.Handler#publish(java.util.logging.LogRecord)
	 *	@param record log record
	 */
	public void publish (LogRecord record)
	{
		if (!isLoggable (record) || m_logs == null)
			return;
		
		//	Output
		synchronized (m_logs)
		{
			if (m_logs.size() >= LOG_SIZE)
				m_logs.removeFirst();
			m_logs.add(record);
		}
		
		//	We have an error
		if (record.getLevel() == Level.SEVERE)
		{
			if (m_errors.size() >= ERROR_SIZE)
			{
				m_errors.removeFirst();
				m_history.removeFirst();
			}
			//	Add Error
			m_errors.add(record);
			record.getSourceClassName();	//	forces Class Name eval
			
			//	Create History
			ArrayList<LogRecord> history = new ArrayList<LogRecord>();
			for (int i = m_logs.size()-1; i >= 0; i--)
			{
				LogRecord rec = (LogRecord)m_logs.get(i);
				if (rec.getLevel() == Level.SEVERE)
				{
					if (history.size() == 0)
						history.add(rec);
					else
						break;		//	don't incluse previous error
				}
				else
				{
					history.add(rec);
					if (history.size() > 10)
						break;		//	no more then 10 history records
				}
					
			}
			LogRecord[] historyArray = new LogRecord[history.size()];
			int no = 0;
			for (int i = history.size()-1; i >= 0; i--) 
				historyArray[no++] = (LogRecord)history.get(i);
			m_history.add(historyArray);
			//	Issue Reporting
			if (m_issueError)
			{
				String loggerName = record.getLoggerName();			//	class name	
				String className = record.getSourceClassName();		//	physical class
				String methodName = record.getSourceMethodName();	//	
				if (DB.isConnected() 
					&& !methodName.equals("saveError")
					&& !methodName.equals("get_Value")
					&& !methodName.equals("dataSave")
					&& loggerName.indexOf("Issue") == -1
					&& loggerName.indexOf("CConnection") == -1
					)
				{
					m_issueError = false;
					MIssue.create(record);
					m_issueError = true;
				}
			}
		}
	}	// publish

	/**
	 * Flush (NOP)
	 * @see java.util.logging.Handler#flush()
	 */
	public void flush ()
	{
	}	// flush

	/**
	 * Close
	 * @see java.util.logging.Handler#close()
	 * @throws SecurityException
	 */
	public void close () throws SecurityException
	{
		if (m_logs != null)
			m_logs.clear();
		m_logs = null;
		if (m_errors != null)
			m_errors.clear();
		m_errors = null;
		if (m_history != null)
			m_history.clear();
		m_history = null;
	}	// close

	
	/**************************************************************************
	 * 	Get ColumnNames of Log Entries
	 * 	@param ctx context (not used)
	 */	
	public Vector<String> getColumnNames(Properties ctx)
	{
		Vector<String> cn = new Vector<String>();
		cn.add("Time");
		cn.add("Level");
		//
		cn.add("Class.Method");
		cn.add("Message");
		//2
		cn.add("Parameter");
		cn.add("Trace");
		//
		return cn;
	}	//	getColumnNames

	/**
	 * 	Get Log Data
	 * 	@param errorsOnly if true errors otherwise log
	 * 	@return data array
	 */
	public Vector<Vector> getLogData (boolean errorsOnly)
	{
		LogRecord[] records = getRecords(errorsOnly);
	//	System.out.println("getLogData - " + events.length);
		Vector<Vector> rows = new Vector<Vector>(records.length);
		
		for (int i = 0; i < records.length; i++)
		{
			LogRecord record = records[i];
			Vector<Object> cols = new Vector<Object>();
			//
			cols.add(new Timestamp(record.getMillis()));
			cols.add(record.getLevel().getName());
			//
			cols.add(CLogFormatter.getClassMethod(record));
			cols.add(record.getMessage());
			//
			cols.add(CLogFormatter.getParameters(record));
			cols.add(CLogFormatter.getExceptionTrace(record));
			//
			rows.add(cols);
		}
		return rows;
	}	//	getData
	
	/**
	 * 	Get Array of events with most recent first
	 * 	@param errorsOnly if true errors otherwise log
	 * 	@return array of events 
	 */
	public LogRecord[] getRecords (boolean errorsOnly)
	{
		LogRecord[] retValue = null;
		if (errorsOnly)
		{
			synchronized (m_errors)
			{
				retValue = new LogRecord[m_errors.size()];
				m_errors.toArray(retValue);
			}
		}
		else
		{
			synchronized (m_logs)
			{
				retValue = new LogRecord[m_logs.size()];
				m_logs.toArray(retValue);
			}
		}
		return retValue;
	}	//	getEvents
	
	/**
	 * 	Reset Error Buffer
	 * 	@param errorsOnly if true errors otherwise log
	 */
	public void resetBuffer (boolean errorsOnly)
	{
		synchronized (m_errors)
		{
			m_errors.clear();
			m_history.clear();
		}
		if (!errorsOnly)
		{
			synchronized (m_logs)
			{
				m_logs.clear();
			}
		}
	}	//	resetBuffer
	
	/**
	 * 	Get/Put Error Info in String
	 *	@param ctx context
	 * 	@param errorsOnly if true errors otherwise log
	 *	@return error info
	 */
	public String getErrorInfo (Properties ctx, boolean errorsOnly)
	{
		StringBuffer sb = new StringBuffer();
		//
		if (errorsOnly)
		{
			for (int i = 0; i < m_history.size(); i++)
			{
				sb.append("-------------------------------\n");
				LogRecord[] records = (LogRecord[])m_history.get(i);
				for (int j = 0; j < records.length; j++) 
				{
					LogRecord record = records[j];
					sb.append(getFormatter().format(record));
				}
			}
		}
		else
		{
			for (int i = 0; i < m_logs.size(); i++)
			{
				LogRecord record = (LogRecord)m_logs.get(i);
				sb.append(getFormatter().format(record));
			}
		}
		sb.append("\n");
		CLogMgt.getInfo(sb);
		CLogMgt.getInfoDetail(sb, ctx);
		//
		return sb.toString();
	}	//	getErrorInfo

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("CLogErrorBuffer[");
		sb.append("Errors=").append(m_errors.size())
			.append(",History=").append(m_history.size())
			.append(",Logs=").append(m_logs.size())
			.append(",Level=").append(getLevel())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	CLogErrorBuffer
