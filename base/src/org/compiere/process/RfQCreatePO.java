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

/**
 * 	Create RfQ PO.
 *	Create purchase order(s) for the resonse(s) and lines marked as 
 *	Selected Winner using the selected Purchase Quantity (in RfQ Line Quantity) 
 *	
 *  @author Jorg Janke
 *  @version $Id: RfQCreatePO.java,v 1.11 2005/09/19 04:49:45 jjanke Exp $
 */
public class RfQCreatePO extends SvrProcess
{
	/**	RfQ 			*/
	private int		p_C_RfQ_ID = 0;
	private int		p_C_DocType_ID = 0;

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
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_RfQ_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process.
	 * 	Create purchase order(s) for the resonse(s) and lines marked as 
	 * 	Selected Winner using the selected Purchase Quantity (in RfQ Line Quantity) . 
	 * 	If a Response is marked as Selected Winner, all lines are created 
	 * 	(and Selected Winner of other responses ignored).  
	 * 	If there is no response marked as Selected Winner, the lines are used.
	 *	@return message
	 */
	protected String doIt () throws Exception
	{
		MRfQ rfq = new MRfQ (getCtx(), p_C_RfQ_ID, get_TrxName());
		if (rfq.get_ID() == 0)
			throw new IllegalArgumentException("No RfQ found");
		log.info(rfq.toString());
		
		//	Complete 
		MRfQResponse[] responses = rfq.getResponses(true, true);
		log.config("#Responses=" + responses.length);
		if (responses.length == 0)
			throw new IllegalArgumentException("No completed RfQ Responses found");
		
		//	Winner for entire RfQ
		for (int i = 0; i < responses.length; i++)
		{
			MRfQResponse response = responses[i];
			if (!response.isSelectedWinner())
				continue;
			//
			MBPartner bp = new MBPartner(getCtx(), response.getC_BPartner_ID(), get_TrxName());
			log.config("Winner=" + bp);
			MOrder order = new MOrder (getCtx(), 0, get_TrxName());
			order.setIsSOTrx(false);
			if (p_C_DocType_ID != 0)
				order.setC_DocTypeTarget_ID(p_C_DocType_ID);
			else
				order.setC_DocTypeTarget_ID();
			order.setBPartner(bp);
			order.setC_BPartner_Location_ID(response.getC_BPartner_Location_ID());
			order.setSalesRep_ID(rfq.getSalesRep_ID());
			if (response.getDateWorkComplete() != null)
				order.setDatePromised(response.getDateWorkComplete());
			else if (rfq.getDateWorkComplete() != null)
				order.setDatePromised(rfq.getDateWorkComplete());
			order.save();
			//
			MRfQResponseLine[] lines = response.getLines(false);
			for (int j = 0; j < lines.length; j++)
			{
				//	Respones Line
				MRfQResponseLine line = lines[j];
				if (!line.isActive())
					continue;
				MRfQResponseLineQty[] qtys = line.getQtys(false);
				//	Response Line Qty
				for (int k = 0; k < qtys.length; k++)
				{
					MRfQResponseLineQty qty = qtys[k];
					//	Create PO Lline for all Purchase Line Qtys
					if (qty.getRfQLineQty().isActive() && qty.getRfQLineQty().isPurchaseQty())
					{
						MOrderLine ol = new MOrderLine (order);
						ol.setM_Product_ID(line.getRfQLine().getM_Product_ID(), 
							qty.getRfQLineQty().getC_UOM_ID());
						ol.setDescription(line.getDescription());
						ol.setQty(qty.getRfQLineQty().getQty());
						BigDecimal price = qty.getNetAmt();
						ol.setPrice(price);
						ol.save();
					}
				}
			}
			response.setC_Order_ID(order.getC_Order_ID());
			response.save();
			return order.getDocumentNo();
		}

		
		//	Selected Winner on Line Level
		int noOrders = 0;
		for (int i = 0; i < responses.length; i++)
		{
			MRfQResponse response = responses[i];
			MBPartner bp = null;
			MOrder order = null;
			//	For all Response Lines
			MRfQResponseLine[] lines = response.getLines(false);
			for (int j = 0; j < lines.length; j++)
			{
				MRfQResponseLine line = lines[j];
				if (!line.isActive() || !line.isSelectedWinner())
					continue;
				//	New/different BP
				if (bp == null)
				{
					bp = new MBPartner(getCtx(), response.getC_BPartner_ID(), get_TrxName());
					order = null;
				}
				log.config("Line=" + line + ", Winner=" + bp);
				//	New Order
				if (order == null)
				{
					order = new MOrder (getCtx(), 0, get_TrxName());
					order.setIsSOTrx(false);
					order.setC_DocTypeTarget_ID();
					order.setBPartner(bp);
					order.setC_BPartner_Location_ID(response.getC_BPartner_Location_ID());
					order.setSalesRep_ID(rfq.getSalesRep_ID());
					order.save();
					noOrders++;
					addLog(0, null, null, order.getDocumentNo());
				}
				//	For all Qtys
				MRfQResponseLineQty[] qtys = line.getQtys(false);
				for (int k = 0; k < qtys.length; k++)
				{
					MRfQResponseLineQty qty = qtys[k];
					if (qty.getRfQLineQty().isActive() && qty.getRfQLineQty().isPurchaseQty())
					{
						MOrderLine ol = new MOrderLine (order);
						ol.setM_Product_ID(line.getRfQLine().getM_Product_ID(), 
							qty.getRfQLineQty().getC_UOM_ID());
						ol.setDescription(line.getDescription());
						ol.setQty(qty.getRfQLineQty().getQty());
						BigDecimal price = qty.getNetAmt();
						ol.setPriceActual(price);
						ol.save();
					}
				}	//	for all Qtys
			}	//	for all Response Lines
			if (order != null)
			{
				response.setC_Order_ID(order.getC_Order_ID());
				response.save();
			}
		}
		
		return "#" + noOrders;
	}	//	doIt
}	//	RfQCreatePO
