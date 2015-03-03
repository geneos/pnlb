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
package org.compiere.www;

import java.util.*;
import javax.servlet.http.*;
import org.compiere.model.*;


/**
 *  WWindow Status Information (Value Object)
 *
 *  @author Jorg Janke
 *  @version  $Id: WWindowStatus.java,v 1.7 2006/01/11 06:55:19 jjanke Exp $
 */
public class WWindowStatus
{
	/**
	 * 	Get Web Window Status.
	 	WWindowStatus ws = WWindowStatus.get(ctx);
	 *	@param request request
	 *	@return ctx or null
	 */
	public static WWindowStatus get (HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);
		if (session == null)
			return null;
		return (WWindowStatus)session.getAttribute(NAME);
	}	//	get
	
	/**************************************************************************
	 *  Constructor - First Tab - First Row - Single Row.
	 *  <br>
	 *  Initialize Formats
	 *  @param mWindowVO window VO
	 */
	public WWindowStatus (MWindowVO mWindowVO)
	{
		mWindow = new MWindow(mWindowVO);
		curTab = mWindow.getTab(0);
		curTab.setSingleRow(true);
		//
		ctx = mWindowVO.ctx;
	}   //  WWindowStatus

	/**	Session Attribute Name			*/
	public static final String NAME	= "WWindowStatus"; 
	
	/** The MWindow                 */
	protected MWindow       mWindow;
	/** The current MTab            */
	protected MTab          curTab;

	/** Window Context 				*/
	public Properties    ctx = null;

	/**
	 *  String representation
	 *  @return String representation
	 */
	public String toString()
	{
		return "WWindowStatus[" + mWindow
			+ " - " + curTab + "]";
	}   //  toString

}   //  WWindowStatus
