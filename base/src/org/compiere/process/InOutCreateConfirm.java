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
 *	Create Confirmation From Shipment
 *	
 *  @author Jorg Janke
 *  @version $Id: InOutCreateConfirm.java,v 1.8 2005/09/19 04:49:45 jjanke Exp $
 */
public class InOutCreateConfirm extends SvrProcess
{
	/**	Shipment				*/
	private int 	p_M_InOut_ID = 0;
	/**	Confirmation Type		*/
	private String		p_ConfirmType = null;

	
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
			else if (name.equals("ConfirmType"))
				p_ConfirmType = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_M_InOut_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Create Confirmation
	 *	@return document no
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("M_InOut_ID=" + p_M_InOut_ID + ", Type=" + p_ConfirmType);
		MInOut shipment = new MInOut (getCtx(), p_M_InOut_ID, null);
		if (shipment.get_ID() == 0)
			throw new IllegalArgumentException("Not found M_InOut_ID=" + p_M_InOut_ID);
		//
		MInOutConfirm confirm = MInOutConfirm.create (shipment, p_ConfirmType, true);
		if (confirm == null)
			throw new Exception ("Cannot create Confirmation for " + shipment.getDocumentNo());
		//
		return confirm.getDocumentNo();
	}	//	doIt
	
}	//	InOutCreateConfirm
