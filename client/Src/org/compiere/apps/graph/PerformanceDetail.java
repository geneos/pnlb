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

import org.compiere.apps.*;
import org.compiere.swing.*;
import org.compiere.model.*;


/**
 * 	Performance Detail Frame.
 * 	BarPanel for Drill-Down
 *	
 *  @author Jorg Janke
 *  @version $Id: PerformanceDetail.java,v 1.1 2005/12/27 06:18:37 jjanke Exp $
 */
public class PerformanceDetail extends CFrame
	implements ActionListener
{
	/**
	 * 	Constructor.
	 * 	Called from PAPanel, ViewPI (Performance Indicator)
	 *	@param goal goal
	 */
	public PerformanceDetail (MGoal goal)
	{
		super (goal.getName());
		barPanel = new BarGraph(goal);
		init();
		AEnv.showCenterScreen(this);
	}	//	PerformanceDetail

	BarGraph barPanel = null;
	ConfirmPanel confirmPanel = new ConfirmPanel();

	/**
	 * 	Static init
	 */
	private void init()
	{
		getContentPane().add(barPanel, BorderLayout.NORTH);
		
		getContentPane().add(confirmPanel, BorderLayout.SOUTH);
		confirmPanel.addActionListener(this);
	}	//	init
	
	/**
	 * 	Action Listener
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
			dispose();
	}	//	actionPerformed
	
}	//	PerformanceDetail
