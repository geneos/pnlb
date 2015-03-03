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
 *Esta clase inserta tuplas en la tabla temporal T_RET_IVA_SIAP luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
*	@author BISion - Santiago Ibañez
*	@version 1.0
 */
public class GenerateRetencionesIVASiap extends SvrProcess {
    
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
        log.info("Comienzo del proceso de generacion de la interface SIAP para retenciones de IVA");
        log.info("borrado de la tabla temporal T_RET_IVA_SIAP");
        String sqlRemove = "delete from T_RET_IVA_SIAP";
        DB.executeUpdate(sqlRemove, null);
        sqlQuery="select pr.ad_client_id, "+ //1- AD_CLIENT_ID
                 "pr.ad_org_id, "+           //2- AD_ORG_ID
                 "reg.nro_regim, "+          //3- NRO_REGIM
                 "cbp.taxid, "+              //4- CUIT
                 "pr.datetrx, "+             //5- FECHA TRX
                 "pr.documentno, "+          //6- CERTIFICADO
                 "pr.importe"+               //7- IMPORTE
                 " from c_paymentret pr"+
                 //INNER porque necesariamente tienen que tener tuplas correspondientes en C_PAYMENT
                 " inner join c_payment pay on (pr.c_payment_id = pay.c_payment_id)"+
                 //LEFT JOIN porque había cobranzas que no tenían asociado un C_BPARTNER
                 " left join c_bpartner cbp on (pay.c_bpartner_id = cbp.c_bpartner_id)"+
                 //LEFT JOIN porque hay campos en nro_regim que son nulos
                 " left join c_regim_reten_percep_recib reg on (pr.c_regim_reten_percep_recib_id = reg.c_regim_reten_percep_recib_id)"+
                 //el tipo de retencion debe ser IVA y la cobranza debe estar completa o cerrada
                 " where pr.tipo_ret = 'I' and (pay.docstatus='CL' or pay.docstatus ='CO')"+
                 //Como en la ventana cobranzas hay pagos y cobranzas se seleccionan solo las cobranzas
                 "and (pay.ISRECEIPT ='Y')"+
                 //la fecha debe estar entre el rango ingresado
                 " and (pr.datetrx BETWEEN ? and ?)";
        PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setTimestamp(2, toDate); 
        ResultSet rs = pstmt.executeQuery();
        try {
        while (rs.next()) {
            sqlInsert = "insert into T_RET_IVA_SIAP values(?,?,'Y',?,?,?,?,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            pstmtInsert.setLong(1, rs.getLong(1));                              //AD_CLIENT_ID
            pstmtInsert.setLong(2, rs.getLong(2));                              //AD_ORG_ID
            pstmtInsert.setLong(3, p_PInstance_ID);     
            if (rs.getString(3).length()<3)                                     //NRO_REGIM
                pstmtInsert.setString(4, getCeros(rs.getString(3),3));
            else
                pstmtInsert.setString(4, rs.getString(3));          
            pstmtInsert.setString(5, rs.getString(4));                          //CUIT
            pstmtInsert.setDate(6, new Date(rs.getDate(5).getTime() + 1000));   //FECHA TRX
            if (rs.getString(6).length()<16)                                    //CERTIFICADO
                pstmtInsert.setString(7, getCeros(rs.getString(6).replaceAll("-", ""),16));
            else
                pstmtInsert.setString(7, rs.getString(6).replaceAll("-", "").substring(0,16));                          
            pstmtInsert.setString(8, getNumero(rs.getFloat(7),16));             //IMPORTE
         
            pstmtInsert.executeQuery();
            DB.commit(true, get_TrxName());
        }
        } catch (Exception exception) {
           log.info("Se produjo un error en org.compiere.process.GenerateRetencionesIVASiap " + exception.getMessage());
           exception.printStackTrace();
        }
        UtilProcess.initViewer("Interface SIAp Retenciones IVA",p_PInstance_ID,getProcessInfo());
        return "success";
    }
    
    // modificado - BiSion - Matías Maenza 16/05/08
    protected String getNumero(float number, int longitud){
        // para que el muestre de la forma 1234,00 y no 1234.00
        Locale.setDefault(Locale.GERMAN);
        DecimalFormat df = new DecimalFormat("#######.00");
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