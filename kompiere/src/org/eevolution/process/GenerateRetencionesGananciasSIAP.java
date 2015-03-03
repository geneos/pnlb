
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
 *Esta clase inserta tuplas en la tabla temporal T_RET_GANACIAS_SIAP luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
*	@author BISion
*	@version 1.0
 */
public class GenerateRetencionesGananciasSIAP extends SvrProcess {
    
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
        log.info("Comienzo del proceso de generacion de la interface SIAP para retenciones a las ganancias");
        log.info("borrado de la tabla temporal T_RET_GANACIAS_SIAP");
        String sqlRemove = "delete from T_RET_GANACIAS_SIAP";
        DB.executeUpdate(sqlRemove, null);
        
        sqlQuery="SELECT pay.ad_client_id,pay.ad_org_id,pay.DATETRX,cp.taxid,ret.DOCUMENTNO,ret.DATETRX,ret.IMPORTE,ret.tipo_ret,reten.NRO_REGIM" +
            " from C_PAYMENT pay "+
            "join C_PAYMENTRET ret on (ret.c_payment_id = pay.c_payment_id) "+
            "left join c_regim_reten_percep_recib reten on (reten.C_REGIM_RETEN_PERCEP_RECIB_ID = ret.C_REGIM_RETEN_PERCEP_RECIB_ID) "+
            "left join C_BPartner cp on(pay.c_bpartner_id = cp.c_bpartner_id) "+
            "where ret.TIPO_RET='G' and (pay.DOCSTATUS='CO' or pay.DOCSTATUS='CL') and pay.ISRECEIPT='Y' and "+
            "(ret.DATETRX between ? and ?)";
        PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setTimestamp(2, toDate);
        ResultSet rs = pstmt.executeQuery();
        try {
        while (rs.next()) {
            sqlInsert = "insert into T_RET_GANACIAS_SIAP values(?,?,'Y',?,?,?,?,?,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            pstmtInsert.setLong(1, rs.getLong(1));
            pstmtInsert.setLong(2, rs.getLong(2));
            pstmtInsert.setLong(3, p_PInstance_ID);
            // para la fecha
            String fecha = "";
            if(rs.getDate(6).getDate()<10) fecha = "0"+rs.getDate(6).getDate(); else fecha += rs.getDate(6).getDate();
            if(rs.getDate(6).getMonth()+1 < 10)  fecha+= "0"+(rs.getDate(6).getMonth()+1); else fecha+= (rs.getDate(6).getMonth()+1);
            fecha +=(rs.getDate(6).getYear()+1900);
            pstmtInsert.setString(4, fecha);            
            // PARA EL CUIT
            cuit= rs.getString(4);
            cuit= cuit.replaceAll("-", "");
            pstmtInsert.setString(5, cuit);
            
            pstmtInsert.setString(6, getCeros(rs.getString(5),8));
            /**modificacion para tomar el codigo de regimen de la ventana Regim Reten/Percep Recib
            actualmente se pide q sea numerico de 3 pero en la ventana esta como alfanumerico
             se pedira un nuevo campo en la ventana y se debera modificar esto
            **/
            
            pstmtInsert.setString(7, String.valueOf(rs.getLong(9)));
            
            //para el importe
            pstmtInsert.setString(8, getNumero(rs.getFloat(7),11));
            pstmtInsert.setDate(9, rs.getDate(6));
            pstmtInsert.executeQuery();
            DB.commit(true, get_TrxName());
        }
        } catch (Exception exception) {
           log.info("Se produjo un error en org.compiere.process.GenerateRetencionesGananciasSIAP " + exception.getMessage());
           exception.printStackTrace();
        }
        UtilProcess.initViewer("Interface SIAp Retenciones Ganancias",p_PInstance_ID,getProcessInfo());
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
        if(longi<0)
            return (numero.substring(numero.length()-length, numero.length()));
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
