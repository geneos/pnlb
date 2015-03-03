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
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Commission Run Amounts
 *	
 *  @author Jorg Janke
 *  @version $Id: MCommissionAmt.java,v 1.8 2005/10/08 02:02:29 jjanke Exp $
 */
public class MCommissionAmt extends X_C_CommissionAmt
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_CommissionAmt_ID id
	 */
	public MCommissionAmt(Properties ctx, int C_CommissionAmt_ID, String trxName)
	{
		super(ctx, C_CommissionAmt_ID, trxName);
		if (C_CommissionAmt_ID == 0)
		{
		//	setC_CommissionRun_ID (0);
		//	setC_CommissionLine_ID (0);
			setActualQty (Env.ZERO);
			setCommissionAmt (Env.ZERO);
			setConvertedAmt (Env.ZERO);
		}
	}	//	MCommissionAmt

	/**
	 * 	Parent Constructor
	 *	@param run parent
	 *	@param C_CommissionLine_ID line
	 */
	public MCommissionAmt (MCommissionRun run, int C_CommissionLine_ID)
	{
		this (run.getCtx(), 0, run.get_TrxName());
		setClientOrg (run);
		setC_CommissionRun_ID (run.getC_CommissionRun_ID());
		setC_CommissionLine_ID (C_CommissionLine_ID);
	}	//	MCommissionAmt

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MCommissionAmt(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCommissionAmt

	/**
	 * 	Get Details
	 *	@return array of details
	 */
	public MCommissionDetail[] getDetails()
	{
		String sql = "SELECT * FROM C_CommissionDetail WHERE C_CommissionAmt_ID=?";
		ArrayList<MCommissionDetail> list = new ArrayList<MCommissionDetail>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_CommissionAmt_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MCommissionDetail(getCtx(), rs, get_TrxName()));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e); 
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}

		//	Convert
		MCommissionDetail[] retValue = new MCommissionDetail[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getDetails

	/**
	 * 	Calculate Commission
	 */
	public void calculateCommission()
	{
		MCommissionDetail[] details = getDetails();
		BigDecimal ConvertedAmt = Env.ZERO;
		BigDecimal ActualQty = Env.ZERO;
		for (int i = 0; i < details.length; i++)
		{
			MCommissionDetail detail = details[i];
			BigDecimal amt = detail.getConvertedAmt();
			if (amt == null)
				amt = Env.ZERO;
			ConvertedAmt = ConvertedAmt.add(amt);
			ActualQty = ActualQty.add(detail.getActualQty());
		}
		setConvertedAmt(ConvertedAmt);
		setActualQty(ActualQty);
		//
		MCommissionLine cl = new MCommissionLine(getCtx(), getC_CommissionLine_ID(), get_TrxName());
		//	Qty
		BigDecimal qty = getActualQty().subtract(cl.getQtySubtract());
		if (cl.isPositiveOnly() && qty.signum() < 0)
			qty = Env.ZERO;
		qty = qty.multiply(cl.getQtyMultiplier());
		//	Amt
		BigDecimal amt = getConvertedAmt().subtract(cl.getAmtSubtract());
		if (cl.isPositiveOnly() && amt.signum() < 0)
			amt = Env.ZERO;
		amt = amt.multiply(cl.getAmtMultiplier());
		//
		setCommissionAmt(amt.add(qty));
	}	//	calculateCommission
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!newRecord)
			updateRunHeader();
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
			updateRunHeader();
		return success;
	}	//	afterDelete
	
	/**
	 * 	Update Amt Header
	 */
	private void updateRunHeader()
	{
		MCommissionRun run = new MCommissionRun(getCtx(), getC_CommissionRun_ID(),get_TrxName());
		run.updateFromAmt();
		run.save();
	}	//	updateRunHeader

}	//	MCommissionAmt
