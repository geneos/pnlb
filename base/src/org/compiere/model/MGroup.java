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
 * 	Request Group Model
 *  @author Jorg Janke
 *  @version $Id: MGroup.java,v 1.3 2005/10/14 00:44:31 jjanke Exp $
 */
public class MGroup extends X_R_Group
{
	/**
	 * 	Get MGroup from Cache
	 *	@param ctx context
	 *	@param R_Group_ID id
	 *	@return MGroup
	 */
	public static MGroup get (Properties ctx, int R_Group_ID)
	{
		Integer key = new Integer (R_Group_ID);
		MGroup retValue = (MGroup) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MGroup (ctx, R_Group_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MGroup> s_cache = new CCache<Integer,MGroup>("R_Group", 20);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_Group_ID group
	 *	@param trxName trx
	 */
	public MGroup (Properties ctx, int R_Group_ID, String trxName)
	{
		super (ctx, R_Group_ID, trxName);
	}	//	MGroup

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MGroup (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MGroup
	
}	//	MGroup
