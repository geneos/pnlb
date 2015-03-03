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
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Location Control (Address)
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VLocation.java,v 1.28 2005/12/09 05:17:55 jjanke Exp $
 */
@SuppressWarnings("serial")
public class VLocation extends JComponent
	implements VEditor, ActionListener
{
	/**
	 *	Constructor
	 *
	 * 	@param columnName column name
	 * 	@param mandatory mandatory
	 * 	@param isReadOnly read only
	 * 	@param isUpdateable updateable
	 * 	@param mLocation location model
	 */
	public VLocation(String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		MLocationLookup mLocation)
	{
		super();
		super.setName(columnName);
		m_columnName = columnName;
		m_mLocation = mLocation;
		//
		LookAndFeel.installBorder(this, "TextField.border");
		this.setLayout(new BorderLayout());
		//  Size
		this.setPreferredSize(m_text.getPreferredSize());		//	causes r/o to be the same length
		int height = m_text.getPreferredSize().height;

		//  Button
		m_button.setIcon(Env.getImageIcon("Location10.gif"));
		m_button.setMargin(new Insets(0,0,0,0));
		m_button.setPreferredSize(new Dimension(height, height));
		m_button.addActionListener(this);
		this.add(m_button, BorderLayout.EAST);
		//	***	Button & Text	***
		m_text.setBorder(null);
		m_text.setEditable(false);
		m_text.setFocusable(false);
		m_text.setFont(CompierePLAF.getFont_Field());
		m_text.setForeground(CompierePLAF.getTextColor_Normal());
		m_text.addMouseListener(new VLocation_mouseAdapter(this));
		this.add(m_text, BorderLayout.CENTER);

		//	Editable
		if (isReadOnly || !isUpdateable)
			setReadWrite (false);
		else
			setReadWrite (true);
		setMandatory (mandatory);
		//
		mDelete = new CMenuItem(Msg.getMsg(Env.getCtx(), "Delete"), Env.getImageIcon ("Delete16.gif"));
		mDelete.addActionListener(this);
		popupMenu.add(mDelete);

	}	//	VLocation

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_text = null;
		m_button = null;
		m_mLocation = null;
	}   //  dispose

	/** The Text Field                  */
	private JTextField			m_text = new JTextField(VLookup.DISPLAY_LENGTH);
	/** The Button                      */
	private CButton				m_button = new CButton();

	private MLocationLookup		m_mLocation;
	private MLocation			m_value;

	private String				m_columnName;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VLocation.class);

	//	Popup
	JPopupMenu 					popupMenu = new JPopupMenu();
	private CMenuItem 			mDelete;

	/**
	 *	Enable/disable
	 *  @param value true if ReadWrite
	 */
	public void setReadWrite (boolean value)
	{
		m_button.setReadWrite (value);
		if (m_button.isVisible() != value)
			m_button.setVisible (value);
		setBackground(false);
	}	//	setReadWrite

	/**
	 *	IsReadWrite
	 *  @return value true if ReadWrite
	 */
	public boolean isReadWrite()
	{
		return m_button.isReadWrite();
	}	//	isReadWrite

	/**
	 *	Set Mandatory (and back bolor)
	 *  @param mandatory true if mandatory
	 */
	public void setMandatory (boolean mandatory)
	{
		m_button.setMandatory(mandatory);
		setBackground(false);
	}	//	setMandatory

	/**
	 *	Is it mandatory
	 *  @return true if mandatory
	 */
	public boolean isMandatory()
	{
		return m_button.isMandatory();
	}	//	isMandatory

	/**
	 *	Set Background
	 *  @param color color
	 */
	public void setBackground (Color color)
	{
		if (!color.equals(m_text.getBackground()))
			m_text.setBackground(color);
	}	//	setBackground

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
		else if (isMandatory())
			setBackground(CompierePLAF.getFieldBackground_Mandatory());
		else
			setBackground(CompierePLAF.getFieldBackground_Normal());
	}   //  setBackground

	/**
	 *  Set Foreground
	 *  @param fg color
	 */
	public void setForeground(Color fg)
	{
		m_text.setForeground(fg);
	}   //  setForeground

	/**
	 *	Set Editor to value
	 *  @param value value
	 */
	public void setValue(Object value)
	{
		if (value == null)
		{
			m_value = null;
			m_text.setText(null);
		}
		else
		{
			m_value = m_mLocation.getLocation(value, null);
			if (m_value == null)
				m_text.setText("<" + value + ">");
			else
				m_text.setText(m_value.toString());
		}
	}	//	setValue

	/**
	 *  Property Change Listener
	 *  @param evt PropertyChangeEvent
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
		if (m_value == null)
			return null;
		return new Integer(m_value.getC_Location_ID());
	}	//	getValue

	/**
	 *	Return Editor value
	 *  @return value
	 */
	public int getC_Location_ID()
	{
		if (m_value == null)
			return 0;
		return m_value.getC_Location_ID();
	}	//	getC_Location_ID

	/**
	 *  Return Display Value
	 *  @return display value
	 */
	public String getDisplay()
	{
		return m_text.getText();
	}   //  getDisplay

	/**
	 *	ActionListener - Button - Start Dialog
	 *  @param e ActionEvent
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == mDelete)
			m_value = null;        //  create new
		//
		log.config( "actionPerformed - " + m_value);
		VLocationDialog ld = new VLocationDialog(Env.getFrame(this),
			Msg.getMsg(Env.getCtx(), "Location"), m_value);
		ld.setVisible(true);
		m_value = ld.getValue();
		//
		if (e.getSource() == mDelete)
			;
		else if (!ld.isChanged())
			return;

		//	Data Binding
		try
		{
			int C_Location_ID = 0;
			if (m_value != null)
				C_Location_ID = m_value.getC_Location_ID();
			Integer ii = new Integer(C_Location_ID);
			//  force Change - user does not realize that embedded object is already saved.
			fireVetoableChange(m_columnName, null, null);   //  resets m_mLocation
			if (C_Location_ID != 0)
				fireVetoableChange(m_columnName, null, ii);
			setValue(ii);
		}
		catch (PropertyVetoException pve)
		{
			log.log(Level.SEVERE, "VLocation.actionPerformed", pve);
		}

	}	//	actionPerformed

	/**
	 *  Action Listener Interface
	 *  @param listener listener
	 */
	public void addActionListener(ActionListener listener)
	{
		m_text.addActionListener(listener);
	}   //  addActionListener

	/**
	 *  Set Field/WindowNo for ValuePreference (NOP)
	 *  @param mField Model Field
	 */
	public void setField (org.compiere.model.MField mField)
	{
	}   //  setField

}	//	VLocation

/*****************************************************************************/

/**
 *	Mouse Listener for Popup Menu
 */
final class VLocation_mouseAdapter extends java.awt.event.MouseAdapter
{
	/**
	 *	Constructor
	 *  @param adaptee adaptee
	 */
	VLocation_mouseAdapter(VLocation adaptee)
	{
		this.adaptee = adaptee;
	}	//	VLookup_mouseAdapter

	private VLocation adaptee;

	/**
	 *	Mouse Listener
	 *  @param e MouseEvent
	 */
	public void mouseClicked(MouseEvent e)
	{
		//	popup menu
		if (SwingUtilities.isRightMouseButton(e))
			adaptee.popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
	}	//	mouse Clicked

}	//	VLocation_mouseAdapter
