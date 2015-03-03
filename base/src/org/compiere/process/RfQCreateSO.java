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

import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Create SO for RfQ.
 *	
 *  @author Jorg Janke
 *  @version $Id: RfQCreateSO.java,v 1.10 2005/10/08 02:02:30 jjanke Exp $
 */
public class RfQCreateSO extends SvrProcess
{
	/**	RfQ 			*/
	private int		p_C_RfQ_ID = 0;
	private int		p_C_DocType_ID = 0;

	/**	100						*/
	private static BigDecimal 	ONEHUNDRED = new BigDecimal (100);

	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_DocType_ID"))
				p_C_DocType_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_C_RfQ_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process.
	 * 	A Sales Order is created for the entered Business Partner.  
	 * 	A sales order line is created for each RfQ line quantity, 
	 * 	where "Offer Quantity" is selected.  
	 * 	If on the RfQ Line Quantity, an offer amount is entered (not 0), 
	 * 	that price is used. 
	 *	If a magin is entered on RfQ Line Quantity, it overwrites the 
	 *	general margin.  The margin is the percentage added to the 
	 *	Best Response Amount.
	 *	@return message
	 */
	protected String doIt () throws Exception
	{
		MRfQ rfq = new MRfQ (getCtx(), p_C_RfQ_ID, get_TrxName());
		if (rfq.get_ID() == 0)
			throw new IllegalArgumentException("No RfQ found");
		log.info("doIt - " + rfq);
		
		if (rfq.getC_BPartner_ID() == 0 || rfq.getC_BPartner_Location_ID() == 0)
			throw new Exception ("No Business Partner/Location");
		MBPartner bp = new MBPartner (getCtx(), rfq.getC_BPartner_ID(), get_TrxName());
		
		MOrder order = new MOrder (getCtx(), 0, get_TrxName());
		order.setIsSOTrx(true);
		if (p_C_DocType_ID != 0)
			order.setC_DocTypeTarget_ID(p_C_DocType_ID);
		else
			order.setC_DocTypeTarget_ID();
		order.setBPartner(bp);
		order.setC_BPartner_Location_ID(rfq.getC_BPartner_Location_ID());
		order.setSalesRep_ID(rfq.getSalesRep_ID());
		if (rfq.getDateWorkComplete() != null)
			order.setDatePromised(rfq.getDateWorkComplete());
		order.save();

		MRfQLine[] lines = rfq.getLines();
		for (int i = 0; i < lines.length; i++)
		{
			MRfQLine line = lines[i];
			MRfQLineQty[] qtys = line.getQtys();
			for (int j = 0; j < qtys.length; j++)
			{
				MRfQLineQty qty = qtys[j];
				if (qty.isActive() && qty.isOfferQty())
				{
					MOrderLine ol = new MOrderLine (order);
					ol.setM_Product_ID(line.getM_Product_ID(),
						qty.getC_UOM_ID());
					ol.setDescription(line.getDescription());
					ol.setQty(qty.getQty());
					//
					BigDecimal price = qty.getOfferAmt();
					if (price == null || price.signum() == 0)
					{
						price = qty.getBestResponseAmt();
						if (price == null || price.signum() == 0)
						{
							price = Env.ZERO;
							log.warning(" - BestResponse=0 - " + qty);
						}
						else
						{
							BigDecimal margin = qty.getMargin();
							if (margin == null || margin.signum() == 0)
								margin = rfq.getMargin();
							if (margin != null && margin.signum() != 0)
							{
								margin = margin.add(ONEHUNDRED);
								price = price.multiply(margin).divide(ONEHUNDRED, 2, BigDecimal.ROUND_HALF_UP);
							}
						}
					}	//	price
					ol.setPrice(price);
					ol.save();
				}	//	Offer Qty
			}	//	All Qtys
		}	//	All Lines

		//
		rfq.setC_Order_ID(order.getC_Order_ID());
		rfq.save();
		return order.getDocumentNo();
	}	//	doIt
}
