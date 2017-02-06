package org.compiere.model;

import java.math.*;
import java.lang.String;
import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;
import org.compiere.util.*;

// Zynnia 2013 !!!
public class Retenciones extends CalloutEngine {
    //TODO PASAR A CONSTANTE

    private static int DOCTYPE_IVA = 1000182;
    private static int DOCTYPE_GAN = 1000183;
    private static int DOCTYPE_SUSS = 1000220;
    private static BigDecimal tAlicuotaIB = BigDecimal.ZERO;

    //	INICIO GANANCIAS
    BigDecimal retgan(BigDecimal MSR, BigDecimal TotalFijo, BigDecimal Porcentaje,
            BigDecimal LimiteMinimo, BigDecimal TotalMinimo, BigDecimal RetAnterior) {

        BigDecimal RETGAN = Env.ZERO;

        if (MSR.compareTo(LimiteMinimo) == 0 || MSR.compareTo(LimiteMinimo) == 1) {
            RETGAN = ((MSR.subtract(LimiteMinimo)).multiply((Porcentaje.divide(BigDecimal.valueOf(100))))).add(TotalFijo).subtract(RetAnterior);
        }

        if (RETGAN.compareTo(TotalMinimo) == 0 || RETGAN.compareTo(TotalMinimo) == 1) {
            return RETGAN;
        }

        return Env.ZERO;
    }
    //	FIN GANANCIAS

    //	INICIO IIBB
    float retib(float MSR, float Porcentaje, float LimiteMinimo, float TotalMinimo) {
        float RETIB = 0;

        if (MSR >= LimiteMinimo) {
            RETIB = (MSR * Porcentaje / 100);
        }

        if (RETIB >= TotalMinimo) {
            return RETIB;
        }

        return 0;
    }
    //	FIN IIBB

    //	INICIO SUSS
    BigDecimal retsuss(BigDecimal MSR, BigDecimal TotalFijo, BigDecimal Porcentaje,
            BigDecimal LimiteMinimo, BigDecimal TotalMinimo) {

        BigDecimal RETSUSS = Env.ZERO;

        if (MSR.compareTo(LimiteMinimo) == 0 || MSR.compareTo(LimiteMinimo) == 1) {
            RETSUSS = TotalFijo.add(MSR.multiply(Porcentaje).divide(BigDecimal.valueOf(100)));
        }

        if (RETSUSS.compareTo(TotalMinimo) == 0 || RETSUSS.compareTo(TotalMinimo) == 1) {
            return RETSUSS;
        }

        return Env.ZERO;
    }
    //	FIN SUSS

    //INICIO IVA
    BigDecimal retiva(BigDecimal MSR, BigDecimal TotalFijo, BigDecimal Porcentaje,
            BigDecimal LimiteMinimo, BigDecimal TotalMinimo) {

        BigDecimal RETIVA = Env.ZERO;

        if (MSR.compareTo(LimiteMinimo) == 0 || MSR.compareTo(LimiteMinimo) == 1) {
            RETIVA = TotalFijo.add((MSR.multiply(Porcentaje).divide(BigDecimal.valueOf(100))));
        }

        if (RETIVA.compareTo(TotalMinimo) == 0 || RETIVA.compareTo(TotalMinimo) == 1) {
            return RETIVA;
        }

        return Env.ZERO;
    }
    //FIN IVA

    BigDecimal getMSR(int C_Payment_ID, BigDecimal TotalPago, BigDecimal tIVA) {


        MPayment payment = new MPayment(Env.getCtx(), C_Payment_ID, null);
        MCurrency moneda = MCurrency.get(Env.getCtx(), payment.getC_Currency_ID());



        BigDecimal MSR = Env.ZERO;

        MDocType tipoDoc = null;

        BigDecimal TotalLineas = Env.ZERO;

        try {


            String consulta = "Select C_Invoice_Id, Amount from C_PaymentAllocate where c_payment_id = ?";

            PreparedStatement pstmt = DB.prepareStatement(consulta, null);
            pstmt.setInt(1, C_Payment_ID);
            ResultSet rs = pstmt.executeQuery();


            /*
             * 	Si entra al ciclo al menos 1 vez:
             *          Asignaci�n de la factura por la pesta�a Asignaci�n de la Ventana Pago.
             * 	Si no entra en el ciclo:	
             *          Pago sin asignaci�n a Factura.
             */

            boolean flag = false;

            while (rs.next()) {

                long C_Invoice_ID = rs.getLong(1);


                /*
                 *  Modificado para que en caso de haber percepciones me las reste de TotalPago para 
                 *  que no entre en el cálculo de retenciones.
                 * 
                 *  Pedido Luis Berneti mail 05/04/2012
                 *  Zynnia - José Fantasia
                 * 
                 */

                int Invoice_ID = rs.getInt(1);
                MInvoice factura = new MInvoice(Env.getCtx(), Invoice_ID, null);


                consulta = "select sum(line.priceactual*line.qtyinvoiced), inv.C_Doctype_ID "
                        + "from C_Invoice inv "
                        + "inner join C_Invoiceline line on (inv.C_Invoice_ID = line.C_Invoice_ID) "
                        + "where inv.c_invoice_id = ? "
                        + "and ((line.c_tax_id != 1000055 "
                        + "and line.c_tax_id != 1000056 "
                        + "and line.m_product_id is not null) or "
                        + "(line.c_charge_id is not null "
                        + "and line.c_charge_id not in (select c_charge_id from c_charge where isretganancias = 'N'))) "
                        + "group by inv.C_Doctype_ID ";

                PreparedStatement pstmtInt = DB.prepareStatement(consulta, null);

                pstmtInt.setLong(1, C_Invoice_ID);

                ResultSet rsInt = pstmtInt.executeQuery();

                while (rsInt.next()) {


                    /*
                     *  Necesito saber si el tipo de documento resta o suma.
                     *  Para esto tengo que diferenciar a partir del tipo de documento (por eso pido
                     *  inv.C_Doctype_ID) el tipo de documento base y luego si es:
                     * 
                     *  Nota de Credito CxP resto.
                     *  Factura CxP sumo.
                     * 
                     *  Zynnia - José Fantasia 16/03/2012
                     *  Problemas del cálculo de retenciones
                     * 
                     */

                    tipoDoc = new MDocType(Env.getCtx(), rsInt.getInt(2), null);
                    if (tipoDoc.getDocBaseType().equals(MDocType.DOCBASETYPE_APCreditMemo)) {
                        TotalLineas = rsInt.getBigDecimal(1).negate();
                        if (moneda != null) {
                            TotalLineas = TotalLineas.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                        }
                    } else {
                        TotalLineas = rsInt.getBigDecimal(1);
                        if (moneda != null) {
                            TotalLineas = TotalLineas.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                        }
                    }


                    /*
                     *  Actualizado para ver la conversión
                     * 
                     */




                    MSR = MSR.add(TotalLineas);

                }
                flag = true;

            }

            if (flag) {

                MSR = MSR.multiply(payment.getCotizacion());
                if (moneda != null) {
                    MSR = MSR.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                }

            } else {

                // Sumo las alicuotas de IVA y la suma de las alícuotas de IIBB
                // 05/02/2015
                // José Fantasia Cooperativa GENEOS

                BigDecimal Divisor = Env.ONE.add((tIVA.add(getSumIIBB(payment.getC_BPartner_ID()))).divide(BigDecimal.valueOf(100)));

                if (moneda != null) {
                    Divisor = Divisor.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                    TotalPago = TotalPago.multiply(payment.getCotizacion()).setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                }


                MSR = TotalPago.divide(Divisor, 2, BigDecimal.ROUND_HALF_UP);

                if (moneda != null) {
                    MSR = MSR.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                }

            }





        } catch (Exception exc) {

            System.out.println(exc.toString());
        }

        System.out.println("MSR para el pago " + C_Payment_ID + " es de " + MSR);

        return MSR;

    }

    // Método Backup
    BigDecimal getMSRAnterior(int C_Payment_ID, BigDecimal TotalPago, BigDecimal tIVA) {
        BigDecimal Divisor = Env.ONE.add(tIVA.divide(BigDecimal.valueOf(100)));


        MPayment payment = new MPayment(Env.getCtx(), C_Payment_ID, null);
        MCurrency moneda = MCurrency.get(Env.getCtx(), payment.getC_Currency_ID());

        if (moneda != null) {
            Divisor = Divisor.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
        }

        BigDecimal MSR = Env.ZERO;

        MDocType tipoDoc = null;

        BigDecimal GranTotal = Env.ZERO;
        BigDecimal TotalLineas = Env.ZERO;

        try {


            String consulta = "Select C_Invoice_Id, Amount from C_PaymentAllocate where c_payment_id = ?";

            PreparedStatement pstmt = DB.prepareStatement(consulta, null);
            pstmt.setInt(1, C_Payment_ID);
            ResultSet rs = pstmt.executeQuery();

            BigDecimal Fi = Env.ZERO;
            BigDecimal AcumTotal = Env.ZERO;

            /*
             * 	Si entra al ciclo al menos 1 vez:
             *          Asignaci�n de la factura por la pesta�a Asignaci�n de la Ventana Pago.
             * 	Si no entra en el ciclo:	
             *          Pago sin asignaci�n a Factura.
             */


            while (rs.next()) {

                long C_Invoice_ID = rs.getLong(1);


                /*
                 *  Modificado para que en caso de haber percepciones me las reste de TotalPago para 
                 *  que no entre en el cálculo de retenciones.
                 * 
                 *  Pedido Luis Berneti mail 05/04/2012
                 *  Zynnia - José Fantasia
                 * 
                 */

                int Invoice_ID = rs.getInt(1);
                MInvoice factura = new MInvoice(Env.getCtx(), Invoice_ID, null);


                List percepciones = factura.getPercepciones();
                BigDecimal montoPercepciones = Env.ZERO;
                for (int j = 0; j < percepciones.size(); j++) {
                    montoPercepciones = montoPercepciones.add(((MINVOICEPERCEP) percepciones.get(j)).getAMOUNT());
                }

                // Como las percepciones estan en la moneda original tengo que hacer la conversión

                montoPercepciones = montoPercepciones.multiply(payment.getCotizacion());

                /*
                 *  Modificado para que haga el control de los decimales de presición (a dos decimales)
                 *  Zynnia - José Fantasia
                 *  19/03/2012
                 * 
                 */

                if (moneda != null) {
                    montoPercepciones = montoPercepciones.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                }

                TotalPago = TotalPago.subtract(montoPercepciones);

                if (moneda != null) {
                    TotalPago = TotalPago.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                }

                //consulta = "Select GrandTotal, TotalLines from C_Invoice where C_Invoice_Id = ?";


                // Acá debería de tomarse solo las líneas que tienen IVA, no hay que tener 
                // en cuanta las No gravadas y excentas
                // José Fantasia
                // Zynnia
                            /*
                consulta = "select sum(line.linenetamt), inv.C_Doctype_ID "
                + "from C_Invoice inv "
                + "inner join C_Invoiceline line on (inv.C_Invoice_ID = line.C_Invoice_ID) "
                + "where inv.c_invoice_id = ? "
                + "and line.c_tax_id != 1000055 "
                + "and line.c_tax_id != 1000056 "
                + "group by inv.C_Doctype_ID";
                 *
                 */
                /*
                 *  22/03/2013 Maria Jesus Martin
                 *  Modificacion para que si la factura tiene mal los netos y totales de linea
                 *  calcule bien las retenciones.
                 * 
                 */

                /*
                consulta = "select sum(line.priceactual*line.qtyinvoiced), inv.C_Doctype_ID "
                + "from C_Invoice inv "
                + "inner join C_Invoiceline line on (inv.C_Invoice_ID = line.C_Invoice_ID) "
                + "where inv.c_invoice_id = ? "
                + "and line.c_tax_id != 1000055 "
                + "and line.c_tax_id != 1000056 "
                + "group by inv.C_Doctype_ID";
                 */

                /*
                 *  29/09/2014 Geneos
                 *  Modificacion para que no considere los cargos no sujetos a retención.
                 *                             
                 */

                consulta = "select sum(line.priceactual*line.qtyinvoiced), inv.C_Doctype_ID "
                        + "from C_Invoice inv "
                        + "inner join C_Invoiceline line on (inv.C_Invoice_ID = line.C_Invoice_ID) "
                        + "where inv.c_invoice_id = ? "
                        + "and line.c_tax_id != 1000055 "
                        + "and line.c_tax_id != 1000056 "
                        + "and line.m_product_id is not null "
                        + "group by inv.C_Doctype_ID "
                        + "union "
                        + "select sum(line.priceactual*line.qtyinvoiced), inv.C_Doctype_ID "
                        + "from C_Invoice inv "
                        + "inner join C_Invoiceline line on (inv.C_Invoice_ID = line.C_Invoice_ID) "
                        + "where inv.c_invoice_id = ? "
                        + "and line.c_tax_id != 1000055 "
                        + "and line.c_tax_id != 1000056 "
                        + "and line.c_charge_id is not null "
                        + "and line.c_charge_id not in (select c_charge_id from c_charge where isretganancias = 'N')"
                        + "group by inv.C_Doctype_ID ";


                /*
                 *  05/03/2013 Maria Jesus Martin
                 *  Para el calculo de retenciones, no estaba tomando ningun monto de las lineas excentas. 
                 *  Sin embargo no tiene que tomar el neto, pero si el total, para que de este modo
                 *  se balancee con el total del pago y asi pueda tomar bien el MSR.
                 */
                /*
                String consultatotal = "select sum(line.linetotalamt) , inv.C_Doctype_ID "
                + "from C_Invoice inv "
                + "inner join C_Invoiceline line on (inv.C_Invoice_ID = line.C_Invoice_ID) "
                + "where inv.c_invoice_id = ? "
                + "group by inv.C_Doctype_ID";
                 */
                /*
                 *  22/03/2013 Maria Jesus Martin
                 *  Modificacion para que si la factura tiene mal los netos y totales de linea
                 *  calcule bien las retenciones.
                 * 
                 */
                String consultatotal = "select sum((line.linenetamt)+line.taxamt), inv.C_Doctype_ID "
                        + "from C_Invoice inv "
                        + "inner join C_Invoiceline line on (inv.C_Invoice_ID = line.C_Invoice_ID) "
                        + "where inv.c_invoice_id = ? "
                        + "group by inv.C_Doctype_ID";

                PreparedStatement pstmtInt = DB.prepareStatement(consulta, null);
                PreparedStatement pstmtIntTotal = DB.prepareStatement(consultatotal, null);

                pstmtInt.setLong(1, C_Invoice_ID);
                pstmtInt.setLong(2, C_Invoice_ID);
                pstmtIntTotal.setLong(1, C_Invoice_ID);

                ResultSet rsInt = pstmtInt.executeQuery();
                ResultSet rsIntTotal = pstmtIntTotal.executeQuery();

                if (rsInt.next() && rsIntTotal.next()) {


                    /*
                     *  Necesito saber si el tipo de documento resta o suma.
                     *  Para esto tengo que diferenciar a partir del tipo de documento (por eso pido
                     *  inv.C_Doctype_ID) el tipo de documento base y luego si es:
                     * 
                     *  Nota de Credito CxP resto.
                     *  Factura CxP sumo.
                     * 
                     *  Zynnia - José Fantasia 16/03/2012
                     *  Problemas del cálculo de retenciones
                     * 
                     */

                    tipoDoc = new MDocType(Env.getCtx(), rsInt.getInt(2), null);
                    if (tipoDoc.getDocBaseType().equals(MDocType.DOCBASETYPE_APCreditMemo)) {
                        GranTotal = rsIntTotal.getBigDecimal(1).negate();
                        if (moneda != null) {
                            GranTotal = GranTotal.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                        }
                        TotalLineas = rsInt.getBigDecimal(1).negate();
                        if (moneda != null) {
                            TotalLineas = TotalLineas.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                        }
                    } else {
                        GranTotal = rsIntTotal.getBigDecimal(1);
                        if (moneda != null) {
                            GranTotal = GranTotal.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                        }
                        TotalLineas = rsInt.getBigDecimal(1);
                        if (moneda != null) {
                            TotalLineas = TotalLineas.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                        }
                    }


                    /*
                     *  Actualizado para ver la conversión
                     * 
                     */




                    Fi = Fi.add(TotalLineas);

                    AcumTotal = AcumTotal.add(GranTotal);

                }

            }

            Fi = Fi.multiply(payment.getCotizacion());
            if (moneda != null) {
                Fi = Fi.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            }
            AcumTotal = AcumTotal.multiply(payment.getCotizacion());
            if (moneda != null) {
                AcumTotal = AcumTotal.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            }

            //Si no entra en el ciclo, se reduce a: MSR = TotalPago / Divisor

            MSR = Fi.add((TotalPago.subtract(AcumTotal)).divide(Divisor, moneda.getStdPrecision(), RoundingMode.HALF_UP));


        } catch (Exception exc) {

            System.out.println(exc.toString());
        }

        System.out.println("MSR para el pago " + C_Payment_ID + " es de " + MSR);

        return MSR;

    }

    private static MJURISDICCION[] getJurisdicciones(MPayment payment, String trxName) {

        MBPartner bpartner = new MBPartner(Env.getCtx(), payment.getC_BPartner_ID(), trxName);
        boolean convenio = bpartner.IsConvenioMultilateral();

        ArrayList<MJURISDICCION> jurisdicciones = new ArrayList<MJURISDICCION>();

        if (convenio) {
            try {
                String consulta = "SELECT C_JURISDICCION_ID FROM C_BPARTNER_JURISDICCION WHERE SIMULTANEO='Y' AND C_BPartner_ID = " + bpartner.getC_BPartner_ID();
                PreparedStatement pstmt = DB.prepareStatement(consulta, null);
                ResultSet rs = pstmt.executeQuery();

                int i = 0;
                while (rs.next()) {
                    jurisdicciones.add(new MJURISDICCION(Env.getCtx(), rs.getInt(1), trxName));
                    i++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            jurisdicciones.add(new MJURISDICCION(Env.getCtx(), payment.getC_JURISDICCION_ID(), trxName));
        }

        MJURISDICCION[] jurisResult = new MJURISDICCION[jurisdicciones.size()];
        return jurisdicciones.toArray(jurisResult);
    }

    private static BigDecimal calcularRetencionIB(MPayment payment, MJURISDICCION jurisdiccion, BigDecimal MSR) {

        BigDecimal tTotalminimoIB = BigDecimal.ZERO;
        BigDecimal tLimiteminimoIB = BigDecimal.ZERO;

        String consulta = " SELECT ALIRET, MINAMTRET, LIMMINRET "
                + " FROM  C_BPartner_Jurisdiccion "
                + " WHERE  C_BPartner_ID = ? AND C_JURISDICCION_ID = ?";
        try {
            PreparedStatement pstmt = DB.prepareStatement(consulta, null);
            pstmt.setInt(1, payment.getC_BPartner_ID());
            pstmt.setInt(2, jurisdiccion.getC_Jurisdiccion_ID());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                tAlicuotaIB = rs.getBigDecimal(1);
                tTotalminimoIB = rs.getBigDecimal(2);
                tLimiteminimoIB = rs.getBigDecimal(3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        Retenciones ret = new Retenciones();
        BigDecimal PERIB = new BigDecimal(ret.retib(MSR.floatValue(), tAlicuotaIB.floatValue(), tLimiteminimoIB.floatValue(), tTotalminimoIB.floatValue()));

        if (PERIB.compareTo(BigDecimal.ZERO) != 0) {
            return PERIB;
        }

        return null;
    }

    public static String inicio(int C_Payment_ID, MPayment payment, String trxName) {


        /*
         *      Al inicio deber�a borra todas las retenciones asociadas al pago.
         *
         */

        BigDecimal tPayamt = Env.ZERO;

        BigDecimal tTotalfijoGAN = Env.ZERO;
        BigDecimal tPorcentajeGAN = Env.ZERO;
        BigDecimal tTotalminimoGAN = Env.ZERO;
        BigDecimal tLimiteminimoGAN = Env.ZERO;
        BigDecimal tLimitemaximoGAN = Env.ZERO;

        BigDecimal cRETENCIONGAN = Env.ZERO;
        boolean tExencionGAN = false;
        boolean tExento = false;
        boolean tRespInsc = false;
        boolean tNoContr = false;
        BigDecimal tIVA = Env.ZERO;
        BigDecimal tSumant = Env.ZERO;
        BigDecimal tSumretencionganancias = Env.ZERO;

        BigDecimal cRETENCIONIB = BigDecimal.ZERO;
        boolean tExencionIB = false;


        BigDecimal tTotalfijoSUSS = Env.ZERO;
        BigDecimal tPorcentajeSUSS = Env.ZERO;
        BigDecimal tTotalminimoSUSS = Env.ZERO;
        BigDecimal tLimiteminimoSUSS = Env.ZERO;

        BigDecimal cRETENCIONSUSS = Env.ZERO;
        boolean tExencionSUSS = false;


        BigDecimal tTotalfijoIVA = Env.ZERO;
        BigDecimal tPorcentajeIVA = Env.ZERO;
        BigDecimal tTotalminimoIVA = Env.ZERO;
        BigDecimal tLimiteminimoIVA = Env.ZERO;

        BigDecimal cRETENCIONIVA = Env.ZERO;
        boolean tExencionIVA = false;


        String estadodoc = payment.getDocStatus();

        try {

            /*
             *      Obtencion de datos generales.
             *
             */

            String excluido = "SELECT EXENCIONGANANCIAS,EXENCIONIB,EXENCIONIVA,EXENCIONSUSS "
                    + "FROM C_BPARTNER WHERE C_BPARTNER_ID = ?";

            PreparedStatement pstmt = DB.prepareStatement(excluido, null);
            pstmt.setLong(1, payment.getC_BPartner_ID());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                tExencionGAN = rs.getString(1).equals("Y");
                tExencionIB = rs.getString(2).equals("Y");
                tExencionIVA = rs.getString(3).equals("Y");
                tExencionSUSS = rs.getString(4).equals("Y");
            }

            String consulta = "SELECT CONDICIONGAN_ID, IVA "
                    + "FROM C_BPartner "
                    + "WHERE C_BPartner_ID = ?";

            pstmt = DB.prepareStatement(consulta, null);
            pstmt.setInt(1, payment.getC_BPartner_ID());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1) == MBPartner.CGAN_EXENTO) {
                    tExento = true;
                } else if (rs.getInt(1) == MBPartner.CGAN_RESPINSCRIPTO) {
                    tRespInsc = true;
                } else if (rs.getInt(1) == MBPartner.CGAN_NOCATEGORIZADO) {
                    tNoContr = true;
                }

                tIVA = rs.getBigDecimal(2);
            }

            //MONTO SUJETO A RETENER            

            // CALCULO
            Retenciones ret = new Retenciones();


            /*
             *  Zynnia
             *  Modificación para que tome el valor convertido en caso de pagos en moneda extranjera
             *  Pedido Flavia Cepeda 23/02/2012
             * 
             *  José Fantasia
             */

            /*
             *  Modificado para que haga el control de los decimales de presición (a dos decimales)
             *  Zynnia - José Fantasia
             *  19/03/2012
             * 
             */

            MCurrency moneda = MCurrency.get(Env.getCtx(), payment.getC_Currency_ID());
            tPayamt = payment.getPayAmt().multiply(payment.getCotizacion());

            if (moneda != null) {
                tPayamt = tPayamt.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            }

            BigDecimal MSR = ret.getMSR(C_Payment_ID, tPayamt, tIVA);

            /*
             *  Modificado para que haga el control de los decimales de presición (a dos decimales)
             *  Zynnia - José Fantasia
             *  19/03/2012
             * 
             */

            if (moneda != null) {
                MSR = MSR.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            }


            // ALMACENA EN LA BASE PARA LUEGO REALIZAR LOS COMPROBANTES DE RETENCI�N


            //MCurrency moneda = MCurrency.get (Env.getCtx(), payment.getC_Currency_ID());

            String query = "update c_payment set MSR = " + MSR.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_EVEN).floatValue() + " where c_payment_id = " + payment.getC_Payment_ID();

            DB.executeUpdate(query, trxName);

            rs.close();
            pstmt.close();


            /*
             *      Una vez que tengo el acumulado y el monto del pago aplico el siguiente procedimiento:
             *
             *
             *      1 - En funci�n de los pagos anteriores verifico en que rango cae el pago actual.
             *      2 - Con el pago saco la diferencia con el limite minimo del rango y el calculo va a ser
             *          para la retencion, ret = (((pago - limite minimo) * % del rango) + fijo) - retenciones acumuladas
             *
             *
             */


            // RETENCIONES GANANCIAS

            /*
             * 		Si es Responsable Inscripto, toma la tabla de retenciones RETGAN
             * 		Si No Contribuye, toma la tabla de retenciones RETGANNC
             * 		Si es Monotributista, no se aplica retencion Ganancias   
             */

            if ((!tExencionGAN) && (!tExento) && (tRespInsc || tNoContr)) {
                /*
                 *      Calculo de los pagos anteriores con ese tipo de retencion.
                 */

                consulta = "SELECT pago.C_Payment_Id, pago.PAYAMT, pago.RETENCIONGANANCIAS FROM C_PAYMENT pago "
                        + "INNER JOIN AD_ORGINFO orginfo ON pago.AD_ORG_ID = orginfo.AD_ORG_ID "
                        + "WHERE pago.C_REGIMENGANANCIAS_V_ID = '" + payment.getC_REGIMENGANANCIAS_V_ID() + "' AND orginfo.AGENTE = 'Y' "
                        + "AND pago.C_DocType_ID = 1000138 AND to_char(pago.datetrx, 'yy') = to_char(?, 'yy') "
                        + "AND to_char(pago.datetrx, 'mm') = to_char(?, 'mm') "
                        + "AND pago.C_Payment_ID <> ? AND (pago.DOCSTATUS = 'CO' OR pago.DOCSTATUS = 'CL') AND pago.C_BPARTNER_ID = " + payment.getC_BPartner_ID();

                pstmt = DB.prepareStatement(consulta, null);
                pstmt.setTimestamp(1, payment.getDateTrx());
                pstmt.setTimestamp(2, payment.getDateTrx());
                pstmt.setInt(3, C_Payment_ID);

                rs = pstmt.executeQuery();

                MPayment paymentAnt;
                float totalAnt = 0;

                while (rs.next()) {
                    /*
                     *  Actualizado para ver la conversión
                     * 
                     */

                    paymentAnt = new MPayment(Env.getCtx(), rs.getInt(1), null);

                    //totalAnt = rs.getFloat(2) * paymentAnt.getCotizacion().floatValue();

                    /*
                     *  Modificado para que haga el control de los decimales de presición (a dos decimales)
                     *  Zynnia - José Fantasia
                     *  19/03/2012
                     * 
                     */

                    MCurrency monedaAnt = MCurrency.get(Env.getCtx(), paymentAnt.getC_Currency_ID());
                    BigDecimal valueRoundAnt = rs.getBigDecimal(2).multiply(paymentAnt.getCotizacion());
                    if (moneda != null) {
                        valueRoundAnt = valueRoundAnt.setScale(monedaAnt.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                    }

                    tSumant = tSumant.add(ret.getMSR(rs.getInt(1), valueRoundAnt, tIVA));
                    tSumretencionganancias = tSumretencionganancias.add(rs.getBigDecimal(3));

                }

                rs.close();
                pstmt.close();


                //	INICIO - Buscar el Monto No Sujeto a Retenci�n.
                consulta = "SELECT MNSR "
                        + "FROM C_WITHHOLDING "
                        + "WHERE ISACTIVE='Y' AND REGIMENGANANCIAS = ? AND NAME = 'RETGAN'";

                pstmt = DB.prepareStatement(consulta, null);
                pstmt.setString(1, payment.getC_REGIMENGANANCIAS_V_ID());

                rs = pstmt.executeQuery();

                BigDecimal MNSR = Env.ZERO;

                if (rs.next()) {
                    MNSR = rs.getBigDecimal(1);
                }

                //	FIN - Buscar el Monto No Sujeto a Retenci�n.


                /*
                 *  MSRFINAL - MONTO SUJETO A RETENER FINAL PARA GANANCIAS, TENIENDO EN CUENTA
                 *  		LOS PAGOS ANTERIORES Y EL MONTO NO SUJETO A RETENER.	
                 */

                BigDecimal MSRFINAL = tSumant.add(MSR).subtract(MNSR);

                /*
                 *  Modificado para que haga el control de los decimales de presición (a dos decimales)
                 *  Zynnia - José Fantasia
                 *  19/03/2012
                 * 
                 */


                if (moneda != null) {
                    MSRFINAL = MSRFINAL.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                }

                System.out.println("MSRFINAL para el pago " + C_Payment_ID + " es de " + MSRFINAL);

                // Conseguir Datos para realizar el c�lculo.
                if (tRespInsc) {

                    consulta = "SELECT FIXAMT, PERCENT, MINAMT, THRESHOLDMIN, THRESHOLDMAX "
                            + "FROM C_WITHHOLDING "
                            + "WHERE ISACTIVE='Y' AND REGIMENGANANCIAS = ? AND NAME = 'RETGAN' "
                            + "ORDER BY THRESHOLDMIN";

                } else {

                    consulta = "SELECT FIXAMT, PERCENT, MINAMT, THRESHOLDMIN, THRESHOLDMAX "
                            + "FROM C_WITHHOLDING "
                            + "WHERE ISACTIVE='Y' AND REGIMENGANANCIAS = ? AND NAME = 'RETGANNC' "
                            + "ORDER BY THRESHOLDMIN";

                }

                pstmt = DB.prepareStatement(consulta, null);
                pstmt.setString(1, payment.getC_REGIMENGANANCIAS_V_ID());

                rs = pstmt.executeQuery();

                boolean cont = true;

                while (rs.next() && cont) {

                    tTotalfijoGAN = rs.getBigDecimal(1);
                    tPorcentajeGAN = rs.getBigDecimal(2);
                    tTotalminimoGAN = rs.getBigDecimal(3);
                    tLimiteminimoGAN = rs.getBigDecimal(4);
                    tLimitemaximoGAN = rs.getBigDecimal(5);

                    int compararLimiteMinimo = MSRFINAL.compareTo(tLimiteminimoGAN);
                    int compararLimiteMaximo = MSRFINAL.compareTo(tLimitemaximoGAN);



                    if (((compararLimiteMinimo == 0 || compararLimiteMinimo == 1)
                            && compararLimiteMaximo == -1) || (tLimiteminimoGAN.equals(Env.ZERO)
                            && tLimitemaximoGAN.equals(Env.ZERO))) {

                        cRETENCIONGAN = ret.retgan(MSRFINAL, tTotalfijoGAN, tPorcentajeGAN, tLimiteminimoGAN, tTotalminimoGAN, tSumretencionganancias);

                        /*
                         *  Modificado para que haga el control de los decimales de presición (a dos decimales)
                         *  Zynnia - José Fantasia
                         *  19/03/2012
                         * 
                         */


                        if (moneda != null) {
                            cRETENCIONGAN = cRETENCIONGAN.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                        }

                        System.out.println("RETENCION GAN para el pago " + C_Payment_ID + " es de " + cRETENCIONGAN);

                        MPAYMENTRET paymentRet = getMPAYMENTRET(payment, getDocType('G', 0), trxName);
                        paymentRet.setIMPORTE(cRETENCIONGAN);
                        paymentRet.setTIPO_RET("G");
                        paymentRet.setALICUOTA(tPorcentajeGAN);
                        paymentRet.save(trxName);

                        cont = false;
                    }
                }

            }
            /* 	if (!tMonotributo) -- ELSE  cRETENCIONGAN = 0, pero ya se inicializa
            
            
            
            // RETENCIONES INGRESOS BRUTOS
            
            /*
             *      Obtencion de regimen a aplicar para IIBB.
             *
             *      Se verifica si al socio de negocio se aplica retenci�n.
             *
             */

            if (!tExencionIB) {

                //	Coeficiente IIBB

                MJURISDICCION[] jurisdicciones = getJurisdicciones(payment, trxName);

                for (int i = 0; i < jurisdicciones.length; i++) {
                    tAlicuotaIB = BigDecimal.ZERO;
                    BigDecimal cRETENCIONIBJur = calcularRetencionIB(payment, jurisdicciones[i], MSR);



                    if (cRETENCIONIBJur != null) {
                        /*
                         *  Modificado para que haga el control de los decimales de presición (a dos decimales)
                         *  Zynnia - José Fantasia
                         *  19/03/2012
                         * 
                         */

                        if (moneda != null) {
                            cRETENCIONIBJur = cRETENCIONIBJur.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                        }
                        MPAYMENTRET paymentRet = getMPAYMENTRET(payment, getDocType('B', jurisdicciones[i].getC_Jurisdiccion_ID()), trxName);
                        paymentRet.setIMPORTE(cRETENCIONIBJur);
                        paymentRet.setTIPO_RET("B");
                        paymentRet.setALICUOTA(tAlicuotaIB);
                        paymentRet.save(trxName);

                        cRETENCIONIB = cRETENCIONIB.add(cRETENCIONIBJur);
                    }
                } // END FOR
            }

            //RETENCIONES SUSS			

            /*
             *      Obtencion de regimen a aplicar para SUSS.
             *
             */

            if (!tExencionSUSS) {

                consulta = "SELECT FIXAMT, PERCENT, MINAMT, THRESHOLDMIN "
                        + "FROM C_WITHHOLDING "
                        + "WHERE ISACTIVE='Y' AND NAME = 'RETSUSS' ";

                pstmt = DB.prepareStatement(consulta, null);

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    tTotalfijoSUSS = rs.getBigDecimal(1);

                    /**
                     * SUSS Enlargement -
                     * @author Ezequiel Scott @ Zynnia
                     */
                    // Get the SUSS percentaje for the given BPartner
                    BigDecimal bPartnerPercentageSUSS = getSUSSPercentage(payment.getC_BPartner_ID());
                    if (!bPartnerPercentageSUSS.equals(Env.ZERO)) // If have SUSS, assign it 
                    {
                        tPorcentajeSUSS = bPartnerPercentageSUSS;
                    } else // If doesnt have SUSS, assign the C_Withholding value (default value)
                    {
                        tPorcentajeSUSS = rs.getBigDecimal(2);
                    }

                    // End SUSS Enlargement

                    tTotalminimoSUSS = rs.getBigDecimal(3);
                    tLimiteminimoSUSS = rs.getBigDecimal(4);
                }

                cRETENCIONSUSS = ret.retsuss(MSR, tTotalfijoSUSS, tPorcentajeSUSS, tLimiteminimoSUSS, tTotalminimoSUSS);



                if (!cRETENCIONSUSS.equals(Env.ZERO)) {
                    /*  Modificado para que haga el control de los decimales de presición (a dos decimales)
                     *  Zynnia - José Fantasia
                     *  19/03/2012
                     * 
                     */

                    if (moneda != null) {
                        cRETENCIONSUSS = cRETENCIONSUSS.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                    }
                    MPAYMENTRET paymentRet = getMPAYMENTRET(payment, getDocType('S', 0), trxName);
                    paymentRet.setIMPORTE(cRETENCIONSUSS);
                    paymentRet.setTIPO_RET("S");
                    paymentRet.setALICUOTA(tPorcentajeSUSS);
                    paymentRet.save(trxName);
                }

            }

            rs.close();
            pstmt.close();


            //RETENCIONES IVA			

            /*
             *      Obtencion de regimen a aplicar para IVA.
             */

            if (!tExencionIVA) {
                //Coeficiente IVA
                consulta = "SELECT COEFICIENTEIVA "
                        + "FROM  C_BPartner "
                        + "WHERE  C_BPartner_ID = ?";

                pstmt = DB.prepareStatement(consulta, null);
                pstmt.setInt(1, payment.getC_BPartner_ID());

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    tPorcentajeIVA = rs.getBigDecimal(1);
                    if (tPorcentajeIVA == null) {
                        tPorcentajeIVA = Env.ZERO;
                    }
                }

                consulta = "SELECT FIXAMT, MINAMT, THRESHOLDMIN "
                        + "FROM C_WITHHOLDING "
                        + "WHERE ISACTIVE='Y' AND NAME = 'RETIVA' ";

                pstmt = DB.prepareStatement(consulta, null);

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    tTotalfijoIVA = rs.getBigDecimal(1);
                    tTotalminimoIVA = rs.getBigDecimal(2);
                    tLimiteminimoIVA = rs.getBigDecimal(3);
                }

                cRETENCIONIVA = ret.retiva(MSR, tTotalfijoIVA, tPorcentajeIVA, tLimiteminimoIVA, tTotalminimoIVA);



                if (!cRETENCIONIVA.equals(Env.ZERO)) {
                    /*  Modificado para que haga el control de los decimales de presición (a dos decimales)
                     *  Zynnia - José Fantasia
                     *  19/03/2012
                     * 
                     */


                    if (moneda != null) {
                        cRETENCIONIVA = cRETENCIONIVA.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                    }
                    MPAYMENTRET paymentRet = getMPAYMENTRET(payment, getDocType('I', 0), trxName);
                    paymentRet.setIMPORTE(cRETENCIONIVA);
                    paymentRet.setTIPO_RET("I");
                    paymentRet.setALICUOTA(tPorcentajeIVA);
                    paymentRet.save(trxName);
                }

            }

            rs.close();
            pstmt.close();

            //--------------------------------------			

            //Si es pago genera las retenciones
            //Si no se omite la generacion                   
            /*
            PreparedStatement psval = DB.prepareStatement("select ISRECEIPT from C_PAYMENT where C_PAYMENT_ID=?", null);  
            psval.setInt(1, C_Payment_ID); 
            ResultSet rsval = psval.executeQuery();  
            if (rsval.next())
            {	espago= rsval.getString(1);	}
            rsval.close();  
            psval.close();
             */

            // Vit4B 14/03/2208 Actualizacion del neto en funci�n de el valor del pago menos las retenciones

            /*
             *      Zynnia 24/02/2012
             *      Modificacion para que el neto del pago en moneda original siendo 
             *      que las retenciones son siempre en $
             *      
             *      José Fantasia
             */

            // Cambio a cálculo con BigDecimal


            BigDecimal neto = (tPayamt.divide(payment.getCotizacion(), moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP));
            neto = neto.subtract((cRETENCIONIVA.divide(payment.getCotizacion(), moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP)));
            neto = neto.subtract((cRETENCIONIB.divide(payment.getCotizacion(), moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP)));
            neto = neto.subtract((cRETENCIONGAN.divide(payment.getCotizacion(), moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP)));
            neto = neto.subtract((cRETENCIONSUSS.divide(payment.getCotizacion(), moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP)));





            /*
            float neto = tPayamt / payment.getCotizacion().floatValue();
            neto = neto - (cRETENCIONIVA / payment.getCotizacion().floatValue());
            neto = neto - (cRETENCIONIB.floatValue() / payment.getCotizacion().floatValue());
            neto = neto - (cRETENCIONGAN / payment.getCotizacion().floatValue());
            neto = neto - (cRETENCIONSUSS / payment.getCotizacion().floatValue());
             */


            //BigDecimal netoBD = BigDecimal.valueOf(neto);


            payment.setPAYNET(neto.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP));


            /*
             *      Vit4B 18/12/2007
             *      Modificacion para genarar las retenciones en una tabla separada a la de pagos
             *      c_paymentret
             */

            //Guarda en tabla de retenciones

            //BigDecimal valcRETENCIONIVA = BigDecimal.valueOf(cRETENCIONIVA).setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
            //BigDecimal valcRETENCIONGAN = BigDecimal.valueOf(cRETENCIONGAN).setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
            //BigDecimal valcRETENCIONSUSS = BigDecimal.valueOf(cRETENCIONSUSS).setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
            //BigDecimal valcRETENCIONIB = cRETENCIONIB.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);

            if (estadodoc == "VO") {
                String sqlestado = new String("update c_paymentret set isactive='N' where c_payment_id=" + C_Payment_ID);
                DB.executeUpdate(sqlestado, trxName);
            } else {
                /*      
                 *      Vit4B 11/03/2008
                 *      Agregado para tomar la numeracion desde un tipo de documento
                 *      JF
                 *
                 */
                if (cRETENCIONIVA.compareTo(Env.ZERO) == 1) {

                    String documentNo = MSequence.getDocumentNo(DOCTYPE_IVA, null).toString();
                    String prefix = documentNo.substring(10, documentNo.length());

                    String prefixIni = documentNo.substring(0, 10);

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
                            JOptionPane.showMessageDialog(null, "N�mero de Documento Inv�lido", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }

                    String sqlactualizacion = "select c_paymentret_id from c_paymentret where c_payment_id=" + C_Payment_ID + " and tipo_ret='I'";
                    PreparedStatement pstmtact = DB.prepareStatement(sqlactualizacion, null);
                    ResultSet rsact = pstmtact.executeQuery();
                    if (rsact.next() == true) {
                        String sqliva = new String("update c_paymentret set importe=" + cRETENCIONIVA + " where tipo_ret='I' and c_payment_id=" + C_Payment_ID);
                        DB.executeUpdate(sqliva, trxName);
                    } else {
                        String sqliva = new String("insert into c_paymentret values(case when (select max(c_paymentret_id)+1 from c_paymentret) is null then 1 else (select max(c_paymentret_id)+1 from c_paymentret) end," + payment.getAD_Client_ID() + "," + payment.getAD_Org_ID() + ",'Y',sysdate,1000684,sysdate,1000684," + DOCTYPE_IVA + ",'" + prefixIni + prefix + "','I'," + cRETENCIONIVA + ",(select DATETRX from C_PAYMENT where C_PAYMENT_ID=" + C_Payment_ID + ")," + C_Payment_ID + ",'C',null)");
                        DB.executeUpdate(sqliva, trxName);
                    }
                    rsact.close();
                    pstmtact.close();

                }

                /*      
                 *      Vit4B 11/03/2008
                 *      Agregado para tomar la numeracion desde un tipo de documento
                 *      JF
                 *
                 */
                if (cRETENCIONGAN.compareTo(Env.ZERO) == 1) {
                    String documentNo = MSequence.getDocumentNo(DOCTYPE_GAN, null).toString();
                    String prefix = documentNo.substring(10, documentNo.length());

                    String prefixIni = documentNo.substring(0, 10);

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
                            JOptionPane.showMessageDialog(null, "N�mero de Documento Inv�lido", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }


                    String sqlactualizacion = "select c_paymentret_id from c_paymentret where c_payment_id=" + C_Payment_ID + " and tipo_ret='G'";
                    PreparedStatement pstmtact = DB.prepareStatement(sqlactualizacion, null);
                    ResultSet rsact = pstmtact.executeQuery();
                    if (rsact.next() == true) {
                        /*  for (PO payret : payment.getRetenciones())
                        if (((MPAYMENTRET)payret).getTIPO_RET().equals("G"))
                        {
                        ((MPAYMENTRET)payret).setIMPORTE(valcRETENCIONGAN);
                        payret.save(trxName);
                        }
                         */
                        String sqlib = new String("update c_paymentret set importe=" + cRETENCIONGAN + " where tipo_ret='G' and c_payment_id=" + C_Payment_ID);

                        System.out.println("RETENCION GAN para la cabecera del pago " + C_Payment_ID + " es de " + cRETENCIONGAN);

                        DB.executeUpdate(sqlib, trxName);
                    } else {
                        String sqlib = new String("insert into c_paymentret values(case when (select max(c_paymentret_id)+1 from c_paymentret) is null then 1 else (select max(c_paymentret_id)+1 from c_paymentret) end," + payment.getAD_Client_ID() + "," + payment.getAD_Org_ID() + ",'Y',sysdate,1000684,sysdate,1000684," + DOCTYPE_GAN + ",'" + prefixIni + prefix + "','G'," + cRETENCIONGAN + ",(select DATETRX from C_PAYMENT where C_PAYMENT_ID=" + C_Payment_ID + ")," + C_Payment_ID + ",'C',null)");
                        DB.executeUpdate(sqlib, trxName);

                        /*
                        MPAYMENTRET payret = new MPAYMENTRET(Env.getCtx(),0,trxName);
                        payret.setAD_Client_ID(payment.getAD_Client_ID());
                        payret.setAD_Org_ID(payment.getAD_Org_ID());
                        payret.setIMPORTE(valcRETENCIONGAN);
                        payret.setDateTrx(payment.getDateTrx());
                        payret.setC_Payment_ID(C_Payment_ID);
                        payret.setC_DocType_ID(DOCTYPE_GAN);
                        payret.setDocumentNo(prefixIni + prefix);
                        payret.setTIPO_RET("G");
                        payret.setTIPO_OPERACION("C");
                        payret.setC_REGIM_RETEN_PERCEP_RECIB_ID(0);
                        
                        payret.save();*/
                    }
                    rsact.close();
                    pstmtact.close();
                }

                if (cRETENCIONSUSS.compareTo(Env.ZERO) == 1) {
                    String sqlactualizacion = "select c_paymentret_id from c_paymentret where c_payment_id=" + C_Payment_ID + " and tipo_ret='S'";
                    PreparedStatement pstmtact = DB.prepareStatement(sqlactualizacion, null);
                    ResultSet rsact = pstmtact.executeQuery();
                    if (rsact.next() == true) {
                        String sqlib = new String("update c_paymentret set importe=" + cRETENCIONSUSS + " where tipo_ret='S' and c_payment_id=" + C_Payment_ID);
                        DB.executeUpdate(sqlib, trxName);
                    } else {
                        String documentNo = MSequence.getDocumentNo(DOCTYPE_SUSS, null).toString();
                        String prefix = documentNo.substring(10, documentNo.length());

                        String prefixIni = documentNo.substring(0, 10);

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
                                JOptionPane.showMessageDialog(null, "N�mero de Documento Inv�lido", "Info", JOptionPane.INFORMATION_MESSAGE);
                        }

                        String sqlib = new String("insert into c_paymentret values(case when (select max(c_paymentret_id)+1 from c_paymentret) is null then 1 else (select max(c_paymentret_id)+1 from c_paymentret) end," + payment.getAD_Client_ID() + "," + payment.getAD_Org_ID() + ",'Y',sysdate,1000684,sysdate,1000684," + DOCTYPE_SUSS + ",'" + prefixIni + prefix + "','S'," + cRETENCIONSUSS + ",(select DATETRX from C_PAYMENT where C_PAYMENT_ID=" + C_Payment_ID + ")," + C_Payment_ID + ",'C',null)");
                        DB.executeUpdate(sqlib, trxName);
                    }
                    rsact.close();
                    pstmtact.close();
                }
            }
            /*
            if (tValores!=0)
            {
            //si es cobranza                            
            totalpago=tValores+(cRETENCIONIB+cRETENCIONGAN+cRETENCIONSUSS+cRETENCIONIVA);
            sql = "UPDATE C_PAYMENT SET PAYAMT = " + totalpago +  " WHERE C_PAYMENT_ID = " + C_Payment_ID + " "; 
            DB.executeUpdate (sql, null);
            }
            else
            {
            //si es pago
            totalpago=tPayamt-(cRETENCIONIB+cRETENCIONGAN+cRETENCIONSUSS+cRETENCIONIVA);                            
            sql = "UPDATE C_PAYMENT SET PAYNET  = " + totalpago +  " WHERE C_PAYMENT_ID = " + C_Payment_ID + " "; 
            sql = "UPDATE C_PAYMENT SET PAYAMT  = " + tPayamt +  " WHERE C_PAYMENT_ID = " + C_Payment_ID + " "; 
            DB.executeUpdate (sql, null);
            }
             */


            payment.setRetencionGanancias(cRETENCIONGAN);
            payment.setRetencionIB(cRETENCIONIB);
            payment.setRetencionIVA(cRETENCIONIVA);
            payment.setRetencionSUSS(cRETENCIONSUSS);


            if (moneda != null) {
                tSumant = tSumant.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
                tSumretencionganancias = tSumretencionganancias.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            }

            payment.setSumAnt(tSumant.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP));
            payment.setSumRetencionGanancias(tSumretencionganancias.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP));


            payment.setFlagSave(true);

            payment.save();
        } catch (Exception exc) {
            System.out.println(exc.toString());
        };

        return "";

    }

    /*      
     *      Bision 04/04/2008
     *      Agregado para mostrar los Codigos de Regimenes correspondientes al tipo de retencion
     *      Nadia
     *
     */
    /*public String fillCodigoRegimen (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){
    
    if(value!=null){
    String typeRet=(String)mField.getValue();
    String sqlQuery = "select C_REGIM_RETEN_PERCEP_RECIB_id,codigo_regimen from C_REGIM_RETEN_PERCEP_RECIB where C_REGIM_RETEN_PERCEP_RECIB.tipo_ret='"+typeRet+"'";
    Integer valueReg;
    try {
    PreparedStatement pstmt = DB.prepareStatement(sqlQuery);
    ResultSet rs = pstmt.executeQuery();
    
    ((MLookup)mTab.getField("C_REGIM_RETEN_PERCEP_RECIB_ID").getLookup()).removeAllElements();
    
    KeyNamePair k=null;
    while (rs.next()) { 
    valueReg= new Integer (((Long)rs.getLong(1)).intValue()); 
    k= new KeyNamePair(valueReg.intValue(),rs.getString(2));
    ((MLookup)mTab.getField("C_REGIM_RETEN_PERCEP_RECIB_ID").getLookup()).addElement(k);
    
    }
    
    ((MLookup)mTab.getField("C_REGIM_RETEN_PERCEP_RECIB_ID").getLookup()).refresh(true);
    
    
    }catch (Exception exception) {
    exception.printStackTrace();
    return "error";
    }                
    }
    return "";
    
    
    }
     */
    private static MPAYMENTRET getMPAYMENTRET(MPayment payment, Integer doctype, String trxName) {

        MPAYMENTRET paymentRet = new MPAYMENTRET(Env.getCtx(), 0, trxName);
        paymentRet.setC_Payment_ID(payment.getC_Payment_ID());
        paymentRet.setDateTrx(payment.getDateAcct());
        paymentRet.setTIPO_OPERACION("C");

        if (doctype != null) {
            paymentRet.setC_DocType_ID(doctype.intValue());
            paymentRet.setDocumentNo(getDocumentNo(doctype.intValue()));
        } else {
            paymentRet.setC_DocType_ID(0);
        }

        return paymentRet;
        //	paymentRet.setC_REGIM_RETEN_PERCEP_RECIB_ID();
    }

    /*      
     *      Vit4B 11/03/2008
     *      Agregado para tomar la numeracion desde un tipo de documento
     *      JF
     *
     */
    private static String getDocumentNo(int docType) {
        String documentNo = MSequence.getDocumentNo(docType, null).toString();
        String prefix = documentNo.substring(10, documentNo.length());
        String prefixIni = documentNo.substring(0, 10);

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
            default: {
                JOptionPane.showMessageDialog(null, "N�mero de Documento Inv�lido", "Info", JOptionPane.INFORMATION_MESSAGE);
                return null;
            }
        }
        return prefixIni + prefix;
    }

    private static Integer getDocType(char r, int jurisdiccion) {

        String consulta;
        PreparedStatement pstmt;

        try {

            switch (r) {
                case 'B':
                    consulta = "SELECT C_DOCTYPE_ID FROM C_JURISDICCION WHERE C_JURISDICCION_ID=?";
                    pstmt = DB.prepareStatement(consulta, null);
                    pstmt.setInt(1, jurisdiccion);
                    break;
                case 'G':
                    consulta = "SELECT C_DOCTYPE_ID FROM C_DOCTYPE WHERE DOCBASETYPE='ING'";
                    pstmt = DB.prepareStatement(consulta, null);
                    break;
                case 'S':
                    consulta = "SELECT C_DOCTYPE_ID FROM C_DOCTYPE WHERE DOCBASETYPE='INS'";
                    pstmt = DB.prepareStatement(consulta, null);
                    break;
                case 'I':
                    consulta = "SELECT C_DOCTYPE_ID FROM C_DOCTYPE WHERE DOCBASETYPE='INI'";
                    pstmt = DB.prepareStatement(consulta, null);
                    break;
                default:
                    return null;
            };

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }
    
        public static Integer getJurisdiccion(int C_DocType_ID) {

        String consulta;
        PreparedStatement pstmt;

        try {

                    consulta = "SELECT  C_JURISDICCION_ID FROM C_JURISDICCION WHERE C_DOCTYPE_ID=?";
                    pstmt = DB.prepareStatement(consulta, null);
                    pstmt.setInt(1, C_DocType_ID);
                    

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    //TODO PASAR A CONSTANTE
    public String fillCodigoRegimen(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {

        if (value != null) {
            String typeRet = (String) mField.getValue();
            MField field = mTab.getField("REGIMENGANANCIAS");
            String sqlQuery = "";
            if (typeRet.equals("Ingresos Brutos")) {
                sqlQuery = "update AD_Column set AD_Reference_Value_ID = 1000074 Where AD_Column_ID=?";
            } else if (typeRet.equals("Ganancias")) {
                sqlQuery = "update AD_Column set AD_Reference_Value_ID = 1000063 Where AD_Column_ID=?";
            } else if (typeRet.equals("SUSS")) {
                sqlQuery = "update AD_Column set AD_Reference_Value_ID = 1000077 Where AD_Column_ID=?";
            } else if (typeRet.equals("IVA")) {
                sqlQuery = "update AD_Column set AD_Reference_Value_ID = 1000074 Where AD_Column_ID=?";
            }
            try {
                PreparedStatement pstmt = DB.prepareStatement(sqlQuery, null);
                pstmt.setLong(1, field.getAD_Column_ID());
                pstmt.executeQuery();
            } catch (Exception exception) {
                exception.printStackTrace();
                return "error";
            }

            ((MLookup) field.getLookup()).refresh(true);
        }
        return "";
    }

    /**
     * Get the SUSS Percentaje for given C_BPartner_ID, if it have.
     * 
     * @param C_BPartner_ID     the C_BPartner_ID that correspond to bussines partner into data base
     * @return  SUSS percentaje in float format 
     * 
     * @author Ezequiel Scott @ Zynnia
     */
    private static BigDecimal getSUSSPercentage(int C_BPartner_ID) throws SQLException {
        String query = "SELECT SUSS_PERCENTAGE FROM C_BPartner "
                + "WHERE EXENCIONSUSS='N' AND isActive = 'Y' AND C_BPartner_ID = ?";

        CPreparedStatement pstmt = DB.prepareStatement(query, null);
        pstmt.setInt(1, C_BPartner_ID);
        ResultSet rs = pstmt.executeQuery();

        BigDecimal val = Env.ZERO;

        if (rs.next()) {
            val = rs.getBigDecimal(1);
        }


        rs.close();
        pstmt.close();

        return val;
    }

    /**
     * Obtener la suma de las alícuotas de IIBB para el cálculo del pago adelantado a un C_BPartner_ID.
     * 
     * @param C_BPartner_ID     the C_BPartner_ID that correspond to bussines partner into data base
     * @return  suma de alicuotas 
     * 
     * @date 05/02/2015
     * @author José Fantasia - Cooperativa GENEOS
     */
    private static BigDecimal getSumIIBB(int C_BPartner_ID) throws SQLException {

        String query = "SELECT sum(ALIPER) FROM C_BPARTNER_JURISDICCION "
                + "WHERE C_BPartner_ID = ?";

        CPreparedStatement pstmt = DB.prepareStatement(query, null);
        pstmt.setInt(1, C_BPartner_ID);
        ResultSet rs = pstmt.executeQuery();

        BigDecimal sum = Env.ZERO;

        if (rs.next()) {
            sum = rs.getBigDecimal(1);
        }

        rs.close();
        pstmt.close();

        return sum;


    }
}
