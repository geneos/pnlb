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
package org.compiere.swing;

import java.awt.*;
import javax.swing.*;

import org.compiere.plaf.CompiereColor;
import org.compiere.plaf.CompierePLAF;
import org.compiere.util.*;
;

/**
 *  Compiere Button supporting colored Background
 *
 *  @author     Jorg Janke
 *  @version    $Id: CButton.java,v 1.17 2005/12/05 02:38:28 jjanke Exp $
 */
public class CButton extends JButton implements CEditor
{
	/**
	 * Creates a button with no set text or icon.
	 */
	public CButton()
	{
		this (null, null);
	}	//	CButton

	/**
	 * Creates a button with an icon.
	 *
	 * @param icon  the Icon image to display on the button
	 */
	public CButton(Icon icon)
	{
		this (null, icon);
	}	//	CButton

	/**
	 * Creates a button with text.
	 *
	 * @param text  the text of the button
	 */
	public CButton(String text)
	{
		this (text, null);
	}	//	CButton

	/**
	 * Creates a button where properties are taken from the
	 * <code>Action</code> supplied.
	 *
	 * @param a the <code>Action</code> used to specify the new button
	 *
	 * @since 1.3
	 */
	public CButton (Action a)
	{
		super (a);
		setContentAreaFilled(false);
		setOpaque(false);
	}	//	CButton

	/**
	 * Creates a button with initial text and an icon.
	 *
	 * @param text  the text of the button
	 * @param icon  the Icon image to display on the button
	 */
	public CButton(String text, Icon icon)
	{
		super (text, icon);        
		setContentAreaFilled(false);
		setOpaque(false);
		//
		setFont(CompierePLAF.getFont_Label());
		setForeground(CompierePLAF.getTextColor_Label());
	}   //  CButton

	
	/**************************************************************************
	 *  Set Background - Differentiates between system & user call.
	 *  If User Call, sets Opaque & ContextAreaFilled to true
	 *  @param bg background color
	 */
	public void setBackground (Color bg)
	{
		if (bg.equals(getBackground()))
			return;
		super.setBackground (bg);
		//  ignore calls from javax.swing.LookAndFeel.installColors(LookAndFeel.java:61)
		if (!Trace.getCallerClass(1).startsWith("javax"))
		{
			setOpaque(true);
			setContentAreaFilled(true);
		}
		this.repaint();
	}   //  setBackground

	/**
	 *  Set Background - NOP
	 *  @param error error
	 */
	public void setBackground (boolean error)
	{
	}   //  setBackground

	/**
	 *  Set Standard Background
	 */
	public void setBackgroundColor ()
	{
		setBackgroundColor (null);
	}   //  setBackground

	/**
	 *  Set Background
	 *  @param bg CompiereColor for Background, if null set standard background
	 */
	public void setBackgroundColor (CompiereColor bg)
	{
		if (bg == null)
			bg = CompiereColor.getDefaultBackground();
		setOpaque(true);
		putClientProperty(CompierePLAF.BACKGROUND, bg);
		super.setBackground (bg.getFlatColor());
		this.repaint();
	}   //  setBackground

	/**
	 *  Get Background
	 *  @return Color for Background
	 */
	public CompiereColor getBackgroundColor ()
	{
		try
		{
			return (CompiereColor)getClientProperty(CompierePLAF.BACKGROUND);
		}
		catch (Exception e)
		{
			System.err.println("CButton - ClientProperty: " + e.getMessage());
		}
		return null;
	}   //  getBackgroundColor

	/** Mandatory (default false)   */
	private boolean m_mandatory = false;
	/** Read-Write                  */
	private boolean m_readWrite = true;

	/**
	 *	Set Editor Mandatory
	 *  @param mandatory true, if you have to enter data
	 */
	public void setMandatory (boolean mandatory)
	{
		m_mandatory = mandatory;
		setBackground(false);
	}   //  setMandatory

	/**
	 *	Is Field mandatory
	 *  @return true, if mandatory
	 */
	public boolean isMandatory()
	{
		return m_mandatory;
	}   //  isMandatory

	/**
	 *	Enable Editor
	 *  @param rw true, if you can enter/select data
	 */
	public void setReadWrite (boolean rw)
	{
		if (isEnabled() != rw)
			setEnabled(rw);
		m_readWrite = rw;
	}   //  setReadWrite

	/**
	 *	Is it possible to edit
	 *  @return true, if editable
	 */
	public boolean isReadWrite()
	{
		return m_readWrite;
	}   //  isReadWrite

	/**
	 *	Set Editor to value
	 *  @param value value of the editor
	 */
	public void setValue (Object value)
	{
		if (value == null)
			setText("");
		else
			setText(value.toString());
	}   //  setValue

	/**
	 *	Return Editor value
	 *  @return current value
	 */
	public Object getValue()
	{
		return getText();
	}   //  getValue

	/**
	 *  Return Display Value
	 *  @return displayed String value
	 */
	public String getDisplay()
	{
		return getText();
	}   //  getDisplay

	/**
	 *	Set Text & Mnemonic
	 *	@param text text
	 */
	public void setText (String text)
	{
		if (text == null)
		{
			super.setText(text);
			return;
		}
		int pos = text.indexOf("&");
		if (pos != -1)					//	We have a nemonic - creates ALT-_
		{
			int mnemonic = text.toUpperCase().charAt(pos+1);
			if (mnemonic != ' ')
			{
				setMnemonic(mnemonic);
				text = text.substring(0, pos) + text.substring(pos+1);
			}
		}
		super.setText (text);
		if (getName() == null)
			setName (text);
	}	//	setText
	
	/**
	 * 	Set Tool Tip Text & Mnemonic
	 *	@param text text
	 */
	public void setToolTipText (String text)
	{
		if (text == null)
		{
			super.setText(text);
			return;
		}
		int pos = text.indexOf("&");
		if (pos != -1)					//	We have a nemonic - creates ALT-_
		{
			int mnemonic = text.toUpperCase().charAt(pos+1);
			if (mnemonic != ' ')
			{
				setMnemonic(mnemonic);
				text = text.substring(0, pos) + text.substring(pos+1);
			}
		}
		super.setToolTipText (text);
		if (getName() == null)
			setName (text);
	}	//	setToolTipText
	
	/**
	 * 	Set Action Command
	 *	@param actionCommand command 
	 */
	public void setActionCommand (String actionCommand)
	{
		super.setActionCommand (actionCommand);
		if (getName() == null && actionCommand != null && actionCommand.length() > 0)
			setName(actionCommand);
	}	//	setActionCommand
	
}   //  CButton
