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
 *	Warehouse Locator Object
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MLocator.java,v 1.10 2005/10/26 00:38:17 jjanke Exp $
 */
public class MLocator extends X_M_Locator
{
	/**
	 * 	Get oldest Default Locator of warehouse with locator
	 *	@param ctx context
	 *	@param M_Locator_ID locator
	 *	@return locator or null
	 */
	public static MLocator getDefault (Properties ctx, int M_Locator_ID)
	{
		String trxName = null;
		MLocator retValue = null;
		String sql = "SELECT * FROM M_Locator l "
			+ "WHERE IsDefault='Y'"
			+ " AND EXISTS (SELECT * FROM M_Locator lx "
				+ "WHERE l.M_Warehouse_ID=lx.M_Warehouse_ID AND lx.M_Locator_ID=?) "
			+ "ORDER BY Created";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Locator_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				retValue = new MLocator (ctx, rs, trxName);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
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
	 * 	Get the Locator with the combination or create new one
	 *	@param ctx Context
	 *	@param M_Warehouse_ID warehouse
	 *	@param Value value
	 *	@param X x
	 *	@param Y y
	 *	@param Z z
	 * 	@return locator
	 */
	 public static MLocator get (Properties ctx, int M_Warehouse_ID, String Value,
		 String X, String Y, String Z)
	 {
		MLocator retValue = null;
		String sql = "SELECT * FROM M_Locator WHERE M_Warehouse_ID=? AND X=? AND Y=? AND Z=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_Warehouse_ID);
			pstmt.setString(2, X);
			pstmt.setString(3, Y);
			pstmt.setString(4, Z);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MLocator (ctx, rs, null);
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, "get", ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		if (retValue == null)
		{
			MWarehouse wh = MWarehouse.get (ctx, M_Warehouse_ID);
			retValue = new MLocator (wh, Value);
			retValue.setXYZ(X, Y, Z);
			retValue.save();
		}
		return retValue;
	 }	//	get

	/**
	 * 	Get Locator from Cache
	 *	@param ctx context
	 *	@param M_Locator_ID id
	 *	@return MLocator
	 */
	public static MLocator get (Properties ctx, int M_Locator_ID)
	{
		if (s_cache == null)
			s_cache	= new CCache<Integer,MLocator>("M_Locator", 20);
		Integer key = new Integer (M_Locator_ID);
		MLocator retValue = (MLocator) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MLocator (ctx, M_Locator_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MLocator> s_cache; 
	 
	/**	Logger						*/
	private static CLogger		s_log = CLogger.getCLogger (MLocator.class);
	
	
	/**************************************************************************
	 * 	Standard Locator Constructor
	 *	@param ctx Context
	 *	@param M_Locator_ID id
	 */
	public MLocator (Properties ctx, int M_Locator_ID, String trxName)
	{
		super (ctx, M_Locator_ID, trxName);
		if (M_Locator_ID == 0)
		{
		//	setM_Locator_ID (0);		//	PK
		//	setM_Warehouse_ID (0);		//	Parent
			setIsDefault (false);
			setPriorityNo (50);
		//	setValue (null);
		//	setX (null);
		//	setY (null);
		//	setZ (null);
		}
	}	//	MLocator

	/**
	 * 	New Locator Constructor with XYZ=000
	 *	@param warehouse parent
	 *	@param Value value
	 */
	public MLocator (MWarehouse warehouse, String Value)
	{
		this (warehouse.getCtx(), 0, warehouse.get_TrxName());
		setClientOrg(warehouse);
		setM_Warehouse_ID (warehouse.getM_Warehouse_ID());		//	Parent
		setValue (Value);
		setXYZ("0","0","0");
	}	//	MLocator

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MLocator (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MLocator

	/**
	 *	Get String Representation
	 * 	@return Value
	 */
	public String toString()
	{
		return getValue();
	}	//	getValue

	/**
	 * 	Set Location
	 *	@param X x
	 *	@param Y y
	 *	@param Z z
	 */
	public void setXYZ (String X, String Y, String Z)
	{
		setX (X);
		setY (Y);
		setZ (Z);
	}	//	setXYZ
	
	
	/**
	 * 	Get Warehouse Name
	 * 	@return name
	 */
	public String getWarehouseName()
	{
		MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
		if (wh.get_ID() == 0)
			return "<" + getM_Warehouse_ID() + ">";
		return wh.getName();
	}	//	getWarehouseName
        
        public boolean isProductionIssue(){
            MWarehouse wh = new MWarehouse(getCtx(), getM_Warehouse_ID(),get_TrxName());
            return wh.isPRODUCTIONISSUE();
        }

}	//	MLocator
