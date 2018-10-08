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

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;

import org.compiere.util.*;

/**
 * 	Callout for Allocate Payments
 *
 *  @author Jorg Janke
 *  @version $Id: CalloutPaymentAllocate.java,v 1.2 2005/11/06 01:17:27 jjanke Exp $
 */
public class CalloutValorPago extends CalloutEngine {

    /**
     *  30/12/2008 - Daniel Gini
     *
     *  	Si es un cheque común las fechas deben ser iguales
     *
     */
    public String setChequeRecibido(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        //	Get Info from DB
        int key = Env.getContextAsInt(ctx, WindowNo, "C_PAYMENTVALORES_ID");

        String sql = "SELECT TIPOCHEQUE,BANCO,IMPORTE FROM C_PAYMENTVALORES WHERE C_PAYMENTVALORES_ID = ?";

        try {

            PreparedStatement pstm = DB.prepareStatement(sql, null);
            pstm.setInt(1, key);

            ResultSet rs = pstm.executeQuery();

            //	Set Info to Tab
            if (rs.next()) {
                mTab.setValue("TIPOCHEQUE", rs.getString(1));
                mTab.setValue("BANCO", rs.getString(2));
                mTab.setValue("IMPORTE", rs.getBigDecimal(3));
            }
        } catch (Exception e) {
        }

        setCalloutActive(false);
        return "";
    }	//	setChequeRecibido

    /*
     * 	Convierte de Moneda Original a Moneda Nacional
     */
    public String convertirOriginal(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        //	Get Info from Tab
        Integer c_payment_id = (Integer) mTab.getValue("C_Payment_ID");
        String monedaExt = Env.getContext(Env.getCtx(), WindowNo, "FOREINGCURR");

        if (c_payment_id != null && monedaExt.equals("Y")) {
            MPayment pay = new MPayment(Env.getCtx(), c_payment_id.intValue(), null);

            /*
             *  Modificado para que haga el control de los decimales de presición (a dos decimales)
             *  Zynnia - José Fantasia
             *  19/03/2012
             * 
             */

            MCurrency moneda = MCurrency.get(ctx, pay.getC_Currency_ID());
            if (moneda != null) {

                BigDecimal valueRound = (BigDecimal) value;
                valueRound = valueRound.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                BigDecimal valueRoundConvert = valueRound.multiply(pay.getCotizacion());
                valueRoundConvert = valueRoundConvert.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                mTab.setValue("IMPORTE", valueRoundConvert);
                mTab.setValue("MEXTRANJERA", valueRound);

            }
            //mTab.setValue("IMPORTE", ((BigDecimal)value).multiply(pay.getCotizacion()));
        }

        setCalloutActive(false);
        return "";
    }	//	dates

    public String chequera(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        /*if (isCalloutActive())		//	assuming it is resetting value
        return "";
        setCalloutActive(true);
        
        if (value!=null)
        {
        //	Get Info from DB
        int key = Env.getContextAsInt(ctx, WindowNo, "C_BankAccount_ID");
        
        String sql = "SELECT CURRENTNEXT,HASTA FROM C_BankAccountDoc WHERE C_BankAccount_ID = ? and IsActive='Y'";
        
        try	{
        
        PreparedStatement pstm = DB.prepareStatement(sql, null);
        pstm.setInt(1, key);
        
        ResultSet rs = pstm.executeQuery();
        
        //	Set Info to Tab
        if (rs.next())
        {
        int next = rs.getInt(1);
        int to = rs.getInt(2);
        
        if (next<=to)
        {
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
        JOptionPane.showMessageDialog(null,"Número de Cheque Incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        mTab.setValue("NROCHEQUE", prefix);
        }
        else
        {
        JOptionPane.showMessageDialog(null,"Actualice Documento de Cuenta Bancaria", "Chequera Completa", JOptionPane.INFORMATION_MESSAGE);
        mTab.setValue("NROCHEQUE", "00000000");
        }
        }
        else
        {
        JOptionPane.showMessageDialog(null,"Actualice Documento de Cuenta Bancaria", "Chequera Completa", JOptionPane.INFORMATION_MESSAGE);
        mTab.setValue("NROCHEQUE", "00000000");
        }
        }
        catch (Exception e) {}
        } // value != NULL
        
        setCalloutActive(false);*/
        return "";
    }	//	chequera

    /** BISion - 12/01/2009 - Santiago Ibañez
     * Metodo creado para resetear los campos que no son propios al tipo
     * seteado.
     */
    public String resetFields(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {

        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        //Resetea los campos siempre que se cambie el tipo

        //  TODOS LOS TIPOS

        //Reseteo tarjeta de credito
        mTab.setValue("C_CREDITCARD_ID", null);
        //Reseteo la fecha de debito
        mTab.setValue("DEBITODATE", null);
        //Reseteo el importe
        mTab.setValue("IMPORTE", BigDecimal.ZERO);
        //Reseteo el Banco
        mTab.setValue("BANCO", null);
        //Reseteo el concepto
        mTab.setValue("CONCEPTO", null);
        //Reseteo la Cuenta Bancaria
        mTab.setValue("C_BankAccount_ID", null);
        //Reseteo el Numero de Recibo
        mTab.setValue("NRORECIBO", null);
        //Reseteo la Fecha de Pago
        mTab.setValue("PAYMENTDATE", null);
        //Reseteo el Lugar de Emisión
        mTab.setValue("REALEASEDLOCATION", null);
        //Reseteo el campo No a la orden
        mTab.setValue("ORDEN", null);
        //Reseteo el campo Nro de cheque
        mTab.setValue("NROCHEQUE", null);
        //Reseteo el campo Nro de tramsferencia
        mTab.setValue("NROTRANSFERENCIA", null);
        //Reseteo el campo de estado de cheques
        mTab.setValue("STATE", null);

        //if (value!=null && ((String)value).equals("P"))
        //{

        if (value != null) {
            //CHEQUES
            if (value.equals("B") || value.equals("P") || value.equals("R")) {

                MTable mTable = new MTable(ctx, mTab.getAD_Tab_ID(), mTab.getTableName(), WindowNo, mTab.getTabNo(), true);

                try {
                    MField field = mTable.getField("TIPOCHEQUE");
                    PreparedStatement pstm = DB.prepareStatement(field.getDefaultValue(), null);
                    ResultSet rs = pstm.executeQuery();

                    if (rs.next()) {
                        mTab.setValue("TIPOCHEQUE", rs.getString(1));
                    } else {
                        mTab.setValue("TIPOCHEQUE", null);
                    }

                    pstm.close();
                    rs.close();

                    field = mTable.getField("REALEASEDDATE");
                    pstm = DB.prepareStatement(field.getDefaultValue(), null);
                    rs = pstm.executeQuery();

                    if (rs.next()) {
                        mTab.setValue("REALEASEDDATE", rs.getDate(1));
                    } else {
                        mTab.setValue("REALEASEDDATE", null);
                    }

                    pstm.close();
                    rs.close();
                } catch (Exception e) {
                }
                //Seteo el tipo de cheque por defecto (Solo si no es Recibido - R)
                if (!value.equals("R")) {
                    mTab.setValue("NROCHEQUE", "00000000");
                }

                if (value.equals("P")) //TODO PASAR A CONSTANTE
                {
                    mTab.setValue("STATE", "E");
                }


            }
            //TRANSFERENCIAS BANCARIAS
            if (value.equals("T")) //TODO PASAR A CONSTANTE
            {
                mTab.setValue("NROTRANSFERENCIA", "00000000");
            }
            
            // Soporte para pago "tipo efectivo" Allaria Ledesma y Cia S.A
            if (value.equals("A")) {
                Integer C_ElementValue_ID = MVALORPAGO.getElementValueForAllaria();
                mTab.setValue("C_AcctSchema_ID", C_ElementValue_ID);
             }
        }


        setCalloutActive(false);
        return "";
    }

    /**
     * Method to set the field NROTRANSFERENCIA to an autoincremental value given for AD_SEQUENCE
     *
     * @author Ezequiel Scott @ Zynnia
     */
    public String setNroTransferencia(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        /*if (isCalloutActive())		//	assuming it is resetting value
        return "";
        setCalloutActive(true);
        
        String Tipo = (String)mTab.getValue("TIPO");
        if (Tipo != null && Tipo.equals("T")) {
        try {
        String sql = "SELECT CurrentNext "
        + "FROM AD_SEQUENCE "
        + "WHERE Name = 'C_VALORPAGO_NROTRANSFERENCIA'";
        
        PreparedStatement pstm = DB.prepareStatement(sql, null);
        ResultSet rs = pstm.executeQuery();
        
        if (rs.next()) {
        mTab.setValue("NROTRANSFERENCIA", rs.getString(1));
        }
        } catch (Exception e) {
        JOptionPane.showMessageDialog(null,"No se puede generar automáticamente el Nro. de Transferencia. \n " + e.getMessage() , "Error en Nro. de Transferencia", JOptionPane.INFORMATION_MESSAGE);
        }
        }
        
        setCalloutActive(false);*/
        return "";
    }
}	//	CalloutValorPago
