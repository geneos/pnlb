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

import org.compiere.model.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Project Issue.
 *	Note:
 *		Will load the default GL Category. 
 *		Set up a document type to set the GL Category. 
 *	
 *  @author Jorg Janke
 *  @version $Id: Doc_ProjectIssue.java,v 1.13 2005/12/20 04:22:07 jjanke Exp $
 */
public class Doc_ProjectIssue extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@parem trxName trx
	 */
	public Doc_ProjectIssue (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, MProjectIssue.class, rs, DOCTYPE_ProjectIssue, trxName);
	}   //  Doc_ProjectIssue

	/**	Is it an Asset Project - default WIP	*/
	private boolean 			m_assetProject = false;
	/**	Pseudo Line								*/
	private DocLine				m_line = null;
	/**	Expense Report							*/
	private int					m_S_TimeExpenseLine_ID = 0;
	/**	Material Receipt						*/
	private int					m_M_InOutLine_ID = 0;


	/**
	 *  Load Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails()
	{
		setC_Currency_ID(NO_CURRENCY);
		MProjectIssue issue = (MProjectIssue)getPO();
		setDateDoc (issue.getMovementDate());
		setDateAcct(issue.getMovementDate());
		m_M_InOutLine_ID = issue.getM_InOutLine_ID();
		m_S_TimeExpenseLine_ID = issue.getS_TimeExpenseLine_ID();
			
		//	Pseudo Line
		m_line = new DocLine (issue, this); 
		m_line.setQty (issue.getMovementQty(), true);    //  sets Trx and Storage Qty
		
		//	Pseudo Line Check
		if (m_line.getM_Product_ID() == 0)
			log.warning(m_line.toString() + " - No Product");
		log.fine(m_line.toString());
		//	Default is WIP project
		loadProjectCategory();
		return null;
	}   //  loadDocumentDetails

	/**
	 * 	Load Project Catefory from Project Type
	 */
	private void loadProjectCategory()
	{
		String sql = "SELECT pj.ProjectCategory FROM C_ProjectType pj"
			+ " INNER JOIN C_Project p ON (p.C_ProjectType_ID=pj.C_ProjectType_ID) "
			+ "WHERE C_Project_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, getC_Project_ID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				String pc = rs.getString(1);
				if (MProjectType.PROJECTCATEGORY_AssetProject.equals(pc))
					m_assetProject = true;				
			}
			else
				log.warning("Not found for C_Project_ID=" + getC_Project_ID());
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
	}	//	loadProjectCategory

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
	 *  PJI
	 *  <pre>
	 *  Issue
	 *      Project         DR
	 *      Inventory               CR
	 *  </pre>
	 *  Project Account is either Asset or WIP depending on Project Type
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

		//  Issue Cost
		BigDecimal cost = null;
		if (m_M_InOutLine_ID != 0)
			cost = getPOCost(as);
		else if (m_S_TimeExpenseLine_ID != 0)
			cost = getLaborCost(as);
		if (cost == null)	//	standard Product Costs
			cost = m_line.getProductCosts(as, getAD_Org_ID(), false);
		//  Project         DR
		dr = fact.createLine(m_line,
		getAccount(m_assetProject ? ACCTTYPE_ProjectAsset : ACCTTYPE_ProjectWIP, as), 
			as.getC_Currency_ID(), cost, null);
		dr.setQty(m_line.getQty().negate());
		
		//  Inventory               CR
		cr = fact.createLine(m_line,
			m_line.getAccount(ProductCost.ACCTTYPE_P_Asset, as),
			as.getC_Currency_ID(), null, cost);
		cr.setM_Locator_ID(m_line.getM_Locator_ID());
		cr.setLocationFromLocator(m_line.getM_Locator_ID(), true);	// from Loc
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

	/**
	 * 	Get PO Costs in Currency of AcctSchema
	 *	@param as Account Schema
	 *	@return Unit PO Cost
	 */
	private BigDecimal getPOCost(MAcctSchema as)
	{
		BigDecimal retValue = null;
		//	Uses PO Date
		String sql = "SELECT currencyConvert(ol.PriceActual, o.C_Currency_ID, ?, o.DateOrdered, o.C_ConversionType_ID, ?, ?) "
			+ "FROM C_OrderLine ol"
			+ " INNER JOIN M_InOutLine iol ON (iol.C_OrderLine_ID=ol.C_OrderLine_ID)"
			+ " INNER JOIN C_Order o ON (o.C_Order_ID=ol.C_Order_ID) "
			+ "WHERE iol.M_InOutLine_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, as.getC_Currency_ID());
			pstmt.setInt(2, getAD_Client_ID());
			pstmt.setInt(3, getAD_Org_ID());
			pstmt.setInt(4, m_M_InOutLine_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = rs.getBigDecimal(1);
				log.fine("POCost = " + retValue);
			}
			else
				log.warning("Not found for M_InOutLine_ID=" + m_M_InOutLine_ID);
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		return retValue;
	}	//	getPOCost();

	/**
	 * 	Get Labor Cost from Expense Report
	 *	@param as Account Schema
	 *	@return Unit Labor Cost
	 */
	private BigDecimal getLaborCost(MAcctSchema as)
	{
		BigDecimal retValue = null;
		/** TODO Labor Cost	*/		
		return retValue;
	}	//	getLaborCost

}	//	DocProjectIssue

