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
 * Copyright (C) 2004 Victor Perez, e-Evolution, S.C.
 * All Rights Reserved.
 * Contributor(s): Victor Perez, e-Evolution, S.C.
 *****************************************************************************/
package org.eevolution.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;
import org.compiere.model.*;

/**
 *	Shipment Material Allocation
 *	
 *  @author Victor Perez
 *  @version $Id: MMPCOrderBOMLineMA.java,v 1.1 2005/04/01 05:59:48 jjanke Exp $
 */
public class MMPCOrderBOMLineMA extends X_MPC_Order_BOMLineMA
{
	/**
	 * 	Get Material Allocations for Line
	 *	@param ctx context
	 *	@param MPC_Order_BOMLine_ID line
	 *	@param trxName trx
	 *	@return allocations
	 */
	public static MMPCOrderBOMLineMA[] get (Properties ctx, int MPC_Order_BOMLine_ID, String trxName)
	{
		ArrayList list = new ArrayList ();
		String sql = "SELECT * FROM MPC_Order_BOMLineMA WHERE MPC_Order_BOMLine_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, MPC_Order_BOMLine_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MMPCOrderBOMLineMA (ctx, rs, trxName));
			}
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
		
		MMPCOrderBOMLineMA[] retValue = new MMPCOrderBOMLineMA[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Delete all Material Allocation for InOut
	 *	@param M_InOut_ID shipment
	 *	@return number of rows deleted or -1 for error
	 */
	public static int deleteOrderBOMLineMA (int MPC_Order_ID, String trxName)
	{
		String sql = "DELETE FROM MPC_Order_BOMLineMA ma WHERE EXISTS "
			+ "(SELECT * FROM MPC_Order_BOMLine l WHERE l.MPC_Order_BOMLine_ID=ma.MPC_Order_BOMLine_ID"
			+ " AND MPC_Order_ID=" + MPC_Order_ID + ")";
		return DB.executeUpdate(sql, trxName);
	}	//	deleteInOutMA
	
	/**	Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MMPCOrderBOMLineMA.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InOutLineMA_ID ignored
	 *	@param trxName trx
	 */
	public MMPCOrderBOMLineMA (Properties ctx, int MPC_Order_BOMLineMA_ID, String trxName)
	{
		super (ctx, MPC_Order_BOMLineMA_ID, trxName);
		if (MPC_Order_BOMLineMA_ID != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MInOutLineMA

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MMPCOrderBOMLineMA (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MInOutLineMA
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param MovementQty qty
	 */
	public MMPCOrderBOMLineMA (MMPCOrderBOMLineMA parent, int M_AttributeSetInstance_ID, BigDecimal MovementQty)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setMPC_Order_BOMLine_ID(parent.getMPC_Order_BOMLine_ID());
		//
		setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
		setMovementQty(MovementQty);
	}	//	MInOutLineMA
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MMPCOrderBOMLineMA[");
		sb.append("MPC_Order_BOMLine_ID=").append(getMPC_Order_BOMLine_ID())
			.append(",M_AttributeSetInstance_ID=").append(getM_AttributeSetInstance_ID())
			.append(", Qty=").append(getMovementQty())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MPC_Order_BOMLineMA
