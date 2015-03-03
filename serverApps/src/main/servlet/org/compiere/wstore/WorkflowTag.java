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
import org.apache.taglibs.standard.tag.el.core.*;
import org.compiere.util.*;
import org.compiere.wf.*;

/**
 *	Workfloa Tag.
 *	<pre>
 *	<cws:workflow activityID="${act.AD_WF_Activity_ID}" />
 *	</pre>
 *	Depending on activity creates respose items
 *	
 *  @author Jorg Janke
 *  @version $Id: WorkflowTag.java,v 1.6 2005/10/01 23:56:02 jjanke Exp $
 */
public class WorkflowTag extends TagSupport
{
	/**	Logging						*/
	private CLogger			log = CLogger.getCLogger(getClass());

	/**	Activity ID 				*/
	private String			m_activityID_el = null;

	/**	CSS Class Tag				*/
	private static final String C_MANDATORY = "Cmandatory";
	
	
	/**
	 * 	Set AD_WF_Activity_ID
	 *	@param info_el activity info
	 */
	public void setActivityID (String info_el)
	{
		m_activityID_el = info_el;
	}	//	setActivityID

	/**
	 *  Start Tag
	 *  @return SKIP_BODY
	 * 	@throws JspException
	 */
	public int doStartTag() throws JspException
	{
		Properties ctx = JSPEnv.getCtx((HttpServletRequest)pageContext.getRequest());

		//	Activity
		int AD_WF_Activity_ID = 0;
		String info = null;
		try
		{
			info = (String)ExpressionUtil.evalNotNull ("workflow", "activityID",
				m_activityID_el, String.class, this, pageContext);
			if (info != null && info.length () != 0)
				AD_WF_Activity_ID = Integer.parseInt (info);
		}
		catch (Exception e)
		{
			log.severe ("doStartTag - Activity" + e);
		}
		MWFActivity act = new MWFActivity (ctx, AD_WF_Activity_ID, null);
		if (AD_WF_Activity_ID == 0 || act == null || act.get_ID() != AD_WF_Activity_ID)
		{
			log.severe ("doStartTag - Activity Not found - " + m_activityID_el + " (" + info + ")");
			return (SKIP_BODY);
		}

		String name = null;
		if (act.isUserApproval())
			name = "IsApproved";
		else if (act.isUserManual())
			name = "IsConfirmed";
		else
			return (SKIP_BODY);

		//	YesNo
		option[] yesNoOptions = new option[3];
		yesNoOptions[0] = new option (" ");
		yesNoOptions[0].addElement(" ");
		yesNoOptions[0].setSelected (true);
		yesNoOptions[1] = new option ("Y");
		yesNoOptions[1].addElement(Util.maskHTML(Msg.translate(ctx, "Yes")));
		yesNoOptions[2] = new option ("N");
		yesNoOptions[2].addElement(Util.maskHTML(Msg.translate(ctx, "No")));
		select yesNoSelect = new select (name, yesNoOptions);
		yesNoSelect.setID("ID_" + name);
		yesNoSelect.setClass(C_MANDATORY);
		//
		String nameTrl = Msg.translate(ctx, name);

		//	Assemble
		HtmlCode html = new HtmlCode();
		html.addElement(new b(nameTrl));
		html.addElement(yesNoSelect);
		html.addElement(new br());

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
	
}	//	WorkflowTag
