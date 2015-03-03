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


import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Create AP Invoices for Commission
 *	
 *  @author Jorg Janke
 *  @version $Id: CommissionAPInvoice.java,v 1.12 2005/09/19 04:49:45 jjanke Exp $
 */
public class CommissionAPInvoice extends SvrProcess
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
	 *  @return Message (variables are parsed)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		log.info("doIt - C_CommissionRun_ID=" + getRecord_ID());
		//	Load Data
		MCommissionRun comRun = new MCommissionRun (getCtx(), getRecord_ID(), get_TrxName());
		if (comRun.get_ID() == 0)
			throw new IllegalArgumentException("CommissionAPInvoice - No Commission Run");
		if (Env.ZERO.compareTo(comRun.getGrandTotal()) == 0)
			throw new IllegalArgumentException("@GrandTotal@ = 0");
		MCommission com = new MCommission (getCtx(), comRun.getC_Commission_ID(), get_TrxName());
		if (com.get_ID() == 0)
			throw new IllegalArgumentException("CommissionAPInvoice - No Commission");
		if (com.getC_Charge_ID() == 0)
			throw new IllegalArgumentException("CommissionAPInvoice - No Charge on Commission");
		MBPartner bp = new MBPartner (getCtx(), com.getC_BPartner_ID(), get_TrxName());
		if (bp.get_ID() == 0)
			throw new IllegalArgumentException("CommissionAPInvoice - No BPartner");
			
		//	Create Invoice
		MInvoice invoice = new MInvoice (getCtx(), 0, null);
		invoice.setClientOrg(com.getAD_Client_ID(), com.getAD_Org_ID());
		invoice.setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_APInvoice);	//	API
		invoice.setBPartner(bp);
	//	invoice.setDocumentNo (comRun.getDocumentNo());		//	may cause unique constraint
		invoice.setSalesRep_ID(getAD_User_ID());	//	caller
		//
		if (com.getC_Currency_ID() != invoice.getC_Currency_ID())
			throw new IllegalArgumentException("CommissionAPInvoice - Currency of PO Price List not Commission Currency");
		//		
		if (!invoice.save())
			throw new IllegalStateException("CommissionAPInvoice - cannot save Invoice");
			
 		//	Create Invoice Line
 		MInvoiceLine iLine = new MInvoiceLine(invoice);
		iLine.setC_Charge_ID(com.getC_Charge_ID());
 		iLine.setQty(1);
 		iLine.setPrice(comRun.getGrandTotal());
		iLine.setTax();
		if (!iLine.save())
			throw new IllegalStateException("CommissionAPInvoice - cannot save Invoice Line");
		//
		return "@C_Invoice_ID@ = " + invoice.getDocumentNo();
	}	//	doIt

}	//	CommissionAPInvoice
