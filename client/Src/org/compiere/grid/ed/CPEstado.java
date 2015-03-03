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
 *  Estado de cheques recibidos
 *
 *  @author Daniel
 */
public class CPEstado extends JComponent
	implements VEditor, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *	IDE Constructor
	 */
	public CPEstado()
	{
		this (false, false, true, 0);
	}	//	VAssigment

	/**
	 *	Create Product Attribute Set Instance Editor.
	 *  @param mandatory mandatory
	 *  @param isReadOnly read only
	 *  @param isUpdateable updateable
	 * 	@param WindowNo WindowNo
	 * 	@param lookup Model Product Attribute
	 */
	public CPEstado (boolean mandatory, boolean isReadOnly, boolean isUpdateable, 
		int WindowNo)
	{
		super.setName("STATE");
		m_WindowNo = WindowNo;
		LookAndFeel.installBorder(this,"TextField.border");
		this.setLayout(new BorderLayout());
		//  Size
		this.setPreferredSize(m_text.getPreferredSize());
		int height = m_text.getPreferredSize().height;

		//	***	Text	***
		m_text.setEditable(false);
		m_text.setFocusable(false);
		m_text.setBorder(null);
		m_text.setHorizontalAlignment(JTextField.LEADING);
		//	Background
		setMandatory(mandatory);
		this.add(m_text, BorderLayout.CENTER);

		//	***	Button	***
		m_button.setIcon(Env.getImageIcon("wfEnd24.b ak.gif"));
		m_button.setMargin(new Insets(0, 0, 0, 0));
		m_button.setPreferredSize(new Dimension(height, height));
		m_button.addActionListener(this);
		m_button.setFocusable(true);
		this.add(m_button, BorderLayout.EAST);

		//	Prefereed Size
		this.setPreferredSize(this.getPreferredSize());		//	causes r/o to be the same length
		//	ReadWrite
		if (isReadOnly || !isUpdateable)
			setReadWrite(false);
		else
			setReadWrite(true);

		//	Popup
		m_text.addMouseListener(new CPEstado_mouseAdapter(this));
		menuEditor = new CMenuItem("Estado Cheques Propios", Env.getImageIcon("Zoom16.gif"));
		menuEditor.addActionListener(this);
		popupMenu.add(menuEditor);
	}	//	VPAttribute

	/**	Data Value				*/
	private Object				m_value = new Object();
	
	/** The Text Field          */
	private JTextField			m_text = new JTextField (VLookup.DISPLAY_LENGTH);
	/** The Button              */
	private CButton				m_button = new CButton();

	JPopupMenu          		popupMenu = new JPopupMenu();
	private CMenuItem 			menuEditor;

	private boolean				m_readWrite;
	private boolean				m_mandatory;
	private int					m_WindowNo;
	
	/**	Calling Window Info				*/
	private int					m_AD_Column_ID = 0;
	/** 	Dispose resources
	 */
	public void dispose()
	{
		m_text = null;
		m_button = null;
	}	//	dispose

	/**
	 * 	Set Mandatory
	 * 	@param mandatory mandatory
	 */
	public void setMandatory (boolean mandatory)
	{
		m_mandatory = mandatory;
		m_button.setMandatory(mandatory);
		setBackground (false);
	}	//	setMandatory

	/**
	 * 	Get Mandatory
	 *  @return mandatory
	 */
	public boolean isMandatory()
	{
		return m_mandatory;
	}	//	isMandatory

	/**
	 * 	Set ReadWrite
	 * 	@param rw read rwite
	 */
	public void setReadWrite (boolean rw)
	{
		m_readWrite = rw;
		m_button.setReadWrite(rw);
		setBackground (false);
	}	//	setReadWrite

	/**
	 * 	Is Read Write
	 * 	@return read write
	 */
	public boolean isReadWrite()
	{
		return m_readWrite;
	}	//	isReadWrite

	/**
	 * 	Set Foreground
	 * 	@param color color
	 */
	public void setForeground (Color color)
	{
		m_text.setForeground(color);
	}	//	SetForeground

	/**
	 * 	Set Background
	 * 	@param error Error
	 */
	public void setBackground (boolean error)
	{
		if (error)
			setBackground(CompierePLAF.getFieldBackground_Error());
		else if (!m_readWrite)
			setBackground(CompierePLAF.getFieldBackground_Inactive());
		else if (m_mandatory)
			setBackground(CompierePLAF.getFieldBackground_Mandatory());
		else
			setBackground(CompierePLAF.getInfoBackground());
	}	//	setBackground

	/**
	 * 	Set Background
	 * 	@param color Color
	 */
	public void setBackground (Color color)
	{
		m_text.setBackground(color);
	}	//	setBackground

	/**************************************************************************
	 * 	Set/lookup Value
	 * 	@param value value
	 */
	public void setValue(Object value)
	{
        m_value = value;
        if (value!=null)
        	m_text.setText(getNameState(((String)value).charAt(0)));
	}	//	setValue
        
    public static String getNameState(char c){
    	
    	switch (c)	{
    	case 'E':
    		return "Emitido";
    	case 'P':
    		return "Pendiente de D�bito";
    	case 'D':
    		return "Debitado";
    	case 'V':
    		return "Vencido";
    	case 'A':
    		return "Anulado";
    	case 'C':
    		return "Rechazado";
    	case 'R':
    		return "Revertido";
    	case 'I':
    		return "Impreso";            
    	default:
    		return "";
		}
    }
	
    /**
	 * 	Get Value
	 * 	@return value
	 */
	public Object getValue()
	{
		return m_value;
	}	//	getValue

	/**
	 * 	Get Display Value
	 *	@return info
	 */
	public String getDisplay()
	{
		return m_text.getText();
	}	//	getDisplay

	
	/**************************************************************************
	 * 	Set Field
	 * 	@param mField MField
	 */
	public void setField(MField mField)
	{
		//	To determine behavior
		m_AD_Column_ID = mField.getAD_Column_ID();
	}	//	setField

	/**
	 *  Action Listener Interface
	 *  @param listener listener
	 */
	public void addActionListener(ActionListener listener)
	{
	}   //  addActionListener

        /**
	 * 	Action Listener - start dialog
	 * 	@param e Event
	 */
	public void actionPerformed(ActionEvent e)
	{             
		if (!m_button.isEnabled ())
			return;
		boolean enabled = m_button.isEnabled();
		m_button.setEnabled (false);
		
		CPEstadoDialog ed = new CPEstadoDialog (Env.getFrame (this), m_WindowNo, (String)getValue());
        if (ed.changed())
        {
        	setValue(ed.getState());
        	
        	int C_VALORPAGO_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "C_VALORPAGO_ID");
        	MVALORPAGO payval = new MVALORPAGO(Env.getCtx(), C_VALORPAGO_ID, null);
			payval.setEstado(ed.getState());
			
        	try{
            	fireVetoableChange("STATE", new Object(), getValue());
            }	catch(Exception exp){}
            
            if ((String)getValue() == "E" ||(String)getValue() == "P")
            	m_button.setEnabled(true);
        }
        else
        	m_button.setEnabled(enabled);

        requestFocus();
	}	//	actionPerformed

	/**
	 *  Property Change Listener
	 *  @param evt event
	 */
	public void propertyChange (PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(org.compiere.model.MField.PROPERTY))
			setValue(evt.getNewValue());
	}   //  propertyChange
        
}	//	VPAttribute

/**
 *	Mouse Listener
 */
final class CPEstado_mouseAdapter extends MouseAdapter
{
	/**
	 *	Constructor
	 *  @param adaptee adaptee
	 */
	CPEstado_mouseAdapter(CPEstado adaptee)
	{
		this.adaptee = adaptee;
	}	//	VPAttribute_mouseAdapter

	private CPEstado adaptee;

	/**
	 *	Mouse Listener
	 *  @param e event
	 */
	public void mouseClicked(MouseEvent e)
	{
		//	Double Click
		if (e.getClickCount() > 1)
			adaptee.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "Mouse"));
		//	popup menu
		if (SwingUtilities.isRightMouseButton(e))
			adaptee.popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
	}	//	mouse Clicked

}	//	VPAttribute_mouseAdapter
