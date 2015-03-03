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

import java.sql.*;
import java.util.*;

import org.compiere.model.*;


/**
 *	Processor Log
 *	
 *  @author Jorg Janke
 *  @version $Id: MWorkflowProcessorLog.java,v 1.4 2005/03/11 20:25:56 jjanke Exp $
 */
public class MWorkflowProcessorLog extends X_AD_WorkflowProcessorLog
	implements CompiereProcessorLog
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_WorkflowProcessorLog_ID id
	 */
	public MWorkflowProcessorLog (Properties ctx, int AD_WorkflowProcessorLog_ID, String trxName)
	{
		super (ctx, AD_WorkflowProcessorLog_ID, trxName);
		if (AD_WorkflowProcessorLog_ID == 0)
		{
			setIsError (false);
		}	
	}	//	MWorkflowProcessorLog

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MWorkflowProcessorLog (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MWorkflowProcessorLog

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param Summary Summary
	 */
	public MWorkflowProcessorLog (MWorkflowProcessor parent, String Summary)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setAD_WorkflowProcessor_ID(parent.getAD_WorkflowProcessor_ID());
		setSummary(Summary);
	}	//	MWorkflowProcessorLog
	
	
}	//	MWorkflowProcessorLog
