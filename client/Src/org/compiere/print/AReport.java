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
package org.compiere.print;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Application Report Launcher.
 *  Called from APanel; Queries available Reports for Table.
 *  If no report available, a new one is created and launched.
 *  If there is one report, it is launched.
 *  If there are more, the available reports are displayed and the selected is launched.
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: AReport.java,v 1.19 2005/11/14 02:10:58 jjanke Exp $
 */
public class AReport implements ActionListener
{
	/**
	 *	Constructor
	 *
	 *  @param AD_Table_ID table
	 *  @param invoker component to display popup (optional)
	 *  @param query query
	 */
	public AReport (int AD_Table_ID, JComponent invoker, MQuery	query)
	{
		log.config("AD_Table_ID=" + AD_Table_ID + " " + query);
		if (!MRole.getDefault().isCanReport(AD_Table_ID))
		{
			ADialog.error(0, invoker, "AccessCannotReport", query.getTableName());
			return;
		}

		m_query = query;

		//	See What is there
		getPrintFormats (AD_Table_ID, invoker);
	}	//	AReport

	/**	The Query						*/
	private MQuery	 	m_query;
	/**	The Popup						*/
	private JPopupMenu 	m_popup = new JPopupMenu("ReportMenu");
	/**	The Option List					*/
	private ArrayList<KeyNamePair>	m_list = new ArrayList<KeyNamePair>();
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(AReport.class);

	/**
	 * 	Get the Print Formats for the table.
	 *  Fill the list and the popup menu
	 * 	@param AD_Table_ID table
	 *  @param invoker component to display popup (optional)
	 */
	private void getPrintFormats (int AD_Table_ID, JComponent invoker)
	{
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		//
		String sql = MRole.getDefault().addAccessSQL (
			"SELECT AD_PrintFormat_ID, Name, AD_Client_ID "
				+ "FROM AD_PrintFormat "
				+ "WHERE AD_Table_ID=? AND IsTableBased='Y' "
				+ "ORDER BY AD_Client_ID DESC, IsDefault DESC, Name",		//	Own First
			"AD_PrintFormat", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		KeyNamePair pp = null;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Table_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				pp = new KeyNamePair (rs.getInt(1), rs.getString(2));
				if (rs.getInt(3) == AD_Client_ID)
				{
					m_list.add(pp);
					m_popup.add(pp.toString()).addActionListener(this);
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		//	No Format exists - create it
		if (m_list.size() == 0)
		{
			if (pp == null)
				createNewFormat (AD_Table_ID);		//	calls launch
			else
				copyFormat(pp.getKey(), AD_Client_ID);
		}
		//	One Format exists or no invoker - show it
		else if (m_list.size() == 1 || invoker == null)
			launchReport ((KeyNamePair)m_list.get(0));
		//	Multiple Formats exist - show selection
		else if (invoker.isShowing())
			m_popup.show(invoker, 0, invoker.getHeight());	//	below button
	}	//	getPrintFormats

	/**
	 * 	Create and Launch new Format for table
	 * 	@param AD_Table_ID table
	 */
	private void createNewFormat (int AD_Table_ID)
	{
		MPrintFormat pf = MPrintFormat.createFromTable(Env.getCtx(), AD_Table_ID);
		launchReport (pf);
	}	//	createNewFormat

	/**
	 * 	Copy existing Format
	 * 	@param AD_PrintFormat_ID print format
	 * 	@param To_Client_ID to client
	 */
	private void copyFormat (int AD_PrintFormat_ID, int To_Client_ID)
	{
		MPrintFormat pf = MPrintFormat.copyToClient(Env.getCtx(), AD_PrintFormat_ID, To_Client_ID);
		launchReport (pf);
	}	//	copyFormatFromClient

	/**
	 * 	Launch Report
	 * 	@param pp Key=AD_PrintFormat_ID
	 */
	private void launchReport (KeyNamePair pp)
	{
		MPrintFormat pf = MPrintFormat.get(Env.getCtx(), pp.getKey(), false);
		launchReport (pf);
	}	//	launchReport

	/**
	 * 	Launch Report
	 * 	@param pf print format
	 */
	private void launchReport (MPrintFormat pf)
	{
		int Record_ID = 0;
		if (m_query.getRestrictionCount()==1 && m_query.getCode(0) instanceof Integer)
			Record_ID = ((Integer)m_query.getCode(0)).intValue();
		PrintInfo info = new PrintInfo(
			pf.getName(),
			pf.getAD_Table_ID(),
			Record_ID);
		info.setDescription(m_query.getInfo());
		ReportEngine re = new ReportEngine (Env.getCtx(), pf, m_query, info);
		new Viewer(re);
	//	if (m_popup.isVisible())
	//		m_popup.setVisible(false);
	}	//	launchReport

	/**
	 * 	Action Listener
	 * 	@param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		m_popup.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		String cmd = e.getActionCommand();
		for (int i = 0; i < m_list.size(); i++)
		{
			KeyNamePair pp = (KeyNamePair)m_list.get(i);
			if (cmd.equals(pp.getName()))
			{
				launchReport (pp);
				return;
			}
		}
	}	//	actionPerformed

	
	/**************************************************************************
	 * 	Get AD_Table_ID for Table Name
	 * 	@param TableName table name
	 * 	@return AD_Table_ID or 0
	 */
	static public int getAD_Table_ID (String TableName)
	{
		int AD_Table_ID = 0;
		String sql = "SELECT AD_Table_ID FROM AD_Table WHERE TableName=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, TableName);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				AD_Table_ID = rs.getInt(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		return AD_Table_ID;
	}	//	getAD_Table_ID

}	//	AReport

