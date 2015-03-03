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

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ViewportUI;
import javax.swing.plaf.basic.BasicViewportUI;

/**
 *  Compiere View Point
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereViewportUI.java,v 1.6 2005/03/11 20:34:36 jjanke Exp $
 */
public class CompiereViewportUI extends BasicViewportUI
{
	/** Shared UI object    */
	private static ViewportUI viewportUI;

	/**
	 *  Create UI
	 *  @param c
	 *  @return CompiereViewpointUI
	 */
	public static ComponentUI createUI (JComponent c)
	{
		if (viewportUI == null)
			viewportUI = new CompiereViewportUI();
		return viewportUI;
	}   //  createUI

	/**
	 *  Install UI
	 *  @param c
	 */
	public void installUI(JComponent c)
	{
		super.installUI(c);
		//  will be ignored as set in constructor after updateUI - Sun bug: 4677611
		c.setOpaque(false);
	}   //  installUI

}   //  CompiereViewpointUI
