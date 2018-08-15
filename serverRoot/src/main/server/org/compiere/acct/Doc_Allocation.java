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
 *  Post Allocation Documents.
 *  <pre>
 *  Table:              C_AllocationHdr
 *  Document Types:     CMA
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Allocation.java,v 1.17 2005/10/28 01:00:53 jjanke Exp $
 */
public class Doc_Allocation extends Doc {

    /**
     *  Constructor
     * 	@param ass accounting schemata
     * 	@param rs record
     * 	@parem trxName trx
     */
    protected Doc_Allocation(MAcctSchema[] ass, ResultSet rs, String trxName) {
        super(ass, MAllocationHdr.class, rs, DOCTYPE_Allocation, trxName);
    }   //  Doc_Allocation
    /**	Tolearance G&L				*/
    private static final BigDecimal TOLERANCE = new BigDecimal(0.02);
    /** Facts						*/
    private ArrayList<Fact> m_facts = null;
    // Begin e-Evolution ogi-cd
    boolean _gainloss = false;
    BigDecimal taxAccount = Env.ZERO;
    BigDecimal taxAccounted = Env.ZERO;
    BigDecimal taxAcct = Env.ZERO;
    BigDecimal taxDifference = Env.ZERO;
    BigDecimal invoiceAccTax = Env.ZERO;
    BigDecimal acctDifference = Env.ZERO;
    BigDecimal invoiceAccountedTax = Env.ZERO;
    private DocTax[] m_taxes = null;
    // End e-Evolution ogi-cd

    /**
     *  Load Specific Document Details
     *  @return error message or null
     */
    protected String loadDocumentDetails() {
        MAllocationHdr alloc = (MAllocationHdr) getPO();
        setDateDoc(alloc.getDateTrx());
        //	Contained Objects
        p_lines = loadLines(alloc);
        return null;
    }   //  loadDocumentDetails

    /**
     *	Load Invoice Line
     *  @return DocLine Array
     */
    private DocLine[] loadLines(MAllocationHdr alloc) {
        ArrayList<DocLine> list = new ArrayList<DocLine>();
        MAllocationLine[] lines = alloc.getLines(false);
        for (int i = 0; i < lines.length; i++) {
            MAllocationLine line = lines[i];
            DocLine_Allocation docLine = new DocLine_Allocation(line, this);

            //	Get Payment Conversion Rate
            if (line.getC_Payment_ID() != 0) {
                MPayment payment = new MPayment(getCtx(), line.getC_Payment_ID(), getTrxName());
                int C_ConversionType_ID = payment.getC_ConversionType_ID();
                docLine.setC_ConversionType_ID(C_ConversionType_ID);
            }
            //
            log.fine(docLine.toString());
            list.add(docLine);
        }

        //	Return Array
        DocLine[] dls = new DocLine[list.size()];
        list.toArray(dls);
        return dls;
    }	//	loadLines

    /**************************************************************************
     *  Get Source Currency Balance - subtracts line and tax amounts from total - no rounding
     *  @return positive amount, if total invoice is bigger than lines
     */
    public BigDecimal getBalance() {
        BigDecimal retValue = Env.ZERO;
        return retValue;
    }   //  getBalance

    /**
     *  Create Facts (the accounting logic) for
     *  CMA.
     *  <pre>
     *  AR_Invoice_Payment
     *      UnAllocatedCash DR
     *      or C_Prepayment
     *      DiscountExp     DR
     *      WriteOff        DR
     *      Receivables             CR
     *  AR_Invoice_Cash
     *      CashTransfer    DR
     *      DiscountExp     DR
     *      WriteOff        DR
     *      Receivables             CR
     * 
     *  AP_Invoice_Payment
     *      Liability       DR
     *      DiscountRev             CR
     *      WriteOff                CR
     *      PaymentSelect           CR
     *      or V_Prepayment
     *  AP_Invoice_Cash
     *      Liability       DR
     *      DiscountRev             CR
     *      WriteOff                CR
     *      CashTransfer            CR
     *  CashBankTransfer
     *      -
     *  ==============================
     *  Realized Gain & Loss
     * 		AR/AP			DR		CR
     * 		Realized G/L	DR		CR
     * 
     *
     *  </pre>
     *  Tax needs to be corrected for discount & write-off;
     *  Currency gain & loss is realized here.
     *  @param as accounting schema
     *  @return Fact
     */
    public ArrayList<Fact> createFacts(MAcctSchema as) {
        m_facts = new ArrayList<Fact>();

        //  create Fact Header
        Fact fact = new Fact(this, as, Fact.POST_Actual);

        for (int i = 0; i < p_lines.length; i++) {
            DocLine_Allocation line = (DocLine_Allocation) p_lines[i];
            setC_BPartner_ID(line.getC_BPartner_ID());

            //  CashBankTransfer - all references null and Discount/WriteOff = 0
            if (line.getC_Payment_ID() != 0
                    && line.getC_Invoice_ID() == 0 && line.getC_Order_ID() == 0
                    && line.getC_CashLine_ID() == 0 && line.getC_BPartner_ID() == 0
                    && Env.ZERO.compareTo(line.getDiscountAmt()) == 0
                    && Env.ZERO.compareTo(line.getWriteOffAmt()) == 0) {
                continue;
            }

            //	Receivables/Liability Amt
            BigDecimal allocationSource = line.getAmtSource() // e-Evolution (solo indicar que antes el nombre era invoiceAmt)
                    .add(line.getDiscountAmt()).add(line.getWriteOffAmt());
            BigDecimal allocationAccounted = null;	// AR/AP balance corrected

            FactLine fl = null;
            MAccount bpAcct = null;		//	Liability/Receivables
            //
            MPayment payment = null;
            if (line.getC_Payment_ID() != 0) {
                payment = new MPayment(getCtx(), line.getC_Payment_ID(), getTrxName());
            }
            MInvoice invoice = null;
            if (line.getC_Invoice_ID() != 0) {
                invoice = new MInvoice(getCtx(), line.getC_Invoice_ID(), null);
            }

            //	No Invoice
            if (invoice == null) {
                //	Payment Only
                if (line.getC_Invoice_ID() == 0 && line.getC_Payment_ID() != 0) {
                    fl = fact.createLine(line, getPaymentAcct(as, line.getC_Payment_ID()),
                            getPaymentRate(payment), getC_Currency_ID(), line.getAmtSource(), null);
                    if (fl != null && payment != null) {
                        fl.setAD_Org_ID(payment.getAD_Org_ID());
                    }
                } else {
                    p_Error = "Cannot determine SO/PO";
                    log.log(Level.SEVERE, p_Error);
                    return null;
                }
            } //	Sales Invoice	
            else if (invoice.isSOTrx()) {
                //	Payment/Cash	DR
                if (line.getC_Payment_ID() != 0) {
                    /**
                     * 	Agregado por DANIEL GINI
                     * 
                     * 		Modifica contabilidad en Cobranza
                     */
                    //fl = fact.createLine (line, getAccount (Doc.ACCTTYPE_C_Prepayment, as), //getPaymentAcct(as, line.getC_Payment_ID()),
                    //	getC_Currency_ID(), line.getAmtSource(), null);
					/*
                     * Modificado para contemplar la cotizacion de la cobranza y no la del sistema
                     */
                    //TODO Cambiar getCotizacionCobranza, por getCotizacion de MPayment
                    fl = fact.createLine(line, getAccount(Doc.ACCTTYPE_C_Prepayment, as), //getPaymentAcct(as, line.getC_Payment_ID()),
                            getPaymentRate(payment), getC_Currency_ID(), line.getAmtSource(), null);
                    if (fl != null && payment != null) {
                        fl.setAD_Org_ID(payment.getAD_Org_ID());
                    }
                } else if (line.getC_CashLine_ID() != 0) {
                    fl = fact.createLine(line, getCashAcct(as, line.getC_CashLine_ID()),
                            getC_Currency_ID(), line.getAmtSource(), null);
                    MCashLine cashLine = new MCashLine(getCtx(), line.getC_CashLine_ID(), getTrxName());
                    if (fl != null && cashLine.get_ID() != 0) {
                        fl.setAD_Org_ID(cashLine.getAD_Org_ID());
                    }
                }
                //	Discount		DR
                if (Env.ZERO.compareTo(line.getDiscountAmt()) != 0) {
                    fl = fact.createLine(line, getAccount(Doc.ACCTTYPE_DiscountExp, as),
                            getPaymentRate(payment), getC_Currency_ID(), line.getDiscountAmt(), null);
                    if (fl != null && payment != null) {
                        fl.setAD_Org_ID(payment.getAD_Org_ID());
                    }
                }
                //	Write off		DR
                if (Env.ZERO.compareTo(line.getWriteOffAmt()) != 0) {
                    fl = fact.createLine(line, getAccount(Doc.ACCTTYPE_WriteOff, as),
                            getPaymentRate(payment), getC_Currency_ID(), line.getWriteOffAmt(), null);
                    if (fl != null && payment != null) {
                        fl.setAD_Org_ID(payment.getAD_Org_ID());
                    }
                }

                //	AR Invoice Amount	CR
                if (as.isAccrual()) {
                    if (as.getC_Currency_ID() != getC_Currency_ID()) {
                        bpAcct = getAccount(Doc.ACCTTYPE_C_MExterno, as);
                    } else {
                        bpAcct = getAccount(Doc.ACCTTYPE_C_Receivable, as);
                    }

                    fl = fact.createLine(line, bpAcct,
                            getInvoiceRate(invoice), getC_Currency_ID(), null, allocationSource);		//	payment currency 
                    if (fl != null) {
                        allocationAccounted = fl.getAcctBalance().negate();
                    }
                    if (fl != null && invoice != null) {
                        fl.setAD_Org_ID(invoice.getAD_Org_ID());
                    }
                } else //	Cash Based
                {
                    allocationAccounted = createCashBasedAcct(as, fact,
                            invoice, allocationSource);
                }
            } //	Purchase Invoice
            else {
                allocationSource = allocationSource.negate();	//	allocation is negative
                //	AP Invoice Amount	DR
                if (as.isAccrual()) {
                    if (as.getC_Currency_ID() != getC_Currency_ID()) {
                        bpAcct = getAccount(Doc.ACCTTYPE_V_MExterno, as);
                    } else {
                        bpAcct = getAccount(Doc.ACCTTYPE_V_Liability, as);
                    }
                    fl = fact.createLine(line, bpAcct,
                            getInvoiceRate(invoice), getC_Currency_ID(), allocationSource, null);		//	payment currency
                    if (fl != null) {
                        allocationAccounted = fl.getAcctBalance();
                    }
                    if (fl != null && invoice != null) {
                        fl.setAD_Org_ID(invoice.getAD_Org_ID());
                    }
                } else //	Cash Based
                {
                    allocationAccounted = createCashBasedAcct(as, fact,
                            invoice, allocationSource);
                }

                //	Discount		CR
                if (Env.ZERO.compareTo(line.getDiscountAmt()) != 0) {
                    fl = fact.createLine(line, getAccount(Doc.ACCTTYPE_DiscountRev, as),
                            getPaymentRate(payment), getC_Currency_ID(), null, line.getDiscountAmt().negate());
                    if (fl != null && payment != null) {
                        fl.setAD_Org_ID(payment.getAD_Org_ID());
                    }
                }
                //	Write off		CR
                if (Env.ZERO.compareTo(line.getWriteOffAmt()) != 0) {
                    fl = fact.createLine(line, getAccount(Doc.ACCTTYPE_WriteOff, as),
                            getPaymentRate(payment), getC_Currency_ID(), null, line.getWriteOffAmt().negate());
                    if (fl != null && payment != null) {
                        fl.setAD_Org_ID(payment.getAD_Org_ID());
                    }
                }
                //	Payment/Cash	CR
                if (line.getC_Payment_ID() != 0) {
                    /**
                     * 
                     * 	Agregado por DANIEL GINI
                     * 
                     * 		Modifica contabilidad en Pago
                     */
                    fl = fact.createLine(line, getAccount(Doc.ACCTTYPE_V_Prepayment, as), //getPaymentAcct(as, line.getC_Payment_ID()),
                            getPaymentRate(payment), getC_Currency_ID(), null, line.getAmtSource().negate());
                    if (fl != null && payment != null) {
                        fl.setAD_Org_ID(payment.getAD_Org_ID());
                    }
                } else if (line.getC_CashLine_ID() != 0) {
                    fl = fact.createLine(line, getCashAcct(as, line.getC_CashLine_ID()),
                            getC_Currency_ID(), null, line.getAmtSource().negate());
                    MCashLine cashLine = new MCashLine(getCtx(), line.getC_CashLine_ID(), getTrxName());
                    if (fl != null && cashLine.get_ID() != 0) {
                        fl.setAD_Org_ID(cashLine.getAD_Org_ID());
                    }
                }
            }

            //	VAT Tax Correction
            if (as.isDiscountCorrectsTax() && invoice != null) {
                BigDecimal taxCorrectionAmt = line.getDiscountAmt().add(line.getWriteOffAmt());
                if (as.isDiscountCorrectsTax() && Env.ZERO.compareTo(taxCorrectionAmt) != 0) {
                    if (!createTaxCorrection(as, fact, line,
                            getAccount(invoice.isSOTrx() ? Doc.ACCTTYPE_DiscountExp : Doc.ACCTTYPE_DiscountRev, as),
                            getAccount(Doc.ACCTTYPE_WriteOff, as))) {
                        p_Error = "Cannot create Tax correction";
                        return null;
                    }
                }
            }

            /* 	DANIEL GINI
             * 	
             * 	REQ-065: La conversion ahora se realiza con la tasa de cobranza.
             */
            if (payment != null && payment.isReceipt()) //TODO Cambiar getCotizacionCobranza, por getCotizacion de MPayment
            {
                allocationAccounted = allocationSource.multiply(getPaymentRate(payment));
            } else {
                allocationAccounted = MConversionRate.convert(getCtx(), invoice.getGrandTotal(), getC_Currency_ID(), as.getC_Currency_ID(),
                        getDateAcct(), getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());
            }

            //	Realized Gain & Loss
            if (invoice != null
                    && (getC_Currency_ID() != as.getC_Currency_ID() //	payment allocation in foreign currency
                    || getC_Currency_ID() != line.getInvoiceC_Currency_ID())) //	allocation <> invoice currency
            {
                p_Error = createRealizedGainLoss(as, fact, bpAcct, invoice,
                        allocationSource, allocationAccounted);
                if (p_Error != null) {
                    return null;
                }
            }


            // Begin e-Evolution ogi-cd 07/Oct/2004 --------------------------------------------------------------------------
            if (invoice.getC_CashLine_ID() != 0 || invoice.getC_Payment_ID() != 0) // 31052006 solo aplica para pagos/cobros
            {
                if (invoice != null
                        && (getC_Currency_ID() != as.getC_Currency_ID() //	payment allocation in foreign currency
                        || getC_Currency_ID() != line.getInvoiceC_Currency_ID())) //	invoice <> payment currency
                {
                    _gainloss = true;
                }

                int moneda = line.getInvoiceC_Currency_ID();

                MAccount gain = MAccount.get(as.getCtx(), as.getAcctSchemaDefault().getRealizedGain_Acct(moneda));
                MAccount loss = MAccount.get(as.getCtx(), as.getAcctSchemaDefault().getRealizedLoss_Acct(moneda));

                if (invoice.getC_CashLine_ID() <= 0) // veririca si es con diario de efectivo el pago
                {
                    m_taxes = loadTaxes(line.getC_Invoice_ID(), payment.getAllocatedAmt());
                } else {
                    m_taxes = loadTaxes(line.getC_Invoice_ID(), invoice.getGrandTotal());
                }

                for (int o = 0; o < m_taxes.length; o++) {
                    //BigDecimal taxAccount   = allocationAccounted.multiply(m_taxes[o].getRate());
                    BigDecimal taxAccounted = taxAccount.divide(Env.ONEHUNDRED, 5, 4);

                    BigDecimal taxInvoice = BigDecimal.ZERO;
                    if (invoice != null) {
                        taxInvoice = getInvoiceRate(invoice);
                    } else {
                        taxInvoice = getPaymentRate(payment);
                    }

                    BigDecimal taxPayment = BigDecimal.ZERO;
                    if (payment != null) {
                        taxPayment = getPaymentRate(payment);
                    } else //Si no hay pago tomo el de la misma factura
                    {
                        taxPayment = getInvoiceRate(invoice);
                    }

                    //
                    if (invoice.isSOTrx()) // is Sales Transaction
                    {
                        if (m_taxes[o].getAmount().compareTo(Env.ZERO) != 0) {
                            //taxDifference = taxAcct.subtract(differenceTax (as, invoice.isSOTrx(), line.getC_Invoice_ID(), 
                            //                m_taxes[o].getAccount(DocTax.ACCTTYPE_TaxDue, as).getAccount_ID()) );
                            //
                            if (_gainloss) // calculate gain-loss for tax
                            {
                                acctDifference = createRealizedGainLossTax(as, fact, // calculation
                                        m_taxes[o].getAccount(DocTax.ACCTTYPE_TaxDue, as),
                                        invoice.isSOTrx(), line.getC_Invoice_ID(), m_taxes[o].getAmount(),
                                        m_taxes[o].getAmount(), allocationSource.subtract(payment.getAllocatedAmt()),
                                        m_taxes[o].getAccount(DocTax.ACCTTYPE_TaxDue, as).getAccount_ID(),
                                        taxInvoice, taxPayment);
                                //
                                fact.createLine(null, m_taxes[o].getAccount(DocTax.ACCTTYPE_TaxDue, as),
                                        as.getC_Currency_ID(), taxInvoice, null);
                                fact.createLine(null, m_taxes[o].getAccount(DocTax.ACCTTYPE_TaxLiability, as),
                                        as.getC_Currency_ID(), null, taxPayment);
                                fact.createLine(null, loss, gain,
                                        as.getC_Currency_ID(), acctDifference);
                            } else // currency schema
                            {
                                fact.createLine(null, m_taxes[o].getAccount(DocTax.ACCTTYPE_TaxDue, as),
                                        getInvoiceRate(invoice), getC_Currency_ID(), null, m_taxes[o].getAmount());
                                fact.createLine(null, m_taxes[o].getAccount(DocTax.ACCTTYPE_TaxLiability, as),
                                        getInvoiceRate(invoice), getC_Currency_ID(), m_taxes[o].getAmount(), null);
                            }
                        }
                    } else // is PO transaction
                    {
                        if (m_taxes[o].getAmount().compareTo(Env.ZERO) != 0) {
                            if (_gainloss) // calculate gain-loss for tax
                            {
                                acctDifference = createRealizedGainLossTax(as, fact,
                                        m_taxes[o].getAccount(DocTax.ACCTTYPE_TaxCredit, as),
                                        invoice.isSOTrx(),
                                        line.getC_Invoice_ID(), m_taxes[o].getAmount(),
                                        m_taxes[o].getAmount(), invoice.getGrandTotal().subtract(payment.getAllocatedAmt().abs()),
                                        m_taxes[o].getAccount(DocTax.ACCTTYPE_TaxCredit, as).getAccount_ID(),
                                        taxInvoice, taxPayment);
                                //
                                fact.createLine(null, m_taxes[o].getAccount(DocTax.ACCTTYPE_TaxCredit, as),
                                        as.getC_Currency_ID(), null, taxInvoice);
                                fact.createLine(null, m_taxes[o].getAccount(DocTax.ACCTTYPE_TaxReceivables, as),
                                        as.getC_Currency_ID(), taxPayment, null);
                                fact.createLine(null, loss, gain,
                                        as.getC_Currency_ID(), acctDifference.negate());
                            } else // currency schema
                            {
                                fact.createLine(null, m_taxes[o].getAccount(DocTax.ACCTTYPE_TaxCredit, as),
                                        getInvoiceRate(invoice), getC_Currency_ID(), null, m_taxes[o].getAmount());
                                fact.createLine(null, m_taxes[o].getAccount(DocTax.ACCTTYPE_TaxReceivables, as),
                                        getInvoiceRate(invoice), getC_Currency_ID(), m_taxes[o].getAmount(), null);
                            }
                        }
                    }
                }
            } // End of not CashLine or Payment
            // End e-Evolution ogi-cd 07/Oct/2004 ----------------------------------------------------------------




        }	//	for all lines

        //	reset line info
        setC_BPartner_ID(0);

        //	REQ-065 y GEN-007: Se reacomodan las l�neas, primero al debe luego al haber.
        fact = fact.reordenarFactLine();

        m_facts.add(fact);
        return m_facts;
    }   //  createFact

    /**
     * 	Create Cash Based Acct
     *	@param fact fact
     *	@param invoice invoice
     *	@param allocationSource allocation amount (incl discount, writeoff)
     *	@return Accounted Amt
     */
    private BigDecimal createCashBasedAcct(MAcctSchema as, Fact fact, MInvoice invoice,
            BigDecimal allocationSource) {
        BigDecimal allocationAccounted = Env.ZERO;
        //	Multiplier
        double percent = invoice.getGrandTotal().doubleValue() / allocationSource.doubleValue();
        if (percent > 0.99 && percent < 1.01) {
            percent = 1.0;
        }
        log.config("Multiplier=" + percent + " - GrandTotal=" + invoice.getGrandTotal()
                + " - Allocation Source=" + allocationSource);

        //	Get Invoice Postings
        //TODO REFERENCIA ESTATICA 
        Doc_Invoice docInvoice = (Doc_Invoice) Doc.get(new MAcctSchema[]{as},
                MInvoice.getTableId(MInvoice.Table_Name), invoice.getC_Invoice_ID(), getTrxName());
        //Doc_Invoice docInvoice = (Doc_Invoice)Doc.get(new MAcctSchema[]{as}, 
        //		318, invoice.getC_Invoice_ID(), getTrxName());

        docInvoice.loadDocumentDetails();
        allocationAccounted = docInvoice.createFactCash(as, fact, new BigDecimal(percent));
        log.config("Allocation Accounted=" + allocationAccounted);

        //	Cash Based Commitment Release 
        if (as.isCreateCommitment() && !invoice.isSOTrx()) {
            MInvoiceLine[] lines = invoice.getLines();
            for (int i = 0; i < lines.length; i++) {
                Fact factC = Doc_Order.getCommitmentRelease(as, this,
                        lines[i].getQtyInvoiced(), lines[i].getC_InvoiceLine_ID(), new BigDecimal(percent));
                if (factC == null) {
                    return null;
                }
                m_facts.add(factC);
            }
        }	//	Commitment

        return allocationAccounted;
    }	//	createCashBasedAcct

    /**
     * 	Get Payment (Unallocated Payment or Payment Selection) Acct of Bank Account
     *	@param as accounting schema
     *	@param C_Payment_ID payment
     *	@return acct
     */
    private MAccount getPaymentAcct(MAcctSchema as, int C_Payment_ID) {
        setC_BankAccount_ID(0);
        //	Doc.ACCTTYPE_UnallocatedCash (AR) or C_Prepayment 
        //	or Doc.ACCTTYPE_PaymentSelect (AP) or V_Prepayment
        int accountType = Doc.ACCTTYPE_UnallocatedCash;
        //
        String sql = "SELECT p.C_BankAccount_ID, d.DocBaseType, p.IsReceipt, p.IsPrepayment "
                + "FROM C_Payment p INNER JOIN C_DocType d ON (p.C_DocType_ID=d.C_DocType_ID) "
                + "WHERE C_Payment_ID=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, getTrxName());
            pstmt.setInt(1, C_Payment_ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                setC_BankAccount_ID(rs.getInt(1));
                if (DOCTYPE_APPayment.equals(rs.getString(2))) {
                    accountType = Doc.ACCTTYPE_PaymentSelect;
                }
                //	Prepayment
                if ("Y".equals(rs.getString(4))) //	Prepayment
                {
                    if ("Y".equals(rs.getString(3))) //	Receipt
                    {
                        accountType = Doc.ACCTTYPE_C_Prepayment;
                    } else {
                        accountType = Doc.ACCTTYPE_V_Prepayment;
                    }
                }
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }

        //
        if (getC_BankAccount_ID() <= 0) {
            log.log(Level.SEVERE, "NONE for C_Payment_ID=" + C_Payment_ID);
            return null;
        }
        return getAccount(accountType, as);
    }	//	getPaymentAcct

    /**
     * 	Get Cash (Transfer) Acct of CashBook
     *	@param as accounting schema
     *	@param C_CashLine_ID
     *	@return acct
     */
    private MAccount getCashAcct(MAcctSchema as, int C_CashLine_ID) {
        String sql = "SELECT c.C_CashBook_ID "
                + "FROM C_Cash c, C_CashLine cl "
                + "WHERE c.C_Cash_ID=cl.C_Cash_ID AND cl.C_CashLine_ID=?";
        setC_CashBook_ID(DB.getSQLValue(null, sql, C_CashLine_ID));
        if (getC_CashBook_ID() <= 0) {
            log.log(Level.SEVERE, "NONE for C_CashLine_ID=" + C_CashLine_ID);
            return null;
        }
        return getAccount(Doc.ACCTTYPE_CashTransfer, as);
    }	//	getCashAcct

    /**************************************************************************
     * 	Create Realized Gain & Loss.
     * 	Compares the Accounted Amount of the Invoice to the
     * 	Accounted Amount of the Allocation
     *	@param as accounting schema
     *	@param fact fact
     *	@param invoice invoice
     *	@param allocationSource source amt
     *	@param allocationAccounted acct amt
     *	@return Error Message or null if OK
     */
    private String createRealizedGainLoss(MAcctSchema as, Fact fact, MAccount acct,
            MInvoice invoice, BigDecimal allocationSource, BigDecimal allocationAccounted) {
        BigDecimal invoiceSource = null;
        BigDecimal invoiceAccounted = null;
        //
        String sql = "SELECT "
                + (invoice.isSOTrx()
                ? "SUM(AmtSourceDr), SUM(AmtAcctDr)" //	so 
                : "SUM(AmtSourceCr), SUM(AmtAcctCr)") //	po
                + " FROM Fact_Acct "
                + "WHERE AD_Table_ID=318 AND Record_ID=?" //	Invoice
                + " AND C_AcctSchema_ID=?"
                + " AND PostingType='A'";
        //AND C_Currency_ID=102
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, getTrxName());
            pstmt.setInt(1, invoice.getC_Invoice_ID());
            pstmt.setInt(2, as.getC_AcctSchema_ID());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                invoiceSource = rs.getBigDecimal(1);
                invoiceAccounted = rs.getBigDecimal(2);
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        // 	Requires that Invoice is Posted
        if (invoiceSource == null || invoiceAccounted == null) {
            return "Gain/Loss - Invoice not posted yet";
        }
        //
        String description = "Invoice=(" + invoice.getC_Currency_ID() + ")" + invoiceSource + "/" + invoiceAccounted
                + " - Allocation=(" + getC_Currency_ID() + ")" + allocationSource + "/" + allocationAccounted;
        log.fine(description);
        //	Allocation not Invoice Currency
        if (getC_Currency_ID() != invoice.getC_Currency_ID()) {
            BigDecimal allocationSourceNew = MConversionRate.convert(getCtx(),
                    allocationSource, getC_Currency_ID(),
                    invoice.getC_Currency_ID(), getDateAcct(),
                    invoice.getC_ConversionType_ID(), invoice.getAD_Client_ID(), invoice.getAD_Org_ID());
            if (allocationSourceNew == null) {
                return "Gain/Loss - No Conversion from Allocation->Invoice";
            }
            String d2 = "Allocation=(" + getC_Currency_ID() + ")" + allocationSource
                    + "->(" + invoice.getC_Currency_ID() + ")" + allocationSourceNew;
            log.fine(d2);
            description += " - " + d2;
            allocationSource = allocationSourceNew;
        }

        BigDecimal acctDifference = null;	//	gain is negative
        //	Full Payment in currency
        if (allocationSource.compareTo(invoiceSource) == 0) {
            acctDifference = invoiceAccounted.subtract(allocationAccounted);	//	gain is negative
            String d2 = "(full) = " + acctDifference;
            log.fine(d2);
            description += " - " + d2;
        } else //	partial or MC
        {
            //	percent of total payment
            double multiplier = allocationSource.doubleValue() / invoiceSource.doubleValue();
            //	Reduce Orig Invoice Accounted
            invoiceAccounted = invoiceAccounted.multiply(new BigDecimal(multiplier));
            //	Difference based on percentage of Orig Invoice
            acctDifference = invoiceAccounted.subtract(allocationAccounted);	//	gain is negative
            //	ignore Tolerance
            if (acctDifference.abs().compareTo(TOLERANCE) < 0) {
                acctDifference = Env.ZERO;
            }
            //	Round
            int precision = as.getStdPrecision();
            if (acctDifference.scale() > precision) {
                acctDifference = acctDifference.setScale(precision, BigDecimal.ROUND_HALF_UP);
            }
            String d2 = "(partial) = " + acctDifference + " - Multiplier=" + multiplier;
            log.fine(d2);
            description += " - " + d2;
        }

        if (acctDifference.signum() == 0) {
            log.fine("No Difference");
            return null;
        }

        int moneda = invoice.getC_Currency_ID();

        MAccount gain = MAccount.get(as.getCtx(), as.getAcctSchemaDefault().getRealizedGain_Acct(moneda));
        MAccount loss = MAccount.get(as.getCtx(), as.getAcctSchemaDefault().getRealizedLoss_Acct(moneda));
        //
        if (invoice.isSOTrx()) {
            FactLine fl = fact.createLineDiff(loss, gain, as.getC_Currency_ID(), acctDifference);
            fl.setDescription(description);
            // Se comenta por que genera una registro de m�s en la contabilidad no necesario.
            //fact.createLine (null, acct, as.getC_Currency_ID(), acctDifference.negate());
            fl.setDescription(description);
        } else {
            fact.createLine(null, acct,
                    as.getC_Currency_ID(), acctDifference);
            FactLine fl = fact.createLine(null, loss, gain,
                    as.getC_Currency_ID(), acctDifference.negate());
        }
        return null;
    }	//	createRealizedGainLoss

    /**************************************************************************
     * 	Create Tax Correction.
     * 	Requirement: Adjust the tax amount, if you did not receive the full
     * 	amount of the invoice (payment discount, write-off).
     * 	Applies to many countries with VAT.
     * 	Example:
     * 		Invoice:	Net $100 + Tax1 $15 + Tax2 $5 = Total $120
     * 		Payment:	$115 (i.e. $5 underpayment)
     * 		Tax Adjustment = Tax1 = 0.63 (15/120*5) Tax2 = 0.21 (5/120/5) 
     * 
     * 	@param as accounting schema
     * 	@param fact fact
     *	@param DiscountAccount discount acct
     *	@param WriteOffAccoint write off acct
     *	@return true if created
     */
    private boolean createTaxCorrection(MAcctSchema as, Fact fact,
            DocLine_Allocation line,
            MAccount DiscountAccount, MAccount WriteOffAccoint) {
        log.info("createTaxCorrection - " + line);
        Doc_AllocationTax tax = new Doc_AllocationTax(
                DiscountAccount, line.getDiscountAmt(),
                WriteOffAccoint, line.getWriteOffAmt());

        //	Get Source Amounts with account
        String sql = "SELECT * "
                + "FROM Fact_Acct "
                + "WHERE AD_Table_ID=318 AND Record_ID=?" //	Invoice
                + " AND C_AcctSchema_ID=?"
                + " AND Line_ID IS NULL";	//	header lines like tax or total
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, getTrxName());
            pstmt.setInt(1, line.getC_Invoice_ID());
            pstmt.setInt(2, as.getC_AcctSchema_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tax.addInvoiceFact(new MFactAcct(getCtx(), rs, fact.get_TrxName()));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        //	Invoice Not posted
        if (tax.getLineCount() == 0) {
            log.warning("Invoice not posted yet - " + line);
            return false;
        }
        //	size = 1 if no tax
        if (tax.getLineCount() < 2) {
            return true;
        }
        return tax.createEntries(as, fact, line);

    }	//	createTaxCorrection

    // Begin E-Evolution ogi-cd 27/10/2004 --------------------------------------------------------------------------
    /**************************************************************************
     * 	Create Realized Gain & Loss for Tax
     * 	Compares the Accounted Amount of the Invoice to the
     * 	Accounted Amount of the Allocation for Tax
     *	@param as accounting schema
     *	@param fact fact
     *  @param MAccount acct
     *  @param boolean SOTrx
     *  @param C_Invoice_ID Invoice
     *	@param allocationSource source amt
     *	@param allocationAccounted acct amt
     *	@param differenceAmt over/underpayment + discount amt
     *  @param AccountTax TaxAccount in to Fact_Acct
     *  @param taxInvoice Tax in to Invoice
     *  @param taxPayment Tax in to Payment actual
     *	@return taxgainloss
     */
    private BigDecimal createRealizedGainLossTax(MAcctSchema as, Fact fact, MAccount acct,
            boolean SOTrx, int C_Invoice_ID,
            BigDecimal allocationSource, BigDecimal allocationAccounted,
            BigDecimal differenceAmt, int AccountTax, BigDecimal taxInvoice, BigDecimal taxPayment) {
        // TODO: Does not work with Split Payment Schedule
        BigDecimal taxgainloss = taxPayment.subtract(taxInvoice);
        //	ignore Tolerance
        if (taxgainloss.abs().compareTo(TOLERANCE) < 0) {
            taxgainloss = Env.ZERO;
        }
        //	Round
        int precision = as.getStdPrecision();
        if (taxgainloss.scale() > precision) {
            taxgainloss = taxgainloss.setScale(precision, BigDecimal.ROUND_HALF_UP);
        }
        System.out.println("oooooooooooooooooooooooooooooo>>>" + taxgainloss);
        return taxgainloss;
    }	//	createRealizedGainLossTax
    // End E-Evolution ogi-cd 27/10/2004 ------------------------------------------------------------------------------

    // Begin e-Evolution 07/Oct/2004 ogi-cd
    /**************************************************************************
     * 	DocTax[]
     * 	Requirement: Create Taxes ArrayList to Invoice actual
     * 	@param C_Invoice_ID Identifique to Invoice actual
     * 	@param pago Import to Payment actual
     *	@return Tax Array
     */
    private DocTax[] loadTaxes(int C_Invoice_ID, BigDecimal pago) {
        ArrayList list = new ArrayList();
        String sql = "SELECT it.C_Tax_ID, t.Name, t.Rate, it.TaxBaseAmt, it.TaxAmt, inv.GrandTotal, inv.TotalLines, t.TaxIndicator,inv.IsSOTrx"
                + " FROM C_Tax t, C_InvoiceTax it, C_Invoice inv"
                + " WHERE t.C_Tax_ID=it.C_Tax_ID AND it.C_Invoice_ID=? AND it.C_Invoice_ID=inv.C_Invoice_ID";
        try {
            //boolean IsSoTrx = ;
            PreparedStatement pstmt = DB.prepareStatement(sql);
            pstmt.setInt(1, C_Invoice_ID);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Estoy por entrar en el While >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
            while (rs.next()) {
                int C_Tax_ID = rs.getInt(1);
                String name = rs.getString(2);
                BigDecimal rate = rs.getBigDecimal(3);
                BigDecimal taxBaseAmt = rs.getBigDecimal(4);
                BigDecimal amount = rs.getBigDecimal(5);
                BigDecimal totalTax = rs.getBigDecimal(4).add(rs.getBigDecimal(5));
                BigDecimal grandtotal = rs.getBigDecimal(6);
                String indicatorX = rs.getString(8);
                String indicator = rs.getString(8) == null ? "" : rs.getString(8).substring(0, 1);
                //IsSoTrx               = rs.getString(9) == true ? true : false; // agregado 22/03/2006
                //
                BigDecimal tax = Env.ZERO;
                BigDecimal pago100 = pago.multiply(Env.ONEHUNDRED);
                BigDecimal pjepago = pago100.divide(grandtotal, 5, 4);
                BigDecimal pjepagoOK = pjepago.abs();
                BigDecimal amountABS = amount.abs();
                BigDecimal taxamount = amountABS.multiply(pjepagoOK);
                BigDecimal tax100 = taxamount.divide(Env.ONEHUNDRED, 5, 4);
                //
                //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> name, rate, taxBaseAmt, amount, totalTax, grandTotal, indicatorX, indicator" + name +"-"+ rate+"-"+taxBaseAmt+"-"+amount+"-"+totalTax+"-"+grandtotal+"-"+indicatorX+"-"+indicator);
                //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> tax, pago100, pjepago, pjepagoOK, amountABS, taxamount, tax100"+tax+"-"+pago100+"-"+pjepago+"-"+pjepagoOK+"-"+amountABS+"-"+taxamount+"-"+tax100);
                //
                if (indicator.equals("R")) // if Tax Hold, negate
                {
                    tax = tax100.negate();
                } else {
                    tax = tax100;
                }
                DocTax taxLine = new DocTax(C_Tax_ID, name, rate, taxBaseAmt, tax, false); // CAMBIAR POR LA VARIABLE DE LA FACTURA
                log.log(Level.SEVERE, taxLine.toString(), "");
                list.add(taxLine);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "loadTaxes", e);
        }
        //	Return Array
        DocTax[] tl = new DocTax[list.size()];
        list.toArray(tl);
        return tl;
    }	//	loadTaxes
    // End e-Evolution 07/Oct/2004 ogi-cd -------------------------------------------------------------------------------------------

    /**
     *  Sustituye momentaneamente a MPayment -> getCotizacion()
     */
    private BigDecimal getPaymentRate(MPayment payment) {

        return payment.getCotizacion();

        /*		BigDecimal cotizacion = BigDecimal.ONE;
        try
        {
        String sql = "Select COTIZACION from C_Payment Where C_Payment_ID = " + payment.get_ID();
        PreparedStatement pstm = DB.prepareStatement(sql, getTrxName());
        ResultSet rs = pstm.executeQuery();
        if (rs.next() && rs.getBigDecimal(1)!=null)
        cotizacion = rs.getBigDecimal(1);
        }catch (Exception e){e.printStackTrace();};
        
        return cotizacion;
         */
    }

    private BigDecimal getInvoiceRate(MInvoice invoice) {

        return invoice.getCotizacion();

        /*		BigDecimal cotizacion = BigDecimal.ONE;
        try
        {
        String sql = "Select COTIZACION from C_Invoice Where C_Invoice_ID = " + invoice.get_ID();
        PreparedStatement pstm = DB.prepareStatement(sql, getTrxName());
        ResultSet rs = pstm.executeQuery();
        if (rs.next() && rs.getBigDecimal(1)!=null)
        cotizacion = rs.getBigDecimal(1);
        }catch (Exception e){e.printStackTrace();};
        
        return cotizacion;
         */
    }
}   //  Doc_Allocation

/**
 * 	Allocation Document Tax Handing
 *	
 *  @author Jorg Janke
 *  @version $Id: Doc_Allocation.java,v 1.17 2005/10/28 01:00:53 jjanke Exp $
 */
class Doc_AllocationTax {

    /**
     * 	Allocation Tax Adjustment
     *	@param DiscountAccount discount acct
     *	@param DiscountAmt discount amt
     *	@param WriteOffAccount write off acct
     *	@param WriteOffAmt write off amt
     */
    public Doc_AllocationTax(MAccount DiscountAccount, BigDecimal DiscountAmt,
            MAccount WriteOffAccount, BigDecimal WriteOffAmt) {
        m_DiscountAccount = DiscountAccount;
        m_DiscountAmt = DiscountAmt;
        m_WriteOffAccount = WriteOffAccount;
        m_WriteOffAmt = WriteOffAmt;
    }	//	Doc_AllocationTax
    private CLogger log = CLogger.getCLogger(getClass());
    private MAccount m_DiscountAccount;
    private BigDecimal m_DiscountAmt;
    private MAccount m_WriteOffAccount;
    private BigDecimal m_WriteOffAmt;
    private ArrayList<MFactAcct> m_facts = new ArrayList<MFactAcct>();
    private int m_totalIndex = 0;

    /**
     * 	Add Invoice Fact Line
     *	@param fact fact line
     */
    public void addInvoiceFact(MFactAcct fact) {
        m_facts.add(fact);
    }	//	addInvoiceLine

    /**
     * 	Get Line Count
     *	@return number of lines
     */
    public int getLineCount() {
        return m_facts.size();
    }	//	getLineCount

    /**
     * 	Create Accounting Entries
     *	@param as account schema
     *	@param fact fact to add lines
     *	@return true if created
     */
    public boolean createEntries(MAcctSchema as, Fact fact, DocLine line) {
        //	get total index (the Receivables/Liabilities line)
        BigDecimal total = Env.ZERO;
        for (int i = 0; i < m_facts.size(); i++) {
            MFactAcct factAcct = (MFactAcct) m_facts.get(i);
            if (factAcct.getAmtSourceDr().compareTo(total) > 0) {
                total = factAcct.getAmtSourceDr();
                m_totalIndex = i;
            }
            if (factAcct.getAmtSourceCr().compareTo(total) > 0) {
                total = factAcct.getAmtSourceCr();
                m_totalIndex = i;
            }
        }

        MFactAcct factAcct = (MFactAcct) m_facts.get(m_totalIndex);
        log.info("createEntries - Total Invoice = " + total + " - " + factAcct);
        int precision = as.getStdPrecision();
        for (int i = 0; i < m_facts.size(); i++) {
            //	No Tax Line
            if (i == m_totalIndex) {
                continue;
            }

            factAcct = (MFactAcct) m_facts.get(i);
            log.info(i + ": " + factAcct);

            //	Create Tax Account
            MAccount taxAcct = factAcct.getMAccount();
            if (taxAcct == null || taxAcct.get_ID() == 0) {
                log.severe("Tax Account not found/created");
                return false;
            }


            //	Discount Amount	
            if (Env.ZERO.compareTo(m_DiscountAmt) != 0) {
                //	Original Tax is DR - need to correct it CR
                if (Env.ZERO.compareTo(factAcct.getAmtSourceDr()) != 0) {
                    BigDecimal amount = calcAmount(factAcct.getAmtSourceDr(),
                            total, m_DiscountAmt, precision);
                    if (amount.signum() != 0) {
                        fact.createLine(line, m_DiscountAccount,
                                as.getC_Currency_ID(), amount, null);
                        fact.createLine(line, taxAcct,
                                as.getC_Currency_ID(), null, amount);
                    }
                } //	Original Tax is CR - need to correct it DR
                else {
                    BigDecimal amount = calcAmount(factAcct.getAmtSourceCr(),
                            total, m_DiscountAmt, precision);
                    if (amount.signum() != 0) {
                        fact.createLine(line, taxAcct,
                                as.getC_Currency_ID(), amount, null);
                        fact.createLine(line, m_DiscountAccount,
                                as.getC_Currency_ID(), null, amount);
                    }
                }
            }	//	Discount

            //	WriteOff Amount	
            if (Env.ZERO.compareTo(m_WriteOffAmt) != 0) {
                //	Original Tax is DR - need to correct it CR
                if (Env.ZERO.compareTo(factAcct.getAmtSourceDr()) != 0) {
                    BigDecimal amount = calcAmount(factAcct.getAmtSourceDr(),
                            total, m_WriteOffAmt, precision);
                    if (amount.signum() != 0) {
                        fact.createLine(line, m_WriteOffAccount,
                                as.getC_Currency_ID(), amount, null);
                        fact.createLine(line, taxAcct,
                                as.getC_Currency_ID(), null, amount);
                    }
                } //	Original Tax is CR - need to correct it DR
                else {
                    BigDecimal amount = calcAmount(factAcct.getAmtSourceCr(),
                            total, m_WriteOffAmt, precision);
                    if (amount.signum() != 0) {
                        fact.createLine(line, taxAcct,
                                as.getC_Currency_ID(), amount, null);
                        fact.createLine(line, m_WriteOffAccount,
                                as.getC_Currency_ID(), null, amount);
                    }
                }
            }	//	WriteOff

        }	//	for all lines
        return true;
    }	//	createEntries

    /**
     * 	Calc Amount tax / total * amt
     *	@param tax tax
     *	@param total total
     *	@param amt reduction amt
     *	@param precision precision
     *	@return tax / total * amt
     */
    private BigDecimal calcAmount(BigDecimal tax, BigDecimal total, BigDecimal amt, int precision) {
        log.fine("Tax=" + tax + " / Total=" + total + " * Amt=" + amt);
        if (tax.signum() == 0
                || total.signum() == 0
                || amt.signum() == 0) {
            return Env.ZERO;
        }
        /**
        BigDecimal percentage = tax.divide(total, 10, BigDecimal.ROUND_HALF_UP); 
        BigDecimal retValue = percentage.multiply(amt);
        if (retValue.scale() > precision)
        retValue = retValue.setScale(precision, BigDecimal.ROUND_HALF_UP);
        log.fine("calcAmount - Percentage=" + percentage + ", Result=" + retValue);
         **/
        BigDecimal retValue = tax.multiply(amt).divide(total, precision, BigDecimal.ROUND_HALF_UP);
        log.fine("Result=" + retValue);
        return retValue;
    }	//	calcAmount
}	//	Doc_AllocationTax
