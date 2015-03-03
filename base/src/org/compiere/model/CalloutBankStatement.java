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
 *	Bank Statement Callout	
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutBankStatement.java,v 1.6 2005/11/06 01:17:27 jjanke Exp $
 */
public class CalloutBankStatement extends CalloutEngine
{
	/**
	 * 	Bank Account Changed.
	 * 	Update Beginning Balance
	 *	@return -
	 */
	public String bankAccount (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (value == null)
			return "";
		int C_BankAccount_ID = ((Integer)value).intValue();
		MBankAccount ba = MBankAccount.get(ctx, C_BankAccount_ID);
		mTab.setValue("BeginningBalance", ba.getCurrentBalance());
		return "";
	}	//	bankAccount
	
	/**
	 *	BankStmt - Amount.
	 *  Calculate ChargeAmt = StmtAmt - TrxAmt - InterestAmt
	 *    or id Charge is entered - InterestAmt = StmtAmt - TrxAmt - ChargeAmt
	 */
	public String amount (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive())
			return "";
		setCalloutActive(true);

		//  Get Stmt & Trx
		BigDecimal stmt = (BigDecimal)mTab.getValue("StmtAmt");
		if (stmt == null)
			stmt = Env.ZERO;
		BigDecimal trx = (BigDecimal)mTab.getValue("TrxAmt");
		if (trx == null)
			trx = Env.ZERO;
		BigDecimal bd = stmt.subtract(trx);

		//  Charge - calculate Interest
		if (mField.getColumnName().equals("ChargeAmt"))
		{
			BigDecimal charge = (BigDecimal)value;
			if (charge == null)
				charge = Env.ZERO;
			bd = bd.subtract(charge);
		//	log.trace(log.l5_DData, "Interest (" + bd + ") = Stmt(" + stmt + ") - Trx(" + trx + ") - Charge(" + charge + ")");
			mTab.setValue("InterestAmt", bd);
		}
		//  Calculate Charge
		else
		{
			BigDecimal interest = (BigDecimal)mTab.getValue("InterestAmt");
			if (interest == null)
				interest = Env.ZERO;
			bd = bd.subtract(interest);
		//	log.trace(log.l5_DData, "Charge (" + bd + ") = Stmt(" + stmt + ") - Trx(" + trx + ") - Interest(" + interest + ")");
			mTab.setValue("ChargeAmt", bd);
		}
		setCalloutActive(false);
		return "";
	}   //  amount


	/**
	 *	BankStmt - Payment.
	 *  Update Transaction Amount when payment is selected
	 */
	public String payment (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer C_Payment_ID = (Integer)value;
		if (C_Payment_ID == null || C_Payment_ID.intValue() == 0)
			return "";
		//
		BigDecimal stmt = (BigDecimal)mTab.getValue("StmtAmt");
		if (stmt == null)
			stmt = Env.ZERO;

		String sql = "SELECT PayAmt FROM C_Payment_v WHERE C_Payment_ID=?";		//	1
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Payment_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				BigDecimal bd = rs.getBigDecimal(1);
				mTab.setValue("TrxAmt", bd);
				if (stmt.compareTo(Env.ZERO) == 0)
					mTab.setValue("StmtAmt", bd);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "BankStmt_Payment", e);
			return e.getLocalizedMessage();
		}
		//  Recalculate Amounts
		amount (ctx, WindowNo, mTab, mField, value);
		return "";
	}	//	payment

}	//	CalloutBankStatement
