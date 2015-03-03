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
 *	Click Counter.
 * 	Counts the click and forwards.
 * 	<code>
	http://www.compiere.com/wstore/click?target=www.yahoo.com
	http://www.compiere.com/wstore/click/www.yahoo.com
	http://www.compiere.com/wstore/click?www.yahoo.com
 *  </code>
 *
 *  @author Jorg Janke
 *  @version $Id: Click.java,v 1.12 2005/10/14 00:42:23 jjanke Exp $
 */
public class Click  extends HttpServlet
{
	/**	Logging						*/
	private CLogger						log = CLogger.getCLogger(getClass());

	/** Name						*/
	static public final String			NAME = "click";

	/** Target Parameter			*/
	static public final String			PARA_TARGET = "target";
	/**	Fallback Target				*/
	static public final String			DEFAULT_TARGET = "http://www.compiere.org/";

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
			throw new ServletException("Click.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	public String getServletInfo()
	{
		return "Compiere Click Servlet";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	public void destroy()
	{
		log.fine("destroy");
	}   //  destroy

	
	/**************************************************************************
	 *  Process the HTTP Get request.
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		long time = System.currentTimeMillis();
		request.getSession(true);	//	force create session for ctx
		//
		String url = getTargetURL(request);
		response.sendRedirect(url);
		response.flushBuffer();
		log.fine("redirect - " + url);
		
		//	Save Click
		saveClick(request, url);
		//
		log.fine(url + " - " + (System.currentTimeMillis()-time) + "ms");
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
		doGet (request, response);
	}	//	doPost

	/**
	 * 	Get Target URL.
	 * 	1 - target parameter
	 *  3 - parameter
	 *  2 - path
	 *	@param request request
	 *	@return URL
	 */
	private String getTargetURL (HttpServletRequest request)
	{
		//	Get Named Parameter		-	/click?target=www...
		String url = WebUtil.getParameter(request, PARA_TARGET);
		//	Check parameters		-	/click?www...
		if (url == null || url.length() == 0)
		{
			Enumeration e = request.getParameterNames ();
			if (e.hasMoreElements ())
				url = (String)e.nextElement ();
		}
		//	Check Path				-	/click/www...
		if (url == null || url.length() == 0)
		{
			url = request.getPathInfo ();
			if (url != null)
				url = url.substring(1);		//	cut off initial /
		}
		//	Still nothing
		if (url == null || url.length() == 0)
			url = DEFAULT_TARGET;
		//	add http protocol
		if (url.indexOf("://") == -1)
			url = "http://" + url;
		return url;
	}	//	getTargetURL

	/**
	 * 	Save Click
	 */
	private boolean saveClick (HttpServletRequest request, String url)
	{
		Properties ctx = JSPEnv.getCtx(request);
		//
		MClick mc = new MClick (ctx, url, null);
		mc.setRemote_Addr(request.getRemoteAddr());
		mc.setRemote_Host(request.getRemoteHost());
		String ref = request.getHeader("referer");
		if (ref == null || ref.length() == 0)
			ref = request.getRequestURL().toString();
		mc.setReferrer(ref);
		//
		mc.setAcceptLanguage(request.getHeader("accept-language"));
		mc.setUserAgent(request.getHeader("user-agent"));
		//
		HttpSession session = request.getSession(false);
		if (session != null)
		{
			WebUser wu = (WebUser)session.getAttribute (WebUser.NAME);
			if (wu != null)
			{
				mc.setEMail (wu.getEmail());
				mc.setAD_User_ID (wu.getAD_User_ID());
			}
		}
		return mc.save();
	}	//	saveClick

}	//	Click
