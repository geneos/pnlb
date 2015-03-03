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
import org.compiere.util.*;

/**
 *	Distribution Run List Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDistributionRunLine.java,v 1.6 2005/09/19 04:49:46 jjanke Exp $
 */
public class MDistributionRunLine extends X_M_DistributionRunLine
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_DistributionRunLine_ID id
	 */
	public MDistributionRunLine (Properties ctx, int M_DistributionRunLine_ID, String trxName)
	{
		super (ctx, M_DistributionRunLine_ID, trxName);
	}	//	MDistributionRunLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MDistributionRunLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MDistributionRunLine
	
	/**	Product						*/
	private MProduct		m_product = null;
	/**	Actual Qty					*/
	private BigDecimal 		m_actualQty = Env.ZERO;
	/**	Actual Min					*/
	private BigDecimal 		m_actualMin = Env.ZERO;
	/**	Actual Allocation			*/
	private BigDecimal 		m_actualAllocation = Env.ZERO;
	/**	Last Allocation Difference	*/
	private BigDecimal 		m_lastDifference = Env.ZERO;
	/**	Max Allocation 				*/
	private BigDecimal 		m_maxAllocation = Env.ZERO;

	
	/**
	 * 	Get Actual Qty
	 *	@return actual Qty
	 */
	public BigDecimal getActualQty()
	{
		return m_actualQty;
	}	//	getActualQty
	
	/**
	 * 	Add to Actual Qty
	 *	@param add number to add
	 */
	public void addActualQty(BigDecimal add)
	{
		m_actualQty = m_actualQty.add(add);
	}	//	addActualQty

	/**
	 * 	Get Actual Min Qty
	 *	@return actual Min Qty
	 */
	public BigDecimal getActualMin()
	{
		return m_actualMin;
	}	//	getActualMin
	
	/**
	 * 	Add to Actual Min Qty
	 *	@param add number to add
	 */
	public void addActualMin(BigDecimal add)
	{
		m_actualMin = m_actualMin.add(add);
	}	//	addActualMin

	/**
	 * 	Is Actual Min Greater than Total
	 *	@return true if act min > total
	 */
	public boolean isActualMinGtTotal()
	{
		return m_actualMin.compareTo(getTotalQty()) > 0;
	}	//	isActualMinGtTotal


	/**
	 * 	Get Actual Allocation Qty
	 *	@return actual Allocation Qty
	 */
	public BigDecimal getActualAllocation()
	{
		return m_actualAllocation;
	}	//	getActualAllocation
	
	/**
	 * 	Add to Actual Min Qty
	 *	@param add number to add
	 */
	public void addActualAllocation(BigDecimal add)
	{
		m_actualAllocation = m_actualAllocation.add(add);
	}	//	addActualAllocation

	/**
	 * 	Is Actual Allocation equals Total
	 *	@return true if act allocation = total
	 */
	public boolean isActualAllocationEqTotal()
	{
		return m_actualAllocation.compareTo(getTotalQty()) == 0;
	}	//	isActualAllocationEqTotal

	/**
	 * 	Get Allocation Difference
	 *	@return Total - Allocation Qty 
	 */
	public BigDecimal getActualAllocationDiff()
	{
		return getTotalQty().subtract(m_actualAllocation);
	}	//	getActualAllocationDiff

	
	/**
	 * 	Get Last Allocation Difference
	 *	@return difference
	 */
	public BigDecimal getLastDifference()
	{
		return m_lastDifference;
	}	//	getLastDifference
	
	/**
	 * 	Set Last Allocation Difference
	 *	@param difference difference
	 */
	public void setLastDifference(BigDecimal difference)
	{
		m_lastDifference = difference;
	}	//	setLastDifference
	
	/**
	 * 	Get Max Allocation
	 *	@return max allocation
	 */
	public BigDecimal getMaxAllocation()
	{
		return m_maxAllocation;
	}	//	getMaxAllocation
	
	/**
	 * 	Set Max Allocation if greater
	 *	@param max allocation
	 *	@param set set to max
	 */
	public void setMaxAllocation (BigDecimal max, boolean set)
	{
		if (set || max.compareTo(m_maxAllocation) > 0)
			m_maxAllocation = max;
	}	//	setMaxAllocation

	/**
	 * 	Reset Calculations
	 */
	public void resetCalculations()
	{
		m_actualQty = Env.ZERO;
		m_actualMin = Env.ZERO;
		m_actualAllocation = Env.ZERO;
	//	m_lastDifference = Env.ZERO;
		m_maxAllocation = Env.ZERO;
		
	}	//	resetCalculations
	
	
	/**************************************************************************
	 * 	Get Product
	 *	@return product
	 */
	public MProduct getProduct()
	{
		if (m_product == null)
			m_product = MProduct.get(getCtx(), getM_Product_ID());
		return m_product;
	}	//	getProduct
	
	/**
	 * 	Get Product Standard Precision
	 *	@return standard precision
	 */
	public int getStandardPrecision()
	{
		return getProduct().getStandardPrecision();
	}	//	getStandardPrecision
	
	
	/**************************************************************************
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MDistributionRunLine[");
		sb.append(get_ID()).append(getInfo())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Get Info
	 *	@return info
	 */
	public String getInfo()
	{
		StringBuffer sb = new StringBuffer ();
		sb.append("Line=").append(getLine())
			.append (",TotalQty=").append(getTotalQty())
			.append(",SumMin=").append(getActualMin())
			.append(",SumQty=").append(getActualQty())
			.append(",SumAllocation=").append(getActualAllocation())
			.append(",MaxAllocation=").append(getMaxAllocation())
			.append(",LastDiff=").append(getLastDifference());
		return sb.toString ();
	}	//	getInfo
	
}	//	MDistributionRunLine
