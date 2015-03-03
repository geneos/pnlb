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
 *	Distribution Run Detail
 *	
 *  @author Jorg Janke
 *  @version $Id: MDistributionRunDetail.java,v 1.7 2005/11/14 02:10:53 jjanke Exp $
 */
public class MDistributionRunDetail extends X_T_DistributionRunDetail
{
	/**
	 * 	Get Distribution Dun details
	 *	@param ctx context
	 *	@param M_DistributionRun_ID id
	 *	@param orderBP if true ordered by Business Partner otherwise Run Line
	 *	@return array of details
	 */
	static public MDistributionRunDetail[] get (Properties ctx, int M_DistributionRun_ID, 
		boolean orderBP)
	{
		ArrayList<MDistributionRunDetail> list = new ArrayList<MDistributionRunDetail>();
		String sql = "SELECT * FROM T_DistributionRunDetail WHERE M_DistributionRun_ID=? ";
		if (orderBP)
			sql += "ORDER BY C_BPartner_ID, C_BPartner_Location_ID";
		else
			sql += "ORDER BY M_DistributionRunLine_ID";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, M_DistributionRun_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MDistributionRunDetail(ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "get", e);
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
		MDistributionRunDetail[] retValue = new MDistributionRunDetail[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MDistributionRunDetail.class);
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MDistributionRunDetail (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	DistributionRunDetail
	
	/**	Precision		*/
	private int	m_precision = 0;
	
	/**
	 * 	Round MinQty & Qty
	 *	@param precision precision (saved)
	 */
	public void round (int precision)
	{
		boolean dirty = false;
		m_precision = precision; 
		BigDecimal min = getMinQty();
		if (min.scale() > m_precision)
		{
			setMinQty(min.setScale(m_precision, BigDecimal.ROUND_HALF_UP));
			dirty = true;
		}
		BigDecimal qty = getQty();
		if (qty.scale() > m_precision)
		{
			setQty(qty.setScale(m_precision, BigDecimal.ROUND_HALF_UP));
			dirty = true;
		}
		if (dirty)
			save();
	}	//	round
	
	/**
	 * 	We can adjust Allocation Qty
	 *	@return true if qty > min
	 */
	public boolean isCanAdjust()
	{
		return (getQty().compareTo(getMinQty()) > 0);
	}	//	isCanAdjust

	/**
	 * 	Get Actual Allocation Qty
	 *	@return the greater of the min/qty
	 */
	public BigDecimal getActualAllocation()
	{
		if (getQty().compareTo(getMinQty()) > 0)
			return getQty();
		else
			return getMinQty();
	}	//	getActualAllocation

	/**
	 * 	Adjust the Quantity maintaining UOM precision
	 * 	@param difference difference
	 * 	@return remaining difference (because under Min or rounding)
	 */
	public BigDecimal adjustQty (BigDecimal difference)
	{
		BigDecimal diff = difference.setScale(m_precision, BigDecimal.ROUND_HALF_UP);
		BigDecimal qty = getQty();
		BigDecimal max = getMinQty().subtract(qty);
		BigDecimal remaining = Env.ZERO;
		if (max.compareTo(diff) > 0)	//	diff+max are negative
		{
			remaining = diff.subtract(max);
			setQty(qty.add(max));
		}
		else
			setQty(qty.add(diff));
		log.fine("adjustQty - Qty=" + qty + ", Min=" + getMinQty() 
			+ ", Max=" + max + ", Diff=" + diff + ", newQty=" + getQty() 
			+ ", Remaining=" + remaining);
		return remaining;
	}	//	adjustQty
	
}	//	DistributionRunDetail
