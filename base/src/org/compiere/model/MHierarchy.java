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
 * 	Reporting Hierarchy Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MHierarchy.java,v 1.1 2005/10/26 00:37:41 jjanke Exp $
 */
public class MHierarchy extends X_PA_Hierarchy
{
	/**
	 * 	Get MHierarchy from Cache
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID id
	 *	@return MHierarchy
	 */
	public static MHierarchy get (Properties ctx, int PA_Hierarchy_ID)
	{
		Integer key = new Integer (PA_Hierarchy_ID);
		MHierarchy retValue = (MHierarchy)s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MHierarchy (ctx, PA_Hierarchy_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer, MHierarchy> s_cache 
		= new CCache<Integer, MHierarchy> ("PA_Hierarchy_ID", 20);
	
	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID id
	 *	@param trxName trx
	 */
	public MHierarchy (Properties ctx, int PA_Hierarchy_ID, String trxName)
	{
		super (ctx, PA_Hierarchy_ID, trxName);
	}	//	MHierarchy

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MHierarchy (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MHierarchy
	
	/**
	 * 	Get AD_Tree_ID based on tree type
	 *	@param TreeType Tree Type
	 *	@return id or 0
	 */
	public int getAD_Tree_ID (String TreeType)
	{
		if (MTree.TREETYPE_Activity.equals(TreeType))
			return getAD_Tree_Activity_ID();
		if (MTree.TREETYPE_BPartner.equals(TreeType))
			return getAD_Tree_BPartner_ID();
		if (MTree.TREETYPE_Campaign.equals(TreeType))
			return getAD_Tree_Campaign_ID();
		if (MTree.TREETYPE_ElementValue.equals(TreeType))
			return getAD_Tree_Account_ID();
		if (MTree.TREETYPE_Organization.equals(TreeType))
			return getAD_Tree_Org_ID();
		if (MTree.TREETYPE_Product.equals(TreeType))
			return getAD_Tree_Product_ID();
		if (MTree.TREETYPE_Project.equals(TreeType))
			return getAD_Tree_Project_ID();
		if (MTree.TREETYPE_SalesRegion.equals(TreeType))
			return getAD_Tree_SalesRegion_ID();
		//
		log.warning("Not supported: " + TreeType);
		return 0;
	}	//	getAD_Tree_ID
	
}	//	MHierarchy
