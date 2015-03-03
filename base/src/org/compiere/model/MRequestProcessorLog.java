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
 *	Request Processor Log
 *	
 *  @author Jorg Janke
 *  @version $Id: MRequestProcessorLog.java,v 1.4 2005/03/11 20:26:04 jjanke Exp $
 */
public class MRequestProcessorLog extends X_R_RequestProcessorLog
	implements CompiereProcessorLog
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_RequestProcessorLog_ID id
	 */
	public MRequestProcessorLog (Properties ctx, int R_RequestProcessorLog_ID, String trxName)
	{
		super (ctx, R_RequestProcessorLog_ID, trxName);
		if (R_RequestProcessorLog_ID == 0)
		{
			setIsError (false);
		}	
	}	//	MRequestProcessorLog

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRequestProcessorLog (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRequestProcessorLog

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param summary summary
	 */
	public MRequestProcessorLog (MRequestProcessor parent, String summary)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setR_RequestProcessor_ID(parent.getR_RequestProcessor_ID());
		setSummary(summary);
	}	//	MRequestProcessorLog

	
}	//	MRequestProcessorLog
