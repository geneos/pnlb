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
package org.compiere.report.core;

import java.math.*;
import java.sql.*;
import java.util.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Report Column
 *
 *  @author Jorg Janke
 *  @version  $Id: RColumn.java,v 1.15 2005/10/26 00:37:42 jjanke Exp $
 */
public class RColumn
{
	/**
	 * 	Create Report Column
	 *	@param ctx context 
	 *	@param columnName column name
	 *	@param displayType display type
	 */
	public RColumn (Properties ctx, String columnName, int displayType)
	{
		this (ctx, columnName, displayType, null, 0);
	}	//	RColumn

	/**
	 * 	Create Report Column
	 *	@param ctx context 
	 *	@param columnName column name
	 *	@param displayType display type
	 *	@param AD_Reference_Value_ID List/Table Reference
	 */
	public RColumn (Properties ctx, String columnName, int displayType, int AD_Reference_Value_ID)
	{
		this (ctx, columnName, displayType, null, AD_Reference_Value_ID);
	}	//	RColumn

	/**
	 *  Create Report Column
	 *	@param ctx context 
	 *	@param columnName column name
	 *	@param displayType display type
	 *	@param sql sql (if null then columnName is used). 
	 *	Will be overwritten if TableDir or Search 
	 */
	public RColumn (Properties ctx, String columnName, int displayType, String sql)
	{
		this (ctx, columnName, displayType, sql, 0);
	}
	/**
	 *  Create Report Column
	 *	@param ctx context 
	 *	@param columnName column name
	 *	@param displayType display type
	 *	@param sql sql (if null then columnName is used). 
	 *	@param AD_Reference_Value_ID List/Table Reference
	 *	Will be overwritten if TableDir or Search 
	 */
	public RColumn (Properties ctx, String columnName, int displayType, String sql, int AD_Reference_Value_ID)
	{
		m_colHeader = Msg.translate(ctx, columnName);
		m_displayType = displayType;
		m_colSQL = sql;
		if (m_colSQL == null || m_colSQL.length() == 0)
			m_colSQL = columnName;

		//  Strings
		if (displayType == DisplayType.String || displayType == DisplayType.Text 
			|| displayType == DisplayType.Memo)
			m_colClass = String.class;                  //  default size=30
		//  Amounts
		else if (displayType == DisplayType.Amount)
		{
			m_colClass = BigDecimal.class;
			m_colSize = 70;
		}
		//  Boolean
		else if (displayType == DisplayType.YesNo)
			m_colClass = Boolean.class;
		//  Date
		else if (DisplayType.isDate(displayType))
			m_colClass = Timestamp.class;
		//  Number
		else if (displayType == DisplayType.Quantity 
			|| displayType == DisplayType.Number  
			|| displayType == DisplayType.CostPrice)
		{
			m_colClass = Double.class;
			m_colSize = 70;
		}
		//  Integer
		else if (displayType == DisplayType.Integer)
			m_colClass = Integer.class;
		//  List
		else if (displayType == DisplayType.List)
		{
			Language language = Language.getLanguage(Env.getAD_Language(ctx));
			m_colSQL = "(" + MLookupFactory.getLookup_ListEmbed(
				language, AD_Reference_Value_ID, columnName) + ")";
			m_colClass = String.class;
			m_isIDcol = false;
		}
		/**  Table
		else if (displayType == DisplayType.Table)
		{
			Language language = Language.getLanguage(Env.getAD_Language(ctx));
			m_colSQL += ",(" + MLookupFactory.getLookup_TableEmbed(
				language, columnName, RModel.TABLE_ALIAS, AD_Reference_Value_ID) + ")";
			m_colClass = String.class;
			m_isIDcol = false;
		}	**/
		//  TableDir, Search,...
		else
		{
			m_colClass = String.class;
			Language language = Language.getLanguage(Env.getAD_Language(ctx));
			if (columnName.equals("Account_ID") || columnName.equals("User1_ID") || columnName.equals("User2_ID"))
			{
				m_colSQL += ",(" + MLookupFactory.getLookup_TableDirEmbed(
					language, "C_ElementValue_ID", RModel.TABLE_ALIAS, columnName) + ")";
				m_isIDcol = true;
			}
			else if (columnName.equals("C_LocFrom_ID") || columnName.equals("C_LocTo_ID"))
			{
				m_colSQL += ",(" + MLookupFactory.getLookup_TableDirEmbed(
					language, "C_Location_ID", RModel.TABLE_ALIAS, columnName) + ")";
				m_isIDcol = true;
			}
			else if (columnName.equals("AD_OrgTrx_ID"))
			{
				m_colSQL += ",(" + MLookupFactory.getLookup_TableDirEmbed(
					language, "AD_Org_ID", RModel.TABLE_ALIAS, columnName) + ")";
				m_isIDcol = true;
			}
			else if (displayType == DisplayType.TableDir)
			{
				m_colSQL += ",(" + MLookupFactory.getLookup_TableDirEmbed(
					language, columnName, RModel.TABLE_ALIAS) + ")";
				m_isIDcol = true;
			}
		}
	}   //  RColumn

	/**
	 *  Create Info Column (r/o and not color column)
	 *
	 *  @param colHeader Column Header
	 *  @param colSQL    SQL select code for column
	 *  @param colClass  class of column - determines display
	 */
	public RColumn (String colHeader, String colSQL, Class colClass)
	{
		m_colHeader = colHeader;
		m_colSQL = colSQL;
		m_colClass = colClass;
	}   //  RColumn


	/** Column Header               */
	private String      m_colHeader;
	/** Column SQL                  */
	private String      m_colSQL;
	/** Column Display Class        */
	private Class       m_colClass;

	/** Display Type                */
	private int         m_displayType = 0;
	/** Column Size im px           */
	private int         m_colSize = 30;

	private boolean     m_readOnly = true;
	private boolean     m_colorColumn = false;
	private boolean     m_isIDcol = false;


	/**
	 *  Column Header
	 */
	public String getColHeader()
	{
		return m_colHeader;
	}
	public void setColHeader(String colHeader)
	{
		m_colHeader = colHeader;
	}

	/**
	 *  Column SQL
	 */
	public String getColSQL()
	{
		return m_colSQL;
	}
	public void setColSQL(String colSQL)
	{
		m_colSQL = colSQL;
	}
	/**
	 *  This column is an ID Column (i.e. two values - int & String are read)
	 */
	public boolean isIDcol()
	{
		return m_isIDcol;
	}

	/**
	 *  Column Display Class
	 */
	public Class getColClass()
	{
		return m_colClass;
	}
	public void setColClass(Class colClass)
	{
		m_colClass = colClass;
	}

	/**
	 *  Column Size in px
	 */
	public int getColSize()
	{
		return m_colSize;
	}   //  getColumnSize

	/**
	 *  Column Size in px
	 */
	public void setColSize(int colSize)
	{
		m_colSize = colSize;
	}   //  getColumnSize

	/**
	 *  Get DisplayType
	 */
	public int getDisplayType()
	{
		return m_displayType;
	}   //  getDisplayType

	/**
	 *  Column is Readonly
	 */
	public boolean isReadOnly()
	{
		return m_readOnly;
	}
	public void setReadOnly(boolean readOnly)
	{
		m_readOnly = readOnly;
	}

	/**
	 *  This Color Determines the Color of the row
	 */
	public void setColorColumn(boolean colorColumn)
	{
		m_colorColumn = colorColumn;
	}
	public boolean isColorColumn()
	{
		return m_colorColumn;
	}
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("RColumn[");
		sb.append(m_colSQL).append("=").append(m_colHeader)
			.append("]");
		return sb.toString();
	}	//	toString

}   //  RColumn
