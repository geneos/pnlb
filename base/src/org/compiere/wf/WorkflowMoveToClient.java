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
import org.compiere.util.*;
import org.compiere.process.*;

/**
 *	Move Workflow Customizations to Client
 *	
 *  @author Jorg Janke
 *  @version $Id: WorkflowMoveToClient.java,v 1.4 2005/03/11 20:25:56 jjanke Exp $
 */
public class WorkflowMoveToClient extends SvrProcess
{
	/**	The new Client			*/
	private int		p_AD_Client_ID = 0;
	/** The Workflow			*/
	private int		p_AD_Workflow_ID = 0;	

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Workflow_ID"))
				p_AD_Workflow_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("doIt - AD_Client_ID=" + p_AD_Client_ID + ", AD_Workflow_ID=" + p_AD_Workflow_ID);
		
		int changes = 0;
		//	WF
		String sql = "UPDATE AD_Workflow SET AD_Client_ID=" + p_AD_Client_ID
			+ " WHERE AD_Client_ID=0 AND EntityType NOT IN ('D','C')"
			+ " AND AD_Workflow_ID=" + p_AD_Workflow_ID;
		int no = DB.executeUpdate(sql, get_TrxName());
		if (no == -1)
			throw new CompiereSystemError ("Error updating Workflow");
		changes += no;
		
		//	Node
		sql = "UPDATE AD_WF_Node SET AD_Client_ID=" + p_AD_Client_ID
			+ " WHERE AD_Client_ID=0 AND EntityType NOT IN ('D','C')"
			+ " AND AD_Workflow_ID=" + p_AD_Workflow_ID;
		no = DB.executeUpdate(sql, get_TrxName());
		if (no == -1)
			throw new CompiereSystemError ("Error updating Workflow Node");
		changes += no;

		//	Node Next
		sql = "UPDATE AD_WF_NodeNext SET AD_Client_ID=" + p_AD_Client_ID
			+ " WHERE AD_Client_ID=0 AND EntityType NOT IN ('D','C')"
			+ " AND (AD_WF_Node_ID IN (SELECT AD_WF_Node_ID FROM AD_WF_Node WHERE AD_Workflow_ID=" + p_AD_Workflow_ID
				+ ") OR AD_WF_Next_ID IN (SELECT AD_WF_Node_ID FROM AD_WF_Node WHERE AD_Workflow_ID=" + p_AD_Workflow_ID 
				+ "))";
		no = DB.executeUpdate(sql, get_TrxName());
		if (no == -1)
			throw new CompiereSystemError ("Error updating Workflow Transition");
		changes += no;

		//	Node Parameters
		sql = "UPDATE AD_WF_Node_Para SET AD_Client_ID=" + p_AD_Client_ID
			+ " WHERE AD_Client_ID=0 AND EntityType NOT IN ('D','C')"
			+ " AND AD_WF_Node_ID IN (SELECT AD_WF_Node_ID FROM AD_WF_Node WHERE AD_Workflow_ID=" + p_AD_Workflow_ID 
			+ ")";
		no = DB.executeUpdate(sql, get_TrxName());
		if (no == -1)
			throw new CompiereSystemError ("Error updating Workflow Node Parameters");
		changes += no;

		//	Node Next Condition
		sql = "UPDATE AD_WF_NextCondition SET AD_Client_ID=" + p_AD_Client_ID
			+ " WHERE AD_Client_ID=0 AND EntityType NOT IN ('D','C')"
			+ " AND AD_WF_NodeNext_ID IN ("
				+ "SELECT AD_WF_NodeNext_ID FROM AD_WF_NodeNext "
				+ "WHERE AD_WF_Node_ID IN (SELECT AD_WF_Node_ID FROM AD_WF_Node WHERE AD_Workflow_ID=" + p_AD_Workflow_ID
				+ ") OR AD_WF_Next_ID IN (SELECT AD_WF_Node_ID FROM AD_WF_Node WHERE AD_Workflow_ID=" + p_AD_Workflow_ID
				+ "))";
		no = DB.executeUpdate(sql, get_TrxName());
		if (no == -1)
			throw new CompiereSystemError ("Error updating Workflow Transition Condition");
		changes += no;
		
		return "@Updated@ - #" + changes;
	}	//	doIt

}	//	WorkflowMoveToClient
