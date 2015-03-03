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
import javax.swing.border.*;

import org.compiere.model.*;
import org.compiere.swing.*;

/**
 *	Editor Interface for single Row Editors (also used as TableCellEditors).
 *  <p>
 *  Editors fire VetoableChange to inform about new entered values
 *  and listen to propertyChange (MField.PROPERTY) to receive new values
 *  or to (MField.ATTRIBUTE) in changes of Background or Editability
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VEditor.java,v 1.12 2005/12/01 01:55:35 jjanke Exp $
 */
public interface VEditor extends CEditor, PropertyChangeListener
{
	/**
	 *	Get Column Name
	 * 	@return column name
	 */
	public String getName();

	/**
	 *	Set Column Name
	 * 	@patam columnName name
	 */
	public void setName(String columnName);

	/**
	 *	Change Listener Interface
	 *  @param listener
	 */
	public void addVetoableChangeListener(VetoableChangeListener listener);
	/**
	 *	Change Listener Interface
	 *  @param listener
	 */
	public void removeVetoableChangeListener(VetoableChangeListener listener);
	/**
	 *  Action Listener
	 *  @param listener
	 */
	public void addActionListener(ActionListener listener);
//	public void removeActionListener(ActionListener listener);

	/**
	 *  Used to set border for table editors
	 *  @param border
	 */
	public void setBorder(Border border);

	/**
	 *  Set Font
	 *  @param font
	 */
	public void setFont(Font font);

	/**
	 *	Set Foreground
	 *  @param color
	 */
	public void setForeground(Color color);

	/**
	 *  Set Field/WindowNo for ValuePreference
	 *  @param mField
	 */
	public void setField (MField mField);

	/**
	 *  Dispose
	 */
	public void dispose();

}	//	VEditor
