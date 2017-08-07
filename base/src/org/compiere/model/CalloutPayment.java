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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.JOptionPane;
import org.apache.tools.ant.taskdefs.condition.IsReference;
import org.compiere.util.*;

/**
 *	Payment Callouts.
 *	org.compiere.model.CalloutPayment.*
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutPayment.java,v 1.17 2005/11/06 01:17:27 jjanke Exp $
 */
public class CalloutPayment extends CalloutEngine {

    /**
     *  Payment_Invoice.
     *  when Invoice selected
     *  - set C_Currency_ID
     * 		- C_BPartner_ID
     *  	- DiscountAmt = C_Invoice_Discount (ID, DateTrx)
     *   	- PayAmt = invoiceOpen (ID) - Discount
     * 		- WriteOffAmt = 0
     */
    public String invoice(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        Integer C_Invoice_ID = (Integer) value;
        if (isCalloutActive() //	assuming it is resetting value
                || C_Invoice_ID == null || C_Invoice_ID.intValue() == 0) {
            return "";
        }
        setCalloutActive(true);
        mTab.setValue("C_Order_ID", null);
        mTab.setValue("C_Charge_ID", null);
        mTab.setValue("IsPrepayment", Boolean.FALSE);
        //
        mTab.setValue("DiscountAmt", Env.ZERO);
        mTab.setValue("WriteOffAmt", Env.ZERO);
        mTab.setValue("IsOverUnderPayment", Boolean.FALSE);
        mTab.setValue("OverUnderAmt", Env.ZERO);

        int C_InvoicePaySchedule_ID = 0;
        if (Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "C_Invoice_ID") == C_Invoice_ID.intValue()
                && Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "C_InvoicePaySchedule_ID") != 0) {
            C_InvoicePaySchedule_ID = Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "C_InvoicePaySchedule_ID");
        }

        //  Payment Date
        Timestamp ts = (Timestamp) mTab.getValue("DateTrx");
        if (ts == null) {
            ts = new Timestamp(System.currentTimeMillis());
        }
        //
        String sql = "SELECT C_BPartner_ID,C_Currency_ID," //	1..2
                + " invoiceOpen(C_Invoice_ID, ?)," //	3		#1
                + " invoiceDiscount(C_Invoice_ID,?,?), IsSOTrx " //	4..5	#2/3
                + "FROM C_Invoice WHERE C_Invoice_ID=?";			//			#4
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, C_InvoicePaySchedule_ID);
            pstmt.setTimestamp(2, ts);
            pstmt.setInt(3, C_InvoicePaySchedule_ID);
            pstmt.setInt(4, C_Invoice_ID.intValue());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                mTab.setValue("C_BPartner_ID", new Integer(rs.getInt(1)));
                int C_Currency_ID = rs.getInt(2);					//	Set Invoice Currency
                mTab.setValue("C_Currency_ID", new Integer(C_Currency_ID));
                //
                BigDecimal InvoiceOpen = rs.getBigDecimal(3);		//	Set Invoice OPen Amount
                if (InvoiceOpen == null) {
                    InvoiceOpen = Env.ZERO;
                }
                BigDecimal DiscountAmt = rs.getBigDecimal(4);		//	Set Discount Amt
                if (DiscountAmt == null) {
                    DiscountAmt = Env.ZERO;
                }
                mTab.setValue("PayAmt", InvoiceOpen.subtract(DiscountAmt));
                mTab.setValue("DiscountAmt", DiscountAmt);
                //  reset as dependent fields get reset
                Env.setContext(ctx, WindowNo, "C_Invoice_ID", C_Invoice_ID.toString());
                mTab.setValue("C_Invoice_ID", C_Invoice_ID);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql, e);
            setCalloutActive(false);
            return e.getLocalizedMessage();
        }

        setCalloutActive(false);
        return docType(ctx, WindowNo, mTab, mField, value);
    }	//	invoice

    /*
     * 	Convierte de Moneda Original a Moneda Nacional
     */
    public String changeRate(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
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
            pay.setCotizacion((BigDecimal) mTab.getValue("COTIZACION"));
            pay.save();
            List<MPAYMENTVALORES> payval = pay.getValoresCobranza();
            if (payval != null) {
                for (int i = 0; i < payval.size(); i++) {
                    MPAYMENTVALORES valor = payval.get(i);
                    if (valor.getTIPO().equals(MPAYMENTVALORES.MEXT)) {
                        valor.setIMPORTE(valor.getExtranjera().multiply(((BigDecimal) value)));
                    } else {
                        valor.setIMPORTE(valor.getConvertido().multiply(pay.getCotizacion()));
                    }
                    valor.save();
                }
            }
        }

        setCalloutActive(false);
        return "";
    }	//	dates

    /**
     *  Payment_Order.
     *  when Waiting Payment Order selected
     *  - set C_Currency_ID
     * 		- C_BPartner_ID
     *  	- DiscountAmt = C_Invoice_Discount (ID, DateTrx)
     *   	- PayAmt = invoiceOpen (ID) - Discount
     * 		- WriteOffAmt = 0
     */
    public String order(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        Integer C_Order_ID = (Integer) value;
        if (isCalloutActive() //	assuming it is resetting value
                || C_Order_ID == null || C_Order_ID.intValue() == 0) {
            return "";
        }
        setCalloutActive(true);
        mTab.setValue("C_Invoice_ID", null);
        mTab.setValue("C_Charge_ID", null);
        mTab.setValue("IsPrepayment", Boolean.TRUE);
        //
        mTab.setValue("DiscountAmt", Env.ZERO);
        mTab.setValue("WriteOffAmt", Env.ZERO);
        mTab.setValue("IsOverUnderPayment", Boolean.FALSE);
        mTab.setValue("OverUnderAmt", Env.ZERO);

        //  Payment Date
        Timestamp ts = (Timestamp) mTab.getValue("DateTrx");
        if (ts == null) {
            ts = new Timestamp(System.currentTimeMillis());
        }
        //
        String sql = "SELECT C_BPartner_ID,C_Currency_ID, GrandTotal "
                + "FROM C_Order WHERE C_Order_ID=?"; 	// #1
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, C_Order_ID.intValue());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                mTab.setValue("C_BPartner_ID", new Integer(rs.getInt(1)));
                int C_Currency_ID = rs.getInt(2);					//	Set Order Currency
                mTab.setValue("C_Currency_ID", new Integer(C_Currency_ID));
                //
                BigDecimal GrandTotal = rs.getBigDecimal(3);		//	Set Pay Amount
                if (GrandTotal == null) {
                    GrandTotal = Env.ZERO;
                }
                mTab.setValue("PayAmt", GrandTotal);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql, e);
            setCalloutActive(false);
            return e.getLocalizedMessage();
        }

        setCalloutActive(false);
        return docType(ctx, WindowNo, mTab, mField, value);
    }	//	order

    /**
     *  Payment_Project.
     *  - reset - C_BPartner_ID, Invoice, Order, Project,
     *  	Discount, WriteOff
     */
    public String project(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        Integer C_Project_ID = (Integer) value;
        if (isCalloutActive() //	assuming it is resetting value
                || C_Project_ID == null || C_Project_ID.intValue() == 0) {
            return "";
        }
        setCalloutActive(true);
        mTab.setValue("C_Charge_ID", null);
        setCalloutActive(false);
        return "";
    }	//	project

    /**
     *  Payment_Charge.
     *  - reset - C_BPartner_ID, Invoice, Order, Project,
     *  	Discount, WriteOff
     */
    public String charge(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        Integer C_Charge_ID = (Integer) value;
        if (isCalloutActive() //	assuming it is resetting value
                || C_Charge_ID == null || C_Charge_ID.intValue() == 0) {
            return "";
        }
        setCalloutActive(true);
        mTab.setValue("C_Invoice_ID", null);
        mTab.setValue("C_Order_ID", null);
        mTab.setValue("C_Project_ID", null);
        mTab.setValue("IsPrepayment", Boolean.FALSE);
        //
        mTab.setValue("DiscountAmt", Env.ZERO);
        mTab.setValue("WriteOffAmt", Env.ZERO);
        mTab.setValue("IsOverUnderPayment", Boolean.FALSE);
        mTab.setValue("OverUnderAmt", Env.ZERO);
        setCalloutActive(false);
        return "";
    }	//	charge

    /**
     *  Payment_Document Type.
     * 	Verify that Document Type (AP/AR) and Invoice (SO/PO) are in sync
     */
    public String docType(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        int C_Invoice_ID = Env.getContextAsInt(ctx, WindowNo, "C_Invoice_ID");
        int C_Order_ID = Env.getContextAsInt(ctx, WindowNo, "C_Order_ID");
        int C_DocType_ID = Env.getContextAsInt(ctx, WindowNo, "C_DocType_ID");
        log.fine("Payment_DocType - C_Invoice_ID=" + C_Invoice_ID + ", C_DocType_ID=" + C_DocType_ID);
        MDocType dt = null;
        if (C_DocType_ID != 0) {
            dt = MDocType.get(ctx, C_DocType_ID);
            Env.setContext(ctx, WindowNo, "IsSOTrx", dt.isSOTrx() ? "Y" : "N");
        }
        //	Invoice
        if (C_Invoice_ID != 0) {
            MInvoice inv = new MInvoice(ctx, C_Invoice_ID, null);
            if (dt != null) {
                if (inv.isSOTrx() != dt.isSOTrx()) {
                    return "PaymentDocTypeInvoiceInconsistent";
                }
            }
        }
        //	Order Waiting Payment (can only be SO)
        if (dt != null) {
            if (C_Order_ID != 0 && !dt.isSOTrx()) {
                return "PaymentDocTypeInvoiceInconsistent";
            }
        }

        return "";
    }	//	docType

    /**
     *  Payment_Amounts.
     *	Change of:
     *		- IsOverUnderPayment -> set OverUnderAmt to 0
     *		- C_Currency_ID, C_ConvesionRate_ID -> convert all
     *		- PayAmt, DiscountAmt, WriteOffAmt, OverUnderAmt -> PayAmt
     *			make sure that add up to InvoiceOpenAmt
     */
    public String amounts(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        int C_Invoice_ID = Env.getContextAsInt(ctx, WindowNo, "C_Invoice_ID");
        //	New Payment
        if (Env.getContextAsInt(ctx, WindowNo, "C_Payment_ID") == 0
                && Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID") == 0
                && C_Invoice_ID == 0) {
            return "";
        }
        setCalloutActive(true);

        //	Changed Column

        String colName = mField.getColumnName();
        if (colName.equals("IsOverUnderPayment") //	Set Over/Under Amt to Zero
                || !"Y".equals(Env.getContext(ctx, WindowNo, "IsOverUnderPayment"))) // Begin e-Evolution 12/04/2004 ogi-cd actualizacion de campo OverUnderAmt
        //mTab.setValue("OverUnderAmt", Env.ZERO);
        {
            mTab.setValue("OverUnderAmt", (BigDecimal) mTab.getValue("WriteOffAmt"));
        }
        mTab.setValue("WriteOffAmt", Env.ZERO);
        // End e-Evolution 12/04/2004 ogi-cd

        int C_InvoicePaySchedule_ID = 0;
        if (Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "C_Invoice_ID") == C_Invoice_ID
                && Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "C_InvoicePaySchedule_ID") != 0) {
            C_InvoicePaySchedule_ID = Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "C_InvoicePaySchedule_ID");
        }

        //	Get Open Amount & Invoice Currency
        BigDecimal InvoiceOpenAmt = Env.ZERO;
        int C_Currency_Invoice_ID = 0;
        if (C_Invoice_ID != 0) {
            Timestamp ts = (Timestamp) mTab.getValue("DateTrx");
            if (ts == null) {
                ts = new Timestamp(System.currentTimeMillis());
            }
            String sql = "SELECT C_BPartner_ID,C_Currency_ID," //	1..2
                    + " invoiceOpen(C_Invoice_ID,?)," //	3		#1
                    + " invoiceDiscount(C_Invoice_ID,?,?), IsSOTrx " //	4..5	#2/3
                    + "FROM C_Invoice WHERE C_Invoice_ID=?";			//			#4
            try {
                PreparedStatement pstmt = DB.prepareStatement(sql, null);
                pstmt.setInt(1, C_InvoicePaySchedule_ID);
                pstmt.setTimestamp(2, ts);
                pstmt.setInt(3, C_InvoicePaySchedule_ID);
                pstmt.setInt(4, C_Invoice_ID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    C_Currency_Invoice_ID = rs.getInt(2);
                    InvoiceOpenAmt = rs.getBigDecimal(3);		//	Set Invoice Open Amount
                    if (InvoiceOpenAmt == null) {
                        InvoiceOpenAmt = Env.ZERO;
                    }
                }
                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                log.log(Level.SEVERE, sql, e);
                setCalloutActive(false);
                return e.getLocalizedMessage();
            }
        }	//	get Invoice Info
        log.fine("Open=" + InvoiceOpenAmt + ", C_Invoice_ID=" + C_Invoice_ID
                + ", C_Currency_ID=" + C_Currency_Invoice_ID);

        //	Get Info from Tab
        BigDecimal PayAmt = (BigDecimal) mTab.getValue("PayAmt");
        BigDecimal DiscountAmt = (BigDecimal) mTab.getValue("DiscountAmt");
        BigDecimal WriteOffAmt = (BigDecimal) mTab.getValue("WriteOffAmt");
        BigDecimal OverUnderAmt = (BigDecimal) mTab.getValue("OverUnderAmt");
        log.fine("Pay=" + PayAmt + ", Discount=" + DiscountAmt
                + ", WriteOff=" + WriteOffAmt + ", OverUnderAmt=" + OverUnderAmt);
        //	Get Currency Info
        int C_Currency_ID = ((Integer) mTab.getValue("C_Currency_ID")).intValue();
        MCurrency currency = MCurrency.get(ctx, C_Currency_ID);
        Timestamp ConvDate = (Timestamp) mTab.getValue("DateTrx");
        int C_ConversionType_ID = 0;
        Integer ii = (Integer) mTab.getValue("C_ConversionType_ID");
        if (ii != null) {
            C_ConversionType_ID = ii.intValue();
        }
        int AD_Client_ID = Env.getContextAsInt(ctx, WindowNo, "AD_Client_ID");
        int AD_Org_ID = Env.getContextAsInt(ctx, WindowNo, "AD_Org_ID");
        //	Get Currency Rate
        BigDecimal CurrencyRate = Env.ONE;
        if ((C_Currency_ID > 0 && C_Currency_Invoice_ID > 0
                && C_Currency_ID != C_Currency_Invoice_ID)
                || colName.equals("C_Currency_ID") || colName.equals("C_ConversionType_ID")) {
            log.fine("InvCurrency=" + C_Currency_Invoice_ID
                    + ", PayCurrency=" + C_Currency_ID
                    + ", Date=" + ConvDate + ", Type=" + C_ConversionType_ID);
            CurrencyRate = MConversionRate.getRate(C_Currency_Invoice_ID, C_Currency_ID,
                    ConvDate, C_ConversionType_ID, AD_Client_ID, AD_Org_ID);
            if (CurrencyRate == null || CurrencyRate.compareTo(Env.ZERO) == 0) {
                //	mTab.setValue("C_Currency_ID", new Integer(C_Currency_Invoice_ID));	//	does not work
                setCalloutActive(false);
                if (C_Currency_Invoice_ID == 0) {
                    return "";		//	no error message when no invoice is selected
                }
                return "NoCurrencyConversion";
            }
            //
            InvoiceOpenAmt = InvoiceOpenAmt.multiply(CurrencyRate).setScale(currency.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            log.fine("Rate=" + CurrencyRate + ", InvoiceOpenAmt=" + InvoiceOpenAmt);
        }

        //	Currency Changed - convert all
        if (colName.equals("C_Currency_ID") || colName.equals("C_ConversionType_ID")) {
            PayAmt = PayAmt.multiply(CurrencyRate).setScale(currency.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            mTab.setValue("PayAmt", PayAmt);
            DiscountAmt = DiscountAmt.multiply(CurrencyRate).setScale(currency.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            mTab.setValue("DiscountAmt", DiscountAmt);
            WriteOffAmt = WriteOffAmt.multiply(CurrencyRate).setScale(currency.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            mTab.setValue("WriteOffAmt", WriteOffAmt);
            OverUnderAmt = OverUnderAmt.multiply(CurrencyRate).setScale(currency.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            mTab.setValue("OverUnderAmt", OverUnderAmt);
        } //	No Invoice - Set Discount, Witeoff, Under/Over to 0
        else if (C_Invoice_ID == 0) {
            if (DiscountAmt == null || Env.ZERO.compareTo(DiscountAmt) != 0) {
                mTab.setValue("DiscountAmt", Env.ZERO);
            }
            if (WriteOffAmt == null || Env.ZERO.compareTo(WriteOffAmt) != 0) {
                mTab.setValue("WriteOffAmt", Env.ZERO);
            }
            if (OverUnderAmt == null || Env.ZERO.compareTo(OverUnderAmt) != 0) {
                mTab.setValue("OverUnderAmt", Env.ZERO);
            }
        } //  PayAmt - calculate write off
        else if (colName.equals("PayAmt")) {
            // Begin e-Evolution 27/12/2005 ogi-cd
            //WriteOffAmt = InvoiceOpenAmt.subtract(PayAmt).subtract(DiscountAmt).subtract(OverUnderAmt);
            //mTab.setValue("WriteOffAmt", WriteOffAmt);
            mTab.setValue("IsOverUnderPayment", "Y");
            mTab.setValue("OverUnderAmt", InvoiceOpenAmt.subtract(PayAmt).subtract(DiscountAmt));
            // End e-Evolution 27/12/2005 ogi-cd

        } else //  calculate PayAmt
        {
            PayAmt = InvoiceOpenAmt.subtract(DiscountAmt).subtract(WriteOffAmt).subtract(OverUnderAmt);
            mTab.setValue("PayAmt", PayAmt);
        }

        setCalloutActive(false);
        return "";
    }	//	amounts

    public String lugarEntrega(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        //	MPayment pay = new MPayment(ctx,Env.getContextAsInt(ctx, WindowNo, "C_Payment_ID"),null);
        try {
            PreparedStatement pstm = DB.prepareStatement("SELECT C_JURISDICCION_ID FROM C_BPartner_Jurisdiccion"
                    + " WHERE C_BPartner_ID = ? ORDER BY C_JURISDICCION_ID", null);
            pstm.setInt(1, Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID"));
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                mTab.setValue("C_JURISDICCION_ID", rs.getInt(1));
            } else {
                mTab.setValue("C_JURISDICCION_ID", null);
            }

            pstm.close();
            rs.close();
        } catch (Exception e) {
        }

        setCalloutActive(false);
        return "";
    }	//	lugarEntrega

    public String MExtranjera(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) //	assuming it is resetting value
        {
            return "";
        }
        setCalloutActive(true);

        MPayment pay = new MPayment(Env.getCtx(), Env.getContextAsInt(Env.getCtx(), WindowNo, "C_Payment_ID"), null);
        /*
         * 04-01-2011 Camarzana Mariano
         * Se agrego el control de si existe el id C_Payment para eliminar el mensaje de null pointer exception
         * al crear una nueva cobranza 
         */
        if (pay.getC_Payment_ID() != 0) {

            pay.save();

            //	Changed Column
            int PCurr = ((Integer) value).intValue();
            int ASCurr = Env.getContextAsInt(ctx, "$C_Currency_ID");;

            Timestamp dateTrx = (Timestamp) mTab.getValue("DateTrx");
            int C_ConversionType_ID = ((Integer) mTab.getValue("C_ConversionType_ID")).intValue();
            int AD_Client_ID = ((Integer) mTab.getValue("AD_Client_ID")).intValue();
            int AD_Org_ID = ((Integer) mTab.getValue("AD_Org_ID")).intValue();


            if (ASCurr != 0 && PCurr != 0) {
                if (ASCurr == PCurr) {
                    mTab.setValue("FOREINGCURR", "N");
                } else {
                    mTab.setValue("FOREINGCURR", "Y");
                }

                //	Get Rate
                mTab.setValue("COTIZACION", TasaCambio.rate(PCurr, ASCurr, dateTrx, C_ConversionType_ID, AD_Client_ID, AD_Org_ID));
            } else {
                mTab.setValue("FOREINGCURR", "N");
            }

            if (!pay.isReceipt()) {
                setCalloutActive(false);
                amounts(ctx, WindowNo, mTab, mField, value, oldValue);
                setCalloutActive(true);
            }
        }
        setCalloutActive(false);
        return "";
    }

    /**  26/11/2010 Camarzana Mariano
     * 	Al deschequear calculo automatico pone los importes en 0
     *  
     */
    public String calculoAutomatico(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {

        //mTab.setValue("COBRADOR", mb.getCobrador());
        //	}
        return "";
    }

    /**
     * 06-01-2011 Camarzana Mariano
     * Metodo requerido para setear automaticamente el cobrador al 
     * seleccionar el socio de negocio en Cobranzas
     */
    public String cobrador(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue) {
        if (isCalloutActive()) {
            return "";
        }

        Integer C_BPartner_ID = (Integer) value;
        if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0) {
            return "";
        }
        setCalloutActive(true);
        MBPartner partner = new MBPartner(ctx, C_BPartner_ID, null);
        if ("Y".equals(Env.getContext(Env.getCtx(), WindowNo, "IsReceipt"))) {
            mTab.setValue("COBRADOR", partner.getSalesRep_ID());
        }

        setCalloutActive(false);
        return "";
    }	//	cobrador	

    /*
     *  Zynnia 26/07/2012 
     *  Agregamos este metodo, para que al elegir un socio de negocio en una OP
     *  traiga por defecto la direccion del mismo.
     * 
     */
    public String bPartner(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        if (isCalloutActive()) {
            return "";
        }

        Integer C_BPartner_ID = (Integer) value;
        if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0) {
            return "";
        }
        setCalloutActive(true);

        MBPartner partner = new MBPartner(ctx, C_BPartner_ID, null);

        String sql = "SELECT lbill.C_BPartner_Location_ID AS BillLocation "
                + " FROM C_BPartner p"
                + " LEFT OUTER JOIN C_BPartner_Location lbill ON (p.C_BPartner_ID=lbill.C_BPartner_ID AND lbill.IsBillTo='Y' AND lbill.IsActive='Y')"
                + " WHERE p.C_BPartner_ID=? AND p.IsActive='Y'";		//	#1
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, partner.getC_BPartner_ID());
            ResultSet rs = pstmt.executeQuery();
            //
            if (rs.next()) {
                mTab.setValue("C_BPartner_Location_ID", rs.getInt("BillLocation"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "bPartner", e);
            setCalloutActive(false);
            return e.getLocalizedMessage();
        }
        /*
         *  27/03/2013 Maria Jesus Martin
         *  Si el pago esta en Borrador, y se cambia el socio de negocio, en el caso que tenga 
         *  facturas asignadas se eliminan las mismas de la pestaña asignacion y se le informa
         *  al usuario que se eliminaron las mismas debido al cambio del SN.
         */
        Integer C_Payment_ID = (Integer) mTab.getValue("C_Payment_ID");
        sql = " SELECT C_PAYMENTALLOCATE_ID "
                + " FROM C_PAYMENTALLOCATE"
                + " WHERE C_PAYMENT_ID = " + C_Payment_ID;
        try {
            PreparedStatement pstmtDelete = DB.prepareStatement(sql, null);
            ResultSet rsDelete = pstmtDelete.executeQuery();
            //
            while (rsDelete.next()) {
                //Elimino las lineas.
                String sqlDelete = "DELETE FROM C_PAYMENTALLOCATE WHERE C_PAYMENTALLOCATE_ID = " + rsDelete.getInt(1);
                PreparedStatement ps = DB.prepareStatement(sqlDelete, null);
                ResultSet rsD = ps.executeQuery();
                rsD.close();
                ps.close();
            }
            //Pongo en cero el total del pago
            MPayment pay = new MPayment(Env.getCtx(), C_Payment_ID.intValue(), null);
            pay.setPayAmt(BigDecimal.ZERO);
            pay.setPAYNET(BigDecimal.ZERO);
            pay.save();
            //Informo al usuario.
            JOptionPane.showMessageDialog(null, "Se han eliminado las facturas asignadas previamente debido a que se cambio el Socio de Negocio", "Cambio de Socio de Negocio", JOptionPane.INFORMATION_MESSAGE);
            rsDelete.close();
            pstmtDelete.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "bPartner", e);
        }
        setCalloutActive(false);
        return "";
    }
}	//	CalloutPayment