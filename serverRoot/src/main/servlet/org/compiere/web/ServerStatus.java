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

import javax.servlet.*;
import javax.servlet.http.*;


/**
 *	
 *	
 *  @author Jorg Janke
 *  @version $Id: ServerStatus.java,v 1.4 2005/10/17 23:44:29 jjanke Exp $
 */
public class ServerStatus extends HttpServlet
{
	/**
	 * 	doGet
	 *	@see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 *	@param arg0
	 *	@param arg1
	 *	@throws javax.servlet.ServletException
	 *	@throws java.io.IOException
	 */
	protected void doGet (HttpServletRequest arg0, HttpServletResponse arg1)
		throws ServletException, IOException
	{
		super.doGet (arg0, arg1);
	}

	/**
	 * 	doPost
	 *	@see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 *	@param arg0
	 *	@param arg1
	 *	@throws javax.servlet.ServletException
	 *	@throws java.io.IOException
	 */
	protected void doPost (HttpServletRequest arg0, HttpServletResponse arg1)
		throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		super.doPost (arg0, arg1);
	}

	/**
	 * 	getServletInfo
	 *	@see javax.servlet.Servlet#getServletInfo()
	 *	@return servlet info
	 */
	public String getServletInfo ()
	{
		return super.getServletInfo ();
	}

	/**
	 * 	init
	 *	@see javax.servlet.GenericServlet#init()
	 *	@throws javax.servlet.ServletException
	 */
	public void init ()
		throws ServletException
	{
		super.init ();
	}

	/**
	 * 	init
	 *	@see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 *	@param arg0
	 *	@throws javax.servlet.ServletException
	 */
	public void init (ServletConfig arg0)
		throws ServletException
	{
		// TODO Auto-generated method stub
		super.init (arg0);
	}

}	//	ServerStatus
