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

import java.sql.*;
import java.util.*;

import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Record Access Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRecordAccess.java,v 1.13 2005/10/26 00:38:16 jjanke Exp $
 */
public class MRecordAccess extends X_AD_Record_Access
{
	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MRecordAccess (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MRecordAccess

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRecordAccess (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRecordAccess

	/**
	 * 	Full New Constructor
	 *	@param ctx context
	 *	@param AD_Role_ID role
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 */
	public MRecordAccess (Properties ctx, int AD_Role_ID, int AD_Table_ID, int Record_ID, String trxName)
	{
		super (ctx,0, trxName);
		setAD_Role_ID(AD_Role_ID);
		setAD_Table_ID(AD_Table_ID);
		setRecord_ID(Record_ID);
		//
		setIsExclude (true);
		setIsReadOnly (false);
		setIsDependentEntities(false);
	}	//	MRecordAccess

	//	Key Column Name			*/
	private String		m_keyColumnName = null;
	
	/**
	 * 	Get Key Column Name
	 *	@return Key Column Name
	 */
	public String getKeyColumnName()
	{
		if (m_keyColumnName != null)
			return m_keyColumnName;
		//
		String sql = "SELECT ColumnName "
			+ "FROM AD_Column "
			+ "WHERE AD_Table_ID=? AND IsKey='Y' AND IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, getAD_Table_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				String s = rs.getString(1);
				if (m_keyColumnName == null)
					m_keyColumnName = s;
				else
					log.log(Level.SEVERE, "More than one key = " + s);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
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
		if (m_keyColumnName == null)
			log.log(Level.SEVERE, "Record Access requires Table with one key column");
		//
		return m_keyColumnName;
	}	//	getKeyColumnName

	/**
	 * 	Get Synonym of Column
	 *	@return Synonym Column Name
	 */
	public String getSynonym()
	{
		if ("AD_User_ID".equals(getKeyColumnName()))
			return "SalesRep_ID";
		else if ("C_ElementValue_ID".equals(getKeyColumnName()))
			return "Account_ID";
		//
		return null;
	}	//	getSynonym

	/**
	 * 	Key Column has a Synonym
	 *	@return true if Key Column has Synonym
	 */
	public boolean isSynonym()
	{
		return getSynonym() == null;
	}	//	isSynonym

	/**
	 * 	Get Key Column Name with consideration of Synonym
	 *	@param tableInfo
	 *	@return key column name
	 */
	public String getKeyColumnName (AccessSqlParser.TableInfo[] tableInfo)
	{
		String columnSyn = getSynonym();
		if (columnSyn == null)
			return m_keyColumnName;
		//	We have a synonym - ignore it if base table inquired
		for (int i = 0; i < tableInfo.length; i++)
		{
			if (m_keyColumnName.equals("AD_User_ID"))
			{
				//	List of tables where not to use SalesRep_ID
				if (tableInfo[i].getTableName().equals("AD_User"))
					return m_keyColumnName;
			}
			else if (m_keyColumnName.equals("AD_ElementValue_ID"))
			{
				//	List of tables where not to use Account_ID
				if (tableInfo[i].getTableName().equals("AD_ElementValue"))
					return m_keyColumnName;
			}
		}	//	tables to be ignored
		return columnSyn;
	}	//	getKeyColumnInfo

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MRecordAccess[AD_Role_ID=")
			.append(getAD_Role_ID())
			.append(",AD_Table_ID=").append(getAD_Table_ID())
			.append(",Record_ID=").append(getRecord_ID())
			.append(",Active=").append(isActive())
			.append(",Exclude=").append(isExclude())
			.append(",ReadOnly=").append(isReadOnly())
			.append(",Dependent=").append(isDependentEntities())
			.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Extended String Representation
	 *	@return extended info
	 */
	public String toStringX (Properties ctx)
	{
		String in = Msg.getMsg(ctx, "Include");
		String ex = Msg.getMsg(ctx, "Exclude");
		StringBuffer sb = new StringBuffer();
		sb.append(Msg.translate(ctx, "AD_Table_ID"))
				.append("=").append(getTableName(ctx)).append(", ")
			.append(Msg.translate(ctx, "Record_ID"))
			.	append("=").append(getRecord_ID())
			.append(" - ").append(Msg.translate(ctx, "IsDependentEntities"))
				.append("=").append(isDependentEntities())
			.append(" (").append(Msg.translate(ctx, "IsReadOnly")).append("=").append(isReadOnly())
			.append(") - ").append(isExclude() ?  ex : in);
		return sb.toString();
	}	//	toStringX

	/**	TableName			*/
	private String		m_tableName;

	/**
	 * 	Get Table Name
	 *	@param ctx context
	 *	@return table name
	 */
	public String getTableName (Properties ctx)
	{
		if (m_tableName == null)
		{
			String sql = "SELECT TableName FROM AD_Table WHERE AD_Table_ID=?";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, getAD_Table_ID());
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
					m_tableName = rs.getString(1);
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql, e);
			}
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
			//	Get Clear Text
			String realName = Msg.translate(ctx, m_tableName + "_ID");
			if (!realName.equals(m_tableName + "_ID"))
				m_tableName = realName;
		}		
		return m_tableName;
	}	//	getTableName

}	//	MRecordAccess
