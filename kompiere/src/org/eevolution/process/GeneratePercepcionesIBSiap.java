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
 *Esta clase inserta tuplas en la tabla temporal T_SIAP_PERCEP_IB luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
*	@author BISion - Matías Maenza
*	@version 1.0
 */
public class GeneratePercepcionesIBSiap extends SvrProcess {
    
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
        log.info("Comienzo del proceso de generacion de la interface SIAP para percepciones a IB");
        log.info("borrado de la tabla temporal T_SIAP_PERCEP_IB");
        String sqlRemove = "delete from T_SIAP_PERCEP_IB";
        DB.executeUpdate(sqlRemove, null);
        sqlQuery = "select c_invoice.AD_CLIENT_ID,c_invoice.AD_ORG_ID,c_bpartner.TAXID,c_invoice.DATEINVOICED, "+ 
                   "case c_invoice.C_DOCTYPETARGET_ID "+
                        "when 1000131 then 'F' "+
                        "when 1000204 then 'F' "+
                        "when 1000205 then 'F' "+
                        "when 1000170 then 'F' "+
                        "when 1000175 then 'D' "+
                        "when 1000197 then 'D' "+
                        "when 1000133 then 'C' "+
                        "when 1000206 then 'C' "+
                        "when 1000172 then 'C' "+
                        "when 1000207 then 'C' "+
                        "when 1000203 then 'C' "+
                        "when 1000171 then 'C' "+
                        "when 1000208 then 'C' "+
                        "when 1000173 then 'C' "+
                        "when 1000178 then 'C' "+
                        "when 1000198 then 'C' "+
                   "end tipoComprobante,  "+
                   "case C_DOCTYPETARGET_ID "+
                        "when 1000131 then 'A' "+
                        "when 1000204 then 'B' "+
                        "when 1000205 then 'A' "+
                        "when 1000170 then 'B' "+
                        "when 1000175 then 'A' "+
                        "when 1000197 then 'B' "+
                        "when 1000133 then 'A' "+
                        "when 1000206 then 'B' "+
                        "when 1000172 then 'A' "+
                        "when 1000207 then 'B' "+
                        "when 1000203 then 'A' "+
                        "when 1000171 then 'B' "+
                        "when 1000208 then 'B' "+
                        "when 1000173 then 'A' "+
                        "when 1000178 then 'A' "+
                        "when 1000198 then 'B' "+
                    "end letraComprobante, "+
                    "c_invoice.DOCUMENTNO,c_invoice.TOTALLINES,c_invoice.PERCEPCIONIB  "+
                    "from c_invoice  "+
                    "join c_bpartner on (c_invoice.C_BPARTNER_ID = c_bpartner.C_BPARTNER_ID) "+
                    "join c_doctype on (c_doctype.C_DOCTYPE_ID = c_invoice.C_DOCTYPETARGET_ID) "+
                    "where (C_DOCTYPETARGET_ID = 1000131 or C_DOCTYPETARGET_ID = 1000204 or C_DOCTYPETARGET_ID = 1000205 or C_DOCTYPETARGET_ID = 1000170  "+
                    "or C_DOCTYPETARGET_ID = 1000175 or C_DOCTYPETARGET_ID = 1000197 or C_DOCTYPETARGET_ID = 1000198 or C_DOCTYPETARGET_ID = 1000178  "+
                    "or C_DOCTYPETARGET_ID = 1000133 or C_DOCTYPETARGET_ID = 1000206 or C_DOCTYPETARGET_ID = 1000172 or C_DOCTYPETARGET_ID = 1000207 "+
                    "or C_DOCTYPETARGET_ID = 1000203 or C_DOCTYPETARGET_ID = 1000171 or C_DOCTYPETARGET_ID = 1000208 or C_DOCTYPETARGET_ID = 1000173) "+
                    "and (c_invoice.DOCSTATUS = 'DR' or c_invoice.DOCSTATUS = 'CO') and c_invoice.ISSOTRX = 'Y' "+ 
                    "and  (c_invoice.DATEINVOICED between ? and ?)";
        PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
        pstmt.setTimestamp(1, fromDate);
        pstmt.setTimestamp(2, toDate);
        ResultSet rs = pstmt.executeQuery();
        try {
        while (rs.next()) {
            sqlInsert = "insert into T_SIAP_PERCEP_IB2 values(?,?,'Y',?,?,?,?,?,?,?,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            pstmtInsert.setLong(1, rs.getLong(1));                              //ad_client_id
            pstmtInsert.setLong(2, rs.getLong(2));                              //ad_org_id
            pstmtInsert.setLong(3, p_PInstance_ID);                             //ad_pinstance_id
            pstmtInsert.setString(4, rs.getString(3));                          //cuit
            pstmtInsert.setDate(5, rs.getDate(4));                              //fecha
            pstmtInsert.setString(6, rs.getString(5));                          //tipo de comprobante
            pstmtInsert.setString(7, rs.getString(6));                          //letra de comprobante
            pstmtInsert.setString(8, "0001");                                   //numero de sucursal
            //limito el nº de documento a 8 caracteres y de ser necesario completo con 0 a izq
            String doc = rs.getString(7);
            if(doc.length()>8){
                //doc = doc.substring(doc.length()-8,doc.length());
                pstmtInsert.setString(9, doc.substring(doc.length()-8,doc.length()));
            }
            else
                pstmtInsert.setString(9, rs.getString(7));
                //pstmtInsert.setString(10, getCeros(rs.getString(7),8));
            //para el importe
            pstmtInsert.setString(10, getNumero(rs.getFloat(8),11));
            String comp = rs.getString(5);
            //si es una nota de crédito entonces el importe debe ser negativo
            if (rs.getString(5).equals("C")){
                String num = getNumero(rs.getFloat(9)*-1,10);
                pstmtInsert.setString(11, num);   
            }
            else
                pstmtInsert.setString(11, getNumero(rs.getFloat(9),10));
            pstmtInsert.executeQuery();
            DB.commit(true, get_TrxName());
        }
        } catch (Exception exception) {
            String ex = exception.getMessage();
            log.info("Se produjo un error en org.compiere.process.GeneratePercepcionesIBSiap " + exception.getMessage());
           exception.printStackTrace();
        }
        UtilProcess.initViewer("Interface SIAp Percepciones IB2",p_PInstance_ID,getProcessInfo());
        return "success";
    }
    
    // modificado - BiSion - Matías Maenza 16/05/08
    protected String getNumero(float number, int longitud){
        // para que el muestre de la forma 1234,00 y no 1234.00
        Locale.setDefault(Locale.GERMAN);
        DecimalFormat df = new DecimalFormat("########.00");
        String nro="";
        String aux = df.format(Math.abs(number));
        nro = getCeros(aux,longitud+1);
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