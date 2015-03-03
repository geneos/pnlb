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
package org.compiere.install;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import javax.swing.*;
import org.compiere.apps.*;
import org.compiere.apps.form.*;
import org.compiere.swing.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Translation Dialog Import + Export.
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: TranslationDialog.java,v 1.10 2005/11/14 02:10:58 jjanke Exp $
 */
public class TranslationDialog extends CPanel
	implements FormPanel, ActionListener
{
	/**
	 *	TranslationDialog Constructor.
	 * 	(Initiated via init())
	 */
	public TranslationDialog()
	{
	}	//	TranslationDialog

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(TranslationDialog.class);
	//
	private GridBagLayout mainLayout = new GridBagLayout();
	private JComboBox cbLanguage = new JComboBox();
	private JLabel lLanguage = new JLabel();
	private JLabel lTable = new JLabel();
	private JComboBox cbTable = new JComboBox();
	private JButton bExport = new JButton();
	private JButton bImport = new JButton();
	//
	private StatusBar statusBar = new StatusBar();
	private JLabel lClient = new JLabel();
	private JComboBox cbClient = new JComboBox();


	/**
	 * 	Static Init
	 * 	@throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setLayout(mainLayout);
		lClient.setText(Msg.translate(Env.getCtx(), "AD_Client_ID"));
		lLanguage.setText(Msg.translate(Env.getCtx(), "AD_Language"));
		lLanguage.setToolTipText(Msg.translate(Env.getCtx(), "IsSystemLanguage"));
		lTable.setText(Msg.translate(Env.getCtx(), "AD_Table_ID"));
		//
		bExport.setText(Msg.getMsg(Env.getCtx(), "Export"));
		bExport.addActionListener(this);
		bImport.setText(Msg.getMsg(Env.getCtx(), "Import"));
		bImport.addActionListener(this);
		//
		this.add(cbLanguage,     new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(lLanguage,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(lTable,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(cbTable,     new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(bExport,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(bImport,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(lClient,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(cbClient,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	}	//	jbInit

	/**
	 * 	Dynamic Init.
	 * 	- fill Language & Table
	 */
	private void dynInit()
	{
		//	Fill Client
		cbClient.addItem(new KeyNamePair (-1, ""));
		String sql = "SELECT Name, AD_Client_ID "
			+ "FROM AD_Client "
			+ "WHERE IsActive='Y' "
			+ "ORDER BY 2";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				KeyNamePair kp = new KeyNamePair (rs.getInt(2), rs.getString(1));
				cbClient.addItem(kp);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		//	Fill Language
		sql = "SELECT Name, AD_Language "
			+ "FROM AD_Language "
			+ "WHERE IsActive='Y' AND (IsSystemLanguage='Y' OR IsBaseLanguage='Y')"
			+ "ORDER BY 1";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				ValueNamePair vp = new ValueNamePair (rs.getString(2), rs.getString(1));
				cbLanguage.addItem(vp);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		//	Fill Table
		cbTable.addItem(new ValueNamePair ("", ""));
		sql = "SELECT Name, TableName "
			+ "FROM AD_Table "
			+ "WHERE TableName LIKE '%_Trl' "
			+ "ORDER BY 1";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				ValueNamePair vp = new ValueNamePair (rs.getString(2), rs.getString(1));
				cbTable.addItem(vp);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		//	Info
		statusBar.setStatusLine(" ");
		statusBar.setStatusDB(" ");
	}	//	dynInit

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		Env.setContext(Env.getCtx(), m_WindowNo, "IsSOTrx", "Y");
		try
		{
			jbInit();
			dynInit();
			frame.getContentPane().add(this, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "", ex);
		}
	}	//	init

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		m_frame.dispose();
	}	//	dispose

	/*************************************************************************/

	/**
	 * 	Action Listener
	 * 	@param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		ValueNamePair AD_Language = (ValueNamePair)cbLanguage.getSelectedItem();
		if (AD_Language == null)
		{
			statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "LanguageSetupError"), true);
			return;
		}
		ValueNamePair AD_Table = (ValueNamePair)cbTable.getSelectedItem();
		if (AD_Table == null)
			return;
		boolean imp = (e.getSource() == bImport);
		KeyNamePair AD_Client = (KeyNamePair)cbClient.getSelectedItem();
		int AD_Client_ID = -1;
		if (AD_Client != null)
			AD_Client_ID = AD_Client.getKey();

		String startDir = Ini.getCompiereHome() + File.separator + "data";
		JFileChooser chooser = new JFileChooser(startDir);
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = imp ? chooser.showOpenDialog(this) : chooser.showSaveDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;
		String directory = chooser.getSelectedFile().getAbsolutePath();
		//
		statusBar.setStatusLine(directory);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Translation t = new Translation(Env.getCtx());
		String msg = t.validateLanguage(AD_Language.getValue());
		if (msg.length() > 0)
		{
			ADialog.error(m_WindowNo, this, "LanguageSetupError", msg);
			return;
		}

		//	All Tables
		if (AD_Table.getValue().equals(""))
		{
			for (int i = 1; i < cbTable.getItemCount(); i++)
			{
				AD_Table = (ValueNamePair)cbTable.getItemAt(i);
				msg = null;
				msg = imp
					? t.importTrl (directory, AD_Client_ID, AD_Language.getValue(), AD_Table.getValue())
					: t.exportTrl (directory, AD_Client_ID, AD_Language.getValue(), AD_Table.getValue());
				statusBar.setStatusLine(msg);
			}
			statusBar.setStatusLine(directory);
		}
		else	//	single table
		{
			msg = null;
			msg = imp
				? t.importTrl (directory, AD_Client_ID, AD_Language.getValue(), AD_Table.getValue())
				: t.exportTrl (directory, AD_Client_ID, AD_Language.getValue(), AD_Table.getValue());
			statusBar.setStatusLine(msg);
		}
		//
		this.setCursor(Cursor.getDefaultCursor());
	}	//	actionPerformed

}	//	Translation
