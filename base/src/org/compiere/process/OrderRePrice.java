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

import java.math.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Re-Price Order or Invoice
 *	
 *  @author Jorg Janke
 *  @version $Id: OrderRePrice.java,v 1.10 2005/03/11 20:25:58 jjanke Exp $
 */
public class OrderRePrice extends SvrProcess
{
	/**	Order to re-price		*/
	private int 	p_C_Order_ID = 0;
	/** Invoice to re-price		*/
	private int 	p_C_Invoice_ID = 0;
	
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
			else if (name.equals("C_Order_ID"))
				p_C_Order_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("C_Invoice_ID"))
				p_C_Invoice_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		log.info("C_Order_ID=" + p_C_Order_ID + ", C_Invoice_ID=" + p_C_Invoice_ID);
		if (p_C_Order_ID == 0 && p_C_Invoice_ID == 0)
			throw new IllegalArgumentException("Nothing to do");

		String retValue = "";
		if (p_C_Order_ID != 0)
		{
			MOrder order = new MOrder (getCtx(), p_C_Order_ID, get_TrxName());
			BigDecimal oldPrice = order.getGrandTotal();
			MOrderLine[] lines = order.getLines();
			for (int i = 0; i < lines.length; i++)
			{
				lines[i].setPrice(order.getM_PriceList_ID());
				lines[i].save();
			}
			order = new MOrder (getCtx(), p_C_Order_ID, get_TrxName());
			BigDecimal newPrice = order.getGrandTotal();
			retValue = order.getDocumentNo() + ":  " + oldPrice + " -> " + newPrice;
		}
		if (p_C_Invoice_ID != 0)
		{
			MInvoice invoice = new MInvoice (getCtx(), p_C_Invoice_ID, null);
			BigDecimal oldPrice = invoice.getGrandTotal();
			MInvoiceLine[] lines = invoice.getLines(false);
			for (int i = 0; i < lines.length; i++)
			{
				lines[i].setPrice(invoice.getM_PriceList_ID(), invoice.getC_BPartner_ID());
				lines[i].save();
			}
			invoice = new MInvoice (getCtx(), p_C_Invoice_ID, null);
			BigDecimal newPrice = invoice.getGrandTotal();
			if (retValue.length() > 0)
				retValue += Env.NL;
			retValue += invoice.getDocumentNo() + ":  " + oldPrice + " -> " + newPrice;
		}
		//
		return retValue;
	}	//	doIt

}	//	OrderRePrice
