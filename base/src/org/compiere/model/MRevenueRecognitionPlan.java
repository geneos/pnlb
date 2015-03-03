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
import org.compiere.util.*;

/**
 *	Revenue Recognition Plan
 *	
 *  @author Jorg Janke
 *  @version $Id: MRevenueRecognitionPlan.java,v 1.6 2005/05/17 05:29:53 jjanke Exp $
 */
public class MRevenueRecognitionPlan extends X_C_RevenueRecognition_Plan
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RevenueRecognition_Plan_ID id
	 */
	public MRevenueRecognitionPlan (Properties ctx, int C_RevenueRecognition_Plan_ID, String trxName)
	{
		super (ctx, C_RevenueRecognition_Plan_ID, trxName);
		if (C_RevenueRecognition_Plan_ID == 0)
		{
		//	setC_AcctSchema_ID (0);
		//	setC_Currency_ID (0);
		//	setC_InvoiceLine_ID (0);
		//	setC_RevenueRecognition_ID (0);
		//	setC_RevenueRecognition_Plan_ID (0);
		//	setP_Revenue_Acct (0);
		//	setUnEarnedRevenue_Acct (0);
			setTotalAmt (Env.ZERO);
			setRecognizedAmt (Env.ZERO);
		}	
	}	//	MRevenueRecognitionPlan

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRevenueRecognitionPlan (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRevenueRecognitionPlan

	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord)
		{
			MRevenueRecognition rr = new MRevenueRecognition(getCtx(), getC_RevenueRecognition_ID(), get_TrxName());
			if (rr.isTimeBased())
			{
				/**	Get InvoiveQty
				SELECT	QtyInvoiced, M_Product_ID 
				  INTO	v_Qty, v_M_Product_ID
				FROM	C_InvoiceLine 
				WHERE 	C_InvoiceLine_ID=:new.C_InvoiceLine_ID;
				--	Insert
				AD_Sequence_Next ('C_ServiceLevel', :new.AD_Client_ID, v_NextNo);
				INSERT INTO C_ServiceLevel
					(C_ServiceLevel_ID, C_RevenueRecognition_Plan_ID,
					AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,
					M_Product_ID, Description, ServiceLevelInvoiced, ServiceLevelProvided,
					Processing,Processed)
				VALUES
					(v_NextNo, :new.C_RevenueRecognition_Plan_ID,
					:new.AD_Client_ID,:new.AD_Org_ID,'Y',SysDate,:new.CreatedBy,SysDate,:new.UpdatedBy,
					v_M_Product_ID, NULL, v_Qty, 0,
					'N', 'N');
				**/
			}
		}
		return success;
	}	//	afterSave
}	//	MRevenueRecognitionPlan
