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
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Help and HTML Window
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: Help.java,v 1.16 2005/12/09 05:17:57 jjanke Exp $
 */
public class Help extends CDialog
	implements ActionListener
{
	/**
	 *	Help System for Window Help
	 *
	 * @param frame Parent
	 * @param title Title
	 * @param mWindow Window Model
	 */
	public Help (Frame frame, String title, MWindow mWindow)
	{
		super(frame, title, false);
		try
		{
			jbInit();
			loadInfo(mWindow);
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "", ex);
		}
		AEnv.positionCenterWindow(frame, this);
	}	//	Help

	/**
	 *	Help System
	 *
	 * @param frame Parent
	 * @param title Window
	 * @param url   URL to display
	 */
	public Help (Frame frame, String title, URL url)
	{
		super(frame, title, false);
		try
		{
			jbInit();
			info.setPage(url);
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "", ex);
		}
		AEnv.positionCenterWindow(frame, this);
	}	//	Help

	/**
	 *	Help System
	 *
	 * @param frame Parent
	 * @param title Window
	 * @param helpHtml Helptext
	 */
	public Help (Frame frame, String title, String helpHtml)
	{
		super(frame, title, false);
		try
		{
			jbInit();
			info.setContentType("text/html");
			info.setEditable(false);
			info.setBackground(CompierePLAF.getFieldBackground_Inactive());
			info.setText(helpHtml);
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "Help", ex);
		}
		AEnv.positionCenterWindow(frame, this);
	}	//	Help

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(Help.class);
	
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private OnlineHelp info = new OnlineHelp();
	private JScrollPane infoPane = new JScrollPane();
	private ConfirmPanel confirmPanel = new ConfirmPanel();

	/**
	 *	Static Init
	 *
	 * @throws Exception
	 */
	void jbInit() throws Exception
	{
		mainPanel.setLayout(mainLayout);
		mainLayout.setHgap(2);
		mainLayout.setVgap(2);
		infoPane.setBorder(BorderFactory.createLoweredBevelBorder());
		infoPane.setPreferredSize(new Dimension(500, 400));
		getContentPane().add(mainPanel);
		mainPanel.add(infoPane, BorderLayout.CENTER);
		mainPanel.add(confirmPanel, BorderLayout.SOUTH);
		infoPane.getViewport().add(info, null);
		confirmPanel.addActionListener(this);
	}	//	jbInit

	
	/*************************************************************************
	 *	Load Info - Windows Help
	 *  @param mWindow window model
	 */
	private void loadInfo(MWindow mWindow)
	{
		WebDoc doc = mWindow.getHelpDoc(true);
		info.setText(doc.toString());
	}	//	loadInfo

	
	/**************************************************************************
	 *	Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
			dispose();
	}	//	actionPerformed

}	//	Help

