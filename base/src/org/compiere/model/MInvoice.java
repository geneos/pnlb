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

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;

import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 *	Invoice Model.
 * 	Please do not set DocStatus and C_DocType_ID directly.
 * 	They are set in the process() method.
 * 	Use DocAction and C_DocTypeTarget_ID instead.
 *
 *  @author Jorg Janke
 *  @version $Id: MInvoice.java,v 1.105 2006/01/04 05:39:20 jjanke Exp $
 */
@SuppressWarnings("serial")
public class MInvoice extends X_C_Invoice implements DocAction
{
	/**
	 * 	Get Payments Of BPartner
	 *	@param ctx context
	 *	@param C_BPartner_ID id
	 *	@return array
	 */
	public static MInvoice[] getOfBPartner (Properties ctx, int C_BPartner_ID, String trxName)
	{
		ArrayList<MInvoice> list = new ArrayList<MInvoice>();
		String sql = "SELECT * FROM C_Invoice WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MInvoice(ctx,rs, trxName));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
		MInvoice[] retValue = new MInvoice[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfBPartner

	/**
	 * 	Create new Invoice by copying
	 * 	@param from invoice
	 * 	@param dateDoc date of the document date
	 * 	@param C_DocTypeTarget_ID target doc type
	 * 	@param isSOTrx sales order
	 * 	@param counter create counter links
	 * 	@param trxName trx
	 * 	@param setOrder set Order links
	 *	@return Invoice
	 */
	public static MInvoice copyFrom (MInvoice from, Timestamp dateDoc,
		int C_DocTypeTarget_ID, boolean isSOTrx, boolean counter,
		String trxName, boolean setOrder)
	{
		MInvoice to = new MInvoice (from.getCtx(), 0, null);
		to.set_TrxName(trxName);
		PO.copyValues (from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("C_Invoice_ID", I_ZERO);

		String sql = "SELECT d.IsDocNoControlled, s.CurrentNext "
			+ "FROM C_DocType d, AD_Sequence s "
			+ "WHERE C_DocType_ID=?"		//	1
			+ " AND d.DocNoSequence_ID=s.AD_Sequence_ID(+)";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_DocTypeTarget_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				if (rs.getString(1).equals("Y"))
					to.setDocumentNo("<" + rs.getString(2) + ">");
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) {e.printStackTrace();}


		to.setDocStatus (DOCSTATUS_Drafted);		//	Draft
		to.setDocAction(DOCACTION_Complete);
		//
		to.setC_DocType_ID(0);
		to.setC_DocTypeTarget_ID (C_DocTypeTarget_ID);
		to.setIsSOTrx(isSOTrx);
		//
		to.setDateInvoiced (dateDoc);
		to.setDateAcct (dateDoc);
		to.setDatePrinted(null);
		to.setIsPrinted (false);
		//
		to.setIsApproved (false);
		to.setC_Payment_ID(0);
		to.setC_CashLine_ID(0);
		to.setIsPaid (false);
		to.setIsInDispute(false);
		//
		//	Amounts are updated by trigger when adding lines
		to.setGrandTotal(Env.ZERO);
		to.setTotalLines(Env.ZERO);
		//	Percepciones
		to.setPercepcionIB(Env.ZERO);
		to.setPercepcionIVA(Env.ZERO);
		//
		to.setIsTransferred (false);
		to.setPosted (false);
		to.setProcessed (false);
		//	delete references
		to.setIsSelfService(false);
		if (!setOrder)
			to.setC_Order_ID(0);
		if (counter)
		{
			to.setRef_Invoice_ID(from.getC_Invoice_ID());
			//	Try to find Order link
			if (from.getC_Order_ID() != 0)
			{
				MOrder peer = new MOrder (from.getCtx(), from.getC_Order_ID(), from.get_TrxName());
				if (peer.getRef_Order_ID() != 0)
					to.setC_Order_ID(peer.getRef_Order_ID());
			}
		}
		else
			to.setRef_Invoice_ID(0);

		if (!to.save(trxName))
			throw new IllegalStateException("Could not create Invoice");
		if (counter)
			from.setRef_Invoice_ID(to.getC_Invoice_ID());

		//	Lines
		if (to.copyLinesFrom(from, counter, setOrder) == 0)
			throw new IllegalStateException("Could not create Invoice Lines");

		return to;
	}	//	copyFrom

	/**
	 * 	Get PDF File Name
	 *	@param documentDir directory
	 * 	@param C_Invoice_ID invoice
	 *	@return file name
	 */
	public static String getPDFFileName (String documentDir, int C_Invoice_ID)
	{
		StringBuffer sb = new StringBuffer (documentDir);
		if (sb.length() == 0)
			sb.append(".");
		if (!sb.toString().endsWith(File.separator))
			sb.append(File.separator);
		sb.append("C_Invoice_ID_")
			.append(C_Invoice_ID)
			.append(".pdf");
		return sb.toString();
	}	//	getPDFFileName


	/**
	 * 	Get MInvoice from Cache
	 *	@param ctx context
	 *	@param C_Invoice_ID id
	 *	@return MInvoice
	 */
	public static MInvoice get (Properties ctx, int C_Invoice_ID)
	{
		Integer key = new Integer (C_Invoice_ID);
		MInvoice retValue = (MInvoice) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MInvoice (ctx, C_Invoice_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MInvoice>	s_cache	= new CCache<Integer,MInvoice>("C_Invoice", 20, 2);	//	2 minutes


	/**************************************************************************
	 * 	Invoice Constructor
	 * 	@param ctx context
	 * 	@param C_Invoice_ID invoice or 0 for new
	 * 	@param trxName trx name
	 */
	public MInvoice (Properties ctx, int C_Invoice_ID, String trxName)
	{
		super (ctx, C_Invoice_ID, trxName);
		if (C_Invoice_ID == 0)
		{
			setDocStatus (DOCSTATUS_Drafted);		//	Draft
			setDocAction (DOCACTION_Complete);

			setDateInvoiced (new Timestamp (System.currentTimeMillis ()));
			setDateAcct (new Timestamp (System.currentTimeMillis ()));
			//
			setChargeAmt (Env.ZERO);
			setTotalLines (Env.ZERO);
			setGrandTotal (Env.ZERO);
			//
			setIsSOTrx (true);
			setIsTaxIncluded (false);
			setIsApproved (false);
			setIsDiscountPrinted (false);
			setIsPaid (false);
			setSendEMail (false);
			setIsPrinted (false);
			setIsTransferred (false);
			setIsSelfService(false);
			setIsPayScheduleValid(false);
			setIsInDispute(false);
			setPosted(false);
			super.setProcessed (false);
			setProcessing(false);
		}
	}	//	MInvoice

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MInvoice (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInvoice

        private boolean flagSave = false;
	/**
	 * 	Create Invoice from Order
	 *	@param order order
	 *	@param C_DocTypeTarget_ID target document type
	 *	@param invoiceDate date or null
	 */
	public MInvoice (MOrder order, int C_DocTypeTarget_ID, Timestamp invoiceDate)
	{
		this (order.getCtx(), 0, order.get_TrxName());
		setClientOrg(order);
		setOrder(order);	//	set base settings
		//
		if (C_DocTypeTarget_ID == 0)
			C_DocTypeTarget_ID =  DB.getSQLValue(null,
				"SELECT C_DocTypeInvoice_ID FROM C_DocType WHERE C_DocType_ID=?",
				order.getC_DocType_ID());
		setC_DocTypeTarget_ID(C_DocTypeTarget_ID);
		if (invoiceDate != null)
			setDateInvoiced(invoiceDate);
		setDateAcct(getDateInvoiced());
		//
		setSalesRep_ID(order.getSalesRep_ID());
		//
		setC_BPartner_ID(order.getBill_BPartner_ID());

		setBill_Location_ID(order.getLocation_Bill_ID());
		setC_Location_ID(order.getC_Location_ID());

		//setC_BPartner_Location_ID(order.getBill_Location_ID());
		setAD_User_ID(order.getBill_User_ID());
                
                String sqldocType = "SELECT name FROM C_DocType WHERE C_DocType_ID = ?";
                PreparedStatement pstmtT = DB.prepareStatement(sqldocType, null);
		ResultSet rsT;
                try {
                    pstmtT.setInt(1, getC_DocType_ID());
                    rsT = pstmtT.executeQuery();
                    if (rsT.next()){
                        String docType = rsT.getString(1);
                        if (docType.equals("RCF C Recibo Factura C") || docType.equals("AP Factura A Proveedor") || 
                                docType.equals("AP Factura B Proveedor") || docType.equals("AP Factura C Proveedor") || 
                                docType.equals("AP Factura E Proveedor") || docType.equals("AP Factura M") || 
                                docType.equals("AP Factura X") || docType.equals("AP Nota de crédito A Proveedor") || 
                                docType.equals("AP Nota de crédito E Proveedor") || docType.equals("AP Nota de débito A Proveedor") || 
                                docType.equals("AP Nota de débito E Proveedor") || 
                                docType.equals("AP Nota de crédito C Proveedor") || docType.equals("AP Nota de débito B Proveedor") || 
                                docType.equals("AP Nota de crédito B Proveedor") || docType.equals("AP Nota de débito C Proveedor")){
                                         MInvoiceTax newITax = new MInvoiceTax(getCtx(), 0, get_TrxName());
                                         newITax.setClientOrg(order);
                                         newITax.setC_Invoice_ID(getC_Invoice_ID()); //VER ESTO.
                                         newITax.setC_Tax_ID(1000055);
                                         newITax.setPrecision(getPrecision());
                                         newITax.setIsTaxIncluded(isTaxIncluded());
                                         newITax.setTaxBaseAmt(Env.ZERO);
                                         newITax.setTaxAmt(getGrandTotal());
                                         newITax.save();
                        }
                        rsT.close();
                        pstmtT.close();
                    }   
                } catch (SQLException ex) {
                        Logger.getLogger(MInvoice.class.getName()).log(Level.SEVERE, null, ex);
                }
	}	//	MInvoice

	/**
	 * 	Create Invoice from Shipment
	 *	@param ship shipment
	 *	@param invoiceDate date or null
	 */
	public MInvoice (MInOut ship, Timestamp invoiceDate)
	{
		this (ship.getCtx(), 0, ship.get_TrxName());
		setClientOrg(ship);
		setShipment(ship);	//	set base settings
		//
		setC_DocTypeTarget_ID();
		if (invoiceDate != null)
			setDateInvoiced(invoiceDate);
		setDateAcct(getDateInvoiced());
		//
		setSalesRep_ID(ship.getSalesRep_ID());
		setAD_User_ID(ship.getAD_User_ID());
	}	//	MInvoice

	/**
	 * 	Create Invoice from Batch Line
	 *	@param batch batch
	 *	@param line batch line
	 */
	public MInvoice (MInvoiceBatch batch, MInvoiceBatchLine line)
	{
		this (line.getCtx(), 0, line.get_TrxName());
		setClientOrg(line);
		setDocumentNo(line.getDocumentNo());
		//
		setIsSOTrx(batch.isSOTrx());
		MBPartner bp = new MBPartner (line.getCtx(), line.getC_BPartner_ID(), line.get_TrxName());
		setBPartner(bp);	//	defaults
		//
		setIsTaxIncluded(line.isTaxIncluded());
		//	May conflict with default price list
		setC_Currency_ID(batch.getC_Currency_ID());
		setC_ConversionType_ID(batch.getC_ConversionType_ID());
		//
	//	setPaymentRule(order.getPaymentRule());
	//	setC_PaymentTerm_ID(order.getC_PaymentTerm_ID());
	//	setPOReference("");
		setDescription(batch.getDescription());
	//	setDateOrdered(order.getDateOrdered());
		//
		setAD_OrgTrx_ID(line.getAD_OrgTrx_ID());
		setC_Project_ID(line.getC_Project_ID());
	//	setC_Campaign_ID(line.getC_Campaign_ID());
		setC_Activity_ID(line.getC_Activity_ID());
		setUser1_ID(line.getUser1_ID());
		setUser2_ID(line.getUser2_ID());
		//
		setC_DocTypeTarget_ID(line.getC_DocType_ID());
		setDateInvoiced(line.getDateInvoiced());
		setDateAcct(line.getDateAcct());
		//
		setSalesRep_ID(batch.getSalesRep_ID());
		//
		setC_BPartner_ID(line.getC_BPartner_ID());
		setC_BPartner_Location_ID(line.getC_BPartner_Location_ID());
		setAD_User_ID(line.getAD_User_ID());
	}	//	MInvoice

	/**	Open Amount				*/
	private BigDecimal 		m_openAmt = null;

	/**	Invoice Lines			*/
	private MInvoiceLine[]	m_lines;
	/**	Invoice Taxes			*/
	private MInvoiceTax[]	m_taxes;
	/**	Logger			*/
	private static CLogger s_log = CLogger.getCLogger(MInvoice.class);

	/**
	 * 	Overwrite Client/Org if required
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg

	/**
	 * 	Set Business Partner Defaults & Details
	 * 	@param bp business partner
	 */
	public void setBPartner (MBPartner bp)
	{
		if (bp == null)
			return;

		setC_BPartner_ID(bp.getC_BPartner_ID());
		//	Set Defaults
		int ii = 0;
		if (isSOTrx())
			ii = bp.getC_PaymentTerm_ID();
		else
			ii = bp.getPO_PaymentTerm_ID();
		if (ii != 0)
			setC_PaymentTerm_ID(ii);
		//
		if (isSOTrx())
			ii = bp.getM_PriceList_ID();
		else
			ii = bp.getPO_PriceList_ID();
		if (ii != 0)
			setM_PriceList_ID(ii);
		//
		String ss = bp.getPaymentRule();
		if (ss != null)
			setPaymentRule(ss);


		//	Set Locations
		MBPartnerLocation[] locs = bp.getLocations(false);
		if (locs != null)
		{
			for (int i = 0; i < locs.length; i++)
			{
				if ((locs[i].isBillTo() && isSOTrx())
				|| (locs[i].isPayFrom() && !isSOTrx()))
					setC_BPartner_Location_ID(locs[i].getC_BPartner_Location_ID());
			}
			//	set to first
			if (getC_BPartner_Location_ID() == 0 && locs.length > 0)
				setC_BPartner_Location_ID(locs[0].getC_BPartner_Location_ID());
		}
		if (getC_BPartner_Location_ID() == 0)
			log.log(Level.SEVERE, "Has no To Address: " + bp);

		//	Set Contact
		MUser[] contacts = bp.getContacts(false);
		if (contacts != null && contacts.length > 0)	//	get first User
			setAD_User_ID(contacts[0].getAD_User_ID());
	}	//	setBPartner

	/**
	 * 	Set Order References
	 * 	@param order order
	 */
	public void setOrder (MOrder order)
	{
		if (order == null)
			return;

		setC_Order_ID(order.getC_Order_ID());
		setIsSOTrx(order.isSOTrx());
		setIsDiscountPrinted(order.isDiscountPrinted());
		setIsSelfService(order.isSelfService());
		setSendEMail(order.isSendEMail());
		//
		setM_PriceList_ID(order.getM_PriceList_ID());
		setIsTaxIncluded(order.isTaxIncluded());
		setC_Currency_ID(order.getC_Currency_ID());
		setC_ConversionType_ID(order.getC_ConversionType_ID());

		//COMENTADO DANIEL GINI - REQ-051
		//setPaymentRule(order.getPaymentRule());

		setC_PaymentTerm_ID(order.getC_PaymentTerm_ID());
		setPOReference(order.getPOReference());
		setDescription(order.getDescription());
		setDateOrdered(order.getDateOrdered());
		//
		setAD_OrgTrx_ID(order.getAD_OrgTrx_ID());
		setC_Project_ID(order.getC_Project_ID());
		setC_Campaign_ID(order.getC_Campaign_ID());
		setC_Activity_ID(order.getC_Activity_ID());
		setUser1_ID(order.getUser1_ID());
		setUser2_ID(order.getUser2_ID());

		/*
		 * 06-01-2011 Camarzana Mariano
		 * Al generar la OV se agrega la causa de emision correspondiente
		 * a la Factura
		 */
		setCausaEmision(order.getCausaEmision());
	}	//	setOrder

	/**
	 * 	Set Shipment References
	 * 	@param ship shipment
	 */
	public void setShipment (MInOut ship)
	{
		if (ship == null)
			return;

		setIsSOTrx(ship.isSOTrx());
		//
		MBPartner bp = new MBPartner (getCtx(), ship.getC_BPartner_ID(), null);
		setBPartner (bp);
		//
		setSendEMail(ship.isSendEMail());
		//
		setPOReference(ship.getPOReference());
		setDescription(ship.getDescription());
		setDateOrdered(ship.getDateOrdered());
		//
		setAD_OrgTrx_ID(ship.getAD_OrgTrx_ID());
		setC_Project_ID(ship.getC_Project_ID());
		setC_Campaign_ID(ship.getC_Campaign_ID());
		setC_Activity_ID(ship.getC_Activity_ID());
		setUser1_ID(ship.getUser1_ID());
		setUser2_ID(ship.getUser2_ID());
		//
		if (ship.getC_Order_ID() != 0)
		{
			setC_Order_ID(ship.getC_Order_ID());
			MOrder order = new MOrder (getCtx(), ship.getC_Order_ID(), get_TrxName());
			setIsDiscountPrinted(order.isDiscountPrinted());
			setM_PriceList_ID(order.getM_PriceList_ID());
			setIsTaxIncluded(order.isTaxIncluded());
			setC_Currency_ID(order.getC_Currency_ID());
			setC_ConversionType_ID(order.getC_ConversionType_ID());
			setPaymentRule(order.getPaymentRule());
			setC_PaymentTerm_ID(order.getC_PaymentTerm_ID());
			//
			MDocType dt = MDocType.get(getCtx(), order.getC_DocType_ID());
			if (dt.getC_DocTypeInvoice_ID() != 0)
				setC_DocTypeTarget_ID(dt.getC_DocTypeInvoice_ID());
			//	Overwrite Invoice Address
			setC_BPartner_Location_ID(order.getBill_Location_ID());
		}
	}	//	setShipment

	/**
	 * 	Set Target Document Type
	 * 	@param DocBaseType doc type MDocType.DOCBASETYPE_
	 */
	public void setC_DocTypeTarget_ID (String DocBaseType)
	{
		String sql = "SELECT C_DocType_ID FROM C_DocType "
			+ "WHERE AD_Client_ID=? AND DocBaseType=? "
			+ "ORDER BY IsDefault DESC";
		int C_DocType_ID = DB.getSQLValue(null, sql, getAD_Client_ID(), DocBaseType);
		if (C_DocType_ID <= 0)
			log.log(Level.SEVERE, "Not found for AC_Client_ID="
				+ getAD_Client_ID() + " - " + DocBaseType);
		else
		{
			log.fine(DocBaseType);
			setC_DocTypeTarget_ID (C_DocType_ID);
			boolean isSOTrx = MDocType.DOCBASETYPE_ARInvoice.equals(DocBaseType)
				|| MDocType.DOCBASETYPE_ARCreditMemo.equals(DocBaseType);
			setIsSOTrx (isSOTrx);
		}
	}	//	setC_DocTypeTarget_ID

	/**
	 * 	Set Target Document Type.
	 * 	Based on SO flag AP/AP Invoice
	 */
	public void setC_DocTypeTarget_ID ()
	{
		if (getC_DocTypeTarget_ID() > 0)
			return;
		if (isSOTrx())
			setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_ARInvoice);
		else
			setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_APInvoice);
	}	//	setC_DocTypeTarget_ID


	/**
	 * 	Get Grand Total
	 * 	@param creditMemoAdjusted adjusted for CM (negative)
	 *	@return grand total
	 */
	public BigDecimal getGrandTotal (boolean creditMemoAdjusted)
	{
		if (!creditMemoAdjusted)
			return super.getGrandTotal();
		//
		BigDecimal amt = getGrandTotal();
		if (isCreditMemo())
			return amt.negate();
		return amt;
	}	//	getGrandTotal


	/**
	 * 	Get Invoice Lines of Invoice
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	private MInvoiceLine[] getLines (String whereClause)
	{
		ArrayList<MInvoiceLine> list = new ArrayList<MInvoiceLine>();
		String sql = "SELECT * FROM C_InvoiceLine WHERE C_Invoice_ID=? ";
		if (whereClause != null)
			sql += whereClause;
		sql += " ORDER BY Line";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_Invoice_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MInvoiceLine il = new MInvoiceLine(getCtx(), rs, get_TrxName());
				il.setInvoice(this);
				list.add(il);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getLines", e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}

		//
		MInvoiceLine[] lines = new MInvoiceLine[list.size()];
		list.toArray(lines);
		return lines;
	}	//	getLines

	/**
	 * 	Get Invoice Lines
	 * 	@param requery
	 * 	@return lines
	 */
	public MInvoiceLine[] getLines (boolean requery)
	{
		if (m_lines == null || m_lines.length == 0 || requery)
			m_lines = getLines(null);
		return m_lines;
	}	//	getLines

	/**
	 * 	Get Lines of Invoice
	 * 	@return lines
	 */
	public MInvoiceLine[] getLines()
	{
		return getLines(false);
	}	//	getLines


	/**
	 * 	Renumber Lines
	 *	@param step start and step
	 */
	public void renumberLines (int step)
	{
		int number = step;
		MInvoiceLine[] lines = getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];
			line.setLine(number);
			line.save();
			number += step;
		}
		m_lines = null;
	}	//	renumberLines

	/**
	 * 	Copy Lines From other Invoice.
	 *	@param otherInvoice invoice
	 * 	@param counter create counter links
	 * 	@param setOrder set order links
	 *	@return number of lines copied
	 */
	public int copyLinesFrom (MInvoice otherInvoice, boolean counter, boolean setOrder)
	{
		if (isProcessed() || isPosted() || otherInvoice == null)
			return 0;
		MInvoiceLine[] fromLines = otherInvoice.getLines(false);
		int count = 0;
		for (int i = 0; i < fromLines.length; i++)
		{
			MInvoiceLine line = new MInvoiceLine (getCtx(), 0, get_TrxName());
			MInvoiceLine fromLine = fromLines[i];
			if (counter)	//	header
				PO.copyValues (fromLine, line, getAD_Client_ID(), getAD_Org_ID());
			else
				PO.copyValues (fromLine, line, fromLine.getAD_Client_ID(), fromLine.getAD_Org_ID());
			line.setC_Invoice_ID(getC_Invoice_ID());
			line.setInvoice(this);
			//begin e-evolution vpj-cd patch version 1.106, Thu Jan 5 06:39:28 2006 UTC
			line.set_ValueNoCheck ("C_InvoiceLine_ID", I_ZERO);     // new
			//line.setC_InvoiceLine_ID(0);	// new
			//end  e-evolution vpj-cd patch version 1.106, Thu Jan 5 06:39:28 2006 UTC
			//	Reset
			if (!setOrder)
				line.setC_OrderLine_ID(0);
			line.setRef_InvoiceLine_ID(0);
			line.setM_InOutLine_ID(0);
			line.setA_Asset_ID(0);
			line.setM_AttributeSetInstance_ID(0);
			line.setS_ResourceAssignment_ID(0);
			//	New Tax
			if (getC_BPartner_ID() != otherInvoice.getC_BPartner_ID())
				line.setTax();	//	recalculate
			//
			if (counter)
			{
				line.setRef_InvoiceLine_ID(fromLine.getC_InvoiceLine_ID());
				if (fromLine.getC_OrderLine_ID() != 0)
				{
					MOrderLine peer = new MOrderLine (getCtx(), fromLine.getC_OrderLine_ID(), get_TrxName());
					if (peer.getRef_OrderLine_ID() != 0)
						line.setC_OrderLine_ID(peer.getRef_OrderLine_ID());
				}
				line.setM_InOutLine_ID(0);
				if (fromLine.getM_InOutLine_ID() != 0)
				{
					MInOutLine peer = new MInOutLine (getCtx(), fromLine.getM_InOutLine_ID(), get_TrxName());
					if (peer.getRef_InOutLine_ID() != 0)
						line.setM_InOutLine_ID(peer.getRef_InOutLine_ID());
				}
			}
			//
			line.setProcessed(false);
			if (line.save(get_TrxName()))
				count++;
			//	Cross Link
			if (counter)
			{
				fromLine.setRef_InvoiceLine_ID(line.getC_InvoiceLine_ID());
				fromLine.save(get_TrxName());
			}
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom

	/** Reversal Flag		*/
	private boolean m_reversal = false;

	/**
	 * 	Set Reversal
	 *	@param reversal reversal
	 */
	private void setReversal(boolean reversal)
	{
		m_reversal = reversal;
	}	//	setReversal
	/**
	 * 	Is Reversal
	 *	@return reversal
	 */
	private boolean isReversal()
	{
		return m_reversal;
	}	//	isReversal

	/**
	 * 	Get Taxes
	 *	@param requery requery
	 *	@return array of taxes
	 */
	public MInvoiceTax[] getTaxes (boolean requery)
	{
		if (m_taxes != null && !requery)
			return m_taxes;
		String sql = "SELECT * FROM C_InvoiceTax WHERE C_Invoice_ID=?";
		ArrayList<MInvoiceTax> list = new ArrayList<MInvoiceTax>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_Invoice_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MInvoiceTax(getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getTaxes", e);
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

		m_taxes = new MInvoiceTax[list.size ()];
		list.toArray (m_taxes);
		return m_taxes;
	}	//	getTaxes

	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription

	/**
	 * 	Is it a Credit Memo?
	 *	@return true if CM
	 */
	public boolean isCreditMemo()
	{
		MDocType dt = MDocType.get(getCtx(),
			getC_DocType_ID()==0 ? getC_DocTypeTarget_ID() : getC_DocType_ID());
		return MDocType.DOCBASETYPE_APCreditMemo.equals(dt.getDocBaseType())
			|| MDocType.DOCBASETYPE_ARCreditMemo.equals(dt.getDocBaseType());
	}	//	isCreditMemo

	/**
	 * 	Set Processed.
	 * 	Propergate to Lines/Taxes
	 *	@param processed processed
	 */
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
                MInvoiceLine[] lines = getLines(true); 
                int noLine = 0;
                for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];
                        line.setProcessed(processed);
                        if(line.save(get_TrxName()))
                            noLine++;
                }

                int noTax = 0;
                MInvoiceTax[] taxes = getTaxes(true);
                for (int i = 0; i < taxes.length; i++)
		{
			MInvoiceTax tax = taxes[i];
                        tax.setProcessed(processed);
                        if ( tax.save(get_TrxName()) )
                            noTax++;
                }
		/*String set = "SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE C_Invoice_ID=" + getC_Invoice_ID();
		int noLine = DB.executeUpdate("UPDATE C_InvoiceLine " + set, get_TrxName());
		int noTax = DB.executeUpdate("UPDATE C_InvoiceTax " + set, get_TrxName());*/
		m_lines = null;
		m_taxes = null;
		log.fine(processed + " - Lines=" + noLine + ", Tax=" + noTax);
                Trx trx = Trx.get(get_TrxName(), true);
                trx.commit();
	}	//	setProcessed

	/**
	 * 	Validate Invoice Pay Schedule
	 *	@return pay schedule is valid
	 */
	public boolean validatePaySchedule()
	{
		MInvoicePaySchedule[] schedule = MInvoicePaySchedule.getInvoicePaySchedule
			(getCtx(), getC_Invoice_ID(), 0, get_TrxName());
		log.fine("#" + schedule.length);
		if (schedule.length == 0)
		{
			setIsPayScheduleValid(false);
			return false;
		}
		//	Add up due amounts
		BigDecimal total = Env.ZERO;
		for (int i = 0; i < schedule.length; i++)
		{
			schedule[i].setParent(this);
			BigDecimal due = schedule[i].getDueAmt();
			if (due != null)
				total = total.add(due);
		}
		boolean valid = getGrandTotal().compareTo(total) == 0;
		setIsPayScheduleValid(valid);

		//	Update Schedule Lines
		for (int i = 0; i < schedule.length; i++)
		{
			if (schedule[i].isValid() != valid)
			{
				schedule[i].setIsValid(valid);
				schedule[i].save(get_TrxName());
			}
		}
		return valid;
	}	//	validatePaySchedule
/**
 *
 *
 */
	private String incrementarNumero(){
		 String documentNo = this.getDocumentNo();
		 documentNo = documentNo.substring(1,documentNo.length()-1);
         Integer num = new Integer(documentNo) + 1;
         MDocType docType = MDocType.get(getCtx(), this.getC_DocTypeTarget_ID());
         MSequence seq = new MSequence(getCtx(),docType.getDocNoSequence_ID(), null);
         int next = seq.getCurrentNext();
         seq.setCurrentNext(next + 1);
         seq.save(get_TrxName());

         String prefijo = "";
         prefijo = seq.getPrefix();
         String nro = num.toString();



         switch (prefijo.length()) {

           case 1:
                 prefijo = "000" + prefijo;
                 break;
           case 2:
                 prefijo = "00" + prefijo;
                 break;
           case 3:
                 prefijo = "0" + prefijo;
                 break;
           case 4:
                 break;

         }

         switch (nro.length()) {
           case 1:
                 nro = "0000000" + nro;
                 break;
           case 2:
                 nro = "000000" + nro;
                 break;
           case 3:
                 nro = "00000" + nro;
                 break;
           case 4:
                 nro = "0000" + nro;
                 break;
           case 5:
                 nro = "000" + nro;
                 break;
           case 6:
                 nro = "00" + nro;
                 break;
           case 7:
                 nro = "0" + nro;
                 break;
           case 8:
                 break;
         }

         String val = prefijo + "-" + nro;
         return val;
	}

	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{

                /*
                ** Vit4B - Agregado para tomar el remito en formato 1-2 y pasarlo
                ** a formato 001-00000002

                **  OJO permitir los que son trx de venta en C_DocType_ID
                **
                ** 26/02/2007
                */

                /*

                String documentNo = this.getDocumentNo();

                MDocType doc = MDocType.get(getCtx(),getC_DocType_ID());

                //	Shipment - Needs Order

                int C_Order_ID = this.getC_Order_ID();
		String issotrx = "";

                int doctype = 0;



                if (C_Order_ID != 0)
                {
			issotrx =  DB.getSQLValueString(null,

                                "SELECT dt.issotrx FROM C_Order o INNER JOIN C_Doctype dt ON (dt.C_Doctype_ID = o.C_Doctype_ID) WHERE o.C_Order_ID=?",
				C_Order_ID);

                }
                else
                {
                    doctype = this.getC_DocType_ID();
                    if(doctype == 0)
                        doctype = Env.getContextAsInt(getCtx(), 2, "C_DocTypeTarget_ID");
                    issotrx =  DB.getSQLValueString(null,"SELECT dt.issotrx FROM C_Doctype dt WHERE C_Doctype_ID=?",doctype);
                }

                if (issotrx.equals("N"))
		{

                    if(documentNo == "" || documentNo == null)
                    {
                        JOptionPane.showMessageDialog(null,"Ingrese N�mero de Documento", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }

                    int indexOf = documentNo.indexOf("-");

                    if(indexOf == -1 || indexOf == 0 || indexOf == documentNo.length())
                    {
                        JOptionPane.showMessageDialog(null,"N�mero de Documento Inv�lido", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }

                    String sucursal = documentNo.substring(0,indexOf);
                    String nro = documentNo.substring(indexOf+1,documentNo.length());

                    switch (sucursal.length()) {
                      case 1:
                            sucursal = "000" + sucursal;
                            break;
                      case 2:
                            sucursal = "00" + sucursal;
                            break;
                      case 3:
                            sucursal = "0" + sucursal;
                            break;
                      case 4:
                            break;
                      default:
                            JOptionPane.showMessageDialog(null,"N�mero de Documento Inv�lido", "Info", JOptionPane.INFORMATION_MESSAGE);
                            return false;
                    }

                    switch (nro.length()) {
                      case 1:
                            nro = "0000000" + nro;
                            break;
                      case 2:
                            nro = "000000" + nro;
                            break;
                      case 3:
                            nro = "00000" + nro;
                            break;
                      case 4:
                            nro = "0000" + nro;
                            break;
                      case 5:
                            nro = "000" + nro;
                            break;
                      case 6:
                            nro = "00" + nro;
                            break;
                      case 7:
                            nro = "0" + nro;
                            break;
                      case 8:
                            break;
                      default:
                            JOptionPane.showMessageDialog(null,"N�mero de Documento Inv�lido", "Info", JOptionPane.INFORMATION_MESSAGE);
                            return false;
                    }

                    this.setDocumentNo(sucursal + "-" + nro);



		}

                 */

        //if (!isSOTrx())
		//{

			/**
			 *  	Vit4B - Agregado para agregar ceros al documento 00000002
			 *
			 * 			26/02/2007
            */

            String documentNo = this.getDocumentNo();

            if(documentNo != null && !documentNo.equals(""))
            {
                /*
                 *      Si el numero es definido por el sistema automaticamente le asigna <>
                 *      en este caso se debe reformatear para anexarle los ceros y aumentar
                 *      el numerador a mano ya que el sistema pierde la referencia autom�tica.
                 */

                if(documentNo.indexOf("<") != -1)
                {
                    documentNo = documentNo.substring(1,documentNo.length()-1);
                    MDocType docType = MDocType.get(getCtx(), this.getC_DocTypeTarget_ID());
                    MSequence seq = new MSequence(getCtx(),docType.getDocNoSequence_ID(), null);
                    int next = seq.getCurrentNext();
                    seq.setCurrentNext(next + 1);
                    seq.save();

                }

                int indexOf = documentNo.indexOf("-");

                String prefijo = "";
                String nro = "";

                if(indexOf == -1)
                {
                    MDocType docType = MDocType.get(getCtx(), this.getC_DocTypeTarget_ID());
                    MSequence seq = new MSequence(getCtx(),docType.getDocNoSequence_ID(), null);
                    prefijo = seq.getPrefix();
                    nro = documentNo;

                }
                else
                {
                    prefijo = this.getDocumentNo().substring(0,indexOf);
                    nro = this.getDocumentNo().substring(indexOf+1,this.getDocumentNo().length());
                }

                if(nro == "" || nro == null)
                {
                    JOptionPane.showMessageDialog(null,"N�mero de Documento Inv�lido", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if(prefijo == "" || prefijo == null)
                {
                    JOptionPane.showMessageDialog(null,"N�mero de Documento Inv�lido", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }



                switch (prefijo.length()) {

                  case 1:
                        prefijo = "000" + prefijo;
                        break;
                  case 2:
                        prefijo = "00" + prefijo;
                        break;
                  case 3:
                        prefijo = "0" + prefijo;
                        break;
                  case 4:
                        break;

                  default:
                        JOptionPane.showMessageDialog(null,"N�mero de Documento Inv�lido", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;

                }

                switch (nro.length()) {
                  case 1:
                        nro = "0000000" + nro;
                        break;
                  case 2:
                        nro = "000000" + nro;
                        break;
                  case 3:
                        nro = "00000" + nro;
                        break;
                  case 4:
                        nro = "0000" + nro;
                        break;
                  case 5:
                        nro = "000" + nro;
                        break;
                  case 6:
                        nro = "00" + nro;
                        break;
                  case 7:
                        nro = "0" + nro;
                        break;
                  case 8:
                        break;
                  default:
                        JOptionPane.showMessageDialog(null,"N�mero de Documento Inv�lido", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                }

                String val = prefijo + "-" + nro;

                if (ValueFormat.validFormat(val,"0000-00000000"))
                {
                	try
    				{	if (!isSOTrx())
    					     {
		                		String query = "select C_Invoice_ID, C_DocTypeTarget_ID, C_BPartner_ID from C_Invoice where DocumentNo = ?";

		    					PreparedStatement pstmt = DB.prepareStatement(query, null);
		    					pstmt.setString(1, val);
		    					ResultSet rs = pstmt.executeQuery();

		    					if (rs.next() && (getC_Invoice_ID() != rs.getInt(1)) && (getC_DocTypeTarget_ID() == rs.getInt(2)) && (getC_BPartner_ID() == rs.getInt(3)))
		    					{
		    						JOptionPane.showMessageDialog(null,"No se permite cargar un mismo N�mero, para un mismo Tipo de documento, para un mismo Socio de Negocio.","Error - Nro. Documento duplicado", JOptionPane.ERROR_MESSAGE);
		    						return false;
		    					}

		    					rs.close();
		    					pstmt.close();
    				        }
    				/*
    				 * 15-11-2010 Camarzana Mariano modificacion agregada para que incremente
    				 * automaticamente el numero de documento en las facturas cliente en caso
    				 * de que coincida la numeracion, el tipo de documento
    				 *
    				 */
    				else {
    							boolean repetido = true;
    							while (repetido)
	    							{
	    								String query = "select C_Invoice_ID, C_DocTypeTarget_ID, C_BPartner_ID from C_Invoice where DocumentNo = ?";
		    							PreparedStatement pstmt = DB.prepareStatement(query, null);
		    							pstmt.setString(1, val);
		    							ResultSet rs = pstmt.executeQuery();
		    							if (rs.next() && (getC_Invoice_ID() != rs.getInt(1)) && (getC_DocTypeTarget_ID() ==  rs.getInt(2)))
		    								   val = incrementarNumero();
		    							else
		    								repetido = false;
				    					rs.close();
				    					pstmt.close();
	    							}

    				}
                }
    				catch (SQLException e){
    				}

                	this.setDocumentNo(val);
                }
                else
                {
                	JOptionPane.showMessageDialog(null,"N�mero de Documento Inv�lido", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

            }
            else
            {
            	JOptionPane.showMessageDialog(null,"No ingres� N�mero de Documento", "Info", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }

		log.fine("");
		//	No Partner Info - set Template
		if (getC_BPartner_ID() == 0)
			setBPartner(MBPartner.getTemplate(getCtx(), getAD_Client_ID()));



		/**
		 * 04-11-2010 Camarzana  Mariano Correccion porque estaba seteando los valores del socio de negocio
		 * en vez de la orden de venta
		 */
		if ((getC_BPartner_Location_ID() == 0)&& !isSOTrx())
			setBPartner(new MBPartner(getCtx(), getC_BPartner_ID(), null));

		//	Price List
		if (getM_PriceList_ID() == 0)
		{
			int ii = Env.getContextAsInt(getCtx(), "#M_PriceList_ID");
			if (ii != 0)
				setM_PriceList_ID(ii);
			else
			{
				String sql = "SELECT M_PriceList_ID FROM M_PriceList WHERE AD_Client_ID=? AND IsDefault='Y'";
				ii = DB.getSQLValue (null, sql, getAD_Client_ID());
				if (ii != 0)
					setM_PriceList_ID (ii);
			}
		}

		//	Currency
		if (getC_Currency_ID() == 0)
		{
			String sql = "SELECT C_Currency_ID FROM M_PriceList WHERE M_PriceList_ID=?";
			int ii = DB.getSQLValue (null, sql, getM_PriceList_ID());
			if (ii != 0)
				setC_Currency_ID (ii);
			else
				setC_Currency_ID(Env.getContextAsInt(getCtx(), "#C_Currency_ID"));
		}

		//	Sales Rep
		if (getSalesRep_ID() == 0)
		{
			int ii = Env.getContextAsInt(getCtx(), "#SalesRep_ID");
			if (ii != 0)
				setSalesRep_ID (ii);
		}

		//	Document Type
		if (getC_DocType_ID() == 0)
			setC_DocType_ID (0);	//	make sure it's set to 0
		if (getC_DocTypeTarget_ID() == 0)
			setC_DocTypeTarget_ID(isSOTrx() ? MDocType.DOCBASETYPE_ARInvoice : MDocType.DOCBASETYPE_APInvoice);

		//	Payment Term
		if (getC_PaymentTerm_ID() == 0)
		{
			int ii = Env.getContextAsInt(getCtx(), "#C_PaymentTerm_ID");
			if (ii != 0)
				setC_PaymentTerm_ID (ii);
			else
			{
				String sql = "SELECT C_PaymentTerm_ID FROM C_PaymentTerm WHERE AD_Client_ID=? AND IsDefault='Y'";
				ii = DB.getSQLValue(null, sql, getAD_Client_ID());
				if (ii != 0)
					setC_PaymentTerm_ID (ii);
			}
		}
                /*
                 *  25/10/2013 Maria Jesus Martin
                 *  Verificacion de la existencia de la direccion de un socio de negocio.
                 */
                MBPartnerLocation bPartnerLocation = new MBPartnerLocation(getCtx(),getC_BPartner_ID(),null);
                if (bPartnerLocation == null){
                    JOptionPane.showMessageDialog(null,"El Socio de Negocio no tiene Localización. Por favor agregue estos datos en la Ventana del Socio de Negocio.","Error - Socio de Negocio", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

		return true;
	}	//	beforeSave

	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{
            /*
         *  12/04/2013 
         *  Modificacion para que el pago y la cobranza no se pueda eliminar si no
         *  esta en estado borrador.
         * 
         */
            if (DOCSTATUS_Drafted.equals(getDocStatus())){
                if (getC_Order_ID() != 0) {
			log.saveError("Error", Msg.getMsg(getCtx(), "CannotDelete"));
			return false;
		}
                return true;
            }
            else
            {
                JOptionPane.showMessageDialog(null,"El documento no se puede eliminar ya que no esta en Estado Borrador.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
       
	}	//	beforeDelete

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInvoice[")
			.append(get_ID()).append("-").append(getDocumentNo())
			.append(",GrandTotal=").append(getGrandTotal());
		if (m_lines != null)
			sb.append(" (#").append(m_lines.length).append(")");
		sb.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo


	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{

                
            if (!success || newRecord)
			return success;

		if (is_ValueChanged("AD_Org_ID"))
		{
			String sql = "UPDATE C_InvoiceLine ol"
				+ " SET AD_Org_ID ="
					+ "(SELECT AD_Org_ID"
					+ " FROM C_Invoice o WHERE ol.C_Invoice_ID=o.C_Invoice_ID) "
				+ "WHERE C_Invoice_ID=" + getC_Order_ID();
			int no = DB.executeUpdate(sql, get_TrxName());
			log.fine("Lines -> #" + no);
		}
		return true;
	}	//	afterSave


	/**
	 * 	Set Price List (and Currency) when valid
	 * 	@param M_PriceList_ID price list
	 */
	public void setM_PriceList_ID (int M_PriceList_ID)
	{
		String sql = "SELECT M_PriceList_ID, C_Currency_ID "
			+ "FROM M_PriceList WHERE M_PriceList_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_PriceList_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				super.setM_PriceList_ID (rs.getInt(1));
				setC_Currency_ID (rs.getInt(2));
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "setM_PriceList_ID", e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
	}	//	setM_PriceList_ID


	/**
	 * 	Get Allocated Amt in Invoice Currency
	 *	@return pos/neg amount or null
	 */
	public BigDecimal getAllocatedAmt ()
	{
		BigDecimal retValue = null;
		String sql = "SELECT SUM(currencyConvert(al.Amount+al.DiscountAmt+al.WriteOffAmt,"
				+ "ah.C_Currency_ID, i.C_Currency_ID,ah.DateTrx,i.C_ConversionType_ID, al.AD_Client_ID,al.AD_Org_ID)) "
			+ "FROM C_AllocationLine al"
			+ " INNER JOIN C_AllocationHdr ah ON (al.C_AllocationHdr_ID=ah.C_AllocationHdr_ID)"
			+ " INNER JOIN C_Invoice i ON (al.C_Invoice_ID=i.C_Invoice_ID) "
			+ "WHERE al.C_Invoice_ID=?"
			+ " AND ah.IsActive='Y' AND al.IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_Invoice_ID());

			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getBigDecimal(1);
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
	//	log.fine("getAllocatedAmt - " + retValue);
		//	? ROUND(NVL(v_AllocatedAmt,0), 2);
		return retValue;
	}	//	getAllocatedAmt

	/**
	 * 	Test Allocation (and set paid flag)
	 *	@return true if updated
	 */
	public boolean testAllocation()
	{
		BigDecimal alloc = getAllocatedAmt();	//	absolute

		if (alloc == null)
			alloc = Env.ZERO;
 		BigDecimal total = getGrandTotal();

		if (!isSOTrx())
			total = total.negate();
		if (isCreditMemo())
			total = total.negate();
		boolean test = total.compareTo(alloc) == 0;
		boolean change = test != isPaid();
		if (change)
			setIsPaid(test);
		log.fine("Paid=" + test
			+ " (" + alloc + "=" + total + ")");
		return change;
	}	//	testAllocation

	/**
	 * 	Set Paid Flag for invoices
	 *	@param C_BPartner_ID if 0 all
	 */
	public static void setIsPaid (Properties ctx, int C_BPartner_ID, String trxName)
	{
		int counter = 0;
		String sql = "SELECT * FROM C_Invoice "
			+ "WHERE IsPaid='N' AND DocStatus IN ('CO','CL')";
		if (C_BPartner_ID > 1)
			sql += " AND C_BPartner_ID=?";
		else
			sql += " AND AD_Client_ID=" + Env.getAD_Client_ID(ctx);
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			if (C_BPartner_ID > 1)
				pstmt.setInt (1, C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MInvoice invoice = new MInvoice(ctx, rs, trxName);
				if (invoice.testAllocation())
					if (invoice.save())
						counter++;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
		s_log.config("#" + counter);
		/**/
	}	//	setIsPaid

	/**
	 * 	Get Open Amount.
	 * 	Used by web interface
	 * 	@return Open Amt
	 */
	public BigDecimal getOpenAmt ()
	{
		return getOpenAmt (true, null);
	}	//	getOpenAmt

	/**
	 * 	Get Open Amount
	 * 	@param creditMemoAdjusted adjusted for CM (negative)
	 * 	@param paymentDate ignored Payment Date
	 * 	@return Open Amt
	 */
	public BigDecimal getOpenAmt (boolean creditMemoAdjusted, Timestamp paymentDate)
	{
		if (isPaid())
			return Env.ZERO;
		//
		if (m_openAmt == null)
		{
			m_openAmt = getGrandTotal();
			if (paymentDate != null)
			{
				//	Payment Discount
				//	Payment Schedule
			}
			BigDecimal allocated = getAllocatedAmt();
			if (allocated != null)
			{
				allocated = allocated.abs();	//	is absolute
				m_openAmt = m_openAmt.subtract(allocated);
			}
		}
		//
		if (!creditMemoAdjusted)
			return m_openAmt;
		if (isCreditMemo())
			return m_openAmt.negate();
		return m_openAmt;
	}	//	getOpenAmt


	/**
	 * 	Get Document Status
	 *	@return Document Status Clear Text
	 */
	public String getDocStatusName()
	{
		return MRefList.getListName(getCtx(), 131, getDocStatus());
	}	//	getDocStatusName


	/**************************************************************************
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
		ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
		if (re == null)
			return null;
		return re.getPDF(file);
	}	//	createPDF

	/**
	 * 	Get PDF File Name
	 *	@param documentDir directory
	 *	@return file name
	 */
	public String getPDFFileName (String documentDir)
	{
		return getPDFFileName (documentDir, getC_Invoice_ID());
	}	//	getPDFFileName

	/**
	 *	Get ISO Code of Currency
	 *	@return Currency ISO
	 */
	public String getCurrencyISO()
	{
		return MCurrency.getISO_Code (getCtx(), getC_Currency_ID());
	}	//	getCurrencyISO

	/**
	 * 	Get Currency Precision
	 *	@return precision
	 */
	public int getPrecision()
	{
		return MCurrency.getStdPrecision(getCtx(), getC_Currency_ID());
	}	//	getPrecision


	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	process

	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**
	 * 	Unlock Document.
	 * 	@return true if success
	 */
	public boolean unlockIt()
	{
		log.info("unlockIt - " + toString());
		setProcessing(false);
		return true;
	}	//	unlockIt

	/**
	 * 	Invalidate Document
	 * 	@return true if success
	 */
	public boolean invalidateIt()
	{
		log.info("invalidateIt - " + toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt

	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt()
	{
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		MDocType dt = MDocType.get(getCtx(), getC_DocTypeTarget_ID());

		//	Std Period open?
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType())) {
            // Change performed by Alejandro Scott
            // If the invoice date belongs to a closed period then change
            // the date of application to the next open period
			Timestamp startDate = MPeriod.getStartDateNextOpenPeriod(getCtx(), getDateAcct(), dt.getDocBaseType());
            if (startDate == null) {
                m_processMsg = "@PeriodClosed@";
                return DocAction.STATUS_Invalid;
            }
            setDateAcct(startDate);
		}
		//	Lines
		MInvoiceLine[] lines = getLines(true);
		if (lines.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Invalid;
		}
		//	No Cash Book
		if (PAYMENTRULE_Cash.equals(getPaymentRule())
			&& MCashBook.get(getCtx(), getAD_Org_ID(), getC_Currency_ID()) == null)
		{
			m_processMsg = "@NoCashBook@";
			return DocAction.STATUS_Invalid;
		}

		//	Convert/Check DocType
		if (getC_DocType_ID() != getC_DocTypeTarget_ID() )
			setC_DocType_ID(getC_DocTypeTarget_ID());
		if (getC_DocType_ID() == 0)
		{
			m_processMsg = "No Document Type";
			return DocAction.STATUS_Invalid;
		}

		explodeBOM();
                /*
                 *  23/10/2013 Maria Jesus Martin
                 *  Si la factura esta en estado borrador, y las lineas quedaron como procesadas, que se revierta.
                 * 
                 */
                setProcessed(false);
                
                if (!calculateTaxTotal())	//	setTotals
		{
			m_processMsg = "Error calculating Tax";
			return DocAction.STATUS_Invalid;
		}

		createPaySchedule();

		//	Credit Status
		if (isSOTrx() && !isReversal())
		{
			MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), null);
			if (MBPartner.SOCREDITSTATUS_CreditStop.equals(bp.getSOCreditStatus()))
			{
				m_processMsg = "@BPartnerCreditStop@ - @TotalOpenBalance@="
					+ bp.getTotalOpenBalance()
					+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
				return DocAction.STATUS_Invalid;
			}
		}

		//	Landed Costs
		if (!isSOTrx())
		{
			for (int i = 0; i < lines.length; i++)
			{
				MInvoiceLine line = lines[i];
				String error = line.allocateLandedCosts();
				if (error != null && error.length() > 0)
				{
					m_processMsg = error;
					return DocAction.STATUS_Invalid;
				}
			}
		}

		//	Add up Amounts
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt

	/**
	 * 	Explode non stocked BOM.
	 */
	private void explodeBOM ()
	{
		String where = "AND IsActive='Y' AND EXISTS "
			+ "(SELECT * FROM M_Product p WHERE C_InvoiceLine.M_Product_ID=p.M_Product_ID"
			+ " AND	p.IsBOM='Y' AND p.IsVerified='Y' AND p.IsStocked='N')";
		//
		String sql = "SELECT COUNT(*) FROM C_InvoiceLine "
			+ "WHERE C_Invoice_ID=? " + where;
		int count = DB.getSQLValue(get_TrxName(), sql, getC_Invoice_ID());
		while (count != 0)
		{
			renumberLines (100);

			//	Order Lines with non-stocked BOMs
			MInvoiceLine[] lines = getLines (where);
			for (int i = 0; i < lines.length; i++)
			{
				MInvoiceLine line = lines[i];
				MProduct product = MProduct.get (getCtx(), line.getM_Product_ID());
				log.fine(product.getName());
				//	New Lines
				int lineNo = line.getLine ();
				MProductBOM[] boms = MProductBOM.getBOMLines (product);
				for (int j = 0; j < boms.length; j++)
				{
					MProductBOM bom = boms[j];
					MInvoiceLine newLine = new MInvoiceLine (this);
					newLine.setLine (++lineNo);
					newLine.setM_Product_ID (bom.getProduct().getM_Product_ID(),
						bom.getProduct().getC_UOM_ID());
					newLine.setQty (line.getQtyInvoiced().multiply(
						bom.getBOMQty ()));		//	Invoiced/Entered
					if (bom.getDescription () != null)
						newLine.setDescription (bom.getDescription ());
					//
					newLine.setPrice ();
					newLine.save (get_TrxName());
				}
				//	Convert into Comment Line
				line.setM_Product_ID (0);
				line.setM_AttributeSetInstance_ID (0);
				line.setPriceEntered (Env.ZERO);
				line.setPriceActual (Env.ZERO);
				line.setPriceLimit (Env.ZERO);
				line.setPriceList (Env.ZERO);
				line.setLineNetAmt (Env.ZERO);
				//
				String description = product.getName ();
				if (product.getDescription () != null)
					description += " " + product.getDescription ();
				if (line.getDescription () != null)
					description += " " + line.getDescription ();
				line.setDescription (description);
				line.save (get_TrxName());
			} //	for all lines with BOM

			m_lines = null;
			count = DB.getSQLValue (get_TrxName(), sql, getC_Invoice_ID ());
			renumberLines (10);
		}	//	while count != 0
	}	//	explodeBOM

	/**
	 * 	Calculate Tax and Total
	 */
	private boolean calculateTaxTotal()
	{
		log.fine("");
		//	Delete Taxes
		DB.executeUpdate("DELETE C_InvoiceTax WHERE C_Invoice_ID=" + getC_Invoice_ID(), get_TrxName());
		m_taxes = null;
                
		//	Lines
		BigDecimal totalLines = Env.ZERO;
		ArrayList<Integer> taxList = new ArrayList<Integer>();
		MInvoiceLine[] lines = getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];
			/**	Sync ownership for SO
			if (isSOTrx() && line.getAD_Org_ID() != getAD_Org_ID())
			{
				line.setAD_Org_ID(getAD_Org_ID());
				line.save();
			}	**/
			Integer taxID = new Integer(line.getC_Tax_ID());
			if (!taxList.contains(taxID))
			{
				MInvoiceTax iTax = MInvoiceTax.get (line, getPrecision(),
					false, get_TrxName());	//	current Tax
				if (iTax != null)
				{
					iTax.setIsTaxIncluded(isTaxIncluded());
					if (!iTax.calculateTaxFromLines())
						return false;
					if (!iTax.save())
						return false;
					taxList.add(taxID);
				}
			}
			totalLines = totalLines.add(line.getLineNetAmt());
		}

		//	Taxes
		BigDecimal grandTotal = totalLines;
		MInvoiceTax[] taxes = getTaxes(true);
		for (int i = 0; i < taxes.length; i++)
		{
			MInvoiceTax iTax = taxes[i];
			MTax tax = iTax.getTax();
			if (tax.isSummary())
			{
				MTax[] cTaxes = tax.getChildTaxes(false);	//	Multiple taxes
				for (int j = 0; j < cTaxes.length; j++)
				{
					MTax cTax = cTaxes[j];
					BigDecimal taxAmt = cTax.calculateTax(iTax.getTaxBaseAmt(), isTaxIncluded(), getPrecision());
					//
					MInvoiceTax newITax = new MInvoiceTax(getCtx(), 0, get_TrxName());
					newITax.setClientOrg(this);
					newITax.setC_Invoice_ID(getC_Invoice_ID());
					newITax.setC_Tax_ID(cTax.getC_Tax_ID());
					newITax.setPrecision(getPrecision());
					newITax.setIsTaxIncluded(isTaxIncluded());
					newITax.setTaxBaseAmt(iTax.getTaxBaseAmt());
					newITax.setTaxAmt(taxAmt);
					if (!newITax.save(get_TrxName()))
						return false;
					//
					if (!isTaxIncluded())
						grandTotal = grandTotal.add(taxAmt);
				}
				if (!iTax.delete(true, get_TrxName()))
					return false;
			}
			else
			{
				if (!isTaxIncluded())
					grandTotal = grandTotal.add(iTax.getTaxAmt());
			}
		}

                /*
                 *      Vit4B - 10/10/2007
                 *
                 *      Agregado para que cuando el proceso de completar actualiza el
                 *      total de la factura contemple la percepci�n calculada
                 *
                 *
                 */

		System.out.print(grandTotal.floatValue());

        BigDecimal percepciones = BigDecimal.ZERO;

        if (!isSOTrx())
        {		String sql = "SELECT COALESCE(SUM(ip.AMOUNT),0) FROM C_InvoicePercep ip, C_Invoice i "
                	+ "WHERE i.C_Invoice_ID=ip.C_Invoice_ID AND ip.IsActive = 'Y' AND ip.C_Invoice_ID=?";

                try {

                	PreparedStatement psmt = DB.prepareStatement(sql, null);
                	psmt.setInt(1, getC_Invoice_ID());
                	ResultSet rs = psmt.executeQuery();

	                if (rs.next())
	                {
	                	percepciones = rs.getBigDecimal(1);
	                }

	                rs.close();
	                psmt.close();

                } catch (Exception e)	{}

                grandTotal = grandTotal.add(percepciones);
                percepciones = percepciones.setScale(2,BigDecimal.ROUND_DOWN);
        }
        else
        {
        	setTotalLines(totalLines);
    		setGrandTotal(grandTotal);

    		/*11-01-2011 Camarzana Mariano
    		 * Ticket 136 (Las notas de cr�dito por descuento no deberian calcular ning�n tipo de percepciones)
    		 * Ticket 118 (Los ajustes al d�bito y al cr�dito no deben calcular retenciones ni percepciones)
    		 * if(!flagSave && grandTotal.compareTo(Env.ZERO) != 0 )
    		*/
    		int c_doctype_id = getC_DocType_ID();
    		MDocType doctype = new MDocType(Env.getCtx(),c_doctype_id,null);
        	if(!flagSave && grandTotal.compareTo(Env.ZERO) != 0 &&	!doctype.getPrintName().contains("NCD") &&
        			 !doctype.getPrintName().contains("AJC") &&!doctype.getPrintName().contains("AJD"))
    		{
    			Percepciones.inicio(getC_Invoice_ID(),this,get_TrxName());
    			percepciones = getPercepcionIB().add(getPercepcionIVA());
    			grandTotal = grandTotal.add(percepciones);
    		}
        }

        setTotalLines(totalLines);
		setGrandTotal(grandTotal);
		return true;

	}	//	calculateTaxTotal


	public BigDecimal getTotalLines(boolean force)
	{
		if (force)
		{
			String sql =
					" SELECT COALESCE(SUM(LineNetAmt),0) FROM C_InvoiceLine "
            	+	" WHERE IsActive = 'Y' AND AD_ORG_ID = ? AND AD_CLIENT_ID = ? AND C_Invoice_ID = ?";

            try {

            	PreparedStatement psmt = DB.prepareStatement(sql, null);
            	psmt.setInt(1, getAD_Org_ID());
            	psmt.setInt(2, getAD_Client_ID());
            	psmt.setInt(3, getC_Invoice_ID());
            	ResultSet rs = psmt.executeQuery();

                if (rs.next())
                	return rs.getBigDecimal(1);

                rs.close();
                psmt.close();

            } catch (Exception e)	{}
		}

		return getTotalLines();
	}


	/**
	 * 	(Re) Create Pay Schedule
	 *	@return true if valid schedule
	 */
	private boolean createPaySchedule()
	{
		if (getC_PaymentTerm_ID() == 0)
			return false;
		MPaymentTerm pt = new MPaymentTerm(getCtx(), getC_PaymentTerm_ID(), null);
		log.fine(pt.toString());
		return pt.apply(this);		//	calls validate pay schedule
	}	//	createPaySchedule


	/**
	 * 	Approve Document
	 * 	@return true if success
	 */
	public boolean  approveIt()
	{
		log.info(toString());
		setIsApproved(true);
		return true;
	}	//	approveIt

	/**
	 * 	Reject Approval
	 * 	@return true if success
	 */
	public boolean rejectIt()
	{
		log.info(toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt

	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}
		//	Implicit Approval
		if (!isApproved())
			approveIt();
		log.info(toString());
		StringBuffer info = new StringBuffer();

		//	Create Cash
		if (PAYMENTRULE_Cash.equals(getPaymentRule()))
		{
			MCash cash = MCash.get (getCtx(), getAD_Org_ID(),
				getDateInvoiced(), getC_Currency_ID(), get_TrxName());
			if (cash == null || cash.get_ID() == 0)
			{
				m_processMsg = "@NoCashBook@";
				return DocAction.STATUS_Invalid;
			}
			MCashLine cl = new MCashLine (cash);
			cl.setInvoice(this);
			if (!cl.save(get_TrxName()))
			{
				m_processMsg = "Could not save Cash Journal Line";
				return DocAction.STATUS_Invalid;
			}
			info.append("@C_Cash_ID@: " + cash.getName() +  " #" + cl.getLine());
			setC_CashLine_ID(cl.getC_CashLine_ID());
		}	//	CashBook

                /*
                 *  25/10/2012
                 *  Modificacion para que compruebe que las lineas de la Factura que sean creadas a partir de una 
                 *  OC, no superen el 5% del monto total de la linea de la OC.
                 * 
                 */
                
                //Buscamos cada una de las lineas de la Factura
                String lineI = "SELECT C_INVOICELINE_ID, C_ORDERLINE_ID,L.C_CHARGE_ID FROM C_INVOICELINE L LEFT JOIN C_INVOICE I "
                        + " ON (L.C_INVOICE_ID=I.C_INVOICE_ID) "
                        + " WHERE L.C_INVOICE_ID = "+ getC_Invoice_ID()
                        + " AND L.C_ORDERLINE_ID IS NOT NULL ";
                PreparedStatement pstmtL = DB.prepareStatement(lineI, null);
		ResultSet rsL;
                try {
                        rsL = pstmtL.executeQuery();
                        while (rsL.next()){

                            /*
                             * GENEOS - Pablo Velazquez
                             * 8/07/2013
                             * Si la linea esta facturando un cargo no se realiza el chequeo
                             * del umbral
                             */
                            Long chargeID = rsL.getLong(3);
                            if ( chargeID == null || chargeID.equals(0) ) {
                                String sumaL = "SELECT SUM(suma) "
                                        + "FROM ( SELECT SUM(linenetamt) as suma "
                                        + "       FROM C_INVOICELINE I LEFT JOIN C_INVOICE L  ON (L.C_INVOICE_ID=I.C_INVOICE_ID) "
                                        + "       WHERE I.c_orderline_id= " +rsL.getInt(2) + " AND L.C_INVOICE_ID = " + getC_Invoice_ID()
                                        + "       UNION"
                                        + "       ( "
                                        + "         SELECT SUM(linenetamt) as suma "
                                        + "         FROM C_INVOICELINE I LEFT JOIN C_INVOICE L ON (L.C_INVOICE_ID=I.C_INVOICE_ID) "
                                        + "         WHERE I.c_orderline_id ="+ rsL.getInt(2) +" AND L.DOCSTATUS = 'CO' "
                                        + "     ))";

                                PreparedStatement pstmtsuma = DB.prepareStatement(sumaL, null);
                                ResultSet rsS = pstmtsuma.executeQuery();
                                //Linea de la Factura sumado a todas las lineas que ya se han hecho.
                                MInvoiceLine iLine = new MInvoiceLine (getCtx(), (Integer)rsL.getInt(1), null);
                                rsS.next();
                                BigDecimal sumaTotal = rsS.getBigDecimal(1);
                                //Linea de la OC para verificar el monto
                                MOrderLine oLine = new MOrderLine (getCtx(), (Integer)rsL.getInt(2), null);
                                BigDecimal montoOC = oLine.getLineNetAmt();

                                /*
                                    *  28/11/2012 Zynnia
                                    *  Se agrega que se valide la moneda del la OC y la Factura. Si no son la misma 
                                    *  moneda tengo que convertir desde la OC a la Factura para poder hacer la validaciÃ³n 
                                    *  sobre la misma moneda.
                                    * 
                                    * 
                                */

                                MCurrency CurFrom = new MCurrency(getCtx(), oLine.getC_Currency_ID(), null);
                                MCurrency CurTo = new MCurrency(getCtx(), this.getC_Currency_ID(), null);

                                if(oLine.getC_Currency_ID() != this.getC_Currency_ID()){
                                    montoOC = MConversionRate.convert(getCtx(), montoOC, CurFrom.getC_Currency_ID(), CurTo.getC_Currency_ID(), this.getDateInvoiced(), 0, this.getAD_Client_ID(), this.getAD_Org_ID());
                                    if(montoOC == null){
                                      JOptionPane.showMessageDialog(null, "Error en tasa de conversión para el cálculo del umbral. Por favor validar.", "Info", JOptionPane.INFORMATION_MESSAGE);
                                      m_processMsg = "Error en tasa de conversión para el cálculo del umbral. Por favor validar.";
                                      return DocAction.STATUS_Invalid;
                                    }

                                }else{
                                    montoOC = oLine.getLineNetAmt();
                                }
                                //
                                /*
                                 *  29/11/2012 Zynnia                            
                                 *  Verificamos que no se pase del 10% pedido por Luis Bernetti ara que coincida 
                                 *  con el excedente de recepciÃ³n.
                                 *  De momento solo avisa y permite seguir por eso se comenta la linea 2216 luego hay que
                                 *  descomentar de modo de que no permita completar si supera el umbral
                                * 
                                */

                                //  Obtengo el umbral predeterminado - se usa el mismo umbral que la RecepciÃ³n
                                //  MInOut lÃ­nea 2469

                                BigDecimal umbral = Env.ZERO;
                                String sqlUmbral = "SELECT valor FROM M_UMBRAL WHERE ad_workflow_id = 1001078";
                                PreparedStatement pstmtUmbral = DB.prepareStatement(sqlUmbral, null);
                                ResultSet rsUmbral = pstmtUmbral.executeQuery();
                                if (rsUmbral.next()) {
                                    umbral = rsUmbral.getBigDecimal(1);
                                }
                                rsUmbral.close();
                                pstmtUmbral.close();

                                BigDecimal montoOCporc = montoOC.multiply(umbral).divide(BigDecimal.valueOf(100));
                                montoOCporc = montoOCporc.add(montoOC);

                                // Llevo a una misma escala de presiciÃ³n

                                if (CurTo != null) {
                                    montoOCporc = montoOCporc.setScale(CurTo.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                                    sumaTotal = sumaTotal.setScale(CurTo.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                                }                            


                                if (montoOCporc.compareTo(sumaTotal)== -1){
                                      JOptionPane.showMessageDialog(null, "El valor ingresado en la linea supera el umbral aceptado.", "Info", JOptionPane.INFORMATION_MESSAGE);
                                      m_processMsg = "El valor ingresado en la linea de Factura supera el umbral aceptado.";
                                      return DocAction.STATUS_Invalid;
                                }

                            }
                            
                        }
                    } catch (SQLException ex) {
                            Logger.getLogger(MInvoice.class.getName()).log(Level.SEVERE, null, ex);
                    }   
		//	Update Order & Match
		int matchInv = 0;
		int matchPO = 0;
		MInvoiceLine[] lines = getLines(false);

		/*
		 * 	Verificaci�n de que una l�nea de remito no sea utilizada m�s de una vez.
		 *
		 * 		REQ-048
		 *
		 *		DANIEL GINI - BISION 05/03/09
		 *
		 */

		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];

			try
			{
				if (line.getM_InOutLine_ID() != 0)
				{
                                                /*
                                                *   14/12/2012
                                                *   Hay que verificar el estado sino una factura anulada la toma y no me deja
                                                *   facturar porque supuestamente existe factura asociada.
                                                * 
                                                */
                                                String sql = "SELECT sum(qty) FROM M_MatchInv match "
                                                        + "INNER JOIN C_InvoiceLine invoiceline on (invoiceline.C_InvoiceLine_ID = match.C_InvoiceLine_ID) "
                                                        + "INNER JOIN C_Invoice invoice on (invoice.C_Invoice_ID = invoiceline.C_Invoice_ID) "
                                                        + "WHERE match.M_InOutLine_ID = " + line.getM_InOutLine_ID() + " AND invoice.docstatus = 'CO'";

						PreparedStatement pstmt = DB.prepareStatement(sql, null);

						ResultSet rs = pstmt.executeQuery();

                                                String sqlQty = "select qtyentered from m_inoutline where m_inoutline_id = "+ line.getM_InOutLine_ID();

                                                PreparedStatement pstmtLine = DB.prepareStatement(sqlQty, null);

						ResultSet rsLine = pstmtLine.executeQuery();

						if (rs.next())
						{
                                                    if (rsLine.next()){
                                                        if (rs.getBigDecimal(1).equals(rsLine.getBigDecimal(1))){
                                                            //31-05-2011 Camarzana Mariano - Cambio del mensaje de error
                                                            //m_processMsg = "Could not create Invoice Matching ";
                                                            m_processMsg = "Could not create Invoice Matching - El Remito ya fue ingresado en otra Factura";
                                                            return DocAction.STATUS_Invalid;
                                                        }
                                                    }
						}
				}

		/*		if (line.getC_OrderLine_ID()!= 0 && line.getM_Product_ID()!= 0 && !isReversal())
				{
					String sql = "SELECT M_MATCHPO_ID FROM M_MatchPO WHERE C_OrderLine_ID = " + line.getC_OrderLine_ID();
					PreparedStatement pstmt = DB.prepareStatement(sql, null);

					ResultSet rs = pstmt.executeQuery();

					if (rs.next())
					{
						m_processMsg = "Could not create Invoice Matching";
						return DocAction.STATUS_Invalid;
					}
				}
			*/
			}	catch (Exception e) {}

		}	//for all lines - FIN VERIFICACION

		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];

			//	Update Order Line
			//MOrderLine ol = null;
			if (line.getC_OrderLine_ID() != 0)
			{
				/*if (isSOTrx() || line.getM_Product_ID() == 0)
				{
					ol = new MOrderLine (getCtx(), line.getC_OrderLine_ID(), get_TrxName());
					if (line.getQtyInvoiced() != null)
						ol.setQtyInvoiced(ol.getQtyInvoiced().add(line.getQtyInvoiced()));
					if (!ol.save(get_TrxName()))
					{
						m_processMsg = "Could not update Order Line";
						return DocAction.STATUS_Invalid;
					}
				}
				*/ // -- ESTO EVALUAR SI ES NECESARIO

				//	Order Invoiced Qty updated via Matching Inv-PO
                        /*
                         * 12/04/2013 Maria Jesus Martin
                         * Cambio para que las OC que tenian cargo y no producto tambien tengan su linea 
                         * de que fueron facturadas en la pestaña de cotejados. De este modo si una OC con cargo
                         * es facturada completa, no vuelve a aparecer para facturar como sucedia hasta el momento.
                         *   
                         */    
			//	if (line.getM_Product_ID() != 0	&& !isReversal())
			    if (!isReversal())
                            {
					//	MatchPO is created also from MInOut when Invoice exists before Shipment
					BigDecimal matchQty = line.getQtyInvoiced();
					MMatchPO po = MMatchPO.create (line, null,
						getDateInvoiced(), matchQty);
					if (!po.save(get_TrxName()))
					{
						m_processMsg = "Could not create PO Matching";
						return DocAction.STATUS_Invalid;
					}
					else
						matchPO++;
                            }
		}

			//	Matching - Inv-Shipment
			if (line.getM_InOutLine_ID() != 0 && line.getM_Product_ID() != 0 && !isReversal())
			{
				BigDecimal matchQty = line.getQtyInvoiced();
				MMatchInv inv = new MMatchInv(line, getDateInvoiced(), matchQty);
				if (!inv.save(get_TrxName()))
				{
					m_processMsg = "Could not create Invoice Matching";
					return DocAction.STATUS_Invalid;
				}
				else
					matchInv++;
			}
		}	//	for all lines


		if (matchInv > 0)
			info.append(" @M_MatchInv_ID@#").append(matchInv).append(" ");
		if (matchPO > 0)
			info.append(" @M_MatchPO_ID@#").append(matchPO).append(" ");

		//	Update BP Statistics
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
		//	Update total revenue and balance / credit limit (reversed on AllocationLine.processIt)
		BigDecimal invAmt = MConversionRate.convertBase(getCtx(), getGrandTotal(true),	//	CM adjusted
			getC_Currency_ID(), getDateInvoiced(), 0, getAD_Client_ID(), getAD_Org_ID());

                if (invAmt==null){
                    m_processMsg = "Verificar si esta cargada la Tasa de Cambio.";
		    return DocAction.STATUS_Invalid;
                }

		System.out.print("\n MONTO: " + invAmt.floatValue());

		//	Total Balance
		BigDecimal newBalance = bp.getTotalOpenBalance(false);
		if (newBalance == null)
			newBalance = Env.ZERO;
		if (isSOTrx())
		{
			newBalance = newBalance.add(invAmt);
			//
			if (bp.getFirstSale() == null)
				bp.setFirstSale(getDateInvoiced());
			BigDecimal newLifeAmt = bp.getActualLifeTimeValue();
			if (newLifeAmt == null)
				newLifeAmt = invAmt;
			else
				newLifeAmt = newLifeAmt.add(invAmt);
			BigDecimal newCreditAmt = bp.getSO_CreditUsed();
			if (newCreditAmt == null)
				newCreditAmt = invAmt;
			else
				newCreditAmt = newCreditAmt.add(invAmt);
			//
			log.fine("GrandTotal=" + getGrandTotal(true) + "(" + invAmt
				+ ") BP Life=" + bp.getActualLifeTimeValue() + "->" + newLifeAmt
				+ ", Credit=" + bp.getSO_CreditUsed() + "->" + newCreditAmt
				+ ", Balance=" + bp.getTotalOpenBalance(false) + " -> " + newBalance);
			bp.setActualLifeTimeValue(newLifeAmt);
			bp.setSO_CreditUsed(newCreditAmt);
		}	//	SO
		else
		{
			newBalance = newBalance.subtract(invAmt);
			log.fine("GrandTotal=" + getGrandTotal(true) + "(" + invAmt
				+ ") Balance=" + bp.getTotalOpenBalance(false) + " -> " + newBalance);
		}

		//	BPartner - Update TotalOpenBalance && SO_CreditUsed
	    if (!updateBP(getC_DocTypeTarget_ID()))
		{
			m_processMsg = "Could not update Business Partner";
			return DocAction.STATUS_Invalid;
		}

		//	User - Last Result/Contact
		if (getAD_User_ID() != 0)
		{
			MUser user = new MUser (getCtx(), getAD_User_ID(), get_TrxName());
			user.setLastContact(new Timestamp(System.currentTimeMillis()));
			user.setLastResult(Msg.translate(getCtx(), "C_Invoice_ID") + ": " + getDocumentNo());
			if (!user.save(get_TrxName()))
			{
				m_processMsg = "Could not update Business Partner User";
				return DocAction.STATUS_Invalid;
			}
		}	//	user

		//	Update Project
		if (isSOTrx() && getC_Project_ID() != 0)
		{
			MProject project = new MProject (getCtx(), getC_Project_ID(), get_TrxName());
			BigDecimal amt = getGrandTotal(true);

			System.out.print("\n MONTO2: " + amt.floatValue());


			int C_CurrencyTo_ID = project.getC_Currency_ID();
			if (C_CurrencyTo_ID != getC_Currency_ID())
				amt = MConversionRate.convert(getCtx(), amt, getC_Currency_ID(), C_CurrencyTo_ID,
					getDateAcct(), 0, getAD_Client_ID(), getAD_Org_ID());
			BigDecimal newAmt = project.getInvoicedAmt();
			if (newAmt == null)
				newAmt = amt;
			else
				newAmt = newAmt.add(amt);
			log.fine("GrandTotal=" + getGrandTotal(true) + "(" + amt
				+ ") Project " + project.getName()
				+ " - Invoiced=" + project.getInvoicedAmt() + "->" + newAmt);


			System.out.print("\n NUEVO MONTO: " + newAmt.floatValue());

			project.setInvoicedAmt(newAmt);
			if (!project.save(get_TrxName()))
			{
				m_processMsg = "Could not update Project";
				return DocAction.STATUS_Invalid;
			}
		}	//	project

		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}

		//	Counter Documents
		MInvoice counter = createCounterDoc();
		if (counter != null)
			info.append(" - @CounterDoc@: @C_Invoice_ID@=").append(counter.getDocumentNo());

		/**
		 * 	@author Daniel
		 *  REQ-068, 21-07-2009
		 *
		 * 	PROVEEDOR/CLIENTE:
		 * 			La cotizaci�n es: - Uno, si se trabaja en moneda nacional.
		 * 							  - Tasa del Sistema, si no se trabaja en moneda nacional.
		 */
		MAcctSchema acct = new MAcctSchema(getCtx(),Env.getContextAsInt(getCtx(), "$C_AcctSchema_ID"),get_TrxName());
		if (acct.getC_Currency_ID()==getC_Currency_ID())
			setCotizacion(BigDecimal.ONE);
		else
			setCotizacion(TasaCambio.rate(getC_Currency_ID(), acct.getC_Currency_ID(), getDateInvoiced(), getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID()));

                // Puede ser que no lo estuviera grabando
                System.out.println("Graba cotización");
                this.save(get_TrxName());
                
		m_processMsg = info.toString().trim();
		setProcessed(true);
		setDocAction(DOCACTION_Close);

                // Notifies new for process automatic Payment
                ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);

		return DocAction.STATUS_Completed;
	}	//	completeIt

	/**
	 * 	Create Counter Document
	 */
	private MInvoice createCounterDoc()
	{
		//	Is this a counter doc ?
		if (getRef_Invoice_ID() != 0)
			return null;

		//	Org Must be linked to BPartner
		MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
		int counterC_BPartner_ID = org.getLinkedC_BPartner_ID();
		if (counterC_BPartner_ID == 0)
			return null;
		//	Business Partner needs to be linked to Org
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), null);
		int counterAD_Org_ID = bp.getAD_OrgBP_ID_Int();
		if (counterAD_Org_ID == 0)
			return null;

		MBPartner counterBP = new MBPartner (getCtx(), counterC_BPartner_ID, null);
		//MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID);
		log.info("Counter BP=" + counterBP.getName());

		//	Document Type
		int C_DocTypeTarget_ID = 0;
		MDocTypeCounter counterDT = MDocTypeCounter.getCounterDocType(getCtx(), getC_DocType_ID());
		if (counterDT != null)
		{
			log.fine(counterDT.toString());
			if (!counterDT.isCreateCounter() || !counterDT.isValid())
				return null;
			C_DocTypeTarget_ID = counterDT.getCounter_C_DocType_ID();
		}
		else	//	indirect
		{
			C_DocTypeTarget_ID = MDocTypeCounter.getCounterDocType_ID(getCtx(), getC_DocType_ID());
			log.fine("Indirect C_DocTypeTarget_ID=" + C_DocTypeTarget_ID);
			if (C_DocTypeTarget_ID <= 0)
				return null;
		}

		//	Deep Copy
		MInvoice counter = copyFrom(this, getDateInvoiced(),
			C_DocTypeTarget_ID, !isSOTrx(), true, get_TrxName(), true);
		//
		counter.setAD_Org_ID(counterAD_Org_ID);
	//	counter.setM_Warehouse_ID(counterOrgInfo.getM_Warehouse_ID());
		//
		counter.setBPartner(counterBP);
		//	Refernces (Should not be required
		counter.setSalesRep_ID(getSalesRep_ID());
		counter.save(get_TrxName());

		//	Update copied lines
		MInvoiceLine[] counterLines = counter.getLines(true);
		for (int i = 0; i < counterLines.length; i++)
		{
			MInvoiceLine counterLine = counterLines[i];
			counterLine.setClientOrg(counter);
			counterLine.setInvoice(counter);	//	copies header values (BP, etc.)
			counterLine.setPrice();
			counterLine.setTax();
			//
			counterLine.save(get_TrxName());
		}

		log.fine(counter.toString());

		//	Document Action
		if (counterDT != null)
		{
			if (counterDT.getDocAction() != null)
			{
				counter.setDocAction(counterDT.getDocAction());
				counter.processIt(counterDT.getDocAction());
				counter.save(get_TrxName());
			}
		}
		return counter;
	}	//	createCounterDoc

	/**
	 * 	Void Comprobantes Anulados.
	 * 	@return true if success
	 */
	public boolean anularComprobantesAsociados()
	{
		//	Desasociar L�neas de Orden de Venta
		MMatchPO[] mPO = MMatchPO.getInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
		for (int i = 0; i < mPO.length; i++)
			// Desasocir cada Match con la orden
			mPO[i].delete(true);

		/*
			 Anular Comprobante Asociado
			Se setea el estado anulado momentaneamente para que no entre en un ciclo infinito.
		*/
		String estadoant = getDocStatus();
		setDocStatus(DOCSTATUS_Voided);
		save(get_TrxName());
		MInvoice invoice = new MInvoice(getCtx(), getC_Invoice_Ref(), get_TrxName());
		if (!invoice.voidIt())
			return false;

		// Desasociar L�neas y Anular remito
		MMatchInv[] mInv = MMatchInv.getInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
		for (int i = 0; i < mInv.length; i++)
		{	//	Anular remito de cada linea asociada, generalmente es s�lo uno.
			MInOutLine inoutLine = new MInOutLine(getCtx(),mInv[i].getM_InOutLine_ID(),get_TrxName());
			MInOut inout = new MInOut(getCtx(),inoutLine.getM_InOut_ID(),get_TrxName());
			if (!inout.voidIt())
				return false;
			mInv[i].delete(true);
		}

		setDocStatus(estadoant);
		//	Desasociar Orden de Venta, decremento por l�nea la cantidad facturada
/*		for (int i = 0; i < getLines().length; i++)
		{	//	Anular remito de cada linea asociada, generalmente es s�lo uno.
			MInvoiceLine invoiceLine = getLines()[i];
			MOrderLine oLine = new MOrderLine(getCtx(),invoiceLine.getC_OrderLine_ID(),get_TrxName());
			oLine.setQtyInvoiced(oLine.getQtyInvoiced().subtract(invoiceLine.getQtyInvoiced()));
			invoiceLine.setC_OrderLine_ID(0);
			if (!invoiceLine.save(get_TrxName()) || !oLine.save(get_TrxName()))
				return false;
		}

		setC_Order_ID(0);
*/
		return true;
	}
	/**
	 * 	Void Document.
	 * 	@return true if success
	 */
	public boolean voidIt()
	{
		//	Temporal.. ver como se cambia
		if (DOCSTATUS_Voided.equals(getDocStatus()))
		{	return true;
		}

		log.info(toString());
		if (DOCSTATUS_Closed.equals(getDocStatus())
			|| DOCSTATUS_Reversed.equals(getDocStatus())
			|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
			setDocAction(DOCACTION_None);
			return false;
		}

		//	Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus())
			|| DOCSTATUS_Invalid.equals(getDocStatus())
			|| DOCSTATUS_InProgress.equals(getDocStatus())
			|| DOCSTATUS_Approved.equals(getDocStatus())
			|| DOCSTATUS_NotApproved.equals(getDocStatus()) )
		{
			//	Set lines to 0
			MInvoiceLine[] lines = getLines(false);
			for (int i = 0; i < lines.length; i++)
			{
				MInvoiceLine line = lines[i];
				BigDecimal old = line.getQtyInvoiced();
				if (old.compareTo(Env.ZERO) != 0)
				{
					line.setQty(Env.ZERO);
					line.setTaxAmt(Env.ZERO);
					line.setLineNetAmt(Env.ZERO);
					line.setLineTotalAmt(Env.ZERO);
					line.addDescription(Msg.getMsg(getCtx(), "Voided") + " (" + old + ")");
					//	Unlink Shipment
					if (line.getM_InOutLine_ID() != 0)
					{
						MInOutLine ioLine = new MInOutLine(getCtx(), line.getM_InOutLine_ID(), get_TrxName());
						ioLine.setIsInvoiced(false);
						ioLine.save(get_TrxName());
						line.setM_InOutLine_ID(0);
					}
					line.save(get_TrxName());
				}
			}
			addDescription(Msg.getMsg(getCtx(), "Voided"));
			setIsPaid(false);
			setC_Payment_ID(0);
		}
		else
		{
			if (verificarAnular())
			{
				if (isSOTrx())
				{
					if (getC_Order_ID()!=0)
					//�	Factura, Remito y Nota de Cr�dito deben ser anulados y se desasocia la Orden de venta.
					{
						JOptionPane.showMessageDialog(null,"Se anular� la Factura " + getDocumentNo() + ", asociada a la Orden de venta", "Info", JOptionPane.INFORMATION_MESSAGE);

						if (!anularComprobantesAsociados())
						{
							m_processMsg = "@Fallo el proceso de Anular/Revertir Comprobantes Asociados@";
							return false;
						}
					}
					else
					//�	Si la Factura fue creada desde un Remito el mismo debe ser desasociado, no anulado. Y ser� posible volver a crear una Factura a partir de �l mediante Crear desde.
					{
						//	No revertir, solo elimina los Match
						MMatchInv[] mInv = MMatchInv.getInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
						for (int i = 0; i < mInv.length; i++)
							mInv[i].delete(true);
					}
				} else {
                                    MMatchInv[] mInv = MMatchInv.getInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
                                    for (int i = 0; i < mInv.length; i++){
                                            mInv[i].setQty(Env.ZERO);
                                            mInv[i].save();
                                    }
                                }

				anularContabilidad();
				CleanUpReversed();
                                /*
                                 *  28/11/2012 Zynnia
                                 *  
                                 *  Si una factura tenia una OC asociada,pero no era una orden de Venta,
                                 *  no revertia la cantidad que tenia la OC facturada.
                                 * 
                                 */
                                
                                if (!revertirCantFacturada())
				{
					m_processMsg = "@Fallo el proceso de Actualizar la cantidad Facturada en la OC@";
					return false;
				}
                                
				// UpDate Business Partner
				MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
				BigDecimal actual = Env.ZERO;
                                MDocType docType = new MDocType(getCtx(),getC_DocTypeTarget_ID(),get_TrxName());
                                //TODO PASAR A CONSTANTE
				if(docType.getDocBaseType().equals("ARI") || docType.getDocBaseType().equals("APC"))
                                    actual = bp.getTotalOpenBalance().subtract(getGrandTotal());
                                else
                                    if(docType.getDocBaseType().equals("ARC") || docType.getDocBaseType().equals("API"))
                                        actual = bp.getTotalOpenBalance().add(getGrandTotal());

				bp.setTotalOpenBalance(actual);
                                bp.setSO_CreditUsed(actual);
				if (!bp.save(get_TrxName()))
					return false;

				setDocStatus(DOCSTATUS_Voided);
				setC_Payment_ID(0);
				setIsPaid(false);
			}
			else
				return false;
		}

		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt
        
        
        
        public boolean revertirCantFacturada(){
            //	Desasociar Orden de Venta, decremento por l�nea la cantidad facturada
		for (int i = 0; i < getLines().length; i++)
		{	//	Anular remito de cada linea asociada, generalmente es s�lo uno.
			MInvoiceLine invoiceLine = getLines()[i];
			MOrderLine oLine = new MOrderLine(getCtx(),invoiceLine.getC_OrderLine_ID(),get_TrxName());
			oLine.setQtyInvoiced(oLine.getQtyInvoiced().subtract(invoiceLine.getQtyInvoiced()));
			invoiceLine.setC_OrderLine_ID(0);
			if (!invoiceLine.save(get_TrxName()) || !oLine.save(get_TrxName()))
				return false;
		}
		setC_Order_ID(0);
                return true;
        }

	/**
	 * 	Close Document.
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		log.info(toString());
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correction - same date
	 * 	@return true if success
	 */
	public boolean reverseCorrectIt()
	{
		verificarAnular();

		if (isSOTrx())
		{
			//	No revertir, solo elimina los Match
			MMatchInv[] mInv = MMatchInv.getInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
			for (int i = 0; i < mInv.length; i++)
				mInv[i].delete(true);

			//Revertir Ordenes de Venta/Compra y remitos
			MMatchPO[] mPO = MMatchPO.getInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
			for (int i = 0; i < mPO.length; i++)
			{
				if (mPO[i].getM_InOutLine_ID() == 0)
					mPO[i].delete(true);
				else
				{
					mPO[i].setC_InvoiceLine_ID(null);
					mPO[i].save(get_TrxName());
				}
			}
		}

		load(get_TrxName());	//	reload allocation reversal info

		MDocType docType = new MDocType(getCtx(),getC_DocType_ID(),get_TrxName());

		//	Deep Copy
		int docType_ID = getC_DocType_ID();
		if (docType.getC_RevDocType_ID()!=0)
			docType_ID = docType.getC_RevDocType_ID();

		MInvoice reversal = copyFrom (this, getDateInvoiced(),
			docType_ID, isSOTrx(), false, get_TrxName(), true);
		if (reversal == null)
		{
			m_processMsg = "Could not create Invoice Reversal";
			return false;
		}
		reversal.setReversal(true);
		reversal.setDateAcct(getDateAcct());

		//	Reverse Line Qty
		MInvoiceLine[] rLines = reversal.getLines(false);
		for (int i = 0; i < rLines.length; i++)
		{
			MInvoiceLine rLine = rLines[i];
			rLine.setQtyEntered(rLine.getQtyEntered().negate());
			rLine.setQtyInvoiced(rLine.getQtyInvoiced().negate());
			rLine.setLineNetAmt(rLine.getLineNetAmt().negate());
			if (rLine.getTaxAmt() != null && rLine.getTaxAmt().compareTo(Env.ZERO) != 0)
				rLine.setTaxAmt(rLine.getTaxAmt().negate());
			if (rLine.getLineTotalAmt() != null && rLine.getLineTotalAmt().compareTo(Env.ZERO) != 0)
				rLine.setLineTotalAmt(rLine.getLineTotalAmt().negate());
			if (!rLine.save(get_TrxName()))
			{
				m_processMsg = "Could not correct Invoice Reversal Line";
				return false;
			}
		}

		reversal.setC_Order_ID(getC_Order_ID());
		reversal.addDescription("{ "+ docType.getName() +" ->" + getDocumentNo() + ")");

		if (!isSOTrx())
		{
			List percepciones = getPercepciones();
			for (int j = 0; j < percepciones.size(); j++)
			{
				MINVOICEPERCEP invPerc = MINVOICEPERCEP.copyFrom(getCtx(),(MINVOICEPERCEP)percepciones.get(j),reversal.getC_Invoice_ID(),get_TrxName());
				invPerc.setAMOUNT(invPerc.getAMOUNT().negate());
				invPerc.save(get_TrxName());
			}
			//Crear percepciones
		}
		else
		{
			reversal.setPercepcionIB(getPercepcionIB().negate());
			reversal.setPercepcionIVA(getPercepcionIVA().negate());
		}

		reversal.setPosted(true);

		//
		if (!reversal.processIt(DocAction.ACTION_Complete))
		{
			m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
			return false;
		}
		reversal.setC_Payment_ID(0);
		reversal.setIsPaid(true);

		reversal.closeIt();
		reversal.setDocStatus(DOCSTATUS_Reversed);
		reversal.setDocAction(DOCACTION_None);
		reversal.save(get_TrxName());
		m_processMsg = reversal.getDocumentNo();

		addDescription("(" + reversal.getDocumentNo() + "<-)");

		CleanUpReversed();

		setProcessed(true);
		setDocStatus(DOCSTATUS_Voided);	//	may come from void
		setDocAction(DOCACTION_None);
		setC_Payment_ID(0);
		setIsPaid(true);
		return true;
	}	//	reverseCorrectIt

	/**
	 * 	Reverse Accrual - none
	 * 	@return false
	 */
	public boolean reverseAccrualIt()
	{
		log.info(toString());
		return false;
	}	//	reverseAccrualIt

	/**
	 * 	Re-activate
	 * 	@return false
	 */
	public boolean reActivateIt() {
            
            if (!isSOTrx() && verificarAnular()) {
                // Desasociar L�neas de Orden de Venta
                MMatchPO[] mPO = MMatchPO.getInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
                for (int i = 0; i < mPO.length; i++){                    
                        /*
                         *  Zynnia 15/01/2013
                         *  Desasociar la factura con MatchPO que no es lo mismo que eliminar el registro
                         *  JF
                         * 
                         */
		

                        //Si MATCH PO tiene asociada una factura
                        if (mPO[i].getC_InvoiceLine_ID() != 0){
                                /*
                                 * Si Ademas tiene asociada una recepcion de materiales,
                                 * Entonces modifico match PO para que no este asociada a la factura
                                 * y disminuyo la cantidad facturada de la linea de la OC
                                 * igual a lo que indica MATCH PO
                                 */
                                if (mPO[i].getM_InOutLine_ID() != 0){
                                    MOrderLine orderLine = new MOrderLine (getCtx(), mPO[i].getC_OrderLine_ID(), get_TrxName());
                                    //MInvoiceLine invoiceLine = new MInvoiceLine (getCtx(), mPO[i].getC_InvoiceLine_ID(), get_TrxName());
                                    orderLine.setQtyInvoiced(orderLine.getQtyInvoiced().subtract(mPO[i].getQty()));
                                    if (!orderLine.save(get_TrxName()))
                                        return false;
                                     mPO[i].setC_InvoiceLine_ID(null);
                                    if (!mPO[i].save(get_TrxName()))
                                         return false;
                                }
                                /*
                                 * Si no tiene asociada una recepcion de materiales entonces la borro
                                 * Al borrarse el Match PO, se modifica automaticamente la linea de la OC
                                 * asociada
                                 */

                                else
                                     if (! mPO[i].delete(true,get_TrxName()) )
                                         return false;
                        }
                }

                //	Desasociar L�neas de Recibo de Material
                MMatchInv[] mInv = MMatchInv.getInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
                for (int i = 0; i < mInv.length; i++)
                        //	Desasocir cada Match con el recibo
                        mInv[i].delete(true);

                anularContabilidad();
                CleanUpReversed();

                // UpDate Business Partner
                MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
                BigDecimal actual = Env.ZERO;
                MDocType docType = new MDocType(getCtx(),getC_DocTypeTarget_ID(),get_TrxName());
                //TODO PASAR A CONSTANTE
                if(docType.getDocBaseType().equals("ARI") || docType.getDocBaseType().equals("APC"))
                    actual = bp.getTotalOpenBalance().subtract(getGrandTotal());
                else
                    if(docType.getDocBaseType().equals("ARC") || docType.getDocBaseType().equals("API"))
                        actual = bp.getTotalOpenBalance().add(getGrandTotal());

                bp.setTotalOpenBalance(actual);
                bp.setSO_CreditUsed(actual);
                if (!bp.save(get_TrxName()))
                        return false;
            } else	// Si es Factura Cliente o no es V�lida la Reactivacion
                    return false;

            if (getC_Project_ID()!=0) {
                MProject project = new MProject (getCtx(), getC_Project_ID(), get_TrxName());
                project.setInvoicedAmt(getGrandTotal(true).negate());
                project.save(get_TrxName());
            }

            log.info(toString());

            setDocStatus(DOCSTATUS_Drafted);
            setDocAction(DOCACTION_Complete);
            setIsPaid(false);
            setProcessed(false);
            setPosted(false);
            setIsApproved(false);
            return true;
                
	}	//	reActivateIt


	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		//	: Grand Total = 123.00 (#1)
		sb.append(": ").
			append(Msg.translate(getCtx(),"GrandTotal")).append("=").append(getGrandTotal())
			.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg

	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getSalesRep_ID();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return getGrandTotal();
	}	//	getApprovalAmt

    void setFlagSave(boolean val) {
        this.flagSave = val;
    }

    public boolean getFlagSave() {
        return this.flagSave;
    }

    /**
     *      @author Daniel-Gini - 02/06/2009
     *
     *      Modificado para actualizar el saldo del cliente con las
     *      percepciones incluidas. Incluye la inversa de facturas
     *
     *      @return bp.save
     *
     */
    public boolean updateBP(int dT)
    {
    	MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());

    	if (getC_BPartner_ID() != 0)
		{
			BigDecimal previo = bp.getTotalOpenBalance();

            //int dT = this.getC_DocTypeTarget_ID();
            BigDecimal actual = Env.ZERO;

            MDocType docType = new MDocType(getCtx(), dT, get_TrxName());
            BigDecimal gt =  getGrandTotal();

    		//TODO PASAR A CONSTANTE
            // Por facturas y ND de cliente o por NC de proveedor, suma al saldo
            if(docType.getDocBaseType().equals("ARI") || docType.getDocBaseType().equals("APC"))
                 actual = previo.add(gt);
            else	// Por NC de cliente o facturas y ND de proveedor por resta al saldo
            	if(docType.getDocBaseType().equals("ARC") || docType.getDocBaseType().equals("API"))
            		actual = previo.subtract(gt);
            	else
            	{	bp.setTotalOpenBalance();
            		if(docType.getDocBaseType().equals("RRI"))
            		{
            			setGrandTotal(getGrandTotal().add(getPercepcionIB()).add(getPercepcionIVA()));
                		actual = previo.add(getGrandTotal());
            		}
            		else
            		{	if(docType.getDocBaseType().equals("RRC"))
            			{
            				setGrandTotal(getGrandTotal().add(getPercepcionIB()).add(getPercepcionIVA()));
                    		actual = previo.subtract(getGrandTotal());
            			}
            			else
            			{	setGrandTotal(getGrandTotal().add(getAmountPercepcion()));
            				if (docType.getDocBaseType().equals("RPC"))
                        		actual = previo.add(getGrandTotal());
            				else
            					if(docType.getDocBaseType().equals("RPI"))
            						actual = previo.subtract(getGrandTotal());
            			}
            		}
            	}

          	bp.setTotalOpenBalance(actual);
           	bp.setSO_CreditUsed(actual);
		}

		//bp.setSOCreditStatus();

		if (!bp.save())
			return false;

		return true;
    }

    public List getPercepciones()
    {
    	List<MINVOICEPERCEP> list = new ArrayList<MINVOICEPERCEP>();

    	String sql = "SELECT C_InvoicePercep_ID "
			+ " FROM C_InvoicePercep "
			+ " WHERE C_Invoice_ID=" + getC_Invoice_ID();

	    try
	    {
	    	PreparedStatement pstm = DB.prepareStatement(sql, get_TrxName());
		    ResultSet rs = pstm.executeQuery();

		    while (rs.next())
		    {
		    	MINVOICEPERCEP percep = new MINVOICEPERCEP(getCtx(),rs.getInt(1),get_TrxName());
		    	list.add(percep);
		    }
	    }
	    catch (SQLException e){}

	    return list;
    }

    public BigDecimal getAmountPercepcion()
    {
    	BigDecimal amount = BigDecimal.ZERO;
    	List list = getPercepciones();

    	for (int i = 0; i< list.size(); i++)
    		amount = amount.add(((MINVOICEPERCEP)list.get(i)).getAMOUNT());

	    return amount;
    }

    public List getContabilidad()
    {
    	List<MFactAcct> list = new ArrayList<MFactAcct>();

    	String sql = "SELECT Fact_Acct_ID "
			+ " FROM Fact_Acct "
			+ " WHERE Record_ID=? AND AD_Table_ID=? ";

	    try
	    {
	    	PreparedStatement pstm = DB.prepareStatement(sql, get_TrxName());
	    	pstm.setInt(1,getC_Invoice_ID());
	    	pstm.setInt(2,get_Table_ID());
	    	//int C_AcctSchema_ID = Env.getContextAsInt(getCtx(), "C_AcctSchema_ID");
	    	//pstm.setInt(3,10);

		    ResultSet rs = pstm.executeQuery();

		    while (rs.next())
		    {
		    	MFactAcct cont = new MFactAcct(getCtx(),rs.getInt(1),get_TrxName());
		    	list.add(cont);
		    }
	    }
	    catch (SQLException e){}

	    return list;
    }

    protected boolean verificarAnular()	{
		log.info(toString());
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType()))
		{
			m_processMsg = "@PeriodClosed@";
			setDocAction(DOCACTION_Close);
			return false;
		}
		//
		MAllocationHdr[] allocations = MAllocationHdr.getOfInvoice(getCtx(),
			getC_Invoice_ID(), get_TrxName());

		if (allocations.length > 0)
		{
			m_processMsg = "@SetAllocation@";
			JOptionPane.showMessageDialog(null,"No es posible reactivar/anular comprobantes que se encuentran asignados", "Problemas al Reactivar", JOptionPane.INFORMATION_MESSAGE);
                        setDocAction(DOCACTION_Close);
			return false;
		}

		return true;
    }

    protected void anularContabilidad()	{
    	//	Acct Reversed (this)
		List fAcct = getContabilidad();
		for (int i = 0; i < fAcct.size(); i++)
			((MFactAcct)fAcct.get(i)).delete(true);
    }

    protected void CleanUpReversed(){
        // Clean up Reversed (this)
    	MInvoiceLine[] iLines = getLines(false);
        for (int i = 0; i < iLines.length; i++) {
            MInvoiceLine iLine = iLines[i];
            if (iLine.getM_InOutLine_ID() != 0) {
                MInOutLine ioLine = new MInOutLine(getCtx(), iLine.getM_InOutLine_ID(), get_TrxName());
                ioLine.setIsInvoiced(false);
                ioLine.save(get_TrxName());
                // Reconsiliation
                iLine.setM_InOutLine_ID(0);
                iLine.save(get_TrxName());
            }
        }
    }
    
}	//	MInvoice
