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
 *  @version 	$Id: VMemo.java,v 1.8 2005/12/09 05:17:55 jjanke Exp $
 */
public class VMemo extends CTextArea
	implements VEditor, KeyListener, FocusListener, ActionListener
{
	/**
	 *	IDE Baan Constructor
	 */
	public VMemo()
	{
		this("", false, false, true, 60, 4000);
	}	//	VMemo

	/**
	 *	Standard Constructor
	 *  @param columnName
	 *  @param mandatory
	 *  @param isReadOnly
	 *  @param isUpdateable
	 *  @param displayLength
	 *  @param fieldLength
	 */
	public VMemo (String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		int displayLength, int fieldLength)
	{
		super (fieldLength/80, 50);
		super.setName(columnName);
		LookAndFeel.installBorder(this, "TextField.border");
		this.addFocusListener(this);    //  to activate editor

		//  Create Editor
		setColumns(displayLength>VString.MAXDISPLAY_LENGTH ? VString.MAXDISPLAY_LENGTH : displayLength);	//  46
		setForeground(CompierePLAF.getTextColor_Normal());
		setBackground(CompierePLAF.getFieldBackground_Normal());

		setLineWrap(true);
		setWrapStyleWord(true);
		addFocusListener(this);
		setInputVerifier(new CInputVerifier()); //Must be set AFTER addFocusListener in order to work
		setMandatory(mandatory);
		m_columnName = columnName;

		if (isReadOnly || !isUpdateable)
			setReadWrite(false);
		addKeyListener(this);

		//	Popup
		addMouseListener(new VMemo_mouseAdapter(this));
		if (columnName.equals("Script"))
			menuEditor = new CMenuItem(Msg.getMsg(Env.getCtx(), "Script"), Env.getImageIcon("Script16.gif"));
		else
			menuEditor = new CMenuItem(Msg.getMsg(Env.getCtx(), "Editor"), Env.getImageIcon("Editor16.gif"));
		menuEditor.addActionListener(this);
		popupMenu.add(menuEditor);
	}	//	VMemo

	/**
	 *  Dispose
	 */
	public void dispose()
	{
	}   //  dispose

	JPopupMenu          popupMenu = new JPopupMenu();
	private CMenuItem 	menuEditor;

	private String		m_columnName;
	private String		m_oldText = "";
	private boolean		m_firstChange;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VMemo.class);

	/**
	 *	Set Editor to value
	 *  @param value
	 */
	public void setValue(Object value)
	{
		super.setValue(value);
		m_firstChange = true;
		//	Always position Top 
		setCaretPosition(0);
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
	 *	ActionListener
	 *  @param e
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
			setValue(s);
			try
			{
				fireVetoableChange(m_columnName, null, getText());
				m_oldText = getText();
			}
			catch (PropertyVetoException pve)	{}
		}
	}	//	actionPerformed

	/**
	 *  Action Listener Interface - NOP
	 *  @param listener
	 */
	public void addActionListener(ActionListener listener)
	{
	}   //  addActionListener

	/**************************************************************************
	 *	Key Listener Interface
	 *  @param e
	 */
	public void keyTyped(KeyEvent e)	{}
	public void keyPressed(KeyEvent e)	{}

	/**
	 *	Escape 	- Restore old Text.
	 *  Indicate Change
	 *  @param e
	 */
	public void keyReleased(KeyEvent e)
	{
		//  ESC
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !getText().equals(m_oldText))
		{
			log.fine( "VMemo.keyReleased - ESC");
			setText(m_oldText);
			return;
		}
		//  Indicate Change
		if (m_firstChange && !m_oldText.equals(getText()))
		{
			log.fine( "VMemo.keyReleased - firstChange");
			m_firstChange = false;
			try
			{
				String text = getText();
				fireVetoableChange(m_columnName, text, null);   //  No data committed - done when focus lost !!!
			}
			catch (PropertyVetoException pve)	{}
		}	//	firstChange
	}	//	keyReleased

	/**
	 *	Focus Gained	- Save for Escape
	 *  @param e
	 */
	public void focusGained (FocusEvent e)
	{
		log.config(e.paramString());
		if (e.getSource() instanceof VMemo)
			requestFocus();
		else
			m_oldText = getText();
	}	//	focusGained

	/**
	 *	Data Binding to MTable (via GridController)
	 *  @param e
	 */
	public void focusLost (FocusEvent e)
	{
		//log.config( "VMemo.focusLost " + e.getSource(), e.paramString());
		//	something changed?
		return;

	}	//	focusLost

	/*************************************************************************/

	/**
	 *  Set Field/WindowNo for ValuePreference (NOP)
	 *  @param mField
	 */
	public void setField (org.compiere.model.MField mField)
	{
	}   //  setField



private class CInputVerifier extends InputVerifier {

	 public boolean verify(JComponent input) {


		//NOTE: We return true no matter what since the InputVerifier is only introduced to fireVetoableChange in due time
		if (getText() == null && m_oldText == null)
			return true;
		else if (getText().equals(m_oldText))
			return true;
		//
		try
		{
			String text = getText();
			fireVetoableChange(m_columnName, null, text);
			m_oldText = text;
			return true;
		}
		catch (PropertyVetoException pve)	{}
		return true;

	 } // verify

   } // CInputVerifier




}	//	VMemo

/*****************************************************************************/

/**
 *	Mouse Listener
 */
final class VMemo_mouseAdapter extends MouseAdapter
{
	/**
	 *	Constructor
	 *  @param adaptee
	 */
	VMemo_mouseAdapter(VMemo adaptee)
	{
		this.adaptee = adaptee;
	}	//	VMemo_mouseAdapter

	private VMemo adaptee;

	/**
	 *	Mouse Listener
	 *  @param e
	 */
	public void mouseClicked(MouseEvent e)
	{
		//	popup menu
		if (SwingUtilities.isRightMouseButton(e))
			adaptee.popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
	}	//	mouse Clicked



}	//	VMemo_mouseAdapter




