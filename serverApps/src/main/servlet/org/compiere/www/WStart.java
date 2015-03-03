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
 *  Application Start Page
 *  <pre>
 *  Creates a Frame with
 *  - Command   - cmd.html  (invisible)
 *  - Menu      - menu.html
 *  - Window    = window.html
 *  </pre>
 *  framesetOuter
 *  +- WCmd
 *  +- framesetMenuWindow
 *     +- WMenu
 *     +- framesetWindow
 *        +- WPopUp
 *        +- WWindow
 *  see webapps/compiere/index.html
 *  @author Jorg Janke
 *  @version $Id: WStart.java,v 1.8 2005/05/17 05:30:59 jjanke Exp $
 */
public class WStart extends HttpServlet
{
	/**
	 *  Set UI directory to Servlet init param
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		if (!WebEnv.initWeb(config))
			throw new ServletException("WStart.init");
	}   //  init

	/**
	 * Process the HTTP Get request
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		WebUtil.createLoginPage (request, response, this, null, null);
	}   //  doGet


	/**
	 *  Process the HTTP Post request
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		doGet (request, response);
	}   //  doPost

}   //  WStart
