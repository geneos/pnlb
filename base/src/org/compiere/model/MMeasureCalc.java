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
 * 	Performance Measure Calculation
 *	
 *  @author Jorg Janke
 *  @version $Id: MMeasureCalc.java,v 1.3 2006/01/04 03:59:24 jjanke Exp $
 */
public class MMeasureCalc extends X_PA_MeasureCalc
{
	/**
	 * 	Get MMeasureCalc from Cache
	 *	@param ctx context
	 *	@param PA_MeasureCalc_ID id
	 *	@return MMeasureCalc
	 */
	public static MMeasureCalc get (Properties ctx, int PA_MeasureCalc_ID)
	{
		Integer key = new Integer (PA_MeasureCalc_ID);
		MMeasureCalc retValue = (MMeasureCalc)s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MMeasureCalc (ctx, PA_MeasureCalc_ID, null);
		if (retValue.get_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer, MMeasureCalc> s_cache 
		= new CCache<Integer, MMeasureCalc> ("PA_MeasureCalc", 10);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_MeasureCalc_ID id
	 *	@param trxName trx
	 */
	public MMeasureCalc (Properties ctx, int PA_MeasureCalc_ID, String trxName)
	{
		super (ctx, PA_MeasureCalc_ID, trxName);
	}	//	MMeasureCalc

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MMeasureCalc (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MMeasureCalc
	
	
	/**
	 * 	Get Sql to return single value for the Performance Indicator
	 *	@param restrictions array of goal restrictions
	 *	@param MeasureScope scope of this value  
	 *	@oaram MeasureDataType data type
	 *	@param reportDate optional report date
	 *	@return sql for performance indicator
	 */
	public String getSqlPI (MGoalRestriction[] restrictions, 
		String MeasureScope, String MeasureDataType, Timestamp reportDate, MRole role)
	{
		StringBuffer sb = new StringBuffer(getSelectClause())
			.append(" ")
			.append(getWhereClause());
		//	Date Restriction
		if (getDateColumn() != null 
			&& MMeasure.MEASUREDATATYPE_QtyAmountInTime.equals(MeasureDataType)
			&& !MGoal.MEASUREDISPLAY_Total.equals(MeasureScope))
		{
			if (reportDate == null)
				reportDate = new Timestamp(System.currentTimeMillis());
			String dateString = DB.TO_DATE(reportDate);
			String trunc = "D";
			if (MGoal.MEASUREDISPLAY_Year.equals(MeasureScope))
				trunc = "Y";
			else if (MGoal.MEASUREDISPLAY_Quarter.equals(MeasureScope))
				trunc = "Q";
			else if (MGoal.MEASUREDISPLAY_Month.equals(MeasureScope))
				trunc = "MM";
			else if (MGoal.MEASUREDISPLAY_Week.equals(MeasureScope))
				trunc = "W";
		//	else if (MGoal.MEASUREDISPLAY_Day.equals(MeasureDisplay))
		//		;
			sb.append(" AND TRUNC(")
				.append(getDateColumn()).append(",'").append(trunc).append("')=TRUNC(")
				.append(DB.TO_DATE(reportDate)).append(",'").append(trunc).append("')");
		}	//	date
		String sql = addRestrictions(sb.toString(), restrictions, role);
		
		log.fine(sql);
		return sql;
	}	//	getSql
	
	/**
	 * 	Get Sql to value for the bar chart
	 *	@param restrictions array of goal restrictions
	 *	@param MeasureDisplay scope of this value  
	 *	@param startDate optional report start date
	 *	@return sql for Bar Chart
	 */
	public String getSqlBarChart (MGoalRestriction[] restrictions, 
		String MeasureDisplay, Timestamp startDate, MRole role)
	{
		StringBuffer sb = new StringBuffer();
		String dateCol = null;
		String groupBy = null;
		if (getDateColumn() != null 
			&& !MGoal.MEASUREDISPLAY_Total.equals(MeasureDisplay))
		{
			String trunc = "D";
			if (MGoal.MEASUREDISPLAY_Year.equals(MeasureDisplay))
				trunc = "Y";
			else if (MGoal.MEASUREDISPLAY_Quarter.equals(MeasureDisplay))
				trunc = "Q";
			else if (MGoal.MEASUREDISPLAY_Month.equals(MeasureDisplay))
				trunc = "MM";
			else if (MGoal.MEASUREDISPLAY_Week.equals(MeasureDisplay))
				trunc = "W";
		//	else if (MGoal.MEASUREDISPLAY_Day.equals(MeasureDisplay))
		//		;
			dateCol = "TRUNC(" + getDateColumn() + ",'" + trunc + "') ";
			groupBy = dateCol; 
		}
		else
			dateCol = "MAX(" + getDateColumn() + ") ";
		//
		String selectFrom = getSelectClause();
		int index = selectFrom.indexOf("FROM ");
		if (index == -1)
			index = selectFrom.toUpperCase().indexOf("FROM ");
		if (index == -1)
			throw new IllegalArgumentException("Cannot find FROM in sql - " + selectFrom);
		sb.append(selectFrom.substring(0, index))
			.append(",").append(dateCol)
			.append(selectFrom.substring(index));
		
		//	** WHERE
		sb.append(" ")
			.append(getWhereClause());
		//	Date Restriction
		if (getDateColumn() != null
			&& startDate != null
			&& !MGoal.MEASUREDISPLAY_Total.equals(MeasureDisplay))
		{
			String dateString = DB.TO_DATE(startDate);
			sb.append(" AND ").append(getDateColumn())
				.append(">=").append(dateString);
		}	//	date
		String sql = addRestrictions(sb.toString(), restrictions, role);
		if (groupBy != null)
			sql += " GROUP BY " + groupBy;
		//
		log.fine(sql);
		return sql;
	}	//	getSqlBarChart
	
	/**
	 * 	Get Zoom Query
	 *	@return query
	 */
	public MQuery getQuery(MGoalRestriction[] restrictions, 
		String MeasureDisplay, Timestamp date, MRole role)
	{
		MQuery query = new MQuery(getAD_Table_ID());
		//
		StringBuffer sql = new StringBuffer("SELECT ").append(getKeyColumn()).append(" ");
		String from = getSelectClause();
		int index = from.indexOf("FROM ");
		if (index == -1)
			throw new IllegalArgumentException("Cannot find FROM " + from);
		sql.append(from.substring(index)).append(" ")
			.append(getWhereClause());
		//	Date Range
		if (getDateColumn() != null 
			&& !MGoal.MEASUREDISPLAY_Total.equals(MeasureDisplay))
		{
			String trunc = "D";
			if (MGoal.MEASUREDISPLAY_Year.equals(MeasureDisplay))
				trunc = "Y";
			else if (MGoal.MEASUREDISPLAY_Quarter.equals(MeasureDisplay))
				trunc = "Q";
			else if (MGoal.MEASUREDISPLAY_Month.equals(MeasureDisplay))
				trunc = "MM";
			else if (MGoal.MEASUREDISPLAY_Week.equals(MeasureDisplay))
				trunc = "W";
		//	else if (MGoal.MEASUREDISPLAY_Day.equals(MeasureDisplay))
		//		;
			sql.append(" AND TRUNC(").append(getDateColumn()).append(",'").append(trunc)
				.append("')=TRUNC(").append(DB.TO_DATE(date)).append(",'").append(trunc).append("')");
		}
		String finalSQL = addRestrictions(sql.toString(), restrictions, role);
		//	Execute
		StringBuffer where = new StringBuffer();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (finalSQL, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				int id = rs.getInt(1);
				if (where.length() > 0)
					where.append(",");
				where.append(id);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, finalSQL, e);
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
		if (where.length() == 0)
			return MQuery.getNoRecordQuery(query.getTableName(), false);
		//
		StringBuffer whereClause = new StringBuffer (getKeyColumn())
			.append(" IN (").append(where).append(")");
		query.addRestriction(whereClause.toString());
		return query;
	}	//	getQuery
	
	/**
	 * 	Add Restrictions
	 *	@param sql existing sql
	 *	@param restrictions restrictions
	 */
	private String addRestrictions(String sql, 
		MGoalRestriction[] restrictions, MRole role)
	{
		StringBuffer sb = new StringBuffer(sql);
		//	Org Restrictions
		if (getOrgColumn() != null)
		{
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < restrictions.length; i++)
			{
				if (MGoalRestriction.GOALRESTRICTIONTYPE_Organization.equals(restrictions[i].getGoalRestrictionType()))
					list.add(restrictions[i].getOrg_ID());
				//	Hierarchy comes here
			}
			if (list.size() == 1)
				sb.append(" AND ").append(getOrgColumn())
					.append("=").append(list.get(0));
			else if (list.size() > 1)
			{
				sb.append(" AND ").append(getOrgColumn()).append(" IN (");
				for (int i = 0; i < list.size(); i++)
				{
					if (i > 0)
						sb.append(",");
					sb.append(list.get(i));
				}
				sb.append(")");
			}
		}	//	org
		
		//	BPartner Restrictions
		if (getBPartnerColumn() != null)
		{
			ArrayList<Integer> listBP = new ArrayList<Integer>();
			ArrayList<Integer> listBPG = new ArrayList<Integer>();
			for (int i = 0; i < restrictions.length; i++)
			{
				if (MGoalRestriction.GOALRESTRICTIONTYPE_BusinessPartner.equals(restrictions[i].getGoalRestrictionType()))
					listBP.add(restrictions[i].getC_BPartner_ID());
				//	Hierarchy comes here
				if (MGoalRestriction.GOALRESTRICTIONTYPE_BusPartnerGroup.equals(restrictions[i].getGoalRestrictionType()))
					listBPG.add(restrictions[i].getC_BP_Group_ID());
			}
			//	BP
			if (listBP.size() == 1)
				sb.append(" AND ").append(getBPartnerColumn())
					.append("=").append(listBP.get(0));
			else if (listBP.size() > 1)
			{
				sb.append(" AND ").append(getBPartnerColumn()).append(" IN (");
				for (int i = 0; i < listBP.size(); i++)
				{
					if (i > 0)
						sb.append(",");
					sb.append(listBP.get(i));
				}
				sb.append(")");
			}
			//	BPG
			if (listBPG.size() == 1)
				sb.append(" AND EXISTS (SELECT * FROM C_BPartner bpx WHERE ")
					.append(getBPartnerColumn())
					.append("=bpx.C_BPartner_ID AND bpx.C_BP_GROUP_ID=")
					.append(listBPG.get(0)).append(")"); 
			else if (listBPG.size() > 1)
			{
				sb.append(" AND EXISTS (SELECT * FROM C_BPartner bpx WHERE ")
					.append(getBPartnerColumn())
					.append("=bpx.C_BPartner_ID AND bpx.C_BP_GROUP_ID IN (");
				for (int i = 0; i < listBPG.size(); i++)
				{
					if (i > 0)
						sb.append(",");
					sb.append(listBPG.get(i));
				}
				sb.append("))");
			}
		}	//	bp
		
		//	Product Restrictions
		if (getProductColumn() != null)
		{
			ArrayList<Integer> listP = new ArrayList<Integer>();
			ArrayList<Integer> listPC = new ArrayList<Integer>();
			for (int i = 0; i < restrictions.length; i++)
			{
				if (MGoalRestriction.GOALRESTRICTIONTYPE_Product.equals(restrictions[i].getGoalRestrictionType()))
					listP.add(restrictions[i].getM_Product_ID());
				//	Hierarchy comes here
				if (MGoalRestriction.GOALRESTRICTIONTYPE_ProductCategory.equals(restrictions[i].getGoalRestrictionType()))
					listPC.add(restrictions[i].getM_Product_Category_ID());
			}
			//	Product
			if (listP.size() == 1)
				sb.append(" AND ").append(getProductColumn())
					.append("=").append(listP.get(0));
			else if (listP.size() > 1)
			{
				sb.append(" AND ").append(getProductColumn()).append(" IN (");
				for (int i = 0; i < listP.size(); i++)
				{
					if (i > 0)
						sb.append(",");
					sb.append(listP.get(i));
				}
				sb.append(")");
			}
			//	Category
			if (listPC.size() == 1)
				sb.append(" AND EXISTS (SELECT * FROM M_Product px WHERE ")
					.append(getProductColumn())
					.append("=px.M_Product_ID AND px.M_Product_Category_ID=")
					.append(listPC.get(0)).append(")"); 
			else if (listPC.size() > 1)
			{
				sb.append(" AND EXISTS (SELECT * FROM M_Product px WHERE ")
				.append(getProductColumn())
				.append("=px.M_Product_ID AND px.M_Product_Category_ID IN (");
				for (int i = 0; i < listPC.size(); i++)
				{
					if (i > 0)
						sb.append(",");
					sb.append(listPC.get(i));
				}
				sb.append("))");
			}
		}	//	product
		String finalSQL = sb.toString();
		if (role == null)
			role = MRole.getDefault();
		String retValue = role.addAccessSQL(sql, getTableName(), true, false);
		return retValue;
	}	//	addRestrictions

	/**
	 * 	Get Table Name
	 *	@return Table Name
	 */
	public String getTableName()
	{
		return M_Table.getTableName (Env.getCtx(), getAD_Table_ID());
	}	//	getTavleName
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MMeasureCalc[");
		sb.append (get_ID()).append ("-").append (getName()).append ("]");
		return sb.toString ();
	}	//	toString
	
	
}	//	MMeasureCalc
