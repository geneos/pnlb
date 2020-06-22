/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.compiere.model.MAllocationHdr;
import org.compiere.model.MDocType;
import org.compiere.model.MFactAcct;
import org.compiere.model.MJournal;
import org.compiere.model.MMOVIMIENTOFONDOS;
import org.compiere.model.MPayment;
import org.compiere.model.MInvoice;
import org.compiere.process.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.*;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *
 * @author Daniel Gini Bision
 */
public class GenerateLibroDiario extends SvrProcess {

    int p_instance;
    private Timestamp fromDate;
    private Timestamp toDate;
    private BigDecimal schema;
    private int table_id = -1;
    private boolean withTotalEachLine = false;
    private Long num_hoja = new Long(1);
    int count = 0;
    long org;
    int secuencia = 100;
    int gl_category_id = 0;
    
    private static int ASIENTO_COMUN = 0;
    private static int ASIENTO_APERTURA = 1;
    private static int ASIENTO_CIERRE = 2;
    private static int ASIENTO_RESULTADO = 3;

    protected String doIt() throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        gl_category_id = getGLCategory("Inflacion");

        try {

            String sqlQuery = "";
            PreparedStatement pstmt;

            if (table_id != -1) {
                sqlQuery = "select Distinct AD_CLIENT_ID,AD_ORG_ID,C_ACCTSCHEMA_ID,DATEACCT,RECORD_ID,COMPANIA,ANO,AD_TABLE_ID,FACTNO from RV_LIBRODIARIO where C_ACCTSCHEMA_ID = ? AND DATEACCT between ? and ? AND AD_TABLE_ID = ? and TIPO_ASIENTO = "+ASIENTO_COMUN+" Order By DATEACCT, FACTNO";
                pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
                pstmt.setInt(1, schema.intValue());
                pstmt.setTimestamp(2, fromDate);
                pstmt.setTimestamp(3, toDate);
                pstmt.setInt(4, table_id);
            } else {
                sqlQuery = "select Distinct AD_CLIENT_ID,AD_ORG_ID,C_ACCTSCHEMA_ID,DATEACCT,RECORD_ID,COMPANIA,ANO,AD_TABLE_ID,FACTNO from RV_LIBRODIARIO where C_ACCTSCHEMA_ID = ? AND DATEACCT between ? and ? and TIPO_ASIENTO = "+ASIENTO_COMUN+" Order By DATEACCT, FACTNO";
                pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
                pstmt.setInt(1, schema.intValue());
                pstmt.setTimestamp(2, fromDate);
                pstmt.setTimestamp(3, toDate);
            }

            ResultSet rs = pstmt.executeQuery();
            System.out.println("[" + formatter.format(date) + "] Start!: ");

            try {

                if (rs.next()) {
                    //	cabecera(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(6), rs.getInt(7));

                    DB.executeUpdate("DELETE from T_LIBRODIARIOASIENTO", null);
                    DB.executeUpdate("DELETE from T_LIBRODIARIO_LINE", null);

                    int asientoid = 1000000;
                    
                    //Inserto asiento de APERTURA (Si corresponde)
                    if (asientoCuentasPatrimoniales(ASIENTO_APERTURA,asientoid,false));
                        asientoid++;
                        
                    //Inserto asiento de APERTURA con inlfacion (Si corresponde)
                    if (asientoCuentasPatrimoniales(ASIENTO_APERTURA,asientoid,true));
                        asientoid++;
                        
                    count = 0;
                    do {
                        System.out.println("Asiento con fecha: "+rs.getDate(4));
                        if (asiento(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(5), rs.getDate(4), rs.getInt(8), rs.getString(9), asientoid)) {
                            secuencia = renglon(rs.getInt(1), rs.getInt(2), rs.getInt(5), secuencia, rs.getInt(8), asientoid, rs.getString(9));
                        }

                        if (count >= 100) {
                            count = 0;
                            DB.commit(true, get_TrxName());

                            date = new Date(System.currentTimeMillis());
                            System.out.println("[" + formatter.format(date) + "] Commit!: " + asientoid);
                        }

                        asientoid++;
                    } while (rs.next());
                    
                    //Inserto asiento RESULTADO (Si corresponde)
                    if (asientoCuentasPatrimoniales(ASIENTO_RESULTADO,asientoid,false));
                        asientoid++;
                    
                    //Inserto asiento RESULTADO con INFLACION (Si corresponde)
                    if (asientoCuentasPatrimoniales(ASIENTO_RESULTADO,asientoid,true));
                        asientoid++;
                    
                    //Inserto asiento CIERRE (Si corresponde)
                    if (asientoCuentasPatrimoniales(ASIENTO_CIERRE,asientoid,false));
                        asientoid++;
                        
                    //Inserto asiento CIERRE con INFLACION (Si corresponde)
                    if (asientoCuentasPatrimoniales(ASIENTO_CIERRE,asientoid,true));
                        asientoid++;
                        
                    DB.commit(true, get_TrxName());
                    UtilProcess.initViewer("Libro Diario General", p_instance, getProcessInfo());
                }

                rs.close();
                pstmt.close();
            } catch (Exception exception) {
                Logger.getLogger(GenerateLibroDiario.class.getName()).log(Level.SEVERE, null, exception);
                throw exception;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenerateLibroDiario.class.getName()).log(Level.SEVERE, null, ex);
        }

        date = new Date(System.currentTimeMillis());
        System.out.println("[" + formatter.format(date) + "] Finish!");
        return "success";
    }

    protected boolean asiento(int ad_client_id, int ad_org_id, int c_acctschema_id, int record_id, Date date, int AD_Table_ID, String nroAsiento, int asientoid) {
        try {
            if (nroAsiento == null) {
                nroAsiento = "00000000";
            }

            //Check non ZERO amount
            String sqlQueryCheck = "SELECT SUM(CREDITO),SUM(DEBITO) FROM RV_LIBRODIARIO WHERE RECORD_ID = ? AND AD_TABLE_ID = ?";

            PreparedStatement pstmtInsert = DB.prepareStatement(sqlQueryCheck, get_TrxName());
            pstmtInsert.setInt(1, record_id);
            pstmtInsert.setInt(2, AD_Table_ID);

            ResultSet rs = pstmtInsert.executeQuery();

            if (rs.next()) {
                BigDecimal credito = rs.getBigDecimal(1);
                BigDecimal debito = rs.getBigDecimal(1);
                if (credito.equals(BigDecimal.ZERO) && debito.equals(BigDecimal.ZERO)) {
                    pstmtInsert.close();
                    return false;
                }

            }

            String detalle = null;
            String sqlInsert = "";

            String sqlQuery = "SELECT CONCEPTO,AD_TABLE_ID,RECORD_ID,DESCRIPTION FROM RV_LIBRODIARIO WHERE RECORD_ID = ? AND AD_TABLE_ID = ?";

            pstmtInsert = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmtInsert.setInt(1, record_id);
            pstmtInsert.setInt(2, AD_Table_ID);

            rs = pstmtInsert.executeQuery();


            if (rs.next()) {
                detalle = rs.getString(1);

                if (rs.getInt(2) == MJournal.getTableId(MJournal.Table_Name)) {
                    /*sqlQuery = "SELECT jb.DESCRIPTION FROM GL_JournalBatch jb INNER JOIN GL_Journal j"
                    + " ON ( j.GL_JournalBatch_ID = jb.GL_JournalBatch_ID and  j.GL_Journal_ID = ?)";
                    PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());
                    ps.setInt(1, rs.getInt(3));
                    ResultSet set = ps.executeQuery();
                    if (set.next())
                    detalle = set.getString(1);
                    set.close();*/
                } else if (rs.getInt(2) == MInvoice.getTableId(MInvoice.Table_Name)) {
                    /*MInvoice invoice = new MInvoice(getCtx(), rs.getInt(3), null);
                    MDocType docType = new MDocType(getCtx(), invoice.getC_DocTypeTarget_ID(), null);
                    detalle = docType.getName() + " - " + invoice.getDocumentNo();
                    detalle ="FC  " + invoice.getDocumentNo();*/
                    //Esto no se que es!
                } else if (rs.getInt(2) == MFactAcct.getTableId(MFactAcct.Table_Name)) {
                    /*detalle = rs.getString(4);
                    
                    sqlQuery = "SELECT RECORD_FACT_ID FROM FACT_ACCT_RESUMEN WHERE RECORD_RES_ID = ? AND TABLE_FACT_ID = " + MPayment.getTableId(MPayment.Table_Name);
                    PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());
                    ps.setInt(1, rs.getInt(3));
                    ResultSet set = ps.executeQuery();
                    
                    if (set.next()) {
                    /*MPayment payment = new MPayment(getCtx(), set.getInt(1), null);
                    if (detalle != null) {
                    detalle += " - " + payment.getDocumentNo();
                    } else {
                    detalle = payment.getDocumentNo();
                    }
                    } else {
                    /*sqlQuery = "SELECT RECORD_FACT_ID FROM FACT_ACCT_RESUMEN WHERE RECORD_RES_ID = ? AND TABLE_FACT_ID = " + MInvoice.getTableId(MInvoice.Table_Name);
                    ps = DB.prepareStatement(sqlQuery, get_TrxName());
                    ps.setInt(1, rs.getInt(3));
                    set = ps.executeQuery();
                    
                    if (set.next()) {
                    MInvoice invoice = new MInvoice(getCtx(), set.getInt(1), null);
                    if (detalle != null) {
                    detalle += " - " + invoice.getDocumentNo();
                    } else {
                    detalle = invoice.getDocumentNo();
                    }
                    }
                    }*/
                } else if (rs.getInt(2) == MMOVIMIENTOFONDOS.getTableId(MMOVIMIENTOFONDOS.Table_Name)) {
                    /*MMOVIMIENTOFONDOS movFondos = new MMOVIMIENTOFONDOS(getCtx(), rs.getInt(3), null);
                    //detalle = movFondos.getStringTipo() + "_" + movFondos.getDocumentNo();
                    detalle ="MF " + movFondos.getDocumentNo();*/
                } else if (rs.getInt(2) == MPayment.getTableId(MPayment.Table_Name)) {
                    /*MPayment payment = new MPayment(getCtx(), rs.getInt(3), null);
                    if (payment.isReceipt()) {
                    detalle = "RC " + payment.getDocumentNo();
                    } else {
                    detalle = "OP " + payment.getDocumentNo();
                    }*/
                } else if (rs.getInt(2) == MAllocationHdr.getTableId(MAllocationHdr.Table_Name)) {
                    /*MAllocationHdr allocate = new MAllocationHdr(getCtx(), rs.getInt(3), null);
                    detalle = "AS " + allocate.getDocumentNo();*/
                } else {
                    detalle = "";
                }
            }
            rs.close();
            pstmtInsert.close();

            //detalle = rellenarNro(nroAsiento) + " - " + detalle + " - " + ValueFormat.getFechaARG(date);

            try {
                Timestamp ts = new Timestamp(date.getTime());

                sqlInsert = "insert into T_LIBRODIARIOASIENTO values(?,?,?,?,?,?,?,'Y',?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());

                pstmtInsert.setInt(1, asientoid);
                pstmtInsert.setInt(2, ad_client_id);
                pstmtInsert.setInt(3, ad_org_id);
                pstmtInsert.setInt(4, p_instance);
                pstmtInsert.setString(5, detalle);
                pstmtInsert.setTimestamp(6, ts);
                pstmtInsert.setInt(7, c_acctschema_id);
                pstmtInsert.setString(8, rellenarNro(nroAsiento));
                pstmtInsert.setLong(9, num_hoja);
                pstmtInsert.setInt(10, AD_Table_ID);
                pstmtInsert.setString(11, withTotalEachLine ? "Y" : "N");

                pstmtInsert.executeQuery();

                pstmtInsert.close();
                count++;
                //DB.commit(true, get_TrxName());

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            
            rs.close();
            pstmtInsert.close();

        } catch (SQLException ex) {
            Logger.getLogger(GenerateLibroDiario.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;

    }

    protected String rellenarNro(String nro) {
        if (nro != null) {
            switch (nro.length()) {
                case 1:
                    return "0000000" + nro;
                case 2:
                    return "000000" + nro;
                case 3:
                    return "00000" + nro;
                case 4:
                    return "0000" + nro;
                case 5:
                    return "000" + nro;
                case 6:
                    return "00" + nro;
                case 7:
                    return "0" + nro;
                default:
                    return nro;
            }
        } else {
            return "00000000";
        }
    }

    protected int renglon(int ad_client_id, int ad_org_id, int record_id, int secuencia, int ad_table_id, int asientoid, String nroAsiento) throws Exception {
        try {
            String sqlQuery = "", sqlInsert = "";

            BigDecimal sumaDR = new BigDecimal(0);
            BigDecimal sumaCR = new BigDecimal(0);
            PreparedStatement pstmtInsert = null;

            sqlQuery = "SELECT NROCUENTA,CUENTA,DEBITO,CONCEPTO FROM RV_LIBRODIARIO WHERE RECORD_ID = ? AND AD_TABLE_ID = ? AND DEBITO <> 0 AND DEBITO IS NOT NULL";
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setInt(1, record_id);
            pstmt.setInt(2, ad_table_id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                do {
                    String descripcion = rs.getString(4);
                    if (ad_table_id == MPayment.getTableId(MPayment.Table_Name) && rs.getString(4) != null) {
                        //Optimization
                        /*MPayment pay = new MPayment(getCtx(), record_id, null);
                        if (!rs.getString(4).equals(pay.getDocumentNo())) {
                        descripcion = rs.getString(4);
                        }*/
                    } else if (ad_table_id == MMOVIMIENTOFONDOS.getTableId(MMOVIMIENTOFONDOS.Table_Name) && rs.getString(4) != null) {
                        descripcion = rs.getString(4);
                    }
                    
                    if (rs.getBigDecimal(3).compareTo(BigDecimal.ZERO) >= 1){
                        sqlInsert = "insert into T_LIBRODIARIO_LINE values(?,?,?,?,?,?,?,?,?,?,'Y',?)";
                        pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());

                        pstmtInsert.setInt(1, secuencia);
                        pstmtInsert.setInt(2, asientoid);
                        pstmtInsert.setInt(3, asientoid);
                        pstmtInsert.setString(4, rs.getString(1));
                        pstmtInsert.setString(5, rs.getString(2));
                        pstmtInsert.setBigDecimal(6, rs.getBigDecimal(3));
                        pstmtInsert.setBigDecimal(7, null);
                        pstmtInsert.setInt(8, p_instance);
                        pstmtInsert.setInt(9, ad_client_id);
                        pstmtInsert.setInt(10, ad_org_id);
                        pstmtInsert.setString(11, descripcion);

                        pstmtInsert.executeQuery();
                        count++;
                        //DB.commit(true, get_TrxName());

                        pstmtInsert.close();

                        secuencia++;
                        sumaDR = sumaDR.add(rs.getBigDecimal(3));
                    }
                    

                } while (rs.next());
            }

            rs.close();
            pstmt.close();

            sqlQuery = "SELECT NROCUENTA,CUENTA,CREDITO,CONCEPTO FROM RV_LIBRODIARIO WHERE RECORD_ID = ? AND AD_TABLE_ID = ? AND CREDITO <> 0 AND CREDITO IS NOT NULL";
            pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setInt(1, record_id);
            pstmt.setInt(2, ad_table_id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                do {
                    String descripcion = "";
                    if (ad_table_id == MPayment.getTableId(MPayment.Table_Name) && rs.getString(4) != null) {
                        //Optimization
                        /*MPayment pay = new MPayment(getCtx(), record_id, null);
                        if (!rs.getString(4).equals(pay.getDocumentNo())) {
                        descripcion = rs.getString(4);
                        }*/
                    } else if (ad_table_id == MMOVIMIENTOFONDOS.getTableId(MMOVIMIENTOFONDOS.Table_Name) && rs.getString(4) != null) {
                        descripcion = rs.getString(4);
                    }

                    if (rs.getBigDecimal(3).compareTo(BigDecimal.ZERO) >= 1){
                        sqlInsert = "insert into T_LIBRODIARIO_LINE values(?,?,?,?,?,?,?,?,?,?,'Y',?)";
                        pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());

                        pstmtInsert.setInt(1, secuencia);
                        pstmtInsert.setInt(2, asientoid);
                        pstmtInsert.setInt(3, asientoid);
                        pstmtInsert.setString(4, rs.getString(1));
                        pstmtInsert.setString(5, rs.getString(2));
                        pstmtInsert.setBigDecimal(6, null);
                        pstmtInsert.setBigDecimal(7, rs.getBigDecimal(3));
                        pstmtInsert.setInt(8, p_instance);
                        pstmtInsert.setInt(9, ad_client_id);
                        pstmtInsert.setInt(10, ad_org_id);
                        pstmtInsert.setString(11, descripcion);

                        pstmtInsert.executeQuery();
                        count++;
                        //DB.commit(true, get_TrxName());

                        pstmtInsert.close();

                        secuencia++;
                        sumaCR = sumaCR.add(rs.getBigDecimal(3));
                    }

                } while (rs.next());
            }

            rs.close();
            pstmt.close();

            if (sumaDR.compareTo(sumaCR) != 0) {
                String message = "Asiento #" + nroAsiento + " no balanceado";
                Exception ex = new Exception(message);
                Logger.getLogger(GenerateLibroDiario.class.getName()).log(Level.SEVERE, message, ex);
                throw ex;
            }


            if (withTotalEachLine) {
                sqlInsert = "insert into T_LIBRODIARIO_LINE values(?,?,?,?,?,?,?,?,?,?,'Y',?)";
                pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());

                pstmtInsert.setInt(1, secuencia);
                pstmtInsert.setInt(2, asientoid);
                pstmtInsert.setInt(3, asientoid);
                pstmtInsert.setString(4, null);
                pstmtInsert.setString(5, null);
                pstmtInsert.setBigDecimal(6, sumaDR);
                pstmtInsert.setBigDecimal(7, sumaCR);
                pstmtInsert.setInt(8, p_instance);
                pstmtInsert.setInt(9, ad_client_id);
                pstmtInsert.setInt(10, ad_org_id);
                pstmtInsert.setString(11, "TOTAL");

                pstmtInsert.executeQuery();


                secuencia++;
                count++;

                rs.close();
                pstmtInsert.close();
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(GenerateLibroDiario.class.getName()).log(Level.SEVERE, null, ex);
        }

        return secuencia;
    }

    protected void cabecera(int ad_client_id, int ad_org_id, int c_acctschema_id, String compania, int ano) {
        String sqlInsert = "";
        PreparedStatement pstmtInsert = null;
        String sqlRemove = "delete from T_LIBRODIARIO_HDR";
        DB.executeUpdate(sqlRemove, null);

        org = ad_org_id;

        try {
            sqlInsert = "insert into T_LIBRODIARIO_HDR values(?,?,?,?,?,?,'Y')";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());

            pstmtInsert.setInt(1, c_acctschema_id);
            pstmtInsert.setString(2, compania);
            pstmtInsert.setInt(3, ano);
            pstmtInsert.setInt(4, p_instance);
            pstmtInsert.setInt(5, ad_client_id);
            pstmtInsert.setInt(6, ad_org_id);

            pstmtInsert.executeQuery();
            //DB.commit(true, get_TrxName());

        } catch (SQLException ex) {
            Logger.getLogger(GeneratePagoCompRet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();

        schema = (BigDecimal) para[0].getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (name.equals("FECHA")) {
                fromDate = (Timestamp) para[i].getParameter();
                toDate = (Timestamp) para[i].getParameter_To();
            } else if (name.equals("TIPO")) {
                table_id = ((BigDecimal) para[i].getParameter()).intValue();
            } else if (name.equals("PAGINA")) {
                num_hoja = ((BigDecimal) para[i].getParameter()).longValue();
            } else if (name.equals("WITHTOTAL")) {
                withTotalEachLine = para[i].getParameter().equals("Y");
            }
        }
        Env.getCtx().put("typePrint", "LIBRO");
        Env.getCtx().put("startPage", num_hoja);

        p_instance = getAD_PInstance_ID();
    }

    

    private boolean asientoCuentasPatrimoniales(int TIPO, int asientoid, boolean isAjusteInflacion) throws Exception {
        boolean found = false;
        String sql = "select Distinct AD_CLIENT_ID,AD_ORG_ID,C_ACCTSCHEMA_ID,DATEACCT,RECORD_ID,COMPANIA,ANO,AD_TABLE_ID,FACTNO from RV_LIBRODIARIO where C_ACCTSCHEMA_ID = ? AND DATEACCT between ? and ? AND TIPO_ASIENTO = ? AND AD_ORG_ID = ? ";
        if (!isAjusteInflacion)
            sql += "  AND GL_Category_Id <> "+gl_category_id;
        else 
            sql += "  AND GL_Category_Id = "+gl_category_id;
         
        PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
        
        try {
            pstmt.setInt(1, schema.intValue());
            pstmt.setTimestamp(2, fromDate);
            pstmt.setTimestamp(3, toDate);
            pstmt.setInt(4, TIPO);
            pstmt.setInt(5, Env.getAD_Org_ID(getCtx()));

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                found = true;
                if (asiento(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(5), rs.getDate(4), rs.getInt(8), rs.getString(9), asientoid)) {
                    secuencia = renglon(rs.getInt(1), rs.getInt(2), rs.getInt(5), secuencia, rs.getInt(8), asientoid, rs.getString(9));
                }
            }
            
            rs.close();
            pstmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(GenerateLibroDiario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return found;
    }
    
    private int getGLCategory(String name) {
        int GL_Category_Id = 1;
         try {

            String sqlQuery = "SELECT GL_Category_ID from GL_Category where name = '"+name+"'"; 

            PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                GL_Category_Id = rs.getInt(1);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateBalance.class.getName()).log(Level.SEVERE, null, ex);
        }
         return GL_Category_Id;
    }
}
