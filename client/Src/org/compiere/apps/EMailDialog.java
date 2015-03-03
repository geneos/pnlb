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
import java.beans.*;
import java.io.*;
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	EMail Dialog
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: EMailDialog.java,v 1.17 2005/10/08 02:03:04 jjanke Exp $
 */
public class EMailDialog extends CDialog 
	implements ActionListener, VetoableChangeListener
{
	/**
	 * 	EMail Dialog
	 *	@param owner calling window
	 *	@param title title
	 *	@param from from
	 *	@param to to 
	 *	@param subject subject
	 *	@param message message
	 *	@param attachment optional attachment
	 */
	public EMailDialog (Dialog owner, String title, MUser from, String to, 
		String subject, String message, File attachment)
	{
		super (owner, title, true);
		commonInit(from, to, subject, message, attachment);
	}	//	EmailDialog

	/**
	 * 	EMail Dialog
	 *	@param owner calling window
	 *	@param title title
	 *	@param from from
	 *	@param to to 
	 *	@param subject subject
	 *	@param message message
	 *	@param attachment optional attachment
	 */
	public EMailDialog (Frame owner, String title, MUser from, String to, 
		String subject, String message, File attachment)
	{
		super (owner, title, true);
		commonInit(from, to, subject, message, attachment);
	}	//	EmailDialog

	/**
	 * 	Common Init
	 *	@param from from
	 *	@param to to 
	 *	@param subject subject
	 *	@param message message
	 *	@param attachment optional attachment
	 */
	private void commonInit (MUser from, String to, 
		String subject, String message, File attachment)
	{
		m_client = MClient.get(Env.getCtx());
		try
		{
			int WindowNo = 0;
			int AD_Column_ID = 0;
			Lookup lookup = MLookupFactory.get (Env.getCtx(), WindowNo, 
				AD_Column_ID, DisplayType.Search,
				Env.getLanguage(Env.getCtx()), "AD_User_ID", 0, false,
				"EMail IS NOT NULL");
			fUser = new VLookup ("AD_User_ID", false, false, true, lookup);
			fUser.addVetoableChangeListener(this);
			jbInit();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "EMailDialog", ex);
		}
		set(from, to, subject, message);
		setAttachment(attachment);
		AEnv.showCenterScreen(this);
	}	//	commonInit


	/**	Client				*/
	private MClient	m_client = null;
	/** Sender				*/
	private MUser	m_from = null;
	/** Primary Recipient	*/
	private MUser	m_user = null;
	//
	private String  m_to;
	private String  m_subject;
	private String  m_message;
	/**	File to be optionally attached	*/
	private File	m_attachFile;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(EMailDialog.class);

	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel headerPanel = new CPanel();
	private GridBagLayout headerLayout = new GridBagLayout();
	private CTextField fFrom = new CTextField(20);
	private CTextField fTo = new CTextField(20);
	private VLookup fUser = null;
	private CTextField fSubject = new CTextField(40);
	private	CLabel lFrom = new CLabel();
	private CLabel lTo = new CLabel();
	private CLabel lSubject = new CLabel();
	private CLabel lAttachment = new CLabel();
	private CTextField fAttachment = new CTextField(40);
	private CTextArea fMessage = new CTextArea();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private StatusBar statusBar = new StatusBar();

	/**
	 *	Static Init
	 */
	void jbInit() throws Exception
	{
		lFrom.setText(Msg.getMsg(Env.getCtx(), "From") + ":");
		lTo.setText(Msg.getMsg(Env.getCtx(), "To") + ":");
		lSubject.setText(Msg.getMsg(Env.getCtx(), "Subject") + ":");
		lAttachment.setText(Msg.getMsg(Env.getCtx(), "Attachment") + ":");
		fFrom.setReadWrite(false);
		//
		mainPanel.setLayout(mainLayout);
		headerPanel.setLayout(headerLayout);
		mainLayout.setHgap(5);
		mainLayout.setVgap(5);
		fMessage.setPreferredSize(new Dimension(150, 150));
		getContentPane().add(mainPanel);
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		
		headerPanel.add(lFrom, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 5), 0, 0));
		headerPanel.add(fFrom, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 10), 0, 0));

		headerPanel.add(lTo, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 5), 0, 0));
		headerPanel.add(fUser, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));
		headerPanel.add(fTo, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));

		headerPanel.add(lSubject, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 0, 5), 0, 0));
		headerPanel.add(fSubject, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 10), 1, 0));

		headerPanel.add(lAttachment, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 0, 5), 0, 0));
		headerPanel.add(fAttachment, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 10), 1, 0));

		mainPanel.add(fMessage, BorderLayout.CENTER);
		//
		mainPanel.add (confirmPanel, BorderLayout.SOUTH);
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);
		confirmPanel.addActionListener(this);
		statusBar.setStatusDB(null);
	}	//	jbInit

	/**
	 *	Set all properties
	 */
	public void set (MUser from, String to, String subject, String message)
	{
		//	Content
		setFrom(from);
		setTo(to);
		setSubject(subject);
		setMessage(message);
		//
		statusBar.setStatusLine(m_client.getSMTPHost());
	}	//	set

	/**
	 *  Set Address
	 */
	public void setTo(String newTo)
	{
		m_to = newTo;
		fTo.setText(m_to);
	}	//	setTo

	/**
	 *  Get Address
	 */
	public String getTo()
	{
		m_to = fTo.getText();
		return m_to;
	}	//	getTo

	/**
	 *  Set Sender
	 */
	public void setFrom(MUser newFrom)
	{
		m_from = newFrom;
		if (newFrom == null 
			|| !newFrom.isEMailValid() 
			|| !newFrom.isCanSendEMail())
		{
			confirmPanel.getOKButton().setEnabled(false);
			fFrom.setText("**Invalid**");
		}
		else
			fFrom.setText(m_from.getEMail());
	}	//	setFrom

	/**
	 *  Get Sender
	 */
	public MUser getFrom()
	{
		return m_from;
	}	//	getFrom

	/**
	 *  Set Subject
	 */
	public void setSubject(String newSubject)
	{
		m_subject = newSubject;
		fSubject.setText(m_subject);
	}	//	setSubject

	/**
	 *  Get Subject
	 */
	public String getSubject()
	{
		m_subject = fSubject.getText();
		return m_subject;
	}	//	getSubject

	/**
	 *  Set Message
	 */
	public void setMessage(String newMessage)
	{
		m_message = newMessage;
		fMessage.setText(m_message);
		fMessage.setCaretPosition(0);
	}   //  setMessage

	/**
	 *  Get Message
	 */
	public String getMessage()
	{
		m_message = fMessage.getText();
		return m_message;
	}   //  getMessage

	/**
	 *  Set Attachment
	 */
	public void setAttachment (File attachment)
	{
		m_attachFile = attachment;
		if (attachment == null)
		{
			lAttachment.setVisible(false);
			fAttachment.setVisible(false);
		}
		else
		{
			lAttachment.setVisible(true);
			fAttachment.setVisible(true);
			fAttachment.setText(attachment.getName());
			fAttachment.setReadWrite(false);
		}
	}	//	setAttachment

	/**
	 *  Get Attachment
	 */
	public File getAttachment()
	{
		return m_attachFile;
	}	//	getAttachment

	/**************************************************************************
	 * 	Action Listener - Send email
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (getTo() == null || getTo().length() == 0)
		{
			return;
		}
		//	Send
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			confirmPanel.getOKButton().setEnabled(false);

			StringTokenizer st = new StringTokenizer(getTo(), " ,;", false);
			String to = st.nextToken();
			EMail email = m_client.createEMail(getFrom(), to, getSubject(), getMessage());
			String status = "Check Setup";
			if (email != null)
			{
				while (st.hasMoreTokens())
					email.addTo(st.nextToken());
				//	Attachment
				if (m_attachFile != null && m_attachFile.exists())
					email.addAttachment(m_attachFile);
				status = email.send();
				//
				if (m_user != null)
					new MUserMail(m_user, m_user.getAD_User_ID(), email).save();
				if (email.isSentOK())
				{
					ADialog.info(0, this, "MessageSent");
					dispose();
				}
				else
					ADialog.error(0, this, "MessageNotSent", status);
			}
			else
				ADialog.error(0, this, "MessageNotSent", status);
			//
			confirmPanel.getOKButton().setEnabled(false);
			setCursor(Cursor.getDefaultCursor());
		}
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
			dispose();

	}	//	actionPerformed

	/**
	 * 	Vetoable Change - User selected 
	 *	@param evt
	 *	@throws PropertyVetoException
	 */
	public void vetoableChange (PropertyChangeEvent evt)
		throws PropertyVetoException
	{
		Object value = evt.getNewValue();
		log.info("Value=" + value);
		if (value == null)
			fTo.setText("");
		if (value instanceof Integer)
		{
			int AD_User_ID = ((Integer)value).intValue();
			m_user = MUser.get(Env.getCtx(), AD_User_ID);
			fTo.setValue(m_user.getEMail());
		}
	}	//	vetoableChange

}	//	VEMailDialog

