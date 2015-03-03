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
package org.compiere.print;

//import java.util.logging.*;

/**
 *	Print Data Column.
 * 	Optional Meta Data of Columns
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: PrintDataColumn.java,v 1.8 2005/03/11 20:34:40 jjanke Exp $
 */
public class PrintDataColumn
{
	/**
	 * 	Print Data Column
	 *
	 * 	@param AD_Column_ID Column
	 * 	@param columnName Column Name
	 * 	@param displayType Display Type
	 * 	@param columnSize Column Size
	 *  @param alias Alias in query or the same as column name or null
	 *  @param isPageBreak if true force page break after function
	 */
	public PrintDataColumn (int AD_Column_ID, String columnName,
		int displayType, int columnSize,
		String alias, boolean isPageBreak)
	{
		m_AD_Column_ID = AD_Column_ID;
		m_columnName = columnName;
		//
		m_displayType = displayType;
		m_columnSize = columnSize;
		//
		m_alias = alias;
		if (m_alias == null)
			m_alias = columnName;
		m_pageBreak = isPageBreak;
	}	//	PrintDataColumn

	private int			m_AD_Column_ID;
	private String		m_columnName;
	private int			m_displayType;
	private int			m_columnSize;
	private String		m_alias;
	private boolean		m_pageBreak;

	/*************************************************************************/

	/**
	 * 	Get AD_Column_ID
	 * 	@return AD_Column_ID
	 */
	public int getAD_Column_ID()
	{
		return m_AD_Column_ID;
	}	//	getAD_Column_ID

	/**
	 * 	Get Column Name
	 * 	@return column name
	 */
	public String getColumnName()
	{
		return m_columnName;
	}	//	getColumnName

	/**
	 * 	Get Display Type
	 * 	@return display type
	 */
	public int getDisplayType()
	{
		return m_displayType;
	}	//	getDisplayType

	/**
	 * 	Get Alias Name
	 * 	@return alias column name
	 */
	public String getAlias()
	{
		return m_alias;
	}	//	getAlias

	/**
	 *	Column has Alias.
	 *  (i.e. has a key)
	 * 	@return true if Alias
	 */
	public boolean hasAlias()
	{
		return !m_columnName.equals(m_alias);
	}	//	hasAlias

	/**
	 * 	Column value forces page break
	 * 	@return true if page break
	 */
	public boolean isPageBreak()
	{
		return m_pageBreak;
	}	//	isPageBreak

	/**
	 *	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("PrintDataColumn[");
		sb.append("ID=").append(m_AD_Column_ID)
			.append("-").append(m_columnName);
		if (hasAlias())
			sb.append("(").append(m_alias).append(")");
		sb.append(",DisplayType=").append(m_displayType)
			.append(",Size=").append(m_columnSize)
			.append("]");
		return sb.toString();
	}	//	toString

}	//	PrintDataColumn
