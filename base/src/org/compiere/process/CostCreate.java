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

import org.compiere.util.*;
import org.compiere.model.*;

/**
 * 	Create/Update Costing for Product
 *	
 *  @author Jorg Janke
 *  @version $Id: CostCreate.java,v 1.2 2005/09/29 20:24:29 jjanke Exp $
 */
public class CostCreate extends SvrProcess
{
	/**	Product				*/
	private int 	p_M_Product_ID = 0; 

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
		//	log.fine("prepare - " + para[i]);
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);		
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (text with variables)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		log.info("M_Product_ID=" + p_M_Product_ID);
		if (p_M_Product_ID == 0)
			throw new CompiereUserError("@NotFound@: @M_Product_ID@ = " + p_M_Product_ID);
		MProduct product = MProduct.get(getCtx(), p_M_Product_ID);
		if (product.get_ID() != p_M_Product_ID)
			throw new CompiereUserError("@NotFound@: @M_Product_ID@ = " + p_M_Product_ID);
		//
		if (MCostDetail.processProduct(product, get_TrxName()))
			return "@OK@";
		return "@Error@";
	}	//	doIt
	
}	//	CostCreate
