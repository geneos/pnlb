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
package org.compiere.plaf;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

/**
 *  Compiere Menu Bar UI.
 *  Main Menu background
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereMenuBarUI.java,v 1.8 2005/10/09 19:01:37 jjanke Exp $
 */
public class CompiereMenuBarUI extends MetalMenuBarUI
{
	/**
	 *  Create own instance
	 *  @param x
	 *  @return CompiereMenuBarUI
	 */
	public static ComponentUI createUI (JComponent x)
	{
		return new CompiereMenuBarUI();
	}   // createUI

	/**
	 *  Install UI
	 *  @param c
	 */
	public void installUI (JComponent c)
	{
		super.installUI(c);
		c.setOpaque(true);
	}   //  installUI

	/**
	 * 	Update UI
	 *	@param g graphics
	 *	@param c component
	 */
	public void update (Graphics g, JComponent c)
	{
		if (c.isOpaque())
		{
			//  Get CompiereColor
			CompiereColor bg = CompiereColor.getDefaultBackground();
			bg.paint (g, c);
		}
		else
		{
			g.setColor(c.getBackground());
			g.fillRect(0,0, c.getWidth(), c.getHeight());
		}
		paint(g,c);
	}	//	update
	
}   //  CompiereMenuBarUI
