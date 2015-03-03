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
import javax.sql.*;

//import org.compiere.util.CPreparedStatement;

/**
 *  Interface for Compiere Databases
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereDatabase.java,v 1.26 2005/12/31 06:33:21 jjanke Exp $
 */
public interface CompiereDatabase
{
	/**
	 *  Get Database Name
	 *  @return database short name
	 */
	public String getName();

	/**
	 *  Get Database Description
	 *  @return database long name and version
	 */
	public String getDescription();

	/**
	 *  Get and register Database Driver
	 *  @return Driver
	 */
	public Driver getDriver() throws SQLException;


	/**
	 *  Get Standard JDBC Port
	 *  @return standard port
	 */
	public int getStandardPort();

	/**
	 *  Get Database Connection String
	 *  @param connection Connection Descriptor
	 *  @return connection String
	 */
	public String getConnectionURL (CConnection connection);
	
	/**
	 * 	Get Connection URL
	 *	@param dbHost db Host
	 *	@param dbPort db Port
	 *	@param dbName db Name
	 *	@param userName user name
	 *	@return url
	 */
	public String getConnectionURL (String dbHost, int dbPort, String dbName,
		String userName);

	/**
	 * 	Get JDBC Catalog
	 *	@return catalog
	 */
	public String getCatalog();
	
	/**
	 * 	Get JDBC Schema
	 *	@return schema
	 */
	public String getSchema();

	/**
	 *  Supports BLOB
	 *  @return true if BLOB is supported
	 */
	public boolean supportsBLOB();

	/**
	 *  String Representation
	 *  @return info
	 */
	public String toString();

	
	/**************************************************************************
	 *  Convert an individual Oracle Style statements to target database statement syntax
	 *
	 *  @param oraStatement oracle statement
	 *  @return converted Statement
	 */
	public String convertStatement (String oraStatement);

	/**
	 *  Get Name of System User
	 *  @return e.g. sa, system
	 */
	public String getSystemUser();
	
	/**
	 *  Get Name of System Database
	 *  @param databaseName database Name
	 *  @return e.g. master or database Name
	 */
	public String getSystemDatabase(String databaseName);
	

	/**
	 *  Create SQL TO Date String from Timestamp
	 *
	 *  @param  time Date to be converted
	 *  @param  dayOnly true if time set to 00:00:00
	 *  @return date function
	 */
	public String TO_DATE (Timestamp time, boolean dayOnly);

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
	public String TO_CHAR (String columnName, int displayType, String AD_Language);

	
	/**
	 * 	Return number as string for INSERT statements with correct precision
	 *	@param number number
	 *	@param displayType display Type
	 *	@return number as string
	 */
	public String TO_NUMBER (BigDecimal number, int displayType);
	
	
	/** Create User commands					*/
	public static final int		CMD_CREATE_USER = 0;
	/** Create Database/Schema Commands			*/
	public static final int		CMD_CREATE_DATABASE = 1;
	/** Drop Database/Schema Commands			*/
	public static final int		CMD_DROP_DATABASE = 2;
	
	/**
	 * 	Get SQL Commands.
	 *  <code>
	 * 	The following variables are resolved:
	 * 	@SystemPassword@, @CompiereUser@, @CompierePassword@
	 * 	@SystemPassword@, @DatabaseName@, @DatabaseDevice@
	 *  </code>
	 *	@param cmdType CMD_*
	 *	@return array of commands to be executed
	 */
	public String[] getCommands (int cmdType);

	
	/**
	 * 	Get Cached Connection on Server
	 *	@param connection info
	 *  @param autoCommit true if autocommit connection
	 *  @param transactionIsolation Connection transaction level
	 *	@return connection or null
	 */
	public Connection getCachedConnection (CConnection connection, 
		boolean autoCommit, int transactionIsolation) throws Exception;

	/**
	 * 	Get Connection from Driver
	 *	@param connection info
	 *	@return connection or null
	 */
	public Connection getDriverConnection (CConnection connection) throws SQLException;

	/**
	 * 	Get Driver Connection
	 *	@param dbUrl URL
	 *	@param dbUid user
	 *	@param dbPwd password
	 *	@return connection
	 *	@throws SQLException
	 */
	public Connection getDriverConnection (String dbUrl, String dbUid, String dbPwd) 
		throws SQLException;

	/**
	 * 	Create DataSource
	 *	@param connection connection
	 *	@return data dource
	 */
	public DataSource getDataSource(CConnection connection);

	/**
	 * 	Get Status
	 * 	@return status info
	 */
	public String getStatus();

	/**
	 * 	Close
	 */
	public void close();

	/**
	 * 	Get Data Type
	 *	@param DisplayType display type
	 *	@return data type
	 */
//	public String getDataType (int displayType, int precision,
//		boolean defaultValue)
	
}   //  CompiereDatabase

