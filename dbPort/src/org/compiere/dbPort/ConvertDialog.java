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
package org.compiere.dbPort;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import javax.swing.*;
import org.compiere.*;
import org.compiere.db.*;
import org.compiere.swing.*;

/**
 *  Conversion Dialog
 *
 *  @author     Jorg Janke
 *  @version    $Id: ConvertDialog.java,v 1.20 2005/12/31 06:33:22 jjanke Exp $
 */
public class ConvertDialog extends CFrame implements ActionListener
{
	/**
	 *  Convert Dialog
	 */
	public ConvertDialog()
	{
		try
		{
			jbInit();
			//
			fSelectFile.addItem("D:\\compiere\\db\\database\\create\\views.sql");
			fSelectFile.addItem("D:\\compiere\\db\\database\\create\\temporary.sql");
			fSelectFile.addItem("D:\\compiere\\db\\database\\create\\sequences.sql");
			fSelectFile.addItem("D:\\compiere\\db\\database\\create\\compiere.sql");
			//  Set up environment
			fConnect.setValue(CConnection.get(Database.DB_DB2,
				"linux", DB_DB2.DEFAULT_PORT, "compiere"));
			fTarget.setSelectedItem(Database.DB_POSTGRESQL);
			fExecute.setSelected(true);

			cmd_execute();  //  set UI
			//
			pack();
			setVisible(true);
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
	}   //  ConvertDialog

	private JPanel parameterPanel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JLabel lSelectFile = new JLabel();
	private JComboBox fSelectFile = new JComboBox();
	private JButton bSelectFile = new JButton();
	private JCheckBox fExecute = new JCheckBox();
	private JLabel lConnect = new JLabel();
	private CConnectionEditor fConnect = new CConnectionEditor();
	private JButton bStart = new JButton();
	private JScrollPane scrollPane = new JScrollPane();
	private JTextArea infoPane = new JTextArea();
	private Component component1;
	private Component component2;
	private Component component3;
	private Component component4;
	private JLabel lTarget = new JLabel();
	private JComboBox fTarget = new JComboBox(Database.DB_NAMES);
	private JCheckBox fVerbose = new JCheckBox();

	/**
	 *  Static Layout
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		component1 = Box.createHorizontalStrut(8);
		component2 = Box.createHorizontalStrut(8);
		component3 = Box.createVerticalStrut(8);
		component4 = Box.createVerticalStrut(8);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("DB Convert Dialog");
		//
		parameterPanel.setLayout(gridBagLayout1);
		lSelectFile.setText("Select File");
		fSelectFile.setEditable(true);
		bSelectFile.setText("add file");
		bSelectFile.addActionListener(this);
		fExecute.setText("Execute Directly");
		fExecute.addActionListener(this);
		lConnect.setText("Connection");
		bStart.setText("Start");
		bStart.addActionListener(this);
		//
		infoPane.setBackground(Color.lightGray);
		infoPane.setEditable(false);
		scrollPane.setPreferredSize(new Dimension(200, 200));
		lTarget.setText("Target");
		fVerbose.setText("Verbose");
		//
		this.getContentPane().add(parameterPanel,  BorderLayout.NORTH);
		parameterPanel.add(lSelectFile,           new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		parameterPanel.add(fSelectFile,             new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bSelectFile,            new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fExecute,        new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(lConnect,         new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		parameterPanel.add(fConnect,         new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		parameterPanel.add(bStart,            new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(component1,      new GridBagConstraints(5, 0, 1, 2, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		parameterPanel.add(component2,     new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		parameterPanel.add(component3,    new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		parameterPanel.add(component4,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		parameterPanel.add(lTarget,     new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(infoPane, null);
		parameterPanel.add(fTarget,  new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fVerbose,   new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	}   //  jbInit

	/**
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		//
		if (e.getSource() == bStart)
		{
			bStart.setEnabled(false);
			cmd_start();
			bStart.setEnabled(true);
		}
		else if (e.getSource() == bSelectFile)
			cmd_selectFile();

		else if (e.getSource() == fExecute)
			cmd_execute();
		//
		setCursor(Cursor.getDefaultCursor());
	}   //  actionListener

	/**
	 *  Execute toggle
	 */
	private void cmd_execute()
	{
		lConnect.setEnabled(fExecute.isSelected());
		fConnect.setReadWrite(fExecute.isSelected());
		lTarget.setEnabled(!fExecute.isSelected());
		fTarget.setEnabled(!fExecute.isSelected());
	}   //  cmd_execute

	/**
	 *  Select File and add to selection
	 */
	private void cmd_selectFile()
	{
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(false);
		if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
		File f = fc.getSelectedFile();
		if (f == null || !f.isFile())
			return;
		String fileName = f.getAbsolutePath();
		//
		fSelectFile.addItem(fileName);
		fSelectFile.setSelectedItem(fileName);
	}   //  cmd_selectFile

	
	/**************************************************************************
	 *  Start Pressed
	 */
	private void cmd_start()
	{
		//  Open and read File
		File file = new File ((String)fSelectFile.getSelectedItem());
		if (!file.exists() || file.isDirectory())
		{
			infoPane.append("File does not exist or a directory: " + file + "\n");
			return;
		}
		infoPane.append("Opening file: " + file + "\n");
		StringBuffer sb = new StringBuffer (1000);
		//
		try
		{
			FileReader fr = new FileReader(file);
			BufferedReader in = new BufferedReader(fr);

			String line = null;
			int lines = 0;
			while((line = in.readLine()) != null)
			{
				lines++;
				sb.append(line).append('\n');
			}
			in.close();
			fr.close();
			infoPane.append("- Read lines: " + lines + ", size: " + sb.length() + "\n");
		}
		catch (FileNotFoundException fnf)
		{
			infoPane.append("Error: " + fnf + "\n");
			return;
		}
		catch (IOException ioe)
		{
			infoPane.append("Error: " + ioe + "\n");
			return;
		}

		//  Target system
		if (fExecute.isSelected())
		{
			CConnection cc = (CConnection)fConnect.getValue();
			Convert convert = new Convert (cc.getType());
			convert.setVerbose(fVerbose.isSelected());
			//
			Connection conn = cc.getConnection (true, Connection.TRANSACTION_READ_COMMITTED);
			convert.execute(sb.toString(), conn);
			if (convert.hasError())
			{
				StringBuffer sbb = new StringBuffer ("- Error: ");
				if (convert.getConversionError() != null)
					sbb.append(convert.getConversionError()).append(' ');
				if (convert.getException() != null)
				{
					sbb.append(convert.getException());
					convert.getException().printStackTrace();
				}
				sbb.append("\n");
				infoPane.append(sbb.toString());
			}
			else
				infoPane.append("- OK\n");
		}
		else
		{
			String target = (String)fTarget.getSelectedItem();
			if (Database.DB_ORACLE.equals(target))
			{
				infoPane.append("No conversion needed.\n");
				return;
			}
			Convert convert = new Convert (target);
			//
			String cc = convert.convertAll(sb.toString());

			//  Output file name
			String fileName = file.getAbsolutePath();
			int pos = fileName.lastIndexOf(".");
			if (pos == -1)
				fileName += target;
			else
				fileName = fileName.substring(0, pos) + target + fileName.substring(pos);
			infoPane.append("Writing to: " + fileName + "\n");
			//  Write to file
			try
			{
				FileWriter fw = new FileWriter(fileName, false);
				BufferedWriter out = new BufferedWriter (fw);
				out.write("-- Compiere dbPort - Convert Oracle to " + target);
				out.newLine();
				out.write("-- " + Compiere.getSummary());
				out.newLine();
				//
				out.write(cc);
				//
				out.close();
				fw.close();
			}
			catch (IOException ioe)
			{
				infoPane.append("Error: " + ioe + "\n");
			}
			infoPane.append("- Written: " + cc.length() + "\n");
		}
	}   //  cmd_start


	/*************************************************************************/

	/**
	 *  Start Dialog
	 *  @param args ignored
	 */
	public static void main(String[] args)
	{
		new ConvertDialog();
	}   //  main
}   //  ConvertDialog
