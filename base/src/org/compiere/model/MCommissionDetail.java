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

import java.math.*;
import java.sql.*;
import java.util.*;
import org.compiere.util.*;

/**
 *	Commission Run Amount Detail Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCommissionDetail.java,v 1.6 2005/03/11 20:26:05 jjanke Exp $
 */
public class MCommissionDetail extends X_C_CommissionDetail
{
	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MCommissionDetail (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MCommissionDetail

	/**
	 * 	Parent Constructor
	 *	@param amt commission amt
	 */
	public MCommissionDetail (MCommissionAmt amt, int C_Currency_ID,
		BigDecimal Amt, BigDecimal Qty)
	{
		super (amt.getCtx(), 0, amt.get_TrxName());
		setClientOrg(amt);
		setC_CommissionAmt_ID(amt.getC_CommissionAmt_ID());
		setC_Currency_ID (C_Currency_ID);
		setActualAmt (Amt);
		setActualQty (Qty);
		setConvertedAmt (Env.ZERO);
	}	//	MCommissionDetail

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MCommissionDetail(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCommissionDetail

	/**
	 * 	Set Line IDs
	 *	@param C_OrderLine_ID order
	 *	@param C_InvoiceLine_ID invoice
	 */
	public void setLineIDs (int C_OrderLine_ID, int C_InvoiceLine_ID)
	{
		if (C_OrderLine_ID != 0)
			setC_OrderLine_ID(C_OrderLine_ID);
		if (C_InvoiceLine_ID != 0)
			setC_InvoiceLine_ID(C_InvoiceLine_ID);
	}	//	setLineIDs

	
	/**
	 * 	Set Converted Amt
	 *	@param date for conversion
	 */
	public void setConvertedAmt (Timestamp date)
	{
		BigDecimal amt = MConversionRate.convertBase(getCtx(), 
			getActualAmt(), getC_Currency_ID(), date, 0, 	//	type
			getAD_Client_ID(), getAD_Org_ID());
		if (amt != null)
			setConvertedAmt(amt);
	}	//	setConvertedAmt

	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!newRecord)
			updateAmtHeader();
		return success;
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		if (success)
			updateAmtHeader();
		return success;
	}	//	afterDelete
	
	/**
	 * 	Update Amt Header
	 */
	private void updateAmtHeader()
	{
		MCommissionAmt amt = new MCommissionAmt(getCtx(), getC_CommissionAmt_ID(), get_TrxName());
		amt.calculateCommission();
		amt.save();
	}	//	updateAmtHeader
	
}	//	MCommissionDetail
