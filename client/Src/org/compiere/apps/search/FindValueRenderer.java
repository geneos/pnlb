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
package org.compiere.apps.search;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import org.compiere.model.*;
import org.compiere.plaf.CompierePLAF;

import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Renderer for Find 'Value' Column.
 *	The value is how it would be used in a query, i.e. with '' for strings
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: FindValueRenderer.java,v 1.10 2005/11/14 02:10:58 jjanke Exp $
 */
public final class FindValueRenderer extends DefaultTableCellRenderer
{
	/**
	 *	Constructor
	 *  @param find find
	 *  @param valueTo true if it is the "to" value column
	 */
	public FindValueRenderer (Find find, boolean valueTo)
	{
		super();
		m_find = find;
		m_valueToColumn = valueTo;
	}	//	FindValueRenderer

	/** Find Window             */
	private Find			m_find;
	/** Value 2(to)             */
	private boolean         m_valueToColumn;
	/**	Between selected		*/
	private boolean			m_between = false;

	/** Current Row             */
	private volatile String	m_columnName = null;
	/** CheckBox                */
	private JCheckBox       m_check = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(FindValueRenderer.class);

	/**
	 * 	Get Check Box
	 *	@return check box
	 */
	private JCheckBox getCheck()
	{
		if (m_check == null)
		{
			m_check = new JCheckBox();
			m_check.setMargin(new Insets(0,0,0,0));
			m_check.setHorizontalAlignment(JLabel.CENTER);
		}
		return m_check;
	}	//	getCheck


	/*************************************************************************
	 *	Get TableCell RendererComponent
	 *  @param table table
	 *  @param value value
	 *  @param isSelected selected
	 *  @param hasFocus focus
	 *  @param row row
	 *  @param col col
	 *  @return renderer component (Label or CheckBox)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int col)
	{
	//	log.config( "FindValueRenderer.getTableCellRendererComponent", "r=" + row + ", c=" + col );
		//	Column
		m_columnName = null;
		Object column = table.getModel().getValueAt(row, Find.INDEX_COLUMNNAME);
		if (column != null)
			m_columnName = ((ValueNamePair)column).getValue();

		//	Between - enables valueToColumn
		m_between = false;
		Object betweenValue = table.getModel().getValueAt(row, Find.INDEX_OPERATOR);
		if (m_valueToColumn && betweenValue != null 
			&& betweenValue.equals(MQuery.OPERATORS[MQuery.BETWEEN_INDEX]))
			m_between = true;
		boolean enabled = !m_valueToColumn || (m_valueToColumn && m_between); 

		//	set Background
		if (enabled)
			setBackground(CompierePLAF.getFieldBackground_Normal());
		else
			setBackground(CompierePLAF.getFieldBackground_Inactive());

	//	log.config( "FindValueRenderer.getTableCellRendererComponent - (" + value + ") - Enabled=" + enabled);

		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		if (value == null || (m_valueToColumn && !m_between))
			return c;
		//
		MField field = getMField();
		if (field != null && field.getDisplayType() == DisplayType.YesNo)
		{
			JCheckBox cb = getCheck();
			if (value instanceof Boolean)
				cb.setSelected(((Boolean)value).booleanValue());
			else
				cb.setSelected(value.toString().indexOf("Y") != -1);
			return cb;
		}
		return c;
	}	//	getTableCellRendererComponent

	
	/**************************************************************************
	 *	Format Display Value
	 *  @param value value
	 */
	protected void setValue(Object value)
	{
		boolean enabled = !m_valueToColumn || (m_valueToColumn && m_between); 
	//	Log.trace (Log.l4_Data, "FindValueRenderer.setValue (" + value + ") - Enabled=" + enabled);
		if (value == null || !enabled)
		{
			super.setValue(null);
			return;
		}

		String retValue = null;

		//	Strip ' '
		if (value != null)
		{
			String str = value.toString();
			if (str.startsWith("'") && str.endsWith("'"))
			{
				str = str.substring(1, str.length()-1);
				value = str;
			}
		}

		int displayType = 0;
		MField field = getMField();
		if (field != null)
			displayType = field.getDisplayType();
		else
			log.log(Level.SEVERE, "FindValueRenderer.setValue (" + value + ") ColumnName=" + m_columnName + " No Target Column");

		setHorizontalAlignment(JLabel.LEFT);
		//	Number
		if (DisplayType.isNumeric(displayType))
		{
			setHorizontalAlignment(JLabel.RIGHT);
			retValue = DisplayType.getNumberFormat(displayType).format(value);
		}
		//	Date
		else if (DisplayType.isDate(displayType))
		{
			if (value instanceof Date)
			{
				retValue = DisplayType.getDateFormat(displayType).format(value);
				setHorizontalAlignment(JLabel.RIGHT);
			}
			else if (value instanceof String)	//	JDBC format
			{
				try
				{
					java.util.Date date = DisplayType.getDateFormat_JDBC().parse((String)value);
					retValue = DisplayType.getDateFormat(displayType).format(date);
					setHorizontalAlignment(JLabel.RIGHT);
				}
				catch (Exception e)
				{
				//	log.log(Level.SEVERE, "FindValueRenderer.setValue", e);
					retValue = value.toString();
				}
			}
			else
				retValue = value.toString();
		}
		//	Row ID
		else if (displayType == DisplayType.RowID)
			retValue = "";
		//	Lookup
		else if (DisplayType.isLookup(displayType) && field != null)
		{
			Lookup lookup = field.getLookup();
			if (lookup != null)
				retValue = lookup.getDisplay(value);
		}
		//	other
		else
		{
			super.setValue(value);
			return;
		}
	//	log.config( "FindValueRenderer.setValue (" + retValue + ") - DT=" + displayType);
		super.setValue(retValue);
	}	//	setValue

	/**
	 * 	Get MField
	 * 	@return field
	 */
	private MField getMField ()
	{
		return m_find.getTargetMField(m_columnName);
	}	//	getMField


}	//	FindValueRenderer
