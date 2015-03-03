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
 *Esta clase inserta tuplas en la tabla temporal T_PERCEPCIONES_SIFERE luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
*	@author BISion
*	@version 1.0
**/

public class GenerateInterfaceSIFEREPercepciones extends SvrProcess{

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
        log.info("Comienzo del proceso de percepciones para la interface sifere");
        log.info("borrado de la tabla temporal T_PERCEPCIONES_SIFERE");
        String sqlRemove = "delete from T_PERCEPCIONES_SIFERE";
        DB.executeUpdate(sqlRemove, null);
        sqlQuery="select fac.ad_client_id,fac.ad_org_id,ret.T_CODIGOJURISDICCION_ID,cp.taxid,fac.dateinvoiced,fac.DOCUMENTNO,fac.C_DOCTYPETARGET_ID,line.LINENETAMT " +
                "from c_invoice fac  " +
                "left join C_BPartner cp on (fac.c_bpartner_id = cp.c_bpartner_id) " +
                "left join C_BPartner_Location loc on(loc.c_bpartner_id = cp.c_bpartner_id)  " +
                "join c_invoiceline line on (line.c_invoice_id = fac.c_invoice_id)  " +
                "join c_charge cha on (line.C_CHARGE_ID = cha.C_CHARGE_ID)  " +
                "left join c_regim_reten_percep_recib ret on (ret.c_regim_reten_percep_recib_id=line.c_regim_reten_percep_recib_id ) " +
                "where (fac.DOCSTATUS='CO' or fac.DOCSTATUS='CL') and fac.ISSOTRX='N' and (fac.dateinvoiced between ? and ?) and  " +
                "(line.C_CHARGE_ID is not null) and (cha.TAXTYPE='IIB') and cha.ISTAX='Y'"; 
        PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setTimestamp(2, toDate);
        ResultSet rs = pstmt.executeQuery();
        try {
        while (rs.next()) {
            sqlInsert = "insert into T_PERCEPCIONES_SIFERE values(?,?,'Y',?,?,?,?,?,?,?,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            pstmtInsert.setLong(1, rs.getLong(1));
            pstmtInsert.setLong(2, rs.getLong(2));
            pstmtInsert.setLong(3, p_PInstance_ID);
            //para el codigo jurisdiccion
            String aux = ""+rs.getInt(3);
            pstmtInsert.setString(4, getCeros(aux,3));
            //  para el cuit
            pstmtInsert.setString(5, rs.getString(4));
            pstmtInsert.setDate(6, new Date(rs.getDate(5).getTime() + 1000));
            pstmtInsert.setString(7, "0001");
            // para el nro de cosntancia
            String constancia = rs.getString(6);
            constancia = constancia.substring(5);
            pstmtInsert.setString(8, constancia);
            pstmtInsert.setString(9, getComprobante(rs.getLong(7)));
            pstmtInsert.setString(10, getLetra(rs.getLong(7)));
            pstmtInsert.setString(11, getNumero(rs.getFloat(8),11));            
            pstmtInsert.executeQuery();
            DB.commit(true, get_TrxName());
        }
        } catch (Exception exception) {
           log.info("Se produjo un error en org.compiere.process.GenerateInterfaceSIFEREPercepciones " + exception.getMessage());
           exception.printStackTrace();
        }
        UtilProcess.initViewer("Interface a SIFERE Percepciones",p_PInstance_ID,getProcessInfo());
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
    
    protected String getComprobante(long id){
        if(id==1000134 || id==1000184 || id==1000185 || id==1000186) return "F";
        else if(id==1000135 || id==1000187 || id==1000188 || id==1000199) return "C";
        else if(id==1000189 || id==1000190 || id==1000191 || id==1000200) return "D";
        else if(id==1000137) return "R";
        else return "O";
    }
    
    protected String getLetra(long id){
        if(id==1000134 || id==1000135 || id==1000189) return "A";
        else if(id==1000187 || id==1000184 || id==1000190) return "B";
        else if(id==1000188 || id==1000185 || id==1000191) return "C";
        else if(id==1000199 || id==1000186 || id==1000200) return "E";
        else return "";
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
