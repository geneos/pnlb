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
 *	Open/Close Period Control
 *	
 *  @author Jorg Janke
 *  @version $Id: PeriodControlStatus.java,v 1.7 2005/09/19 04:49:45 jjanke Exp $
 */
public class PeriodControlStatus extends SvrProcess
{
	/** Period Control				*/
	private int 		p_C_PeriodControl_ID = 0;
	
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
		p_C_PeriodControl_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		log.info ("C_PeriodControl_ID=" + p_C_PeriodControl_ID);
		MPeriodControl pc = new MPeriodControl (getCtx(), p_C_PeriodControl_ID, get_TrxName());
		if (pc.get_ID() == 0)
			throw new CompiereUserError("@NotFound@  @C_PeriodControl_ID@=" + p_C_PeriodControl_ID);
		//	Permanently closed
		if (MPeriodControl.PERIODACTION_PermanentlyClosePeriod.equals(pc.getPeriodStatus()))
			throw new CompiereUserError("@PeriodStatus@ = " + pc.getPeriodStatus());
		//	No Action
		if (MPeriodControl.PERIODACTION_NoAction.equals(pc.getPeriodAction()))
			return "@OK@";
	
		//	Open
		if (MPeriodControl.PERIODACTION_OpenPeriod.equals(pc.getPeriodAction()))
			pc.setPeriodStatus(MPeriodControl.PERIODSTATUS_Open);
		//	Close
		if (MPeriodControl.PERIODACTION_ClosePeriod.equals(pc.getPeriodAction()))
			pc.setPeriodStatus(MPeriodControl.PERIODSTATUS_Closed);
		//	Close Permanently
		if (MPeriodControl.PERIODACTION_PermanentlyClosePeriod.equals(pc.getPeriodAction()))
			pc.setPeriodStatus(MPeriodControl.PERIODSTATUS_PermanentlyClosed);
		pc.setPeriodAction(MPeriodControl.PERIODACTION_NoAction);
		//
		boolean ok = pc.save();
		
		//	Reset Cache
		CacheMgt.get().reset("C_PeriodControl", 0);
		CacheMgt.get().reset("C_Period", pc.getC_Period_ID());

		if (!ok)
			return "@Error@";
		return "@OK@";
	}	//	doIt

}	//	PeriodControlStatus
