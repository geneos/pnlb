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
 *  CheckOut Links.
 * 	Creates Basket / Checkout Link
 *  <pre>
 *  <cws:checkOutLink/>
 *	</pre>
 *
 *  @author Jorg Janke
 *  @version $Id: CheckOutLinkTag.java,v 1.12 2005/03/11 20:34:46 jjanke Exp $
 */
public class CheckOutLinkTag extends TagSupport
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
		HttpSession session = pageContext.getSession();
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		WebBasket wb = (WebBasket)session.getAttribute(WebBasket.NAME);

	//	log.fine("doStartTag - WebBasket=" + wb);
		if (wb != null && wb.getLineCount() > 0)
		{
			log.fine("doStartTag - WebBasket exists");
			//
			JspWriter out = pageContext.getOut();
			HtmlCode html = new HtmlCode();
			//
			if (!m_oneLine)
				html.addElement(new hr("90%", "left"));
			//
			img img = new img ("basket.gif");
			img.setBorder(0);
			a a = new a("basket.jsp");
			a.setClass("menuMain");
			if (m_oneLine)
			{
				a.addElement (img);
				a.addElement ("Basket");
				html.addElement(a);
				html.addElement("&nbsp;- ");
			}
			else
			{
				a.addElement ("Basket");
				a.addElement (img);
				html.addElement(a);
				//	List Content
				p p = new p();
				p.setClass("Cbasket");
				ArrayList lines = wb.getLines();
				for (int i = 0; i < lines.size(); i++)
				{
					p.addElement("<br>");
					Object line = lines.get(i);
					p.addElement(line.toString());
				}
				p.addElement("<br><br>");
				html.addElement(p);
			//	html.addElement(new br());
			}
			//
			img = new img ("checkout.gif");
			img.setBorder(0);
			String url = CheckOutServlet.NAME;
			if (!request.isSecure())
				url = "https://" + request.getServerName() + request.getContextPath() + "/" + CheckOutServlet.NAME;
			a = new a(url);
			a.setClass("menuMain");
			a.addElement("Create Order");
			a.addElement(img);
			html.addElement(a);
			//
			html.output(out);
		}
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

}	//	CheckOutLinkTag
