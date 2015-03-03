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

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import org.compiere.model.X_MPC_ORDER_PARENTORDER;

import org.compiere.util.*;

/**
*	Bank Statement Model
*
*	@author Eldir Tomassen/Jorg Janke
*	@version $Id: MBankStatement.java,v 1.20 2005/11/12 22:58:56 jjanke Exp $
*/
@SuppressWarnings("serial")
public class MMPCOrderParentOrder extends X_MPC_ORDER_PARENTORDER
{


        /**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MMPCOrderParentOrder.class);

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BankStatement_ID id
	 */
	public MMPCOrderParentOrder (Properties ctx, int MPC_OrderParentOrder_ID, String trxName)
	{
		super (ctx, MPC_OrderParentOrder_ID, trxName);
		if (MPC_OrderParentOrder_ID == 0)
		{
		//	setC_BankAccount_ID (0);	//	parent

		}
	}	//	MMPCOrderParentOrder

	/**
	 * 	Load Constructor
	 * 	@param ctx Current context
	 * 	@param rs result set
	 */
	public MMPCOrderParentOrder(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//

        /**
	 * 	Get MMPCOrder child with order
	 *	@param ctx context
	 *	@param MPC_Order_ID order
         *      @param traxName transaction Name
	 *	@return array of matches (mus be only one)
	 */
	public static MMPCOrder[] getChilds (Properties ctx,
		int MPC_Order_ID, String trxName)
	{
		if (MPC_Order_ID == 0)
			return new MMPCOrder[]{};
		//
		String sql = "SELECT MPC_ORDER_ID FROM MPC_ORDER_PARENTORDER WHERE MPC_ParentOrder_ID=? and isActive = 'Y' ";
		ArrayList<MMPCOrder> list = new ArrayList<MMPCOrder>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, MPC_Order_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MMPCOrder (ctx, rs.getInt(1), trxName));
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
		MMPCOrder[] retValue = new MMPCOrder[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	getChilds

	/**
	 * 	Get MMPCOrder Parent with order
	 *	@param ctx context
	 *	@param MPC_Order_ID order
         *      @param traxName transaction Name
	 *	@return MMP Parent Order
	 */
	public static MMPCOrder getParent (Properties ctx,
		int MPC_Order_ID, String trxName)
	{
		if (MPC_Order_ID == 0)
			return null;

		String sql = "SELECT MPC_PARENTORDER_ID FROM MPC_ORDER_PARENTORDER WHERE MPC_Order_ID=? and isActive = 'Y' ";
		MMPCOrder returnValue = null;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, MPC_Order_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				returnValue = new MMPCOrder (ctx, rs.getInt(1), trxName);
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
		return returnValue;
	}	//	getParent
        
/**
	 * 	Get MMPCOrder Parents with order
	 *	@param ctx context
	 *	@param MPC_Order_ID order
         *      @param traxName transaction Name
	 *	@return MMP Parent Order
	 */
	public static MMPCOrder[] getParents (Properties ctx,
		int MPC_Order_ID, String trxName)
	{
		if (MPC_Order_ID == 0)
			return new MMPCOrder[]{};
		//
		String sql = "SELECT MPC_PARENTORDER_ID FROM MPC_ORDER_PARENTORDER WHERE MPC_Order_ID=? and isActive = 'Y' ";
		ArrayList<MMPCOrder> list = new ArrayList<MMPCOrder>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, MPC_Order_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MMPCOrder (ctx, rs.getInt(1), trxName));
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
		MMPCOrder[] retValue = new MMPCOrder[list.size()];
		list.toArray (retValue);
		return retValue;
                
	}	//	getParents        
        
        /**
	 * 	Get all MMPCOrderParentOrder with order (child or parent)
	 *	@param ctx context
	 *	@param MPC_Order_ID order
         *      @param traxName transaction Name
	 *	@return array of matches (mus be only one)
	 */
	public static MMPCOrderParentOrder[] getAllForOrder (Properties ctx,
		int MPC_Order_ID, String trxName)
	{
		if (MPC_Order_ID == 0)
			return new MMPCOrderParentOrder[]{};
		//
		String sql = "SELECT * FROM MPC_ORDER_PARENTORDER WHERE MPC_ParentOrder_ID=? or MPC_Order_ID=? and isActive = 'Y' ";
		ArrayList<MMPCOrderParentOrder> list = new ArrayList<MMPCOrderParentOrder>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, MPC_Order_ID);
                        pstmt.setInt (2, MPC_Order_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MMPCOrderParentOrder (ctx, rs, trxName));
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
		MMPCOrderParentOrder[] retValue = new MMPCOrderParentOrder[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	getAllForOrder
 }		
