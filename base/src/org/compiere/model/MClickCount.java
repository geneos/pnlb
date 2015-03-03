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
import java.text.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 * 	Click Count (header)
 *
 *  @author Jorg Janke
 *  @version $Id: MClickCount.java,v 1.12 2005/11/06 01:17:27 jjanke Exp $
 */
public class MClickCount extends X_W_ClickCount
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param W_ClickCount_ID id
	 */
	public MClickCount (Properties ctx, int W_ClickCount_ID, String trxName)
	{
		super (ctx, W_ClickCount_ID, trxName);
		if (W_ClickCount_ID == 0)
		{
		//	setName (null);
		//	setTargetURL (null);
		}
	}	//	MClickCount
	
	/** 
	 * 	Load Constructor
	 * 	@param ctx context
	 * 	@param rs result set 
	 */
	public MClickCount (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MClickCount

	/** 
	 * 	Parent Constructor
	 * 	@param ad parent
	 */
	public MClickCount (MAdvertisement ad)
	{
		this (ad.getCtx(), 0, ad.get_TrxName());
		setName(ad.getName());
		setTargetURL("#");
		setC_BPartner_ID(ad.getC_BPartner_ID());
	}	//	MClickCount
	
	private SimpleDateFormat		m_dateFormat = DisplayType.getDateFormat(DisplayType.Date);
	private DecimalFormat			m_intFormat = DisplayType.getNumberFormat(DisplayType.Integer);

	
	/**************************************************************************
	 * 	Get Clicks
	 *	@return clicks
	 */
	public MClick[] getMClicks()
	{
		ArrayList<MClick> list = new ArrayList<MClick>();
		/** @todo Clicks */
		//
		MClick[] retValue = new MClick[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getMClicks

	/**
	 * 	Get Count for date format
	 *	@param DateFormat valid TRUNC date format
	 *	@return count
	 */
	protected ValueNamePair[] getCount (String DateFormat)
	{
		ArrayList<ValueNamePair> list = new ArrayList<ValueNamePair>();
		String sql = "SELECT TRUNC(Created, '" + DateFormat + "'), Count(*) "
			+ "FROM W_Click "
			+ "WHERE W_ClickCount_ID=? "
			+ "GROUP BY TRUNC(Created, '" + DateFormat + "')";
		//
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, getW_ClickCount_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				String value = m_dateFormat.format(rs.getTimestamp(1));
				String name = m_intFormat.format(rs.getInt(2));
				ValueNamePair pp = new ValueNamePair (value, name);
				list.add(pp);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		ValueNamePair[] retValue = new ValueNamePair[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getCount

	/**
	 * 	Get Monthly Count
	 *	@return monthly count
	 */
	public ValueNamePair[] getCountQuarter ()
	{
		return getCount("Q");
	}	//	getCountQuarter

	/**
	 * 	Get Monthly Count
	 *	@return monthly count
	 */
	public ValueNamePair[] getCountMonth ()
	{
		return getCount("MM");
	}	//	getCountMonth

	/**
	 * 	Get Weekly Count
	 *	@return weekly count
	 */
	public ValueNamePair[] getCountWeek ()
	{
		return getCount("DY");
	}	//	getCountWeek

	/**
	 * 	Get Daily Count
	 *	@return dailt count
	 */
	public ValueNamePair[] getCountDay ()
	{
		return getCount("J");
	}	//	getCountDay

}	//	MClickCount
