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
package org.compiere.print.layout;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import org.compiere.print.*;


/**
 *	Line / Box Element
 *	
 *  @author Jorg Janke
 *  @version $Id: BoxElement.java,v 1.2 2005/03/11 20:34:41 jjanke Exp $
 */
public class BoxElement extends PrintElement
{
	/**
	 * 	BoxElement
	 * 	@param item item
	 */
	public BoxElement (MPrintFormatItem item, Color color)
	{
		super ();
		if (item != null && item.isTypeBox())
		{
			m_item = item;
			m_color = color;
		}
	}	//	BoxElement
	
	/** The Item					*/
	private MPrintFormatItem 	m_item = null;
	private Color				m_color = Color.BLACK;

	/**
	 * 	Calculate Size
	 *	@return true if calculated
	 */
	protected boolean calculateSize ()
	{
		p_width = 0;
		p_height = 0;
		if (m_item == null)
			return true;
		return true;
	}	//	calculateSize

	/**
	 * 	Paint
	 *	@param g2D graphics
	 *	@param pageNo page
	 *	@param pageStart page start
	 *	@param ctx context
	 *	@param isView true if Java
	 */
	public void paint (Graphics2D g2D, int pageNo, Point2D pageStart,
		Properties ctx, boolean isView)
	{
		if (m_item == null)
			return;
		//
		g2D.setColor(m_color);
		BasicStroke s = new BasicStroke(m_item.getLineWidth());
		g2D.setStroke(s);
		//
		Point2D.Double location = getAbsoluteLocation(pageStart);
		int x = (int)location.x;
		int y = (int)location.y;

		int width = m_item.getMaxWidth();
		int height = m_item.getMaxHeight();
		
		if (m_item.getPrintFormatType().equals(MPrintFormatItem.PRINTFORMATTYPE_Line))
			g2D.drawLine(x, y, x+width, y+height);
		else
		{
			String type = m_item.getShapeType();
			if (type == null)
				type = "";
			if (m_item.isFilledRectangle())
			{
				if (type.equals(MPrintFormatItem.SHAPETYPE_3DRectangle))
					g2D.fill3DRect(x, y, width, height, true);
				else if (type.equals(MPrintFormatItem.SHAPETYPE_Oval))
					g2D.fillOval(x, y, width, height);
				else if (type.equals(MPrintFormatItem.SHAPETYPE_RoundRectangle))
					g2D.fillRoundRect(x, y, width, height, m_item.getArcDiameter(), m_item.getArcDiameter());
				else
					g2D.fillRect(x, y, width, height);
			}
			else
			{
				if (type.equals(MPrintFormatItem.SHAPETYPE_3DRectangle))
					g2D.draw3DRect(x, y, width, height, true);
				else if (type.equals(MPrintFormatItem.SHAPETYPE_Oval))
					g2D.drawOval(x, y, width, height);
				else if (type.equals(MPrintFormatItem.SHAPETYPE_RoundRectangle))
					g2D.drawRoundRect(x, y, width, height, m_item.getArcDiameter(), m_item.getArcDiameter());
				else
					g2D.drawRect(x, y, width, height);
			}
		}
	}	//	paint
	
}	//	BoxElement
