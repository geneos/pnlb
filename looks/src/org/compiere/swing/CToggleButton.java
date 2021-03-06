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

import java.awt.Color;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;

import org.compiere.plaf.CompiereColor;
import org.compiere.plaf.CompierePLAF;
import org.compiere.util.Trace;

/**
 *  Compiere Color Taggle Button
 *
 *  @author     Jorg Janke
 *  @version    $Id: CToggleButton.java,v 1.9 2005/10/09 19:01:37 jjanke Exp $
 */
public class CToggleButton extends JToggleButton implements CEditor
{
	/**
	 * Creates an initially unselected toggle button
	 * without setting the text or image.
	 */
	public CToggleButton () 
	{
		this(null, null, false);
	}

	/**
	 * Creates an initially unselected toggle button
	 * with the specified image but no text.
	 *
	 * @param icon  the image that the button should display
	 */
	public CToggleButton(Icon icon) 
	{
		this(null, icon, false);
	}

	/**
	 * Creates a toggle button with the specified image
	 * and selection state, but no text.
	 *
	 * @param icon  the image that the button should display
	 * @param selected  if true, the button is initially selected;
	 *                  otherwise, the button is initially unselected
	 */
	public CToggleButton(Icon icon, boolean selected) 
	{
		this(null, icon, selected);
	}

	/**
	 * Creates an unselected toggle button with the specified text.
	 *
	 * @param text  the string displayed on the toggle button
	 */
	public CToggleButton (String text) 
	{
		this(text, null, false);
	}

	/**
	 * Creates a toggle button with the specified text
	 * and selection state.
	 *
	 * @param text  the string displayed on the toggle button
	 * @param selected  if true, the button is initially selected;
	 *                  otherwise, the button is initially unselected
	 */
	public CToggleButton (String text, boolean selected) 
	{
		this(text, null, selected);
	}

	/**
	 * Creates a toggle button where properties are taken from the
	 * Action supplied.
	 * @param a
	 */
	public CToggleButton(Action a) 
	{
		this(null, null, false);
		setAction(a);
	}

	/**
	 * Creates a toggle button that has the specified text and image,
	 * and that is initially unselected.
	 *
	 * @param text the string displayed on the button
	 * @param icon  the image that the button should display
	 */
	public CToggleButton(String text, Icon icon) 
	{
		this(text, icon, false);
	}

	/**
	 * Creates a toggle button with the specified text, image, and
	 * selection state.
	 *
	 * @param text the text of the toggle button
	 * @param icon  the image that the button should display
	 * @param selected  if true, the button is initially selected;
	 *                  otherwise, the button is initially unselected
	 */
	public CToggleButton (String text, Icon icon, boolean selected)
	{
		super(text, icon, selected);
		setContentAreaFilled(false);
		setOpaque(false);
		//
		setFont(CompierePLAF.getFont_Label());
		setForeground(CompierePLAF.getTextColor_Label());
	}

	/*************************************************************************/

	/**
	 *  Set Background - Differentiates between system & user call.
	 *  If User Call, sets Opaque & ContextAreaFilled to true
	 *  @param bg
	 */
	public void setBackground(Color bg)
	{
		if (bg.equals(getBackground()))
			return;
		super.setBackground( bg);
		//  ignore calls from javax.swing.LookAndFeel.installColors(LookAndFeel.java:61)
		if (!Trace.getCallerClass(1).startsWith("javax"))
		{
			setOpaque(true);
			setContentAreaFilled(true);
		}
	}   //  setBackground

	/**
	 *  Set Background - NOP
	 *  @param error
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
		if (super.isEnabled() != rw)
			super.setEnabled(rw);
	}   //  setReadWrite

	/**
	 *	Is it possible to edit
	 *  @return true, if editable
	 */
	public boolean isReadWrite()
	{
		return super.isEnabled();
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

}   //  CToggleButton
