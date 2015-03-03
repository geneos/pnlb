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

import java.sql.*;
import java.util.*;

import org.compiere.util.*;

/**
 * 	Tax Declaration Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MTaxDeclarationLine.java,v 1.3 2005/11/25 21:57:24 jjanke Exp $
 */
public class MTaxDeclarationLine extends X_C_TaxDeclarationLine
{
	/**
	 * 	Standard Constructor
	 *	@param ctx ctx
	 *	@param C_TaxDeclarationLine_ID id
	 *	@param trxName trx
	 */
	public MTaxDeclarationLine (Properties ctx, int C_TaxDeclarationLine_ID, String trxName)
	{
		super (ctx, C_TaxDeclarationLine_ID, trxName);
		if (C_TaxDeclarationLine_ID == 0)
		{
			setIsManual(true);
			setTaxAmt (Env.ZERO);
			setTaxBaseAmt (Env.ZERO);
		}
	}	//	MTaxDeclarationLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs rs
	 *	@param trxName trx
	 */
	public MTaxDeclarationLine (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MTaxDeclarationLine

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param invoice invoice
	 *	@param iLine invoice line
	 */
	public MTaxDeclarationLine (MTaxDeclaration parent, MInvoice invoice, MInvoiceLine iLine)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(invoice);
		setC_TaxDeclaration_ID(parent.getC_TaxDeclaration_ID());
		setIsManual(false);
		//
		setC_Invoice_ID(invoice.getC_Invoice_ID());
		setC_BPartner_ID (invoice.getC_BPartner_ID());
		setC_Currency_ID (invoice.getC_Currency_ID());
		setDateAcct (invoice.getDateAcct());
		//
		setC_InvoiceLine_ID(iLine.getC_InvoiceLine_ID());
		setC_Tax_ID (iLine.getC_Tax_ID());
		if (invoice.isTaxIncluded())
		{
			setTaxBaseAmt (iLine.getLineNetAmt());
			setTaxAmt (iLine.getTaxAmt());
		}
		else
		{
			setTaxBaseAmt (iLine.getLineNetAmt());
			setTaxAmt (iLine.getTaxAmt());
		}
	}	//	MTaxDeclarationLine
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param invoice invoice
	 *	@param tLine tax line
	 */
	public MTaxDeclarationLine (MTaxDeclaration parent, MInvoice invoice, MInvoiceTax tLine)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(invoice);
		setC_TaxDeclaration_ID(parent.getC_TaxDeclaration_ID());
		setIsManual(false);
		//
		setC_Invoice_ID(invoice.getC_Invoice_ID());
		setC_BPartner_ID (invoice.getC_BPartner_ID());
		setC_Currency_ID (invoice.getC_Currency_ID());
		setDateAcct (invoice.getDateAcct());
		//
		setC_Tax_ID (tLine.getC_Tax_ID());
		setTaxBaseAmt (tLine.getTaxBaseAmt());
		setTaxAmt (tLine.getTaxAmt());
	}	//	MTaxDeclarationLine
	

}	//	MTaxDeclarationLine
