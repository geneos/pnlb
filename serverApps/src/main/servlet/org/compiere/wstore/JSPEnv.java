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
import org.compiere.model.*;
import org.compiere.util.*;
 
/**
 *  JSP Environment Utilities
 *
 *  @author Jorg Janke
 *  @version $Id: JSPEnv.java,v 1.32 2005/07/18 03:56:02 jjanke Exp $
 */
public class JSPEnv
{
	/**
	 * 	Get Context from Session
	 *	@param request request
	 * 	@return properties
	 */
	public static Properties getCtx (HttpServletRequest request)
	{
		WebSessionCtx wsc = WebSessionCtx.get(request);
		HttpSession session = request.getSession(true);

		//	Add/set current user
		WebUser wu = WebUser.get(request);
		if (wu != null)
		{
			int AD_User_ID = wu.getAD_User_ID();
			Env.setContext(wsc.ctx, "#AD_User_ID", AD_User_ID);		//	security
		}

		//	Finish
		session.setMaxInactiveInterval(1800);	//	30 Min	HARDCODED
		String info = (String)wsc.ctx.get(WebSessionCtx.HDR_INFO);
		if (info != null)
			session.setAttribute(WebSessionCtx.HDR_INFO, info);
		return wsc.ctx;
	}	//	getCtx

	/*************************************************************************/

	private final static String		COOKIE_NAME = "CompiereWebUser";

	/**
	 * 	Get Web User from Cookie
	 * 	@param request request with cookie
	 * 	@return web user or null
	 */
	public static String getCookieWebUser (HttpServletRequest request)
	{
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		for (int i = 0; i < cookies.length; i++)
		{
			if (COOKIE_NAME.equals(cookies[i].getName()))
				return cookies[i].getValue();
		}
		return null;
	}	//	getCookieWebUser

	/**
	 * 	Add Cookie with web user
	 * 	@param request request (for context path)
	 * 	@param response response to add cookie
	 * 	@param webUser email address
	 */
	public static void addCookieWebUser (HttpServletRequest request, HttpServletResponse response, String webUser)
	{
		Cookie cookie = new Cookie(COOKIE_NAME, webUser);
		cookie.setComment("Compiere Web User");
		cookie.setPath(request.getContextPath());
		cookie.setMaxAge(2592000);      //  30 days in seconds   60*60*24*30
		response.addCookie(cookie);
	}	//	setCookieWebUser

	/**
	 * 	Remove Cookie with web user by setting user to _
	 * 	@param request request (for context path)
	 * 	@param response response to add cookie
	 */
	public static void deleteCookieWebUser (HttpServletRequest request, HttpServletResponse response)
	{
		Cookie cookie = new Cookie(COOKIE_NAME, " ");
		cookie.setComment("Compiere Web User");
		cookie.setPath(request.getContextPath());
		cookie.setMaxAge(1);      //  second
		response.addCookie(cookie);
	}	//	deleteCookieWebUser

	/**
	 * 	Get Remote From info
	 * 	@param request request
	 * 	@return remore info
	 */
	public static String getFrom (HttpServletRequest request)
	{
		String host = request.getRemoteHost();
		if (!host.equals(request.getRemoteAddr()))
			host += " (" + request.getRemoteAddr() + ")";
		return host;
	}	//	getFrom
	
	/**************************************************************************
	 * 	Send EMail
	 *	@param request request
	 *	@param to web user
	 *	@param msgType see MMailMsg.MAILMSGTYPE_*
	 * 	@return mail EMail.SENT_OK or error message 
	 */
	public static String sendEMail (HttpServletRequest request, WebUser to,
		String msgType, Object[] parameter)
	{
		WebSessionCtx wsc = WebSessionCtx.get(request);
		MStore wStore = wsc.wstore;
		MMailMsg mailMsg = wStore.getMailMsg(msgType);
		//
		StringBuffer subject = new StringBuffer(mailMsg.getSubject());
		if (parameter.length > 0 && parameter[0] != null)
			subject.append(parameter[0]);
		//
		StringBuffer message = new StringBuffer();
		String hdr = wStore.getEMailFooter();
		if (hdr != null && hdr.length() > 0)
			message.append(hdr).append("\n");
		message.append(mailMsg.getMessage());
		if (parameter.length > 1 && parameter[1] != null)
			message.append(parameter[1]);
		if (mailMsg.getMessage2() != null)
		{
			message.append("\n")
				.append(mailMsg.getMessage2());
			if (parameter.length > 2 && parameter[2] != null)
				message.append(parameter[2]);
		}
		if (mailMsg.getMessage3() != null)
		{
			message.append("\n")
				.append(mailMsg.getMessage3());
			if (parameter.length > 3 && parameter[3] != null)
				message.append(parameter[3]);
		}
		message.append(MRequest.SEPARATOR)
			.append("http://").append(request.getServerName()).append(request.getContextPath())
			.append("/ - ").append(wStore.getName())
			.append("\n").append("Request from: ").append(getFrom(request))
			.append("\n");
		String ftr = wStore.getEMailFooter();
		if (ftr != null && ftr.length() > 0)
			message.append(ftr);
		
		//	Create Mail
		EMail email = wStore.createEMail(to.getEmail(), 
			subject.toString(), message.toString());
		//	CC Order
		if (msgType == MMailMsg.MAILMSGTYPE_OrderAcknowledgement)
		{
			String orderEMail = wStore.getWebOrderEMail();
			String storeEMail = wStore.getWStoreEMail();
			if (orderEMail != null && orderEMail.length() > 0
				&& !orderEMail.equals(storeEMail))	//	already Bcc
				email.addBcc(orderEMail);
		}

		//	Send
		String retValue = email.send();
		//	Log
		MUserMail um = new MUserMail(mailMsg, to.getAD_User_ID(), email);
		um.save();
		//
		return retValue;
	}	//	sendEMail

}	//	JSPEnv
