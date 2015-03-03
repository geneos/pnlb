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
 *	Product Category List
 *	<code>
 *	<cws:productCategoryList/>
 *	</code>
 *	
 *  @author Jorg Janke
 *  @version $Id: ProductCategoryListTag.java,v 1.7 2005/10/08 02:04:10 jjanke Exp $
 */
public class ProductCategoryListTag extends TagSupport
{
	/**	Logging						*/
	private static CLogger		log = CLogger.getCLogger(ProductCategoryListTag.class);
	
	
	/**
	 *  Start Tag
	 *  @return SKIP_BODY
	 * 	@throws JspException
	 */
	public int doStartTag() throws JspException
	{
		Properties ctx = JSPEnv.getCtx((HttpServletRequest)pageContext.getRequest());
		
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		String name = "M_Product_Category_ID";
		
		option[] options = getCategories (AD_Client_ID);
		select sel = new select (name, options);
		sel.setID("ID_" + name);

		log.fine("AD_Client_ID=" + AD_Client_ID + ", #=" + options.length);

		//	Assemble
		HtmlCode html = new HtmlCode();
		html.addElement(sel);
		
		JspWriter out = pageContext.getOut();
		html.output(out);
		//
		return (SKIP_BODY);
	}   //  doStartTag

	/**
	 * 	End Tag - NOP
	 * 	@return EVAL_PAGE
	 * 	@throws JspException
	 */
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}	//	doEndTag

	/**
	 * 	Get Product Category Options.
	 * 	@param AD_Client_ID client
	 *	@return array of category options
	 */
	private option[] getCategories (int AD_Client_ID)
	{
		option[] options = (option[])s_categories.get(new Integer(AD_Client_ID));
		if (options != null)
			return options;
		
		String sql = "SELECT M_Product_Category_ID, Name "
			+ "FROM M_Product_Category "
			+ "WHERE AD_Client_ID=" + AD_Client_ID
			+ " AND IsActive='Y' AND IsSelfService='Y' "
			+ "ORDER BY Name";
		KeyNamePair[] pairs = DB.getKeyNamePairs(sql, true);
		options = new option[pairs.length];
		//
		for (int i = 0; i < pairs.length; i++)
		{
			if (i == 0)
			{
				options[i] = new option ("-1");
				options[i].addElement(" ");
			}
			else
			{
				options[i] = new option (pairs[i].getID());
				options[i].addElement(Util.maskHTML(pairs[i].getName()));
			}
		}
		//
		s_categories.put(new Integer(AD_Client_ID), options);
		return options;
	}	//	getCountries

	/** Client Category Cache		*/
	static CCache<Integer,option[]> s_categories
		= new CCache<Integer,option[]>("ProductCategory", 10, 60);

}	//	ProductCategoryListTag

