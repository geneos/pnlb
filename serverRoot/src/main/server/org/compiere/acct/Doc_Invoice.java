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
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              C_Invoice (318)
 *  Document Types:     ARI, ARC, ARF, API, APC
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Invoice.java,v 1.27 2005/12/27 06:20:45 jjanke Exp $
 */
public class Doc_Invoice extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@parem trxName trx
	 */
	protected Doc_Invoice(MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, MInvoice.class, rs, null, trxName);
	}	//	Doc_Invoice

	/** Contained Optional Tax Lines    */
	private DocTax[]        m_taxes = null;
	
	/** Perceptions Invoice    */
	private BigDecimal 		m_AmountPercepIB = BigDecimal.ZERO;
	private BigDecimal 		m_AmountPercepIVA = BigDecimal.ZERO;
    
    /**
     * Considerate other perceptions, i.e. 'Ganancias'
     * @author Ezequiel Scott @ Zynnia
     */
    private BigDecimal      m_AmountPercepOthers = BigDecimal.ZERO;
	
	/** Rate Invoice    */
	private BigDecimal      m_rate = BigDecimal.ONE;
	
	/** Contained Optional Percepcion Lines    */
	private DocPercep[]  m_percep = null;
	
	/** Contained Optional Percepcion Realizada IIBB Lines    */
	private DocPercepRealizadasIB[]  m_percepIB = null;
		
	/** Currency Precision				*/
	private int				m_precision = -1;
	/** All lines are Service			*/
	private boolean			m_allLinesService = true;
	/** All lines are product item		*/
	private boolean			m_allLinesItem = true;
        
        public static int logLevel;

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		MInvoice invoice = (MInvoice)getPO();
		m_AmountPercepIB = invoice.getPercepcionIB();
		m_AmountPercepIVA = invoice.getPercepcionIVA();
		setDateDoc(invoice.getDateInvoiced());
		setIsTaxIncluded(invoice.isTaxIncluded());
		//	Amounts
		setAmount(Doc.AMTTYPE_Gross, invoice.getGrandTotal());
		setAmount(Doc.AMTTYPE_Net, invoice.getTotalLines());
		setAmount(Doc.AMTTYPE_Charge, invoice.getChargeAmt());
				
		//	Contained Objects
		//calculateRate(invoice);
		m_rate = invoice.getCotizacion();
		m_taxes = loadTaxes();
		p_lines = loadLines(invoice);
		m_percep = loadPercepcionRecibida();
		m_percepIB = loadPercepcionRealizada();
		
		log.fine("Lines=" + p_lines.length + ", Taxes=" + m_taxes.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 * 	CREADO REQ-068
	 * 	
	 * @author Daniel
	 * @return rate
	 *  	
	 */
/*	private void calculateRate(MInvoice inv) {
		//TODO GETCOTIZACION de la clase.
		
		try
		{
			String sql = "Select COTIZACION from C_Invoice Where C_Invoice_ID = " + inv.getC_Invoice_ID();
			PreparedStatement pstm = DB.prepareStatement(sql, getTrxName());
			ResultSet rs = pstm.executeQuery();
			if (rs.next() && rs.getBigDecimal(1)!=null)		
				m_rate = rs.getBigDecimal(1);
		}
		catch (Exception e)
		{	e.printStackTrace();	};
	}
	*/
	
	/**
	 *	Agregado por DANIEL GINI @BISion It Solutions
	 *
	 *  Load Invoice Percepciones (Toma solo los activos)
	 *  @return DocPercep Array
	 */
	private DocPercep[] loadPercepcionRecibida()
	{
		ArrayList<DocPercep> list = new ArrayList<DocPercep>();
		
		String sql = "SELECT C_InvoicePercep_ID, C_RegPercep_Recib_ID, Amount "
			+ "FROM C_InvoicePercep "
			+ "WHERE C_Invoice_ID=? AND IsActive = 'Y'";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, getTrxName());
			pstmt.setInt(1, get_ID());
			ResultSet rs = pstmt.executeQuery();
			//
			while (rs.next())
			{
				int C_InvoicePercep_ID = rs.getInt(1);
				int C_RegPercep_Recib_ID = rs.getInt(2);
				BigDecimal amount = rs.getBigDecimal(3);
				//
				DocPercep percepLine = new DocPercep(C_InvoicePercep_ID, C_RegPercep_Recib_ID, amount);
				log.fine(percepLine.toString());
				list.add(percepLine);
			}
			//
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return null;
		}

		//	Return Array
		DocPercep[] tl = new DocPercep[list.size()];
		list.toArray(tl);
		return tl;
	}	//	loadPercepcionRecibida
	
	/**
	 *	Agregado por DANIEL GINI @BISion It Solutions
	 *
	 *  Load Invoice Percepciones Realizadas de IIBB (Toma solo los activos)
	 *  @return DocPercepRealizadasIB Array
	 */
	private DocPercepRealizadasIB[] loadPercepcionRealizada()
	{
		ArrayList<DocPercepRealizadasIB> list = new ArrayList<DocPercepRealizadasIB>();
		
		String sql =
			" SELECT C_Jurisdiccion_ID, DESCRIPCION, SUM(Monto) " +
			" FROM C_InvoicePercep_SOTrx " +
			" WHERE C_Invoice_ID=? AND IsActive = 'Y' AND C_Jurisdiccion_ID>0" +
			" GROUP BY C_Jurisdiccion_ID, DESCRIPCION";
			
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, getTrxName());
			pstmt.setInt(1, get_ID());
			ResultSet rs = pstmt.executeQuery();
			//
			int InvoicePercep_SOTrx = 0;
			while (rs.next())
			{
				int C_Jurisdiccion_ID = rs.getInt(1);
				String descripcion = rs.getString(2);
				BigDecimal amount = rs.getBigDecimal(3);
				
				DocPercepRealizadasIB percepLine = new DocPercepRealizadasIB(InvoicePercep_SOTrx, C_Jurisdiccion_ID, amount, descripcion);
				InvoicePercep_SOTrx++;
				log.fine(percepLine.toString());
				list.add(percepLine);
			}
			//
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return null;
		}

		//	Return Array
		DocPercepRealizadasIB[] tl = new DocPercepRealizadasIB[list.size()];
		list.toArray(tl);
		return tl;
	}	//	loadPercepcionRealizada
	
	/**
	 *	Load Invoice Taxes
	 *  @return DocTax Array
	 */
	private DocTax[] loadTaxes()
	{
		ArrayList<DocTax> list = new ArrayList<DocTax>();
		String sql = "SELECT it.C_Tax_ID, t.Name, t.Rate, it.TaxBaseAmt, it.TaxAmt, t.IsSalesTax "
			+ "FROM C_Tax t, C_InvoiceTax it "
			+ "WHERE t.C_Tax_ID=it.C_Tax_ID AND it.C_Invoice_ID=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, getTrxName());
			pstmt.setInt(1, get_ID());
			ResultSet rs = pstmt.executeQuery();
			//
			while (rs.next())
			{
				int C_Tax_ID = rs.getInt(1);
				String name = rs.getString(2);
				BigDecimal rate = rs.getBigDecimal(3);
				BigDecimal taxBaseAmt = rs.getBigDecimal(4);
				BigDecimal amount = rs.getBigDecimal(5);
				boolean salesTax = "Y".equals(rs.getString(6));
				//
				DocTax taxLine = new DocTax(C_Tax_ID, name, rate, 
					taxBaseAmt, amount, salesTax);
				log.fine(taxLine.toString());
				list.add(taxLine);
			}
			//
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return null;
		}

		//	Return Array
		DocTax[] tl = new DocTax[list.size()];
		list.toArray(tl);
		return tl;
	}	//	loadTaxes

	/**
	 *	Load Invoice Line
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines(MInvoice invoice)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		//
		MInvoiceLine[] lines = invoice.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];
			if (line.isDescription())
				continue;
			DocLine docLine = new DocLine(line, this);
			//	Qty
			BigDecimal Qty = line.getQtyInvoiced();
			boolean cm = getDocumentType().equals(DOCTYPE_ARCredit) 
				|| getDocumentType().equals(DOCTYPE_APCredit);
			docLine.setQty(cm ? Qty.negate() : Qty, invoice.isSOTrx());
			//
			BigDecimal LineNetAmt = line.getLineNetAmt();
			BigDecimal PriceList = line.getPriceList();
			int C_Tax_ID = docLine.getC_Tax_ID();
			//	Correct included Tax
			if (isTaxIncluded() && C_Tax_ID != 0)
			{
				MTax tax =MTax.get(getCtx(), C_Tax_ID, getTrxName());
				if (!tax.isZeroTax())
				{
					BigDecimal LineNetAmtTax = tax.calculateTax(LineNetAmt, true, getStdPercision());
					log.fine("LineNetAmt=" + LineNetAmt + " - Tax=" + LineNetAmtTax);
					LineNetAmt = LineNetAmt.subtract(LineNetAmtTax);
					for (int t = 0; t < m_taxes.length; t++)
					{
						if (m_taxes[t].getC_Tax_ID() == C_Tax_ID)
						{
							m_taxes[t].addIncludedTax(LineNetAmtTax);
							break;
						}
					}
					BigDecimal PriceListTax = tax.calculateTax(PriceList, true, getStdPercision());
					PriceList = PriceList.subtract(PriceListTax);
				}
			}	//	correct included Tax
			
			docLine.setAmount (LineNetAmt, PriceList, Qty);	//	qty for discount calc
			if (docLine.isItem())
				m_allLinesService = false;
			else
				m_allLinesItem = false;
			//
			log.fine(docLine.toString());
			list.add(docLine);
		}
		
		//	Convert to Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);

		//	Included Tax - make sure that no difference
		if (isTaxIncluded())
		{
			for (int i = 0; i < m_taxes.length; i++)
			{
				if (m_taxes[i].isIncludedTaxDifference())
				{
					BigDecimal diff = m_taxes[i].getIncludedTaxDifference(); 
					for (int j = 0; j < dls.length; j++)
					{
						if (dls[j].getC_Tax_ID() == m_taxes[i].getC_Tax_ID())
						{
							dls[j].setLineNetAmtDifference(diff);
							break;
						}
					}	//	for all lines
				}	//	tax difference
			}	//	for all taxes
		}	//	Included Tax difference
		
		//	Return Array
		return dls;
	}	//	loadLines

	/**
	 * 	Get Currency Percision
	 *	@return precision
	 */
	private int getStdPercision()
	{
		if (m_precision == -1)
			m_precision = MCurrency.getStdPrecision(getCtx(), getC_Currency_ID());
		return m_precision;
	}	//	getPrecision

	
	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line and tax amounts from total - no rounding
	 *  @return positive amount, if total invoice is bigger than lines
	 */
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;
		StringBuffer sb = new StringBuffer (" [");
		//  Total
		retValue = retValue.add(getAmount(Doc.AMTTYPE_Gross));
		sb.append(getAmount(Doc.AMTTYPE_Gross));
		//  - Header Charge
		retValue = retValue.subtract(getAmount(Doc.AMTTYPE_Charge));
		sb.append("-").append(getAmount(Doc.AMTTYPE_Charge));
		//  - Tax
		for (int i = 0; i < m_taxes.length; i++)
		{
			retValue = retValue.subtract(m_taxes[i].getAmount());
			sb.append("-").append(m_taxes[i].getAmount());
		}
		//  - Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			retValue = retValue.subtract(p_lines[i].getAmtSource());
			sb.append("-").append(p_lines[i].getAmtSource());
		}
		// - Percepciones
		
		retValue = retValue.subtract(m_AmountPercepIB);
		sb.append("-").append(m_AmountPercepIB);
		retValue = retValue.subtract(m_AmountPercepIVA);
		sb.append("-").append(m_AmountPercepIVA);
		
        for (int i = 0; i < m_percep.length; i++)
        {
            m_AmountPercepOthers = m_AmountPercepOthers.add(m_percep[i].getAmount());
        }
        // PercepOthers = PercepTotal - PercepIB - PercepIVA
        m_AmountPercepOthers = m_AmountPercepOthers.subtract(m_AmountPercepIB);
        m_AmountPercepOthers = m_AmountPercepOthers.subtract(m_AmountPercepIVA);
        
        retValue = retValue.subtract(m_AmountPercepOthers);
        sb.append("-").append(m_AmountPercepOthers);

		sb.append("]");
		//
		log.fine(toString() + " Balance=" + retValue + sb.toString());
		return retValue;
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  ARI, ARC, ARF, API, APC.
	 *  <pre>
	 *  ARI, ARF
	 *      Receivables     DR
	 *      Charge                  CR
	 *      TaxDue                  CR
	 *      Revenue                 CR
	 *
	 *  ARC
	 *      Receivables             CR
	 *      Charge          DR
	 *      TaxDue          DR
	 *      Revenue         RR
	 *
	 *  API
	 *      Payables                CR
	 *      Charge          DR
	 *      TaxCredit       DR
	 *      Expense         DR
	 *
	 *  APC
	 *      Payables        DR
	 *      Charge                  CR
	 *      TaxCredit               CR
	 *      Expense                 CR
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
        System.out.println("Create Facts for DocumentNo " + getDocumentNo() + " in Account Schema "+ as);
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);

		//  Cash based accounting
		if (!as.isAccrual())
			return facts;

		//  ** ARI, ARF y RRC(!ARC)
		if (getDocumentType().equals(DOCTYPE_ARInvoice) 
			|| getDocumentType().equals(DOCTYPE_ARProForma)
			|| getDocumentType().equals(DOCTYPE_ARCredit_Rev))
		{  
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			BigDecimal amt = null;
				
			//	Receivables     	DR
			for (int i = 0; i < p_lines.length; i++)
			{
				amt = p_lines[i].getAmtSource();
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
						amt = amt.add(discount);
				}
				if (!p_lines[i].isItem())
				{
					grossAmt = grossAmt.subtract(amt);
					serviceAmt = serviceAmt.add(amt);
				}
			}
			
			int receivables_ID = getValidCombination_ID(Doc.ACCTTYPE_C_Receivable, as);
			int receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_C_Receivable_Services, as);
			int mExterno_ID = getValidCombination_ID(Doc.ACCTTYPE_C_MExterno, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| receivables_ID == receivablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			//	Si es Revertida, se invierten los montos,
			if (getDocumentType().equals(DOCTYPE_ARCredit_Rev))
			{
				grossAmt = grossAmt.negate();
				serviceAmt = serviceAmt.negate();
			}
			
			if (getC_Currency_ID()!=as.getC_Currency_ID())
			{	
				if (grossAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), mExterno_ID),
							m_rate, getC_Currency_ID(), grossAmt, null);
				if (serviceAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), mExterno_ID),
							m_rate, getC_Currency_ID(), serviceAmt, null);
			}
			else
			{	
				if (grossAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
							m_rate, getC_Currency_ID(), grossAmt, null);
				if (serviceAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
							m_rate, getC_Currency_ID(), serviceAmt, null);
			}
			
			//  Revenue	- Discount		DR
			for (int i = 0; i < p_lines.length; i++)
			{
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
					{	if (getDocumentType().equals(DOCTYPE_ARCredit_Rev))
							discount = discount.negate();
						fact.createLine (p_lines[i],
							p_lines[i].getAccount(ProductCost.ACCTTYPE_P_TDiscountGrant, as),
							m_rate, getC_Currency_ID(), discount, null);
					}
				}
			}
			
			//  Header Charge           CR
			amt = getAmount(Doc.AMTTYPE_Charge);
			if (amt != null && amt.signum() != 0)
			{	if (getDocumentType().equals(DOCTYPE_ARCredit_Rev))
					amt = amt.negate();
				fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
						m_rate, getC_Currency_ID(), null, amt);
			}
			
			//  TaxDue                  CR
			for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					if (getDocumentType().equals(DOCTYPE_ARCredit_Rev))
						amt = amt.negate();
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
							m_rate, getC_Currency_ID(), null, amt);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}
			
			//  Revenue                 CR
			for (int i = 0; i < p_lines.length; i++)
			{
				amt = p_lines[i].getAmtSource();
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
						amt = amt.add(discount);
				}
				if (getDocumentType().equals(DOCTYPE_ARCredit_Rev))
					amt = amt.negate();
				fact.createLine (p_lines[i],
					p_lines[i].getAccount(ProductCost.ACCTTYPE_P_Revenue, as),
					m_rate, getC_Currency_ID(), null, amt);
			}
			
			/**
			 *	Agregado por DANIEL GINI @BISion It Solutions
			 *
			 *			Contabilidad a Percepciones Realizadas.
			 *
			 *  INICIO
			 */
			
			//	  PercepcionCredit       CR
			MInvoice mInv = (MInvoice)getPO();

			if (getDocumentType().equals(DOCTYPE_ARCredit_Rev))
				fact.createLine(null, getAccount(Doc.ACCTTYPE_PercepcionVentaIVA, as),
						m_rate, getC_Currency_ID(), mInv.getPercepcionIVA());
			else
				fact.createLine(null, getAccount(Doc.ACCTTYPE_PercepcionVentaIVA, as),
						m_rate, getC_Currency_ID(), mInv.getPercepcionIVA().negate());
			
			/*
			MLocation loc = new MLocation(Env.getCtx(),mInv.getC_Location_ID(),getTrxName());
			MAccount acct = getAccountVPercepIB(loc.getC_Jurisdiccion_ID(), as);
			
			if (acct==null)
				acct = getAccount(Doc.ACCTTYPE_PercepcionVentaIB, as); 
			
			if (getDocumentType().equals(DOCTYPE_ARCredit_Rev))
				fact.createLine(null, acct, m_rate, getC_Currency_ID(), mInv.getPercepcionIB());
			else
				fact.createLine(null, acct, m_rate, getC_Currency_ID(), mInv.getPercepcionIB().negate());
			*/
			
			/**
			 *	Agregado por DANIEL GINI @BISion It Solutions
			 *
			 *	REQ-071: Contabilidad a Percepciones Recibidas
			 *			 para IIBB con Convenio Multilateral.
			 *
			 *  INICIO
			 */
			
			//	  PercepcionCredit       CR
			for (int i = 0; i < m_percepIB.length; i++)
			{
				if (getDocumentType().equals(DOCTYPE_ARCredit_Rev))
					fact.createLine(null, m_percepIB[i].getAccount(m_percepIB[i].getAPPercepcionType(), as),
							m_rate, getC_Currency_ID(), m_percepIB[i].getAmount());
				else
					fact.createLine(null, m_percepIB[i].getAccount(m_percepIB[i].getAPPercepcionType(), as),
							m_rate, getC_Currency_ID(), m_percepIB[i].getAmount().negate());
			}
			
			/**
			 *	FIN
			 */
			
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);      //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				}
			}
		}
		//  ARC y RRI(!ARI)
		else if (getDocumentType().equals(DOCTYPE_ARCredit)
				 || getDocumentType().equals(DOCTYPE_ARInvoice_Rev))
		{          
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			BigDecimal amt = getAmount(Doc.AMTTYPE_Charge);
			
			//  Header Charge   DR
			if (getDocumentType().equals(DOCTYPE_ARInvoice_Rev))
				amt = amt.negate();
			if (amt != null && amt.signum() != 0)
				fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
						m_rate, getC_Currency_ID(), amt, null);
			
			//  TaxDue          DR
			for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					if (getDocumentType().equals(DOCTYPE_ARInvoice_Rev))
						amt = amt.negate();
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxCredit, as),
							m_rate, getC_Currency_ID(), amt, null);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}

			//  Revenue         DR
			for (int i = 0; i < p_lines.length; i++)
			{
				amt = p_lines[i].getAmtSource();
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
						amt = amt.add(discount);
				}
				if (getDocumentType().equals(DOCTYPE_ARInvoice_Rev))
					amt = amt.negate();
				fact.createLine (p_lines[i],
					p_lines[i].getAccount (ProductCost.ACCTTYPE_P_Revenue, as),
					m_rate, getC_Currency_ID(), amt, null);
				if (!p_lines[i].isItem())
				{
					grossAmt = grossAmt.subtract(amt);
					serviceAmt = serviceAmt.add(amt);
				}
			}
			
			/**
			 *	Agregado por DANIEL GINI @BISion It Solutions
			 *
			 *			Contabilidad a Percepciones Realizadas.
			 *
			 *  INICIO
			 */
			
			//	  PercepcionCredit       DR
			MInvoice mInv = (MInvoice)getPO();
			
			if (getDocumentType().equals(DOCTYPE_ARInvoice_Rev))
				fact.createLine(null, getAccount(Doc.ACCTTYPE_PercepcionVentaIVA, as),
						 m_rate, getC_Currency_ID(), mInv.getPercepcionIVA().negate());
			else
				fact.createLine(null, getAccount(Doc.ACCTTYPE_PercepcionVentaIVA, as),
						 m_rate, getC_Currency_ID(), mInv.getPercepcionIVA());
			
			/*
			MAccount acct = getAccountVPercepIB(mInv.getC_JURISDICCION_ID(), as);
			
			if (acct==null)
				acct = getAccount(Doc.ACCTTYPE_PercepcionVentaIB, as); 
			
			if (getDocumentType().equals(DOCTYPE_ARInvoice_Rev))
				fact.createLine(null, acct, m_rate, getC_Currency_ID(), mInv.getPercepcionIB().negate());
			else
				fact.createLine(null, acct, m_rate, getC_Currency_ID(), mInv.getPercepcionIB());
			*/
			
			/**
			 *	Agregado por DANIEL GINI @BISion It Solutions
			 *
			 *	REQ-071: Contabilidad a Percepciones Recibidas
			 *			 para IIBB con Convenio Multilateral.
			 *
			 *  INICIO
			 */
			
			//	  PercepcionCredit       CR
			for (int i = 0; i < m_percepIB.length; i++)
			{
				if (getDocumentType().equals(DOCTYPE_ARInvoice_Rev))
					fact.createLine(null, m_percepIB[i].getAccount(m_percepIB[i].getAPPercepcionType(), as),
							m_rate, getC_Currency_ID(), m_percepIB[i].getAmount().negate());
				else
					fact.createLine(null, m_percepIB[i].getAccount(m_percepIB[i].getAPPercepcionType(), as),
							m_rate, getC_Currency_ID(), m_percepIB[i].getAmount());
			}
			
			/**
			 *	FIN
			 */
			
			//	Revenue	- Discount		CR
			for (int i = 0; i < p_lines.length; i++)
			{
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
					{	if (getDocumentType().equals(DOCTYPE_ARInvoice_Rev))
							discount = discount.negate();
						fact.createLine (p_lines[i],
							p_lines[i].getAccount(ProductCost.ACCTTYPE_P_TDiscountGrant, as),
							m_rate, getC_Currency_ID(), null, discount);
					}
				}
			}
			
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);      //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				}
			}
			//  Receivables             CR
			int receivables_ID = getValidCombination_ID (Doc.ACCTTYPE_C_Receivable, as);
			int receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_C_Receivable_Services, as);
			int mExterno_ID = getValidCombination_ID(Doc.ACCTTYPE_C_MExterno, as);
			
			if (m_allLinesItem || !as.isPostServices() 
				|| receivables_ID == receivablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (getDocumentType().equals(DOCTYPE_ARInvoice_Rev))
			{
				grossAmt = grossAmt.negate();
				serviceAmt = serviceAmt.negate();
			}
			

			if (getC_Currency_ID()!=as.getC_Currency_ID())
			{	
				if (grossAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), mExterno_ID),
							m_rate, getC_Currency_ID(), null, grossAmt);
				if (serviceAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), mExterno_ID),
							m_rate, getC_Currency_ID(), null, serviceAmt);
			}
			else
			{	
				if (grossAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
							m_rate, getC_Currency_ID(), null, grossAmt);
				if (serviceAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
							m_rate, getC_Currency_ID(), null, serviceAmt);
			}
			
		}
		//  ** API y RPC(!APC)
		else if (getDocumentType().equals(DOCTYPE_APInvoice)
				 || getDocumentType().equals(DOCTYPE_APCredit_Rev))
		{   
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;

			//  Charge          DR
			if (getDocumentType().equals(DOCTYPE_APCredit_Rev))
				fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
						m_rate, getC_Currency_ID(), getAmount(Doc.AMTTYPE_Charge).negate(), null);
			else
				fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
						m_rate, getC_Currency_ID(), getAmount(Doc.AMTTYPE_Charge), null);
			//  TaxCredit       DR
			for (int i = 0; i < m_taxes.length; i++)
			{
				FactLine tl;
				if (getDocumentType().equals(DOCTYPE_APCredit_Rev))
					tl = fact.createLine(null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
							m_rate, getC_Currency_ID(), m_taxes[i].getAmount().negate(), null);
				else
					tl = fact.createLine(null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
							m_rate, getC_Currency_ID(), m_taxes[i].getAmount(), null);
				if (tl != null)
					tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
			}
			//  Expense         DR
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				boolean landedCost = landedCost(as, fact, line, true);
				if (landedCost && as.isExplicitCostAdjustment())
				{
					FactLine fl;
					if (getDocumentType().equals(DOCTYPE_APCredit_Rev))
					{	fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
							m_rate, getC_Currency_ID(), line.getAmtSource().negate(), null);
						fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
							m_rate, getC_Currency_ID(), null, line.getAmtSource().negate());
					}
					else
					{	fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
							m_rate, getC_Currency_ID(), line.getAmtSource(), null);
						fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
							m_rate, getC_Currency_ID(), null, line.getAmtSource());
					}
					String desc = line.getDescription();
					if (desc == null)
						desc = "100%";
					else
						desc += " 100%";
					fl.setDescription(desc);
				}
				if (!landedCost)
				{
					MAccount expense = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
					if (line.isItem())
						expense = line.getAccount (ProductCost.ACCTTYPE_P_Asset, as);
					BigDecimal amt = line.getAmtSource();
					BigDecimal dAmt = null;
					if (as.isTradeDiscountPosted() && !line.isItem())
					{
						BigDecimal discount = line.getDiscount();
						if (discount != null && discount.signum() != 0)
						{
							amt = amt.add(discount);
							dAmt = discount;
						}
					}
					if (getDocumentType().equals(DOCTYPE_APCredit_Rev))
					{
						amt = amt.negate();
						if (dAmt != null)
							dAmt = dAmt.negate();
					}
					fact.createLine (line, expense,
							m_rate, getC_Currency_ID(), amt, dAmt);
					if (!line.isItem())
					{
						grossAmt = grossAmt.subtract(amt);
						serviceAmt = serviceAmt.add(amt);
					}
					
                    if (line.getM_Product_ID() != 0 && line.getProduct().isService())	//	otherwise Inv Matching
                    	MCostDetail.createInvoice(as, line.getAD_Org_ID(), 
					
                    line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
                    line.get_ID(), 0,		//	No Cost Element
                    line.getAmtSource(), line.getQty(),
                    line.getDescription(), getTrxName());
				}
			}
			
			
			/**
			 *	Agregado por DANIEL GINI @BISion It Solutions
			 *
			 *			Contabilidad a Percepciones Recibidas.
			 *
			 *  INICIO
			 */
			
			//	  PercepcionCredit       DR
			for (int i = 0; i < m_percep.length; i++)
			{
				if (getDocumentType().equals(DOCTYPE_APCredit_Rev))
					fact.createLine(null, m_percep[i].getAccount(m_percep[i].getAPPercepcionType(), as),
							m_rate, getC_Currency_ID(), m_percep[i].getAmount().negate(), null);
				else
					fact.createLine(null, m_percep[i].getAccount(m_percep[i].getAPPercepcionType(), as),
							m_rate, getC_Currency_ID(), m_percep[i].getAmount(), null);

			}
			
			/**
			 *	FIN
			 */
			//percepciones
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
			}

			//  Liability               CR
			int payables_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability, as);
			int payablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability_Services, as);
			int mExterno_ID = getValidCombination_ID(Doc.ACCTTYPE_V_MExterno, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| payables_ID == payablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (getDocumentType().equals(DOCTYPE_APCredit_Rev))
			{
				grossAmt = grossAmt.negate();
				serviceAmt = serviceAmt.negate();
			}
			
			if (getC_Currency_ID()!=as.getC_Currency_ID())
			{	
				if (grossAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), mExterno_ID),
							m_rate, getC_Currency_ID(), null, grossAmt);
				if (serviceAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), mExterno_ID),
							m_rate, getC_Currency_ID(), null, serviceAmt);
			}
			else
			{	
				if (grossAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), payables_ID),
							m_rate, getC_Currency_ID(), null, grossAmt);
				if (serviceAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), payablesServices_ID),
							m_rate, getC_Currency_ID(), null, serviceAmt);
			}
			
			//
			updateProductPO(as);	//	Only API
			updateProductInfo (as.getC_AcctSchema_ID());    //  only API
		}
		//  APC y RPI (!API)
		else if (getDocumentType().equals(DOCTYPE_APCredit)
				 || getDocumentType().equals(DOCTYPE_APInvoice_Rev))
		{   
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			//  Charge                  CR
			if (getDocumentType().equals(DOCTYPE_APInvoice_Rev))
				fact.createLine (null, getAccount(Doc.ACCTTYPE_Charge, as),
						m_rate, getC_Currency_ID(), null, getAmount(Doc.AMTTYPE_Charge).negate());
			else
				fact.createLine (null, getAccount(Doc.ACCTTYPE_Charge, as),
						m_rate, getC_Currency_ID(), null, getAmount(Doc.AMTTYPE_Charge));

			//  TaxCredit               CR
			for (int i = 0; i < m_taxes.length; i++)
			{
				FactLine tl;
				if (getDocumentType().equals(DOCTYPE_APInvoice_Rev))
					tl = fact.createLine (null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
							m_rate, getC_Currency_ID(), null, m_taxes[i].getAmount().negate());
				else
					tl = fact.createLine (null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
							m_rate, getC_Currency_ID(), null, m_taxes[i].getAmount());
				if (tl != null)
					tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
			}
			//  Expense                 CR
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				boolean landedCost = landedCost(as, fact, line, false);
                                System.out.println("***** landed cost " +landedCost);
				if (landedCost && as.isExplicitCostAdjustment())
				{
					FactLine fl;
					if (getDocumentType().equals(DOCTYPE_APInvoice_Rev))
					{	fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
							m_rate, getC_Currency_ID(), null, line.getAmtSource().negate());
						fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
							m_rate, getC_Currency_ID(), line.getAmtSource().negate(), null);
					}
					else
					{	fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
							m_rate, getC_Currency_ID(), null, line.getAmtSource());
						fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
							m_rate, getC_Currency_ID(), line.getAmtSource(), null);
					}
					String desc = line.getDescription();
					if (desc == null)
						desc = "100%";
					else
						desc += " 100%";
					fl.setDescription(desc);
				}
				if (!landedCost)
				{
					MAccount expense = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
					if (line.isItem())
						expense = line.getAccount (ProductCost.ACCTTYPE_P_Asset, as);
					BigDecimal amt = line.getAmtSource();
					BigDecimal dAmt = null;
					if (as.isTradeDiscountPosted() && !line.isItem())
					{
						BigDecimal discount = line.getDiscount();
						if (discount != null && discount.signum() != 0)
						{
							amt = amt.add(discount);
							dAmt = discount;
						}
					}
					if (getDocumentType().equals(DOCTYPE_APInvoice_Rev))
					{
						amt = amt.negate();
						if (dAmt != null)
							dAmt = dAmt.negate();
					}
					fact.createLine (line, expense,
						m_rate, getC_Currency_ID(), dAmt, amt);
					if (!line.isItem())
					{
						grossAmt = grossAmt.subtract(amt);
						serviceAmt = serviceAmt.add(amt);
					}
					//
					if (line.getM_Product_ID() != 0
						&& line.getProduct().isService())	//	otherwise Inv Matching
						MCostDetail.createInvoice(as, line.getAD_Org_ID(), 
							line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
							line.get_ID(), 0,		//	No Cost Element
							line.getAmtSource().negate(), line.getQty(),
							line.getDescription(), getTrxName());
				}
			}
			
			
			
			/**
			 *	Agregado por DANIEL GINI @BISion It Solutions
			 *
			 *			Contabilidad a Percepciones Recibidas.
			 *
			 *  INICIO
			 */
			
			//	  PercepcionCredit       DR
			for (int i = 0; i < m_percep.length; i++)
			{
				if (getDocumentType().equals(DOCTYPE_APInvoice_Rev))
					fact.createLine(null, m_percep[i].getAccount(m_percep[i].getAPPercepcionType(), as),
						m_rate, getC_Currency_ID(), null, m_percep[i].getAmount().negate());
				else
					fact.createLine(null, m_percep[i].getAccount(m_percep[i].getAPPercepcionType(), as),
						m_rate, getC_Currency_ID(), null, m_percep[i].getAmount());
			}
			
			/**
			 *	FIN
			 */
			
			
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
			}
			//  Liability       DR
			int payables_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability, as);
			int payablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability_Services, as);
			int mExterno_ID = getValidCombination_ID (Doc.ACCTTYPE_V_MExterno, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| payables_ID == payablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (getDocumentType().equals(DOCTYPE_APInvoice_Rev))
			{
				serviceAmt = serviceAmt.negate();
				grossAmt = grossAmt.negate();
			}
			
			
			if (getC_Currency_ID()!=as.getC_Currency_ID())
			{	
				if (grossAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), mExterno_ID),
							m_rate, getC_Currency_ID(), grossAmt, null);
				if (serviceAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), mExterno_ID),
							m_rate, getC_Currency_ID(), serviceAmt, null);
			}
			else
			{	
				if (grossAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), payables_ID),
							m_rate, getC_Currency_ID(), grossAmt, null);
				if (serviceAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), payablesServices_ID),
							m_rate, getC_Currency_ID(), serviceAmt, null);
			}
			
		}
		else
		{
			p_Error = "DocumentType unknown: " + getDocumentType();
			log.log(Level.SEVERE, p_Error);
			fact = null;
		}
		//
		facts.add(fact);
		return facts;
	}   //  createFact
	
	/**
	 * 	Create Fact Cash Based (i.e. only revenue/expense)
	 *	@param as accounting schema
	 *	@param fact fact to add lines to
	 *	@param multiplier source amount multiplier
	 *	@return accounted amount
	 */
	public BigDecimal createFactCash (MAcctSchema as, Fact fact, BigDecimal multiplier)
	{
		boolean creditMemo = getDocumentType().equals(DOCTYPE_ARCredit)
			|| getDocumentType().equals(DOCTYPE_APCredit);
		boolean payables = getDocumentType().equals(DOCTYPE_APInvoice)
			|| getDocumentType().equals(DOCTYPE_APCredit);
		BigDecimal acctAmt = Env.ZERO;
		FactLine fl = null;
		//	Revenue/Cost
		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine line = p_lines[i];
			boolean landedCost = false;
			if  (payables)
				landedCost = landedCost(as, fact, line, false);
			if (landedCost && as.isExplicitCostAdjustment())
			{
				fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
					m_rate, getC_Currency_ID(), null, line.getAmtSource());
				//
				fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
					m_rate, getC_Currency_ID(), line.getAmtSource(), null);
				String desc = line.getDescription();
				if (desc == null)
					desc = "100%";
				else
					desc += " 100%";
				fl.setDescription(desc);
			}
			if (!landedCost)
			{
				MAccount acct = line.getAccount(
					payables ? ProductCost.ACCTTYPE_P_Expense : ProductCost.ACCTTYPE_P_Revenue, as);
				if (payables)
				{
					//	if Fixed Asset
					if (line.isItem())
						acct = line.getAccount (ProductCost.ACCTTYPE_P_InventoryClearing, as);
				}
				BigDecimal amt = line.getAmtSource().multiply(multiplier);
				BigDecimal amt2 = null;
				if (creditMemo)
				{
					amt2 = amt;
					amt = null;
				}
				if (payables)	//	Vendor = DR
					fl = fact.createLine (line, acct,
						m_rate, getC_Currency_ID(), amt, amt2);
				else			//	Customer = CR
					fl = fact.createLine (line, acct,
						m_rate, getC_Currency_ID(), amt2, amt);
				if (fl != null)
					acctAmt = acctAmt.add(fl.getAcctBalance());
			}
		}
		//  Tax
		for (int i = 0; i < m_taxes.length; i++)
		{
			BigDecimal amt = m_taxes[i].getAmount();
			BigDecimal amt2 = null;
			if (creditMemo)
			{
				amt2 = amt;
				amt = null;
			}
			FactLine tl = null;
			if (payables)
				tl = fact.createLine (null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
					m_rate, getC_Currency_ID(), amt, amt2);
			else
				tl = fact.createLine (null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
					m_rate, getC_Currency_ID(), amt2, amt);
			if (tl != null)
				tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
		}
		//  Set Locations
		FactLine[] fLines = fact.getLines();
		for (int i = 0; i < fLines.length; i++)
		{
			if (fLines[i] != null)
			{
				if (payables)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
				else
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);    //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				}
			}
		}
		return acctAmt;
	}	//	createFactCash
	
	
	/**
	 * 	Create Landed Cost accounting & Cost lines
	 *	@param as accounting schema
	 *	@param fact fact
	 *	@param line document line
	 *	@param dr DR entry (normal api)
	 *	@return true if landed costs were created
	 */
	private boolean landedCost (MAcctSchema as, Fact fact, DocLine line, boolean dr) 
	{
		int C_InvoiceLine_ID = line.get_ID();
		MLandedCostAllocation[] lcas = MLandedCostAllocation.getOfInvoiceLine(
			getCtx(), C_InvoiceLine_ID, getTrxName());
		if (lcas.length == 0)
			return false;
		
		//	Delete Old
		String sql = "DELETE M_CostDetail WHERE C_InvoiceLine_ID=" + C_InvoiceLine_ID;
		int no = DB.executeUpdate(sql, getTrxName());
		if (no != 0)
			log.config("CostDetail Deleted #" + no);

		//	Calculate Total Base
		double totalBase = 0;
		for (int i = 0; i < lcas.length; i++)
			totalBase += lcas[i].getBase().doubleValue();
		
		//	Create New
		MInvoiceLine il = new MInvoiceLine (getCtx(), C_InvoiceLine_ID, getTrxName());
		for (int i = 0; i < lcas.length; i++)
		{
			MLandedCostAllocation lca = lcas[i];
			if (lca.getBase().signum() == 0)
				continue;
			double percent = totalBase / lca.getBase().doubleValue();
			String desc = il.getDescription();
			if (desc == null)
				desc = percent + "%";
			else
				desc += " - " + percent + "%";
			if (line.getDescription() != null)
				desc += " - " + line.getDescription(); 
			
			//	Accounting
			ProductCost pc = new ProductCost (Env.getCtx(), 
				lca.getM_Product_ID(), lca.getM_AttributeSetInstance_ID(), getTrxName());
			BigDecimal drAmt = null;
			BigDecimal crAmt = null;
			if (dr)
				drAmt = lca.getAmt();
			else
				crAmt = lca.getAmt();
			FactLine fl = fact.createLine (line, pc.getAccount(ProductCost.ACCTTYPE_P_CostAdjustment, as),
					m_rate, getC_Currency_ID(), drAmt, crAmt);
			fl.setDescription(desc);
			
			//	Cost Detail - Convert to AcctCurrency
			BigDecimal allocationAmt =  lca.getAmt();
			if (getC_Currency_ID() != as.getC_Currency_ID())
				allocationAmt = MConversionRate.convert(getCtx(), allocationAmt, 
					getC_Currency_ID(), as.getC_Currency_ID(),
					getDateAcct(), getC_ConversionType_ID(), 
					getAD_Client_ID(), getAD_Org_ID());
			if (allocationAmt.scale() > as.getCostingPrecision())
				allocationAmt = allocationAmt.setScale(as.getCostingPrecision(), BigDecimal.ROUND_HALF_UP);
			if (!dr)
				allocationAmt = allocationAmt.negate();
			MCostDetail cd = new MCostDetail (as, lca.getAD_Org_ID(), 
				lca.getM_Product_ID(), lca.getM_AttributeSetInstance_ID(),
				lca.getM_CostElement_ID(),
				allocationAmt, Env.ZERO,		//	Qty
				desc, getTrxName());
			cd.setC_InvoiceLine_ID(C_InvoiceLine_ID);
			boolean ok = cd.save();
			if (ok && !cd.isProcessed())
			{
				MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
				if (client.isCostImmediate())
					cd.process();
			}
		}
		
		log.config("Created #" + lcas.length);
		return true;
	}	//	landedCosts

	/**
	 * 	Update ProductPO PriceLastInv
	 *	@param as accounting schema
	 */
	private void updateProductPO (MAcctSchema as)
	{
		MClientInfo ci = MClientInfo.get(getCtx(), as.getAD_Client_ID());
		if (ci.getC_AcctSchema1_ID() != as.getC_AcctSchema_ID())
			return;
		
		StringBuffer sql = new StringBuffer (
			"UPDATE M_Product_PO po "
			+ "SET PriceLastInv = "
			//	select
			+ "(SELECT currencyConvert(il.PriceActual,i.C_Currency_ID,po.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID) "
			+ "FROM C_Invoice i, C_InvoiceLine il "
			+ "WHERE i.C_Invoice_ID=il.C_Invoice_ID"
			+ " AND po.M_Product_ID=il.M_Product_ID AND po.C_BPartner_ID=i.C_BPartner_ID"
			+ " AND ROWNUM=1 AND i.C_Invoice_ID=").append(get_ID()).append(") ")
			//	update
			.append("WHERE EXISTS (SELECT * "
			+ "FROM C_Invoice i, C_InvoiceLine il "
			+ "WHERE i.C_Invoice_ID=il.C_Invoice_ID"
			+ " AND po.M_Product_ID=il.M_Product_ID AND po.C_BPartner_ID=i.C_BPartner_ID"
			+ " AND i.C_Invoice_ID=").append(get_ID()).append(")");
		int no = DB.executeUpdate(sql.toString(), getTrxName());
		log.fine("Updated=" + no);
	}	//	updateProductPO
	
	/**
	 *  Update Product Info (old).
	 *  - Costing (PriceLastInv)
	 *  - PO (PriceLastInv)
	 *  @param C_AcctSchema_ID accounting schema
	 *  @deprecated old costing
	 */
	private void updateProductInfo (int C_AcctSchema_ID)
	{
		log.fine("C_Invoice_ID=" + get_ID());

		/** @todo Last.. would need to compare document/last updated date
		 *  would need to maintain LastPriceUpdateDate on _PO and _Costing */

		//  update Product Costing
		//  requires existence of currency conversion !!
		//  if there are multiple lines of the same product last price uses first
		//	-> TotalInvAmt is sometimes NULL !! -> error
		StringBuffer sql = new StringBuffer (
			"UPDATE M_Product_Costing pc "
			+ "SET (PriceLastInv, TotalInvAmt,TotalInvQty) = "
			//	select
			+ "(SELECT currencyConvert(il.PriceActual,i.C_Currency_ID,a.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID),"
			+ " currencyConvert(il.LineNetAmt,i.C_Currency_ID,a.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID),il.QtyInvoiced "
			+ "FROM C_Invoice i, C_InvoiceLine il, C_AcctSchema a "
			+ "WHERE i.C_Invoice_ID=il.C_Invoice_ID"
			+ " AND pc.M_Product_ID=il.M_Product_ID AND pc.C_AcctSchema_ID=a.C_AcctSchema_ID"
			+ " AND ROWNUM=1"
			+ " AND pc.C_AcctSchema_ID=").append(C_AcctSchema_ID).append(" AND i.C_Invoice_ID=")
			.append(get_ID()).append(") ")
			//	update
			.append("WHERE EXISTS (SELECT * "
			+ "FROM C_Invoice i, C_InvoiceLine il, C_AcctSchema a "
			+ "WHERE i.C_Invoice_ID=il.C_Invoice_ID"
			+ " AND pc.M_Product_ID=il.M_Product_ID AND pc.C_AcctSchema_ID=a.C_AcctSchema_ID"
			+ " AND pc.C_AcctSchema_ID=").append(C_AcctSchema_ID).append(" AND i.C_Invoice_ID=")
				.append(get_ID()).append(")");
		int no = DB.executeUpdate(sql.toString(), getTrxName());
		log.fine("M_Product_Costing - Updated=" + no);
	}   //  updateProductInfo

}   //  Doc_Invoice
