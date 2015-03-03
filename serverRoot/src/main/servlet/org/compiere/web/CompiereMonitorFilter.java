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
package org.compiere.web;

import java.io.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.http.*;
import org.compiere.model.*;
import org.compiere.util.*;
import sun.misc.*;

/**
 * 	Compiere Monitor Filter.
 * 	Application Server independent check of username/password
 * 	
 *  @author Jorg Janke
 *  @version $Id: CompiereMonitorFilter.java,v 1.5 2005/03/21 04:54:22 jjanke Exp $
 */
public class CompiereMonitorFilter implements Filter
{
	/**
	 * 	CompiereMonitorFilter
	 */
	public CompiereMonitorFilter ()
	{
		super ();
		m_authorization = new Long (System.currentTimeMillis());
	}	//	CompiereMonitorFilter

	/**	Logger			*/
	protected CLogger	log = CLogger.getCLogger(getClass());

	/**	Authorization ID				*/
	private static final String		AUTHORIZATION = "CompiereAuthorization";
	/** Authorization Marker			*/
	private Long					m_authorization = null;
	
	/**
	 * 	Init
	 *	@param config configuration
	 *	@throws ServletException
	 */
	public void init (FilterConfig config)
		throws ServletException
	{
		log.info ("");
	}	//	Init

	/**
	 * 	Filter
	 *	@param request request
	 *	@param response response
	 *	@param chain chain
	 *	@throws IOException
	 *	@throws ServletException
	 */
	public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException
	{
		boolean error = false;
		String errorPage = "/error.html";
		boolean pass = false;
		try
		{
			if (!(request instanceof HttpServletRequest && response instanceof HttpServletResponse))
			{
				request.getRequestDispatcher(errorPage).forward(request, response);
				return;
			}
			HttpServletRequest req = (HttpServletRequest)request;
			HttpServletResponse resp = (HttpServletResponse)response;
			//	Previously checked
			HttpSession session = req.getSession(true);
			Long compare = (Long)session.getAttribute(AUTHORIZATION);
			if (compare != null && compare.compareTo(m_authorization) == 0)
			{
				pass = true;
			}
			else if (checkAuthorization (req.getHeader("Authorization")))
			{
				session.setAttribute(AUTHORIZATION, m_authorization);
				pass = true;
			}
			//	--------------------------------------------
			if (pass)
			{
				chain.doFilter(request, response);
			}
			else
			{
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				resp.setHeader("WWW-Authenticate", "BASIC realm=\"Compiere Server\"");
			}
			return;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "filter", e);
		}
		request.getRequestDispatcher(errorPage).forward(request, response);
	}	//	doFilter

	/**
	 * 	Check Authorization
	 *	@param authorization authorization
	 *	@return true if authenticated
	 */
	private boolean checkAuthorization (String authorization)
	{
		if (authorization == null)
			return false;
		try
		{
			String userInfo = authorization.substring(6).trim();
			BASE64Decoder decoder = new BASE64Decoder();
			String namePassword = new String (decoder.decodeBuffer(userInfo));
		//	log.fine("checkAuthorization - Name:Password=" + namePassword);
			int index = namePassword.indexOf(":");
			String name = namePassword.substring(0, index);
			String password = namePassword.substring(index+1);
			MUser user = MUser.get(Env.getCtx(), name, password);
			if (user == null)
			{
				log.warning ("User not found: '" + name + "/" + password + "'");
				return false;
			}
			if (!user.isAdministrator())
			{
				log.warning ("Not a Sys Admin = " + name);
				return false;
			}
			log.info ("Name=" + name);
			return true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "check", e);
		}
		return false;
	}	//	check
	
	/**
	 * 	Destroy
	 */
	public void destroy ()
	{
		log.info ("");
	}	//	destroy

}	//	CompiereMonitorFilter
