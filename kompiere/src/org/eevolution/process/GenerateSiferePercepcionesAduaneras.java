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
*Esta clase inserta tuplas en la tabla temporal T_PERCEPCIONES_SIFERE luego de un previo filtrado por fecha 
*  y calculos posteriores.
*
*	@author BISion
*	@version 1.0
**/

public class GenerateSiferePercepcionesAduaneras extends SvrProcess{

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
        String sqlQuery,sqlInsert="",nro="",cuit,aux;
        PreparedStatement pstmtInsert = null;
        log.info("Comienzo del proceso de percepciones aduaneras para la interface sifere");
        log.info("borrado de la tabla temporal T_PERCEP_ADUANERAS_SIFERE");
        String sqlRemove = "delete from T_PERCEP_ADUANERAS_SIFERE";
        DB.executeUpdate(sqlRemove, null);
        
        sqlQuery="select fac.ad_client_id,fac.ad_org_id,line.CODIGO_JURISDICCION,par.taxid,fac.dateinvoiced,fac.DOCUMENTNO,line.LINENETAMT "+
                "from c_invoice fac "+
                "left join c_invoiceline line on (line.c_invoice_id = fac.c_invoice_id) "+
                "left join c_bpartner par on (fac.C_BPARTNER_ID = par.C_BPARTNER_ID) "+
                "join c_charge cha on (cha.c_charge_id = line.c_charge_id) "+
                "where (fac.DOCSTATUS='CO' OR fac.DOCSTATUS='CL') and (fac.DATEINVOICED between ? and ?) "+
                "and (fac.ISSOTRX='N') and (cha.istax='Y') and (cha.TAXTYPE='IIB') and (cha.ISPERCEPADUANERA='Y') order by fac.dateinvoiced";
        PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setTimestamp(2, toDate);
        ResultSet rs = pstmt.executeQuery();
        try {
        while (rs.next()) {
            sqlInsert = "insert into T_PERCEP_ADUANERAS_SIFERE values(?,?,'Y',?,?,?,?,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            pstmtInsert.setLong(1, rs.getLong(1));
            pstmtInsert.setLong(2, rs.getLong(2));
            pstmtInsert.setLong(3, p_PInstance_ID);
            //para el codigo jurisdiccion
            aux = ""+rs.getInt(3);
            pstmtInsert.setString(4, getCeros(aux,3));
            //  para el cuit
            pstmtInsert.setString(5, rs.getString(4));
            pstmtInsert.setDate(6, new Date(rs.getDate(5).getTime() + 1000));
            // para el numero de despacho aduanero
            nro = rs.getString(6).substring(5);
            pstmtInsert.setString(7, getCeros(nro,20));
            pstmtInsert.setString(8, getNumero(rs.getFloat(7),11));            
            pstmtInsert.executeQuery();
            DB.commit(true, get_TrxName());
        }
        } catch (Exception exception) {
           log.info("Se produjo un error en org.compiere.process.GenerateSiferePercepcionesAduaneras " + exception.getMessage());
           exception.printStackTrace();
        }
        UtilProcess.initViewer("Interface Sifere Percepciones Aduaneras",p_PInstance_ID,getProcessInfo());
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
}
