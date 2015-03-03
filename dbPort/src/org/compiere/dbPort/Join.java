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
package org.compiere.dbPort;

import org.compiere.util.*;

/**
 *  Join Clause.
 *  <pre>
 *  f.AD_Column_ID = c.AD_Column_ID(+)
 *  </pre>
 *  @author     Jorg Janke
 *  @version    $Id: Join.java,v 1.6 2005/03/11 20:29:03 jjanke Exp $
 */
public class Join
{
	/**
	 *  Constructor
	 *  @param joinClause
	 */
	public Join (String joinClause)
	{
		if (joinClause == null)
			throw new IllegalArgumentException("Join - clause cannot be null");
		evaluate (joinClause);
	}   //  Join

	private String  m_joinClause;
	private String  m_mainTable;
	private String  m_mainAlias;
	private String  m_joinTable;
	private String  m_joinAlias;
	private boolean m_left;
	private String  m_condition;

	/**
	 *  Evaluate the clause.
	 *  e.g.    tb.AD_User_ID(+)=?
	 *          f.AD_Column_ID = c.AD_Column_ID(+)
	 *  @param joinClause
	 */
	private void evaluate (String joinClause)
	{
		m_joinClause = joinClause;
		int indexEqual = joinClause.indexOf('=');
		m_left = indexEqual < joinClause.indexOf("(+)");    //  converts to LEFT if true
		//  get table alias of it
		if (m_left)     //  f.AD_Column_ID = c.AD_Column_ID(+)  => f / c
		{
			m_mainAlias = joinClause.substring
				(0, Util.findIndexOf(joinClause, '.','=')).trim();          //  f
			int end = joinClause.indexOf('.', indexEqual);
			if (end == -1)  //  no alias
				end = joinClause.indexOf('(', indexEqual);
			m_joinAlias  = joinClause.substring(indexEqual+1, end).trim();  //  c
		}
		else            //  f.AD_Column_ID(+) = c.AD_Column_ID  => c / f
		{
			int end = joinClause.indexOf('.', indexEqual);
			if (end == -1)  //  no alias
				end = joinClause.length();
			m_mainAlias  = joinClause.substring(indexEqual+1, end).trim();  //  c
			m_joinAlias = joinClause.substring
				(0, Util.findIndexOf(joinClause, '.','(')).trim();          //  f
		}
		m_condition = Util.replace(joinClause, "(+)", "").trim();
	}   //  evaluate

	/**
	 *  Get origial Join Clause.
	 *  e.g. f.AD_Column_ID = c.AD_Column_ID(+)
	 *  @return Join cluase
	 */
	public String getJoinClause()
	{
		return m_joinClause;
	}   //  getJoinClause

	/**
	 *  Get Main Table Alias
	 *  @return Main Table Alias
	 */
	public String getMainAlias()
	{
		return m_mainAlias;
	}   //  getMainAlias

	/**
	 *  Get Join Table Alias
	 *  @return Join Table Alias
	 */
	public String getJoinAlias()
	{
		return m_joinAlias;
	}   //  getJoinAlias

	/**
	 *  Is Left Aouter Join
	 *  @return true if left outer join
	 */
	public boolean isLeft()
	{
		return m_left;
	}   //  isLeft

	/**
	 *  Get Join condition.
	 *  e.g. f.AD_Column_ID = c.AD_Column_ID
	 *  @return join condition
	 */
	public String getCondition()
	{
		return m_condition;
	}   //  getCondition

	/*************************************************************************/

	/**
	 *  Set Main Table Name.
	 *  If table name equals alias, the alias is set to ""
	 *  @param mainTable
	 */
	public void setMainTable(String mainTable)
	{
		if (mainTable == null || mainTable.length() == 0)
			return;
		m_mainTable = mainTable;
		if (m_mainAlias.equals(mainTable))
			m_mainAlias = "";
	}   //  setMainTable

	/**
	 *  Get Main Table Name
	 *  @return Main Table Name
	 */
	public String getMainTable()
	{
		return m_mainTable;
	}   //  getMainTable

	/**
	 *  Set Main Table Name.
	 *  If table name equals alias, the alias is set to ""
	 *  @param joinTable
	 */
	public void setJoinTable(String joinTable)
	{
		if (joinTable == null || joinTable.length() == 0)
			return;
		m_joinTable = joinTable;
		if (m_joinAlias.equals(joinTable))
			m_joinAlias = "";
	}   //  setJoinTable

	/**
	 *  Get Join Table Name
	 *  @return Join Table Name
	 */
	public String getJoinTable()
	{
		return m_joinTable;
	}   //  getJoinTable

	/*************************************************************************/

	/**
	 *  This Join is a condition of the first Join.
	 *  e.g. tb.AD_User_ID(+)=?  or tb.AD_User_ID(+)='123'
	 *  @param first
	 *  @return true if condition
	 */
	public boolean isConditionOf (Join first)
	{
		if (m_mainTable == null         //  did not find Table from "Alias"
			&& (first.getJoinTable().equals(m_joinTable)        //  same join table
				|| first.getMainAlias().equals(m_joinTable)))   //  same main table
			return true;
		return false;
	}   //  isConditionOf

	/**
	 *  String representation
	 *  @return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("Join[");
		sb.append(m_joinClause)
			.append(" - Main=").append(m_mainTable).append("/").append(m_mainAlias)
			.append(", Join=").append(m_joinTable).append("/").append(m_joinAlias)
			.append(", Left=").append(m_left)
			.append(", Condition=").append(m_condition)
			.append("]");
		return sb.toString();
	}   //  toString

}   //  Join
