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

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.apache.ecs.xhtml.*;
import org.apache.taglibs.standard.tag.el.core.*;
import org.compiere.util.*;

/**
 * 	Request Order Reference Tag
 *  <pre>
 *	<cws:requestOrder bpartnerID="${webUser.bpartnerID}" />
 *	</pre>
 *	
 *  @author Jorg Janke
 *  @version $Id: RequestOrderRefTag.java,v 1.2 2005/11/28 03:34:21 jjanke Exp $
 */
public class RequestOrderRefTag extends TagSupport
{
	/**	Logger							*/
	private static CLogger			log = CLogger.getCLogger (RequestOrderRefTag.class);
	
	/** Business Partner Parameter		*/
	private String m_bpartnerID_el	= null;
	
	/**
	 * 	Set B.Partner parameter
	 *	@param bpartnerID_el region info
	 */
	public void setBpartnerID (String bpartnerID_el)
	{
		m_bpartnerID_el = bpartnerID_el;
	}	//	setBPartner

	/**
	 *  Start Tag
	 *  @return SKIP_BODY
	 */
	public int doStartTag()
	{
		//	Parameter
		int C_BPartner_ID = 0;
		try
		{
			String info = (String)ExpressionUtil.evalNotNull ("requestOrder", "bpartnerID",
				m_bpartnerID_el, String.class, this, pageContext);
			if (info != null && info.length () != 0)
				C_BPartner_ID = Integer.parseInt (info);
		}
		catch (Exception e)
		{
			log.severe ("BPartner - " + e);
		}
		
		JspWriter out = pageContext.getOut();
		select select = getRefOrders(C_BPartner_ID);
		select.output(out);
		//
		return (SKIP_BODY);
	}   //  doStartTag

	/**
	 * 	Create Select List
	 * 	@param C_BPartner_ID b partner
	 *	@return select list
	 */
	private select getRefOrders(int C_BPartner_ID)
	{
		select select = new select(RequestServlet.P_REF_ORDER_ID, getOrders(C_BPartner_ID));
		select.setID("ID_" + RequestServlet.P_REF_ORDER_ID);
		return select;
	}	//	getRequestType

	/**
	 * 	Get the Request Type options
	 * 	@param C_BPartner_ID b partner
	 * 	@return array of options
	 */
	private option[] getOrders(int C_BPartner_ID)
	{
		Properties ctx = JSPEnv.getCtx((HttpServletRequest)pageContext.getRequest());
		ArrayList<option> list = new ArrayList<option>();
		//	Optional Element
		option o = new option ("0").addElement(" ");
		o.setSelected(true);
		list.add(o);
		//
		String sql = "SELECT C_Order_ID, DocumentNo, DateOrdered, GrandTotal "
			+ "FROM C_Order "
			+ "WHERE C_BPartner_ID=? "
			+ "ORDER BY CreatedBy DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				o = new option (rs.getString(1));
				String display = rs.getString(2)
					+ "_" + rs.getTimestamp(3)
					+ "_" + rs.getBigDecimal(4);
				o.addElement(Util.maskHTML(display));
				list.add(o);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}

		//	Return to Array and return
		option options[] = new option [list.size()];
		list.toArray(options);
		log.fine("#" + options.length);
		return options;
	}	//	getOptions

}	//	RequestOrderReference
