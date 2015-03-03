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
 *	Verify GL Distribution
 *	
 *  @author Jorg Janke
 *  @version $Id: DistributionVerify.java,v 1.5 2005/09/19 04:49:45 jjanke Exp $
 */
public class DistributionVerify extends SvrProcess
{

	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("doIt - GL_Distribution_ID=" + getRecord_ID());
		MDistribution distribution = new MDistribution (getCtx(), getRecord_ID(), get_TrxName());
		if (distribution.get_ID() == 0)
			throw new CompiereUserError("Not found GL_Distribution_ID=" + getRecord_ID());

		String error = distribution.validate();
		boolean saved = distribution.save();
		if (error != null)
			throw new CompiereUserError(error);
		if (!saved)
			throw new CompiereSystemError("@NotSaved@");
		
		return "@OK@";
	}	//	doIt

}	//	DistributionVerify
