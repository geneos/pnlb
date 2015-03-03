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

import java.io.*;
import java.rmi.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.db.*;
import org.compiere.interfaces.*;
import org.compiere.util.*;

/**
 * 	Persistent Object LOB.
 * 	Allows to store LOB remotely
 * 	Currently Oracle specific!
 *	
 *  @author Jorg Janke
 *  @version $Id: PO_LOB.java,v 1.13 2005/10/01 23:56:39 jjanke Exp $
 */
public class PO_LOB implements Serializable
{
	/**
	 * 	Constructor
	 *	@param tableName table name
	 *	@param columnName column name
	 *	@param whereClause where 
	 *	@param displayType display type
	 *	@param value value
	 */
	public PO_LOB (String tableName, String columnName, String whereClause, 
		int displayType, Object value)
	{
		m_tableName = tableName;
		m_columnName = columnName;
		m_whereClause = whereClause;
		m_displayType = displayType;
		m_value = value;
	}	//	PO_LOB

	/**	Logger					*/
	protected CLogger	log = CLogger.getCLogger (getClass());
	/**	Table Name				*/
	private String 		m_tableName;
	/** Column Name				*/
	private String 		m_columnName;
	/** Where Clause			*/
	private String		m_whereClause;
	
	/** Display Type			*/
	private int			m_displayType;
	/** Data					*/
	private Object 		m_value;

	/**
	 * 	Save LOB
	 * 	@param whereClause clause
	 * 	@param trxName trx name
	 *	@return true if saved
	 */
	public boolean save (String whereClause, String trxName)
	{
		m_whereClause = whereClause;
		return save(trxName);
	}	//	save

	/**
	 * 	Save LOB.
	 * 	see also org.compiere.session.ServerBean#updateLOB
	 * 	@param trxName trx name
	 *	@return true if saved
	 */
	public boolean save (String trxName)
	{
		if (m_value == null
			|| (!(m_value instanceof String || m_value instanceof byte[])) 
			|| (m_value instanceof String && m_value.toString().length() == 0)
			|| (m_value instanceof byte[] && ((byte[])m_value).length == 0)
			)
		{
			StringBuffer sql = new StringBuffer ("UPDATE ")
				.append(m_tableName)
				.append(" SET ").append(m_columnName)
				.append("=null WHERE ").append(m_whereClause);
			int no = DB.executeUpdate(sql.toString(), trxName);
			log.fine("save [" + trxName + "] #" + no + " - no data - set to null - " + m_value);
			if (no == 0)
				log.warning("save [" + trxName + "] - not updated - " + sql);
			return true;
		}
		
		StringBuffer sql = new StringBuffer ("UPDATE ")
			.append(m_tableName)
			.append(" SET ").append(m_columnName)
			.append("=? WHERE ").append(m_whereClause);
		//
		boolean success = true;
		if (DB.isRemoteObjects())
		{
			log.fine("save [" + trxName + "] - Remote - " + m_value);
			Server server = CConnection.get().getServer();
			try
			{
				if (server != null)
				{	//	See ServerBean
					success = server.updateLOB (sql.toString(), m_displayType, m_value); 
					if (CLogMgt.isLevelFinest())
						log.fine("save - server => " + success);
					if (success)
						return true;
				}
				log.log(Level.SEVERE, "save - AppsServer not found"); 
			}
			catch (RemoteException ex)
			{
				log.log(Level.SEVERE, "save - AppsServer error", ex);
			}
		}
		
		log.fine("save [" + trxName + "] - Local - " + m_value);
		//	Connection
		Trx trx = null;
		if (trxName != null)
			trx = Trx.get(trxName, false);
		Connection con = null;
		//	Create Connection
		if (trx != null)
			con = trx.getConnection();
		if (con == null)
			con = DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED);
		if (con == null)
		{
			log.log(Level.SEVERE, "Could not get Connection");
			return false;
		}

		PreparedStatement pstmt = null;
		success = true;
		try
		{
			pstmt = con.prepareStatement(sql.toString());
			if (m_displayType == DisplayType.TextLong)
				pstmt.setString(1, (String)m_value);
			else
				pstmt.setBytes(1, (byte[])m_value);
			int no = pstmt.executeUpdate();
			if (no != 1)
			{
				log.fine("save [" + trxName + "] - Not updated #" + no + " - " + sql);
				success = false;
			}
			//
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.FINE, "[" + trxName + "] - " + sql, e);
			success = false;
		}
		//	Close Statement
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		
		//	Success - commit local trx
		if (success)
		{
			if (trx != null)
			{
				trx = null;
				con = null;
			}
			else
			{
				try
				{
					con.commit();
					con.close();
					con = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, "save [" + trxName + "] - commit " , e);
					success = false;
				}
			}
		}
		//	Error - roll back
		if (!success)
		{
			log.severe ("save [" + trxName + "] - rollback");
			if (trx != null)
			{
				trx.rollback();
				trx = null;
				con = null;
			}
			else
			{
				try
				{
					con.rollback();
					con.close();
					con = null;
				}
				catch (Exception ee)
				{
					log.log(Level.SEVERE, "save [" + trxName + "] - rollback" , ee);
				}
			}
		}
		
		//	Clean Connection
		try
		{
			if (con != null)
				con.close();
			con = null;
		}
		catch (Exception e)
		{
			con = null;
		}
		return success;
	}	//	save

	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("PO_LOB[");
		sb.append(m_tableName).append(".").append(m_columnName)
			.append(",DisplayType=").append(m_displayType)
			.append("]");
		return sb.toString();
	}	//	toString

}	//	PO_LOB
