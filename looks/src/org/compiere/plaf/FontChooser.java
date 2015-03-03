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
package org.compiere.plaf;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.compiere.swing.*;

/**
 *  Font Chooser Dialog
 *
 *  @author     Jorg Janke
 *  @version    $Id: FontChooser.java,v 1.9 2005/12/27 06:20:19 jjanke Exp $
 */
public class FontChooser extends CDialog
	implements ActionListener
{
	/**
	 *  Show Dialog with initial font and return selected font
	 *  @param owner Base window
	 *  @param title Chooser Title
	 *  @param initFont initial font
	 *  @return selected font
	 */
	public static Font showDialog (Dialog owner, String title, Font initFont)
	{
		Font retValue = initFont;
		FontChooser fc = new FontChooser(owner, title, initFont);
		retValue = fc.getFont();
		fc = null;
		return retValue;
	}   //  showDialog

	
	/**************************************************************************
	 *  Constructor
	 *
	 *  @param owner Base window
	 *  @param title Chooser Title
	 *  @param initFont Initial Font
	 */
	public FontChooser(Dialog owner, String title, Font initFont)
	{
		super(owner, title, true);
		try
		{
			jbInit();
			dynInit();
			setFont(initFont);
			CompierePLAF.showCenterScreen(this);
		}
		catch(Exception ex)
		{
			System.err.println ("FontChooser");
			ex.printStackTrace();
		}
	}   //  FontChooser

	/**
	 *  IDE Constructor
	 */
	public FontChooser()
	{
		this (null, s_res.getString("FontChooser"), null);
	}   //  FontChooser

	static ResourceBundle   s_res = ResourceBundle.getBundle("org.compiere.plaf.PlafRes");

	/** Static list of Styles       */
	public static FontStyle[] s_list = {
		new FontStyle(s_res.getString("Plain"), Font.PLAIN),
		new FontStyle(s_res.getString("Italic"), Font.ITALIC),
		new FontStyle(s_res.getString("Bold"), Font.BOLD),
		new FontStyle(s_res.getString("BoldItalic"), Font.BOLD|Font.ITALIC)};

	private Font        m_font = super.getFont();
	private Font        m_retFont = null;

	private boolean     m_setting = false;

	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel selectPanel = new CPanel();
	private CLabel nameLabel = new CLabel();
	private CComboBox fontName = new CComboBox();
	private CLabel sizeLabel = new CLabel();
	private CLabel styleLabel = new CLabel();
	private CComboBox fontStyle = new CComboBox();
	private CComboBox fontSize = new CComboBox();
	private JTextArea fontTest = new JTextArea();
	private JTextArea fontInfo = new JTextArea();
	private GridBagLayout selectLayout = new GridBagLayout();
	private CPanel confirmPanel = new CPanel();
	private CButton bCancel = CompierePLAF.getCancelButton();
	private CButton bOK = CompierePLAF.getOKButton();
	private FlowLayout confirmLayout = new FlowLayout();

	/**
	 *  Static Layout
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		mainPanel.setLayout(mainLayout);
		nameLabel.setText(s_res.getString("Name"));
		selectPanel.setLayout(selectLayout);
		sizeLabel.setText(s_res.getString("Size"));
		styleLabel.setText(s_res.getString("Style"));
		fontTest.setText(s_res.getString("TestString"));
		fontTest.setLineWrap(true);
		fontTest.setWrapStyleWord(true);
		fontTest.setBackground(CompierePLAF.getFieldBackground_Inactive());
		fontTest.setBorder(BorderFactory.createLoweredBevelBorder());
		fontTest.setPreferredSize(new Dimension(220, 100));
		fontInfo.setText(s_res.getString("FontString"));
		fontInfo.setLineWrap(true);
		fontInfo.setWrapStyleWord(true);
		fontInfo.setBackground(CompierePLAF.getFieldBackground_Inactive());
		fontInfo.setOpaque(false);
		fontInfo.setEditable(false);
		confirmPanel.setLayout(confirmLayout);
		confirmLayout.setAlignment(FlowLayout.RIGHT);
		confirmPanel.setOpaque(false);
		selectPanel.setOpaque(false);
		getContentPane().add(mainPanel);
		mainPanel.add(selectPanel, BorderLayout.CENTER);
		selectPanel.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		selectPanel.add(fontName,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		selectPanel.add(sizeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		selectPanel.add(styleLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		selectPanel.add(fontStyle,  new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		selectPanel.add(fontSize,  new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		selectPanel.add(fontTest,  new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(20, 5, 5, 5), 0, 0));
		selectPanel.add(fontInfo, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 5, 10, 5), 0, 0));
		//
		mainPanel.add(confirmPanel, BorderLayout.SOUTH);
		confirmPanel.add(bCancel, null);
		confirmPanel.add(bOK, null);
		bCancel.addActionListener(this);
		bOK.addActionListener(this);
	}   //  jbInit

	/**
	 *  Dynamic Init
	 */
	private void dynInit()
	{
		String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		Arrays.sort(names);
		for (int i = 0; i < names.length; i++)
			fontName.addItem(names[i]);
		fontName.addActionListener(this);
		//
		for (int i = 6; i < 32; i++)
			fontSize.addItem(String.valueOf(i));
		fontSize.addActionListener(this);
		//
		for (int i = 0; i < s_list.length; i++)
			fontStyle.addItem(s_list[i]);
		fontStyle.addActionListener(this);
	}   //  dynInit

	/**
	 *  Set Font - sets font for chooser - not the component font
	 *  @param font
	 */
	public void setFont(Font font)
	{
		if (font == null)
			return;
	//	Log.trace("FontChooser.setFont - " + font.toString());
		if (m_retFont == null)
			m_retFont = font;
		//
		fontTest.setFont(font);
		fontInfo.setFont(font);
		fontInfo.setText(font.toString());
		//
		m_setting = true;
		fontName.setSelectedItem(font.getName());
		if (!fontName.getSelectedItem().equals(font.getName()))
			System.err.println("FontChooser.setFont" + fontName.getSelectedItem().toString() + " <> " + font.getName());
		//
		fontSize.setSelectedItem(String.valueOf(font.getSize()));
		if (!fontSize.getSelectedItem().equals(String.valueOf(font.getSize())))
			System.err.println("FontChooser.setFont" + fontSize.getSelectedItem() + " <> " + font.getSize());
		//  find style
		for (int i = 0; i < s_list.length; i++)
			if (s_list[i].getID() == font.getStyle())
				fontStyle.setSelectedItem(s_list[i]);
		if (((FontStyle)fontStyle.getSelectedItem()).getID() != font.getStyle())
			System.err.println("FontChooser.setFont" + ((FontStyle)fontStyle.getSelectedItem()).getID() + " <> " + font.getStyle());
		//
		m_font = font;
		this.pack();
		m_setting = false;
	}   //  setFont

	/**
	 *  Return selected font
	 *  @return font
	 */
	public Font getFont()
	{
		return m_retFont;
	}   //  getFont

	/**
	 *  ActionListener
	 *  @param e
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (m_setting)
			return;

		if (e.getSource() == bOK)
		{
			m_retFont = m_font;
			dispose();
		}

		else if (e.getSource() == bCancel)
			dispose();

		else if (e.getSource() == fontName)
		{
			String s = fontName.getSelectedItem().toString();
			m_font = new Font(s, m_font.getStyle(), m_font.getSize());
		}
		else if (e.getSource() == fontSize)
		{
			String s = fontSize.getSelectedItem().toString();
			m_font = new Font(m_font.getName(), m_font.getStyle(), Integer.parseInt(s));
		}
		else if (e.getSource() == fontStyle)
		{
			FontStyle fs = (FontStyle)fontStyle.getSelectedItem();
			m_font = new Font(m_font.getName(), fs.getID(), m_font.getSize());
		}
	//	System.out.println("NewFont - " + m_font.toString());
		setFont(m_font);
	}   //  actionPerformed
}   //  FontChooser

/**
 *  Font Style Value Object
 */
class FontStyle
{
	/**
	 *  Create FontStyle
	 *  @param name
	 *  @param id
	 */
	public FontStyle(String name, int id)
	{
		m_name = name;
		m_id = id;
	}   //  FontStyle

	private String  m_name;
	private int     m_id;

	/**
	 *  Get Name
	 *  @return name
	 */
	public String toString()
	{
		return m_name;
	}   //  getName

	/**
	 *  Get int value of Font Style
	 *  @return id
	 */
	public int getID()
	{
		return m_id;
	}   //  getID
}   //  FontStyle
