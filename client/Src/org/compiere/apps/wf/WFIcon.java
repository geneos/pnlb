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
package org.compiere.apps.wf;

import java.awt.*;
import javax.swing.*;

import org.compiere.model.*;


/**
 *	Work Flow Icon
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: WFIcon.java,v 1.8 2005/03/11 20:28:37 jjanke Exp $
 */
public class WFIcon implements Icon
{
	/**
	 * 	Constructor
	 *	@param type see MTreeNode.TYPE_
	 */
	public WFIcon (int type)
	{
		if (type > 0 && type < MTreeNode.IMAGES.length)
			m_type = type;
	}	//	WFIcon

	/**
	 * 	Constructor
	 *	@param action image indicator
	 */
	public WFIcon (String action)
	{
		if (action != null)
			m_type = MTreeNode.getImageIndex(action);
	}	//	WFIcon


	private static int 		WIDTH = 20;		//	Image is 16x16
	private static int 		HEIGHT = 20;

	/**	Image Index			*/
	private int				m_type = 0;

	/**
	 *	Draw the icon at the specified location.  Icon implementations
	 *	may use the Component argument to get properties useful for
	 *	painting, e.g. the foreground or background color.
	 *
	 * 	@param c	Component
	 * 	@param g	Graphics
	 * 	@param x	X
	 * 	@param y	Y
	 * @see javax.swing.Icon#paintIcon(Component, Graphics, int, int)
	 */
	public void paintIcon (Component c, Graphics g, int x, int y)
	{
		Graphics2D g2D = (Graphics2D)g;
		Icon icon = MTreeNode.getIcon(m_type);
		if (icon != null)
		{
			int xI = x + ((WIDTH - icon.getIconWidth()) / 2);
			int yI = y + ((HEIGHT - icon.getIconHeight()) / 2);
			icon.paintIcon(c, g, xI, yI);
		}
		else	//	draw dot
		{
			int size = 10;
			int xI = x + ((WIDTH - size) / 2);
			int yI = y + ((HEIGHT - size) / 2);
			g2D.setColor(Color.magenta);
			g2D.fillOval(xI, yI, size, size);
		}
	}	//	PaintIcon

	/**
	 *	Returns the icon's width.
	 *	@return an int specifying the fixed width of the icon.
	 * @see javax.swing.Icon#getIconWidth()
	 */
	public int getIconWidth()
	{
		return WIDTH;
	}	//	getIconWidth

	/**
	 *	Returns the icon's height.
	 *	@return an int specifying the fixed height of the icon.
	 * @see javax.swing.Icon#getIconHeight()
	 */
	public int getIconHeight()
	{
		return HEIGHT;
	}	//	getIconHeight

}	//	WFIcon
