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
 *	Product PO Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MProductPO.java,v 1.8 2005/10/14 00:44:31 jjanke Exp $
 */
public class MProductPO extends X_M_Product_PO
{
	/**
	 * 	Get current PO of Product
	 *	@param M_Product_ID product
	 *	@return PO - current vendor first
	 */
	public static MProductPO[] getOfProduct (Properties ctx, int M_Product_ID, String trxName)
	{
		ArrayList<MProductPO> list = new ArrayList<MProductPO>();
		String sql = "SELECT * FROM M_Product_PO "
			+ "WHERE M_Product_ID=? AND IsActive='Y' "
			+ "ORDER BY IsCurrentVendor DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				list.add(new MProductPO (ctx, rs, trxName));
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
		//
		MProductPO[] retValue = new MProductPO[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfProduct

	
	/** Static Logger					*/
	private static CLogger s_log = CLogger.getCLogger(MProductPO.class);

        public static MProductPO getCurrentOfProduct(Properties ctx, int m_Product_ID, String _TrxName) {
            MProductPO[] ppos = MProductPO.getOfProduct(ctx, m_Product_ID, _TrxName);
            for (int i = 0; i < ppos.length; i++)
            {
                    if (ppos[i].isCurrentVendor() && ppos[i].getC_BPartner_ID() != 0)
                    {
                            return ppos[i];	               	                			
                    }
            }
            return null;
        }    

	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MProductPO (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		else
		{
		//	setM_Product_ID (0);	// @M_Product_ID@
		//	setC_BPartner_ID (0);	// 0
		//	setVendorProductNo (null);	// @Value@
			setIsCurrentVendor (true);	// Y
		}
	}	//	MProduct_PO
	
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MProductPO(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProductPO

}	//	MProductPO
