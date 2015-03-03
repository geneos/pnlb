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
import java.util.logging.*;
import javax.swing.*;

import org.compiere.plaf.CompierePLAF;
import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.swing.*;



/**
 *  Application Task
 *
 *  @author     Jorg Janke
 *  @version    $Id: ATask.java,v 1.8 2005/12/27 06:18:36 jjanke Exp $
 */
public class ATask extends CFrame 
	implements ActionListener
{
	/**
	 *  Start Application Task
	 *  @param task task model
	 */
	static public void start (final String title, final MTask task)
	{
		new Thread()
		{
			public void run()
			{
				new ATask(title, task);
			}
		}.start();
	}   //  start

	
	/**************************************************************************
	 *  Full Constructor
	 *  @param title title
	 *  @param task task
	 */
	public ATask (String title, MTask task)
	{
		super (title);
		try
		{
			jbInit();
			AEnv.showCenterScreen(this);
			//
			if (task.isServerProcess())
				info.setText("Executing on Server ...");
			else
				info.setText("Executing locally ...");
			String result = task.execute();
			info.setText(result);
			confirmPanel.getCancelButton().setEnabled(false);
			confirmPanel.getOKButton().setEnabled(true);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, task.toString(), e);
		}
	}   //  ATask

	private Task    m_task = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ATask.class);

	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private JScrollPane infoScrollPane = new JScrollPane();
	private JTextArea info = new JTextArea();

	/**
	 *  Static Layout
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		info.setEditable(false);
		info.setBackground(CompierePLAF.getFieldBackground_Inactive());
		infoScrollPane.getViewport().add(info, null);
		infoScrollPane.setPreferredSize(new Dimension(500,300));
		this.getContentPane().add(infoScrollPane, BorderLayout.CENTER);
		this.getContentPane().add(confirmPanel,  BorderLayout.SOUTH);
		//
		confirmPanel.addActionListener(this);
		confirmPanel.getOKButton().setEnabled(false);
	}   //  jbInit


	/**
	 *  Action Listener
	 *  @param e
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (m_task != null && m_task.isAlive())
			m_task.interrupt();
		dispose();
	}   //  actionPerformed

}   //  ATask
