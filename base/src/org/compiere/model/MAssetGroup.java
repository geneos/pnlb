/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import org.compiere.util.*;

/**
 * 	Asset Group Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAssetGroup.java,v 1.3 2006/01/03 02:39:36 jjanke Exp $
 */
public class MAssetGroup extends X_A_Asset_Group
{
	/**
	 * 	Get from Cache
	 *	@param ctx context
	 *	@param A_Asset_Group_ID id
	 *	@return category
	 */
	public static MAssetGroup get (Properties ctx, int A_Asset_Group_ID)
	{
		Integer ii = new Integer (A_Asset_Group_ID);
		MAssetGroup pc = (MAssetGroup)s_cache.get(ii);
		if (pc == null)
			pc = new MAssetGroup (ctx, A_Asset_Group_ID, null);
		return pc;
	}	//	get

	/**	Categopry Cache				*/
	private static CCache	s_cache = new CCache ("A_Asset_Group", 10);

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param A_Asset_Group_ID id
	 *	@param trxName trx
	 */
	public MAssetGroup (Properties ctx, int A_Asset_Group_ID, String trxName)
	{
		super (ctx, A_Asset_Group_ID, trxName);
		if (A_Asset_Group_ID == 0)
		{
		//	setName (null);
			setIsDepreciated (false);
			setIsOneAssetPerUOM (false);
			setIsOwned (false);
			setIsCreateAsActive(true);
			setIsTrackIssues(false);
		}
	}	//	MAssetGroup

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MAssetGroup (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAssetGroup
	
}	//	MAssetGroup
