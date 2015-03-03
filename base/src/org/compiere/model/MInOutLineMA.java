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
 *	Shipment Material Allocation
 *	
 *  @author Jorg Janke
 *  @version $Id: MInOutLineMA.java,v 1.2 2005/09/28 01:34:03 jjanke Exp $
 */
public class MInOutLineMA extends X_M_InOutLineMA
{
	/**
	 * 	Get Material Allocations for Line
	 *	@param ctx context
	 *	@param M_InOutLine_ID line
	 *	@param trxName trx
	 *	@return allocations
	 */
	public static MInOutLineMA[] get (Properties ctx, int M_InOutLine_ID, String trxName)
	{
		ArrayList<MInOutLineMA> list = new ArrayList<MInOutLineMA>();
		String sql = "SELECT * FROM M_InOutLineMA WHERE M_InOutLine_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_InOutLine_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MInOutLineMA (ctx, rs, trxName));
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
		
		MInOutLineMA[] retValue = new MInOutLineMA[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Delete all Material Allocation for InOut
	 *	@param M_InOut_ID shipment
	 *	@return number of rows deleted or -1 for error
	 */
	public static int deleteInOutMA (int M_InOut_ID, String trxName)
	{
		String sql = "DELETE FROM M_InOutLineMA ma WHERE EXISTS "
			+ "(SELECT * FROM M_InOutLine l WHERE l.M_InOutLine_ID=ma.M_InOutLine_ID"
			+ " AND M_InOut_ID=" + M_InOut_ID + ")";
		return DB.executeUpdate(sql, trxName);
	}	//	deleteInOutMA
	
	/**	Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MInOutLineMA.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InOutLineMA_ID ignored
	 *	@param trxName trx
	 */
	public MInOutLineMA (Properties ctx, int M_InOutLineMA_ID, String trxName)
	{
		super (ctx, M_InOutLineMA_ID, trxName);
		if (M_InOutLineMA_ID != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MInOutLineMA

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MInOutLineMA (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MInOutLineMA
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param MovementQty qty
	 */
	public MInOutLineMA (MInOutLine parent, int M_AttributeSetInstance_ID, BigDecimal MovementQty)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setM_InOutLine_ID(parent.getM_InOutLine_ID());
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
		StringBuffer sb = new StringBuffer ("MInOutLineMA[");
		sb.append("M_InOutLine_ID=").append(getM_InOutLine_ID())
			.append(",M_AttributeSetInstance_ID=").append(getM_AttributeSetInstance_ID())
			.append(", Qty=").append(getMovementQty())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MInOutLineMA
