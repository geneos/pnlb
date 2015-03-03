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
 *	Check if Response is Complete
 *	
 *  @author Jorg Janke
 *  @version $Id: RfQResponseCComplete.java,v 1.4 2005/03/11 20:25:57 jjanke Exp $
 */
public class RfQResponseCComplete extends SvrProcess
{
	/**	RfQ Response				*/
	private int		p_C_RfQResponse_ID = 0;
	
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
		p_C_RfQResponse_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		MRfQResponse response = new MRfQResponse (getCtx(), p_C_RfQResponse_ID, get_TrxName());
		log.info("doIt - " + response);
		//
		String error = response.checkComplete();
		if (error != null && error.length() > 0)
			throw new Exception (error);
		//
		response.save();
		return "OK";
	}	//	doIt
	
}	//	RfQResponseCComplete
