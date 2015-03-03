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
 * 	Recurring Run Model
 *
 *	@author Jorg Janke
 *	@version $Id: MRecurringRun.java,v 1.5 2005/03/11 20:26:05 jjanke Exp $
 */
public class MRecurringRun extends X_C_Recurring_Run
{

	public MRecurringRun (Properties ctx, int C_Recurring_Run_ID, String trxName)
	{
		super (ctx, C_Recurring_Run_ID, trxName);
	}	//	MRecurringRun

	public MRecurringRun (Properties ctx, MRecurring recurring)
	{
		super(ctx, 0, recurring.get_TrxName());
		if (recurring != null)
		{
			setAD_Client_ID(recurring.getAD_Client_ID());
			setAD_Org_ID(recurring.getAD_Org_ID());
			setC_Recurring_ID (recurring.getC_Recurring_ID ());
			setDateDoc(recurring.getDateNextRun());
		}
	}	//	MRecurringRun

	public MRecurringRun (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRecurringRun

}	//	MRecurringRun
