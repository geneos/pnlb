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
package org.compiere.plaf;

import javax.swing.*;
import javax.swing.plaf.basic.*;
import org.compiere.swing.*;

/**
 *  Compiere Combo Popup - allows to prevent the display of the popup
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereComboPopup.java,v 1.8 2005/10/09 19:01:37 jjanke Exp $
 */
public class CompiereComboPopup extends BasicComboPopup
{
	/**
	 *  Constructor
	 *  @param combo
	 */
	public CompiereComboPopup(JComboBox combo)
	{
		super(combo);
	}   //  CompiereComboPopup

	/**
	 *  Conditionally show the Popup.
	 *  If the combo is a CComboBox/CField, the return value of the
	 *  method displayPopup determines if the popup is actually displayed
	 *  @see CComboBox#displayPopup()
	 *  @see CField#displayPopup()
	 */
	public void show()
	{
		//  Check ComboBox if popup should be displayed
		if (comboBox instanceof CComboBox && !((CComboBox)comboBox).displayPopup())
			return;
		//  Check Field if popup should be displayed
		if (comboBox instanceof CField && !((CField)comboBox).displayPopup())
			return;
		super.show();
	}   //  show


	/**
	 *  Inform CComboBox and CField that Popup was hidden
	 *  @see CComboBox.hidingPopup
	 *  @see CField.hidingPopup
	 *
	public void hide()
	{
		super.hide();
		//  Inform ComboBox that popup was hidden
		if (comboBox instanceof CComboBox)
			(CComboBox)comboBox).hidingPopup();
		else if (comboBox instanceof CComboBox)
			(CComboBox)comboBox).hidingPopup();
	}   //  hided
	/**/
}   //  CompiereComboPopup
