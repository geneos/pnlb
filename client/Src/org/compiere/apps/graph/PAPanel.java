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

import java.awt.event.*;
import javax.swing.*;

import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 * 	Performance Analysis Panel.
 * 	Key Performace Indicators
 *	
 *  @author Jorg Janke
 *  @version $Id: PAPanel.java,v 1.1 2005/12/27 06:18:37 jjanke Exp $
 */
public class PAPanel extends CPanel implements ActionListener
{
	/**
	 * 	Get Panel if User has Perfpormance Goals
	 *	@return panel pr null
	 */
	public static PAPanel get()
	{
		int AD_User_ID = Env.getAD_User_ID(Env.getCtx());
		MGoal[] goals = MGoal.getUserGoals(Env.getCtx(), AD_User_ID);
		if (goals.length == 0)
			return null;
		return new PAPanel(goals);
	}	//	get
	
	
	/**************************************************************************
	 * 	Constructor
	 *	@param goals
	 */
	private PAPanel (MGoal[] goals)
	{
		super ();
		m_goals = goals;
		init();
	}	//	PAPanel
	
	/** Goals			*/
	private MGoal[] 	m_goals = null;
	
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (PAPanel.class);
	
	/**
	 * 	Static/Dynamic Init
	 */
	private void init()
	{
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		for (int i = 0; i < m_goals.length; i++)
		{
			PerformanceIndicator pi = new PerformanceIndicator(m_goals[i]);
			pi.addActionListener(this);
			add (pi);
		}
	}	//	init

	/**
	 * 	Action Listener for Drill Down
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() instanceof PerformanceIndicator)
		{
			PerformanceIndicator pi = (PerformanceIndicator)e.getSource();
			log.info(pi.getName());
			MGoal goal = pi.getGoal();
			if (goal.getMeasure() != null)
				new PerformanceDetail(goal);
		}
	}	//	actionPerformed

}	//	PAPanel
