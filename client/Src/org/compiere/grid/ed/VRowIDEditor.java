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
import javax.swing.table.*;
import org.compiere.util.*;

/**
 *  RowID Cell Editor providing Selection
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VRowIDEditor.java,v 1.6 2005/03/11 20:28:26 jjanke Exp $
 */
public class VRowIDEditor extends AbstractCellEditor implements TableCellEditor
{
	/**
	 *	Constructor
	 */
	public VRowIDEditor(boolean select)
	{
		super();
		m_select = select;
		m_cb.setMargin(new Insets(0,0,0,0));
		m_cb.setHorizontalAlignment(JLabel.CENTER);
	}	//	VRowIDEditor

	private JCheckBox 	m_cb = new JCheckBox();
	private Object[] 	m_rid;
	private boolean		m_select;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VRowIDEditor.class);

	/**
	 *	Enable Selection to be displayed
	 */
	public void setEnableSelection(boolean showSelection)
	{
		m_select = showSelection;
	}	//	setEnableSelection

	/**
	 *	Ask the editor if it can start editing using anEvent.
	 *	This method is intended for the use of client to avoid the cost of
	 *	setting up and installing the editor component if editing is not possible.
	 *	If editing can be started this method returns true
	 */
	public boolean isCellEditable(EventObject anEvent)
	{
		return m_select;
	}	//	isCellEditable

	/**
	 *	Sets an initial value for the editor. This will cause the editor to
	 *	stopEditing and lose any partially edited value if the editor is editing
	 *	when this method is called.
	 *	Returns the component that should be added to the client's Component hierarchy.
	 *	Once installed in the client's hierarchy this component
	 *	will then be able to draw and receive user input.
	 */
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		m_rid = (Object[])value;
		if (m_rid == null || m_rid[1] == null)
			m_cb.setSelected(false);
		else
		{
			Boolean sel = (Boolean)m_rid[1];
			m_cb.setSelected(sel.booleanValue());
		}
		return m_cb;
	}	//	getTableCellEditorComponent

	/**
	 *	The editing cell should be selected or not
	 */
	public boolean shouldSelectCell(EventObject anEvent)
	{
		return m_select;
	}	//	shouldSelectCell

	/**
	 *	Returns the value contained in the editor
	 */
	public Object getCellEditorValue()
	{
		log.fine( "VRowIDEditor.getCellEditorValue - " + m_cb.isSelected());
		if (m_rid == null)
			return null;
		m_rid[1] = new Boolean (m_cb.isSelected());
		return m_rid;
	}	//	getCellEditorValue

}	//	VRowIDEditor
