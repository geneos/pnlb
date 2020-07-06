/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ActFact BV
 *****************************************************************************/
package org.eevolution.model;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import org.compiere.model.MStorage;
import org.compiere.model.X_MPC_ORDER_QTYRESERVED;

import org.compiere.util.*;

/**
*	Bank Statement Model
*
*	@author Eldir Tomassen/Jorg Janke
*	@version $Id: MBankStatement.java,v 1.20 2005/11/12 22:58:56 jjanke Exp $
*/
@SuppressWarnings("serial")
public class MMPCOrderQtyReserved extends X_MPC_ORDER_QTYRESERVED
{


        /**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MMPCOrderQtyReserved.class);

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param MStorage storage
         *      @param MPCOrderBomLine order
         *      @param trxName trx
	 */        
        private MMPCOrderQtyReserved (Properties ctx, MStorage storage, MMPCOrderBOMLine orderBomLine, String trxName)
	{
		this (ctx, 0, trxName);
                setMPC_Order_ID(orderBomLine.getMPC_Order_ID());
                setMPC_Order_BOMLine_ID(orderBomLine.getMPC_Order_BOMLine_ID());
		setM_Locator_ID (storage.getM_Locator_ID());
		setM_Product_ID (storage.getM_Product_ID());
		setM_AttributeSetInstance_ID (storage.getM_AttributeSetInstance_ID());
	}
      
        
	/**************************************************************************
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MMPCOrderQtyReserved (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		//
		setTotalQty (Env.ZERO);
                setRemainingQty (Env.ZERO);
	}	//	MMPCOrderQtyReserved

	/**
	 * 	Load Constructor
	 * 	@param ctx Current context
	 * 	@param rs result set
	 */
	public MMPCOrderQtyReserved(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//
        
        /**
	 * 	Create or Get MMPCOrderQtyReserved Info
	 *	@param ctx context
	 *	@param MStorage storage
         *      @param MPC_Order_BomLine_ID line id
         *      @param trxName 
	 *	@return existing/new or null
	 */
	public static MMPCOrderQtyReserved getCreate (Properties ctx, MStorage storage, 
                int MPC_Order_BomLine_ID, String trxName)
	{
		if (storage == null)
			throw new IllegalArgumentException("storage=null");
		if (MPC_Order_BomLine_ID == 0)
			throw new IllegalArgumentException("MPC_Order_BomLine_ID=0");
		MMPCOrderQtyReserved retValue = get(ctx, storage, MPC_Order_BomLine_ID, trxName);
		if (retValue != null)
			return retValue;
		
		//	Insert row based on locator
		MMPCOrderBOMLine line = new MMPCOrderBOMLine (ctx, MPC_Order_BomLine_ID, trxName);
		if (line.get_ID() != MPC_Order_BomLine_ID)
			throw new IllegalArgumentException("Not found MPC_Order_BomLine_ID=" + MPC_Order_BomLine_ID);
		//
		retValue = new MMPCOrderQtyReserved (ctx,storage, line, trxName);
		s_log.fine("New " + retValue);
		return retValue;
	}	//	getCreate

        
        /**
	 * 	Get MMPCOrderQtyReserved Info
	 *	@param ctx context
	 *	@param MStorage storage
         *      @param MPCOrderBomLine order
         *      @param trxName
	 *	@return existing or null
	 */
	public static MMPCOrderQtyReserved get (Properties ctx, MStorage storage, 
		int MPC_Order_BomLine_ID, String trxName)
	{
		MMPCOrderQtyReserved retValue = null;
		String sql = "SELECT * FROM MPC_ORDER_QTYRESERVED "
			+ "WHERE MPC_ORDER_BOMLINE_ID=? AND M_Product_ID=? AND "
                        + "M_AttributeSetInstance_ID=? AND M_LOCATOR_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, MPC_Order_BomLine_ID);
			pstmt.setInt (2, storage.getM_Product_ID());
			pstmt.setInt (3, storage.getM_AttributeSetInstance_ID());
                        pstmt.setInt (4, storage.getM_Locator_ID());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MMPCOrderQtyReserved (ctx, rs, trxName);
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
			s_log.fine("Not Found - Storage=" + storage 
				+ ", MPC_Order_BomLine_ID=" + MPC_Order_BomLine_ID);
		else
			s_log.fine("Storage=" + storage 
				+ ", MPC_Order_BomLine_ID=" + MPC_Order_BomLine_ID);
		return retValue;
	}	//	get
        
        
        /**
	 * 	Get MMPCOrderQtyReserved Info
	 *	@param ctx context
	 *	@param MStorage storage
         *      @param MPCOrderBomLine order
         *      @param trxName
	 *	@return existing or null
	 */
	public static MMPCOrderQtyReserved[] getAllForBOMLine (Properties ctx, 
		int MPC_Order_BomLine_ID, String trxName)
	{
                ArrayList<MMPCOrderQtyReserved> list = new ArrayList<MMPCOrderQtyReserved>();
		String sql = "SELECT * FROM MPC_ORDER_QTYRESERVED "
			+ "WHERE MPC_ORDER_BOMLINE_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, MPC_Order_BomLine_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add( new MMPCOrderQtyReserved (ctx, rs, trxName) );
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
                MMPCOrderQtyReserved[] retValue = new MMPCOrderQtyReserved[list.size()];
                list.toArray (retValue);
		return retValue;
	}	//	get
        
        
        public boolean beforeDelete(){
            if (getRemainingQty().compareTo(BigDecimal.ZERO) > 0){
                s_log.severe("Error al eliminar: "+toString()+", remainingQty != 0");
                return false;
            }
            if (getTotalQty().compareTo(BigDecimal.ZERO) > 0){
                s_log.severe("Error al eliminar: "+toString()+", totalQty != 0");
                return false;
            }
            return true;
        }
        
        /**
	 * 	Get all MMPCOrderQtyReserved for a storage, order by MPCOrder release
	 *	@param ctx context
	 *	@param MStorage storage
         *      @param trxName
	 *	@return existing or empty
	 */
	public static MMPCOrderQtyReserved[] getAllForStorage (Properties ctx, 
		MStorage str, String trxName)
	{
                ArrayList<MMPCOrderQtyReserved> list = new ArrayList<MMPCOrderQtyReserved>();
		String sql = "SELECT ctrl.* FROM MPC_ORDER_QTYRESERVED ctrl "
                        + "JOIN MPC_ORDER o ON (o.MPC_ORDER_ID = ctrl.MPC_ORDER_ID) "
			+ "WHERE ctrl.M_LOCATOR_ID=? AND ctrl.M_ATTRIBUTESETINSTANCE_ID=? AND ctrl.M_PRODUCT_ID=?"
                        + "ORDER BY o.dateconfirm asc";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, str.getM_Locator_ID());
                        pstmt.setInt (2, str.getM_AttributeSetInstance_ID());
                        pstmt.setInt (3, str.getM_Product_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add( new MMPCOrderQtyReserved (ctx, rs, trxName) );
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
                MMPCOrderQtyReserved[] retValue = new MMPCOrderQtyReserved[list.size()];
                list.toArray (retValue);
		return retValue;
	}	//	get
 }		
