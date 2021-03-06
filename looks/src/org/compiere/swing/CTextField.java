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

import javax.swing.*;
import javax.swing.text.*;

import org.compiere.plaf.CompierePLAF;

/**
 *  Compiere Text Field
 *
 *  @author     Jorg Janke
 *  @version    $Id: CTextField.java,v 1.12 2005/03/11 20:34:38 jjanke Exp $
 */
public class CTextField extends JTextField 
	implements CEditor, KeyListener
{
	/**
	 * Constructs a new <code>TextField</code>.  A default model is created,
	 * the initial string is <code>null</code>,
	 * and the number of columns is set to 0.
	 */
	public CTextField()
	{
		super();
		init();
	}   //  CTextField

	/**
	 * Constructs a new <code>TextField</code> initialized with the
	 * specified text. A default model is created and the number of
	 * columns is 0.
	 *
	 * @param text the text to be displayed, or <code>null</code>
	 */
	public CTextField (String text)
	{
		super (text);
		init();
	}   //  CTextField

	/**
	 * Constructs a new empty <code>TextField</code> with the specified
	 * number of columns.
	 * A default model is created and the initial string is set to
	 * <code>null</code>.
	 *
	 * @param columns  the number of columns to use to calculate
	 *   the preferred width; if columns is set to zero, the
	 *   preferred width will be whatever naturally results from
	 *   the component implementation
	 */
	public CTextField (int columns)
	{
		super (columns);
		init();
	}   //  CTextField

	/**
	 * Constructs a new <code>TextField</code> initialized with the
	 * specified text and columns.  A default model is created.
	 *
	 * @param text the text to be displayed, or <code>null</code>
	 * @param columns  the number of columns to use to calculate
	 *   the preferred width; if columns is set to zero, the
	 *   preferred width will be whatever naturally results from
	 *   the component implementation
	 */
	public CTextField (String text, int columns)
	{
		super (text, columns);
		init();
	}   //  CTextField

	/**
	 * Constructs a new <code>JTextField</code> that uses the given text
	 * storage model and the given number of columns.
	 * This is the constructor through which the other constructors feed.
	 * If the document is <code>null</code>, a default model is created.
	 *
	 * @param doc  the text storage to use; if this is <code>null</code>,
	 *		a default will be provided by calling the
	 *		<code>createDefaultModel</code> method
	 * @param text  the initial string to display, or <code>null</code>
	 * @param columns  the number of columns to use to calculate
	 *   the preferred width >= 0; if <code>columns</code>
	 *   is set to zero, the preferred width will be whatever
	 *   naturally results from the component implementation
	 * @exception IllegalArgumentException if <code>columns</code> < 0
	 */
	public CTextField (Document doc, String text, int columns)
	{
		super (doc, text, columns);
		init();
	}   //  CTextField

	/**
	 *  Initialization
	 */
	private void init()
	{
		setFont(CompierePLAF.getFont_Field());
		setForeground(CompierePLAF.getTextColor_Normal());
		setBackground (false);
		//	Minimum Size
		Dimension size = getPreferredSize();
		if (size != null)
			size = new Dimension (20,10);
		size.width = 30;
		setMinimumSize(size);
	}   //  init

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
		if (super.isEditable() != rw)
			super.setEditable (rw);
		setBackground(false);
	}   //  setEditable

	/**
	 *	Is it possible to edit
	 *  @return true, if editable
	 */
	public boolean isReadWrite()
	{
		return super.isEditable();
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
	 *  Set Background
	 *  @param bg background
	 */
	public void setBackground (Color bg)
	{
		if (bg.equals(getBackground()))
			return;
		super.setBackground(bg);
	}   //  setBackground

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
	 * 	key Pressed
	 *	@see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 *	@param e
	 */
	public void keyPressed(KeyEvent e)
	{
	}	//	keyPressed

	/**
	 * 	key Released
	 *	@see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 *	@param e
	 */
	public void keyReleased(KeyEvent e)
	{
	}	//	keyReleased

	/**
	 * 	key Typed
	 *	@see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 *	@param e
	 */
	public void keyTyped(KeyEvent e)
	{
	}	//	keyTyped

}   //  CTextField
