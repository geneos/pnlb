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
package org.compiere.apps.graph;

import java.awt.*;
import java.util.*;

import org.compiere.util.*;

/**
 * 	Bar Graph Layout
 *	
 *  @author Jorg Janke
 *  @version $Id: BarGraphLayout.java,v 1.1 2005/12/27 06:18:37 jjanke Exp $
 */
public class BarGraphLayout
	implements LayoutManager
{
	/**
	 * 	Bar Graph Layout
	 *	@param parent parenr
	 */
	public BarGraphLayout (BarGraph parent)
	{
		m_parent = parent;
	}	//	BarGraphLayout
	
	/**	Parent Container		*/
	private BarGraph			m_parent;
	/** List of Components		*/
	private ArrayList<BarGraphColumn> m_list = new ArrayList<BarGraphColumn>();
	/** Layout Complete 		*/
	private boolean				m_layoutComplete = false;
	/** Gap between columns		*/
	private static int			XGAP = 2;
	/** Gap to Axix				*/
	private static int			YGAP = 1;
	
	/**	Logger	*/
	private static CLogger 		log = CLogger.getCLogger (BarGraphLayout.class);
	
	/**
	 * 	Add Layout Component
	 *	@param name name
	 *	@param comp component
	 */
	public void addLayoutComponent (String name, Component comp)
	{
		if (comp instanceof BarGraphColumn)
			m_list.add((BarGraphColumn)comp);
		else
			log.severe("Invalid Class: " + comp);
		m_layoutComplete = false;
	}	//	addLayoutComponent

	/**
	 * 	Remove Layout Component
	 *	@param comp component
	 */
	public void removeLayoutComponent (Component comp)
	{
		m_list.remove(comp);
		m_layoutComplete = false;
	}	//	removeLayoutComponent

	/**
	 * 	Preferred Layout Size
	 *	@param parent parent
	 *	@return size
	 */
	public Dimension preferredLayoutSize (Container parent)
	{
		return parent.getPreferredSize();
	}	//	preferredLayoutSize

	/**
	 * 	Minimum Layout Size
	 *	@param parent parent
	 *	@return size
	 */
	public Dimension minimumLayoutSize (Container parent)
	{
		return parent.getMinimumSize();
	}	//	minimumLayoutSize

	
	/**
	 * 	Layout Container
	 *	@param parent
	 */
	public void layoutContainer (Container parent)
	{
		if (m_layoutComplete)
			return;
		
		//	Find Max
		double maxValue = 0;
		for (int i = 0; i < m_list.size(); i++)
		{
			BarGraphColumn column = m_list.get(i);
			maxValue = Math.max(maxValue, column.getValue());
		}
		//
		Dimension size = m_parent.getPreferredSize();
		Point point0_0 = m_parent.getPoint0_0();
		
		double graphHeight = size.height - (size.height-point0_0.y) - (2*YGAP);
		double graphWidth = size.width - point0_0.x - XGAP;
		double columnWidth = (graphWidth - (XGAP*m_list.size())) / m_list.size();
		columnWidth = Math.min(30, columnWidth);
		FontMetrics fm = m_parent.getFontMetrics(m_parent.getFont());
		int fontHeight = fm.getHeight();
		columnWidth = Math.max(fontHeight, columnWidth);

		log.fine("Height=" + graphHeight + ", MaxValue=" + maxValue 
			+ ", Width=" + graphWidth + ", ColumnWidth=" + columnWidth);
		
		int x = point0_0.x + (2*XGAP);
		//	Set Values
		for (int i = 0; i < m_list.size(); i++)
		{
			BarGraphColumn column = m_list.get(i);
			double multiplier = column.getValue() / maxValue;
			double height = graphHeight * multiplier;
			column.setColHeight(height);
			column.setColWidth(columnWidth);
			Dimension ps = column.getPreferredSize();
			column.setBackground(GraphUtil.getBackground(i));
			//
			int y = point0_0.y - ps.height - YGAP;
			column.setLocation(x, y);
			column.setBounds(x, y, ps.width, ps.height);
			x += ps.width + XGAP;
			log.finer(i + " - " + ((int)(multiplier*100)) + "% - " + column.getBounds());
		}
		m_layoutComplete = true;
	}	//	layoutContainer
	
}	//	BarGraphLayout
