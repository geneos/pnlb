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
package org.compiere.wstore;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.util.*;


/**
 *  Check Out.
 *
 *  @author Jorg Janke
 *  @version $Id: CheckOutServlet.java,v 1.14 2005/07/18 03:56:02 jjanke Exp $
 */
public class CheckOutServlet extends HttpServlet
{
	/**	Logging						*/
	private CLogger			log = CLogger.getCLogger(getClass());
	/** Name						*/
	static public final String		NAME = "checkOutServlet";
	/** Attribute					*/
	static public final String		ATTR_CHECKOUT = "CheckOut";

	/**
	 *	Initialize global variables
	 *
	 *  @param config Configuration
	 *  @throws ServletException
	 */
	public void init(ServletConfig config)
		throws ServletException
	{
		super.init(config);
		if (!WebEnv.initWeb(config))
			throw new ServletException("CheckOutServlet.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	public String getServletInfo()
	{
		return "Compiere Web CheckOut Servlet";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	public void destroy()
	{
		log.fine("destroy");
	}   //  destroy


	/**
	 *  Process the HTTP Get request.
	 * 	(logout, deleteCookie)
	 *  Sends Web Request Page
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("Get from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		HttpSession session = request.getSession(true);
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);

		//	Web User/Basket
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		WebBasket wb = (WebBasket)session.getAttribute(WebBasket.NAME);

		String url = "/login.jsp";
		//	Nothing in basket
		if (wb == null || wb.getLineCount() == 0)
			url = "/basket.jsp";
		else
		{
			session.setAttribute(ATTR_CHECKOUT, "Y");	//	indicate checkout
			if (wu != null && wu.isLoggedIn ())
				url = "/addressInfo.jsp";
		}

	//	if (request.isSecure())
	//	{
			log.info ("Forward to " + url);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
			dispatcher.forward (request, response);
	//	}
	//	else
		//	Switch to secure
	//	{
	//		url = "https://" + request.getServerName() + request.getContextPath() + "/" + url;
	//		log.info ("doGet - Secure Forward to " + url);
	//		WUtil.createForwardPage(response, "Secure Access", url);
	//	}
	}	//	doGet

	/**
	 *  Process the HTTP Post request
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("Post from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		HttpSession session = request.getSession(false);
	}	//	doPost

}	//	CheckOutServlet
