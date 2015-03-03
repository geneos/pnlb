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
import javax.swing.*;
import org.compiere.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	About Dialog
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: AboutBox.java,v 1.23 2005/12/27 06:18:37 jjanke Exp $
 */
public final class AboutBox extends CDialog implements ActionListener
{
	/**
	 *	Constructor for modal about dialog
	 *  @param parent parent
	 */
	public AboutBox(JFrame parent)
	{
		super (parent, true);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		//
		labelVersion.setText(Compiere.MAIN_VERSION + " @ " + Compiere.DATE_VERSION);
		/** Removing/modifying the Compiere copyright notice is a violation of the license	*/
		labelCopyright.setText(Compiere.COPYRIGHT);
		infoArea.setText(CLogMgt.getInfo(null).toString());
		//  create 5 pt border
		Dimension d = imageControl.getPreferredSize();
		imageControl.setPreferredSize(new Dimension(d.width+10, d.height+10));
		//
		AEnv.positionCenterWindow(parent, this);
	}	//	AWindow_AboutBox

	private CPanel panel = new CPanel();
	private CPanel mainPanel = new CPanel();
	private JLabel imageControl = new JLabel();
	private JLabel labelHeading = new JLabel();
	private JLabel labelVersion = new JLabel();
	private JLabel labelCopyright = new JLabel();
	private JLabel labelDescription = new JLabel();
	private BorderLayout panelLayout = new BorderLayout();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private CPanel headerPanel = new CPanel();
	private GridLayout headerLayout = new GridLayout();
	private CTextArea infoArea = new CTextArea();
	private BorderLayout northLayout = new BorderLayout();
	private ConfirmPanel confirmPanel = new ConfirmPanel(false);

	/**
	 *	Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setTitle(Msg.translate(Env.getCtx(), "About"));
		//
		setResizable(false);
		labelHeading.setFont(new java.awt.Font("Dialog", 1, 14));
		labelHeading.setHorizontalAlignment(SwingConstants.CENTER);
		labelHeading.setHorizontalTextPosition(SwingConstants.CENTER);
		labelHeading.setText(" Smart ERP & CRM Business Solution ");
		labelVersion.setHorizontalAlignment(SwingConstants.CENTER);
		labelVersion.setHorizontalTextPosition(SwingConstants.CENTER);
		labelVersion.setText(".");
		labelCopyright.setHorizontalAlignment(SwingConstants.CENTER);
		labelCopyright.setHorizontalTextPosition(SwingConstants.CENTER);
		labelCopyright.setText(".");
		labelDescription.setForeground(Color.blue);
		labelDescription.setHorizontalAlignment(SwingConstants.CENTER);
		labelDescription.setHorizontalTextPosition(SwingConstants.CENTER);
		labelDescription.setText(Compiere.getURL());
		//
		imageControl.setFont(new java.awt.Font("Serif", 2, 10));
		imageControl.setForeground(Color.blue);
		imageControl.setAlignmentX((float) 0.5);
		imageControl.setHorizontalAlignment(SwingConstants.CENTER);
		imageControl.setHorizontalTextPosition(SwingConstants.CENTER);
		/** Removing/modifying the Compiere copyright notice is a violation of the license	*/
		imageControl.setIcon(Compiere.getImageIconLogo());
		imageControl.setText(Compiere.getSubtitle());
		imageControl.setVerticalTextPosition(SwingConstants.BOTTOM);
		//
		mainPanel.setLayout(mainLayout);
		mainLayout.setHgap(10);
		mainLayout.setVgap(10);
		northPanel.setLayout(northLayout);
		northLayout.setHgap(10);
		northLayout.setVgap(10);
		panel.setLayout(panelLayout);
		panelLayout.setHgap(10);
		panelLayout.setVgap(10);
		headerPanel.setLayout(headerLayout);
		headerLayout.setColumns(1);
		headerLayout.setRows(4);
		//
		infoArea.setReadWrite(false);

		this.getContentPane().add(panel, null);
		panel.add(northPanel, BorderLayout.NORTH);
		northPanel.add(imageControl, BorderLayout.WEST);
		northPanel.add(headerPanel, BorderLayout.CENTER);
		headerPanel.add(labelHeading, null);
		headerPanel.add(labelCopyright, null);
		headerPanel.add(labelVersion, null);
		headerPanel.add(labelDescription, null);
		panel.add(mainPanel, BorderLayout.CENTER);
		mainPanel.add(infoArea, BorderLayout.CENTER);
		mainPanel.add(confirmPanel, BorderLayout.SOUTH);
		confirmPanel.addActionListener(this);
	}   //  jbInit

	
	/**
	 *	ActionListener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals(ConfirmPanel.A_OK))
			dispose();
	}   //  actionPerformed
}	//	AboutBox
