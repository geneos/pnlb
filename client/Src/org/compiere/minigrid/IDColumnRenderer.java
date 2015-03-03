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
import javax.swing.*;
import javax.swing.table.*;

/**
 *  ID Column Renderer
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: IDColumnRenderer.java,v 1.6 2005/03/11 20:28:30 jjanke Exp $
 */
public class IDColumnRenderer extends DefaultTableCellRenderer
{
	/**
	 *  Constructor
	 *  @param multiSelection determines layout - button for single, check for multi
	 */
	public IDColumnRenderer(boolean multiSelection)
	{
		super();
		m_multiSelection = multiSelection;
		//          Multi => Check
		if (m_multiSelection)
		{
			m_check = new JCheckBox();
			m_check.setMargin(new Insets(0,0,0,0));
			m_check.setHorizontalAlignment(JLabel.CENTER);
		}
		else    //  Single => Button
		{
			m_button = new JButton();
			m_button.setMargin(new Insets(0,0,0,0));
			m_button.setSize(new Dimension(5,5));
		}
	}   //  IDColumnRenderer

	/** Mult-Selection flag */
	private boolean     m_multiSelection;
	/** The Single-Selection renderer   */
	private JButton     m_button;
	/* The Multi-Selection renderer     */
	private JCheckBox   m_check;

	/**
	 *  Set Value (for multi-selection)
	 *  @param value
	 */
	protected void setValue(Object value)
	{
		if (m_multiSelection)
		{
			boolean sel = false;
			if (value == null)
				;
			else if (value instanceof IDColumn)
				sel = ((IDColumn)value).isSelected();
			else if (value instanceof Boolean)
				sel = ((Boolean)value).booleanValue();
			else
				sel = value.toString().equals("Y");
			//
			m_check.setSelected(sel);
		}
	}   //  setValue

	/**
	 *  Return rendering component
	 *  @param table
	 *  @param value
	 *  @param isSelected
	 *  @param hasFocus
	 *  @param row
	 *  @param column
	 *  @return Component (CheckBox or Button)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		setValue(value);
		if (m_multiSelection)
			return m_check;
		else
			return m_button;
	}   //  setTableCellRenderereComponent

}   //  IDColumnRenderer
