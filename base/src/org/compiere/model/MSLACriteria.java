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

import org.compiere.sla.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Service Level Agreement Criteria Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MSLACriteria.java,v 1.7 2005/11/14 02:10:52 jjanke Exp $
 */
public class MSLACriteria extends X_PA_SLA_Criteria
{
	/**
	 * 	Get MSLACriteria from Cache
	 *	@param ctx context
	 *	@param PA_SLA_Criteria_ID id
	 *	@return MSLACriteria
	 */
	public static MSLACriteria get (Properties ctx, int PA_SLA_Criteria_ID, String trxName)
	{
		Integer key = new Integer (PA_SLA_Criteria_ID);
		MSLACriteria retValue = (MSLACriteria) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MSLACriteria (ctx, PA_SLA_Criteria_ID, trxName);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MSLACriteria>	s_cache	= new CCache<Integer,MSLACriteria>("PA_SLA_Criteria", 20);
	
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_SLA_Criteria_ID id
	 */
	public MSLACriteria (Properties ctx, int PA_SLA_Criteria_ID, String trxName)
	{
		super (ctx, PA_SLA_Criteria_ID, trxName);
	}	//	MSLACriteria

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MSLACriteria (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MSLACriteria

	/**
	 * 	Get Goals of Criteria
	 *	@return array of Goals
	 */
	public MSLAGoal[] getGoals()
	{
		String sql = "SELECT * FROM PA_SLA_Goal "
			+ "WHERE PA_SLA_Criteria_ID=?";
		ArrayList<MSLAGoal> list = new ArrayList<MSLAGoal>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getPA_SLA_Criteria_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MSLAGoal(getCtx(), rs, get_TrxName()));
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
		MSLAGoal[] retValue = new MSLAGoal[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getGoals
	
	
	/**
	 * 	Create New Instance of SLA Criteria
	 *	@return instanciated class
	 *	@throws Exception
	 */
	public SLACriteria newInstance() throws Exception
	{
		if (getClassname() == null || getClassname().length() == 0)
			throw new CompiereSystemError("No SLA Criteria Classname");
		
		try
		{
			Class clazz = Class.forName(getClassname());
			SLACriteria retValue = (SLACriteria)clazz.newInstance();
			return retValue;
		}
		catch (Exception e)
		{
			throw new CompiereSystemError("Could not intsnciate SLA Criteria", e);
		}
	}	//	newInstance
	
}	//	MSLACriteria
