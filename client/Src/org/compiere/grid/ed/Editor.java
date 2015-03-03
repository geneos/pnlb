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
import javax.swing.*;
import javax.swing.event.*;
import org.compiere.apps.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *  Editor for Text (textArea) with HTML (textPane) View
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: Editor.java,v 1.13 2005/12/09 05:17:55 jjanke Exp $
 */
public class Editor extends CDialog
	implements ChangeListener, ActionListener
{
	/**
	 *	Minimum constructor
	 * 	@param frame parent
	 */
	public Editor(Frame frame)
	{
		this (frame, Msg.getMsg(Env.getCtx(), "Editor"), "", true);
	}   //  Editor

	/**
	 *	Standard constructor
	 *	@param frame parent
	 *	@param header heading
	 *	@param text initial text
	 *	@param editable if false = r/o
	 */
	public Editor(Frame frame, String header, String text, boolean editable)
	{
		super (frame, header, frame != null);
		log.config( "Editor");
		try
		{
			jbInit();
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "Editor", ex);
		}
		//	Set Text
		m_text = text;
		textArea.setText(m_text);
		textArea.setEditable(editable);
		if (editable)
			textArea.setBackground(CompierePLAF.getFieldBackground_Normal());
		else
			textArea.setBackground(CompierePLAF.getFieldBackground_Inactive());
		textPane.setBackground(CompierePLAF.getFieldBackground_Inactive());
	}	//	Editor

	/**
	 *  IDE Constructor
	 */
	public Editor()
	{
		this (null);
	}	//	Editor

	private String m_text;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(Editor.class);

	private CPanel panel = new CPanel();
	private BorderLayout panelLayout = new BorderLayout();
	private JTabbedPane tabbedPane = new JTabbedPane();
	private CTextArea textArea = new CTextArea();
	private CTextPane textPane = new CTextPane();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu mFile = new JMenu();
	private CMenuItem mImport = new CMenuItem();
	private CMenuItem mExport = new CMenuItem();
	private ConfirmPanel confirmPanel = new ConfirmPanel();

	/**
	 *	Static Init
	 * 	@throws Exception
	 */
	private void jbInit() throws Exception
	{
		panel.setLayout(panelLayout);
		this.setJMenuBar(menuBar);
		//	Text Tab
		textArea.setPreferredSize(new Dimension(300, 300));
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		tabbedPane.add(textArea, "Text");
		//	HTML Tab
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		tabbedPane.add(textPane, "HTML");
		//
		mFile.setText("File");
		mImport.setText("Import");
		mImport.addActionListener(this);
		mExport.setText("Export");
		mExport.addActionListener(this);
		tabbedPane.addChangeListener(this);
		getContentPane().add(panel);
		panel.add(tabbedPane, BorderLayout.CENTER);
		this.getContentPane().add(confirmPanel, BorderLayout.SOUTH);
		menuBar.add(mFile);
		mFile.add(mImport);
		mFile.add(mExport);
		confirmPanel.addActionListener(this);
	}	//	jbInit

	/**
	 *	Factory: Start Editor
	 *	@param jc container to get parent frame
	 *	@param header heading
	 *	@param text initial text
	 *	@param editable if false = r/o
	 *	@return edited string
	 */
	public static String startEditor(Container jc, String header, String text, boolean editable)
	{
		//	Find frame
		JFrame frame = Env.getFrame(jc);
		String hdr = header;
		if (hdr == null || hdr.length() == 0)
			hdr = Msg.getMsg(Env.getCtx(), "Editor");
		//	Start it
		Editor ed = new Editor(frame, hdr, text, editable);
		AEnv.showCenterWindow(frame, ed);
		String s = ed.getText();
		ed = null;
		return s;
	}	//	startEditor

	/**
	 *	ActionListener
	 * 	@param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			m_text = textArea.getText();
			log.fine( "Editor.actionPerformed - OK - length=" + m_text.length());
			dispose();
		}
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			dispose();
		}
		
		else if (e.getSource() == mImport)
		{
			importText();
		}
		else if (e.getSource() == mExport)
		{
			exportText();
		}
	}	//	actionPerformed

	/**
	 *	Get Text
	 * 	@return edited text
	 */
	public String getText()
	{
		return m_text;
	}	//	getText

	/**
	 *	Import Text from File
	 */
	private void importText()
	{
		JFileChooser jc = new JFileChooser();
		jc.setDialogTitle(Msg.getMsg(Env.getCtx(), "ImportText"));
		jc.setDialogType(JFileChooser.OPEN_DIALOG);
		jc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//
		if (jc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
			return;

		StringBuffer sb = new StringBuffer();
		try
		{
			InputStreamReader in = new InputStreamReader (new FileInputStream (jc.getSelectedFile()));
			char[] cbuf = new char[1024];
			int count;
			while ((count = in.read(cbuf)) > 0)
				sb.append(cbuf, 0, count);
			in.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Editor.importText" + e.getMessage());
			return;
		}
		textArea.setText(sb.toString());
	}	//	importText

	/**
	 *	Export Text to File
	 */
	private void exportText()
	{
		JFileChooser jc = new JFileChooser();
		jc.setDialogTitle(Msg.getMsg(Env.getCtx(), "ExportText"));
		jc.setDialogType(JFileChooser.SAVE_DIALOG);
		jc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//
		if (jc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
			return;

		try
		{
			BufferedWriter bout = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (jc.getSelectedFile())));
			bout.write(textArea.getText());
			bout.flush();
			bout.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Editor.exportText" + e.getMessage());
		}
	}	//	exportText

	/**
	 *	ChangeListener for TabbedPane
	 * 	@param e event
	 */
	public void stateChanged(ChangeEvent e)
	{
		if (tabbedPane.getSelectedIndex() == 1)		//	switch to HTML
			textPane.setText(textArea.getText());
	}	//	stateChanged

}	//	Editor
