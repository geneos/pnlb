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
 *	Validate Payment Term and Schedule	
 *	
 *  @author Jorg Janke
 *  @version $Id: PaymentTermValidate.java,v 1.6 2005/04/27 05:20:58 jjanke Exp $
 */
public class PaymentTermValidate extends SvrProcess
{
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
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		log.info ("C_PaymentTerm_ID=" + getRecord_ID());
		MPaymentTerm pt = new MPaymentTerm (getCtx(), getRecord_ID(), get_TrxName());
		String msg = pt.validate();
		pt.save();
		//
		if ("@OK@".equals(msg))
			return msg;
		throw new CompiereUserError (msg);
	}	//	doIt

}	//	PaymentTermValidate
