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

import javax.swing.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Combobox with KeyNamePair/ValueNamePair or Locator.
 *  <p>
 *  It has the same hight as a TextField
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VComboBox.java,v 1.15 2005/09/16 03:54:26 jjanke Exp $
 */
public class VComboBox extends CComboBox
{
	/**
	 *  Constructor
	 */
	public VComboBox()
	{
		super();
//		common_init();
	}
	public VComboBox(Object[] items)
	{
		super(items);
//		common_init();
	}
	public VComboBox(ComboBoxModel model)
	{
		super(model);
//		common_init();
	}	//	VComboBox

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VComboBox.class);
	
	/**
	 *  Common Setup
	 *
	private void common_init()
	{
		LookAndFeel.installColorsAndFont(this, "TextField.background", "TextField.foreground", "TextField.font");
		setForeground(CompierePLAF.getTextColor_Normal());
		setBackground(CompierePLAF.getFieldBackground_Normal());
		setPreferredSize(s_text.getPreferredSize());
	//	this.setKeySelectionManager(new ComboSelectionManager());
	}   //  common_init

	/** Reference Field         *
	private static  JTextField  s_text = new JTextField(VTextField.DISPLAY_SIZE);

	/**
	 *	Set Selected Item to key.
	 *		Find key value in list
	 *  @param key
	 */
	public void setValue(Object key)
	{
		if (key == null)
		{
			this.setSelectedIndex(-1);
			return;
		}

		ComboBoxModel model = getModel();
		int size = model.getSize();
		for (int i = 0; i < size; i++)
		{
			Object element = model.getElementAt(i);
			String ID = null;
			if (element instanceof NamePair)
				ID = ((NamePair)element).getID();
			else if (element instanceof MLocator)
				ID = String.valueOf(((MLocator)element).getM_Locator_ID());
			else
				log.log(Level.SEVERE, "VComboBox.setValue - Element not NamePair - " + element.getClass().toString());

			if (key == null || ID == null)
			{
				if (key == null && ID == null)
				{
					setSelectedIndex(i);
					return;
				}
			}
			else if (ID.equals(key.toString()))
			{
				setSelectedIndex(i);
				return;
			}
		}
		setSelectedIndex(-1);
		setSelectedItem(null);
	}	//	setValue

	/**
	 *  Set Selected item to key if exists
	 *  @param key
	 */
	public void setValue (int key)
	{
		setValue(String.valueOf(key));
	}   //  setValue

	/**
	 *	Get Value
	 *  @return key as Integer or String value
	 */
	public Object getValue()
	{
		NamePair p = (NamePair)getSelectedItem();
		if (p == null)
			return null;
		//
		if (p instanceof KeyNamePair)
		{
			if (p.getID() == null)	//	-1 return null
				return null;
			return new Integer(((KeyNamePair)p).getID());
		}
		return p.getID();
	}	//	getValue

	/**
	 *  Get Display
	 *  @return displayed string
	 */
	public String getDisplay()
	{
		if (getSelectedIndex() == -1)
			return "";
		//
		NamePair p = (NamePair)getSelectedItem();
		if (p == null)
			return "";
		return p.getName();
	}   //  getDisplay

}	//	VComboBox
