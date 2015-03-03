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

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Landed Cost Model
 *  @author Jorg Janke
 *  @version $Id: MLandedCost.java,v 1.7 2005/11/14 02:10:53 jjanke Exp $
 */
public class MLandedCost extends X_C_LandedCost
{
	/**
	 *	Get Costs of Invoice Line
	 * 	@param il invoice line
	 *	@return array of landed cost lines
	 */
	public static MLandedCost[] getLandedCosts (MInvoiceLine il)
	{
		ArrayList<MLandedCost> list = new ArrayList<MLandedCost> ();
		String sql = "SELECT * FROM C_LandedCost WHERE C_InvoiceLine_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, il.get_TrxName());
			pstmt.setInt (1, il.getC_InvoiceLine_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MLandedCost (il.getCtx(), rs, il.get_TrxName()));
			}
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
		//
		MLandedCost[] retValue = new MLandedCost[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	// getLandedCosts

	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (MLandedCost.class);

	
	/***************************************************************************
	 * Standard Constructor
	 * 
	 * @param ctx context
	 * @param C_LandedCost_ID id
	 * @param trxName trx
	 */
	public MLandedCost (Properties ctx, int C_LandedCost_ID, String trxName)
	{
		super (ctx, C_LandedCost_ID, trxName);
		if (C_LandedCost_ID == 0)
		{
		//	setC_InvoiceLine_ID (0);
		//	setM_CostElement_ID (0);
			setLandedCostDistribution (LANDEDCOSTDISTRIBUTION_Quantity);	// Q
		}
	}	//	MLandedCost

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MLandedCost (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MLandedCost
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if ok
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	One Reference
		if (getM_Product_ID() == 0 
			&& getM_InOut_ID() == 0 
			&& getM_InOutLine_ID() == 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), 
				"@NotFound@ @M_Product_ID@ | @M_InOut_ID@ | @M_InOutLine_ID@"));
			return false;
		}
		//	No Product if Line entered
		if (getM_InOutLine_ID() != 0 && getM_Product_ID() != 0)
			setM_Product_ID(0);
				
		return true;
	}	//	beforeSave
	
	/**
	 * 	Allocate Costs.
	 * 	Done at Invoice Line Level
	 * 	@return error message or ""
	 */
	public String allocateCosts()
	{
		MInvoiceLine il = new MInvoiceLine (getCtx(), getC_InvoiceLine_ID(), get_TrxName());
		return il.allocateLandedCosts();
	}	//	allocateCosts
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MLandedCost[");
		sb.append (get_ID ())
			.append (",CostDistribution=").append (getLandedCostDistribution())
			.append(",M_CostElement_ID=").append(getM_CostElement_ID());
		if (getM_InOut_ID() != 0)
			sb.append (",M_InOut_ID=").append (getM_InOut_ID());
		if (getM_InOutLine_ID() != 0)
			sb.append (",M_InOutLine_ID=").append (getM_InOutLine_ID());
		if (getM_Product_ID() != 0)
			sb.append (",M_Product_ID=").append (getM_Product_ID());
		sb.append ("]");
		return sb.toString ();
	} //	toString
	
}	//	MLandedCost
