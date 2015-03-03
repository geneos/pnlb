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

import java.io.*;
import java.sql.*;
import java.util.*;

import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Localtion Region Model (Value Object)
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MRegion.java,v 1.15 2005/11/14 02:28:58 jjanke Exp $
 */
public final class MRegion extends X_C_Region
	implements Comparator, Serializable
{
	/**
	 * 	Load Regions (cached)
	 *	@param ctx context
	 */
	private static void loadAllRegions (Properties ctx)
	{
		s_regions = new CCache<String,MRegion>("C_Region", 100);
		String sql = "SELECT * FROM C_Region WHERE IsActive='Y'";
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				MRegion r = new MRegion (ctx, rs, null);
				s_regions.put(String.valueOf(r.getC_Region_ID()), r);
				if (r.isDefault())
					s_default = r;
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		s_log.fine(s_regions.size() + " - default=" + s_default);
	}	//	loadAllRegions

	/**
	 * 	Get Country (cached)
	 * 	@param ctx context
	 *	@param C_Region_ID ID
	 *	@return Country
	 */
	public static MRegion get (Properties ctx, int C_Region_ID)
	{
		if (s_regions == null || s_regions.size() == 0)
			loadAllRegions(ctx);
		String key = String.valueOf(C_Region_ID);
		MRegion r = (MRegion)s_regions.get(key);
		if (r != null)
			return r;
		r = new MRegion (ctx, C_Region_ID, null);
		if (r.getC_Region_ID() == C_Region_ID)
		{
			s_regions.put(key, r);
			return r;
		}
		return null;
	}	//	get

	/**
	 * 	Get Default Region
	 * 	@param ctx context
	 *	@return Region or null
	 */
	public static MRegion getDefault (Properties ctx)
	{
		if (s_regions == null || s_regions.size() == 0)
			loadAllRegions(ctx);
		return s_default;
	}	//	get

	/**
	 *	Return Regions as Array
	 * 	@param ctx context
	 *  @return MCountry Array
	 */
	@SuppressWarnings("unchecked")
	public static MRegion[] getRegions(Properties ctx)
	{
		if (s_regions == null || s_regions.size() == 0)
			loadAllRegions(ctx);
		MRegion[] retValue = new MRegion[s_regions.size()];
		s_regions.values().toArray(retValue);
		Arrays.sort(retValue, new MRegion(ctx, 0, null));
		return retValue;
	}	//	getRegions

	/**
	 *	Return Array of Regions of Country
	 * 	@param ctx context
	 *  @param C_Country_ID country
	 *  @return MRegion Array
	 */
	@SuppressWarnings("unchecked")
	public static MRegion[] getRegions (Properties ctx, int C_Country_ID)
	{
		if (s_regions == null || s_regions.size() == 0)
			loadAllRegions(ctx);
		ArrayList<MRegion> list = new ArrayList<MRegion>();
		Iterator it = s_regions.values().iterator();
		while (it.hasNext())
		{
			MRegion r = (MRegion)it.next();
			if (r.getC_Country_ID() == C_Country_ID)
				list.add(r);
		}
		//  Sort it
		MRegion[] retValue = new MRegion[list.size()];
		list.toArray(retValue);
		Arrays.sort(retValue, new MRegion(ctx, 0, null));
		return retValue;
	}	//	getRegions

	/**	Region Cache				*/
	private static CCache<String,MRegion> s_regions = null;
	/** Default Region				*/
	private static MRegion		s_default = null;
	/**	Static Logger				*/
	private static CLogger		s_log = CLogger.getCLogger (MRegion.class);

	
	/**************************************************************************
	 *	Create empty Region
	 * 	@param ctx context
	 * 	@param C_Region_ID id
	 */
	public MRegion (Properties ctx, int C_Region_ID, String trxName)
	{
		super (ctx, C_Region_ID, trxName);
		if (C_Region_ID == 0)
		{
		}
	}   //  MRegion

	/**
	 *	Create Region from current row in ResultSet
	 * 	@param ctx context
	 *  @param rs result set
	 */
	public MRegion (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRegion

	/**
	 *	Return Name
	 *  @return Name
	 */
	public String toString()
	{
		return getName();
	}   //  toString

	/**
	 *  Compare
	 *  @param o1 object 1
	 *  @param o2 object 2
	 *  @return -1,0, 1
	 */
	public int compare(Object o1, Object o2)
	{
		String s1 = o1.toString();
		if (s1 == null)
			s1 = "";
		String s2 = o2.toString();
		if (s2 == null)
			s2 = "";
		return s1.compareTo(s2);
	}	//	compare

}	//	MRegion
