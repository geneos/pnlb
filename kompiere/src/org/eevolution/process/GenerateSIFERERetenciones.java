/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

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
 *
 * @author Nadia
 */
public class GenerateSIFERERetenciones extends SvrProcess {
    private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    
     protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++)
	{
            String name = para[i].getParameterName();
            fromDate=(Timestamp)para[i].getParameter();
            toDate=(Timestamp)para[i].getParameter_To();
        }
            p_PInstance_ID = getAD_PInstance_ID();     
            Env.getCtx().put("Delimitador", "");
    }  
    protected String doIt() throws Exception {
        String sqlQuery,sqlInsert="",nro="",cuit;
        PreparedStatement pstmtInsert = null;
        log.info("Comienzo del proceso de generacion del proceso de retenciones SIFERE ");
        log.info("borrado de la tabla temporal T_RET_IB_SIFERE");
        String sqlRemove = "delete from T_RET_IB_SIFERE";
        DB.executeUpdate(sqlRemove, null);
        
        sqlQuery="SELECT pay.ad_client_id,pay.ad_org_id,cj.value,cbp.TAXID,ret.DATETRX,ret.DOCUMENTNO,pay.DOCUMENTNO,ret.IMPORTE " +
                "from C_PAYMENT pay " +
                "join C_PAYMENTRET ret on (ret.c_payment_id = pay.c_payment_id) " +
                "left join C_REGIM_RETEN_PERCEP_RECIB rp on (rp.C_REGIM_RETEN_PERCEP_RECIB_ID=ret.C_REGIM_RETEN_PERCEP_RECIB_ID) " +
                "left join T_CODIGOJURISDICCION cj on (cj.T_CODIGOJURISDICCION_id=rp.t_codigojurisdiccion_id) " +
                "left join C_BPARTNER cbp on(pay.c_bpartner_id = cbp.c_bpartner_id) " +
                "where ret.TIPO_RET='B' and (pay.DOCSTATUS='CO' or pay.DOCSTATUS='CL') and pay.C_DOCTYPE_ID=1000137 and " +
                "(ret.DATETRX between ? and ?)";
        
        
        PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setTimestamp(2, toDate);
        ResultSet rs = pstmt.executeQuery();
        try {
          while (rs.next()) {
            sqlInsert = "insert into T_RET_IB_SIFERE values(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            pstmtInsert.setLong(1, rs.getLong(1));
            pstmtInsert.setLong(2, rs.getLong(2));
            pstmtInsert.setLong(3, p_PInstance_ID);
            
            
            //codigo jurisdiccion
            pstmtInsert.setString(4, (getCeros(rs.getString(3),3) != null) ? getCeros(rs.getString(3),3):"   "); 
             
            
           
            // PARA EL CUIT
            pstmtInsert.setString(5, rs.getString(4));
            //FECHA RETENCION
            pstmtInsert.setDate(6, rs.getDate(5)); 
            
            //NRO SUCURSAL
            pstmtInsert.setString(7, "0001");
            //NRO CONSTANCIA falta verificar -
            String constancia = rs.getString(6).replaceAll("-", "");
            pstmtInsert.setString(8, getCeros(constancia,16));
            
            //tipo comprobante
            pstmtInsert.setString(9,"R");
            //letra del comprobante
            pstmtInsert.setString(10," ");
            //Número de Comprobante Original falta controlar -
            String numero=rs.getString(7).replaceAll("-", "");
            pstmtInsert.setString(11,getCeros(numero,20));
            
            //para el importe
            pstmtInsert.setString(12, getNumero(rs.getFloat(8),11));
            
            
            pstmtInsert.setDate(13, new Date(fromDate.getTime()+1000));
            
            pstmtInsert.executeQuery();
            DB.commit(true, get_TrxName());
        }
        } catch (Exception exception) {
           log.info("Se produjo un error en org.compiere.process.GenerateSIFERERetenciones " + exception.getMessage());
           exception.printStackTrace();
        }
       UtilProcess.initViewer("Interface Sifere Retenciones",p_PInstance_ID,getProcessInfo());
        return "success";
    }
    // modificado - BiSion - Matías Maenza 16/05/08
    protected String getNumero(float number, int longitud){
        // para que el muestre de la forma 1234,00 y no 1234.00
        Locale.setDefault(Locale.GERMAN);
        DecimalFormat df = new DecimalFormat("########.00");
        String nro="";
        String aux = df.format(Math.abs(number));
        nro = getCeros(aux,longitud);
        if(number < 0){
            nro = nro.substring(1);
            return ("-"+nro);
        }
        return nro;
    }
    
    protected String getCeros(String numero,int length){
        int cant=0;
        if(numero!=null)
            cant = numero.length();
        String nro="";
        int longi = length-cant;            
        while(longi!=0){
              nro+="0";
              longi-=1;
        }
        if (cant == 0)
            return nro;
        else
            return (nro+numero);
    }
}
