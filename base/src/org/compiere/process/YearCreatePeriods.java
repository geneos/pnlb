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
 *	Create Periods of year
 *	
 *  @author Jorg Janke
 *  @version $Id: YearCreatePeriods.java,v 1.6 2005/09/19 04:49:45 jjanke Exp $
 */
public class YearCreatePeriods extends SvrProcess
{
	private int	p_C_Year_ID = 0;
	
	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		p_C_Year_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt ()
		throws Exception
	{
		MYear year = new MYear (getCtx(), p_C_Year_ID, get_TrxName());
		if (p_C_Year_ID == 0 || year.get_ID() != p_C_Year_ID)
			throw new CompiereUserError ("@NotFound@: @C_Year_ID@ - " + p_C_Year_ID);
		log.info(year.toString());
		//
		if (year.createStdPeriods(null))
			return "@OK@";
		return "@Error@";
	}	//	doIt
	
}	//	YearCreatePeriods
