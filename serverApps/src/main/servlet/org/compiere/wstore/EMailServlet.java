/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.wstore;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	EMail Servlet
 *  @author Jorg Janke
 *  @version $Id: EMailServlet.java,v 1.4 2005/12/09 05:20:23 jjanke Exp $
 */
public class EMailServlet extends HttpServlet
{
	/**	Logging						*/
	private static CLogger			log = CLogger.getCLogger(EMailServlet.class);

	/** Name						*/
	static public final String		NAME = "emailServlet";

	/**
	 * Initialize global variables
	 *
	 * @param config servlet config
	 * @throws ServletException
	 */
	public void init(ServletConfig config)
		throws ServletException
	{
		super.init(config);
		if (!WebEnv.initWeb(config))
			throw new ServletException("EMailServlet.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	public String getServletInfo()
	{
		return "Compiere EMail";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	public void destroy()
	{
		log.info("");
	}   //  destroy

	
	/**************************************************************************
	 *  Process the HTTP Get request
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	public void doGet (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("Get from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		doPost (request, response);
	}   //  doGet

	/**
	 *  Process the HTTP Post request
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	public void doPost (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("Post from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		Properties ctx = JSPEnv.getCtx(request);
		HttpSession session = request.getSession(true);
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);

		String url = WebUtil.getParameter(request, "ForwardTo");
		if (url == null || url.length() == 0)
			url = "emailVerify.jsp";
		
		//	Web User
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		if (wu == null)
		{
			log.warning("No web user");
			response.sendRedirect("loginServlet?ForwardTo=" + url);
			return;
		}
		
		log.info(url + " - " + wu.toString());
		//
		String cmd = WebUtil.getParameter(request, "ReSend");
		if (cmd != null && cmd.length() > 1)
			resendCode(request, wu);
		else
			wu.setEMailVerifyCode(WebUtil.getParameter(request, "VerifyCode"), request.getRemoteAddr());

		url = "/" + url;
		log.info ("Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
	}   //  doPost

	/**
	 * 	Resend Validation Code
	 * 	@param request request
	 *	@param wu user
	 */
	private void resendCode(HttpServletRequest request, WebUser wu)
	{
		String msg = JSPEnv.sendEMail(request, wu, 
			MMailMsg.MAILMSGTYPE_UserValidation,
			new Object[]{
				request.getServerName(),
				wu.getName(),
				wu.getEMailVerifyCode()});
		if (EMail.SENT_OK.equals(msg))
			wu.setPasswordMessage ("EMail sent");
		else
			wu.setPasswordMessage ("Problem sending EMail: " + msg);
	}	//	resendCode
	
}	//	EMailServlet
