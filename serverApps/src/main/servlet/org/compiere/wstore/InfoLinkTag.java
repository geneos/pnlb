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
import org.compiere.model.*;

/**
 *  Info Links (Menu).
 * 	Creates Invoice/Payment/Asset/AddressInfo/PaymentInfo Link
 *  <pre>
 *  <cws:infoLink/>
 *	</pre>
 *
 *  @author Jorg Janke
 *  @version $Id: InfoLinkTag.java,v 1.22 2005/07/20 19:30:04 jjanke Exp $
 */
public class InfoLinkTag extends TagSupport
{
	/**	Logger							*/
	private CLogger			log = CLogger.getCLogger (getClass());
	/** One Line						*/
	private boolean			m_oneLine = false;

	/**
	 *	Set to one line
	 *	@param var Y or something else
	 */
	public void setOneLine (String var)
	{
		m_oneLine = "Y".equals(var);
	}	//	setOneLine

	/**
	 *  Start Tag
	 *  @return SKIP_BODY
	 * 	@throws JspException
	 */
	public int doStartTag() throws JspException
	{
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		Properties ctx = JSPEnv.getCtx(request);		//	creates wsc/wu
		WebSessionCtx wsc = WebSessionCtx.get(request);
		//
		HttpSession session = pageContext.getSession();
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		if (wu != null && wu.isLoggedIn())
		{
			if (ctx != null)
			{
				Info info = (Info)session.getAttribute(Info.NAME);
				if (info == null || wu.getAD_User_ID() != info.getAD_User_ID())
					session.setAttribute (Info.NAME, new Info (ctx, wu));
			}

			//
		//	log.fine("WebUser exists - " + wu);
			//
			JspWriter out = pageContext.getOut();
			HtmlCode html = new HtmlCode();
			//
			if (wu.isCustomer())
				menuCustomer(html, wsc.wstore);
			if (wu.isSalesRep())
				menuSalesRep(html, wsc.wstore);
			if (wu.isEmployee() || wu.isSalesRep())
				menuUser (html, wu.isEmployee(), wsc.wstore);
			menuAll (html, wsc.wstore);
			//
			html.output(out);
		}
		else
		{
			if (CLogMgt.isLevelFiner())
				log.fine("No WebUser");
			if (session.getAttribute(Info.NAME) == null)
				session.setAttribute (Info.NAME, Info.getGeneral());
		}
		return (SKIP_BODY);
	}   //  doStartTag

	/**
	 * 	Add Customer Links
	 *	@param html code
	 */
	private void menuCustomer (HtmlCode html, MStore wstore)
	{
		boolean first = true;
		if (wstore.isMenuAssets())
		{
			nl (html, first);		//		---	Assets
			first = false;
			a a = new a ("assets.jsp");
			a.setClass ("menuSub");
			a.addElement ("My Assets");
			html.addElement (a);
		}
		//
		if (wstore.isMenuInvoices())
		{
			nl (html, first);		//		---	Invoices
			first = false;
			a a = new a ("invoices.jsp");
			a.setClass ("menuSub");
			a.addElement ("My Invoices");
			html.addElement (a);
		}
		//
		if (wstore.isMenuPayments())
		{
			nl (html, first);		//		--- Payments
			first = false;
			a a = new a ("payments.jsp");
			a.setClass ("menuSub");
			a.addElement ("My Payments");
			html.addElement (a);
		}
		//
		if (wstore.isMenuOrders())
		{
			nl (html, first);		//		--- Orders
			first = false;
			a a = new a ("orders.jsp");
			a.setClass ("menuSub");
			a.addElement ("My Orders");
			html.addElement (a);
		}
		//
		if (wstore.isMenuShipments())
		{
			nl (html, first);		//		--- Shipments
			first = false;
			a a = new a ("shipments.jsp");
			a.setClass ("menuSub");
			a.addElement ("My Shipments");
			html.addElement (a);
		}
		//
		if (wstore.isMenuRfQs())
		{
			nl (html, first);		//		--- RfQs
			first = false;
			a a = new a ("rfqs.jsp");
			a.setClass ("menuSub");
			a.addElement ("My RfQs");
			html.addElement (a);
		}
	}	//	menuCustomer

	/**
	 * 	Add Links for all
	 *	@param html code
	 */
	private void menuAll (HtmlCode html, MStore wstore)
	{
		if (wstore.isMenuRequests())
		{
			nl (html, true);		//	Requests
			a a = new a ("requests.jsp");
			a.setClass ("menuMain");
			a.addElement ("My Requests");
			html.addElement (a);
		}
		//
		if (wstore.isMenuInterests())
		{
			nl (html, true);		//		--- Interest Area
			a a = new a ("info.jsp");
			a.setClass ("menuSub");
			a.addElement ("Interest Area");
			html.addElement (a);
		}
		
		if (wstore.isMenuRegistrations())
		{
			nl (html, false);		//		--- Registration
			a a = new a ("registrations.jsp");
			a.setClass ("menuSub");
			a.addElement ("Registration");
			html.addElement (a);
		}
	}	//	menuAll

	/**
	 * 	Add Links for Sales Reps
	 *	@param html code
	 */
	private void menuSalesRep (HtmlCode html, MStore wstore)
	{
		nl (html, true); 			//	------------
		//							--- Assigned Requests
		a a = new a ("requests_sr.jsp");
		a.setClass ("menuSub");
		a.addElement ("Open Requests");
		html.addElement (a);
		//
		nl (html, false);
		//							--- Advertisements
		a = new a ("advertisements.jsp");
		a.setClass ("menuSub");
		a.addElement ("Advertisements");
		html.addElement (a);
		//
		nl (html, false);
		//							--- Commissions
		a = new a ("commissionRuns.jsp");
		a.setClass ("menuSub");
		a.addElement ("Commissions");
		html.addElement (a);
		//							--- C.Invoices
		a = new a ("commissionedInvoices.jsp");
		a.setClass ("menuDetail");
		a.addElement ("C.Invoices");
		html.addElement (a);
		//
		nl (html, false);
	}	//	menuSalesRep

	/**
	 * 	Add Links for Users
	 *	@param html code
	 *	@param isEmployee employee
	 */
	private void menuUser (HtmlCode html, boolean isEmployee, MStore wstore)
	{
		nl (html, true); 			//	------------
		//							--- Notices
		if (isEmployee)
		{
			a a = new a ("notes.jsp");
			a.setClass ("menuMain");
			a.addElement ("Notices");
			html.addElement (a);
			//
			nl (html, false);
		}
		//							--- Expense
		a a = new a ("expenses.jsp");
		a.setClass ("menuSub");
		a.addElement ("Expenses");
		html.addElement (a);
	}	//	menuUser

	/**
	 * 	Add New Line / Break
	 * 	@param html code
	 * 	@param hr insert HR rather BR
	 */
	private void nl (HtmlCode html, boolean hr)
	{
		if (m_oneLine)
			html.addElement("&nbsp;- ");
		else if (hr)
			html.addElement(new hr("90%", "left"));
		else
			html.addElement(new br());
	}	//	nl

	/**
	 * 	End Tag
	 * 	@return EVAL_PAGE
	 * 	@throws JspException
	 */
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}	//	doEndTag

}	//	InfoLinkTag
