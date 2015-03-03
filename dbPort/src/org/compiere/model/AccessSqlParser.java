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

import java.util.*;

import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Parse FROM in SQL WHERE clause
 *	
 *  @author Jorg Janke
 *  @version $Id: AccessSqlParser.java,v 1.13 2006/01/03 02:40:05 jjanke Exp $
 */
public class AccessSqlParser
{
	/**
	 *	Base Constructor.
	 *	You need to set the SQL and start the parsing manually.
	 */
	public AccessSqlParser ()
	{
	}	//	AccessSqlParser

	/**
	 *	Full Constructor 
	 *	@param sql sql command
	 */
	public AccessSqlParser (String sql)
	{
		setSql(sql);
	}	//	AccessSqlParser

	/**	FROM String			*/
	private static final String		FROM = " FROM ";
	private static final int		FROM_LENGTH = FROM.length();
	private static final String		WHERE = " WHERE ";
	private static final String		ON = " ON ";

	/**	Logger				*/
	private CLogger	log = CLogger.getCLogger(getClass());
	/**	Original SQL			*/
	private String		m_sqlOriginal;
	/**	SQL Selects			*/
	private String[]	m_sql;
	/**	List of Arrays		*/
	private ArrayList<TableInfo[]>	m_tableInfo = new ArrayList<TableInfo[]>(); 

	/**
	 * 	Set Sql and parse it
	 *	@param sql sql
	 */
	public void setSql (String sql)
	{
		if (sql == null)
			throw new IllegalArgumentException("No SQL");
		m_sqlOriginal = sql;
		int index = m_sqlOriginal.indexOf("\nFROM ");
		if (index != -1)
			m_sqlOriginal = m_sqlOriginal.replace("\nFROM ", FROM);
		index = m_sqlOriginal.indexOf("\nWHERE ");
		if (index != -1)
			m_sqlOriginal = m_sqlOriginal.replace("\nWHERE ", WHERE);
		//
		parse();
	}	//	setSQL

	/**
	 * 	Get (original) Sql
	 *	@return sql
	 */
	public String getSql()
	{
		return m_sqlOriginal;
	}	//	getSql

	/**
	 * 	Parse Original SQL.
	 * 	Called from setSql or Constructor.
	 */
	public boolean parse()
	{
		if (m_sqlOriginal == null || m_sqlOriginal.length() == 0)
			throw new IllegalArgumentException("No SQL");
		//
	//	if (CLogMgt.isLevelFinest())
	//		log.fine(m_sqlOriginal);
		getSelectStatements();
		//	analyse each select	
		for (int i = 0; i < m_sql.length; i++)
		{	
			TableInfo[] info = getTableInfo(m_sql[i].trim());
			m_tableInfo.add(info);
		}
		//
		if (CLogMgt.isLevelFinest())
			log.fine(toString());
		return m_tableInfo.size() > 0;
	}	//	parse

	/**
	 * 	Parses 	m_sqlOriginal and creates Array of m_sql statements
	 */
	private void getSelectStatements()
	{
		String[] sqlIn = new String[] {m_sqlOriginal};
		String[] sqlOut = null;
		try
		{
			sqlOut = getSubSQL (sqlIn);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, m_sqlOriginal, e);
			throw new IllegalArgumentException(m_sqlOriginal);
		}
		//	a sub-query was found
		while (sqlIn.length != sqlOut.length)
		{
			sqlIn = sqlOut;
			try
			{
				sqlOut = getSubSQL (sqlIn);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, m_sqlOriginal, e);
				throw new IllegalArgumentException(sqlOut.length + ": "+ m_sqlOriginal);
			}
		}
		m_sql = sqlOut;
		/** List & check **
		for (int i = 0; i < m_sql.length; i++)
		{
			if (m_sql[i].indexOf("SELECT ",2) != -1)
				log.log(Level.SEVERE, "#" + i + " Has embedded SQL - " + m_sql[i]);
			else
				log.fine("#" + i + " - " + m_sql[i]);
		}
		/** **/
	}	//	getSelectStatements

	/**
	 * 	Get Sub SQL of sql statements
	 *	@param sqlIn array of input sql
	 *	@return array of resulting sql
	 */
	private String[] getSubSQL (String[] sqlIn)
	{
		ArrayList<String> list = new ArrayList<String>();
		for (int sqlIndex = 0; sqlIndex < sqlIn.length; sqlIndex++)
		{
			String sql = sqlIn[sqlIndex];
			int index = sql.indexOf("(SELECT ", 7);
			while (index != -1)
			{
				int endIndex = index+1;
				int parenthesisLevel = 0;
				//	search for the end of the sql
				while (endIndex++ < sql.length())
				{
					char c = sql.charAt(endIndex); 
					if (c == ')')
					{
						if (parenthesisLevel == 0)
							break;
						else
							parenthesisLevel--;
					}
					else if (c == '(')
						parenthesisLevel++;
				}
				String subSQL = sql.substring(index, endIndex+1);
				list.add(subSQL);
				//	remove inner SQL (##)
				sql = sql.substring(0,index+1) + "##" 
					+ sql.substring(endIndex);
				index = sql.indexOf("(SELECT ", 7);
			}			
			list.add(sql);	//	last SQL
		}
		String[] retValue = new String[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getSubSQL

	/**
	 * 	Get Table Info for SQL
	 *	@param sql sql
	 *	@return array of table info for sql
	 */
	private TableInfo[] getTableInfo (String sql)
	{
		ArrayList<TableInfo> list = new ArrayList<TableInfo>();
		//	remove ()
		if (sql.startsWith("(") && sql.endsWith(")"))
			sql = sql.substring(1,sql.length()-1);
			
		int fromIndex = sql.indexOf(FROM);
		if (fromIndex != sql.lastIndexOf(FROM))
			log.log(Level.SEVERE, "getTableInfo - More than one FROM clause - " + sql);
		while (fromIndex != -1)
		{
			String from = sql.substring(fromIndex+FROM_LENGTH);
			int index = from.lastIndexOf(WHERE);	//	end at where
			if (index != -1)
				from = from.substring(0, index);
			from = Util.replace(from, " AS ", " ");
			from = Util.replace(from, " as ", " ");
			from = Util.replace(from, " INNER JOIN ", ", ");
			from = Util.replace(from, " LEFT OUTER JOIN ", ", ");
			from = Util.replace(from, " RIGHT OUTER JOIN ", ", ");
			from = Util.replace(from, " FULL JOIN ", ", ");
			//	Remove ON clause - assumes that there is no IN () in the clause
			index = from.indexOf(ON);
			while (index != -1)
			{
				int indexClose = from.indexOf(')');		//	does not catch "IN (1,2)" in ON
				int indexNextOn = from.indexOf(ON, index+4);
				if (indexNextOn != -1)
					indexClose = from.lastIndexOf(')', indexNextOn);
				if (indexClose != -1)
					from = from.substring(0, index) + from.substring(indexClose+1);
				else
				{
					log.log(Level.SEVERE, "parse - could not remove ON " + from);
					break;
				}			
				index = from.indexOf(ON);
			}
			
//			log.fine("getTableInfo - " + from);
			StringTokenizer tableST = new StringTokenizer (from, ",");
			while (tableST.hasMoreTokens())
			{
				String tableString = tableST.nextToken().trim();
				StringTokenizer synST = new StringTokenizer (tableString, " ");
				TableInfo tableInfo = null;
				if (synST.countTokens() > 1)
					tableInfo = new TableInfo(synST.nextToken(), synST.nextToken());
				else
					tableInfo = new TableInfo(tableString);
//				log.fine("getTableInfo -- " + tableInfo);
				list.add(tableInfo);
			}
			//
			sql = sql.substring(0, fromIndex);
			fromIndex = sql.lastIndexOf(FROM);
		}
		TableInfo[] retValue = new TableInfo[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getTableInfo


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("AccessSqlParser[");
		if (m_tableInfo == null)
			sb.append(m_sqlOriginal);
		else
		{
			for (int i = 0; i < m_tableInfo.size(); i++)
			{
				if (i > 0)
					sb.append("|");
				TableInfo[] info = (TableInfo[])m_tableInfo.get(i);
				for (int ii = 0; ii < info.length; ii++)
				{
					if (ii > 0)
						sb.append(",");
					sb.append(info[ii].toString());
				}
			}
		}
		sb.append("|").append(getMainSqlIndex());
		sb.append("]");
		return sb.toString();
	} //	toString

	/**
	 * 	Get Table Info
	 *	@return table info
	 */
	public TableInfo[] getTableInfo (int index)
	{
		if (index < 0 || index > m_tableInfo.size())
			return null;
		TableInfo[] retValue = (TableInfo[])m_tableInfo.get(index);
		return retValue;
	}	//	getTableInfo

	/**
	 * 	Get Sql Statements
	 *	@return index index of query
	 */
	public String getSqlStatement (int index)
	{
		if (index < 0 || index > m_sql.length)
			return null;
		return m_sql[index];
	}	//	getSqlStatement

	/**
	 * 	Get No of SQL Statements
	 *	@return FROM clause count
	 */
	public int getNoSqlStatments()
	{
		if (m_sql == null)
			return 0;
		return m_sql.length;
	}	//	getNoSqlStatments

	/**
	 * 	Get index of main Statements
	 *	@return index of main statement or -1 if not found
	 */
	public int getMainSqlIndex()
	{
		if (m_sql == null)
			return -1;
		else if (m_sql.length == 1)
			return 0;
		for (int i = m_sql.length-1; i >= 0; i--)
		{
			if (m_sql[i].charAt(0) != '(')
				return i;
		}
		return -1;
	}	//	getMainSqlIndex

	/**
	 * 	Get main sql Statement
	 *	@return main statement
	 */
	public String getMainSql()
	{
		if (m_sql == null)
			return m_sqlOriginal;
			
		if (m_sql.length == 1)
			return m_sql[0];
		for (int i = m_sql.length-1; i >= 0; i--)
		{
			if (m_sql[i].charAt(0) != '(')
				return m_sql[i];
		}
		return "";
	}	//	getMainSql

	/**
	 * 	Table Info VO
	 */
	public class TableInfo
	{
		/**
		 * 	Constructor
		 *	@param tableName table
		 *	@param synonym synonym
		 */
		public TableInfo (String tableName, String synonym)
		{
			m_tableName = tableName;
			m_synonym = synonym;
		}	//	TableInfo
		
		/**
		 * 	Short Constuctor - no syn
		 *	@param tableName table
		 */
		public TableInfo (String tableName)
		{
			this (tableName, null);
		}	//	TableInfo
		
		private String m_tableName;
		private String m_synonym;
		
		/**
		 * 	Get Table Synonym
		 *	@return synonym
		 */
		public String getSynonym()
		{
			if (m_synonym == null)
				return "";
			return m_synonym;
		}	//	getSynonym

		/**
		 * 	Get TableName
		 *	@return table name
		 */
		public String getTableName()
		{
			return m_tableName;
		}	//	getTableName

		/**
		 * 	String Representation
		 *	@return info
		 */
		public String toString()
		{
			StringBuffer sb = new StringBuffer(m_tableName);
			if (getSynonym().length() > 0)
				sb.append("=").append(m_synonym);
			return sb.toString();
		} 	//	toString

	}	//	TableInfo

}	//	AccessSqlParser
