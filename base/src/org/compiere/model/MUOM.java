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
 *	Unit Of Measure Model
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MUOM.java,v 1.10 2005/11/14 02:10:52 jjanke Exp $
 */
public class MUOM extends X_C_UOM
{
	/**
	 *	Constructor.
	 *	@param ctx context
	 *  @param C_UOM_ID UOM ID
	 */
	public MUOM (Properties ctx, int C_UOM_ID, String trxName)
	{
		super (ctx, C_UOM_ID, trxName);
		if (C_UOM_ID == 0)
		{
		//	setName (null);
		//	setX12DE355 (null);
			setIsDefault (false);
			setStdPrecision (2);
			setCostingPrecision (6);
		}
	}	//	UOM

	/**
	 *	Load Constructor.
	 *	@param ctx context
	 *  @param rs result set
	 */
	public MUOM (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	UOM

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("UOM[");
		sb.append("ID=").append(get_ID())
			.append(", Name=").append(getName());
		return sb.toString();
	}	//	toString

	/**
	 * 	Round qty
	 *	@param qty quantity
	 *	@param stdPrecision true if std precisison
	 *	@return rounded quantity
	 */
	public BigDecimal round (BigDecimal qty, boolean stdPrecision)
	{
		int precision = getStdPrecision();
		if (!stdPrecision)
			precision = getCostingPrecision();
		if (qty.scale() > precision)
			return qty.setScale(getStdPrecision(), BigDecimal.ROUND_HALF_UP);
		return qty;
	}	//	round


	public boolean isMinute()
	{
		return X12_MINUTE.equals(getX12DE355());
	}
	public boolean isHour()
	{
		return X12_HOUR.equals(getX12DE355());
	}
	public boolean isDay()
	{
		return X12_DAY.equals(getX12DE355());
	}
	public boolean isWorkDay()
	{
		return X12_DAY_WORK.equals(getX12DE355());
	}
	public boolean isWeek()
	{
		return X12_WEEK.equals(getX12DE355());
	}
	public boolean isMonth()
	{
		return X12_MONTH.equals(getX12DE355());
	}
	public boolean isWorkMonth()
	{
		return X12_MONTH_WORK.equals(getX12DE355());
	}
	public boolean isYear()
	{
		return X12_YEAR.equals(getX12DE355());
	}

	/*************************************************************************/

	/** X12 Element 355 Code	Minute	*/
	static final String		X12_MINUTE = "MJ";
	/** X12 Element 355 Code	Hour	*/
	static final String		X12_HOUR = "HR";
	/** X12 Element 355 Code	Day 	*/
	static final String		X12_DAY = "DA";
	/** X12 Element 355 Code	Work Day (8 hours / 5days)	 	*/
	static final String		X12_DAY_WORK = "WD";
	/** X12 Element 355 Code	Week 	*/
	static final String		X12_WEEK = "WK";
	/** X12 Element 355 Code	Month 	*/
	static final String		X12_MONTH = "MO";
	/** X12 Element 355 Code	Work Month (20 days / 4 weeks) 	*/
	static final String		X12_MONTH_WORK = "WM";
	/** X12 Element 355 Code	Year 	*/
	static final String		X12_YEAR = "YR";

	/**	Logger			*/
	private static CLogger s_log = CLogger.getCLogger(MUOM.class);

	/**
	 * 	Get Minute C_UOM_ID
	 *  @param ctx context
	 * 	@return C_UOM_ID for Minute
	 */
	public static int getMinute_UOM_ID (Properties ctx)
	{
		if (Ini.isClient())
		{
			Iterator it = s_cache.values().iterator();
			while (it.hasNext())
			{
				MUOM uom = (MUOM)it.next();
				if (uom.isMinute())
					return uom.getC_UOM_ID();
			}
		}
		//	Server
		int C_UOM_ID = 0;
		String sql = "SELECT C_UOM_ID FROM C_UOM "
			+ "WHERE IsActive='Y' AND X12DE355='MJ'";	//	HardCoded
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				C_UOM_ID = rs.getInt(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		return C_UOM_ID;
	}	//	getMinute_UOM_ID

	/**
	 * 	Get Default C_UOM_ID
	 *	@param ctx context for AD_Client
	 *	@return C_UOM_ID
	 */
	public static int getDefault_UOM_ID (Properties ctx)
	{
		String sql = "SELECT C_UOM_ID "
			+ "FROM C_UOM "
			+ "WHERE AD_Client_ID IN (0,?) "
			+ "ORDER BY IsDefault DESC, AD_Client_ID DESC, C_UOM_ID";
		return DB.getSQLValue(null, sql, Env.getAD_Client_ID(ctx));
	}	//	getDefault_UOM_ID

	/*************************************************************************/

	/**	UOM Cache				*/
	private static CCache<Integer,MUOM>	s_cache 
		= new CCache<Integer,MUOM>("C_UOM", 30);

	/**
	 * 	Get UOM from Cache
	 * 	@param ctx context
	 *	@param C_UOM_ID ID
	 * 	@return UOM
	 */
	public static MUOM get (Properties ctx, int C_UOM_ID)
	{
		if (s_cache.size() == 0)
			loadUOMs (ctx);
		//
		Integer ii = new Integer (C_UOM_ID);
		MUOM uom = (MUOM)s_cache.get(ii);
		if (uom != null)
			return uom;
		//
		uom = new MUOM (ctx, C_UOM_ID, null);
		s_cache.put(new Integer(C_UOM_ID), uom);
		return uom;
	}	//	getUOMfromCache

	/**
	 * 	Load All UOMs
	 * 	@param ctx context
	 */
	private static void loadUOMs (Properties ctx)
	{
		String sql = MRole.getDefault(ctx, false).addAccessSQL(
			"SELECT * FROM C_UOM "
			+ "WHERE IsActive='Y'",
			"C_UOM", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MUOM uom = new MUOM(ctx, rs, null);
				s_cache.put (new Integer(uom.getC_UOM_ID()), uom);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
	}	//	loadUOMs

}	//	MUOM
