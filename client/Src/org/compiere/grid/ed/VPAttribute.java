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
import java.util.logging.*;
import javax.swing.*;
import org.compiere.model.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *  Product Attribute Set Instance Editor
 *
 *  @author Jorg Janke
 *  @version $Id: VPAttribute.java,v 1.25 2005/12/09 05:17:55 jjanke Exp $
 */
public class VPAttribute extends JComponent
	implements VEditor, ActionListener
{
	/**
	 *	IDE Constructor
	 */
	public VPAttribute()
	{
		this (false, false, true, 0, null);
	}	//	VAssigment

	/**
	 *	Create Product Attribute Set Instance Editor.
	 *  @param mandatory mandatory
	 *  @param isReadOnly read only
	 *  @param isUpdateable updateable
	 * 	@param WindowNo WindowNo
	 * 	@param lookup Model Product Attribute
	 */
	public VPAttribute (boolean mandatory, boolean isReadOnly, boolean isUpdateable, 
		int WindowNo, MPAttributeLookup lookup)
	{
		super.setName("M_AttributeSetInstance_ID");
		m_WindowNo = WindowNo;
		m_mPAttribute = lookup;
		m_C_BPartner_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "C_BPartner_ID");
		LookAndFeel.installBorder(this, "TextField.border");
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
		m_button.setIcon(Env.getImageIcon("PAttribute10.gif"));
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
		m_text.addMouseListener(new VPAttribute_mouseAdapter(this));
		menuEditor = new CMenuItem(Msg.getMsg(Env.getCtx(), "PAttribute"), Env.getImageIcon("Zoom16.gif"));
		menuEditor.addActionListener(this);
		popupMenu.add(menuEditor);
	}	//	VPAttribute

	/**	Data Value				*/
	private Object				m_value = new Object();
	/** The Attribute Instance	*/
	private MPAttributeLookup	m_mPAttribute;

	/** The Text Field          */
	private JTextField			m_text = new JTextField (VLookup.DISPLAY_LENGTH);
	/** The Button              */
	private CButton				m_button = new CButton();

	JPopupMenu          		popupMenu = new JPopupMenu();
	private CMenuItem 			menuEditor;

	private boolean				m_readWrite;
	private boolean				m_mandatory;
	private int					m_WindowNo;
	private int					m_C_BPartner_ID;
	
	/**	Calling Window Info				*/
	private int					m_AD_Column_ID = 0;
	/**	No Instance Key					*/
	private static Integer		NO_INSTANCE = new Integer(0);
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VPAttribute.class);

	/**
	 * 	Dispose resources
	 */
	public void dispose()
	{
		m_text = null;
		m_button = null;
		m_mPAttribute.dispose();
		m_mPAttribute = null;
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
		/*
                if (value == null || NO_INSTANCE.equals(value))
		{
			m_text.setText("");
			m_value = value;
			return;
		}
                */
                if (value == null)
		{
			m_text.setText("");
			m_value = value;
			return;
		}		
		//	The same
		if (value.equals(m_value)) 
			return;
		//	new value
		log.fine("Value=" + value);
		m_value = value;
		m_text.setText(m_mPAttribute.getDisplay(value));	//	loads value
	}	//	setValue

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
		m_button.setEnabled (false);
		//
		Integer oldValue = (Integer)getValue ();
		int M_AttributeSetInstance_ID = oldValue == null ? 0 : oldValue.intValue ();
		int M_Product_ID = Env.getContextAsInt (Env.getCtx (), m_WindowNo, "M_Product_ID");
		int M_ProductBOM_ID = Env.getContextAsInt (Env.getCtx (), m_WindowNo, "M_ProductBOM_ID");

		log.config("M_Product_ID=" + M_Product_ID + "/" + M_ProductBOM_ID
			+ ",M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID
			+ ", AD_Column_ID=" + m_AD_Column_ID);
		
		//	M_Product.M_AttributeSetInstance_ID = 8418
		boolean productWindow = m_AD_Column_ID == 8418;		//	HARDCODED
		//begin vpj-cd e-evolution 29 Dic 2005 
				 productWindow = m_AD_Column_ID == 8418;		//	HARDCODED
        //end vpj-cd e-evolution 29 Dic 2005 
		
		//	Exclude ability to enter ASI
		boolean exclude = true;
		if (M_Product_ID != 0)
		{
			MProduct product = MProduct.get(Env.getCtx(), M_Product_ID);
			int M_AttributeSet_ID = product.getM_AttributeSet_ID();
			if (M_AttributeSet_ID != 0)
			{
				MAttributeSet mas = MAttributeSet.get(Env.getCtx(), M_AttributeSet_ID);
				exclude = mas.excludeEntry(m_AD_Column_ID, Env.isSOTrx(Env.getCtx(), m_WindowNo));
			}
		}
		
		boolean changed = false;
		if (M_ProductBOM_ID != 0)	//	Use BOM Component
			M_Product_ID = M_ProductBOM_ID;
		//	
		if (!productWindow && (M_Product_ID == 0 || exclude))
		{
			changed = true;
			m_text.setText(null);
			M_AttributeSetInstance_ID = 0;
		}
		else
		{
			VPAttributeDialog vad = new VPAttributeDialog (Env.getFrame (this), 
				M_AttributeSetInstance_ID, M_Product_ID, m_C_BPartner_ID,
				productWindow, m_AD_Column_ID, m_WindowNo);
			if (vad.isChanged())
			{
				m_text.setText(vad.getM_AttributeSetInstanceName());
				M_AttributeSetInstance_ID = vad.getM_AttributeSetInstance_ID();
				changed = true;
			}
		}
		/** Selection
		{
			//	Get Model
			MAttributeSetInstance masi = MAttributeSetInstance.get(Env.getCtx(), M_AttributeSetInstance_ID, M_Product_ID);
			if (masi == null)
			{
				log.log(Level.SEVERE, "No Model for M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID + ", M_Product_ID=" + M_Product_ID);
			}
			else
			{
				Env.setContext(Env.getCtx(), m_WindowNo, "M_AttributeSet_ID", masi.getM_AttributeSet_ID());
				//	Get Attribute Set
				MAttributeSet as = masi.getMAttributeSet();
				//	Product has no Attribute Set
				if (as == null)		
					ADialog.error(m_WindowNo, this, "PAttributeNoAttributeSet");
				//	Product has no Instance Attributes
				else if (!as.isInstanceAttribute())
					ADialog.error(m_WindowNo, this, "PAttributeNoInstanceAttribute");
				else
				{
					int M_Warehouse_ID = Env.getContextAsInt (Env.getCtx (), m_WindowNo, "M_Warehouse_ID");
					int M_Locator_ID = Env.getContextAsInt (Env.getCtx (), m_WindowNo, "M_Locator_ID");
					String title = "";
					PAttributeInstance pai = new PAttributeInstance (
						Env.getFrame(this), title, 
						M_Warehouse_ID, M_Locator_ID, M_Product_ID, m_C_BPartner_ID);
					if (pai.getM_AttributeSetInstance_ID() != -1)
					{
						m_text.setText(pai.getM_AttributeSetInstanceName());
						M_AttributeSetInstance_ID = pai.getM_AttributeSetInstance_ID();
						changed = true;
					}
				}
			}
		}
		**/
		
		//	Set Value
		if (changed)
		{
			log.finest("Changed M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID);
			m_value = new Object();				//	force re-query display
			if (M_AttributeSetInstance_ID == 0)
				setValue(null);
			else
				setValue(new Integer(M_AttributeSetInstance_ID));
			try
			{
				fireVetoableChange("M_AttributeSetInstance_ID", new Object(), getValue());
			}
			catch (PropertyVetoException pve)
			{
				log.log(Level.SEVERE, "", pve);
			}
		}	//	change
		m_button.setEnabled(true);
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
final class VPAttribute_mouseAdapter extends MouseAdapter
{
	/**
	 *	Constructor
	 *  @param adaptee adaptee
	 */
	VPAttribute_mouseAdapter(VPAttribute adaptee)
	{
		this.adaptee = adaptee;
	}	//	VPAttribute_mouseAdapter

	private VPAttribute adaptee;

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
