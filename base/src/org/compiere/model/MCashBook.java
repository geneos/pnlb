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
 *	Cash Book Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCashBook.java,v 1.22 2005/11/09 17:38:39 jjanke Exp $
 */
public class MCashBook extends X_C_CashBook
{
	/**
	 * 	Get MCashBook from Cache
	 *	@param ctx context
	 *	@param C_CashBook_ID id
	 *	@return MCashBook
	 */
	public static MCashBook get (Properties ctx, int C_CashBook_ID)
	{
		Integer key = new Integer (C_CashBook_ID);
		MCashBook retValue = (MCashBook) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MCashBook (ctx, C_CashBook_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get CashBook for Org and Currency
	 *	@param ctx context
	 *	@param AD_Org_ID org
	 *	@param C_Currency_ID currency
	 *	@return cash book or null
	 */
	public static MCashBook get (Properties ctx, int AD_Org_ID, int C_Currency_ID)
	{
		//	Try from cache
		Iterator it = s_cache.values().iterator();
		while (it.hasNext())
		{
			MCashBook cb = (MCashBook)it.next();
			if (cb.getAD_Org_ID() == AD_Org_ID && cb.getC_Currency_ID() == C_Currency_ID)
				return cb;
		}
		
		//	Get from DB
		MCashBook retValue = null;
		String sql = "SELECT * FROM C_CashBook "
			+ "WHERE AD_Org_ID=? AND C_Currency_ID=? "
			+ "ORDER BY IsDefault DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_Org_ID);
			pstmt.setInt (2, C_Currency_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MCashBook (ctx, rs, null);
				Integer key = new Integer (retValue.getC_CashBook_ID());
				s_cache.put (key, retValue);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "get", e);
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
	

	/**	Cache						*/
	private static CCache<Integer,MCashBook> s_cache
		= new CCache<Integer,MCashBook>("", 20);
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MCashBook.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_CashBook_ID id
	 */
	public MCashBook (Properties ctx, int C_CashBook_ID, String trxName)
	{
		super (ctx, C_CashBook_ID, trxName);
	}	//	MCashBook

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MCashBook (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCashBook
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord & success)
			insert_Accounting("C_CashBook_Acct", "C_AcctSchema_Default", null);

		return success;
	}	//	afterSave

	/**
	 * 	Before Delete
	 *	@return true
	 */
	protected boolean beforeDelete ()
	{
		return delete_Accounting("C_Cashbook_Acct"); 
	}	//	beforeDelete

}	//	MCashBook
