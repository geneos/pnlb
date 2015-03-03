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
 * 	Performance Achievement
 *	
 *  @author Jorg Janke
 *  @version $Id: MAchievement.java,v 1.1 2005/12/27 06:17:56 jjanke Exp $
 */
public class MAchievement extends X_PA_Achievement
{
	/**
	 * 	Get Of Measure
	 *	@param ctx context
	 *	@param PA_Measure_ID id
	 *	@return array of Achievements
	 */
	public static MAchievement[] getOfMeasure (Properties ctx, int PA_Measure_ID)
	{
		ArrayList<MAchievement> list = new ArrayList<MAchievement>();
		String sql = "SELECT * FROM PA_Achievement WHERE PA_Measure_ID=? ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, PA_Measure_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MAchievement (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
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
		//
		MAchievement[] retValue = new MAchievement[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfMeasure
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MAchievement.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_Achievement_ID id
	 *	@param trxName trx
	 */
	public MAchievement (Properties ctx, int PA_Achievement_ID, String trxName)
	{
		super (ctx, PA_Achievement_ID, trxName);
	}	//	MAchievement

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MAchievement (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAchievement
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAchievement[");
		sb.append (get_ID()).append ("-").append (getName()).append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (success)
			updateAchievementGoals();
		return success;
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		if (success)
			updateAchievementGoals();
		return success;
	}	//	afterDelete

	/**
	 * 	Update Goals with Achievement
	 */
	private void updateAchievementGoals()
	{
		MMeasure measure = MMeasure.get (getCtx(), getPA_Measure_ID());
		measure.updateGoals();
	}	//	updateAchievementGoals
	
}	//	MAchievement
