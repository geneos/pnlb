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

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import javax.naming.*;

import javax.sql.*;
import java.sql.*;

import org.compiere.interfaces.*;

/**
 *
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: StatusInfo.java,v 1.7 2005/03/11 20:34:58 jjanke Exp $
 */
public class StatusInfo extends HttpServlet
{
	static final private String CONTENT_TYPE = "text/html";
	//Initialize global variables
	public void init() throws ServletException
	{
		getServletContext().log("StatusInfo.init");
	}

	//Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>Status Info</title></head>");
		out.println("<body>");

		InitialContext context = null;
		try
		{
			context = new InitialContext();
		}
		catch (Exception ex)
		{
			out.println("<p><b>" + ex + "</b></p>");
		}

		try
		{
			StatusHome statusHome = (StatusHome)context.lookup (StatusHome.JNDI_NAME);
			Status status = statusHome.create();
			out.println("<p>" + status.getStatus() + "</p>");
			status.remove();
		}
		catch (Exception ex)
		{
			out.println("<p><b>" + ex + "</b></p>");
		}

		try
		{
			ServerHome serverHome = (ServerHome)context.lookup (ServerHome.JNDI_NAME);
			Server server = serverHome.create();
			out.println("<p>" + server.getStatus() + "</p>");
			server.remove();
		}
		catch (Exception ex)
		{
			out.println("<p><b>" + ex + "</b></p>");
		}

		try
		{
			out.println("<h2>-- /</h2>");
			NamingEnumeration ne = context.list("/");
			while (ne.hasMore())
				out.println("<br>   " + ne.nextElement());
			out.println("<h2>-- java</h2>");
			ne = context.list("java:");
			while (ne.hasMore())
				out.println("<br>   " + ne.nextElement());
			out.println("<h2>-- ejb</h2>");
			ne = context.list("ejb");
			while (ne.hasMore())
				out.println("<br>   " + ne.nextElement());

			//

			out.println("<h2>-- DS</h2>");
			DataSource ds = (DataSource)context.lookup("java:/OracleDS");
			out.println("<br>  DataSource " + ds.getClass().getName() + " LoginTimeout=" + ds.getLoginTimeout());

			Connection con = ds.getConnection("compiere","compiere");
			out.println("<br>  Connection ");

			getServletContext().log("Connection closed=" + con.isClosed());
			DatabaseMetaData dbmd = con.getMetaData();
			getServletContext().log("DB " + dbmd.getDatabaseProductName());
			getServletContext().log("DB V " + dbmd.getDatabaseProductVersion());
			getServletContext().log("Driver " + dbmd.getDriverName());
			getServletContext().log("Driver V " + dbmd.getDriverVersion());
			getServletContext().log("JDBC " + dbmd.getJDBCMajorVersion());
			getServletContext().log("JDBC mV " + dbmd.getJDBCMinorVersion());

			getServletContext().log("User " + dbmd.getUserName());

			getServletContext().log("ANSI 92 " + dbmd.supportsANSI92FullSQL());
			getServletContext().log("Connection Alter Table ADD" + dbmd.supportsAlterTableWithAddColumn());
			getServletContext().log("Connection Alter Table DROP " + dbmd.supportsAlterTableWithDropColumn());
			getServletContext().log("Connection DDL&DML " + dbmd.supportsDataDefinitionAndDataManipulationTransactions());
			getServletContext().log("Connection CatalogsIn DML " + dbmd.supportsCatalogsInDataManipulation());
			getServletContext().log("Connection Schema In DML " + dbmd.supportsSchemasInDataManipulation());



		}
		catch (Exception e)
		{
			out.println("<p><b>" + e + "</b></p>");
		}


		out.println("</body></html>");
	}
	//Process the HTTP Put request
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet (request, response);
	}
	//Clean up resources
	public void destroy()
	{
		getServletContext().log("StatusInfo.destroy");
	}
}
