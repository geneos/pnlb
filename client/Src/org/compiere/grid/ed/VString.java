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
import java.beans.*;
import javax.swing.*;
import org.compiere.model.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Data Binding:
 *		VEditors call fireVetoableChange(m_columnName, null, getText());
 *		GridController (for Single-Row) and VCellExitor (for Multi-Row)
 *      listen to Vetoable Change Listener (vetoableChange)
 *		then set the value for that column in the current row in the table
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VString.java,v 1.27 2005/12/09 05:17:55 jjanke Exp $
 */
public final class VString extends CTextField
	implements VEditor, ActionListener, FocusListener
{
	public static final int MAXDISPLAY_LENGTH = org.compiere.model.MField.MAXDISPLAY_LENGTH;

	/**
	 *	IDE Bean Constructor for 30 character updateable field
	 */
	public VString()
	{
		this("String", false, false, true, 30, 30, "", null);
	}	//	VString

	/**
	 *	Detail Constructor
	 *  @param columnName column name
	 *  @param mandatory mandatory
	 *  @param isReadOnly read only
	 *  @param isUpdateable updateable
	 *  @param displayLength display length
	 *  @param fieldLength field length
	 *  @param VFormat format
	 */
	public VString (String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		int displayLength, int fieldLength, String VFormat, String ObscureType)
	{
		super(displayLength>MAXDISPLAY_LENGTH ? MAXDISPLAY_LENGTH : displayLength);
		super.setName(columnName);
		m_columnName = columnName;
		if (VFormat == null)
			VFormat = "";
		m_VFormat = VFormat;
		m_fieldLength = fieldLength;
		if (m_VFormat.length() != 0 || m_fieldLength != 0)
			setDocument(new MDocString(m_VFormat, m_fieldLength, this));
		if (m_VFormat.length() != 0)
			setCaret(new VOvrCaret());
		//
		setMandatory(mandatory);
		if (ObscureType != null && ObscureType.length() > 0)
		{
			m_obscure = new Obscure ("", ObscureType);
			m_stdFont = getFont();
			m_obscureFont = new Font("SansSerif", Font.ITALIC, m_stdFont.getSize());
			addFocusListener(this);
		}

		//	Editable
		if (isReadOnly || !isUpdateable)
		{
			setEditable(false);
			setBackground(CompierePLAF.getFieldBackground_Inactive());
		}

		this.addKeyListener(this);
		this.addActionListener(this);
		//	Popup for Editor
		if (fieldLength > displayLength)
		{
			addMouseListener(new VString_mouseAdapter(this));
			mEditor = new CMenuItem (Msg.getMsg(Env.getCtx(), "Editor"), Env.getImageIcon("Editor16.gif"));
			mEditor.addActionListener(this);
			popupMenu.add(mEditor);
		}
		setForeground(CompierePLAF.getTextColor_Normal());
		setBackground(CompierePLAF.getFieldBackground_Normal());
	}	//	VString

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_mField = null;
	}   //  dispose

	JPopupMenu 					popupMenu = new JPopupMenu();
	private CMenuItem 			mEditor;

	private MField      		m_mField = null;

	private String				m_columnName;
	private String				m_oldText;
	private String				m_initialText;
	private String				m_VFormat;
	private int					m_fieldLength;
	/**	Obcure Setting				*/
	private Obscure				m_obscure = null;
	private Font				m_stdFont = null;
	private Font				m_obscureFont = null;
	/**	Setting new value			*/
	private volatile boolean	m_setting = false;
	/**	Field in focus				*/
	private volatile boolean	m_infocus = false;


	/**
	 *	Set Editor to value
	 *  @param value value
	 */
	public void setValue(Object value)
	{
	//	log.config( "VString.setValue", value);
		if (value == null)
			m_oldText = "";
		else
			m_oldText = value.toString();
		//	only set when not updated here
		if (m_setting)
			return;
		setText (m_oldText);
		m_initialText = m_oldText;
		//	If R/O left justify 
		if (!isEditable() || !isEnabled())
			setCaretPosition(0);
	}	//	setValue

	/**
	 *  Property Change Listener
	 *  @param evt event
	 */
	public void propertyChange (PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(org.compiere.model.MField.PROPERTY))
			setValue(evt.getNewValue());
	}   //  propertyChange

	/**
	 *	Return Editor value
	 *  @return value
	 */
	public Object getValue()
	{
		return getText();
	}	//	getValue

	/**
	 *  Return Display Value
	 *  @return value
	 */
	public String getDisplay()
	{
		return super.getText();		// optionally obscured
	}   //  getDisplay


	/**
	 *	Key Released.
	 *	if Escape Restore old Text
	 *  @param e event
	 */
	public void keyReleased(KeyEvent e)
	{
		//  ESC
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			setText(m_initialText);
		m_setting = true;
		try
		{
			String clear = getText();
			fireVetoableChange (m_columnName, m_oldText, clear);
		}
		catch (PropertyVetoException pve)	
		{
		}
		m_setting = false;
	}	//	keyReleased

	/**
	 *	Data Binding to MTable (via GridController)	-	Enter pressed
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(ValuePreference.NAME))
		{
			if (MRole.getDefault().isShowPreference())
				ValuePreference.start (m_mField, getValue());
			return;
		}

		//  Invoke Editor
		if (e.getSource() == mEditor)
		{
			String s = Editor.startEditor(this, Msg.translate(Env.getCtx(), m_columnName), getText(), isEditable());
			setText(s);
		}
		//  Data Binding
		try
		{
			fireVetoableChange(m_columnName, m_oldText, getText());
		}
		catch (PropertyVetoException pve)	
		{
		}
	}	//	actionPerformed

	/**
	 *  Set Field/WindowNo for ValuePreference
	 *  @param mField field
	 */
	public void setField (MField mField)
	{
		m_mField = mField;
		if (m_mField != null
			&& MRole.getDefault().isShowPreference())
			ValuePreference.addMenu (this, popupMenu);
	}   //  setField

	
	/**
	 * 	Set Text (optionally obscured)
	 *	@param text text
	 */
	public void setText (String text)
	{
		if (m_obscure != null && !m_infocus)
		{
			super.setFont(m_obscureFont);
			super.setText (m_obscure.getObscuredValue(text));
			super.setForeground(Color.gray);
		}
		else
		{
			if (m_stdFont != null)
			{
				super.setFont(m_stdFont);
				super.setForeground(CompierePLAF.getTextColor_Normal());
			}
			super.setText (text);
		}
	}	//	setText

	
	/**
	 * 	Get Text (clear)
	 *	@return text
	 */
	public String getText ()
	{
		String text = super.getText();
		if (m_obscure != null && text != null && text.length() > 0)
		{
			if (text.equals(m_obscure.getObscuredValue()))
				text = m_obscure.getClearValue();
		}
		return text;
	}	//	getText

	/**
	 * 	Focus Gained.
	 * 	Enabled with Obscure
	 *	@param e event
	 */
	public void focusGained (FocusEvent e)
	{
		m_infocus = true;
		setText(getText());		//	clear
	}	//	focusGained

	/**
	 * 	Focus Lost
	 * 	Enabled with Obscure
	 *	@param e event
	 */
	public void focusLost (FocusEvent e)
	{
		m_infocus = false;
		setText(getText());		//	obscure
	}	//	focus Lost
	
}	//	VString


/******************************************************************************
 *	Mouse Listener
 */
final class VString_mouseAdapter extends MouseAdapter
{
	/**
	 *	Constructor
	 *  @param adaptee adaptee
	 */
	VString_mouseAdapter(VString adaptee)
	{
		this.adaptee = adaptee;
	}	//	VString_mouseAdapter

	private VString adaptee;

	/**
	 *	Mouse Listener
	 *  @param e event
	 */
	public void mouseClicked(MouseEvent e)
	{
		//	popup menu
		if (SwingUtilities.isRightMouseButton(e))
			adaptee.popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
	}	//	mouseClicked

}	//	VText_mouseAdapter
