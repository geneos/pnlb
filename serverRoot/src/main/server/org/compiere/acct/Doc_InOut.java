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
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Post Shipment/Receipt Documents.
 *  <pre>
 *  Table:              M_InOut (319)
 *  Document Types:     MMS, MMR
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_InOut.java,v 1.19 2005/12/20 04:22:07 jjanke Exp $
 */
public class Doc_InOut extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@parem trxName trx
	 */
	public Doc_InOut (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, MInOut.class, rs, null, trxName);
	}   //  DocInOut

	/**
	 *  Load Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails()
	{
		setC_Currency_ID(NO_CURRENCY);
		MInOut inout = (MInOut)getPO();
		setDateDoc (inout.getMovementDate());
		//	Contained Objects
		p_lines = loadLines(inout);
		log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 *	Load Invoice Line
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines(MInOut inout)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MInOutLine[] lines = inout.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInOutLine line = lines[i];
			if (line.isDescription() 
				|| line.getM_Product_ID() == 0
				|| line.getMovementQty().signum() == 0)
			{
				log.finer("Ignored: " + line);
				continue;
			}
			
			DocLine docLine = new DocLine (line, this);
			BigDecimal Qty = line.getMovementQty();
			docLine.setQty (Qty, getDocumentType().equals(DOCTYPE_MatShipment));    //  sets Trx and Storage Qty
			//
			log.fine(docLine.toString());
			list.add (docLine);
		}

		//	Return Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);
		return dls;
	}	//	loadLines

	/**
	 *  Get Balance
	 *  @return Zero (always balanced)
	 */
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;
		return retValue;
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  MMS, MMR.
	 *  <pre>
	 *  Shipment
	 *      CoGS (RevOrg)   DR
	 *      Inventory               CR
	 *  Shipment of Project Issue
	 *      CoGS            DR
	 *      Project                 CR
	 *  Receipt
	 *      Inventory       DR
	 *      NotInvoicedReceipt      CR
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		setC_Currency_ID (as.getC_Currency_ID());

		//  Line pointers
		FactLine dr = null;
		FactLine cr = null;

		//  *** Sales - Shipment
		if (getDocumentType().equals(DOCTYPE_MatShipment))
		{
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				BigDecimal costs = line.getProductCosts(as, line.getAD_Org_ID(), true);
				if (costs == null || costs.signum() == 0)	//	zero costs OK
				{
					MProduct product = line.getProduct();
					if (product.isStocked())
					{
						p_Error = "No Costs for " + line.getProduct().getName();
						log.log(Level.SEVERE, p_Error);
						return null;
					}
					else	//	ignore service
						continue;
				}
				//  CoGS            DR
				dr = fact.createLine(line,
					line.getAccount(ProductCost.ACCTTYPE_P_Cogs, as),
					as.getC_Currency_ID(), costs, null);
				if (dr == null)
				{
					p_Error = "FactLine DR not created: " + line;
					log.log(Level.SEVERE, p_Error);
					return null;
				}
				dr.setM_Locator_ID(line.getM_Locator_ID());
				dr.setLocationFromLocator(line.getM_Locator_ID(), true);    //  from Loc
				dr.setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				dr.setAD_Org_ID(line.getOrder_Org_ID());		//	Revenue X-Org
				dr.setQty(line.getQty().negate());
				
				//  Inventory               CR
				cr = fact.createLine(line,
					line.getAccount(ProductCost.ACCTTYPE_P_Asset, as),
					as.getC_Currency_ID(), null, costs);
				if (cr == null)
				{
					p_Error = "FactLine CR not created: " + line;
					log.log(Level.SEVERE, "createFact - " + p_Error);
					return null;
				}
				cr.setM_Locator_ID(line.getM_Locator_ID());
				cr.setLocationFromLocator(line.getM_Locator_ID(), true);    // from Loc
				cr.setLocationFromBPartner(getC_BPartner_Location_ID(), false);  // to Loc
				//
				if (line.getM_Product_ID() != 0)
				{
					MCostDetail.createShipment(as, line.getAD_Org_ID(), 
						line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
						line.get_ID(), 0,
						costs, line.getQty(),
						line.getDescription(), true, getTrxName());
				}
			}	//	for all lines
			updateProductInfo(as.getC_AcctSchema_ID());     //  only for SO!
		}	//	Shipment
		
		//  *** Purchasing - Receipt
		else if (getDocumentType().equals(DOCTYPE_MatReceipt))
		{
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				BigDecimal costs = line.getProductCosts(as, line.getAD_Org_ID(), false);	//	non-zero costs
				MProduct product = line.getProduct();
				if (costs == null || costs.signum() == 0)
				{
					p_Error = "Resubmit - No Costs for " + product.getName();
					log.log(Level.SEVERE, p_Error);
					return null;
				}
				//  Inventory/Asset			DR
				MAccount assets = line.getAccount(ProductCost.ACCTTYPE_P_Asset, as);
				if (product.isService())
					assets = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
				dr = fact.createLine(line, assets,
					as.getC_Currency_ID(), costs, null);
				if (dr == null)
				{
					p_Error = "DR not created: " + line;
					log.log(Level.SEVERE, p_Error);
					return null;
				}
				dr.setM_Locator_ID(line.getM_Locator_ID());
				dr.setLocationFromBPartner(getC_BPartner_Location_ID(), true);   // from Loc
				dr.setLocationFromLocator(line.getM_Locator_ID(), false);   // to Loc
				//  NotInvoicedReceipt				CR
				cr = fact.createLine(line,
					getAccount(Doc.ACCTTYPE_NotInvoicedReceipts, as),
					as.getC_Currency_ID(), null, costs);
				if (cr == null)
				{
					p_Error = "CR not created: " + line;
					log.log(Level.SEVERE, p_Error);
					return null;
				}
				cr.setM_Locator_ID(line.getM_Locator_ID());
				cr.setLocationFromBPartner(getC_BPartner_Location_ID(), true);   //  from Loc
				cr.setLocationFromLocator(line.getM_Locator_ID(), false);   //  to Loc
				cr.setQty(line.getQty().negate());
			}
		}	//	Receipt
		else
		{
			p_Error = "DocumentType unknown: " + getDocumentType();
			log.log(Level.SEVERE, p_Error);
			return null;
		}
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact


	/**
	 *  Update Sales Order Costing Product Info (old).
	 *  Purchase side handeled in Invoice Matching.
	 *  <br>
	 *  decrease average cumulatives
	 *  @param C_AcctSchema_ID accounting schema
	 *  @deprecated old costing
	 */
	private void updateProductInfo (int C_AcctSchema_ID)
	{
		log.fine("M_InOut_ID=" + get_ID());
		//	Old Model
		StringBuffer sql = new StringBuffer(
			"UPDATE M_Product_Costing pc "
			+ "SET (CostAverageCumQty, CostAverageCumAmt)="
			+ "(SELECT CostAverageCumQty - SUM(il.MovementQty),"
			+ " CostAverageCumAmt - SUM(il.MovementQty*CurrentCostPrice) "
			+ "FROM M_InOutLine il "
			+ "WHERE pc.M_Product_ID=il.M_Product_ID"
			+ " AND il.M_InOut_ID=").append(get_ID()).append(") ")
			.append("WHERE EXISTS (SELECT * "
			+ "FROM M_InOutLine il "
			+ "WHERE pc.M_Product_ID=il.M_Product_ID"
			+ " AND il.M_InOut_ID=").append(get_ID()).append(")");
		int no = DB.executeUpdate(sql.toString(), getTrxName());
		log.fine("M_Product_Costing - Updated=" + no);
		//
	}   //  updateProductInfo

}   //  Doc_InOut
