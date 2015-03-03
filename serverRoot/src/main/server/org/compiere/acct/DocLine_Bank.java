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
//import org.compiere.model.*;

/**
 *  Bank Statement Line
 *
 *  @author Jorg Janke
 *  @version  $Id: DocLine_Bank.java,v 1.6 2005/10/17 23:43:52 jjanke Exp $
 */
public class DocLine_Bank extends DocLine
{
	/**
	 *  Constructor
	 *  @param line statement line
	 *  @param doc header
	 */
	public DocLine_Bank (MBankStatementLine line, Doc_Bank doc)
	{
		super (line, doc);
		m_C_Payment_ID = line.getC_Payment_ID();
		m_IsReversal = line.isReversal();
		//
		m_StmtAmt = line.getStmtAmt();
		m_InterestAmt = line.getInterestAmt();
		m_TrxAmt = line.getTrxAmt();
		//
		setDateDoc(line.getValutaDate());
		setC_BPartner_ID(line.getC_BPartner_ID());
	}   //  DocLine_Bank

	/** Reversal Flag			*/
	private boolean     m_IsReversal = false;
	/** Payment					*/
	private int         m_C_Payment_ID = 0;

	private BigDecimal  m_TrxAmt = Env.ZERO;
	private BigDecimal  m_StmtAmt = Env.ZERO;
	private BigDecimal  m_InterestAmt = Env.ZERO;

	/**
	 *  Get Payment
	 *  @return C_Paymnet_ID
	 */
	public int getC_Payment_ID()
	{
		return m_C_Payment_ID;
	}   //  getC_Payment_ID

	/**
	 * 	Get AD_Org_ID
	 * 	@param payment if true get Org from payment
	 *	@return org
	 */
	public int getAD_Org_ID (boolean payment)
	{
		if (payment && getC_Payment_ID() != 0)
		{
			String sql = "SELECT AD_Org_ID FROM C_Payment WHERE C_Payment_ID=?";
			int id = DB.getSQLValue(null, sql, getC_Payment_ID());
			if (id > 0)
				return id;
		}
		return super.getAD_Org_ID();
	}	//	getAD_Org_ID
	
	/**
	 *  Is Reversal
	 *  @return true if reversal
	 */
	public boolean isReversal()
	{
		return m_IsReversal;
	}   //  isReversal

	/**
	 *  Get Interest
	 *  @return InterestAmount
	 */
	public BigDecimal getInterestAmt()
	{
		return m_InterestAmt;
	}   //  getInterestAmt

	/**
	 *  Get Statement
	 *  @return Starement Amount
	 */
	public BigDecimal getStmtAmt()
	{
		return m_StmtAmt;
	}   //  getStrmtAmt

	/**
	 *  Get Transaction
	 *  @return transaction amount
	 */
	public BigDecimal getTrxAmt()
	{
		return m_TrxAmt;
	}   //  getTrxAmt

}   //  DocLine_Bank
