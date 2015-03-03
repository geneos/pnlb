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

import javax.swing.table.*;
import java.awt.*;
import javax.swing.*;

/**
 *	Renderer for RowID Column
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VRowIDRenderer.java,v 1.5 2005/03/11 20:28:25 jjanke Exp $
 */
public final class VRowIDRenderer implements TableCellRenderer
{
	/**
	 *	Constructor
	 */
	public VRowIDRenderer(boolean enableSelection)
	{
		m_select = enableSelection;
	}	//	VRowIDRenderer

	private boolean		m_select = false;
	private JButton 	m_button = new JButton();
	private JCheckBox	m_check = null;

	/**
	 *	Enable Selection to be displayed
	 */
	public void setEnableSelection(boolean showSelection)
	{
		m_select = showSelection;
	}	//	setEnableSelection

	/**
	 *	Return TableCell Renderer Component
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column)
	{
		if (m_select)
		{
			if (m_check == null)
				m_check = new JCheckBox();
			Object[] data = (Object[])value;
			if (data == null || data[1] == null)
				m_check.setSelected(false);
			else
			{
				Boolean sel = (Boolean)data[1];
				m_check.setSelected(sel.booleanValue());
			}
			return m_check;
		}
		else
			return m_button;
	}	//	getTableCellRenderereComponent

}	//	VRowIDRenderer
