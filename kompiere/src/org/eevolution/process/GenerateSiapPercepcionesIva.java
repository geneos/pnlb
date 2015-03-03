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
*Esta clase inserta tuplas en la tabla temporal T_SIAP_PERCEP_IVA luego de un previo filtrado por fecha 
*  y calculos posteriores.
*
*	@author BISion - Matías Maenza
*	@version 1.0
**/

public class GenerateSiapPercepcionesIva extends SvrProcess{

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
        String sqlQuery,sqlInsert="",aux;
        PreparedStatement pstmtInsert = null;
        log.info("Comienzo del proceso de generacion de archivo para aplicativo Siap de percepciones Iva");
        log.info("borrado de la tabla temporal T_SIAP_PERCEP_IVA");
        String sqlRemove = "delete from T_SIAP_PERCEP_IVA";
        DB.executeUpdate(sqlRemove, null);
        
        sqlQuery="select fac.ad_client_id,fac.ad_org_id,socio.taxid,fac.dateinvoiced,fac.documentno,line.LINENETAMT,reten.CODIGO_REGIMEN "+
                "from c_invoice fac "+
                "left join c_invoiceline line on (fac.c_invoice_id = line.c_invoice_id) "+
                "left join c_bpartner socio on (fac.c_bpartner_id = socio.c_bpartner_id) "+
                "left join c_regim_reten_percep_recib reten on (reten.C_REGIM_RETEN_PERCEP_RECIB_ID = line.C_REGIM_RETEN_PERCEP_RECIB_ID) "+
                "join c_charge cha on (line.c_charge_id = cha.c_charge_id) "+
                "where (fac.isSOTrx='N') and (fac.docstatus='CL' or fac.docstatus='CO')  "+
                "and (cha.taxtype='IVA') and (cha.istax='Y') and (fac.dateinvoiced between ? and ?) order by fac.dateinvoiced";
        PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setTimestamp(2, toDate);
        ResultSet rs = pstmt.executeQuery();
        try {
        while (rs.next()) {
            sqlInsert = "insert into T_SIAP_PERCEP_IVA values(?,?,'Y',?,?,?,?,?,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            pstmtInsert.setLong(1, rs.getLong(1));
            pstmtInsert.setLong(2, rs.getLong(2));
            pstmtInsert.setLong(3, p_PInstance_ID);
            pstmtInsert.setString(4, "767");
            pstmtInsert.setString(5, rs.getString(3));
            pstmtInsert.setDate(6, new Date(rs.getDate(4).getTime() + 1000));
            pstmtInsert.setString(7, getCeros(rs.getString(5).substring(0, 4),8));
            pstmtInsert.setString(8, getCeros(rs.getString(5).substring(5),8));
            pstmtInsert.setString(9, getNumero(rs.getFloat(6),16));            
            pstmtInsert.executeQuery();
            DB.commit(true, get_TrxName());
        }
        } catch (Exception exception) {
           log.info("Se produjo un error en org.compiere.process.GenerateSiapPercepcionesIva " + exception.getMessage());
           exception.printStackTrace();
        }
        UtilProcess.initViewer("Interface SIAp Percepciones IVA",p_PInstance_ID,getProcessInfo());
        return "success";
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
    
   
   // modificado - BiSion - Matías Maenza 16/05/08
    protected String getNumero(float number, int longitud){
        // para que el muestre de la forma 1234,00 y no 1234.00
        Locale.setDefault(Locale.GERMAN);
        DecimalFormat df = new DecimalFormat("#############.00");
        String nro="";
        String aux = df.format(Math.abs(number));
        nro = getCeros(aux,longitud);
        if(number < 0){
            nro = nro.substring(1);
            return ("-"+nro);
        }
        return nro;
    }
}