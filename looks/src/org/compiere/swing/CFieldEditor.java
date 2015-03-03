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
package org.compiere.swing;

import java.awt.Component;

import javax.swing.ComboBoxEditor;
import javax.swing.JTextField;

/**
 *  Compiere Field Editor.
 *
 *
 *  @author     Jorg Janke
 *  @version    $Id: CFieldEditor.java,v 1.5 2005/03/11 20:34:38 jjanke Exp $
 */
public class CFieldEditor extends JTextField implements ComboBoxEditor
{
	/**
	 *
	 */
	public CFieldEditor()
	{
	}

	/**
	 *  Return the component that should be added to the tree hierarchy
	 *  for this editor
	 */
	public Component getEditorComponent()
	{
		return this;
	}   //  getEditorCimponent

	/**
	 *  Set Editor
	 *  @param anObject
	 */
	public void setItem (Object anObject)
	{
		if (anObject == null)
			setText("");
		else
			setText(anObject.toString());
	}   //  setItem

	/**
	 *  Get edited item
	 *  @return edited text
	 */
	public Object getItem()
	{
		return getText();
	}   //  getItem

	/**
	 *  Returns format Info (for Popup)
	 *  @return format
	 */
	public Object getFormat()
	{
		return null;
	}   //  getFormat

}   //  CFieldEditor
