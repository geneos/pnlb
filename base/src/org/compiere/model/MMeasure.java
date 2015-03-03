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
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Performance Measure
 *	
 *  @author Jorg Janke
 *  @version $Id: MMeasure.java,v 1.3 2006/01/04 03:59:24 jjanke Exp $
 */
public class MMeasure extends X_PA_Measure
{
	/**
	 * 	Get MMeasure from Cache
	 *	@param ctx context
	 *	@param PA_Measure_ID id
	 *	@return MMeasure
	 */
	public static MMeasure get (Properties ctx, int PA_Measure_ID)
	{
		Integer key = new Integer (PA_Measure_ID);
		MMeasure retValue = (MMeasure)s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MMeasure (ctx, PA_Measure_ID, null);
		if (retValue.get_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer, MMeasure> s_cache 
		= new CCache<Integer, MMeasure> ("PA_Measure", 10);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_Measure_ID id
	 *	@param trxName trx
	 */
	public MMeasure (Properties ctx, int PA_Measure_ID, String trxName)
	{
		super (ctx, PA_Measure_ID, trxName);
	}	//	MMeasure

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MMeasure (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MMeasure
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MMeasure[");
		sb.append (get_ID()).append ("-").append (getName()).append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (MEASURETYPE_Calculated.equals(getMeasureType())
			&& getPA_MeasureCalc_ID() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "PA_MeasureCalc_ID"));
			return false;
		}
		else if (MEASURETYPE_Ratio.equals(getMeasureType())
			&& getPA_Ratio_ID() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "PA_Ratio_ID"));
			return false;
		}
		else if (MEASURETYPE_UserDefined.equals(getMeasureType())
			&& (getCalculationClass() == null || getCalculationClass().length()==0))
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "CalculationClass"));
			return false;
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return succes
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Update Goals with Manual Measure
		if (success && MEASURETYPE_Manual.equals(getMeasureType()))
			updateManualGoals();
		
		return success;
	}	//	afterSave
	
	/**
	 * 	Update Goals
	 * 	@return true if updated
	 */
	public boolean updateGoals()
	{
		String mt = getMeasureType();
		try
		{
			if (MEASURETYPE_Manual.equals(mt))
				return updateManualGoals();
			else if (MEASURETYPE_Achievements.equals(mt))
				return updateAchievementGoals();
			else if (MEASURETYPE_Calculated.equals(mt))
				return updateCalculatedGoals();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "MeasureType=" + mt, e);
		}
		return false;
	}	//	updateGoals
	
	/**
	 * 	Update Manual Goals
	 * 	@return true if updated
	 */
	private boolean updateManualGoals()
	{
		if (!MEASURETYPE_Manual.equals(getMeasureType()))
			return false;
		MGoal[] goals = MGoal.getMeasureGoals (getCtx(), getPA_Measure_ID());
		for (int i = 0; i < goals.length; i++)
		{
			MGoal goal = goals[i];
			goal.setMeasureActual(getManualActual());
			goal.save();
		}
		return true;
	}	//	updateManualGoals
	
	/**
	 * 	Update Goals with Achievement
	 * 	@return true if updated
	 */
	private boolean updateAchievementGoals()
	{
		if (!MEASURETYPE_Achievements.equals(getMeasureType()))
			return false;
		MGoal[] goals = MGoal.getMeasureGoals (getCtx(), getPA_Measure_ID());
		for (int i = 0; i < goals.length; i++)
		{
			MGoal goal = goals[i];
			MAchievement[] achievements = MAchievement.getOfMeasure(getCtx(), getPA_Measure_ID());
			BigDecimal ManualActual = Env.ZERO;
			for (int j = 0; j < achievements.length; j++)
			{
				MAchievement achievement = achievements[j];
				if (achievement.isActive())
					ManualActual = ManualActual.add(achievement.getManualActual());
			}
			goal.setMeasureActual(ManualActual);
			goal.save();
		}
		return true;
	}	//	updateAchievementGoals

	/**
	 * 	Update Goals with Calculation
	 * 	@return true if updated
	 */
	private boolean updateCalculatedGoals()
	{
		if (!MEASURETYPE_Calculated.equals(getMeasureType()))
			return false;
		MGoal[] goals = MGoal.getMeasureGoals (getCtx(), getPA_Measure_ID());
		for (int i = 0; i < goals.length; i++)
		{
			MGoal goal = goals[i];
			//	Find Role
			MRole role = null;
			if (goal.getAD_Role_ID() != 0)
				role = MRole.get(getCtx(), goal.getAD_Role_ID());
			else if (goal.getAD_User_ID() != 0)
			{
				MUser user = MUser.get(getCtx(), goal.getAD_User_ID());
				MRole[] roles = user.getRoles(goal.getAD_Org_ID());
				if (roles.length > 0)
					role = roles[0];
			}
			if (role == null)
				role = MRole.getDefault(getCtx(), false);	//	could result in wrong data
			//
			MMeasureCalc mc = MMeasureCalc.get(getCtx(), getPA_MeasureCalc_ID());
			if (mc == null || mc.get_ID() == 0 || mc.get_ID() != getPA_MeasureCalc_ID())
			{
				log.log(Level.SEVERE, "Not found PA_MeasureCalc_ID=" + getPA_MeasureCalc_ID());
				return false;
			}
			BigDecimal ManualActual = null;
			String sql = mc.getSqlPI(goal.getRestrictions(false), 
				goal.getMeasureScope(), getMeasureDataType(), null, role);		
			PreparedStatement pstmt = null;
			try		//	SQL statement could be wrong
			{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					ManualActual = rs.getBigDecimal(1);
				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, sql, e);
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
			//	SQL may return no rows or null
			if (ManualActual == null)
			{
				ManualActual = Env.ZERO;
				log.fine("No Value = " + sql);
			}
			goal.setMeasureActual(ManualActual);
			goal.save();
		}
		return true;
	}	//	updateCalculatedGoals
	
}	//	MMeasure
