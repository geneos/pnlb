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

import java.awt.event.*;
import java.beans.*;

import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.model.*;


/**
 *	Data Binding:
 *		VEditors call fireVetoableChange(m_columnName, null, getText());
 *		GridController (for Single-Row) and VCellExitor (for Multi-Row)
 *      listen to Vetoable Change Listener (vetoableChange)
 *		then set the value for that column in the current row in the table
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VPassword.java,v 1.12 2005/03/11 20:28:25 jjanke Exp $
 */
public final class VPassword extends CPassword
	implements VEditor, KeyListener, ActionListener
{
	/**
	 *	IDE Bean Constructor for 30 character updateable field
	 */
	public VPassword()
	{
		this("Password", false, false, true, 30, 30, "");
	}	//	VPassword

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
	public VPassword (String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		int displayLength, int fieldLength, String VFormat)
	{
		super (displayLength>VString.MAXDISPLAY_LENGTH ? VString.MAXDISPLAY_LENGTH : displayLength);
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

		//	Editable
		if (isReadOnly || !isUpdateable)
		{
			setEditable(false);
			setBackground(CompierePLAF.getFieldBackground_Inactive());
		}

		this.addKeyListener(this);
		this.addActionListener(this);

		setForeground(CompierePLAF.getTextColor_Normal());
		setBackground(CompierePLAF.getFieldBackground_Normal());
	}	//	VPassword

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_mField = null;
	}   //  dispose

	private MField				m_mField = null;

	private String				m_columnName;
	private String				m_oldText;
	private String				m_VFormat;
	private int					m_fieldLength;
	private volatile boolean	m_setting = false;

	/**
	 *	Set Editor to value
	 *  @param value value
	 */
	public void setValue (Object value)
	{
		if (value == null)
			m_oldText = "";
		else
			m_oldText = value.toString();
		if (!m_setting)
			setText (m_oldText);
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
		return String.valueOf(getPassword());
	}	//	getValue

	/**
	 *  Return Display Value
	 *  @return value
	 */
	public String getDisplay()
	{
		return String.valueOf(getPassword());
	}   //  getDisplay

	/**************************************************************************
	 *	Key Listener Interface
	 *  @param e event
	 */
	public void keyTyped(KeyEvent e)	{}
	public void keyPressed(KeyEvent e)	{}

	/**
	 *	Key Listener.
	 *  @param e event
	 */
	public void keyReleased(KeyEvent e)
	{
		String newText = String.valueOf(getPassword());
		m_setting = true;
		try
		{
			fireVetoableChange(m_columnName, m_oldText, newText);
		}
		catch (PropertyVetoException pve)	{}
		m_setting = false;
	}	//	keyReleased

	/**
	 *	Data Binding to MTable (via GridController)	-	Enter pressed
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		String newText = String.valueOf(getPassword());
		//  Data Binding
		try
		{
			fireVetoableChange(m_columnName, m_oldText, newText);
		}
		catch (PropertyVetoException pve)	{}
	}	//	actionPerformed

	/**
	 *  Set Field/WindowNo for ValuePreference
	 *  @param mField field
	 */
	public void setField (MField mField)
	{
		m_mField = mField;
	}   //  setField

}	//	VPassword

