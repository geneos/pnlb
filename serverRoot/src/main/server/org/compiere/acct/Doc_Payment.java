/**
 * ****************************************************************************
 * The contents of this file are subject to the Compiere License Version 1.1 ("License"); You may not use this file
 * except in compliance with the License You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for the specific language governing rights and limitations under the License. The
 * Original Code is Compiere ERP & CRM Smart Business Solution. The Initial Developer of the Original Code is Jorg
 * Janke. Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke. All parts are Copyright (C) 1999-2005
 * ComPiere, Inc. All Rights Reserved. Contributor(s): ______________________________________.
 ****************************************************************************
 */
package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.compiere.model.*;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * Post Invoice Documents.
 * <pre>
 *  Table:              C_Payment (335)
 *  Document Types      ARP, APP
 * </pre>
 *
 * @author Jorg Janke
 * @version $Id: Doc_Payment.java,v 1.7 2005/10/26 00:40:02 jjanke Exp $
 */
public class Doc_Payment extends Doc {

    /**
     * Constructor
     *
     * @param ass accounting schemata
     * @param rs record @parem trxName trx
     */
    protected Doc_Payment(MAcctSchema[] ass, ResultSet rs, String trxName) {
        super(ass, MPayment.class, rs, null, trxName);
    }	//	Doc_Payment
    /**
     * Tender Type
     */
    private String m_TenderType = null;
    /**
     * Rate value
     */
    BigDecimal m_rate = BigDecimal.ONE;
    /**
     * Bank Account
     */
    private int m_C_BankAccount_ID = 0;
    /**
     * Contained Optional Retencion Lines
     */
    private DocReten[] m_reten = null;
    /**
     * Contained Optional Valores Lines
     */
    private DocValue[] m_value = null;
    /**
     * Contained Optional Valores Lines
     */
    private DocValor[] m_valor = null;

    /**
     * Load Specific Document Details
     *
     * @return error message or null
     */
    protected String loadDocumentDetails() {
        MPayment pay = (MPayment) getPO();
        setDateDoc(pay.getDateTrx());
        m_TenderType = pay.getTenderType();
        //calculateRate(pay);
        m_rate = pay.getCotizacion();

        if (pay.isReceipt()) {
            m_value = loadValoresCobranza();
            m_reten = loadRetencion();
        } else {
            m_valor = loadValoresPago();
        }

        setAmount(Doc.AMTTYPE_Gross, pay.getPayAmt());

        return null;
    }   //  loadDocumentDetails

    /**
     * CREADO REQ-065
     *
     * @author Daniel
     * @return rate
     *
     */
    /*
     * private void calculateRate(MPayment pay) { //T O D O GETCOTIZACION de la clase.
     *
     * try { String sql = "Select COTIZACION from C_Payment Where C_Payment_ID = " + pay.getC_Payment_ID();
     * PreparedStatement pstm = DB.prepareStatement(sql, getTrxName()); ResultSet rs = pstm.executeQuery(); if
     * (rs.next() && rs.getBigDecimal(1)!=null) m_rate = rs.getBigDecimal(1); } catch (Exception e) {
     * e.printStackTrace();	}; }
     */
    /**
     * Agregado por DANIEL GINI @BISion It Solutions
     */
    private DocValue[] loadValoresCobranza() {
        ArrayList<DocValue> list = new ArrayList<DocValue>();

        String sql = "SELECT C_PaymentValores_Id, C_BankAccount_ID, C_Currency_ID, C_Charge_ID, TIPO "
                + "FROM C_PaymentValores "
                + "WHERE C_Payment_ID=? AND IsActive = 'Y'";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, getTrxName());
            pstmt.setInt(1, get_ID());
            ResultSet rs = pstmt.executeQuery();
            //
            while (rs.next()) {
                int C_PaymentValores_Id = rs.getInt(1);
                int C_BankAccount_ID = rs.getInt(2);
                int C_Currency_ID = rs.getInt(3);
                if (C_Currency_ID == 0) {
                    C_Currency_ID = getC_Currency_ID();
                }
                int C_Charge_ID = rs.getInt(4);
                String tipo = rs.getString(5);

                //
                DocValue valueLine = new DocValue(C_PaymentValores_Id, C_BankAccount_ID, C_Currency_ID, C_Charge_ID, tipo);
                log.fine(valueLine.toString());
                list.add(valueLine);
            }
            //
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql, e);
            return null;
        }

        //	Return Array
        DocValue[] tl = new DocValue[list.size()];
        list.toArray(tl);
        return tl;
    }

    /**
     * Agregado por DANIEL GINI @BISion It Solutions
     *
     * REQ-037
     */
    private DocValor[] loadValoresPago() {
        ArrayList<DocValor> list = new ArrayList<DocValor>();

        String sql = "SELECT C_ValorPago_Id, C_BankAccount_ID, Importe, Mextranjera, TIPO, C_AcctSchema_ID "
                + "FROM C_ValorPago "
                + "WHERE C_Payment_ID=? AND IsActive = 'Y'";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, getTrxName());
            pstmt.setInt(1, get_ID());
            ResultSet rs = pstmt.executeQuery();
            //
            while (rs.next()) {
                int C_ValorPago_ID = rs.getInt(1);
                int C_BankAccount_ID = rs.getInt(2);

                /*
                 * Agregado por Zynnia 16/02/2012
                 *
                 * Modificación para que el registro de los asientos se realice correctamente según la moneda sea ARS o
                 * extranjera (como fuente debe pasarse el valor en la extranjera ya que luego el asiento lo convierte).
                 *
                 * Ticket Problema #181 de error en registración contable (reflejo en el mayor)
                 *
                 */

                BigDecimal amount = Env.ZERO;

                if (!(rs.getBigDecimal(4).compareTo(Env.ZERO) == 0)) {
                    amount = rs.getBigDecimal(4);
                } else {
                    amount = rs.getBigDecimal(3);
                }

                String tipo = rs.getString(5);
                int account = rs.getInt(6);
                //
                DocValor valorLine = new DocValor(C_ValorPago_ID, C_BankAccount_ID, amount, tipo, account);
                log.fine(valorLine.toString());
                list.add(valorLine);
            }
            //
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql, e);
            return null;
        }

        //	Return Array
        DocValor[] tl = new DocValor[list.size()];
        list.toArray(tl);
        return tl;
    }

    /**
     * Agregado por DANIEL GINI @BISion It Solutions
     *
     * Load Invoice Retenciones
     *
     * @return DocReten Array
     */
    private DocReten[] loadRetencion() {
        ArrayList<DocReten> list = new ArrayList<DocReten>();

        String sql = "SELECT C_Cobranza_Ret_Id, C_RegReten_Recib_ID, Importe "
                + "FROM C_Cobranza_Ret "
                + "WHERE C_Payment_ID=? AND IsActive = 'Y'";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, getTrxName());
            pstmt.setInt(1, get_ID());
            ResultSet rs = pstmt.executeQuery();
            //
            while (rs.next()) {
                int C_Cobranza_Ret_Id = rs.getInt(1);
                int C_RegReten_Recib_ID = rs.getInt(2);
                BigDecimal amount = rs.getBigDecimal(3);

                DocReten retenLine = new DocReten(C_Cobranza_Ret_Id, C_RegReten_Recib_ID, amount);
                log.fine(retenLine.toString());
                list.add(retenLine);
            }
            //
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql, e);
            return null;
        }

        //	Return Array
        DocReten[] tl = new DocReten[list.size()];
        list.toArray(tl);
        return tl;
    }	//	loadRetencion

    /**
     * ************************************************************************
     * Get Source Currency Balance - always zero
     *
     * @return Zero (always balanced)
     */
    public BigDecimal getBalance() {
        BigDecimal retValue = Env.ZERO;
        //	log.config( toString() + " Balance=" + retValue);
        return retValue;
    }   //  getBalance

    /**
     * Create Facts (the accounting logic) for ARP, APP.
     * <pre>
     *  ARP
     *      BankInTransit   DR
     *      UnallocatedCash         CR
     *      or Charge/C_Prepayment
     *  APP
     *      PaymentSelect   DR
     *      or Charge/V_Prepayment
     *      BankInTransit           CR
     *  CashBankTransfer
     *      -
     * </pre>
     *
     * @param as accounting schema
     * @return Fact
     */
    public ArrayList<Fact> createFacts(MAcctSchema as) {
        //  create Fact Header
        Fact fact = new Fact(this, as, Fact.POST_Actual);
        //	Cash Transfer
        if ("X".equals(m_TenderType)) {
            ArrayList<Fact> facts = new ArrayList<Fact>();
            facts.add(fact);
            return facts;
        }

        int AD_Org_ID = getBank_Org_ID();		//	Bank Account Org
        if (getDocumentType().equals(DOCTYPE_ARReceipt)) {
            /**
             * -------ANTES-------- //	Asset FactLine fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_BankInTransit,
             * as), getC_Currency_ID(), getAmount(), null); if (fl != null && AD_Org_ID != 0)
             * fl.setAD_Org_ID(AD_Org_ID); // MAccount acct = null; if (getC_Charge_ID() != 0) acct =
             * MCharge.getAccount(getC_Charge_ID(), as, getAmount()); else if (m_Prepayment) acct =
             * getAccount(Doc.ACCTTYPE_C_Prepayment, as); else acct = getAccount(Doc.ACCTTYPE_UnallocatedCash, as); fl =
             * fact.createLine(null, acct, getC_Currency_ID(), null, getAmount()); if (fl != null && AD_Org_ID != 0 &&
             * getC_Charge_ID() == 0)	//	don't overwrite charge fl.setAD_Org_ID(AD_Org_ID);
             */
            FactLine fl;

            /*
             * Asset - Efectivo/Banco/Moneda Extranjera/Cheque/Bono OS DR Para obtener el monto, interviene el Esquema
             * Contable para diferenciar si se trabaja con la misma moneda o no.
             */
            for (int i = 0; i < m_value.length; i++) {
                fl = fact.createLine(null, m_value[i].getAccount(m_value[i].getValueType(), as),
                        m_rate, getC_Currency_ID(), m_value[i].getAmount(as), null);
                if (fl != null) {
                    fl.setDescription(m_value[i].getDescription());
                    fl.setAD_Org_ID(AD_Org_ID);
                    fl.save(getTrxName());
                }
            }

            // Retenciones DR
            /**
             * Agregado por DANIEL GINI @BISion It Solutions
             *
             * Contabilidad a Retenciones Recibidas.
             *
             * INICIO
             */
            for (int i = 0; i < m_reten.length; i++) {
                fl = fact.createLine(null, m_reten[i].getAccount(m_reten[i].getAPRetencionType(), as),
                        m_rate, getC_Currency_ID(), m_reten[i].getAmount(), null);
                if (fl == null)
                    return null;
            }

            /**
             * FIN
             */
            // Anticipo de Clientes CR
            fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_C_Prepayment, as),
                    m_rate, getC_Currency_ID(), null, getAmount());
            if (fl != null && AD_Org_ID != 0) {
                fl.setAD_Org_ID(AD_Org_ID);
            }

        } //  APP
        else if (getDocumentType().equals(DOCTYPE_APPayment)) {

            /**
             * MODIFICADO POR BISion 01/12/08 DANIEL GINI - CAMBIO DE CUENTAS
             *
             * INICIO
             */
            MPayment mPay = (MPayment) getPO();

            FactLine fl;

            if (getC_Charge_ID() != 0) {
                fl = fact.createLine(null, MCharge.getAccount(getC_Charge_ID(), as, getAmount()),
                        m_rate, getC_Currency_ID(), getAmount(), null); //getAmount()
            } /*
             * else if (m_Prepayment) { fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_V_Prepayment, as),
             * getC_Currency_ID(), m_rate, getAmount(), null); //getAmount() } else { BigDecimal ir =
             * impRemanente(mPay);
             *
             * fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_PaymentSelect, as), getC_Currency_ID(), m_rate,
             * getAmount().subtract(ir), null); //getAmount() fl = fact.createLine(null,
             * getAccount(Doc.ACCTTYPE_V_Prepayment, as), getC_Currency_ID(), m_rate, ir, null); //getAmount() }
             */ else // Anticipo de Clientes DR
            {
                /**
                 * Add for select correct account of Partner for payment
                 * Corrección para que tome ACCTTYPE_V_MExterno siendo que estaba tomanco ACCTTYPE_C_MExterno (clientes)
                 *
                 * @author Alejandro Scott @ Zynnia
                 */
                MAccount tgtAccount = null;
                if (mPay.isPrepayment() || (mPay.getAllocate() != null && mPay.getAllocate().isEmpty())) {
                    // The payment hasn't allocate then is prepaymente
                    tgtAccount = getAccount(Doc.ACCTTYPE_V_Prepayment, as);
                } else {
                    if ("ARS".equals(mPay.getCurrencyISO())) {
                        tgtAccount = getAccount(Doc.ACCTTYPE_V_Liability, as);
                    } else {
                        tgtAccount = getAccount(Doc.ACCTTYPE_V_MExterno, as);
                    }
                }
//				fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_V_Prepayment, as),
//						 m_rate, getC_Currency_ID(), getAmount(), null);
                fl = fact.createLine(null, tgtAccount,
                        m_rate, getC_Currency_ID(), getAmount(), null);
            }

            if (fl != null && AD_Org_ID != 0 && getC_Charge_ID() == 0) //	don't overwrite charge
            {
                fl.setAD_Org_ID(AD_Org_ID);
            }

            /**
             * FIN
             */
            
            // Retenciones CR
      
            // Moneda del pago
            MCurrency moneda = MCurrency.get (Env.getCtx(), mPay.getC_Currency_ID());                
     
            //Monto retenciones 
            BigDecimal montoRetRound = mPay.getRetencionGanancias().divide(mPay.getCotizacion(),moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            
            
            /*
             *  Zynnia - 04/06/2012
             *  Agregado para que no genere registros en caso de retenciones con valor 0.
             *  JF
             * 
             */
            
            if (!(montoRetRound.compareTo(Env.ZERO) == 0)){
                
                if (moneda != null) {                    
                     montoRetRound = montoRetRound.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                }
                
                fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_WithholdingPaymentGan, as),
                    m_rate, getC_Currency_ID(), null, montoRetRound);            
                if (fl != null && AD_Org_ID != 0)
                    fl.setAD_Org_ID(AD_Org_ID);
                
            }    
                
            /*
             * GENEOS - Pablo Velazquez
             * Modificacion para que se cree una linea por cada retencion IB del pago
             * Ticket 1085
             */
            List<PO> retenciones = mPay.getRetenciones();
            for (int r = 0 ; r<retenciones.size() ; r++) {
                MPAYMENTRET aPaymentRet = (MPAYMENTRET) retenciones.get(r);
                if ( !aPaymentRet.getTIPO_RET().equals(MPAYMENTRET.TIPO_RET_IB) )
                    continue;
                BigDecimal montoRetIBRound = aPaymentRet.getIMPORTE().divide(mPay.getCotizacion(),moneda.getStdPrecision() ,BigDecimal.ROUND_HALF_UP);

                //Las retenciones no deben ser con monto 0
                if (!(montoRetIBRound.compareTo(Env.ZERO) == 0)){
                    
                    if (moneda != null) {                    
                        montoRetIBRound = montoRetIBRound.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                    }
                    
                    // La cuenta depende de la jurisdiccion de la retencion
                    int C_JURISDICCION_ID = Retenciones.getJurisdiccion(aPaymentRet.getC_DocType_ID());
                    MAccount acct = getAccountPRetenIB(C_JURISDICCION_ID, as);
                    if (acct == null) {
                        acct = getAccount(Doc.ACCTTYPE_WithholdingPaymentIB, as);
                    }      

                    fl = fact.createLine(null, acct, m_rate, getC_Currency_ID(), null, montoRetIBRound);
                    if (fl != null && AD_Org_ID != 0) 
                        fl.setAD_Org_ID(AD_Org_ID);
                }
               
                
            }
            
            
            BigDecimal montoRetIVARound = mPay.getRetencionIVA().divide(mPay.getCotizacion(),moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);

            /*
             *  Zynnia - 04/06/2012
             *  Agregado para que no genere registros en caso de retenciones con valor 0.
             *  JF
             * 
             */
            
            if (!(montoRetIVARound.compareTo(Env.ZERO) == 0) ){
            
                if (moneda != null) {                    
                     montoRetIVARound = montoRetIVARound.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                }

                fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_WithholdingPaymentIVA, as),
                        m_rate, getC_Currency_ID(), null, montoRetIVARound);
                if (fl != null && AD_Org_ID != 0)
                    fl.setAD_Org_ID(AD_Org_ID);  
                
            }            


            BigDecimal montoRetSUSSRound = mPay.getRetencionSUSS().divide(mPay.getCotizacion(),moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);

            /*
             *  Zynnia - 04/06/2012
             *  Agregado para que no genere registros en caso de retenciones con valor 0.
             *  JF
             * 
             */
            
            if (!(montoRetSUSSRound.compareTo(Env.ZERO) == 0)){            
            
                if (moneda != null) {                    
                     montoRetSUSSRound = montoRetSUSSRound.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                }

                fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_WithholdingPaymentSUSS, as),
                        m_rate, getC_Currency_ID(), null, montoRetSUSSRound);

                if (fl != null && AD_Org_ID != 0)
                    fl.setAD_Org_ID(AD_Org_ID);
                
            }           

            

            /**
             * FIN
             */
            

            /*
             * //	Asset - - - - ANTES
             *
             * fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_BankInTransit, as), getC_Currency_ID(), null,
             * mPay.getPAYNET()); if (fl != null && AD_Org_ID != 0) fl.setAD_Org_ID(AD_Org_ID);
             *
             */
            //	Asset - Cheque Propio/Cheque Recibido/Banco DR
            for (int i = 0; i < m_valor.length; i++) {
                fl = fact.createLine(null, m_valor[i].getAccount(m_valor[i].getValueType(), as),
                        m_rate, getC_Currency_ID(), null, m_valor[i].getAmount());
                if (fl != null) {
                    fl.setDescription(m_valor[i].getDescription());
                    fl.setAD_Org_ID(AD_Org_ID);
                    fl.save(getTrxName());
                }
            }
        } else {
            p_Error = "DocumentType unknown: " + getDocumentType();
            log.log(Level.SEVERE, p_Error);
            fact = null;
        }
        //
        ArrayList<Fact> facts = new ArrayList<Fact>();
        facts.add(fact);
        return facts;
    }   //  createFact

    private MAccount getAccountPRetenIB(int C_JURISDICCION_ID, MAcctSchema as) {
        String sql = "SELECT A_RETEN_ACCT "
                + "FROM C_Jurisdiccion_Acct "
                + "WHERE C_Jurisdiccion_ID=? and C_AcctSchema_ID = ? AND IsActive = 'Y'";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, C_JURISDICCION_ID);
            pstmt.setInt(2, as.getC_AcctSchema_ID());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return MAccount.get(as.getCtx(), rs.getInt(1));
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql, e);
            return null;
        }
        return null;
    }

    /**
     * Get AD_Org_ID from Bank Account
     *
     * @return AD_Org_ID or 0
     */
    private int getBank_Org_ID() {
        if (m_C_BankAccount_ID == 0) {
            return 0;
        }
        //
        MBankAccount ba = MBankAccount.get(getCtx(), m_C_BankAccount_ID);
        return ba.getAD_Org_ID();
    }	//	getBank_Org_ID

    /*
     * private BigDecimal impRemanente(MPayment mPay) { String sql;
     *
     * try {
     *
     * if (mPay.getC_Invoice_ID() == 0) {
     *
     * sql = "SELECT C_Invoice_Id " + "FROM C_PaymentAllocate " + "WHERE C_Payment_ID = ?";
     *
     * PreparedStatement pstmt = DB.prepareStatement(sql, null); pstmt.setLong(1, mPay.getC_Payment_ID());
     *
     * ResultSet rs = pstmt.executeQuery();
     *
     * if (rs.next()) { sql = "SELECT MAX(p.PayAmt)-COALESCE(SUM(a.Amount),0) " + "FROM C_Payment p " + "LEFT OUTER JOIN
     * C_PaymentAllocate a ON (p.C_Payment_ID=a.C_Payment_ID) " + "WHERE p.C_Payment_ID = ?";
     *
     * pstmt = DB.prepareStatement(sql, null); pstmt.setLong(1, mPay.getC_Payment_ID());
     *
     * rs = pstmt.executeQuery();
     *
     * if (rs.next())	{ return rs.getBigDecimal(1); } } } } catch (SQLException esql){
     *
     * }
     * return mPay.getOverUnderAmt(); }
     */
}   //  Doc_Payment
