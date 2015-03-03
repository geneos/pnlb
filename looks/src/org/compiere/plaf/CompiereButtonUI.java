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
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;

/**
 *  Button UI
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereButtonUI.java,v 1.14 2005/12/05 02:38:28 jjanke Exp $
 */
public class CompiereButtonUI extends MetalButtonUI
{
	/**
	 *  Static Create UI
	 *  @param c
	 *  @return Compiere Button UI
	 */
	public static ComponentUI createUI (JComponent c)
	{
		return s_buttonUI;
	}   //  createUI

	/** UI shared   */
	private static CompiereButtonUI s_buttonUI = new CompiereButtonUI();

	
	/**************************************************************************
	 *  Install Defaults
	 *  @param b
	 */
	public void installDefaults(AbstractButton b)
	{
		super.installDefaults(b);
		b.setOpaque(false);
	}   //  installDefaults

	/**
	 *  Update.
	 *  This method is invoked by <code>JComponent</code> when the specified
	 *  component is being painted.
	 *
	 *  By default this method will fill the specified component with
	 *  its background color (if its <code>opaque</code> property is
	 *  <code>true</code>) and then immediately call <code>paint</code>.
	 *
	 *  @param g the <code>Graphics</code> context in which to paint
	 *  @param c the component being painted
	 *
	 *  @see #paint
	 *  @see javax.swing.JComponent#paintComponent
	 */
	public void update(Graphics g, JComponent c)
	{
	//	System.out.println(c.getClass() + " ** " + ((JButton)c).getText() + " ** " + c.isOpaque());
		if (c.isOpaque())
			CompiereUtils.fillRectange((Graphics2D)g, c, CompiereLookAndFeel.ROUND);
		paint (g, c);
	}   //  update

	/**
	 *  Paint 3D boxes
	 *  @param g
	 *  @param c
	 */
	public void paint (Graphics g, JComponent c)
	{
		super.paint( g, c);
		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();
		boolean in = model.isPressed() || model.isSelected();
		//
		if (b.isBorderPainted())
			CompiereUtils.paint3Deffect((Graphics2D)g, c, CompiereLookAndFeel.ROUND, !in);
	}   //  paint

	/**
	 *  Don't get selected Color - use default (otherwise the pressed button is gray)
	 *  @param g
	 *  @param b
	 */
	protected void paintButtonPressed(Graphics g, AbstractButton b)
	{
	//	if (b.isContentAreaFilled())
	//	{
	//		Dimension size = b.getSize();
	//		g.setColor(getSelectColor());
	//		g.fillRect(0, 0, size.width, size.height);
	//	}
	}   //  paintButtonPressed

	/**
	 * 	Is Tool Bar Button
	 *	@param c
	 *	@return true if toolbar
	 */
	boolean isToolBarButton(JComponent c) 
	{
        return (c.getParent() instanceof JToolBar);
    }	//	isToolBarButton
	
	
	/**
	 * 	Create Button Listener
	 *	@param b button
	 *	@return listener
	 */
	protected BasicButtonListener createButtonListener (AbstractButton b)
	{
		return new CompiereButtonListener(b);
	}	//	createButtonListener
	
}   //  CompiereButtonUI
