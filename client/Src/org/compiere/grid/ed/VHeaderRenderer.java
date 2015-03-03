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
import javax.swing.*;
import javax.swing.table.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Table Header Renderer based on Display Type for aligmnet
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VHeaderRenderer.java,v 1.5 2005/03/11 20:28:25 jjanke Exp $
 */
public final class VHeaderRenderer implements TableCellRenderer
{
	/**
	 *	Constructor
	 *  @param displayType
	 */
	public VHeaderRenderer(int displayType)
	{
		super();
		//	Alignment
		if (DisplayType.isNumeric(displayType))
			m_button.setHorizontalAlignment(JLabel.RIGHT);
		else if (displayType == DisplayType.YesNo)
			m_button.setHorizontalAlignment(JLabel.CENTER);
		else
			m_button.setHorizontalAlignment(JLabel.LEFT);
		m_button.setMargin(new Insets(0,0,0,0));
	}	//	VHeaderRenderer

	//  for 3D effect in Windows
	private CButton m_button = new CButton();

	/**
	 *	Get TableCell RendererComponent
	 *  @param table
	 *  @param value
	 *  @param isSelected
	 *  @param hasFocus
	 *  @param row
	 *  @param column
	 *  @return Button
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column)
	{
	//	Log.trace(this,10, "VHeaderRenderer.getTableCellRendererComponent", value==null ? "null" : value.toString());
		//  indicator for invisible column
		if (value == null)
		{
			m_button.setPreferredSize(new Dimension(0,0));
			return m_button;
		}
		m_button.setText(value.toString());
		return m_button;
	}	//	getTableCellRendererComponent

}	//	VHeaderRenderer
