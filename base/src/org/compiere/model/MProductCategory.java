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
 *	Product Category Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MProductCategory.java,v 1.13 2005/11/09 17:38:39 jjanke Exp $
 */
public class MProductCategory extends X_M_Product_Category
{
	/**
	 * 	Get from Cache
	 *	@param ctx context
	 *	@param M_Product_Category_ID id
	 *	@return category
	 */
	public static MProductCategory get (Properties ctx, int M_Product_Category_ID)
	{
		Integer ii = new Integer (M_Product_Category_ID);
		MProductCategory pc = (MProductCategory)s_cache.get(ii);
		if (pc == null)
			pc = new MProductCategory (ctx, M_Product_Category_ID, null);
		return pc;
	}	//	get
	
	/**
	 * 	Is Product in Category
	 *	@param M_Product_Category_ID category
	 *	@param M_Product_ID product
	 *	@return true if product has category
	 */
	public static boolean isCategory (int M_Product_Category_ID, int M_Product_ID)
	{
		if (M_Product_ID == 0 || M_Product_Category_ID == 0)
			return false;
		//	Look up
		Integer product = new Integer (M_Product_ID);
		Integer category = (Integer)s_products.get(product);
		if (category != null)
			return category.intValue() == M_Product_Category_ID;
		
		String sql = "SELECT M_Product_Category_ID FROM M_Product WHERE M_Product_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, M_Product_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				category = new Integer(rs.getInt(1));
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
		if (category != null)
		{
			//	TODO: LRU logic  
			s_products.put(product, category);
			//
			s_log.fine("M_Product_ID=" + M_Product_ID + "(" + category
				+ ") in M_Product_Category_ID=" + M_Product_Category_ID
				+ " - " + (category.intValue() == M_Product_Category_ID));
			return category.intValue() == M_Product_Category_ID;
		}
		s_log.log(Level.SEVERE, "Not found M_Product_ID=" + M_Product_ID);
		return false;
	}	//	isCategory
	
	/**	Categopry Cache				*/
	private static CCache<Integer,MProductCategory>	s_cache = new CCache<Integer,MProductCategory>("M_Product_Category", 20);
	/**	Product Cache				*/
	private static CCache<Integer,Integer> s_products = new CCache<Integer,Integer>("M_Product", 100);
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MProductCategory.class);

	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_Product_Category_ID id
	 */
	public MProductCategory (Properties ctx, int M_Product_Category_ID, String trxName)
	{
		super(ctx, M_Product_Category_ID, trxName);
		if (M_Product_Category_ID == 0)
		{
		//	setName (null);
		//	setValue (null);
			setMMPolicy (MMPOLICY_FiFo);	// F
			setPlannedMargin (Env.ZERO);
			setIsDefault (false);
			setIsSelfService (true);	// Y
		}
	}	//	MProductCategory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MProductCategory(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProductCategory

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord & success)
			insert_Accounting("M_Product_Category_Acct", "C_AcctSchema_Default", null);

		return success;
	}	//	afterSave

	/**
	 * 	Before Delete
	 *	@return true
	 */
	protected boolean beforeDelete ()
	{
		return delete_Accounting("M_Product_Category_Acct"); 
	}	//	beforeDelete
	
	/**
	 * 	FiFo Material Movement Policy
	 *	@return true if FiFo
	 */
	public boolean isFiFo()
	{
		return MMPOLICY_FiFo.equals(getMMPolicy());
	}	//	isFiFo
	
}	//	MProductCategory
