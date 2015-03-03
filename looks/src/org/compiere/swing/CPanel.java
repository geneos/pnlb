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
package org.compiere.swing;

import java.awt.*;
import javax.swing.*;

import org.compiere.plaf.CompiereColor;
import org.compiere.plaf.CompierePLAF;
import org.compiere.plaf.CompierePanelUI;
import org.compiere.util.*;

/**
 *  Compiere Panel supporting colored Backgrounds
 *
 *  @author     Jorg Janke
 *  @version    $Id: CPanel.java,v 1.17 2005/12/09 05:19:58 jjanke Exp $
 */
public class CPanel extends JPanel
{
	/**
	 * Creates a new CompierePanel with the specified layout manager
	 * and buffering strategy.
	 * @param layout  the LayoutManager to use
	 * @param isDoubleBuffered  a boolean, true for double-buffering, which
	 *        uses additional memory space to achieve fast, flicker-free updates
	 */
	public CPanel (LayoutManager layout, boolean isDoubleBuffered)
	{
		super (layout, isDoubleBuffered);
		init();
	}   //  CPanel

	/**
	 * Create a new buffered CPanel with the specified layout manager
	 * @param layout  the LayoutManager to use
	 */
	public CPanel (LayoutManager layout)
	{
		super (layout);
		init();
	}   //  CPanel

	/**
	 * Creates a new <code>CPanel</code> with <code>FlowLayout</code>
	 * and the specified buffering strategy.
	 * If <code>isDoubleBuffered</code> is true, the <code>CPanel</code>
	 * will use a double buffer.
	 * @param isDoubleBuffered  a boolean, true for double-buffering, which
	 *        uses additional memory space to achieve fast, flicker-free updates
	 */
	public CPanel (boolean isDoubleBuffered)
	{
		super (isDoubleBuffered);
		init();
	}   //  CPanel

	/**
	 * Creates a new <code>CPanel</code> with a double buffer and a flow layout.
	 */
	public CPanel()
	{
		super ();
		init();
	}   //  CPanel

	/**
	 * Creates a new <code>CPanel</code> with a double buffer and a flow layout.
	 * @param bc Initial Background Color
	 */
	public CPanel(CompiereColor bc)
	{
		this ();
		init();
		setBackgroundColor (bc);
	}   //  CPanel

	/**
	 *  Common init.
	 *  Compiere backround requires that for the base, background is set explictily.
	 *  The additional panels should be transparent.
	 */
	private void init()
	{
		setOpaque(false);	//	transparent
	}   //  init

	
	/**************************************************************************
	 *  Set Background - ignored by UI -
	 *  @param bg ignored
	 */
	public void setBackground (Color bg)
	{
		if (bg.equals(getBackground()))
			return;
		super.setBackground (bg);
		//  ignore calls from javax.swing.LookAndFeel.installColors(LookAndFeel.java:61)
		if (!Trace.getCallerClass(1).startsWith("javax"))
			setBackgroundColor (new CompiereColor(bg));
	}   //  setBackground

	/**
	 *  Set Background
	 *  @param bg CompiereColor for Background, if null set standard background
	 */
	public void setBackgroundColor (CompiereColor bg)
	{
		if (bg == null)
			bg = CompierePanelUI.getDefaultBackground();
		setOpaque(true);	//	not transparent
		putClientProperty(CompierePLAF.BACKGROUND, bg);
		super.setBackground (bg.getFlatColor());
	}   //  setBackground

	/**
	 *  Get Background
	 *  @return Color for Background
	 */
	public CompiereColor getBackgroundColor ()
	{
		try
		{
			return (CompiereColor)getClientProperty(CompierePLAF.BACKGROUND);
		}
		catch (Exception e)
		{
			System.err.println("CPanel - ClientProperty: " + e.getMessage());
		}
		return null;
	}   //  getBackgroundColor

	/*************************************************************************/

	/**
	 *  Set Tab Hierarchy Level.
	 *  Has only effect, if tabs are on left or right side
	 *
	 *  @param level
	 */
	public void setTabLevel (int level)
	{
		if (level == 0)
			putClientProperty(CompierePLAF.TABLEVEL, null);
		else
			putClientProperty(CompierePLAF.TABLEVEL, new Integer(level));
	}   //  setTabLevel

	/**
	 *  Get Tab Hierarchy Level
	 *  @return Tab Level
	 */
	public int getTabLevel()
	{
		try
		{
			Integer ll = (Integer)getClientProperty(CompierePLAF.TABLEVEL);
			if (ll != null)
				return ll.intValue();
		}
		catch (Exception e)
		{
			System.err.println("ClientProperty: " + e.getMessage());
		}
		return 0;
	}   //  getTabLevel

	
	/**************************************************************************
	 *  String representation
	 *  @return String representation
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("CPanel [");
		sb.append(super.toString());
		CompiereColor bg = getBackgroundColor();
		if (bg != null)
			sb.append(bg.toString());
		sb.append("]");
		return sb.toString();
	}   //  toString

}   //  CPanel
