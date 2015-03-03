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
package org.compiere.model;

import java.sql.*;
import java.util.*;


/**
 *	Scheduler Log
 *	
 *  @author Jorg Janke
 *  @version $Id: MSchedulerLog.java,v 1.5 2005/05/17 05:29:53 jjanke Exp $
 */
public class MSchedulerLog extends X_AD_SchedulerLog
	implements CompiereProcessorLog
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_SchedulerLog_ID id
	 */
	public MSchedulerLog (Properties ctx, int AD_SchedulerLog_ID, String trxName)
	{
		super (ctx, AD_SchedulerLog_ID, trxName);
	}	//	MSchedulerLog

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MSchedulerLog (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MSchedulerLog

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param summary summary
	 */
	public MSchedulerLog (MScheduler parent, String summary)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setAD_Scheduler_ID(parent.getAD_Scheduler_ID());
		setSummary(summary);
	}	//	MSchedulerLog

}	//	MSchedulerLog
