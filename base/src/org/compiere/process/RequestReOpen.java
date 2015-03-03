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
 *	Re-Open Request
 *	
 *  @author Jorg Janke
 *  @version $Id: RequestReOpen.java,v 1.6 2005/09/19 04:49:45 jjanke Exp $
 */
public class RequestReOpen extends SvrProcess
{
	/** Request					*/
	private int	p_R_Request_ID = 0;
	
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
			else if (name.equals("R_Request_ID"))
				p_R_Request_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process It
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		MRequest request = new MRequest (getCtx(), p_R_Request_ID, get_TrxName());
		log.info(request.toString());
		if (request.get_ID() == 0)
			throw new CompiereUserError("@NotFound@ @R_Request_ID@ " + p_R_Request_ID);
		
		request.setR_Status_ID();	//	set default status
		request.setProcessed(false);
		if (request.save() && !request.isProcessed())
			return "@OK@";
		return "@Error@";
	}	//	doUt

}	//	RequestReOpen
