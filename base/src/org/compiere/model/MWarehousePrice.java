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
 *	Product Warehouse Availability and Price Model.
 *	The Ownership (Client, Org) is determined by the Warehouse
 *	Active is determined if the product is discontinued (the product/price/warehouse need to be active)
 *	Created.. is determined by the price list version 
 *	
 *  @author Jorg Janke
 *  @version $Id: MWarehousePrice.java,v 1.11 2005/11/14 02:10:53 jjanke Exp $
 */
public class MWarehousePrice extends X_RV_WarehousePrice
{
	/**
	 * 	Find Products in Warehouse with Price
	 * 	@param ctx context
	 *	@param M_PriceList_Version_ID mandatory price list
	 *	@param M_Warehouse_ID mandatory warehouse
	 *	@param Value optional value
	 *	@param Name optional name
	 *	@param UPC optional full match upc
	 *	@param SKU optional full match ski
	 *	@return array of product prices and warehouse availability
	 */
	public static MWarehousePrice[] find (Properties ctx,
		int M_PriceList_Version_ID, int M_Warehouse_ID,
		String Value, String Name, String UPC, String SKU, String trxName)
	{
		StringBuffer sql = new StringBuffer ("SELECT * FROM RV_WarehousePrice "
			+ "WHERE M_PriceList_Version_ID=? AND M_Warehouse_ID=?");
		StringBuffer sb = new StringBuffer();
		Value = getFindParameter (Value);
		if (Value != null)
			sb.append("UPPER(Value) LIKE ?");
		Name = getFindParameter (Name);
		if (Name != null)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPPER(Name) LIKE ?");
		}
		if (UPC != null && UPC.length() > 0)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPC=?");
		}
		if (SKU != null && SKU.length() > 0)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("SKU=?");
		}
		if (sb.length() > 0)
			sql.append(" AND (").append(sb).append(")");
		sql.append(" ORDER BY Value");
		//
		String finalSQL = MRole.getDefault().addAccessSQL(sql.toString(), 
			"RV_WarehousePrice", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		s_log.fine("find - M_PriceList_Version_ID=" + M_PriceList_Version_ID 
			+ ", M_Warehouse_ID=" + M_Warehouse_ID
			+ " - " + finalSQL);
		ArrayList<MWarehousePrice> list = new ArrayList<MWarehousePrice>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(finalSQL, trxName);
			int index = 1;
			pstmt.setInt(index++, M_PriceList_Version_ID);
			pstmt.setInt(index++, M_Warehouse_ID);
			if (Value != null)
				pstmt.setString(index++, Value);
			if (Name != null)
				pstmt.setString(index++, Name);
			if (UPC != null && UPC.length() > 0)
				pstmt.setString(index++, UPC);
			if (SKU != null && SKU.length() > 0)
				pstmt.setString(index++, SKU);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MWarehousePrice(ctx, rs, trxName));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, finalSQL, e);
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
		s_log.fine("find - #" + list.size());
		MWarehousePrice[] retValue = new MWarehousePrice[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	find

	/**
	 * 	Find Products in Warehouse with Price for customer
	 * 	@param bPartner business partner
	 *	@param IsSOTrx if true SO
	 *	@param valid the date the price must be valid
	 *	@param M_Warehouse_ID mandatory warehouse
	 *	@param Value optional value
	 *	@param Name optional name
	 *	@param UPC optional upc
	 *	@param SKU optional ski
	 *	@return array of product prices and warehouse availability or null
	 */
	public static MWarehousePrice[] find (MBPartner bPartner,
		boolean IsSOTrx, Timestamp valid, int M_Warehouse_ID,
		String Value, String Name, String UPC, String SKU, String trxName)
	{
		int M_PriceList_ID = IsSOTrx ? bPartner.getM_PriceList_ID() : bPartner.getPO_PriceList_ID();
		MPriceList pl = null;
		if (M_PriceList_ID == 0)
			pl = MPriceList.getDefault(bPartner.getCtx(), IsSOTrx);
		else
			pl = MPriceList.get(bPartner.getCtx(), M_PriceList_ID, trxName);
		if (pl == null)
		{
			s_log.severe ("No PriceList found");
			return null;
		}
		MPriceListVersion plv = pl.getPriceListVersion (valid);
		if (plv == null)
		{
			s_log.severe ("No PriceListVersion found for M_PriceList_ID=" + pl.getM_PriceList_ID());
			return null;
		}
		//
		return find (bPartner.getCtx(), plv.getM_PriceList_Version_ID(), M_Warehouse_ID,
			Value, Name, UPC, SKU, trxName);
	}	//	find

	/**
	 * 	Get MWarehouse Price
	 *	@param product product
	 *	@param M_PriceList_Version_ID plv
	 *	@param M_Warehouse_ID wh
	 *	@return warehouse price
	 */
	public static MWarehousePrice get (MProduct product,
		int M_PriceList_Version_ID, int M_Warehouse_ID, String trxName)
	{
		MWarehousePrice retValue = null;
		String sql = "SELECT * FROM RV_WarehousePrice "
			+ "WHERE M_Product_ID=? AND M_PriceList_Version_ID=? AND M_Warehouse_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, product.getM_Product_ID());
			pstmt.setInt(2, M_PriceList_Version_ID);
			pstmt.setInt(3, M_Warehouse_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MWarehousePrice(product.getCtx(), rs, trxName);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
	}	//	get

	/** Static Logger					*/
	private static CLogger 	s_log = CLogger.getCLogger(MWarehousePrice.class);

	
	/*************************************************************************
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MWarehousePrice (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MWarehousePrice
	
	/**
	 * 	Is Product Available
	 *	@return true if available qty > 0
	 */
	public boolean isAvailable()
	{
		return getQtyAvailable().signum() == 1;	//	> 0
	}	//	isAvailable

}	//	MWarehousePrice
