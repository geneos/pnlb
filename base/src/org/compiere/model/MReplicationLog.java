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

import java.util.*;

/**
 *  Replication Log Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReplicationLog.java,v 1.5 2005/03/11 20:26:03 jjanke Exp $
 */
public class MReplicationLog extends X_AD_Replication_Log
{

	/**
	 * 	Create new Log
	 * 	@param ctx context
	 * 	@param AD_Replication_Run_ID id
	 * 	@param AD_ReplicationTable_ID id
	 * 	@param P_Msg msg
	 */
	public MReplicationLog(Properties ctx, int AD_Replication_Run_ID, int AD_ReplicationTable_ID, String P_Msg, String trxName)
	{
		super(ctx, 0, trxName);
		setAD_Replication_Run_ID(AD_Replication_Run_ID);
		setAD_ReplicationTable_ID(AD_ReplicationTable_ID);
		setIsReplicated(false);
		setP_Msg(P_Msg);
	}	//	MReplicationLog

}	//	MReplicationLog
