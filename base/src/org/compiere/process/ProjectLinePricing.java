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
 *  Price Project Line.
 *
 *	@author Jorg Janke
 *	@version $Id: ProjectLinePricing.java,v 1.9 2005/03/11 20:25:58 jjanke Exp $
 */
public class ProjectLinePricing extends SvrProcess
{
	/**	Project Line from Record			*/
	private int 		m_C_ProjectLine_ID = 0;

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
		m_C_ProjectLine_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (m_C_ProjectLine_ID == 0)
			throw new IllegalArgumentException("No Project Line");
		MProjectLine projectLine = new MProjectLine (getCtx(), m_C_ProjectLine_ID, get_TrxName());
		log.info("doIt - " + projectLine);
		if (projectLine.getM_Product_ID() == 0)
			throw new IllegalArgumentException("No Product");
		//
		MProject project = new MProject (getCtx(), projectLine.getC_Project_ID(), get_TrxName());
		if (project.getM_PriceList_ID() == 0)
			throw new IllegalArgumentException("No PriceList");
		//
		boolean isSOTrx = true;
		MProductPricing pp = new MProductPricing (projectLine.getM_Product_ID(), 
			project.getC_BPartner_ID(), projectLine.getPlannedQty(), isSOTrx);
		pp.setM_PriceList_ID(project.getM_PriceList_ID());
		pp.setPriceDate(project.getDateContract());
		//
		projectLine.setPlannedPrice(pp.getPriceStd());
		projectLine.setPlannedMarginAmt(pp.getPriceStd().subtract(pp.getPriceLimit()));
		projectLine.save();
		//
		String retValue = Msg.getElement(getCtx(), "PriceList") + pp.getPriceList() + " - "
			+ Msg.getElement(getCtx(), "PriceStd") + pp.getPriceStd() + " - "
			+ Msg.getElement(getCtx(), "PriceLimit") + pp.getPriceLimit();
		return retValue;
	}	//	doIt

}	//	ProjectLinePricing
