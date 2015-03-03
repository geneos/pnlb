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
import org.compiere.util.*;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              C_BankStatement (392)
 *  Document Types:     CMB
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Bank.java,v 1.13 2005/10/26 00:40:02 jjanke Exp $
 */
public class Doc_Bank extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@parem trxName trx
	 */
	protected Doc_Bank (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, MBankStatement.class, rs, DOCTYPE_BankStatement, trxName);
	}	//	Doc_Bank
	
	/** Bank Account			*/
	private int			m_C_BankAccount_ID = 0;

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		MBankStatement bs = (MBankStatement)getPO();
		setDateDoc(bs.getStatementDate());
		setDateAcct(bs.getStatementDate());	//	Overwritten on Line Level
		
		m_C_BankAccount_ID = bs.getC_BankAccount_ID();
		//	Amounts
		setAmount(AMTTYPE_Gross, bs.getStatementDifference());

		//  Set Bank Account Info (Currency)
		MBankAccount ba = MBankAccount.get (getCtx(), m_C_BankAccount_ID);
		setC_Currency_ID (ba.getC_Currency_ID());

		//	Contained Objects
		p_lines = loadLines(bs);
		log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 *	Load Invoice Line.
	 *  4 amounts
	 *  AMTTYPE_Payment
	 *  AMTTYPE_Statement2
	 *  AMTTYPE_Charge
	 *  AMTTYPE_Interest
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines(MBankStatement bs)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MBankStatementLine[] lines = bs.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MBankStatementLine line = lines[i];
			DocLine_Bank docLine = new DocLine_Bank(line, this);
			//	Set Date Acct
			if (i == 0)
				setDateAcct(line.getDateAcct());
			MPeriod period = MPeriod.get(getCtx(), line.getDateAcct());
			if (period != null && period.isOpen(DOCTYPE_BankStatement))
				docLine.setC_Period_ID(period.getC_Period_ID());
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
			BigDecimal lineBalance = ((DocLine_Bank)p_lines[i]).getStmtAmt();
			retValue = retValue.subtract(lineBalance);
			sb.append("-").append(lineBalance);
		}
		sb.append("]");
		//
		log.fine(toString() + " Balance=" + retValue + sb.toString());
		return retValue;
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  CMB.
	 *  <pre>
	 *      BankAsset       DR      CR  (Statement)
	 *      BankInTransit   DR      CR              (Payment)
	 *      Charge          DR          (Charge)
	 *      Interest        DR      CR  (Interest)
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);

		//  Header -- there may be different currency amounts

		FactLine fl = null;
		int AD_Org_ID = getBank_Org_ID();	//	Bank Account Org
		//  Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine_Bank line = (DocLine_Bank)p_lines[i];
			int C_BPartner_ID = line.getC_BPartner_ID();
			
			//  BankAsset       DR      CR  (Statement)
			fl = fact.createLine(line,
				getAccount(Doc.ACCTTYPE_BankAsset, as),
				line.getC_Currency_ID(), line.getStmtAmt());
			if (fl != null && AD_Org_ID != 0)
				fl.setAD_Org_ID(AD_Org_ID);
			if (fl != null && C_BPartner_ID != 0)
				fl.setC_BPartner_ID(C_BPartner_ID);
			
			//  BankInTransit   DR      CR              (Payment)
			fl = fact.createLine(line,
				getAccount(Doc.ACCTTYPE_BankInTransit, as),
				line.getC_Currency_ID(), line.getTrxAmt().negate());
			if (fl != null)
			{
				if (C_BPartner_ID != 0)
					fl.setC_BPartner_ID(C_BPartner_ID);
				if (AD_Org_ID != 0)
					fl.setAD_Org_ID(AD_Org_ID);
				else
					fl.setAD_Org_ID(line.getAD_Org_ID(true)); // from payment
			}
			//  Charge          DR          (Charge)
			fl = fact.createLine(line,
				line.getChargeAccount(as, line.getChargeAmt().negate()),
				line.getC_Currency_ID(), line.getChargeAmt().negate(), null);
			if (fl != null && C_BPartner_ID != 0)
				fl.setC_BPartner_ID(C_BPartner_ID);

			//  Interest        DR      CR  (Interest)
			if (line.getInterestAmt().signum() < 0)
				fl = fact.createLine(line,
					getAccount(Doc.ACCTTYPE_InterestExp, as), getAccount(Doc.ACCTTYPE_InterestExp, as),
					line.getC_Currency_ID(), line.getInterestAmt().negate());
			else
				fl = fact.createLine(line,
					getAccount(Doc.ACCTTYPE_InterestRev, as), getAccount(Doc.ACCTTYPE_InterestRev, as),
					line.getC_Currency_ID(), line.getInterestAmt().negate());
			if (fl != null && C_BPartner_ID != 0)
				fl.setC_BPartner_ID(C_BPartner_ID);
			//
		//	fact.createTaxCorrection();
		}
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

	/**
	 * 	Get AD_Org_ID from Bank Account
	 * 	@return AD_Org_ID or 0
	 */
	private int getBank_Org_ID ()
	{
		if (m_C_BankAccount_ID == 0)
			return 0;
		//
		MBankAccount ba = MBankAccount.get(getCtx(), m_C_BankAccount_ID);
		return ba.getAD_Org_ID();
	}	//	getBank_Org_ID

}   //  Doc_Bank
