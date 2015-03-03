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
package org.compiere.util;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;

import org.compiere.swing.*;
import org.compiere.plaf.CompierePLAF;

/**
 *  Init Dialog (License)
 *
 *  @author     Jorg Janke
 *  @version    $Id: IniDialog.java,v 1.9 2005/10/08 02:03:47 jjanke Exp $
 */
public final class IniDialog extends JDialog implements ActionListener
{
	/**
	 *  Constructor
	 */
	public IniDialog()
	{
		super();
		try
		{
			jbInit();
			//  get License file
			String where = s_res.getString("license_htm");
			if (where == null || where.length() == 0)
			{
				log.fine("No license pointer in resource");
				where = "org/compiere/license.htm";
			}
			URL url = null;
			ClassLoader cl = getClass().getClassLoader();
			if (cl != null)	//	Bootstrap
				url = cl.getResource(where);
			if (url == null)
			{
				log.fine("No license in resource ");
				url = new URL("http://www.compiere.org/license.htm");
			}
			if (url == null)
				cmd_reject();
			//
			licensePane.setPage(url);
			CompierePLAF.showCenterScreen(this);
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "init", ex);
			cmd_reject();
		}
	}   //  IniDialog

	/** Translation     */
	static ResourceBundle   s_res = ResourceBundle.getBundle("org.compiere.util.IniRes");
	private boolean         m_accept = false;
	/**	Logger	*/
	private static Logger	log	= Logger.getLogger (IniDialog.class.getName());
	

	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private JScrollPane scrollPane = new JScrollPane();
	private CPanel southPanel = new CPanel();
	private JButton bReject = CompierePLAF.getCancelButton();
	private JButton bAccept = CompierePLAF.getOKButton();
	private FlowLayout southLayout = new FlowLayout();
	private JLabel southLabel = new JLabel();
	private JEditorPane licensePane = new JEditorPane();

	/**
	 *  Static Layout
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		setTitle("Compiere - " + s_res.getString("Compiere_License"));
		southLabel.setText(s_res.getString("Do_you_accept"));
		bReject.setText(s_res.getString("No"));
		bAccept.setText(s_res.getString("Yes_I_Understand"));
		//
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		//
		mainPanel.setLayout(mainLayout);
		bReject.setForeground(Color.red);
		bReject.addActionListener(this);
		bAccept.addActionListener(this);
		southPanel.setLayout(southLayout);
		southLayout.setAlignment(FlowLayout.RIGHT);
		licensePane.setEditable(false);
		licensePane.setContentType("text/html");
		scrollPane.setPreferredSize(new Dimension(700, 400));
		southPanel.add(southLabel, null);
		getContentPane().add(mainPanel);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(licensePane, null);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		southPanel.add(bReject, null);
		southPanel.add(bAccept, null);
	}   //  jbInit

	/**
	 * ActionListener
	 * @param e event
	 */
	public final void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bAccept)
			m_accept = true;
		dispose();
	}   //  actionPerformed

	/**
	 *  Dispose
	 */
	public final void dispose()
	{
		super.dispose();
		if (!m_accept)
			cmd_reject();
	}   //  dispose

	/**
	 *  Is Accepted
	 *  @return true if accepted
	 */
	public final boolean isAccepted()
	{
		return m_accept;
	}   //  isAccepted

	/**
	 *  Reject License
	 */
	public final void cmd_reject()
	{
		String info = "License rejected or expired";
		try
		{
			info = s_res.getString("License_rejected");
		}
		catch (Exception e)
		{
		}
		log.severe(info);
		System.exit(10);
	}   //  cmd_reject

	/**
	 *  Display License and exit if rejected
	 *  @return true if acceptes
	 */
	public static final boolean accept()
	{
		IniDialog id = new IniDialog();
		if (id.isAccepted())
		{
			log.info("License Accepted");
			return true;
		}
		System.exit(10);
		return false;       //  never executed.
	}   //  accpept

}   //  IniDialog 
