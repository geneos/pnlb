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
import org.compiere.wf.*;

/**
 *  Web Workflow.
 *
 *  @author     Jorg Janke
 *  @version    $Id: WorkflowServlet.java,v 1.11 2006/01/11 06:55:19 jjanke Exp $
 */
public class WorkflowServlet extends HttpServlet
{
	/**	Logging						*/
	private CLogger					log = CLogger.getCLogger(getClass());
	/** Name						*/
	static public final String		NAME = "WorkflowServlet";

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
			throw new ServletException("WorkflowServlet.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	public String getServletInfo()
	{
		return "Compiere Web Workflow Servlet";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	public void destroy()
	{
		log.fine("destroy");
	}   //  destroy

	/*************************************************************************/

	public static final String  P_WF_Activity_ID	= "AD_WF_Activity_ID";
	public static final String	P_ATTACHMENT_INDEX 	= "AttachmentIndex";

	/**
	 *  Process the HTTP Get request.
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
		log.info("doGet from " + request.getRemoteHost() + " - " + request.getRemoteAddr() + " - forward to notes.jsp");
		String url = "/notes.jsp";
		//
		HttpSession session = request.getSession(false);
		if (session == null 
			|| session.getAttribute(Info.NAME) == null)
			url = "/login.jsp";
		else
		{
			session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
			Info info = (Info)session.getAttribute(Info.NAME);
			if (info != null)
				info.setMessage("");

			//	Parameter = Activity_ID - if valid and belongs to wu then create PDF & stream it
			String msg = streamAttachment (request, response);
			if (msg == null || msg.length() == 0)
				return;
			if (info != null)
				info.setMessage(msg);
		}

		log.info ("doGet - Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
	}   //  doGet

	/**
	 * 	Stream Attachment
	 * 	@param request request
	 * 	@param response response
	 * 	@return "" or error message
	 */
	private String streamAttachment (HttpServletRequest request, HttpServletResponse response)
	{
		//	Get Activity ID
		int AD_WF_Activity_ID = WebUtil.getParameterAsInt(request, P_WF_Activity_ID);
		if (AD_WF_Activity_ID == 0)
		{
			log.fine("streamAttachment - no AD_WF_Activity_ID)");
			return "No Activity ID";
		}
		int attachmentIndex = WebUtil.getParameterAsInt(request, P_ATTACHMENT_INDEX);
		if (attachmentIndex == 0)
		{
			log.fine("streamAttachment - no index)");
			return "No Request Attachment index";
		}
		log.info("streamAttachment - AD_WF_Activity_ID=" + AD_WF_Activity_ID + " / " + attachmentIndex);

		//	Get Note
		Properties ctx = JSPEnv.getCtx(request);
		MWFActivity doc = new MWFActivity (ctx, AD_WF_Activity_ID, null);
		if (doc.get_ID() != AD_WF_Activity_ID)
		{
			log.fine("streamAttachment - Activity not found - ID=" + AD_WF_Activity_ID);
			return "Activity not found";
		}
		
		MAttachment attachment = doc.getAttachment(false);
		if (attachment == null)
		{
			log.fine("streamAttachment - No Attachment for AD_WF_Activity_ID=" + AD_WF_Activity_ID);
			return "Notice Attachment not found";
		}

		//	Get WebUser & Compare with invoice
		HttpSession session = request.getSession(true);
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		if (wu.getAD_User_ID() != doc.getAD_User_ID())
		{
			log.warning ("streamAttachment - AD_WF_Activity_ID="
				+ AD_WF_Activity_ID + " - User_Activity=" + doc.getAD_User_ID()
				+ " = Web_User=" + wu.getAD_User_ID());
			return "Your Activity not found";
		}

		//	Stream it
		return WebUtil.streamAttachment (response, attachment, attachmentIndex);
	}	//	streamAttachment


	/**************************************************************************
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
		log.info("doPost from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		String url = "/notes.jsp";
		//
	//	Log.setTraceLevel(9);
	//	WebEnv.dump(request);
		//
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute(Info.NAME) == null)
			url = "/login.jsp";
		else
		{
			session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
			Properties ctx = JSPEnv.getCtx(request);
			WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
			if (wu == null)
			{
				log.warning("doPost - no web user");
				response.sendRedirect("loginServlet?ForwardTo=note.jsp");	//	entry
				return;
			}
			//	Get Feedback
			int AD_WF_Activity_ID = WebUtil.getParameterAsInt(request, P_WF_Activity_ID);
			boolean isConfirmed = WebUtil.getParameterAsBoolean (request, "IsConfirmed");
			boolean isApproved = WebUtil.getParameterAsBoolean (request, "IsApproved");
			boolean isRejected = WebUtil.getParameterAsBoolean (request, "IsApproved");
			String textMsg = WebUtil.getParameter (request, "textMsg");
			log.fine("doPost - TextMsg=" + textMsg);
			//
			MWFActivity act = new MWFActivity (ctx, AD_WF_Activity_ID, null);
			log.fine("doPost - " + act);
			
			if (AD_WF_Activity_ID == 0 || act == null || act.getAD_WF_Activity_ID() != AD_WF_Activity_ID)
				session.setAttribute(WebSessionCtx.HDR_MESSAGE, "Activity not found");
			else
			{
				if (act.isUserApproval() && (isApproved || isRejected))
				{
					try
					{
						act.setUserChoice(wu.getAD_User_ID(), isApproved ? "Y" : "N", 
							DisplayType.YesNo, textMsg);
						act.save();
					}
					catch (Exception e)
					{
					}
				}	//	approval
				else if (act.isUserManual() && isConfirmed)
				{
					act.setUserConfirmation(wu.getAD_User_ID(), textMsg);
					act.save();
				}
				else if (textMsg != null && textMsg.length() > 0)
				{
					act.setTextMsg (textMsg);
					act.save();
				}
			}
		}
		
		log.info ("doGet - Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
	}   //  doPost

}   //  WorkflowServlet
