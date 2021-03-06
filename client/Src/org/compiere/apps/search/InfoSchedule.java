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
package org.compiere.apps.search;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;

import org.compiere.apps.*;
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Schedule - Resource availability & assigment.
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: InfoSchedule.java,v 1.19 2005/12/27 06:18:37 jjanke Exp $
 */
public class InfoSchedule extends CDialog
	implements ActionListener, ChangeListener
{
	/**
	 *  Constructor
	 *  @param frame Parent
	 *  @param mAssignment optional assignment
	 *  @param createNew if true, allows to create new assignments
	 */
	public InfoSchedule (Frame frame, MResourceAssignment mAssignment, boolean createNew)
	{
		super(frame, Msg.getMsg(Env.getCtx(), "InfoSchedule"), frame != null && createNew);
		if (mAssignment == null)
			m_mAssignment = new MResourceAssignment(Env.getCtx(), 0, null);
		else
			m_mAssignment = mAssignment;
		if (mAssignment != null)
			log.info(mAssignment.toString());
		m_dateFrom = m_mAssignment.getAssignDateFrom();
		if (m_dateFrom == null)
			m_dateFrom = new Timestamp(System.currentTimeMillis());
		m_createNew = createNew;
		try
		{
			jbInit();
			dynInit(createNew);
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "InfoSchedule", ex);
		}
		AEnv.showCenterWindow(frame, this);
	}	//	InfoSchedule

	/**
	 * 	IDE Constructor
	 */
	public InfoSchedule()
	{
		this (null, null, false);
	}	//	InfoSchedule

	/**	Resource 					*/
	private MResourceAssignment		m_mAssignment;
	/** Date						*/
	private Timestamp		m_dateFrom = null;
	/**	Loading						*/
	private boolean			m_loading = false;
	/**	 Ability to create new assignments	*/
	private boolean			m_createNew;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(InfoSchedule.class);

	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private JLabel labelResourceType = new JLabel();
	private JComboBox fieldResourceType = new JComboBox();
	private JLabel labelResource = new JLabel();
	private JComboBox fieldResource = new JComboBox();
	private JButton bPrevious = new JButton();
	private JLabel labelDate = new JLabel();
	private VDate fieldDate = new VDate();
	private JButton bNext = new JButton();
	private JTabbedPane timePane = new JTabbedPane();
	private VSchedule daySchedule = new VSchedule(this, VSchedule.TYPE_DAY);
	private VSchedule weekSchedule = new VSchedule(this, VSchedule.TYPE_WEEK);
	private VSchedule monthSchedule = new VSchedule(this, VSchedule.TYPE_MONTH);
	private StatusBar statusBar = new StatusBar();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);

	/**
	 * 	Static Layout
	 * 	@throws Exception
	 */
	private void jbInit() throws Exception
	{
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		labelResourceType.setHorizontalTextPosition(SwingConstants.LEADING);
		labelResourceType.setText(Msg.translate(Env.getCtx(), "S_ResourceType_ID"));
		labelResource.setHorizontalTextPosition(SwingConstants.LEADING);
		labelResource.setText(Msg.translate(Env.getCtx(), "S_Resource_ID"));
		bPrevious.setMargin(new Insets(0, 0, 0, 0));
		bPrevious.setText("<");
		labelDate.setText(Msg.translate(Env.getCtx(), "Date"));
		bNext.setMargin(new Insets(0, 0, 0, 0));
		bNext.setText(">");
		getContentPane().add(mainPanel,  BorderLayout.CENTER);
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		parameterPanel.add(labelResourceType,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(8, 8, 0, 0), 0, 0));
		parameterPanel.add(fieldResourceType,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 8, 8, 4), 0, 0));
		parameterPanel.add(labelResource,    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(8, 4, 0, 4), 0, 0));
		parameterPanel.add(fieldResource,      new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 8, 4), 0, 0));
		parameterPanel.add(bPrevious,   new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 8, 8, 0), 0, 0));
		parameterPanel.add(labelDate,   new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(8, 0, 0, 0), 0, 0));
		parameterPanel.add(fieldDate,   new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 8, 0), 0, 0));
		parameterPanel.add(bNext,   new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 8, 8), 0, 0));
		//
		mainPanel.add(new JScrollPane(timePane),  BorderLayout.CENTER);
		timePane.add(daySchedule,  Msg.getMsg(Env.getCtx(), "Day"));
		timePane.add(weekSchedule,  Msg.getMsg(Env.getCtx(), "Week"));
		timePane.add(monthSchedule,   Msg.getMsg(Env.getCtx(), "Month"));
	//	timePane.add(daySchedule,  Msg.getMsg(Env.getCtx(), "Day"));
	//	timePane.add(weekSchedule,  Msg.getMsg(Env.getCtx(), "Week"));
	//	timePane.add(monthSchedule,   Msg.getMsg(Env.getCtx(), "Month"));
		timePane.addChangeListener(this);
		//
		mainPanel.add(confirmPanel,  BorderLayout.SOUTH);
		//
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);
	}	//	jbInit

	/**
	 * 	Dynamic Init
	 *  @param createNew if true, allows to create new assignments
	 */
	private void dynInit (boolean createNew)
	{
		//	Resource
		fillResourceType();
		fillResource();
		fieldResourceType.addActionListener(this);
		fieldResource.addActionListener(this);

		//	Date
		fieldDate.setValue(m_dateFrom);
		fieldDate.addActionListener(this);
		bPrevious.addActionListener(this);
		bNext.addActionListener(this);

		//	Set Init values
		daySchedule.setCreateNew(createNew);
		weekSchedule.setCreateNew(createNew);
		monthSchedule.setCreateNew(createNew);
		//
		confirmPanel.addActionListener(this);
		displayCalendar();
	}	//	dynInit

	/**
	 * 	Fill Resource Type (one time)
	 */
	private void fillResourceType()
	{
		//	Get ResourceType of selected Resource
		int S_ResourceType_ID = 0;
		if (m_mAssignment.getS_Resource_ID() != 0)
		{
			String sql = "SELECT S_ResourceType_ID FROM S_Resource WHERE S_Resource_ID=?";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, m_mAssignment.getS_Resource_ID());
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					S_ResourceType_ID = rs.getInt(1);
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
			}
		}

		//	Get Resource Types
		String sql = MRole.getDefault().addAccessSQL(
			"SELECT S_ResourceType_ID, Name FROM S_ResourceType WHERE IsActive='Y' ORDER BY 2",
			"S_ResourceType", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		KeyNamePair defaultValue = null;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				KeyNamePair pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				if (S_ResourceType_ID == pp.getKey())
					defaultValue = pp;
				fieldResourceType.addItem(pp);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		if (defaultValue != null)
			fieldResourceType.setSelectedItem(defaultValue);
	}	//	fillResourceType

	/**
	 * 	Fill Resource Pick from Resource Type
	 */
	private void fillResource()
	{
		//	Get Resource Type
		KeyNamePair pp = (KeyNamePair)fieldResourceType.getSelectedItem();
		if (pp == null)
			return;
		int S_ResourceType_ID = pp.getKey();

		KeyNamePair defaultValue = null;

		//	Load Resources
		m_loading = true;
		fieldResource.removeAllItems();
		String sql = "SELECT S_Resource_ID, Name FROM S_Resource WHERE S_ResourceType_ID=? ORDER BY 2";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, S_ResourceType_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				if (m_mAssignment.getS_Resource_ID() == pp.getKey())
					defaultValue = pp;
				fieldResource.addItem(pp);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		if (defaultValue != null)
			fieldResource.setSelectedItem(defaultValue);

		m_loading = false;
	}	//	fillResource

	/**
	 * 	Display Calendar for selected Resource, Time(day/week/month) and Date
	 */
	private void displayCalendar ()
	{
		//	Get Values
		KeyNamePair pp = (KeyNamePair)fieldResource.getSelectedItem();
		if (pp == null)
			return;
		int S_Resource_ID = pp.getKey();
		m_mAssignment.setS_Resource_ID(S_Resource_ID);
		Timestamp date = fieldDate.getTimestamp();
		int index = timePane.getSelectedIndex();
		log.config("Index=" + index + ", ID=" + S_Resource_ID + " - " + date);

		//	Set Info
		m_loading = true;
		if (index == 0)
			daySchedule.recreate (S_Resource_ID, date);
		else if (index == 1)
			weekSchedule.recreate (S_Resource_ID, date);
		else
			monthSchedule.recreate (S_Resource_ID, date);
		m_loading = false;
		repaint();
	}	//	displayCalendar

	/**************************************************************************
	 * 	Dispose.
	 */
	public void dispose()
	{
		daySchedule.dispose();
		weekSchedule.dispose();
		monthSchedule.dispose();
		this.removeAll();
		super.dispose();
	}	//	dispose

	/**
	 * 	Action Listener
	 * 	@param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (m_loading)
			return;
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
			dispose();
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
			dispose();
		//
		else if (e.getSource() == fieldResourceType)
		{
			fillResource();
			displayCalendar();
		}
		//
		else if (e.getSource() == fieldResource || e.getSource() == fieldDate)
			displayCalendar();
		//
		else if (e.getSource() == bPrevious)
			adjustDate(-1);
		else if (e.getSource() == bNext)
			adjustDate(+1);
		//
		this.setCursor(Cursor.getDefaultCursor());
	}	//	actionPerformed

	/**
	 * 	Change Listener (Tab Pane)
	 * 	@param e event
	 */
	public void stateChanged (ChangeEvent e)
	{
		displayCalendar();
	}	//	stateChanged

	/**
	 * 	Adjust Date
	 * 	@param diff difference
	 */
	private void adjustDate (int diff)
	{
		Timestamp date = fieldDate.getTimestamp();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		if (timePane.getSelectedIndex() == 0)
			cal.add(java.util.Calendar.DAY_OF_YEAR, diff);
		else if (timePane.getSelectedIndex() == 1)
			cal.add(java.util.Calendar.WEEK_OF_YEAR, diff);
		else
			cal.add(java.util.Calendar.MONTH, diff);
		//
		fieldDate.setValue(new Timestamp(cal.getTimeInMillis()));
		displayCalendar ();
	}	//	adjustDate

	/*************************************************************************/

	/**
	 * 	Callback.
	 * 	Called from VSchedulePanel after VAssignmentDialog finished
	 * 	@param assignment New/Changed Assignment
	 */
	public void mAssignmentCallback (MResourceAssignment assignment)
	{
		m_mAssignment = assignment;
		if (m_createNew)
			dispose();
		else
			displayCalendar();
	}	//	mAssignmentCallback

	/**
	 * 	Get Assignment
	 * 	@return Assignment
	 */
	public MResourceAssignment getMResourceAssignment()
	{
		return m_mAssignment;
	}	//	getMResourceAssignment



	/**
SELECT o.DocumentNo, ol.Line, ol.Description
FROM C_OrderLine ol, C_Order o
WHERE ol.S_ResourceAssignment_ID=1
  AND ol.C_Order_ID=o.C_Order_ID
UNION
SELECT i.DocumentNo, il.Line, il.Description
FROM C_InvoiceLine il, C_Invoice i
WHERE il.S_ResourceAssignment_ID=1
  AND il.C_Invoice_ID=i.C_Invoice_ID
UNION
SELECT e.DocumentNo, el.Line, el.Description
FROM S_TimeExpenseLine el, S_TimeExpense e
WHERE el.S_ResourceAssignment_ID=1
  AND el.S_TimeExpense_ID=el.S_TimeExpense_ID
	 */
}	//	InfoSchedule
