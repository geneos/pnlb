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
package org.compiere.impexp;

/**
 *	Bank Statement Match Information.
 *	Returned by Bank Statement Matcher	
 *	
 *  @author Jorg Janke
 *  @version $Id: BankStatementMatchInfo.java,v 1.3 2005/03/11 20:26:10 jjanke Exp $
 */
public class BankStatementMatchInfo
{
	/**
	 * 	Standard Constructor
	 */
	public BankStatementMatchInfo()
	{
		super();
	}	//	BankStatementMatchInfo


	private int m_C_BPartner_ID = 0;
	private int m_C_Payment_ID = 0;
	private int m_C_Invoice_ID = 0;


	/**
	 * 	Do we have a match?
	 *	@return true if something could be matched
	 */
	public boolean isMatched()
	{
		return m_C_BPartner_ID > 0 || m_C_Payment_ID > 0 || m_C_Invoice_ID > 0;
	}	//	isValid


	/**
	 *	Get matched BPartner
	 * 	@return BPartner
	 */
	public int getC_BPartner_ID() 
	{
		return m_C_BPartner_ID;
	}
	/**
	 * 	Set matched BPartner
	 * 	@param C_BPartner_ID BPartner
	 */
	public void setC_BPartner_ID (int C_BPartner_ID) 
	{
		m_C_BPartner_ID = C_BPartner_ID;
	}
	
	/**
	 *	Get matched Payment
	 *	@return Payment
	 */
	public int getC_Payment_ID() 
	{
		return m_C_Payment_ID;
	}
	/**
	 *	Set matched Payment
	 *	@param C_Payment_ID payment
	 */
	public void setC_Payment_ID (int C_Payment_ID) 
	{
		m_C_Payment_ID = C_Payment_ID;
	}
	
	/**
	 *	Get matched Invoice
	 *	@return invoice
	 */
	public int getC_Invoice_ID() 
	{
		return m_C_Invoice_ID;
	}
	/**
	 * 	Set matched Invoice
	 *	@param C_Invoice_ID invoice
	 */
	public void setC_Invoice_ID (int C_Invoice_ID) 
	{
		m_C_Invoice_ID = C_Invoice_ID;
	}
	
}	//	BankStatementMatchInfo
