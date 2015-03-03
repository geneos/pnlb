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
 *  Compiere Toggle Button UI
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereToggleButtonUI.java,v 1.9 2005/10/11 02:26:34 jjanke Exp $
 */
public class CompiereToggleButtonUI extends MetalToggleButtonUI
{
	/**
	 *  Static Create UI
	 *  @param c Component
	 *  @return Compiere ToggleButton UI
	 */
	public static ComponentUI createUI (JComponent c)
	{
		return s_toggleButtonUI;
	}   //  createUI

	/** UI shared   */
	private static CompiereToggleButtonUI s_toggleButtonUI = new CompiereToggleButtonUI();

	
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
	 *  Update -
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
		if (c.isOpaque())   //  flat background
	//		CompiereColor.getDefaultBackground().paint(g, c);
			CompiereUtils.fillRectange((Graphics2D)g, c, CompiereLookAndFeel.ROUND);
		paint (g, c);
	}   //  update

	/**
	 *  Paint 3D Box
	 *  @param g Graphics
	 *  @param c Component
	 */
	public void paint(Graphics g, JComponent c)
	{
		super.paint(g, c);
		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();
		boolean in = model.isPressed() || model.isSelected();
		//
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

}   //  CompiereToggleButton
