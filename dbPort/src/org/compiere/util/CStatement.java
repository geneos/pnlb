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

import java.math.*;
import java.rmi.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.sql.*;
import org.compiere.db.*;
import org.compiere.interfaces.*;

/**
 *	Compiere Statement
 *	
 *  @author Jorg Janke
 *  @version $Id: CStatement.java,v 1.18 2005/11/20 22:40:44 jjanke Exp $
 */
public class CStatement implements Statement
{
	/**
	 *	Prepared Statement Constructor
	 *
	 *  @param resultSetType - ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE
	 *  @param resultSetConcurrency - ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 * 	@param trxName transaction name or null
	 */
	public CStatement (int resultSetType, int resultSetConcurrency,
		String trxName)
	{
		p_vo = new CStatementVO (resultSetType, resultSetConcurrency);

		//	Local access
		if (!DB.isRemoteObjects())
		{
			try
			{
				Connection conn = null;
				Trx trx = trxName == null ? null : Trx.get(trxName, true);
				if (trx != null)
					conn = trx.getConnection();
				else
				{
					if (resultSetConcurrency == ResultSet.CONCUR_UPDATABLE)
						conn = DB.getConnectionRW ();
					else
						conn = DB.getConnectionRO();
				}
				if (conn == null)
					throw new DBException("No Connection");
				p_stmt = conn.createStatement(resultSetType, resultSetConcurrency);
				return;
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, "CStatement", e);
			}
		}
	}	//	CPreparedStatement

	/**
	 * 	Minimum Constructor for sub classes
	 */
	protected CStatement()
	{
		super();
	}	//	CStatement

	/**
	 * 	Remote Constructor
	 *	@param vo value object
	 */
	public CStatement (CStatementVO vo)
	{
		p_vo = vo;
	}	//	CPreparedStatement


	/**	Logger							*/
	protected transient CLogger			log = CLogger.getCLogger (getClass());
	/** Used if local					*/
	protected transient Statement		p_stmt = null;
	/**	Value Object					*/
	protected CStatementVO				p_vo = null;
	/** Remote Errors					*/
	protected int						p_remoteErrors = 0;


	/**
	 * 	Execute Query
	 * 	@param sql0 unconverted SQL to execute
	 * 	@return ResultSet or RowSet
	 * 	@throws SQLException
	 * @see java.sql.Statement#executeQuery(String)
	 */
	public ResultSet executeQuery (String sql0) throws SQLException
	{
		//	Convert SQL
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (p_stmt != null)	//	local
			return p_stmt.executeQuery(p_vo.getSql());
			
		//	Client -> remote sever
		log.finest("server => " + p_vo + ", Remote=" + DB.isRemoteObjects());
		try
		{
			boolean remote = DB.isRemoteObjects() && CConnection.get().isAppsServerOK(false);
			if (remote && p_remoteErrors > 1)
				remote = CConnection.get().isAppsServerOK(true);
			if (remote)
			{
				Server server = CConnection.get().getServer();
				if (server != null)
				{
					ResultSet rs = server.stmt_getRowSet (p_vo);
					if (rs == null)
						log.warning("ResultSet is null - " + p_vo);
					else
						p_remoteErrors = 0;
					return rs;
				}
				log.log(Level.SEVERE, "AppsServer not found");
				p_remoteErrors++;
			}
		}
		catch (RemoteException ex)
		{
			log.log(Level.SEVERE, "AppsServer error", ex);
			p_remoteErrors++;
		}
		//	Try locally
		log.warning("execute locally");
		Statement stmt = local_getStatement (false, null);	// shared connection
		return stmt.executeQuery(p_vo.getSql());
	}	//	executeQuery


	/**
	 * 	Execute Update
	 *	@param sql0 unconverted sql
	 *	@return no of updated rows
	 *	@throws SQLException
	 * @see java.sql.Statement#executeUpdate(String)
	 */
	public int executeUpdate (String sql0) throws SQLException
	{
		//	Convert SQL
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (p_stmt != null)	//	local
			return p_stmt.executeUpdate (p_vo.getSql());

		//	Client -> remote sever
		log.finest("server => " + p_vo + ", Remote=" + DB.isRemoteObjects());
		try
		{
			boolean remote = DB.isRemoteObjects() && CConnection.get().isAppsServerOK(false);
			if (remote && p_remoteErrors > 1)
				remote = CConnection.get().isAppsServerOK(true);
			if (remote)
			{
				Server server = CConnection.get().getServer();
				if (server != null)
				{
					int result = server.stmt_executeUpdate(p_vo);
					p_vo.clearParameters();		//	re-use of result set
					return result;
				}
				log.log(Level.SEVERE, "AppsServer not found");
				p_remoteErrors++;
			}
		}
		catch (RemoteException ex)
		{
			log.log(Level.SEVERE, "AppsServer error", ex);
			p_remoteErrors++;
		}
		//	Try locally
		log.warning("execute locally");
		Statement pstmt = local_getStatement (false, null);	//	shared connection
		return pstmt.executeUpdate(p_vo.getSql());
	}	//	executeUpdate

	/**
	 * 	Get Sql
	 *	@return sql
	 */
	public String getSql()
	{
		if (p_vo != null)
			return p_vo.getSql();
		return null;
	}	//	getSql


	/**
	 * 	Get Connection
	 *	@return connection for local - or null for remote
	 *	@throws SQLException
	 * @see java.sql.Statement#getConnection()
	 */
	public Connection getConnection () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getConnection();
		return null;
	}	//	getConnection

	/**
	 * 	Commit (if local)
	 *	@throws SQLException
	 */
	public void commit() throws SQLException
	{
		Connection conn = getConnection();
		if (conn != null && !conn.getAutoCommit())
		{
			conn.commit();
			log.fine("commit");
		}
	}	//	commit


	/**
	 * Method executeUpdate
	 * @param sql0 String
	 * @param autoGeneratedKeys int
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(String, int)
	 */
	public int executeUpdate (String sql0, int autoGeneratedKeys) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (p_stmt != null)
			return p_stmt.executeUpdate(p_vo.getSql(), autoGeneratedKeys);
		throw new java.lang.UnsupportedOperationException ("Method executeUpdate() not yet implemented.");
	}

	/**
	 * Method executeUpdate
	 * @param sql0 String
	 * @param columnIndexes int[]
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(String, int[])
	 */
	public int executeUpdate (String sql0, int[] columnIndexes) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (p_stmt != null)
			return p_stmt.executeUpdate(p_vo.getSql(), columnIndexes);
		throw new java.lang.UnsupportedOperationException ("Method executeUpdate() not yet implemented.");
	}

	/**
	 * Method executeUpdate
	 * @param sql0 String
	 * @param columnNames String[]
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(String, String[])
	 */
	public int executeUpdate (String sql0, String[] columnNames) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (p_stmt != null)
			return p_stmt.executeUpdate(p_vo.getSql(), columnNames);
		throw new java.lang.UnsupportedOperationException ("Method executeUpdate() not yet implemented.");
	}


	/**
	 * Method execute
	 * @param sql0 String
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Statement#execute(String)
	 */
	public boolean execute (String sql0) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (p_stmt != null)
			return p_stmt.execute(p_vo.getSql());
		throw new java.lang.UnsupportedOperationException ("Method execute() not yet implemented.");
	}

	/**
	 * Method execute
	 * @param sql0 String
	 * @param autoGeneratedKeys int
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Statement#execute(String, int)
	 */
	public boolean execute (String sql0, int autoGeneratedKeys) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (p_stmt != null)
			return p_stmt.execute(p_vo.getSql(), autoGeneratedKeys);
		throw new java.lang.UnsupportedOperationException ("Method execute() not yet implemented.");
	}

	/**
	 * Method execute
	 * @param sql0 String
	 * @param columnIndexes int[]
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Statement#execute(String, int[])
	 */
	public boolean execute (String sql0, int[] columnIndexes) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (p_stmt != null)
			return p_stmt.execute(p_vo.getSql(), columnIndexes);
		throw new java.lang.UnsupportedOperationException ("Method execute() not yet implemented.");
	}

	/**
	 * Method execute
	 * @param sql0 String
	 * @param columnNames String[]
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Statement#execute(String, String[])
	 */
	public boolean execute (String sql0, String[] columnNames) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (p_stmt != null)
			return p_stmt.execute(p_vo.getSql(), columnNames);
		throw new java.lang.UnsupportedOperationException ("Method execute() not yet implemented.");
	}



	/**************************************************************************
	 * 	Get Max Field Size
	 * 	@return field size
	 * 	@throws SQLException
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	public int getMaxFieldSize () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getMaxFieldSize();
		throw new java.lang.UnsupportedOperationException ("Method getMaxFieldSize() not yet implemented.");
	}

	/**
	 * Method setMaxFieldSize
	 * @param max int
	 * @throws SQLException
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	public void setMaxFieldSize (int max) throws SQLException
	{
		if (p_stmt != null)
			p_stmt.setMaxFieldSize(max);
		else
			throw new java.lang.UnsupportedOperationException ("Method setMaxFieldSize() not yet implemented.");
	}

	/**
	 * Method getMaxRows
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getMaxRows()
	 */
	public int getMaxRows () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getMaxRows();
		throw new java.lang.UnsupportedOperationException ("Method getMaxRows() not yet implemented.");
	}

	/**
	 * Method setMaxRows
	 * @param max int
	 * @throws SQLException
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	public void setMaxRows (int max) throws SQLException
	{
		if (p_stmt != null)
			p_stmt.setMaxRows(max);
		else
			throw new java.lang.UnsupportedOperationException ("Method setMaxRows() not yet implemented.");
	}

	/*************************************************************************
	 * 	Add Batch
	 *	@param sql sql
	 *	@throws SQLException
	 * @see java.sql.Statement#addBatch(String)
	 */
	public void addBatch (String sql) throws SQLException
	{
		if (p_stmt != null)
			p_stmt.addBatch(sql);
		else
			throw new java.lang.UnsupportedOperationException ("Method addBatch() not yet implemented.");
	}

	/**
	 * Method clearBatch
	 * @throws SQLException
	 * @see java.sql.Statement#clearBatch()
	 */
	public void clearBatch () throws SQLException
	{
		if (p_stmt != null)
			p_stmt.clearBatch();
		else
			throw new java.lang.UnsupportedOperationException ("Method clearBatch() not yet implemented.");
	}

	/**
	 * Method executeBatch
	 * @return int[]
	 * @throws SQLException
	 * @see java.sql.Statement#executeBatch()
	 */
	public int[] executeBatch () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.executeBatch();
		throw new java.lang.UnsupportedOperationException ("Method executeBatch() not yet implemented.");
	}


	/**
	 * Method getMoreResults
	 * @param current int
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	public boolean getMoreResults (int current) throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getMoreResults(current);
		throw new java.lang.UnsupportedOperationException ("Method getMoreResults() not yet implemented.");
	}


	/**
	 * Method getGeneratedKeys
	 * @return ResultSet
	 * @throws SQLException
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	public ResultSet getGeneratedKeys () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getGeneratedKeys();
		throw new java.lang.UnsupportedOperationException ("Method getGeneratedKeys() not yet implemented.");
	}

	/**
	 * Method getResultSetHoldability
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	public int getResultSetHoldability () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getResultSetHoldability();
		throw new java.lang.UnsupportedOperationException ("Method getResultSetHoldability() not yet implemented.");
	}

	/**
	 * Method setEscapeProcessing
	 * @param enable boolean
	 * @throws SQLException
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	public void setEscapeProcessing (boolean enable) throws SQLException
	{
		if (p_stmt != null)
			p_stmt.setEscapeProcessing(enable);
		else
			throw new java.lang.UnsupportedOperationException ("Method setEscapeProcessing() not yet implemented.");
	}

	/**
	 * Method getQueryTimeout
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	public int getQueryTimeout () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getQueryTimeout();
		throw new java.lang.UnsupportedOperationException ("Method getQueryTimeout() not yet implemented.");
	}

	/**
	 * Method setQueryTimeout
	 * @param seconds int
	 * @throws SQLException
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	public void setQueryTimeout (int seconds) throws SQLException
	{
		if (p_stmt != null)
			p_stmt.setQueryTimeout (seconds);
		else
			throw new java.lang.UnsupportedOperationException ("Method setQueryTimeout() not yet implemented.");
	}

	/**
	 * Method cancel
	 * @throws SQLException
	 * @see java.sql.Statement#cancel()
	 */
	public void cancel () throws SQLException
	{
		if (p_stmt != null)
			p_stmt.cancel();
		else
			throw new java.lang.UnsupportedOperationException ("Method cancel() not yet implemented.");
	}

	/**
	 * Method getWarnings
	 * @return SQLWarning
	 * @throws SQLException
	 * @see java.sql.Statement#getWarnings()
	 */
	public SQLWarning getWarnings () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getWarnings();
		throw new java.lang.UnsupportedOperationException ("Method getWarnings() not yet implemented.");
	}

	/**
	 * Method clearWarnings
	 * @throws SQLException
	 * @see java.sql.Statement#clearWarnings()
	 */
	public void clearWarnings () throws SQLException
	{
		if (p_stmt != null)
			p_stmt.clearWarnings();
		else
			throw new java.lang.UnsupportedOperationException ("Method clearWarnings() not yet implemented.");
	}

	/**
	 * Method setCursorName
	 * @param name String
	 * @throws SQLException
	 * @see java.sql.Statement#setCursorName(String)
	 */
	public void setCursorName (String name) throws SQLException
	{
		if (p_stmt != null)
			p_stmt.setCursorName(name);
		else
			throw new java.lang.UnsupportedOperationException ("Method setCursorName() not yet implemented.");
	}


	/**
	 * Method getResultSet
	 * @return ResultSet
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSet()
	 */
	public ResultSet getResultSet () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getResultSet();
		throw new java.lang.UnsupportedOperationException ("Method getResultSet() not yet implemented.");
	}

	/**
	 * Method getUpdateCount
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getUpdateCount()
	 */
	public int getUpdateCount () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getUpdateCount();
		throw new java.lang.UnsupportedOperationException ("Method getUpdateCount() not yet implemented.");
	}

	/**
	 * Method getMoreResults
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Statement#getMoreResults()
	 */
	public boolean getMoreResults () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getMoreResults();
		throw new java.lang.UnsupportedOperationException ("Method getMoreResults() not yet implemented.");
	}

	/**
	 * Method setFetchDirection
	 * @param direction int
	 * @throws SQLException
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	public void setFetchDirection (int direction) throws SQLException
	{
		if (p_stmt != null)
			p_stmt.setFetchDirection(direction);
		else
			throw new java.lang.UnsupportedOperationException ("Method setFetchDirection() not yet implemented.");
	}

	/**
	 * Method getFetchDirection
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getFetchDirection()
	 */
	public int getFetchDirection () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getFetchDirection();
		throw new java.lang.UnsupportedOperationException ("Method getFetchDirection() not yet implemented.");
	}

	/**
	 * Method setFetchSize
	 * @param rows int
	 * @throws SQLException
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	public void setFetchSize (int rows) throws SQLException
	{
		if (p_stmt != null)
			p_stmt.setFetchSize(rows);
		else
			throw new java.lang.UnsupportedOperationException ("Method setFetchSize() not yet implemented.");
	}

	/**
	 * Method getFetchSize
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getFetchSize()
	 */
	public int getFetchSize () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getFetchSize();
		throw new java.lang.UnsupportedOperationException ("Method getFetchSize() not yet implemented.");
	}

	/**
	 * Method getResultSetConcurrency
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	public int getResultSetConcurrency () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getResultSetConcurrency();
		throw new java.lang.UnsupportedOperationException ("Method getResultSetConcurrency() not yet implemented.");
	}

	/**
	 * Method getResultSetType
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetType()
	 */
	public int getResultSetType () throws SQLException
	{
		if (p_stmt != null)
			return p_stmt.getResultSetType();
		throw new java.lang.UnsupportedOperationException ("Method getResultSetType() not yet implemented.");
	}

	/**
	 * 	Close
	 * 	@throws SQLException
	 * @see java.sql.Statement#close()
	 */
	public void close () throws SQLException
	{
		if (p_stmt != null)
			p_stmt.close();
	}	//	close

	/*************************************************************************
	 * 	Execute Update.
	 *	@return row count
	 */
	public int remote_executeUpdate()
	{
		log.finest("");
		try
		{
			CompiereDatabase db = CConnection.get().getDatabase();
			if (db == null)
				throw new NullPointerException("Remote - No Database");
			//
			Statement pstmt = local_getStatement (false, null);	//	shared connection
			int result = pstmt.executeUpdate(p_vo.getSql());
			pstmt.close();
			//
			return result;
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, p_vo.toString(), ex);
			throw new RuntimeException (ex);
		}
	}	//	remote_executeUpdate

	/**************************************************************************
	 * 	Get Prepared Statement to create RowSet.
	 * 	Method called on Remote to execute locally.
	 * 	@param dedicatedConnection if true gets new connection - if false gets anormal RO/RW connection
	 * 	@return Prepared Statement
	 */
	private Statement local_getStatement (boolean dedicatedConnection, String trxName)
	{
		log.finest("");
		Connection conn = null;
		Trx trx = trxName == null ? null : Trx.get(trxName, true);
		if (trx != null)
			conn = trx.getConnection();
		else
		{
			if (dedicatedConnection)
				conn = DB.createConnection (false, Connection.TRANSACTION_READ_COMMITTED);
			else
				conn = local_getConnection (trxName);
		}
		Statement stmt = null;
		try
		{
			stmt = conn.createStatement(p_vo.getResultSetType(), p_vo.getResultSetConcurrency());
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, "local", ex);
			try
			{
				if (stmt != null)
					stmt.close();
				stmt = null;
			}
			catch (SQLException ex1)
			{
			}
		}
		return stmt;
	}	//	local_getStatement

	/**
	 * 	Get Local Connection
	 *	@return connection
	 */
	protected Connection local_getConnection(String trxName)
	{
		Connection conn = null;
		Trx trx = trxName == null ? null : Trx.get(trxName, true);
		if (trx != null)
			conn = trx.getConnection();
		else
		{
			if (p_vo.getResultSetConcurrency () == ResultSet.CONCUR_UPDATABLE)
				conn = DB.getConnectionRW ();
			else
				conn = DB.getConnectionRO ();
		}
		return conn;
	}	//	local_getConnection

	/*************************************************************************
	 * 	Get Result as RowSet for Remote.
	 * 	Get shared connection for RMI!
	 * 	If RowSet is transfred via RMI, closing the RowSet does not close the connection
	 *	@return result as RowSet
	 */
	public RowSet remote_getRowSet()
	{
		log.finest("remote");
		/**
		try
		{
			CompiereDatabase db = CConnection.get().getDatabase();
			if (db == null)
			{
				log.log(Level.SEVERE, "No Database");
				throw new NullPointerException("Remote - No Database");
			}
			//
			Statement stmt = local_getStatement (false, null);	// shared connection
			ResultSet rs = stmt.executeQuery(p_vo.getSql());
			RowSet rowSet = db.getRowSet (rs);
			rs.close();
			stmt.close();
			//
			if (rowSet != null)
				return rowSet;
			else
				log.log(Level.SEVERE, "No RowSet");
			throw new NullPointerException("Remore - No RowSet");
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, p_vo.toString(), ex);
			throw new RuntimeException (ex);
		}
	//	return null;
	 	**/
		//	Shared Connection
		Connection conn = local_getConnection (null);
		PreparedStatement pstmt = null;
		RowSet rowSet = null;
		try
		{
			pstmt = conn.prepareStatement(p_vo.getSql(),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//	Set Parameters
			ArrayList parameters = p_vo.getParameters();
			for (int i = 0; i < parameters.size(); i++)
			{
				Object o = parameters.get(i);
				if (o == null)
					throw new IllegalArgumentException ("Null Parameter #" + i);
				else if (o instanceof NullParameter)
				{
					int type = ((NullParameter)o).getType();
					pstmt.setNull(i+1, type);
					log.finest("#" + (i+1) + " - Null");
				}
				else if (o instanceof Integer)
				{
					pstmt.setInt(i+1, ((Integer)o).intValue());
					log.finest("#" + (i+1) + " - int=" + o);
				}
				else if (o instanceof String)
				{
					pstmt.setString(i+1, (String)o);
					log.finest("#" + (i+1) + " - String=" + o);
				}
				else if (o instanceof Timestamp)
				{
					pstmt.setTimestamp(i+1, (Timestamp)o);
					log.finest("#" + (i+1) + " - Timestamp=" + o);
				}
				else if (o instanceof BigDecimal)
				{
					pstmt.setBigDecimal(i+1, (BigDecimal)o);
					log.finest("#" + (i+1) + " - BigDecimal=" + o);
				}
				else
					throw new java.lang.UnsupportedOperationException ("Unknown Parameter Class=" + o.getClass());
			}
			//
			ResultSet rs = pstmt.executeQuery();
			rowSet = CCachedRowSet.getRowSet(rs);
			pstmt.close();
			pstmt = null;
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, p_vo.toString(), ex);
			throw new RuntimeException (ex);
		}
		//	Close Cursor
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "close pstmt", e);
		}
		return rowSet;
	}	//	remote_getRowSet

}	//	CStatement
