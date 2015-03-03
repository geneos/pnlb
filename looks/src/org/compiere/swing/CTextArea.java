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
 *  Compiere TextArea - A ScrollPane with a JTextArea.
 *  Manages visibility, opaque and color consistently
 *
 *  @author     Jorg Janke
 *  @version    $Id: CTextArea.java,v 1.11 2005/12/05 02:38:28 jjanke Exp $
 */
public class CTextArea extends JScrollPane
	implements CEditor
{
	/**
	 * Constructs a new TextArea.  A default model is set, the initial string
	 * is null, and rows/columns are set to 0.
	 */
	public CTextArea()
	{
		this (new JTextArea());
	}	//	CText

	/**
	 * Constructs a new TextArea with the specified text displayed.
	 * A default model is created and rows/columns are set to 0.
	 *
	 * @param text the text to be displayed, or null
	 */
	public CTextArea (String text)
	{
		this (new JTextArea (text));
	}	//	CText

	/**
	 * Constructs a new empty TextArea with the specified number of
	 * rows and columns.  A default model is created, and the initial
	 * string is null.
	 *
	 * @param rows the number of rows >= 0
	 * @param columns the number of columns >= 0
	 * @exception IllegalArgumentException if the rows or columns
	 *  arguments are negative.
	 */
	public CTextArea (int rows, int columns)
	{
		this (new JTextArea (rows, columns));
	}	//	CText

	/**
	 * Constructs a new TextArea with the specified text and number
	 * of rows and columns.  A default model is created.
	 *
	 * @param text the text to be displayed, or null
	 * @param rows the number of rows >= 0
	 * @param columns the number of columns >= 0
	 * @exception IllegalArgumentException if the rows or columns
	 *  arguments are negative.
	 */
	public CTextArea (String text, int rows, int columns)
	{
		this (new JTextArea(text, rows, columns));
	}	//	CText

	/**
	 * Constructs a new JTextArea with the given document model, and defaults
	 * for all of the other arguments (null, 0, 0).
	 *
	 * @param doc  the model to use
	 */
	public CTextArea (Document doc)
	{
		this (new JTextArea (doc));
	}	//	CText

	/**
	 * Constructs a new JTextArea with the specified number of rows
	 * and columns, and the given model.  All of the constructors
	 * feed through this constructor.
	 *
	 * @param doc the model to use, or create a default one if null
	 * @param text the text to be displayed, null if none
	 * @param rows the number of rows >= 0
	 * @param columns the number of columns >= 0
	 * @exception IllegalArgumentException if the rows or columns
	 *  arguments are negative.
	 */
	public CTextArea (Document doc, String text, int rows, int columns)
	{
		this (new JTextArea (doc, text, rows, columns));
	}	//	CTextArea


	/**
	 *  Create a JScrollArea with a JTextArea.
	 *  (use Cpmpiere Colors, Line wrap)
	 *  @param textArea
	 */
	public CTextArea (JTextArea textArea)
	{
		super (textArea);
		m_textArea = textArea;
		super.setOpaque(false);
		super.getViewport().setOpaque(false);
		m_textArea.setFont(CompierePLAF.getFont_Field());
		m_textArea.setForeground(CompierePLAF.getTextColor_Normal());
		m_textArea.setLineWrap(true);
		m_textArea.setWrapStyleWord(true);
		//	Overwrite default Tab
		m_textArea.firePropertyChange("editable", !isEditable(), isEditable());
	}   //  CTextArea

	/**	Text Area					*/
	private JTextArea m_textArea = null;

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
		if (m_textArea.isEditable() != rw)
			m_textArea.setEditable (rw);
		setBackground(false);
	}   //  setReadWrite

	/**
	 *	Is it possible to edit
	 *  @return true, if editable
	 */
	public boolean isReadWrite()
	{
		return m_textArea.isEditable();
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

	public void setBackground (Color color)
	{
		if (color.equals(getBackground()))
			return;
		if (m_textArea == null)     //  during init
			super.setBackground(color);
		else
			m_textArea.setBackground(color);
	}
	public Color getBackground ()
	{
		if (m_textArea == null)     //  during init
			return super.getBackground();
		else
			return m_textArea.getBackground();
	}
	public void setForeground (Color color)
	{
		if (m_textArea == null)     //  during init
			super.setForeground(color);
		else
			m_textArea.setForeground(color);
	}
	public Color getForeground ()
	{
		if (m_textArea == null)     //  during init
			return super.getForeground();
		else
			return m_textArea.getForeground();
	}

	/**
	 *	Set Editor to value
	 *  @param value value of the editor
	 */
	public void setValue (Object value)
	{
		if (value == null)
			m_textArea.setText("");
		else
			m_textArea.setText(value.toString());
	}   //  setValue

	/**
	 *	Return Editor value
	 *  @return current value
	 */
	public Object getValue()
	{
		return m_textArea.getText();
	}   //  getValue

	/**
	 *  Return Display Value
	 *  @return displayed String value
	 */
	public String getDisplay()
	{
		return m_textArea.getText();
	}   //  getDisplay

	/*************************************************************************
	 *  Set Text and position top
	 *  @param text
	 */
	public void	setText (String text)
	{
		m_textArea.setText(text);
		m_textArea.setCaretPosition(0);
	}
	public String getText()
	{
		return m_textArea.getText();
	}
	public void append (String text)
	{
		m_textArea.append (text);
	}

	public void setColumns (int cols)
	{
		m_textArea.setColumns (cols);
	}
	public int getColumns()
	{
		return m_textArea.getColumns();
	}

	public void setRows (int rows)
	{
		m_textArea.setRows(rows);
	}
	public int getRows()
	{
		return m_textArea.getRows();
	}

	public void setCaretPosition (int pos)
	{
		m_textArea.setCaretPosition (pos);
	}
	public int getCaretPosition()
	{
		return m_textArea.getCaretPosition();
	}

	public void setEditable (boolean edit)
	{
		m_textArea.setEditable(edit);
	}
	public boolean isEditable()
	{
		return m_textArea.isEditable();
	}

	public void setLineWrap (boolean wrap)
	{
		m_textArea.setLineWrap (wrap);
	}
	public void setWrapStyleWord (boolean word)
	{
		m_textArea.setWrapStyleWord (word);
	}

	public void setOpaque (boolean isOpaque)
	{
		//  JScrollPane & Viewport is always not Opaque
		if (m_textArea == null)     //  during init of JScrollPane
			super.setOpaque(isOpaque);
		else
			m_textArea.setOpaque(isOpaque);
	}   //  setOpaque

	public void addFocusListener (FocusListener l)
	{
		if (m_textArea == null) //  during init
			super.addFocusListener(l);
		else
			m_textArea.addFocusListener(l);
	}
	public void addMouseListener (MouseListener l)
	{
		m_textArea.addMouseListener(l);
	}
	public void addKeyListener (KeyListener l)
	{
		m_textArea.addKeyListener(l);
	}

	public void addInputMethodListener (InputMethodListener l)
	{
		m_textArea.addInputMethodListener(l);
	}
	public InputMethodRequests getInputMethodRequests()
	{
		return m_textArea.getInputMethodRequests();
	}
	public void setInputVerifier (InputVerifier l)
	{
		m_textArea.setInputVerifier(l);
	}

}   //  CTextArea
