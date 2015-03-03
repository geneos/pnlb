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

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/**
 *  Compiere Split Pane UI.
 *  When moving, the divider is painted in darkGray.
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereSplitPaneUI.java,v 1.8 2005/10/09 19:01:36 jjanke Exp $
 */
public class CompiereSplitPaneUI extends BasicSplitPaneUI
{
	/**
	 *  Creates a new MetalSplitPaneUI instance
	 *  @param x
	 *  @return ComponentUI
	 */
	public static ComponentUI createUI (JComponent x)
	{
		return new CompiereSplitPaneUI();
	}   //  createUI

	/**
	 *  Creates the default divider.
	 *  @return SplitPaneDivider
	 */
	public BasicSplitPaneDivider createDefaultDivider()
	{
		return new CompiereSplitPaneDivider (this);
	}

	/**
	 *  Installs the UI.
	 *  @param c
	 */
	public void installUI (JComponent c)
	{
		super.installUI(c);
		c.setOpaque(false);
		//  BasicBorders$SplitPaneBorder paints gray border
		//  resulting in a 2pt border for the left/right components
		//  but results in 1pt gray line on top/button of divider.
		//  Still, a 1 pt shaddow light gay line is painted
		c.setBorder(null);
	}   //  installUI



}   //  CompiereSplitPaneUI
