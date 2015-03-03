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
package org.compiere.acct;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Post MatchPO Documents.
 *  <pre>
 *  Table:              C_MatchPO (473)
 *  Document Types:     MXP
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_MatchPO.java,v 1.23 2005/12/20 04:22:07 jjanke Exp $
 */
public class Doc_MatchPO extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@parem trxName trx
	 */
	protected Doc_MatchPO (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super(ass, MMatchPO.class, rs, DOCTYPE_MatMatchPO, trxName);
	}   //  Doc_MatchPO

	private int         m_C_OrderLine_ID = 0;
	private MOrderLine	m_oLine = null;
	//
	private int         m_M_InOutLine_ID = 0;
	private int         m_C_InvoiceLine_ID = 0;
	private ProductCost m_pc;
	private int			m_M_AttributeSetInstance_ID = 0;

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		setC_Currency_ID (Doc.NO_CURRENCY);
		MMatchPO matchPO = (MMatchPO)getPO();
		setDateDoc(matchPO.getDateTrx());
		//
		m_M_AttributeSetInstance_ID = matchPO.getM_AttributeSetInstance_ID();
		setQty (matchPO.getQty());
		//
		m_C_OrderLine_ID = matchPO.getC_OrderLine_ID();
		m_oLine = new MOrderLine (getCtx(), m_C_OrderLine_ID, getTrxName());
		//
		m_M_InOutLine_ID = matchPO.getM_InOutLine_ID();
		m_C_InvoiceLine_ID = matchPO.getC_InvoiceLine_ID();
		//
		m_pc = new ProductCost (Env.getCtx(), 
			getM_Product_ID(), m_M_AttributeSetInstance_ID, getTrxName());
		m_pc.setQty(getQty());
		return null;
	}   //  loadDocumentDetails

	
	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line and tax amounts from total - no rounding
	 *  @return Zero - always balanced
	 */
	public BigDecimal getBalance()
	{
		return Env.ZERO;
	}   //  getBalance

	
	/**
	 *  Create Facts (the accounting logic) for
	 *  MXP.
	 *  <pre>
	 *      Product PPV     <difference>
	 *      PPV_Offset                  <difference>
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		ArrayList<Fact> facts = new ArrayList<Fact>();
		//
		if (getM_Product_ID() == 0		//  Nothing to do if no Product
			|| getQty().signum() == 0
			|| m_M_InOutLine_ID == 0)	//  No posting if not matched to Shipment
		{
			log.fine("No Product/Qty - M_Product_ID=" + getM_Product_ID()
				+ ",Qty=" + getQty());
			return facts;
		}

		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		setC_Currency_ID(as.getC_Currency_ID());
		
		//	Purchase Order Line
		BigDecimal poCost = m_oLine.getPriceCost();
		if (poCost == null || poCost.signum() == 0)
			poCost = m_oLine.getPriceActual();
		poCost = poCost.multiply(getQty());			//	Delivered so far
		//	Different currency
		if (m_oLine.getC_Currency_ID() != as.getC_Currency_ID())
		{
			MOrder order = m_oLine.getParent();
			BigDecimal rate = MConversionRate.getRate(
				order.getC_Currency_ID(), as.getC_Currency_ID(),
				order.getDateAcct(), order.getC_ConversionType_ID(),
				m_oLine.getAD_Client_ID(), m_oLine.getAD_Org_ID());
			if (rate == null)
			{
				p_Error = "Purchase Order not convertible - " + as.getName();
				return null;
			}
			poCost = poCost.multiply(rate);
			if (poCost.scale() > as.getCostingPrecision())
				poCost = poCost.setScale(as.getCostingPrecision(), BigDecimal.ROUND_HALF_UP);
		}

		//	Create PO Cost Detail Record firs
		MCostDetail.createOrder(as, m_oLine.getAD_Org_ID(), 
			getM_Product_ID(), m_M_AttributeSetInstance_ID,
			m_C_OrderLine_ID, 0,		//	no cost element
			poCost, getQty(),			//	Delivered
			m_oLine.getDescription(), getTrxName());

		//	Current Costs
		String costingMethod = as.getCostingMethod();
		MProduct product = MProduct.get(getCtx(), getM_Product_ID());
		MProductCategoryAcct pca = MProductCategoryAcct.get(getCtx(), 
			product.getM_Product_Category_ID(), as.getC_AcctSchema_ID(), getTrxName());
		if (pca.getCostingMethod() != null)
			costingMethod = pca.getCostingMethod();
		BigDecimal costs = m_pc.getProductCosts(as, getAD_Org_ID(), 
			costingMethod, m_C_OrderLine_ID, false);	//	non-zero costs

		//	No Costs yet - no PPV
		if (costs == null || costs.signum() == 0)
		{
			p_Error = "Resubmit - No Costs for " + product.getName();
			log.log(Level.SEVERE, p_Error);
			return null;
		}

		//	Difference
		BigDecimal difference = poCost.subtract(costs);
		//	Nothing to post
		if (difference.signum() == 0)
		{
			log.log(Level.FINE, "No Cost Difference for M_Product_ID=" + getM_Product_ID());
			facts.add(fact);
			return facts;
		}

		//  Product PPV
		FactLine cr = fact.createLine(null,
			m_pc.getAccount(ProductCost.ACCTTYPE_P_PPV, as),
			as.getC_Currency_ID(), difference);
		if (cr != null)
		{
			cr.setQty(getQty());
			cr.setC_BPartner_ID(m_oLine.getC_BPartner_ID());
			cr.setC_Activity_ID(m_oLine.getC_Activity_ID());
			cr.setC_Campaign_ID(m_oLine.getC_Campaign_ID());
			cr.setC_Project_ID(m_oLine.getC_Project_ID());
			cr.setC_UOM_ID(m_oLine.getC_UOM_ID());
			cr.setUser1_ID(m_oLine.getUser1_ID());
			cr.setUser2_ID(m_oLine.getUser2_ID());
		}

		//  PPV Offset
		FactLine dr = fact.createLine(null,
			getAccount(Doc.ACCTTYPE_PPVOffset, as),
			as.getC_Currency_ID(), difference.negate());
		if (dr != null)
		{
			dr.setQty(getQty().negate());
			dr.setC_BPartner_ID(m_oLine.getC_BPartner_ID());
			dr.setC_Activity_ID(m_oLine.getC_Activity_ID());
			dr.setC_Campaign_ID(m_oLine.getC_Campaign_ID());
			dr.setC_Project_ID(m_oLine.getC_Project_ID());
			dr.setC_UOM_ID(m_oLine.getC_UOM_ID());
			dr.setUser1_ID(m_oLine.getUser1_ID());
			dr.setUser2_ID(m_oLine.getUser2_ID());
		}

		//
		facts.add(fact);
		return facts;
	}   //  createFact

	/**
	 *  Update Product Info (old).
	 *  - Costing (CostStandardPOQty, CostStandardPOAmt)
	 *  @param C_AcctSchema_ID accounting schema
	 *  @deprecated old costing
	 */
	private void updateProductInfo (int C_AcctSchema_ID)
	{
		log.fine("M_MatchPO_ID=" + get_ID());

		//  update Product Costing
		//  requires existence of currency conversion !!
		StringBuffer sql = new StringBuffer (
			"UPDATE M_Product_Costing pc "
			+ "SET (CostStandardPOQty,CostStandardPOAmt) = "
			+ "(SELECT CostStandardPOQty + m.Qty,"
			+ " CostStandardPOAmt + currencyConvert(ol.PriceActual,ol.C_Currency_ID,a.C_Currency_ID,ol.DateOrdered,null,ol.AD_Client_ID,ol.AD_Org_ID)*m.Qty "
			+ "FROM M_MatchPO m, C_OrderLine ol, C_AcctSchema a "
			+ "WHERE m.C_OrderLine_ID=ol.C_OrderLine_ID"
			+ " AND pc.M_Product_ID=ol.M_Product_ID"
			+ " AND pc.C_AcctSchema_ID=a.C_AcctSchema_ID"
			+ "AND m.M_MatchPO_ID=").append(get_ID()).append(") ")
			.append("WHERE pc.C_AcctSchema_ID=").append(C_AcctSchema_ID)
			.append(" AND pc.M_Product_ID=").append(getM_Product_ID());
		int no = DB.executeUpdate(sql.toString(), getTrxName());
		log.fine("M_Product_Costing - Updated=" + no);
	}   //  updateProductInfo

}   //  Doc_MatchPO
