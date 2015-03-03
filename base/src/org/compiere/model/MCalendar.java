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
 *	Calendar Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCalendar.java,v 1.8 2005/10/08 02:02:29 jjanke Exp $
 */
public class MCalendar extends X_C_Calendar
{
	/**
	 * 	Get MCalendar from Cache
	 *	@param ctx context
	 *	@param C_Calendar_ID id
	 *	@return MCalendar
	 */
	public static MCalendar get (Properties ctx, int C_Calendar_ID)
	{
		Integer key = new Integer (C_Calendar_ID);
		MCalendar retValue = (MCalendar) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MCalendar (ctx, C_Calendar_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Get Default Calendar for Client
	 *	@param ctx context
	 *	@param AD_Client_ID id
	 *	@return MCalendar
	 */
	public static MCalendar getDefault (Properties ctx, int AD_Client_ID)
	{
		MClientInfo info = MClientInfo.get(ctx, AD_Client_ID);
		return get (ctx, info.getC_Calendar_ID());
	}	//	getDefault
	
	/**
	 * 	Get Default Calendar for Client
	 *	@param ctx context
	 *	@return MCalendar
	 */
	public static MCalendar getDefault (Properties ctx)
	{
		return getDefault(ctx, Env.getAD_Client_ID(ctx));
	}	//	getDefault
	
	/**	Cache						*/
	private static CCache<Integer,MCalendar> s_cache
		= new CCache<Integer,MCalendar>("C_Calendar", 20);
	
	
	/*************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Calendar_ID id
	 */
	public MCalendar (Properties ctx, int C_Calendar_ID, String trxName)
	{
		super(ctx, C_Calendar_ID, trxName);
	}	//	MCalendar

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MCalendar (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCalendar

	/**
	 * 	Parent Constructor
	 *	@param client parent
	 */
	public MCalendar (MClient client)
	{
		super(client.getCtx(), 0, client.get_TrxName());
		setClientOrg(client);
		setName(client.getName() + " " + Msg.translate(client.getCtx(), "C_Calendar_ID"));
	}	//	MCalendar
	
	/**
	 * 	Create (current) Calendar Year
	 * 	@param locale locale
	 *	@return The Year
	 */
	public MYear createYear(Locale locale)
	{
		if (get_ID() == 0)
			return null;
		MYear year = new MYear (this);
		if (year.save())
			year.createStdPeriods(locale);
		//
		return year;
	}	//	createYear
	
}	//	MCalendar
