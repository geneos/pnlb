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
* Esta clase inserta tuplas en la tabla temporal T_INTERFACE_SICORE luego de un previo filtrado por fecha 
*  y calculos posteriores.
*
*	@author BISion
*	@version 1.0
*/
public class GenerateIterface_SICORE extends SvrProcess{

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
            // el delimitador para la exportacion
            Env.getCtx().put("Delimitador", "");
    }   

   
    protected String doIt() throws Exception {
        StringBuffer sqlQuery = new StringBuffer();            
        String sqlInsert="";
        PreparedStatement pstmtInsert = null;
        log.info("Comienzo del proceso de generacion de la interface Sicore");
        log.info("borrado de la tabla temporal T_INTERFACE_SICORE");
        String sqlRemove = "delete from T_INTERFACE_SICORE";
        DB.executeUpdate(sqlRemove, null);
        
        sqlQuery.append("select ad_client_id,ad_org_id,isactive,fecha_emision_comp,NRO_COMPROBANTE,importe,coderegimen,fecha_emision_ret,importeRetencion,cuit,ISVENDOR,PAGONETO from RV_INTERFACE_SICORE"+
                " where (fecha_emision_ret between ? and ?) and ISVENDOR='Y'");
        PreparedStatement pstmt = DB.prepareStatement(sqlQuery.toString(), get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setTimestamp(2, toDate);
        ResultSet rs = pstmt.executeQuery();
        //DecimalFormat format14 = new DecimalFormat("00000000000.00");                
        
        try {
        while (rs.next()) {
            sqlInsert = "insert into T_INTERFACE_SICORE values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            pstmtInsert.setLong(1, rs.getLong(1));
            pstmtInsert.setLong(2, rs.getLong(2));
            pstmtInsert.setString(3, rs.getString(3));
            pstmtInsert.setLong(4, p_PInstance_ID);
            pstmtInsert.setString(5, "06");
            pstmtInsert.setDate(6, new Date(rs.getDate(4).getTime() + 1000));
            //  para el numero de comprobante
            String documento = rs.getString(5);
            documento = documento.replaceAll("-", "");
            pstmtInsert.setString(7, getCeros(documento,16));            
            pstmtInsert.setString(8,getNumero(rs.getFloat(6),13));
            pstmtInsert.setString(9, "217");
            pstmtInsert.setString(10, getCeros(rs.getString(7),3));
            pstmtInsert.setString(11, "1");
            pstmtInsert.setString(12,getNumero(rs.getFloat(12),13));
            pstmtInsert.setDate(13,rs.getDate(8));
            pstmtInsert.setString(14, "01");
            pstmtInsert.setString(15,getNumero(rs.getFloat(9),13));
            pstmtInsert.setString(16, "000,000");
            pstmtInsert.setString(17, "00/00/0000");
            pstmtInsert.setInt(18,80);
            // para el cuit
            String cuit = rs.getString(10).replaceAll("-", "");
            pstmtInsert.setString(19, getCeros(cuit,20));
            pstmtInsert.setString(20,"00000000000000");
            pstmtInsert.executeQuery();
            DB.commit(true, get_TrxName());
        }
        } catch (Exception exception) {
           log.info("Se produjo un error en org.compiere.process.GenerateInterfaceSICORE " + exception.getMessage());
           exception.printStackTrace();
        }   
        UtilProcess.initViewer("Interface SICORE",p_PInstance_ID,getProcessInfo());
        return "success";
    }
    
   // modificado - BiSion - Matías Maenza 16/05/08
    protected String getNumero(float number, int longitud){
        // para que el muestre de la forma 1234,00 y no 1234.00
        Locale.setDefault(Locale.GERMAN);
        DecimalFormat df = new DecimalFormat("###########.00");
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
