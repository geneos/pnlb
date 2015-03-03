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
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.compiere.swing.*;

/**
 *  ID Column Editor (with Select Box).
 *  CheckBox change is only detected, if you move out of the cell.
 *  A ActionListener is added to the check box and the table forced
 *  to notice the change immediately.
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: IDColumnEditor.java,v 1.8 2005/03/11 20:28:30 jjanke Exp $
 */
public class IDColumnEditor extends AbstractCellEditor
	implements TableCellEditor, ActionListener
{
	/**
	 *  Constructor
	 */
	public IDColumnEditor()
	{
		m_check.setMargin(new Insets(0,0,0,0));
		m_check.setHorizontalAlignment(JLabel.CENTER);
		m_check.addActionListener(this);
	}   //  IDColumnEditor

	/** the selection       */
	private JCheckBox   m_check = new CCheckBox();
	/** temporary value     */
	private IDColumn    m_value = null;

	private JTable      m_table;

	/**
	 *  Return Selection Status as IDColumn
	 *  @return value
	 */
	public Object getCellEditorValue()
	{
	//	log.fine( "IDColumnEditor.getCellEditorValue - " + m_check.isSelected());
		if (m_value != null)
			m_value.setSelected (m_check.isSelected());
		return m_value;
	}   //  getCellEditorValue

	/**
	 *  Get visual Component
	 *  @param table
	 *  @param value
	 *  @param isSelected
	 *  @param row
	 *  @param column
	 *  @return Component
	 */
	public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int column)
	{
	//	log.fine( "IDColumnEditor.getTableCellEditorComponent", value);
		m_table = table;
		//  set value
		if (value != null && value instanceof IDColumn)
			m_value = (IDColumn)value;
		else
		{
			m_value = null;
			throw new IllegalArgumentException("ICColumnEditor.getTableCellEditorComponent - value=" + value);
		}
		//  set editor value
		m_check.setSelected(m_value.isSelected());
		return m_check;
	}   //  getTableCellEditorComponent

	/**
	 *  Can we edit it
	 *  @param anEvent
	 *  @return true (cobstant)
	 */
	public boolean isCellEditable (EventObject anEvent)
	{
		return true;
	}   //  isCellEditable

	/**
	 *  Can the cell be selected
	 *  @param anEvent
	 *  @return true (constant)
	 */
	public boolean shouldSelectCell (EventObject anEvent)
	{
		return true;
	}   //  shouldSelectCell

	/**
	 *  Action Listener
	 *  @param e
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (m_table != null)
			m_table.editingStopped(new ChangeEvent(this));
	}   //  actionPerformed

}   //  IDColumnEditor
