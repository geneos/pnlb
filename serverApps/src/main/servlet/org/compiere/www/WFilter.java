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
import java.util.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.ecs.*;
import org.apache.ecs.xhtml.*;
import org.compiere.util.*;

/**
 *  HTML UI Filter to do timing and list parameters
 *
 *  @version $Id: WFilter.java,v 1.10 2005/07/18 03:56:03 jjanke Exp $
 */

public final class WFilter implements javax.servlet.Filter
{
	/**  The filter configuration object we are associated with.
	 *   If this value is null, this filter instance is not currently configured    */
	private FilterConfig 		m_filterConfig = null;

	/** Timing indicator                    */
	private boolean             m_timing = false;
	/**	Logging								*/
	private static CLogger		log = null;

	/**
	 *  Place this filter into service.
	 *
	 *  @param filterConfig The filter configuration object
	 *  @throws ServletException
	 */
	public void init (FilterConfig filterConfig) throws ServletException
	{
		m_filterConfig = filterConfig;
		WebEnv.initWeb(filterConfig.getServletContext());
		if (log == null)
			log = CLogger.getCLogger(WFilter.class);

		//  List all Parameters
		log.info(filterConfig.getFilterName());
		Enumeration en = filterConfig.getInitParameterNames();
		while (en.hasMoreElements())
		{
			String name = en.nextElement().toString();
			String value = filterConfig.getInitParameter(name);
			log.config(name + "=" + value);
			if (name.equals("Timing") && value.equals("Y"))
				m_timing = true;
		}
	}   //  init

	/**
	 *  Take this filter out of service.
	 */
	public void destroy()
	{
		m_filterConfig = null;
	}   //  destroy

	/**
	 *  Time the processing that is performed by all subsequent filters in the
	 *  current filter stack, including the ultimately invoked servlet.
	 *
	 * @param request The servlet request we are processing
	 * @param response response
	 * @param chain The filter chain we are processing
	 *
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet error occurs
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException
	{
		//  Get URI
		String uri = "";
		if (request instanceof HttpServletRequest)
		{
			HttpServletRequest req = (HttpServletRequest)request;
			uri = req.getRequestURI();
		}

		//  Ignore static content
		boolean check = true;
		if (!uri.startsWith(WebEnv.DIR_BASE)      //  not requesting /compiere/...
			|| uri.endsWith(".gif") || uri.endsWith(".jpg") || uri.endsWith(".png") 
			|| uri.endsWith(".html") || uri.endsWith(".css")
			|| uri.endsWith(".js"))
			check = false;
		//
		boolean pass = true;

		// We need to check
		StringBuffer sb = new StringBuffer ("| Parameters");
		if (check)
		{
			try
			{
				String enc = request.getCharacterEncoding();
				if (enc == null)
					request.setCharacterEncoding(WebEnv.ENCODING);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "Set CharacterEndocung=" + WebEnv.ENCODING, e);
			}
			//  print parameter
			Enumeration en = request.getParameterNames();
			while (en.hasMoreElements())
			{
				String name = (String)en.nextElement();
				sb.append(" - ").append(name).append("=").append(request.getParameter(name));
			}
			if (uri.endsWith("WWindowStatus"))
				pass = false;
		}
		if (pass && check)
			log.info("Start " + uri + sb.toString());

		//  Timing
		long myTime = 0l;
		if (pass && check && m_timing)
			myTime = System.currentTimeMillis();

		//  **  Start   **
		if (pass)
			chain.doFilter(request, response);
		else
		{
			log.warning("Rejected " + uri);
			String msg = "Error: Access Rejected";
			WebDoc doc = WebDoc.create (msg);
			//	Body
			body b = doc.getBody();
			b.addElement(new p(uri, AlignType.CENTER));
			//	fini
			response.setContentType("text/html");
			PrintWriter out = new PrintWriter (response.getOutputStream());
			doc.output(out);
			out.close();
		}

		//  Post
		if (check && pass)
		{
			if (m_timing)
				myTime = System.currentTimeMillis() - myTime;
			log.info("End   " + uri + "| " + (m_timing ? String.valueOf(myTime) : null));
		}
	}   //  doFilter


	/**
	 *  Return a String representation of this object.
	 *  @return String info
	 */
	public String toString()
	{
		if (m_filterConfig == null)
			return ("WFilter[]");
		StringBuffer sb = new StringBuffer("WFilter[");
		sb.append(m_filterConfig);
		sb.append("]");
		return (sb.toString());
	}   //  toString

}   //  Filter
