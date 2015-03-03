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
import org.compiere.util.*;

/**
 *  ComboBox Selection Manager for AuroReduction
 *
 *  @author Jorg Janke
 *  @version  $Id: ComboSelectionManager.java,v 1.4 2005/03/11 20:28:24 jjanke Exp $
 */
public class ComboSelectionManager implements JComboBox.KeySelectionManager
{
	/**
	 *
	 */
	public ComboSelectionManager()
	{
	}   //  ComboSelectionManager

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ComboSelectionManager.class);
	
	/**
	 *  Given <code>aKey</code> and the model, returns the row
	 *  that should become selected. Return -1 if no match was
	 *  found.
	 *
	 *  @param key  a char value, usually indicating a keyboard key that was pressed
	 *  @param model a ComboBoxModel -- the component's data model, containing the list of selectable items
	 *  @return an int equal to the selected row, where 0 is the first item and -1 is none
	 */
	public int selectionForKey (char key, ComboBoxModel model)
	{
		log.fine("Key=" + key);
		//
		int currentSelection = -1;
		Object selectedItem = model.getSelectedItem();


		return 0;
	}   //	selectionForKey


}   //  ComboSelectionManager
