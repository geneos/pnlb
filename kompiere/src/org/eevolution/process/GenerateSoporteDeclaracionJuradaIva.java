package org.eevolution.process;
import org.compiere.process.*;
import java.math.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.*;
import org.compiere.model.*;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;
/**
 *Esta clase inserta tuplas en la tabla temporal T_DECLARACIONJURIVA, luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
*	@author BISion Matías Maenza
*	@version 1.0
 */
public class GenerateSoporteDeclaracionJuradaIva extends SvrProcess {
    
    private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    private static String[] meses={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++)
	{
            String name = para[i].getParameterName();
            fromDate=(Timestamp)para[i].getParameter();
            toDate=(Timestamp)para[i].getParameter_To();
        }
            p_PInstance_ID = getAD_PInstance_ID();     
            
    }   

   
    protected String doIt() throws Exception {
        float nota23=0,importe=0,nota24=0;
        String sqlQuery,sqlInsert="";
        PreparedStatement pstmtInsert = null;
        log.info("Comienzo del proceso de generacion del soporte para la declaracion jurada de iva");
        log.info("borrado de la tabla temporal T_DECLARACIONJURIVA");
        String sqlRemove = "delete from T_DECLARACIONJURIVA";
        DB.executeUpdate(sqlRemove, null);
        try {
                sqlInsert = "insert into T_DECLARACIONJURIVA values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                pstmtInsert.setLong(1, getAD_Client_ID());
                pstmtInsert.setLong(2, 1000033);
                pstmtInsert.setString(3,"Y");
                pstmtInsert.setLong(4, p_PInstance_ID);
                pstmtInsert.setString(5, "PANALAB S.A.");
                // para la fecha
                java.util.Date fecha = new java.util.Date();
                pstmtInsert.setString(6,fecha.getDate()+" de "+meses[fecha.getMonth()]+" "+(fecha.getYear()+1900));
                pstmtInsert.setString(7, "DECLARACIÓN JURADA "+getFecha(fromDate)+" a "+getFecha(toDate)+" IVA");
                pstmtInsert.setDate(8, new Date(fromDate.getTime() + 1000));
                /*
                 *   DEBITO FISCAL
                 * */
                // titulo1
                pstmtInsert.setString(9, "Operaciones con Responsables Inscriptos");
                pstmtInsert.setString(10, "Alícuota");
                pstmtInsert.setString(11, "21%");
                pstmtInsert.setString(12, "Monto Neto Gravado");
                pstmtInsert.setFloat(13, getValorNotaFacturas("LINENETAMT","1000131,1000205,1000175"," and (line.c_tax_id in (1000052))","Y",""));
                pstmtInsert.setString(14, "Débito Fiscal Facturado");
                importe = getValorNotaFacturas("TAXAMT","1000131,1000205,1000175"," and (line.c_tax_id in (1000052))","Y","");
                pstmtInsert.setFloat(15, importe);
                nota23+=importe;
                //titulo 2
                pstmtInsert.setString(16, "Operaciones con Consumidores Finales, Exentos y no alcanzados");
                pstmtInsert.setString(17, "Alícuota");
                pstmtInsert.setString(18, "21%");
                pstmtInsert.setString(19, "Monto Total Facturado");
                pstmtInsert.setFloat(20, getValorNotaFacturas("LINENETAMT","1000204,1000170,1000197"," and (line.c_tax_id in (1000052))","Y",""));
                pstmtInsert.setString(21, "Débito Fiscal Facturado");
                importe = getValorNotaFacturas("TAXAMT","1000204,1000170,1000197"," and (line.c_tax_id in (1000052))","Y","");
                pstmtInsert.setFloat(22, importe);
                nota23+=importe;
                //titulo 3
                pstmtInsert.setString(23, "Operaciones No Gravadas y Exentas, excepto Exportaciones");
                pstmtInsert.setString(24, "Monto");
                pstmtInsert.setFloat(25, getValorNotaFacturas("LINENETAMT","1000131,1000205,1000175,1000204,1000170,1000197"," and (line.c_tax_id in (1000055,1000056))","Y",""));
                /*
                 * RESTITUCION DE CREDITOS FISCALES
                 */
                // titulo 4
                pstmtInsert.setString(26, "Por Compra de Bienes en el Mercado Local (excepto Bienes de Uso)");
                pstmtInsert.setString(27, "Crédito Fiscal Total");
                importe = getValorNotaFacturas("TAXAMT","1000135"," and (line.c_tax_id in (1000052,1000054,1000053))","N"," and (line.m_product_id is not null)");
                pstmtInsert.setFloat(28, importe);
                nota23+=importe;
                //titulo 5
                pstmtInsert.setString(29, "Por Otros Conceptos");
                pstmtInsert.setString(30, "Crédito Total Fiscal");
                importe = getValorNotaFacturas("TAXAMT","1000135"," and (line.c_tax_id in (1000052,1000054,1000053))","N"," and (line.c_charge_id is not null)");
                pstmtInsert.setFloat(31, importe);
                nota23+=importe;
                /*
                 * COMPRAS - - CREDITO FISCAL
                 */
                //titulo 6
                pstmtInsert.setString(32, "Compra de Bienes en el Mercado Local (Excepto Bienes de uso)");
                pstmtInsert.setString(33, "Crédito Fiscal Facturado");
                pstmtInsert.setFloat(34, getValorNotaFacturas("TAXAMT","1000134,1000189"," and (line.c_tax_id in (1000052,1000054,1000053))","N"," and (line.m_product_id is not null)"));
                pstmtInsert.setString(35, "Crédito Fiscal Computable");
                importe = getValorNota9("1000134,1000189","1000052,1000054,1000053","N"," and (line.m_product_id is not null)");
                pstmtInsert.setFloat(36, importe);
                nota24 += importe;
                // titulo 7
                pstmtInsert.setString(37, "Otros conceptos");
                //modificacion... "CFC" por "CFF"
                pstmtInsert.setString(38, "Crédito Fiscal Facturado");
                pstmtInsert.setFloat(39, getValorNotaFacturas("TAXAMT","1000134,1000189"," and (line.c_tax_id in (1000052,1000054,1000053))","N"," and (line.c_charge_id is not null)"));
                //modificacion... "DFF" por "CFC"
                pstmtInsert.setString(40, "Crédito Fiscal Computable");
                importe = getValorNota9("1000134,1000189","1000052,1000054,1000053","N"," and (line.c_charge_id is not null)");
                pstmtInsert.setFloat(41, importe);
                nota24 += importe;
                /*
                 *      RESTITUCION DE DEBITO FISCAL
                 * 
                 */
                pstmtInsert.setString(42, "Operaciones con Responsables Inscriptos");
                pstmtInsert.setString(43, "Alícuota");
                pstmtInsert.setString(44, "21%");
                pstmtInsert.setString(45, "Monto Neto Gravado");
                pstmtInsert.setFloat(46, getValorNotaFacturas("LINENETAMT","1000133,1000203,1000172,1000178,1000173"," and (line.c_tax_id in (1000052))","Y",""));
                pstmtInsert.setString(47, "Débito Fiscal Facturado");
                importe = getValorNotaFacturas("TAXAMT","1000133,1000203,1000172,1000178,1000173"," and (line.c_tax_id in (1000052))","Y","");
                pstmtInsert.setFloat(48, importe);
                nota24+=importe;
                // titulo 9
                pstmtInsert.setString(49, "Operaciones con Consumidores Finales, Exentos y No Alcanzados");
                pstmtInsert.setString(50, "Alícuota");
                pstmtInsert.setString(51, "21%");
                pstmtInsert.setString(52, "Monto Total Facturado");
                pstmtInsert.setFloat(53, getValorNotaFacturas("LINENETAMT","1000206,1000171,1000207,1000198,1000208"," and (line.c_tax_id in (1000052))","Y",""));
                pstmtInsert.setString(54, "Débito Fiscal Facturado");
                importe = getValorNotaFacturas("TAXAMT","1000206,1000171,1000207,1000198,1000208"," and (line.c_tax_id in (1000052))","Y","");
                pstmtInsert.setFloat(55, importe);
                nota24+=importe;
                /*
                 * OPERACIONES QUE NO GENERAN CREDITO FISCAL
                 */
                // titulo 10
                pstmtInsert.setString(56, "Compras No Gravadas y Exentas");
                pstmtInsert.setString(57, "Monto Neto");
                pstmtInsert.setFloat(58, getValorNotaFacturas("LINENETAMT","1000134,1000189"," and (line.c_tax_id in (1000055,1000056))","N",""));
                // titulo 11
                pstmtInsert.setString(59, "Compras a Monotributistas");
                pstmtInsert.setString(60, "Monto Neto");
                pstmtInsert.setFloat(61, getValorNotaFacturas("LINENETAMT","1000185,1000191","","N",""));
                // titulo 12
                pstmtInsert.setString(62, "Otras Compras que no generan Crédito Fiscal");
                pstmtInsert.setString(63, "Monto Neto");
                pstmtInsert.setFloat(64, 0);
                /*
                 *  INGRESOS DIRECTOS -  - - -  INGRESOS A CUENTA ORIGINADOS EN EL PERIODO
                 */
                // titulo 13
                float reten = getValorNota19();
                pstmtInsert.setString(65, "Régimen de Retenciones");
                pstmtInsert.setFloat(66, reten);
                // titulo 14
                float percep = getValorNota20();
                pstmtInsert.setString(67, "Régimen de Percepciones");
                pstmtInsert.setFloat(68, percep);
                // titulo 15
                pstmtInsert.setString(69, "Total Retenciones y Percepciones");
                pstmtInsert.setFloat(70, (reten + percep));
                /*
                 *      OPERACIONES DE EXPORTACION
                 */
                // titulo 16
                pstmtInsert.setString(71, "Total de exportaciones");
                pstmtInsert.setFloat(72, getValorNotaFacturas("LINENETAMT","1000174,1000176,1000177","","Y",""));
                /*
                 *      DETERMINACION DEL IMPUESTO
                 */
                // titulo 17
                pstmtInsert.setString(73, "Total de Débito Fiscal del período");
                pstmtInsert.setFloat(74, nota23);
                // titulo 18
                pstmtInsert.setString(75, "Total del Crédito Fiscal del período");
                pstmtInsert.setFloat(76, nota24);
                // titulo 19
                pstmtInsert.setString(77, "Saldo a favor del Responsable");
                if(nota24 > nota23)
                    pstmtInsert.setFloat(78, (nota24-nota23));
                else
                    pstmtInsert.setFloat(78, 0);
                // titulo 20
                pstmtInsert.setString(79, "Saldo a favor de AFIP");
                if(nota23 > nota24)
                    pstmtInsert.setFloat(80, (nota23-nota24));
                else
                    pstmtInsert.setFloat(80, 0);
                
                pstmtInsert.executeQuery();DB.commit(true, get_TrxName());
                    
        } catch (SQLException ex) {
            Logger.getLogger(GenerateSoporteDeclaracionJuradaIva.class.getName()).log(Level.SEVERE, null, ex);
        }
        UtilProcess.initViewer("Declaración Jurada IVA F731",p_PInstance_ID,getProcessInfo());
        return "success";
    }
        
        protected float getValorNotaFacturas(String campo,String docs,String taxs,String esCliente,String demas){
            String sqlQuery="select sum(line."+campo+") from c_invoice fac "+
                            "left join c_invoiceline line on (fac.c_invoice_id = line.c_invoice_id) "+
                            "where (fac.ISSOTRX='"+esCliente+"') and (fac.dateinvoiced between ? and ?) and (fac.C_DOCTYPETARGET_ID in ("+docs+")) "+
                            "and (fac.docstatus='CL' or fac.docstatus='CO')"+taxs+demas;
            try {
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
              return (rs.getFloat(1));  
            }
            else
              return 0;
            } catch (Exception exception) {
               log.info("Se produjo un error en org.compiere.process.GenerateSoporteDeclaracionJuradaIva " + exception.getMessage());
               exception.printStackTrace();
            }
            return 0;
    }
       
        protected float getValorNota9(String docs,String taxs,String esCliente,String demas){
            float total=0;
            String sqlQuery="select line.c_charge_id,line.TAXAMT from c_invoice fac "+
                            "left join c_invoiceline line on (fac.c_invoice_id = line.c_invoice_id) "+
                            "left join c_charge cha on (cha.c_charge_id = line.c_charge_id) "+
                            "where (fac.ISSOTRX='"+esCliente+"') and (fac.dateinvoiced between ? and ?) and (fac.C_DOCTYPETARGET_ID in ("+docs+")) "+
                            "and (line.c_tax_id in ("+taxs+")) and (fac.docstatus='CL' or fac.docstatus='CO') "+demas;
            try {
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
              String sql = "select ISCREDITOFISCAL,ISMONTOFIJO,ISPORCENTAJE,MONTOMAXIMO,PORCENTAJE from c_charge "+
                            "where c_charge_id="+rs.getLong(1);
              PreparedStatement pstmt2 = DB.prepareStatement(sql);
              ResultSet rs2 = pstmt2.executeQuery();
              if(rs2.next()){
                if(rs2.getString(1).equals("Y"))
                    total += rs.getFloat(2);
                else{
                    if(rs2.getString(2).equals("Y")){
                        if(rs2.getFloat(4)<rs.getFloat(2))
                            total+=rs2.getFloat(4);
                        else
                            total+=rs.getFloat(2);
                    }
                    else{
                        if(rs2.getString(3).equals("Y")){
                            float aux = rs.getFloat(2)*rs2.getFloat(5)/100;
                            total+=aux;
                        }
                    }
                }
              }              
            }            
            } catch (Exception exception) {
               log.info("Se produjo un error en org.compiere.process.GenerateSoporteDeclaracionJuradaIva " + exception.getMessage());
               exception.printStackTrace();
            }
            return total;
        }
        
        protected float getValorNota19(){
            String sqlQuery="select sum(ret.IMPORTE) "+
                            "from c_payment cob "+
                            "join c_paymentret ret on (cob.c_payment_id = ret.c_payment_id) "+
                            "where (cob.ISRECEIPT='Y') and (cob.DATETRX between ? and ?) "+
                            "and (ret.TIPO_RET='I') and (cob.docstatus='CL' or cob.docstatus='CO')";
            try {
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
              return (rs.getFloat(1));  
            }
            else
              return 0;
            } catch (Exception exception) {
               log.info("Se produjo un error en org.compiere.process.GenerateSoporteDeclaracionJuradaIva " + exception.getMessage());
               exception.printStackTrace();
            }
            return 0;
        }
        
        protected float getValorNota20(){
            String sqlQuery="select sum(line.LINENETAMT) from c_invoice fac "+
                            "left join c_invoiceline line on (fac.c_invoice_id = line.c_invoice_id) "+
                            "left join c_charge cha on (line.c_charge_id = cha.c_charge_id) "+
                            "where (fac.ISSOTRX='N') and (fac.dateinvoiced between ? and ?) and (fac.docstatus='CL' or fac.docstatus='CO') "+
                            "and (cha.istax='Y') and (cha.taxtype='IVA')";
            try {
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
              return (rs.getFloat(1));  
            }
            else
              return 0;
            } catch (Exception exception) {
               log.info("Se produjo un error en org.compiere.process.GenerateSoporteDeclaracionJuradaIva " + exception.getMessage());
               exception.printStackTrace();
            }
            return 0;
        }
       
    protected String getFecha(Timestamp date){
        String fecha = "";
        if(date.getDate()<10) fecha = "0"+date.getDate(); else fecha += date.getDate();
        if(date.getMonth()+1 < 10)  fecha+= "/0"+(date.getMonth()+1); else fecha+= "/"+(date.getMonth()+1);
        fecha +="/"+(date.getYear()+1900);
        return fecha;
    }   
  
}
