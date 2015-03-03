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

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.MetalCheckBoxUI;

/**
 *  Check Box UI
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereCheckBoxUI.java,v 1.9 2005/12/05 02:38:28 jjanke Exp $
 */
public class CompiereCheckBoxUI extends MetalCheckBoxUI
{
	/**
	 *  Create UI
	 *  @param b
	 *  @return ComponentUI
	 */
	public static ComponentUI createUI (JComponent b)
	{
		return s_checkBoxUI;
	}   //  createUI

	/** UI shared   */
	private static CompiereCheckBoxUI s_checkBoxUI = new CompiereCheckBoxUI();

	
	/**************************************************************************
	 *  Install Defaults
	 *  @param b
	 */
	public void installDefaults (AbstractButton b)
	{
		super.installDefaults(b);
		b.setOpaque(false);
	}   //  installDefaults

	/**
	 * 	Create Button Listener
	 *	@param b button
	 *	@return listener
	 */
	protected BasicButtonListener createButtonListener (AbstractButton b)
	{
		return new CompiereButtonListener(b);
	}	//	createButtonListener
	
}   //  CompiereCheckBoxUI
