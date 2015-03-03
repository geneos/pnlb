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
 *	Movement Material Allocation
 *	
 *  @author Jorg Janke
 *  @version $Id: MMovementLineMA.java,v 1.3 2005/09/28 01:34:03 jjanke Exp $
 */
public class MMovementLineMA extends X_M_MovementLineMA
{
	/**
	 * 	Get Material Allocations for Line
	 *	@param ctx context
	 *	@param M_MovementLine_ID line
	 *	@param trxName trx
	 *	@return allocations
	 */
	public static MMovementLineMA[] get (Properties ctx, int M_MovementLine_ID, String trxName)
	{
		ArrayList<MMovementLineMA> list = new ArrayList<MMovementLineMA>();
		String sql = "SELECT * FROM M_MovementLineMA WHERE M_MovementLine_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_MovementLine_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MMovementLineMA (ctx, rs, trxName));
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
		
		MMovementLineMA[] retValue = new MMovementLineMA[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Delete all Material Allocation for Movement
	 *	@param M_Movement_ID movement
	 *	@return number of rows deleted or -1 for error
	 */
	public static int deleteMovementMA (int M_Movement_ID, String trxName)
	{
		String sql = "DELETE FROM M_MovementLineMA ma WHERE EXISTS "
			+ "(SELECT * FROM M_MovementLine l WHERE l.M_MovementLine_ID=ma.M_MovementLine_ID"
			+ " AND M_Movement_ID=" + M_Movement_ID + ")";
		return DB.executeUpdate(sql, trxName);
	}	//	deleteInOutMA
	
	/**	Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MMovementLineMA.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_MovementLineMA_ID ignored
	 *	@param trxName trx
	 */
	public MMovementLineMA (Properties ctx, int M_MovementLineMA_ID,
		String trxName)
	{
		super (ctx, M_MovementLineMA_ID, trxName);
		if (M_MovementLineMA_ID != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MMovementLineMA

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result ser
	 *	@param trxName trx
	 */
	public MMovementLineMA (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MMovementLineMA
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param MovementQty qty
	 */
	public MMovementLineMA (MMovementLine parent, int M_AttributeSetInstance_ID, BigDecimal MovementQty)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setM_MovementLine_ID(parent.getM_MovementLine_ID());
		//
		setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
		setMovementQty(MovementQty);
	}	//	MMovementLineMA
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MMovementLineMA[");
		sb.append("M_MovementLine_ID=").append(getM_MovementLine_ID())
			.append(",M_AttributeSetInstance_ID=").append(getM_AttributeSetInstance_ID())
			.append(", Qty=").append(getMovementQty())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MMovementLineMA
