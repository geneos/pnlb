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

import java.io.*;
import java.util.*;
import org.compiere.util.*;

/**
 *  Report Model.
 *  Data is maintained in RModelData
 *
 *  @author Jorg Janke
 *  @version  $Id: RModel.java,v 1.10 2005/09/29 22:01:54 jjanke Exp $
 */
public class RModel implements Serializable
{
	/**
	 *  Constructor. Creates RModelData
	 *  @param TableName
	 */
	public RModel (String TableName)
	{
		m_data = new RModelData (TableName);
	}   //  RModel

	/** Table Alias for SQL     */
	public static final String      TABLE_ALIAS = "zz";
	/** Function: Count         */
	public static final String      FUNCTION_COUNT = "Count";
	/** Function: Sum           */
	public static final String      FUNCTION_SUM = "Sum";

	/** Helper to retrieve Actual Data  */
	private RModelData      m_data = null;
	/** Is Content editable by user     */
	private boolean         m_editable = false;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(RModel.class);
	
	/**************************************************************************
	 *  Get Column Display Name
	 *  @param col
	 *  @return RColumn
	 */
	protected RColumn getRColumn (int col)
	{
		if (col < 0 || col > m_data.cols.size())
			throw new java.lang.IllegalArgumentException("Column invalid");
		return (RColumn)m_data.cols.get(col);
	}   //  getRColumn

	
	/**************************************************************************
	 *  Add Column
	 *  @param rc
	 */
	public void addColumn (RColumn rc)
	{
		m_data.cols.add(rc);
	}   //  addColumn

	/**
	 *  Add Column at Index
	 *  @param rc
	 *  @param index
	 */
	public void addColumn (RColumn rc, int index)
	{
		m_data.cols.add(index, rc);
	}   //  addColumn

	/**
	 *  Add Row
	 */
	public void addRow ()
	{
		m_data.rows.add(new ArrayList<Object>());
		m_data.rowsMeta.add(null);
	}   //  addRow

	/**
	 *  Add Row at Index
	 *  @param index
	 */
	public void addRow (int index)
	{
		m_data.rows.add(index, new ArrayList<Object>());
		m_data.rowsMeta.add(index, null);
	}   //  addRow

	/**
	 *  Add Row
	 *  @param l
	 */
	public void addRow (ArrayList<Object> l)
	{
		m_data.rows.add(l);
		m_data.rowsMeta.add(null);
	}   //  addRow

	/**
	 *  Add Row at Index
	 *  @param l
	 *  @param index
	 */
	public void addRow (ArrayList<Object> l, int index)
	{
		m_data.rows.add(index, l);
		m_data.rowsMeta.add(index, null);
	}   //  addRow

	/**
	 *  Find index for ColumnName
	 *  @param columnName
	 *  @return index or -1 if not found
	 */
	public int getColumnIndex (String columnName)
	{
		if (columnName == null || columnName.length() == 0)
			return -1;
		//
		for (int i = 0; i < m_data.cols.size(); i++)
		{
			RColumn rc = (RColumn)m_data.cols.get(i);
		//	log.fine( "Column " + i + " " + rc.getColSQL() + " ? " + columnName);
			if (rc.getColSQL().startsWith(columnName))
			{
				log.fine( "Column " + i + " " + rc.getColSQL() + " = " + columnName);
				return i;
			}
		}
		return -1;
	}   //  getColumnIndex

	
	/**************************************************************************
	 *  Query
	 *  @param ctx
	 *  @param whereClause
	 *  @param orderClause
	 */
	public void query (Properties ctx, String whereClause, String orderClause)
	{
		m_data.query (ctx, whereClause, orderClause);
	}   //  query

	
	/**************************************************************************
	 *  Set a group column
	 *  @param columnName
	 */
	public void setGroup (String columnName)
	{
		setGroup(getColumnIndex(columnName));
	}   //  setGroup

	/**
	 *  Set a group column -
	 *  if the group value changes, a new row in inserted
	 *  performing the calculations set in setGroupFunction().
	 *  If you set multiple groups, start with the highest level
	 *  (e.g. Country, Region, City)
	 *  The data is assumed to be sorted to result in meaningful groups.
	 *  <pre>
	 *  A AA 1
	 *  A AB 2
	 *  B BA 3
	 *  B BB 4
	 *  after setGroup (0)
	 *  A AA 1
	 *  A AB 2
	 *  A
	 *  B BA 3
	 *  B BB 4
	 *  B
	 *  </pre>
	 *  @param col   The Group BY Column
	 */
	public void setGroup (int col)
	{
		log.config( "RModel.setGroup col=" + col);
		if (col < 0 || col >= m_data.cols.size())
			return;
		Integer ii = new Integer(col);
		if (!m_data.groups.contains(ii))
			m_data.groups.add(ii);
	}   //  setGroup

	/**
	 *  Is Row a Group Row
	 *  @param row index
	 *  @return true if row is a group row
	 */
	public boolean isGroupRow (int row)
	{
		return m_data.isGroupRow(row);
	}   // isGroupRow

	/**
	 *  Set Group Function
	 *  @param columnName
	 *  @param function
	 */
	public void setFunction (String columnName, String function)
	{
		setFunction(getColumnIndex(columnName), function);
	}   //  setFunction

	/**
	 *  Set Group Function -
	 *  for the column, set a function like FUNCTION_SUM, FUNCTION_COUNT, ...
	 *  @param col   The column on which the function is performed
	 *  @param function  The function
	 */
	public void setFunction (int col, String function)
	{
		log.config( "RModel.setFunction col=" + col + " - " + function);
		if (col < 0 || col >= m_data.cols.size())
			return;
		m_data.functions.put(new Integer(col), function);
	}   //  setFunction

	/*************************************************************************/
	//  TableModel interface

	/**
	 *  Return Total mumber of rows
	 *  @return row count
	 */
	public int getRowCount()
	{
		return m_data.rows.size();
	}   //  getRowCount

	/**
	 *  Get total number of columns
	 *  @return column count
	 */
	public int getColumnCount()
	{
		return m_data.cols.size();
	}   //  getColumnCount

	/**
	 *  Get Column Display Name
	 *  @param col index
	 *  @return ColumnName
	 */
	public String getColumnName (int col)
	{
		if (col < 0 || col > m_data.cols.size())
			throw new java.lang.IllegalArgumentException("Column invalid");
		RColumn rc = (RColumn)m_data.cols.get(col);
		if (rc != null)
			return rc.getColHeader();
		return null;
	}   //  getColumnName

	/**
	 *  Get Column Class
	 *  @param col index
	 *  @return Column Class
	 */
	public Class getColumnClass (int col)
	{
		if (col < 0 || col > m_data.cols.size())
			throw new java.lang.IllegalArgumentException("Column invalid");
		RColumn rc = (RColumn)m_data.cols.get(col);
		if (rc != null)
			return rc.getColClass();
		return null;
	}   //  getColumnC;ass

	/**
	 *  Is Cell Editable
	 *  @param rowIndex
	 *  @param columnIndex
	 *  @return true, if editable
	 */
	public boolean isCellEditable (int rowIndex, int columnIndex)
	{
		return m_editable;
	}   //  isCellEditable

	/**
	 *  Get Value At
	 *  @param row
	 *  @param col
	 *  @return value
	 */
	public Object getValueAt(int row, int col)
	{
		//  invalid row
		if (row < 0 || row >= m_data.rows.size())
			return null;
	//		throw new java.lang.IllegalArgumentException("Row invalid");
		if (col < 0 || col >= m_data.cols.size())
			return null;
	//		throw new java.lang.IllegalArgumentException("Column invalid");
		//
		ArrayList myRow = (ArrayList)m_data.rows.get(row);
		//  invalid column
		if (myRow == null || col >= myRow.size())
			return null;
		//  setValue
		return myRow.get(col);
	}   //  getValueAt

	/**
	 *  Set Value At
	 *  @param aValue
	 *  @param row
	 *  @param col
	 *  @throws IllegalArgumentException if row/column is invalid or cell is read only
	 */
	public void setValueAt(Object aValue, int row, int col)
	{
		//  invalid row
		if (row < 0 || row >= m_data.rows.size())
			throw new IllegalArgumentException("Row invalid");
		if (col < 0 || col >= m_data.cols.size())
			throw new IllegalArgumentException("Column invalid");
		if (!isCellEditable(row, col))
			throw new IllegalArgumentException("Cell is read only");
		//
		ArrayList<Object> myRow = m_data.rows.get(row);
		//  invalid row
		if (myRow == null)
			throw new java.lang.IllegalArgumentException("Row not initialized");
		//  not enough columns - add nulls
		if (col >= myRow.size())
			while (myRow.size() < m_data.cols.size())
				myRow.add(null);
		//  setValue
		myRow.set(col, aValue);
	}   //  setValueAt

	/**
	 *  Move Row
	 *  @param from index
	 *  @param to index
	 */
	public void moveRow (int from, int to)
	{
		m_data.moveRow(from,to);
	}   //  moveRow


	/*************************************************************************/

}   //  RModel
