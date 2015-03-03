package org.compiere.plaf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuUI;

/**
 * 	Compiere Menu UI.
 * 	The main menu.
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereMenuUI.java,v 1.4 2005/10/09 19:01:37 jjanke Exp $
 */
public class CompiereMenuUI extends BasicMenuUI
{
	/**
	 *  Create own instance
	 *  @param x
	 *  @return CompiereMenuBarUI
	 */
	public static ComponentUI createUI(JComponent x)
	{
		return new CompiereMenuUI();
	}	//	createUI

	/**
	 *  Install UI
	 *  @param c
	 */
	public void installUI (JComponent c)
	{
		super.installUI(c);
		c.setOpaque(false);		//	use MenuBarUI background
	}   //  installUI

}	//	CompiereMenuUI