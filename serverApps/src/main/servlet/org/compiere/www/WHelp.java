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

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.util.*;


/**
 *	Web (window) Help
 *	
 *  @author Jorg Janke
 *  @version $Id: WHelp.java,v 1.6 2005/03/11 20:34:47 jjanke Exp $
 */
public class WHelp extends HttpServlet
{
	/**	Logger			*/
	protected CLogger	log = CLogger.getCLogger(getClass());

	/**
	 * Initialize global variables
	 */
	public void init(ServletConfig config)
		throws ServletException
	{
		super.init(config);
		if (!WebEnv.initWeb(config))
			throw new ServletException("WHelp.init");
	}   //  init

	/**
	 * Process the HTTP Get request
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.fine("doGet");
		WWindowStatus ws = WWindowStatus.get(request);
		//
		WebDoc doc = null;
		if (ws == null)
		{
			doc = WebDoc.createPopup("No Context");
			doc.addPopupClose();
		}
		else
			doc = ws.mWindow.getHelpDoc(false);
		//
		WebUtil.createResponse(request, response, this, null, doc, false);
	}   //  doGet


	/**
	 *  Process the HTTP Post request
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		WebDoc doc = WebDoc.create ("Help - Post - Not Implemented");
		WebUtil.createResponse(request, response, this, null, doc, false);
	}   //  doPost

}	//	WHelp
