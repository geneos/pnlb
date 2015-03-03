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
package org.compiere.grid.ed;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.compiere.util.*;

/**
 * 	No Table Edit/Renderer
 *	
 *  @author Jorg Janke
 *  @version $Id: TableCellNone.java,v 1.1 2006/01/09 19:37:46 jjanke Exp $
 */
public class TableCellNone
	implements TableCellRenderer, TableCellEditor
{
	/**
	 * 	Table Cell None constructor
	 *	@param ColumnName name
	 */
	public TableCellNone (String ColumnName)
	{
		m_ColumnName = ColumnName;
	}	//	TableCellNone
	
	/** Column Name				*/
	private String		m_ColumnName;
	/** Object					*/
	private Object		m_value = null;
	
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (TableCellNone.class);

	/**
	 * 	Get Table Cell Renderer Component
	 *	@param table
	 *	@param value
	 *	@param isSelected
	 *	@param hasFocus
	 *	@param row
	 *	@param col
	 *	@return null
	 */
	public Component getTableCellRendererComponent (JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int col)
	{
		log.finest(m_ColumnName + ": Value=" + value + ", row=" + row + ", col=" + col);
		m_value = value;
		return null;
	}

	/**
	 * 	Get Table Cell Editor Component
	 *	@param table
	 *	@param value
	 *	@param isSelected
	 *	@param row
	 *	@param col
	 *	@return null
	 */
	public Component getTableCellEditorComponent (JTable table, Object value,
		boolean isSelected, int row, int col)
	{
		log.finest(m_ColumnName + ": Value=" + value + ", row=" + row + ", col=" + col);
		m_value = value;
		return null;
	}

	/**
	 * 	Get Cell Editor Value
	 *	@return null
	 */
	public Object getCellEditorValue ()
	{
		log.finest(m_ColumnName + "=" + m_value);
		return m_value;
	}

	/**
	 * 	Is Cell Editable
	 *	@param anEvent
	 *	@return false
	 */
	public boolean isCellEditable (EventObject anEvent)
	{
		log.finest(m_ColumnName);
		return false;
	}

	/**
	 * 	Should Select Cell
	 *	@param anEvent
	 *	@return false
	 */
	public boolean shouldSelectCell (EventObject anEvent)
	{
		log.finest(m_ColumnName);
		return false;
	}

	/**
	 * 	Stop Cell Editing
	 *	@return true
	 */
	public boolean stopCellEditing ()
	{
		return true;
	}

	/**
	 * 	Cancel Cell Editing
	 */
	public void cancelCellEditing ()
	{
	}

	
	public void addCellEditorListener (CellEditorListener l)
	{
	}

	public void removeCellEditorListener (CellEditorListener l)
	{
	}
	
}	//	TableCellNone
