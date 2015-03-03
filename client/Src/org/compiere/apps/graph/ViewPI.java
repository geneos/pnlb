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
package org.compiere.apps.graph;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import javax.swing.*;

import org.compiere.apps.*;
import org.compiere.apps.form.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 * 	View Performance Indicators
 *	
 *  @author Jorg Janke
 *  @version $Id: ViewPI.java,v 1.1 2005/12/27 06:18:37 jjanke Exp $
 */
public class ViewPI extends CPanel
	implements FormPanel, ActionListener
{
	/**
	 * 	Init
	 *	@param WindowNo
	 *	@param frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		log.fine("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			//	Top Selection Panel
		//	m_frame.getContentPane().add(selectionPanel, BorderLayout.NORTH);
			//	Center
			initPanel();
			CScrollPane scroll = new CScrollPane (this);
			m_frame.getContentPane().add(scroll, BorderLayout.CENTER);
			//	South
			confirmPanel.addActionListener(this);
			m_frame.getContentPane().add(confirmPanel, BorderLayout.SOUTH);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		sizeIt();
	}	//	init

	/**
	 * 	Size Window
	 */
	private void sizeIt()
	{
		//	Frame
		m_frame.pack();
	//	Dimension size = m_frame.getPreferredSize();
	//	size.width = WINDOW_WIDTH;
	//	m_frame.setSize(size);
	}	//	size
	
	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
		removeAll();
	}	//	dispose

	/**	Window No					*/
	private int         m_WindowNo = 0;
	/**	FormFrame					*/
	private FormFrame 	m_frame;
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (ViewPI.class);
	/** Confirmation Panel			*/
	private ConfirmPanel confirmPanel = new ConfirmPanel();
	
	
	/**
	 * 	Init Panel
	 */
	private void initPanel()
	{
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		MGoal[] goals = MGoal.getGoals(Env.getCtx());
		for (int i = 0; i < goals.length; i++)
		{
			PerformanceIndicator pi = new PerformanceIndicator(goals[i]);
			pi.addActionListener(this);
			add (pi);
		}
	}	//	initPanel
	
	
	/**
	 * 	Action Listener for Drill Down
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
			dispose();
		else if (e.getSource() instanceof PerformanceIndicator)
		{
			PerformanceIndicator pi = (PerformanceIndicator)e.getSource();
			log.info(pi.getName());
			MGoal goal = pi.getGoal();
			if (goal.getMeasure() != null)
				new PerformanceDetail(goal);
		}
	}	//	actionPerformed

}	//	ViewPI
