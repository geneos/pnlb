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
import java.net.*;
import java.util.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Issue Reporting
 *	
 *  @author Jorg Janke
 *  @version $Id: IssueReportServlet.java,v 1.2 2006/01/28 01:30:38 jjanke Exp $
 */
public class IssueReportServlet extends HttpServlet
{
	/**	Logging						*/
	private static CLogger			log = CLogger.getCLogger(IssueReportServlet.class);

	/**
	 * 	Initialize global variables
	 *  @param config servlet configuration
	 *  @throws ServletException
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		if (!WebEnv.initWeb(config))
			throw new ServletException("IssueReportServlet.init");
	}	//	init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	public String getServletInfo()
	{
		return "Compiere Issue Reporting";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	public void destroy()
	{
		log.info("");
	}   //  destroy

	
	/**************************************************************************
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
		Properties ctx = JSPEnv.getCtx(request);
		HttpSession session = request.getSession(true);
	//	WEnv.dump(session);
	//	WEnv.dump(request);

		int AD_Issue_ID = WebUtil.getParameterAsInt(request, "RECORDID");
		String DBAddress = WebUtil.getParameter(request, "DBADDRESS");
		String Comments = WebUtil.getParameter(request, "COMMENTS");
		String IssueString = WebUtil.getParameter(request, "ISSUE");
		//
		StringBuffer responseText = new StringBuffer("Compiere Support - ")
			.append(new Date().toString())
			.append("\n");
		MIssue issue = null;
		if (AD_Issue_ID != 0)
		{
			issue = new MIssue(ctx, AD_Issue_ID, null);
			if (issue.get_ID() != AD_Issue_ID)
				responseText.append("Issue Unknown - Request Ignored");
			else if (!issue.getDBAddress().equals(DBAddress))
				responseText.append("Not Issue Owner - Request Ignored");
			else
			{
				issue.addComments(Comments);
				responseText.append(issue.createAnswer());
			}
		}
		else if (IssueString == null || IssueString.length() == 0)
		{
			responseText.append("Unknown Request");
		}
		else
		{
			issue = MIssue.create(ctx, IssueString);
			if (issue == null || !issue.save())
				responseText.append("Could not save Issue");
			else
				responseText.append(issue.process());
		}
		
		//
		StringBuffer answer = new StringBuffer();
		if (issue != null && issue.get_ID() != 0)
		{
			answer.append("RECORDID=").append(issue.get_ID())
				.append(MIssue.DELIMITER);
		//	answer.append("DOCUMENTNO=").append(".")
		//		.append(MIssue.DELIMITER);
		}
		answer.append("RESPONSE=").append(responseText);
		//
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();     //  with character encoding support
		out.write(URLEncoder.encode(answer.toString(), "UTF-8"));
		out.flush();
		if (out.checkError())
			log.log(Level.SEVERE, "error writing");
		out.close();
	}   //  doGet

	/**
	 *  Process the HTTP Post request.
	 * 	The actual payment processing
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
		doGet(request, response);
	}   //  doPost

}	//	IssueReportServlet
