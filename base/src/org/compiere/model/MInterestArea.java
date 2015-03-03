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

import org.compiere.util.*;

/**
 *  Interest Area.
 * 	Note: if model is changed, update
 * 	org.compiere.wstore.Info.getInterests()
 *  manually
 *
 *  @author Jorg Janke
 *  @version $Id: MInterestArea.java,v 1.12 2005/10/14 00:44:31 jjanke Exp $
 */
public class MInterestArea extends X_R_InterestArea
{
	/**
	 * 	Get MInterestArea from Cache
	 *	@param ctx context
	 *	@param R_InterestArea_ID id
	 *	@return MInterestArea
	 */
	public static MInterestArea get (Properties ctx, int R_InterestArea_ID)
	{
		Integer key = new Integer (R_InterestArea_ID);
		MInterestArea retValue = (MInterestArea) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MInterestArea (ctx, R_InterestArea_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MInterestArea> s_cache = new CCache<Integer,MInterestArea>("R_InterestArea", 5);
	
	
	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param R_InterestArea_ID interest area
	 */
	public MInterestArea (Properties ctx, int R_InterestArea_ID, String trxName)
	{
		super (ctx, R_InterestArea_ID, trxName);
		if (R_InterestArea_ID == 0)
		{
		//	setName (null);
		//	setR_InterestArea_ID (0);
		}
	}	//	MInterestArea

	/**
	 * 	Loader Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MInterestArea (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInterestArea


	/**
	 * 	String representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInterestArea[")
			.append (get_ID()).append(" - ").append(getName())
			.append ("]");
		return sb.toString ();
	}

	/*************************************************************************/

	private int 				m_AD_User_ID = -1;
	private MContactInterest 	m_ci = null;

	/**
	 * 	Set Subscription info "constructor"
	 *	@param AD_User_ID contact
	 */
	public void setSubscriptionInfo (int AD_User_ID)
	{
		m_AD_User_ID = AD_User_ID;
		m_ci = MContactInterest.get(getCtx(), getR_InterestArea_ID(), AD_User_ID);
	}	//	setSubscription

	public void setAD_User_ID (int AD_User_ID)
	{
		m_AD_User_ID = AD_User_ID;
	}

	public int getAD_User_ID ()
	{
		return m_AD_User_ID;
	}


	public Timestamp getSubscribeDate ()
	{
		if (m_ci != null)
			return m_ci.getSubscribeDate();
		return null;
	}

	public Timestamp getOptOutDate ()
	{
		if (m_ci != null)
			return m_ci.getOptOutDate();
		return null;
	}

	/**
	 * 	Is Subscribed
	 *	@return true if sunscribed
	 */
	public boolean isSubscribed()
	{
		if (m_AD_User_ID <= 0 || m_ci == null)
			return false;
		//	We have a BPartner Contact
		return m_ci.isSubscribed();
	}	//	isSubscribed

}	//	MInterestArea
