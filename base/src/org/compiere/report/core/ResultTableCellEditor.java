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

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *  Cell Editor for Report Result Table
 *
 *  @author Jorg Janke
 *  @version  $Id: ResultTableCellEditor.java,v 1.4 2005/03/11 20:26:10 jjanke Exp $
 */
public class ResultTableCellEditor extends AbstractCellEditor implements TableCellEditor
{
	/**
	 *  Constructor (read only)
	 */
	public ResultTableCellEditor()
	{
	}   //  ResultTableCellEditor

	/**
	 *  Constructor
	 */
	public ResultTableCellEditor(RColumn rc)
	{
		m_rc = rc;
	}   //  ResultTableCellEditor

	/** Report Column           */
	private RColumn m_rc = null;

	/**
	 *  Return Editor
	 */
	public Component getTableCellEditorComponent (JTable table, Object value,
		boolean isSelected, int row, int col)
	{
		if (m_rc == null)
			return null;
		return null;
	}   //  getTableCellEditorComponent

	/**
	 *  Get Value
	 */
	public Object getCellEditorValue()
	{
		if (m_rc == null)
			return null;
		return null;
	}   //  getCellEditorValue

	/**
	 *  Is Cell editable
	 */
	public boolean isCellEditable(EventObject anEvent)
	{
		if (m_rc == null)
			return false;
		return !m_rc.isReadOnly();
	}   //  isCellEditable

	/**
	 *  Should Cell be selected
	 */
	public boolean shouldSelectCell(EventObject anEvent)
	{
		if (m_rc == null)
			return false;
		return !m_rc.isReadOnly();
	}   //  shouldSelectCell

}   //  ResultTableCellEditor
