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
 *	Message Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MMessage.java,v 1.7 2005/10/26 00:38:15 jjanke Exp $
 */
public class MMessage extends X_AD_Message
{
	/**
	 * 	Get Message (cached)
	 *	@param ctx context
	 *	@param Value message value
	 */
	public static MMessage get (Properties ctx, String Value)
	{
		if (Value == null || Value.length() == 0)
			return null;
		MMessage retValue = (MMessage)s_cache.get(Value);
		//
		if (retValue == null)
		{
			String sql = "SELECT * FROM AD_Message WHERE Value=?";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setString(1, Value);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					retValue = new MMessage (ctx, rs, null);
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
			if (retValue != null)
				s_cache.put(Value, retValue);
		}
		return retValue;
	}	//	get

	/**
	 * 	Get Message (cached)
	 *	@param ctx context
	 *	@param AD_Message_ID id
	 */
	public static MMessage get (Properties ctx, int AD_Message_ID)
	{
		String key = String.valueOf(AD_Message_ID);
		MMessage retValue = (MMessage)s_cache.get(key);
		if (retValue == null)
		{
			retValue = new MMessage (ctx, AD_Message_ID, null);
			s_cache.put(key, retValue);
		}
		return retValue;
	}	//	get
	
	/**
	 * 	Get Message ID (cached)
	 *	@param ctx context
	 *	@param Value message value
	 *	@return AD_Message_ID
	 */
	public static int getAD_Message_ID (Properties ctx, String Value)
	{
		MMessage msg = get(ctx, Value);
		if (msg == null)
			return 0;
		return msg.getAD_Message_ID();
	}	//	getAD_Message_ID
	
	/**	Cache					*/
	static private CCache<String,MMessage> s_cache = new CCache<String,MMessage>("AD_Message", 100);
	/** Static Logger					*/
	private static CLogger 	s_log = CLogger.getCLogger(MMessage.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Message_ID id
	 */
	public MMessage (Properties ctx, int AD_Message_ID, String trxName)
	{
		super(ctx, AD_Message_ID, trxName);
	}	//	MMessage

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MMessage(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MMessage

}	//	MMessage
