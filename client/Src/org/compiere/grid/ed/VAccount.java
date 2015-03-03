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
import javax.swing.*;
import org.compiere.model.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Account Control - Displays ValidCombination and launches Dialog
 *
 *  @author Jorg Janke
 *  @version  $Id: VAccount.java,v 1.21 2005/11/14 02:10:57 jjanke Exp $
 */
public final class VAccount extends JComponent
	implements VEditor, ActionListener
{
	/**
	 *	Constructor
	 *  @param columnName
	 *  @param mandatory
	 *  @param isReadOnly
	 *  @param isUpdateable
	 *  @param mAccount
	 *  @param title
	 */
	public VAccount(String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		MAccountLookup mAccount, String title)
	{
		super();
		super.setName(columnName);
		m_columnName = columnName;
		m_mAccount = mAccount;
		m_title = title;
		//
		LookAndFeel.installBorder(this, "TextField.border");
		this.setLayout(new BorderLayout());
		//  Size
		this.setPreferredSize(m_text.getPreferredSize());		//	causes r/o to be the same length
		int height = m_text.getPreferredSize().height;

		//	***	Button & Text	***
		m_text.setBorder(null);
		m_text.addActionListener(this);
		m_text.setFont(CompierePLAF.getFont_Field());
		m_text.setForeground(CompierePLAF.getTextColor_Normal());
		this.add(m_text, BorderLayout.CENTER);

		m_button.setIcon(Env.getImageIcon("Account10.gif"));
		m_button.setMargin(new Insets(0, 0, 0, 0));
		m_button.setPreferredSize(new Dimension(height, height));
		m_button.addActionListener(this);
		m_button.setFocusable(false);
		this.add(m_button, BorderLayout.EAST);

		//	Editable
		if (isReadOnly || !isUpdateable)
			setReadWrite (false);
		else
			setReadWrite (true);
		setMandatory (mandatory);
	}	//	VAccount

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_text = null;
		m_button = null;
		m_mAccount = null;
	}   //  dispose

	private CTextField			m_text = new CTextField (VLookup.DISPLAY_LENGTH);
	private CButton				m_button = new CButton();
	private MAccountLookup		m_mAccount;
	private Object				m_value;
	private String				m_title;
	private int					m_WindowNo;

	private String				m_columnName;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VAccount.class);

	/**
	 *	Enable/disable
	 *  @param value
	 */
	public void setReadWrite(boolean value)
	{
		m_button.setReadWrite(value);
		m_text.setReadWrite(value);
		if (m_button.isVisible() != value)
			m_button.setVisible(value);
		setBackground(false);
	}	//	setReadWrite

	/**
	 *	IsReadWrite
	 *  @return true if read write
	 */
	public boolean isReadWrite()
	{
		return m_button.isReadWrite();
	}	//	isReadWrite

	/**
	 *	Set Mandatory (and back bolor)
	 *  @param mandatory
	 */
	public void setMandatory (boolean mandatory)
	{
		m_button.setMandatory(mandatory);
		setBackground(false);
	}	//	setMandatory

	/**
	 *	Is it mandatory
	 *  @return mandatory
	 */
	public boolean isMandatory()
	{
		return m_button.isMandatory();
	}	//	isMandatory

	/**
	 *	Set Background
	 *  @param color
	 */
	public void setBackground (Color color)
	{
	//	if (!color.equals(m_text.getBackground()))
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
	 *  @param fg
	 */
	public void setForeground(Color fg)
	{
		m_text.setForeground(fg);
	}   //  setForeground

	/**
	 *	Set Editor to value
	 *  @param value
	 */
	public void setValue (Object value)
	{
		m_value = value;
		m_text.setText(m_mAccount.getDisplay(value));	//	loads value
		m_text.setToolTipText(m_mAccount.getDescription());
	}	//	setValue

	/**
	 *  Property Change Listener
	 *  @param evt
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
		return new Integer (m_mAccount.C_ValidCombination_ID);
	}	//	getValue

	/**
	 *  Return Display Value
	 *  @return String representation
	 */
	public String getDisplay()
	{
		return m_text.getText();
	}   //  getDisplay

	/**
	 *	ActionListener - Button - Start Dialog
	 *  @param e
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == m_text)
			cmd_text();
		else
			cmd_button();
	}	//	actionPerformed

	/**
	 *	Button - Start Dialog
	 */
	public void cmd_button()
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		int C_AcctSchema_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "C_AcctSchema_ID");
		VAccountDialog ad = new VAccountDialog (Env.getFrame(this), m_title, 
			m_mAccount, C_AcctSchema_ID);
		setCursor(Cursor.getDefaultCursor());
		//
		Integer newValue = ad.getValue();
		if (newValue == null)
			return;

		//	set & redisplay
		setValue(newValue);

		//	Data Binding
		try
		{
			fireVetoableChange(m_columnName, null, newValue);
		}
		catch (PropertyVetoException pve)
		{
		}
	}	//	cmd_button

	/**
	 *	Text - try to find Alias or start Dialog
	 */
	public void cmd_text()
	{
		String text = m_text.getText();
		log.info("Text=" + text);
		if (text == null || text.length() == 0 || text.equals("%"))
		{
			cmd_button();
			return;
		}
		if (!text.endsWith("%"))
			text += "%";
		//
		String sql = "SELECT C_ValidCombination_ID FROM C_ValidCombination "
			+ "WHERE C_AcctSchema_ID=?" 
			+ " AND (UPPER(Alias) LIKE ? OR UPPER(Combination) LIKE ?)";
		sql = MRole.getDefault().addAccessSQL(sql, 
			"C_ValidCombination", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		int C_AcctSchema_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "C_AcctSchema_ID");
		//
		int C_ValidCombination_ID = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_AcctSchema_ID);
			pstmt.setString(2, text.toUpperCase());
			pstmt.setString(3, text.toUpperCase());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				C_ValidCombination_ID = rs.getInt(1);
				if (rs.next())		//	only one
					C_ValidCombination_ID = 0;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		//	We have a Value
		if (C_ValidCombination_ID > 0)
		{
			Integer newValue = new Integer(C_ValidCombination_ID);
			//	Data Binding
			try
			{
				fireVetoableChange(m_columnName, null, newValue);
			}
			catch (PropertyVetoException pve)
			{
			}
		}
		else
			cmd_button();
	}	//	actionPerformed


	/**
	 *  Action Listener Interface
	 *  @param listener
	 */
	public void addActionListener(ActionListener listener)
	{
		m_text.addActionListener(listener);
	}   //  addActionListener

	/**
	 *  Set Field/WindowNo for ValuePreference (NOP)
	 *  @param mField
	 */
	public void setField (org.compiere.model.MField mField)
	{
		if (mField != null)
			m_WindowNo = mField.getWindowNo();
	}   //  setField

}	//	VAccount

