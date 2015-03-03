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

import java.sql.ResultSet;
import java.util.Properties;

/**
 *	Payment Batch Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPaymentBatch.java,v 1.5 2005/05/17 05:29:53 jjanke Exp $
 */
public class MPaymentBatch extends X_C_PaymentBatch
{
	/**
	 * 	Get Payment Batch for PaySelection
	 *	@param ctx context
	 *	@param C_PaySelection_ID id
	 *	@return payment batch
	 */
	public static MPaymentBatch getForPaySelection (Properties ctx, int C_PaySelection_ID, String trxName)
	{
		MPaySelection ps = new MPaySelection (ctx, C_PaySelection_ID, trxName);
		MPaymentBatch retValue = new MPaymentBatch (ps);
		return retValue;
	}	//	getForPaySelection
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_PaymentBatch_ID id
	 */
	public MPaymentBatch (Properties ctx, int C_PaymentBatch_ID, String trxName)
	{
		super(ctx, C_PaymentBatch_ID, trxName);
		if (C_PaymentBatch_ID == 0)
		{
		//	setName (null);
			setProcessed (false);
			setProcessing (false);
		}
	}	//	MPaymentBatch

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPaymentBatch (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPaymentBatch

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param Name name
	 *	@param trxName trx
	 */
	public MPaymentBatch (Properties ctx, String Name, String trxName)
	{
		this (ctx, 0, trxName);
		setName (Name);
	}	//	MPaymentBatch

	/**
	 * 	Parent Constructor
	 *	@param ps Pay Selection
	 */
	public MPaymentBatch (MPaySelection ps)
	{
		this (ps.getCtx(), 0, ps.get_TrxName());
		setClientOrg(ps);
		setName (ps.getName());
	}	//	MPaymentBatch

}	//	MPaymentBatch
