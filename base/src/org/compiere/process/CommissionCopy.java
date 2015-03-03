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

import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Copy Commission	
 *	
 *  @author Jorg Janke
 *  @version $Id: CommissionCopy.java,v 1.5 2005/09/19 04:49:45 jjanke Exp $
 */
public class CommissionCopy extends SvrProcess
{
	/**	From Commission			*/
	private int 	p_C_Commission_ID = 0;
	/** To Commission			*/
	private int		p_C_CommissionTo_ID = 0;
	
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
			else if (name.equals("C_Commission_ID"))
				p_C_Commission_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_C_CommissionTo_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process - copy
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("doIt - C_Commission_ID=" + p_C_Commission_ID + " - copy to " + p_C_CommissionTo_ID);
		MCommission comFrom = new MCommission (getCtx(), p_C_Commission_ID, get_TrxName());
		if (comFrom.get_ID() == 0)
			throw new CompiereUserError ("No From Commission");
		MCommission comTo = new MCommission (getCtx(), p_C_CommissionTo_ID, get_TrxName());
		if (comTo.get_ID() == 0)
			throw new CompiereUserError ("No To Commission");
		
		//
		int no = comTo.copyLinesFrom(comFrom);
		return "@Copied@: " + no;
	}	//	doIt

}	//	CommissionCopy
