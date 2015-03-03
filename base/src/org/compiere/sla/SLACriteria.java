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

import java.math.*;
import java.sql.*;

import org.compiere.model.*;

/**
 *	Service Level Agreement Criteria
 *	
 *  @author Jorg Janke
 *  @version $Id: SLACriteria.java,v 1.4 2005/09/29 22:01:55 jjanke Exp $
 */
public abstract class SLACriteria
{
	/**
	 * 	Create new Measures for the Goal
	 * 	@param goal the goal
	 * 	@return number created
	 */
	public abstract int createMeasures (MSLAGoal goal);

	/**
	 * 	Calculate Goal Actual from unprocessed Measures of the Goal
	 * 	@param goal the goal
	 * 	@return new Actual Measure
	 */
	public abstract BigDecimal calculateMeasure (MSLAGoal goal);


	/**
	 * 	Create new Measures for the Criteria
	 * 	@param criteria the criteria
	 */
	public int createMeasures (MSLACriteria criteria)
	{
		int counter = 0;
		MSLAGoal[] goals = criteria.getGoals();
		for (int i = 0; i < goals.length; i++)
		{
			MSLAGoal goal = goals[i];
			if (goal.isActive())
				counter += createMeasures (goal);
		}
		return counter;
	}	//	createMeasures

	/**
	 * 	Calculate Goal Actual from unprocessed Measures of the Goal
	 * 	@param criteria SLA criteria
	 */
	public void calculateMeasures (MSLACriteria criteria)
	{
		MSLAGoal[] goals = criteria.getGoals();
		for (int i = 0; i < goals.length; i++)
		{
			MSLAGoal goal = goals[i];
			if (goal.isActive())
			{
				goal.setMeasureActual(calculateMeasure(goal));
				goal.setDateLastRun(new Timestamp(System.currentTimeMillis()));
				goal.save();
			}
		}
	}	//	calculateMeasures
	
}	//	SLACriteria
