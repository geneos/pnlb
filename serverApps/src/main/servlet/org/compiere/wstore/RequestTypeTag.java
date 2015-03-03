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
import org.compiere.util.*;

/**
 *  Request Type Tag.
 * 	Create Drop Down List with valid values
 *	<code>
 *	<cws:requestType/>
 *	</code>
 *  @author Jorg Janke
 *  @version $Id: RequestTypeTag.java,v 1.12 2005/11/28 03:34:21 jjanke Exp $
 */
public class RequestTypeTag extends TagSupport
{
	/**	Logger							*/
	private static CLogger			log = CLogger.getCLogger (RequestTypeTag.class);

	/**
	 *  Start Tag
	 *  @return SKIP_BODY
	 */
	public int doStartTag()
	{
		JspWriter out = pageContext.getOut();
		select select = getRequestType();
		select.output(out);
		//
		return (SKIP_BODY);
	}   //  doStartTag

	/**
	 * 	Create Select List
	 *	@return select list
	 */
	private select getRequestType()
	{
		select select = new select(RequestServlet.P_REQUESTTYPE_ID, getOptions());
		select.setID("ID_" + RequestServlet.P_REQUESTTYPE_ID);
		return select;
	}	//	getRequestType

	/**
	 * 	Get the Request Type options
	 * 	@return array of options
	 */
	private option[] getOptions()
	{
		Properties ctx = JSPEnv.getCtx((HttpServletRequest)pageContext.getRequest());
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		if (AD_Client_ID == 0)
			log.log(Level.SEVERE, "AD_Client_ID not found");
		else
			log.config("AD_Client_ID=" + AD_Client_ID);
		ArrayList<option> list = new ArrayList<option>();
		//
		String sql = "SELECT R_RequestType_ID, Name FROM R_RequestType "
			+ "WHERE AD_Client_ID=? AND IsActive='Y' AND IsSelfService='Y' "
			+ "ORDER BY IsDefault DESC, Name";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				option o = new option (rs.getString(1));
				o.addElement(Util.maskHTML(rs.getString(2)));
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

}	//	RequestTypeTag
