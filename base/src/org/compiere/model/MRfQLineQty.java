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
 *	RfQ Line Qty Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQLineQty.java,v 1.10 2005/11/14 02:10:53 jjanke Exp $
 */
public class MRfQLineQty extends X_C_RfQLineQty
{
	/**
	 * 	Get MRfQLineQty from Cache
	 *	@param ctx context
	 *	@param C_RfQLineQty_ID id
	 *	@return MRfQLineQty
	 */
	public static MRfQLineQty get (Properties ctx, int C_RfQLineQty_ID, String trxName)
	{
		Integer key = new Integer (C_RfQLineQty_ID);
		MRfQLineQty retValue = (MRfQLineQty) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MRfQLineQty (ctx, C_RfQLineQty_ID, trxName);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MRfQLineQty>	s_cache	= new CCache<Integer,MRfQLineQty>("C_RfQLineQty", 20);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RfQLineQty_ID id
	 */
	public MRfQLineQty (Properties ctx, int C_RfQLineQty_ID, String trxName)
	{
		super (ctx, C_RfQLineQty_ID, trxName);
		if (C_RfQLineQty_ID == 0)
		{
		//	setC_RfQLine_ID (0);
		//	setC_UOM_ID (0);
			setIsOfferQty (false);
			setIsPurchaseQty (false);
			setQty (Env.ONE);	// 1
		}
	}	//	MRfQLineQty

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRfQLineQty (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
		if (get_ID() > 0)
			s_cache.put (new Integer (get_ID()), this);
	}	//	MRfQLineQty
	
	/**
	 * 	Parent Constructor
	 *	@param line RfQ line
	 */
	public MRfQLineQty (MRfQLine line)
	{
		this (line.getCtx(), 0, line.get_TrxName());
		setClientOrg(line);
		setC_RfQLine_ID (line.getC_RfQLine_ID());
	}	//	MRfQLineQty
	
	/**	Unit of Measure		*/
	private MUOM	m_uom = null;
	
	/**
	 * 	Get Uom Name
	 *	@return UOM
	 */
	public String getUomName()
	{
		if (m_uom == null)
			m_uom = MUOM.get(getCtx(), getC_UOM_ID());
		return m_uom.getName();
	}	//	getUomText
	
	/**
	 * 	Get active Response Qtys of this RfQ Qty
	 * 	@param onlyValidAmounts only valid amounts
	 *	@return array of response line qtys
	 */
	public MRfQResponseLineQty[] getResponseQtys (boolean onlyValidAmounts)
	{
		ArrayList<MRfQResponseLineQty> list = new ArrayList<MRfQResponseLineQty>();
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM C_RfQResponseLineQty WHERE C_RfQLineQty_ID=? AND IsActive='Y'";
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_RfQLineQty_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MRfQResponseLineQty qty = new MRfQResponseLineQty(getCtx(), rs, get_TrxName());
				if (onlyValidAmounts && !qty.isValidAmt())
					;
				else
					list.add (qty);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		MRfQResponseLineQty[] retValue = new MRfQResponseLineQty[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getResponseQtys
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MRfQLineQty[");
		sb.append(get_ID()).append(",Qty=").append(getQty())
			.append(",Offer=").append(isOfferQty())
			.append(",Purchase=").append(isPurchaseQty())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MRfQLineQty
