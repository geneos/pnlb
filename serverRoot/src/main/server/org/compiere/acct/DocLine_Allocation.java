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
 *	Allocation Line
 *	
 *  @author Jorg Janke
 *  @version $Id: DocLine_Allocation.java,v 1.5 2005/10/17 23:43:52 jjanke Exp $
 */
public class DocLine_Allocation extends DocLine
{
	/**
	 * 	DocLine_Allocation
	 *	@param line allocation line
	 *	@param doc header
	 */
	public DocLine_Allocation (MAllocationLine line, Doc doc)
	{
		super (line, doc);
		m_C_Payment_ID = line.getC_Payment_ID();
		m_C_CashLine_ID = line.getC_CashLine_ID();
		m_C_Invoice_ID = line.getC_Invoice_ID();
		m_C_Order_ID = line.getC_Order_ID();
		//
		setAmount(line.getAmount());
		m_DiscountAmt = line.getDiscountAmt();
		m_WriteOffAmt = line.getWriteOffAmt();
		m_OverUnderAmt = line.getOverUnderAmt();
	}	//	DocLine_Allocation

	private int 		m_C_Invoice_ID;
	private int 		m_C_Payment_ID;
	private int 		m_C_CashLine_ID;
	private int 		m_C_Order_ID;
	private BigDecimal	m_DiscountAmt; 
	private BigDecimal	m_WriteOffAmt; 
	private BigDecimal	m_OverUnderAmt; 
	
	/**
	 * 	Get Invoice C_Currency_ID
	 *	@return 0 if no invoice -1 if not found
	 */
	public int getInvoiceC_Currency_ID()
	{
		if (m_C_Invoice_ID == 0)
			return 0;
		String sql = "SELECT C_Currency_ID "
			+ "FROM C_Invoice "
			+ "WHERE C_Invoice_ID=?";
		return  DB.getSQLValue(null, sql, m_C_Invoice_ID);
	}	//	getInvoiceC_Currency_ID

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("DocLine_Allocation[");
		sb.append(get_ID())
			.append(",Amt=").append(getAmtSource())
			.append(",Discount=").append(getDiscountAmt())
			.append(",WriteOff=").append(getWriteOffAmt())
			.append(",OverUnderAmt=").append(getOverUnderAmt())
			.append(" - C_Payment_ID=").append(m_C_Payment_ID)
			.append(",C_CashLine_ID=").append(m_C_CashLine_ID)
			.append(",C_Invoice_ID=").append(m_C_Invoice_ID)
			.append("]");
		return sb.toString ();
	}	//	toString
	
	
	/**
	 * @return Returns the c_Order_ID.
	 */
	public int getC_Order_ID ()
	{
		return m_C_Order_ID;
	}
	/**
	 * @return Returns the discountAmt.
	 */
	public BigDecimal getDiscountAmt ()
	{
		return m_DiscountAmt;
	}
	/**
	 * @return Returns the overUnderAmt.
	 */
	public BigDecimal getOverUnderAmt ()
	{
		return m_OverUnderAmt;
	}
	/**
	 * @return Returns the writeOffAmt.
	 */
	public BigDecimal getWriteOffAmt ()
	{
		return m_WriteOffAmt;
	}
	/**
	 * @return Returns the c_CashLine_ID.
	 */
	public int getC_CashLine_ID ()
	{
		return m_C_CashLine_ID;
	}
	/**
	 * @return Returns the c_Invoice_ID.
	 */
	public int getC_Invoice_ID ()
	{
		return m_C_Invoice_ID;
	}
	/**
	 * @return Returns the c_Payment_ID.
	 */
	public int getC_Payment_ID ()
	{
		return m_C_Payment_ID;
	}
}	//	DocLine_Allocation
