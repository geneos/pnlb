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
 *  Test Servlet 2
 *
 *  @author Jorg Janke
 *  @version  $Id: Test2.java,v 1.5 2005/03/11 20:34:48 jjanke Exp $
 */
public class Test2 extends HttpServlet
{
	/**
	 * Initialize global variables
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		WebEnv.initWeb(config);
	}   //  init

	/**
	 * Process the HTTP Get request
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher dispatcher = request.getRequestDispatcher("/Test");
		dispatcher.forward(request, response);
	}   //  doGet


	/**
	 *  Process the HTTP Post request
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		WebDoc doc = WebDoc.create ("Get Request Test2");
		//
		WebUtil.createResponse(request, response, this, null, doc, true);
	}   //  doPost

}   //  Test2
