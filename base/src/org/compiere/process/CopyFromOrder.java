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
 *  Copy Order Lines
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromOrder.java,v 1.8 2005/05/31 05:42:02 jjanke Exp $
 */
public class CopyFromOrder extends SvrProcess
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
				p_C_Order_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		int To_C_Order_ID = getRecord_ID();
		log.info("From C_Order_ID=" + p_C_Order_ID + " to " + To_C_Order_ID);
		if (To_C_Order_ID == 0)
			throw new IllegalArgumentException("Target C_Order_ID == 0");
		if (p_C_Order_ID == 0)
			throw new IllegalArgumentException("Source C_Order_ID == 0");
		MOrder from = new MOrder (getCtx(), p_C_Order_ID, get_TrxName());
		MOrder to = new MOrder (getCtx(), To_C_Order_ID, get_TrxName());
		//
		int no = to.copyLinesFrom (from, false, false);		//	no Attributes
		//
		return "@Copied@=" + no;
	}	//	doIt

}	//	CopyFromOrder
