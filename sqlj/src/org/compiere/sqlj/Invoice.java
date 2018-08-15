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
package org.compiere.sqlj;

import java.math.*;
import java.sql.*;

/**
 *	SQLJ Invoice related Functions
 *	
 *  @author Jorg Janke
 *  @version $Id: Invoice.java,v 1.9 2005/10/11 02:26:14 jjanke Exp $
 */
public class Invoice {

    /**
     * 	Open Invoice Amount.
     * 	- incoiceOpen
     *	@param p_C_Invoice_ID invoice
     *	@param p_C_InvoicePaySchedule_ID payment schedule
     *	@return open amount
     *	@throws SQLException
     */
    public static BigDecimal open(int p_C_Invoice_ID, int p_C_InvoicePaySchedule_ID)
            throws SQLException {
        //	Invoice info
        int C_Currency_ID = 0;
        int C_ConversionType_ID = 0;
        BigDecimal GrandTotal = null;
        BigDecimal MultiplierAP = null;
        BigDecimal MultiplierCM = null;
        //

        String sql = "SELECT MAX(C_Currency_ID),MAX(C_ConversionType_ID),"
                + " SUM(GrandTotal), MAX(MultiplierAP), MAX(Multiplier) "
                + "FROM	C_Invoice_v " //	corrected for CM / Split Payment
                + "WHERE C_Invoice_ID=?";
        if (p_C_InvoicePaySchedule_ID != 0) {
            sql += " AND C_InvoicePaySchedule_ID=?";
        }
        PreparedStatement pstmt = Compiere.prepareStatement(sql);
        pstmt.setInt(1, p_C_Invoice_ID);
        if (p_C_InvoicePaySchedule_ID != 0) {
            pstmt.setInt(2, p_C_InvoicePaySchedule_ID);
        }
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            C_Currency_ID = rs.getInt(1);
            C_ConversionType_ID = rs.getInt(2);
            GrandTotal = rs.getBigDecimal(3);
            MultiplierAP = rs.getBigDecimal(4);
            MultiplierCM = rs.getBigDecimal(5);
        }
        rs.close();
        pstmt.close();
        //	No Invoice
        if (GrandTotal == null) {
            return null;
        }

        BigDecimal paidAmt = allocatedAmt(p_C_Invoice_ID, C_Currency_ID,
                C_ConversionType_ID, MultiplierAP);
        BigDecimal TotalOpenAmt;
        TotalOpenAmt = GrandTotal.subtract(paidAmt);
        // TotalOpenAmt = GrandTotal.add(paidAmt);

        /**
        GrandTotal	Paid	TotalOpen	Remaining	Due		x
        100			0		100			=0
        1a						=50-0					50		x
        1b									=0-50 =0	50
        2a									=0-50 =0	50		
        2b						=50-0					50 		x
        --
        100			10		100			=10
        1a						=50-10					50		x
        1b									=10-50 =0	50
        2a									=10-50 =0	50		
        2b						=50-0					50 		x
        --
        100			60		100			=60
        1a						=50-60 =0					50		x
        1b									=60-50 		50
        2a									=60-50 =10	50		
        2b						=50-10					50 		x
        --
         **/
        //	Do we have a Payment Schedule ?
        if (p_C_InvoicePaySchedule_ID > 0) //	if not valid = lists invoice amount
        {
            TotalOpenAmt = GrandTotal;
            BigDecimal remainingAmt = paidAmt;
            sql = "SELECT C_InvoicePaySchedule_ID, DueAmt "
                    + "FROM C_InvoicePaySchedule "
                    + "WHERE C_Invoice_ID=?"
                    + " AND IsValid='Y' "
                    + "ORDER BY DueDate";
            pstmt = Compiere.prepareStatement(sql);
            pstmt.setInt(1, p_C_Invoice_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int C_InvoicePaySchedule_ID = rs.getInt(1);
                BigDecimal DueAmt = rs.getBigDecimal(2);
                //
                if (C_InvoicePaySchedule_ID == p_C_InvoicePaySchedule_ID) {
                    if (DueAmt.signum() > 0) //	positive
                    {
                        if (DueAmt.compareTo(remainingAmt) < 0) // paid more 
                        {
                            TotalOpenAmt = Compiere.ZERO;
                        } else {
                            TotalOpenAmt = DueAmt.multiply(MultiplierCM).subtract(remainingAmt);
                        }
                    } else {
                        if (DueAmt.compareTo(remainingAmt) > 0) // paid more 
                        {
                            TotalOpenAmt = Compiere.ZERO;
                        } else {
                            TotalOpenAmt = DueAmt.multiply(MultiplierCM).add(remainingAmt);
                        }
                    }
                } else {
                    if (DueAmt.signum() > 0) //	positive
                    {
                        remainingAmt = remainingAmt.subtract(DueAmt);
                        if (remainingAmt.signum() < 0) {
                            remainingAmt = Compiere.ZERO;
                        }
                    } else {
                        remainingAmt = remainingAmt.add(DueAmt);
                        if (remainingAmt.signum() < 0) {
                            remainingAmt = Compiere.ZERO;
                        }
                    }
                }
            }
            rs.close();
            pstmt.close();
        }	//	Invoice Schedule

        //	Rounding
        TotalOpenAmt = Currency.round(TotalOpenAmt, C_Currency_ID, null);

        //	Ignore Penny if there is a payment

        /*
         *  Zynnia 30/05/2012
         *  Se elimina esta validación para que tome todos los comprobantes con cualquier remanente
         *  JF
         * 
         * 
        if (paidAmt.signum() != 0)
        {
        double open = TotalOpenAmt.doubleValue();
        if (open >= -0.01 && open <= 0.01)
        TotalOpenAmt = Compiere.ZERO;
        }
         * 
         */
        //
        return TotalOpenAmt;
    }	//	open

// Begin vpj-cd e-Evolution 15/03/2006        
    /**
     * 	Open Invoice Amount.
     * 	- incoiceOpen
     *	@param p_C_Invoice_ID invoice
     *	@param p_C_InvoicePaySchedule_ID payment schedule
     *	@return open amount
     *	@throws SQLException
     */
    public static BigDecimal open(int p_C_Invoice_ID, int p_C_InvoicePaySchedule_ID, Timestamp DateAcct)
            throws SQLException {
        //	Invoice info
        int C_Currency_ID = 0;
        int C_ConversionType_ID = 0;
        BigDecimal GrandTotal = null;
        BigDecimal MultiplierAP = null;
        BigDecimal MultiplierCM = null;
        //
        String sql = "SELECT MAX(C_Currency_ID),MAX(C_ConversionType_ID),"
                + " SUM(GrandTotal), MAX(MultiplierAP), MAX(Multiplier) "
                + "FROM	C_Invoice_v " //	corrected for CM / Split Payment
                + "WHERE C_Invoice_ID=?"
                // Begin 
                + " AND DateAcct <= ?";
        // End
        if (p_C_InvoicePaySchedule_ID != 0) {
            sql += " AND C_InvoicePaySchedule_ID=?";
        }
        PreparedStatement pstmt = Compiere.prepareStatement(sql);
        pstmt.setInt(1, p_C_Invoice_ID);
        // Begin
        pstmt.setTimestamp(2, DateAcct);
        // End
        if (p_C_InvoicePaySchedule_ID != 0) // Begin
        //pstmt.setInt(2, p_C_InvoicePaySchedule_ID);
        {
            pstmt.setInt(3, p_C_InvoicePaySchedule_ID);
        }
        // End
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            C_Currency_ID = rs.getInt(1);
            C_ConversionType_ID = rs.getInt(2);
            GrandTotal = rs.getBigDecimal(3);
            MultiplierAP = rs.getBigDecimal(4);
            MultiplierCM = rs.getBigDecimal(5);
        }
        rs.close();
        pstmt.close();
        //	No Invoice
        if (GrandTotal == null) {
            return null;
        }

        BigDecimal paidAmt = allocatedAmt(p_C_Invoice_ID, C_Currency_ID,
                C_ConversionType_ID, MultiplierAP, DateAcct);
        BigDecimal TotalOpenAmt = GrandTotal.subtract(paidAmt);

        /**
        GrandTotal	Paid	TotalOpen	Remaining	Due		x
        100			0		100			=0
        1a						=50-0					50		x
        1b									=0-50 =0	50
        2a									=0-50 =0	50		
        2b						=50-0					50 		x
        --
        100			10		100			=10
        1a						=50-10					50		x
        1b									=10-50 =0	50
        2a									=10-50 =0	50		
        2b						=50-0					50 		x
        --
        100			60		100			=60
        1a						=50-60 =0					50		x
        1b									=60-50 		50
        2a									=60-50 =10	50		
        2b						=50-10					50 		x
        --
         **/
        //	Do we have a Payment Schedule ?
        if (p_C_InvoicePaySchedule_ID > 0) //	if not valid = lists invoice amount
        {
            TotalOpenAmt = GrandTotal;
            BigDecimal remainingAmt = paidAmt;
            sql = "SELECT C_InvoicePaySchedule_ID, DueAmt "
                    + "FROM C_InvoicePaySchedule "
                    + "WHERE C_Invoice_ID=?"
                    + " AND IsValid='Y' "
                    + "ORDER BY DueDate";
            pstmt = Compiere.prepareStatement(sql);
            pstmt.setInt(1, p_C_Invoice_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int C_InvoicePaySchedule_ID = rs.getInt(1);
                BigDecimal DueAmt = rs.getBigDecimal(2);
                //
                if (C_InvoicePaySchedule_ID == p_C_InvoicePaySchedule_ID) {
                    if (DueAmt.signum() > 0) //	positive
                    {
                        if (DueAmt.compareTo(remainingAmt) < 0) // paid more 
                        {
                            TotalOpenAmt = Compiere.ZERO;
                        } else {
                            TotalOpenAmt = DueAmt.multiply(MultiplierCM).subtract(remainingAmt);
                        }
                    } else {
                        if (DueAmt.compareTo(remainingAmt) > 0) // paid more 
                        {
                            TotalOpenAmt = Compiere.ZERO;
                        } else {
                            TotalOpenAmt = DueAmt.multiply(MultiplierCM).add(remainingAmt);
                        }
                    }
                } else {
                    if (DueAmt.signum() > 0) //	positive
                    {
                        remainingAmt = remainingAmt.subtract(DueAmt);
                        if (remainingAmt.signum() < 0) {
                            remainingAmt = Compiere.ZERO;
                        }
                    } else {
                        remainingAmt = remainingAmt.add(DueAmt);
                        if (remainingAmt.signum() < 0) {
                            remainingAmt = Compiere.ZERO;
                        }
                    }
                }
            }
            rs.close();
            pstmt.close();
        }	//	Invoice Schedule

        //	Rounding
        TotalOpenAmt = Currency.round(TotalOpenAmt, C_Currency_ID, null);

        /*
         *  Zynnia 30/05/2012
         *  Se elimina esta validación para que tome todos los comprobantes con cualquier remanente
         *  JF
         * 
         * 
        if (paidAmt.signum() != 0)
        {
        double open = TotalOpenAmt.doubleValue();
        if (open >= -0.01 && open <= 0.01)
        TotalOpenAmt = Compiere.ZERO;
        }
         * 
         */

        return TotalOpenAmt;
    }	//	open
// End e-Evolution 15/03/2006        

    /**
     * 	Get Invoice paid(allocated) amount.
     * 	- invoicePaid
     *	@param p_C_Invoice_ID invoice
     *	@param p_C_Currency_ID currency
     *	@param p_MultiplierAP multiplier
     *	@return paid amount
     *	@throws SQLException
     */
    public static BigDecimal paid(int p_C_Invoice_ID, int p_C_Currency_ID, int p_MultiplierAP)
            throws SQLException {
        //	Invalid Parameters
        if (p_C_Invoice_ID == 0 || p_C_Currency_ID == 0) {
            return null;
        }
        //	Parameters
        BigDecimal MultiplierAP = new BigDecimal((double) p_MultiplierAP);
        if (p_MultiplierAP == 0) {
            MultiplierAP = Compiere.ONE;
        }
        int C_ConversionType_ID = 0;

        //	Calculate Allocated Amount
        BigDecimal paymentAmt = allocatedAmt(p_C_Invoice_ID,
                p_C_Currency_ID, C_ConversionType_ID, MultiplierAP);
        return Currency.round(paymentAmt, p_C_Currency_ID, null);
    }	//	paid

// Begin e-Evolution 15/03/2006
    /**
     * 	Get Invoice paid(allocated) amount.
     * 	- invoicePaid
     *	@param p_C_Invoice_ID invoice
     *	@param p_C_Currency_ID currency
     *	@param p_MultiplierAP multiplier
     *	@return paid amount
     *	@throws SQLException
     */
    public static BigDecimal paid(int p_C_Invoice_ID, int p_C_Currency_ID, int p_MultiplierAP, Timestamp DateAcct)
            throws SQLException {
        //	Invalid Parameters
        if (p_C_Invoice_ID == 0 || p_C_Currency_ID == 0) {
            return null;
        }
        //	Parameters
        BigDecimal MultiplierAP = new BigDecimal((double) p_MultiplierAP);
        if (p_MultiplierAP == 0) {
            MultiplierAP = Compiere.ONE;
        }
        int C_ConversionType_ID = 0;

        //	Calculate Allocated Amount
        BigDecimal paymentAmt = allocatedAmt(p_C_Invoice_ID,
                p_C_Currency_ID, C_ConversionType_ID, MultiplierAP, DateAcct);
        return Currency.round(paymentAmt, p_C_Currency_ID, null);
    }	//	paid
// End e-Evolution 15/03/2006

    /**
     * 	Get Allocated Amt (not directly used)
     *	@param C_Invoice_ID invoice
     *	@param C_Currency_ID currency
     *	@param C_ConversionType_ID conversion type
     *	@param MultiplierAP multiplier
     *	@return allocated amount
     *	@throws SQLException
     */
    public static BigDecimal allocatedAmt(int C_Invoice_ID,
            int C_Currency_ID, int C_ConversionType_ID, BigDecimal MultiplierAP)
            throws SQLException {
        //	Calculate Allocated Amount
        BigDecimal paidAmt = Compiere.ZERO;
        String sql = "SELECT a.AD_Client_ID, a.AD_Org_ID,"
                + " al.Amount, al.DiscountAmt, al.WriteOffAmt,"
                + " a.C_Currency_ID, a.DateTrx "
                + "FROM C_AllocationLine al"
                + " INNER JOIN C_AllocationHdr a ON (al.C_AllocationHdr_ID=a.C_AllocationHdr_ID) "
                + "WHERE al.C_Invoice_ID=?"
                + " AND a.IsActive='Y'";
        PreparedStatement pstmt = Compiere.prepareStatement(sql);

        pstmt.setInt(1, C_Invoice_ID);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int AD_Client_ID = rs.getInt(1);
            int AD_Org_ID = rs.getInt(2);
            BigDecimal Amount = rs.getBigDecimal(3);
            BigDecimal DiscountAmt = rs.getBigDecimal(4);
            BigDecimal WriteOffAmt = rs.getBigDecimal(5);
            int C_CurrencyFrom_ID = rs.getInt(6);
            Timestamp DateTrx = rs.getTimestamp(7);
            //
            BigDecimal invAmt = Amount.add(DiscountAmt).add(WriteOffAmt);
            BigDecimal allocation = Currency.convert(invAmt.multiply(MultiplierAP),
                    C_CurrencyFrom_ID, C_Currency_ID, DateTrx, C_ConversionType_ID,
                    AD_Client_ID, AD_Org_ID);
            if (allocation != null) {
                paidAmt = paidAmt.add(allocation);
            }
        }
        rs.close();
        pstmt.close();
        //
        return paidAmt;
    }	//	getAllocatedAmt

// Begin e-Evolution ogi-cd 15/03/2006        
    /**
     * 	Get Allocated Amt (not directly used)
     *	@param C_Invoice_ID invoice
     *	@param C_Currency_ID currency
     *	@param C_ConversionType_ID conversion type
     *	@param MultiplierAP multiplier
     *	@return allocated amount
     *	@throws SQLException
     */
    public static BigDecimal allocatedAmt(int C_Invoice_ID,
            int C_Currency_ID, int C_ConversionType_ID, BigDecimal MultiplierAP, Timestamp DateAcct)
            throws SQLException {
        //	Calculate Allocated Amount
        BigDecimal paidAmt = Compiere.ZERO;
        String sql = "SELECT a.AD_Client_ID, a.AD_Org_ID,"
                + " al.Amount, al.DiscountAmt, al.WriteOffAmt,"
                + " a.C_Currency_ID, a.DateTrx "
                + "FROM C_AllocationLine al"
                + " INNER JOIN C_AllocationHdr a ON (al.C_AllocationHdr_ID=a.C_AllocationHdr_ID) "
                + "WHERE al.C_Invoice_ID=?"
                + " AND a.IsActive='Y' AND a.DateAcct <= ?";
        PreparedStatement pstmt = Compiere.prepareStatement(sql);
        pstmt.setInt(1, C_Invoice_ID);
        pstmt.setTimestamp(2, DateAcct);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int AD_Client_ID = rs.getInt(1);
            int AD_Org_ID = rs.getInt(2);
            BigDecimal Amount = rs.getBigDecimal(3);
            BigDecimal DiscountAmt = rs.getBigDecimal(4);
            BigDecimal WriteOffAmt = rs.getBigDecimal(5);
            int C_CurrencyFrom_ID = rs.getInt(6);
            Timestamp DateTrx = rs.getTimestamp(7);
            //
            BigDecimal invAmt = Amount.add(DiscountAmt).add(WriteOffAmt);
            BigDecimal allocation = Currency.convert(invAmt.multiply(MultiplierAP),
                    C_CurrencyFrom_ID, C_Currency_ID, DateTrx, C_ConversionType_ID,
                    AD_Client_ID, AD_Org_ID);
            if (allocation != null) {
                paidAmt = paidAmt.add(allocation);
            }
        }
        rs.close();
        pstmt.close();
        //
        return paidAmt;
    }	//	getAllocatedAmt
// End e-Evolution 15/03/2006

    /**
     * 	Get Invoice discount.
     * 	C_Invoice_Discount - invoiceDiscount
     *	@param p_C_Invoice_ID invoice
     *	@param p_PayDate pay date
     *	@param p_C_InvoicePaySchedule_ID pay schedule
     *	@return discount amount or null
     */
    public static BigDecimal discount(int p_C_Invoice_ID,
            Timestamp p_PayDate, int p_C_InvoicePaySchedule_ID)
            throws SQLException {
        //	Parameters
        if (p_C_Invoice_ID == 0) {
            return null;
        }
        Timestamp PayDate = p_PayDate;
        if (PayDate == null) {
            PayDate = new Timestamp(System.currentTimeMillis());
        }
        PayDate = Compiere.trunc(PayDate);

        //	Invoice Info
        boolean IsDiscountLineAmt = false;
        BigDecimal GrandTotal = null;
        BigDecimal TotalLines = null;
        int C_PaymentTerm_ID = 0;
        Timestamp DateInvoiced = null;
        boolean IsPayScheduleValid = false;
        int C_Currency_ID = 0;
        String sql = "SELECT ci.IsDiscountLineAmt, i.GrandTotal, i.TotalLines, "
                + " i.C_PaymentTerm_ID, i.DateInvoiced, i.IsPayScheduleValid, i.C_Currency_ID "
                + "FROM C_Invoice i"
                + " INNER JOIN AD_ClientInfo ci ON (ci.AD_Client_ID=i.AD_Client_ID) "
                + "WHERE i.C_Invoice_ID=?";
        PreparedStatement pstmt = Compiere.prepareStatement(sql);
        pstmt.setInt(1, p_C_Invoice_ID);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            IsDiscountLineAmt = "Y".equals(rs.getString(1));
            GrandTotal = rs.getBigDecimal(2);
            TotalLines = rs.getBigDecimal(3);
            C_PaymentTerm_ID = rs.getInt(4);
            DateInvoiced = rs.getTimestamp(5);
            IsPayScheduleValid = "Y".equals(rs.getString(6));
            C_Currency_ID = rs.getInt(7);
        }
        rs.close();
        pstmt.close();
        //	Not found
        if (GrandTotal == null) {
            return null;
        }

        //	What Amount is the Discount Base?
        BigDecimal amount = GrandTotal;
        if (IsDiscountLineAmt) {
            amount = TotalLines;
        }

        //	Anything to discount?
        if (amount.signum() == 0) {
            return Compiere.ZERO;
        }

        //	Valid Payment Schedule (has discount)
        if (IsPayScheduleValid && p_C_InvoicePaySchedule_ID > 0) {
            BigDecimal discount = Compiere.ZERO;
            sql = "SELECT DiscountAmt "
                    + "FROM C_InvoicePaySchedule "
                    + "WHERE C_InvoicePaySchedule_ID=?"
                    + " AND TRUNC(DiscountDate) <= ?";
            pstmt = Compiere.prepareStatement(sql);
            pstmt.setInt(1, p_C_InvoicePaySchedule_ID);
            pstmt.setTimestamp(2, PayDate);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                discount = rs.getBigDecimal(1);
            }
            rs.close();
            pstmt.close();
            //
            return discount;
        }

        //	return discount amount	
        return PaymentTerm.discount(amount, C_Currency_ID,
                C_PaymentTerm_ID, DateInvoiced, PayDate);
    }

    /**
     * Bision, Nadia 20/06/2008
     * get the first or the last part of the input string
     * @return
     */
    public static String getPart(String input, int inf, int sup) throws SQLException {


        String result = "";

        int index = inf;

        char blanco = ' ';

        if (index == 0) {
            while (index < sup && index < input.length()) {
                result = result + input.charAt(index);
                index++;
            }
        } else {
            while (index < sup && index < input.length() && input.charAt(index) != blanco) {
                index++;
            }
        }


        if (index < input.length()) {

            if (input.charAt(index) == blanco && sup == 76) {
                return result;
            } else {
                while (index < input.length() && input.charAt(index) != blanco && inf == 0) {
                    result = result + input.charAt(index);
                    index++;
                }
                while (index < input.length() && inf == 76 && index < sup) {
                    result = result + input.charAt(index);
                    index++;
                }

            }

        }

        return result;
    }

    public static String num_to_word(int num) {
        return doThings(num);
    }

    private static String doThings(int _counter) {
        //Limite
        if (_counter > 2000000) {
            return "DOS MILLONES";
        }

        switch (_counter) {
            case 0:
                return "CERO";
            case 1:
                return "UN"; //UNO
            case 2:
                return "DOS";
            case 3:
                return "TRES";
            case 4:
                return "CUATRO";
            case 5:
                return "CINCO";
            case 6:
                return "SEIS";
            case 7:
                return "SIETE";
            case 8:
                return "OCHO";
            case 9:
                return "NUEVE";
            case 10:
                return "DIEZ";
            case 11:
                return "ONCE";
            case 12:
                return "DOCE";
            case 13:
                return "TRECE";
            case 14:
                return "CATORCE";
            case 15:
                return "QUINCE";
            case 20:
                return "VEINTE";
            case 30:
                return "TREINTA";
            case 40:
                return "CUARENTA";
            case 50:
                return "CINCUENTA";
            case 60:
                return "SESENTA";
            case 70:
                return "SETENTA";
            case 80:
                return "OCHENTA";
            case 90:
                return "NOVENTA";
            case 100:
                return "CIEN";
            case 200:
                return "DOSCIENTOS";
            case 300:
                return "TRESCIENTOS";
            case 400:
                return "CUATROCIENTOS";
            case 500:
                return "QUINIENTOS";
            case 600:
                return "SEISCIENTOS";
            case 700:
                return "SETECIENTOS";
            case 800:
                return "OCHOCIENTOS";
            case 900:
                return "NOVECIENTOS";

            case 1000:
                return "MIL";

            case 1000000:
                return "UN MILLON";
            case 2000000:
                return "DOS MILLONES";
        }
        if (_counter < 20) {
            //System.out.println(">15");
            return "DIECI" + doThings(_counter - 10);
        }
        if (_counter < 30) {
            //System.out.println(">20");
            return "VEINTI" + doThings(_counter - 20);
        }
        if (_counter < 100) {
            //System.out.println("<100");
            return doThings((int) (_counter / 10) * 10) + " Y " + doThings(_counter % 10);
        }
        if (_counter < 200) {
            //System.out.println("<200");
            return "CIENTO " + doThings(_counter - 100);
        }
        if (_counter < 1000) {
            //System.out.println("<1000");
            return doThings((int) (_counter / 100) * 100) + " " + doThings(_counter % 100);
        }
        if (_counter < 2000) {
            //System.out.println("<2000");
            return "MIL " + doThings(_counter % 1000);
        }
        if (_counter < 1000000) {
            String var = "";
            //System.out.println("<1000000");
            var = doThings((int) (_counter / 1000)) + " MIL";
            if (_counter % 1000 != 0) {
                //System.out.println(var);
                var += " " + doThings(_counter % 1000);
            }
            return var;
        }
        if (_counter < 2000000) {
            return "UN MILLON " + doThings(_counter % 1000000);
        }
        return "";
    }
    //	discount
    /**
     * 	Test
     *	@param args
     **/
    /*public static void main (String[] args)
    {
    
    /*try
    {
    //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
    //Compiere.s_type = Compiere.TYPE_ORACLE;
    Compiere.s_url = "jdbc:oracle:thin:@//dev1:1521/dev1.compiere.org";
    Compiere.s_uid = "compiere";
    Compiere.s_pwd = "compiere";
    //	System.out.println(Invoice.open(1000000, 1000004));
    //	System.out.println(Invoice.open(1000000, 1000005));
    //	System.out.println(Invoice.open(1000001, 1000006));
    //	System.out.println(Invoice.open(1000001, 1000007));
    //System.out.println(Invoice.paid(101, 100, 1));
    //System.out.println(Invoice.paid(1000000, 100, 1));
    //System.out.println(Invoice.paid(1000001, 100, 1));
    //System.out.println(Invoice.paid(1000002, 100, 1));
    
    }
    catch (SQLException e)
    {
    e.printStackTrace();
    }*/
    //String test="esto es un test para dividir una descripcion";
    //System.out.println("Longitud de test:"+ test.length());
    //System.out.println("char nro 75: "+ test.charAt(75));
    //System.out.println("primera parte: "+Invoice.getPart(test, 0,76)); 
    //System.out.println("segunda parte: "+Invoice.getPart(test, 76,151)); 
    //}	
}	//	Invoice
