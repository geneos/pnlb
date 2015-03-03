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
import java.sql.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Warehouse Locator Control
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VLocator.java,v 1.23 2005/12/09 05:17:55 jjanke Exp $
 */
public class VLocator extends JComponent
	implements VEditor, ActionListener
{
	/**
	 *  IDE Constructor
	 */
	public VLocator ()
	{
		this("M_Locator_ID", false, false, true, null, 0);
	}   //  VLocator

	/**
	 *	Constructor
	 *
	 * 	@param columnName ColumnName
	 *	@param mandatory mandatory
	 *	@param isReadOnly read only
	 *	@param isUpdateable updateable
	 *	@param mLocator locator (lookup) model
	 * 	@param WindowNo window no
	 */
	public VLocator(String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		MLocatorLookup mLocator, int WindowNo)
	{
		super();
		super.setName(columnName);
		m_columnName = columnName;
		m_mLocator = mLocator;
		m_WindowNo = WindowNo;
		//
		LookAndFeel.installBorder(this, "TextField.border");
		this.setLayout(new BorderLayout());
		//  Size
		this.setPreferredSize(m_text.getPreferredSize());		//	causes r/o to be the same length
		int height = m_text.getPreferredSize().height;

		//	***	Button & Text	***
		m_text.setBorder(null);
		m_text.setEditable(true);
		m_text.setFocusable(true);
		m_text.addMouseListener(new VLocator_mouseAdapter(this));	//	popup
		m_text.setFont(CompierePLAF.getFont_Field());
		m_text.setForeground(CompierePLAF.getTextColor_Normal());
		m_text.addActionListener(this);
		this.add(m_text, BorderLayout.CENTER);

		m_button.setIcon(new ImageIcon(org.compiere.Compiere.class.getResource("images/Locator10.gif")));
		m_button.setMargin(new Insets(0, 0, 0, 0));
		m_button.setPreferredSize(new Dimension(height, height));
		m_button.addActionListener(this);
		this.add(m_button, BorderLayout.EAST);

		//	Prefereed Size
		this.setPreferredSize(this.getPreferredSize());		//	causes r/o to be the same length

		//	ReadWrite
		if (isReadOnly || !isUpdateable)
			setReadWrite (false);
		else
			setReadWrite (true);
		setMandatory (mandatory);
		//
		mZoom = new CMenuItem(Msg.getMsg(Env.getCtx(), "Zoom"), Env.getImageIcon("Zoom16.gif"));
		mZoom.addActionListener(this);
		popupMenu.add(mZoom);
		mRefresh = new CMenuItem(Msg.getMsg(Env.getCtx(), "Refresh"), Env.getImageIcon("Refresh16.gif"));
		mRefresh.addActionListener(this);
		popupMenu.add(mRefresh);
	}	//	VLocator

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_text = null;
		m_button = null;
		m_mLocator = null;
	}   //  dispose

	private JTextField			m_text = new JTextField (VLookup.DISPLAY_LENGTH);
	private CButton				m_button = new CButton();
	private MLocatorLookup		m_mLocator;
	private Object				m_value;
	//
	private String				m_columnName;
	private int					m_WindowNo;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VLocator.class);
	//	Popup
	JPopupMenu 					popupMenu = new JPopupMenu();
	private CMenuItem 			mZoom;
	private CMenuItem 			mRefresh;

	/**
	 *	Enable/disable
	 *  @param value r/w
	 */
	public void setReadWrite (boolean value)
	{
		m_button.setReadWrite(value);
		if (m_button.isVisible() != value)
			m_button.setVisible(value);
		setBackground(false);
	}	//	setReadWrite

	/**
	 *	IsReadWrite
	 *  @return true if ReadWrite
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
	 *  @param value Integer
	 */
	public void setValue(Object value)
	{
		setValue (value, false);
	}	//	setValue

	/**
	 * 	Set Value
	 *	@param value value
	 *	@param fire data binding
	 */
	private void setValue (Object value, boolean fire)
	{
		if (value != null)
		{
			m_mLocator.setOnly_Warehouse_ID (getOnly_Warehouse_ID ());
			if (!m_mLocator.isValid(value))
				value = null;
		}
		//
		m_value = value;
		m_text.setText(m_mLocator.getDisplay(value));	//	loads value

		//	Data Binding
		try
		{
			fireVetoableChange(m_columnName, null, value);
		}
		catch (PropertyVetoException pve)
		{
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
		if (getM_Locator_ID() == 0)
			return null;
		return m_value;
	}	//	getValue
	
	/**
	 * 	Get M_Locator_ID
	 *	@return id
	 */
	public int getM_Locator_ID()
	{
		if (m_value != null 
			&& m_value instanceof Integer)
			return ((Integer)m_value).intValue();
		return 0;
	}	//	getM_Locator_ID

	/**
	 *  Return Display Value
	 *  @return display value
	 */
	public String getDisplay()
	{
		return m_text.getText();
	}   //  getDisplay

	/**
	 *	ActionListener
	 *  @param e ActionEvent
	 */
	public void actionPerformed(ActionEvent e)
	{
		//	Refresh
		if (e.getSource() == mRefresh)
		{
			m_mLocator.refresh();
			return;
		}

		//	Zoom to M_Warehouse
		if (e.getSource() == mZoom)
		{
			actionZoom();
			return;
		}

		//	Warehouse
		int only_Warehouse_ID = getOnly_Warehouse_ID();
		log.config("Only Wharehouse_ID=" + only_Warehouse_ID);

		//	Text Entry ok
		if (e.getSource() == m_text && actionText(only_Warehouse_ID))
				return;

		//	 Button - Start Dialog
		int M_Locator_ID = 0;
		if (m_value instanceof Integer)
			M_Locator_ID = ((Integer)m_value).intValue();
		//
		m_mLocator.setOnly_Warehouse_ID(only_Warehouse_ID);
		VLocatorDialog ld = new VLocatorDialog(Env.getFrame(this),
			Msg.translate(Env.getCtx(), m_columnName),
			m_mLocator, M_Locator_ID, isMandatory(), only_Warehouse_ID);
		//	display
		ld.setVisible(true);
		m_mLocator.setOnly_Warehouse_ID(0);

		//	redisplay
		if (!ld.isChanged())
			return;
		setValue (ld.getValue(), true);
	}	//	actionPerformed

	/**
	 * 	Hit Enter in Text Field
	 * 	@param only_Warehouse_ID if not 0 restrict warehouse
	 * 	@return true if found
	 */
	private boolean actionText(int only_Warehouse_ID)
	{
		String text = m_text.getText();
		log.fine(text);
		if (text == null || text.length() == 0)
		{
			if (isMandatory())
				return false;
			else
			{
				setValue (null, true);
				return true;
			}
		}
		if (text.endsWith("%"))
			text = text.toUpperCase();
		else
			text = text.toUpperCase() + "%";
		//	Look up
		int M_Locator_ID = 0;
		String sql = "SELECT M_Locator_ID FROM M_Locator WHERE UPPER(Value) LIKE "
			+ DB.TO_STRING(text);
		if (only_Warehouse_ID != 0)
			sql += " AND M_Warehouse_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			if (only_Warehouse_ID != 0)
				pstmt.setInt(1, only_Warehouse_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				M_Locator_ID = rs.getInt(1);
				if (rs.next())
					M_Locator_ID = 0;	//	more than one
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		if (M_Locator_ID == 0)
			return false;

		setValue (new Integer(M_Locator_ID), true);
		return true;
	}	//	actionText

	/**
	 *  Action Listener Interface
	 *  @param listener listener
	 */
	public void addActionListener(ActionListener listener)
	{
		m_text.addActionListener(listener);
	}   //  addActionListener


	/**
	 *	Action - Zoom
	 */
	private void actionZoom()
	{
		int AD_Window_ID = 139;				//	hardcoded
		log.info("");
		//
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		AWindow frame = new AWindow();
		if (!frame.initWindow(AD_Window_ID, null))
			return;
		AEnv.showCenterScreen(frame);
		frame = null;
		setCursor(Cursor.getDefaultCursor());
	}	//	actionZoom

	/**
	 *  Set Field/WindowNo for ValuePreference (NOP)
	 *  @param mField Model Field
	 */
	public void setField (org.compiere.model.MField mField)
	{
	}   //  setField


	/**
	 * 	Get Warehouse restriction if any.
	 *	@return	M_Warehouse_ID or 0
	 */
	private int getOnly_Warehouse_ID()
	{
		String only_Warehouse = Env.getContext(Env.getCtx(), m_WindowNo, "M_Warehouse_ID", true);
		int only_Warehouse_ID = 0;
		try
		{
			if (only_Warehouse != null && only_Warehouse.length () > 0)
				only_Warehouse_ID = Integer.parseInt (only_Warehouse);
		}
		catch (Exception ex)
		{
		}
		return only_Warehouse_ID;
	}	//	getOnly_Warehouse_ID

}	//	VLocator

/*****************************************************************************/

/**
 *	Mouse Listener for Popup Menu
 */
final class VLocator_mouseAdapter extends java.awt.event.MouseAdapter
{
	/**
	 *	Constructor
	 *  @param adaptee VLocator
	 */
	VLocator_mouseAdapter(VLocator adaptee)
	{
		this.adaptee = adaptee;
	}	//	VLookup_mouseAdapter

	private VLocator adaptee;

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

}	//	VLocator_mouseAdapter
