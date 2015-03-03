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
import org.compiere.util.*;

/**
 *  Message/Translation Tag.
 * 	<cws:message txt="AD_Message"/>
 *
 *  @author Jorg Janke
 *  @version $Id: MessageTag.java,v 1.6 2005/03/11 20:34:46 jjanke Exp $
 */
public class MessageTag  extends TagSupport
{
	/**	Logger				*/
	private CLogger		log = CLogger.getCLogger (getClass());
	/** Text				*/
	private String		m_txt;

	/**
	 * 	Set text
	 * 	@param txt text to be translated
	 */
	public void setTxt (String txt)
	{
		m_txt = txt;
	}	//	setVar


	/**
	 *  Start Tag
	 *  @return SKIP_BODY
	 * 	@throws JspException
	 */
	public int doStartTag() throws JspException
	{
		if (m_txt != null && m_txt.length() > 0)
		{
			Properties ctx = JSPEnv.getCtx((HttpServletRequest)pageContext.getRequest());
			String msg = Msg.translate(ctx, m_txt);
			log.fine(m_txt + "->" + msg);
			//
			try
			{
				JspWriter out = pageContext.getOut();
				out.print (msg);
			}
			catch (Exception e)
			{
				throw new JspException(e);
			}
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

}	//	MessageTag
