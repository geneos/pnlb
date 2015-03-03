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
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	SLA Delivery Accuracy.
 *	How accurate is the promise date?
 *	<p>
 *	The measure are the average days between promise date (PO/SO) and delivery date 
 *	(Material receipt/shipment) It is positive if before, negative if later. 
 *	The lower the number, the better
 *	
 *  @author Jorg Janke
 *  @version $Id: DeliveryAccuracy.java,v 1.6 2005/11/06 01:17:27 jjanke Exp $
 */
public class DeliveryAccuracy extends SLACriteria
{

	/**
	 * 	DeliveryAccuracy
	 */
	public DeliveryAccuracy ()
	{
		super ();
	}	//	DeliveryAccuracy
	
	/**	Logger			*/
	protected CLogger	log = CLogger.getCLogger(getClass());
	
	
	/**
	 * 	Create new Measures for the Goal
	 * 	@param goal the goal
	 * 	@return number created
	 */
	public int createMeasures (MSLAGoal goal)
	{
		String sql = "SELECT M_InOut_ID, io.MovementDate-o.DatePromised," 	//	1..2
			+ " io.MovementDate, o.DatePromised, o.DocumentNo "
			+ "FROM M_InOut io"
			+ " INNER JOIN C_Order o ON (io.C_Order_ID=o.C_Order_ID) "
			+ "WHERE io.C_BPartner_ID=?"
			+ " AND NOT EXISTS "
				+ "(SELECT * FROM PA_SLA_Measure m "
				+ "WHERE m.PA_SLA_Goal_ID=?"
				+ " AND m.AD_Table_ID=" + MInOut.getTableId(MInOut.Table_Name)
				+ " AND m.Record_ID=io.M_InOut_ID)";
		int counter = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, goal.getC_BPartner_ID());
			pstmt.setInt (2, goal.getPA_SLA_Goal_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				int M_InOut_ID = rs.getInt(1);
				BigDecimal MeasureActual = rs.getBigDecimal(2);
				Timestamp MovementDate = rs.getTimestamp(3);
				String Description = rs.getString(5) + ": " + rs.getTimestamp(4);
				if (goal.isDateValid(MovementDate))
				{
					MSLAMeasure measure = new MSLAMeasure(goal, MovementDate, 
						MeasureActual, Description);
					measure.setLink(MInOut.getTableId(MInOut.Table_Name), M_InOut_ID);
					if (measure.save())
						counter++;
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "createMeasures", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return counter;
	}	//	createMeasures

	
	/**************************************************************************
	 * 	Calculate Goal Actual from unprocessed Measures
	 *	@return goal actual measure
	 */
	public BigDecimal calculateMeasure (MSLAGoal goal)
	{
		//	Average
		BigDecimal retValue = Env.ZERO;
		BigDecimal total = Env.ZERO;
		int count = 0;
		//
		MSLAMeasure[] measures = goal.getAllMeasures();
		for (int i = 0; i < measures.length; i++)
		{
			MSLAMeasure measure = measures[i];
			if (!measure.isActive() 
				|| (goal.getValidFrom() != null && measure.getDateTrx().before(goal.getValidFrom()))
				|| (goal.getValidTo() != null && measure.getDateTrx().after(goal.getValidTo())))
				continue;
			//
			total = total.add(measure.getMeasureActual());
			count++;
			//
			if (!measure.isProcessed())
			{
				measure.setProcessed(true);
				measure.save();
			}
		}
		//	Goal Expired
		if (goal.getValidTo() != null 
			&& goal.getValidTo().after(new Timestamp(System.currentTimeMillis())))
			goal.setProcessed(true);
			
		//	Calculate with 2 digits precision
		if (count != 0)
			retValue = total.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP);
		return retValue;
	}	//	calculateMeasure

}	//	DeliveryAccuracy
