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
import java.awt.event.*;
import java.awt.im.*;

import javax.swing.*;
import javax.swing.text.*;

import org.compiere.plaf.CompierePLAF;


/**
 *	Compiere TextPane - A ScrollPane with a JTextPane.
 *  Manages visibility, opaque and color consistently *	
 *  @author Jorg Janke
 *  @version $Id: CTextPane.java,v 1.6 2005/05/28 21:03:44 jjanke Exp $
 */
public class CTextPane extends JScrollPane
	implements CEditor

{
	/**
	 *	Constructs a new TextPane (HTML)
	 */
	public CTextPane()
	{
		this (new JTextPane());
	}	//	CTextPane

	/**
	 *	Constructs a new JTextPane with the given document
	 * 	@param doc  the model to use
	 */
	public CTextPane (StyledDocument doc)
	{
		this (new JTextPane (doc));
	}	//	CTextPane

	/**
	 *  Create a JScrollArea with a JTextEditor
	 *  @param textPane
	 */
	public CTextPane (JTextPane textPane)
	{
		super (textPane);
		m_textPane = textPane;
		super.setOpaque(false);
		super.getViewport().setOpaque(false);
		m_textPane.setContentType("text/html");
		m_textPane.setFont(CompierePLAF.getFont_Field());
		m_textPane.setForeground(CompierePLAF.getTextColor_Normal());
	}   //  CTextPane

	private JTextPane m_textPane = null;

	/*************************************************************************/

	/** Mandatory (default false)   */
	private boolean m_mandatory = false;

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
		if (m_textPane.isEditable() != rw)
			m_textPane.setEditable (rw);
		setBackground(false);
	}   //  setReadWrite

	/**
	 *	Is it possible to edit
	 *  @return true, if editable
	 */
	public boolean isReadWrite()
	{
		return m_textPane.isEditable();
	}   //  isReadWrite

	/**
	 *  Set Background based on editable / mandatory / error
	 *  @param error if true, set background to error color, otherwise mandatory/editable
	 */
	public void setBackground (boolean error)
	{
		if (error)
			setBackground(CompierePLAF.getFieldBackground_Error());
		else if (!isReadWrite())
			setBackground(CompierePLAF.getFieldBackground_Inactive());
		else if (m_mandatory)
			setBackground(CompierePLAF.getFieldBackground_Mandatory());
		else
			setBackground(CompierePLAF.getFieldBackground_Normal());
	}   //  setBackground

	/**
	 * 	Set Background
	 *	@param color color
	 */
	public void setBackground (Color color)
	{
		if (color.equals(getBackground()))
			return;
		if (m_textPane == null)     //  during init
			super.setBackground(color);
		else
			m_textPane.setBackground(color);
	}	//	setBackground
	
	/**
	 * 	Get Background
	 *	@return color
	 */
	public Color getBackground ()
	{
		if (m_textPane == null)     //  during init
			return super.getBackground();
		else
			return m_textPane.getBackground();
	}	//	getBackground
	
	/**
	 * 	Set Foreground
	 *	@param color color
	 */
	public void setForeground (Color color)
	{
		if (m_textPane == null)     //  during init
			super.setForeground(color);
		else
			m_textPane.setForeground(color);
	}	//	setForeground
	
	/**
	 * 	Get Foreground
	 *	@return color
	 */
	public Color getForeground ()
	{
		if (m_textPane == null)     //  during init
			return super.getForeground();
		else
			return m_textPane.getForeground();
	}	//	getForeground

	/**
	 * 	Set Content Type
	 *	@param type e.g. text/html
	 */
	public void setContentType (String type)
	{
		if (m_textPane != null)     //  during init
			m_textPane.setContentType(type);
	}	//	setContentType

	
	/**
	 *	Set Editor to value
	 *  @param value value of the editor
	 */
	public void setValue (Object value)
	{
		if (value == null)
			m_textPane.setText("");
		else
			m_textPane.setText(value.toString());
	}   //  setValue

	/**
	 *	Return Editor value
	 *  @return current value
	 */
	public Object getValue()
	{
		return m_textPane.getText();
	}   //  getValue

	/**
	 *  Return Display Value
	 *  @return displayed String value
	 */
	public String getDisplay()
	{
		return m_textPane.getText();
	}   //  getDisplay

	
	/**************************************************************************
	 *  Set Text and position top
	 *  @param text
	 */
	public void	setText (String text)
	{
		m_textPane.setText(text);
		m_textPane.setCaretPosition(0);
	}
	public String getText()
	{
		return m_textPane.getText();
	}

	public void setCaretPosition (int pos)
	{
		m_textPane.setCaretPosition (pos);
	}
	public int getCaretPosition()
	{
		return m_textPane.getCaretPosition();
	}

	public void setEditable (boolean edit)
	{
		m_textPane.setEditable(edit);
	}
	public boolean isEditable()
	{
		return m_textPane.isEditable();
	}

	public void setOpaque (boolean isOpaque)
	{
		//  JScrollPane & Viewport is always not Opaque
		if (m_textPane == null)     //  during init of JScrollPane
			super.setOpaque(isOpaque);
		else
			m_textPane.setOpaque(isOpaque);
	}   //  setOpaque

	public void addFocusListener (FocusListener l)
	{
		if (m_textPane == null) //  during init
			super.addFocusListener(l);
		else
			m_textPane.addFocusListener(l);
	}
	public void addMouseListener (MouseListener l)
	{
		m_textPane.addMouseListener(l);
	}
	public void addKeyListener (KeyListener l)
	{
		m_textPane.addKeyListener(l);
	}

	public void addInputMethodListener (InputMethodListener l)
	{
		m_textPane.addInputMethodListener(l);
	}
	public InputMethodRequests getInputMethodRequests()
	{
		return m_textPane.getInputMethodRequests();
	}
	public void setInputVerifier (InputVerifier l)
	{
		m_textPane.setInputVerifier(l);
	}

}	//	CTextPane
