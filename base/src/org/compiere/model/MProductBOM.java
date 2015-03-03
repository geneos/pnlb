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
 *	Product BOM Model.
 *	M_Product_ID = the parent 
 *	M_Product_BOM_ID = the BOM line
 *	M_ProductBOM_ID = the BOM line product
 *	
 *  @author Jorg Janke
 *  @version $Id: MProductBOM.java,v 1.11 2005/11/14 02:10:53 jjanke Exp $
 */
public class MProductBOM extends X_M_Product_BOM
{
	/**
	 * 	Get BOM Lines for Product
	 *	@param product product
	 *	@return array of BOMs
	 */
	public static MProductBOM[] getBOMLines (MProduct product)
	{
		return getBOMLines(product.getCtx(), product.getM_Product_ID(), product.get_TrxName());
	}	//	getBOMLines
	
	/**
	 * 	Get BOM Lines for Product
	 *	@param M_Product_ID product
	 *	@return array of BOMs
	 */
	public static MProductBOM[] getBOMLines (Properties ctx, int M_Product_ID, String trxName)
	{
		String sql = "SELECT * FROM M_Product_BOM WHERE M_Product_ID=? ORDER BY Line";
		ArrayList<MProductBOM> list = new ArrayList<MProductBOM>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, M_Product_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MProductBOM (ctx, rs, trxName));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getBOMLines", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		//
	//	s_log.fine("getBOMLines - #" + list.size() + " - M_Product_ID=" + M_Product_ID);
		MProductBOM[] retValue = new MProductBOM[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getBOMLines

	/** Static Logger					*/
	private static CLogger s_log = CLogger.getCLogger(MProductBOM.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Product_BOM_ID id
	 */
	public MProductBOM (Properties ctx, int M_Product_BOM_ID, String trxName)
	{
		super (ctx, M_Product_BOM_ID, trxName);
		if (M_Product_BOM_ID == 0)
		{
		//	setM_Product_ID (0);	//	parent
		//	setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_Product_BOM WHERE M_Product_ID=@M_Product_ID@
		//	setM_ProductBOM_ID(0);
			setBOMQty (Env.ZERO);	// 1
		}
	}	//	MProductBOM

	/**
	 * 	Load Construvtor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MProductBOM (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProductBOM

	/**	Included Product		*/
	private MProduct m_product = null;


	/**
	 * 	Get BOM Product
	 *	@return product
	 */
	public MProduct getProduct()
	{
		if (m_product == null && getM_ProductBOM_ID() != 0)
			m_product = MProduct.get (getCtx(), getM_ProductBOM_ID());
		return m_product;
	}	//	getProduct

	/**
	 * 	Set included Product
	 *	@param M_ProductBOM_ID product ID
	 */
	public void setM_ProductBOM_ID(int M_ProductBOM_ID)
	{
		super.setM_ProductBOM_ID (M_ProductBOM_ID);
		m_product = null;
	}	//	setM_ProductBOM_ID

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MProductBOM[");
		sb.append(get_ID()).append(",Line=").append(getLine())
			.append(",Type=").append(getBOMType()).append(",Qty=").append(getBOMQty());
		if (m_product == null)
			sb.append(",M_Product_ID=").append(getM_ProductBOM_ID());
		else
			sb.append(",").append(m_product);
		sb.append("]");
		return sb.toString();
	}	//	toString

	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord || is_ValueChanged("M_ProductBOM_ID"))
		{
			MProduct product = new MProduct (getCtx(), getM_Product_ID(), get_TrxName());
			if (get_TrxName() != null)
				product.load(get_TrxName());
			if (product.isVerified())
			{
				product.setIsVerified(false);
				product.save(get_TrxName());
			}
		}
		return success;
	}	//	afterSave
	
}	//	MProductBOM
