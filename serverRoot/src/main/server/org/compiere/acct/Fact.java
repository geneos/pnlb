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
import java.util.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Accounting Fact
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: Fact.java,v 1.12 2005/11/01 16:38:56 jjanke Exp $
 */
public final class Fact {

    /**
     *	Constructor
     *  @param  document    pointer to document
     *  @param  acctSchema  Account Schema to create accounts
     *  @param  defaultPostingType  the default Posting type (actual,..) for this posting
     */
    public Fact(Doc document, MAcctSchema acctSchema, String defaultPostingType) {
        m_doc = document;
        m_acctSchema = acctSchema;
        m_postingType = defaultPostingType;
        //
        log.config(toString());
    }	//	Fact
    /**	Log					*/
    private CLogger log = CLogger.getCLogger(getClass());
    /** Document            */
    private Doc m_doc = null;
    /** Accounting Schema   */
    private MAcctSchema m_acctSchema = null;
    /** Transaction			*/
    private String m_trxName;
    /** Posting Type        */
    private String m_postingType = null;
    /** Actual Balance Type */
    public static final String POST_Actual = MFactAcct.POSTINGTYPE_Actual;
    /** Budget Balance Type */
    public static final String POST_Budget = MFactAcct.POSTINGTYPE_Budget;
    ;
	/** Encumbrance Posting */
	public static final String POST_Commitment = MFactAcct.POSTINGTYPE_Commitment;
    /** Encumbrance Posting */
    public static final String POST_Reservation = MFactAcct.POSTINGTYPE_Reservation;
    /** Is Converted        */
    private boolean m_converted = false;
    /** Lines               */
    private ArrayList<FactLine> m_lines = new ArrayList<FactLine>();

    /**
     *  Dispose
     */
    public void dispose() {
        m_lines.clear();
        m_lines = null;
    }   //  dispose

    /**
     *	Create Difference Fact Line.
     *  Used to create a DR or CR entry
     *
     *  @param  accountDr     if null, line can't be created
     *  @param  accountCr     if null, line can't be created
     *  @param  C_Currency_ID   the currency
     *  @param  Amt    amount, can be null
     *  @return Fact Line
     */
    public FactLine createLineDiff(MAccount accountDr, MAccount accountCr,
            int C_Currency_ID, BigDecimal Amt) {
        //  Data Check
        if (accountDr == null && accountCr == null) {
            log.info("No account"
                    + ": Amt=" + Amt
                    + " - " + toString());
            return null;
        }

        FactLine line = new FactLine(m_doc.getCtx(), m_doc.get_Table_ID(),
                m_doc.get_ID(), 0, m_trxName);
        //  Set Info & Account
        line.setDocumentInfo(m_doc, null);
        line.setPostingType(m_postingType);

        if (Amt.signum() < 0) {
            line.setAccount(m_acctSchema, accountCr);
            line.setAmtAcctDr(Env.ZERO);
            line.setAmtAcctCr(Amt.abs());
        } else {
            line.setAccount(m_acctSchema, accountDr);
            line.setAmtAcctDr(Amt.abs());
            line.setAmtAcctCr(Env.ZERO);
        }

        line.setAmtSource(C_Currency_ID, Env.ZERO, Env.ZERO);

        log.fine(line.toString());
        add(line);
        return line;
    }	//	createLine

    /**
     *	Create and convert Fact Line.
     *  Used to create a DR and/or CR entry
     *
     *	@param  docLine     the document line or null
     *  @param  account     if null, line is not created
     *  @param  C_Currency_ID   the currency
     *  @param  debitAmt    debit amount, can be null
     *  @param  creditAmt  credit amount, can be null
     *  @return Fact Line
     */
    public FactLine createLine(DocLine docLine, MAccount account,
            int C_Currency_ID, BigDecimal debitAmt, BigDecimal creditAmt) {
        //	log.fine("createLine - " + account	+ " - Dr=" + debitAmt + ", Cr=" + creditAmt);

        //  Data Check
        if (account == null) {
            log.info("No account for " + docLine
                    + ": Amt=" + debitAmt + "/" + creditAmt
                    + " - " + toString());
            System.out.println("No account for " + docLine
                    + ": Amt=" + debitAmt + "/" + creditAmt
                    + " - " + toString());
            return null;
        }
        //

        FactLine line = new FactLine(m_doc.getCtx(), m_doc.get_Table_ID(),
                m_doc.get_ID(),
                docLine == null ? 0 : docLine.get_ID(), m_trxName);
        //  Set Info & Account
        line.setDocumentInfo(m_doc, docLine);
        line.setPostingType(m_postingType);
        line.setAccount(m_acctSchema, account);

        if (creditAmt != null) {
            System.out.print("\n Veamos el CRED - F: " + creditAmt.floatValue());
        }

        if (debitAmt != null) {
            System.out.print("\n Veamos el DEB - F: " + debitAmt.floatValue());
        }

        //  Amounts - one needs to not zero
        if (!line.setAmtSource(C_Currency_ID, debitAmt, creditAmt)) {

            if (docLine == null || docLine.getQty() == null || docLine.getQty().signum() == 0) {
                log.fine("Both amounts & qty = 0/Null - " + docLine
                        + " - " + toString());
                return null;
            }
            log.fine("Both amounts = 0/Null, Qty=" + docLine.getQty() + " - " + docLine
                    + " - " + toString());
        }
        //  Convert
        line.convert();

        //  Optionally overwrite Acct Amount
        if (docLine != null
                && (docLine.getAmtAcctDr() != null || docLine.getAmtAcctCr() != null)) {
            line.setAmtAcct(docLine.getAmtAcctDr(), docLine.getAmtAcctCr());
        }
        //
        log.fine(line.toString());
        System.out.print("\n" + line);

        add(line);
        return line;
    }	//	createLine

    /**
     *	Create and convert Fact Line.
     *  Used to create a DR and/or CR entry
     *
     *	@param  docLine     the document line or null
     *  @param  account     if null, line is not created
     *  @param  Rate Amount to convert
     *  @param  C_Currency_ID   the currency
     *  @param  debitAmt    debit amount, can be null
     *  @param  creditAmt  credit amount, can be null
     *  @return Fact Line
     */
    public FactLine createLine(DocLine docLine, MAccount account, BigDecimal rate,
            int C_Currency_ID, BigDecimal debitAmt, BigDecimal creditAmt) {
        //	log.fine("createLine - " + account	+ " - Dr=" + debitAmt + ", Cr=" + creditAmt);

        //  Data Check
        if (account == null) {
            log.warning("No account for " + docLine
                    + ": Amt=" + debitAmt + "/" + creditAmt
                    + " - " + toString());
            return null;
        }
        //

        FactLine line = new FactLine(m_doc.getCtx(), m_doc.get_Table_ID(),
                m_doc.get_ID(),
                docLine == null ? 0 : docLine.get_ID(), m_trxName);
        //  Set Info & Account
        line.setDocumentInfo(m_doc, docLine);
        line.setPostingType(m_postingType);
        line.setAccount(m_acctSchema, account);

        if (creditAmt != null) {
            System.out.print("\n Veamos el CRED - F: " + creditAmt.floatValue());
        }

        if (debitAmt != null) {
            System.out.print("\n Veamos el DEV - F: " + debitAmt.floatValue());
        }

        //  Amounts - one needs to not zero
        if (!line.setAmtSource(C_Currency_ID, debitAmt, creditAmt)) {

            if (docLine == null || docLine.getQty() == null || docLine.getQty().signum() == 0) {
                log.fine("Both amounts & qty = 0/Null - " + docLine
                        + " - " + toString());
                return null;
            }
            log.fine("Both amounts = 0/Null, Qty=" + docLine.getQty() + " - " + docLine
                    + " - " + toString());
        }
        //  Convert
        line.convert(rate);

        //  Optionally overwrite Acct Amount
        if (docLine != null
                && (docLine.getAmtAcctDr() != null || docLine.getAmtAcctCr() != null)) {
            line.setAmtAcct(docLine.getAmtAcctDr(), docLine.getAmtAcctCr());
        }
        //
        log.fine(line.toString());
        add(line);
        return line;
    }	//	createLine

    /**
     *  Add Fact Line
     *  @param line fact line
     */
    void add(FactLine line) {
        m_lines.add(line);
    }   //  add

    /**
     *	Create and convert Fact Line.
     *  Used to create either a DR or CR entry
     *
     *	@param  docLine     Document Line or null
     *  @param  accountDr   Account to be used if Amt is DR balance
     *  @param  accountCr   Account to be used if Amt is CR balance
     *  @param  C_Currency_ID Currency
     *  @param  Amt if negative Cr else Dr
     *  @return FactLine
     */
    public FactLine createLine(DocLine docLine, MAccount accountDr, MAccount accountCr,
            int C_Currency_ID, BigDecimal Amt) {
        if (Amt.signum() < 0) {
            return createLine(docLine, accountCr, C_Currency_ID, null, Amt.abs());
        } else {
            return createLine(docLine, accountDr, C_Currency_ID, Amt, null);
        }
    }   //  createLine

    /**
     *	Create and convert Fact Line.
     *  Used to create either a DR or CR entry
     *
     *	@param  docLine Document line or null
     *  @param  account   Account to be used
     *  @param  C_Currency_ID Currency
     *  @param  Amt if negative Cr else Dr
     *  @return FactLine
     */
    public FactLine createLine(DocLine docLine, MAccount account,
            int C_Currency_ID, BigDecimal Amt) {
        if (Amt.signum() < 0) {
            return createLine(docLine, account, C_Currency_ID, null, Amt.abs());
        } else {
            return createLine(docLine, account, C_Currency_ID, Amt, null);
        }
    }   //  createLine

    /**
     *	Create and convert Fact Line.
     *  Used to create either a DR or CR entry, whit Rate
     *
     *	@param  docLine     Document Line or null
     *  @param  accountDr   Account to be used if Amt is DR balance
     *  @param  accountCr   Account to be used if Amt is CR balance
     *  @param  Rate Amount to convert
     *  @param  C_Currency_ID Currency
     *  @param  Amt if negative Cr else Dr
     *  @return FactLine
     */
    public FactLine createLine(DocLine docLine, MAccount accountDr, MAccount accountCr,
            BigDecimal rate, int C_Currency_ID, BigDecimal Amt) {
        if (Amt.signum() < 0) {
            return createLine(docLine, accountCr, rate, C_Currency_ID, null, Amt.abs());
        } else {
            return createLine(docLine, accountDr, rate, C_Currency_ID, Amt, null);
        }
    }   //  createLine

    /**
     *	Create and convert Fact Line.
     *  Used to create either a DR or CR entry
     *
     *	@param  docLine Document line or null
     *  @param  account   Account to be used
     *  @param  Rate Amount to convert
     *  @param  C_Currency_ID Currency
     *  @param  Amt if negative Cr else Dr
     *  @return FactLine
     */
    public FactLine createLine(DocLine docLine, MAccount account, BigDecimal rate,
            int C_Currency_ID, BigDecimal Amt) {
        if (Amt.signum() < 0) {
            return createLine(docLine, account, rate, C_Currency_ID, null, Amt.abs());
        } else {
            return createLine(docLine, account, rate, C_Currency_ID, Amt, null);
        }
    }   //  createLine

    /**
     *  Is Posting Type
     *  @param  PostingType - see POST_*
     *  @return true if document is posting type
     */
    public boolean isPostingType(String PostingType) {
        return m_postingType.equals(PostingType);
    }   //  isPostingType

    /**
     *	Is converted
     *  @return true if converted
     */
    public boolean isConverted() {
        return m_converted;
    }	//	isConverted

    /**
     *	Get AcctSchema
     *  @return AcctSchema
     */
    public MAcctSchema getAcctSchema() {
        return m_acctSchema;
    }	//	getAcctSchema

    /**************************************************************************
     *	Are the lines Source Balanced
     *  @return true if source lines balanced
     */
    public boolean isSourceBalanced() {
        //  No lines -> balanded
        if (m_lines.size() == 0) {
            return true;
        }
        BigDecimal balance = getSourceBalance();
        boolean retValue = balance.signum() == 0;
        if (retValue) {
            log.finer(toString());
        } else {
            log.warning("NO - Diff=" + balance + " - " + toString());
        }

        System.out.println("Diff=" + balance + " - " + toString());
        return retValue;
    }	//	isSourceBalanced

    /**
     *	Return Source Balance
     *  @return source balance
     */
    protected BigDecimal getSourceBalance() {
        BigDecimal result = Env.ZERO;
        for (int i = 0; i < m_lines.size(); i++) {
            FactLine line = (FactLine) m_lines.get(i);
            result = result.add(line.getSourceBalance());
        }
        //	log.fine("getSourceBalance - " + result.toString());
        return result;
    }	//	getSourceBalance

    /**
     *	Create Source Line for Suspense Balancing.
     *  Only if Suspense Balancing is enabled and not a multi-currency document
     *  (double check as otherwise the rule should not have fired)
     *  If not balanced create balancing entry in currency of the document
     *  @return FactLine
     */
    public FactLine balanceSource() {
        if (!m_acctSchema.isSuspenseBalancing() || m_doc.isMultiCurrency()) {
            return null;
        }
        BigDecimal diff = getSourceBalance();
        log.finer("Diff=" + diff);

        //  new line
        FactLine line = new FactLine(m_doc.getCtx(), m_doc.get_Table_ID(),
                m_doc.get_ID(), 0, m_trxName);
        line.setDocumentInfo(m_doc, null);
        line.setPostingType(m_postingType);

        //  Amount
        if (diff.signum() < 0) //  negative balance => DR
        {
            line.setAmtSource(m_doc.getC_Currency_ID(), diff.abs(), Env.ZERO);
        } else //  positive balance => CR
        {
            line.setAmtSource(m_doc.getC_Currency_ID(), Env.ZERO, diff);
        }

        //	Account
        line.setAccount(m_acctSchema, m_acctSchema.getSuspenseBalancing_Acct());

        //  Convert
        line.convert();
        //
        log.fine(line.toString());
        m_lines.add(line);
        return line;
    }   //  balancingSource

    /*
     * Se numeran los asientos contables
     * De manera transaccional, o sea que bloquea tablas  
     */
    public boolean numerateFactLines() {
        FactLine[] factLine = getLines();
        int i, j;

        MSequence seq = MSequence.get(Env.getCtx(), "Fact_FactNo", get_TrxName());

        //Itero sobre el DEBE
        /*for (i = 0; i < factLine.length; i++) {
            if (!factLine[i].isCredit()) {
                int next = seq.getCurrentNext();

                //Seteo numero de asiento en DEBE
                factLine[i].setFactNo(next);
                boolean match = false;
                //Busco su par en el HABER
                for (j = 0; j < factLine.length; i++) {
                    if (factLine[j].isCredit()
                            && factLine[j].getRecord_ID() == factLine[i].getRecord_ID()
                            && factLine[j].getLine_ID() == factLine[i].getLine_ID()) {
                        //Seteo numero de asiento en HABER
                        factLine[j].setFactNo(next);
                        match = true;
                    }
                }

                if (!match) {
                    log.info("No se encontro par para asiento: " + factLine[i].isCredit());
                    //return false;
                }

                //Actualizo secuencia
                seq.setCurrentNext(next + 1);

            }
        }
        
        //Itero sobre el HABER por si quedo algun asiento sin matching
        for (j = 0; j < factLine.length; i++) {
            if (factLine[j].isCredit() && factLine[j].getFactNo() == 0) {
                //Seteo numero de asiento en HABER
                int next = seq.getCurrentNext();
                factLine[j].setFactNo(next);
                seq.setCurrentNext(next + 1);
            }
        }*/
        
        int next = seq.getCurrentNext();

        for (i = 0; i < factLine.length; i++) {
            factLine[i].setFactNo(next);
        }

        seq.setCurrentNext(next + 1);
        if (!seq.save()){
            log.info("Error saving sequence for factlines: " +seq);
        }
        
        return true;
    }

    /**************************************************************************
     *  Are all segments balanced
     *  @return true if segments are balanced
     */
    public boolean isSegmentBalanced() {
        if (m_lines.size() == 0) {
            return true;
        }

        MAcctSchemaElement[] elements = m_acctSchema.getAcctSchemaElements();
        //  check all balancing segments
        for (int i = 0; i < elements.length; i++) {
            MAcctSchemaElement ase = elements[i];
            if (ase.isBalanced() && !isSegmentBalanced(ase.getElementType())) {
                return false;
            }
        }
        return true;
    }   //  isSegmentBalanced

    /**
     *  Is Source Segment balanced.
     *  @param  segmentType - see AcctSchemaElement.SEGMENT_*
     *  Implemented only for Org
     *  Other sensible candidates are Project, User1/2
     *  @return true if segments are balanced
     */
    public boolean isSegmentBalanced(String segmentType) {
        if (segmentType.equals(MAcctSchemaElement.ELEMENTTYPE_Organization)) {
            HashMap<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
            //  Add up values by key
            for (int i = 0; i < m_lines.size(); i++) {
                FactLine line = (FactLine) m_lines.get(i);
                Integer key = new Integer(line.getAD_Org_ID());
                BigDecimal bal = line.getSourceBalance();
                BigDecimal oldBal = (BigDecimal) map.get(key);
                if (oldBal != null) {
                    bal = bal.add(oldBal);
                }
                map.put(key, bal);
                //	System.out.println("Add Key=" + key + ", Bal=" + bal + " <- " + line);
            }
            //  check if all keys are zero
            Iterator values = map.values().iterator();
            while (values.hasNext()) {
                BigDecimal bal = (BigDecimal) values.next();
                if (bal.signum() != 0) {
                    map.clear();
                    log.warning("(" + segmentType + ") NO - " + toString() + ", Balance=" + bal);
                    return false;
                }
            }
            map.clear();
            log.finer("(" + segmentType + ") - " + toString());
            return true;
        }
        log.finer("(" + segmentType + ") (not checked) - " + toString());
        return true;
    }   //  isSegmentBalanced

    /**
     *  Balance all segments.
     *  - For all balancing segments
     *      - For all segment values
     *          - If balance <> 0 create dueTo/dueFrom line
     *              overwriting the segment value
     */
    public void balanceSegments() {
        MAcctSchemaElement[] elements = m_acctSchema.getAcctSchemaElements();
        //  check all balancing segments
        for (int i = 0; i < elements.length; i++) {
            MAcctSchemaElement ase = elements[i];
            if (ase.isBalanced()) {
                balanceSegment(ase.getElementType());
            }
        }
    }   //  balanceSegments

    /**
     *  Balance Source Segment
     *  @param elementType segment element type
     */
    private void balanceSegment(String elementType) {
        //  no lines -> balanced
        if (m_lines.size() == 0) {
            return;
        }

        log.fine("(" + elementType + ") - " + toString());

        //  Org
        if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Organization)) {
            HashMap<Integer, Balance> map = new HashMap<Integer, Balance>();
            //  Add up values by key
            for (int i = 0; i < m_lines.size(); i++) {
                FactLine line = (FactLine) m_lines.get(i);
                Integer key = new Integer(line.getAD_Org_ID());
                //	BigDecimal balance = line.getSourceBalance();
                Balance oldBalance = (Balance) map.get(key);
                if (oldBalance == null) {
                    oldBalance = new Balance(line.getAmtSourceDr(), line.getAmtSourceCr());
                    map.put(key, oldBalance);
                } else {
                    oldBalance.add(line.getAmtSourceDr(), line.getAmtSourceCr());
                }
                //	log.info ("Key=" + key + ", Balance=" + balance + " - " + line);
            }

            //  Create entry for non-zero element
            Iterator keys = map.keySet().iterator();
            while (keys.hasNext()) {
                Integer key = (Integer) keys.next();
                Balance difference = (Balance) map.get(key);
                log.info(elementType + "=" + key + ", " + difference);
                //
                if (!difference.isZeroBalance()) {
                    //  Create Balancing Entry
                    FactLine line = new FactLine(m_doc.getCtx(), m_doc.get_Table_ID(),
                            m_doc.get_ID(), 0, m_trxName);
                    line.setDocumentInfo(m_doc, null);
                    line.setPostingType(m_postingType);
                    //  Amount & Account
                    if (difference.getBalance().signum() < 0) {
                        if (difference.isReversal()) {
                            line.setAmtSource(m_doc.getC_Currency_ID(), Env.ZERO, difference.getPostBalance());
                            line.setAccount(m_acctSchema, m_acctSchema.getDueTo_Acct(elementType));
                        } else {
                            line.setAmtSource(m_doc.getC_Currency_ID(), difference.getPostBalance(), Env.ZERO);
                            line.setAccount(m_acctSchema, m_acctSchema.getDueFrom_Acct(elementType));
                        }
                    } else {
                        if (difference.isReversal()) {
                            line.setAmtSource(m_doc.getC_Currency_ID(), difference.getPostBalance(), Env.ZERO);
                            line.setAccount(m_acctSchema, m_acctSchema.getDueFrom_Acct(elementType));
                        } else {
                            line.setAmtSource(m_doc.getC_Currency_ID(), Env.ZERO, difference.getPostBalance());
                            line.setAccount(m_acctSchema, m_acctSchema.getDueTo_Acct(elementType));
                        }
                    }
                    line.convert();
                    line.setAD_Org_ID(key.intValue());
                    //
                    m_lines.add(line);
                    log.fine("(" + elementType + ") - " + line);
                }
            }
            map.clear();
        }
    }   //  balanceSegment

    /**************************************************************************
     *	Are the lines Accounting Balanced
     *  @return true if accounting lines are balanced
     */
    public boolean isAcctBalanced() {
        //  no lines -> balanced
        if (m_lines.size() == 0) {
            return true;
        }
        BigDecimal balance = getAcctBalance();
        boolean retValue = balance.signum() == 0;
        if (retValue) {
            log.finer(toString());
        } else {
            log.warning("NO - Diff=" + balance + " - " + toString());
        }
        System.out.println("Diff=" + balance + " - " + toString());
        return retValue;
    }	//	isAcctBalanced

    /**
     *	Return Accounting Balance
     *  @return true if accounting lines are balanced
     */
    protected BigDecimal getAcctBalance() {
        BigDecimal result = Env.ZERO;
        for (int i = 0; i < m_lines.size(); i++) {
            FactLine line = (FactLine) m_lines.get(i);
            result = result.add(line.getAcctBalance());
        }
        //	log.fine(result.toString());
        return result;
    }	//	getAcctBalance

    /**
     *  Balance Accounting Currency.
     *  If the accounting currency is not balanced,
     *      if Currency balancing is enabled
     *          create a new line using the currency balancing account with zero source balance
     *      or
     *          adjust the line with the largest balance sheet account
     *          or if no balance sheet account exist, the line with the largest amount
     *  @return FactLine
     */
    public FactLine balanceAccounting() {
        BigDecimal diff = getAcctBalance();		//	DR-CR
        log.fine("Balance=" + diff
                + ", CurrBal=" + m_acctSchema.isCurrencyBalancing()
                + " - " + toString());
        FactLine line = null;

        //  Create Currency Entry
        if (m_acctSchema.isCurrencyBalancing()) {
            line = new FactLine(m_doc.getCtx(), m_doc.get_Table_ID(),
                    m_doc.get_ID(), 0, m_trxName);
            line.setDocumentInfo(m_doc, null);
            line.setPostingType(m_postingType);
            line.setAccount(m_acctSchema, m_acctSchema.getCurrencyBalancing_Acct());

            //  Amount
            line.setAmtSource(m_doc.getC_Currency_ID(), Env.ZERO, Env.ZERO);
            line.convert();
            if (diff.signum() < 0) {
                line.setAmtAcct(diff.abs(), Env.ZERO);
            } else {
                line.setAmtAcct(Env.ZERO, diff.abs());
            }
            log.fine(line.toString());
            m_lines.add(line);
        } else //  Adjust biggest (Balance Sheet) line amount
        {
            BigDecimal BSamount = Env.ZERO;
            FactLine BSline = null;
            BigDecimal PLamount = Env.ZERO;
            FactLine PLline = null;

            //  Find line
            for (int i = 0; i < m_lines.size(); i++) {
                FactLine l = (FactLine) m_lines.get(i);
                BigDecimal amt = l.getAcctBalance().abs();
                if (l.isBalanceSheet() && amt.compareTo(BSamount) > 0) {
                    BSamount = amt;
                    BSline = l;
                } else if (!l.isBalanceSheet() && amt.compareTo(PLamount) > 0) {
                    PLamount = amt;
                    PLline = l;
                }
            }
            if (BSline != null) {
                line = BSline;
            } else {
                line = PLline;
            }
            if (line == null) {
                log.severe("No Line found");
            } else {
                log.fine("Adjusting Amt=" + diff + "; Line=" + line);
                line.currencyCorrect(diff);
                log.fine(line.toString());
            }
        }   //  correct biggest amount

        return line;
    }   //  balanceAccounting

    /**
     * 	Check Accounts of Fact Lines
     *	@return true if success
     */
    public boolean checkAccounts() {
        //  no lines -> nothing to distribute
        if (m_lines.size() == 0) {
            return true;
        }

        //	For all fact lines
        for (int i = 0; i < m_lines.size(); i++) {
            FactLine line = (FactLine) m_lines.get(i);
            MAccount account = line.getAccount();
            if (account == null) {
                log.warning("No Account for " + line);
                return false;
            }
            MElementValue ev = account.getAccount();
            if (ev == null) {
                log.warning("No Element Value for " + account
                        + ": " + line);
                return false;
            }
            if (ev.isSummary()) {
                log.warning("Cannot post to Summary Account " + ev
                        + ": " + line);
                return false;
            }
            if (!ev.isActive()) {
                log.warning("Cannot post to Inactive Account " + ev
                        + ": " + line);
                return false;
            }

        }	//	for all lines

        return true;
    }	//	checkAccounts

    /**
     * 	GL Distribution of Fact Lines
     *	@return true if success
     */
    public boolean distribute() {
        //  no lines -> nothing to distribute
        if (m_lines.size() == 0) {
            return true;
        }

        ArrayList<FactLine> newLines = new ArrayList<FactLine>();
        //	For all fact lines
        for (int i = 0; i < m_lines.size(); i++) {
            FactLine dLine = (FactLine) m_lines.get(i);
            MDistribution[] distributions = MDistribution.get(dLine.getAccount(),
                    m_postingType, m_doc.getC_DocType_ID());
            //	No Distribution for this line
            if (distributions == null || distributions.length == 0) {
                continue;
            }
            //	Just the first
            if (distributions.length > 1) {
                log.warning("More then one Distributiion for " + dLine.getAccount());
            }
            MDistribution distribution = distributions[0];
            //	Add Reversal
            FactLine reversal = dLine.reverse(distribution.getName());
            log.info("Reversal=" + reversal);
            newLines.add(reversal);		//	saved in postCommit
            //	Prepare
            distribution.distribute(dLine.getAccount(), dLine.getSourceBalance(), dLine.getC_Currency_ID());
            MDistributionLine[] lines = distribution.getLines(false);
            for (int j = 0; j < lines.length; j++) {
                MDistributionLine dl = lines[j];
                if (!dl.isActive() || dl.getAmt().signum() == 0) {
                    continue;
                }
                FactLine factLine = new FactLine(m_doc.getCtx(), m_doc.get_Table_ID(),
                        m_doc.get_ID(), 0, m_trxName);
                //  Set Info & Account
                factLine.setDocumentInfo(m_doc, dLine.getDocLine());
                factLine.setAccount(m_acctSchema, dl.getAccount());
                factLine.setPostingType(m_postingType);
                if (dl.isOverwriteOrg()) //	set Org explicitly
                {
                    factLine.setAD_Org_ID(dl.getOrg_ID());
                }
                //
                if (dl.getAmt().signum() < 0) {
                    factLine.setAmtSource(dLine.getC_Currency_ID(), null, dl.getAmt().abs());
                } else {
                    factLine.setAmtSource(dLine.getC_Currency_ID(), dl.getAmt(), null);
                }
                //  Convert
                factLine.convert();
                //
                String description = distribution.getName() + " #" + dl.getLine();
                if (dl.getDescription() != null) {
                    description += " - " + dl.getDescription();
                }
                factLine.addDescription(description);
                //
                log.info(factLine.toString());
                newLines.add(factLine);
            }
        }	//	for all lines

        //	Add Lines
        for (int i = 0; i < newLines.size(); i++) {
            m_lines.add(newLines.get(i));
        }

        return true;
    }	//	distribute

    /**************************************************************************
     * String representation
     * @return String
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("Fact[");
        sb.append(m_doc.toString());
        sb.append(",").append(m_acctSchema.toString());
        sb.append(",PostType=").append(m_postingType);
        sb.append("]");
        return sb.toString();
    }	//	toString

    /**
     *	Get Lines
     *  @return FactLine Array
     */
    public FactLine[] getLines() {
        FactLine[] temp = new FactLine[m_lines.size()];
        m_lines.toArray(temp);
        return temp;
    }	//	getLines

    /**
     *  Save Fact
     *  @param trxName transaction
     *  @return true if all lines were saved
     */
    public boolean save(String trxName) {
        //  save Lines
        for (int i = 0; i < m_lines.size(); i++) {
            FactLine fl = (FactLine) m_lines.get(i);
            //	log.fine("save - " + fl);
            if (!fl.save(trxName)) //  abort on first error
            {
                return false;
            }
        }
        return true;
    }   //  commit

    /**
     * 	Get Transaction
     *	@return trx
     */
    public String get_TrxName() {
        return m_trxName;
    }	//	getTrxName

    /**
     * 	Set Transaction name
     * 	@param trxName
     */
    public void set_TrxName(String trxName) {
        m_trxName = trxName;
    }	//	set_TrxName

    /**
     * 	Fact Balance Utility
     *	
     *  @author Jorg Janke
     *  @version $Id: Fact.java,v 1.12 2005/11/01 16:38:56 jjanke Exp $
     */
    public class Balance {

        /**
         * 	New Balance
         *	@param dr DR
         *	@param cr CR
         */
        public Balance(BigDecimal dr, BigDecimal cr) {
            DR = dr;
            CR = cr;
        }
        /** DR Amount	*/
        public BigDecimal DR = Env.ZERO;
        /** CR Amount	*/
        public BigDecimal CR = Env.ZERO;

        /**
         * 	Add 
         *	@param dr DR
         *	@param cr CR
         */
        public void add(BigDecimal dr, BigDecimal cr) {
            DR = DR.add(dr);
            CR = CR.add(cr);
        }

        /**
         * 	Get Balance
         *	@return balance
         */
        public BigDecimal getBalance() {
            return DR.subtract(CR);
        }	//	getBalance

        /**
         * 	Get Post Balance
         *	@return absolute balance - negative if reversal
         */
        public BigDecimal getPostBalance() {
            BigDecimal bd = getBalance().abs();
            if (isReversal()) {
                return bd.negate();
            }
            return bd;
        }	//	getPostBalance

        /**
         * 	Zero Balance
         *	@return true if 0
         */
        public boolean isZeroBalance() {
            return getBalance().signum() == 0;
        }	//	isZeroBalance

        /**
         * 	Reversal
         *	@return true if both DR/CR are negative or zero
         */
        public boolean isReversal() {
            return DR.signum() <= 0 && CR.signum() <= 0;
        }	//	isReversal

        /**
         * 	String Representation
         *	@return info
         */
        public String toString() {
            StringBuffer sb = new StringBuffer("Balance[");
            sb.append("DR=").append(DR).append("-CR=").append(CR).append(" = ").append(getBalance()).append("]");
            return sb.toString();
        } //	toString
    }	//	Balance

    /*
     * 	Modificado: GEN-007 por DANIEL GINI 30/06/2009
     *
     *	Creado para dejar todos los registros contables del DEBE
     * antes de los registros del HABER.
     */
    public Fact reordenarFactLine() {
        Fact fact = new Fact(m_doc, m_acctSchema, m_postingType);
        FactLine[] factLine = getLines();

        int i;
        //	Inicialmente se agregan todos los registros al DEBE.
        for (i = 0; i < factLine.length; i++) {
            if (!factLine[i].isCredit()) {
                fact.add(factLine[i]);
            }
        }

        //	Finalmente se agregan todos los registros al HABER.
        for (i = 0; i < factLine.length; i++) {
            if (factLine[i].isCredit()) {
                fact.add(factLine[i]);
            }
        }

        return fact;
    }
}   //  Fact
