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

import java.util.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.apache.ecs.xhtml.*;
import org.compiere.util.*;

/**
 *  Login Link.
 * 	Creates Login/Logout Link
 *  <pre>
 *  <cws:loginLink />
 *  Variable used - "webUser"
 *	</pre>
 *
 *  @author Jorg Janke
 *  @version $Id: LoginLinkTag.java,v 1.25 2005/07/18 03:56:02 jjanke Exp $
 */
public class LoginLinkTag extends TagSupport
{
	/**	Logger							*/
	protected static CLogger	log = CLogger.getCLogger (LoginLinkTag.class);

	/**
	 *  Start Tag
	 *  @return SKIP_BODY
	 * 	@throws JspException
	 */
	public int doStartTag() throws JspException
	{
		Properties ctx = JSPEnv.getCtx((HttpServletRequest)pageContext.getRequest());
		//
		WebUser wu = getWebUser(ctx);
		if (wu == null)
			pageContext.getSession().removeAttribute(WebUser.NAME);
		else
			pageContext.getSession().setAttribute (WebUser.NAME, wu);
		//
		String serverContext = ctx.getProperty(WebSessionCtx.CTX_SERVER_CONTEXT);
	//	log.fine("doStartTag - ServerContext=" + serverContext);
		HtmlCode html = null;
		if (wu != null && wu.isValid())
			html = getWelcomeLink (serverContext, wu);
		else
			html = getLoginLink (serverContext);
		//
		JspWriter out = pageContext.getOut();
		/**
		//	Delete Cookie Call
		if (cookieUser != null && !cookieUser.equals(" "))
		{
			log.fine("- Cookie=" + cookieUser);
			html.addElement(" ");
			a a = new a("loginServlet?mode=deleteCookie");
			a.setClass("menuDetail");
			a.addElement("(Delete&nbsp;Cookie)");
			html.addElement(a);
		}
		**/
		html.output(out);
		//
		//
		return (SKIP_BODY);
	}   //  doStartTag

	/**
	 * 	End Tag
	 * 	@return EVAL_PAGE
	 * 	@throws JspException
	 */
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}	//	doEndTag


	/**
	 *	Get WebUser.
	 * 	@param ctx context
	 * 	@return Web User or null
	 */
	private WebUser getWebUser (Properties ctx)
	{
		String address = pageContext.getRequest().getRemoteAddr();
		//	Get stored User
		WebUser wu = (WebUser)pageContext.getSession().getAttribute (WebUser.NAME);
		if (wu != null)
		{
			log.finest("(" + address + ") - SessionContext: " + wu);
		}
		else
		{
			wu = (WebUser)pageContext.getAttribute(WebUser.NAME);
			if (wu != null)
				log.finest ("(" + address + ") - Context: " + wu);
		}
		if (wu != null)
			return wu;

		//	Check Coockie
		String cookieUser = JSPEnv.getCookieWebUser ((HttpServletRequest)pageContext.getRequest());
		if (cookieUser == null || cookieUser.trim().length() == 0)
			log.finer ("(" + address + ") - no cookie");
		else
		{
			//	Try to Load
			wu = WebUser.get (ctx, cookieUser);
			log.finer ("(" + address + ") - Cookie: " + wu);
		}
		if (wu != null)
			return wu;
		//
		return null;
	}	//	getWebUser

	
	/**************************************************************************
	 * 	Get Login Link
	 * 	@param	serverContext server context
	 * 	@return link
	 */
	private HtmlCode getLoginLink(String serverContext)
	{
		HtmlCode retValue = new HtmlCode();
		//	Login button
		input button = new input(input.TYPE_BUTTON, "Login", "Login");
		button.setOnClick("window.top.location.replace('https://" + serverContext + "/loginServlet');");
		retValue.addElement(button);

		/**	Link
		a a = new a("https://" + serverContext + "/login.jsp");
		a.setClass("menuMain");
		a.addElement("Login");
		retValue.addElement(a);
		**/

		retValue.addElement(" ");
		return retValue;
	}	//	getLoginLink

	/**
	 * 	Get Welcome Link
	 * 	@param	serverContext server Context
	 * 	@param wu web user
	 * 	@return link
	 */
	private HtmlCode getWelcomeLink(String serverContext, WebUser wu)
	{
		HtmlCode retValue = new HtmlCode();
		//
		a a = new a("https://" + serverContext + "/login.jsp");
		a.setClass("menuMain");
		String msg = "Welcome " + wu.getName();
		a.addElement(msg);
		retValue.addElement(a);
		//
		retValue.addElement(" &nbsp; ");
		if (wu.isLoggedIn())
		{
			//	Verify
			if (!wu.isEMailVerified())
			{
				input button = new input(input.TYPE_BUTTON, "Verify", "Verify EMail");
				button.setOnClick("window.top.location.replace('emailVerify.jsp');");
				retValue.addElement(button);
				retValue.addElement(" ");
			}
			
			//	Update
			input button = new input(input.TYPE_BUTTON, "Update", "Update");
			button.setOnClick("window.top.location.replace('login.jsp');");
			retValue.addElement(button);
			retValue.addElement(" ");
			
			//	Logout
			button = new input(input.TYPE_BUTTON, "Logout", "Logout");
			button.setOnClick("window.top.location.replace('loginServlet?mode=logout');");
			retValue.addElement(button);

			/** Link
			a = new a ("loginServlet?mode=logout");
			a.setClass ("menuMain");
			a.addElement ("Logout");
			retValue.addElement (a);
			**/
		}
		else
		{
			input button = new input (input.TYPE_BUTTON, "Login", "Login");
			button.setOnClick ("window.top.location.replace('https://" + serverContext + "/login.jsp');");
			retValue.addElement (button);
		}
		retValue.addElement (" ");
		//
		return retValue;
	}	//	getWelcomeLink

}	//	LoginLinkTag
