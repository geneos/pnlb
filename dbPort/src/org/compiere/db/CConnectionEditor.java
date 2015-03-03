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
package org.compiere.db;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;

/**
 *  Connection Editor.
 *  A combo box and a button
 *
 *  @author     Jorg Janke
 *  @version    $Id: CConnectionEditor.java,v 1.10 2005/12/13 00:16:10 jjanke Exp $
 */
public class CConnectionEditor extends JComponent
	implements CEditor
{
	/**
	 *  Connection Editor creating new Connection
	 */
	public CConnectionEditor()
	{
		super();
		setName("ConnectionEditor");
		CConnectionEditor_MouseListener ml = new CConnectionEditor_MouseListener();
		//  Layout
		m_text.setEditable(false);
		m_text.setBorder(null);
		m_text.addMouseListener(ml);
		m_server.setIcon(new ImageIcon(getClass().getResource("Server16.gif")));
		m_server.setFocusable(false);
		m_server.setBorder(null);
		m_server.setOpaque(true);
		m_server.addMouseListener(ml);
		m_db.setIcon(new ImageIcon(getClass().getResource("Database16.gif")));
		m_db.setFocusable(false);
		m_db.setBorder(null);
		m_db.setOpaque(true);
		m_db.addMouseListener(ml);
		LookAndFeel.installBorder(this, "TextField.border");
		//
		setLayout(new BorderLayout(0,0));
		add(m_server, BorderLayout.WEST);
		add(m_text, BorderLayout.CENTER);
		add(m_db, BorderLayout.EAST);
	}   //  CConnectionEditor

	/** Text Element        */
	private JTextField  m_text = new JTextField(10);
	/** DB Button Element   */
	private JLabel      m_db = new JLabel ();
	/** Host Button Element */
	private JLabel      m_server = new JLabel();
	/** The Value           */
	private CConnection m_value = null;
	/** ReadWrite           */
	private boolean     m_rw = true;
	/** Mandatory           */
	private boolean     m_mandatory = false;

    private String      userName = null;
    
    /**
     * Set user name that invoke the CConnetionEditor
     * @param user  the user name to set
     * @author Ezequiel Scott at Zynnia
     */
    public void setUserName(String user){
        userName = user;
    }
    
	/**
	 *	Enable Editor
	 *  @param rw true, if you can enter/select data
	 */
	public void setReadWrite (boolean rw)
	{
		m_rw = rw;
		setBackground(false);
	}   //  setReadWrite

	/**
	 *	Is it possible to edit
	 *  @return true, if editable
	 */
	public boolean isReadWrite()
	{
		return m_rw;
	}   //  isReadWrite

	/**
	 *	Set Editor Mandatory
	 *  @param mandatory true, if you have to enter data
	 */
	public void setMandatory (boolean mandatory)
	{
		m_mandatory = mandatory;
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
	 *  Set Background based on editable / mandatory / error
	 *  @param error if true, set background to error color, otherwise mandatory/editable
	 */
	public void setBackground (boolean error)
	{
		Color c = null;
		if (error)
			c = CompierePLAF.getFieldBackground_Error();
		else if (!m_rw)
			c = CompierePLAF.getFieldBackground_Inactive();
		else if (m_mandatory)
			c = CompierePLAF.getFieldBackground_Mandatory();
		else
			c = CompierePLAF.getFieldBackground_Normal();
		setBackground(c);
	}   //  setBackground

	/**
	 *  Set Background color
	 *  @param color
	 */
	public void setBackground (Color color)
	{
		m_server.setBackground(color);
		m_text.setBackground(color);
		m_db.setBackground(color);
	}   //  setBackground

	/**
	 *  Set Visible
	 *  @param visible true if field is to be shown
	 */
	public void setVisible (boolean visible)
	{
		this.setVisible(visible);
	}

	/**
	 *	Set Editor to value
	 *  @param value value of the editor
	 */
	public void setValue (Object value)
	{
		if (value != null && value instanceof CConnection)
			m_value = (CConnection)value;
		setDisplay();
	}   //  setValue

	/**
	 *	Return Editor value
	 *  @return current value
	 */
	public Object getValue()
	{
		return m_value;
	}   //  getValue

	/**
	 *  Return Display Value
	 *  @return displayed String value
	 */
	public String getDisplay()
	{
		if (m_value == null)
			return "";
		return m_value.getName();
	}   //  getDisplay

	/**
	 *  Update Display with Connection info
	 */
	public void setDisplay()
	{
		m_text.setText(getDisplay());
		if (m_value == null)
			return;
		//  Text
		if (m_value.isAppsServerOK(false) || m_value.isDatabaseOK())
		{
			m_text.setForeground(CompierePLAF.getTextColor_OK());
			setBackground(false);
			if (!m_value.isAppsServerOK(false))
				m_server.setBackground(CompierePLAF.getFieldBackground_Error());
			if (!m_value.isDatabaseOK())
				m_db.setBackground(CompierePLAF.getFieldBackground_Error());
		}
		else
		{
			m_text.setForeground(CompierePLAF.getTextColor_Issue());
			setBackground(true);
		}
	}   //  setDisplay


	/**************************************************************************
	 *  Remove Action Listener
	 *  @param l
	 */
	public synchronized void removeActionListener(ActionListener l)
	{
		listenerList.remove(ActionListener.class, l);
	}   //  removeActionListener

	/**
	 *  Add Action Listener
	 *  @param l
	 */
	public synchronized void addActionListener(ActionListener l)
	{
	    listenerList.add(ActionListener.class, l);
	}   //  addActionListener

	/**
	 *  Fire Action Performed
	 */
	private void fireActionPerformed()
	{
		ActionEvent e = null;
		ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        for (int i = 0; i < listeners.length; i++) 
        {
       		if (e == null) 
       			e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "actionPerformed");
       		listeners[i].actionPerformed(e);
        }
	}   //  fireActionPerformed

	
	/**************************************************************************
	 *  Test Method
	 *  @param args
	 */
	public static void main(String[] args)
	{
	//	System.out.println("CConnectionEditor");
		JFrame frame = new JFrame("CConnectionEditor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getRootPane().getContentPane().add(new CConnectionEditor());
		CompierePLAF.showCenterScreen(frame);
	}   //  main


	/**
	 *  MouseListener
	 */
	public class CConnectionEditor_MouseListener extends MouseAdapter
	{       
		/**
		 *  Mouse Clicked - Open Dialog
		 *  @param e
		 */
		public void mouseClicked(MouseEvent e)
		{
			if (!isEnabled() || !m_rw || m_active)
				return;
            
            /**
             * Validation Added:
             * Only the users PanAdmin or System can modify connection values
             * 
             * @author Ezequiel Scott at Zynnia
             */
            if ( userName != null && !userName.equals("PanAdmin") && !userName.equals("System") ) {
                JOptionPane.showMessageDialog(e.getComponent(),
                    "Usted no tiene permisos para cambiar esta configuración",
                    "Operacion no permitida",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
			m_active = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			//
            CConnectionDialog cd = new CConnectionDialog(m_value);
            setValue(cd.getConnection());
			fireActionPerformed();
			//
			setCursor(Cursor.getDefaultCursor());
			m_active = false;
		}   //  mouseClicked

		private boolean     m_active = false;
	}   //  CConnectionExitor_MouseListener

}   //  CConnectionEditor
