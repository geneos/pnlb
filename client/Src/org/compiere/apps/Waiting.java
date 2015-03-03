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
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Waiting Dialog
 *
 *  @author     Jorg Janke
 *  @version    $Id: Waiting.java,v 1.10 2005/12/27 06:18:37 jjanke Exp $
 */
public class Waiting extends CDialog 
	implements ActionListener
{
	/**
	 *  Constructor - non nodal as otherwise process is blocked
	 *  @param owner
	 *  @param text     Message to be displayed
	 *  @param canNotWait user can continue with other work
	 *  @param timer    timer ticks (seconds) - if 0 then 10
	 */
	public Waiting (Frame owner, String text, boolean canNotWait, int timer)
	{
		super(owner, Msg.getMsg(Env.getCtx(), "Processing"));
		init (text, canNotWait, timer);
	}   //  Waiting

	/**
	 *  Constructor - non modal as otherwise process is blocked
	 *  @param owner
	 *  @param text     Message to be displayed
	 *  @param canNotWait user can continue with other work
	 *  @param timer    timer ticks (seconds) - if 0 then 10
	 */
	public Waiting (Dialog owner, String text, boolean canNotWait, int timer)
	{
		super(owner, Msg.getMsg(Env.getCtx(), "Processing"));
		init (text, canNotWait, timer);
	}   //  Waiting

	/**
	 *  Common Initialize routine - does not create if timer == 1
	 *  @param text     Message to be displayed
	 *  @param canNotWait user can continue with other work
	 *  @param timer    timer ticks (seconds) - if less than 5 then 10
	 */
	private void init (String text, boolean canNotWait, int timer)
	{
		log.fine(text + " - Sec=" + timer);
		//  don't show if 1 sec average
		if (timer == 1)
			return;

		try
		{
			jbInit();
			setText (text);
			if (!canNotWait)
				bDoNotWait.setVisible(false);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "Waiting", e);
		}
		//  set progress Bar
		progressBar.setMinimum(0);
		progressBar.setMaximum(timer < 5 ? 10 : timer); //  min 2 seconds

		//  Timer
		m_timer = new Timer (1000, this);     //  every second
		m_timer.start();
		AEnv.showCenterWindow(getOwner(), this);
	}   //  init

	private int     m_timervalue = 0;
	private Timer   m_timer;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(Waiting.class);

	private CPanel southPanel = new CPanel();
	private CButton bDoNotWait = new CButton();
	private CLabel infoLabel = new CLabel();
	private FlowLayout southLayout = new FlowLayout();
	private CPanel mainPanel = new CPanel();
	private JProgressBar progressBar = new JProgressBar();

	/**
	 *  Static Layout
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		this.getContentPane().add(Box.createVerticalStrut(8), BorderLayout.NORTH);
		this.getContentPane().add(Box.createHorizontalStrut(8), BorderLayout.WEST);
		this.getContentPane().add(Box.createVerticalStrut(8), BorderLayout.SOUTH);
		this.getContentPane().add(Box.createHorizontalStrut(8), BorderLayout.EAST);
		mainPanel.setLayout(new BorderLayout(5,5));
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		//
		infoLabel.setFont(new java.awt.Font("Dialog", 3, 14));
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		infoLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		infoLabel.setIcon(Env.getImageIcon("C10030.gif"));
		infoLabel.setIconTextGap(10);
		mainPanel.add(infoLabel, BorderLayout.NORTH);
		mainPanel.add(progressBar,  BorderLayout.CENTER);
		//
//		bDoNotWait.setText(Msg.getMsg(Env.getCtx(), "DoNotWait"));
//		bDoNotWait.setToolTipText(Msg.getMsg(Env.getCtx(), "DoNotWaitInfo"));
//		bDoNotWait.addActionListener(this);
//		southPanel.setLayout(southLayout);
//		southPanel.add(bDoNotWait, null);
//		mainPanel.add(southPanel, BorderLayout.SOUTH);
	}   //  jbInit

	/**
	 *  Set Info Text
	 *  @param text
	 */
	public void setText (String text)
	{
		infoLabel.setText(text);
	}   //  setText

	/**
	 *  ActionListener
	 *  @param e
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == bDoNotWait)
			doNotWait();
		//
		progressBar.setValue(m_timervalue++);
		if (m_timervalue > progressBar.getMaximum())
			m_timervalue = progressBar.getMinimum();
	//	progressBar.setString(progressBar.getPercentComplete());
	}   //  actionPerformed

	/**
	 *  Set Timer Estimate
	 *  @param max Seconds
	 */
	public void setTimerEstimate (int max)
	{
		progressBar.setMaximum(max);
	}   //  setMaximum

	/**
	 *  User does not want to wait for result and continue with other worg
	 *  Callback & dispose
	 */
	public void doNotWait()
	{
		/** @todo callback */
		dispose();
	}   //  doNotWait

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		if (m_timer != null)
			m_timer.stop();
		m_timer = null;
		super.dispose();
	}   //  dispose

}   //  Waiting
