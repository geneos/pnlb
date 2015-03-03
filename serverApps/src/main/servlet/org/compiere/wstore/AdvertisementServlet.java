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
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Web Request.
 *
 *  @author     Jorg Janke
 *  @version    $Id: AdvertisementServlet.java,v 1.13 2005/09/19 04:46:33 jjanke Exp $
 */
public class AdvertisementServlet extends HttpServlet
{
	/**	Logging						*/
	private static CLogger			log = CLogger.getCLogger(AdvertisementServlet.class);
	/** Name						*/
	static public final String		NAME = "AdvertisementServlet";

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
			throw new ServletException("AdvertisementServlet.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	public String getServletInfo()
	{
		return "Compiere Web Avertisement Servlet";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	public void destroy()
	{
		log.fine("");
	}   //  destroy

	/*************************************************************************/

	public static final String  P_ADVERTISEMENT_ID	= "W_Advertisement_ID";

	/**
	 *  Process the HTTP Get request
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
		log.info("Get from " + request.getRemoteHost() + " - " + request.getRemoteAddr() + " - forward to request.jsp");
		response.sendRedirect("advertisements.jsp");
	}   //  doGet


	/**************************************************************************
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

		//  Get Session attributes
		HttpSession session = request.getSession(true);
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
		//
		Properties ctx = JSPEnv.getCtx(request);
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		if (wu == null)
		{
			log.warning("No web user");
			response.sendRedirect("loginServlet?ForwardTo=advertisement.jsp");	//	entry
			return;
		}
		int W_Advertisement_ID = WebUtil.getParameterAsInt(request, P_ADVERTISEMENT_ID);
		MAdvertisement ad = new MAdvertisement (ctx, W_Advertisement_ID, null);
		if (ad.get_ID() == 0)
		{
			WebUtil.createForwardPage(response, "Web Advertisement Not Found", "advertisements.jsp", 0);
			return;
		}
		StringBuffer info = new StringBuffer();
		//
		String Name = WebUtil.getParameter (request, "Name");
		if (Name != null && Name.length() > 0 && !Name.equals(ad.getName()))
		{
			ad.setName(Name);
			info.append("Name - ");
		}
		String Description = WebUtil.getParameter (request, "Description");
		if (Description != null && Description.length() > 0 && !Description.equals(ad.getDescription()))
		{
			ad.setDescription(Description);
			info.append("Description - ");
		}
		String ImageURL = null;
		String AdText = WebUtil.getParameter (request, "AdText");
		if (AdText != null && AdText.length() > 0 && !AdText.equals(ad.getAdText()))
		{
			ad.setAdText(AdText);
			info.append("AdText - ");
		}
		String ClickTargetURL = WebUtil.getParameter (request, "ClickTargetURL");
		if (ClickTargetURL != null && ClickTargetURL.length() > 0 && !ClickTargetURL.equals(ad.getClickTargetURL()))
		{
			ad.setClickTargetURL(ClickTargetURL);
			info.append("ClickTargetURL - ");
		}
		if (info.length() > 0)
		{
			if (ad.save())
				WebUtil.createForwardPage(response, "Web Advertisement Updated: " + info.toString(), "advertisements.jsp", 0);
			else
				WebUtil.createForwardPage(response, "Web Advertisement Update Error", "advertisements.jsp", 0);
		}
		else
			WebUtil.createForwardPage(response, "Web Advertisement not changed", "advertisements.jsp", 0);
	}   //  doPost

}   //  AdvertisementSerlet
