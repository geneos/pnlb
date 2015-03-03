/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;
import com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt.GetOpt;
import java.sql.SQLException;
import org.compiere.process.*;
import java.math.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.logging.*;
import org.compiere.model.*;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *Esta clase inserta tuplas en la tabla temporal T_RET_GAN_PAGOS,T_RET_IB_PAGOS,T_RET_IVA_PAGOS,T_RET_SUSS_PAGOS luego de un previo filtrado por fecha,
 *tipo de retencion y calculos posteriores.
 * 
 * @author BISION - Mat�as Maenza
 * @version 1.0
 */
public class GenerateRetencionesPago extends SvrProcess{
    
    private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    private String tipoRet;
    int page;
    private static String[] meses={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    long org;
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++)
	{
            //String name = para[i].getParameterName();
            //if(name.equals("TIPO_RET"))
                
            //else{
                fromDate=(Timestamp)para[i].getParameter();
                toDate=(Timestamp)para[i].getParameter_To();
            //}
        }
            tipoRet="G";
            p_PInstance_ID = getAD_PInstance_ID(); 
            page = 1;
            //Env.getCtx().put("Delimitador", "");
    }

    protected String doIt() throws Exception {
        pie();
        cabecera();
        UtilProcess.initViewer(getViewer(),p_PInstance_ID,getProcessInfo());
        return "success";
    }
    
    protected void pie() throws Exception{
        String sqlQuery,sqlQuery2,sqlQuery3,sqlInsert="",sqlInsert2="",sqlInsert3="",descripcion="";
        PreparedStatement pstmtInsert = null,pstmtInsert2 = null,pstmtInsert3 = null;
        double importeRegimen=0,importeCliente=0,total=0;
        org=0;
        log.info("Comienzo del proceso de generacion de retenciones para pagos");
        log.info("borrado de la tabla temporal que corresponda(T_RETEN_GAN_PAGOS,T_RETEN_IB_PAGOS,T_RETEN_IVA_PAGOS,T_RETEN_SUSS_PAGOS)");
        String sqlRemove = "delete from "+getTabla();
        DB.executeUpdate(sqlRemove, null);DB.commit(true, get_TrxName());
                
        sqlQuery="select distinct cp.c_regimenganancias_v_id,ret.DESCRIPTION,cp.ad_client_id,cp.ad_org_id "+
                  "from c_payment cp "+
                  "join C_Withholding ret on (cp.c_regimenganancias_v_id = ret.REGIMENGANANCIAS) "+
                  "left join c_paymentret reten on (cp.C_PAYMENT_ID = reten.C_PAYMENT_ID) "+
                  "left join c_bpartner par on (cp.C_BPARTNER_ID = par.C_BPARTNER_ID) "+
                  "left join c_doctype doc on (doc.C_DOCTYPE_ID = reten.C_DOCTYPE_ID) "+
                  "where (cp.ISRECEIPT='N') and (cp.DOCSTATUS='CO' OR CP.DOCSTATUS='CL') and (cp.DATETRX between ? and ?) "+
                  "and (reten.tipo_ret=?) order by cp.c_regimenganancias_v_id";
        PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setTimestamp(2, toDate);
        pstmt.setString(3, tipoRet);
        ResultSet rs = pstmt.executeQuery();
        try {
        while (rs.next()) {
            // para las primeras l�neas
            descripcion="";
            if(rs.getString(2)!=null)
                descripcion = rs.getString(2);
            importeRegimen=0;
            sqlInsert = "insert into "+getTabla()+" values(?,?,'Y',?,?,null,null,null,null,null,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            pstmtInsert.setLong(1, rs.getLong(3));
            pstmtInsert.setLong(2, rs.getLong(4));
            pstmtInsert.setLong(3, p_PInstance_ID);
            pstmtInsert.setString(4, "R�gimen: "+rs.getString(1)+"/"+descripcion);
            pstmtInsert.setInt(5, page);
            pstmtInsert.setString(6, tipoRet);
            page+=1;org = rs.getLong(4);
            pstmtInsert.executeQuery();DB.commit(true, get_TrxName());
            // para las segundas l�neas
            sqlQuery2="select distinct par.c_bpartner_id,cp.ad_client_id,cp.ad_org_id,par.value,par.NAME,par.taxid "+
                  "from c_payment cp "+
                  "join C_Withholding ret on (cp.c_regimenganancias_v_id = ret.REGIMENGANANCIAS) "+
                  "left join c_paymentret reten on (cp.C_PAYMENT_ID = reten.C_PAYMENT_ID) "+
                  "left join c_bpartner par on (cp.C_BPARTNER_ID = par.C_BPARTNER_ID) "+
                  "left join c_doctype doc on (doc.C_DOCTYPE_ID = reten.C_DOCTYPE_ID) "+
                  "where (cp.ISRECEIPT='N') and (cp.DOCSTATUS='CO' OR CP.DOCSTATUS='CL') and (cp.DATETRX between ? and ?) "+
                  "and (reten.tipo_ret=?) and (cp.c_regimenganancias_v_id=?) order by par.value";
            PreparedStatement pstmt2 = DB.prepareStatement(sqlQuery2, get_TrxName());
            pstmt2.setTimestamp(1, fromDate);
            pstmt2.setTimestamp(2, toDate);
            pstmt2.setString(3, tipoRet);
            pstmt2.setString(4, rs.getString(1));
            ResultSet rs2 = pstmt2.executeQuery();
            try {
            while (rs2.next()) {
                sqlInsert2 = "insert into "+getTabla()+" values(?,?,'Y',?,?,?,null,null,null,null,?,?)";
                pstmtInsert2 = DB.prepareStatement(sqlInsert2, get_TrxName());
                pstmtInsert2.setLong(1, rs2.getLong(2));
                pstmtInsert2.setLong(2, rs2.getLong(3));
                pstmtInsert2.setLong(3, p_PInstance_ID);
                pstmtInsert2.setString(4, rs2.getString(4)+" "+rs2.getString(5));
                //para el cuit
                pstmtInsert2.setString(5, rs2.getString(6));
                pstmtInsert2.setInt(6, page);
                pstmtInsert2.setString(7, tipoRet);
                pstmtInsert2.executeQuery();DB.commit(true, get_TrxName());
                page+=1;
                importeCliente=0;
                // para las terceras lineas
                sqlQuery3="select cp.ad_client_id,cp.ad_org_id,cp.DATETRX,doc.PRINTNAME,reten.DOCUMENTNO,reten.IMPORTE,cp.PAYAMT "+
                  "from c_payment cp "+
                  "join C_Withholding ret on (cp.c_regimenganancias_v_id = ret.REGIMENGANANCIAS) "+
                  "left join c_paymentret reten on (cp.C_PAYMENT_ID = reten.C_PAYMENT_ID) "+
                  "left join c_bpartner par on (cp.C_BPARTNER_ID = par.C_BPARTNER_ID) "+
                  "left join c_doctype doc on (doc.C_DOCTYPE_ID = reten.C_DOCTYPE_ID) "+
                  "where (cp.ISRECEIPT='N') and (cp.DOCSTATUS='CO' OR CP.DOCSTATUS='CL') and (cp.DATETRX between ? and ?) "+
                  "and (reten.tipo_ret=?) and (cp.c_regimenganancias_v_id=?) and (par.c_bpartner_id=?) " +
                  "group by cp.c_regimenganancias_v_id,par.value,cp.DATETRX,cp.C_PAYMENT_ID,cp.ad_client_id,cp.ad_org_id,doc.PRINTNAME,reten.DOCUMENTNO,reten.IMPORTE,cp.PAYAMT " +
                  "order by cp.c_regimenganancias_v_id,par.value,cp.DATETRX,cp.C_PAYMENT_ID,cp.ad_client_id,cp.ad_org_id,doc.PRINTNAME,reten.DOCUMENTNO,reten.IMPORTE,cp.PAYAMT";
                PreparedStatement pstmt3 = DB.prepareStatement(sqlQuery3, get_TrxName());
                pstmt3.setTimestamp(1, fromDate);
                pstmt3.setTimestamp(2, toDate);
                pstmt3.setString(3, tipoRet);
                pstmt3.setString(4, rs.getString(1));
                pstmt3.setLong(5, rs2.getLong(1));
                ResultSet rs3 = pstmt3.executeQuery();
                try {
                while (rs3.next()) {
                    sqlInsert3 = "insert into "+getTabla()+" values(?,?,'Y',?,null,null,?,?,?,?,?,?)";
                    pstmtInsert3 = DB.prepareStatement(sqlInsert3, get_TrxName());
                    pstmtInsert3.setLong(1, rs3.getLong(1));
                    pstmtInsert3.setLong(2, rs3.getLong(2));
                    pstmtInsert3.setLong(3, p_PInstance_ID);
                    pstmtInsert3.setDate(4, new Date(rs3.getDate(3).getTime() + 1000));
                    // para el comprobante
                    pstmtInsert3.setString(5, rs3.getString(4).substring(0,3)+" "+rs3.getString(5));
                    pstmtInsert3.setString(6, getString(rs3.getDouble(7)));
                    pstmtInsert3.setString(7, getString(rs3.getDouble(6)));
                    importeCliente+=rs3.getDouble(6);
                    pstmtInsert3.setInt(8, page);
                    pstmtInsert3.setString(9, tipoRet);
                    pstmtInsert3.executeQuery();DB.commit(true, get_TrxName());
                    page+=1;
                }// fin while para terceras lineas
                // para las cuartas l�neas
                sqlInsert3 = "insert into "+getTabla()+" values(?,?,'Y',?,?,null,null,null,null,?,?,?)";
                pstmtInsert3 = DB.prepareStatement(sqlInsert3, get_TrxName());
                pstmtInsert3.setLong(1, getAD_Client_ID());
                pstmtInsert3.setLong(2, rs2.getLong(3));
                pstmtInsert3.setLong(3, p_PInstance_ID);
                pstmtInsert3.setString(4, "Total Cliente: "+rs2.getString(4)+" "+rs2.getString(5));
                pstmtInsert3.setString(5,getString(importeCliente));
                pstmtInsert3.setInt(6, page);
                pstmtInsert3.setString(7, tipoRet);
                pstmtInsert3.executeQuery();DB.commit(true, get_TrxName());
                importeRegimen+=importeCliente;
                page+=1;
                } catch (Exception exception) {
                   log.info("Se produjo un error en org.compiere.process.GenerateRetencionesPago " + exception.getMessage());
                   exception.printStackTrace();
                }                
            }   // fin while para las segundas lineas 
            // para las quintas l�neas
                sqlInsert3 = "insert into "+getTabla()+" values(?,?,'Y',?,?,null,null,null,null,?,?,?)";
                pstmtInsert3 = DB.prepareStatement(sqlInsert3, get_TrxName());
                pstmtInsert3.setLong(1, getAD_Client_ID());
                pstmtInsert3.setLong(2, org);
                pstmtInsert3.setLong(3, p_PInstance_ID);
                pstmtInsert3.setString(4, "Total R�gimen: "+rs.getString(1)+"/"+descripcion);
                pstmtInsert3.setString(5,getString(importeRegimen));
                pstmtInsert3.setInt(6, page);
                pstmtInsert3.setString(7, tipoRet);
                pstmtInsert3.executeQuery();DB.commit(true, get_TrxName());
                page+=1;
                total+=importeRegimen;
            } catch (Exception exception) {
               log.info("Se produjo un error en org.compiere.process.GenerateRetencionesPago " + exception.getMessage());
               exception.printStackTrace();
            }
        } // fin while para las primeras lineas
        // para el total
         sqlInsert3 = "insert into "+getTabla()+" values(?,?,'Y',?,?,null,null,null,null,?,?,?)";
         pstmtInsert3 = DB.prepareStatement(sqlInsert3, get_TrxName());
         pstmtInsert3.setLong(1, getAD_Client_ID());
         pstmtInsert3.setLong(2, org);
         pstmtInsert3.setLong(3, p_PInstance_ID);
         pstmtInsert3.setString(4, "Total General");
         pstmtInsert3.setString(5,getString(total));
         pstmtInsert3.setInt(6, page);
         pstmtInsert3.setString(7, tipoRet);
         pstmtInsert3.executeQuery();DB.commit(true, get_TrxName());
        } catch (Exception exception) {
           log.info("Se produjo un error en org.compiere.process.GenerateRetencionesPago " + exception.getMessage());
           exception.printStackTrace();
        }
    }
    
    protected void cabecera(){
        try {
                PreparedStatement pstmtInsert = null;
                String sqlInsert;
                log.info("borrado de la cabecera de la tabla temporal");
                String sqlRemove = "delete from " + getCabecera();
                DB.executeUpdate(sqlRemove, null);DB.commit(true, get_TrxName());
                try {
                    
                        sqlInsert = "insert into "+getCabecera()+" values(?,?,'Y',?,?,?,?,?,?)";
                        pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                        pstmtInsert.setLong(1, getAD_Client_ID());
                        pstmtInsert.setLong(2, org);
                        pstmtInsert.setLong(3, p_PInstance_ID);
                        pstmtInsert.setString(4, "PANALAB S.A.");
                        // para la fecha
                        java.util.Date fecha = new java.util.Date();
                        pstmtInsert.setString(5,fecha.getDate()+" de "+meses[fecha.getMonth()]+" "+(fecha.getYear()+1900));
                        pstmtInsert.setString(6, "Listado de Retenciones de "+getRetencion()+" Realizadas per�odo "+getFecha(fromDate)+" - "+getFecha(toDate));
                        pstmtInsert.setString(7, tipoRet);
                        pstmtInsert.setDate(8, new Date(fromDate.getTime() + 1000));
                        pstmtInsert.executeQuery();DB.commit(true, get_TrxName());
                    
                    } catch (Exception exception) {
                       log.info("Se produjo un error en org.compiere.process.GenerateRetencionesPago.Cabecera " + exception.getMessage());
                       exception.printStackTrace();
                    }
        } catch (SQLException ex) {
            Logger.getLogger(GenerateRetencionesCobranzas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getString (Double value){
     Locale.setDefault(Locale.GERMAN);
     DecimalFormat df = new DecimalFormat("###,###,##0.00");
     String coma=df.format(value);
     /*   
     coma=coma.replace(".", ",");
     if ( coma.length() > 6 ){
        coma=coma.replaceFirst(",", ".");
     }
     if ( coma.length() > 10 ){
        coma=coma.replaceFirst(",", ".");
     }*/
     return coma;
    
    }
    
    protected String getTabla(){
        if(tipoRet.equals("I")) return "T_RET_IVA_PAGOS";
        else if(tipoRet.equals("B")) return "T_RET_IB_PAGOS";
        else if(tipoRet.equals("S")) return "T_RET_SUSS_PAGOS";
        else return "T_RET_GAN_PAGOS";       
    }
    
    protected String getCabecera(){
        if(tipoRet.equals("I")) return "T_RET_IVA_PAGOS_CAB";
        else if(tipoRet.equals("B")) return "T_RET_IB_PAGOS_CAB";
        else if(tipoRet.equals("S")) return "T_RET_SUSS_PAGOS_CAB";
        else return "T_RET_GAN_PAGOS_CAB";       
    }
    
    protected String getRetencion(){
        if(tipoRet.equals("I")) return "IVA";
        else if(tipoRet.equals("B")) return "IB";
        else if(tipoRet.equals("S")) return "SUSS";
        else return "Ganancias"; 
    }
    
    protected String getFecha(Timestamp date){
        String fecha = "";
        if(date.getDate()<10) fecha = "0"+date.getDate(); else fecha += date.getDate();
        if(date.getMonth()+1 < 10)  fecha+= "/0"+(date.getMonth()+1); else fecha+= "/"+(date.getMonth()+1);
        fecha +="/"+(date.getYear()+1900);
        return fecha;
    }
    
    protected String getViewer(){
        if(tipoRet.equals("I")) return "Listado de Retenciones de IVA Realizadas";
        else if(tipoRet.equals("B")) return "Listado de Retenciones de IB Realizadas";
        else if(tipoRet.equals("S")) return "Listado de Retenciones de SUSS Realizadas";
        else return "Listado de Retenciones de Ganancias Realizadas";
    }
        
}
