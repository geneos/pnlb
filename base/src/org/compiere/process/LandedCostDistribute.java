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
 * 	Distribute Landed Costs
 *	
 *  @author Jorg Janke
 *  @version $Id: LandedCostDistribute.java,v 1.2 2005/09/19 04:49:45 jjanke Exp $
 */
public class LandedCostDistribute extends SvrProcess
{
	/** Parameter			*/
	private int			p_C_LandedCost_ID = 0;
	/** LC					*/
	private MLandedCost	m_lc = null;
	
	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		p_C_LandedCost_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		m_lc = new MLandedCost (getCtx(), p_C_LandedCost_ID, get_TrxName());
		log.info(m_lc.toString());
		if (m_lc.get_ID() == 0)
			throw new CompiereUserError("@NotFound@: @C_LandedCost_ID@ - " + p_C_LandedCost_ID);

		String error = m_lc.allocateCosts();
		if (error == null || error.length() == 0)
			return "@OK@";
		return error;
	}	//	doIt
	
}	//	LandedCostDistribute
