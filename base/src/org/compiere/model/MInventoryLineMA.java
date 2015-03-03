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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Inventory Material Allocation
 *	
 *  @author Jorg Janke
 *  @version $Id: MInventoryLineMA.java,v 1.3 2005/09/28 01:34:03 jjanke Exp $
 */
public class MInventoryLineMA extends X_M_InventoryLineMA
{
	/**
	 * 	Get Material Allocations for Line
	 *	@param ctx context
	 *	@param M_InventoryLine_ID line
	 *	@param trxName trx
	 *	@return allocations
	 */
	public static MInventoryLineMA[] get (Properties ctx, int M_InventoryLine_ID, String trxName)
	{
		ArrayList<MInventoryLineMA> list = new ArrayList<MInventoryLineMA>();
		String sql = "SELECT * FROM M_InventoryLineMA WHERE M_InventoryLine_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_InventoryLine_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MInventoryLineMA (ctx, rs, trxName));
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
		
		MInventoryLineMA[] retValue = new MInventoryLineMA[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Delete all Material Allocation for Inventory
	 *	@param M_Inventory_ID inventory
	 *	@return number of rows deleted or -1 for error
	 */
	public static int deleteInventoryMA (int M_Inventory_ID, String trxName)
	{
		String sql = "DELETE FROM M_InventoryLineMA ma WHERE EXISTS "
			+ "(SELECT * FROM M_InventoryLine l WHERE l.M_InventoryLine_ID=ma.M_InventoryLine_ID"
			+ " AND M_Inventory_ID=" + M_Inventory_ID + ")";
		return DB.executeUpdate(sql, trxName);
	}	//	deleteInventoryMA
	
	/**	Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MInventoryLineMA.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InventoryLineMA_ID ignored
	 *	@param trxName trx
	 */
	public MInventoryLineMA (Properties ctx, int M_InventoryLineMA_ID, String trxName)
	{
		super (ctx, M_InventoryLineMA_ID, trxName);
		if (M_InventoryLineMA_ID != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MInventoryLineMA

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MInventoryLineMA (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MInventoryLineMA
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param MovementQty qty
	 */
	public MInventoryLineMA (MInventoryLine parent, int M_AttributeSetInstance_ID, BigDecimal MovementQty)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setM_InventoryLine_ID(parent.getM_InventoryLine_ID());
		//
		setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
		setMovementQty(MovementQty);
	}	//	MInventoryLineMA
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInventoryLineMA[");
		sb.append("M_InventoryLine_ID=").append(getM_InventoryLine_ID())
			.append(",M_AttributeSetInstance_ID=").append(getM_AttributeSetInstance_ID())
			.append(", Qty=").append(getMovementQty())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MInventoryLineMA
