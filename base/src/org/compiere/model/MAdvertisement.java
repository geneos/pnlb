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
 *	Advertisement Model
 *
 *  @author Jorg Janke
 *  @version $Id: MAdvertisement.java,v 1.8 2005/05/06 00:22:58 jjanke Exp $
 */
public class MAdvertisement extends X_W_Advertisement
{
	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param W_Advertisement_ID id
	 */
	public MAdvertisement (Properties ctx, int W_Advertisement_ID, String trxName)
	{
		super (ctx, W_Advertisement_ID, trxName);
		/** if (W_Advertisement_ID == 0)
		{
			setC_BPartner_ID (0);
			setIsSelfService (false);
			setName (null);
			setW_Advertisement_ID (0);
		}
		**/
	}	//	MAdvertisement

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAdvertisement (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAdvertisement

	/**	Click Count					*/
	private MClickCount		m_clickCount = null;
	/** Counter Count				*/
	private MCounterCount	m_counterCount = null;
	/**	Sales Rep					*/
	private int				m_SalesRep_ID = 0;

	
	/**************************************************************************
	 *	Get ClickCount
	 *	@return Click Count
	 */
	public MClickCount getMClickCount()
	{
		if (getW_ClickCount_ID() == 0)
			return null;
		if (m_clickCount == null)
			m_clickCount = new MClickCount (getCtx(), getW_ClickCount_ID(), get_TrxName());
		return m_clickCount;
	}	//	MClickCount

	/**
	 * 	Get Click Target URL (from ClickCount) 
	 *	@return URL
	 */
	public String getClickTargetURL()
	{
		getMClickCount();
		if (m_clickCount == null)
			return "-";
		return m_clickCount.getTargetURL();
	}	//	getClickTargetURL

	/**
	 * 	Set Click Target URL (in ClickCount) 
	 *	@param TargetURL url
	 */
	public void setClickTargetURL(String TargetURL)
	{
		getMClickCount();
		if (m_clickCount == null)
			m_clickCount = new MClickCount(this);
		if (m_clickCount != null)
		{
			m_clickCount.setTargetURL(TargetURL);
			m_clickCount.save(get_TrxName());
		}
	}	//	getClickTargetURL
	
	
	/**
	 * 	Get Weekly Count
	 *	@return weekly count
	 */
	public ValueNamePair[] getClickCountWeek ()
	{
		getMClickCount();
		if (m_clickCount == null)
			return new ValueNamePair[0];
		return m_clickCount.getCountWeek();
	}	//	getClickCountWeek


	/**
	 *	Get CounterCount
	 *	@return Counter Count
	 */
	public MCounterCount getMCounterCount()
	{
		if (getW_CounterCount_ID() == 0)
			return null;
		if (m_counterCount == null)
			m_counterCount = new MCounterCount (getCtx(), getW_CounterCount_ID(), get_TrxName());
		return m_counterCount;
	}	//	MCounterCount

	/**
	 * 	Get Sales Rep ID.
	 * 	(AD_User_ID of oldest BP user)
	 *	@return Sales Rep ID
	 */
	public int getSalesRep_ID()
	{
		if (m_SalesRep_ID == 0)
		{
			m_SalesRep_ID = getAD_User_ID();
			if (m_SalesRep_ID == 0)
				m_SalesRep_ID = DB.getSQLValue(null,
					"SELECT AD_User_ID FROM AD_User "
					+ "WHERE C_BPartner_ID=? AND IsActive='Y' ORDER BY 1", getC_BPartner_ID());
		}
		return m_SalesRep_ID;
	}	//	getSalesRep_ID

}	//	MAdvertisement
