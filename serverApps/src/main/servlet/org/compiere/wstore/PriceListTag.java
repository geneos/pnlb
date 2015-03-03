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
import javax.servlet.jsp.jstl.core.*;
import javax.servlet.jsp.tagext.*;
import org.compiere.util.*;

/**
 * 	PriceList Tag.
 * 	Loads Price List
 *  <pre>
 *  <cws:priceList priceList_ID="0"/>
 *  Variable used = "priceList"
 *	</pre>
 *
 *  @author Jorg Janke
 *  @version $Id: PriceListTag.java,v 1.11 2005/03/11 20:34:46 jjanke Exp $
 */
public class PriceListTag extends TagSupport
{
	/**	Price List ID			*/
	private int					m_priceList_ID = 0;

	/**	Web User				*/
	private PriceList			m_priceList;
	/**	Logger							*/
	private CLogger				log = CLogger.getCLogger (getClass());

	/**
	 * 	Set Price List
	 * 	@param var price list
	 */
	public void setPriceList_ID (String var)
	{
		try
		{
			m_priceList_ID = Integer.parseInt (var);
		}
		catch (NumberFormatException ex)
		{
			log.warning("setPriceList_ID - " + ex.toString());
		}
	}	//	setM_PriceList_ID


	/**
	 *  Start Tag
	 *  @return SKIP_BODY
	 * 	@throws JspException
	 */
	public int doStartTag() throws JspException
	{
		//	Create Price List
		Properties ctx = JSPEnv.getCtx((HttpServletRequest)pageContext.getRequest());
		int AD_Client_ID = Env.getContextAsInt(ctx, "AD_Client_ID");
		int M_PriceList_ID = m_priceList_ID;
		if (M_PriceList_ID == 0)
			M_PriceList_ID = Env.getContextAsInt(ctx, "M_PriceList_ID");

		//	Check Business Partner
		WebUser wu = (WebUser)pageContext.getSession().getAttribute(WebUser.NAME);
		if (wu != null)
		{
			int PriceList_ID = wu.getM_PriceList_ID();
			if (PriceList_ID != 0)
			{
				log.fine("- using BP PriceList_ID=" + PriceList_ID);
				M_PriceList_ID = PriceList_ID;
			}
		}
		
		//	Get Parameters
		String searchString = ctx.getProperty(ProductServlet.P_SEARCHSTRING);
		String productCategory = ctx.getProperty(ProductServlet.P_M_PRODUCT_CATEGORY_ID);

		
		//	get price list
		m_priceList = PriceList.get (ctx, AD_Client_ID, M_PriceList_ID, 
			searchString, productCategory, false);
		if (M_PriceList_ID == 0)
			Env.setContext(ctx, "#M_PriceList_ID", m_priceList.getPriceList_ID());

		//	Set Price List
		HttpSession session = pageContext.getSession();
		session.setAttribute (PriceList.NAME, m_priceList);
		log.fine("PL=" + m_priceList);

		//	Set Locale from Price List
		String AD_Language = m_priceList.getAD_Language();
		if (AD_Language == null || AD_Language.length() == 0)
			AD_Language = "en_US";
		Config.set(session, Config.FMT_LOCALE, AD_Language);
		Config.set(session, Config.FMT_FALLBACK_LOCALE, "en_US");
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

}	//	PriceListTag
