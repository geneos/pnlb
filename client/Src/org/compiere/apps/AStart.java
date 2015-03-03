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
package org.compiere.apps;

import java.awt.*;
import javax.swing.*;

/**
 *  Applet Start
 *
 *  @author Jorg Janke
 *  @version  $Id: AStart.java,v 1.5 2005/03/11 20:28:21 jjanke Exp $
 */
public final class AStart extends JApplet
{
	boolean isStandalone = false;

	/**
	 *  Get a parameter value
	 */
	public String getParameter(String key, String def)
	{
		return isStandalone ? System.getProperty(key, def) :
			(getParameter(key) != null ? getParameter(key) : def);
	}

	/**
	 *  Construct the applet
	 */
	public AStart()
	{
	}

	/**
	 *  Initialize the applet
	 */
	public void init()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 *  Component initialization
	 */
	private void jbInit() throws Exception
	{
		this.setSize(new Dimension(400,300));
	}

	/**
	 * Start the applet
	 */
	public void start()
	{
	}

	/**
	 * Stop the applet
	 */
	public void stop()
	{
	}

	/**
	 * Destroy the applet
	 */
	public void destroy()
	{
	}

	/**
	 * Get Applet information
	 */
	public String getAppletInfo()
	{
		return "Start Applet";
	}

	/**
	 *  Get parameter info
	 */
	public String[][] getParameterInfo()
	{
		return null;
	}

	/**
	 *  Main method
	 */
	public static void main(String[] args)
	{
		AStart applet = new AStart();
		applet.isStandalone = true;
		JFrame frame = new JFrame();
		//EXIT_ON_CLOSE == 3
		frame.setDefaultCloseOperation(3);
		frame.setTitle("Start Applet");
		frame.getContentPane().add(applet, BorderLayout.CENTER);
		applet.init();
		applet.start();
		frame.setSize(400,320);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
		frame.setVisible(true);
	}

	//static initializer for setting look & feel
	static
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch(Exception e)
		{
		}
	}
}   //  AStert
