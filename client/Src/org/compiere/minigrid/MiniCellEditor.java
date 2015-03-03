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
package org.compiere.minigrid;

import java.awt.*;
import java.math.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import org.compiere.grid.ed.*;
import org.compiere.util.*;

/**
 *  MiniTable Cell Editor based on class - Timestamp, BigDecimal
 *
 *  @author     Jorg Janke
 *  @version    $Id: MiniCellEditor.java,v 1.6 2005/03/11 20:28:31 jjanke Exp $
 */
public class MiniCellEditor extends AbstractCellEditor implements TableCellEditor
{
	/**
	 *  Default Constructor
	 *  @param c Class
	 */
	public MiniCellEditor(Class c)
	{
		super();
		//  Date
		if (c == Timestamp.class)
			m_editor = new VDate();
		else if (c == BigDecimal.class)
			m_editor = new VNumber("Amount", false, false, true, DisplayType.Amount, "Amount");
		else if (c == Double.class)
			m_editor = new VNumber("Number", false, false, true, DisplayType.Number, "Number");
		else if (c == Integer.class)
			m_editor = new VNumber("Integer", false, false, true, DisplayType.Integer, "Integer");
		else
			m_editor = new VString();

	}   //  MiniCellEditor

	private VEditor m_editor = null;

	/**
	 *	Sets an initial value for the editor. This will cause the editor to
	 *	stopEditing and lose any partially edited value if the editor is editing
	 *	when this method is called.
	 *	Returns the component that should be added to the client's Component hierarchy.
	 *	Once installed in the client's hierarchy this component
	 *	will then be able to draw and receive user input.
	 *  @param table
	 *  @param value
	 *  @param isSelected
	 *  @param row
	 *  @param column
	 *  @return Component
	 */
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
	//	ADebug.trace(ADebug.l5_DData, "VCellEditor.getTableCellEditorComponent - " + value == null ? "null" : value.toString());

		//	Set Value
		m_editor.setValue(value);

		//	Set UI
		m_editor.setBorder(null);
	//	m_editor.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		m_editor.setFont(table.getFont());
		return (Component)m_editor;
	}	//	getTableCellEditorComponent

	/**
	 *	Returns the value contained in the editor
	 *  @return value
	 */
	public Object getCellEditorValue()
	{
	//	ADebug.trace(ADebug.l5_DData, "VCellEditor.getCellEditorValue - ");

		if (m_editor != null)
			return m_editor.getValue();
		return null;
	}	//	getCellEditorValue

}   //  MiniCellEditor
