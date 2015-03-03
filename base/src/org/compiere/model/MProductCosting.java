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
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Product Costing Model (old).
 *	deprecated old costing
 *
 *  @author Jorg Janke
 *  @version $Id: MProductCosting.java,v 1.6 2005/10/08 02:02:30 jjanke Exp $
 */
public class MProductCosting extends X_M_Product_Costing
{
	/**
	 * 	Get Costing Of Product
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param trxName trx
	 *	@return array of costs
	 */
	public static MProductCosting[] getOfProduct (Properties ctx, int M_Product_ID, String trxName)
	{
		String sql = "SELECT * FROM M_Product_Costing WHERE M_Product_ID=?";
		ArrayList<MProductCosting> list = new ArrayList<MProductCosting>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MProductCosting (ctx, rs, trxName));
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e); 
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
		//
		MProductCosting[] retValue = new MProductCosting[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfProduct

	/**
	 * 	Get Costing
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param C_AcctSchema_ID as
	 *	@param trxName trx
	 *	@return array of costs
	 */
	public static MProductCosting get (Properties ctx, int M_Product_ID, 
		int C_AcctSchema_ID, String trxName)
	{
		MProductCosting retValue = null;
		String sql = "SELECT * FROM M_Product_Costing WHERE M_Product_ID=? AND C_AcctSchema_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_ID);
			pstmt.setInt (2, C_AcctSchema_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next())
				retValue = new MProductCosting (ctx, rs, trxName);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e); 
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
		//
		return retValue;
	}	//	get

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MProductCosting.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param ignored (multi key)
	 */
	public MProductCosting (Properties ctx, int ignored, String trxName)
	{
		super (ctx, ignored, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		else
		{
		//	setM_Product_ID (0);
		//	setC_AcctSchema_ID (0);
			//
			setCostAverage (Env.ZERO);
			setCostAverageCumAmt (Env.ZERO);
			setCostAverageCumQty (Env.ZERO);
			setCostStandard (Env.ZERO);
			setCostStandardCumAmt (Env.ZERO);
			setCostStandardCumQty (Env.ZERO);
			setCostStandardPOAmt (Env.ZERO);
			setCostStandardPOQty (Env.ZERO);
			setCurrentCostPrice (Env.ZERO);
			setFutureCostPrice (Env.ZERO);
			setPriceLastInv (Env.ZERO);
			setPriceLastPO (Env.ZERO);
			setTotalInvAmt (Env.ZERO);
			setTotalInvQty (Env.ZERO);
		}
	}	//	MProductCosting

	/**
	 * 	Parent Constructor
	 *	@param product parent
	 *	@param C_AcctSchema_ID accounting schema
	 */
	public MProductCosting (MProduct product, int C_AcctSchema_ID)
	{
		super (product.getCtx(), 0, product.get_TrxName());
		setClientOrg(product);
		setM_Product_ID (product.getM_Product_ID());
		setC_AcctSchema_ID (C_AcctSchema_ID);
	}	//	MProductCosting
	
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MProductCosting (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProductCosting
	
}	//	MProductCosting

