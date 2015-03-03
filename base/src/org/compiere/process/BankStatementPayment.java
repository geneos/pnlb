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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Create Payment from Bank Statement Info
 *	
 *  @author Jorg Janke
 *  @version $Id: BankStatementPayment.java,v 1.11 2005/11/28 03:36:02 jjanke Exp $
 */
public class BankStatementPayment extends SvrProcess
{

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		int Table_ID = getTable_ID();
		int Record_ID = getRecord_ID();
		log.info ("Table_ID=" + Table_ID + ", Record_ID=" + Record_ID);
		
		if (Table_ID == X_I_BankStatement.getTableId(X_I_BankStatement.Table_Name))
			return createPayment (new X_I_BankStatement(getCtx(), Record_ID, get_TrxName()));
		else if (Table_ID == MBankStatementLine.getTableId(MBankStatementLine.Table_Name))
			return createPayment (new MBankStatementLine(getCtx(), Record_ID, get_TrxName()));
		
		throw new CompiereSystemError("??");
	}	//	doIt

	/**
	 * 	Create Payment for Import
	 *	@param ibs import bank statement
	 *	@return Message
	 */
	private String createPayment (X_I_BankStatement ibs) throws Exception
	{
		if (ibs == null || ibs.getC_Payment_ID() != 0)
			return "--";
		log.fine(ibs.toString());
		if (ibs.getC_Invoice_ID() == 0 && ibs.getC_BPartner_ID() == 0)
			throw new CompiereUserError ("@NotFound@ @C_Invoice_ID@ / @C_BPartner_ID@");
		if (ibs.getC_BankAccount_ID() == 0)
			throw new CompiereUserError ("@NotFound@ @C_BankAccount_ID@");
		//
		MPayment payment = createPayment (ibs.getC_Invoice_ID(), ibs.getC_BPartner_ID(),
			ibs.getC_Currency_ID(), ibs.getStmtAmt(), ibs.getTrxAmt(), 
			ibs.getC_BankAccount_ID(), ibs.getStatementLineDate() == null ? ibs.getStatementDate() : ibs.getStatementLineDate(), 
			ibs.getDateAcct(), ibs.getDescription(), ibs.getAD_Org_ID());
		if (payment == null)
			throw new CompiereSystemError("Could not create Payment");
		
		ibs.setC_Payment_ID(payment.getC_Payment_ID());
		ibs.setC_Currency_ID (payment.getC_Currency_ID());
		ibs.setTrxAmt(payment.getPayAmt());
		ibs.save();
		//
		String retString = "@C_Payment_ID@ = " + payment.getDocumentNo();
		if (payment.getOverUnderAmt().signum() != 0)
			retString += " - @OverUnderAmt@=" + payment.getOverUnderAmt();
		return retString;
	}	//	createPayment - Import
	
	/**
	 * 	Create Payment for BankStatement
	 *	@param bsl bank statement Line
	 *	@return Message
	 */
	private String createPayment (MBankStatementLine bsl) throws Exception
	{
		if (bsl == null || bsl.getC_Payment_ID() != 0)
			return "--";
		log.fine(bsl.toString());
		if (bsl.getC_Invoice_ID() == 0 && bsl.getC_BPartner_ID() == 0)
			throw new CompiereUserError ("@NotFound@ @C_Invoice_ID@ / @C_BPartner_ID@");
		//
		MBankStatement bs = new MBankStatement (getCtx(), bsl.getC_BankStatement_ID(), get_TrxName());
		//
		MPayment payment = createPayment (bsl.getC_Invoice_ID(), bsl.getC_BPartner_ID(),
			bsl.getC_Currency_ID(), bsl.getStmtAmt(), bsl.getTrxAmt(), 
			bs.getC_BankAccount_ID(), bsl.getStatementLineDate(), bsl.getDateAcct(),
			bsl.getDescription(), bsl.getAD_Org_ID());
		if (payment == null)
			throw new CompiereSystemError("Could not create Payment");
		//	update statement
		bsl.setPayment(payment);
		bsl.save();
		//
		String retString = "@C_Payment_ID@ = " + payment.getDocumentNo();
		if (payment.getOverUnderAmt().signum() != 0)
			retString += " - @OverUnderAmt@=" + payment.getOverUnderAmt();
		return retString;
	}	//	createPayment


	/**
	 * 	Create actual Payment
	 *	@param C_Invoice_ID invoice
	 *	@param C_BPartner_ID partner ignored when invoice exists
	 *	@param C_Currency_ID currency
	 *	@param StmtAmt statement amount
	 *	@param TrxAmt transaction amt
	 *	@return payment
	 */
	private MPayment createPayment (int C_Invoice_ID, int C_BPartner_ID, 
		int C_Currency_ID, BigDecimal StmtAmt, BigDecimal TrxAmt,
		int C_BankAccount_ID, Timestamp DateTrx, Timestamp DateAcct, 
		String Description, int AD_Org_ID)
	{
		//	Trx Amount = Payment overwrites Statement Amount if defined
		BigDecimal PayAmt = TrxAmt;
		if (PayAmt == null || Env.ZERO.compareTo(PayAmt) == 0)
			PayAmt = StmtAmt;
		if (C_Invoice_ID == 0
			&& (PayAmt == null || Env.ZERO.compareTo(PayAmt) == 0))
			throw new IllegalStateException ("@PayAmt@ = 0");
		if (PayAmt == null)
			PayAmt = Env.ZERO;
		//
		MPayment payment = new MPayment (getCtx(), 0, get_TrxName());
		payment.setAD_Org_ID(AD_Org_ID);
		payment.setC_BankAccount_ID(C_BankAccount_ID);
		payment.setTenderType(MPayment.TENDERTYPE_Check);
		if (DateTrx != null)
			payment.setDateTrx(DateTrx);
		else if (DateAcct != null)
			payment.setDateTrx(DateAcct);
		if (DateAcct != null)
			payment.setDateAcct(DateAcct);
		else
			payment.setDateAcct(payment.getDateTrx());
		payment.setDescription(Description);
		//
		if (C_Invoice_ID != 0)
		{
			MInvoice invoice = new MInvoice (getCtx(), C_Invoice_ID, null);
			payment.setC_DocType_ID(invoice.isSOTrx());		//	Receipt
			payment.setC_Invoice_ID(invoice.getC_Invoice_ID());
			payment.setC_BPartner_ID (invoice.getC_BPartner_ID());
			if (PayAmt.signum() != 0)	//	explicit Amount
			{
				payment.setC_Currency_ID(C_Currency_ID);
				if (invoice.isSOTrx())
					payment.setPayAmt(PayAmt);
				else	//	payment is likely to be negative
					payment.setPayAmt(PayAmt.negate());
				payment.setOverUnderAmt(invoice.getGrandTotal(true).subtract(payment.getPayAmt()));
			}
			else	// set Pay Amout from Invoice
			{
				payment.setC_Currency_ID(invoice.getC_Currency_ID());
				payment.setPayAmt(invoice.getGrandTotal(true));
			}
		}
		else if (C_BPartner_ID != 0)
		{
			payment.setC_BPartner_ID(C_BPartner_ID);
			payment.setC_Currency_ID(C_Currency_ID);
			if (PayAmt.signum() < 0)	//	Payment
			{
				payment.setPayAmt(PayAmt.abs());
				payment.setC_DocType_ID(false);
			}
			else	//	Receipt
			{
				payment.setPayAmt(PayAmt);
				payment.setC_DocType_ID(true);
			}
		}
		else
			return null;
		payment.save();
		//
		payment.processIt(MPayment.DOCACTION_Complete);
		payment.save();
		return payment;		
	}	//	createPayment

}	//	BankStatementPayment
