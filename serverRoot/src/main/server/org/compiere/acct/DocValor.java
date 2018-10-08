/**
 * ****************************************************************************
 * The contents of this file are subject to the Compiere License Version 1.1 ("License"); You may not use this file
 * except in compliance with the License You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for the specific language governing rights and limitations under the License. The
 * Original Code is Compiere ERP & CRM Smart Business Solution. The Initial Developer of the Original Code is Jorg
 * Janke. Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke. All parts are Copyright (C) 1999-2005
 * ComPiere, Inc. All Rights Reserved. Contributor(s): ______________________________________.
 * ***************************************************************************
 */
package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import org.compiere.model.*;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * Document Percep Line
 *
 * @author DANIEL GINI
 *
 * Creado para registrar contabilidad en cobranzas
 *
 * @version $Id: DocPercep.java,v 1.0 2008/11/20 12:15:02 dgini Exp $
 */
public final class DocValor {

    public DocValor(int C_ValorPago_ID, int C_BankAccount_ID, BigDecimal amount, String tipo) {
        this(C_ValorPago_ID, C_BankAccount_ID, amount, tipo, 0);
    }

    /**
     * Create Percep
     *
     * @param C_Cobranza_Ret_Id retencion
     * @param C_RegReten_Recib_ID name
     * @param amount amount
     */
    public DocValor(int C_ValorPago_ID, int C_BankAccount_ID, BigDecimal amount, String tipo, int C_AcctSchema_ID) {
        m_C_ValorPago_ID = C_ValorPago_ID;

        m_C_BankAccount_ID = C_BankAccount_ID;

        m_amount = amount;

        m_C_AcctSchema_ID = C_AcctSchema_ID;

        switch (tipo.charAt(0)) {
            case 'P':
                m_tipo = ACCTTYPE_PROPIO;
                break;
            /**
             * Ticket 111 - New Valor added PC Banking as 'B' PC Banking has same functionality that Cheque Propio
             *
             * @author Ezequiel Scott @ Zynnia
             */
            case 'B':
                m_tipo = ACCTTYPE_PROPIO;
                break;
            case 'R':
                m_tipo = ACCTTYPE_RECIBIDO;
                break;
            case 'T':
                m_tipo = ACCTTYPE_TRANSF;
                break;
            case 'C':
                m_tipo = ACCTTYPE_EFECTIVO;
                break;
            case 'A':
                m_tipo = ACCTTYPE_EFECTIVO;
                break;
            case 'N':
                m_tipo = ACCTTYPE_CREDITCARD;
                break;
            default:
                m_tipo = ACCTTYPE_ERROR;
        }

    }	//	DocPercep
    /**
     * InvoicePercepcion ID
     */
    private int m_C_ValorPago_ID = 0;
    /**
     * Percepcion ID
     */
    private int m_C_BankAccount_ID = 0;
    private int ACCTTYPE_ERROR = 0;
    private int ACCTTYPE_PROPIO = 1;
    private int ACCTTYPE_RECIBIDO = 2;
    private int ACCTTYPE_TRANSF = 3;
    private int ACCTTYPE_EFECTIVO = 4;
    private int ACCTTYPE_CREDITCARD = 5;
    /**
     * Amount
     */
    private BigDecimal m_amount = null;
    /**
     * Tipo
     */
    private int m_tipo = 0;
    /**
     * Logger
     */
    private static CLogger log = CLogger.getCLogger(DocValor.class);
    private final int m_C_AcctSchema_ID;

    /**
     * Percepcion Acct
     */
    /**
     * Get Account
     *
     * @param AcctType see ACCTTYPE_
     *
     * @param as account schema
     * @return Account
     */
    public MAccount getAccount(int AcctType, MAcctSchema as) {
        int para_1;     //  first parameter (second is always AcctSchema)
        String sql;

        if (AcctType == ACCTTYPE_TRANSF) {
            sql = "SELECT B_InTransit_Acct FROM C_BankAccount_Acct WHERE C_BankAccount_ID=? AND C_AcctSchema_ID=?";
            para_1 = getC_BankAccount_ID();
        } else if (AcctType == ACCTTYPE_PROPIO) {
            sql = "SELECT B_InTransit_Acct FROM C_BankAccount_Acct WHERE C_BankAccount_ID=? AND C_AcctSchema_ID=?";
            para_1 = getC_BankAccount_ID();
        } else if (AcctType == ACCTTYPE_RECIBIDO) {
            sql = "SELECT B_Unidentified_Acct FROM C_AcctSchema_Default WHERE C_AcctSchema_ID=?";
            para_1 = -1;
        } else if (AcctType == ACCTTYPE_EFECTIVO) {
            sql = "SELECT CB_Asset_Acct FROM C_AcctSchema_Default WHERE C_AcctSchema_ID=?";
            para_1 = -1;
        } else if (AcctType == ACCTTYPE_CREDITCARD) {
            MVALORPAGO pay = new MVALORPAGO(Env.getCtx(), m_C_ValorPago_ID, null);
            sql = "SELECT C_Tar_Acct FROM C_Creditcard_Acct WHERE C_Creditcard_ID = ? AND C_AcctSchema_ID = ?";
            para_1 = pay.getC_CREDITCARD_ID();;
        } else {
            return null;
        }

        int validCombination_ID = 0;
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            if (para_1 == -1) { //  GL Accounts
                pstmt.setInt(1, as.getC_AcctSchema_ID());
            } else {
                pstmt.setInt(1, para_1);
                pstmt.setInt(2, as.getC_AcctSchema_ID());
            }
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                validCombination_ID = rs.getInt(1);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "AcctType=" + AcctType + " - SQL=" + sql, e);
            return null;
        }
        if (validCombination_ID == 0) {
            return null;
        }

        MAccount account = MAccount.get(as.getCtx(), validCombination_ID);
        if (AcctType == ACCTTYPE_EFECTIVO) {
            // Si es efectivo clono las propiedades de la combinacion por defecto
            return MAccount.get(as.getCtx(), account.getAD_Client_ID(), account.getAD_Org_ID(), account.getC_AcctSchema_ID(),
                    m_C_AcctSchema_ID, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }
        return account;
    }   //  getAccount

    /**
     * Get Amount
     *
     * @return amount
     */
    public BigDecimal getAmount() {
        return m_amount;
    }	//getAmount

    /**
     * Get Name of Percepcion
     *
     * @return name
     */
    public int getValueType() {
        return m_tipo;
    }	 //getValueType

    /**
     * Get C_InvoicePercep_ID
     *
     * @return percepcion id
     */
    public int getC_ValorPago_ID() {
        return m_C_ValorPago_ID;
    }	//	getC_PaymentValores_Id

    /**
     * Return String representation
     *
     * @return percepcion name and base amount
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("Valor=(");
        sb.append(m_tipo);
        sb.append(" Amt=").append(m_amount);
        sb.append(")");
        return sb.toString();
    }	//	toString

    public int getC_BankAccount_ID() {
        return m_C_BankAccount_ID;
    }	//	getC_BankAccount_ID

    public String getDescription() {
        MVALORPAGO pay = new MVALORPAGO(Env.getCtx(), m_C_ValorPago_ID, null);

        if (getValueType() == ACCTTYPE_TRANSF) { //Banco_Número
            MBankAccount bank = MBankAccount.get(Env.getCtx(), m_C_BankAccount_ID);
            return bank.getName() + "_" + pay.getNroTransferencia();
        } else if (getValueType() == ACCTTYPE_PROPIO) { //Banco_Número_Fecha Vto
            MBankAccount bank = MBankAccount.get(Env.getCtx(), m_C_BankAccount_ID);
            if (pay.getVencimientoDate() != null) {
                return bank.getName() + "_" + pay.getNroCheque() + "_" + pay.getVencimientoDate().toString();
            }
            return bank.getName() + "_" + pay.getNroCheque();
        } else if (getValueType() == ACCTTYPE_RECIBIDO) { //Banco_Número_Fecha Vto
            MPAYMENTVALORES rec = new MPAYMENTVALORES(Env.getCtx(), pay.getC_PAYMENTVALORES_ID(), null);
            if (rec.getVencimientoDate() != null) {
                return rec.getBank() + "_" + rec.getNroCheque() + "_" + rec.getVencimientoDate().toString();
            }
            return rec.getBank() + "_" + rec.getNroCheque();
        } else if (getValueType() == ACCTTYPE_CREDITCARD) { //Banco_Tarjeta
            MBankAccount bank = MBankAccount.get(Env.getCtx(), m_C_BankAccount_ID);
            X_C_CREDITCARD card = new X_C_CREDITCARD(Env.getCtx(), pay.getC_CREDITCARD_ID(), null);
            return bank.getName() + "_" + card.getName();
        }
        return "";
    }	//	getDescription
}	//	DocValue
