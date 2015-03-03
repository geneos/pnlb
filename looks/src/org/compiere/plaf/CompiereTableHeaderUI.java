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

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;

/**
 *  Table Header UI
 *  3D effect
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereTableHeaderUI.java,v 1.10 2005/10/09 19:01:37 jjanke Exp $
 */
public class CompiereTableHeaderUI extends BasicTableHeaderUI
{
	/**
	 *  Static Create UI
	 *  @param c Component
	 *  @return Compiere TableHeader UI
	 */
	public static ComponentUI createUI(JComponent c)
	{
		return new CompiereTableHeaderUI();
	}   //  createUI


	/**
	 *  Install UI - set not Opaque
	 *  @param c
	 */
	public void installUI(JComponent c)
	{
		super.installUI(c);
		//  TableHeader is in JViewpoiunt, which is Opaque
		//  When UI created, TableHeader not added to viewpoint
		c.setOpaque(true);
		c.putClientProperty(CompierePLAF.BACKGROUND_FILL, "Y");
	}   //  installUI

	
	/**************************************************************************
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
	//	CompiereUtils.printParents (c);     //  Parent is JViewpoint
		if (c.isOpaque())   //  flat
			CompiereUtils.fillRectange((Graphics2D)g, c, CompiereLookAndFeel.ROUND);
		//
		paint (g, c);
	}   //  update

	/**
	 *  Paint 3D box
	 *  @param g
	 *  @param c
	 */
	public void paint(Graphics g, JComponent c)
	{
		super.paint( g, c);
		CompiereUtils.paint3Deffect((Graphics2D)g, c, CompiereLookAndFeel.ROUND, true);
	}   //  paint

}   //  CompiereTableHeader
