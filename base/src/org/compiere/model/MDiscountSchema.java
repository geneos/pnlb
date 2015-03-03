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
 *	Discount Schema Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDiscountSchema.java,v 1.13 2005/10/08 02:02:29 jjanke Exp $
 */
public class MDiscountSchema extends X_M_DiscountSchema
{
	/**
	 * 	Get Discount Schema from Cache
	 *	@param ctx context
	 *	@param M_DiscountSchema_ID id
	 *	@return MDiscountSchema
	 */
	public static MDiscountSchema get (Properties ctx, int M_DiscountSchema_ID)
	{
		Integer key = new Integer (M_DiscountSchema_ID);
		MDiscountSchema retValue = (MDiscountSchema) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MDiscountSchema (ctx, M_DiscountSchema_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,MDiscountSchema>	s_cache
		= new CCache<Integer,MDiscountSchema>("M_DiscountSchema", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_DiscountSchema_ID id
	 */
	public MDiscountSchema (Properties ctx, int M_DiscountSchema_ID, String trxName)
	{
		super (ctx, M_DiscountSchema_ID, trxName);
		if (M_DiscountSchema_ID == 0)
		{
		//	setName();
			setDiscountType (DISCOUNTTYPE_FlatPercent);
			setFlatDiscount(Env.ZERO);
			setIsBPartnerFlatDiscount (false);
			setIsQuantityBased (true);	// Y
			setCumulativeLevel(CUMULATIVELEVEL_Line);
		//	setValidFrom (new Timestamp(System.currentTimeMillis()));
		}	
	}	//	MDiscountSchema

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MDiscountSchema (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MDiscountSchema

	/**	Breaks							*/
	private MDiscountSchemaBreak[]	m_breaks  = null;
	/**	Lines							*/
	private MDiscountSchemaLine[]	m_lines  = null;
	
	/**
	 * 	Get Breaks
	 *	@param reload reload
	 *	@return breaks
	 */
	public MDiscountSchemaBreak[] getBreaks(boolean reload)
	{
		if (m_breaks != null && !reload)
			return m_breaks;
		
		String sql = "SELECT * FROM M_DiscountSchemaBreak WHERE M_DiscountSchema_ID=? ORDER BY SeqNo";
		ArrayList<MDiscountSchemaBreak> list = new ArrayList<MDiscountSchemaBreak>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getM_DiscountSchema_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MDiscountSchemaBreak(getCtx(), rs, get_TrxName()));
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
		m_breaks = new MDiscountSchemaBreak[list.size ()];
		list.toArray (m_breaks);
		return m_breaks;
	}	//	getBreaks
	
	/**
	 * 	Get Lines
	 *	@param reload reload
	 *	@return lines
	 */
	public MDiscountSchemaLine[] getLines(boolean reload)
	{
		if (m_lines != null && !reload)
			return m_lines;
		
		String sql = "SELECT * FROM M_DiscountSchemaLine WHERE M_DiscountSchema_ID=? ORDER BY SeqNo";
		ArrayList<MDiscountSchemaLine> list = new ArrayList<MDiscountSchemaLine>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getM_DiscountSchema_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MDiscountSchemaLine(getCtx(), rs, get_TrxName()));
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
		m_lines = new MDiscountSchemaLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getBreaks

	/**
	 * 	Calculate Discounted Price
	 *	@param Qty quantity
	 *	@param Price price
	 *	@param M_Product_ID product
	 *	@param BPartnerFlatDiscount flat discount
	 *	@return discount or zero
	 */
	public BigDecimal calculatePrice (BigDecimal Qty, BigDecimal Price,  
		int M_Product_ID, int M_Product_Category_ID,  
		BigDecimal BPartnerFlatDiscount, int C_BPartner_ID)
	{
		log.fine("Price=" + Price + ",Qty=" + Qty);
		if (Price == null || Env.ZERO.compareTo(Price) == 0)
			return Price;
		//
		BigDecimal discount = calculateDiscount(Qty, Price, 
			M_Product_ID, M_Product_Category_ID, BPartnerFlatDiscount, C_BPartner_ID);
		//	nothing to calculate
		if (discount == null || discount.signum() == 0)
			return Price;
		//
		BigDecimal onehundred = new BigDecimal(100);
		BigDecimal multiplier = (onehundred).subtract(discount);
		multiplier = multiplier.divide(onehundred, 5, BigDecimal.ROUND_HALF_UP);
		BigDecimal newPrice = Price.multiply(multiplier);
		log.fine("=>" + newPrice);
		return newPrice;
	}	//	calculatePrice

	/**
	 * 	Calculate Discount Percentage
	 *	@param Qty quantity
	 *	@param Price price
	 *	@param M_Product_ID product
	 *	@param BPartnerFlatDiscount flat discount
	 *	@return discount or zero
	 */
	public BigDecimal calculateDiscount (BigDecimal Qty, BigDecimal Price,  
		int M_Product_ID, int M_Product_Category_ID,
		BigDecimal BPartnerFlatDiscount, int C_BPartner_ID)
	{
		if (BPartnerFlatDiscount == null)
			BPartnerFlatDiscount = BigDecimal.ZERO;
		
		//
		if (DISCOUNTTYPE_FlatPercent.equals(getDiscountType()))
		{
			if (isBPartnerFlatDiscount())
				return BPartnerFlatDiscount;
			return getFlatDiscount();
		}
		else if (DISCOUNTTYPE_Formula.equals(getDiscountType()))
		{
			MProductDiscount prodDis = MProductDiscount.get(M_Product_ID,C_BPartner_ID);
			if (prodDis!=null)
			{
				BigDecimal discount = BigDecimal.ZERO;
				if (prodDis.getDiscount()!=null)
					discount = prodDis.getDiscount();
					
				if (prodDis.isExclusivo())
					return discount;
				return BPartnerFlatDiscount.add(discount);
			}
			else return BPartnerFlatDiscount;
		}
		//	Not supported
		else if (DISCOUNTTYPE_Pricelist.equals(getDiscountType()))
		{
			log.info ("Not supported (yet) DiscountType=" + getDiscountType());
			return Env.ZERO;
		}
		
		//	Price Breaks
		getBreaks(false);
		boolean found = false;
		BigDecimal Amt = Price.multiply(Qty);
		if (isQuantityBased())
			log.finer("Qty=" + Qty + ",M_Product_ID=" + M_Product_ID + ",M_Product_Category_ID=" + M_Product_Category_ID);
		else
			log.finer("Amt=" + Amt + ",M_Product_ID=" + M_Product_ID + ",M_Product_Category_ID=" + M_Product_Category_ID);
		for (int i = 0; i < m_breaks.length; i++)
		{
			MDiscountSchemaBreak br = m_breaks[i];
			if (!br.isActive())
				continue;
			
			if (isQuantityBased())
			{
				if (!br.applies(Qty, M_Product_ID, M_Product_Category_ID))
				{
					log.finer("No: " + br);
					continue;
				}
				log.finer("Yes: " + br);
			}
			else
			{
				if (!br.applies(Amt, M_Product_ID, M_Product_Category_ID))
				{
					log.finer("No: " + br);
					continue;
				}
				log.finer("Yes: " + br);
			}
			
			//	Line applies
			BigDecimal discount = null;
			if (br.isBPartnerFlatDiscount())
				discount = BPartnerFlatDiscount;
			else
				discount = br.getBreakDiscount();
			log.fine("Discount=>" + discount);
			return discount;
		}	//	for all breaks
		
		return Env.ZERO;
	}	//	calculateDiscount
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getValidFrom() == null)
			setValidFrom (TimeUtil.getDay(null));

		return true;
	}	//	beforeSave
	
	/**
	 * 	Renumber
	 *	@return lines updated
	 */
	public int reSeq()
	{
		int count = 0;
		//	Lines
		MDiscountSchemaLine[] lines = getLines(true);
		for (int i = 0; i < lines.length; i++)
		{
			int line = (i+1) * 10;
			if (line != lines[i].getSeqNo())
			{
				lines[i].setSeqNo(line);
				if (lines[i].save())
					count++;
			}
		}
		m_lines = null;
		
		//	Breaks
		MDiscountSchemaBreak[] breaks = getBreaks(true);
		for (int i = 0; i < breaks.length; i++)
		{
			int line = (i+1) * 10;
			if (line != breaks[i].getSeqNo())
			{
				breaks[i].setSeqNo(line);
				if (breaks[i].save())
					count++;
			}
		}
		m_breaks = null;
		return count;
	}	//	reSeq
	
}	//	MDiscountSchema
