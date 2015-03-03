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
 *	Material Distribution List
 *	
 *  @author Jorg Janke
 *  @version $Id: MDistributionList.java,v 1.5 2005/09/24 01:52:52 jjanke Exp $
 */
public class MDistributionList extends X_M_DistributionList
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_DistributionList_ID id
	 */
	public MDistributionList (Properties ctx, int M_DistributionList_ID, String trxName)
	{
		super (ctx, M_DistributionList_ID, trxName);
	}	//	MDistributionList

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MDistributionList (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MDistributionList
	
	/**
	 * 	Get Distibution Lines.
	 * 	Add/Correct also Total Ratio
	 *	@return array of lines
	 */
	public MDistributionListLine[] getLines()
	{
		ArrayList<MDistributionListLine> list = new ArrayList<MDistributionListLine>();
		BigDecimal ratioTotal = Env.ZERO;
		//
		String sql = "SELECT * FROM M_DistributionListLine WHERE M_DistributionList_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getM_DistributionList_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MDistributionListLine line = new MDistributionListLine(getCtx(), rs, get_TrxName());
				list.add(line);
				BigDecimal ratio = line.getRatio();
				if (ratio != null)
					ratioTotal = ratioTotal.add(ratio);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getLines", e);
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
		//	Update Ratio
		if (ratioTotal.compareTo(getRatioTotal()) != 0)
		{
			log.info("getLines - Set RatioTotal from " + getRatioTotal() + " to " + ratioTotal);
			setRatioTotal(ratioTotal);
			save();
		}
		
		MDistributionListLine[] retValue = new MDistributionListLine[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getLines
	
	
	
}	//	MDistributionList
