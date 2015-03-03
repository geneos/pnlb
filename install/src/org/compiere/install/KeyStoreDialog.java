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
import javax.swing.*;
import org.compiere.apps.*;
import org.compiere.swing.*;


/**
 *	Key Store Dialog
 *	
 *  @author Jorg Janke
 *  @version $Id: KeyStoreDialog.java,v 1.2 2005/03/11 20:30:21 jjanke Exp $
 */
public class KeyStoreDialog extends CDialog
{

	/**
	 * 	Constructor
	 *	@param cn common name
	 *	@param ou org unit
	 *	@param o organization
	 *	@param l locale
	 *	@param s state
	 *	@param c country
	 */
	public KeyStoreDialog (JFrame owner,
		String cn, String ou, String o, String l, String s, String c)
		throws HeadlessException
	{
		super (owner, true);
		setTitle("Key Store Dialog");
		//
		jbInit();
		setValues(cn, ou, o, l, s, c);
		//
		AEnv.showCenterWindow(owner, this);
	}	//	KeyStoreDialog

	private CLabel 		lCN = new CLabel("(ON) Common Name");
	private CTextField 	fCN = new CTextField(20);
	private CLabel 		lOU = new CLabel("(OU) Organization Unit");
	private CTextField 	fOU = new CTextField(20);
	private CLabel 		lO = new CLabel("(O) Organization");
	private CTextField 	fO = new CTextField(20);
	private CLabel 		lL = new CLabel("(L) Locale/Town");
	private CTextField 	fL = new CTextField(20);
	private CLabel 		lS = new CLabel("(S) State");
	private CTextField 	fS = new CTextField(20);
	private CLabel 		lC = new CLabel("(C) Country (2 Char)");
	private CTextField 	fC = new CTextField(2);

	private CButton		bOK = ConfirmPanel.createOKButton("OK");
	private CButton		bCancel = ConfirmPanel.createCancelButton("Cancel");
	private boolean		m_ok = false;
	
	/**
	 * 	Static Layout
	 */
	private void jbInit()
	{
		CPanel panel = new CPanel(new ALayout());
		panel.add(lCN, new ALayoutConstraint(0, 0));
		panel.add(fCN, null);
		panel.add(lOU, new ALayoutConstraint(1, 0));
		panel.add(fOU, null);
		panel.add(lO, new ALayoutConstraint(2, 0));
		panel.add(fO, null);
		panel.add(lL, new ALayoutConstraint(3, 0));
		panel.add(fL, null);
		panel.add(lS, new ALayoutConstraint(4, 0));
		panel.add(fS, null);
		panel.add(lC, new ALayoutConstraint(5, 0));
		panel.add(fC, null);
		panel.setPreferredSize(new Dimension(400,150));
		//
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add (panel, BorderLayout.CENTER);
		//
		CPanel confirmPanel = new CPanel(new FlowLayout(FlowLayout.RIGHT));
		confirmPanel.add(bCancel);
		confirmPanel.add(bOK);
		getContentPane().add (confirmPanel, BorderLayout.SOUTH);
		//
		bCancel.addActionListener(this);
		bOK.addActionListener(this);
	}	//	jbInit
	
	/**
	 * 	Action Listener
	 *	@param e evt
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == bOK)
			m_ok = true;
		dispose();
	}	//	actionPerformed
	
	/**
	 * 	OK Pressed
	 *	@return true if OK
	 */
	public boolean isOK()
	{
		return m_ok;
	}	//	isOK
	
	/**
	 * 	Set Values
	 *	@param cn common name
	 *	@param ou org unit
	 *	@param o organization
	 *	@param l locale
	 *	@param s state
	 *	@param c country
	 */
	public void setValues(String cn, String ou, String o, String l, String s, String c)
	{
		fCN.setText(cn);
		fOU.setText(ou);
		fO.setText(o);
		fL.setText(l);
		fS.setText(s);
		fC.setText(c);
	}	//	setValues

	public String getCN()
	{
		return fCN.getText();
	}
	public String getOU()
	{
		return fOU.getText();
	}
	public String getO()
	{
		return fO.getText();
	}
	public String getL()
	{
		return fL.getText();
	}
	public String getS()
	{
		return fS.getText();
	}
	public String getC()
	{
		return fC.getText();
	}
	
}	//	KeyStoreDialog
