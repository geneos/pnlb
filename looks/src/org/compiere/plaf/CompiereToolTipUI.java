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
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

/**
 * 	Tool Tip
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereToolTipUI.java,v 1.2 2005/12/05 02:38:28 jjanke Exp $
 */
public class CompiereToolTipUI extends MetalToolTipUI
{
	
    public static ComponentUI createUI(JComponent c) 
    {
        return sharedInstance;
    }

    static CompiereToolTipUI sharedInstance = new CompiereToolTipUI();

    private JToolTip tip;
	
    public Dimension getPreferredSize(JComponent c) 
    {
    	tip = (JToolTip)c;
    	return super.getPreferredSize(c);
    }
    public void paint(Graphics g, JComponent c) 
    {
    	tip = (JToolTip)c;
    	super.paint(g, c);
    }

    /**
     * 	Get Accelerator String
     *	@return string
     */
    public String getAcceleratorString ()
	{
    	String str = super.getAcceleratorString();
    	
		if (tip == null || isAcceleratorHidden ())
			return str;
		JComponent comp = tip.getComponent ();
		if (comp == null || comp instanceof JTabbedPane || comp instanceof JMenuItem)
			return str;
		
		KeyStroke[] keys = comp.getRegisteredKeyStrokes ();
		StringBuffer controlKeyStr = new StringBuffer();
		for (int i = 0; i < keys.length; i++)
		{
			int mod = keys[i].getModifiers ();
			int condition = comp.getConditionForKeyStroke (keys[i]);
			if (condition == JComponent.WHEN_IN_FOCUSED_WINDOW)
			{
				String prefix = KeyEvent.getKeyModifiersText (mod);
				if (prefix.length() > 1)
				{
					if (controlKeyStr.length() > 0)
						controlKeyStr.append("  ");
					controlKeyStr.append(prefix).append("-")
						.append(KeyEvent.getKeyText(keys[i].getKeyCode()));
					break;		//	just first
				}
			}
		}
		return controlKeyStr.toString();
	}	//	getAcceleratorString
	
}	//	CompierelToolTipUI
