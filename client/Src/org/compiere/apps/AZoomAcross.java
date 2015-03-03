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
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Application Zoom Across Launcher.
 *  Called from APanel; Queries available Zoom Targets for Table.
 *	
 *  @author Jorg Janke
 *  @version $Id: AZoomAcross.java,v 1.9 2005/11/14 02:10:58 jjanke Exp $
 */
public class AZoomAcross implements ActionListener
{
	/**
	 *	Constructor
	 *  @param invoker component to display popup (optional)
	 *  @param tableName table name
	 *  @param query query
	 */
	public AZoomAcross (JComponent invoker, String tableName, MQuery query)
	{
		log.config("TableName=" + tableName + " - " + query);
		m_query = query;
		
		//	See What is there
		getZoomTargets (invoker, tableName);
	}	//	AReport

	/**	The Query						*/
	private MQuery	 	m_query;
	/**	The Popup						*/
	private JPopupMenu 	m_popup = new JPopupMenu("ZoomMenu");
	/**	The Option List					*/
	private ArrayList<KeyNamePair>	m_list = new ArrayList<KeyNamePair>();
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(AZoomAcross.class);

	/**
	 * 	Get the Zomm Targets for the table.
	 *  Fill the list and the popup menu
	 *  @param invoker component to display popup (optional)
	 * 	@param tableName table
	 */
	private void getZoomTargets (JComponent invoker, String tableName)
	{
		String sql = "SELECT DISTINCT ws.AD_Window_ID,ws.Name, wp.AD_Window_ID,wp.Name, t.TableName "
			+ "FROM AD_Table t ";
		boolean baseLanguage = Env.isBaseLanguage(Env.getCtx(), "AD_Window"); 
		if (baseLanguage)
			sql += "INNER JOIN AD_Window ws ON (t.AD_Window_ID=ws.AD_Window_ID)"
				+ " LEFT OUTER JOIN AD_Window wp ON (t.PO_Window_ID=wp.AD_Window_ID) ";
		else
			sql += "INNER JOIN AD_Window_Trl ws ON (t.AD_Window_ID=ws.AD_Window_ID AND ws.AD_Language=?)"
				+ " LEFT OUTER JOIN AD_Window_Trl wp ON (t.PO_Window_ID=wp.AD_Window_ID AND wp.AD_Language=?) ";
		//
		sql	+= "WHERE t.TableName NOT LIKE 'I%'"			//	No Import
			+ " AND EXISTS (SELECT * FROM AD_Tab tt " 		//	First Tab
				+ "WHERE (tt.AD_Window_ID=ws.AD_Window_ID OR tt.AD_Window_ID=wp.AD_Window_ID)"
				+ " AND tt.AD_Table_ID=t.AD_Table_ID AND tt.SeqNo=10)"
			+ " AND t.AD_Table_ID IN "
				+ "(SELECT AD_Table_ID FROM AD_Column "
				+ "WHERE ColumnName=? AND IsKey='N' AND IsParent='N') "	//	#x
			+ "ORDER BY 2";
		KeyNamePair pp = null;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			int index = 1;
			if (!baseLanguage)
			{
				pstmt.setString (index++, Env.getAD_Language(Env.getCtx()));
				pstmt.setString (index++, Env.getAD_Language(Env.getCtx()));
			}
			pstmt.setString (index++, tableName + "_ID");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int AD_Window_ID = rs.getInt(1);
				String Name = rs.getString(2);
				int PO_Window_ID = rs.getInt(3);
				String targetTableName = rs.getString(5);
				if (PO_Window_ID == 0)
					addTarget(targetTableName, AD_Window_ID, Name, null);
				else
					addTarget(targetTableName, AD_Window_ID, Name, Boolean.TRUE);
				//	PO
				if (PO_Window_ID != 0)
				{
					Name = rs.getString(4);
					addTarget(targetTableName, PO_Window_ID, Name, Boolean.FALSE);
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		//	No Zoom
		if (m_list.size() == 0)
		{
			ADialog.info(0, invoker, "NoZoomTarget");
			log.info("BaseLanguage=" + baseLanguage);
		}
		else if (invoker.isShowing())
			m_popup.show(invoker, 0, invoker.getHeight());	//	below button
	}	//	getZoomTargets

	/**
	 * 	Check Target and Add to popup
	 *	@param targetTableName table name
	 *	@param AD_Window_ID window
	 *	@param Name name
	 *	@param isSO has po/so Window
	 *	@return true if there is a record
	 */
	private boolean addTarget (String targetTableName, 
		int AD_Window_ID, String Name, Boolean isSO)
	{
		String sql = "SELECT COUNT(*) FROM " + targetTableName
			+ " WHERE " + m_query.getWhereClause(false);
		String sqlAdd = "";
		if (isSO != null)
			sqlAdd = " AND IsSOTrx=" + (isSO.booleanValue() ? "'Y'" : "'N'");
		int count = DB.getSQLValue(null, sql+sqlAdd);
		if (count < 0 && isSO != null)	//	error try again w/o SO
			DB.getSQLValue(null, sql);
		if (count <= 0)
			return false;
		//
		KeyNamePair pp = new KeyNamePair (AD_Window_ID, Name);
		m_list.add(pp);
		m_popup.add(pp.toString()).addActionListener(this);
		return true;
	}	//	checkTarget
	
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
				launchZoom (pp);
				return;
			}
		}
	}	//	actionPerformed
	
	/**
	 * 	Launch Zoom
	 *	@param pp KeyPair
	 */
	private void launchZoom (KeyNamePair pp)
	{
		int AD_Window_ID = pp.getKey();
		log.info("AD_Window_ID=" + AD_Window_ID 
			+ " - " + m_query); 
		AWindow frame = new AWindow();
		if (!frame.initWindow(AD_Window_ID, m_query))
			return;
		AEnv.showCenterScreen(frame);
		frame = null;
	}	//	launchZoom
	
}	//	AZoom
