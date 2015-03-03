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
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.compiere.util.*;

/**
 *  The Table to present RModel information
 *
 *  @author Jorg Janke
 *  @version  $Id: ResultTable.java,v 1.11 2005/11/14 02:10:53 jjanke Exp $
 */
public class ResultTable extends JTable implements MouseListener
{
	/**
	 *  Constructor
	 */
	public ResultTable()
	{
		super();
		setCellSelectionEnabled(false);
		setColumnSelectionAllowed(false);
		setRowSelectionAllowed(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		//  Default Editor
		ResultTableCellEditor rtce = new ResultTableCellEditor();
		setCellEditor(rtce);

		//  Mouse Listener
		addMouseListener(this);
		getTableHeader().addMouseListener(this);
	}   //  ResultTable

	/** Last model index sorted */
	private int         m_lastSortIndex = -1;
	/** Sort direction          */
	private boolean     m_asc = true;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ResultTable.class);

	/**
	 *  Create a JTable Model from ReportModel
	 *  @param reportModel
	 */
	public ResultTable (RModel reportModel)
	{
		this();
		setModel(reportModel);
	}   //  ResultTable

	/**
	 *  Set Model
	 *  @param reportModel
	 */
	public void setModel (RModel reportModel)
	{
		log.config(reportModel.toString());
		super.setModel(new ResultTableModel(reportModel));
		//
		TableColumnModel tcm = getColumnModel();
		//  Set Editor/Renderer
		for (int i = 0; i < tcm.getColumnCount(); i++)
		{
			TableColumn tc = tcm.getColumn(i);
			RColumn rc = reportModel.getRColumn(i);
			if (rc.getColHeader().equals(tc.getHeaderValue()))
			{
				ResultTableCellRenderer rtcr = new ResultTableCellRenderer(reportModel, rc);
				tc.setCellRenderer(rtcr);
				//
			}
			else
				log.log(Level.SEVERE, "RColumn=" + rc.getColHeader() + " <> TableColumn=" + tc.getHeaderValue());
		}
		autoSize();
	}   //  setModel

	/**
	 *  Set Model
	 *  @param ignored
	 */
	public void setModel (TableModel ignored)
	{
		//  throw new IllegalArgumentException("Requires RModel");  //  default construvtor calls this
		super.setModel(ignored);
	}   //  setModel

	/**
	 *  Table Model Listener
	 *  @param e
	 */
	public void tableChanged(TableModelEvent e)
	{
		super.tableChanged(e);
		log.fine("Type=" + e.getType());
	}   //  tableChanged

	/*************************************************************************/

	/**
	 *  Mouse Clicked
	 *  @param e
	 */
	public void mouseClicked(MouseEvent e)
	{
		int col = getColumnModel().getColumnIndexAtX(e.getX());
		log.fine("Column " + col + " = " + getColumnModel().getColumn(col).getHeaderValue()
			+ ", Table r=" + this.getSelectedRow() + " c=" + this.getSelectedColumn());

		//  clicked Cell
		if (e.getSource() == this)
		{
		}
		//  clicked Header
		else
		{
			int mc = convertColumnIndexToModel(col);
			sort(mc);
		}
	}   //  mouseClicked

	public void mousePressed(MouseEvent e)
	{
	}
	public void mouseReleased(MouseEvent e)
	{
	}
	public void mouseEntered(MouseEvent e)
	{
	}
	public void mouseExited(MouseEvent e)
	{
	}

	
	/**************************************************************************
	 *	Size Columns
	 */
	private void autoSize()
	{
		log.config("");
		//
		final int SLACK = 8;		//	making sure it fits in a column
		final int MAXSIZE = 300;    //	max size of a column
		//
		TableColumnModel tcm = getColumnModel();
		//  For all columns
		for (int col = 0; col < tcm.getColumnCount(); col++)
		{
			TableColumn tc = tcm.getColumn(col);
		//  log.config( "Column=" + col, tc.getHeaderValue());
			int width = 0;

			//	Header
			TableCellRenderer renderer = tc.getHeaderRenderer();
			if (renderer == null)
				renderer = new DefaultTableCellRenderer();
			Component comp = renderer.getTableCellRendererComponent
				(this, tc.getHeaderValue(), false, false, 0, 0);
		//	log.fine( "Hdr - preferred=" + comp.getPreferredSize().width + ", width=" + comp.getWidth());
			width = comp.getPreferredSize().width + SLACK;

			//	Cells
			int maxRow = Math.min(30, getRowCount());       //  first 30 rows
			for (int row = 0; row < maxRow; row++)
			{
				renderer = getCellRenderer(row, col);
				comp = renderer.getTableCellRendererComponent
					(this, getValueAt(row, col), false, false, row, col);
				int rowWidth = comp.getPreferredSize().width + SLACK;
				width = Math.max(width, rowWidth);
			}
			//	Width not greater ..
			width = Math.min(MAXSIZE, width);
			tc.setPreferredWidth(width);
		//	log.fine( "width=" + width);
		}	//	for all columns
	}	//	autoSize

	/**
	 *  Sort Table
	 *  @param modelColumnIndex
	 */
	@SuppressWarnings("unchecked")
	private void sort (int modelColumnIndex)
	{
		int rows = getRowCount();
		if (rows == 0)
			return;
		//  other column
		if (modelColumnIndex != m_lastSortIndex)
			m_asc = true;
		else
			m_asc = !m_asc;

		m_lastSortIndex = modelColumnIndex;
		//
		log.config("#" + modelColumnIndex
			+ " - rows=" + rows + ", asc=" + m_asc);
		ResultTableModel model = (ResultTableModel)getModel();

		//  Prepare sorting
		MSort sort = new MSort(0, null);
		sort.setSortAsc(m_asc);
		//  while something to sort
		sorting:
		while (true)
		{
			//  Create sortList
			ArrayList<MSort> sortList = new ArrayList<MSort>(rows);
			//	fill with data entity
			for (int i = 0; i < rows; i++)
			{
				Object value = model.getValueAt(i, modelColumnIndex);
				sortList.add(new MSort(i, value));
			}
			//	sort list it
			Collections.sort(sortList, sort);
			//  move out of sequence row
			for (int i = 0; i < rows; i++)
			{
				int index = ((MSort)sortList.get(i)).index;
				if (i != index)
				{
			//		log.config( "move " + i + " to " + index);
					model.moveRow (i, index);
					continue sorting;
				}
			}
			//  we are done
		//	log.config( "done");
			break;
		}   //  while something to sort
	}   //  sort

}   //  ResultTable
