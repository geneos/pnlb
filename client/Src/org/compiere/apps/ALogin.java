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
import javax.swing.*;
import javax.swing.event.*;
import org.compiere.*;
import org.compiere.db.*;
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.print.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Application Login Window
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: ALogin.java,v 1.74 2005/12/27 06:18:37 jjanke Exp $
 */
public final class ALogin extends CDialog
	implements ActionListener, ChangeListener
{
	/**
	 *	Construct the dialog.
	 *	Need to call initLogin for dynamic start
	 *  @param parent parent
	 */
	public ALogin (Frame parent)
	{
		super (parent, "Login", true);	//	Modal
		log.finer("");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		m_WindowNo = Env.createWindowNo (null);
		res = ResourceBundle.getBundle(RESOURCE);
		//
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			log.severe(e.toString());
		}
		//  Focus to OK
		this.getRootPane().setDefaultButton(confirmPanel.getOKButton());
		parent.setIconImage(Compiere.getImage16());
	}   //  ALogin


	protected static final String RESOURCE = "org.compiere.apps.ALoginRes";
	private static ResourceBundle res = ResourceBundle.getBundle(RESOURCE);
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ALogin.class);

	private CPanel mainPanel = new CPanel(new BorderLayout());
	private CTabbedPane loginTabPane = new CTabbedPane();
	private BorderLayout conTabLayout = new BorderLayout();
	private CPanel connectionPanel = new CPanel();
	private CLabel hostLabel = new CLabel();
	private CConnectionEditor hostField = new CConnectionEditor();
	private CLabel userLabel = new CLabel();
	private CTextField userTextField = new CTextField();
	private CLabel passwordLabel = new CLabel();
	private JPasswordField passwordField = new JPasswordField();
	private CPanel defaultPanel = new CPanel();
	private BorderLayout defaultLayout = new BorderLayout();
	private CLabel clientLabel = new CLabel();
	private CLabel orgLabel = new CLabel();
	private CLabel dateLabel = new CLabel();
	private VDate dateField = new VDate(DisplayType.Date);
	private VComboBox orgCombo = new VComboBox();
	private VComboBox clientCombo = new VComboBox();
	private CLabel warehouseLabel = new CLabel();
	private VComboBox warehouseCombo = new VComboBox();
	private CLabel printerLabel = new CLabel();
	private CPrinter printerField = new CPrinter();
	private CLabel roleLabel = new CLabel();
	private VComboBox roleCombo = new VComboBox();
	private CLabel copy0Label = new CLabel();
	private CLabel titleLabel = new CLabel();
	private CLabel versionLabel = new CLabel();
	private CLabel copy1Label = new CLabel();
	private GridBagLayout connectionLayout = new GridBagLayout();
	private GridBagLayout defaultPanelLayout = new GridBagLayout();
	private CLabel languageLabel = new CLabel();
	private VComboBox languageCombo = new VComboBox(Language.getNames());
	private CLabel compileDate = new CLabel();
	private CPanel southPanel = new CPanel();
	private BorderLayout southLayout = new BorderLayout();
	private StatusBar statusBar = new StatusBar();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true, false, false, false, false, false, false);
	private OnlineHelp onlineHelp = new OnlineHelp(true);
	private CPanel helpPanel = new CPanel();
	private JScrollPane helpScollPane = new JScrollPane();
	private BorderLayout helpLayout = new BorderLayout();

	/** Server Connection       */
	private CConnection 	m_cc;
	/** Application User        */
	private String 			m_user;
	/** Application Password    */
	private String 			m_pwd;
	
	/**	Combo Active			*/
	private boolean			m_comboActive = false;
	/**	Combo Active			*/
	private boolean			m_okPressed = false;
	/**	Connection OK			*/
	private boolean		    m_connectionOK = false;
	/**	Window No				*/
	private int			    m_WindowNo;
	/** Context					*/
	private Properties      m_ctx = Env.getCtx();
	
	private Login			m_login = null;

	
	/**************************************************************************
	 *	Component initialization
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setName("Login");
		titleLabel.setFont(new java.awt.Font("Serif", 2, 10));
		titleLabel.setForeground(Color.blue);
		titleLabel.setRequestFocusEnabled(false);
		titleLabel.setToolTipText(Compiere.getURL());
		titleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		/** Removing/modifying the Compiere logo is a violation of the license	*/
		titleLabel.setIcon(Compiere.getImageIconLogo());
		titleLabel.setText(Compiere.getSubtitle());
		titleLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		versionLabel.setRequestFocusEnabled(false);
		versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		versionLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		hostLabel.setRequestFocusEnabled(false);
		hostLabel.setLabelFor(hostField);
		hostField.addActionListener(this);
		userLabel.setRequestFocusEnabled(false);
		userLabel.setLabelFor(userTextField);
		passwordLabel.setRequestFocusEnabled(false);
		passwordLabel.setLabelFor(passwordField);
		languageLabel.setLabelFor(languageCombo);
		copy0Label.setFont(new java.awt.Font("Serif", 2, 10));
		copy0Label.setForeground(Color.blue);
		copy0Label.setRequestFocusEnabled(false);
		copy1Label.setRequestFocusEnabled(false);
		roleLabel.setRequestFocusEnabled(false);
		roleLabel.setLabelFor(roleCombo);
		clientLabel.setRequestFocusEnabled(false);
		orgLabel.setRequestFocusEnabled(false);
		dateLabel.setRequestFocusEnabled(false);
		warehouseLabel.setRequestFocusEnabled(false);
		printerLabel.setRequestFocusEnabled(false);
		compileDate.setHorizontalAlignment(SwingConstants.RIGHT);
		compileDate.setHorizontalTextPosition(SwingConstants.RIGHT);
		compileDate.setText(Compiere.DATE_VERSION);
		compileDate.setToolTipText(Compiere.getImplementationVendor());
		southPanel.setLayout(southLayout);
		loginTabPane.addChangeListener(this);

		//	ConnectionTab
		connectionPanel.setLayout(connectionLayout);
		//
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		versionLabel.setText(Compiere.MAIN_VERSION);
		versionLabel.setToolTipText(Compiere.getImplementationVersion());
		hostLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		hostLabel.setText("Host");
		connectionPanel.add(hostLabel,         new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 5), 0, 0));
		connectionPanel.add(hostField,           new GridBagConstraints(1, 2, 3, 1, 1.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 12), 0, 0));
		userLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		userLabel.setText("User");
		connectionPanel.add(userLabel,        new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 5), 0, 0));
		userTextField.setText("System");			//	default
		connectionPanel.add(userTextField,         new GridBagConstraints(1, 3, 3, 1, 1.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 12), 0, 0));
		passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		passwordLabel.setText("Password");
		connectionPanel.add(passwordLabel,         new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		passwordField.setText("System");			//	default
		connectionPanel.add(passwordField,         new GridBagConstraints(1, 4, 3, 1, 1.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
		languageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		languageLabel.setText("Language");
		connectionPanel.add(languageLabel,        new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 5), 0, 0));
		languageCombo.addActionListener(this);
		connectionPanel.add(languageCombo,          new GridBagConstraints(1, 5, 3, 1, 1.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 12), 0, 0));
		copy0Label.setHorizontalAlignment(SwingConstants.RIGHT);
		connectionPanel.add(copy0Label,       new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		/** Removing/modifying the Compiere copyright notice is a violation of the license	*/
		copy1Label.setText(Compiere.COPYRIGHT);
		connectionPanel.add(copy1Label,         new GridBagConstraints(1, 6, 2, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 12, 12), 0, 0));
		connectionPanel.add(compileDate,        new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0
			,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(2, 0, 0, 12), 0, 0));
		connectionPanel.add(titleLabel,         new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		connectionPanel.add(versionLabel,        new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 5, 0, 12), 0, 0));

        /**
         * Listen for changes in the text, enabling to add CConnectionEditor userName and check it  
         * 
         * @author Ezequiel Scott at Zynnia
         */
        userTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                hostField.setUserName(userTextField.getText());
            }
        });
        
		loginTabPane.add(connectionPanel, res.getString("Connection"));

		//	DefaultTab
		defaultPanel.setLayout(defaultPanelLayout);
		//
		roleLabel.setText("Role");
		roleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		roleCombo.addActionListener(this);
		defaultPanel.add(roleLabel,        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 5, 5), 0, 0));
		defaultPanel.add(roleCombo,        new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 5, 12), 0, 0));
		clientLabel.setText("Client");
		clientLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		defaultPanel.add(clientLabel,       new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 5), 0, 0));
		clientCombo.addActionListener(this);
		defaultPanel.add(clientCombo,        new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 12), 0, 0));
		orgLabel.setText("Organization");
		orgLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		defaultPanel.add(orgLabel,         new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		orgCombo.addActionListener(this);
		defaultPanel.add(orgCombo,        new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
		dateLabel.setText("Date");
		dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		defaultPanel.add(dateLabel,       new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 5), 0, 0));
		defaultPanel.add(dateField,        new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 12), 0, 0));
		//
		warehouseLabel.setText("Warehouse");
		warehouseLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		printerLabel.setText("Printer");
		printerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		defaultPanel.add(printerLabel,        new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 12, 5), 0, 0));
		defaultPanel.add(printerField,        new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 12), 0, 0));
		defaultPanel.add(warehouseLabel,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 5), 0, 0));
		defaultPanel.add(warehouseCombo,   new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 12), 0, 0));
		//
		loginTabPane.add(defaultPanel, res.getString("Defaults"));

		//  Help
		helpPanel.setLayout(helpLayout);
		helpPanel.setPreferredSize(new Dimension (100,100));
		helpPanel.add(helpScollPane,  BorderLayout.CENTER);
		loginTabPane.add(helpPanel,  "?");
		//
		this.getContentPane().add(mainPanel);
		mainPanel.add(loginTabPane, BorderLayout.CENTER);
		mainPanel.setName("loginMainPanel");
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		//
		southPanel.add(confirmPanel, BorderLayout.NORTH);
		southPanel.add(statusBar, BorderLayout.SOUTH);
		helpScollPane.getViewport().add(onlineHelp, null);
		confirmPanel.addActionListener(this);
		statusBar.setStatusDB(null);
	} 	//	jbInit

	/**
	 *	Set Initial & Ini Parameters
	 *	Optional Automatic login
	 *	@return true, if connected & parameters set
	 */
	public boolean initLogin()
	{
		m_cc = CConnection.get(Compiere.getCodeBaseHost());
		hostField.setValue(m_cc);
		validateConnection ();

		//  Application/PWD
		userTextField.setText(Ini.getProperty(Ini.P_UID));
        if (Ini.isPropertyBool(Ini.P_STORE_PWD))
			passwordField.setText(Ini.getProperty(Ini.P_PWD));
		else
			passwordField.setText("");
        //
		languageCombo.setSelectedItem(Ini.getProperty(Ini.P_LANGUAGE));

		//	AutoLogin - assumes that connection is OK
		if (Ini.isPropertyBool(Ini.P_A_LOGIN))
		{
			connectionOK ();
			defaultsOK ();
			if (m_connectionOK)		//	simulate
				m_okPressed = true;
			return m_connectionOK;
		}
		return false;
	}	//	initLogin

	/**
	 *  Window Events - requestFocus
	 *  @param e event
	 */
	protected void processWindowEvent(WindowEvent e)
	{
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_OPENED)
		{
			this.toFront();
			confirmPanel.getOKButton().requestFocusInWindow();
		}
	}   //  processWindowEvent

	/**
	 *  Validate Connection
	 */
	private void validateConnection()
	{
		m_connectionOK = false;
		//
		m_cc.testAppsServer();
		m_cc.testDatabase(false);
		//
		hostField.setDisplay();
	}   //  validateConnection
	
	
	/*************************************************************************
	 *	Exit action performed
	 */
	private void appExit()
	{
		m_connectionOK = false;
		dispose();
	}	//	appExit_actionPerformed


	/**
	 * 	Return true, if logged in
	 *  @return true if connected
	 */
	public boolean isConnected()
	{
		return m_connectionOK;
	}	//	isConnected

	/**
	 * 	Did the user press OK
	 *	@return true if user pressed final OK button
	 */
	public boolean isOKpressed()
	{
		return m_okPressed;
	}	//	isOKpressed
	
	/**************************************************************************
	 *	Action Event handler
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			if (loginTabPane.getSelectedIndex() == 0)
				connectionOK();		//	first ok
			else
			{
				m_okPressed = true;
				defaultsOK();	//	disposes
			}
		}
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
			appExit();
		//
		else if (e.getSource() == hostField)
			validateConnection();
		else if (e.getSource() == languageCombo)
			languageComboChanged();
		//
		else if (e.getSource() == roleCombo)
			roleComboChanged();
		else if (e.getSource() == clientCombo)
			clientComboChanged();
		else if (e.getSource() == orgCombo)
			orgComboChanged();
	}	//	actionPerformed


	/**************************************************************************
	 *	Connection OK pressed
	 */
	private void connectionOK ()
	{
		log.info("");
		//
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		confirmPanel.getOKButton().setEnabled(false);

		m_connectionOK = tryConnection();

		if (m_connectionOK)			
		{
			//  Verify Language & Load Msg
			Language l = Language.getLoginLanguage();
			Env.verifyLanguage (m_ctx, l);
			Env.setContext(m_ctx, Env.LANGUAGE, l.getAD_Language());
			Msg.getMsg(m_ctx, "0");
			
			//	Migration
			MSystem system = MSystem.get(m_ctx);
			if (system.isJustMigrated())
			{
				statusBar.setStatusLine("Running: After Migration ....", true);
				ADialog.info (m_WindowNo, this, "AfterMigration");
				Thread.yield();
				DB.afterMigration(m_ctx);
			}
			//	Set Defaults
			printerField.setValue(Ini.getProperty(Ini.P_PRINTER));
			//	Change Tab to Default
			loginTabPane.setSelectedIndex(1);
		}

		confirmPanel.getOKButton().setEnabled(true);
		setCursor(Cursor.getDefaultCursor());
	}	//	connectionOK


	/**
	 *	Change of tab					<->
	 *  @param e event
	 */
	public void stateChanged(ChangeEvent e)
	{
		if (loginTabPane.getSelectedIndex() == 2)   //  allow access to help
			return;

		if (!(String.valueOf(passwordField.getPassword()).equals(m_pwd)
			&& userTextField.getText().equals(m_user)))
			m_connectionOK = false;
		//
		if (m_connectionOK)
			statusBar.setStatusLine(txt_LoggedIn);
		else
		{
			statusBar.setStatusLine(txt_NotConnected, true);
			loginTabPane.setSelectedIndex(0);
		}
		confirmPanel.getOKButton().requestFocus();
	}	//	loginTabPane


	/**************************************************************************
	 *	Defaults OK pressed
	 *	@return true if ok
	 */
	private boolean defaultsOK ()
	{
		log.info("");

		KeyNamePair org = (KeyNamePair)orgCombo.getSelectedItem();
		if (org == null)
			return false;

		//	Set Properties
		Ini.setProperty(Ini.P_CONNECTION, CConnection.get().toStringLong());
		Ini.setProperty(Ini.P_LANGUAGE, (String)languageCombo.getSelectedItem());

		String error = m_login.validateLogin(org);
		if (error != null && error.length() > 0)
		{
			ADialog.info(m_WindowNo, this, error);
			appExit();
			return false;
		}
		
		//  Load Properties and save Ini values
		statusBar.setStatusLine("Loading Preferences");
		String msg = m_login.loadPreferences (org,
			(KeyNamePair)warehouseCombo.getSelectedItem(),
			dateField.getTimestamp(), printerField.getDisplay());
		if (msg.length() > 0)
			ADialog.info(m_WindowNo, this, msg);

		//	Check Apps Server - DB Checked in Menu
		checkVersion();			//	exits if conflict

		//  Close - we are done
		if (m_connectionOK)
			this.dispose();
		return m_connectionOK;
	}	//	defaultsOK


	/**************************************************************************
	 *	Try to connect.
	 *  - Get Connection
	 *  - Compare User info
	 *  @return true if connected
	 */
	private boolean tryConnection()
	{
		m_user = userTextField.getText();
		m_pwd = new String (passwordField.getPassword());

		//	Establish connection
		DB.setDBTarget(CConnection.get());
		if (!DB.isConnected())
		{
			statusBar.setStatusLine(txt_NoDatabase, true);
			hostField.setBackground(CompierePLAF.getFieldBackground_Error());
			return false;
		}
		
		//	Reference check
		Ini.setProperty(Ini.P_COMPIERESYS, "Reference".equalsIgnoreCase(CConnection.get().getDbUid()));

		//  Get Roles
		m_login = new Login(m_ctx);
		KeyNamePair[] roles = m_login.getRoles(m_user, m_pwd);
		if (roles == null || roles.length == 0)
		{
			statusBar.setStatusLine(txt_UserPwdError, true);
			userTextField.setBackground(CompierePLAF.getFieldBackground_Error());
			passwordField.setBackground(CompierePLAF.getFieldBackground_Error());
			return false;
		}
		
		//	Delete existing role items
		m_comboActive = true;
		if (roleCombo.getItemCount() > 0)
			roleCombo.removeAllItems();

		//  Initial role
		KeyNamePair iniValue = null;
		String iniDefault = Ini.getProperty(Ini.P_ROLE);

		//  fill roles
		for (int i = 0; i < roles.length; i++)
		{
			roleCombo.addItem(roles[i]);
			if (roles[i].getName().equals(iniDefault))
				iniValue = roles[i];
		}
		if (iniValue != null)
			roleCombo.setSelectedItem(iniValue);

		userTextField.setBackground(CompierePLAF.getFieldBackground_Normal());
		passwordField.setBackground(CompierePLAF.getFieldBackground_Normal());
		//
		statusBar.setStatusLine(txt_LoggedIn);
		m_comboActive = false;
		roleComboChanged();
		return true;
	}	//	tryConnection


	/**
	 *	Role changed - fill Client List
	 */
	private void roleComboChanged ()
	{
		KeyNamePair role = (KeyNamePair)roleCombo.getSelectedItem();
		if (role == null || m_comboActive)
			return;
		log.config(": " + role);
		m_comboActive = true;
		//
		KeyNamePair[] clients = m_login.getClients(role);
		//  delete existing client/org items
		if (clientCombo.getItemCount() > 0)
			clientCombo.removeAllItems();
		if (orgCombo.getItemCount() > 0)
			orgCombo.removeAllItems();
		//  No Clients
		if (clients == null || clients.length == 0)
		{
			statusBar.setStatusLine(txt_RoleError, true);
			m_comboActive = false;
			return;
		}
		//  initial client
		KeyNamePair iniValue = null;
		String iniDefault = Ini.getProperty(Ini.P_CLIENT);

		//  fill clients
		for (int i = 0; i < clients.length; i++)
		{
			clientCombo.addItem(clients[i]);
			if (clients[i].getName().equals(iniDefault))
				iniValue = clients[i];
		}
		//	fini
		if (iniValue != null)
			clientCombo.setSelectedItem(iniValue);
		//
		m_comboActive = false;
		clientComboChanged();
	}	//	roleComboChanged


	/**
	 *	Client changed - fill Org & Warehouse List
	 */
	private void clientComboChanged()
	{
		KeyNamePair client = (KeyNamePair)clientCombo.getSelectedItem();
		if (client == null || m_comboActive)
			return;
		log.config(": " + client);
		m_comboActive = true;
		//
		KeyNamePair[] orgs = m_login.getOrgs(client);
		//  delete existing cleint items
		if (orgCombo.getItemCount() > 0)
			orgCombo.removeAllItems();

		//  No Orgs
		if (orgs == null || orgs.length == 0)
		{
			statusBar.setStatusLine(txt_RoleError, true);
			m_comboActive = false;
			return;
		}
		//  initial client
		KeyNamePair orgValue = null;
		KeyNamePair orgValue2 = null;
		String iniDefault = Ini.getProperty(Ini.P_ORG);

		//  fill orgs
		for (int i = 0; i < orgs.length; i++)
		{
			orgCombo.addItem(orgs[i]);
			if (orgs[i].getName().equals(iniDefault))
				orgValue = orgs[i];
			if (orgValue2 == null && orgs[i].getKey() != 0)
				orgValue2 = orgs[i];	//	first non-0 org
		}
		//	Non-0 Org exists and last login was with 0
		if (orgValue2 != null && orgValue != null && orgValue.getKey() == 0)
			orgValue = orgValue2;
		//	Last Org
		if (orgValue != null)
			orgCombo.setSelectedItem(orgValue);
		//	Get first Org
		else
			orgValue = (KeyNamePair)orgCombo.getSelectedItem();
		//
		m_comboActive = false;
		orgComboChanged();
	}	//	clientComboChanged

	/**
	 *	Org changed - fill Warehouse List
	 */
	private void orgComboChanged()
	{
		KeyNamePair org = (KeyNamePair)orgCombo.getSelectedItem();
		if (org == null || m_comboActive)
			return;
		log.config(": " + org);
		m_comboActive = true;
		//
		KeyNamePair[] whs = m_login.getWarehouses(org);
		//	Delete existing warehouse items
		if (warehouseCombo.getItemCount() > 0)
			warehouseCombo.removeAllItems();
		
		//  fill warehouses
		if (whs != null)
		{
			//  initial warehouse
			KeyNamePair iniValue = null;
			String iniDefault = Ini.getProperty(Ini.P_WAREHOUSE);
			for (int i = 0; i < whs.length; i++)
			{
				warehouseCombo.addItem(whs[i]);
				if (whs[i].getName().equals(iniDefault))
					iniValue = whs[i];
			}
			if (iniValue != null)
				warehouseCombo.setSelectedItem(iniValue);
		}
		m_comboActive = false;
	}	//	orgComboChanged
	
	
	/**
	 *  Check Version
	 *  @return true if version is OK and
	 *  false if version could not be checked or is not the same
	 *  @see AEnv#getServerVersion
	 */
	private boolean checkVersion()
	{
		boolean retValue = false;
		try
		{
			String version = AEnv.getServerVersion();
			if (Compiere.DATE_VERSION.equals(version))
			{
				log.config("Server = Client - " + version);
				retValue = true;
			}
			else if (version != null)
			{
				StringBuffer msg = new StringBuffer (">>\n");
				msg.append(res.getString("VersionConflict")).append("\n")
					.append(res.getString("VersionInfo")).append("\n");
				msg.append(version == null ? "null" : version).append(" <> ")
					.append(Compiere.DATE_VERSION).append("\n");
				msg.append(res.getString("PleaseUpgrade")).append("\n<<");
				JOptionPane.showMessageDialog(null, msg.toString(),
					Compiere.getName() + " - " + res.getString("VersionConflict"),
					JOptionPane.ERROR_MESSAGE);
				AEnv.exit(1);
			}
		}
		catch (Exception e)
		{
			log.severe("Contact Server failed - "
				+ e.getClass().toString() + ": " + e.getMessage());
		}
		return retValue;
	}   //  checkVersion


	/**************************************************************************
	 *	Language issues
	 */
	private String	txt_Connected, txt_NotConnected, txt_NoDatabase,
					txt_UserPwdError, txt_RoleError, txt_LoggedIn;

	/**
	 *	Change Language
	 */
	private void languageComboChanged ()
	{
		String langName = (String)languageCombo.getSelectedItem();
	//	log.info( "Language: " + langName);
		Language language = Language.getLanguage(langName);
		Language.setLoginLanguage(language);
		Env.setContext(m_ctx, Env.LANGUAGE, language.getAD_Language());

		//	Locales
		Locale loc = language.getLocale();
		Locale.setDefault(loc);
		this.setLocale(loc);
		res = ResourceBundle.getBundle(RESOURCE, loc);
		//
		this.setTitle(res.getString("Login"));
		hostLabel.setText(res.getString("Host"));
		userLabel.setText(res.getString("User"));
		userLabel.setToolTipText(res.getString("EnterUser"));
		passwordLabel.setText(res.getString("Password"));
		passwordLabel.setToolTipText(res.getString("EnterPassword"));
		languageLabel.setText(res.getString("Language"));
		languageLabel.setToolTipText(res.getString("SelectLanguage"));
		//
		roleLabel.setText(res.getString("Role"));
		clientLabel.setText(res.getString("Client"));
		orgLabel.setText(res.getString("Organization"));
		dateLabel.setText(res.getString("Date"));
		warehouseLabel.setText(res.getString("Warehouse"));
		printerLabel.setText(res.getString("Printer"));
		defaultPanel.setToolTipText(res.getString("Defaults"));
		connectionPanel.setToolTipText(res.getString("Connection"));
		//
		txt_Connected = res.getString("Connected");
		txt_NotConnected = res.getString("NotConnected");
		txt_NoDatabase = res.getString("DatabaseNotFound");
		txt_UserPwdError = res.getString("UserPwdError");
		txt_RoleError = res.getString("RoleNotFound");
		txt_LoggedIn = res.getString("Authorized");
		//
		loginTabPane.setTitleAt(0, res.getString("Connection"));
		loginTabPane.setTitleAt(1, res.getString("Defaults"));
		confirmPanel.getOKButton().setToolTipText(res.getString("Ok"));
		confirmPanel.getCancelButton().setToolTipText(res.getString("Cancel"));

		//	DateField with new format
		dateField.setFormat();
		dateField.setValue(new Timestamp(System.currentTimeMillis()));
		//
		if (m_connectionOK)
			statusBar.setStatusLine(txt_LoggedIn);
		else
			statusBar.setStatusLine(txt_NotConnected, true);
	}   //	languageCombo_actionPerformed

}	//	ALogin
