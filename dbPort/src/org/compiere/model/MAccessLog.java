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
 *	Access Log Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAccessLog.java,v 1.6 2005/03/11 20:28:36 jjanke Exp $
 */
public class MAccessLog extends X_AD_AccessLog
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_AccessLog_ID id
	 */
	public MAccessLog (Properties ctx, int AD_AccessLog_ID, String trxName)
	{
		super (ctx, AD_AccessLog_ID, trxName);
	}	//	MAccessLog

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAccessLog (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAccessLog

	/**
	 * 	New Constructor
	 *	@param ctx context
	 */
	public MAccessLog (Properties ctx, String Remote_Host, String Remote_Addr, 
		String TextMsg, String trxName)
	{
		this (ctx, 0, trxName);
		setRemote_Addr(Remote_Addr);
		setRemote_Host(Remote_Host);
		setTextMsg(TextMsg);
	}	//	MAccessLog

	/**
	 * 	Discontinue
	 */
	public MAccessLog (Properties ctx, String Remote_Host, String Remote_Addr, 
		String TextMsg)
	{
		this (ctx, Remote_Host, Remote_Addr, TextMsg, null);
	}	//	MAccessLog

	/**
	 * 	New Constructor
	 *	@param ctx context
	 */
	public MAccessLog (Properties ctx, int AD_Table_ID, int AD_Column_ID, int Record_ID, String trxName)
	{
		this (ctx, 0, trxName);
		setAD_Table_ID(AD_Table_ID);
		setAD_Column_ID(AD_Column_ID);
		setRecord_ID(Record_ID);
	}	//	MAccessLog
	
}	//	MAccessLog
