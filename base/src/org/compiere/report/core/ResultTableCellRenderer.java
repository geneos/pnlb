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

import java.awt.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;

import org.compiere.plaf.CompierePLAF;
import org.compiere.util.*;


/**
 *  Cell Renderer for Report Result Table
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ResultTableCellRenderer.java,v 1.7 2005/03/11 20:26:11 jjanke Exp $
 */
class ResultTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer
{
	/**
	 *  Constructor (extends Label)
	 *  @param rm
	 *  @param rc
	 */
	public ResultTableCellRenderer(RModel rm, RColumn rc)
	{
		m_rm = rm;
		m_rc = rc;
		int dt = m_rc.getDisplayType();
		//  Numbers
		if (DisplayType.isNumeric(dt))
		{
			super.setHorizontalAlignment(JLabel.TRAILING);
			m_nFormat = DisplayType.getNumberFormat(dt);
		}
		//  Dates
		else if (DisplayType.isDate(m_rc.getDisplayType()))
		{
			super.setHorizontalAlignment(JLabel.TRAILING);
			m_dFormat = DisplayType.getDateFormat(dt);
		}
		//
		else if (dt == DisplayType.YesNo)
		{
			m_check = new JCheckBox();
			m_check.setMargin(new Insets(0,0,0,0));
			m_check.setHorizontalAlignment(JLabel.CENTER);
		}
	}   //  ResultTableCellRenderer

	/** Report Column           */
	private RModel              m_rm = null;
	/** Report Column           */
	private RColumn             m_rc = null;
	/** Number Format           */
	private DecimalFormat       m_nFormat = null;
	/** Date Format             */
	private SimpleDateFormat    m_dFormat = null;
	/** Boolean renderer        */
	private JCheckBox           m_check;


	/**
	 *  Return Renderer Component
	 *  @param table
	 *  @param value
	 *  @param isSelected
	 *  @param hasFocus
	 *  @param row
	 *  @param col
	 *  @return renderer component
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int col)
	{
		//  Get Component
		Component c = m_check;
		if (c == null)  //  default JLabel
			c = super.getTableCellRendererComponent(table,value, isSelected,hasFocus, row,col);
		//  Background
		if (m_rm.isCellEditable(row, col))
			c.setBackground(CompierePLAF.getFieldBackground_Normal());
		else
			c.setBackground(CompierePLAF.getFieldBackground_Inactive());
		//
		if (m_rm.isGroupRow(row))
			c.setFont(c.getFont().deriveFont(Font.BOLD));
		//  Value
		setValue (value);
		return c;
	}   //  getTableCellRendererComponent

	/**
	 *  Set Value
	 *  @param value
	 */
	protected void setValue (Object value)
	{
		//  Boolean
		if (m_check != null)
		{
			boolean sel = false;
			if (value != null && ((Boolean)value).booleanValue())
				sel = true;
			m_check.setSelected(sel);
			return;
		}

		//  JLabel
		if (value == null)
			setText("");
		else if (m_nFormat != null)
			try
			{
				setText(m_nFormat.format(value));
			}
			catch (Exception e)
			{
				setText(value.toString());
			}
		else if (m_dFormat != null)
			try
			{
				setText(m_dFormat.format(value));
			}
			catch (Exception e)
			{
				setText(value.toString());
			}
		else
			setText(value.toString());
	}   //  setValue

}   //  ResultTableCellRenderer
