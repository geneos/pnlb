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
import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Document Tax Line
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: DocTax.java,v 1.5 2005/10/26 00:40:02 jjanke Exp $
 */
public final class DocTax
{
	/**
	 *	Create Tax
	 *  @param C_Tax_ID tax
	 *  @param name name
	 *  @param rate rate
	 *  @param taxBaseAmt tax base amount
	 *  @param amount amount
	 * 	@oaram salesTax sales tax flag
	 */
	public DocTax (int C_Tax_ID, String name, BigDecimal rate, 
		BigDecimal taxBaseAmt, BigDecimal amount, boolean salesTax)
	{
		m_C_Tax_ID = C_Tax_ID;
		m_name = name;
		m_rate = rate;
		m_amount = amount;
		m_salesTax = salesTax;
	}	//	DocTax

	/** Tax ID              */
	private int			m_C_Tax_ID = 0;
	/** Amount              */
	private BigDecimal 	m_amount = null;
	/** Tax Rate            */
	private BigDecimal 	m_rate = null;
	/** Name                */
	private String 		m_name = null;
	/** Base Tax Amt        */
	private BigDecimal  m_taxBaseAmt = null;
	/** Included Tax		*/
	private BigDecimal	m_includedTax = Env.ZERO;
	/** Sales Tax			*/
	private boolean		m_salesTax = false;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(DocTax.class);
	

	/** Tax Due Acct        */
	public static final int    ACCTTYPE_TaxDue = 0;
	/** Tax Liability       */
	public static final int    ACCTTYPE_TaxLiability = 1;
	/** Tax Credit          */
	public static final int    ACCTTYPE_TaxCredit = 2;
	/** Tax Receivables     */
	public static final int    ACCTTYPE_TaxReceivables = 3;
	/** Tax Expense         */
	public static final int    ACCTTYPE_TaxExpense = 4;

	/**
	 *	Get Account
	 *  @param AcctType see ACCTTYPE_*
	 *  @param as account schema
	 *  @return Account
	 */
	public MAccount getAccount (int AcctType, MAcctSchema as)
	{
		if (AcctType < 0 || AcctType > 4)
			return null;
		//
		String sql = "SELECT T_Due_Acct, T_Liability_Acct, T_Credit_Acct, T_Receivables_Acct, T_Expense_Acct "
			+ "FROM C_Tax_Acct WHERE C_Tax_ID=? AND C_AcctSchema_ID=?";
		int validCombination_ID = 0;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_C_Tax_ID);
			pstmt.setInt(2, as.getC_AcctSchema_ID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				validCombination_ID = rs.getInt(AcctType+1);    //  1..5
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		if (validCombination_ID == 0)
			return null;
		return MAccount.get(as.getCtx(), validCombination_ID);
	}   //  getAccount

	/**
	 *	Get Amount
	 *  @return gross amount
	 */
	public BigDecimal getAmount()
	{
		return m_amount;
	}

	/**
	 *  Get Base Amount
	 *  @return net amount
	 */
	public BigDecimal getTaxBaseAmt()
	{
		return m_taxBaseAmt;
	}

	/**
	 *	Get Rate
	 *  @return tax rate in percent
	 */
	public BigDecimal getRate()
	{
		return m_rate;
	}

	/**
	 *  Get Name of Tax
	 *  @return name
	 */
	public String getName()
	{
		return m_name;
	}

	/**
	 * 	Get C_Tax_ID
	 *	@return tax id
	 */
	public int getC_Tax_ID()
	{
		return m_C_Tax_ID;
	}	//	getC_Tax_ID
	
	/**
	 *  Get Description (Tax Name and Base Amount)
	 *  @return tax anme and base amount
	 */
	public String getDescription()
	{
		return m_name + " " + m_taxBaseAmt.toString();
	}   //  getDescription

	/**
	 * 	Add to Included Tax
	 *	@param amt amount
	 */
	public void addIncludedTax (BigDecimal amt)
	{
		m_includedTax = m_includedTax.add(amt);
	}	//	addIncludedTax
	
	/**
	 * 	Get Included Tax
	 *	@return tax amount
	 */
	public BigDecimal getIncludedTax()
	{
		return m_includedTax;
	}	//	getIncludedTax
	
	/**
	 * 	Get Included Tax Difference
	 *	@return tax ampunt - included amount
	 */
	public BigDecimal getIncludedTaxDifference()
	{
		return m_amount.subtract(m_includedTax);
	}	//	getIncludedTaxDifference
	
	/**
	 * 	Included Tax differs from tax amount
	 *	@return true if difference
	 */
	public boolean isIncludedTaxDifference()
	{
		return Env.ZERO.compareTo(getIncludedTaxDifference()) != 0;
	}	//	isIncludedTaxDifference
	
	/**
	 * 	Get AP Tax Type
	 *	@return AP tax type (Credit or Expense) 
	 */
	public int getAPTaxType()
	{
		if (isSalesTax())
			return ACCTTYPE_TaxExpense;
		return ACCTTYPE_TaxCredit;
	}	//	getAPTaxAcctType

	/**
	 * 	Is Sales Tax
	 *	@return sales tax
	 */
	public boolean isSalesTax()
	{
		return m_salesTax;
	}	//	isSalesTax
	
	
	/**
	 *	Return String representation
	 *  @return tax anme and base amount
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("Tax=(");
		sb.append(m_name);
		sb.append(" Amt=").append(m_amount);
		sb.append(")");
		return sb.toString();
	}	//	toString

}	//	DocTax
