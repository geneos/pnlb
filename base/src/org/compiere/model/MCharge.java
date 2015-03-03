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
 *	Charge Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCharge.java,v 1.11 2006/01/21 02:23:37 jjanke Exp $
 */
public class MCharge extends X_C_Charge
{
	/**
	 *  Get Charge Account
	 *  @param as account schema
	 *  @param amount amount for expense(+)/revenue(-)
	 *  @return Charge Account or null
	 */
	public static MAccount getAccount (int C_Charge_ID, MAcctSchema as, BigDecimal amount)
	{
		if (C_Charge_ID == 0 || as == null)
			return null;

		int acct_index = 1;     //  Expense (positive amt)
		if (amount != null && amount.signum() < 0)
			acct_index = 2;     //  Revenue (negative amt)
		String sql = "SELECT CH_Expense_Acct, CH_Revenue_Acct FROM C_Charge_Acct WHERE C_Charge_ID=? AND C_AcctSchema_ID=?";
		int Account_ID = 0;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt (1, C_Charge_ID);
			pstmt.setInt (2, as.getC_AcctSchema_ID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				Account_ID = rs.getInt(acct_index);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
			return null;
		}
		//	No account
		if (Account_ID == 0)
		{
			s_log.severe ("NO account for C_Charge_ID=" + C_Charge_ID);
			return null;
		}

		//	Return Account
		MAccount acct = MAccount.get (as.getCtx(), Account_ID);
		return acct;
	}   //  getAccount

	/**
	 * 	Get MCharge from Cache
	 *	@param ctx context
	 *	@param C_Charge_ID id
	 *	@return MCharge
	 */
	public static MCharge get (Properties ctx, int C_Charge_ID)
	{
		Integer key = new Integer (C_Charge_ID);
		MCharge retValue = (MCharge)s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MCharge (ctx, C_Charge_ID, null);
		if (retValue.get_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer, MCharge> s_cache 
		= new CCache<Integer, MCharge> ("C_Charge", 10);
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MCharge.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Charge_ID id
	 */
	public MCharge (Properties ctx, int C_Charge_ID, String trxName)
	{
		super (ctx, C_Charge_ID, null);
		if (C_Charge_ID == 0)
		{
			setChargeAmt (Env.ZERO);
			setIsSameCurrency (false);
			setIsSameTax (false);
			setIsTaxIncluded (false);	// N
		//	setName (null);
		//	setC_TaxCategory_ID (0);
		}
	}	//	MCharge

	/**
	 * 	Load Constructor
	 *	@param ctx ctx
	 *	@param rs result set
	 */
	public MCharge (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCharge

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord & success)
			insert_Accounting("C_Charge_Acct", "C_AcctSchema_Default", null);

		return success;
	}	//	afterSave

	/**
	 * 	Before Delete
	 *	@return true
	 */
	protected boolean beforeDelete ()
	{
		return delete_Accounting("C_Charge_Acct"); 
	}	//	beforeDelete


}	//	MCharge
