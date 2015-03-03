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
package org.compiere.grid.tree;

import javax.swing.tree.*;
import java.awt.*;
import javax.swing.*;

import org.compiere.model.*;

/**
 *	Tree Cell Renderer to context sensitive display Icon
 *  @author 	Jorg Janke
 *  @version 	$Id: VTreeCellRenderer.java,v 1.7 2005/03/11 20:28:29 jjanke Exp $
 */
public final class VTreeCellRenderer extends DefaultTreeCellRenderer
{
	/**
	 *	Constructor
	 */
	public VTreeCellRenderer()
	{
		super();
	}	//	VTreeCellRenderer


	/**
	 *	Get Tree Cell Renderer Component.
	 *  Sets Icon, Name, Description for leaves
	 *  @param tree tree
	 *  @param value value
	 *  @param selected selected
	 *  @param expanded expanded
	 *  @param leaf leaf
	 *  @param row row
	 *  @param hasFocus focus
	 *  @return renderer
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
		boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		VTreeCellRenderer c = (VTreeCellRenderer)super.getTreeCellRendererComponent
			(tree, value, selected, expanded, leaf, row, hasFocus);
		if (!leaf)
			return c;

		//	We have a leaf
		MTreeNode nd = (MTreeNode)value;
		Icon icon = nd.getIcon();
		if (icon != null)
			c.setIcon(icon);
		c.setText(nd.getName());
		c.setToolTipText(nd.getDescription());
		if (!selected)
			c.setForeground(nd.getColor());
		return c;
	}	//	getTreeCellRendererComponent

}	//	VTreeCellRenderer
