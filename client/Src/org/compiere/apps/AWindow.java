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

import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.swing.*;
import org.compiere.db.*;

/**
 *  Main Application Window.
 *  - Constructs, initializes and positions JFrame
 *  - Gets content, menu, title from APanel
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: AWindow.java,v 1.33 2005/12/27 06:18:36 jjanke Exp $
 */
public class AWindow extends CFrame
{
	/**
	 *	Standard Constructor - requires initWindow
	 */
	public AWindow ()
	{
		super();
		//	Set UI Components
		this.setIconImage(org.compiere.Compiere.getImage16());
		this.getContentPane().add(m_APanel, BorderLayout.CENTER);
		this.setGlassPane(m_glassPane);
	}	//	AWindow

	/** The GlassPane           */
	private AGlassPane  	m_glassPane = new AGlassPane();
	/** Application Window  	*/
	private APanel			m_APanel = new APanel();
	/**	Logger			*/
	private static CLogger 	log = CLogger.getCLogger(AWindow.class);

	/**
	 *  Dynamic Initialization Workbench
	 *  @param AD_Workbench_ID workbench
	 *  @return true if loaded OK
	 */
	protected boolean initWorkbench (int AD_Workbench_ID)
	{
		this.setName("AWindow_WB_" + AD_Workbench_ID);
		boolean loadedOK = m_APanel.initPanel (AD_Workbench_ID, 0, null);
		//
		commonInit();
		return loadedOK;
	}   //  initWorkbench

	/**
	 *	Dynamic Initialization Single Window
	 *  @param AD_Window_ID window
	 *  @param query query
	 *  @return true if loaded OK
	 */
	public boolean initWindow (int AD_Window_ID, MQuery query)
	{
		this.setName("AWindow_" + AD_Window_ID);
		//
		boolean loadedOK = m_APanel.initPanel (0, AD_Window_ID, query);
		commonInit();
		return loadedOK;
	}	//	initWindow

	/**
	 *  Common Init.
	 * 	After APanel loaded
	 */
	private void commonInit()
	{
		this.setJMenuBar(m_APanel.getMenuBar());
		this.setTitle(m_APanel.getTitle());
		//
		Image image = m_APanel.getImage();
		if (image != null)
			setIconImage(image);
	}   //  commonInit

	
	/*************************************************************************
	 *  Set Window Busy
	 *  @param busy busy
	 */
	public void setBusy (boolean busy)
	{
		if (busy == m_glassPane.isVisible()
			|| CConnection.get().isTerminalServer())
			return;
		log.config(getName() + " - " + busy);
		m_glassPane.setMessage(null);
		m_glassPane.setVisible(busy);
		if (busy)
			m_glassPane.requestFocus();
	}   //  setBusy

	/**
	 *  Set Busy Message
	 *  @param AD_Message message
	 */
	public void setBusyMessage (String AD_Message)
	{
		m_glassPane.setMessage(AD_Message);
	}   //  setBusyMessage

	/**
	 *  Set and start Busy Counter
	 *  @param time in seconds
	 */
	public void setBusyTimer (int time)
	{
		m_glassPane.setBusyTimer (time);
	}   //  setBusyTimer

	/**
	 *  Window Events
	 *  @param e event
	 */
	protected void processWindowEvent(WindowEvent e)
	{
		super.processWindowEvent(e);
//		System.out.println(">> Apps WE_" + e.getID()    // + " Frames=" + getFrames().length
//			+ " " + e);
	}   //  processWindowEvent

	/**
	 * 	Get Application Panel
	 *	@return application panel
	 */
	protected APanel getAPanel()
	{
		return m_APanel;
	}	//	getAPanel
	
	/**
	 *	Dispose
	 */
	public void dispose()
	{
		log.info("");
		if (m_APanel != null)
			m_APanel.dispose();
		m_APanel = null;
		this.removeAll();
		super.dispose();
	//	System.gc();
	}	//	dispose

	/**
	 *  String Representation
	 *  @return Name
	 */
	public  String toString()
	{
		return getName();
	}   //  toString

}	//	AWindow
