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
package org.compiere.apps;

import java.awt.*;

/**
 * 	Apps Keyboard Focus Manager
 *	
 *  @author Jorg Janke
 *  @version $Id: AKeyboardFocusManager.java,v 1.1 2005/12/05 02:36:08 jjanke Exp $
 */
public class AKeyboardFocusManager extends DefaultKeyboardFocusManager
{
	/**
	 * 	Get default Keyboard Focus Mgr
	 *	@return focus manager
	 */
	public static AKeyboardFocusManager get()
	{
		if (s_kfm == null)
			s_kfm = new AKeyboardFocusManager();
		return s_kfm;
	}	//	get
	
	/**	Default Focus Manager				*/
	private static AKeyboardFocusManager	s_kfm = new AKeyboardFocusManager();
	
	/**
	 * 	Constructor
	 */
	public AKeyboardFocusManager ()
	{
		super ();
		setDefaultFocusTraversalPolicy(AFocusTraversalPolicy.get());
	}	//	AKeyboardFocusManager
	
}	//	AKeyboardFocusManager
