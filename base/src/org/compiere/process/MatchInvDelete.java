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
import org.compiere.util.*;


/**
 *	Delete Inv Match
 *	
 *  @author Jorg Janke
 *  @version $Id: MatchInvDelete.java,v 1.6 2005/09/19 04:49:45 jjanke Exp $
 */
public class MatchInvDelete extends SvrProcess
{
	/**	ID					*/
	private int		p_M_MatchInv_ID = 0;

	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		p_M_MatchInv_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt()	throws Exception
	{
		log.info ("M_MatchInv_ID=" + p_M_MatchInv_ID);
		MMatchInv inv = new MMatchInv (getCtx(), p_M_MatchInv_ID, get_TrxName());
		if (inv.get_ID() == 0)
			throw new CompiereUserError("@NotFound@ @M_MatchInv_ID@ " + p_M_MatchInv_ID);
		if (inv.delete(true))
			return "@OK@";
		inv.save();
		return "@Error@";
	}	//	doIt

}	//	MatchInvDelete
