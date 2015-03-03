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
 *	Renumber Discount Schema
 *	
 *  @author Jorg Janke
 *  @version $Id: DiscountSchemaReSeq.java,v 1.5 2005/09/19 04:49:45 jjanke Exp $
 */
public class DiscountSchemaReSeq extends SvrProcess
{
	/** Discount Schema			*/
	private int p_M_DiscountSchema_ID = 0;

	/**
	 * 	Prepare
	 *	@see org.compiere.process.SvrProcess#prepare()
	 */
	protected void prepare ()
	{
		p_M_DiscountSchema_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Execute
	 *	@return info
	 */
	protected String doIt () throws Exception
	{
		log.info("M_DiscountSchema_ID=" + p_M_DiscountSchema_ID);
		if (p_M_DiscountSchema_ID == 0)
			throw new CompiereUserError("@M_DiscountSchema_ID@ = 0");
		MDiscountSchema ds = new MDiscountSchema(getCtx(), p_M_DiscountSchema_ID, get_TrxName());
		if (ds.get_ID() == 0)
			throw new CompiereUserError("@NotFound@ M_DiscountSchema_ID=" + p_M_DiscountSchema_ID);
		//
		int updated = ds.reSeq();
		
		return "@Updated@ #" + updated;
	}	//	doIt
	
}	// DiscountSchemaRenumber
