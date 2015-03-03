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
package org.compiere.install;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.*;

import org.compiere.apps.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Setup Dialog Frame.
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: Setup.java,v 1.12 2005/12/09 05:19:33 jjanke Exp $
 */
public class Setup extends CFrame implements ActionListener
{
	/**
	 * 	Constructor
	 */
	public Setup()
	{
		CLogger.get().info(Compiere.getSummaryAscii());
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//	addWindowListener(this);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		/** Init Panel			**/
		AEnv.showCenterScreen(this);
		try
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			configurationPanel.dynInit();
			AEnv.positionCenterScreen(this);
			setCursor(Cursor.getDefaultCursor());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}	//	Setup


	//	Static UI
	static ResourceBundle res = ResourceBundle.getBundle("org.compiere.install.SetupRes");
	private JPanel contentPane;
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuFile = new JMenu();
	private CMenuItem menuFileExit = new CMenuItem();
	private JMenu menuHelp = new JMenu();
	private CMenuItem menuHelpInfo = new CMenuItem();
	private JLabel statusBar = new JLabel();
	private BorderLayout borderLayout = new BorderLayout();
	private ConfigurationPanel configurationPanel = new ConfigurationPanel (statusBar);

	/**
	 * 	Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setIconImage(Compiere.getImage16());
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(borderLayout);

		this.setTitle(res.getString("CompiereServerSetup"));
		statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
		statusBar.setText(" ");
		menuFile.setText(res.getString("File"));
		menuFileExit.setText(res.getString("Exit"));
		menuFileExit.addActionListener(this);
		menuHelp.setText(res.getString("Help"));
		menuHelpInfo.setText(res.getString("Help"));
		menuHelpInfo.addActionListener(this);
		borderLayout.setHgap(5);
		borderLayout.setVgap(5);
		menuFile.add(menuFileExit);
		menuHelp.add(menuHelpInfo);
		menuBar.add(menuFile);
		menuBar.add(menuHelp);
		this.setJMenuBar(menuBar);
		contentPane.add(statusBar, BorderLayout.SOUTH);
		contentPane.add(configurationPanel, BorderLayout.CENTER);
	}	//	jbInit

	/**
	 * 	ActionListener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == menuFileExit)
			System.exit(0);
		else if (e.getSource() == menuHelpInfo)
			new Setup_Help(this);
	}	//	actionPerformed
	

	/**
	 * 	Start
	 * 	@param args Log Level e.g. ALL, FINE
	 */
	public static void main(String[] args)
	{
		CLogMgt.initialize(true);
		Handler fileHandler = new CLogFile(System.getProperty("user.dir"), false, false);
		CLogMgt.addHandler(fileHandler);
		//	Log Level
		if (args.length > 0)
			CLogMgt.setLevel(args[0]);
		else
			CLogMgt.setLevel(Level.INFO);
		//	File Loger at least FINE
		if (fileHandler.getLevel().intValue() > Level.FINE.intValue())
			fileHandler.setLevel(Level.FINE);
		
		new Setup();
	}	//	main

}	//	Setup
