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
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.ecs.*;
import org.apache.ecs.xhtml.*;
import org.compiere.util.*;

/**
 * 	Web Store Filter
 *	
 *  @author Jorg Janke
 *  @version $Id: StoreFilter.java,v 1.1 2005/07/18 03:56:02 jjanke Exp $
 */
public class StoreFilter implements javax.servlet.Filter
{
	/**	Logging								*/
	private static CLogger		log = null;

	/**
	 * 	Init
	 *	@param config configuration
	 *	@throws ServletException
	 */
	public void init (FilterConfig config) throws ServletException
	{
		WebEnv.initWeb(config.getServletContext());
		if (log == null)
			log = CLogger.getCLogger(StoreFilter.class);
		log.info(config.getFilterName());
	}	//	init
	
	/**
	 * 	Destroy
	 */
	public void destroy ()
	{
	}	//	destroy

	
	public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
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
		boolean check = uri.indexOf("Servlet") != -1;
		boolean pass = true;

		// We need to check
		if (check)
		{
			String enc = request.getCharacterEncoding();
			try
			{
				enc = request.getCharacterEncoding();
				if (enc == null)
					request.setCharacterEncoding(WebEnv.ENCODING);
				if (enc == null)
					log.finer("Checked=" + uri);
				else
					log.finer("Checked=" + uri + " - Enc=" + enc);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "Set CharacterEndocung=" 
					+ enc + "->" + WebEnv.ENCODING, e);
			}
		}
	//	else
	//		log.finer("NotChecked=" + uri);
		
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

	}	//	doFilter
	
}	//	StoreFilter
