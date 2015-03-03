/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Landed Cost Allocation Model
 *  @author Jorg Janke
 *  @version $Id: MLandedCostAllocation.java,v 1.6 2005/11/14 02:10:53 jjanke Exp $
 */
public class MLandedCostAllocation extends X_C_LandedCostAllocation
{	
	/**
	 * 	Get Cost Allocations for invoice Line
	 *	@param ctx context
	 *	@param C_InvoiceLine_ID invoice line
	 *	@param trxName trx
	 *	@return landed cost alloc
	 */
	public static MLandedCostAllocation[] getOfInvoiceLine (Properties ctx, int C_InvoiceLine_ID, String trxName)
	{
		ArrayList<MLandedCostAllocation> list = new ArrayList<MLandedCostAllocation>();
		String sql = "SELECT * FROM C_LandedCostAllocation WHERE C_InvoiceLine_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, C_InvoiceLine_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MLandedCostAllocation (ctx, rs, trxName));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		MLandedCostAllocation[] retValue = new MLandedCostAllocation[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfInvliceLine
	
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (MLandedCostAllocation.class);
	
	
	/***************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_LandedCostAllocation_ID id
	 *	@param trxName trx
	 */
	public MLandedCostAllocation (Properties ctx, int C_LandedCostAllocation_ID, String trxName)
	{
		super (ctx, C_LandedCostAllocation_ID, trxName);
		if (C_LandedCostAllocation_ID == 0)
		{
		//	setM_CostElement_ID(0);
			setAmt (Env.ZERO);
			setQty (Env.ZERO);
			setBase (Env.ZERO);
		}
	}	//	MLandedCostAllocation

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result name
	 *	@param trxName trx
	 */
	public MLandedCostAllocation (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MLandedCostAllocation

	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param M_CostElement_ID cost element
	 */
	public MLandedCostAllocation (MInvoiceLine parent, int M_CostElement_ID)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setC_InvoiceLine_ID(parent.getC_InvoiceLine_ID());
		setM_CostElement_ID(M_CostElement_ID);
	}	//	MLandedCostAllocation
	
	/**
	 * 	Set Amt
	 *	@param Amt amount
	 *	@param precision precision
	 */
	public void setAmt (double Amt, int precision)
	{
		BigDecimal bd = new BigDecimal(Amt);
		if (bd.scale() > precision)
			bd = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
		super.setAmt(bd);
	}	//	setAmt

}	//	MLandedCostAllocation
