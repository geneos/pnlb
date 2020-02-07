/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.SQLException;



import org.compiere.model.MPeriod;
import org.compiere.process.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.*;

import javax.swing.JOptionPane;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *
 * @author Daniel Gini Bision
 */
public class GenerateBalance extends SvrProcess {

    int p_instance;
    int gl_category_id;
    private Timestamp fromDate;
    private Timestamp toDate;
    private boolean IsAjusteInflacion;
    private BigDecimal schema;
    private Long num_hoja;
    int org;

    protected String doIt() throws Exception {
        try {

            String sqlQuery = "";
            PreparedStatement pstmt;

            MPeriod perFrom = MPeriod.get(Env.getCtx(), fromDate);
            MPeriod perTo = MPeriod.get(Env.getCtx(), toDate);

            if (perFrom.getC_Year_ID() == perTo.getC_Year_ID()) {

                sqlQuery = "select Distinct AD_CLIENT_ID,0,C_ACCTSCHEMA_ID,NROCUENTA,CUENTA,COMPANIA,ANO from RV_BALANCE where C_ACCTSCHEMA_ID = ? AND DATEACCT between ? and ?";
                pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
                pstmt.setInt(1, schema.intValue());
                pstmt.setTimestamp(2, fromDate);
                pstmt.setTimestamp(3, toDate);
                
                if (!IsAjusteInflacion)
                    sqlQuery += "  AND GL_Category_Id <> "+gl_category_id;

                ResultSet rs = pstmt.executeQuery();

                try {

                    if (rs.next()) {

                        cabecera(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(6), rs.getString(7), 100);

                        DB.executeUpdate("DELETE from T_BALANCE_LINE", null);
                        int secuencia = 100;

                        do {

                            secuencia = renglon(rs.getInt(1), org, rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(7), secuencia, 100);

                        } while (rs.next());

                    }
                    System.out.println("Nueva version!");
                    if (!IsAjusteInflacion)
                        UtilProcess.initViewer("Balance Sumas y Saldos", p_instance, getProcessInfo());
                    else
                        //Se remueven los acentos para evitar problemas de compatibilidad
                        UtilProcess.initViewer("Balance Sumas y Saldos: con ajuste por inflacion", p_instance, getProcessInfo());


                    rs.close();
                    pstmt.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "El rango de fechas no pertenece a un �nico ejercicio comercial.", "Par�metros", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            Logger.getLogger(GenerateBalance.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "success";
    }

    protected int renglon(int ad_client_id, int ad_org_id, int c_acctschema_id, String nroCuenta, String cuenta, String anio, int secuencia, int reference) {
        try {

            String sqlQuery = "", sqlInsert = "";

            BigDecimal inicial = new BigDecimal(0);
            BigDecimal sumaDR = new BigDecimal(0);
            BigDecimal sumaCR = new BigDecimal(0);
            PreparedStatement pstmtInsert = null;

            sqlQuery = "SELECT SUM(DEBITO),SUM(CREDITO) FROM RV_BALANCE WHERE NROCUENTA = ? AND CUENTA = ? AND DATEACCT >= to_date('01-01-" + anio + "','dd-mm-yyyy') AND DATEACCT < ?";
            if (!IsAjusteInflacion)
                sqlQuery += "  AND GL_Category_Id <> "+gl_category_id;
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setString(1, nroCuenta);
            pstmt.setString(2, cuenta);
            pstmt.setTimestamp(3, fromDate);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getBigDecimal(2) != null && rs.getBigDecimal(1) != null) {
                    inicial = rs.getBigDecimal(1).subtract(rs.getBigDecimal(2));
                }
            }

            rs.close();
            pstmt.close();

            sqlQuery = "SELECT SUM(DEBITO),SUM(CREDITO) FROM RV_BALANCE WHERE NROCUENTA = ? AND CUENTA = ? AND DATEACCT between ? and ?";
            if (!IsAjusteInflacion)
                sqlQuery += "  AND GL_Category_Id <> "+gl_category_id;
            pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setString(1, nroCuenta);
            pstmt.setString(2, cuenta);
            pstmt.setTimestamp(3, fromDate);
            pstmt.setTimestamp(4, toDate);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getBigDecimal(2) != null && rs.getBigDecimal(1) != null) {
                    sumaDR = rs.getBigDecimal(1);
                    sumaCR = rs.getBigDecimal(2);
                } else if (rs.getBigDecimal(1) != null) {
                    sumaDR = rs.getBigDecimal(1);
                } else if (rs.getBigDecimal(2) != null) {
                    sumaCR = rs.getBigDecimal(2);
                }

            }

            rs.close();
            pstmt.close();

            BigDecimal sfinal = (inicial.add(sumaDR)).subtract(sumaCR);

            try {
                sqlInsert = "insert into T_BALANCE_LINE values(?,?,?,?,?,?,'Y',?,?,?,?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());

                pstmtInsert.setInt(1, secuencia);
                pstmtInsert.setInt(2, reference);
                pstmtInsert.setInt(3, c_acctschema_id);
                pstmtInsert.setInt(4, ad_client_id);
                pstmtInsert.setInt(5, ad_org_id);
                pstmtInsert.setInt(6, p_instance);

                pstmtInsert.setString(7, nroCuenta);
                pstmtInsert.setString(8, cuenta);
                pstmtInsert.setBigDecimal(9, inicial);
                pstmtInsert.setBigDecimal(10, sumaDR);
                pstmtInsert.setBigDecimal(11, sumaCR);
                pstmtInsert.setBigDecimal(12, sfinal);
                pstmtInsert.setBigDecimal(13, Env.ZERO);

                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());

                pstmtInsert.close();

                secuencia++;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenerateBalance.class.getName()).log(Level.SEVERE, null, ex);
        }

        return secuencia;
    }

    protected void cabecera(int ad_client_id, int ad_org_id, int c_acctschema_id, String compania, String anio, int secuencia) {
        String sqlInsert = "";
        PreparedStatement pstmtInsert = null;
        String sqlRemove = "delete from T_BALANCE_HDR";
        DB.executeUpdate(sqlRemove, null);

        org = ad_org_id;

        try {
            sqlInsert = "insert into T_BALANCE_HDR values(?,?,?,?,'Y',?,?,?,?,?,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());

            pstmtInsert.setInt(1, secuencia);
            pstmtInsert.setInt(2, c_acctschema_id);
            pstmtInsert.setInt(3, ad_client_id);
            pstmtInsert.setInt(4, ad_org_id);

            pstmtInsert.setString(5, compania);
            pstmtInsert.setTimestamp(6, fromDate);
            pstmtInsert.setTimestamp(7, toDate);
            pstmtInsert.setString(8, anio);
            pstmtInsert.setInt(9, p_instance);
            pstmtInsert.setLong(10, num_hoja);
            pstmtInsert.setString(11, IsAjusteInflacion ? "Y" : "N");

            pstmtInsert.executeQuery();
            DB.commit(true, get_TrxName());
        } catch (SQLException ex) {
            Logger.getLogger(GeneratePagoCompRet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        
        for (int i = 0; i < para.length; i++)
        {
                String name = para[i].getParameterName();
                if (para[i].getParameter() == null)
                        ;
                else if (name.equals("C_AcctSchema_ID"))
                        schema =  (BigDecimal) para[i].getParameter();
                else if (name.equals("FECHAD")){
                        fromDate = (Timestamp) para[i].getParameter();
                        toDate = (Timestamp) para[i].getParameter_To();
                }
                else if (name.equals("PAGINA"))
                         num_hoja = ((BigDecimal) para[i].getParameter()).longValue();
                else if (name.equals("IsAjusteInflacion"))
                         IsAjusteInflacion =  "Y".equals(para[i].getParameter());
                else
                        log.log(Level.SEVERE, "Unknown Parameter: " + name);
        }

        if (fromDate == null && toDate == null) {
            Calendar cal = TimeUtil.getToday();
            fromDate = new Timestamp(cal.getTime().getYear(), 0, 1, 0, 0, 0, 0);
            toDate = new Timestamp(cal.getTime().getYear(), 11, 31, 0, 0, 0, 0);
        } else if (fromDate == null) {
            fromDate = new Timestamp(toDate.getYear(), 0, 1, 0, 0, 0, 0);
        } else if (toDate == null) {
            toDate = new Timestamp(fromDate.getYear(), 11, 31, 0, 0, 0, 0);
        }

        Env.getCtx().put("startPage", num_hoja);
        
        //Sin acento para evitar problemas de compatibilidad
         if (!IsAjusteInflacion)
            gl_category_id = getGLCategory("Inflacion");

        p_instance = getAD_PInstance_ID();
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
