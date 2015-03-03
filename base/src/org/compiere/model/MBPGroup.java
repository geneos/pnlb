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
 *	Business Partner Group Model 
 *	
 *  @author Jorg Janke
 *  @version $Id: MBPGroup.java,v 1.9 2005/11/28 03:36:03 jjanke Exp $
 */
public class MBPGroup extends X_C_BP_Group
{
	/**
	 * 	Get MBPGroup from Cache
	 *	@param ctx context
	 *	@param C_BP_Group_ID id
	 *	@return MBPGroup
	 */
	public static MBPGroup get (Properties ctx, int C_BP_Group_ID)
	{
		Integer key = new Integer (C_BP_Group_ID);
		MBPGroup retValue = (MBPGroup) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MBPGroup (ctx, C_BP_Group_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get MBPGroup from Business Partner
	 *	@param ctx context
	 *	@param C_BPartner_ID business partner id
	 *	@return MBPGroup
	 */
	public static MBPGroup getOfBPartner (Properties ctx, int C_BPartner_ID)
	{
		MBPGroup retValue = null;
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM C_BP_Group g "
			+ "WHERE EXISTS (SELECT * FROM C_BPartner p "
				+ "WHERE p.C_BPartner_ID=? AND p.C_BP_Group_ID=g.C_BP_Group_ID)";
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MBPGroup (ctx, rs, null);
				Integer key = new Integer (retValue.getC_BP_Group_ID());
				if (retValue.get_ID () != 0)
					s_cache.put (key, retValue);
			}
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
		
		return retValue;
	}	//	getOfBPartner
	
	/**	Cache						*/
	private static CCache<Integer,MBPGroup>	s_cache
		= new CCache<Integer,MBPGroup>("BP_Group", 10);
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MBPGroup.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BP_Group_ID id
	 */
	public MBPGroup (Properties ctx, int C_BP_Group_ID, String trxName)
	{
		super (ctx, C_BP_Group_ID, trxName);
		if (C_BP_Group_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setIsConfidentialInfo (false);	// N
			setIsDefault (false);
			setPriorityBase(PRIORITYBASE_Same);
		}	
	}	//	MBPGroup

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MBPGroup (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBPGroup
	
	
	/**
	 * 	Get Credit Watch Percent
	 *	@return 90 or defined percent
	 */
	public BigDecimal getCreditWatchPercent ()
	{
		BigDecimal bd = super.getCreditWatchPercent();
		if (bd != null)
			return bd;
		return new BigDecimal(90);
	}	//	getCreditWatchPercent

	/**
	 * 	Get Credit Watch Ratio
	 *	@return 0.90 or defined percent
	 */
	public BigDecimal getCreditWatchRatio()
	{
		BigDecimal bd = super.getCreditWatchPercent();
		if (bd != null)
			return bd.divide(Env.ONEHUNDRED, 2, BigDecimal.ROUND_HALF_UP);
		return new BigDecimal(0.90);
	}	//	getCreditWatchRatio

	
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		// TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * 	After Save
	 *	@param newRecord new record
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord & success)
			return insert_Accounting("C_BP_Group_Acct", "C_AcctSchema_Default", null);
		return success;
	}	//	afterSave
	
	
	/**
	 * 	Before Delete
	 *	@return true
	 */
	protected boolean beforeDelete ()
	{
		return delete_Accounting("C_BP_Group_Acct");
	}	//	beforeDelete
	
}	//	MBPGroup
