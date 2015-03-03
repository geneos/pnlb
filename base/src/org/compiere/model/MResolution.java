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
 * 	Request Resolution Model
 *  @author Jorg Janke
 *  @version $Id: MResolution.java,v 1.3 2005/11/14 02:10:53 jjanke Exp $
 */
public class MResolution extends X_R_Resolution
{
	/**
	 * 	Get MResolution from Cache
	 *	@param ctx context
	 *	@param R_Resolution_ID id
	 *	@return MResolution
	 */
	public static MResolution get (Properties ctx, int R_Resolution_ID)
	{
		Integer key = new Integer (R_Resolution_ID);
		MResolution retValue = (MResolution) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MResolution (ctx, R_Resolution_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,MResolution>	s_cache	= new CCache<Integer,MResolution>("R_Resolution", 10);
	
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_Resolution_ID id
	 *	@param trxName
	 */
	public MResolution (Properties ctx, int R_Resolution_ID, String trxName)
	{
		super (ctx, R_Resolution_ID, trxName);
	}	//	MResolution

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MResolution (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MResolution
	
}	//	MResolution
