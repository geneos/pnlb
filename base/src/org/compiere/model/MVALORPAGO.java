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
package org.compiere.model;

import java.util.Calendar;
import java.util.Date;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.compiere.util.*;

/**
 * valores de la cobranza
 *
 * @author vit4b
 * @version $Id: MMeasure.java,v 1.3 2008/01/22 $
 */
public class MVALORPAGO extends X_C_VALORPAGO {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    static Integer getElementValueForAllaria() {
        Integer aux = 0;
        String sql = "SELECT C_ElementValue_ID "
                        + "FROM C_ElementValue "
                        + "WHERE Value = '1.1.2.5.04' ";
                try {
                    PreparedStatement pstm = DB.prepareStatement(sql, null);
                    ResultSet rs = pstm.executeQuery();
                    if (rs.next()) 
                        aux = rs.getInt(1);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "No Existe Secuencia para el Tipo de Valor" + e.getMessage(), "Error en Nro. de Transferencia", JOptionPane.INFORMATION_MESSAGE);
                }
                        
        return aux;
    }

    /**
     * Standard Constructor
     *
     * @param ctx context
     * @param PA_Measure_ID id
     * @param trxName trx
     */
    public MVALORPAGO(Properties ctx, int C_ValorPago_ID, String trxName) {
        super(ctx, C_ValorPago_ID, trxName);
    }

    public MVALORPAGO(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
    /**
     * Ticket 111 - New Valor added PC Banking Added field type for PC Banking payment value
     *
     * Ezequiel Scott @ Zynnia
     */
    public static String PCBANKING = "B";
    public static String BANCO = "T";
    public static String CHEQUEPROPIO = "P";
    public static String CHEQUERECIBIDO = "R";
    public static String EFECTIVO = "C";
    public static String TARJETACREDITO = "N";
    public static String EMITIDO = "E";
    public static String PENDIENTEDEBITO = "P";
    public static String DEBITADO = "D";
    public static String VENCIDO = "V";
    public static String ANULADO = "A";
    public static String RECHAZADO = "C";
    public static String REVERTIDO = "R";
    
    //Soporte para pago "tipo efectivo" Allaria Ledesma y Cia S.A
    public static String ALLARIA = "A";

    /*
     * José Fantasia Anexado para manejar estado impreso
     *
     */
    public static String IMPRESO = "I";

    /**
     * After Save
     *
     * @param newRecord new
     * @param success success
     * @return succes
     */
    protected boolean afterSave(boolean newRecord, boolean success) {
        //	Actualiza la cobranza
       /* if (success && newRecord) {
            // PC Banking has same behaviour that CHEQUE PROPIO type
            if (getTIPO().equals(PROPIO) || getTIPO().equals(PCBANKING)) {
                int key = getC_BankAccount_ID();

                String nextNroCheque = getNroCheque();
                int nroCheque = Integer.valueOf(nextNroCheque).intValue() + 1;
                //nextNroCheque = String.valueOf(nroCheque);

                DB.executeUpdate("UPDATE C_BankAccountDoc SET CURRENTNEXT = " + nroCheque + " WHERE C_BankAccount_ID = " + key, null);
            }
        }*/
        // if success and whethever other condificion
        if (success) {
            if (getTIPO().equals("T")) {
                /**
                 * [DEV] ERP - Transferencia autoincremental en Pago Update the sequence for Nro. de Transferencia only
                 * if the type is "Transferencia Bancaria"
                 *
                 * @author Ezequiel Scott @ Zynnia
                 */
                // SQL sentence for select the sequence ID
                /*
                String sql = "SELECT AD_SEQUENCE_ID "
                        + "FROM AD_SEQUENCE "
                        + "WHERE Name = 'C_VALORPAGO_NROTRANSFERENCIA'";
                try {
                    PreparedStatement pstm = DB.prepareStatement(sql, null);
                    ResultSet rs = pstm.executeQuery();
                    int AD_Sequence_ID = 0;
                    if (rs.next()) {
                        AD_Sequence_ID = rs.getInt(1);
                        // Obtain the objetct MSquence given the AD_Sequence_ID
                        MSequence seq = new MSequence(getCtx(), AD_Sequence_ID, get_TrxName());

                        // Sequence increment and update
                        seq.setCurrentNext(seq.getCurrentNext() + seq.getIncrementNo());
                        seq.save();

                    } else {
                        JOptionPane.showMessageDialog(null, "No Existe Secuencia para el Tipo de Valor", "Error en Nro. de Transferencia", JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "No Existe Secuencia para el Tipo de Valor" + e.getMessage(), "Error en Nro. de Transferencia", JOptionPane.INFORMATION_MESSAGE);
                }*/
            }
        }
        return success;
    }	//	afterSave
    /**
     * Implementación REQ-037.
     *
     * Módulo de Cheques.
     *
     */
    //TODO PASAR A CONSTANTE
    public static String TRANSFERENCIA = "T";
    public static String RECIBIDO = "R";
    public static String PROPIO = "P";
    private int LEY_DIAS_FA = 30;
    private int LEY_DIAS_FE = 360;
    public static String COMUN = "C";
    public static String DIFERIDO = "D";


    protected boolean beforeSave(boolean newRecord) {

        MPayment pay;
        MMOVIMIENTOFONDOS mov;

        if (getC_Payment_ID() != 0 && getC_MOVIMIENTOFONDOS_ID() == 0) {
            pay = new MPayment(getCtx(), getC_Payment_ID(), get_TrxName());
            mov = null;
        } else if (getC_Payment_ID() == 0 && getC_MOVIMIENTOFONDOS_ID() != 0) {
            mov = new MMOVIMIENTOFONDOS(getCtx(), getC_MOVIMIENTOFONDOS_ID(), get_TrxName());
            pay = null;
        } else {
            mov = null;
            pay = null;
        }

        //---
        if ((pay != null && !pay.isProcessed()) || (mov != null && !mov.isProcessed())) {
            //	VerificaTipo la paog
            if (getTIPO().equals(TRANSFERENCIA)) //TODO PASAR A CONSTANTE
            {
                //	Verificación Parámetros
                if ((getC_BankAccount_ID() == null) || (getC_BankAccount_ID().equals(0))) {
                    JOptionPane.showMessageDialog(null, "Ingrese Cuenta Bancaria", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if ((getNroTransferencia() == null)/* || (new Integer(getNroTransferencia()) == 0)*/) {
                    JOptionPane.showMessageDialog(null, "Ingrese Nro de Transferencia", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (getDebitoDate() == null) {
                    JOptionPane.showMessageDialog(null, "Ingrese Fecha de Débito", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                //	Verificación de Datos
                //	Lógica
                setEstado("");
                setNroCheque(null);
            } else {
                if (getTIPO().equals(RECIBIDO)) {
                    //	Verificación Parámetros
                    if (getC_PAYMENTVALORES_ID() == 0) {
                        JOptionPane.showMessageDialog(null, "Ingrese Nro de Cheque", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    //	Verificación de Datos
                    //	Lógica
                    setEstado("");
                    MPAYMENTVALORES payval = new MPAYMENTVALORES(getCtx(), getC_PAYMENTVALORES_ID(), get_TrxName());
                    payval.setEstado("E");		//TODO PASAR A CONSTANTE
                    payval.save(get_TrxName());
                } else {
                    // PC Banking has same behaviour that CHEQUE PROPIO type
                    if (getTIPO().equals(PROPIO) || getTIPO().equals(PCBANKING)) {
                        //	Verificación Parámetros

                        // Forced the state issued for checks PCBANKING because the field
                        // is not displayed in the form
                        if(getTIPO().equals(PCBANKING)) {
                            setEstado(EMITIDO);
                        }

                        if (getIMPORTE() == null || getIMPORTE().equals(BigDecimal.ZERO)) {
                            JOptionPane.showMessageDialog(null, "Ingrese Importe", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        if ((getC_BankAccount_ID() == null) || (getC_BankAccount_ID().equals(0))) {
                            JOptionPane.showMessageDialog(null, "Ingrese Cuenta Bancaria", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                        //Set Nro de Cheque.
                        
                        /* Now in completeIt MPayment
                        String sql = "SELECT CURRENTNEXT,TO FROM C_BankAccountDoc WHERE C_BankAccount_ID = ? and IsActive='Y'";

                        try {

                            PreparedStatement pstm = DB.prepareStatement(sql, null);
                            pstm.setInt(1, getC_BankAccount_ID());

                            ResultSet rs = pstm.executeQuery();

                            //	Set Info to Tab
                            if (rs.next()) {
                                int next = rs.getInt(1);
                                int to = rs.getInt(2);

                                if (next <= to) {
                                    String prefix = rs.getString(1);

                                    switch (prefix.length()) {
                                        case 1:
                                            prefix = "0000000" + prefix;
                                            break;
                                        case 2:
                                            prefix = "000000" + prefix;
                                            break;
                                        case 3:
                                            prefix = "00000" + prefix;
                                            break;
                                        case 4:
                                            prefix = "0000" + prefix;
                                            break;
                                        case 5:
                                            prefix = "000" + prefix;
                                            break;
                                        case 6:
                                            prefix = "00" + prefix;
                                            break;
                                        case 7:
                                            prefix = "0" + prefix;
                                            break;
                                        case 8:
                                            break;
                                        default:
                                            JOptionPane.showMessageDialog(null, "Número de Cheque Incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                                    }

                                    setNroCheque(prefix);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Actualice Documento de Cuenta Bancaria", "Chequera Completa", JOptionPane.INFORMATION_MESSAGE);
                                    return false;
                                }
                            }
                        } catch (Exception e) {
                        }*/

                        if ((getTipoCheque() == null) || (getTipoCheque().equals(""))) {
                            JOptionPane.showMessageDialog(null, "Ingrese Tipo de Cheque", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        if (getReleasedDate() == null) {
                            JOptionPane.showMessageDialog(null, "Ingrese Fecha de Emisión", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        if (getPaymentDate() == null) {
                            JOptionPane.showMessageDialog(null, "Ingrese Fecha de Pago", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                        //	Verificación de Datos
                        if (pay != null) {
                            if (getReleasedDate().compareTo(pay.getDateTrx()) == -1) {
                                JOptionPane.showMessageDialog(null, "Verifique que la Fecha de Emisión sea superior o igual a la Fecha del Pago", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }
                        } else if (getReleasedDate().compareTo(mov.getDateTrx()) == -1) {
                            JOptionPane.showMessageDialog(null, "Verifique que la Fecha de Emisión sea superior o igual a la Fecha del Movimiento", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                        Date datePay = new Date(getPaymentDate().getTime());
                        Date dateEmi = new Date(getReleasedDate().getTime());

                        Calendar date = Calendar.getInstance();

                        Date dateAct = date.getTime();

                        date.setTime(dateEmi);
                        date.add(Calendar.DATE, LEY_DIAS_FE);
                        Date dateVerFE = date.getTime();

                        date.setTime(datePay);
                        date.add(Calendar.DATE, LEY_DIAS_FA);
                        Date dateVerFA = date.getTime();

                        if (dateAct.after(dateVerFA)) {
                            JOptionPane.showMessageDialog(null, "La Fecha de Pago supera en al menos " + LEY_DIAS_FA + " días la Fecha Actual", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                            return false;
                        } else if (getTipoCheque().equals(COMUN)) {
                            if (!datePay.equals(dateEmi)) {
                                JOptionPane.showMessageDialog(null, "Verifique que la Fecha de Emisión sea igual a la Fecha de Pago", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }
                        } else {
                            date.setTime(dateEmi);
                            date.add(Calendar.DATE, 1);
                            Date EmiTomorrow = date.getTime();
                            if (EmiTomorrow.after(datePay)) {
                                JOptionPane.showMessageDialog(null, "Verifique que la Fecha de Pago sea mayor a la Fecha de Emisión", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }
                            if (datePay.after(dateVerFE)) {
                                JOptionPane.showMessageDialog(null, "Verifique que la Fecha de Pago no supere la Fecha de Emisión por más de " + LEY_DIAS_FE + " días", "Error - Verificación", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }
                        }

                        try {
                            String query = " SELECT v.C_VALORPAGO_ID "
                                    + " FROM C_VALORPAGO v "
                                    + " INNER JOIN C_Payment p ON (p.c_payment_id = v.c_payment_id) "
                                    + " WHERE v.NROCHEQUE = '" + getNroCheque() + "' AND v.ISACTIVE='Y' "
                                    + "   AND p.ISACTIVE='Y' AND v.C_BankAccount_ID = " + getC_BankAccount_ID();

                            PreparedStatement pstmt = DB.prepareStatement(query, null);
                            ResultSet rs = pstmt.executeQuery();

                            if (rs.next() && (getC_VALORPAGO_ID() != rs.getInt(1)) && !getNroCheque().equals("00000000")) {
                                JOptionPane.showMessageDialog(null, "El Nro de Cheque ingresado ya existe.", "Error - Nro de Cheque duplicado", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }

                            rs.close();
                            pstmt.close();
                        } catch (Exception n) {
                        }

                        //	Lógica

                        setC_MOVIMIENTOFONDOS_ID(0);

                    }// PROPIO
                }
            }

        } // DocStatus = DR
        //TODO PASAR A CONSTANTE
        if (("D".equals(getEstado())) && (getDebitoDate() == null)) {
            JOptionPane.showMessageDialog(null, "Ingrese Fecha de Débito", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (("P".equals(getEstado())) && ((getNroRecibo() == null) || (getNroRecibo().equals("0")))) {
            JOptionPane.showMessageDialog(null, "Ingrese Nro de Recibo", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (("P".equals(getEstado())) && (getEntregaDate() == null)) {
            JOptionPane.showMessageDialog(null, "Ingrese Fecha de Entrega", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if ((isChange()) && (getVencimientoDate() == null)) {
            JOptionPane.showMessageDialog(null, "La nueva Fecha de Pago no ha sido ingresada", "Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!((getNroRecibo() == null) || (getNroRecibo().equals("")))) {
            setReadOnlyNR(true);
        }
        if (getEntregaDate() != null) {
            setReadOnlyED(true);
        }
        if (getDebitoDate() != null) {
            setReadOnlyDD(true);
        }
        if (getVencimientoDate() != null) {
            setReadOnlyVD(true);
        }
        
        //Validacion ALLARIA -> Solo cuenta de allaria
        if ( getTIPO().equals(ALLARIA) )
            setC_AcctSchema_ID(getElementValueForAllaria());

        return true;
    }	//	beforeSave

    public static MVALORPAGO get(int C_BankAccount_ID, String nroCheque, String trxName) {

        String sql = " SELECT C_ValorPago_ID "
                + " FROM C_ValorPago "
                + " WHERE C_BankAccount_ID = ? AND IsActive = 'Y' AND NroCheque=?";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, C_BankAccount_ID);
            pstmt.setString(2, nroCheque);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new MVALORPAGO(Env.getCtx(), rs.getInt(1), trxName);
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }
    
    /**
     * Set MEXTRANJERA
     */
    
    public void setMEXTRANJERA(BigDecimal MEXTRANJERA) {
        if (MEXTRANJERA == null) {
            throw new IllegalArgumentException("IMPORTE is mandatory.");
        }
        
        MPayment pay = new MPayment(Env.getCtx(),getC_Payment_ID(),null);
        
        /*
         *  Modificado para que haga el control de los decimales de presición (a dos decimales)
         *  Zynnia - José Fantasia
         *  19/03/2012
         * 
         */
                        
        MCurrency moneda = MCurrency.get (Env.getCtx(), pay.getC_Currency_ID());
        if (moneda != null) {
            BigDecimal valueRound = (BigDecimal)MEXTRANJERA;
            valueRound = valueRound.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
            set_Value("MEXTRANJERA", valueRound);
        } else {
            set_Value("MEXTRANJERA", MEXTRANJERA);
        }   
        
        
        
    }
    
    /**
     * Set IMPORTE
     */
    public void setIMPORTE(BigDecimal IMPORTE) {
        
        if (IMPORTE == null) {
            throw new IllegalArgumentException("IMPORTE is mandatory.");
        }
       
        MPayment pay = new MPayment(Env.getCtx(),getC_Payment_ID(),get_TrxName());
        
        /*
         *  Modificado para que haga el control de los decimales de presición (a dos decimales)
         *  Zynnia - José Fantasia
         *  19/03/2012
         * 
         */
        int currencyBase = Env.getContextAsInt(Env.getCtx(), "$C_Currency_ID");
        
        MCurrency moneda = MCurrency.get (Env.getCtx(), currencyBase);
        
        if (moneda != null) {
            BigDecimal valueRound = (BigDecimal)IMPORTE;
            valueRound = valueRound.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
            set_Value("IMPORTE", valueRound);
        } else {
            set_Value("IMPORTE", IMPORTE);
        }
        
    }   
    
    public static MVALORPAGO getCheque(Properties ctx, String cheque, String trxName) {
        MVALORPAGO ret = null;
        String sql = "SELECT * "
                        + "FROM C_ValorPago "
                        + "WHERE NroCheque = '" +cheque + "'";
                try {
                    PreparedStatement pstm = DB.prepareStatement(sql, trxName);
                    ResultSet rs = pstm.executeQuery();
                    if (rs.next()) 
                        ret = new MVALORPAGO(ctx, rs, trxName);
                } catch (Exception e) {
                    e.printStackTrace();
                    
                }
                        
        return ret;
    }
    
    
    
    
}	//	MValorPago
