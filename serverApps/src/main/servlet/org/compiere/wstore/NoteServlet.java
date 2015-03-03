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
 *  Web Notice.
 *
 *  @author     Jorg Janke
 *  @version    $Id: NoteServlet.java,v 1.13 2006/01/11 06:55:19 jjanke Exp $
 */
public class NoteServlet extends HttpServlet
{
	/**	Logging						*/
	private CLogger					log = CLogger.getCLogger(getClass());
	/** Name						*/
	static public final String		NAME = "NoteServlet";

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
			throw new ServletException("NoteServlet.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	public String getServletInfo()
	{
		return "Compiere Web Note Servlet";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	public void destroy()
	{
		log.fine("destroy");
	}   //  destroy

	/*************************************************************************/

	public static final String  P_Note_ID			= "AD_Note_ID";
	public static final String	P_ATTACHMENT_INDEX 	= "AttachmentIndex";

	/**
	 *  Process the HTTP Get request.
	 *  Attachment Download request
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

			//	Parameter = Note_ID - if is valid and belongs to wu then create PDF & stream it
			String msg = streamAttachment (request, response);
			if (msg == null || msg.length() == 0)
				return;
			if (info != null)
				info.setMessage(msg);
		}

		log.info ("Forward to " + url);
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
		//	Get Note ID
		int AD_Note_ID = WebUtil.getParameterAsInt(request, P_Note_ID);
		if (AD_Note_ID == 0)
		{
			log.fine("No AD_Note_ID)");
			return "No Notice ID";
		}
		int attachmentIndex = WebUtil.getParameterAsInt(request, P_ATTACHMENT_INDEX);
		if (attachmentIndex == 0)
		{
			log.fine("No index)");
			return "No Request Attachment index";
		}
		log.info("AD_Notice_ID=" + AD_Note_ID + " / " + attachmentIndex);

		//	Get Note
		Properties ctx = JSPEnv.getCtx(request);
		MNote doc = new MNote (ctx, AD_Note_ID, null);
		if (doc.getAD_Note_ID() != AD_Note_ID)
		{
			log.fine("Note not found - ID=" + AD_Note_ID);
			return "Notice not found";
		}
		
		MAttachment attachment = doc.getAttachment(false);
		if (attachment == null)
		{
			log.fine("No Attachment for AD_Note_ID=" + AD_Note_ID);
			return "Notice Attachment not found";
		}
		
		//	Get WebUser & Compare with invoice
		HttpSession session = request.getSession(true);
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		if (wu.getAD_User_ID() != doc.getAD_User_ID())
		{
			log.warning ("AD_Note_ID="
				+ AD_Note_ID + " - User_ID=" + doc.getAD_User_ID()
				+ " = Web_User=" + wu.getAD_User_ID());
			return "Your Notice not found";
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
		log.info("From " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		String url = "/notes.jsp";

		//  Get Session attributes
		HttpSession session = request.getSession(true);
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
		//
		Properties ctx = JSPEnv.getCtx(request);
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		if (wu == null)
		{
			log.warning("No web user");
			response.sendRedirect("loginServlet?ForwardTo=notes.jsp");	//	entry
			return;
		}
		WebEnv.dump(request);
		
		int AD_Note_ID = WebUtil.getParameterAsInt(request, P_Note_ID);
		String processed = WebUtil.getParameter (request, "Processed");
		boolean prc = processed != null && processed.length() > 0;
		if (prc)
		{
			MNote note = new MNote (ctx, AD_Note_ID, null);
			if (note.get_ID() == AD_Note_ID)
			{
				note.setProcessed(true);
				note.save();
				log.fine("doPost - " + note);
			}
		}
		
		//	Redisplay
		log.info ("Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
	}   //  doPost

}   //  NoteServlet
