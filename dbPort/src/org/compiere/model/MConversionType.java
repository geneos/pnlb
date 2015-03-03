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
 *	Currency Conversion Type Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MConversionType.java,v 1.7 2005/10/08 02:03:35 jjanke Exp $
 */
public class MConversionType extends X_C_ConversionType
{
	/**
	 * 	Get Default Conversion Rate for Client/Org
	 *	@param AD_Client_ID client
	 *	@return C_ConversionType_ID or 0 if not found
	 */
	public static int getDefault (int AD_Client_ID)
	{
		//	Try Cache
		Integer key = new Integer (AD_Client_ID);
		Integer ii = (Integer)s_cache.get(key);
		if (ii != null)
			return ii.intValue();
			
		//	Get from DB
		int C_ConversionType_ID = 0;
		String sql = "SELECT C_ConversionType_ID "
			+ "FROM C_ConversionType "
			+ "WHERE IsActive='Y'"
			+ " AND AD_Client_ID IN (0,?) "		//	#1
			+ "ORDER BY IsDefault DESC, AD_Client_ID DESC";
		C_ConversionType_ID = DB.getSQLValue(null, sql, AD_Client_ID);
		//	Return
		s_cache.put(key, new Integer(C_ConversionType_ID));
		return C_ConversionType_ID;
	}	//	getDefault
	
	/**	Cache Client-ID					*/
	private static CCache<Integer,Integer> s_cache = new CCache<Integer,Integer>("C_ConversionType", 4);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_ConversionType_ID id
	 */
	public MConversionType(Properties ctx, int C_ConversionType_ID, String trxName)
	{
		super(ctx, C_ConversionType_ID, trxName);
	}	//	MConversionType

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MConversionType(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MConversionType

}	//	MConversionType
