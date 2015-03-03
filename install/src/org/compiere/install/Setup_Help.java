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
import java.io.*;
import java.util.*;
import javax.swing.*;

import org.compiere.apps.*;
import org.compiere.swing.*;

/**
 *	Setup Online Help
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: Setup_Help.java,v 1.7 2005/10/08 02:03:29 jjanke Exp $
 */
public class Setup_Help extends JDialog implements ActionListener
{
	/**
	 * 	Constructor
	 * 	@param parent parent frame
	 */
	public Setup_Help (Frame parent)
	{
		super (parent, true);
		init(parent);
	}	//	Setup_Help

	/**
	 * 	Constructor
	 * 	@param parent parent dialog
	 */
	public Setup_Help (Dialog parent)
	{
		super (parent, true);
		init(parent);
	}	//	Setup_Help

	/**
	 * 	Constructor init
	 * 	@param parent parent window
	 */
	private void init (Window parent)
	{
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try
		{
			jbInit();
			dynInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		Dimension dlgSize = getPreferredSize();
		Dimension frmSize = parent.getSize();
		Point loc = parent.getLocation();
		setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		try
		{
			pack();
			setVisible(true);	//	HTML load errors
		}
		catch (Exception ex)
		{
		}
	}	//	init


	static ResourceBundle res = ResourceBundle.getBundle("org.compiere.install.SetupRes");
	private CPanel mainPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	private JButton bOK = new JButton();
	private BorderLayout mainLayout = new BorderLayout();
	private JScrollPane centerScrollPane = new JScrollPane();
	private JEditorPane editorPane = new OnlineHelp();


	/**
	 * 	Static layout
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		//imageLabel.setIcon(new ImageIcon(SetupFrame_AboutBox.class.getResource("[Your Image]")));
		this.setTitle(res.getString("CompiereServerSetup") + " " + res.getString("Help"));
		mainPanel.setLayout(mainLayout);
		bOK.setText(res.getString("Ok"));
		bOK.addActionListener(this);
		centerScrollPane.setPreferredSize(new Dimension(600, 400));
		this.getContentPane().add(mainPanel, null);
		southPanel.add(bOK, null);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		setResizable(true);
		mainPanel.add(centerScrollPane, BorderLayout.CENTER);
		centerScrollPane.getViewport().add(editorPane, null);
	}	//	jbInit

	/**
	 * 	Set Content
	 */
	private void dynInit()
	{
		try
		{
			editorPane.setPage("http://www.compiere.org/help/serverSetup.html");
		}
		catch (IOException ex)
		{
			editorPane.setText(res.getString("PleaseCheck")
				+ "	http://www.compiere.org/support <p>("
				+ res.getString("UnableToConnect") + ")");
		}
	}	//	dynInit

	/**
	 * 	Close Dialog if closing
	 *  @param e event
	 */
	protected void processWindowEvent(WindowEvent e)
	{
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
			dispose();
		super.processWindowEvent(e);
	}	//	processWindowEvent

	/**
	 * 	Action Listener
	 * 	@param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bOK)
			dispose();
	}	//	actionPerformed

}	//	Setup_Help
