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
//import org.compiere.model.*;

/**
 *	HTML Form Print ELement.
 *  Restrictions:
 *  - Label is not printed
 * 	- Alighnment is ignored
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: HTMLElement.java,v 1.4 2005/03/11 20:34:41 jjanke Exp $
 */
public class HTMLElement extends PrintElement
{
	/**
	 * 	HTML String Constructor
	 * 	@param html html code
	 */
	public HTMLElement (String html)
	{
		if (html == null || html.equals(""))
			throw new IllegalArgumentException("HTMLElement is null");
		log.fine("Length=" + html.length()); 
		//	Create View
		m_renderer = HTMLRenderer.get(html);
	}	//	HTMLElement

	/**	View for Printing						*/
	private HTMLRenderer 	m_renderer;
	
	
	/**************************************************************************
	 * 	Layout and Calculate Size.
	 * 	Set p_width & p_height
	 * 	@return Size
	 */
	protected boolean calculateSize()
	{
		if (p_sizeCalculated)
			return true;
		//
		p_height = m_renderer.getHeight();
		p_width = m_renderer.getWidth();

		//	Limits
		if (p_maxWidth != 0f)
			p_width = p_maxWidth;
		if (p_maxHeight != 0f)
		{
			if (p_maxHeight == -1f)		//	one line only
				p_height = m_renderer.getHeightOneLine();
			else
				p_height = p_maxHeight;
		}
	//	System.out.println("HTMLElement.calculate size - Width="
	//		+ p_width + "(" + p_maxWidth + ") - Height=" + p_height + "(" + p_maxHeight + ")");
		//
		m_renderer.setAllocation((int)p_width, (int)p_height);
		return true;
	}	//	calculateSize

	/*************************************************************************

	/**
	 * 	Paint/Print.
	 *  Calculate actual Size.
	 *  The text is printed in the topmost left position - i.e. the leading is below the line
	 * 	@param g2D Graphics
	 *  @param pageStart top left Location of page
	 *  @param pageNo page number for multi page support (0 = header/footer) - ignored
	 *  @param ctx print context
	 *  @param isView true if online view (IDs are links)
	 */
	public void paint (Graphics2D g2D, int pageNo, Point2D pageStart, Properties ctx, boolean isView)
	{
		//	36.0/137.015625, Clip=java.awt.Rectangle[x=0,y=0,width=639,height=804], Translate=1.0/56.0, Scale=1.0/1.0, Shear=0.0/0.0
	//	log.finest( "HTMLElement.paint", p_pageLocation.x + "/" + p_pageLocation.y
	//		+ ", Clip=" + g2D.getClip()
	//		+ ", Translate=" + g2D.getTransform().getTranslateX() + "/" + g2D.getTransform().getTranslateY()
	//		+ ", Scale=" + g2D.getTransform().getScaleX() + "/" + g2D.getTransform().getScaleY()
	//		+ ", Shear=" + g2D.getTransform().getShearX() + "/" + g2D.getTransform().getShearY());
		//
		Point2D.Double location = getAbsoluteLocation(pageStart);
	//	log.finest( "HTMLElement.paint - PageStart=" + pageStart + ", Location=" + location);
		//
		Rectangle allocation = m_renderer.getAllocation();
		g2D.translate(location.x, location.y);
		m_renderer.paint(g2D, allocation);
		g2D.translate(-location.x, -location.y);
	}	//	paint

	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("HTMLElement[");
		sb.append("Bounds=").append(getBounds())
			.append(",Height=").append(p_height).append("(").append(p_maxHeight)
			.append("),Width=").append(p_width).append("(").append(p_maxHeight)
			.append("),PageLocation=").append(p_pageLocation).append(" - ");
		sb.append("]");
		return sb.toString();
	}	//	toString


	/**************************************************************************
	 * 	Is content HTML
	 *	@param content content
	 *	@return true if HTML
	 */
	public static boolean isHTML (Object content)
	{
		if (content == null)
			return false;
		String s = content.toString();
		if (s.length() < 20)	//	assumption
			return false;
		s = s.trim().toUpperCase();
		if (s.startsWith("<HTML>"))
			return true;
		return false;
	}	//	isHTML

}	//	HTMLElement
