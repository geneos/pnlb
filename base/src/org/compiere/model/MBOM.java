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
 * 	BOM Model
 *  @author Jorg Janke
 *  @version $Id: MBOM.java,v 1.4 2005/10/08 02:02:29 jjanke Exp $
 */
public class MBOM extends X_M_BOM
{
	/**
	 * 	Get BOM from Cache
	 *	@param ctx context
	 *	@param M_BOM_ID id
	 *	@return MBOM
	 */
	public static MBOM get (Properties ctx, int M_BOM_ID)
	{
		Integer key = new Integer (M_BOM_ID);
		MBOM retValue = (MBOM) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MBOM (ctx, M_BOM_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get BOMs Of Product
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param trxName trx
	 *	@param whereClause optional WHERE clause w/o AND
	 *	@return array of BOMs
	 */
	public static MBOM[] getOfProduct (Properties ctx, int M_Product_ID, 
		String trxName, String whereClause)
	{
		ArrayList<MBOM> list = new ArrayList<MBOM>();
		String sql = "SELECT * FROM M_BOM WHERE M_Product_ID=?";
		if (whereClause != null && whereClause.length() > 0)
			sql += " AND " + whereClause;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MBOM (ctx, rs, trxName));
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
		
		MBOM[] retValue = new MBOM[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfProduct

	/**	Cache						*/
	private static CCache<Integer,MBOM>	s_cache	
		= new CCache<Integer,MBOM>("M_BOM", 20);
	/**	Logger						*/
	private static CLogger	log	= CLogger.getCLogger (MBOM.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_BOM_ID id
	 *	@param trxName trx
	 */
	public MBOM (Properties ctx, int M_BOM_ID, String trxName)
	{
		super (ctx, M_BOM_ID, trxName);
		if (M_BOM_ID == 0)
		{
		//	setM_Product_ID (0);
		//	setName (null);
			setBOMType (BOMTYPE_CurrentActive);	// A
			setBOMUse (BOMUSE_Master);	// A
		}
	}	//	MBOM

	/**
	 * 	Load Constructor
	 *	@param ctx ctx
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MBOM (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MBOM

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true/false
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	BOM Type
		if (newRecord || is_ValueChanged("BOMType"))
		{
			//	Only one Current Active
			if (getBOMType().equals(BOMTYPE_CurrentActive))
			{
				MBOM[] boms = getOfProduct(getCtx(), getM_Product_ID(), get_TrxName(),
					"BOMType='A' AND BOMUse='" + getBOMUse() + "' AND IsActive='Y'");
				if (boms.length == 0	//	only one = this 
					|| (boms.length == 1 && boms[0].getM_BOM_ID() == getM_BOM_ID()))
					;
				else
				{
					log.saveError("Error", Msg.parseTranslation(getCtx(), 
						"Can only have one Current Active BOM for Product BOM Use (" + getBOMType() + ")"));
					return false;
				}
			}
			//	Only one MTO
			else if (getBOMType().equals(BOMTYPE_Make_To_Order))
			{
				MBOM[] boms = getOfProduct(getCtx(), getM_Product_ID(), get_TrxName(), 
					"IsActive='Y'");
				if (boms.length == 0	//	only one = this 
					|| (boms.length == 1 && boms[0].getM_BOM_ID() == getM_BOM_ID()))
					;
				else
				{
					log.saveError("Error", Msg.parseTranslation(getCtx(), 
						"Can only have single Make-to-Order BOM for Product"));
					return false;
				}
			}
		}	//	BOM Type
		
		return true;
	}	//	beforeSave
	
}	//	MBOM
