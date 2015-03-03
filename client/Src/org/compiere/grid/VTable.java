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
package org.compiere.grid;

import java.awt.*;
import java.beans.*;
import javax.swing.table.*;
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Table Grid based on CTable.
 * 	Used in GridController
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VTable.java,v 1.14 2005/12/09 05:17:53 jjanke Exp $
 */
public final class VTable extends CTable 
	implements PropertyChangeListener
{
	/**
	 *	Default Constructor
	 */
	public VTable()
	{
		super();
		setAutoscrolls(true);
	}	//	VTable

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VTable.class);
	
	/**
	 *  Property Change Listener for CurrentRow.
	 *  - Selects the current row if not already selected
	 *  - Required when navigating via Buttons
	 *  @param evt event
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
	//	log.config(evt);
		if (evt.getPropertyName().equals(MTab.PROPERTY))
		{
			int row = ((Integer)evt.getNewValue()).intValue();
			int selRow = getSelectedRow();
			if (row == selRow)
				return;
			log.config(MTab.PROPERTY + "=" + row + " from " + selRow);
			setRowSelectionInterval(row,row);
		    Rectangle cellRect = getCellRect(row, 1, false);
		    if (cellRect != null)
		    	scrollRectToVisible(cellRect);
			log.config(MTab.PROPERTY + "=" + row + " from " + selRow);
		}
	}   //  propertyChange

	/**
	 *	Get ColorCode for Row.
	 *  <pre>
	 *	If numerical value in compare column is
	 *		negative = -1,
	 *      positive = 1,
	 *      otherwise = 0
	 *  </pre>
	 *  @param row row
	 *  @return color code
	 */
	public int getColorCode (int row)
	{
		return ((MTable)getModel()).getColorCode(row);
	}   //  getColorCode

	/**
	 *  Sort Table
	 *  @param modelColumnIndex model column sort index
	 */
	protected void sort (int modelColumnIndex)
	{
		int rows = getRowCount();
		if (rows == 0)
			return;
		//
		TableModel model = getModel();
		if (!(model instanceof MTable))
		{
			super.sort(modelColumnIndex);
			return;
		}

		//  other sort column
		if (modelColumnIndex != p_lastSortIndex)
			p_asc = true;
		else
			p_asc = !p_asc;

		p_lastSortIndex = modelColumnIndex;
		//
		log.config("#" + modelColumnIndex
			+ " - rows=" + rows + ", asc=" + p_asc);

		((MTable)model).sort(modelColumnIndex, p_asc);
		//  table model fires "Sorted" DataStatus event which causes MTab to position to row 0
	}   //  sort

	/**
	 *  Transfer focus explicitly to editor due to editors with multiple components
	 *
	 *  @param row row
	 *  @param column column
	 *  @param e event
	 *  @return true if cell is editing
	 */
	public boolean editCellAt (int row, int column, java.util.EventObject e)
	{
		if (!super.editCellAt(row, column, e))
			return false;
	//	log.fine( "VTable.editCellAt", "r=" + row + ", c=" + column);

		Object ed = getCellEditor();
		if (ed instanceof VEditor)
			((Component)ed).requestFocus();
		else if (ed instanceof VCellEditor)
		{
			ed = ((VCellEditor)ed).getEditor();
			((Component)ed).requestFocus();
		}
		return true;
	}   //  editCellAt

	/**
	 *  toString
	 *  @return String representation
	 */
	public String toString()
	{
		return new StringBuffer("VTable[").append(getModel()).append("]").toString();
	}   //  toString


	/**
	 * 	Remove All - dispose
	 *	@see java.awt.Container#removeAll()
	 */
	public void removeAll()
	{
		super.removeAll();
		/**	Test
		TableColumnModel tcm = getColumnModel();
		for (int i = 0; i < tcm.getColumnCount(); i++)
		{
			TableColumn tc = tcm.getColumn(i);
			TableCellEditor tce = tc.getCellEditor();
			if (tce != null)
			{
				if (tce instanceof VCellEditor)
					((VCellEditor)tce).dispose();
				tc.setCellEditor(null);
			}
			TableCellRenderer tcr = tc.getCellRenderer();
			if (tcr != null)
			{
				if (tcr instanceof VCellRenderer)
					((VCellRenderer)tcr).dispose();
				tc.setCellRenderer(null);
			}
		}
		**/
	}	//	removeAll
}	//	VTable
