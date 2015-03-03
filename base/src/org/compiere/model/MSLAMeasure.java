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
package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import org.compiere.util.*;

/**
 *	Service Level Agreement Measure
 *	
 *  @author Jorg Janke
 *  @version $Id: MSLAMeasure.java,v 1.5 2005/09/19 04:49:46 jjanke Exp $
 */
public class MSLAMeasure extends X_PA_SLA_Measure
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_SLA_Measure_ID id
	 */
	public MSLAMeasure (Properties ctx, int PA_SLA_Measure_ID, String trxName)
	{
		super (ctx, PA_SLA_Measure_ID, trxName);
		if (PA_SLA_Measure_ID == 0)
		{
		//	setPA_SLA_Goal_ID (0);
			setDateTrx (new Timestamp(System.currentTimeMillis()));
			setMeasureActual (Env.ZERO);
			setProcessed (false);
		}
	}	//	MSLAMeasure

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MSLAMeasure (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MSLAMeasure

	/**
	 * 	Parent Constructor
	 *	@param goal parent
	 *	@param DateTrx optional date
	 *	@param MeasureActual optional measure
	 *	@param Description optional description
	 */
	public MSLAMeasure (MSLAGoal goal, Timestamp DateTrx, BigDecimal MeasureActual,
		String Description)
	{
		super (goal.getCtx(), 0, goal.get_TrxName());
		setClientOrg(goal);
		setPA_SLA_Goal_ID(goal.getPA_SLA_Goal_ID());
		if (DateTrx != null)
			setDateTrx (DateTrx);
		else
			setDateTrx (new Timestamp(System.currentTimeMillis()));
		if (MeasureActual != null)
			setMeasureActual(MeasureActual);
		else
			setMeasureActual (Env.ZERO);
		if (Description != null)
			setDescription(Description);
	}	//	MSLAMeasure
	
	/**
	 * 	Set Link to Source
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 */
	public void setLink (int AD_Table_ID, int Record_ID)
	{
		setAD_Table_ID(AD_Table_ID);
		setRecord_ID(Record_ID);
	}	//	setLink
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MSLAMeasure[");
		sb.append(get_ID()).append("-PA_SLA_Goal_ID=").append(getPA_SLA_Goal_ID())
			.append(",").append(getDateTrx())
			.append(",Actual=").append(getMeasureActual())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MSLAMeasure
