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
 *	GL Category
 *	
 *  @author Jorg Janke
 *  @version $Id: MGLCategory.java,v 1.5 2005/12/19 01:16:46 jjanke Exp $
 */
public class MGLCategory extends X_GL_Category
{
	/**
	 * 	Get MGLCategory from Cache
	 *	@param ctx context
	 *	@param GL_Category_ID id
	 *	@return MGLCategory
	 */
	public static MGLCategory get (Properties ctx, int GL_Category_ID)
	{
		Integer key = new Integer (GL_Category_ID);
		MGLCategory retValue = (MGLCategory)s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MGLCategory (ctx, GL_Category_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Default Category
	 *	@param ctx context
	 *	@param CategoryType optional CategoryType (ignored, if not exists)
	 *	@return GL Category or null
	 */
	public static MGLCategory getDefault (Properties ctx, String CategoryType)
	{
		MGLCategory retValue = null;
		String sql = "SELECT * FROM GL_Category "
			+ "WHERE AD_Client_ID=? AND IsDefault='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, Env.getAD_Client_ID(ctx));
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MGLCategory temp = new MGLCategory (ctx, rs, null);
				if (CategoryType != null && CategoryType.equals(temp.getCategoryType()))
				{
					retValue = temp;
					break;
				}
				if (retValue == null)
					retValue = temp;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return retValue;
	}	//	getDefault

	/**
	 * 	Get Default System Category
	 *	@param ctx context
	 *	@return GL Category
	 */
	public static MGLCategory getDefaultSystem (Properties ctx)
	{
		MGLCategory retValue = getDefault(ctx, CATEGORYTYPE_SystemGenerated);
		if (retValue == null 
			|| !retValue.getCategoryType().equals(CATEGORYTYPE_SystemGenerated))
		{
			retValue = new MGLCategory(ctx, 0, null);
			retValue.setName("Default System");
			retValue.setCategoryType(CATEGORYTYPE_SystemGenerated);
			retValue.setIsDefault(true);
			if (!retValue.save())
				throw new IllegalStateException("Could not save default system GL Category");
		}
		return retValue;
	}	//	getDefaultSystem

	
	/**	Logger						*/
	private static CLogger log = CLogger.getCLogger (MGLCategory.class);
	/**	Cache						*/
	private static CCache<Integer, MGLCategory> s_cache 
		= new CCache<Integer, MGLCategory> ("GL_Category", 5);
	

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param GL_Category_ID id
	 */
	public MGLCategory (Properties ctx, int GL_Category_ID, String trxName)
	{
		super (ctx, GL_Category_ID, trxName);
		if (GL_Category_ID == 0)
		{
		//	setName (null);
			setCategoryType (CATEGORYTYPE_Manual);
			setIsDefault (false);
		}
	}	//	MGLCategory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MGLCategory (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MGLCategory
	
}	//	MGLCategory
