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
*Esta clase inserta tuplas en la tabla temporal T_RECBANCARIA_SIFERE luego de un previo filtrado por fecha 
*  y calculos posteriores.
*
*	@author BISion
*	@version 1.0
**/

public class GenerateSifereRecaudacionBancaria extends SvrProcess{

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
        log.info("Comienzo del proceso de recaudación bancaria para la interface sifere");
        log.info("borrado de la tabla temporal T_RECBANCARIA_SIFERE");
        String sqlRemove = "delete from T_RECBANCARIA_SIFERE";
        DB.executeUpdate(sqlRemove, null);
        
        sqlQuery="select fac.ad_client_id,fac.ad_org_id,line.codigo_jurisdiccion,socio.taxid,line.month,line.year,cuenta.CBU,case cuenta.BANKACCOUNTTYPE "+
                "when 'C' then 'CC' when 'S' then 'CA' when null then 'CC' end tipocuenta,fac.c_currency_id,line.LINENETAMT,fac.dateinvoiced "+
                "from c_invoice fac "+
                "left join c_invoiceline line on (fac.c_invoice_id = line.c_invoice_id) "+
                "left join c_bpartner socio on (fac.c_bpartner_id = socio.c_bpartner_id) "+
                "left join c_bp_bankaccount cuenta on (socio.c_bpartner_id = cuenta.c_bpartner_id) "+
                "join c_charge cha on (line.c_charge_id = cha.c_charge_id) "+
                "where (fac.isSOTrx='N') and (fac.docstatus='CL' or fac.docstatus='CO') and (cuenta.ISACH='Y') "+
                "and (cha.taxtype='IIB') and (cha.istax='Y') and (cha.ISRETBANK='Y') and (fac.dateinvoiced between ? and ?) order by fac.dateinvoiced";
        PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setTimestamp(2, toDate);
        ResultSet rs = pstmt.executeQuery();
        try {
        while (rs.next()) {
            sqlInsert = "insert into T_RECBANCARIA_SIFERE values(?,?,'Y',?,?,?,?,?,?,?,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            pstmtInsert.setLong(1, rs.getLong(1));
            pstmtInsert.setLong(2, rs.getLong(2));
            pstmtInsert.setLong(3, p_PInstance_ID);
            pstmtInsert.setDate(4, new Date(rs.getDate(11).getTime()+1000));
            //para el codigo jurisdiccion
            aux = ""+rs.getInt(3);
            pstmtInsert.setString(5, getCeros(aux,3));
            //  para el cuit
            pstmtInsert.setString(6, rs.getString(4));
            //para el período
            aux = ""+rs.getInt(6);
            //pstmtInsert.setString(7, getCeros(aux,4)+"/"+getMes(rs.getInt(5))); comentado por modificacion solicitada por julio 14/07/2008
            pstmtInsert.setString(7, getMes(rs.getInt(5))+"-"+getCeros(aux,4));
            pstmtInsert.setString(8, getCeros(rs.getString(7),22));
            pstmtInsert.setString(9, rs.getString(8));
            if(rs.getLong(9)==118)   
                pstmtInsert.setString(10, "P");
            else
                pstmtInsert.setString(10, "E");
            pstmtInsert.setString(11, getNumero(rs.getFloat(10),11));            
            pstmtInsert.executeQuery();
            DB.commit(true, get_TrxName());
        }
        } catch (Exception exception) {
           log.info("Se produjo un error en org.compiere.process.GenerateSiferePercepcionesAduaneras " + exception.getMessage());
           exception.printStackTrace();
        }
        UtilProcess.initViewer("Interface Sifere Recaudaciones Bancarias",p_PInstance_ID,getProcessInfo());
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
    
   protected String getMes(int mes){
       if(mes==0) return "00";
       else if(mes==1) return "01";
       else if(mes==2) return "02";
       else if(mes==3) return "03";
       else if(mes==4) return "04";
       else if(mes==5) return "05";
       else if(mes==6) return "06";
       else if(mes==7) return "07";
       else if(mes==8) return "08";
       else if(mes==9) return "09";
       else if(mes==90) return "10";
       else if(mes==91) return "11";
       else return "12";
   }   
}
