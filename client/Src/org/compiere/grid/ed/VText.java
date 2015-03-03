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
import org.compiere.apps.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *  Text Control (JTextArea embedded in JScrollPane)
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VText.java,v 1.29 2005/12/09 05:17:55 jjanke Exp $
 */
public class VText extends CTextArea
	implements VEditor, KeyListener, ActionListener
{
	/**
	 *	Standard Constructor
	 *  @param columnName column name
	 *  @param mandatory mandatory
	 *  @param isReadOnly read only
	 *  @param isUpdateable updateable
	 *  @param displayLength display length
	 *  @param fieldLength field length
	 */
	public VText (String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		int displayLength, int fieldLength)
	{
		super (fieldLength < 300 ? 2 : 3, 50);
		super.setName(columnName);
		LookAndFeel.installBorder(this, "TextField.border");

		//  Create Editor
		setColumns(displayLength>VString.MAXDISPLAY_LENGTH ? VString.MAXDISPLAY_LENGTH : displayLength);	//  46
		setForeground(CompierePLAF.getTextColor_Normal());
		setBackground(CompierePLAF.getFieldBackground_Normal());

		setLineWrap(true);
		setWrapStyleWord(true);
		setMandatory(mandatory);
		m_columnName = columnName;

		if (isReadOnly || !isUpdateable)
			setReadWrite(false);
		addKeyListener(this);

		//	Popup
		addMouseListener(new VText_mouseAdapter(this));
		if (columnName.equals("Script"))
			menuEditor = new CMenuItem(Msg.getMsg(Env.getCtx(), "Script"), Env.getImageIcon("Script16.gif"));
		else
			menuEditor = new CMenuItem(Msg.getMsg(Env.getCtx(), "Editor"), Env.getImageIcon("Editor16.gif"));
		menuEditor.addActionListener(this);
		popupMenu.add(menuEditor);
	}	//	VText

	/**
	 *  Dispose
	 */
	public void dispose()
	{
	}   //  dispose

	JPopupMenu          		popupMenu = new JPopupMenu();
	private CMenuItem 			menuEditor;

	private String				m_columnName;
	private String				m_oldText;
	private String				m_initialText;
	private volatile boolean	m_setting = false;

	/**
	 *	Set Editor to value
	 *  @param value value
	 */
	public void setValue(Object value)
	{
		if (value == null)
			m_oldText = "";
		else
			m_oldText = value.toString();
		if (m_setting)
			return;
		super.setValue(m_oldText);
		m_initialText = m_oldText;
		//	Always position Top 
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
	 *	ActionListener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == menuEditor)
		{
			menuEditor.setEnabled(false);
			String s = null;
			if (m_columnName.equals("Script"))
				s = ScriptEditor.start (Msg.translate(Env.getCtx(), m_columnName), getText(), isEditable(), 0);
			else
				s = Editor.startEditor (this, Msg.translate(Env.getCtx(), m_columnName), getText(), isEditable());
			menuEditor.setEnabled(true);
			//	Data Binding
			try
			{
				fireVetoableChange(m_columnName, m_oldText, s);
			}
			catch (PropertyVetoException pve)	{}
		}
	}	//	actionPerformed

	/**
	 *  Action Listener Interface - NOP
	 *  @param listener listener
	 */
	public void addActionListener(ActionListener listener)
	{
	}   //  addActionListener

	/**************************************************************************
	 *	Key Listener Interface
	 *  @param e event
	 */
	public void keyTyped(KeyEvent e)	{}
	public void keyPressed(KeyEvent e)	{}

	/**
	 * 	Key Released
	 *	if Escape restore old Text.
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
			fireVetoableChange(m_columnName, m_oldText, getText());
		}
		catch (PropertyVetoException pve)	{}
		m_setting = false;
	}	//	keyReleased

	/**
	 *  Set Field/WindowNo for ValuePreference (NOP)
	 *  @param mField field model
	 */
	public void setField (org.compiere.model.MField mField)
	{
	}   //  setField

}	//	VText

/*****************************************************************************/

/**
 *	Mouse Listener
 */
final class VText_mouseAdapter extends MouseAdapter
{
	/**
	 *	Constructor
	 *  @param adaptee VText
	 */
	VText_mouseAdapter(VText adaptee)
	{
		this.adaptee = adaptee;
	}	//	VText_mouseAdapter

	private VText adaptee;

	/**
	 *	Mouse Listener
	 *  @param e event
	 */
	public void mouseClicked(MouseEvent e)
	{
		//	popup menu
		if (SwingUtilities.isRightMouseButton(e))
			adaptee.popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
	}	//	mouse Clicked

}	//	VText_mouseAdapter
