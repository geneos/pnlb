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
 
/**
 *	Re-Open Order Process (from Closed to Completed)
 *	
 *  @author Jorg Janke
 *  @version $Id: OrderOpen.java,v 1.5 2005/03/11 20:25:59 jjanke Exp $
 */
public class OrderOpen extends SvrProcess
{
	/**	The Order				*/
	private int		p_C_Order_ID = 0;

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
				p_C_Order_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		log.info("doIt - Open C_Order_ID=" + p_C_Order_ID);
		if (p_C_Order_ID == 0)
			throw new IllegalArgumentException("C_Order_ID == 0");
		//
		MOrder order = new MOrder (getCtx(), p_C_Order_ID, get_TrxName());
		if (MOrder.DOCSTATUS_Closed.equals(order.getDocStatus()))
		{
			order.setDocStatus(MOrder.DOCSTATUS_Completed);
			return order.save() ? "@OK@" : "@Error@";
		}
		else
			throw new IllegalStateException("Order is not closed");
	}	//	doIt
	
}	//	OrderOpen
