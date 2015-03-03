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
 *	Copy Lines	
 *	
 *  @author Jorg Janke
 *  @version $Id: RfQCopyLines.java,v 1.6 2005/09/19 04:49:45 jjanke Exp $
 */
public class RfQCopyLines extends SvrProcess
{
	/**	From RfQ 			*/
	private int		p_From_RfQ_ID = 0;
	/**	From RfQ 			*/
	private int		p_To_RfQ_ID = 0;

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
			else if (name.equals("C_RfQ_ID"))
				p_From_RfQ_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_To_RfQ_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@see org.compiere.process.SvrProcess#doIt()
	 *	@return message
	 */
	protected String doIt () throws Exception
	{
		log.info("doIt - From_RfQ_ID=" + p_From_RfQ_ID + ", To_RfQ_ID=" + p_To_RfQ_ID);
		//
		MRfQ to = new MRfQ (getCtx(), p_To_RfQ_ID, get_TrxName());
		if (to.get_ID() == 0)
			throw new IllegalArgumentException("No To RfQ found");
		MRfQ from = new MRfQ (getCtx(), p_From_RfQ_ID, get_TrxName());
		if (from.get_ID() == 0)
			throw new IllegalArgumentException("No From RfQ found");
		
		//	Copy Lines
		int counter = 0;
		MRfQLine[] lines = from.getLines();
		for (int i = 0; i < lines.length; i++)
		{
			MRfQLine newLine = new MRfQLine (to);
			newLine.setLine(lines[i].getLine());
			newLine.setDescription(lines[i].getDescription());
			newLine.setHelp(lines[i].getHelp());
			newLine.setM_Product_ID(lines[i].getM_Product_ID());
			newLine.setM_AttributeSetInstance_ID(lines[i].getM_AttributeSetInstance_ID());
		//	newLine.setDateWorkStart();
		//	newLine.setDateWorkComplete();
			newLine.setDeliveryDays(lines[i].getDeliveryDays());
			newLine.save();
			//	Copy Qtys
			MRfQLineQty[] qtys = lines[i].getQtys();
			for (int j = 0; j < qtys.length; j++)
			{
				MRfQLineQty newQty = new MRfQLineQty (newLine);
				newQty.setC_UOM_ID(qtys[j].getC_UOM_ID());
				newQty.setQty(qtys[j].getQty());
				newQty.setIsOfferQty(qtys[j].isOfferQty());
				newQty.setIsPurchaseQty(qtys[j].isPurchaseQty());
				newQty.setMargin(qtys[j].getMargin());
				newQty.save();
			}
			counter++;
		}	//	copy all lines	
		
		//
		return "# " + counter;
	}	//	doIt
}
