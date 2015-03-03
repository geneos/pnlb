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
 *Esta clase inserta tuplas en la tabla temporal T_RET_IB_SIAP luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
*	@author BISion - Matías Maenza
*	@version 1.0
 */
public class GenerateRetencionesIBSiap extends SvrProcess {
    
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
        log.info("Comienzo del proceso de generacion de la interface SIAP para retenciones a IB");
        log.info("borrado de la tabla temporal T_RET_IB_SIAP");
        String sqlRemove = "delete from T_RET_IB_SIAP";
        DB.executeUpdate(sqlRemove, null);
        
        sqlQuery="SELECT pay.ad_client_id,pay.ad_org_id,cp.taxid,ret.DATETRX,ret.DOCUMENTNO,ret.IMPORTE,ret.tipo_ret from C_PAYMENT pay "+
                 "join C_PAYMENTRET ret on (ret.c_payment_id = pay.c_payment_id) "+
                 "left join C_BPartner cp on(pay.c_bpartner_id = cp.c_bpartner_id) "+
                 "where ret.TIPO_RET='B' and (pay.DOCSTATUS='CO' or pay.DOCSTATUS='CL') and pay.ISRECEIPT='N' and "+
                 "(ret.DATETRX between ? and ?)";
        PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setTimestamp(2, toDate);
        ResultSet rs = pstmt.executeQuery();
        try {
        while (rs.next()) {
            sqlInsert = "insert into T_RET_IB_SIAP values(?,?,'Y',?,?,?,?,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            pstmtInsert.setLong(1, rs.getLong(1));
            pstmtInsert.setLong(2, rs.getLong(2));
            pstmtInsert.setLong(3, p_PInstance_ID);
            pstmtInsert.setString(4, rs.getString(3));            
            pstmtInsert.setDate(5, new Date(rs.getDate(4).getTime() + 1000));
            pstmtInsert.setString(6, "0001");
            if(rs.getString(5).length()>8)
                pstmtInsert.setString(7, rs.getString(5).substring(0, 8));
            else
                pstmtInsert.setString(7, getCeros(rs.getString(5),8));
            //para el importe
            pstmtInsert.setString(8, getNumero(rs.getFloat(6),10));
            
            pstmtInsert.executeQuery();
            DB.commit(true, get_TrxName());
        }
        } catch (Exception exception) {
           log.info("Se produjo un error en org.compiere.process.GenerateRetencionesIBSiap " + exception.getMessage());
           exception.printStackTrace();
        }
        UtilProcess.initViewer("Interface SIAp Retenciones IB",p_PInstance_ID,getProcessInfo());
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