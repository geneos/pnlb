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
import javax.swing.plaf.basic.*;

/**
 *  Split Pane Divider
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereSplitPaneDivider.java,v 1.7 2005/10/09 19:01:37 jjanke Exp $
 */
class CompiereSplitPaneDivider extends BasicSplitPaneDivider
{
	/**
	 *  Constructor
	 *  @param ui
	 */
	public CompiereSplitPaneDivider (BasicSplitPaneUI ui)
	{
		super (ui);
		//  BasicBorders$SplitPaneDividerBorder - ignored set after constructor
		setBorder(null);
	}   //  CompiereSplitPaneDivider

	/**
	 *  Paints the divider.
	 *  If the border is painted, it creates a light gray bar on top/button.
	 *  Still, a light gray 1 pt shaddow border is painted on top/button
	 *  @param g
	 */
	public void paint (Graphics g)
	{
		//  BasicBorders$SplitPaneDividerBorder
		setBorder(null);
		super.paint(g);
	}   //  paint

}   //  CompiereSplitPaneDivider
