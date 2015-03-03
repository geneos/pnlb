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
 *	Commission Run
 *	
 *  @author Jorg Janke
 *  @version $Id: MCommissionRun.java,v 1.7 2005/09/24 01:52:52 jjanke Exp $
 */
public class MCommissionRun extends X_C_CommissionRun
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_CommissionRun_ID id
	 */
	public MCommissionRun (Properties ctx, int C_CommissionRun_ID, String trxName)
	{
		super(ctx, C_CommissionRun_ID, trxName);
		if (C_CommissionRun_ID == 0)
		{
		//	setC_Commission_ID (0);
		//	setDocumentNo (null);
		//	setStartDate (new Timestamp(System.currentTimeMillis()));
			setGrandTotal (Env.ZERO);
			setProcessed (false);
		}
	}	//	MCommissionRun

	/**
	 * 	Parent Constructor
	 *	@param commission parent
	 */
	public MCommissionRun (MCommission commission)
	{
		this (commission.getCtx(), 0, commission.get_TrxName());
		setClientOrg (commission);
		setC_Commission_ID (commission.getC_Commission_ID());
	}	//	MCommissionRun

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MCommissionRun(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCommissionRun

	/**
	 * 	Get Amounts
	 *	@return array of amounts
	 */
	public MCommissionAmt[] getAmts()
	{
		String sql = "SELECT * FROM C_CommissionAmt WHERE C_CommissionRun_ID=?";
		ArrayList<MCommissionAmt> list = new ArrayList<MCommissionAmt>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_CommissionRun_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MCommissionAmt(getCtx(), rs, get_TrxName()));
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
		MCommissionAmt[] retValue = new MCommissionAmt[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getAmts

	/**
	 * 	Update From Amt
	 */
	public void updateFromAmt()
	{
		MCommissionAmt[] amts = getAmts();
		BigDecimal GrandTotal = Env.ZERO;
		for (int i = 0; i < amts.length; i++)
		{
			MCommissionAmt amt = amts[i];
			GrandTotal = GrandTotal.add(amt.getCommissionAmt());
		}
		setGrandTotal(GrandTotal);
	}	//	updateFromAmt
	
}	//	MCommissionRun
