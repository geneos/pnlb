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
package org.compiere.model;

import java.sql.*;
import java.util.*;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.ValueFormat;

/**
 * Callout for Allocate Payments
 *
 * @author Jorg Janke
 * @version $Id: CalloutPaymentAllocate.java,v 1.2 2005/11/06 01:17:27 jjanke Exp $
 */
public class CalloutMovimietoFondos extends CalloutEngine {

    /**
     * REQ-040 - 14/01/2009 - Daniel Gini
     *
     * Dependiendo del Movimiento seleccionado, se carga el nro de documento
     *
     */
    public String setNroComp(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        //	Get Info from Tab
        String Tipo = (String) mTab.getValue("TIPO");

        try {
            String sql = "SELECT dt.C_DocType_ID, sq.CurrentNext "
                         + "FROM C_DocType dt LEFT JOIN AD_SEQUENCE sq ON (dt.DocNoSequence_ID = sq.AD_Sequence_ID) "
                         + "WHERE PrintName = 'MVF " + Tipo + "'";

            PreparedStatement pstm = DB.prepareStatement(sql, null);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                mTab.setValue("C_DocType_ID", rs.getInt(1));
                mTab.setValue("DocumentNo", rs.getString(2));
            }

        } catch (Exception e) {
        }

        setCalloutActive(false);
        return "";
    }	//	setNroComp

    public String setFixedAccount(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);


        setCalloutActive(false);
        return "";
    }	//	setNroComp

    public String setCheques(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        MField fPayment = mTab.getField("C_PAYMENTVALORES_ID");
        fPayment.getLookup().refresh();
        fPayment.getLookup().setSelectedItem(0);

        setCalloutActive(false);
        return "";

        /*
         * String Tipo = Env.getContext(ctx, WindowNo, "TIPO");
         *
         * //	Get Info from Tab
         *
         * String banco = (String)mTab.getValue("BANCO"); MLookup look =
         * (MLookup)mTab.getField("C_PAYMENTVALORES_ID").getLookup(); look.removeAllElements();
         *
         * String sql = null;
         *
         * if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_VencCqTercero)) { sql = "SELECT C_PAYMENTVALORES_ID,NROCHEQUE " +
         * "FROM C_PAYMENTVALORES " + "WHERE TIPO = '"+ MPAYMENTVALORES.CHEQUE +"' AND (STATE = '"+
         * MPAYMENTVALORES.CARTERA +"' OR STATE = '"+ MPAYMENTVALORES.ENTREGADOPROVEEDOR +"') " + "AND IsActive = 'Y'
         * AND BANCO = ?"; } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_RechCqTercero)) { sql = "SELECT
         * C_PAYMENTVALORES_ID,NROCHEQUE " + "FROM C_PAYMENTVALORES " + "WHERE TIPO = '"+ MPAYMENTVALORES.CHEQUE +"' AND
         * (STATE = '"+ MPAYMENTVALORES.DEPOSITADO +"' OR STATE = '"+ MPAYMENTVALORES.ENTREGADOPROVEEDOR +"') " + "AND
         * IsActive = 'Y' AND BANCO = ?"; }
         *
         * /**
         * Setear el primer cheque seleccionado.
         */
        /*
         * if (sql!=null) { try { PreparedStatement pstm = DB.prepareStatement(sql, null); pstm.setString(1,banco);
         * ResultSet rs = pstm.executeQuery();
         *
         * int first = 0;
         *
         * while (rs.next()) { if (first==0) first=rs.getInt(1); look.addElement(new
         * KeyNamePair(rs.getInt(1),rs.getString(2))); }
         *
         * setDatosTercerosDeb(first,mTab); } catch (Exception e)	{	/*Tipo Incorrecto	} }
         *
         * look.refresh();
         *
         */

    }	//	setCheques

    public String setChequesCred(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        MMOVIMIENTOFONDOS mov = new MMOVIMIENTOFONDOS(ctx, Env.getContextAsInt(ctx, WindowNo, "C_MOVIMIENTOFONDOS_ID"), null);
        String Tipo = mov.getTIPO();
        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), Tipo);

        //	Get Info from Tab

        String banco = (String) mTab.getValue("BANCO");
        MLookup look = (MLookup) mTab.getField("C_PAYMENTVALORES_ID").getLookup();
        look.removeAllElements();

        String sql = null;
        
        if (dynMovFondos == null) {
            
            // if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_CambioCheque) || dynMovFondos.isDEB_CHEQUE_REC() || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
            if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_CambioCheque)) {
                sql = "SELECT C_PAYMENTVALORES_ID,NROCHEQUE "
                    + "FROM C_PAYMENTVALORES "
                    + "WHERE TIPO = '" + MPAYMENTVALORES.CHEQUE + "' AND STATE = '" + MPAYMENTVALORES.CARTERA + "' "
                    + "AND IsActive = 'Y' AND BANCO = ?";
            } else {
                String Fecha = ValueFormat.getFechaFormateada(mov.getDateTrx());
                /*
                if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_DepCheque)
                || dynMovFondos.isDEB_CUENTA_BANCO()
                || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
                */
                if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_DepCheque)) {
                    sql = "SELECT C_PAYMENTVALORES_ID,NROCHEQUE "
                        + "FROM C_PAYMENTVALORES "
                        + "WHERE TIPO = '" + MPAYMENTVALORES.CHEQUE + "' AND STATE = '" + MPAYMENTVALORES.CARTERA + "' "
                        + "AND IsActive = 'Y' AND BANCO = ? AND PAYMENTDATE <= to_date('" + Fecha + "','ddmmyyyy')";
                    /*
                    } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_ValNegociados)
                    || dynMovFondos.isDEB_CUENTA_BANCO()
                    || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
                    */
                } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_ValNegociados)) {
                    sql = "SELECT C_PAYMENTVALORES_ID,NROCHEQUE "
                        + "FROM C_PAYMENTVALORES "
                        + "WHERE TIPO = '" + MPAYMENTVALORES.CHEQUE + "' AND STATE = '" + MPAYMENTVALORES.CARTERA + "' "
                        + "AND IsActive = 'Y' AND BANCO = ? AND PAYMENTDATE > to_date('" + Fecha + "','ddmmyyyy')";
                }
            }            
            
        } else {
            
            // if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_CambioCheque) || dynMovFondos.isDEB_CHEQUE_REC() || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
            if (dynMovFondos.isCRE_CHEQUE_RECI()) {
                sql = "SELECT C_PAYMENTVALORES_ID,NROCHEQUE "
                    + "FROM C_PAYMENTVALORES "
                    + "WHERE TIPO = '" + MPAYMENTVALORES.CHEQUE + "' AND STATE = '" + MPAYMENTVALORES.CARTERA + "' "
                    + "AND IsActive = 'Y' AND BANCO = ?";
            } else {
                String Fecha = ValueFormat.getFechaFormateada(mov.getDateTrx());
                /*
                if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_DepCheque)
                || dynMovFondos.isDEB_CUENTA_BANCO()
                || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
                */
                if (dynMovFondos.isCRE_CHEQUE_DEPO()) {
                    sql = "SELECT C_PAYMENTVALORES_ID,NROCHEQUE "
                        + "FROM C_PAYMENTVALORES "
                        + "WHERE TIPO = '" + MPAYMENTVALORES.CHEQUE + "' AND STATE = '" + MPAYMENTVALORES.CARTERA + "' "
                        + "AND IsActive = 'Y' AND BANCO = ? AND PAYMENTDATE <= to_date('" + Fecha + "','ddmmyyyy')";
                    /*
                    } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_ValNegociados)
                    || dynMovFondos.isDEB_CUENTA_BANCO()
                    || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
                    */
                } else if (dynMovFondos.isCRE_VALORES_NEG()) {
                    sql = "SELECT C_PAYMENTVALORES_ID,NROCHEQUE "
                        + "FROM C_PAYMENTVALORES "
                        + "WHERE TIPO = '" + MPAYMENTVALORES.CHEQUE + "' AND STATE = '" + MPAYMENTVALORES.CARTERA + "' "
                        + "AND IsActive = 'Y' AND BANCO = ? AND PAYMENTDATE > to_date('" + Fecha + "','ddmmyyyy')";
                }
            }            
            
        }
        


        /**
         * Setear el primer cheque seleccionado.
         */
        if (sql != null) {
            try {
                PreparedStatement pstm = DB.prepareStatement(sql, null);
                pstm.setString(1, banco);
                ResultSet rs = pstm.executeQuery();

                int first = 0;

                while (rs.next()) {
                    if (first == 0) {
                        first = rs.getInt(1);
                    }
                    look.addElement(new KeyNamePair(rs.getInt(1), rs.getString(2)));
                }

                setDatosTercerosCred(first, mTab);
            } catch (Exception e) {	/*
                 * Tipo Incorrecto
                 */            }
        }

        look.refresh();

        setCalloutActive(false);
        return "";
    }	//	setChequesCred

    public String setAcctCred(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        //---------------------------
        //	Get Info from Tab
        String Tipo = Env.getContext(ctx, WindowNo, "TIPO");
        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), Tipo);

        if (dynMovFondos == null) {

                /**
                * SET DE CUENTA CONTABLE
                */
                try {
                    //	Get Info from Tab
                    Integer schema = (Integer) mTab.getValue("C_AcctSchema_ID");

                    PreparedStatement pstm = null;
                    ResultSet rs = null;

                    if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_MovEfectivo)) {
                        String sql = "SELECT CB_ASSET_ACCT "
                                    + "FROM C_AcctSchema_Default "
                                    + "WHERE C_AcctSchema_ID = ?";

                        pstm = DB.prepareStatement(sql, null);
                        pstm.setInt(1, schema.intValue());

                    } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_ValNegociados)
                                || Tipo.equals(MMOVIMIENTOFONDOS.TIPO_CambioCheque)) {
                        String sql = "SELECT B_UNIDENTIFIED_ACCT "
                                    + "FROM C_AcctSchema_Default "
                                    + "WHERE C_AcctSchema_ID = ?";

                        pstm = DB.prepareStatement(sql, null);
                        pstm.setInt(1, schema.intValue());

                    } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_RechCqTercero)) {
                        int key = Env.getContextAsInt(ctx, WindowNo, "C_MOVIMIENTOFONDOS_ID");

                        String sql = "SELECT  pv.C_PAYMENTVALORES_ID,pv.C_PAYMENT_ID,pv.STATE "
                                    + "FROM C_PAYMENTVALORES pv, C_MOVIMIENTOFONDOS_DEB md "
                                    + "WHERE md.C_PAYMENTVALORES_ID = pv.C_PAYMENTVALORES_ID AND md.C_MOVIMIENTOFONDOS_ID = ?";

                        PreparedStatement pstmPre = DB.prepareStatement(sql, null);
                        pstmPre.setInt(1, key);

                        ResultSet rsPre = pstmPre.executeQuery();

                        int C_PAYMENTVALORES_ID = 0;
                        int C_PAYMENT_ID = 0;
                        String STATE = null;

                        if (rsPre.next()) {
                            C_PAYMENTVALORES_ID = rsPre.getInt(1);
                            C_PAYMENT_ID = rsPre.getInt(2);
                            STATE = rsPre.getString(3);
                        }

                        pstmPre.close();
                        rsPre.close();

                        //TODO PASAR A CONSTANTE
                        if (STATE != null) {
                            if (STATE.equals("D")) {

                                sql = "SELECT  C_MOVIMIENTOFONDOS_ID "
                                    + "FROM C_MOVIMIENTOFONDOS_CRE "
                                    + "WHERE C_PAYMENTVALORES_ID = ?";

                                pstmPre = DB.prepareStatement(sql, null);
                                pstmPre.setInt(1, C_PAYMENTVALORES_ID);
                                rsPre = pstmPre.executeQuery();

                                int C_MOVIMIENTOFONDOS_ID = 0;

                                if (rsPre.next()) {
                                    C_MOVIMIENTOFONDOS_ID = rsPre.getInt(1);
                                }

                                pstmPre.close();
                                rsPre.close();

                                sql = "SELECT  b.B_ASSET_ACCT "
                                    + "FROM C_BankAccount_Acct b, C_MOVIMIENTOFONDOS_DEB md "
                                    + "WHERE b.C_BankAccount_ID = md.C_BankAccount_ID AND md.C_MOVIMIENTOFONDOS_ID = ?";

                                pstm = DB.prepareStatement(sql, null);
                                pstm.setInt(1, C_MOVIMIENTOFONDOS_ID);
                            } else if (STATE.equals("E")) {
                                sql = "SELECT  bp.V_LIABILITY_ACCT "
                                    + "FROM C_Payment p, C_BP_Ventor_Acct bp "
                                    + "WHERE bp.C_BPartner_ID = p.C_BPartner_ID AND p.C_Payment_ID = ? AND bp.C_ACCTSCHEMA_ID = ?";

                                pstm = DB.prepareStatement(sql, null);
                                pstm.setInt(1, C_PAYMENT_ID);
                                pstm.setInt(2, schema.intValue());
                            }
                        } // STATE!=null
                    } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_RechCqPropio)) {
                        String sql = "SELECT CQ_OWNREJ_ACCT "
                                    + "FROM C_AcctSchema_Default "
                                    + "WHERE C_AcctSchema_ID = ?";
                        pstm = DB.prepareStatement(sql, null);
                        pstm.setInt(1, schema.intValue());
                    } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_VencCqPropio)) {
                        String sql = "SELECT CQ_OWNEXP_ACCT "
                                    + "FROM C_AcctSchema_Default "
                                    + "WHERE C_AcctSchema_ID = ?";
                        pstm = DB.prepareStatement(sql, null);
                        pstm.setInt(1, schema.intValue());
                    } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_VencCqTercero)) {
                        int key = Env.getContextAsInt(ctx, WindowNo, "C_MOVIMIENTOFONDOS_ID");

                        String sql = "SELECT  pv.C_PAYMENT_ID,pv.STATE "
                                    + "FROM C_PAYMENTVALORES pv, C_MOVIMIENTOFONDOS_DEB md "
                                    + "WHERE md.C_PAYMENTVALORES_ID = pv.C_PAYMENTVALORES_ID AND  md.C_MOVIMIENTOFONDOS_ID = ?";

                        PreparedStatement pstmPre = DB.prepareStatement(sql, null);
                        pstmPre.setInt(1, key);

                        ResultSet rsPre = pstmPre.executeQuery();

                        int C_PAYMENT_ID = 0;
                        String STATE = null;

                        if (rsPre.next()) {
                            C_PAYMENT_ID = rsPre.getInt(1);
                            STATE = rsPre.getString(2);
                        }

                        pstmPre.close();
                        rsPre.close();

                        if (STATE != null) {
                            if (STATE.equals("C")) {
                                sql = "SELECT B_UNIDENTIFIED_ACCT "
                                    + "FROM C_AcctSchema_Default "
                                    + "WHERE C_AcctSchema_ID = ?";

                                pstm = DB.prepareStatement(sql, null);
                                pstm.setInt(1, schema.intValue());
                            } else if (STATE.equals("E")) {
                                sql = "SELECT  bp.V_LIABILITY_ACCT "
                                    + "FROM C_Payment p, C_BP_Ventor_Acct bp "
                                    + "WHERE bp.C_BPartner_ID = p.C_BPartner_ID AND p.C_Payment_ID = ? AND bp.C_ACCTSCHEMA_ID = ?";

                                pstm = DB.prepareStatement(sql, null);
                                pstm.setInt(1, C_PAYMENT_ID);
                                pstm.setInt(2, schema.intValue());
                            }
                        } // STATE!=null
                    } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_DepositoPendiente)) {
                        String sql = "SELECT  b.B_ASSET_ACCT "
                                    + "FROM C_BankAccount_Acct b, C_MOVIMIENTOFONDOS_CRE md "
                                    + "WHERE b.C_BankAccount_ID = md.C_BankAccount_ID AND md.C_MOVIMIENTOFONDOS_ID = ?";

                        pstm = DB.prepareStatement(sql, null);
                        pstm.setInt(1, Env.getContextAsInt(ctx, WindowNo, "C_MOVIMIENTOFONDOS_ID"));
                    }

                    if (pstm != null) {
                        rs = pstm.executeQuery();
                        if (rs.next()) {
                            setAccount(mTab, "MV_CREDITO_ACCT", rs.getInt(1));
                        }
                    }

                } //	try
                catch (Exception e) {
                }        
            
        } else {
            
            if (dynMovFondos.isCRE_CUENTA_CREDITO_FIJA()) {
                        mTab.setValue("MV_CREDITO_ACCT", dynMovFondos.getC_Element_ID());
            } else {
                /**
                * SET DE CUENTA CONTABLE
                */
                try {
                    //	Get Info from Tab
                    Integer schema = (Integer) mTab.getValue("C_AcctSchema_ID");

                    PreparedStatement pstm = null;
                    ResultSet rs = null;

                    /*
                    if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_MovEfectivo)
                    || dynMovFondos.isDEB_CUENTA_DEBITO()
                    || dynMovFondos.isCRE_CUENTA_CREDITO()) {
                    */
                    if (dynMovFondos.isCRE_EFECTIVO() || dynMovFondos.isCRE_CUENTA_CREDITO()
                        || dynMovFondos.isCRE_CUENTA_CREDITO_FIJA()) {
                        String sql = "SELECT CB_ASSET_ACCT "
                                    + "FROM C_AcctSchema_Default "
                                    + "WHERE C_AcctSchema_ID = ?";

                        pstm = DB.prepareStatement(sql, null);
                        pstm.setInt(1, schema.intValue());

                    } //Efectivo
                    /*
                    else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_ValNegociados)
                    || Tipo.equals(MMOVIMIENTOFONDOS.TIPO_CambioCheque)
                    || dynMovFondos.isDEB_CHEQUE_REC()
                    || dynMovFondos.isCRE_CHEQUE_TERCERO()
                    || dynMovFondos.isDEB_CUENTA_BANCO()) {
                    */ else if (dynMovFondos.isCRE_CHEQUE_RECI() || dynMovFondos.isCRE_VALORES_NEG()) {
                        String sql = "SELECT B_UNIDENTIFIED_ACCT "
                                    + "FROM C_AcctSchema_Default "
                                    + "WHERE C_AcctSchema_ID = ?";

                        pstm = DB.prepareStatement(sql, null);
                        pstm.setInt(1, schema.intValue());

                    } else if (dynMovFondos.isDEB_CHEQUE_TER_RECH() || dynMovFondos.isCRE_CHEQUE_PRO_RECH()) {
                        int key = Env.getContextAsInt(ctx, WindowNo, "C_MOVIMIENTOFONDOS_ID");

                        String sql = "SELECT  pv.C_PAYMENTVALORES_ID,pv.C_PAYMENT_ID,pv.STATE "
                                    + "FROM C_PAYMENTVALORES pv, C_MOVIMIENTOFONDOS_DEB md "
                                    + "WHERE md.C_PAYMENTVALORES_ID = pv.C_PAYMENTVALORES_ID AND md.C_MOVIMIENTOFONDOS_ID = ?";

                        PreparedStatement pstmPre = DB.prepareStatement(sql, null);
                        pstmPre.setInt(1, key);

                        ResultSet rsPre = pstmPre.executeQuery();

                        int C_PAYMENTVALORES_ID = 0;
                        int C_PAYMENT_ID = 0;
                        String STATE = null;

                        if (rsPre.next()) {
                            C_PAYMENTVALORES_ID = rsPre.getInt(1);
                            C_PAYMENT_ID = rsPre.getInt(2);
                            STATE = rsPre.getString(3);
                        }

                        pstmPre.close();
                        rsPre.close();

                        //TODO PASAR A CONSTANTE
                        if (STATE != null) {
                            if (STATE.equals("D")) {

                                sql = "SELECT  C_MOVIMIENTOFONDOS_ID "
                                    + "FROM C_MOVIMIENTOFONDOS_CRE "
                                    + "WHERE C_PAYMENTVALORES_ID = ?";

                                pstmPre = DB.prepareStatement(sql, null);
                                pstmPre.setInt(1, C_PAYMENTVALORES_ID);
                                rsPre = pstmPre.executeQuery();

                                int C_MOVIMIENTOFONDOS_ID = 0;

                                if (rsPre.next()) {
                                    C_MOVIMIENTOFONDOS_ID = rsPre.getInt(1);
                                }

                                pstmPre.close();
                                rsPre.close();

                                sql = "SELECT  b.B_ASSET_ACCT "
                                    + "FROM C_BankAccount_Acct b, C_MOVIMIENTOFONDOS_DEB md "
                                    + "WHERE b.C_BankAccount_ID = md.C_BankAccount_ID AND md.C_MOVIMIENTOFONDOS_ID = ?";

                                pstm = DB.prepareStatement(sql, null);
                                pstm.setInt(1, C_MOVIMIENTOFONDOS_ID);
                            } else if (STATE.equals("E")) {
                                sql = "SELECT  bp.V_LIABILITY_ACCT "
                                    + "FROM C_Payment p, C_BP_Ventor_Acct bp "
                                    + "WHERE bp.C_BPartner_ID = p.C_BPartner_ID AND p.C_Payment_ID = ? AND bp.C_ACCTSCHEMA_ID = ?";

                                pstm = DB.prepareStatement(sql, null);
                                pstm.setInt(1, C_PAYMENT_ID);
                                pstm.setInt(2, schema.intValue());
                            }
                        } // STATE!=null
                    } // TerRechazar
                    /*
                    else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_RechCqPropio)
                    || dynMovFondos.isDEB_CHEQUE_PROPIO()
                    || dynMovFondos.isCRE_CUENTA_CREDITO()) {
                    */ else if (dynMovFondos.isCRE_CHEQUE_PRO_RECH()) {
                        String sql = "SELECT CQ_OWNREJ_ACCT "
                                    + "FROM C_AcctSchema_Default "
                                    + "WHERE C_AcctSchema_ID = ?";
                        pstm = DB.prepareStatement(sql, null);
                        pstm.setInt(1, schema.intValue());
                    } // ProRechazar
                    /*
                    else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_VencCqPropio)
                    || dynMovFondos.isDEB_CHEQUE_TERCERO()
                    || dynMovFondos.isCRE_CUENTA_CREDITO()) {
                    */ else if (dynMovFondos.isCRE_CHEQUE_PRO_VENC()) {
                        String sql = "SELECT CQ_OWNEXP_ACCT "
                                    + "FROM C_AcctSchema_Default "
                                    + "WHERE C_AcctSchema_ID = ?";
                        pstm = DB.prepareStatement(sql, null);
                        pstm.setInt(1, schema.intValue());
                    } // Propio
                    /*
                    else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_VencCqTercero)
                    || dynMovFondos.isDEB_CHEQUE_TERCERO()
                    || dynMovFondos.isCRE_CUENTA_CREDITO()) {
                    */ else if (dynMovFondos.isDEB_CHEQUE_TER_VENC()) {
                        int key = Env.getContextAsInt(ctx, WindowNo, "C_MOVIMIENTOFONDOS_ID");

                        String sql = "SELECT  pv.C_PAYMENT_ID,pv.STATE "
                                    + "FROM C_PAYMENTVALORES pv, C_MOVIMIENTOFONDOS_DEB md "
                                    + "WHERE md.C_PAYMENTVALORES_ID = pv.C_PAYMENTVALORES_ID AND  md.C_MOVIMIENTOFONDOS_ID = ?";

                        PreparedStatement pstmPre = DB.prepareStatement(sql, null);
                        pstmPre.setInt(1, key);

                        ResultSet rsPre = pstmPre.executeQuery();

                        int C_PAYMENT_ID = 0;
                        String STATE = null;

                        if (rsPre.next()) {
                            C_PAYMENT_ID = rsPre.getInt(1);
                            STATE = rsPre.getString(2);
                        }

                        pstmPre.close();
                        rsPre.close();

                        if (STATE != null) {
                            if (STATE.equals("C")) {
                                sql = "SELECT B_UNIDENTIFIED_ACCT "
                                    + "FROM C_AcctSchema_Default "
                                    + "WHERE C_AcctSchema_ID = ?";

                                pstm = DB.prepareStatement(sql, null);
                                pstm.setInt(1, schema.intValue());
                            } else if (STATE.equals("E")) {
                                sql = "SELECT  bp.V_LIABILITY_ACCT "
                                    + "FROM C_Payment p, C_BP_Ventor_Acct bp "
                                    + "WHERE bp.C_BPartner_ID = p.C_BPartner_ID AND p.C_Payment_ID = ? AND bp.C_ACCTSCHEMA_ID = ?";

                                pstm = DB.prepareStatement(sql, null);
                                pstm.setInt(1, C_PAYMENT_ID);
                                pstm.setInt(2, schema.intValue());
                            }
                        } // STATE!=null
                    } // Propio
                    /*
                    else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_DepositoPendiente)
                    || dynMovFondos.isDEB_CUENTA_BANCO()
                    || dynMovFondos.isCRE_CUENTA_BANCO()) {
                    */ else if (dynMovFondos.isCRE_DEPOSITO_PEND()) {
                        String sql = "SELECT  b.B_ASSET_ACCT "
                                    + "FROM C_BankAccount_Acct b, C_MOVIMIENTOFONDOS_CRE md "
                                    + "WHERE b.C_BankAccount_ID = md.C_BankAccount_ID AND md.C_MOVIMIENTOFONDOS_ID = ?";

                        pstm = DB.prepareStatement(sql, null);
                        pstm.setInt(1, Env.getContextAsInt(ctx, WindowNo, "C_MOVIMIENTOFONDOS_ID"));
                    }

                    if (pstm != null) {
                        rs = pstm.executeQuery();
                        if (rs.next()) {
                            setAccount(mTab, "MV_CREDITO_ACCT", rs.getInt(1));
                        }
                    }

                } //	try
                catch (Exception e) {
                }

            }            
            
        }        
        
        
        

        setCalloutActive(false);
        return "";
    }	//	setAcctCred

    public String setAcctDeb(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        //---------------------------
        //	Get Info from Tab
        String Tipo = Env.getContext(ctx, WindowNo, "TIPO");
        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), Tipo);

        /**
         * SET DE CUENTA CONTABLE
         */
        
        if (dynMovFondos == null) {
        
            try {
                //	Get Info from Tab
                Integer schema = (Integer) mTab.getValue("C_AcctSchema_ID");

                PreparedStatement pstm = null;
                ResultSet rs = null;

                if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_CambioCheque)) {
                    String sql = "SELECT B_UNIDENTIFIED_ACCT "
                                + "FROM C_AcctSchema_Default "
                                + "WHERE C_AcctSchema_ID = ?";

                    pstm = DB.prepareStatement(sql, null);
                    pstm.setInt(1, schema.intValue());

                } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_DepositoPendiente)
                            || Tipo.equals(MMOVIMIENTOFONDOS.TIPO_CreditoBancario)) {
                    String sql = "SELECT  b.B_ASSET_ACCT "
                                + "FROM C_BankAccount_Acct b, C_MOVIMIENTOFONDOS_DEB md "
                                + "WHERE b.C_BankAccount_ID = md.C_BankAccount_ID AND md.C_MOVIMIENTOFONDOS_ID = ?";

                    pstm = DB.prepareStatement(sql, null);
                    pstm.setInt(1, Env.getContextAsInt(ctx, WindowNo, "C_MOVIMIENTOFONDOS_ID"));
                }

                if (pstm != null) {
                    rs = pstm.executeQuery();
                    if (rs.next()) {
                        setAccount(mTab, "MV_DEBITO_ACCT", rs.getInt(1));
                    }
                }

            } //	try
            catch (Exception e) {
            }
            
        }
        else {
            if (dynMovFondos.isDEB_CUENTA_DEBITO_FIJA()) {
                            mTab.setValue("MV_DEBITO_ACCT", dynMovFondos.getDEB_C_Element_ID());
                }
            else {

                try {
                    //	Get Info from Tab
                    Integer schema = (Integer) mTab.getValue("C_AcctSchema_ID");

                    PreparedStatement pstm = null;
                    ResultSet rs = null;
                    /*
                    if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_CambioCheque)
                    || dynMovFondos.isDEB_CHEQUE_REC()
                    || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
                    */
                    if (dynMovFondos.isDEB_CHEQUE_REC()) {
                        String sql = "SELECT B_UNIDENTIFIED_ACCT "
                                    + "FROM C_AcctSchema_Default "
                                    + "WHERE C_AcctSchema_ID = ?";

                        pstm = DB.prepareStatement(sql, null);
                        pstm.setInt(1, schema.intValue());

                    } else if (dynMovFondos.isDEB_CREDITO_BANCO() || dynMovFondos.isDEB_DEPOSITO_PEND()) {
                        String sql = "SELECT  b.B_ASSET_ACCT "
                                    + "FROM C_BankAccount_Acct b, C_MOVIMIENTOFONDOS_DEB md "
                                    + "WHERE b.C_BankAccount_ID = md.C_BankAccount_ID AND md.C_MOVIMIENTOFONDOS_ID = ?";

                        pstm = DB.prepareStatement(sql, null);
                        pstm.setInt(1, Env.getContextAsInt(ctx, WindowNo, "C_MOVIMIENTOFONDOS_ID"));
                    }

                    if (pstm != null) {
                        rs = pstm.executeQuery();
                        if (rs.next()) {
                            setAccount(mTab, "MV_DEBITO_ACCT", rs.getInt(1));
                        }
                    }

                } //	try
                catch (Exception e) {
                }

            }
        }

        setCalloutActive(false);
        return "";
    }	//	setAcctDeb

    private void setAccount(MTab mTab, String campo, int cuenta) {
        String sql = " SELECT  ACCOUNT_ID "
                     + " FROM C_VALIDCOMBINATION "
                     + " WHERE C_VALIDCOMBINATION_ID = ?";
        try {
            PreparedStatement pstmQuery = DB.prepareStatement(sql, null);
            pstmQuery.setLong(1, cuenta);
            ResultSet rs = pstmQuery.executeQuery();

            if (rs.next()) {
                mTab.setValue(campo, rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String setCuitFirm(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        //---------------------------
        //	Get Info from Tab
        MMOVIMIENTOFONDOS mov = new MMOVIMIENTOFONDOS(ctx, Env.getContextAsInt(ctx, WindowNo, "C_MOVIMIENTOFONDOS_ID"), null);
        String Tipo = mov.getTIPO();
        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), Tipo);

        /**
         * SET CUIT del FIRMANTE para Cambio de Cheques Recibidos
         */
        
        if (dynMovFondos == null) {
        
            if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_CambioCheque)) {
                ArrayList<MMOVIMIENTOFONDOSCRE> cred = mov.getC_MOVIMIENTOFONDOS_CRE_ID();
                if (cred != null) {
                    MPAYMENTVALORES pay = new MPAYMENTVALORES(ctx, cred.get(0).getC_PAYMENTVALORES_ID(), null);
                    mTab.setValue("CUITFIRM", pay.getCuitFirmante());
                }
            }	//Cambio
            
        } else {

            if (dynMovFondos.isCRE_CHEQUE_RECI()) {
                ArrayList<MMOVIMIENTOFONDOSCRE> cred = mov.getC_MOVIMIENTOFONDOS_CRE_ID();
                if (cred != null) {
                    MPAYMENTVALORES pay = new MPAYMENTVALORES(ctx, cred.get(0).getC_PAYMENTVALORES_ID(), null);
                    mTab.setValue("CUITFIRM", pay.getCuitFirmante());
                }
            }	//Cambio
            
        }

        setCalloutActive(false);
        return "";
    }	//	setCuitFirm

    public String setupBankDeb(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        String Tipo = Env.getContext(ctx, WindowNo, "TIPO");
        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), Tipo);


        //	Get Info from Tab
        Integer bank = (Integer) mTab.getValue("C_BankAccount_ID");


        if (dynMovFondos == null) {
        
            if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_RechCqPropio)) {
                //	Get Info from Tab
                MLookup cheque = (MLookup) mTab.getField("C_VALORPAGO_ID").getLookup();

                if (cheque != null) {
                    cheque.removeAllElements();
                }

                try {

                    /*
                    *  Zynnia 25/06/2012
                    *  Anexa que tome los de tipo PC-Bancking
                    *  José Fantasia
                    *
                    */

                    String sql = "SELECT C_VALORPAGO_ID, NROCHEQUE "
                                + "FROM C_VALORPAGO "
                                + "WHERE (TIPO='" + MVALORPAGO.CHEQUEPROPIO + "' OR TIPO='" + MVALORPAGO.PCBANKING + "')  AND (STATE='" + MVALORPAGO.EMITIDO + "' OR STATE='" + MVALORPAGO.PENDIENTEDEBITO + "' OR STATE='" + MVALORPAGO.IMPRESO + "') AND C_BankAccount_ID = ? ORDER BY NROCHEQUE";

                    PreparedStatement pstm = DB.prepareStatement(sql, null);
                    pstm.setInt(1, bank.intValue());
                    ResultSet rs = pstm.executeQuery();

                    int first = 0;

                    while (rs.next()) {
                        if (first == 0) {
                            first = rs.getInt(1);
                        }
                        cheque.addElement(new KeyNamePair(rs.getInt(1), rs.getString(2)));
                    }

                    cheque.refresh();

                    setDatosPropiosDeb(first, mTab);
                } catch (Exception e) {
                }

            } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_VencCqPropio)) {
                //	Get Info from Tab
                MLookup cheque = (MLookup) mTab.getField("C_VALORPAGO_ID").getLookup();

                cheque.removeAllElements();

                try {
                    String sql = "SELECT C_VALORPAGO_ID, NROCHEQUE "
                                + "FROM C_VALORPAGO "
                                + "WHERE TIPO='" + MVALORPAGO.CHEQUEPROPIO + "' AND (STATE='" + MVALORPAGO.EMITIDO + "' OR STATE='" + MVALORPAGO.IMPRESO + "') AND C_BankAccount_ID = ? ORDER BY NROCHEQUE";


                    PreparedStatement pstm = DB.prepareStatement(sql, null);
                    pstm.setInt(1, bank.intValue());
                    ResultSet rs = pstm.executeQuery();

                    int first = 0;

                    while (rs.next()) {
                        if (first == 0) {
                            first = rs.getInt(1);
                        }
                        cheque.addElement(new KeyNamePair(rs.getInt(1), rs.getString(2)));
                    }

                    cheque.refresh();

                    setDatosPropiosDeb(first, mTab);
                } catch (Exception e) {
                }
            }	//Deposito

            
        } else {

            if (dynMovFondos.isDEB_CHEQUE_PRO_RECH()) {
                //	Get Info from Tab
                MLookup cheque = (MLookup) mTab.getField("C_VALORPAGO_ID").getLookup();

                if (cheque != null) {
                    cheque.removeAllElements();
                }

                try {

                    /*
                    *  Zynnia 25/06/2012
                    *  Anexa que tome los de tipo PC-Bancking
                    *  José Fantasia
                    *
                    */

                    String sql = "SELECT C_VALORPAGO_ID, NROCHEQUE "
                                + "FROM C_VALORPAGO "
                                + "WHERE (TIPO='" + MVALORPAGO.CHEQUEPROPIO + "' OR TIPO='" + MVALORPAGO.PCBANKING + "')  AND (STATE='" + MVALORPAGO.EMITIDO + "' OR STATE='" + MVALORPAGO.PENDIENTEDEBITO + "' OR STATE='" + MVALORPAGO.IMPRESO + "') AND C_BankAccount_ID = ? ORDER BY NROCHEQUE";

                    PreparedStatement pstm = DB.prepareStatement(sql, null);
                    pstm.setInt(1, bank.intValue());
                    ResultSet rs = pstm.executeQuery();

                    int first = 0;

                    while (rs.next()) {
                        if (first == 0) {
                            first = rs.getInt(1);
                        }
                        cheque.addElement(new KeyNamePair(rs.getInt(1), rs.getString(2)));
                    }

                    cheque.refresh();

                    setDatosPropiosDeb(first, mTab);
                } catch (Exception e) {
                }
                
            } else if (dynMovFondos.isDEB_CHEQUE_PRO_VENC()) {
                //	Get Info from Tab
                MLookup cheque = (MLookup) mTab.getField("C_VALORPAGO_ID").getLookup();

                cheque.removeAllElements();

                try {
                    String sql = "SELECT C_VALORPAGO_ID, NROCHEQUE "
                                + "FROM C_VALORPAGO "
                                + "WHERE TIPO='" + MVALORPAGO.CHEQUEPROPIO + "' AND (STATE='" + MVALORPAGO.EMITIDO + "' OR STATE='" + MVALORPAGO.IMPRESO + "') AND C_BankAccount_ID = ? ORDER BY NROCHEQUE";


                    PreparedStatement pstm = DB.prepareStatement(sql, null);
                    pstm.setInt(1, bank.intValue());
                    ResultSet rs = pstm.executeQuery();

                    int first = 0;

                    while (rs.next()) {
                        if (first == 0) {
                            first = rs.getInt(1);
                        }
                        cheque.addElement(new KeyNamePair(rs.getInt(1), rs.getString(2)));
                    }

                    cheque.refresh();

                    setDatosPropiosDeb(first, mTab);
                } catch (Exception e) {
                }
            }	//Deposito

            
        }        
        
        

        /**
         * SET DE CUENTA CONTABLE
         */
        try {
            String sql = "SELECT B_Asset_Acct "
                         + "FROM C_BankAccount_Acct "
                         + "WHERE C_BankAccount_ID = ?";

            PreparedStatement pstm = DB.prepareStatement(sql, null);
            pstm.setInt(1, bank.intValue());
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                setAccount(mTab, "MV_DEBITO_ACCT", rs.getInt(1));
            }
        } catch (Exception e) {
        }

        setCalloutActive(false);
        return "";
    }	//	setupBankDeb

    public String setupBankCred(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        //	Get Info from Tab
        Integer bank = (Integer) mTab.getValue("C_BankAccount_ID");

        try {
            String sql = "SELECT B_Asset_Acct "
                         + "FROM C_BankAccount_Acct "
                         + "WHERE C_BankAccount_ID = ?";

            PreparedStatement pstm = DB.prepareStatement(sql, null);
            pstm.setInt(1, bank.intValue());
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                setAccount(mTab, "MV_CREDITO_ACCT", rs.getInt(1));
            }

        } catch (Exception e) {
        }

        setCalloutActive(false);
        return "";
    }	//	setupBankCred

    public String setPaymentValoresDeb(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        //	Get Info from DB
        setDatosTercerosDeb(Env.getContextAsInt(ctx, WindowNo, "C_PAYMENTVALORES_ID"), mTab);

        /**
         * SET DE CUENTA CONTABLE
         */
        //	Get Info from Tab
        String Tipo = Env.getContext(ctx, WindowNo, "TIPO");
        Integer schema = (Integer) mTab.getValue("C_AcctSchema_ID");
        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), Tipo);
        
        if (dynMovFondos == null) {        

            String sql = null;

            try {
                if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_VencCqTercero)) {
                    sql = "SELECT CQ_RECEXP_ACCT "
                        + "FROM C_AcctSchema_Default "
                        + "WHERE C_AcctSchema_ID = ?";

                } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_RechCqTercero)) {

                    sql = "SELECT CQ_RECREJ_ACCT "
                        + "FROM C_AcctSchema_Default "
                        + "WHERE C_AcctSchema_ID = ?";
                }

                PreparedStatement pstm = DB.prepareStatement(sql, null);
                pstm.setInt(1, schema.intValue());
                ResultSet rs = pstm.executeQuery();
                if (rs.next()) {
                    setAccount(mTab, "MV_DEBITO_ACCT", rs.getInt(1));
                }
            } catch (Exception e) {	

            }
        
        } else {
        
            String sql = null;

            try {
                if (dynMovFondos.isDEB_CHEQUE_TER_VENC()) {
                    sql = "SELECT CQ_RECEXP_ACCT "
                        + "FROM C_AcctSchema_Default "
                        + "WHERE C_AcctSchema_ID = ?";
                    /*
                    } else if (Tipo.equals(MMOVIMIENTOFONDOS.TIPO_RechCqTercero)
                    || dynMovFondos.isDEB_CHEQUE_TERCERO()
                    || dynMovFondos.isCRE_CUENTA_CREDITO()) {
                    */
                } else if (dynMovFondos.isDEB_CHEQUE_TER_RECH()) {

                    sql = "SELECT CQ_RECREJ_ACCT "
                        + "FROM C_AcctSchema_Default "
                        + "WHERE C_AcctSchema_ID = ?";
                }

                PreparedStatement pstm = DB.prepareStatement(sql, null);
                pstm.setInt(1, schema.intValue());
                ResultSet rs = pstm.executeQuery();
                if (rs.next()) {
                    setAccount(mTab, "MV_DEBITO_ACCT", rs.getInt(1));
                }
            } catch (Exception e) {	

            }        
            
        }

        setCalloutActive(false);
        return "";
    }	//	setPaymentValoresDeb

    public String setPaymentValoresCred(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        //		Get Info from DB
        int key = Env.getContextAsInt(ctx, WindowNo, "C_PAYMENTVALORES_ID");

        setDatosTercerosCred(key, mTab);

        /**
         * SET DE CUENTA CONTABLE
         */
        //	Get Info from Tab
        Integer schema = (Integer) mTab.getValue("C_AcctSchema_ID");

        try {
            String sql = "SELECT B_UNIDENTIFIED_ACCT "
                         + "FROM C_AcctSchema_Default "
                         + "WHERE C_AcctSchema_ID = ?";

            PreparedStatement pstm = DB.prepareStatement(sql, null);
            pstm.setInt(1, schema.intValue());
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                setAccount(mTab, "MV_CREDITO_ACCT", rs.getInt(1));
            }

        } catch (Exception e) {
        }

        setCalloutActive(false);
        return "";
    }	//	setPaymentValoresCred

    public String setValorPago(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        //	Get Info from DB
        setDatosPropiosDeb(Env.getContextAsInt(ctx, WindowNo, "C_VALORPAGO_ID"), mTab);

        setCalloutActive(false);
        return "";
    }	//	setValorPago

    private void setDatosTercerosCred(int C_VALORPAGO_ID, MTab mTab) {
        String sql = "SELECT TIPOCHEQUE,IMPORTE,PAYMENTDATE FROM C_PAYMENTVALORES WHERE C_PAYMENTVALORES_ID = ?";

        try {

            PreparedStatement pstm = DB.prepareStatement(sql, null);
            pstm.setInt(1, C_VALORPAGO_ID);

            ResultSet rs = pstm.executeQuery();

            //	Set Info to Tab
            if (rs.next()) {
                mTab.setValue("TIPOCHEQUE", rs.getString(1));
                mTab.setValue("CREDITO", rs.getBigDecimal(2));
                mTab.setValue("PAYMENTDATE", rs.getTimestamp(3));
            } else {
                mTab.setValue("TIPOCHEQUE", "");
                mTab.setValue("CREDITO", 0);
                mTab.setValue("PAYMENTDATE", null);
            }
        } catch (Exception e) {
        }
    }

    private void setDatosPropiosDeb(int C_VALORPAGO_ID, MTab mTab) {
        String sql = "SELECT TIPOCHEQUE,IMPORTE,PAYMENTDATE FROM C_VALORPAGO WHERE C_VALORPAGO_ID = ?";

        try {

            PreparedStatement pstm = DB.prepareStatement(sql, null);
            pstm.setInt(1, C_VALORPAGO_ID);

            ResultSet rs = pstm.executeQuery();

            //	Set Info to Tab
            if (rs.next()) {
                mTab.setValue("TIPOCHEQUE", rs.getString(1));
                mTab.setValue("DEBITO", rs.getBigDecimal(2));
                mTab.setValue("PAYMENTDATE", rs.getTimestamp(3));
            } else {
                mTab.setValue("TIPOCHEQUE", "");
                mTab.setValue("DEBITO", 0);
                mTab.setValue("PAYMENTDATE", null);
            }
        } catch (Exception e) {
        }
    }

    private void setDatosTercerosDeb(int C_PAYMENTVALORES_ID, MTab mTab) {
        String sql = "SELECT TIPOCHEQUE,IMPORTE,PAYMENTDATE FROM C_PAYMENTVALORES WHERE C_PAYMENTVALORES_ID = ?";

        try {

            PreparedStatement pstm = DB.prepareStatement(sql, null);
            pstm.setInt(1, C_PAYMENTVALORES_ID);

            ResultSet rs = pstm.executeQuery();

            //	Set Info to Tab
            if (rs.next()) {
                mTab.setValue("TIPOCHEQUE", rs.getString(1));
                mTab.setValue("DEBITO", rs.getBigDecimal(2));
                mTab.setValue("PAYMENTDATE", rs.getTimestamp(3));
            } else {
                mTab.setValue("TIPOCHEQUE", "");
                mTab.setValue("DEBITO", 0);
                mTab.setValue("PAYMENTDATE", null);
            }
        } catch (Exception e) {
        }
    }
}	//	CalloutMovimientoFondos
