/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import org.compiere.util.*;


/**
 *	Invoice Batch Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInvoiceBatchLine.java,v 1.3 2005/09/03 01:57:11 jjanke Exp $
 */
public class MInvoiceBatchLine extends X_C_InvoiceBatchLine
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_InvoiceBatchLine_ID id
	 *	@param trxName trx
	 */
	public MInvoiceBatchLine (Properties ctx, int C_InvoiceBatchLine_ID,
		String trxName)
	{
		super (ctx, C_InvoiceBatchLine_ID, trxName);
		if (C_InvoiceBatchLine_ID == 0)
		{
		//	setC_InvoiceBatch_ID (0);
			/**
			setC_BPartner_ID (0);
			setC_BPartner_Location_ID (0);
			setC_Charge_ID (0);
			setC_DocType_ID (0);	// @C_DocType_ID@
			setC_Tax_ID (0);
			setDocumentNo (null);
			setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_InvoiceBatchLine WHERE C_InvoiceBatch_ID=@C_InvoiceBatch_ID@
			**/
			setDateAcct (new Timestamp(System.currentTimeMillis()));	// @DateDoc@
			setDateInvoiced (new Timestamp(System.currentTimeMillis()));	// @DateDoc@
			setIsTaxIncluded (false);
			setLineNetAmt (Env.ZERO);
			setLineTotalAmt (Env.ZERO);
			setPriceEntered (Env.ZERO);
			setQtyEntered (Env.ONE);	// 1
			setTaxAmt (Env.ZERO);
			setProcessed (false);
		}
	}	//	MInvoiceBatchLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MInvoiceBatchLine (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MInvoiceBatchLine
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		// Amount
		if (getPriceEntered().signum() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "PriceEntered"));
			return false;
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save.
	 * 	Update Header
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (success)
		{
			String sql = "UPDATE C_InvoiceBatch h "
				+ "SET DocumentAmt = NVL((SELECT SUM(LineTotalAmt) FROM C_InvoiceBatchLine l "
					+ "WHERE h.C_InvoiceBatch_ID=l.C_InvoiceBatch_ID AND l.IsActive='Y'),0) "
				+ "WHERE C_InvoiceBatch_ID=" + getC_InvoiceBatch_ID();
			DB.executeUpdate(sql, get_TrxName());
		}
		return success;
	}	//	afterSave
	
}	//	MInvoiceBatchLine
