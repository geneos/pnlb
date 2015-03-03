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
 *  Payment Processor Model
 *
 *  @author Jorg Janke
 *  @version $Id: MPaymentProcessor.java,v 1.14 2005/10/14 00:44:31 jjanke Exp $
 */
public class MPaymentProcessor extends X_C_PaymentProcessor
{
	/**
	 * 	Get BankAccount & PaymentProcessor
	 * 	@param ctx context
	 *  @param tender optional Tender see TENDER_
	 *  @param CCType optional CC Type see CC_
	 *  @param AD_Client_ID Client
	 *  @param C_Currency_ID Currency (ignored)
	 *  @param Amt Amount (ignored)
	 *  @return Array of BankAccount[0] & PaymentProcessor[1] or null
	 */
	protected static MPaymentProcessor[] find (Properties ctx,
		String tender, String CCType,
		int AD_Client_ID, int C_Currency_ID, BigDecimal Amt, String trxName)
	{
		ArrayList<MPaymentProcessor> list = new ArrayList<MPaymentProcessor>();
		StringBuffer sql = new StringBuffer("SELECT * "
			+ "FROM C_PaymentProcessor "
			+ "WHERE AD_Client_ID=? AND IsActive='Y'"				//	#1
			+ " AND (C_Currency_ID IS NULL OR C_Currency_ID=?)"		//	#2
			+ " AND (MinimumAmt IS NULL OR MinimumAmt = 0 OR MinimumAmt <= ?)");	//	#3
		if (MPayment.TENDERTYPE_DirectDeposit.equals(tender))
			sql.append(" AND AcceptDirectDeposit='Y'");
		else if (MPayment.TENDERTYPE_DirectDebit.equals(tender))
			sql.append(" AND AcceptDirectDebit='Y'");
		else if (MPayment.TENDERTYPE_Check.equals(tender))
			sql.append(" AND AcceptCheck='Y'");
		//  CreditCards
		else if (MPayment.CREDITCARDTYPE_ATM.equals(CCType))
			sql.append(" AND AcceptATM='Y'");
		else if (MPayment.CREDITCARDTYPE_Amex.equals(CCType))
			sql.append(" AND AcceptAMEX='Y'");
		else if (MPayment.CREDITCARDTYPE_Visa.equals(CCType))
			sql.append(" AND AcceptVISA='Y'");
		else if (MPayment.CREDITCARDTYPE_MasterCard.equals(CCType))
			sql.append(" AND AcceptMC='Y'");
		else if (MPayment.CREDITCARDTYPE_Diners.equals(CCType))
			sql.append(" AND AcceptDiners='Y'");
		else if (MPayment.CREDITCARDTYPE_Discover.equals(CCType))
			sql.append(" AND AcceptDiscover='Y'");
		else if (MPayment.CREDITCARDTYPE_PurchaseCard.equals(CCType))
			sql.append(" AND AcceptCORPORATE='Y'");
		//
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), trxName);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, C_Currency_ID);
			pstmt.setBigDecimal(3, Amt);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MPaymentProcessor (ctx, rs, trxName));
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, "find - " + sql, e);
			return null;
		}
		//
		if (list.size() == 0)
			s_log.warning("find - not found - AD_Client_ID=" + AD_Client_ID
				+ ", C_Currency_ID=" + C_Currency_ID + ", Amt=" + Amt);
		else
			s_log.fine("find - #" + list.size() + " - AD_Client_ID=" + AD_Client_ID
				+ ", C_Currency_ID=" + C_Currency_ID + ", Amt=" + Amt);
		MPaymentProcessor[] retValue = new MPaymentProcessor[list.size()];
		list.toArray(retValue);
		return retValue;
	}   //  find
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MPaymentProcessor.class);

	
	/**************************************************************************
	 *	Payment Processor Model
	 * 	@param ctx context
	 * 	@param C_PaymentProcessor_ID payment processor
	 */
	public MPaymentProcessor (Properties ctx, int C_PaymentProcessor_ID, String trxName)
	{
		super (ctx, C_PaymentProcessor_ID, trxName);
		if (C_PaymentProcessor_ID == 0)
		{
		//	setC_BankAccount_ID (0);		//	Parent
		//	setUserID (null);
		//	setPassword (null);
		//	setHostAddress (null);
		//	setHostPort (0);
			setCommission (Env.ZERO);
			setAcceptVisa (false);
			setAcceptMC (false);
			setAcceptAMEX (false);
			setAcceptDiners (false);
			setCostPerTrx (Env.ZERO);
			setAcceptCheck (false);
			setRequireVV (false);
			setAcceptCorporate (false);
			setAcceptDiscover (false);
			setAcceptATM (false);
			setAcceptDirectDeposit(false);
			setAcceptDirectDebit(false);
		//	setName (null);
		}
	}	//	MPaymentProcessor

	/**
	 *	Payment Processor Model
	 * 	@param ctx context
	 * 	@param rs result set
	 */
	public MPaymentProcessor (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPaymentProcessor


	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPaymentProcessor[")
			.append(get_ID ()).append("-").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Does Payment Processor accepts tender / CC
	 *	@param TenderType tender type
	 *	@param CreditCardType credit card type
	 *	@return true if acceptes
	 */
	public boolean accepts (String TenderType, String CreditCardType)
	{
		if ((MPayment.TENDERTYPE_DirectDeposit.equals(TenderType) && isAcceptDirectDeposit())
			|| (MPayment.TENDERTYPE_DirectDebit.equals(TenderType) && isAcceptDirectDebit())
			|| (MPayment.TENDERTYPE_Check.equals(TenderType) && isAcceptCheck())
			//
			|| (MPayment.CREDITCARDTYPE_ATM.equals(CreditCardType) && isAcceptATM())
			|| (MPayment.CREDITCARDTYPE_Amex.equals(CreditCardType) && isAcceptAMEX())
			|| (MPayment.CREDITCARDTYPE_PurchaseCard.equals(CreditCardType) && isAcceptCorporate())
			|| (MPayment.CREDITCARDTYPE_Diners.equals(CreditCardType) && isAcceptDiners())
			|| (MPayment.CREDITCARDTYPE_Discover.equals(CreditCardType) && isAcceptDiscover())
			|| (MPayment.CREDITCARDTYPE_MasterCard.equals(CreditCardType) && isAcceptMC())
			|| (MPayment.CREDITCARDTYPE_Visa.equals(CreditCardType) && isAcceptVisa()))
			return true;
		return false;
	}	//	accepts

}	//	MPaymentProcessor
