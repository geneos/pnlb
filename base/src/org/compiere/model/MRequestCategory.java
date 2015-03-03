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
 * 	Request Category Model
 *  @author Jorg Janke
 *  @version $Id: MRequestCategory.java,v 1.3 2005/10/08 02:02:29 jjanke Exp $
 */
public class MRequestCategory extends X_R_Category
{
	/**
	 * 	Get MCategory from Cache
	 *	@param ctx context
	 *	@param R_Category_ID id
	 *	@return MCategory
	 */
	public static MRequestCategory get (Properties ctx, int R_Category_ID)
	{
		Integer key = new Integer (R_Category_ID);
		MRequestCategory retValue = (MRequestCategory) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MRequestCategory (ctx, R_Category_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MRequestCategory>	s_cache	
		= new CCache<Integer,MRequestCategory>("R_Category", 20);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_Category_ID id
	 *	@param trxName trx
	 */
	public MRequestCategory (Properties ctx, int R_Category_ID, String trxName)
	{
		super (ctx, R_Category_ID, trxName);
	}	//	MCategory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MRequestCategory (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MCategory
	
}	//	MCategory
