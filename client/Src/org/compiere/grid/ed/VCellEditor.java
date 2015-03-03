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
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import org.compiere.model.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.util.*;


/**
 *  Cell Editor.
 *  <pre>
 *		Sequence of events:
 *			isCellEditable
 *			getTableCellEditor
 *			shouldSelectCell
 *			getCellEditorValue
 *  </pre>
 *  A new Editor is created for editable columns.
 *  @author 	Jorg Janke
 *  @version 	$Id: VCellEditor.java,v 1.16 2005/07/25 18:31:36 jjanke Exp $
 */
public final class VCellEditor extends AbstractCellEditor
	implements TableCellEditor, VetoableChangeListener, ActionListener
{
	/**
	 *	Constructor for Grid
	 *  @param mField
	 */
	public VCellEditor (MField mField)
	{
		super();
		m_mField = mField;
		//  Click
	}	//	VCellEditor

	/** The Field               */
	private MField          m_mField = null;
	/** The Table Editor        */
	private VEditor	        m_editor = null;
	/** Table                   */
	private JTable          m_table = null;
	/** ClickCount              */
	private static int      CLICK_TO_START = 2;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VCellEditor.class);

	/**
	 *  Create Editor
	 */
	private void createEditor()
	{
		m_editor = VEditorFactory.getEditor(m_mField, true);
		m_editor.addVetoableChangeListener(this);
		m_editor.addActionListener(this);
	}   //  createEditor

	/**
	 *	Ask the editor if it can start editing using anEvent.
	 *	If editing can be started this method returns true.
	 *	Previously called: MTable.isCellEditable
	 *  @param anEvent event
	 *  @return true if editable
	 */
	public boolean isCellEditable (EventObject anEvent)
	{
	//	log.config( "VCellEditor.isCellEditable", anEvent);
		if (!m_mField.isEditable (true))	//	row data is in context
			return false;
		//	not enough mouse clicks
		if (anEvent instanceof MouseEvent 
			&& ((MouseEvent)anEvent).getClickCount() < CLICK_TO_START)
			return false;
			
		if (m_editor == null)
			createEditor();
		return true;
	}	//	isCellEditable

	/**
	 *	Sets an initial value for the editor. This will cause the editor to
	 *	stopEditing and lose any partially edited value if the editor is editing
	 *	when this method is called.
	 *	Returns the component that should be added to the client's Component hierarchy.
	 *	Once installed in the client's hierarchy this component
	 *	will then be able to draw and receive user input.
	 *
	 *  @param table
	 *  @param value
	 *  @param isSelected
	 *  @param row
	 *  @param col
	 *  @return component
	 */
	public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int col)
	{
		log.fine("Value=" + value + ", row=" + row + ", col=" + col);
		table.setRowSelectionInterval(row,row);     //  force moving to new row
		if (m_editor == null)
			createEditor();

		m_table = table;

		//	Set Value
		m_editor.setValue(value);

		//	Set Background/Foreground to "normal" (unselected) colors
		m_editor.setBackground(m_mField.isError());
		m_editor.setForeground(CompierePLAF.getTextColor_Normal());

		//  Other UI
		m_editor.setFont(table.getFont());
		m_editor.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		//
		return (Component)m_editor;
	}	//	getTableCellEditorComponent

	/**
	 *	The editing cell should be selected or not
	 *  @param e
	 *  @return true (constant)
	 */
	public boolean shouldSelectCell(EventObject e)
	{
	//	log.fine( "VCellEditor.shouldSelectCell", e.toString());
		return true;
	}	//	shouldSelectCell

	/**
	 *	Returns the value contained in the editor
	 *  @return value
	 */
	public Object getCellEditorValue()
	{
	//	log.fine( "VCellEditor.getCellEditorValue", m_editor.getValue());
		return m_editor.getValue();
	}	//	getCellEditorValue

	/**
	 *  VEditor Change Listener (property name is columnName).
	 *  - indicate change  (for String/Text/..) <br>
	 *  When editing is complete the value is retrieved via getCellEditorValue
	 *  @param e
	 */
	public void vetoableChange(PropertyChangeEvent e)
	{
		if (m_table == null)
			return;
		log.fine(e.getPropertyName() + "=" + e.getNewValue());
		//
		((MTable)m_table.getModel()).setChanged(true);
	}   //  vetoableChange

	/**
	 *  Get Actual Editor.
	 *  Called from GridController to add ActionListener to Button
	 *  @return VEditor
	 */
	public VEditor getEditor()
	{
		return m_editor;
	}   //  getEditor

	/**
	 *  Action Editor - Stop Editor
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		log.fine("Value=" + m_editor.getValue());
//		super.stopCellEditing();	//	causes VLookup.Search Text not to work
	}   //  actionPerformed

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		m_editor = null;
		m_mField = null;
		m_table = null;
	}	//	dispose

}	//	VCellEditor
