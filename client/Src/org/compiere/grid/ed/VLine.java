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
import javax.swing.border.*;

import org.compiere.plaf.CompierePLAF;
import org.compiere.util.*;


/**
 *  Horizontal Line with Text
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VLine.java,v 1.8 2005/03/11 20:28:26 jjanke Exp $
 */
public class VLine extends AbstractBorder
{
	/**
	 *  IDE Bean Constructor
	 */
	public VLine()
	{
		this("");
	}   //  VLine

	/**
	 *	Constructor
	 *  @param header
	 */
	public VLine(String header)
	{
		super();
		setHeader(header);
	}	//	VLine

	/** Header          */
	private String  m_header = "";

	private Font    m_font = CompierePLAF.getFont_Label();
	private Color   m_color = CompierePLAF.getTextColor_Label();

	/** Gap between element     */
	public final static int    GAP = 5;
	/** space for characters below line (y) */
	public final static int    SPACE = 4;       //  used in VPanel

	/**
	 * Paint Border
	 * @param c the component for which this border is being painted
	 * @param g the paint graphics
	 * @param x the x position of the painted border
	 * @param y the y position of the painted border
	 * @param w the width of the painted border
	 * @param h the height of the painted border
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
	{
		Graphics copy = g.create();
		if (copy != null)
		{
			try
			{
				copy.translate(x, y);
				paintLine(c, copy, w, h);
			}
			finally
			{
				copy.dispose();
			}
		}
	}	//	paintBorder

	/**
	 *	Paint Line with Header
	 *  @param c
	 *  @param g
	 *  @param w
	 *  @param h
	 */
	private void paintLine (Component c, Graphics g, int w, int h)
	{
		int y = h-SPACE;
		//	Line
		g.setColor(Color.darkGray);
		g.drawLine(GAP, y, w-GAP, y);
		g.setColor(Color.white);
		g.drawLine(GAP, y+1, w-GAP, y+1);       //	last part of line

		if (m_header == null || m_header.length() == 0)
			return;

		//	Header Text
		g.setColor(m_color);
		g.setFont(m_font);
		int x = GAP;
		if (!Language.getLoginLanguage().isLeftToRight())
		{
		}
		g.drawString(m_header, GAP, h-SPACE-1);
	}	//	paintLine

	/**
	 *  Set Header
	 *  @param newHeader String - '_' are replaced by spaces
	 */
	public void setHeader(String newHeader)
	{
		m_header = newHeader.replace('_', ' ');
	}   //  setHeader

	/**
	 *  Get Header
	 *  @return header string
	 */
	public String getHeader()
	{
		return m_header;
	}   //  getHeader

}	//	VLine
