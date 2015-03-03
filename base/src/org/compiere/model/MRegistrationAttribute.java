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
 *	Asset Registration Attribute
 *	
 *  @author Jorg Janke
 *  @version $Id: MRegistrationAttribute.java,v 1.7 2005/11/14 02:10:52 jjanke Exp $
 */
public class MRegistrationAttribute extends X_A_RegistrationAttribute
{
	/**
	 * 	Get All Asset Registration Attributes (not cached).
	 * 	Refreshes Cache for direct addess
	 *	@param ctx context
	 *	@return array of Registration Attributes
	 */
	public static MRegistrationAttribute[] getAll (Properties ctx)
	{
		//	Store/Refresh Cache and add to List
		ArrayList<MRegistrationAttribute> list = new ArrayList<MRegistrationAttribute>();
		String sql = "SELECT * FROM A_RegistrationAttribute "
			+ "WHERE AD_Client_ID=? "
			+ "ORDER BY SeqNo";
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MRegistrationAttribute value = new MRegistrationAttribute(ctx, rs, null);
				Integer key = new Integer(value.getA_RegistrationAttribute_ID());
				s_cache.put(key, value);
				list.add(value);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
		//
		MRegistrationAttribute[] retValue = new MRegistrationAttribute[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getAll

	/**
	 * 	Get Registration Attribute (cached)
	 *	@param ctx context
	 *	@param A_RegistrationAttribute_ID id
	 *	@return Registration Attribute
	 */
	public static MRegistrationAttribute get (Properties ctx, int A_RegistrationAttribute_ID, String trxName)
	{
		Integer key = new Integer(A_RegistrationAttribute_ID);
		MRegistrationAttribute retValue = (MRegistrationAttribute)s_cache.get(key);
		if (retValue == null)
		{
			retValue = new MRegistrationAttribute (ctx, A_RegistrationAttribute_ID, trxName);
			s_cache.put(key, retValue);
		}
		return retValue;
	}	//	getAll

	/** Static Logger					*/
	private static CLogger s_log = CLogger.getCLogger(MRegistrationAttribute.class);
	/**	Cache						*/
	private static CCache<Integer,MRegistrationAttribute> s_cache 
		= new CCache<Integer,MRegistrationAttribute>("A_RegistrationAttribute", 20);

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param A_RegistrationAttribute_ID id
	 */
	public MRegistrationAttribute (Properties ctx, int A_RegistrationAttribute_ID, String trxName)
	{
		super(ctx, A_RegistrationAttribute_ID, trxName);
	}	//	MRegistrationAttribute

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRegistrationAttribute (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRegistrationAttribute

}	//	MRegistrationAttribute
