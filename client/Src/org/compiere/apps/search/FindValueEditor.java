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
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Cell editor for Find Value field.
 *  Editor depends on Column setting
 *	Has to save entries how they are used in the query, i.e. '' for strings
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: FindValueEditor.java,v 1.10 2005/09/03 01:57:16 jjanke Exp $
 */
public final class FindValueEditor extends AbstractCellEditor implements TableCellEditor
{
	/**
	 *	Constructor
	 *  @param find find
	 *  @param valueTo true if it is the "to" value column
	 */
	public FindValueEditor (Find find, boolean valueTo)
	{
		super();
		m_find = find;
		m_valueToColumn = valueTo;
	}	//	FindValueEditor

	/** Find Window             */
	private Find 			m_find;
	/** Value 2(to)             */
	private boolean         m_valueToColumn;
	/**	Between selected		*/
	private boolean			m_between = false;
	/**	Editor					*/
	private VEditor			m_editor = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(FindValueEditor.class);
	
	/**
	 *	Get Value
	 *	Need to convert to String
	 *  @return current value
	 */
	public Object getCellEditorValue()
	{
		if (m_editor == null)
			return null;
		Object obj = m_editor.getValue();		//	returns Integer, BidDecimal, String
		log.config("Obj=" + obj);
		return obj;
		/**
		if (obj == null)
			return null;
		//
		String retValue = obj.toString();
		log.config( "FindValueEditor.getCellEditorValue");
		return retValue;
		**/
	}	//	getCellEditorValue

	/**
	 *	Get Editor
	 *
	 *  @param table Table
	 *  @param value Value
	 *  @param isSelected cell is selected
	 *  @param row row
	 *  @param col column
	 *  @return Editor component
	 */
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col)
	{
	//	log.config( "FindValueEditor.getTableCellEditorComponent", "r=" + row + ", c=" + col);
		//	Between - enables valueToColumn
		m_between = false;
		Object betweenValue = table.getModel().getValueAt(row, Find.INDEX_OPERATOR);
		if (m_valueToColumn &&  betweenValue != null 
			&& betweenValue.equals(MQuery.OPERATORS[MQuery.BETWEEN_INDEX]))
			m_between = true;

		boolean enabled = !m_valueToColumn || (m_valueToColumn && m_between); 
		log.config("(" + value + ") - Enabled=" + enabled);

		String columnName = null;
		Object column = table.getModel().getValueAt(row, Find.INDEX_COLUMNNAME);
		if (column != null)
			columnName = ((ValueNamePair)column).getValue();

		//  Create Editor
		MField field = m_find.getTargetMField(columnName);
	//	log.fine( "Field=" + field.toStringX());
		if (field.isKey())
			m_editor = new VNumber(columnName, false, false, true, DisplayType.Integer, columnName);
		else
			m_editor = VEditorFactory.getEditor(field, true);
		if (m_editor == null)
			m_editor = new VString();

		m_editor.setValue(value);
		m_editor.setReadWrite(enabled);
		m_editor.setBorder(null);
		//
		return (Component)m_editor;
	}   //	getTableCellEditorComponent

	/**
	 *  Cell Editable.
	 * 	Called before getTableCellEditorComponent
	 *  @param e event
	 *  @return true if editable
	 */
	public boolean isCellEditable (EventObject e)
	{
	//	log.config( "FindValueEditor.isCellEditable");
		return true;
	}   //  isCellEditable

	/**
	 *  Cell Selectable.
	 * 	Called after getTableCellEditorComponent
	 *  @param e event
	 *  @return true if selectable
	 */
	public boolean shouldSelectCell (EventObject e) 
	{
		boolean retValue = !m_valueToColumn || (m_valueToColumn && m_between); 
	//	log.config( "FindValueEditor.shouldSelectCell - " + retValue);
		return retValue; 
	}	//	shouldSelectCell

}	//	FindValueEditor
