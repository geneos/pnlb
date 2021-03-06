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
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Post MatchInv Documents.
 *  <pre>
 *  Table:              M_MatchInv (472)
 *  Document Types:     MXI
 *  </pre>
 *  Update Costing Records
 *  @author Jorg Janke
 *  @version  $Id: Doc_MatchInv.java,v 1.25 2005/12/20 04:22:07 jjanke Exp $
 */
public class Doc_MatchInv extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@parem trxName trx
	 */
	protected Doc_MatchInv (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super(ass, MMatchInv.class, rs, DOCTYPE_MatMatchInv, trxName);
	}   //  Doc_MatchInv

	/** Invoice Line			*/
	private MInvoiceLine	m_invoiceLine = null;
	/** Material Receipt		*/
	private MInOutLine		m_receiptLine = null;
	
	private ProductCost		m_pc = null;

	/** Commitments			*/
	private DocLine[]	m_commitments = null;
	
	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		setC_Currency_ID (Doc.NO_CURRENCY);
		MMatchInv matchInv = (MMatchInv)getPO();
		setDateDoc(matchInv.getDateTrx());
		setQty (matchInv.getQty());
		//	Invoice Info
		int C_InvoiceLine_ID = matchInv.getC_InvoiceLine_ID();
		m_invoiceLine = new MInvoiceLine (getCtx(), C_InvoiceLine_ID, null);
		//		BP for NotInvoicedReceipts
		int C_BPartner_ID = m_invoiceLine.getParent().getC_BPartner_ID(); 
		setC_BPartner_ID(C_BPartner_ID);
		//
		int M_InOutLine_ID = matchInv.getM_InOutLine_ID();
		m_receiptLine = new MInOutLine (getCtx(), M_InOutLine_ID, null);		
		//
		m_pc = new ProductCost (Env.getCtx(), 
			getM_Product_ID(), matchInv.getM_AttributeSetInstance_ID(), null);
		m_pc.setQty(getQty());
		
		return null;
	}   //  loadDocumentDetails


	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line and tax amounts from total - no rounding
	 *  @return Zero (always balanced)
	 */
	public BigDecimal getBalance()
	{
		return Env.ZERO;
	}   //  getBalance

	
	/**
	 *  Create Facts (the accounting logic) for
	 *  MXI.
	 * 	(single line)
	 *  <pre>
	 *      NotInvoicedReceipts     DR			(Receipt Org)
	 *      InventoryClearing               CR
	 *      InvoicePV               DR      CR  (difference)
	 *  Commitment
	 * 		Expense							CR
	 * 		Offset					DR
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		ArrayList<Fact> facts = new ArrayList<Fact>();
		//  Nothing to do
		if (getM_Product_ID() == 0								//	no Product
			|| getQty().signum() == 0
			|| m_receiptLine.getMovementQty().signum() == 0)	//	Qty = 0
		{
			log.fine("No Product/Qty - M_Product_ID=" + getM_Product_ID()
				+ ",Qty=" + getQty() + ",InOutQty=" + m_receiptLine.getMovementQty());
			return facts;
		}
		MMatchInv matchInv = (MMatchInv)getPO();
		
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		setC_Currency_ID (as.getC_Currency_ID());

		/**	Needs to be handeled in PO Matching as no Receipt info
		if (m_pc.isService())
		{
			log.fine("Service - skipped");
			return fact;
		}
		**/
		
		
		//  NotInvoicedReceipt      DR
		//  From Receipt
		BigDecimal multiplier = getQty()
			.divide(m_receiptLine.getMovementQty(), 12, BigDecimal.ROUND_HALF_UP)
			.abs();
		FactLine dr = fact.createLine (null,
			getAccount(Doc.ACCTTYPE_NotInvoicedReceipts, as),
			as.getC_Currency_ID(), Env.ONE, null);			// updated below
		if (dr == null)
		{
			p_Error = "No Product Costs";
			return null;
		}
		dr.setQty(getQty());
	//	dr.setM_Locator_ID(m_receiptLine.getM_Locator_ID());
	//	MInOut receipt = m_receiptLine.getParent();
	//	dr.setLocationFromBPartner(receipt.getC_BPartner_Location_ID(), true);	//  from Loc
	//	dr.setLocationFromLocator(m_receiptLine.getM_Locator_ID(), false);		//  to Loc
		BigDecimal temp = dr.getAcctBalance();
		//	Set AmtAcctCr/Dr from Receipt (sets also Project)
		if (!dr.updateReverseLine (MInOut.getTableId(MInOut.Table_Name), 		//	Amt updated
			m_receiptLine.getM_InOut_ID(), m_receiptLine.getM_InOutLine_ID(),
			multiplier))
		{
			p_Error = "Mat.Receipt not posted yet";
			return null;
		}
		log.fine("CR - Amt(" + temp + "->" + dr.getAcctBalance() 
			+ ") - " + dr.toString());

		//  InventoryClearing               CR
		//  From Invoice
		MAccount expense = m_pc.getAccount(ProductCost.ACCTTYPE_P_InventoryClearing, as);
		if (m_pc.isService())
			expense = m_pc.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
		BigDecimal LineNetAmt = m_invoiceLine.getLineNetAmt();
		multiplier = getQty()
			.divide(m_invoiceLine.getQtyInvoiced(), 12, BigDecimal.ROUND_HALF_UP)
			.abs();
		if (multiplier.compareTo(Env.ONE) != 0)
			LineNetAmt = LineNetAmt.multiply(multiplier);
		if (m_pc.isService())
			LineNetAmt = dr.getAcctBalance();	//	book out exact receipt amt
		FactLine cr = null;
		if (as.isAccrual())
		{
			cr = fact.createLine (null, expense,
				as.getC_Currency_ID(), null, LineNetAmt);		//	updated below
			if (cr == null)
			{
				log.fine("Line Net Amt=0 - M_Product_ID=" + getM_Product_ID()
					+ ",Qty=" + getQty() + ",InOutQty=" + m_receiptLine.getMovementQty());
				facts.add(fact);
				return facts;
			}
			cr.setQty(getQty().negate());
			temp = cr.getAcctBalance();
			//	Set AmtAcctCr/Dr from Invoice (sets also Project)
//TODO REVISAR REFERENCIAS
			if (as.isAccrual() && !cr.updateReverseLine (MInvoice.getTableId(MInvoice.Table_Name), 		//	Amt updated
//			if (as.isAccrual() && !cr.updateReverseLine (318, 		//	Amt updated			
				m_invoiceLine.getC_Invoice_ID(), m_invoiceLine.getC_InvoiceLine_ID(), multiplier))
			{
				p_Error = "Invoice not posted yet";
				return null;
			}
			log.fine("DR - Amt(" + temp + "->" + cr.getAcctBalance() 
				+ ") - " + cr.toString());
		}
		else	//	Cash Acct
		{
			MInvoice invoice = m_invoiceLine.getParent();
			if (as.getC_Currency_ID() == invoice.getC_Currency_ID())
				LineNetAmt = MConversionRate.convert(getCtx(), LineNetAmt, 
					invoice.getC_Currency_ID(), as.getC_Currency_ID(),
					invoice.getDateAcct(), invoice.getC_ConversionType_ID(),
					invoice.getAD_Client_ID(), invoice.getAD_Org_ID());
			cr = fact.createLine (null, expense,
				as.getC_Currency_ID(), null, LineNetAmt);
			cr.setQty(getQty().multiply(multiplier).negate());
		}
		cr.setC_Activity_ID(m_invoiceLine.getC_Activity_ID());
		cr.setC_Campaign_ID(m_invoiceLine.getC_Campaign_ID());
		cr.setC_Project_ID(m_invoiceLine.getC_Project_ID());
		cr.setC_UOM_ID(m_invoiceLine.getC_UOM_ID());
		cr.setUser1_ID(m_invoiceLine.getUser1_ID());
		cr.setUser2_ID(m_invoiceLine.getUser2_ID());


		//  Invoice Price Variance 	difference
		BigDecimal ipv = cr.getAcctBalance().add(dr.getAcctBalance()).negate();
		if (ipv.signum() != 0)
		{
			FactLine pv = fact.createLine(null,
				m_pc.getAccount(ProductCost.ACCTTYPE_P_IPV, as),
				as.getC_Currency_ID(), ipv);
			pv.setC_Activity_ID(m_invoiceLine.getC_Activity_ID());
			pv.setC_Campaign_ID(m_invoiceLine.getC_Campaign_ID());
			pv.setC_Project_ID(m_invoiceLine.getC_Project_ID());
			pv.setC_UOM_ID(m_invoiceLine.getC_UOM_ID());
			pv.setUser1_ID(m_invoiceLine.getUser1_ID());
			pv.setUser2_ID(m_invoiceLine.getUser2_ID());
		}
		log.fine("IPV=" + ipv + "; Balance=" + fact.getSourceBalance());

		//	Cost Detail Record - data from Expense/IncClearing (CR) record
		MCostDetail.createInvoice(as, getAD_Org_ID(), 
			getM_Product_ID(), matchInv.getM_AttributeSetInstance_ID(),
			m_invoiceLine.getC_InvoiceLine_ID(), 0,		//	No cost element
			cr.getAcctBalance().negate(), getQty(),		//	correcting
			getDescription(), getTrxName());

		//  Update Costing
		updateProductInfo(as.getC_AcctSchema_ID(), 
			MAcctSchema.COSTINGMETHOD_StandardCosting.equals(as.getCostingMethod()));
		//
		facts.add(fact);
		
		/** Commitment release										****/
		if (as.isAccrual() && as.isCreateCommitment())
		{
			fact = Doc_Order.getCommitmentRelease(as, this, 
				getQty(), m_invoiceLine.getC_InvoiceLine_ID(), Env.ONE);
			if (fact == null)
				return null;
			facts.add(fact);
		}	//	Commitment
		
		return facts;
	}   //  createFact

	/**
	 *  Update Product Info (old).
	 *  - Costing (CostStandardCumQty, CostStandardCumAmt, CostAverageCumQty, CostAverageCumAmt)
	 *  @param C_AcctSchema_ID accounting schema
	 *  @param standardCosting true if std costing
	 *  @deprecated old costing
	 */
	private boolean updateProductInfo (int C_AcctSchema_ID, boolean standardCosting)
	{
		log.fine("M_MatchInv_ID=" + get_ID());

		//  update Product Costing Qty/Amt
		//  requires existence of currency conversion !!
		StringBuffer sql = new StringBuffer (
			"UPDATE M_Product_Costing pc "
			+ "SET (CostStandardCumQty,CostStandardCumAmt, CostAverageCumQty,CostAverageCumAmt) = "
			+ "(SELECT pc.CostStandardCumQty + m.Qty,"
			+ "pc.CostStandardCumAmt + currencyConvert(il.PriceActual,i.C_Currency_ID,a.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*m.Qty, "
			+ "pc.CostAverageCumQty + m.Qty,"
			+ "pc.CostAverageCumAmt + currencyConvert(il.PriceActual,i.C_Currency_ID,a.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*m.Qty "
			+ "FROM M_MatchInv m"
			+ " INNER JOIN C_InvoiceLine il ON (m.C_InvoiceLine_ID=il.C_InvoiceLine_ID)"
			+ " INNER JOIN C_Invoice i ON (il.C_Invoice_ID=i.C_Invoice_ID),"
			+ " C_AcctSchema a "
			+ "WHERE pc.C_AcctSchema_ID=a.C_AcctSchema_ID"
			+ " AND pc.M_Product_ID=m.M_Product_ID"
			+ " AND m.M_MatchInv_ID=").append(get_ID()).append(")"
			//
			+ "WHERE pc.C_AcctSchema_ID=").append(C_AcctSchema_ID).append(
			  " AND EXISTS (SELECT * FROM M_MatchInv m "
				+ "WHERE pc.M_Product_ID=m.M_Product_ID"
				+ " AND m.M_MatchInv_ID=").append(get_ID()).append(")"); 
		int no = DB.executeUpdate(sql.toString(), getTrxName());
		log.fine("M_Product_Costing - Qty/Amt Updated #=" + no);

		//  Update Average Cost
		sql = new StringBuffer (
			"UPDATE M_Product_Costing "
			+ "SET CostAverage = CostAverageCumAmt/DECODE(CostAverageCumQty, 0,1, CostAverageCumQty) "
			+ "WHERE C_AcctSchema_ID=").append(C_AcctSchema_ID)
			.append(" AND M_Product_ID=").append(getM_Product_ID());
		no = DB.executeUpdate(sql.toString(), getTrxName());
		log.fine("M_Product_Costing - AvgCost Updated #=" + no);
		

		//  Update Current Cost
		if (!standardCosting)
		{
			sql = new StringBuffer (
				"UPDATE M_Product_Costing "
				+ "SET CurrentCostPrice = CostAverage "
				+ "WHERE C_AcctSchema_ID=").append(C_AcctSchema_ID)
				.append(" AND M_Product_ID=").append(getM_Product_ID());
			no = DB.executeUpdate(sql.toString(), getTrxName());
			log.fine("M_Product_Costing - CurrentCost Updated=" + no);
		}
		return true;
	}   //  updateProductInfo

}   //  Doc_MatchInv
