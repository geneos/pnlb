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

import java.math.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * Document Percep Line
 *
 * @author DANIEL GINI
 *
 * Creado para registrar contabilidad en cobranzas
 *
 * @version $Id: DocPercep.java,v 1.0 2008/11/20 12:15:02 dgini Exp $
 */
public final class DocLine_Debito {

    /**
     * Create Percep
     *
     * @param C_Cobranza_Ret_Id retencion
     * @param C_RegReten_Recib_ID name
     * @param amount amount
     */
    public DocLine_Debito(int C_MovimientoFondos_Id, int C_MovimientoFondos_xxx_Id, BigDecimal amount, String tipo, boolean isDeb) {
        m_C_MovimientoFondos_Id = C_MovimientoFondos_Id;

        m_C_MovimientoFondos_xxx_Id = C_MovimientoFondos_xxx_Id;

        m_isDebito = isDeb;

        m_amount = amount;

        switch (tipo.charAt(0)) {
            case 'D':
                m_tipo = ACCTTYPE_DEBITO;
                break;
            case 'C':
                m_tipo = ACCTTYPE_CREDITO;
                break;
            default:
                m_tipo = ACCTTYPE_ERROR;
        }

    }	//	DocPercep
    /**
     * m_C_MovimientoFondos_Id
     */
    private int m_C_MovimientoFondos_Id = 0;
    /**
     * m_C_MovimientoFondos_deb_Id or m_C_MovimientoFondos_cre_Id
     */
    private int m_C_MovimientoFondos_xxx_Id = 0;
    private boolean m_isDebito = false;
    private int ACCTTYPE_ERROR = 0;
    private int ACCTTYPE_DEBITO = 1;
    private int ACCTTYPE_CREDITO = 2;
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
    private static CLogger log = CLogger.getCLogger(DocLine_Debito.class);

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
        int para_1 = 0;     //  first parameter (second is always AcctSchema)
        String sql = null;

        if (AcctType == ACCTTYPE_DEBITO) {
            //sql = "SELECT MV_DEBITO_ACCT FROM C_MovimientoFondos_Deb WHERE C_MovimientoFondos_Deb_ID = ? AND C_AcctSchema_ID=?";
            sql = " SELECT cb.C_VALIDCOMBINATION_ID FROM C_VALIDCOMBINATION cb, C_MovimientoFondos_Deb mf "
                  + " WHERE cb.account_ID=mf.MV_DEBITO_ACCT and mf.C_MovimientoFondos_Deb_ID=? AND mf.C_AcctSchema_ID=? AND mf.AD_ORG_ID=cb.AD_ORG_ID";

            para_1 = getC_MovimientoFondos_xxx_Id();
        } else if (AcctType == ACCTTYPE_CREDITO) {
            //sql = "SELECT MV_CREDITO_ACCT FROM C_MovimientoFondos_Cre WHERE C_MovimientoFondos_Cre_ID = ? AND C_AcctSchema_ID=?";
            sql = " SELECT cb.C_VALIDCOMBINATION_ID FROM C_VALIDCOMBINATION cb, C_MovimientoFondos_Cre mf "
                  + " WHERE cb.account_ID=mf.MV_CREDITO_ACCT and mf.C_MovimientoFondos_Cre_ID=? AND mf.C_AcctSchema_ID=? AND mf.AD_ORG_ID=cb.AD_ORG_ID";

            para_1 = getC_MovimientoFondos_xxx_Id();
        } else {
            return null;
        }

        int validCombination_ID = 0;
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            if (para_1 == -1) //  GL Accounts
            {
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

        return MAccount.get(as.getCtx(), validCombination_ID);
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

    public int getC_MovimientoFondos_xxx_Id() {
        return m_C_MovimientoFondos_xxx_Id;
    }	//	getC_MovimientoFondos_xxx_Id

    /**
     * Get C_InvoicePercep_ID
     *
     * @return percepcion id
     */
    public int getC_MovimientoFondos_Id() {
        return m_C_MovimientoFondos_Id;
    }	//	getC_MovimientoFondos_Id

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

    public String getDescription() {
        MMOVIMIENTOFONDOS movFondos = new MMOVIMIENTOFONDOS(Env.getCtx(), m_C_MovimientoFondos_Id, null);
        String detalle = "";
        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), movFondos.getTIPO());

        
        if (dynMovFondos == null) {
            
            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_DepCheque) && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MPAYMENTVALORES valor = new MPAYMENTVALORES(Env.getCtx(), movCre.getC_PAYMENTVALORES_ID(), null);
                detalle = movCre.getBank() + "_" + valor.getNroCheque() + "_" + movCre.getPaymentDate();
            }

            if ((movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_RechCqTercero)
                || movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_VencCqTercero)) && m_isDebito) {
                MMOVIMIENTOFONDOSDEB movDeb = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MPAYMENTVALORES valor = new MPAYMENTVALORES(Env.getCtx(), movDeb.getC_PAYMENTVALORES_ID(), null);
                detalle = movDeb.getBank() + "_" + valor.getNroCheque() + "_" + movDeb.getPaymentDate();
            }

            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_TraBancaria) && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movCre.getC_BankAccount_ID(), null);
                detalle = bank.getName();
                if (movCre.getNroTransferencia() != null) {
                    detalle += "_" + movCre.getNroTransferencia();
                }
            }

            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_RechCqPropio)) {
                if (m_isDebito) {
                    MMOVIMIENTOFONDOSDEB movDeb = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                    MVALORPAGO valor = new MVALORPAGO(Env.getCtx(), movDeb.getC_VALORPAGO_ID(), null);
                    MBankAccount bank = new MBankAccount(Env.getCtx(), movDeb.getC_BankAccount_ID(), null);
                    detalle = bank.getName() + "_" + valor.getNroCheque() + "_" + movDeb.getPaymentDate();
                }
            }

            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_CambioCheque) && m_isDebito) {
                MMOVIMIENTOFONDOSDEB movDeb = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                detalle = movDeb.getBank() + "_" + movDeb.getNroCheque() + "_" + movDeb.getPaymentDate();
            }

            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_CambioCheque) && !m_isDebito) {
                MMOVIMIENTOFONDOSDEB movDeb = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                detalle = movDeb.getBank() + "_" + movDeb.getNroCheque() + "_" + movDeb.getPaymentDate();
            }

            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_CreditoBancario) && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                if (movCre.getDescription() != null) {
                    detalle = movCre.getDescription();
                }
            }

            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_EmiCheque) && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movCre.getC_BankAccount_ID(), null);
                detalle = bank.getName() + "_" + movCre.getNroCheque() + "_" + movCre.getPaymentDate();
            }

            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_VencCqPropio) && m_isDebito) {
                MMOVIMIENTOFONDOSDEB movDeb = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MVALORPAGO valor = new MVALORPAGO(Env.getCtx(), movDeb.getC_VALORPAGO_ID(), null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movDeb.getC_BankAccount_ID(), null);
                detalle = bank.getName() + "_" + valor.getNroCheque() + "_" + movDeb.getPaymentDate();
            }

            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_DepositoPendiente) && m_isDebito) {
                MMOVIMIENTOFONDOSDEB movDev = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movDev.getC_BankAccount_ID(), null);
                detalle = bank.getName();
            }

            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_DepositoPendiente) && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movCre.getC_BankAccount_ID(), null);
                detalle = bank.getName();
            }

            /*
            *   Agregado por Zynnia 16/02/2012 
            *   Requerimiento nuevo tipo de movimientos
            */
            
            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias) && m_isDebito) {
                MMOVIMIENTOFONDOSDEB movDev = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movDev.getC_BankAccount_ID(), null);
                detalle = bank.getName();
            }

            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias) && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movCre.getC_BankAccount_ID(), null);
                detalle = bank.getName();
            }

            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_ValNegociados) && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MPAYMENTVALORES valor = new MPAYMENTVALORES(Env.getCtx(), movCre.getC_PAYMENTVALORES_ID(), null);
                detalle = movCre.getBank() + "_" + valor.getNroCheque() + "_" + movCre.getPaymentDate();
            }

            if (movFondos.getTIPO().equals(MMOVIMIENTOFONDOS.TIPO_CancelacionCesionFacturas) && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MPAYMENTVALORES valor = new MPAYMENTVALORES(Env.getCtx(), movCre.getC_PAYMENTVALORES_ID(), null);
                detalle = movCre.getBank() + "_" + valor.getNroCheque() + "_" + movCre.getPaymentDate();
            }            
            
        } else {
            
            if (dynMovFondos.isCRE_CHEQUE_DEPO() && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MPAYMENTVALORES valor = new MPAYMENTVALORES(Env.getCtx(), movCre.getC_PAYMENTVALORES_ID(), null);
                detalle = movCre.getBank() + "_" + valor.getNroCheque() + "_" + movCre.getPaymentDate();
            }

            if ((dynMovFondos.isDEB_CHEQUE_TER_RECH() || dynMovFondos.isDEB_CHEQUE_TER_VENC()) && m_isDebito) {
                MMOVIMIENTOFONDOSDEB movDeb = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MPAYMENTVALORES valor = new MPAYMENTVALORES(Env.getCtx(), movDeb.getC_PAYMENTVALORES_ID(), null);
                detalle = movDeb.getBank() + "_" + valor.getNroCheque() + "_" + movDeb.getPaymentDate();
            }

            if (dynMovFondos.isCRE_TRANSFERENCIA() && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movCre.getC_BankAccount_ID(), null);
                detalle = bank.getName();
                if (movCre.getNroTransferencia() != null) {
                    detalle += "_" + movCre.getNroTransferencia();
                }
            }

            if (dynMovFondos.isDEB_CHEQUE_PRO_RECH()) {
                if (m_isDebito) {
                    MMOVIMIENTOFONDOSDEB movDeb = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                    MVALORPAGO valor = new MVALORPAGO(Env.getCtx(), movDeb.getC_VALORPAGO_ID(), null);
                    MBankAccount bank = new MBankAccount(Env.getCtx(), movDeb.getC_BankAccount_ID(), null);
                    detalle = bank.getName() + "_" + valor.getNroCheque() + "_" + movDeb.getPaymentDate();
                }
            }

            if (dynMovFondos.isDEB_CHEQUE_REC() && m_isDebito) {
                MMOVIMIENTOFONDOSDEB movDeb = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                detalle = movDeb.getBank() + "_" + movDeb.getNroCheque() + "_" + movDeb.getPaymentDate();
            }

            if (dynMovFondos.isCRE_CHEQUE_RECI() && !m_isDebito) {
                MMOVIMIENTOFONDOSDEB movDeb = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                detalle = movDeb.getBank() + "_" + movDeb.getNroCheque() + "_" + movDeb.getPaymentDate();
            }

            if (dynMovFondos.isCRE_CUENTA_BANCO_DESC() && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                if (movCre.getDescription() != null) {
                    detalle = movCre.getDescription();
                }
            }

            if (dynMovFondos.isCRE_CHEQUE_PROPIO() && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movCre.getC_BankAccount_ID(), null);
                detalle = bank.getName() + "_" + movCre.getNroCheque() + "_" + movCre.getPaymentDate();
            }

            if (dynMovFondos.isDEB_CHEQUE_PRO_VENC() && m_isDebito) {
                MMOVIMIENTOFONDOSDEB movDeb = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MVALORPAGO valor = new MVALORPAGO(Env.getCtx(), movDeb.getC_VALORPAGO_ID(), null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movDeb.getC_BankAccount_ID(), null);
                detalle = bank.getName() + "_" + valor.getNroCheque() + "_" + movDeb.getPaymentDate();
            }

            if (dynMovFondos.isDEB_CUENTA_BANCO() && m_isDebito) {
                MMOVIMIENTOFONDOSDEB movDev = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movDev.getC_BankAccount_ID(), null);
                detalle = bank.getName();
            }

            if (dynMovFondos.isCRE_DEPOSITO_PEND() && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movCre.getC_BankAccount_ID(), null);
                detalle = bank.getName();
            }

            /*
            *   Agregado por Zynnia 16/02/2012 
            *   Requerimiento nuevo tipo de movimientos
            */
            
            if (dynMovFondos.isDEB_CUENTA_BANCO() && m_isDebito) {
                MMOVIMIENTOFONDOSDEB movDev = new MMOVIMIENTOFONDOSDEB(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movDev.getC_BankAccount_ID(), null);
                detalle = bank.getName();
            }

            if (dynMovFondos.isCRE_DEPOSITO_PEND() && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MBankAccount bank = new MBankAccount(Env.getCtx(), movCre.getC_BankAccount_ID(), null);
                detalle = bank.getName();
            }

            if (dynMovFondos.isCRE_VALORES_NEG() && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MPAYMENTVALORES valor = new MPAYMENTVALORES(Env.getCtx(), movCre.getC_PAYMENTVALORES_ID(), null);
                detalle = movCre.getBank() + "_" + valor.getNroCheque() + "_" + movCre.getPaymentDate();
            }

            if (dynMovFondos.isCRE_CANCEL_FACT() && !m_isDebito) {
                MMOVIMIENTOFONDOSCRE movCre = new MMOVIMIENTOFONDOSCRE(Env.getCtx(), m_C_MovimientoFondos_xxx_Id, null);
                MPAYMENTVALORES valor = new MPAYMENTVALORES(Env.getCtx(), movCre.getC_PAYMENTVALORES_ID(), null);
                detalle = movCre.getBank() + "_" + valor.getNroCheque() + "_" + movCre.getPaymentDate();
            }                
            
        }

        return detalle;
        
    }	//	getDescription
    
}	//	DocLine_Debito
