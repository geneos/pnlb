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
package org.compiere.sqlj;

import java.math.*;
import java.sql.*;


/**
 *	SQLJ Account related Functions
 *	
 *  @author Jorg Janke
 *  @version $Id: Account.java,v 1.4 2005/03/11 20:35:02 jjanke Exp $
 */
public class Account
{

	/**
	 * 	Get Balance based on Account Sign and Type.
	 * 	Acct_Balance - acctBalance
	 *  If an account is specified and found
	 *  - If the account sign is Natural it sets it based on Account Type
	 *	@param p_Account_ID account
	 *	@param p_AmtDr debit
	 *	@param p_AmtCr credit
	 *	@return cr or dr balance
	 */
	public static BigDecimal balance (int p_Account_ID, BigDecimal p_AmtDr, BigDecimal p_AmtCr)
		throws SQLException
	{
		BigDecimal AmtDr = p_AmtDr;
		if (AmtDr == null)
			AmtDr = Compiere.ZERO;
		BigDecimal AmtCr = p_AmtCr;
		if (AmtCr == null)
			AmtCr = Compiere.ZERO;
		BigDecimal balance = AmtDr.subtract(AmtCr);
		//
		if (p_Account_ID != 0)
		{
			String sql = "SELECT AccountType, AccountSign "
				+ "FROM C_ElementValue "
				+ "WHERE C_ElementValue_ID=?";
			PreparedStatement pstmt = Compiere.prepareStatement(sql);
			pstmt.setInt(1, p_Account_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				String AccountType = rs.getString(1);
				String AccountSign = rs.getString(2);
				//	Natural Account Sign -> D/C
				if (AccountSign.equals("N"))
				{
					if (AccountType.equals("A") || AccountType.equals("E"))
		                AccountSign = "D";
					else
		                AccountSign = "C";
				}
				//	Debit Balance
				if (AccountSign.equals("C"))
					balance = AmtCr.subtract(AmtDr);
			}
			rs.close();
			pstmt.close();
		}
		//
		return balance;
	}	//	balance
	
}	//	Account
