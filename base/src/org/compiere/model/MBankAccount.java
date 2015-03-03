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
import org.compiere.util.*;


/**
 *  Bank Account Model
 *
 *  @author Jorg Janke
 *  @version $Id: MBankAccount.java,v 1.17 2005/12/27 06:17:56 jjanke Exp $
 */
public class MBankAccount extends X_C_BankAccount
{
	/**
	 * 	Get BankAccount from Cache
	 *	@param ctx context
	 *	@param C_BankAccount_ID id
	 *	@return MBankAccount
	 */
	public static MBankAccount get (Properties ctx, int C_BankAccount_ID)
	{
		Integer key = new Integer (C_BankAccount_ID);
		MBankAccount retValue = (MBankAccount) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MBankAccount (ctx, C_BankAccount_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MBankAccount>	s_cache
		= new CCache<Integer,MBankAccount>("C_BankAccount", 5);
	
	/**
	 * 	Bank Account Model
	 *	@param ctx context
	 *	@param C_BankAccount_ID bank account
	 */
	public MBankAccount (Properties ctx, int C_BankAccount_ID, String trxName)
	{
		super (ctx, C_BankAccount_ID, trxName);
		if (C_BankAccount_ID == 0)
		{
			setIsDefault (false);
			setBankAccountType (BANKACCOUNTTYPE_Checking);
			setCurrentBalance (Env.ZERO);
		//	setC_Currency_ID (0);
			setCreditLimit (Env.ZERO);
		//	setC_BankAccount_ID (0);
		}
	}	//	MBankAccount

	/**
	 * 	Bank Account Model
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MBankAccount (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBankAccount

	/**
	 * 	String representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MBankAccount[")
			.append (get_ID())
			.append("-").append(getAccountNo())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Bank
	 *	@return bank parent
	 */
	public MBank getBank()
	{
		return MBank.get(getCtx(), getC_Bank_ID());
	}	//	getBank
	
	/**
	 * 	Get Bank Name and Account No
	 *	@return Bank/Account
	 */
	public String getName()
	{
		return getBank().getName() + " " + getAccountNo();
	}	//	getName
	
	/**
	 * 	After Save
	 *	@param newRecord new record
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord & success)
			return insert_Accounting("C_BankAccount_Acct", "C_AcctSchema_Default", null);
		return success;
	}	//	afterSave
	
	/**
	 * 	Before Delete
	 *	@return true
	 */
	protected boolean beforeDelete ()
	{
		return delete_Accounting("C_BankAccount_Acct");
	}	//	beforeDelete

}	//	MBankAccount
