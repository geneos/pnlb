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
package org.compiere.acct;

import java.math.*;
import java.sql.*;
import java.util.*;

import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              C_Cash (407)
 *  Document Types:     CMC
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Cash.java,v 1.7 2005/10/26 00:40:02 jjanke Exp $
 */
public class Doc_Cash extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@parem trxName trx
	 */
	protected Doc_Cash (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super(ass, MCash.class, rs, DOCTYPE_CashJournal, trxName);
	}	//	Doc_Cash

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		MCash cash = (MCash)getPO();
		setDateDoc(cash.getStatementDate());

		//	Amounts
		setAmount(Doc.AMTTYPE_Gross, cash.getStatementDifference());

		//  Set CashBook Org & Currency
		MCashBook cb = MCashBook.get(getCtx(), cash.getC_CashBook_ID());
		setC_CashBook_ID(cb.getC_CashBook_ID());
		setC_Currency_ID(cb.getC_Currency_ID());

		//	Contained Objects
		p_lines = loadLines(cash, cb);
		log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails


	/**
	 *	Load Cash Line
	 *	@param cash journal
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines(MCash cash, MCashBook cb)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MCashLine[] lines = cash.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MCashLine line = lines[i];
			DocLine_Cash docLine = new DocLine_Cash (line, this);
			//
			list.add(docLine);
		}

		//	Return Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);
		return dls;
	}	//	loadLines

	
	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line amounts from total - no rounding
	 *  @return positive amount, if total invoice is bigger than lines
	 */
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;
		StringBuffer sb = new StringBuffer (" [");
		//  Total
		retValue = retValue.add(getAmount(Doc.AMTTYPE_Gross));
		sb.append(getAmount(Doc.AMTTYPE_Gross));
		//  - Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			retValue = retValue.subtract(p_lines[i].getAmtSource());
			sb.append("-").append(p_lines[i].getAmtSource());
		}
		sb.append("]");
		//
		log.fine(toString() + " Balance=" + retValue + sb.toString());
	//	return retValue;
		return Env.ZERO;    //  Lines are balanced
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  CMC.
	 *  <pre>
	 *  Expense
	 *          CashExpense     DR
	 *          CashAsset               CR
	 *  Receipt
	 *          CashAsset       DR
	 *          CashReceipt             CR
	 *  Charge
	 *          Charge          DR
	 *          CashAsset               CR
	 *  Difference
	 *          CashDifference  DR
	 *          CashAsset               CR
	 *  Invoice
	 *          CashAsset       DR
	 *          CashTransfer            CR
	 *  Transfer
	 *          BankInTransit   DR
	 *          CashAsset               CR
	 *  </pre>
	 *  @param as account schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		//  Need to have CashBook
		if (getC_CashBook_ID() == 0)
		{
			p_Error = "C_CashBook_ID not set";
			log.log(Level.SEVERE, p_Error);
			return null;
		}

		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);

		//  Header posting amt as Invoices and Transfer could be differenet currency
		//  CashAsset Total
		BigDecimal assetAmt = Env.ZERO;

		//  Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine_Cash line = (DocLine_Cash)p_lines[i];
			String CashType = line.getCashType();

			if (CashType.equals(DocLine_Cash.CASHTYPE_EXPENSE))
			{   //  amount is negative
				//  CashExpense     DR
				//  CashAsset               CR
				fact.createLine(line, getAccount(Doc.ACCTTYPE_CashExpense, as),
					getC_Currency_ID(), line.getAmount().negate(), null);
			//	fact.createLine(line, getAccount(Doc.ACCTTYPE_CashAsset, as),
			//		p_vo.C_Currency_ID, null, line.getAmount().negate());
				assetAmt = assetAmt.subtract(line.getAmount().negate());
			}
			else if (CashType.equals(DocLine_Cash.CASHTYPE_RECEIPT))
			{   //  amount is positive
				//  CashAsset       DR
				//  CashReceipt             CR
			//	fact.createLine(line, getAccount(Doc.ACCTTYPE_CashAsset, as),
			//		p_vo.C_Currency_ID, line.getAmount(), null);
				assetAmt = assetAmt.add(line.getAmount());
				fact.createLine(line, getAccount(Doc.ACCTTYPE_CashReceipt, as),
					getC_Currency_ID(), null, line.getAmount());
			}
			else if (CashType.equals(DocLine_Cash.CASHTYPE_CHARGE))
			{   //  amount is negative
				//  Charge          DR
				//  CashAsset               CR
				fact.createLine(line, line.getChargeAccount(as, getAmount()),
					getC_Currency_ID(), line.getAmount().negate(), null);
			//	fact.createLine(line, getAccount(Doc.ACCTTYPE_CashAsset, as),
			//		p_vo.C_Currency_ID, null, line.getAmount().negate());
				assetAmt = assetAmt.subtract(line.getAmount().negate());
			}
			else if (CashType.equals(DocLine_Cash.CASHTYPE_DIFFERENCE))
			{   //  amount is pos/neg
				//  CashDifference  DR
				//  CashAsset               CR
				fact.createLine(line, getAccount(Doc.ACCTTYPE_CashDifference, as),
					getC_Currency_ID(), line.getAmount().negate());
			//	fact.createLine(line, getAccount(Doc.ACCTTYPE_CashAsset, as),
			//		p_vo.C_Currency_ID, line.getAmount());
				assetAmt = assetAmt.add(line.getAmount());
			}
			else if (CashType.equals(DocLine_Cash.CASHTYPE_INVOICE))
			{   //  amount is pos/neg
				//  CashAsset       DR      dr      --   Invoice is in Invoice Currency !
				//  CashTransfer    cr      CR
				if (line.getC_Currency_ID() == getC_Currency_ID())
					assetAmt = assetAmt.add (line.getAmount());
				else
					fact.createLine(line,
						getAccount(Doc.ACCTTYPE_CashAsset, as),
						line.getC_Currency_ID(), line.getAmount());
				fact.createLine(line,
					getAccount(Doc.ACCTTYPE_CashTransfer, as),
					line.getC_Currency_ID(), line.getAmount().negate());
			}
			else if (CashType.equals(DocLine_Cash.CASHTYPE_TRANSFER))
			{   //  amount is pos/neg
				//  BankInTransit   DR      dr      --  Transfer is in Bank Account Currency
				//  CashAsset       dr      CR
				int temp = getC_BankAccount_ID();
				setC_BankAccount_ID (line.getC_BankAccount_ID());
				fact.createLine(line,
					getAccount(Doc.ACCTTYPE_BankInTransit, as),
					line.getC_Currency_ID(), line.getAmount().negate());
				setC_BankAccount_ID(temp);
				if (line.getC_Currency_ID() == getC_Currency_ID())
					assetAmt = assetAmt.add (line.getAmount());
				else
					fact.createLine(line,
						getAccount(Doc.ACCTTYPE_CashAsset, as),
						line.getC_Currency_ID(), line.getAmount());
			}
		}	//  lines

		//  Cash Asset
		fact.createLine(null, getAccount(Doc.ACCTTYPE_CashAsset, as),
			getC_Currency_ID(), assetAmt);

		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

}   //  Doc_Cash
