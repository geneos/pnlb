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
package org.compiere.grid;

import org.compiere.apps.*;

/**
 *	Application Panel Tab Interface.
 *  Interface for CPanels displayed as a Tab in APanel
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: APanelTab.java,v 1.3 2005/03/11 20:28:28 jjanke Exp $
 */
public interface APanelTab
{
	/**
	 * 	Load Data
	 *  Called when tab is displayed.
	 */
	public void loadData();

	/**
	 * 	Save Data
	 *  Called when tab is swiched to another tab.
	 */
	public void saveData();

	/**
	 * 	Register APanel
	 * 	@param panel panel
	 */
	public void registerAPanel (APanel panel);

	/**
	 * 	Unregister APanel
	 */
	public void unregisterPanel ();

}	//	APanelTab
