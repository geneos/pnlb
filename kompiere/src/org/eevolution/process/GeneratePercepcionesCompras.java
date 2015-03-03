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
import java.util.logging.*;
import org.compiere.model.*;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *Esta clase inserta tuplas en la tabla temporal T_RET_GAN_COBRANZAS,T_RET_IB_COBRANZAS,T_RET_IVA_COBRANZAS,T_RET_SUSS_COBRANZAS luego de un previo filtrado por fecha,
 *tipo de retencion y calculos posteriores.
 * 
 * @author BISION - Matías Maenza
 * @version 1.0
 */
public class GeneratePercepcionesCompras extends SvrProcess{
    
    private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    private String tipoPercep;
    int page;
    private static String[] meses={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    long org;
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++)
	{
            String name = para[i].getParameterName();
            if(name.equals("TIPO_PERCEP"))
                tipoPercep= (String) para[i].getParameter();
            else{
                fromDate=(Timestamp)para[i].getParameter();
                toDate=(Timestamp)para[i].getParameter_To();
            }
        }
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
       
        String importe="";
        String sqlQuery,sqlQuery2,sqlQuery3,sqlInsert="",sqlInsert2="",sqlInsert3="";
        PreparedStatement pstmtInsert = null,pstmtInsert2 = null,pstmtInsert3 = null;
        double importeRegimen=0,importeCliente=0,total=0;
        org=0;
        log.info("Comienzo del proceso de generacion de retenciones para cobranza");
        log.info("borrado de la tabla temporal que corresponda(T_PERCEP_GAN_COMPRAS,T_PERCEP_IIB_COMPRAS,T_PERCEP_IVA_COMPRAS)");
        String sqlRemove = "delete from "+getTabla();
        DB.executeUpdate(sqlRemove, null);DB.commit(true, get_TrxName());
                
        sqlQuery="select distinct reten.CODIGO_REGIMEN,reten.DESCRIPCION,fac.ad_client_id,fac.ad_org_id "+
                "from c_invoice fac "+
                "left join c_invoiceline line on (line.c_invoice_id = fac.c_invoice_id) "+
                "left join c_bpartner par on (fac.C_BPARTNER_ID = par.C_BPARTNER_ID) "+
                "left join c_doctype doc on (doc.C_DOCTYPE_ID = fac.C_DOCTYPETARGET_ID) "+
                "join c_regim_reten_percep_recib reten on (reten.C_REGIM_RETEN_PERCEP_RECIB_ID = line.C_REGIM_RETEN_PERCEP_RECIB_ID) "+
                "where (fac.DOCSTATUS='CO' OR fac.DOCSTATUS='CL') and (fac.DATEINVOICED between ? and ?) "+
                "and (fac.ISSOTRX='N') and (reten.tipo_percep=?) order by reten.CODIGO_REGIMEN";
        PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setTimestamp(2, toDate);
        pstmt.setString(3, tipoPercep);
        ResultSet rs = pstmt.executeQuery();
        try {
        while (rs.next()) {
            // para las primeras líneas
            importeRegimen=0;
            sqlInsert = "insert into "+getTabla()+" values(?,?,'Y',?,?,null,null,null,null,null,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            pstmtInsert.setLong(1, rs.getLong(3));
            pstmtInsert.setLong(2, rs.getLong(4));
            pstmtInsert.setLong(3, p_PInstance_ID);
            pstmtInsert.setString(4, "Régimen: "+rs.getString(1)+"/"+rs.getString(2));
            pstmtInsert.setInt(5, page);
            pstmtInsert.setString(6, tipoPercep);
            page+=1;org = rs.getLong(4);
            pstmtInsert.executeQuery();DB.commit(true, get_TrxName());
            // para las segundas líneas
            sqlQuery2="select distinct par.c_bpartner_id,fac.ad_client_id,fac.ad_org_id,par.value,par.NAME,par.taxid "+
                    "from c_invoice fac "+
                    "left join c_invoiceline line on (line.c_invoice_id = fac.c_invoice_id) "+
                    "left join c_bpartner par on (fac.C_BPARTNER_ID = par.C_BPARTNER_ID) "+
                    "left join c_doctype doc on (doc.C_DOCTYPE_ID = fac.C_DOCTYPETARGET_ID) "+
                    "join c_regim_reten_percep_recib reten on (reten.C_REGIM_RETEN_PERCEP_RECIB_ID = line.C_REGIM_RETEN_PERCEP_RECIB_ID) "+
                    "where (fac.DOCSTATUS='CO' OR fac.DOCSTATUS='CL') and (fac.DATEINVOICED between ? and ?) "+
                    "and (fac.ISSOTRX='N') and (reten.tipo_percep=?) and (reten.CODIGO_REGIMEN=?) order by par.value";
            PreparedStatement pstmt2 = DB.prepareStatement(sqlQuery2, get_TrxName());
            pstmt2.setTimestamp(1, fromDate);
            pstmt2.setTimestamp(2, toDate);
            pstmt2.setString(3, tipoPercep);
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
                pstmtInsert2.setString(7, tipoPercep);
                pstmtInsert2.executeQuery();DB.commit(true, get_TrxName());
                page+=1;
                importeCliente=0;
                // para las terceras lineas
                sqlQuery3="select fac.ad_client_id,fac.ad_org_id,fac.dateinvoiced,doc.PRINTNAME,fac.DOCUMENTNO,line.LINENETAMT "+
                        "from c_invoice fac "+
                        "left join c_invoiceline line on (line.c_invoice_id = fac.c_invoice_id) "+
                        "left join c_bpartner par on (fac.C_BPARTNER_ID = par.C_BPARTNER_ID) "+
                        "left join c_doctype doc on (doc.C_DOCTYPE_ID = fac.C_DOCTYPETARGET_ID) "+
                        "join c_regim_reten_percep_recib reten on (reten.C_REGIM_RETEN_PERCEP_RECIB_ID = line.C_REGIM_RETEN_PERCEP_RECIB_ID) "+
                        "where (fac.DOCSTATUS='CO' OR fac.DOCSTATUS='CL') and (fac.DATEINVOICED between ? and ?) "+
                        "and (fac.ISSOTRX='N') and (reten.tipo_percep=?) and (reten.CODIGO_REGIMEN=?) and (par.c_bpartner_id=?) order by reten.CODIGO_REGIMEN,fac.dateinvoiced";
                PreparedStatement pstmt3 = DB.prepareStatement(sqlQuery3, get_TrxName());
                pstmt3.setTimestamp(1, fromDate);
                pstmt3.setTimestamp(2, toDate);
                pstmt3.setString(3, tipoPercep);
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
                   
                    //para la base imponible
                    pstmtInsert3.setString(6,getString(rs3.getDouble(6)) );
                    
                    
                    pstmtInsert3.setString(7,getString(rs3.getDouble(6)) );
                    
                    importeCliente+=rs3.getDouble(6);
                    pstmtInsert3.setInt(8, page);
                    pstmtInsert3.setString(9, tipoPercep);
                    pstmtInsert3.executeQuery();DB.commit(true, get_TrxName());
                    page+=1;
                }// fin while para terceras lineas
                // para las cuartas líneas
                sqlInsert3 = "insert into "+getTabla()+" values(?,?,'Y',?,?,null,null,null,null,?,?,?)";
                pstmtInsert3 = DB.prepareStatement(sqlInsert3, get_TrxName());
                pstmtInsert3.setLong(1, getAD_Client_ID());
                pstmtInsert3.setLong(2, rs2.getLong(3));
                pstmtInsert3.setLong(3, p_PInstance_ID);
                pstmtInsert3.setString(4, "Total Cliente: "+rs2.getString(4)+" "+rs2.getString(5));
                
                pstmtInsert3.setString(5,getString(importeCliente));
                
                pstmtInsert3.setInt(6, page);
                pstmtInsert3.setString(7, tipoPercep);
                pstmtInsert3.executeQuery();DB.commit(true, get_TrxName());
                importeRegimen+=importeCliente;
                page+=1;
                } catch (Exception exception) {
                   log.info("Se produjo un error en org.compiere.process.GeneratePercepcionesCompras " + exception.getMessage());
                   exception.printStackTrace();
                }                
            }   // fin while para las segundas lineas 
            // para las quintas líneas
                sqlInsert3 = "insert into "+getTabla()+" values(?,?,'Y',?,?,null,null,null,null,?,?,?)";
                pstmtInsert3 = DB.prepareStatement(sqlInsert3, get_TrxName());
                pstmtInsert3.setLong(1, getAD_Client_ID());
                pstmtInsert3.setLong(2, org);
                pstmtInsert3.setLong(3, p_PInstance_ID);
                pstmtInsert3.setString(4, "Total Régimen: "+rs.getString(1)+"/"+rs.getString(2));
                
                
              
                pstmtInsert3.setString(5,getString(importeRegimen));
                pstmtInsert3.setInt(6, page);
                pstmtInsert3.setString(7, tipoPercep);
                pstmtInsert3.executeQuery();DB.commit(true, get_TrxName());
                page+=1;
                total+=importeRegimen;
            } catch (Exception exception) {
               log.info("Se produjo un error en org.compiere.process.GeneratePercepcionesCompras " + exception.getMessage());
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
         pstmtInsert3.setString(7, tipoPercep);
         pstmtInsert3.executeQuery();DB.commit(true, get_TrxName());
        } catch (Exception exception) {
           log.info("Se produjo un error en org.compiere.process.GeneratePercepcionesCompras " + exception.getMessage());
           exception.printStackTrace();
        }
    }
    
    public String getString (Double value){
     DecimalFormat df = new DecimalFormat("###,###,##0.00");
     String coma=df.format(value);
        
     coma=coma.replace(".", ",");
     if ( coma.length() > 6 ){
        coma=coma.replaceFirst(",", ".");
     }
     if ( coma.length() > 10 ){
        coma=coma.replaceFirst(",", ".");
     }
     return coma;
    
    }
    
    protected void cabecera(){
        try {
                PreparedStatement pstmtInsert = null;
                String sqlInsert;
                log.info("borrado de la cabecera de la tabla temporal");
                String sqlRemove = "delete from " + getCabecera();
                DB.executeUpdate(sqlRemove, null);DB.commit(true, get_TrxName());
                String sqlQuery="select value from ad_client where AD_CLIENT_ID=?";
                PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
                pstmt.setLong(1, getAD_Client_ID());
                ResultSet rs = pstmt.executeQuery();
                try {
                    if (rs.next()) {
                        sqlInsert = "insert into "+getCabecera()+" values(?,?,'Y',?,?,?,?,?,?)";
                        pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                        pstmtInsert.setLong(1, getAD_Client_ID());
                        pstmtInsert.setLong(2, org);
                        pstmtInsert.setLong(3, p_PInstance_ID);
                        pstmtInsert.setString(4, rs.getString(1));
                        // para la fecha
                        java.util.Date fecha = new java.util.Date();
                        pstmtInsert.setString(5,fecha.getDate()+" de "+meses[fecha.getMonth()]+" "+(fecha.getYear()+1900));
                        pstmtInsert.setString(6, "Listado de Percepciones de "+getRetencion()+" período "+getFecha(fromDate)+" - "+getFecha(toDate));
                        pstmtInsert.setString(7, tipoPercep);
                        pstmtInsert.setDate(8, new Date(fromDate.getTime() + 1000));
                        pstmtInsert.executeQuery();DB.commit(true, get_TrxName());
                    }
                    } catch (Exception exception) {
                       log.info("Se produjo un error en org.compiere.process.GeneratePercepcionesCompras.Cabecera " + exception.getMessage());
                       exception.printStackTrace();
                    }
        } catch (SQLException ex) {
            Logger.getLogger(GenerateRetencionesCobranzas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected String getTabla(){
        if(tipoPercep.equals("IVA")) return "T_PERCEP_IVA_COMPRAS";
        else if(tipoPercep.equals("IIB")) return "T_PERCEP_IIB_COMPRAS";
        else return "T_PERCEP_GAN_COMPRAS";       
    }
    
    protected String getCabecera(){
        if(tipoPercep.equals("IVA")) return "T_PERCEP_IVA_COMPRAS_CAB";
        else if(tipoPercep.equals("IIB")) return "T_PERCEP_IIB_COMPRAS_CAB";
        else return "T_PERCEP_GAN_COMPRAS_CAB";       
    }
    
    protected String getRetencion(){
        if(tipoPercep.equals("IVA")) return "IVA";
        else if(tipoPercep.equals("IIB")) return "IB";
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
        if(tipoPercep.equals("IVA")) return "Listado de Percepciones de IVA Recibidas";
        else if(tipoPercep.equals("IIB")) return "Listado de Percepciones de IB Recibidas";
        else return "Listado de Percepciones de Ganancias Recibidas";
    }
       
}
