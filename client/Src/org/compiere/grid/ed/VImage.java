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
import org.compiere.util.*;

/**
 *  Image Display of AD_Iamge_ID
 *
 *  @author  Jorg Janke
 *  @version $Id: VImage.java,v 1.15 2005/09/19 04:48:58 jjanke Exp $
 */
public class VImage extends JButton
	implements VEditor, ActionListener
{
	/**
	 *  Image Editor
	 *  @param WindowNo
	 */
	public VImage(int WindowNo)
	{
		super("-/-");
		m_WindowNo = WindowNo;
		super.addActionListener(this);
	}   //  VImage

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_mImage = null;
	}   //  dispose

	/** @todo Display existing Images from MImage */

	/** WindowNo                */
	private int     m_WindowNo;
	/** The Image Model         */
	private MImage  m_mImage = null;
	/** Mandatory flag          */
	private boolean m_mandatory = false;
	/** Column Name             */
	private static final String COLUMN_NAME = "AD_Image_ID";
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VImage.class);

	/**
	 *  Set Value
	 *  @param value
	 */
	public void setValue(Object value)
	{
		log.config("=" + value);
		int newValue = 0;
		if (value == null && value instanceof Integer)
			newValue = ((Integer)value).intValue();

		//  Get/Create Image
		if (m_mImage == null || newValue != m_mImage.get_ID())
			m_mImage = new MImage(Env.getCtx(), newValue, null);
		//
		log.fine( m_mImage.toString());
	//	super.setIcon(m_mImage.getImage());
		super.setToolTipText(m_mImage.getName());
	}   //  setValue

	/**
	 *  Get Value
	 *  @return value
	 */
	public Object getValue()
	{
		if (m_mImage.get_ID() == 0)
			return null;
		return new Integer(m_mImage.get_ID());
	}   //  getValue

	/**
	 *  Get Display Value
	 *  @return image name
	 */
	public String getDisplay()
	{
		return m_mImage.getName();
	}   //  getDisplay

	/**
	 *  Set ReadWrite
	 *  @param rw
	 */
	public void setReadWrite (boolean rw)
	{
		if (isEnabled() != rw)
			setEnabled (rw);
	}   //  setReadWrite

	/**
	 *  Get ReadWrite
	 *  @return true if rw
	 */
	public boolean isReadWrite()
	{
		return super.isEnabled();
	}   //  getReadWrite

	/**
	 *  Set Mandatory
	 *  @param mandatory
	 */
	public void setMandatory (boolean mandatory)
	{
		m_mandatory = mandatory;
	}   //  setMandatory

	/**
	 *  Get Mandatory
	 *  @return true if mandatory
	 */
	public boolean isMandatory()
	{
		return m_mandatory;
	}   //  isMandatory

	/**
	 *  Set Background - nop
	 *  @param color
	 */
	public void setBackground(Color color)
	{
	}   //  setBackground

	/**
	 *  Set Background - nop
	 */
	public void setBackground()
	{
	}   //  setBackground

	/**
	 *  Set Background - nop
	 *  @param error
	 */
	public void setBackground(boolean error)
	{
	}   //  setBackground

	/**
	 *  Property Change
	 *  @param evt
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(org.compiere.model.MField.PROPERTY))
			setValue(evt.getNewValue());
	}   //  propertyChange

	/**
	 *  ActionListener - start dialog and set value
	 *  @param e
	 */
	public void actionPerformed (ActionEvent e)
	{
		VImageDialog vid = new VImageDialog(Env.getWindow(m_WindowNo), m_mImage);
		vid.setVisible(true);     //  m_mImage is updated
		//
		Integer value = null;
		if (m_mImage.get_ID() != 0)
			value = new Integer(m_mImage.get_ID());
		try
		{
			fireVetoableChange(COLUMN_NAME, null, value);
		}
		catch (PropertyVetoException pve)	{}
	}   //  actionPerformed

	/**
	 *  Set Field/WindowNo for ValuePreference (NOP)
	 *  @param mField
	 */
	public void setField (org.compiere.model.MField mField)
	{
	}   //  setField

}   //  VImage
