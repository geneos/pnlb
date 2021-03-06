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
package org.compiere.db;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;

import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *  Connection Dialog.
 *
 *  @author     Jorg Janke
 *  @author     Marek Mosiewicz<marek.mosiewicz@jotel.com.pl> - support for RMI over HTTP
 *  @version    $Id: CConnectionDialog.java,v 1.27 2005/12/27 06:20:13 jjanke Exp $
 */
public class CConnectionDialog extends CDialog implements ActionListener
{
	/**
	 *  Connection Dialog using current Connection
	 */
	public CConnectionDialog()
	{
		this (null);
	}   //  CConnectionDialog

	/**
	 *  Connection Dialog
	 *  @param cc Compiere Connection
	 */
	public CConnectionDialog(CConnection cc)
	{
		super((Frame)null, true);
		try
		{
			jbInit();
			setConnection (cc);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		CompierePLAF.showCenterScreen(this);
	}   //  CConnection

	/** Resources							*/
	private static ResourceBundle res = ResourceBundle.getBundle("org.compiere.db.DBRes");

	static
	{
		/** Connection Profiles					*/
		CConnection.CONNECTIONProfiles = new ValueNamePair[]{
			new ValueNamePair("L", res.getString("LAN")),
			new ValueNamePair("T", res.getString("TerminalServer")),
			new ValueNamePair("V", res.getString("VPN")),
			new ValueNamePair("W", res.getString("WAN"))
		};
	}
	
	/**	 Default HTTP Port					*/
	public static final String	APPS_PORT_HTTP = "80";
	/** Default RMI Port					*/
	public static final String	APPS_PORT_JNP = "1099";
	/** Connection							*/
	private CConnection 	m_cc = null;
	private CConnection 	m_ccResult = null;
	private boolean 		m_updating = false;
	private boolean     	m_saved = false;

	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (CConnectionDialog.class);

	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel centerPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	private CButton bOK = CompierePLAF.getOKButton();
	private CButton bCancel = CompierePLAF.getCancelButton();
	private FlowLayout southLayout = new FlowLayout();
	private GridBagLayout centerLayout = new GridBagLayout();
	private CLabel nameLabel = new CLabel();
	private CTextField nameField = new CTextField();
	private CLabel hostLabel = new CLabel();
	private CTextField hostField = new CTextField();
	private CLabel portLabel = new CLabel();
	private CTextField dbPortField = new CTextField();
	private CLabel sidLabel = new CLabel();
	private CTextField sidField = new CTextField();
	private CCheckBox cbFirewall = new CCheckBox();
	private CLabel fwHostLabel = new CLabel();
	private CTextField fwHostField = new CTextField();
	private CLabel fwPortLabel = new CLabel();
	private CTextField fwPortField = new CTextField();
	private CButton bTestDB = new CButton();
	private CLabel dbTypeLabel = new CLabel();
	private CComboBox dbTypeField = new CComboBox(Database.DB_NAMES);
	private CCheckBox cbBequeath = new CCheckBox();
	private CLabel appsHostLabel = new CLabel();
	private CTextField appsHostField = new CTextField();
	private CLabel appsPortLabel = new CLabel();
	private CTextField appsPortField = new CTextField();
	private CButton bTestApps = new CButton();
	private CCheckBox cbOverwrite = new CCheckBox();
	private CLabel dbUidLabel = new CLabel();
	private CTextField dbUidField = new CTextField();
	private JPasswordField dbPwdField = new JPasswordField();
	private CLabel connectionProfileLabel = new CLabel();
	private CComboBox connectionProfileField = new CComboBox(CConnection.CONNECTIONProfiles);


	/**
	 *  Static Layout
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setTitle(res.getString("CConnectionDialog"));
		mainPanel.setLayout(mainLayout);
		southPanel.setLayout(southLayout);
		southLayout.setAlignment(FlowLayout.RIGHT);
		centerPanel.setLayout(centerLayout);
		nameLabel.setText(res.getString("Name"));
		nameField.setColumns(30);
		nameField.setReadWrite(false);
		hostLabel.setText(res.getString("DBHost"));
		hostField.setColumns(30);
		portLabel.setText(res.getString("DBPort"));
		dbPortField.setColumns(10);
		sidLabel.setText(res.getString("DBName"));
		cbFirewall.setToolTipText("");
		cbFirewall.setText(res.getString("ViaFirewall"));
		fwHostLabel.setText(res.getString("FWHost"));
		fwHostField.setColumns(30);
		fwPortLabel.setText(res.getString("FWPort"));
		bTestDB.setText(res.getString("TestConnection"));
		bTestDB.setHorizontalAlignment(JLabel.LEFT);
		dbTypeLabel.setText(res.getString("Type"));
		sidField.setColumns(30);
		fwPortField.setColumns(10);
		cbBequeath.setText(res.getString("BequeathConnection"));
		appsHostLabel.setText(res.getString("AppsHost"));
		appsHostField.setColumns(30);
		appsPortLabel.setText(res.getString("AppsPort"));
		appsPortField.setColumns(10);
		bTestApps.setText(res.getString("TestApps"));
		bTestApps.setHorizontalAlignment(JLabel.LEFT);
		cbOverwrite.setText(res.getString("Overwrite"));
		dbUidLabel.setText(res.getString("DBUidPwd"));
		dbUidField.setColumns(10);
		connectionProfileLabel.setText(res.getString("ConnectionProfile"));
		connectionProfileField.addActionListener(this);
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		southPanel.add(bCancel, null);
		southPanel.add(bOK, null);
		//
		centerPanel.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		centerPanel.add(nameField,  new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 12), 0, 0));
		centerPanel.add(appsHostLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 5), 0, 0));
		centerPanel.add(appsHostField, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 12), 0, 0));
		
		centerPanel.add(appsPortLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(appsPortField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		centerPanel.add(connectionProfileLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 5), 0, 0));
		centerPanel.add(connectionProfileField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
		//
		centerPanel.add(bTestApps, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 12, 0), 0, 0));
		centerPanel.add(cbOverwrite, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 5, 0, 12), 0, 0));
		//	DB
		centerPanel.add(dbTypeLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 5), 0, 0));
		centerPanel.add(dbTypeField, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
		centerPanel.add(cbBequeath, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 12), 0, 0));
		centerPanel.add(hostLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 5), 0, 0));
		centerPanel.add(hostField,  new GridBagConstraints(1, 6, 2, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 12), 0, 0));
		centerPanel.add(portLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(dbPortField, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
		centerPanel.add(sidLabel,  new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(sidField,   new GridBagConstraints(1, 8, 2, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
		centerPanel.add(dbUidLabel, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(dbUidField, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
		centerPanel.add(dbPwdField, new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 12), 0, 0));
		centerPanel.add(cbFirewall, new GridBagConstraints(1, 10, 2, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 12), 0, 0));
		centerPanel.add(fwHostLabel, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(fwHostField,  new GridBagConstraints(1, 11, 2, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
		centerPanel.add(fwPortLabel, new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(fwPortField, new GridBagConstraints(1, 12, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
		centerPanel.add(bTestDB,  new GridBagConstraints(1, 13, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 12, 0), 0, 0));
		//
		nameField.addActionListener(this);
		appsHostField.addActionListener(this);
		appsPortField.addActionListener(this);
		cbOverwrite.addActionListener(this);
		bTestApps.addActionListener(this);
		//
		dbTypeField.addActionListener(this);
		hostField.addActionListener(this);
		dbPortField.addActionListener(this);
		sidField.addActionListener(this);
		cbBequeath.addActionListener(this);
		cbFirewall.addActionListener(this);
		fwHostField.addActionListener(this);
		fwPortField.addActionListener(this);
		bTestDB.addActionListener(this);
		bOK.addActionListener(this);
		bCancel.addActionListener(this);

		//	Server
		if (!Ini.isClient())
		{
			appsHostLabel.setVisible(false);
			appsHostField.setVisible(false);
			appsPortLabel.setVisible(false);
			appsPortField.setVisible(false);
			bTestApps.setVisible(false);
			connectionProfileLabel.setVisible(false);
			connectionProfileField.setVisible(false);
		}
		else	//	Client
			cbBequeath.setVisible(false);
	}   //  jbInit

	/**
	 *  Set Busy - lock UI
	 *  @param busy busy
	 */
	private void setBusy (boolean busy)
	{
		if (busy)
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		else
			this.setCursor(Cursor.getDefaultCursor());
		m_updating = busy;
	}   //  setBusy

	/**
	 *  Set Connection
	 *  @param cc - if null use current connection
	 */
	public void setConnection (CConnection cc)
	{
		m_cc = cc;
		if (m_cc == null)
		{
			m_cc = CConnection.get();
			m_cc.setName();
		}
		//	Should copy values
		m_ccResult = m_cc;
		//
		String type = m_cc.getType();
		if (type == null || type.length() == 0)
			dbTypeField.setSelectedItem(null);
		else
			m_cc.setType(m_cc.getType());   //  sets defaults
		updateInfo();
	}   //  setConnection

	/**
	 *  Get Connection
	 *  @return CConnection
	 */
	public CConnection getConnection()
	{
		return m_ccResult;
	}   //  getConnection;

	/**
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (m_updating)
			return;
		Object src = e.getSource();

		if (src == bOK)
		{
			m_cc.setName();
			m_ccResult = m_cc;
			dispose();
			return;
		}
		else if (src == bCancel)
		{
			m_cc.setName();
			dispose();
			return;
		}
		else if (src == connectionProfileField)
		{
			ValueNamePair pp = (ValueNamePair)connectionProfileField.getSelectedItem();
			m_cc.setConnectionProfile(pp.getValue());
			if (m_cc.isRMIoverHTTP())
				appsPortField.setText(APPS_PORT_HTTP);
			else
				appsPortField.setText(APPS_PORT_JNP);
			return;
		}
		else if (src == dbTypeField)
		{
			if (dbTypeField.getSelectedItem() == null)
				return;
		}


		if (Ini.isClient())
		{
			m_cc.setAppsHost(appsHostField.getText());
			m_cc.setAppsPort(appsPortField.getText());
		}
		else
			m_cc.setAppsHost("localhost");
		//
		ValueNamePair pp = (ValueNamePair)connectionProfileField.getSelectedItem();
		m_cc.setConnectionProfile(pp.getValue());
		//
		m_cc.setType((String)dbTypeField.getSelectedItem());
		m_cc.setDbHost(hostField.getText());
		m_cc.setDbPort(dbPortField.getText());
		m_cc.setDbName(sidField.getText());
		m_cc.setDbUid(dbUidField.getText());
		m_cc.setDbPwd(String.valueOf(dbPwdField.getPassword()));
		m_cc.setBequeath(cbBequeath.isSelected());
		m_cc.setViaFirewall(cbFirewall.isSelected());
		m_cc.setFwHost(fwHostField.getText());
		m_cc.setFwPort(fwPortField.getText());
		//
		if (src == bTestApps)
			cmd_testApps();

		//  Database Selection Changed
		else if (src == dbTypeField)
		{
			m_cc.setType((String)dbTypeField.getSelectedItem());
			dbPortField.setText(String.valueOf(m_cc.getDbPort()));
			cbBequeath.setSelected(m_cc.isBequeath());
			fwPortField.setText(String.valueOf(m_cc.getFwPort()));
		}
		//
		else if (src == bTestDB)
			cmd_testDB();

		//  Name
		if (src == nameField)
			m_cc.setName(nameField.getText());

		updateInfo();
	}   //  actionPerformed

	/**
	 *  Update Fields from Connection
	 */
	private void updateInfo()
	{
		m_updating = true;
		nameField.setText(m_cc.getName());
		appsHostField.setText(m_cc.getAppsHost());
		appsPortField.setText(String.valueOf(m_cc.getAppsPort()));
		//
		String cp = m_cc.getConnectionProfile();
		ValueNamePair cpPP = null;
		for (int i = 0; i < CConnection.CONNECTIONProfiles.length; i++)
		{
			if (cp.equals(CConnection.CONNECTIONProfiles[i].getValue()))
			{
				cpPP = CConnection.CONNECTIONProfiles[i];
				break;
			}
		}
		if (cpPP == null)	//	LAN
			cpPP = CConnection.CONNECTIONProfiles[0];
		connectionProfileField.setSelectedItem(cpPP);
		bTestApps.setIcon(getStatusIcon(m_cc.isAppsServerOK(false)));
	//	bTestApps.setToolTipText(m_cc.getRmiUri());

		cbOverwrite.setVisible(m_cc.isAppsServerOK(false));
		boolean rw = cbOverwrite.isSelected() || !m_cc.isAppsServerOK(false);
		//
		dbTypeLabel.setReadWrite(rw);
		dbTypeField.setReadWrite(rw);
		dbTypeField.setSelectedItem(m_cc.getType());
		//
		hostLabel.setReadWrite(rw);
		hostField.setReadWrite(rw);
		hostField.setText(m_cc.getDbHost());
		portLabel.setReadWrite(rw);
		dbPortField.setReadWrite(rw);
		dbPortField.setText(String.valueOf(m_cc.getDbPort()));
		sidLabel.setReadWrite(rw);
		sidField.setReadWrite(rw);
		sidField.setText(m_cc.getDbName());
		//
		dbUidLabel.setReadWrite(rw);
		dbUidField.setReadWrite(rw);
		dbUidField.setText(m_cc.getDbUid());
		dbPwdField.setEditable(rw);
		dbPwdField.setText(m_cc.getDbPwd());
		//
		cbBequeath.setReadWrite(rw);
		cbBequeath.setEnabled(m_cc.isOracle());
		cbBequeath.setSelected(m_cc.isBequeath());
		//
		boolean fwEnabled = rw && m_cc.isViaFirewall() && m_cc.isOracle();
		cbFirewall.setReadWrite(rw && m_cc.isOracle());
		cbFirewall.setSelected(m_cc.isViaFirewall());
		fwHostLabel.setReadWrite(fwEnabled);
		fwHostField.setReadWrite(fwEnabled);
		fwHostField.setText(m_cc.getFwHost());
		fwPortLabel.setReadWrite(fwEnabled);
		fwPortField.setReadWrite(fwEnabled);
		fwPortField.setText(String.valueOf(m_cc.getFwPort()));
		//
		bTestDB.setToolTipText(m_cc.getConnectionURL());
		bTestDB.setIcon(getStatusIcon(m_cc.isDatabaseOK()));
		m_updating = false;
	}   //  updateInfo

	/**
	 *  Get Status Icon - ok or not
	 *  @param ok ok
	 *  @return Icon
	 */
	private Icon getStatusIcon (boolean ok)
	{
		if (ok)
			return bOK.getIcon();
		else
			return bCancel.getIcon();
	}   //  getStatusIcon

	/**
	 *  Test Database connection
	 */
	private void cmd_testDB()
	{
		setBusy (true);
		Exception e = m_cc.testDatabase(true);
		if (e != null)
		{
			JOptionPane.showMessageDialog(this,
				e,		//	message
				res.getString("ConnectionError") + ": " + m_cc.getConnectionURL(),
				JOptionPane.ERROR_MESSAGE);
		}
		setBusy (false);
	}   //  cmd_testDB

	/**
	 *  Test Application connection
	 */
	private void cmd_testApps()
	{
		setBusy (true);
		Exception e = m_cc.testAppsServer();
		if (e != null)
		{
			JOptionPane.showMessageDialog(this,
				e.getLocalizedMessage(),
				res.getString("ServerNotActive") + " - " + m_cc.getAppsHost(),
				JOptionPane.ERROR_MESSAGE);
		}
		setBusy (false);
	}   //  cmd_testApps

}   //  CConnectionDialog
