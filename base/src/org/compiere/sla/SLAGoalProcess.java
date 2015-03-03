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
package org.compiere.sla;

import java.sql.*;

import org.compiere.model.*;
import org.compiere.process.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Service Level Agreement Goal.
 *	If automatic, capture measures - and calculate/update the actual measure.
 *	
 *  @author Jorg Janke
 *  @version $Id: SLAGoalProcess.java,v 1.5 2005/09/19 04:49:48 jjanke Exp $
 */
public class SLAGoalProcess extends SvrProcess
{
	/** Goal					*/
	private int			p_PA_SLA_Goal_ID;

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
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_PA_SLA_Goal_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info ("PA_SLA_Goal_ID=" + p_PA_SLA_Goal_ID);
		MSLAGoal goal = new MSLAGoal(getCtx(), p_PA_SLA_Goal_ID, get_TrxName());
		if (goal.get_ID() == 0)
			throw new CompiereUserError("@PA_SLA_Goal_ID@ " + p_PA_SLA_Goal_ID);

		MSLACriteria criteria = MSLACriteria.get(getCtx(), goal.getPA_SLA_Criteria_ID(), get_TrxName());
		if (criteria.get_ID() == 0)
			throw new CompiereUserError("@PA_SLA_Criteria_ID@ " + goal.getPA_SLA_Criteria_ID());
		
		SLACriteria pgm = criteria.newInstance();
		int no = pgm.createMeasures(goal);
		//
		goal.setMeasureActual(pgm.calculateMeasure(goal));
		goal.setDateLastRun(new Timestamp(System.currentTimeMillis()));
		goal.save();
		//
		return "@Created@ " + no + " - @MeasureActual@=" + goal.getMeasureActual();
	}	//	doIt

}	//	SLAGoalProcess
