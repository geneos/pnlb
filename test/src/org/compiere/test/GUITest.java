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
package org.compiere.test;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.compiere.plaf.CompierePLAF;
import org.compiere.plaf.CompiereTheme;
import org.compiere.swing.*;
import org.compiere.util.*;
import org.compiere.apps.*;
//import org.eevolution.plaf.*;



public class GUITest extends CDialog
{

	public GUITest ()
	{
		super ();
		getContentPane().add(p1);
		
		AppsAction aa = new AppsAction("&Report",	
			KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), false);
		aa.setDelegate(this);
		p1.add(aa.getButton());
		AEnv.showCenterScreen(this);
		
		Util.printActionInputMap(p1);
		Util.printActionInputMap(aa.getButton());
	}	//	GUITest
	
	FlowLayout p1Layout = new FlowLayout();
	CPanel p1 = new CPanel(p1Layout);
	
	
	@Override
	public void actionPerformed (ActionEvent e)
	{
		System.out.println(e);
		super.actionPerformed (e);
	}

	/**
	 * 	main
	 *	@param args
	 */
	public static void main (String[] args)
	{
		Ini.loadProperties (false);
		CompiereTheme.load();
		CompierePLAF.setPLAF (null);
		new GUITest();
	}
	
}	//	GUITest

