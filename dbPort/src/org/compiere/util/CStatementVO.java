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

import java.io.*;
import java.util.*;

/**
 *	Compiere Statement Value Object
 *	
 *  @author Jorg Janke
 *  @version $Id: CStatementVO.java,v 1.10 2005/10/08 02:03:38 jjanke Exp $
 */
public class CStatementVO implements Serializable
{
	/**
	 * 	VO Constructor
	 *  @param resultSetType - ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE
	 *  @param resultSetConcurrency - ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 */
	public CStatementVO (int resultSetType, int resultSetConcurrency)
	{
		setResultSetType(resultSetType);
		setResultSetConcurrency(resultSetConcurrency);
	}	//	CStatementVO

	/**
	 * 	VO Constructor
	 *  @param resultSetType - ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE
	 *  @param resultSetConcurrency - ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 * 	@param sql sql
	 */
	public CStatementVO (int resultSetType, int resultSetConcurrency, String sql)
	{
		this (resultSetType, resultSetConcurrency);
		setSql(sql);
	}	//	CStatementVO

	/**	Serialization Info	**/
	static final long serialVersionUID = -3393389471515956399L;
	
	/**	Type			*/
	private int					m_resultSetType;
	/** Concurrency		*/
	private int 				m_resultSetConcurrency;
	/** SQL Statement	*/
	private String 				m_sql;
	/** Parameters		*/
	private ArrayList<Object>	m_parameters = new ArrayList<Object>();

	/**
	 * 	String representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("CStatementVO[");
		sb.append(getSql());
		for (int i = 0; i < m_parameters.size(); i++)
			sb.append("; #").append(i+1).append("=").append(m_parameters.get(i));
		sb.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Set Parameter
	 * 	@param index1 1 based index
	 * 	@param element element
	 */
	public void setParameter (int index1, Object element)
	{
		if (element != null && !(element instanceof Serializable))
			throw new java.lang.RuntimeException("setParameter not Serializable - " + element.getClass().toString());
		int zeroIndex = index1 - 1;
		if (m_parameters.size() == zeroIndex)
		{
			m_parameters.add(element);
		}
		else if (m_parameters.size() < zeroIndex)
		{
			while (m_parameters.size() < zeroIndex)
				m_parameters.add (null);	//	fill with nulls
			m_parameters.add(element);
		}
		else
			m_parameters.set(zeroIndex, element);
	}	//	setParametsr

	/**
	 *	Clear Parameters
	 */
	public void clearParameters()
	{
		m_parameters = new ArrayList<Object>();
	}	//	clearParameters

	/**
	 * 	Get Parameters
	 *	@return arraylist
	 */
	public ArrayList getParameters()
	{
		return m_parameters;
	}	//	getParameters
	
	/**
	 * 	Get Parameter Count
	 *	@return arraylist
	 */
	public int getParameterCount()
	{
		return m_parameters.size();
	}	//	getParameterCount


	/**
	 * 	Get SQL
	 * 	@return sql
	 */
	public String getSql()
	{
		return m_sql;
	}	//	getSql

	/**
	 * 	Set SQL.
	 * 	Replace ROWID with TRIM(ROWID) for remote SQL
	 * 	to convert into String as ROWID is not serialized
	 *	@param sql sql
	 */
	public void setSql(String sql)
	{
		if (sql != null && DB.isRemoteObjects())
		{
			//	Handle RowID in the select part (not where clause)
			int pos = sql.indexOf("ROWID");
			int posTrim = sql.indexOf("TRIM(ROWID)");
			int posWhere = sql.indexOf("WHERE");
			if (pos != -1 && posTrim == -1 && (posWhere == -1 || pos < posWhere))
				m_sql = sql.substring(0, pos) + "TRIM(ROWID)" + sql.substring(pos+5);
			else
				m_sql = sql;
		}
		else
			m_sql = sql;
	}	//	setSql

	/**
	 * 	Get ResultSet Concurrency
	 *	@return rs concurrency
	 */
	public int getResultSetConcurrency()
	{
		return m_resultSetConcurrency;
	}
	/**
	 * 	Get ResultSet Type
	 *	@return rs type
	 */
	public int getResultSetType()
	{
		return m_resultSetType;
	}
	/**
	 * 	Set ResultSet Type
	 *	@param resultSetType type
	 */
	public void setResultSetType(int resultSetType)
	{
		m_resultSetType = resultSetType;
	}
	/**
	 * 	Set ResultSet Concurrency
	 *	@param resultSetConcurrency concurrency
	 */
	public void setResultSetConcurrency(int resultSetConcurrency)
	{
		m_resultSetConcurrency = resultSetConcurrency;
	}

}	//	CStatementVO
