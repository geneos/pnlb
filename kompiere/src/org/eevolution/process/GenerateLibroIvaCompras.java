/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.process.*;
import java.math.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.*;

import org.compiere.util.*;
import org.eevolution.tools.DateTimeUtil;
import org.eevolution.tools.UtilProcess;

/**
 *  Esta clase inserta tuplas en la tabla temporal T_LIBRO_IVA_VENTA luego de un previo filtrado por fecha
 *  y calculos posteriores.
 *
 *	@author BISion
 *	@version 1.0
 */
public class GenerateLibroIvaCompras extends SvrProcess {

    //Máximo que permite la base de datos para los campos String
    private static int N = 30;
    private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    private Long num_hoja;
    SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
    private Long AD_CLIENT_ID;
    private Long AD_ORG_ID;
    private int SCALE = 2;
    private int ID_IVANG = 5000000;

    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (name.equals("NRO_HOJA")) {
                num_hoja = ((BigDecimal) para[i].getParameter()).longValue();
                Env.getCtx().put("typePrint", "LIBRO");
                Env.getCtx().put("startPage", num_hoja);
            } else {
                fromDate = (Timestamp) para[i].getParameter();
                toDate = (Timestamp) para[i].getParameter_To();
            }
            p_PInstance_ID = getAD_PInstance_ID();
        }
        AD_CLIENT_ID = new Long(getAD_Client_ID());
        AD_ORG_ID = new Long(0);
    }

    protected String doIt() {
        try {
            loadTransporte();
            loadLibro();
            setearTemp();

            loadDebitoFiscal();
            loadCreditoFiscal();
            loadRetenciones();
            loadPercepciones();

            DB.commit(true, get_TrxName());
            UtilProcess.initViewer("Libro IVA Compras", p_PInstance_ID, getProcessInfo());
        } catch (SQLException ex) {
            Logger.getLogger(GenerateLibroIvaCompras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private Timestamp getFechaInicial() {
        Date date = new Date(fromDate.getYear(), fromDate.getMonth(), 1);
        Timestamp ts = new Timestamp(date.getTime());
        return ts;
    }
    BigDecimal transGravado = BigDecimal.ZERO;
    BigDecimal transIVACredito = BigDecimal.ZERO;
    BigDecimal transOtros = BigDecimal.ZERO;
    BigDecimal transExento = BigDecimal.ZERO;
    BigDecimal transTotal = BigDecimal.ZERO;

    protected void loadTransporte() {
        // Verificar que no se solicite desde el princiopio de período

        if (!TimeUtil.isSameDay(fromDate, getFechaInicial())) {
            BigDecimal grabado = BigDecimal.ZERO;
            BigDecimal exento = BigDecimal.ZERO;
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal ivaCredito = BigDecimal.ZERO;
            BigDecimal otros = BigDecimal.ZERO;

            // Agregago: Los AJC y AJD son ignorados a pedido de Luis Bernetti
            // 23/10/2011
            // Alejandro Scott
            String sqlQuery = "SELECT AD_CLIENT_ID, AD_ORG_ID, ISACTIVE, 0, DATETRX, C_DOCTYPE_ID, COMPROBANTE_ID, 0, "
                    + "COMPROBANTE, NRO_COMPROBANTE, RAZON_SOCIAL, CUIT, REGRETEN, REGESPECIAL, NETLINE, "
                    + "DOCSTATUS "
                    + "FROM RV_LIBRO_COMPRA "
                    + "WHERE ( DATEACCT BETWEEN ? AND ? ) AND C_DOCTYPE_ID NOT IN (1000245, 5000012, 1000246, 5000011) "
   //                 + "      AND C_INVOICE_ID NOT IN (5006737, 5007361, 5009456, 5008721) "
                    + "ORDER BY DATETRX asc";

            // Realice la consulta y guardo los resultados en la tabla
            try {
                PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
                String sqlQuery3 = "select sum(AMOUNT) FROM C_INVOICEPERCEP WHERE C_INVOICE_ID = ?";

                //	Iva Credito Fiscal
                String sqlQuery4 = "select taxamt,c_tax_id,TAXBASEAMT from c_invoicetax where c_invoice_id= ?";

                pstmt.setTimestamp(1, getFechaInicial());
                pstmt.setTimestamp(2, TimeUtil.addDays(fromDate, -1));
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    AD_CLIENT_ID = rs.getLong(1);
                    AD_ORG_ID = rs.getLong(2);
                    BigDecimal impuesto_iva = BigDecimal.ZERO;
                    BigDecimal impuesto_exento = BigDecimal.ZERO;
                    BigDecimal NETO = rs.getBigDecimal(15);
                    BigDecimal PERCEPCIONIB = rs.getBigDecimal(14);
                    BigDecimal iva_parcial = BigDecimal.ZERO;
                    String STATUS = rs.getString(16);
                    PERCEPCIONIB = rs.getBigDecimal(14);
                    if (PERCEPCIONIB.equals(BigDecimal.ZERO)) {
                        PreparedStatement pstmt3 = DB.prepareStatement(sqlQuery3, null);
                        pstmt3.setLong(1, rs.getLong(7));
                        ResultSet rs3 = pstmt3.executeQuery();
                        if (rs3.next() && rs3.getBigDecimal(1) != null) {
                            PERCEPCIONIB = rs3.getBigDecimal(1);
                        }
                        rs3.close();
                        closeStatement(pstmt3);
                    }

                    //	Iva Credito Fiscal
                    PreparedStatement pstmt4 = DB.prepareStatement(sqlQuery4, null);
                    pstmt4.setLong(1, rs.getLong(7));
                    ResultSet rs3 = pstmt4.executeQuery();
                    MDocType doctype = MDocType.get(Env.getCtx(), rs.getInt(6));
                    MInvoice invoice =MInvoice.get(Env.getCtx(), rs.getInt(7));
                    if (doctype.getDocBaseType().equals(MDocType.DOCBASETYPE_APCreditMemo)) {
                        PERCEPCIONIB = PERCEPCIONIB.multiply(new BigDecimal(-1));
                    }
                    while (rs3.next()) {
                        if (doctype.getDocBaseType().equals(MDocType.DOCBASETYPE_APInvoice)) {
                            if (rs3.getLong(2) == 5000001 || rs3.getLong(2) == 1000052 || rs3.getLong(2) == 1000053 || rs3.getLong(2) == 1000054) {
                                iva_parcial = iva_parcial.add(rs3.getBigDecimal(1).multiply(invoice.getCotizacion()));
                                impuesto_iva = impuesto_iva.add(rs3.getBigDecimal(3).multiply(invoice.getCotizacion()));
                            } else if (rs3.getLong(2) == 1000055 || rs3.getLong(2) == 1000057 || rs3.getLong(2) == 1000056) {
                                impuesto_exento = impuesto_exento.add(rs3.getBigDecimal(3).multiply(invoice.getCotizacion()));
                            } else if (rs3.getLong(2) == ID_IVANG) { 
                                impuesto_iva = impuesto_iva.add(rs3.getBigDecimal(3).multiply(invoice.getCotizacion()));
                            }
                        } else if (doctype.getDocBaseType().equals(MDocType.DOCBASETYPE_APCreditMemo)) {
                            if (rs3.getLong(2) == 5000001 || rs3.getLong(2) == 1000052 || rs3.getLong(2) == 1000053 || rs3.getLong(2) == 1000054) {
                                iva_parcial = iva_parcial.subtract(rs3.getBigDecimal(1).multiply(invoice.getCotizacion()));
                                impuesto_iva = impuesto_iva.subtract(rs3.getBigDecimal(3).multiply(invoice.getCotizacion()));
                            } else if (rs3.getLong(2) == 1000055 || rs3.getLong(2) == 1000057 || rs3.getLong(2) == 1000056) {
                                impuesto_exento = impuesto_exento.subtract(rs3.getBigDecimal(3).multiply(invoice.getCotizacion()));
                        } else if (rs3.getLong(2) == ID_IVANG) { 
                            impuesto_iva = impuesto_iva.add(rs3.getBigDecimal(3).multiply(invoice.getCotizacion()));
                        }
                        }
                    }
                    rs3.close();
                    closeStatement(pstmt4);

                    if (!STATUS.equals("VO")) {
                        if (rs.getString(13) == null) {
                            NETO = PERCEPCIONIB.add(iva_parcial).add(impuesto_exento).add(impuesto_iva);
                        }
                    } else {
                        impuesto_iva = BigDecimal.ZERO;
                        impuesto_exento = BigDecimal.ZERO;
                        iva_parcial = BigDecimal.ZERO;
                        PERCEPCIONIB = BigDecimal.ZERO;
                        NETO = BigDecimal.ZERO;
                    }

                    grabado = grabado.add(impuesto_iva);
                    exento = exento.add(impuesto_exento);
                    ivaCredito = ivaCredito.add(iva_parcial);
                    otros = otros.add(PERCEPCIONIB);
                    total = total.add(NETO);
                }
                rs.close();
                closeStatement(pstmt);

                total = grabado.add(exento.add(ivaCredito.add(otros)));

                transExento = exento.setScale(2, BigDecimal.ROUND_HALF_UP);
                transOtros = otros.setScale(2, BigDecimal.ROUND_HALF_UP);
                transGravado = grabado.setScale(2, BigDecimal.ROUND_HALF_UP);
                transIVACredito = ivaCredito.setScale(2, BigDecimal.ROUND_HALF_UP);
                transTotal = total.setScale(2, BigDecimal.ROUND_HALF_UP);
            } catch (SQLException ex) {
                Logger.getLogger(GenerateLibroIvaCompras.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
            }
        }
    }

    private void closeStatement(PreparedStatement stat) throws SQLException {
        if (stat != null) {
            stat.close();
            stat = null;
        }
    }

    protected void loadLibro() {

        String sqlQuery3 = "";
        String sqlQuery4 = "";
        PreparedStatement pstmt = null, pstmt3 = null, pstmt4 = null;
        PreparedStatement pstmtInsert = null;
        String sqlInsert;
        BigDecimal NETO = BigDecimal.ZERO;
        BigDecimal grabado = BigDecimal.ZERO;
        BigDecimal exento = BigDecimal.ZERO;
        BigDecimal ivaCredito = BigDecimal.ZERO;
        BigDecimal otros = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal iva_parcial = BigDecimal.ZERO;
        BigDecimal PERCEPCIONIB = BigDecimal.ZERO;
        BigDecimal impuesto_iva = BigDecimal.ZERO;
        BigDecimal impuesto_exento = BigDecimal.ZERO;
        //auxiliares
        Date DATETRX = null;
        String RAZON_SOCIAL = "", STATUS = "";

        //List queries = new ArrayList();

        log.info("Comienzo del proceso de generación de LIBRO IVA DE COMPRAS");
        log.info("borrado de la tabla temporal T_LIBRO_IVA_COMPRA");
        String sqlRemove = "delete from T_LIBRO_IVA_COMPRA";
        DB.executeUpdate(sqlRemove, null);
        boolean ok = false;

        if (transTotal.compareTo(BigDecimal.ZERO) != 0) {
            Timestamp transp = new Timestamp(fromDate.getTime());
            transp = TimeUtil.addDays(transp, -1);
//            String fecha = "";
//            if (transp.getDate() < 10) {
//                fecha = "0" + transp.getDate();
//            } else String{
//                fecha += transp.getDate();
//            }
//            if (transp.getMonth() + 1 < 10) {
//                fecha += "/0" + (transp.getMonth() + 1);
//            } else {
//                fecha += "/" + (transp.getMonth() + 1);
//            }
//            fecha += "/" + (transp.getYear() + 1900);

            //  "AD_CLIENT_ID", "AD_ORG_ID", "ISACTIVE", "AD_PINSTANCE_ID", "TEMPDATE", "TIPO_COMPROBANTE",
            //  "NUMERO_COMPROBANTE", "RAZON_SOCIAL", "CUIT", "NETO_GRAVADO", "EXENTO", "IVA_CREDITO_FISCAL",
            //  "REGRETEN", "OTROS", "TOTAL", "FECHA"
            sqlInsert = "INSERT into T_LIBRO_IVA_COMPRA  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            try {
                pstmtInsert.setLong(1, AD_CLIENT_ID);
                pstmtInsert.setLong(2, AD_ORG_ID);
                pstmtInsert.setString(3, "Y");
                pstmtInsert.setLong(4, p_PInstance_ID);
                pstmtInsert.setDate(5, new Date(fromDate.getTime() + 1000));
                pstmtInsert.setString(6, null);
                pstmtInsert.setString(7, null);
                pstmtInsert.setString(8, null);
                pstmtInsert.setString(9, "TRANSPORTE");

                pstmtInsert.setBigDecimal(10, transGravado.setScale(SCALE, BigDecimal.ROUND_HALF_UP));
                pstmtInsert.setBigDecimal(11, transExento.setScale(SCALE, BigDecimal.ROUND_HALF_UP));
                pstmtInsert.setBigDecimal(12, transIVACredito.setScale(SCALE, BigDecimal.ROUND_HALF_UP));
                pstmtInsert.setString(13, null);
                pstmtInsert.setBigDecimal(14, transOtros.setScale(SCALE, BigDecimal.ROUND_HALF_UP));
                pstmtInsert.setBigDecimal(15, transTotal.setScale(SCALE, BigDecimal.ROUND_HALF_UP));
                pstmtInsert.setTimestamp(16, transp);

                pstmtInsert.executeQuery();
                DB.commit(true, null);
                pstmtInsert.close();
                pstmtInsert = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        log.info("Consulta de la vista RV_LIBRO_IVA_COMPRA, se filtra por el rango indicado");
        // Agregago: Los AJC y AJD son ignorados a pedido de Luis Bernetti
        // 23/10/2011
        // Alejandro Scott
        String sqlQuery = "SELECT AD_CLIENT_ID, AD_ORG_ID, ISACTIVE, 0, DATETRX, C_DOCTYPE_ID, COMPROBANTE_ID, 0, "
                + "COMPROBANTE, NRO_COMPROBANTE, RAZON_SOCIAL, CUIT, REGRETEN, REGESPECIAL, NETLINE, "
                + "DOCSTATUS, DATEACCT "
                + "FROM RV_LIBRO_COMPRA "
                + "WHERE ( DATEACCT BETWEEN ? AND ? ) AND C_DOCTYPE_ID NOT IN (1000245, 5000012, 1000246, 5000011) "
//                + "      AND C_INVOICE_ID NOT IN (5006737, 5007361, 5009456, 5008721) "
                + "ORDER BY DATETRX asc";

        // Realice la consulta y guardo los resultados en la tabla
        try {
            pstmt = DB.prepareStatement(sqlQuery, get_TrxName());

            //sqlQuery3 = "select sum(AMOUNT) FROM C_INVOICEPERCEP WHERE C_INVOICE_ID = ?";
            // Correccion: Conversion del total de la percepcion a Pesos Argentinos
            // 30/11/2011
            // Alejandro Scott
            sqlQuery3 = "SELECT sum(currencyConvert(AMOUNT, C_Invoice.C_CURRENCY_ID, 118, C_Invoice.DATEACCT, C_Invoice.C_CONVERSIONTYPE_ID, C_InvoicePercep.AD_CLIENT_ID, C_InvoicePercep.AD_ORG_ID)) "
                    +   "FROM C_InvoicePercep "
                    +   "JOIN C_Invoice ON (C_InvoicePercep.C_INVOICE_ID = C_Invoice.C_INVOICE_ID) "
                    +   "WHERE C_InvoicePercep.C_INVOICE_ID = ?";

            sqlQuery4 = "select taxamt,c_tax_id,TAXBASEAMT from c_invoicetax where c_invoice_id=?";

             //  "AD_CLIENT_ID", "AD_ORG_ID", "ISACTIVE", "AD_PINSTANCE_ID", "TEMPDATE", "TIPO_COMPROBANTE",
            //  "NUMERO_COMPROBANTE", "RAZON_SOCIAL", "CUIT", "NETO_GRAVADO", "EXENTO", "IVA_CREDITO_FISCAL",
            //  "REGRETEN", "OTROS", "TOTAL", "FECHA"
            sqlInsert = "INSERT into T_LIBRO_IVA_COMPRA values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ok = true;
                impuesto_iva = BigDecimal.ZERO;
                impuesto_exento = BigDecimal.ZERO;
                // 	Para obtener el cliente y la organizacion
                AD_CLIENT_ID = rs.getLong(1);
                AD_ORG_ID = rs.getLong(2);

                RAZON_SOCIAL = rs.getString(11);
                NETO = rs.getBigDecimal(15);
                STATUS = rs.getString(16);
                DATETRX = rs.getDate(5);
                PERCEPCIONIB = rs.getBigDecimal(14);
                if (PERCEPCIONIB.equals(BigDecimal.ZERO)) {
                    pstmt3 = DB.prepareStatement(sqlQuery3, null);
                    pstmt3.setLong(1, rs.getLong(7));
                    ResultSet rs3 = pstmt3.executeQuery();
                    if (rs3.next() && rs3.getBigDecimal(1) != null) {
                        PERCEPCIONIB = rs3.getBigDecimal(1);
                    }
                    rs3.close();
                    closeStatement(pstmt3);
                }

                //	Debido a que no anda el dateformat
                String fecha = "";
                if (DATETRX.getDate() < 10) {
                    fecha = "0" + DATETRX.getDate();
                } else {
                    fecha += DATETRX.getDate();
                }
                if (DATETRX.getMonth() + 1 < 10) {
                    fecha += "/0" + (DATETRX.getMonth() + 1);
                } else {
                    fecha += "/" + (DATETRX.getMonth() + 1);
                }
                fecha += "/" + (DATETRX.getYear() + 1900);

                //	Iva Credito Fiscal
                iva_parcial = BigDecimal.ZERO;

                Calendar cal = new GregorianCalendar();
                cal.setTime(new Date(toDate.getTime()));
                cal.getActualMaximum(Calendar.DAY_OF_MONTH);

                pstmt4 = DB.prepareStatement(sqlQuery4, null);
                pstmt4.setLong(1, rs.getLong(7));
                ResultSet rs3 = pstmt4.executeQuery();

                MDocType doctype = MDocType.get(Env.getCtx(), rs.getInt(6));
                MInvoice invoice = MInvoice.get(Env.getCtx(), rs.getInt(7));
                if (doctype.getDocBaseType().equals(MDocType.DOCBASETYPE_APCreditMemo)) {
                    PERCEPCIONIB = PERCEPCIONIB.multiply(new BigDecimal(-1));
                }
                
                /*
                 * 
                 *      Zynnia 13/02/2012 - José Fantasia
                 *      Hoffman
                 *      IVA No Computable no tiene que sumar a ningún total ni a los parciales
                 *      de crédito o débito fiscal.
                 * 
                 * 
                 */
                
                while (rs3.next()) {
                    if (doctype.getDocBaseType().equals(MDocType.DOCBASETYPE_APInvoice)) {
                        //if (rs3.getLong(2) == 1000052 || rs3.getLong(2) == 1000053 || rs3.getLong(2) == 1000054 || rs3.getLong(2) == ID_IVANG) {
                        if (rs3.getLong(2) == 5000001 || rs3.getLong(2) == 1000052 || rs3.getLong(2) == 1000053 || rs3.getLong(2) == 1000054) {
                            iva_parcial = iva_parcial.add(rs3.getBigDecimal(1).multiply(invoice.getCotizacion()));
                            impuesto_iva = impuesto_iva.add(rs3.getBigDecimal(3).multiply(invoice.getCotizacion()));
                        } else if (rs3.getLong(2) == 1000055 || rs3.getLong(2) == 1000057 || rs3.getLong(2) == 1000056) {
                            impuesto_exento = impuesto_exento.add(rs3.getBigDecimal(3).multiply(invoice.getCotizacion()));
                        } else if (rs3.getLong(2) == ID_IVANG) { 
                            impuesto_iva = impuesto_iva.add(rs3.getBigDecimal(3).multiply(invoice.getCotizacion()));
                        }
                    } else if (doctype.getDocBaseType().equals(MDocType.DOCBASETYPE_APCreditMemo)) {
                        //if (rs3.getLong(2) == 1000052 || rs3.getLong(2) == 1000053 || rs3.getLong(2) == 1000054 || rs3.getLong(2) == ID_IVANG) {
                        if (rs3.getLong(2) == 5000001 || rs3.getLong(2) == 1000052 || rs3.getLong(2) == 1000053 || rs3.getLong(2) == 1000054) {
                            iva_parcial = iva_parcial.subtract(rs3.getBigDecimal(1).multiply(invoice.getCotizacion()));
                            impuesto_iva = impuesto_iva.subtract(rs3.getBigDecimal(3).multiply(invoice.getCotizacion()));
                        } else if (rs3.getLong(2) == 1000055 || rs3.getLong(2) == 1000057 || rs3.getLong(2) == 1000056) {
                            impuesto_exento = impuesto_exento.subtract(rs3.getBigDecimal(3).multiply(invoice.getCotizacion()));
                        } else if (rs3.getLong(2) == ID_IVANG) { 
                            impuesto_iva = impuesto_iva.subtract(rs3.getBigDecimal(3).multiply(invoice.getCotizacion()));
                        }
                    }
                }
                rs3.close();
                closeStatement(pstmt4);

                pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                pstmtInsert.setLong(1, AD_CLIENT_ID);
                pstmtInsert.setLong(2, AD_ORG_ID);
                pstmtInsert.setString(3, rs.getString(3));
                pstmtInsert.setLong(4, p_PInstance_ID);
                pstmtInsert.setDate(5, new Date(fromDate.getTime() + 1000));
                pstmtInsert.setString(6, rs.getString(9));
                pstmtInsert.setString(7, rs.getString(10));
                if (!STATUS.equals("VO")) {
                    if (RAZON_SOCIAL.length() > N) {
                        pstmtInsert.setString(8, RAZON_SOCIAL.substring(0, N - 1));
                    } else {
                        pstmtInsert.setString(8, RAZON_SOCIAL);
                    }

                    pstmtInsert.setString(9, rs.getString(12));
                    pstmtInsert.setBigDecimal(10, impuesto_iva.setScale(SCALE, BigDecimal.ROUND_HALF_UP));
                    pstmtInsert.setBigDecimal(11, impuesto_exento.setScale(SCALE, BigDecimal.ROUND_HALF_UP));
                    pstmtInsert.setBigDecimal(12, iva_parcial.setScale(SCALE, BigDecimal.ROUND_HALF_UP));

                    pstmtInsert.setString(13, rs.getString(13));

                    pstmtInsert.setBigDecimal(14, PERCEPCIONIB.setScale(SCALE, BigDecimal.ROUND_HALF_UP));
                    if (rs.getString(13) == null) {
                        NETO = PERCEPCIONIB.add(iva_parcial).add(impuesto_exento).add(impuesto_iva);
                    } else {
                        NETO = PERCEPCIONIB;
                    }
                    pstmtInsert.setBigDecimal(15, NETO.setScale(SCALE, BigDecimal.ROUND_HALF_UP));

                } else {
                    pstmtInsert.setString(8, "ANULADO");
                    pstmtInsert.setString(9, null);
                    pstmtInsert.setBigDecimal(10, null);
                    pstmtInsert.setBigDecimal(11, null);
                    pstmtInsert.setBigDecimal(12, null);
                    pstmtInsert.setString(13, null);
                    pstmtInsert.setBigDecimal(14, null);
                    pstmtInsert.setBigDecimal(15, null);
                    impuesto_iva = BigDecimal.ZERO;
                    impuesto_exento = BigDecimal.ZERO;
                    iva_parcial = BigDecimal.ZERO;
                    PERCEPCIONIB = BigDecimal.ZERO;
                }
                pstmtInsert.setDate(16, DATETRX);
                //pstmtInsert.setString(16, fecha);

                pstmtInsert.executeQuery();
                closeStatement(pstmtInsert);
                grabado = grabado.add(impuesto_iva);
                exento = exento.add(impuesto_exento);
                ivaCredito = ivaCredito.add(iva_parcial);
                otros = otros.add(PERCEPCIONIB);

                DB.commit(true, get_TrxName());
            }

            rs.close();
            closeStatement(pstmt);

            total = grabado.add(exento.add(ivaCredito.add(otros)));

            if (ok) {
                // ingreso la ultima fila de totales
                sqlInsert = "delete from T_LIVACOMPRAS_TOTAL";
                DB.executeUpdate(sqlInsert, null);
                DB.commit(true, get_TrxName());
                String sqlUpdate = "insert into T_LIVACOMPRAS_TOTAL VALUES(?,?,'Y',?,?,?,?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlUpdate.toString(), get_TrxName());
                pstmtInsert.setLong(1, AD_CLIENT_ID);
                pstmtInsert.setLong(2, AD_ORG_ID);
                pstmtInsert.setInt(3, p_PInstance_ID);
                pstmtInsert.setString(4, "Total al " + DateTimeUtil.parserFecha(toDate));
                pstmtInsert.setBigDecimal(5, exento.add(transExento).setScale(SCALE, BigDecimal.ROUND_HALF_UP));
                pstmtInsert.setBigDecimal(6, otros.add(transOtros).setScale(SCALE, BigDecimal.ROUND_HALF_UP));
                pstmtInsert.setBigDecimal(7, grabado.add(transGravado).setScale(SCALE, BigDecimal.ROUND_HALF_UP));
                pstmtInsert.setBigDecimal(8, ivaCredito.add(transIVACredito).setScale(SCALE, BigDecimal.ROUND_HALF_UP));
                pstmtInsert.setBigDecimal(9, total.add(transTotal).setScale(SCALE, BigDecimal.ROUND_HALF_UP));
                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
                pstmtInsert.close();
                pstmtInsert = null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenerateLibroIvaCompras.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void loadDebitoFiscal() {
        String sqlRemove = "DELETE FROM T_LIVACOMPRAS_DEBFIS";
        DB.executeUpdate(sqlRemove, null);

        BigDecimal totalExe = BigDecimal.ZERO;
        BigDecimal totalNet = BigDecimal.ZERO;
        BigDecimal totalIVA = BigDecimal.ZERO;
        int cliente = 0;
        int org = 0;

        // Correccion: Incorporacion de libro IVA no grabado
        // 01/12/2011
        // Alejandro Scott
        // Recuperar las notas de debito realizadas entre la fecha indicada
        String sqlQuery =
                " SELECT C_InvoiceTax.AD_CLIENT_ID as AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID as AD_ORG_ID, "
                + " 	 to_Char(C_Tax.NAME) as CATEGORIA, 0 as EXENTO, 0 as EXPORTACION, "
                + "      SUM(C_InvoiceTax.TAXBASEAMT*C_Invoice.COTIZACION) as NETO_GRAVADO, "
                + "      SUM(C_InvoiceTax.TAXAMT*C_Invoice.COTIZACION) as IVA, 'COMMON' as Type "
                + " FROM C_Tax"
                + " INNER JOIN C_InvoiceTax ON (C_InvoiceTax.C_Tax_ID = C_Tax.C_Tax_ID)"
                + " INNER JOIN C_Invoice ON (C_InvoiceTax.C_Invoice_ID = C_Invoice.C_Invoice_ID)"
                + " INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_Invoice.C_DocType_ID)"
                + " WHERE C_Invoice.DATEACCT BETWEEN ? AND ? AND C_InvoiceTax.IsActive = 'Y' AND C_Tax.RATE <> 0 AND "
                + "       C_Tax.IsActive = 'Y' and C_DocType.DocBaseType in ('APC') AND C_Invoice.DOCSTATUS IN ('CO','CL') AND "
                + "       C_Tax.C_Tax_ID <> " + ID_IVANG + " AND C_Invoice.C_DOCTYPE_ID NOT IN (1000245, 5000012, 1000246, 5000011)"
                + " GROUP BY C_InvoiceTax.AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID, C_Tax.NAME "
                + " "
                + " UNION"
                + " "
                + " SELECT C_InvoiceTax.AD_CLIENT_ID as AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID as AD_ORG_ID, "
                + "        'EXENTO' as CATEGORIA, SUM(C_InvoiceTax.TAXBASEAMT*C_Invoice.COTIZACION) as EXENTO, "
                + "        0 as EXPORTACION, 0 as NETO_GRAVADO, SUM(C_InvoiceTax.TAXAMT*C_Invoice.COTIZACION) as IVA, "
                + "        'NORATE' as Type"
                + " FROM C_Tax"
                + " INNER JOIN C_InvoiceTax ON (C_InvoiceTax.C_Tax_ID = C_Tax.C_Tax_ID)"
                + " INNER JOIN C_Invoice ON (C_InvoiceTax.C_Invoice_ID = C_Invoice.C_Invoice_ID)"
                + " INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_Invoice.C_DocType_ID)"
                + " WHERE C_Invoice.DATEACCT BETWEEN ? AND ? AND C_InvoiceTax.IsActive = 'Y' AND C_Tax.RATE = 0 AND "
                + "       C_Tax.IsActive = 'Y' and C_Tax.NAME <> 'EXPORTACIÓN' and C_DocType.DocBaseType in ('APC') AND "
                + "       C_Invoice.DOCSTATUS IN ('CO','CL') AND C_Tax.C_Tax_ID <> " + ID_IVANG + " AND C_Invoice.C_DOCTYPE_ID NOT IN (1000245, 5000012, 1000246, 5000011)"
                + " GROUP BY C_InvoiceTax.AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID "
                + " "
                + " UNION"
                + " "
                + " SELECT C_InvoiceTax.AD_CLIENT_ID as AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID as AD_ORG_ID, "
                + " 	   to_Char(C_Tax.NAME) as CATEGORIA, 0 as EXENTO, 0 as EXPORTACION, "
                + "        SUM(C_InvoiceTax.TAXBASEAMT*C_Invoice.COTIZACION) as NETO_GRAVADO, "
                + "        SUM(C_InvoiceTax.TAXAMT*C_Invoice.COTIZACION) as IVA, 'IVANC' as Type "
                + " FROM C_Tax"
                + " INNER JOIN C_InvoiceTax ON (C_InvoiceTax.C_Tax_ID = C_Tax.C_Tax_ID)"
                + " INNER JOIN C_Invoice ON (C_InvoiceTax.C_Invoice_ID = C_Invoice.C_Invoice_ID)"
                + " INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_Invoice.C_DocType_ID)"
                + " WHERE C_Invoice.DATEACCT BETWEEN ? AND ? AND C_InvoiceTax.IsActive = 'Y' AND C_Tax.IsActive = 'Y' AND "
                + "       C_DocType.DocBaseType in ('APC') AND C_Invoice.DOCSTATUS IN ('CO','CL') AND "
                + "       C_Tax.C_Tax_ID = " + ID_IVANG + " AND C_Invoice.C_DOCTYPE_ID NOT IN (1000245, 5000012, 1000246, 5000011)"
                + " GROUP BY C_InvoiceTax.AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID, C_Tax.NAME";

        try {

            PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());

            sqlQuery = "INSERT INTO T_LIVACOMPRAS_DEBFIS VALUES(?,?,'Y',?,?,?,?,?,?)";
            PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());

            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate);
            pstmt.setTimestamp(3, fromDate);
            pstmt.setTimestamp(4, toDate);
            pstmt.setTimestamp(5, fromDate);
            pstmt.setTimestamp(6, toDate);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ps.setInt(1, rs.getInt(1));					//	CLIENTE
                ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
                ps.setInt(3, p_PInstance_ID);				//	INSTANCE
                ps.setString(4, rs.getString(3));			//	CATEGORIA
                ps.setBigDecimal(5, rs.getBigDecimal(4).setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	EXENTO
                ps.setBigDecimal(6, BigDecimal.ZERO);	//	EXPORTACION
                ps.setBigDecimal(7, rs.getBigDecimal(6).setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	NETO_GRAVADO
                ps.setBigDecimal(8, rs.getBigDecimal(7).setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	IVA

                cliente = rs.getInt(1);
                org = rs.getInt(2);
                totalExe = totalExe.add(rs.getBigDecimal(4));
                
                /*
                 * 
                 *      Zynnia 13/02/2012 - José Fantasia
                 *      Hoffman
                 *      IVA No Computable no tiene que sumar a ningún total ni a los parciales
                 *      de crédito o débito fiscal.
                 * 
                 * 
                 */                
                
                if (rs.getString(8).equals("IVANC")) { // IVA no gravado
                    //totalIVA = totalIVA.add(rs.getBigDecimal(7));
                } else {
                    totalIVA = totalIVA.add(rs.getBigDecimal(7));
                }                
                //totalIVA = totalIVA.add(rs.getBigDecimal(7));
                totalNet = totalNet.add(rs.getBigDecimal(6));

                ps.executeQuery();
            }
            rs.close();
            closeStatement(pstmt);
            closeStatement(ps);

            sqlQuery = "INSERT INTO T_LIVACOMPRAS_DEBFIS VALUES(?,?,'Y',?,?,?,?,?,?)";
            ps = DB.prepareStatement(sqlQuery, get_TrxName());
            ps.setInt(1, cliente);			//	CLIENTE
            ps.setInt(2, org);				//	ORGANIZACION
            ps.setInt(3, p_PInstance_ID);	//	INSTANCE
            ps.setString(4, "TOTAL");		//	CATEGORIA
            ps.setBigDecimal(5, totalExe.setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	EXENTO
            ps.setBigDecimal(6, BigDecimal.ZERO);	//	EXPORTACION
            ps.setBigDecimal(7, totalNet.setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	NETO_GRAVADO
            ps.setBigDecimal(8, totalIVA.setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	IVA and IVA No Gravado

            ps.executeQuery();
            ps.close();
            ps = null;

        } catch (Exception exception) {
            exception.printStackTrace();
            log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaVentas.doIt.loadPercepciones " + exception.getMessage());
        }
    }

    protected void loadCreditoFiscal() {
        String sqlRemove = "DELETE FROM T_LIVACOMPRAS_CREDFIS";
        DB.executeUpdate(sqlRemove, null);

        BigDecimal totalExe = BigDecimal.ZERO;
        BigDecimal totalNet = BigDecimal.ZERO;
        BigDecimal totalIVA = BigDecimal.ZERO;
        int cliente = 0;
        int org = 0;

        // Recuperar las notas de credito realizadas entre la fecha indicada
        String sqlQuery =
                " SELECT C_InvoiceTax.AD_CLIENT_ID as AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID as AD_ORG_ID, "
                + " 	 to_Char(C_Tax.NAME) as CATEGORIA, 0 as EXENTO, 0 as EXPORTACION, "
                + "      SUM(C_InvoiceTax.TAXBASEAMT*C_Invoice.COTIZACION) as NETO_GRAVADO, "
                + "      SUM(C_InvoiceTax.TAXAMT*C_Invoice.COTIZACION) as IVA, 'COMMON' as Type "
                + " FROM C_Tax"
                + " INNER JOIN C_InvoiceTax ON (C_InvoiceTax.C_Tax_ID = C_Tax.C_Tax_ID)"
                + " INNER JOIN C_Invoice ON (C_InvoiceTax.C_Invoice_ID = C_Invoice.C_Invoice_ID)"
                + " INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_Invoice.C_DocType_ID)"
                + " WHERE C_Invoice.DATEACCT between ? and ? AND C_InvoiceTax.IsActive = 'Y' AND C_Tax.RATE <> 0 AND "
                + "       C_Tax.IsActive = 'Y' AND C_DocType.DocBaseType in ('API') AND C_Invoice.DOCSTATUS IN ('CO','CL') AND "
                + "       C_Tax.C_Tax_ID <> " + ID_IVANG + " AND C_Invoice.C_DOCTYPE_ID NOT IN (1000245, 5000012, 1000246, 5000011)"
                + " GROUP BY C_InvoiceTax.AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID, C_Tax.NAME"
                + " "
                + " UNION"
                + " "
                + " SELECT C_InvoiceTax.AD_CLIENT_ID as AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID as AD_ORG_ID, "
                + "        'EXENTO' as CATEGORIA, SUM(C_InvoiceTax.TAXBASEAMT*C_Invoice.COTIZACION) as EXENTO, "
                + "        0 as EXPORTACION, 0 as NETO_GRAVADO, SUM(C_InvoiceTax.TAXAMT*C_Invoice.COTIZACION) as IVA, "
                + "        'NORATE' as Type"
                + " FROM C_Tax"
                + " INNER JOIN C_InvoiceTax ON (C_InvoiceTax.C_Tax_ID = C_Tax.C_Tax_ID)"
                + " INNER JOIN C_Invoice ON (C_InvoiceTax.C_Invoice_ID = C_Invoice.C_Invoice_ID)"
                + " INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_Invoice.C_DocType_ID)"
                + " WHERE C_Invoice.DATEACCT between ? and ? AND C_InvoiceTax.IsActive = 'Y' AND C_Tax.RATE = 0 AND "
                + "       C_Tax.IsActive = 'Y' and C_Tax.NAME <> 'EXPORTACIÓN' and C_DocType.DocBaseType in ('API') AND "
                + "       C_Invoice.DOCSTATUS IN ('CO','CL') AND C_Tax.C_Tax_ID <> " + ID_IVANG + " AND C_Invoice.C_DOCTYPE_ID NOT IN (1000245, 5000012, 1000246, 5000011)"
                + " GROUP BY C_InvoiceTax.AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID"
                + " "
                + " UNION"
                + " "
                + " SELECT C_InvoiceTax.AD_CLIENT_ID as AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID as AD_ORG_ID, "
                + " 	   to_Char(C_Tax.NAME) as CATEGORIA, 0 as EXENTO, 0 as EXPORTACION, "
                + "      SUM(C_InvoiceTax.TAXBASEAMT*C_Invoice.COTIZACION) as NETO_GRAVADO, "
                + "      SUM(C_InvoiceTax.TAXAMT*C_Invoice.COTIZACION) as IVA, 'IVANC' as Type "
                + " FROM C_Tax"
                + " INNER JOIN C_InvoiceTax ON (C_InvoiceTax.C_Tax_ID = C_Tax.C_Tax_ID)"
                + " INNER JOIN C_Invoice ON (C_InvoiceTax.C_Invoice_ID = C_Invoice.C_Invoice_ID)"
                + " INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_Invoice.C_DocType_ID)"
                + " WHERE C_Invoice.DATEACCT between ? and ? AND C_InvoiceTax.IsActive = 'Y' AND C_Tax.IsActive = 'Y' AND "
                + "       C_DocType.DocBaseType in ('API') AND C_Invoice.DOCSTATUS IN ('CO','CL') AND "
                + "       C_Tax.C_Tax_ID = " + ID_IVANG + " AND C_Invoice.C_DOCTYPE_ID NOT IN (1000245, 5000012, 1000246, 5000011)"
                + " GROUP BY C_InvoiceTax.AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID, C_Tax.NAME";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());

            sqlQuery = "INSERT INTO T_LIVACOMPRAS_CREDFIS VALUES(?,?,'Y',?,?,?,?,?,?)";
            PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());

            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate);
            pstmt.setTimestamp(3, fromDate);
            pstmt.setTimestamp(4, toDate);
            pstmt.setTimestamp(5, fromDate);
            pstmt.setTimestamp(6, toDate);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ps.setInt(1, rs.getInt(1));					//	CLIENTE
                ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
                ps.setInt(3, p_PInstance_ID);				//	INSTANCE
                ps.setString(4, rs.getString(3));			//	CATEGORIA
                ps.setBigDecimal(5, rs.getBigDecimal(4).setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	EXENTO
                ps.setBigDecimal(6, BigDecimal.ZERO);	//	EXPORTACION
                ps.setBigDecimal(7, rs.getBigDecimal(6).setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	NETO_GRAVADO
                
                
                /*
                 * 
                 *      Zynnia 13/02/2012 - José Fantasia
                 *      Hoffman
                 *      IVA No Computable no tiene que sumar a ningún total ni a los parciales
                 *      de crédito o débito fiscal.
                 * 
                 * 
                 */
                
                ps.setBigDecimal(8, rs.getBigDecimal(7).setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	IVA
                
                if (rs.getString(8).equals("IVANC")) { // IVA no gravado
                    //totalIVA = totalIVA.subtract(rs.getBigDecimal(7));
                } else {
                    totalIVA = totalIVA.add(rs.getBigDecimal(7));
                }

                cliente = rs.getInt(1);
                org = rs.getInt(2);
                totalExe = totalExe.add(rs.getBigDecimal(4));
                totalNet = totalNet.add(rs.getBigDecimal(6));

                ps.executeQuery();
            }
            rs.close();
            closeStatement(pstmt);
            closeStatement(ps);

            sqlQuery = "INSERT INTO T_LIVACOMPRAS_CREDFIS VALUES(?,?,'Y',?,?,?,?,?,?)";
            ps = DB.prepareStatement(sqlQuery, get_TrxName());
            ps.setInt(1, cliente);			//	CLIENTE
            ps.setInt(2, org);				//	ORGANIZACION
            ps.setInt(3, p_PInstance_ID);	//	INSTANCE
            ps.setString(4, "TOTAL");		//	CATEGORIA
            ps.setBigDecimal(5, totalExe.setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	EXENTO
            ps.setBigDecimal(6, BigDecimal.ZERO);	//	EXPORTACION
            ps.setBigDecimal(7, totalNet.setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	NETO_GRAVADO
            ps.setBigDecimal(8, totalIVA.setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	IVA and IVA No Gravado

            ps.executeQuery();
            ps.close();
            ps = null;

        } catch (Exception exception) {
            log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaVentas.doIt.loadPercepciones " + exception.getStackTrace());
        }
    }

    protected void loadRetenciones() {
        String sqlRemove = "DELETE FROM T_LIVACOMPRAS_RETEN";
        DB.executeUpdate(sqlRemove, null);

        BigDecimal total = BigDecimal.ZERO;
        int cliente = Env.getAD_Client_ID(Env.getCtx());
        int org = Env.getAD_Org_ID(Env.getCtx());

        boolean ok = false;

        // Recuperar las retenciones realizadas entre la fecha indicada
        String sqlQuery =
                " SELECT C_PAYMENTRET.AD_CLIENT_ID, C_PAYMENTRET.AD_ORG_ID, to_char(C_DocType.NAME) as CONCEPTO, SUM(C_PAYMENTRET.IMPORTE) as IMPORTE, to_char(rg.NAME) AS REGIMEN "
                + " FROM C_PAYMENTRET "
                + " INNER JOIN C_Payment ON (C_Payment.C_Payment_ID = C_PAYMENTRET.C_Payment_ID) "
                + " INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_PAYMENTRET.C_DocType_ID) "
                + " INNER JOIN AD_REF_LIST rg ON (rg.VALUE = C_Payment.C_REGIMENGANANCIAS_V_ID) "
                + " INNER JOIN AD_REFERENCE re ON (re.AD_REFERENCE_ID = rg.AD_REFERENCE_ID) "
                + " WHERE C_PAYMENTRET.TIPO_RET = 'G' AND C_PAYMENTRET.IsActive = 'Y' AND C_Payment.DATETRX between ? and ? AND C_Payment.docstatus in ('CO','CL')  AND re.NAME = 'C_REGIMENGANANCIAS_V'"
                + " GROUP BY C_PAYMENTRET.AD_CLIENT_ID, C_PAYMENTRET.AD_ORG_ID, C_DocType.NAME, to_char(rg.NAME) "
                + "  UNION "
                + "  ( "
                + " 	SELECT C_PAYMENTRET.AD_CLIENT_ID, C_PAYMENTRET.AD_ORG_ID, to_char(C_DocType.NAME) as CONCEPTO, SUM(C_PAYMENTRET.IMPORTE) as IMPORTE, to_char(C_PAYMENTRET.ALICUOTA) AS REGIMEN "
                + " 	FROM C_PAYMENTRET  "
                + " 	INNER JOIN C_Payment ON (C_Payment.C_Payment_ID = C_PAYMENTRET.C_Payment_ID) "
                + " 	INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_PAYMENTRET.C_DocType_ID) "
                + " 	WHERE C_PAYMENTRET.TIPO_RET = 'B' AND C_PAYMENTRET.IsActive = 'Y' AND C_Payment.DATETRX between ? and ? AND C_Payment.docstatus in ('CO','CL') "
                + " 	GROUP BY C_PAYMENTRET.AD_CLIENT_ID, C_PAYMENTRET.AD_ORG_ID, C_DocType.NAME, to_char(C_PAYMENTRET.ALICUOTA) "
                + " 	  UNION "
                + " 	  ( "
                + " 		SELECT C_PAYMENTRET.AD_CLIENT_ID, C_PAYMENTRET.AD_ORG_ID, to_char(C_DocType.NAME) as CONCEPTO, SUM(C_PAYMENTRET.IMPORTE) as IMPORTE, '' AS REGIMEN "
                + " 			FROM C_PAYMENTRET  "
                + " 		INNER JOIN C_Payment ON (C_Payment.C_Payment_ID = C_PAYMENTRET.C_Payment_ID) "
                + " 		INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_PAYMENTRET.C_DocType_ID) "
                + " 		WHERE C_PAYMENTRET.TIPO_RET <> 'B' AND C_PAYMENTRET.TIPO_RET <> 'G' AND C_PAYMENTRET.IsActive = 'Y' AND C_Payment.DATETRX between ? and ? AND C_Payment.docstatus in ('CO','CL') "
                + " 		GROUP BY C_PAYMENTRET.AD_CLIENT_ID, C_PAYMENTRET.AD_ORG_ID, C_DocType.NAME, '' "
                + " 	  ) "
                + " 	)";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());

            sqlQuery = "INSERT INTO T_LIVACOMPRAS_RETEN VALUES(?,?,'Y',?,?,?)";
            PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());

            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate);
            pstmt.setTimestamp(3, fromDate);
            pstmt.setTimestamp(4, toDate);
            pstmt.setTimestamp(5, fromDate);
            pstmt.setTimestamp(6, toDate);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ok = true;
                ps.setInt(1, rs.getInt(1));					//	CLIENTE
                ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
                ps.setInt(3, p_PInstance_ID);				//	INSTANCE
                if (rs.getString(5) != null) {
                    ps.setString(4, rs.getString(3) + " - " + rs.getString(5));
                } else {
                    ps.setString(4, rs.getString(3));		//	CONCEPTO
                }
                ps.setBigDecimal(5, rs.getBigDecimal(4).setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	IMPORTE

                cliente = rs.getInt(1);
                org = rs.getInt(2);
                total = total.add(rs.getBigDecimal(4));

                ps.executeUpdate();
            }
            rs.close();
            closeStatement(pstmt);
            closeStatement(ps);

            if (ok) {
                sqlQuery = "INSERT INTO T_LIVACOMPRAS_RETEN VALUES(?,?,'Y',?,?,?)";
                ps = DB.prepareStatement(sqlQuery, get_TrxName());
                ps.setInt(1, cliente);				//	CLIENTE
                ps.setInt(2, org);					//	ORGANIZACION
                ps.setInt(3, p_PInstance_ID);		//	INSTANCE
                ps.setString(4, "TOTAL");			//	CONCEPTO
                ps.setBigDecimal(5, total.setScale(SCALE, BigDecimal.ROUND_HALF_UP));			//	IMPORTE

                ps.executeUpdate();
                ps.close();
                ps = null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaVentas.doIt.loadPercepciones " + exception.getMessage());
        }
    }

    protected void loadPercepciones() {
        String sqlRemove = "DELETE FROM T_LIVACOMPRAS_PERCEP";
        DB.executeUpdate(sqlRemove, null);

        BigDecimal total = BigDecimal.ZERO;
        int cliente = 0;
        int org = 0;

        // Recuperar las percepciones realizadas entre la fecha indicada
        String sqlQuery =
                " SELECT ip.AD_CLIENT_ID as AD_CLIENT_ID, ip.AD_ORG_ID as AD_ORG_ID, rp.NAME as CONCEPTO, "
                + " CASE "
                + " 	WHEN dt.docbasetype = 'API' THEN SUM(ip.AMOUNT)"
                + " 	ELSE -SUM(ip.AMOUNT)"
                + " END IMPORTE "
                + " FROM C_InvoicePercep ip"
                + " INNER JOIN C_REGPERCEP_RECIB rp ON (rp.C_REGPERCEP_RECIB_ID = ip.C_REGPERCEP_RECIB_ID)"
                + " INNER JOIN C_Invoice i ON (i.C_Invoice_ID = ip.C_Invoice_ID)"
                + " INNER JOIN C_DocType dt ON (dt.C_Doctype_ID = i.c_doctype_id)"
                + " WHERE ip.IsActive = 'Y' AND i.DATEACCT between ? and ?"
                + "   AND i.docstatus in ('CO','CL') AND i.C_DOCTYPE_ID NOT IN (1000245, 5000012, 1000246, 5000011) "
                + " GROUP BY ip.AD_CLIENT_ID, ip.AD_ORG_ID, rp.NAME,dt.docbasetype";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());

            sqlQuery = "INSERT INTO T_LIVACOMPRAS_PERCEP VALUES(?,?,'Y',?,?,?)";
            PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());

            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ps.setInt(1, rs.getInt(1));					//	CLIENTE
                ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
                ps.setInt(3, p_PInstance_ID);				//	INSTANCE
                ps.setString(4, rs.getString(3));			//	CONCEPTO
                //ps.setBigDecimal(5, rs.getBigDecimal(4));	//	ALICUOTA
                ps.setBigDecimal(5, rs.getBigDecimal(4).setScale(SCALE, BigDecimal.ROUND_HALF_UP));	//	IMPORTE

                cliente = rs.getInt(1);
                org = rs.getInt(2);
                total = total.add(rs.getBigDecimal(4));

                ps.executeQuery();
            }
            rs.close();
            closeStatement(pstmt);
            closeStatement(ps);

            sqlQuery = "INSERT INTO T_LIVACOMPRAS_PERCEP VALUES(?,?,'Y',?,?,?)";
            ps = DB.prepareStatement(sqlQuery, get_TrxName());
            ps.setInt(1, cliente);					//	CLIENTE
            ps.setInt(2, org);					//	ORGANIZACION
            ps.setInt(3, p_PInstance_ID);				//	INSTANCE
            ps.setString(4, "TOTAL");					//	CONCEPTO
            //ps.setBigDecimal(5, null);					//	ALICUOTA
            ps.setBigDecimal(5, total.setScale(SCALE, BigDecimal.ROUND_HALF_UP));					//	IMPORTE

            ps.executeUpdate();
            ps.close();
            ps = null;
        } catch (Exception exception) {
            exception.printStackTrace();
            log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaVentas.doIt.loadPercepciones " + exception.getMessage());
        }

    }

    protected void setearTemp() {
        try {
            if (AD_CLIENT_ID != null) {
                String sqlRemove2 = "delete from TEMP_LIBRO_IVA_COMPRAS";
                DB.executeUpdate(sqlRemove2, null);
                DB.commit(true, null);
                String sqlUpdate = "insert into TEMP_LIBRO_IVA_COMPRAS VALUES(?,?,'Y',?,?,?)";
                PreparedStatement pstmt = DB.prepareStatement(sqlUpdate.toString(), get_TrxName());
                pstmt.setLong(1, AD_CLIENT_ID);
                pstmt.setLong(2, AD_ORG_ID);
                pstmt.setTimestamp(3, toDate);
                pstmt.setLong(4, num_hoja);
                //pstmt.setBigDecimal(5, new BigDecimal(0));
                pstmt.setTimestamp(5, fromDate);
                pstmt.executeQuery();
                DB.commit(true, null);
                pstmt.close();
                pstmt = null;
            }
        } catch (Exception exception) {
            log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaVentas.doIt.Sector1 " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}