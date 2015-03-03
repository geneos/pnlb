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
package org.compiere.grid.ed;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Image Dialog
 *
 *  @author   Jorg Janke
 *  @version  $Id: VImageDialog.java,v 1.10 2005/12/27 06:18:36 jjanke Exp $
 */
public class VImageDialog extends CDialog
	implements ActionListener
{
	/**
	 *  Constructor
	 *  @param owner
	 *  @param mImage
	 */
	public VImageDialog (Frame owner, MImage mImage)
	{
		super (owner, Msg.translate(Env.getCtx(), "AD_Image_ID"), true);
		log.info("MImage=" + mImage);
		m_mImage = mImage;
		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "VImageDialog", ex);
		}
		//  load data
		fileButton.setText(m_mImage.getName());
	//	imageLabel.setIcon(m_mImage.getImage());
		AEnv.positionCenterWindow(owner, this);
	}   //  VImageDialog

	/**  Image Model            */
	private MImage      m_mImage;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VImageDialog.class);

	/** @todo Display existing Images from MImage */

	/** */
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	private CLabel fileLabel = new CLabel();
	private CButton fileButton = new CButton();
	private CLabel imageLabel = new CLabel();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);

	/**
	 *  Static Init
	 *  @throws Exception
	 */
	void jbInit() throws Exception
	{
		mainPanel.setLayout(mainLayout);
		fileLabel.setText(Msg.getMsg(Env.getCtx(), "SelectFile"));
		fileButton.setText("-/-");
		imageLabel.setBackground(Color.white);
		imageLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		imageLabel.setPreferredSize(new Dimension(50, 50));
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(mainPanel);
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		parameterPanel.add(fileLabel, null);
		parameterPanel.add(fileButton, null);
		mainPanel.add(imageLabel, BorderLayout.CENTER);
		mainPanel.add(confirmPanel, BorderLayout.SOUTH);
		//
		fileButton.addActionListener(this);
		confirmPanel.addActionListener(this);
	}   //  jbInit

	/**
	 *  ActionListener
	 *  @param e
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == fileButton)
			cmd_file();

		else if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (m_mImage.save())
				dispose();
			else
				setCursor(Cursor.getDefaultCursor());
		}

		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
			dispose();
	}   //  actionPerformed

	/**
	 *  Load file & display
	 */
	private void cmd_file()
	{
		//  Show File Open Dialog
		JFileChooser jfc = new JFileChooser();
		jfc.setMultiSelectionEnabled(false);
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.showOpenDialog(this);

		//  Get File Name
		File imageFile = jfc.getSelectedFile();
		if (imageFile == null || imageFile.isDirectory() || !imageFile.exists())
			return;

		//  See if we can load & display it
		try
		{
			ImageIcon image = new ImageIcon (imageFile.toURL());
			imageLabel.setIcon(image);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "cmd_file", e);
			return;
		}

		//  OK
		fileButton.setText(imageFile.getAbsolutePath());
		pack();

		//  Save info
		String fileName = imageFile.getAbsolutePath();
		m_mImage.setName(fileName);
		m_mImage.setImageURL(fileName);
	//	m_mImage.setImageFile(imageFile);
	}   //  cmd_file

}   //  VImageDialog
