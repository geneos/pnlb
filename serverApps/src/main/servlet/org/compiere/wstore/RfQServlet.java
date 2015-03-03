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
import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Web RfQ.
 *
 *  @author     Jorg Janke
 *  @version    $Id: RfQServlet.java,v 1.13 2006/01/11 06:55:19 jjanke Exp $
 */
public class RfQServlet extends HttpServlet
{
	/**	Logging						*/
	private CLogger					log = CLogger.getCLogger(getClass());
	/** Name						*/
	static public final String		NAME = "RfQServlet";

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
			throw new ServletException("RfQServlet.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	public String getServletInfo()
	{
		return "Compiere Web RfQ Servlet";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	public void destroy()
	{
		log.fine("destroy");
	}   //  destroy

	/*************************************************************************/

	public static final String  P_RfQResponse_ID	= "C_RfQResponse_ID";

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
		log.info("doGet from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		String url = "/rfqs.jsp";
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

			//	Parameter = Note_ID - if is valid create PDF & stream it
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
		//	Get Note ID
		int C_RfQ_ID = WebUtil.getParameterAsInt(request, "C_RfQ_ID");
		if (C_RfQ_ID == 0)
		{
			log.fine("streamAttachment - no ID)");
			return "No RfQ ID";
		}

		//	Get Note
		Properties ctx = JSPEnv.getCtx(request);
		MRfQ doc = new MRfQ (ctx, C_RfQ_ID, null);
		if (doc.getC_RfQ_ID() != C_RfQ_ID)
		{
			log.fine("streamAttachment - RfQ not found - ID=" + C_RfQ_ID);
			return "RfQ not found";
		}

		if (!doc.isPdfAttachment())
			return "No PDF Attachment found";
		byte[] data = doc.getPdfAttachment(); 
		if (data == null)
			return "No PDF Attachment";

		//	Send PDF
		try
		{
			int bufferSize = 2048; //	2k Buffer
			int fileLength = data.length;
			//
			response.setContentType("application/pdf");
			response.setBufferSize(bufferSize);
			response.setContentLength(fileLength);
			//
			log.fine("streamAttachment - length=" + fileLength);
			long time = System.currentTimeMillis();		//	timer start
			//
			ServletOutputStream out = response.getOutputStream ();
			out.write (data);
			out.flush();
			out.close();
			//
			time = System.currentTimeMillis() - time;
			double speed = (fileLength/1024) / ((double)time/1000);
			log.fine("streamInvoice - length=" 
				+ fileLength + " - " 
				+ time + " ms - " 
				+ speed + " kB/sec");
		}
		catch (IOException ex)
		{
			log.log(Level.SEVERE, "streamAttachment - " + ex);
			return "Streaming error";
		}
		return null;
	}	//	streamAttachment

	
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
		log.info("From " + request.getRemoteHost() + " - " + request.getRemoteAddr());
	//	Log.setTraceLevel(9);
	//	WebEnv.dump(request);
	//	WebEnv.dump(request.getSession());

		//  Get Session attributes
		HttpSession session = request.getSession(true);
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
		//
		Properties ctx = JSPEnv.getCtx(request);
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		if (wu == null)
		{
			log.warning("doPost - no web user");
			response.sendRedirect("loginServlet?ForwardTo=note.jsp");	//	entry
			return;
		}
		int C_RfQResponse_ID = WebUtil.getParameterAsInt(request, P_RfQResponse_ID);
		int C_RfQ_ID = WebUtil.getParameterAsInt(request, "C_RfQ_ID");
		MRfQResponse rfqResponse = new MRfQResponse (ctx, C_RfQResponse_ID, null);
		if (C_RfQResponse_ID == 0 || rfqResponse == null || rfqResponse.get_ID() != C_RfQResponse_ID)
		{
			WebUtil.createForwardPage(response, "RfQ Response not found", "rfqs.jsp", 5);
			return;
		}
		if (wu.getC_BPartner_ID() != rfqResponse.getC_BPartner_ID())
		{
			WebUtil.createForwardPage(response, "Your RfQ Response not found", "rfqs.jsp", 5);
			return;
		}
		//	Update Data
		String msg = updateResponse(request, rfqResponse);
		session.setAttribute(WebSessionCtx.HDR_MESSAGE, msg);
		String url = "/rfqDetails.jsp?C_RfQ_ID=" + C_RfQ_ID;
		//
		log.info ("doGet - Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
	}   //  doPost

	/**
	 * 	Update Response
	 *	@param request request
	 *	@param rfqResponse response
	 *	@return msg
	 */
	private String updateResponse (HttpServletRequest request, MRfQResponse rfqResponse)
	{
		log.fine("updateResponse - " + rfqResponse);
		String saveError = "RfQ NOT updated";
		String msg = "RfQ updated";
		//	RfQ Response
		rfqResponse.setName(WebUtil.getParameter (request, "Name"));
		rfqResponse.setDescription(WebUtil.getParameter (request, "Description"));
		rfqResponse.setHelp(WebUtil.getParameter (request, "Help"));
		rfqResponse.setDateWorkStart(WebUtil.getParameterAsDate(request, "DateWorkStart"));
		rfqResponse.setDateWorkComplete(WebUtil.getParameterAsDate(request, "DateWorkComplete"));
		rfqResponse.setDeliveryDays(WebUtil.getParameterAsInt(request, "DeliveryDays"));
		rfqResponse.setPrice(WebUtil.getParameterAsBD(request, "Price"));
		rfqResponse.setIsSelfService(true);
		rfqResponse.setDateResponse(new Timestamp(System.currentTimeMillis()));
		//	Check for Completeness
		if (WebUtil.getParameterAsBoolean(request, "IsComplete"))
		{
			String msgComplete = rfqResponse.checkComplete();
			if (msgComplete != null && msgComplete.length() > 0)
				msg = msgComplete;
		}
		if (!rfqResponse.save())
			return saveError;
		
		//	RfQ Response Line
		MRfQResponseLine[] lines = rfqResponse.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MRfQResponseLine line = lines[i];
			if (!line.isActive())
				continue;
			String paraAdd = "_" + line.getC_RfQResponseLine_ID();
			line.setDescription(WebUtil.getParameter (request, "Description" + paraAdd));
			line.setHelp(WebUtil.getParameter (request, "Help" + paraAdd));
			line.setDateWorkStart(WebUtil.getParameterAsDate(request, "DateWorkStart" + paraAdd));
			line.setDateWorkComplete(WebUtil.getParameterAsDate(request, "DateWorkComplete" + paraAdd));
			line.setDeliveryDays(WebUtil.getParameterAsInt(request, "DeliveryDays" + paraAdd));
			line.setIsSelfService(true);
			if (!line.save())
				return saveError;
			
			//	RfQ Response Line Qty
			MRfQResponseLineQty[] qtys = line.getQtys(true);
			for (int j = 0; j < qtys.length; j++)
			{
				MRfQResponseLineQty qty = qtys[j];
				if (!qty.isActive())
					continue;
				paraAdd = "_" + qty.getC_RfQResponseLineQty_ID();
				qty.setDiscount(WebUtil.getParameterAsBD(request, "Discount" + paraAdd));
				qty.setPrice(WebUtil.getParameterAsBD(request, "Price" + paraAdd));
				if (!qty.save())
					return saveError;
			}
		}
		log.fine("complete - " + rfqResponse);
		return msg;
	}	//	updateResponse

	
}   //  NoteServlet
