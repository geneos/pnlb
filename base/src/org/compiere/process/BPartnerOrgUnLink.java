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
 *	UnLink Business Partner from Organization 
 *	
 *  @author Jorg Janke
 *  @version $Id: BPartnerOrgUnLink.java,v 1.5 2005/09/19 04:49:45 jjanke Exp $
 */
public class BPartnerOrgUnLink extends SvrProcess
{
	/** Business Partner		*/
	private int			p_C_BPartner_ID;
	
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
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = ((BigDecimal)para[i].getParameter()).intValue();
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
		log.info("doIt - C_BPartner_ID=" + p_C_BPartner_ID); 
		if (p_C_BPartner_ID == 0)
			throw new IllegalArgumentException ("No Business Partner ID");
		MBPartner bp = new MBPartner (getCtx(), p_C_BPartner_ID, get_TrxName());
		if (bp.get_ID() == 0)
			throw new IllegalArgumentException ("Business Partner not found - C_BPartner_ID=" + p_C_BPartner_ID);
		//
		if (bp.getAD_OrgBP_ID_Int() == 0)
			throw new IllegalArgumentException ("Business Partner not linked to an Organization");
		bp.setAD_OrgBP_ID(null);
		if (!bp.save())
			throw new IllegalArgumentException ("Business Partner not changed");
		
		return "OK";
	}	//	doIt
	
}	//	BPartnerOrgUnLink
