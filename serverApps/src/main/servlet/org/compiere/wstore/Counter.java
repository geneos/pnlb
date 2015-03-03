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

import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Web Page Counter
 *  <code>
	http://www.compiere.com/wstore/counter
 *  </code>
 *
 *  @author     Jorg Janke
 *  @version    $Id: Counter.java,v 1.12 2005/10/14 00:42:23 jjanke Exp $
 */
public class Counter extends HttpServlet implements Runnable
{
	/**	Logging						*/
	private static CLogger			log = CLogger.getCLogger(Counter.class);

	/** Name						*/
	static public final String		NAME = "counter";

	/**	Requests					*/
	private List<HttpServletRequest>	m_requests = Collections.synchronizedList(new ArrayList<HttpServletRequest>());

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
			throw new ServletException("Counter.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	public String getServletInfo()
	{
		return "Compiere Web Counter";
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
		m_requests.add(request);
		new Thread(this).start();
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
		doGet (request, response);
	}   //  doPost

	/**************************************************************************
	 * 	Async Process
	 */
	public void run()
	{
		long time = System.currentTimeMillis();
		//	get Request
		HttpServletRequest request = null;
		if (m_requests.size() > 0)
			request = (HttpServletRequest)m_requests.remove(0);
		if (request == null)
		{
			log.log(Level.SEVERE, "Nothing in queue");
			return;
		}

		Properties ctx = JSPEnv.getCtx(request);
		String ref = request.getHeader("referer");
		if (ref == null || ref.length() == 0)
			ref = request.getRequestURL().toString();
		log.info("Referer=" + request.getHeader("referer") + " - URL=" + request.getRequestURL());
	}	//	run

}   //  Counter
