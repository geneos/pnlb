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
 * 	Product Category Account Model
 *  @author Jorg Janke
 *  @version $Id: MProductCategoryAcct.java,v 1.3 2005/08/02 01:52:23 jjanke Exp $
 */
public class MProductCategoryAcct extends X_M_Product_Category_Acct
{
	/**
	 * 	Get Category Acct
	 *	@param ctx context
	 *	@param M_Product_Category_ID category
	 *	@param C_AcctSchema_ID acct schema
	 *	@param trxName trx
	 *	@return category acct
	 */
	public static MProductCategoryAcct get (Properties ctx, 
		int M_Product_Category_ID, int C_AcctSchema_ID, String trxName)
	{
		MProductCategoryAcct retValue = null;
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM  M_Product_Category_Acct "
			+ "WHERE M_Product_Category_ID=? AND C_AcctSchema_ID=?";
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_Category_ID);
			pstmt.setInt (2, C_AcctSchema_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MProductCategoryAcct (ctx, rs, trxName);
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
		return retValue;
	}	//	get
	
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (MProductCategoryAcct.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param ignored ignored 
	 *	@param trxName
	 */
	public MProductCategoryAcct (Properties ctx, int ignored, String trxName)
	{
		super (ctx, ignored, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MProductCategoryAcct

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MProductCategoryAcct (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MProductCategoryAcct

	/**
	 * 	Check Costing Setup
	 */
	public void checkCosting()
	{
		//	Create Cost Elements
		if (getCostingMethod() != null && getCostingMethod().length() > 0)
			MCostElement.getMaterialCostElement(this, getCostingMethod());
	}	//	checkCosting

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		checkCosting();
		return success;
	}	//	afterSave
	
}	//	MProductCategoryAcct
