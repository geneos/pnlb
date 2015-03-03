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
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.util.*;

/**
 *	Single Item Purchase
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: SingleItem.java,v 1.6 2005/07/18 03:56:02 jjanke Exp $
 */
public class SingleItem extends HttpServlet
{
	/**	Logging						*/
	private CLogger			log = CLogger.getCLogger(getClass());

	/**
	 * 	Initialize global variables
	 *  @param config servlet configuration
	 *  @throws ServletException
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		if (!WebEnv.initWeb(config))
			throw new ServletException("SingleItem");
	}	//	init

	/**
	 * Clean up resources
	 */
	public void destroy()
	{
		log.info("destroy");
	}   //  destroy

	//  Parameter Names
	private static final String P_ITEM_NAME =			"item_name";
	private static final String P_ITEM_NUMBER =			"item_number";
	private static final String P_AMOUNT =				"amount";
	private static final String P_QUANTITY =			"quantity";
	private static final String P_UNDEFINED_QUANTITY =	"undefined_quantity";
	private static final String P_RETURN_URL =			"return";
	private static final String P_CANCEL_URL =			"cancel_return";
	//
	private static final String P_SUBMIT =				"SUBMIT";

	/*************************************************************************/

	/**
	 *  Process the initial HTTP Get request
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("From " + request.getRemoteHost() + " - " + request.getRemoteAddr());
	}   //  doGet


	/**
	 *  Process the HTTP Post request
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("From " + request.getRemoteHost() + " - " + request.getRemoteAddr());
	}   //  doPost

}	//	SingleItem
