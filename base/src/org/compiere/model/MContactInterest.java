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
 *  Business Partner Contact Interest
 *
 *  @author Jorg Janke
 *  @version $Id: MContactInterest.java,v 1.10 2005/03/11 20:26:04 jjanke Exp $
 */
public class MContactInterest extends X_R_ContactInterest
{
	/**
	 * 	Get Contact Interest
	 *	@param ctx context
	 *	@param R_InterestArea_ID interest ares
	 *	@param AD_User_ID user
	 *	@return Contact Interest 
	 */
	public static MContactInterest get (Properties ctx, int R_InterestArea_ID, int AD_User_ID)
	{
		MContactInterest retValue = null;
		String sql = "SELECT * FROM R_ContactInterest "
			+ "WHERE R_InterestArea_ID=? AND AD_User_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, R_InterestArea_ID);
			pstmt.setInt(2, AD_User_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MContactInterest (ctx, rs, null);
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "get", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}		
		if (retValue == null)
		{
			retValue = new MContactInterest (ctx, R_InterestArea_ID, AD_User_ID, null);
			s_log.fine("get - NOT found - " + retValue);
		}
		else
			s_log.fine("get - found - " + retValue);
		return retValue;
	}	//	get

	
	/**************************************************************************
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MContactInterest (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MContactInterest

	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param R_InterestArea_ID interest area
	 * 	@param AD_User_ID partner contact
	 */
	public MContactInterest (Properties ctx, int R_InterestArea_ID, int AD_User_ID, String trxName)
	{
		super(ctx, 0, trxName);
		setR_InterestArea_ID (R_InterestArea_ID);
		setAD_User_ID (AD_User_ID);
		setIsActive(false);
	}	//	MContactInterest

	/**
	 *  Create & Load existing Persistent Object.
	 *  @param ctx context
	 *  @param rs load from current result set position (no navigation, not closed)
	 */
	public MContactInterest (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MContactInterest

	/**	Static Logger				*/
	private static CLogger		s_log = CLogger.getCLogger (MContactInterest.class);

	/**
	 * 	Set OptOut Date
	 *	@param OptOutDate date
	 */
	public void setOptOutDate (Timestamp OptOutDate)
	{
		if (OptOutDate == null)
			OptOutDate = new Timestamp(System.currentTimeMillis());
		log.fine("setOptOutDate - " + OptOutDate);
		super.setOptOutDate(OptOutDate);
		setIsActive(false);
	}	//	setOptOutDate
	
	/**
	 * 	Unsubscribe
	 */
	public void unsubscribe()
	{
		setOptOutDate(null);
	}	//	unsubscribe

	/**
	 * 	Set Subscribe Date
	 *	@param SubscribeDate date
	 */
	public void setSubscribeDate (Timestamp SubscribeDate)
	{
		if (SubscribeDate == null)
			SubscribeDate = new Timestamp(System.currentTimeMillis());
		log.fine("setSubscribeDate - " + SubscribeDate);
		super.setSubscribeDate(SubscribeDate);
		super.setOptOutDate(null);
		setIsActive(true);
	}	//	setSubscribeDate

	/**
	 * 	Subscribe
	 */
	public void subscribe()
	{
		setSubscribeDate(null);
	}	//	subscribe

	/**
	 * 	Subscribe
	 */
	public void subscribe (boolean subscribe)
	{
		if (subscribe)
			setSubscribeDate(null);
		else
			setOptOutDate(null);
	}	//	subscribe


	/**
	 * 	Is Subscribed
	 *	@return true if subscribed
	 */
	public boolean isSubscribed()
	{
		if (!isActive() || getOptOutDate() != null)
			return false;
		return true;
	}	//	isSubscribed


	/**
	 * 	String representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MContactInterest[")
			.append("R_InterestArea_ID=").append(getR_InterestArea_ID())
			.append(",AD_User_ID=").append(getAD_User_ID())
			.append(",Subscribed=").append(isSubscribed())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**************************************************************************
	 */
	public static void main (String[] args)
	{
		org.compiere.Compiere.startup(true);
		int R_InterestArea_ID = 1000002;
		int AD_User_ID = 1000002;
		MContactInterest ci = MContactInterest.get(Env.getCtx(), R_InterestArea_ID, AD_User_ID);
		ci.subscribe();
		ci.save();
		//
		ci = MContactInterest.get(Env.getCtx(), R_InterestArea_ID, AD_User_ID);
	}	//	main


}	//	MContactInterest
