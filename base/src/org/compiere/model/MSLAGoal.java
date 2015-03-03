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

import java.sql.*;
import java.util.*;

import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Service Level Agreement Goals
 *	
 *  @author Jorg Janke
 *  @version $Id: MSLAGoal.java,v 1.7 2005/11/14 02:10:53 jjanke Exp $
 */
public class MSLAGoal extends X_PA_SLA_Goal
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_SLA_Goal_ID id
	 */
	public MSLAGoal (Properties ctx, int PA_SLA_Goal_ID, String trxName)
	{
		super (ctx, PA_SLA_Goal_ID, trxName);
		if (PA_SLA_Goal_ID == 0)
		{
			setMeasureActual (Env.ZERO);
			setMeasureTarget (Env.ZERO);
			setProcessed (false);
		}
	}	//	MSLAGoal

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MSLAGoal (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MSLAGoal

	/**
	 * 	Get All Measures
	 *	@return array of measures
	 */
	public MSLAMeasure[] getAllMeasures()
	{
		String sql = "SELECT * FROM PA_SLA_Measure "
			+ "WHERE PA_SLA_Goal_ID=? "
			+ "ORDER BY DateTrx";
		return getMeasures (sql);
	}	//	getAllMeasures

	/**
	 * 	Get New Measures only
	 *	@return array of unprocessed Measures
	 */
	public MSLAMeasure[] getNewMeasures()
	{
		String sql = "SELECT * FROM PA_SLA_Measure "
			+ "WHERE PA_SLA_Goal_ID=?"
			+ " AND Processed='N' "
			+ "ORDER BY DateTrx";
		return getMeasures (sql);
	}	//	getNewMeasures
	
	/**
	 * 	Get Measures
	 *	@param sql sql
	 *	@return array of measures
	 */
	private MSLAMeasure[] getMeasures (String sql)
	{
		ArrayList<MSLAMeasure> list = new ArrayList<MSLAMeasure>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getPA_SLA_Goal_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MSLAMeasure(getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		MSLAMeasure[] retValue = new MSLAMeasure[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getMeasures
	
	/**
	 * 	Is the Date in the Valid Range
	 *	@param date date
	 *	@return true if valid
	 */
	public boolean isDateValid (Timestamp date)
	{
		if (date == null)
			return false;
		if (getValidFrom() != null && date.before(getValidFrom()))
			return false;
		if (getValidTo() != null && date.after(getValidTo()))
			return false;
		return true;
	}	//	isDateValid
	
}	//	MSLAGoal
