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
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

import org.compiere.swing.*;

/**
 *  Java Theme Editor.
 *  Edit the attributes and save them in Ini.properties.
 *  Does not set background of CompiereColorUI.
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereThemeEditor.java,v 1.14 2005/12/27 06:20:19 jjanke Exp $
 */
public class CompiereThemeEditor extends CDialog
	implements ActionListener
{
	/**
	 *  Constructor
	 *  @param  owner Frame owner
	 */
	public CompiereThemeEditor (JDialog owner)
	{
		super(owner, s_res.getString("CompiereThemeEditor"), true);
		try
		{
			jbInit();
			loadTheme();
			dynInit();
			CompierePLAF.showCenterScreen(this);
		}
		catch(Exception e)
		{
			System.err.println("CompiereThemeEditor");
			e.printStackTrace();
		}
	}   //  CompiereThemeEditor

	static ResourceBundle   s_res = ResourceBundle.getBundle("org.compiere.plaf.PlafRes");

	private CButton primary1 = new CButton();
	private CButton primary2 = new CButton();
	private CButton primary3 = new CButton();
	private CButton secondary1 = new CButton();
	private CButton secondary2 = new CButton();
	private CButton secondary3 = new CButton();
	private CButton controlFont = new CButton();
	private CButton systemFont = new CButton();
	private CButton userFont = new CButton();
	private CButton smallFont = new CButton();
	private CButton mandatory = new CButton();
	private CButton error = new CButton();
	private CButton windowFont = new CButton();
	private CButton menuFont = new CButton();
	private CButton white = new CButton();
	private CButton black = new CButton();
	private CPanel confirmPanel = new CPanel();

	private CButton inactive = new CButton();
	private CButton txt_ok = new CButton();
	private CButton txt_error = new CButton();
	private CButton bCancel = CompierePLAF.getCancelButton();
	private CButton bOK = CompierePLAF.getOKButton();
	private FlowLayout confirmLayout = new FlowLayout();
	private CPanel centerPanel = new CPanel();
	private CPanel metalColorPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout();
	private CPanel compiereColorPanel = new CPanel();
	private TitledBorder metalColorBorder;
	private TitledBorder compiereColorBorder;
	private GridLayout metalColorLayout = new GridLayout();
	private GridLayout compiereColorLayout = new GridLayout();
	private CPanel fontPanel = new CPanel();
	private GridLayout fontLayout = new GridLayout();
	private TitledBorder fontBorder;
	private CButton info = new CButton();

	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		metalColorBorder = new TitledBorder(s_res.getString("MetalColors"));
		compiereColorBorder = new TitledBorder(s_res.getString("CompiereColors"));
		fontBorder = new TitledBorder(s_res.getString("CompiereFonts"));
		fontPanel.setBorder(fontBorder);
		fontPanel.setOpaque(false);

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//
		primary1.setToolTipText(s_res.getString("Primary1Info"));
		primary1.setText(s_res.getString("Primary1"));
		primary1.addActionListener(this);
		primary2.setToolTipText(s_res.getString("Primary2Info"));
		primary2.setText(s_res.getString("Primary2"));
		primary2.addActionListener(this);
		primary3.setToolTipText(s_res.getString("Primary3Info"));
		primary3.setText(s_res.getString("Primary3"));
		primary3.addActionListener(this);
		secondary1.setToolTipText(s_res.getString("Secondary1Info"));
		secondary1.setText(s_res.getString("Secondary1"));
		secondary1.addActionListener(this);
		secondary2.setToolTipText(s_res.getString("Secondary2Info"));
		secondary2.setText(s_res.getString("Secondary2"));
		secondary2.addActionListener(this);
		secondary3.setToolTipText(s_res.getString("Secondary3Info"));
		secondary3.setText(s_res.getString("Secondary3"));
		secondary3.addActionListener(this);
		controlFont.setToolTipText(s_res.getString("ControlFontInfo"));
		controlFont.setText(s_res.getString("ControlFont"));
		controlFont.addActionListener(this);
		systemFont.setToolTipText(s_res.getString("SystemFontInfo"));
		systemFont.setText(s_res.getString("SystemFont"));
		systemFont.addActionListener(this);
		userFont.setToolTipText(s_res.getString("UserFontInfo"));
		userFont.setText(s_res.getString("UserFont"));
		userFont.addActionListener(this);
		smallFont.setText(s_res.getString("SmallFont"));
		smallFont.addActionListener(this);
		mandatory.setToolTipText(s_res.getString("MandatoryInfo"));
		mandatory.setText(s_res.getString("Mandatory"));
		mandatory.addActionListener(this);
		error.setToolTipText(s_res.getString("ErrorInfo"));
		error.setText(s_res.getString("Error"));
		error.addActionListener(this);
		info.setToolTipText(s_res.getString("InfoInfo"));
		info.setText(s_res.getString("Info"));
		info.addActionListener(this);
		windowFont.setText(s_res.getString("WindowTitleFont"));
		windowFont.addActionListener(this);
		menuFont.setText(s_res.getString("MenuFont"));
		menuFont.addActionListener(this);
		white.setToolTipText(s_res.getString("WhiteInfo"));
		white.setText(s_res.getString("White"));
		white.addActionListener(this);
		black.setToolTipText(s_res.getString("BlackInfo"));
		black.setText(s_res.getString("Black"));
		black.addActionListener(this);
		inactive.setToolTipText(s_res.getString("InactiveInfo"));
		inactive.setText(s_res.getString("Inactive"));
		inactive.addActionListener(this);
		txt_ok.setToolTipText(s_res.getString("TextOKInfo"));
		txt_ok.setText(s_res.getString("TextOK"));
		txt_ok.addActionListener(this);
		txt_error.setToolTipText(s_res.getString("TextIssueInfo"));
		txt_error.setText(s_res.getString("TextIssue"));
		txt_error.addActionListener(this);
		//
		confirmPanel.setLayout(confirmLayout);
		confirmLayout.setAlignment(FlowLayout.RIGHT);
		centerPanel.setLayout(centerLayout);
		metalColorPanel.setBorder(metalColorBorder);
		metalColorPanel.setOpaque(false);
		metalColorPanel.setLayout(metalColorLayout);
		compiereColorPanel.setLayout(compiereColorLayout);
		compiereColorPanel.setBorder(compiereColorBorder);
		compiereColorPanel.setOpaque(false);
		metalColorLayout.setColumns(3);
		metalColorLayout.setHgap(5);
		metalColorLayout.setRows(3);
		metalColorLayout.setVgap(5);
		compiereColorLayout.setColumns(4);
		compiereColorLayout.setHgap(5);
		compiereColorLayout.setRows(2);
		compiereColorLayout.setVgap(5);
		fontPanel.setLayout(fontLayout);
		fontLayout.setColumns(3);
		fontLayout.setHgap(5);
		fontLayout.setRows(2);
		fontLayout.setVgap(5);
		centerLayout.setVgap(5);
		fontBorder.setTitle(s_res.getString("Fonts"));
		confirmPanel.setOpaque(false);
		this.getContentPane().add(confirmPanel, BorderLayout.SOUTH);
		confirmPanel.add(bCancel, null);
		confirmPanel.add(bOK, null);
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(metalColorPanel,  BorderLayout.NORTH);
		metalColorPanel.add(primary1, null);
		metalColorPanel.add(primary2, null);
		metalColorPanel.add(primary3, null);
		metalColorPanel.add(secondary1, null);
		metalColorPanel.add(secondary2, null);
		metalColorPanel.add(secondary3, null);
		metalColorPanel.add(white, null);
		metalColorPanel.add(black, null);
		centerPanel.add(compiereColorPanel, BorderLayout.CENTER);
		compiereColorPanel.add(txt_error, null);
		centerPanel.add(fontPanel, BorderLayout.SOUTH);
		fontPanel.add(controlFont, null);
		fontPanel.add(systemFont, null);
		fontPanel.add(menuFont, null);
		fontPanel.add(userFont, null);
		fontPanel.add(windowFont, null);
		fontPanel.add(smallFont, null);
		compiereColorPanel.add(error, null);
		compiereColorPanel.add(inactive, null);
		compiereColorPanel.add(txt_ok, null);
		compiereColorPanel.add(mandatory, null);
		compiereColorPanel.add(info, null);
		bCancel.addActionListener(this);
		bOK.addActionListener(this);
	}   //  jbInit

	/**
	 *  Load Theme from current Setting (if MetalLookAndFeel)
	 */
	private void loadTheme()
	{
		if (UIManager.getLookAndFeel() instanceof MetalLookAndFeel)
		{
			CompiereTheme.setTheme();
		}
		else    //  Not a Metal Theme
		{
			primary1.setEnabled(false);
			primary2.setEnabled(false);
			primary3.setEnabled(false);
			secondary1.setEnabled(false);
			secondary2.setEnabled(false);
			secondary3.setEnabled(false);
		}
	}   //  loadTheme

	/**
	 *  Dynamic Init
	 */
	private void dynInit()
	{
	//	System.out.println("CompiereThemeEditor.dynInit");
		CompiereTheme ct = CompiereLookAndFeel.getCompiereTheme();
		//  Colors
		primary1.setBackground(ct.getPrimary1());
		primary2.setBackground(ct.getPrimary2());
		primary3.setBackground(ct.getPrimary3());
		secondary1.setBackground(ct.getSecondary1());
		secondary2.setBackground(ct.getSecondary2());
		secondary3.setBackground(ct.getSecondary3());
		//
		white.setBackground(ct.getWhite());
		black.setBackground(ct.getSecondary3());
		black.setForeground(ct.getBlack());
		//
		error.setBackground(CompierePLAF.getFieldBackground_Error());
		mandatory.setBackground(CompierePLAF.getFieldBackground_Mandatory());
		inactive.setBackground(CompierePLAF.getFieldBackground_Inactive());
		info.setBackground(CompierePLAF.getInfoBackground());
		//
		txt_ok.setBackground(CompierePLAF.getFieldBackground_Normal());
		txt_ok.setForeground(CompierePLAF.getTextColor_OK());
		txt_error.setBackground(CompierePLAF.getFieldBackground_Normal());
		txt_error.setForeground(CompierePLAF.getTextColor_Issue());

		//  Fonts
		controlFont.setFont(ct.getControlTextFont());
		systemFont.setFont(ct.getSystemTextFont());
		userFont.setFont(ct.getUserTextFont());
		smallFont.setFont(ct.getSubTextFont());
		menuFont.setFont(ct.getMenuTextFont());
		windowFont.setFont(ct.getWindowTitleFont());
	}   //  dynInit

	/**
	 *  Action Listener
	 *  @param e
	 */
	public void actionPerformed(ActionEvent e)
	{
	//	System.out.println("CompiereThemeEditor.actionPerformed " + e);
		//  Confirm
		if (e.getSource() == bOK)
		{
			CompiereTheme.save();
			dispose();
			return;
		}
		//  Cancel
		else if (e.getSource() == bCancel)
		{
			dispose();
			return;
		}
		CompiereTheme vt = new CompiereThemeBlueMetal();

		try {                   //  to capture errors when Cancel in JColorChooser

		if (e.getSource() == primary1)
		{
			CompiereTheme.primary1 = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("Primary1"), CompiereTheme.primary1));
		}
		else if (e.getSource() == primary2)
		{
			CompiereTheme.primary2 = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("Primary2"), CompiereTheme.primary2));
		}
		else if (e.getSource() == primary3)
		{
			CompiereTheme.primary3 = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("Primary3"), CompiereTheme.primary3));
		}
		else if (e.getSource() == secondary1)
		{
			CompiereTheme.secondary1 = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("Secondary1"), CompiereTheme.secondary1));
		}
		else if (e.getSource() == secondary2)
		{
			CompiereTheme.secondary2 = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("Secondary2"), CompiereTheme.secondary2));
		}
		else if (e.getSource() == secondary3)
		{
			CompiereTheme.secondary3 = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("Secondary3"), CompiereTheme.secondary3));
		}

		else if (e.getSource() == error)
		{
			CompiereTheme.error = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("Error"), CompiereTheme.error));
		}
		else if (e.getSource() == mandatory)
		{
			CompiereTheme.mandatory = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("Mandatory"), CompiereTheme.mandatory));
		}
		else if (e.getSource() == inactive)
		{
			CompiereTheme.inactive = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("Inactive"), CompiereTheme.inactive));
		}
		else if (e.getSource() == info)
		{
			CompiereTheme.info = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("Info"), CompiereTheme.info));
		}

		else if (e.getSource() == black)
		{
			CompiereTheme.black = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("Black"), CompiereTheme.black));
		}
		else if (e.getSource() == white)
		{
			CompiereTheme.white = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("White"), CompiereTheme.white));
		}
		else if (e.getSource() == txt_ok)
		{
			CompiereTheme.txt_ok = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("TextOK"), CompiereTheme.txt_ok));
		}
		else if (e.getSource() == txt_error)
		{
			CompiereTheme.txt_error = new ColorUIResource(JColorChooser.showDialog
				(this, s_res.getString("TextIssue"), CompiereTheme.txt_error));
		}

		else if (e.getSource() == controlFont)
		{
			CompiereTheme.controlFont = new FontUIResource(FontChooser.showDialog
				(this, s_res.getString("ControlFont"), vt.getControlTextFont()));
		}
		else if (e.getSource() == systemFont)
		{
			CompiereTheme.systemFont = new FontUIResource(FontChooser.showDialog
				(this, s_res.getString("SystemFont"), vt.getSystemTextFont()));
		}
		else if (e.getSource() == userFont)
		{
			CompiereTheme.userFont = new FontUIResource(FontChooser.showDialog
				(this, s_res.getString("UserFont"), vt.getUserTextFont()));
		}
		else if (e.getSource() == smallFont)
		{
			CompiereTheme.smallFont = new FontUIResource(FontChooser.showDialog
				(this, s_res.getString("SmallFont"), vt.getSubTextFont()));
		}
		else if (e.getSource() == menuFont)
		{
			CompiereTheme.menuFont = new FontUIResource(FontChooser.showDialog
				(this, s_res.getString("MenuFont"), vt.getMenuTextFont()));
		}
		else if (e.getSource() == windowFont)
		{
			CompiereTheme.windowFont = new FontUIResource(FontChooser.showDialog
				(this, s_res.getString("WindowTitleFont"), vt.getWindowTitleFont()));
		}

		} catch (Exception ee) {}   //  to capture errors when Cancel in JColorChooser

		dynInit();
	}   //  actionPerformed

}   //  CompiereThemeEditor
