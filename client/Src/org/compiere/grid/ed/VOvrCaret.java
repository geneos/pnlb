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

import javax.swing.plaf.*;
import javax.swing.text.*;
import java.awt.*;

/**
 *	Overwrite Caret
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VOvrCaret.java,v 1.5 2005/03/11 20:28:26 jjanke Exp $
 */
public class VOvrCaret extends DefaultCaret
{
	/**
	 *	Constructor
	 */
	public VOvrCaret()
	{
		super();
	}	//	VOvrCaret

	/**
	 * Renders the caret as a top and button bracket.
	 *
	 * @param g the graphics context
	 * @see #damage
	 */
	public void paint(Graphics g)
	{
		boolean dotLTR = true;			//	left-to-right
		Position.Bias dotBias = Position.Bias.Forward;

		//
		if (isVisible())
		{
			try
			{
				TextUI mapper = getComponent().getUI();
				Rectangle r = mapper.modelToView(getComponent(), getDot(), dotBias);
				Rectangle e = mapper.modelToView(getComponent(), getDot()+1, dotBias);
			//	g.setColor(getComponent().getCaretColor());
				g.setColor(Color.blue);
				//
				int cWidth = e.x-r.x;
				int cHeight = 4;
				int cThick = 2;
				//
				g.fillRect(r.x-1, r.y, cWidth, cThick);						//	 top
				g.fillRect(r.x-1, r.y, cThick, cHeight);					//	|
				g.fillRect(r.x-1+cWidth, r.y, cThick, cHeight);				//	  |
				//
				int yStart = r.y+r.height;
				g.fillRect(r.x-1, yStart-cThick, cWidth, cThick);			//	 button
				g.fillRect(r.x-1, yStart-cHeight, cThick, cHeight);			//	|
				g.fillRect(r.x-1+cWidth, yStart-cHeight, cThick, cHeight);	//	  |
			}
			catch (BadLocationException e)
			{
				//	can't render
			//	System.err.println("Can't render cursor");
			}
		}	//	isVisible
	}	//	paint

	/**
	 * Damages the area surrounding the caret to cause
	 * it to be repainted in a new location.
	 * This method should update the caret bounds (x, y, width, and height).
	 *
	 * @param r  the current location of the caret
	 * @see #paint
	 */
	protected synchronized void damage(Rectangle r)
	{
		if (r != null)
		{
			x = r.x - 4;		//	start 4 pixles before	(one required)
			y = r.y;
			width = 18;			//	sufficent for standard font (18-4=14)
			height = r.height;
			repaint();
		}
	}	//	damage

}	//	VOvrCaret
