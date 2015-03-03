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

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Class to Create a new Compiere Database from a reference DB.
 *  <pre>
 *  - Create User
 *  - Create DDL (table, procedures, functions, etc.)
 *  </pre>
 *
 *  @author     Jorg Janke
 *  @version    $Id: CreateCompiere.java,v 1.9 2005/10/26 00:38:20 jjanke Exp $
 */
public class CreateCompiere
{
	/**
	 * 	Constructor
	 *	@param databaseType CompiereDatabase.TYPE_
	 *	@param databaseHost database host
	 *	@param databasePort database port 0 for default
	 *	@param systemPassword system password
	 */
	public CreateCompiere(String databaseType, String databaseHost, int databasePort,
		String systemPassword)
	{
		initDatabase(databaseType);
		m_databaseHost = databaseHost;
		if (databasePort == 0)
			m_databasePort = m_dbTarget.getStandardPort();
		else
			m_databasePort = databasePort;
		m_systemPassword = systemPassword;
		log.info(m_dbTarget.getName() + " on " + databaseHost);
	}   //  create

	/** Compiere Target Database */
	private CompiereDatabase 	m_dbTarget = null;
	/** Compiere Source Database */
	private CompiereDatabase 	m_dbSource = null;
	//
	private String				m_databaseHost = null;
	private int					m_databasePort = 0;
	private String 				m_systemPassword = null;
	private String 				m_compiereUser = null;
	private String 				m_compierePassword = null;
	private String 				m_databaseName = null;
	private String 				m_databaseDevice = null;
	//
	private Properties			m_ctx = new Properties ();
	/** Cached connection		*/
	private Connection			m_conn = null;
	
	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (CreateCompiere.class);
  
        //begin vpj-cd e-evolution
	private FileOutputStream file; // declare a file output object
        private PrintStream filedata; // declare a print stream object
        //end
	
	/**
	 * 	Create Compiere Database
	 *	@param databaseType Database.DB_
	 */
	private void initDatabase(String databaseType)
	{
		try
		{
			for (int i = 0; i < Database.DB_NAMES.length; i++)
			{
				if (Database.DB_NAMES[i].equals (databaseType))
				{
					m_dbTarget = (CompiereDatabase)Database.DB_CLASSES[i].
						   newInstance ();
					break;
				}
			}
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
			e.printStackTrace();
		}
		if (m_dbTarget == null)
			throw new IllegalStateException("No database: " + databaseType);
		
		//	Source Database
		m_dbSource = DB.getDatabase();
	}	//	createDatabase
	
	/**
	 * 	Clean Start - drop & re-create DB
	 */
	public void cleanStart()
	{
		Connection conn = getConnection(true, true);
		if (conn == null)
			throw new IllegalStateException("No Database");
		//
		dropDatabase(conn);
		createUser(conn);
		createDatabase(conn);
		//
		try
		{
			if (conn != null)
				conn.close();
		}
		catch (SQLException e2)
		{
			log.log(Level.SEVERE, "close connection", e2);
		}
		conn = null;
	}	//	cleanStart
	
	
	/**
	 *  Set Compiere User
	 *  @param compiereUser compiere id
	 *  @param compierePassword compiere password
	 */
	public void setCompiereUser (String compiereUser, String compierePassword)
	{
		m_compiereUser = compiereUser;
		m_compierePassword = compierePassword;
	}   //  setCompiereUser

	/**
	 *  Set Database Name
	 *  @param databaseName db name
	 *  @param databaseDevice device or table space
	 */
	public void setDatabaseName (String databaseName, String databaseDevice)
	{
		m_databaseName = databaseName;
		m_databaseDevice = databaseDevice;
	}   //  createDatabase

	
	/**
	 * 	Test Connection
	 *	@return connection
	 */
	public boolean testConnection()
	{
		String dbUrl = m_dbTarget.getConnectionURL (m_databaseHost, m_databasePort, 
			m_databaseName, m_dbTarget.getSystemUser());	//	compiere may not be defined yet
		log.info(dbUrl + " - " + m_dbTarget.getSystemUser() + "/" + m_systemPassword);
		try
		{
			Connection conn = m_dbTarget.getDriverConnection(dbUrl, m_dbTarget.getSystemUser(), m_systemPassword);
			//
			JDBCInfo info = new JDBCInfo(conn);
			if (CLogMgt.isLevelFinest())
			{
				info.listCatalogs();
				info.listSchemas();
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "test", e);
			return false;
		}

		return true;
	}	//	testConnection
	
	
	/**************************************************************************
	 *  Create User
	 *  @return true if success
	 */
	public boolean createUser (Connection sysConn)
	{
		log.info(m_compiereUser + "/" + m_compierePassword);
		return executeCommands(m_dbTarget.getCommands(CompiereDatabase.CMD_CREATE_USER), 
			sysConn, true, false);
	}   //  createUser

	/**
	 *  Create Database
	 *  @return true if success
	 */
	public boolean createDatabase (Connection sysConn)
	{
		log.info(m_databaseName + "(" + m_databaseDevice + ")");
		return executeCommands(m_dbTarget.getCommands(CompiereDatabase.CMD_CREATE_DATABASE), 
			sysConn, true, false);
	}   //  createDatabase	
	
	/**
	 *  Drop Database
	 *  @return true if success
	 */
	public boolean dropDatabase (Connection sysConn)
	{
		log.info(m_databaseName);
		return executeCommands(m_dbTarget.getCommands(CompiereDatabase.CMD_DROP_DATABASE), 
			sysConn, true, false);
	}   //  dropDatabase	
	
	
	/**
	 * 	Create Tables and copy data
	 * 	@param whereClause optional where clause
	 * 	@param dropFirst drop first
	 *	@return true if executed
	 */
	public boolean copy (String whereClause, boolean dropFirst)
	{
		log.info(whereClause);
		if (getConnection(false, true) == null)
			return false;
		//
		boolean success = true;
		int count = 0;
		ArrayList<String> list = new ArrayList<String>();
		String sql = "SELECT * FROM AD_Table";
		if (whereClause != null && whereClause.length() > 0)
			sql += " WHERE " + whereClause;
		sql += " ORDER BY TableName";
		//
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			DatabaseMetaData md = pstmt.getConnection().getMetaData();
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next() && success)
			{
				M_Table table = new M_Table (m_ctx, rs, null);
				if (table.isView())
					continue;
				if (dropFirst)
				{
					executeCommands(new String[]
					    {"DROP TABLE " + table.getTableName()}, 
						m_conn, false, false);
				}
				//
				if (createTable (table, md))
				{
					list.add(table.getTableName());
					count++;
				}
				else
					success = false;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
			success = false;
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		if (!success)
			return false;
		
		/**	Enable Contraints */
		enableConstraints(list);

		databaseBuild();
		
		log.info("#" + count);
		
		try
		{
			if (m_conn != null)
				m_conn.close();
		}
		catch (SQLException e2)
		{
			log.log(Level.SEVERE, "close connection", e2);
		}
		m_conn = null;
		return success;
	}	//	copy

	/**
	 * 	Execute Script
	 *	@return true if executed
	 */
	public boolean execute (File script)
	{
		return false;
	}	//	createTables
	

	
	/**
	 * 	Create Table
	 *	@param mTable table model
	 *	@param md meta data
	 *	@return true if created
	 */
	private boolean createTable (M_Table mTable, DatabaseMetaData md)
	{
		String tableName = mTable.getTableName();
		log.info(tableName);
		String catalog = m_dbSource.getCatalog();
		String schema = m_dbSource.getSchema();
		String table = tableName.toUpperCase();
		//	
		M_Column[] columns = mTable.getColumns(false);
		
		StringBuffer sb = new StringBuffer("CREATE TABLE ");
		sb.append(tableName).append(" (");
		try
		{
			//	Columns
			boolean first = true;
			ResultSet sourceColumns = md.getColumns(catalog, schema, table, null);
			while (sourceColumns.next())
			{
				sb.append(first ? "" : ", ");
				first = false;
				//	Case sensitive Column Name
				M_Column column = null;
				String columnName = sourceColumns.getString("COLUMN_NAME");
				for (int i = 0; i < columns.length; i++)
				{
					String cn = columns[i].getColumnName();
					if (cn.equalsIgnoreCase(columnName))
					{
						columnName = cn;
						column = columns[i];
						break;
					}
				}
				sb.append(columnName).append(" ");
				//	Data Type & Precision
				int sqlType = sourceColumns.getInt ("DATA_TYPE");		//	sql.Types
				String typeName = sourceColumns.getString ("TYPE_NAME");	//	DB Dependent
				int size = sourceColumns.getInt ("COLUMN_SIZE");
				int decDigits = sourceColumns.getInt("DECIMAL_DIGITS");
				if (sourceColumns.wasNull())
					decDigits = -1;
				if (typeName.equals("NUMBER"))
				{
					/** Oracle Style	*
					if (decDigits == -1)
						sb.append(typeName);
					else
						sb.append(typeName).append("(")
							.append(size).append(",").append(decDigits).append(")");
					/** Other DBs		*/
					int dt = column.getAD_Reference_ID();
					if (DisplayType.isID(dt))
						sb.append("INTEGER");
					else 
					{
						int scale = DisplayType.getDefaultPrecision(dt);
						sb.append("DECIMAL(")
							.append(18+scale).append(",").append(scale).append(")");
					}
				}					
				else if (typeName.equals("DATE") || typeName.equals("BLOB") || typeName.equals("CLOB"))
					sb.append(typeName);
				else if (typeName.equals("CHAR") || typeName.startsWith("VARCHAR"))
					sb.append(typeName).append("(").append(size).append(")");
				else if (typeName.startsWith("NCHAR") || typeName.startsWith("NVAR"))
					sb.append(typeName).append("(").append(size/2).append(")");
				else if (typeName.startsWith("TIMESTAMP"))
					sb.append("DATE");
				else 
					log.severe("Do not support data type " + typeName);
				//	Default
				String def = sourceColumns.getString("COLUMN_DEF");
				if (def != null)
					sb.append(" DEFAULT ").append(def);
				//	Null
				if (sourceColumns.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls)
					sb.append(" NOT NULL");
				else
					sb.append(" NULL");
				
				//	Check Contraints


			}	//	for all columns
			sourceColumns.close();

			//	Primary Key
			ResultSet sourcePK = md.getPrimaryKeys(catalog, schema, table);
			//	TABLE_CAT=null, TABLE_SCHEM=REFERENCE, TABLE_NAME=A_ASSET, COLUMN_NAME=A_ASSET_ID, KEY_SEQ=1, PK_NAME=A_ASSET_KEY
			first = true;
			boolean hasPK = false;
			while (sourcePK.next())
			{
				hasPK = true;
                                //begin vpj-cd e-evolution 06/14/2005
				String PK_NAME = sourcePK.getString("PK_NAME");
				if(PK_NAME.indexOf("KEY") < 0)
				{
					PK_NAME =  PK_NAME + "_KEY";
				}				
				//end vpj-cd e-evolution
				if (first)
                                        //begin vpj-cd e-evolution 06/14/2005
					//sb.append(", CONSTRAINT ").append(sourcePK.getString("PK_NAME")).append(" PRIMARY KEY (");
					sb.append(", CONSTRAINT ").append(PK_NAME).append(" PRIMARY KEY (");
					//end vpj-cd e-evolution 06/14/2005					
				else
					sb.append(",");
				first = false;
				String columnName = sourcePK.getString("COLUMN_NAME");
				sb.append(checkColumnName(columnName));
			}
			if (hasPK)	//	close constraint
				sb.append(")");	// USING INDEX TABLESPACE INDX
			sourcePK.close();
			//
			sb.append(")");	//	close create table
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "createTable", ex);
			return false;
		}

		//	Execute Create Table
		if (!executeCommands(new String[]{sb.toString()}, m_conn, false, true))
			return true;	// continue
		
		//	Create Inexes
		// begin vpj-cd e-evolution 03/11/2005		
		//createTableIndexes(mTable, md);
                // end vpj-cd e-evolution 03/11/2005
		
		return createTableData(mTable);
	}	//	createTable
	
	/**
	 * 	Check Column Name
	 *	@param columnName column name
	 *	@return column name with correct case
	 */
	private String checkColumnName (String columnName)
	{
		return M_Element.getColumnName (columnName);
	}	//	checkColumnName
	
	/**
	 * 	Create Table Indexes
	 *	@param mTable table
	 */
	private void createTableIndexes(M_Table mTable, DatabaseMetaData md)
	{
		String tableName = mTable.getTableName();
		log.info(tableName);
		String catalog = m_dbSource.getCatalog();
		String schema = m_dbSource.getSchema();
		String table = tableName.toUpperCase();
		try
		{
			ResultSet sourceIndex = md.getIndexInfo(catalog, schema, table, false, false);

		}
		catch (Exception e)
		{
			
		}
	}	//	createTableIndexes
	
	
	/**
	 * 	Create/Copy Table Data
	 *	@param mTable model table
	 *	@return true if data created/copied
	 */
	private boolean createTableData (M_Table mTable)
	{
		boolean success = true;
		int count = 0;
		int errors = 0;
		long start = System.currentTimeMillis();
		
		//	Get Table Data
		String sql = "SELECT * FROM " + mTable.getTableName();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, mTable.get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				if (createTableDataRow(rs, mTable))
					count++;
				else
					errors++;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
			success = false;
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		long elapsed = System.currentTimeMillis() - start;
		log.config("Inserted=" + count + " - Errors=" + errors 
			+ " - " + elapsed + " ms");
		return success;
	}	//	createTableData
	
	/**
	 * 	Create Table Data Row
	 *	@param rs result set
	 *	@param mTable table
	 *	@return true if created
	 */
	private boolean createTableDataRow (ResultSet rs, M_Table mTable)
	{
		StringBuffer insert = new StringBuffer ("INSERT INTO ")
			.append(mTable.getTableName()).append(" (");
		StringBuffer values = new StringBuffer ();
		//
		M_Column[] columns = mTable.getColumns(false);
		for (int i = 0; i < columns.length; i++)
		{
			if (i != 0)
			{
				insert.append(",");
				values.append(",");
			}
			M_Column column = columns[i];
			String columnName = column.getColumnName();
			insert.append(columnName);
			//
			int dt = column.getAD_Reference_ID();
			try
			{
				Object value = rs.getObject(columnName);
				if (rs.wasNull())
				{
					values.append("NULL");
				}
				else if (columnName.endsWith("_ID")	// Record_ID, C_ProjectType defined as Button
					|| DisplayType.isNumeric(dt) 
					|| (DisplayType.isID(dt) && !columnName.equals("AD_Language"))) 
				{
					BigDecimal bd = rs.getBigDecimal(columnName);
					String s = m_dbTarget.TO_NUMBER(bd, dt);
					values.append(s);
				}
				else if (DisplayType.isDate(dt))
				{
					Timestamp ts = rs.getTimestamp(columnName);
					String tsString = m_dbTarget.TO_DATE(ts, dt == DisplayType.Date);
					values.append(tsString);
				}
				else if (DisplayType.isLOB(dt))
				{
					// ignored
					values.append("NULL");
				}
				else if (DisplayType.isText(dt) || dt == DisplayType.YesNo 
					|| dt == DisplayType.List || dt == DisplayType.Button
					|| columnName.equals("AD_Language"))
				{
					String s = rs.getString(columnName);
					values.append(DB.TO_STRING(s));
				}
				else
				{
					log.warning("Unknown DisplayType=" + dt 
						+ " - " + value + " [" + value.getClass().getName() + "]");
					values.append("NuLl");
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, columnName, e);
			}
		}	//	for all columns
		
		//
		insert.append(") VALUES (").append(values).append(")");
		return executeCommands(new String[]{insert.toString()}, 
			m_conn, false, false);	//	do not convert as text is converted
	}	//	createTableDataRow
	
	
	/**
	 * 	Enable Constraints
	 *	@param list list
	 *	@return true if constraints enabled/created
	 */
	private boolean enableConstraints (ArrayList list)
	{
		log.info("");
		return false;
	}	//	enableConstraints
	

	private void databaseBuild()
	{
		//	Build Script
		String fileName = "C:\\Compiere\\compiere-all2\\db\\database\\DatabaseBuild.sql";
		File file = new File (fileName);
		if (!file.exists())
			log.severe("No file: " + fileName);
		
	//	FileReader reader = new FileReader (file);
		
		
		
	}	//	databaseBuild
	
	/**
	 * 	Get Connection
	 * 	@param asSystem if true execute as db system administrator 
	 *	@return connection or null
	 */
	private Connection getConnection (boolean asSystem, boolean createNew)
	{
		if (!createNew && m_conn != null)
			return m_conn;
		//
		String dbUrl = m_dbTarget.getConnectionURL(m_databaseHost, m_databasePort, 
			(asSystem ? m_dbTarget.getSystemDatabase(m_databaseName) : m_databaseName), 
			(asSystem ? m_dbTarget.getSystemUser() : m_compiereUser));
		try
		{
			if (asSystem)
				m_conn = m_dbTarget.getDriverConnection(dbUrl, m_dbTarget.getSystemUser(), m_systemPassword);
			else
				m_conn = m_dbTarget.getDriverConnection(dbUrl, m_compiereUser, m_compierePassword);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, dbUrl, e);
		}
		return m_conn;
	}	//	getConnection
	
	
	/**************************************************************************
	 * 	Execute Commands
	 * 	@param cmds array of SQL commands
	 * 	@param conn connection
	 * 	@param batch tf true commit as batch
	 * 	@param doConvert convert to DB specific notation
	 *	@return true if success
	 */
	private boolean executeCommands (String[] cmds, Connection conn, 
		boolean batch, boolean doConvert)
	{
		if (cmds == null || cmds.length == 0)
		{
			log.warning("No Commands");
			return false;
		}
		
		Statement stmt = null;
		String cmd = null;
		String cmdOriginal = null;
		try
		{
			if (conn == null)
			{
				conn = getConnection(false, false);
				if (conn == null)
					return false;
			}
			if (conn.getAutoCommit() == batch)
				conn.setAutoCommit(!batch);
			stmt = conn.createStatement();
			
			//	Commands
			for (int i = 0; i < cmds.length; i++)
			{
				cmd = cmds[i];
				cmdOriginal = cmds[i];
				if (cmd == null || cmd.length() == 0)
					continue;
				//
				if (cmd.indexOf('@') != -1)
				{
					cmd = Util.replace(cmd, "@SystemPassword@", m_systemPassword);
					cmd = Util.replace(cmd, "@CompiereUser@", m_compiereUser);
					cmd = Util.replace(cmd, "@CompierePassword@", m_compierePassword);
					cmd = Util.replace(cmd, "@SystemPassword@", m_systemPassword);
					cmd = Util.replace(cmd, "@DatabaseName@", m_databaseName);
					if (m_databaseDevice != null)
						cmd = Util.replace(cmd, "@DatabaseDevice@", m_databaseDevice);
				}
				if (doConvert)
					cmd = m_dbTarget.convertStatement(cmd);
				writeLog(cmd);
				log.finer(cmd);
				int no = stmt.executeUpdate(cmd);
				log.finest("# " + no);
			}
			//
			stmt.close();
			stmt = null;
			//
			if (batch)
				conn.commit();
			//
			return true;
		}
		catch (Exception e)
		{
			String msg = e.getMessage();
			if (msg == null || msg.length() == 0)
				msg = e.toString();
			msg += " (";
			if (e instanceof SQLException)
			{
				msg += "State=" + ((SQLException)e).getSQLState() 
					+ ",ErrorCode=" + ((SQLException)e).getErrorCode();
			}
			msg += ")";
			if (cmdOriginal != null && !cmdOriginal.equals(cmd))
				msg += " - " + cmdOriginal;
			msg += "\n=>" + cmd;
			log.log(Level.SEVERE, msg);
		}
		//	Error clean up
		try
		{
			if (stmt != null)
				stmt.close();
		}
		catch (SQLException e1)
		{
			log.log(Level.SEVERE, "close statement", e1);
		}
		stmt = null;
		return false;
	}	//	execureCommands

	
	/**
	 * 	Write to File Log
	 *	@param cmd cmd
	 */
	private void writeLog (String cmd)
	{
		try
		{
			if (m_writer == null)
			{
				File file = File.createTempFile("create", ".log");
				m_writer = new PrintWriter(new FileWriter(file));
				log.info(file.toString());
			}
			m_writer.println(cmd);
			m_writer.flush();
		}
		catch (Exception e)
		{
			log.severe(e.toString());
		}
	}	//	writeLog
	
	private PrintWriter 	m_writer = null;
	
	
	/**************************************************************************
	 * 	Create DB
	 *	@param args
	 */
	public static void main (String[] args)
	{
		Compiere.startup(true);
		CLogMgt.setLevel(Level.FINE);
		CLogMgt.setLoggerLevel(Level.FINE,null);

		//	C_UOM_Conversion
		//	I_BankStatement
		//	
		//	Sybase
                //begin vpj-cd e-Evolution 03/03/2005 PostgreSQL
		//PostgreSQL
		//CreateCompiere cc = new CreateCompiere (Database.DB_SYBASE, "dev2", 0, "");
		//cc.setCompiereUser("compiere", "compiere");
		//cc.setDatabaseName("compiere", "compiere");
                CreateCompiere cc = new CreateCompiere (Database.DB_POSTGRESQL, "vpj", 5432 , "postgres");
		cc.setCompiereUser("compiere", "compiere");
		cc.setDatabaseName("compiere", "compiere");
                // end begin vpj-cd e-Evolution 03/03/2005 PostgreSQL	
		if (!cc.testConnection())
			return;
		cc.cleanStart();
		//
	//	cc.copy(null, false);
		cc.copy("TableName > 'C_RfQResponseLineQty'", false);
	}	//	main
        
        //begin vpj-cd e-evolution 02.10.2005
        	/**
	 * 	Create Tables and copy data
	 * 	@param whereClause optional where clause
	 * 	@param dropFirst drop first
	 *	@return true if executed
	 */
	public boolean copydata (String whereClause)
	{
		log.info(whereClause);
		if (getConnection(false, true) == null)
			return false;
		//
		boolean success = true;
		int count = 0;
		ArrayList list = new ArrayList();
		String sql = "SELECT * FROM AD_Table";
		if (whereClause != null && whereClause.length() > 0)
			sql += " WHERE " + whereClause;
		sql += " ORDER BY TableName";
		//
		PreparedStatement pstmt = null;
		try
		{
			file = new FileOutputStream("/home/vpj-cd/kompiere/data.sql");
	        filedata = new PrintStream( file );
	        //java.io.FileWriter file = new java.io.FileWriter("/home/vpj-cd/data.sql");                
	        filedata.println("SET CONSTRAINTS ALL DEFERRED;");
	        filedata.println("BEGIN;");       
	        
			pstmt = DB.prepareStatement (sql);
			DatabaseMetaData md = pstmt.getConnection().getMetaData();
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next() && success)
			{
				M_Table table = new M_Table (m_ctx, rs, null);
				if (table.isView())
					continue;  
                                
                                getTableDataRow(table);
                                                                 
				/*if (dropFirst)
				{
					executeCommands(new String[]
					    {"DROP TABLE " + table.getTableName()}, 
						m_conn, false, false);
				}
				//
				if (createTable (table, md))
				{
					list.add(table.getTableName());
					count++;
				}
				else
					success = false;
                                 */
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
			success = false;
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
                //begin e-evolution vpj-cd

                //end e-evolution vpj-cd
		if (!success)
			return false;
		
		/**	Enable Contraints */
		//enableConstraints(list);

		//databaseBuild();
		
		log.info("#" + count);
		
		try
		{
			if (m_conn != null)
				m_conn.close();
		}
		catch (SQLException e2)
		{
			log.log(Level.SEVERE, "close connection", e2);
		}
		m_conn = null;
		filedata.println("COMMIT;");                
        filedata.close();
		return success;
	}	//	copy
        //end vpj-cd e-evolution 02.10.200
                //begin vpj-cd e-evolution 06/14/2005
        /**
	 * 	Create Table Data Row
	 *	@param rs result set
	 *	@param mTable table
	 *	@return true if created
	 */
	private boolean getTableDataRow (M_Table mTable) 
	{
              	                         
        boolean success = true;
		//int count = 0;
		//int errors = 0;
		//long start = System.currentTimeMillis();
		
		//	Get Table Data
        
		String sql = "SELECT * FROM " + mTable.getTableName();
		PreparedStatement pstmt = null;
		try
		{                                  
                
			pstmt = DB.prepareStatement (sql, mTable.get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
                                            StringBuffer insert = new StringBuffer ("INSERT INTO ").append(mTable.getTableName()).append(" (");
                                            StringBuffer values = new StringBuffer ();
                                            //
                                            M_Column[] columns = mTable.getColumns(false);
                                            for (int i = 0; i < columns.length; i++)
                                            {
                                                    if (i != 0)
                                                    {
                                                            insert.append(",");
                                                            values.append(",");
                                                    }
                                                    M_Column column = columns[i];
                                                    String columnName = column.getColumnName();
                                                    insert.append(columnName);
                                                    //
                                                    int dt = column.getAD_Reference_ID();
                                                    try
                                                    {
                                                            Object value = rs.getObject(columnName);
                                                            if (rs.wasNull())
                                                            {
                                                                    values.append("NULL");
                                                            }
                                                            else if (columnName.endsWith("_ID")	// Record_ID, C_ProjectType defined as Button
                                                                    || DisplayType.isNumeric(dt) 
                                                                    || (DisplayType.isID(dt) && !columnName.equals("AD_Language"))) 
                                                            {
                                                                    BigDecimal bd = rs.getBigDecimal(columnName);
                                                                    String s = m_dbTarget.TO_NUMBER(bd, dt);
                                                                    values.append(s);
                                                            }
                                                            else if (DisplayType.isDate(dt))
                                                            {
                                                                    Timestamp ts = rs.getTimestamp(columnName);
                                                                    String tsString = m_dbTarget.TO_DATE(ts, dt == DisplayType.Date);
                                                                    values.append(tsString);
                                                            }
                                                            else if (DisplayType.isLOB(dt))
                                                            {
                                                                    // ignored
                                                                    values.append("NULL");
                                                            }
                                                            else if (DisplayType.isText(dt) || dt == DisplayType.YesNo 
                                                                    || dt == DisplayType.List || dt == DisplayType.Button
                                                                    || columnName.equals("AD_Language"))
                                                            {
                                                                    String s = rs.getString(columnName);
                                                                    values.append(DB.TO_STRING(s));
                                                            }
                                                            else
                                                            {
                                                                    log.warning("Unknown DisplayType=" + dt 
                                                                            + " - " + value + " [" + value.getClass().getName() + "]");
                                                                    values.append("NuLl");
                                                            }
                                                    }
                                                    catch (Exception e)
                                                    {
                                                            log.log(Level.SEVERE, columnName, e);
                                                    }
                                            }	//	for all columns

                                            //
                                            insert.append(") VALUES (").append(values).append(")");
                                            String data = insert.toString()+";";                                            
                                            //filedata.println("dato");
                                            filedata.println(data);                                                          
                                            
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;                			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
			success = false;
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}               
        return success;
		//m_conn, false, false);	//	do not convert as text is converted
	}	//	createTableDataRow
	//end vpj-cd e-evolution 06/14/2005	
	
}   //  CreateCompiere
