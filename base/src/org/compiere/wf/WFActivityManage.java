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
package org.compiere.wf;

import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.process.*;

/**
 *	Manage Workflow Activity
 *	
 *  @author Jorg Janke
 *  @version $Id: WFActivityManage.java,v 1.4 2005/03/11 20:25:56 jjanke Exp $
 */
public class WFActivityManage extends SvrProcess
{
	/**	Abort It				*/	
	private boolean		p_IsAbort = false;
	/** New User				*/
	private int			p_AD_User_ID = 0;
	/** New Responsible			*/
	private int			p_AD_WF_Responsible_ID = 0;
	/** Record					*/
	private int			p_AD_WF_Activity_ID = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("IsAbort"))
				p_IsAbort = "Y".equals(para[i].getParameter());
			else if (name.equals("AD_User_ID"))
				p_AD_User_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_WF_Responsible_ID"))
				p_AD_WF_Responsible_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_AD_WF_Activity_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (variables are parsed)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		MWFActivity activity = new MWFActivity (getCtx(), p_AD_WF_Activity_ID, get_TrxName());
		log.info("doIt - " + activity);
		
		MUser user = MUser.get(getCtx(), getAD_User_ID());
		//	Abort
		if (p_IsAbort)
		{
			String msg = user.getName() + ": Abort";
			activity.setTextMsg(msg);
			activity.setAD_User_ID(getAD_User_ID());
			activity.setWFState(StateEngine.STATE_Aborted);
			return msg;
		}
		String msg = null;
		//	Change User
		if (p_AD_User_ID != 0 && activity.getAD_User_ID() != p_AD_User_ID)
		{
			MUser from = MUser.get(getCtx(), activity.getAD_User_ID());
			MUser to = MUser.get(getCtx(), p_AD_User_ID);
			msg = user.getName() + ": " + from.getName() + " -> " + to.getName();
			activity.setTextMsg(msg);
			activity.setAD_User_ID(p_AD_User_ID);
		}
		//	Change Responsible
		if (p_AD_WF_Responsible_ID != 0 && activity.getAD_WF_Responsible_ID() != p_AD_WF_Responsible_ID)
		{
			MWFResponsible from = MWFResponsible.get(getCtx(), activity.getAD_WF_Responsible_ID());
			MWFResponsible to = MWFResponsible.get(getCtx(), p_AD_WF_Responsible_ID);
			String msg1 = user.getName() + ": " + from.getName() + " -> " + to.getName();
			activity.setTextMsg(msg1);
			activity.setAD_WF_Responsible_ID(p_AD_WF_Responsible_ID);
			if (msg == null)
				msg = msg1;
			else
				msg += " - " + msg1;
		}
		//
		activity.save();
		
		return msg;
	}	//	doIt
	
}	//	WFActivityManage
