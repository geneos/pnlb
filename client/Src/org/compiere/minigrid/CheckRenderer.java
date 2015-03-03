/******************************************************************************
 * The contents of this file are subject to the  Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                               Compiere ERP&CRM.
 * The Initial Developer of the Original Code is      Jorg Janke.
 * Portions created by Jorg Janke are Copyright       (C) 1999-2005 Jorg Janke.
 * All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.minigrid;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

import org.compiere.plaf.CompierePLAF;


/**
 *  Check Box Renderer based on Boolean values
 *
 *  @author     Jorg Janke
 *  @version    $Id: CheckRenderer.java,v 1.6 2005/01/05 04:24:28 jjanke Exp $
 */
public final class CheckRenderer extends DefaultTableCellRenderer
{
	/**
	 *  Constructor
	 */
	public CheckRenderer()
	{
		super();
		m_check.setMargin(new Insets(0,0,0,0));
		m_check.setHorizontalAlignment(JLabel.CENTER);
		m_check.setOpaque(true);
	}   //  CheckRenderer

	private JCheckBox   m_check = new JCheckBox();

	/**
	 *  Return centered, white Check Box
	 *  @param table
	 *  @param value
	 *  @param isSelected
	 *  @param hasFocus
	 *  @param row
	 *  @param col
	 *  @return Component
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int col)
	{
		//  Background & Foreground
		Color bg = CompierePLAF.getFieldBackground_Normal();
		//  Selected is white on blue in Windows
		if (isSelected && !hasFocus)
			bg = table.getSelectionBackground();
		//  row not selected or field has focus
		else
		{
			//  Inactive Background
			if (!table.isCellEditable(row, col))
				bg = CompierePLAF.getFieldBackground_Inactive();
		}
		//  Set Color
		m_check.setBackground(bg);

		//  Value
		setValue(value);
		return m_check;
	}	//	getTableCellRendererComponent

	/**
	 *  Set Value
	 *  @param value
	 */
	public void setValue(Object value)
	{
		if (value != null && ((Boolean)value).booleanValue())
			m_check.setSelected(true);
		else
			m_check.setSelected(false);
	}   //  setValue

}   //  CheckRenderer
