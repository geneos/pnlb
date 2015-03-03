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

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Web Store Subscription Info.
 *	http://dev2/wstore/infoServlet?mode=subscribe&area=1000002&contact=1000000
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: InfoServlet.java,v 1.13 2006/01/23 04:55:51 jjanke Exp $
 */
public class InfoServlet  extends HttpServlet
{
	/**	Logging						*/
	private CLogger			log = CLogger.getCLogger(getClass());

	/**
	 * 	Initialize global variables
	 *  @param config servlet configuration
	 *  @throws ServletException
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		if (!WebEnv.initWeb(config))
			throw new ServletException("InfoServlet.init");
	}	//	init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	public String getServletInfo()
	{
		return "Compiere Interest Area Servlet";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	public void destroy()
	{
		log.info("destroy");
	}   //  destroy

	/*************************************************************************/

	/**
	 *  Process the initial HTTP Get request.
	 *  Reads the Parameter Amt and optional C_Invoice_ID
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("From " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		HttpSession session = request.getSession(true);
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
	//	WEnv.dump(session);
	//	WEnv.dump(request);

		boolean success = processParameter(request);

		String url = "/info.jsp";
		log.info ("Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
	}   //  doGet

	/**
	 *  Process the HTTP Post request.
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("From " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		doGet (request, response);
	}   //  doPost


	/**************************************************************************
	 * 	Process Parameter and check them
	 * 	@param request request
	 *	@return true if processed
	 */
	private boolean processParameter (HttpServletRequest request)
	{
		HttpSession session = request.getSession(true);
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
		Properties ctx = JSPEnv.getCtx(request);

		//	mode = subscribe
		String mode = WebUtil.getParameter (request, "mode");
		if (mode == null)
			return false;
		boolean subscribe = !mode.startsWith("un");
		//	area = 101
		int R_InterestArea_ID = WebUtil.getParameterAsInt(request, "area");
		MInterestArea ia = MInterestArea.get(ctx, R_InterestArea_ID);
		//	contact = -1
		int AD_User_ID = WebUtil.getParameterAsInt(request, "contact");
		//
		log.fine("Subscribe=" + subscribe
			+ ",R_InterestArea_ID=" + R_InterestArea_ID
			+ ",AD_User_ID=" + AD_User_ID);
		if (R_InterestArea_ID == 0 || AD_User_ID == 0)
			return false;
		//
		MContactInterest ci = MContactInterest.get (ctx, R_InterestArea_ID, AD_User_ID);
		ci.subscribe(subscribe);
		boolean ok = ci.save();
		if (ok)
			log.fine("success");
		else
			log.log(Level.SEVERE, "subscribe failed");

		//	Lookup user if direct link
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		if (wu == null)
		{
			wu = WebUser.get(ctx, AD_User_ID);
			session.setAttribute(WebUser.NAME, wu);
		}
		sendEMail (request, wu, ia.getName(), subscribe);

		return ok;
	}	//	processParameter

	/**
	 * 	Send Subscription EMail.
	 * 	@param request request
	 * 	@param wu web user
	 */
	private void sendEMail (HttpServletRequest request, WebUser wu, 
		String listName, boolean subscribe)
	{
		String msg = JSPEnv.sendEMail(request, wu, 
			subscribe ? MMailMsg.MAILMSGTYPE_Subscribe : MMailMsg.MAILMSGTYPE_UnSubscribe,
			new Object[]{listName, wu.getName(), listName});
	}	//	sendEMail


}	//	InfoServlet
