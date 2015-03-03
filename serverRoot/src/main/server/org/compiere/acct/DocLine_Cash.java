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

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Cash Journal Line
 *
 *  @author Jorg Janke
 *  @version  $Id: DocLine_Cash.java,v 1.4 2005/10/17 23:43:52 jjanke Exp $
 */
public class DocLine_Cash extends DocLine
{
	/**
	 *  Constructor
	 *  @param line cash line
	 *  @param doc header
	 */
	public DocLine_Cash (MCashLine line, Doc_Cash doc)
	{
		super (line, doc);
		m_CashType = line.getCashType();
		m_C_BankAccount_ID = line.getC_BankAccount_ID();
		m_C_Invoice_ID = line.getC_Invoice_ID();
		//
		if (m_C_Invoice_ID != 0)
		{
			MInvoice invoice = MInvoice.get(line.getCtx(), m_C_Invoice_ID);
			setC_BPartner_ID(invoice.getC_BPartner_ID());
		}

		//
		m_Amount = line.getAmount();
		m_DiscountAmt = line.getDiscountAmt();
		m_WriteOffAmt = line.getWriteOffAmt();
		setAmount(m_Amount);


	}   //  DocLine_Cash

	/** Cash Type               */
	private String  m_CashType = "";

	//  AD_Reference_ID=217
	public static final String  CASHTYPE_CHARGE = "C";
	public static final String  CASHTYPE_DIFFERENCE = "D";
	public static final String  CASHTYPE_EXPENSE = "E";
	public static final String  CASHTYPE_INVOICE = "I";
	public static final String  CASHTYPE_RECEIPT = "R";
	public static final String  CASHTYPE_TRANSFER = "T";

	//  References
	private int     m_C_BankAccount_ID = 0;
	private int     m_C_Invoice_ID = 0;

	//  Amounts
	private BigDecimal      m_Amount = Env.ZERO;
	private BigDecimal      m_DiscountAmt = Env.ZERO;
	private BigDecimal      m_WriteOffAmt = Env.ZERO;


	/**
	 *  Get Cash Type
	 *  @return cash type
	 */
	public String getCashType()
	{
		return m_CashType;
	}   //  getCashType

	/**
	 *  Get Bank Account
	 *  @return Bank Account
	 */
	public int getC_BankAccount_ID()
	{
		return m_C_BankAccount_ID;
	}   //  getC_BankAccount_ID

	/**
	 *  Get Invoice
	 *  @return C_Invoice_ID
	 */
	public int getC_Invoice_ID()
	{
		return m_C_Invoice_ID;
	}   //  getC_Invoice_ID

	/**
	 *  Get Amount
	 *  @return Payment Amount
	 */
	public BigDecimal getAmount()
	{
		return m_Amount;
	}
	/**
	 *  Get Discount
	 *  @return Discount Amount
	 */
	public BigDecimal getDiscountAmt()
	{
		return m_DiscountAmt;
	}
	/**
	 *  Get WriteOff
	 *  @return Write-Off Amount
	 */
	public BigDecimal getWriteOffAmt()
	{
		return m_WriteOffAmt;
	}

}   //  DocLine_Cash
