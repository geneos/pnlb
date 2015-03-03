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

import java.util.*;

import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Generate Sales Order from Project.
 *
 *	@author Jorg Janke
 *	@version $Id: ProjectGenOrder.java,v 1.13 2005/04/28 05:51:43 jjanke Exp $
 */
public class ProjectGenOrder extends SvrProcess
{
	/**	Project ID from project directly		*/
	private int		m_C_Project_ID = 0;

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
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		m_C_Project_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		log.info("C_Project_ID=" + m_C_Project_ID);
		if (m_C_Project_ID == 0)
			throw new IllegalArgumentException("C_Project_ID == 0");
		MProject fromProject = getProject (getCtx(), m_C_Project_ID, get_TrxName());
		Env.setSOTrx(getCtx(), true);	//	Set SO context

		/** @todo duplicate invoice prevention */

		MOrder order = new MOrder (fromProject, true, MOrder.DocSubTypeSO_OnCredit);
		if (!order.save())
			throw new Exception("Could not create Order");

		//	***	Lines ***
		int count = 0;
		
		//	Service Project	
		if (MProject.PROJECTCATEGORY_ServiceChargeProject.equals(fromProject.getProjectCategory()))
		{
			/** @todo service project invoicing */
			throw new Exception("Service Charge Projects are on the TODO List");
		}	//	Service Lines

		else	//	Order Lines
		{
			MProjectLine[] lines = fromProject.getLines ();
			for (int i = 0; i < lines.length; i++)
			{
				MOrderLine ol = new MOrderLine(order);
				ol.setLine(lines[i].getLine());
				ol.setDescription(lines[i].getDescription());
				//
				ol.setM_Product_ID(lines[i].getM_Product_ID(), true);
				ol.setQty(lines[i].getPlannedQty().subtract(lines[i].getInvoicedQty()));
				ol.setPrice();
				if (lines[i].getPlannedPrice() != null && lines[i].getPlannedPrice().compareTo(Env.ZERO) != 0)
					ol.setPrice(lines[i].getPlannedPrice());
				ol.setDiscount();
				ol.setTax();
				if (ol.save())
					count++;
			}	//	for all lines
			if (lines.length != count)
				log.log(Level.SEVERE, "Lines difference - ProjectLines=" + lines.length + " <> Saved=" + count);
		}	//	Order Lines

		return "@C_Order_ID@ " + order.getDocumentNo() + " (" + count + ")";
	}	//	doIt

	/**
	 * 	Get and validate Project
	 * 	@param ctx context
	 * 	@param C_Project_ID id
	 * 	@return valid project
	 */
	static protected MProject getProject (Properties ctx, int C_Project_ID, String trxName)
	{
		MProject fromProject = new MProject (ctx, C_Project_ID, trxName);
		if (fromProject.getC_Project_ID() == 0)
			throw new IllegalArgumentException("Project not found C_Project_ID=" + C_Project_ID);
		if (fromProject.getM_PriceList_Version_ID() == 0)
			throw new IllegalArgumentException("Project has no Price List");
		if (fromProject.getM_Warehouse_ID() == 0)
			throw new IllegalArgumentException("Project has no Warehouse");
		if (fromProject.getC_BPartner_ID() == 0 || fromProject.getC_BPartner_Location_ID() == 0)
			throw new IllegalArgumentException("Project has no Business Partner/Location");
		return fromProject;
	}	//	getProject

}	//	ProjectGenOrder
