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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 * 	Inventory Storage Model
 *
 *	@author Jorg Janke
 *	@version $Id: MStorage.java,v 1.27 2005/10/08 02:02:30 jjanke Exp $
 */
public class MStorage extends X_M_Storage
{
	/**
	 * 	Get Storage Info
	 *	@param ctx context
	 *	@param M_Locator_ID locator
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID instance
	 *	@return existing or null
	 */
	public static MStorage get (Properties ctx, int M_Locator_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID, String trxName)
	{
		MStorage retValue = null;
		String sql = "SELECT * FROM M_Storage "
			+ "WHERE M_Locator_ID=? AND M_Product_ID=? AND ";
		if (M_AttributeSetInstance_ID == 0)
			sql += "(M_AttributeSetInstance_ID=? OR M_AttributeSetInstance_ID IS NULL)";
		else
			sql += "M_AttributeSetInstance_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Locator_ID);
			pstmt.setInt (2, M_Product_ID);
			pstmt.setInt (3, M_AttributeSetInstance_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MStorage (ctx, rs, trxName);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		if (retValue == null)
			s_log.fine("Not Found - M_Locator_ID=" + M_Locator_ID 
				+ ", M_Product_ID=" + M_Product_ID + ", M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID);
		else
			s_log.fine("M_Locator_ID=" + M_Locator_ID 
				+ ", M_Product_ID=" + M_Product_ID + ", M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID);
		return retValue;
	}	//	get

	/**
	 * 	Get all Storages for Product with ASI
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param M_Locator_ID locator
	 *	@param FiFo first in-first-out
	 *	@return existing or null
	 */
	public static MStorage[] getAllWithASI (Properties ctx, int M_Product_ID, int M_Locator_ID, 
		boolean FiFo, String trxName)
	{
		ArrayList<MStorage> list = new ArrayList<MStorage>();
		String sql = "SELECT * FROM M_Storage "
			+ "WHERE M_Product_ID=? AND M_Locator_ID=?"
			+ " AND M_AttributeSetInstance_ID > 0"
			+ " AND QtyOnHand > 0 "
			+ "ORDER BY M_AttributeSetInstance_ID";
		if (!FiFo)
			sql += " DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_ID);
			pstmt.setInt (2, M_Locator_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MStorage (ctx, rs, trxName));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		MStorage[] retValue = new MStorage[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getAllWithASI
        
        
        /**
	 * 	Get all Storages for Product with ASI (IsRelease = Y)
	 *	@param ctx context
	 *	@param M_Product_ID product
         *      @param M_Warehouse_ID warehouse
	 *	@param FeFo first-expire first-out
	 *	@return existing or null
	 */
	public static MStorage[] getAllWithASIFEFO (Properties ctx, int M_Product_ID,
		boolean FeFo, boolean vencidas, String trxName)
	{
		ArrayList<MStorage> list = new ArrayList<MStorage>();
		String sql = "SELECT ms.* FROM M_Storage ms "
                        + "INNER JOIN M_AttributeSetInstance asi"
                        + " on ms.M_AttributeSetInstance_ID = asi.M_AttributeSetInstance_ID"
			+ " WHERE ms.M_Product_ID=?"
			+ " AND ms.M_AttributeSetInstance_ID > 0"
			+ " AND ms.QtyOnHand - ms.QtyReserved > 0 "
                        + " and ms.m_locator_id in (select l.m_locator_id from m_locator l "
                        + "                         JOIN m_warehouse wh ON (wh.m_warehouse_id = l.m_warehouse_id and wh.ISRELEASE = 'Y') )";	
                if (!vencidas)
			sql+= " AND ( asi.guaranteedate > current_date )";
		if (FeFo)
			sql+= " ORDER BY asi.guaranteedate asc, asi.m_attributesetinstance_id asc";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_ID);
                        //pstmt.setInt (2, M_Warehouse_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MStorage (ctx, rs, trxName));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		MStorage[] retValue = new MStorage[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getAllWithASI
        
        /**
	 * 	Get all Storages for Product with ASI For MRP (Is Required MRP) and isactive = Y
         *      Tambien trae partidas vencidas !
	 *	@param ctx context
	 *	@param M_Product_ID product
         *      @param M_Warehouse_ID warehouse
	 *	@param FeFo first-expire first-out
	 *	@return existing or null
	 */
	public static MStorage[] getAllWithASIFEFOMRP (Properties ctx, int M_Product_ID,
		boolean FeFo, boolean vencidas, String trxName)
	{
		ArrayList<MStorage> list = new ArrayList<MStorage>();
		String sql = "SELECT ms.* FROM M_Storage ms "
                        + "INNER JOIN M_AttributeSetInstance asi"
                        + " on ms.M_AttributeSetInstance_ID = asi.M_AttributeSetInstance_ID"
			+ " WHERE ms.M_Product_ID=?"
			+ " AND ms.M_AttributeSetInstance_ID > 0"
			+ " AND ms.QtyOnHand - ms.QtyReserved > 0 "
                        + " and ms.m_locator_id in (select l.m_locator_id from m_locator l "
                        + "                         JOIN m_warehouse wh ON (wh.m_warehouse_id = l.m_warehouse_id and wh.ISREQUIREDMRP = 'Y' ) )"
                        + " AND ms.isactive='Y'";	
		if (!vencidas)
			sql+= " AND ( asi.guaranteedate > current_date )";
                if (FeFo)
			sql+= " ORDER BY asi.guaranteedate asc, asi.m_attributesetinstance_id asc";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_ID);
                        //pstmt.setInt (2, M_Warehouse_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MStorage (ctx, rs, trxName));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		MStorage[] retValue = new MStorage[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getAllWithASI

	/**
	 * 	Get all Storages for Product
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param M_Locator_ID locator
	 *	@return existing or null
	 */
	public static MStorage[] getAll (Properties ctx, 
		int M_Product_ID, int M_Locator_ID, String trxName)
	{
		ArrayList<MStorage> list = new ArrayList<MStorage>();
		String sql = "SELECT * FROM M_Storage "
			+ "WHERE M_Product_ID=? AND M_Locator_ID=?"
			+ " AND QtyOnHand <> 0 "
			+ "ORDER BY M_AttributeSetInstance_ID";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_ID);
			pstmt.setInt (2, M_Locator_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MStorage (ctx, rs, trxName));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		MStorage[] retValue = new MStorage[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getAll
        
        /**
	 * 	Get all Available Storages for Locator
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param M_Locator_ID locator
	 *	@return existing or null
	 */
	public static MStorage[] getAll (Properties ctx, 
		int M_Locator_ID, String trxName)
	{
		ArrayList<MStorage> list = new ArrayList<MStorage>();
		String sql = "SELECT * FROM M_Storage "
			+ "WHERE M_Locator_ID=?"
			+ " AND QtyOnHand > 0 AND M_AttributeSetInstance_ID <> 0"
			+ " ORDER BY M_AttributeSetInstance_ID";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Locator_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MStorage (ctx, rs, trxName));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		MStorage[] retValue = new MStorage[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getAll

	
	/**
	 * 	Get Storage Info for Product across warehouses
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@return existing or null
	 */
	public static MStorage[] getOfProduct (Properties ctx, int M_Product_ID, String trxName)
	{
		ArrayList<MStorage> list = new ArrayList<MStorage>();
		String sql = "SELECT * FROM M_Storage "
			+ "WHERE M_Product_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				list.add(new MStorage (ctx, rs, trxName));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		MStorage[] retValue = new MStorage[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfProduct
	
	/**
	 * 	Get Storage Info for Warehouse
	 *	@param ctx context
	 *	@param M_Warehouse_ID 
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID instance
	 *	@param allAttributeInstances if true, all attribute set instances
	 *	@param minGuaranteeDate optional minimum guarantee date if all attribute instances
	 *	@param FiFo first in-first-out
	 *	@return existing - ordered by location priority (desc) and/or guarantee date
	 */
	public static MStorage[] getWarehouse (Properties ctx, int M_Warehouse_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID, int M_AttributeSet_ID,
		boolean allAttributeInstances, Timestamp minGuaranteeDate,
		boolean FiFo, String trxName)
	{
		if (M_Warehouse_ID == 0 || M_Product_ID == 0)
			return new MStorage[0];
		
		if (M_AttributeSet_ID == 0)
			allAttributeInstances = true;
		else
		{
			MAttributeSet mas = MAttributeSet.get(ctx, M_AttributeSet_ID);
			if (!mas.isInstanceAttribute())
				allAttributeInstances = true;
		}
		
		ArrayList<MStorage> list = new ArrayList<MStorage>();
		//	Specific Attribute Set Instance
		String sql = "SELECT s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID,"
			+ "s.AD_Client_ID,s.AD_Org_ID,s.IsActive,s.Created,s.CreatedBy,s.Updated,s.UpdatedBy,"
			+ "s.QtyOnHand,s.QtyReserved,s.QtyOrdered,s.DateLastInventory "
			+ "FROM M_Storage s"
			+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID) "
			+ "WHERE l.M_Warehouse_ID=?" 
			+ " AND s.M_Product_ID=?"
                        + " AND COALESCE(s.M_AttributeSetInstance_ID,0)=? "
			+ "ORDER BY l.PriorityNo DESC, M_AttributeSetInstance_ID";
		if (!FiFo)
			sql += " DESC";
		//	All Attribute Set Instances
		if (allAttributeInstances)
		{
			sql = "SELECT s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID,"
				+ "s.AD_Client_ID,s.AD_Org_ID,s.IsActive,s.Created,s.CreatedBy,s.Updated,s.UpdatedBy,"
				+ "s.QtyOnHand,s.QtyReserved,s.QtyOrdered,s.DateLastInventory "
				+ "FROM M_Storage s"
				+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID)"
				+ " LEFT OUTER JOIN M_AttributeSetInstance asi ON (s.M_AttributeSetInstance_ID=asi.M_AttributeSetInstance_ID) "
				+ "WHERE l.M_Warehouse_ID=?"
				+ " AND s.M_Product_ID=? ";
                        
			if (minGuaranteeDate != null)
			{
				sql += "AND (asi.GuaranteeDate IS NULL OR asi.GuaranteeDate>?) "
					+ "ORDER BY asi.GuaranteeDate, M_AttributeSetInstance_ID";
				if (!FiFo)
					sql += " DESC";
				sql += ", l.PriorityNo DESC, s.QtyOnHand DESC";
			}
			else
			{
				sql += "ORDER BY l.PriorityNo DESC, l.M_Locator_ID, s.M_AttributeSetInstance_ID";
				if (!FiFo)
					sql += " DESC";
				sql += ", s.QtyOnHand DESC";
			}
		} 
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, M_Warehouse_ID);
			pstmt.setInt(2, M_Product_ID);
			if (!allAttributeInstances)
				pstmt.setInt(3, M_AttributeSetInstance_ID);
			else if (minGuaranteeDate != null)
				pstmt.setTimestamp(3, minGuaranteeDate);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add (new MStorage (ctx, rs, trxName));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
		MStorage[] retValue = new MStorage[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getWarehouse

	
	/**
	 * 	Create or Get Storage Info
	 *	@param ctx context
	 *	@param M_Locator_ID locator
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID instance
	 *	@return existing/new or null
	 */
	public static MStorage getCreate (Properties ctx, int M_Locator_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID, String trxName)
	{
		if (M_Locator_ID == 0)
			throw new IllegalArgumentException("M_Locator_ID=0");
		if (M_Product_ID == 0)
			throw new IllegalArgumentException("M_Product_ID=0");
		MStorage retValue = get(ctx, M_Locator_ID, M_Product_ID, M_AttributeSetInstance_ID, trxName);
		if (retValue != null)
			return retValue;
		
		//	Insert row based on locator
		MLocator locator = new MLocator (ctx, M_Locator_ID, trxName);
		if (locator.get_ID() != M_Locator_ID)
			throw new IllegalArgumentException("Not found M_Locator_ID=" + M_Locator_ID);
		//
		retValue = new MStorage (locator, M_Product_ID, M_AttributeSetInstance_ID);
		retValue.save(trxName);
		s_log.fine("New " + retValue);
		return retValue;
	}	//	getCreate

	
	/**
	 * 	Update Storage Info add.
	 * 	Called from MProjectIssue
	 *	@param ctx context
	 *	@param M_Locator_ID locator
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID AS Instance
	 *	@param reservationAttributeSetInstance_ID reservation AS Instance
	 *	@param diffQtyOnHand add on hand
	 *	@param diffQtyReserved add reserved
	 *	@param diffQtyOrdered add order
	 *	@return true if updated
	 */
	public static boolean add (Properties ctx, int M_Warehouse_ID, int M_Locator_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID, int reservationAttributeSetInstance_ID,
		BigDecimal diffQtyOnHand, 
		BigDecimal diffQtyReserved, BigDecimal diffQtyOrdered, String trxName)
	{
		
                s_log.fine ("#### MODIFICACION STORAGE: diffQtyOnHand - " + diffQtyOnHand + " diffQtyReserved - " + " diffQtyOrdered - " + diffQtyOrdered);
            
                MStorage storage = null;
		StringBuffer diffText = new StringBuffer("(");

		//	Get Storage
		if (storage == null)
			storage = getCreate (ctx, M_Locator_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, trxName);
		//	Verify
		if (storage.getM_Locator_ID() != M_Locator_ID 
			&& storage.getM_Product_ID() != M_Product_ID
			&& storage.getM_AttributeSetInstance_ID() != M_AttributeSetInstance_ID)
		{
			s_log.severe ("No Storage found - M_Locator_ID=" + M_Locator_ID 
				+ ",M_Product_ID=" + M_Product_ID + ",ASI=" + M_AttributeSetInstance_ID);
			return false;
		}
		MStorage storage0 = null;
                MStorage storage01 = null; // fjv e-evolution
		if (M_AttributeSetInstance_ID != reservationAttributeSetInstance_ID)
		{
			storage0 = get(ctx, M_Locator_ID, 
				M_Product_ID, reservationAttributeSetInstance_ID, trxName);
			if (storage0 == null)	//	create if not existing - should not happen
			{
				MWarehouse wh = MWarehouse.get(ctx, M_Warehouse_ID);
				int xM_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
				storage0 = getCreate (ctx, xM_Locator_ID, 
					M_Product_ID, reservationAttributeSetInstance_ID, trxName);
			}
		}	
                
                 // fjv e-evolution todos los cambios de reserved y ordered los haga en un solo almacen
                MWarehouse wh1 = MWarehouse.get(ctx, M_Warehouse_ID);
				int x1M_Locator_ID = wh1.getDefaultLocator().getM_Locator_ID();
				storage01 = get (ctx, x1M_Locator_ID, 
					M_Product_ID, 0, trxName);
                 System.out.println("*****------------------------------------------------------------------------ storage " +storage +" storage0 " +storage0 +" storage01 " +storage01);
                 if (storage01 == null)	//	create if not existing - should not happen
			{
				MWarehouse wh = MWarehouse.get(ctx, M_Warehouse_ID);
				int xM_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
				storage01 = getCreate (ctx, xM_Locator_ID, 
					M_Product_ID, 0, trxName);
			}
                 System.out.println("*****------------------------------------------------------------------------ storage " +storage +" storage0 " +storage0 +" storage01 " +storage01);
                // end fjv e-evolution
                
		boolean changed = false;
                
		if (diffQtyOnHand != null && diffQtyOnHand.signum() != 0)
		{
			s_log.fine ("#### MODIFICACION STORAGE: diffQtyOnHand - " + diffQtyOnHand + " OnHand - " + storage.getQtyOnHand());
                        
                        storage.setQtyOnHand (storage.getQtyOnHand().add (diffQtyOnHand));
                        diffText.append("OnHand=").append(diffQtyOnHand);
			changed = true;
		}
                
		if (diffQtyReserved != null && diffQtyReserved.signum() != 0)
		{
                    
                    // fjv e-evolution todos los cambios de reserved y ordered los haga en un solo almacen
                    //System.out.println("*****------------------------------------------------------------------------ storage01 " +storage01 +" storage " +storage01.getQtyReserved() +" diff " +diffQtyReserved);
                    if (storage01.getQtyReserved().add (diffQtyReserved).compareTo(Env.ZERO)>=0)
                    {
                        s_log.fine ("#### MODIFICACION STORAGE: diffQtyReserved - " + diffQtyReserved + " QtyReserved - " + storage01.getQtyReserved());
                        storage01.setQtyReserved (storage01.getQtyReserved().add (diffQtyReserved));
                    }
                    else
                    {
                        s_log.fine ("#### MODIFICACION STORAGE: QtyReserved - " + Env.ZERO);
                        storage01.setQtyReserved(Env.ZERO);
                    
                    }
                    
                    //change
//			if (storage0 == null)
//				storage.setQtyReserved (storage.getQtyReserved().add (diffQtyReserved));
//			else
//				storage0.setQtyReserved (storage0.getQtyReserved().add (diffQtyReserved));
                    // end change
                    // end fjv e-evolution
			
			diffText.append(" Reserved=").append(diffQtyReserved);
			changed = true;
		}
		if (diffQtyOrdered != null && diffQtyOrdered.signum() != 0)
		{
			// fjv e-evolution todos los cambios de reserved y ordered los haga en un solo almacen
                    if (storage01.getQtyOrdered().add (diffQtyOrdered).compareTo(Env.ZERO)>=0)
                    {
                        s_log.fine ("#### MODIFICACION STORAGE: diffQtyOrdered - " + diffQtyOrdered + " QtyOrdered - " + storage01.getQtyOrdered());
                        storage01.setQtyOrdered (storage01.getQtyOrdered().add (diffQtyOrdered));
                    
                    }
                    else
                    {
                        s_log.fine ("#### MODIFICACION STORAGE: QtyOrdered - " + Env.ZERO);
                        storage01.setQtyOrdered(Env.ZERO);
                    }
                    //change
//			if (storage0 == null)
//				storage.setQtyOrdered (storage.getQtyOrdered().add (diffQtyOrdered));
//			else
//				storage0.setQtyOrdered (storage0.getQtyOrdered().add (diffQtyOrdered));
                    // end change
                    // end fjv e-evolution
                    
                   
			diffText.append(" Ordered=").append(diffQtyOrdered);
			changed = true;
		}
		if (changed)
		{
			diffText.append(") -> ").append(storage.toString());
			s_log.fine(diffText.toString());
			if (storage0 != null)
				storage0.save(trxName);		//	No AttributeSetInstance (reserved/ordered)
                         if (storage01 != null)
				storage01.save(trxName);
			return storage.save (trxName);
		}
		
		return true;
	}	//	add
        
        /**
	 * 	Update Storage Info add.
	 * 	Called from MProjectIssue
	 *	@param ctx context
	 *	@param M_Locator_ID locator
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID AS Instance
	 *	@param reservationAttributeSetInstance_ID reservation AS Instance
	 *	@param diffQtyOnHand add on hand
	 *	@param diffQtyReserved add reserved
	 *	@param diffQtyOrdered add order
	 *	@return true if updated
	 */
	public static boolean addDist (Properties ctx, int M_Warehouse_ID, int M_Locator_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID, int reservationAttributeSetInstance_ID,
		BigDecimal diffQtyOnHand, 
		BigDecimal diffQtyReserved, BigDecimal diffQtyOrdered, String trxName)
	{
                /*
                 * GENEOS - Pablo Velazquez
                 * 24/07/2013
                 * Se crea metodo para que actualize los reservados
                 * de la partida que se indica (reservationAttributeSetInstance_ID)
                 * Ademas se valida que la QtyOnHand no quede negativa
                 * Esquema:
                 *  - Cantidad Ordenada se lleva el control en partida = 0 (storageOrd)
                 *  - Cantidad en Mano se lleva el control en partida M_AttributeSetInstance_ID (storageHand)
                 *  - Cantidad Reservada se lleva el control en partida reservationAttributeSetInstance_ID (storageRes)
                 */
		
                s_log.fine ("#### MODIFICACION STORAGE: diffQtyOnHand - " + diffQtyOnHand + " diffQtyReserved - " + " diffQtyOrdered - " + diffQtyOrdered);
                
                
                /* Cantidad En Mano */
                
                MStorage storageHand = null;               		
                
                // Actualizo QtyOnHand en storageHand.
		if (diffQtyOnHand != null && diffQtyOnHand.signum() != 0)
		{
                    
                    //	Get Storage
                    if (storageHand == null)
                            storageHand = getCreate (ctx, M_Locator_ID, 
                                    M_Product_ID, M_AttributeSetInstance_ID, trxName);
                    //	Verify
                    if (storageHand.getM_Locator_ID() != M_Locator_ID 
                            && storageHand.getM_Product_ID() != M_Product_ID
                            && storageHand.getM_AttributeSetInstance_ID() != M_AttributeSetInstance_ID)
                    {
                            s_log.severe ("No Storage found - M_Locator_ID=" + M_Locator_ID 
                                    + ",M_Product_ID=" + M_Product_ID + ",ASI=" + M_AttributeSetInstance_ID);
                            return false;
                    }
                    
                    //Set
                    if (storageHand.getQtyOnHand().add (diffQtyOnHand).compareTo(Env.ZERO)>=0)
                        storageHand.setQtyOnHand (storageHand.getQtyOnHand().add (diffQtyOnHand));
                    else
                        storageHand.setQtyOnHand(Env.ZERO);
                    s_log.fine ("#### MODIFICACION STORAGE ( diffQtyOnHand - " + diffQtyOnHand + " QtyOnHand - " + storageHand.getQtyOnHand()+ ") -> "+storageHand.toString());
		}
                
                /* Cantidad Ordenada */            
                
		MStorage storageOrd = null;
                
                // Actualizo QtyOrdered en storageOrd.
                if (diffQtyOrdered != null && diffQtyOrdered.signum() != 0 && (M_Warehouse_ID != 0) )
                {
                    //Get Default Locator
                    MWarehouse wh = MWarehouse.get(ctx, M_Warehouse_ID);
                    int defaultLocator_ID = wh.getDefaultLocator().getM_Locator_ID();

                    //	Get Storage
                    if (storageOrd == null)
                            storageOrd = getCreate (ctx, defaultLocator_ID, 
                                    M_Product_ID, 0, trxName);
                    //	Verify
                    if (storageOrd.getM_Locator_ID() != defaultLocator_ID 
                            && storageOrd.getM_Product_ID() != M_Product_ID
                            && storageOrd.getM_AttributeSetInstance_ID() != 0)
                    {
                            s_log.severe ("No Storage found - M_Locator_ID=" + defaultLocator_ID 
                                    + ",M_Product_ID=" + M_Product_ID + ",ASI=" + 0);
                            return false;
                    }

                    //Set
                    if (storageOrd.getQtyOrdered().add (diffQtyOrdered).compareTo(Env.ZERO)>=0)
                        storageOrd.setQtyOrdered (storageOrd.getQtyOrdered().add (diffQtyOrdered));
                    else
                        storageOrd.setQtyOrdered(Env.ZERO);
                    s_log.fine ("#### MODIFICACION STORAGE ( diffQtyOrdered - " + diffQtyOrdered + " QtyOrdered - " + storageOrd.getQtyOrdered()+ ") -> "+storageOrd.toString());
                }
                
                /* Cantidad Reservada */            
                
		MStorage storageRes = null;
                             
                // Actualizo QtyReserved en storageRes.
		if (diffQtyReserved != null && diffQtyReserved.signum() != 0)
		{
                    
                    //	Get Storage
                    if (storageRes == null)
                            storageRes = getCreate (ctx, M_Locator_ID, 
                                    M_Product_ID, reservationAttributeSetInstance_ID, trxName);
                    //	Verify
                    if (storageRes.getM_Locator_ID() != M_Locator_ID 
                            && storageRes.getM_Product_ID() != M_Product_ID
                            && storageRes.getM_AttributeSetInstance_ID() != reservationAttributeSetInstance_ID)
                    {
                            s_log.severe ("No Storage found - M_Locator_ID=" + M_Locator_ID 
                                    + ",M_Product_ID=" + M_Product_ID + ",ASI=" + reservationAttributeSetInstance_ID);
                            return false;
                    }
                    
                    //Set                    
                    if (storageRes.getQtyReserved().add (diffQtyReserved).compareTo(Env.ZERO)>=0)
                        storageRes.setQtyReserved (storageRes.getQtyReserved().add (diffQtyReserved));
                    else
                        storageRes.setQtyReserved(Env.ZERO);
                    s_log.fine ("#### MODIFICACION STORAGE ( diffQtyQtyReserved - " + diffQtyReserved + " QtyReserved - " + storageRes.getQtyReserved()+ ") -> "+storageRes.toString());
		}		
                
                boolean retValue = true;
                
                if (storageHand != null)
                     retValue = storageHand.save();
                
                if (storageRes != null)
                     retValue = storageRes.save() && retValue;
                
                if (storageOrd != null)
                     retValue = storageOrd.save() && retValue;
                
		return retValue;
	}	//	add

	
	/**************************************************************************
	 * 	Get Location with highest Locator Priority and a sufficient OnHand Qty
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param M_Product_ID product
	 * 	@param M_AttributeSetInstance_ID asi
	 * 	@return id
	 */
	public static int getM_Locator_ID (int M_Warehouse_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID, BigDecimal Qty,
		String trxName)
	{
		int M_Locator_ID = 0;
		int firstM_Locator_ID = 0;
		String sql = "SELECT s.M_Locator_ID, s.QtyOnHand "
			+ "FROM M_Storage s"
			+ " INNER JOIN M_Locator l ON (s.M_Locator_ID=l.M_Locator_ID)"
			+ " INNER JOIN M_Product p ON (s.M_Product_ID=p.M_Product_ID)"
			+ " LEFT OUTER JOIN M_AttributeSet mas ON (p.M_AttributeSet_ID=mas.M_AttributeSet_ID) "
			+ "WHERE l.M_Warehouse_ID=?"
			+ " AND s.M_Product_ID=?"
			+ " AND (mas.IsInstanceAttribute IS NULL OR mas.IsInstanceAttribute='N' OR s.M_AttributeSetInstance_ID=?)"
			+ " AND l.IsActive='Y' "
			+ "ORDER BY l.PriorityNo DESC, s.QtyOnHand DESC";
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, M_Warehouse_ID);
			pstmt.setInt(2, M_Product_ID);
			pstmt.setInt(3, M_AttributeSetInstance_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				BigDecimal QtyOnHand = rs.getBigDecimal(2);
				if (QtyOnHand != null && Qty.compareTo(QtyOnHand) <= 0)
				{
					M_Locator_ID = rs.getInt(1);
					break;
				}
				if (firstM_Locator_ID == 0)
					firstM_Locator_ID = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
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
		if (M_Locator_ID != 0)
			return M_Locator_ID;
		return firstM_Locator_ID;
	}	//	getM_Locator_ID

	/**
	 * 	Get Available Qty.
	 * 	The call is accurate only if there is a storage record 
	 * 	and assumes that the product is stocked 
	 *	@param M_Warehouse_ID wh
	 *	@param M_Product_ID product
	 *	@return qty available (QtyOnHand-QtyReserved) or null
	 */
	public static BigDecimal getQtyAvailable (int M_Warehouse_ID, int M_Product_ID, String trxName)
	{
		BigDecimal retValue = null;
		PreparedStatement pstmt = null;
		String sql = "SELECT SUM(QtyOnHand-QtyReserved) "
			+ "FROM M_Storage s"
			+ " INNER JOIN M_Locator l ON (s.M_Locator_ID=l.M_Locator_ID) "
			+ "WHERE s.M_Product_ID=?"
			+ " AND l.M_Warehouse_ID=?";
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_ID);
			pstmt.setInt (2, M_Warehouse_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = rs.getBigDecimal(1);
				if (rs.wasNull())
					retValue = null;
			}
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
		s_log.fine("M_Warehouse_ID=" + M_Warehouse_ID 
			+ ",M_Product_ID=" + M_Product_ID + " = " + retValue);
		return retValue;
	}	//	getQtyAvailable
	
	
	/**************************************************************************
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MStorage (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		//
		setQtyOnHand (Env.ZERO);
		setQtyOrdered (Env.ZERO);
		setQtyReserved (Env.ZERO);
	}	//	MStorage

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MStorage (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MStorage

	/**
	 * 	Full NEW Constructor
	 *	@param locator (parent) locator
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID attribute
	 */
	private MStorage (MLocator locator, int M_Product_ID, int M_AttributeSetInstance_ID)
	{
		this (locator.getCtx(), 0, locator.get_TrxName());
		setClientOrg(locator);
		setM_Locator_ID (locator.getM_Locator_ID());
		setM_Product_ID (M_Product_ID);
		setM_AttributeSetInstance_ID (M_AttributeSetInstance_ID);
	}	//	MStorage

	/** Log								*/
	private static CLogger		s_log = CLogger.getCLogger (MStorage.class);
	/** Warehouse						*/
	private int		m_M_Warehouse_ID = 0;
	
	/**
	 * 	Change Qty OnHand
	 *	@param qty quantity
	 *	@param add add if true 
	 */
	public void changeQtyOnHand (BigDecimal qty, boolean add)
	{
		if (qty == null || qty.signum() == 0)
			return;
		if (add)
			setQtyOnHand(getQtyOnHand().add(qty));
		else
			setQtyOnHand(getQtyOnHand().subtract(qty));
	}	//	changeQtyOnHand

	/**
	 * 	Get M_Warehouse_ID of Locator
	 *	@return warehouse
	 */
	public int getM_Warehouse_ID()
	{
		if (m_M_Warehouse_ID == 0)
		{
			MLocator loc = MLocator.get(getCtx(), getM_Locator_ID());
			m_M_Warehouse_ID = loc.getM_Warehouse_ID();
		}
		return m_M_Warehouse_ID;
	}	//	getM_Warehouse_ID
	
	/**
	 *	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MStorage[")
			.append("M_Locator_ID=").append(getM_Locator_ID())
				.append(",M_Product_ID=").append(getM_Product_ID())
				.append(",M_AttributeSetInstance_ID=").append(getM_AttributeSetInstance_ID())
			.append(": OnHand=").append(getQtyOnHand())
			.append(",Reserved=").append(getQtyReserved())
			.append(",Ordered=").append(getQtyOrdered())
			.append("]");
		return sb.toString();
	}	//	toString

    
    public static BigDecimal getQtyDisponible(int M_Product_ID, int M_AttributeSetInstance_ID, int M_Locator_ID, String trxName){
        try {
            BigDecimal qty = BigDecimal.ZERO;
            String sql = "select sum(qtyonhand-qtyreserved) from m_storage " +
                         "where M_Product_ID = ? and M_AttributeSetInstance_ID = ? and M_Locator_ID = ?";
            PreparedStatement ps = DB.prepareStatement(sql, trxName);
            ps.setInt(1, M_Product_ID);
            ps.setInt(2, M_AttributeSetInstance_ID);
            ps.setInt(3, M_Locator_ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                qty = rs.getBigDecimal(1);
            rs.close();
            ps.close();
            return qty;
        } catch (SQLException ex) {
            Logger.getLogger(MStorage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getQtyAvailable() {
        BigDecimal qtyAvailable = getQtyOnHand().subtract(getQtyReserved());
        if (qtyAvailable.signum() == -1)
            return BigDecimal.ZERO;
        else
            return qtyAvailable;
    }

}	//	MStorage
