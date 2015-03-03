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
 *  Post Inventory Documents.
 *  <pre>
 *  Table:              M_Inventory (321)
 *  Document Types:     MMI
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Inventory.java,v 1.18 2005/12/20 04:22:07 jjanke Exp $
 */
public class Doc_Inventory extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@parem trxName trx
	 */
	public Doc_Inventory (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, MInventory.class, rs, DOCTYPE_MatInventory, trxName);
	}   //  Doc_Inventory

	/**
	 *  Load Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails()
	{
		setC_Currency_ID (NO_CURRENCY);
		MInventory inventory = (MInventory)getPO();
		setDateDoc (inventory.getMovementDate());
		setDateAcct(inventory.getMovementDate());
		//	Contained Objects
		p_lines = loadLines(inventory);
		log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 *	Load Invoice Line
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines(MInventory inventory)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MInventoryLine[] lines = inventory.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInventoryLine line = lines[i];
			//	nothing to post
			if (line.getQtyBook().compareTo(line.getQtyCount()) == 0
				&& line.getQtyInternalUse().signum() == 0)
				continue;
			//
			DocLine docLine = new DocLine (line, this); 
			BigDecimal Qty = line.getQtyInternalUse();
			if (Qty.signum() != 0)
				Qty = Qty.negate();		//	Internal Use entered positive
			else
			{
				BigDecimal QtyBook = line.getQtyBook();
				BigDecimal QtyCount = line.getQtyCount();
				Qty = QtyCount.subtract(QtyBook);
			}
			docLine.setQty (Qty, false);		// -5 => -5
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
	 *  MMI.
	 *  <pre>
	 *  Inventory
	 *      Inventory       DR      CR
	 *      InventoryDiff   DR      CR   (or Charge)
	 *  </pre>
	 *  @param as account schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		setC_Currency_ID(as.getC_Currency_ID());

		//  Line pointers
		FactLine dr = null;
		FactLine cr = null;

		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine line = p_lines[i];
			BigDecimal costs = line.getProductCosts(as, line.getAD_Org_ID(), false);
			if (costs == null || costs.signum() == 0)
			{
				p_Error = "No Costs for " + line.getProduct().getName();
				return null;
			}
			//  Inventory       DR      CR
			dr = fact.createLine(line,
				line.getAccount(ProductCost.ACCTTYPE_P_Asset, as),
				as.getC_Currency_ID(), costs);
			//  may be zero difference - no line created.
			if (dr == null)
				continue;
			dr.setM_Locator_ID(line.getM_Locator_ID());
			
			//  InventoryDiff   DR      CR
			//	or Charge
			MAccount invDiff = line.getChargeAccount(as, costs.negate());
			if (invDiff == null)
				invDiff = getAccount(Doc.ACCTTYPE_InvDifferences, as);
			cr = fact.createLine(line, invDiff,
				as.getC_Currency_ID(), costs.negate());
			if (cr == null)
				continue;
			cr.setM_Locator_ID(line.getM_Locator_ID());
			cr.setQty(line.getQty().negate());
			if (line.getC_Charge_ID() != 0)	//	explicit overwrite for charge
				cr.setAD_Org_ID(line.getAD_Org_ID());

			//	Cost Detail
			MCostDetail.createInventory(as, line.getAD_Org_ID(), 
				line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), 
				line.get_ID(), 0, 
				costs, line.getQty(), 
				line.getDescription(), getTrxName());
		}
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

}   //  Doc_Inventory
