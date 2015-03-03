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

import javax.swing.event.*;
import javax.swing.table.*;

/**
 *  The TableModel for JTable to present RModel information
 *
 *  @author Jorg Janke
 *  @version  $Id: ResultTableModel.java,v 1.7 2005/12/13 00:15:27 jjanke Exp $
 */
class ResultTableModel extends AbstractTableModel
{
	/**
	 *  Create a JTable Model from ReportModel
	 *  @param reportModel
	 */
	public ResultTableModel (RModel reportModel)
	{
		m_model = reportModel;
	}   //  ResultTableModel

	/** The Report Model        */
	private RModel      m_model;

	/**
	 *  Get Row Count
	 *  @return row count
	 */
	public int getRowCount()
	{
		return m_model.getRowCount();
	}   //  getRowCount

	/**
	 *  Get ColumnCount
	 *  @return column count
	 */
	public int getColumnCount()
	{
		return m_model.getColumnCount();
	}   //  getColumnCount

	/**
	 *  Get Column Name
	 *  @param columnIndex
	 *  @return Column Name
	 */
	public String getColumnName(int columnIndex)
	{
		return m_model.getColumnName(columnIndex);
	}   //  getColumnIndex

	/**
	 *  Get Column Class
	 *  @param columnIndex
	 *  @return Column Class
	 */
	public Class<?> getColumnClass(int columnIndex)
	{
		return m_model.getColumnClass(columnIndex);
	}   //  getColumnClass

	/**
	 *  Is Cell Editable
	 *  @param rowIndex
	 *  @param columnIndex
	 *  @return true, if editable
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}   //  isCellEditable

	/**
	 *  Get Value At
	 *  @param row
	 *  @param col
	 *  @return value
	 */
	public Object getValueAt(int row, int col)
	{
		return m_model.getValueAt(row, col);
	}   //  getValueAt

	/**
	 *  Set Value At
	 *  @param aValue
	 *  @param row
	 *  @param col
	 */
	public void setValueAt(Object aValue, int row, int col)
	{
		m_model.setValueAt(aValue, row, col);
		fireTableChanged(new TableModelEvent (this, row, row, col, TableModelEvent.UPDATE));
	}   //  setValueAt

	/**
	 *  Move Row
	 *  @param from index
	 *  @param to index
	 */
	public void moveRow (int from, int to)
	{
		m_model.moveRow (from, to);
	}   //  moveRow

}   //  ResultTableModel
