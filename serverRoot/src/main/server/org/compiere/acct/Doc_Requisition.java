/*******************************************************************************
 * The contents of this file are subject to the Compiere License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License. The
 * Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke. All parts are Copyright (C) 1999-2005
 * ComPiere, Inc. All Rights Reserved. Contributor(s):
 * ______________________________________.
 ******************************************************************************/
package org.compiere.acct;

import java.math.*;
import java.util.*;
import java.util.logging.*;
import java.sql.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * Post Order Documents.
 * 
 * <pre>
 *   Table:              M_Requisition
 *   Document Types:     POR (Requisition)
 * </pre>
 * 
 * @author Jorg Janke
 * @version $Id: Doc_Requisition.java,v 1.2 2005/10/26 00:40:02 jjanke Exp $
 */
public class Doc_Requisition extends Doc
{
	/**
	 * Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@parem trxName trx
	 */
	protected Doc_Requisition (MAcctSchema[]  ass, ResultSet rs, String trxName)
	{
		super (ass, MRequisition.class, rs, DOCTYPE_PurchaseRequisition, trxName);
	}	//	Doc_Requisition

	/**
	 *	Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		setC_Currency_ID(NO_CURRENCY);
		MRequisition req = (MRequisition)getPO();
		setDateDoc (req.getDateDoc());
		setDateAcct (req.getDateDoc());
		// Amounts
		setAmount(AMTTYPE_Gross, req.getTotalLines());
		setAmount(AMTTYPE_Net, req.getTotalLines());
		// Contained Objects
		p_lines = loadLines (req);
		// log.fine( "Lines=" + p_lines.length + ", Taxes=" + m_taxes.length);
		return null;
	}	// loadDocumentDetails

	/**
	 * Load Invoice Line
	 * @return DocLine Array
	 */
	private DocLine[] loadLines (MRequisition req)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine> ();
		MRequisitionLine[] lines = req.getLines();
		for (int i = 0; i < lines.length; i++)
		{
			MRequisitionLine line = lines[i];
			DocLine docLine = new DocLine (line, this);
			BigDecimal Qty = line.getQty();
			docLine.setQty (Qty, false);
			BigDecimal PriceActual = line.getPriceActual();
			BigDecimal LineNetAmt = line.getLineNetAmt();
			docLine.setAmount (LineNetAmt);	 // DR
			list.add (docLine);
		}
		// Return Array
		DocLine[] dls = new DocLine[list.size ()];
		list.toArray (dls);
		return dls;
	}	// loadLines

	/***************************************************************************
	 * Get Source Currency Balance - subtracts line and tax amounts from total -
	 * no rounding
	 * 
	 * @return positive amount, if total invoice is bigger than lines
	 */
	public BigDecimal getBalance ()
	{
		BigDecimal retValue = new BigDecimal (0.0);
		return retValue;
	}	// getBalance

	/***************************************************************************
	 * Create Facts (the accounting logic) for POR.
	 * <pre>
	 * Reservation
	 * 	Expense		CR
	 * 	Offset			DR
	 * </pre>
	 * @param as accounting schema
	 * @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		ArrayList<Fact> facts = new ArrayList<Fact>();
		Fact fact = new Fact (this, as, Fact.POST_Reservation);
		setC_Currency_ID(as.getC_Currency_ID());
		//
		BigDecimal grossAmt = getAmount (Doc.AMTTYPE_Gross);
		// Commitment
		if (as.isCreateReservation ())
		{
			BigDecimal total = Env.ZERO;
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				BigDecimal cost = line.getAmtSource();
				total = total.add (cost);
				// Account
				MAccount expense = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
				//
				fact.createLine (line, expense, as.getC_Currency_ID(), cost, null);
			}
			// Offset
			MAccount offset = getAccount (ACCTTYPE_CommitmentOffset, as);
			if (offset == null)
			{
				p_Error = "@NotFound@ @CommitmentOffset_Acct@";
				log.log (Level.SEVERE, p_Error);
				return null;
			}
			fact.createLine (null, offset, getC_Currency_ID(), null, total);
			facts.add(fact);
		}
		
		return facts;
	} // createFact
} // Doc_Requisition
