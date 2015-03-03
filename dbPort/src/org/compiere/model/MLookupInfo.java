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
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Info Class for Lookup SQL (ValueObject)
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MLookupInfo.java,v 1.18 2005/10/26 00:38:17 jjanke Exp $
 */
public class MLookupInfo implements Serializable, Cloneable
{
	/**
	 *  Constructor.
	 * 	(called from MLookupFactory)
	 *  @param sqlQuery SQL query
	 *  @param keyColumn key column
	 *  @param zoomWindow zoom window
	 *  @param zoomWindowPO PO zoom window
	 *  @param zoomQuery zoom query
	 */
	public MLookupInfo (String sqlQuery, String tableName, String keyColumn, int zoomWindow, int zoomWindowPO, 
		MQuery zoomQuery)
	{
		if (sqlQuery == null)
			throw new IllegalArgumentException("sqlQuery is null");
		Query = sqlQuery;
		if (keyColumn == null)
			throw new IllegalArgumentException("keyColumn is null");
		TableName = tableName;
		KeyColumn = keyColumn;
		ZoomWindow = zoomWindow;
		ZoomWindowPO = zoomWindowPO;
		ZoomQuery = zoomQuery;
	}   //  MLookupInfo
	
	static final long serialVersionUID = -7958664359250070233L;

	/** SQL Query       */
	public String       Query = null;
	/** Table Name      */
	public String       TableName = "";
	/** Key Column      */
	public String       KeyColumn = "";
	/** Zoom Window     */
	public int          ZoomWindow;
	/** Zoom Window     */
	public int          ZoomWindowPO;
	/** Zoom Query      */
	public MQuery       ZoomQuery = null;

	/** Direct Access Query 	*/
	public String       QueryDirect = "";
	/** Parent Flag     */
	public boolean      IsParent = false;
	/** Key Flag     	*/
	public boolean      IsKey = false;
	/** Validation code */
	public String       ValidationCode = "";
	/** Validation flag */
	public boolean      IsValidated = true;

	public Properties   ctx = null;
	public int          WindowNo;

	/**	AD_Column_Info or AD_Process_Para	*/
	public int          Column_ID;
	/** AD_Reference_ID						*/
	public int          DisplayType;
	/** Real AD_Reference_ID				*/
	public int 			AD_Reference_Value_ID;
	/** CreadedBy?updatedBy					*/
	public boolean		IsCreadedUpdatedBy = false;
	

	/**
	 * String representation
	 * @return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MLookupInfo[")
			.append(KeyColumn).append(" - Direct=").append(QueryDirect)
			.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Clone
	 *	@return deep copy
	 */
	public MLookupInfo cloneIt()
	{
		try
		{
			MLookupInfo clone = (MLookupInfo)super.clone();
			return clone;
		}
		catch (Exception e)
		{
			CLogger.get().log(Level.SEVERE, "cloneIt", e);
		}
		return null;
	}	//	clone

	
	/**************************************************************************
	 *  Get first AD_Reference_ID of a matching Reference Name.
	 *  Can have SQL LIKE placeholders.
	 *  (This is more a development tool than used for production)
	 *  @param referenceName reference name
	 *  @return AD_Reference_ID
	 */
	public static int getAD_Reference_ID (String referenceName)
	{
		int retValue = 0;
		String sql = "SELECT AD_Reference_ID,Name,ValidationType,IsActive "
			+ "FROM AD_Reference WHERE Name LIKE ?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, referenceName);
			ResultSet rs = pstmt.executeQuery();
			//
			int i = 0;
			int id = 0;
			String refName = "";
			String validationType = "";
			boolean isActive = false;
			while (rs.next())
			{
				id = rs.getInt(1);
				if (i == 0)
					retValue = id;
				refName = rs.getString(2);
				validationType = rs.getString(3);
				isActive = rs.getString(4).equals("Y");
				CLogger.get().config("AD_Reference Name=" + refName + ", ID=" + id + ", Type=" + validationType + ", Active=" + isActive);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			CLogger.get().log(Level.SEVERE, "getAD_Reference_ID", e);
		}
		return retValue;
	}   //  getAD_Reference_ID

	/**
	 *  Get first AD_Column_ID of matching ColumnName.
	 *  Can have SQL LIKE placeholders.
	 *  (This is more a development tool than used for production)
	 *  @param columnName column name
	 *  @return AD_Column_ID
	 */
	public static int getAD_Column_ID (String columnName)
	{
		int retValue = 0;
		String sql = "SELECT c.AD_Column_ID,c.ColumnName,t.TableName "
			+ "FROM AD_Column c, AD_Table t "
			+ "WHERE c.ColumnName LIKE ? AND c.AD_Table_ID=t.AD_Table_ID";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, columnName);
			ResultSet rs = pstmt.executeQuery();
			//
			int i = 0;
			int id = 0;
			String colName = "";
			String tabName = "";
			while (rs.next())
			{
				id = rs.getInt(1);
				if (i == 0)
					retValue = id;
				colName = rs.getString(2);
				tabName = rs.getString(3);
				CLogger.get().config("AD_Column Name=" + colName + ", ID=" + id + ", Table=" + tabName);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			CLogger.get().log(Level.SEVERE, "getAD_Column_ID", e);
		}
		return retValue;
	}   //  getAD_Reference_ID

}   //  MLookupInfo
