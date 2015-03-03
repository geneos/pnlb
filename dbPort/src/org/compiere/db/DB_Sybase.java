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
package org.compiere.db;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.sql.*;
import org.compiere.dbPort.*;
import org.compiere.util.*;


/**
 *	Sybase Database Port
 *	
 *  @author Jorg Janke
 *  @version $Id: DB_Sybase.java,v 1.10 2005/12/31 06:33:21 jjanke Exp $
 */
public class DB_Sybase implements CompiereDatabase
{
	/** What driver to use - Sybase or jTDS	- requires change in tools/build.xml	*/
	private static final boolean JTDS = true;
	
	/**
	 * 	DB Sybase Port
	 */
	public DB_Sybase ()
	{
		try
		{
			getDriver();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage());
		}
	}	//	DB_Sybase

	/**	Drver					*/
	private static Driver	s_driver = null;
	/** Default Port            */
	public static final int DEFAULT_PORT = 5000;
	
	/** Connection String       */
	private String          m_connectionURL;
	/**	Data Source				*/
	private DataSource 		m_ds = null;
	/** Cached Database Name	*/
	private String			m_dbName = null;
	/** Statement Converter     */
	private Convert         m_convert = new Convert(Database.DB_SYBASE);
	
	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (DB_Sybase.class);
	
	
	/**
	 *  Get Database Name
	 *  @return database short name
	 */
	public String getName()
	{
		return Database.DB_SYBASE;
	}   //  getName

	/**
	 * 	Get Description
	 *	@return info
	 */
	public String getDescription ()
	{
		return s_driver.toString(); 
		//	s_driver.getMajorVersion() + " - " + s_driver.getMinorVersion();
	}	//	getDrescription

	/**
	 * 	Get Standard Port
	 *	@return port
	 */
	public int getStandardPort ()
	{
		return DEFAULT_PORT;
	}	//	getStndardPort

	/**
	 * 	Get Driver
	 *	@return driver
	 *	@throws SQLException
	 */
	public Driver getDriver () throws SQLException
	{
		if (s_driver == null)
		{
		//	if (JTDS)
				s_driver = new net.sourceforge.jtds.jdbc.Driver();
		//	else
		//		s_driver = new com.sybase.jdbc3.jdbc.SybDriver();
			DriverManager.registerDriver (s_driver);
			DriverManager.setLoginTimeout (Database.CONNECTION_TIMEOUT);
		}
		return s_driver;
	}	//	getDriver


	/**
	 * 	Get Connection URL
	 *	@param connection connection
	 *	@return url
	 */
	public String getConnectionURL (CConnection connection)
	{
		StringBuffer sb = null;
		if (JTDS)
			sb = new StringBuffer("jdbc:jtds:sybase://");
		else
			sb = new StringBuffer("jdbc:sybase:Tds:");
		//
		sb.append(connection.getDbHost())
			.append(":").append(connection.getDbPort())
			.append("/").append(connection.getDbName());
		//	optional parameters via ?...
		m_connectionURL = sb.toString();
		m_dbName = connection.getDbName();
		return m_connectionURL;
	}	//	getConnectionURL

	/**
	 * 	Get Connection URL.
	 * 	Mainly used for connection test
	 *	@param dbHost db Host
	 *	@param dbPort db Port
	 *	@param dbName db Name (optional)
	 *	@param userName user name (ignored)
	 *	@return connection url
	 */
	public String getConnectionURL (String dbHost, int dbPort, String dbName,
		String userName)
	{
		StringBuffer sb = null;
		if (JTDS)
			sb = new StringBuffer("jdbc:jtds:sybase://");
		else
			sb = new StringBuffer("jdbc:sybase:Tds:");
		sb.append(dbHost)
			.append(":").append(dbPort);
		//
		if (dbName != null && dbName.length() > 0)
		{
			m_dbName = dbName;
			sb.append("/").append(dbName);
		}
		return sb.toString();
	}	//	getConnectionURL

	/**
	 * 	Get JDBC Catalog
	 *	@return catalog (database name)
	 */
	public String getCatalog()
	{
		if (m_dbName != null)
			return m_dbName;
		log.severe("Database Name not set (yet) - call getConnectionURL first");
		return null;
	}	//	getCatalog
	
	/**
	 * 	Get JDBC Schema
	 *	@return schema (dbo)
	 */
	public String getSchema()
	{
		return "dbo";
	}	//	getSchema

	/**
	 * 	Supports BLOB
	 *	@return true
	 */
	public boolean supportsBLOB ()
	{
		return true;
	}	//	supportsBLOB

	/**
	 *  String Representation
	 *  @return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("DB_Sybase[");
		sb.append(m_connectionURL);
		sb.append("]");
		return sb.toString();
	}   //  toString

	/**
	 * 	Get Status
	 * 	@return status info
	 */
	public String getStatus()
	{
		StringBuffer sb = new StringBuffer("Status");
		return sb.toString();
	}	//	getStatus

	
	/**************************************************************************
	 * 	Convert Oracle style Statement
	 *	@param oraStatement oracle style statement
	 *	@return statement
	 */
	public String convertStatement (String oraStatement)
	{
		String retValue[] = m_convert.convert(oraStatement);
		if (retValue == null)
			throw new IllegalArgumentException
				("Not Converted (" + oraStatement + ") - "
					+ m_convert.getConversionError());
		if (retValue.length != 1)
			throw new IllegalArgumentException
				("Convert Command Number=" + retValue.length
					+ " (" + oraStatement + ") - " + m_convert.getConversionError());
		//  Diagnostics (show changed, but not if AD_Error
		if (!oraStatement.equals(retValue[0]) && retValue[0].indexOf("AD_Error") == -1)
			log.finest("=>" + retValue[0] + "<= [" + oraStatement + "]");
		//
		return retValue[0];
	}	//	convertStatement

	/**
	 *  Get Name of System User
	 *  @return e.g. sa, system
	 */
	public String getSystemUser()
	{
		return "sa";
	}	//	getSystemUser
	
	/**
	 *  Get Name of System Database
	 *  @param databaseName database Name ignored
	 *  @return e.g. master or database Name
	 */
	public String getSystemDatabase(String databaseName)
	{
		return "master";
	}	//	getSystemDatabase


	private static final String[] MONTHS = new String[]{
		"JAN","FEB","MAR", "APR","MAY","JUN", "JUL","AUG","SEP", "OCT","NOV","DEC"};
	
	/**
	 *  Create SQL TO Date String from Timestamp
	 *
	 *  @param  time Date to be converted
	 *  @param  dayOnly true if time set to 00:00:00
	 *  @return date function
	 */
	public String TO_DATE (Timestamp time, boolean dayOnly)
	{
		if (time == null)
		{
			if (dayOnly)
				return "convert(date,getdate())";
			return "getdate()";
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(time);
		//
		StringBuffer dateString = new StringBuffer("convert(datetime,'");
		//	yyyy.mm.dd	- format 2 p.411 
		if (dayOnly)
		{
			int yyyy = cal.get(Calendar.YEAR);
			String format = "102";	//	"SQL Standard" format
			if (yyyy < 100)
				format = "2";
			dateString.append(yyyy).append(".")
				.append(getXX(cal.get(Calendar.MONTH)+1)).append(".")
				.append(getXX(cal.get(Calendar.DAY_OF_MONTH)))
				.append("',").append(format).append(")");
		}
		//	mon dd yyy hh:mi:ss - format 116
		else
		{
			int yyyy = cal.get(Calendar.YEAR);
			String format = "116";	//	n/a format
			if (yyyy < 100)
				format = "16";
			dateString.append(MONTHS[cal.get(Calendar.MONTH)]).append(" ")
				.append(getXX(cal.get(Calendar.DAY_OF_MONTH))).append(" ")
				.append(getXX(cal.get(Calendar.YEAR))).append(" ")
				.append(getXX(cal.get(Calendar.HOUR_OF_DAY))).append(":")
				.append(getXX(cal.get(Calendar.MINUTE))).append(":")
				.append(getXX(cal.get(Calendar.SECOND)))
				.append("',").append(format).append(")");
		}
		return dateString.toString();
	}	//	TO_DATE

	/**
	 * 	Get integer as two string digits (leading zero)
	 *	@param x integer
	 *	@return string of x
	 */
	private String getXX (int x)
	{
		if (x < 10)
			return "0" + x;
		return String.valueOf(x);
	}	//	getXX
	
	/**
	 *  Create SQL for formatted Date, Number
	 *
	 *  @param  columnName  the column name in the SQL
	 *  @param  displayType Display Type
	 *  @param  AD_Language 6 character language setting (from Env.LANG_*)
	 *
	 *  @return TRIM(TO_CHAR(columnName,'9G999G990D00','NLS_NUMERIC_CHARACTERS='',.'''))
	 *      or TRIM(TO_CHAR(columnName,'TM9')) depending on DisplayType and Language
	 *  @see org.compiere.util.DisplayType
	 *  @see org.compiere.util.Env
	 *
	 **/
	public String TO_CHAR (String columnName, int displayType, String AD_Language)
	{
		return "";
	}	//	TO_CHAR

	/**
	 * 	Return number as string for INSERT statements with correct precision
	 *	@param number number
	 *	@param displayType display Type
	 *	@return number as string
	 */
	public String TO_NUMBER (BigDecimal number, int displayType)
	{
		if (number == null)
			return "NULL";
		BigDecimal result = number;
		int scale = DisplayType.getDefaultPrecision(displayType);
		if (number.scale() > scale)
		{
			try
			{
				result = number.setScale(scale, BigDecimal.ROUND_HALF_UP);
			}
			catch (Exception e)
			{
				log.severe("Number=" + number + ", Scale=" + " - " + e.getMessage());
			}
		}
		return result.toString();
	}	//	TO_NUMBER
	
	
	/**
	 * 	Get SQL Commands.
	 * 	The following variables are resolved:
	 * 	@SystemPassword@, @CompiereUser@, @CompierePassword@
	 * 	@SystemPassword@, @DatabaseName@, @DatabaseDevice@
	 *	@param cmdType CMD_*
	 *	@return array of commands to be executed
	 */
	public String[] getCommands (int cmdType)
	{
		if (CMD_CREATE_USER == cmdType)
			return new String[]
			{
			
			};
		//
		if (CMD_CREATE_DATABASE == cmdType)
			return new String[]
			{
				"CREATE database @DatabaseName@ on @DatabaseDevice@ = 200",
				"sp_configure \"enable java\", 1"
			};
		//
		if (CMD_DROP_DATABASE == cmdType)
			return new String[]
			{
				"DROP database @DatabaseName@"
			};
		//
		return null;
	}	//	getCommands

	
	/**
	 * 	Get Cached Connection
	 *	@param connection connection
	 *	@param autoCommit auto commit
	 *	@param transactionIsolation trx isolation
	 *	@return Connection
	 *	@throws Exception
	 */
	public Connection getCachedConnection (CConnection connection,
		boolean autoCommit, int transactionIsolation)
		throws Exception
	{
		if (m_ds == null)
			getDataSource(connection);
		//
		Connection conn = m_ds.getConnection();
	//	Connection conn = getDriverConnection(connection);
		//
		conn.setAutoCommit(autoCommit);
		conn.setTransactionIsolation(transactionIsolation);
		return conn;
	}	//	getCachedConnection

	/**
	 * 	Get Driver Connection
	 *	@param connection connection info
	 *	@return new connection
	 *	@throws SQLException
	 */
	public Connection getDriverConnection (CConnection connection)
		throws SQLException
	{
		getDriver();
		return DriverManager.getConnection (getConnectionURL (connection), 
			connection.getDbUid(), connection.getDbPwd());
	}	//	getDiverConnection

	/**
	 * 	Get Driver Connection
	 *	@param dbUrl URL
	 *	@param dbUid user
	 *	@param dbPwd password
	 *	@return connection
	 *	@throws SQLException
	 */
	public Connection getDriverConnection (String dbUrl, String dbUid, String dbPwd) 
		throws SQLException
	{
		getDriver();
		return DriverManager.getConnection (dbUrl, dbUid, dbPwd);
	}	//	getDriverConnection

	/**
	 * 	Get Data Source
	 *	@param connection connection
	 *	@return n/a
	 */
	public DataSource getDataSource (CConnection connection)
	{
		if (m_ds != null)
			return m_ds;

	//	if (JTDS)
		{
			net.sourceforge.jtds.jdbcx.JtdsDataSource ds = new net.sourceforge.jtds.jdbcx.JtdsDataSource();
			ds.setServerType(net.sourceforge.jtds.jdbc.Driver.SYBASE);
			ds.setTds("5.0");
			ds.setServerName(connection.getDbHost());
			ds.setPortNumber(connection.getDbPort());
			ds.setDatabaseName(connection.getDbName());
			//
			ds.setUser(connection.getDbUid());
			ds.setPassword(connection.getDbPwd());
			m_ds = ds;
		}
		/**
		else
		{
			com.sybase.jdbc3.jdbc.SybDataSource ds = new com.sybase.jdbc3.jdbc.SybDataSource();
			ds.setServerName(connection.getDbHost());
			ds.setPortNumber(connection.getDbPort());
			ds.setDatabaseName(connection.getDbName());
			ds.setDataSourceName("SybaseDS");
			//
			ds.setUser(connection.getDbUid());
			ds.setPassword(connection.getDbPwd());
			m_ds = ds;
		}
		**/
	//	m_ds.setLoginTimeout(10);
		//
		return m_ds;
	}	//	getDataSource


	/**
	 * 	Close
	 */
	public void close ()
	{
		m_ds = null;
	}	//	close
	
	
	/**
	 * 	Test
	 *	@param args ignored
	 */
	public static void main(String[] args)
	{
		DB_Sybase sybase = new DB_Sybase();
		//
		String databaseName = "compiere";
		String uid = "compiere";
		String pwd = "compiere";
		String jdbcURL = sybase.getConnectionURL("dev2", DEFAULT_PORT, databaseName, uid);
		System.out.println(jdbcURL);
		try
		{
			sybase.getDriver();
			Connection conn = DriverManager.getConnection (jdbcURL, uid, pwd);
			
			RowSet rs = CCachedRowSet.getRowSet("SELECT * FROM AD_Client", conn);
			//
			conn.close();
			conn = null;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}	//	main
	
}	//	DB_Sybase
