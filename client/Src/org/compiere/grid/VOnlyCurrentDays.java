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
package org.compiere.grid;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Queries how many days back history is displayed as current
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VOnlyCurrentDays.java,v 1.7 2005/12/09 05:17:53 jjanke Exp $
 */
public class VOnlyCurrentDays extends CDialog
	implements ActionListener
{
	/**
	 *	Constructor
	 *  @param parent parent frame
	 *  @param buttonLocation lower left corner of the button
	 */
	public VOnlyCurrentDays(Frame parent, Point buttonLocation)
	{
		//	How long back in History?
		super (parent, Msg.getMsg(Env.getCtx(), "VOnlyCurrentDays", true), true);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "VOnlyCurrentDays", e);
		}
		this.pack();
		buttonLocation.x -= (int)getPreferredSize().getWidth()/2;
		this.setLocation(buttonLocation);
		this.setVisible(true);
	}	//	VOnlyCurrentDays

	private CPanel mainPanel = new CPanel();
	private CButton bShowAll = new CButton();
	private CButton bShowMonth = new CButton();
	private CButton bShowWeek = new CButton();
	private CButton bShowDay = new CButton();
	private CButton bShowYear = new CButton();

	/**	Days (0=all)			*/
	private int 	m_days = 0;
	/**	Margin					*/
	private static Insets	s_margin = new Insets (2, 2, 2, 2);
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VOnlyCurrentDays.class);

	/**
	 * 	Static Initializer
	 * 	@throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		bShowAll.setText(Msg.getMsg(Env.getCtx(), "All"));
		bShowAll.addActionListener(this);
		bShowAll.setMargin(s_margin);
		bShowYear.setText(Msg.getMsg(Env.getCtx(), "Year"));
		bShowYear.addActionListener(this);
		bShowYear.setMargin(s_margin);
		bShowMonth.setText(Msg.getMsg(Env.getCtx(), "Month"));
		bShowMonth.addActionListener(this);
		bShowMonth.setMargin(s_margin);
		bShowWeek.setText(Msg.getMsg(Env.getCtx(), "Week"));
		bShowWeek.addActionListener(this);
		bShowWeek.setMargin(s_margin);
		bShowDay.setText(Msg.getMsg(Env.getCtx(), "Day"));
		bShowDay.addActionListener(this);
		bShowDay.setMargin(s_margin);
		bShowDay.setDefaultCapable(true);
		//
		mainPanel.add(bShowDay, null);
		mainPanel.add(bShowWeek, null);
		mainPanel.add(bShowMonth, null);
		mainPanel.add(bShowYear, null);
		mainPanel.add(bShowAll, null);
		//
		mainPanel.setToolTipText(Msg.getMsg(Env.getCtx(), "VOnlyCurrentDays", false));
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		this.getRootPane().setDefaultButton(bShowDay);
	}	//	jbInit

	/**
	 * 	Action Listener
	 * 	@param e evant
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bShowDay)
			m_days = 1;
		else if (e.getSource() == bShowWeek)
			m_days = 7;
		else if (e.getSource() == bShowMonth)
			m_days = 31;
		else if (e.getSource() == bShowYear)
			m_days = 356;
		else
			m_days = 0;		//	all
		dispose();
	}	//	actionPerformed

	/**
	 * 	Get selected number of days
	 * 	@return days or -1 for all
	 */
	public int getCurrentDays()
	{
		return m_days;
	}	//	getCurrentDays

}	//	VOnlyCurrentDays
