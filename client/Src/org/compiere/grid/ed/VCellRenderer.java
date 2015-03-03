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
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import org.compiere.model.*;
import org.compiere.plaf.CompierePLAF;

import java.util.logging.*;
import org.compiere.util.*;
;

/**
 *  Table Cell Renderer based on DisplayType
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VCellRenderer.java,v 1.15 2005/07/25 18:31:36 jjanke Exp $
 */
public final class VCellRenderer extends DefaultTableCellRenderer
{
	/**
	 *	Constructor for Grid
	 *  @param mField field model
	 */
	public VCellRenderer(MField mField)
	{
		this (mField.getDisplayType());
		m_columnName = mField.getColumnName();
		this.setName(m_columnName);
		m_lookup = mField.getLookup();
		m_password = mField.isEncryptedField();
	}	//	VCellRenderer

	/**
	 *  Constructor for MiniGrid
	 *  @param displayType Display Type
	 */
	public VCellRenderer (int displayType)
	{
		super();
		m_displayType = displayType;
		//	Number
		if (DisplayType.isNumeric(m_displayType))
		{
			m_numberFormat = DisplayType.getNumberFormat(m_displayType);
			setHorizontalAlignment(JLabel.RIGHT);
		}
		//	Date
		else if (DisplayType.isDate(m_displayType))
			m_dateFormat = DisplayType.getDateFormat(m_displayType);
		//
		else if (m_displayType == DisplayType.YesNo)
		{
			m_check = new JCheckBox();
			m_check.setMargin(new Insets(0,0,0,0));
			m_check.setHorizontalAlignment(JLabel.CENTER);
			m_check.setOpaque(true);
		}
	}   //  VCellRenderer

	private int 				m_displayType;
	private String				m_columnName = null;
	private Lookup              m_lookup = null;
	private boolean             m_password = false;
	//
	private SimpleDateFormat 	m_dateFormat = null;
	private DecimalFormat		m_numberFormat = null;
	private JCheckBox           m_check = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VCellRenderer.class);
	
	/**
	 *	Get TableCell RendererComponent.
	 *  @param table table
	 *  @param value value
	 *  @param isSelected selected
	 *  @param hasFocus focus
	 *  @param row row
	 *  @param col col
	 *  @return component
	 */
	public Component getTableCellRendererComponent (JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int col)
	{
	//	log.fine( "VCellRenderer.getTableCellRendererComponent - " + (value == null ? "null" : value.toString()),
	//		"Row=" + row + ", Col=" + col);

		Component c = null;
		if (m_displayType == DisplayType.YesNo)
			c = m_check;
		else
		{
			c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			c.setFont(CompierePLAF.getFont_Field());
		}

		//  Background & Foreground
		Color bg = CompierePLAF.getFieldBackground_Normal();
		Color fg = CompierePLAF.getTextColor_Normal();
		//  Selected is white on blue in Windows
		if (isSelected && !hasFocus)
		{
			bg = table.getSelectionBackground();
			fg = table.getSelectionForeground();
		}
		//  row not selected or field has focus
		else
		{
			//  Foregroundw
			int cCode = 0;
			//  Grid
			if (table instanceof org.compiere.grid.VTable)
				cCode = ((org.compiere.grid.VTable)table).getColorCode (row);
			//  MiniGrid
			else if (table instanceof org.compiere.minigrid.MiniTable)
				cCode = ((org.compiere.minigrid.MiniTable)table).getColorCode (row);
			//
			if (cCode == 0)
				;
			else if (cCode < 0)
				fg = CompierePLAF.getTextColor_Issue();
			else
				fg = CompierePLAF.getTextColor_OK();
			//  Inactive Background
			if (!table.isCellEditable(row, col))
				bg = CompierePLAF.getFieldBackground_Inactive();
		}
		//  Set Color
		c.setBackground(bg);
		c.setForeground(fg);
		//
	//	log.fine( "r=" + row + " c=" + col, // + " - " + c.getClass().getName(),
	//		"sel=" + isSelected + ", focus=" + hasFocus + ", edit=" + table.isCellEditable(row, col));
	//	Log.trace(7, "BG=" + (bg.equals(Color.white) ? "white" : bg.toString()), "FG=" + (fg.equals(Color.black) ? "black" : fg.toString()));

		//  Format it
		setValue(value);
		return c;
	}	//	getTableCellRendererComponent


	/**
	 *	Format Display Value
	 *  @param value (key)value
	 */
	protected void setValue (Object value)
	{
		String retValue = null;
		try
		{
			//  Checkbox
			if (m_displayType == DisplayType.YesNo)
			{
				if (value instanceof Boolean)
					m_check.setSelected(((Boolean)value).booleanValue());
				else
					m_check.setSelected("Y".equals(value));
				return;
			}
			else if (value == null)
				;
			//	Number
			else if (DisplayType.isNumeric(m_displayType))
				retValue = m_numberFormat.format(value);
			//	Date
			else if (DisplayType.isDate(m_displayType))
				retValue = m_dateFormat.format(value);
			//	Row ID
			else if (m_displayType == DisplayType.RowID)
				retValue = "";
			//	Lookup
			else if (m_lookup != null && (DisplayType.isLookup(m_displayType)
					|| m_displayType == DisplayType.Location
					|| m_displayType == DisplayType.Account
					|| m_displayType == DisplayType.Locator
					|| m_displayType == DisplayType.PAttribute ))
				retValue = m_lookup.getDisplay(value);
			//	Button
			else if (m_displayType == DisplayType.Button)
			{
				if ("Record_ID".equals(m_columnName))
					retValue = "#" + value + "#";
				else
					retValue = null;
			}
			//  Password (fixed string)
			else if (m_password)
				retValue = "**********";
			//	other (String ...)
			else
			{
				super.setValue(value);
				return;
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "(" + value + ") " + value.getClass().getName() , e);
			retValue = value.toString();
		}
		super.setValue(retValue);
	}	//	setValue

	/**
	 *  to String
	 *  @return String representation
	 */
	public String toString()
	{
		return "VCellRenderer - DisplayType=" + m_displayType + " - " + m_lookup;
	}   //  toString

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_lookup != null)
			m_lookup.dispose();
		m_lookup = null;
	}	//	dispose

}	//	VCellRenderer
