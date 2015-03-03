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
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Web User Login.
 * 	<pre>
 * 	User posts Login
 * 	- OK = forward
 *  - Did not find user
 * 	- Invalid Password
 *	</pre>
 *  @author     Jorg Janke
 *  @version    $Id: LoginServlet.java,v 1.38 2005/08/19 01:30:10 jjanke Exp $
 */
public class LoginServlet extends HttpServlet
{
	/**	Logging						*/
	private CLogger				log = CLogger.getCLogger(getClass());
	/** Name						*/
	static public final String	NAME = "loginServlet";

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
			throw new ServletException("LoginServlet.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	public String getServletInfo()
	{
		return "Compiere Web Login Servlet";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	public void destroy()
	{
		log.fine("destroy");
	}   //  destroy

	/** Forward Parameter					*/
	public static final String		P_ForwardTo = "ForwardTo";
	/** SalesRep Parameter					*/
	public static final String		P_SalesRep_ID = "SalesRep_ID";
	/** Login Page							*/
	public static final String		LOGIN_JSP = "/login.jsp";

	/**
	 *  Process the HTTP Get request.
	 * 	(logout, deleteCookie)
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
		log.info("From " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		HttpSession session = request.getSession(true);
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
		//
	//	WEnv.dump(request);

		//	save forward parameter
		String forward = WebUtil.getParameter (request, P_ForwardTo);			//	get forward from request
		if (forward != null)
			session.setAttribute(P_ForwardTo, forward);
		String salesRep = WebUtil.getParameter (request, P_SalesRep_ID);		//	get SalesRep from request
		if (salesRep != null)
			session.setAttribute(P_SalesRep_ID, salesRep);
		//
		String url = LOGIN_JSP;
		//	Mode
		String mode = WebUtil.getParameter (request, "mode");
		boolean deleteCookie = "deleteCookie".equals(mode);
		if (deleteCookie)
		{
			log.fine("** deleteCookie");
			JSPEnv.deleteCookieWebUser (request, response);
		}
		boolean logout = "logout".equals(mode);
		if (logout || deleteCookie)
		{
			log.fine("** logout");
			if (session != null)
			{
				Properties ctx = JSPEnv.getCtx(request);
				MSession cSession = MSession.get (ctx, false);
				if (cSession != null)
					cSession.logout();
				//
				WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
				if (wu != null)
					wu.logout();
				session.setMaxInactiveInterval(1);
				session.invalidate ();
			}
			//	Forward to unsecure /
			WebUtil.createForwardPage(response, "Logout", "http://" + request.getServerName() + "/", 2);
			return;
		}

		if (!url.startsWith("/"))
			url = "/" + url;
		log.info ("Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
		return;
	}	//	doGet

	/**
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
		Properties ctx = JSPEnv.getCtx(request);
		HttpSession session = request.getSession(true);
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
	//	WEnv.dump(session);
	//	WEnv.dump(request);
		
		//	Forward URL
		String url = WebUtil.getParameter (request, P_ForwardTo);			//	get forward from request
		String salesRep = WebUtil.getParameter (request, P_SalesRep_ID);	//	get SalesRep from request
		if (salesRep != null)
			session.setAttribute(P_SalesRep_ID, salesRep);
		boolean checkOut = "Y".equals(session.getAttribute(CheckOutServlet.ATTR_CHECKOUT));
		//	Set in login.jsp & addressInfo.jsp
		boolean addressConfirm = "Y".equals(WebUtil.getParameter (request, "AddressConfirm"));
		if (checkOut)
		{
			if (addressConfirm)
				url = "/orderServlet";
			else
				url = "/addressInfo.jsp";
		}
		else
			addressConfirm = false;
		if (url == null || url.length() == 0)
		{
			url = (String)session.getAttribute(P_ForwardTo);	//	get from session
			if (url == null || url.length() == 0)
				url = "/index.jsp";
		}
		else
		{
			if (!url.startsWith("/"))
				url = "/" + url;
			session.setAttribute(P_ForwardTo, url);				//	save for log in issues
		}
		
		//	SalesRep Parameter
		salesRep = (String)session.getAttribute(P_SalesRep_ID);	//	get SalesRep from session
		if (salesRep != null)
			url += "?SalesRep_ID=" + salesRep;
		//
		String mode = WebUtil.getParameter (request, "Mode");
		log.fine("- targeting url=" + url + " - mode=" + mode);

		//	Web User
		WebUser wu = WebUser.get(request);

		//	Get Base Info
		String email = WebUtil.getParameter (request, "EMail");
		if (email == null)
			email = "";
		email = email.trim();
		String password = WebUtil.getParameter (request, "Password");
		if (password == null)
			password = "";	//	null loads w/o check
		password = password.trim();

		//	Send EMail				***	Send Password EMail Request
		if ("SendEMail".equals(mode))
		{
			log.info("** send mail");
			wu = WebUser.get (ctx, email);			//	find it
			if (!wu.isEMailValid())
				wu.setPasswordMessage("EMail not found in system");
			else
			{
				wu.setPassword();		//	set password to current
				//
				String msg = JSPEnv.sendEMail (request, wu,
					MMailMsg.MAILMSGTYPE_UserPassword, new Object[]{
						request.getServerName(),
						wu.getName(),
						JSPEnv.getFrom(request),
						wu.getPassword()});
				if (EMail.SENT_OK.equals(msg))
					wu.setPasswordMessage ("EMail sent");
				else
					wu.setPasswordMessage ("Problem sending EMail: " + msg);
			}
			url = LOGIN_JSP;
		}	//	SendEMail

		//	Login
		else if ("Login".equals(mode))
		{
			log.info("** login " + email + "/" + password);
			//	add Cookie
			JSPEnv.addCookieWebUser(request, response, email);

			//	Always re-query
			wu = WebUser.get (ctx, email, password, false);
			wu.login(password);
			//	Password valid
			if (wu.isLoggedIn())
			{
				if (url.equals(LOGIN_JSP))
					url = "/index.jsp";
				//	Create Session with User ID
				MSession cSession = MSession.get (ctx, request.getRemoteAddr(), 
					request.getRemoteHost(), session.getId());
				if (cSession != null)
					cSession.setWebStoreSession(true);
			}
			else
			{
				url = LOGIN_JSP;
				log.fine("- PasswordMessage=" + wu.getPasswordMessage());
			}
			session.setAttribute (Info.NAME, new Info (ctx, wu));
		}	//	Login

		//	Login New
		else if ("LoginNew".equals(mode))
		{
			log.info("** loginNew");
			JSPEnv.addCookieWebUser(request, response, "");
			wu =  WebUser.get (ctx, "");
			url = LOGIN_JSP;
		}

		//	Submit - update/new Contact
		else if ("Submit".equals(mode))
		{
			log.info("** submit " + email + "/" + password + " - AddrConf=" + addressConfirm);
			//	we have a record for address update
			if (wu != null && wu.isLoggedIn() && addressConfirm)	//	address update
				;
			else	//	Submit - always re-load user record
				wu = WebUser.get (ctx, email, null, false); //	load w/o password check direct
			//
			if (wu.getAD_User_ID() != 0)		//	existing BPC
			{
				String passwordNew = WebUtil.getParameter (request, "PasswordNew");
				if (passwordNew == null)
					passwordNew = "";
				boolean passwordChange = passwordNew.length() > 0 && !passwordNew.equals(password);
				if (addressConfirm || wu.login (password))
				{
					//	Create / set session
					if (wu.isLoggedIn())
					{
						MSession cSession = MSession.get (ctx, request.getRemoteAddr(), 
							request.getRemoteHost(), session.getId());
						if (cSession != null)
							cSession.setWebStoreSession(true);
					}
					//
					if (passwordChange)
						log.fine("- update Pwd " + email + ", Old=" + password + ", DB=" + wu.getPassword() + ", New=" + passwordNew);
					if (updateFields(request, wu, passwordChange))
					{
						if (passwordChange)
							session.setAttribute(WebSessionCtx.HDR_MESSAGE, "Password changed");
						session.setAttribute (Info.NAME, new Info (ctx, wu));
					}
					else
					{
						url = LOGIN_JSP;
						log.warning(" - update not done");
					}
				}
				else
				{
					url = LOGIN_JSP;
					session.setAttribute(WebSessionCtx.HDR_MESSAGE, "Email/Password not correct");
					log.warning(" - update not confirmed");
				}
			}
			else	//	new
			{
				log.fine("** new " + email + "/" + password);
				wu.setEmail (email);
				wu.setPassword (password);
				if (updateFields (request, wu, true))
				{
					if (wu.login(password))
					{
						session.setAttribute (Info.NAME, new Info (ctx, wu));
						//	Create / set session
						MSession cSession = MSession.get (ctx, request.getRemoteAddr(), 
							request.getRemoteHost(), session.getId());
						if (cSession != null)
							cSession.setWebStoreSession(true);
					}
					else
						url = LOGIN_JSP;
				}
				else
				{
					log.fine("- failed - " + wu.getSaveErrorMessage() + " - " + wu.getPasswordMessage());
					url = LOGIN_JSP;
				}
			}	//	new

		}	//	Submit
		else
			log.log(Level.WARNING, "Unknown request='" + mode + "'");
		session.setAttribute (WebUser.NAME, wu);

		if (!url.startsWith("/"))
			url = "/" + url;
		log.info("doPost - Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}	//	doPost


	/**
	 * 	Update Web User
	 * 	@param request request
	 * 	@param wu user
	 * 	@param updateEMailPwd if true, change email/password
	 * 	@return true if saved
	 */
	private boolean updateFields (HttpServletRequest request, WebUser wu, boolean updateEMailPwd)
	{
		if (updateEMailPwd)
		{
			String s = WebUtil.getParameter (request, "PasswordNew");
			wu.setPasswordMessage (null);
			wu.setPassword (s);
			if (wu.getPasswordMessage () != null)
				return false;
			//
			s = WebUtil.getParameter (request, "EMail");
			if (!WebUtil.isEmailValid (s))
			{
				wu.setPasswordMessage ("EMail Invalid");
				return false;
			}
			wu.setEmail (s.trim());
		}
		//
		StringBuffer mandatory = new StringBuffer();
		String s = WebUtil.getParameter (request, "Name");
		if (s != null && s.length() != 0)
			wu.setName(s.trim());
		else
			mandatory.append(" - Name");
		s = WebUtil.getParameter (request, "Company");
		if (s != null && s.length() != 0)
			wu.setCompany(s);
		s = WebUtil.getParameter (request, "Title");
		if (s != null && s.length() != 0)
			wu.setTitle(s);
		//
		s = WebUtil.getParameter (request, "Address");
		if (s != null && s.length() != 0)
			wu.setAddress(s);
		else
			mandatory.append(" - Address");
		s = WebUtil.getParameter (request, "Address2");
		if (s != null && s.length() != 0)
			wu.setAddress2(s);
		//
		s = WebUtil.getParameter (request, "City");
		if (s != null && s.length() != 0)
			wu.setCity(s);
		else
			mandatory.append(" - City");
		s = WebUtil.getParameter (request, "Postal");
		if (s != null && s.length() != 0)
			wu.setPostal(s);
		else
			mandatory.append(" - Postal");
		//	Set Country before Region for validation
		s = WebUtil.getParameter (request, "C_Country_ID");
		if (s != null && s.length() != 0)
			wu.setC_Country_ID(s);
		s = WebUtil.getParameter (request, "C_Region_ID");
		if (s != null && s.length() != 0)
			wu.setC_Region_ID(s);
		s = WebUtil.getParameter (request, "RegionName");
		if (s != null && s.length() != 0)
			wu.setRegionName(s);
		//
		s = WebUtil.getParameter (request, "Phone");
		if (s != null && s.length() != 0)
			wu.setPhone(s);
		s = WebUtil.getParameter (request, "Phone2");
		if (s != null && s.length() != 0)
			wu.setPhone2(s);
		s = WebUtil.getParameter (request, "Fax");
		if (s != null && s.length() != 0)
			wu.setFax(s);
		//
		if (mandatory.length() > 0)
		{
			mandatory.insert(0, "Enter Mandatory");
			wu.setSaveErrorMessage(mandatory.toString());
			return false;
		}
		return wu.save();
	}	//	updateFields

}	//	LoginServlet
