/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;
import java.sql.SQLException;
import org.compiere.process.*;

import java.math.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Date;

import java.util.logging.*;
import org.compiere.model.*;

import org.compiere.util.*;
import org.eevolution.tools.Numero2Letras;
import org.eevolution.tools.UtilProcess;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Bision
 */
public class GeneratePagoCompRet extends SvrProcess{
    
    int p_instance;
    private Timestamp fromDate;
    private Timestamp toDate;
    long org;
        
     protected String doIt() throws Exception{
    	try {
    	 	
    		String sqlQuery = "";  
        	PreparedStatement pstmt;
        	if (fromDate != null && toDate != null)
        	{	
        		sqlQuery = "select AD_CLIENT_ID,AD_ORG_ID,C_PAYMENT_ID,NOMBRE,NRO,FECHA,CUIT,IIBB,DIR,CPCUI,PAIS,IDPROV,PROVEEDOR,CUITPROV,DIRPROV,CPCUIPROV,PAISPR,LEYENDA1,LEYENDA2,LEYENDA3 from RV_PAGO_CABECERA where FECHA between ? and ? and c_payment_id in (Select c_payment_id from c_payment where c_doctype_id = '1000138') Order By FECHA, PROVEEDOR";
        		pstmt = DB.prepareStatement(sqlQuery);
        		pstmt.setTimestamp(1, fromDate);
        		pstmt.setTimestamp(2, toDate);
        	}	
        	else
        		if (fromDate == null)
        		{
        			sqlQuery = "select AD_CLIENT_ID,AD_ORG_ID,C_PAYMENT_ID,NOMBRE,NRO,FECHA,CUIT,IIBB,DIR,CPCUI,PAIS,IDPROV,PROVEEDOR,CUITPROV,DIRPROV,CPCUIPROV,PAISPR,LEYENDA1,LEYENDA2,LEYENDA3 from RV_PAGO_CABECERA where FECHA <= ? and c_payment_id in (Select c_payment_id from c_payment where c_doctype_id = '1000138') Order By FECHA, PROVEEDOR";
            		pstmt = DB.prepareStatement(sqlQuery);
            		pstmt.setTimestamp(1, toDate);
        		}
        		else
        			if (toDate == null)
        			{
        				sqlQuery = "select AD_CLIENT_ID,AD_ORG_ID,C_PAYMENT_ID,NOMBRE,NRO,FECHA,CUIT,IIBB,DIR,CPCUI,PAIS,IDPROV,PROVEEDOR,CUITPROV,DIRPROV,CPCUIPROV,PAISPR,LEYENDA1,LEYENDA2,LEYENDA3 from RV_PAGO_CABECERA where FECHA >= ? and c_payment_id in (Select c_payment_id from c_payment where c_doctype_id = '1000138') Order By FECHA, PROVEEDOR";
                		pstmt = DB.prepareStatement(sqlQuery);
                		pstmt.setTimestamp(1, fromDate);
                	}
        			else
        			{
        				sqlQuery = "select AD_CLIENT_ID,AD_ORG_ID,C_PAYMENT_ID,NOMBRE,NRO,FECHA,CUIT,IIBB,DIR,CPCUI,PAIS,IDPROV,PROVEEDOR,CUITPROV,DIRPROV,CPCUIPROV,PAISPR,LEYENDA1,LEYENDA2,LEYENDA3 from RV_PAGO_CABECERA where c_payment_id in (Select c_payment_id from c_payment where c_doctype_id = '1000138') Order By FECHA, PROVEEDOR";
        				pstmt = DB.prepareStatement(sqlQuery);
        			}
        
        	ResultSet rs = pstmt.executeQuery();
        	
        	try {
            	while (rs.next()) {
                	cabecera(rs);
                	listado(rs.getLong(3));
                	pie(rs.getLong(3));
                	
                	UtilProcess.initPrint("PF_PAGOS",p_instance,getProcessInfo());
                	
                	compGan(rs.getLong(3));
                	compIB(rs.getLong(3));
               	}
        	}
        	catch (Exception exception) {
        		exception.printStackTrace();
        	} 
     	}	
     	catch (SQLException ex) {
    	 Logger.getLogger(GeneratePagoCompRet.class.getName()).log(Level.SEVERE, null, ex);
    	}
      
        return "success"; 
    }

     protected void listado(Long c_payment_id){
         try {
            
        	String sqlQuery = "",sqlInsert = "";
            PreparedStatement pstmtInsert = null;
            String sqlRemove = "delete from T_PAGO_LISTADO";
            DB.executeUpdate(sqlRemove, null);
            
            sqlQuery = "select ad_client_id,ad_org_id,nro,fecha,tipo,total,formapago,c_bankaccount_id,pago from RV_PAGO_LISTADO where c_payment_id = " + c_payment_id.toString();
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery);
            ResultSet rs = pstmt.executeQuery();
            
            try {
          	
            	if (rs.next())
            	{            		
            			sqlInsert = "insert into T_PAGO_LISTADO values(?,?,'Y',?,?,?,?,?,?,?,?,?)";
            			pstmtInsert = DB.prepareStatement(sqlInsert);
            			pstmtInsert.setLong(1, rs.getLong(1));
            			pstmtInsert.setLong(2, rs.getLong(2));
            			pstmtInsert.setInt(3, p_instance);
            			pstmtInsert.setDate(4, rs.getDate(4));
            			pstmtInsert.setString(5, rs.getString(5));
            			pstmtInsert.setString(6, rs.getString(3));
            			pstmtInsert.setString(7, rs.getString(7));
            			pstmtInsert.setLong(8, rs.getLong(8));
            			pstmtInsert.setDouble(9, rs.getDouble(9));
            			pstmtInsert.setDouble(10, rs.getDouble(6));   
            			pstmtInsert.setLong(11, c_payment_id);
                    
            			pstmtInsert.executeQuery();
            			DB.commit(true, get_TrxName());
                	
            			while (rs.next()) //Hasta que termine de cargar los registros con mismo C_Payment_Id
            			{
            						sqlInsert = "insert into T_PAGO_LISTADO values(?,?,'Y',?,?,?,?,null,null,null,?,?)";
            						pstmtInsert = DB.prepareStatement(sqlInsert);
            						pstmtInsert.setLong(1, rs.getLong(1));
            						pstmtInsert.setLong(2, rs.getLong(2));
            						pstmtInsert.setInt(3, p_instance);
            						pstmtInsert.setDate(4, rs.getDate(4));
            						pstmtInsert.setString(5, rs.getString(5));
            						pstmtInsert.setString(6, rs.getString(3));
            						pstmtInsert.setDouble(7, rs.getDouble(6));  
            						pstmtInsert.setLong(8, c_payment_id);
            						
            						pstmtInsert.executeQuery();
            						DB.commit(true, get_TrxName());
            			}
            	} //if
            	else	{
            		// para completar la forma de pago, si el pago no tiene asignaciones
            	
            		sqlQuery =  "select ad_client_id,ad_org_id,C_BANKACCOUNT_ID,PAYNET,"+
            		"case TENDERTYPE "+
            		"when 'K' then 'Cheque' "+
            		"when 'A' then 'ACH' "+
            		"when 'C' then 'Tarjeta de Crédito' "+
            		"when 'D' then 'Débito Directo' "+
            		"end formapago "+
            		"from c_payment where c_payment_id = " + c_payment_id.toString();;
            		pstmt = DB.prepareStatement(sqlQuery);
            		rs = pstmt.executeQuery();
            		
            		if (rs.next())
            		{
            			sqlInsert = "insert into T_PAGO_LISTADO values(?,?,'Y',?,?,?,?,?,?,?,?,?)";
            			pstmtInsert = DB.prepareStatement(sqlInsert);
            			pstmtInsert.setLong(1, rs.getLong(1));
            			pstmtInsert.setLong(2, rs.getLong(2));
            			pstmtInsert.setInt(3, p_instance);
            			pstmtInsert.setDate(4, null);
            			pstmtInsert.setString(5, "");
            			pstmtInsert.setString(6, "");
            			pstmtInsert.setString(7, rs.getString(5));
            			pstmtInsert.setLong(8, rs.getLong(3));
            			pstmtInsert.setDouble(9, rs.getDouble(4));
            			pstmtInsert.setDouble(10, 0);
            			pstmtInsert.setLong(11, c_payment_id);
            			
                        pstmtInsert.executeQuery();
                       	DB.commit(true, get_TrxName());
            		}
            	}	//else
            } catch (Exception exception) {
                exception.printStackTrace();
            }            
        } catch (SQLException ex) {
            Logger.getLogger(GeneratePagoCompRet.class.getName()).log(Level.SEVERE, null, ex);
        }
     }

     protected void cabecera(ResultSet rs){
     //   try {
            // para el pago total

        	String sqlInsert = "",importe="";
            PreparedStatement pstmtInsert = null;
            String sqlRemove = "delete from T_PAGO_CABECERA";
            DB.executeUpdate(sqlRemove, null);
            
            Numero2Letras conver = new Numero2Letras();
        	
            try {
                    sqlInsert = "insert into T_PAGO_CABECERA values(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pstmtInsert = DB.prepareStatement(sqlInsert);
                    pstmtInsert.setLong(1, rs.getLong(1));
                    pstmtInsert.setLong(2, rs.getLong(2));
                    pstmtInsert.setInt(3, p_instance);
                    pstmtInsert.setLong(4, rs.getLong(3));
                    pstmtInsert.setString(5, rs.getString(5));
                    pstmtInsert.setString(6, rs.getString(9));
                    pstmtInsert.setDate(7, rs.getDate(6));
                    pstmtInsert.setString(8, rs.getString(10));
                    pstmtInsert.setString(9, rs.getString(7));
                    pstmtInsert.setString(10, rs.getString(8));
                    pstmtInsert.setString(11, rs.getString(11));
                    pstmtInsert.setString(12, rs.getString(13));
                    pstmtInsert.setString(13, rs.getString(15));
                    pstmtInsert.setString(14, rs.getString(16));
                    pstmtInsert.setString(15, rs.getString(17));
                    pstmtInsert.setString(16, rs.getString(12));
                    pstmtInsert.setString(17, rs.getString(14));
                    pstmtInsert.setString(18, rs.getString(4));
                    pstmtInsert.setString(19, rs.getString(18));
                                        
                    org = rs.getLong(2);
                    //para calculo del importe
                    String moneda;
                    if(rs.getString(19)!=null)
                        moneda = rs.getString(19);
                    else
                        moneda = " ";
                    
                	double total=0;
                    String sql = "select RETENCIONIVA,RETENCIONGANANCIAS,RETENCIONSUSS,RETENCIONIB,TOTAL from RV_PAGO_PIE where C_PAYMENT_ID = " + rs.getLong(3);
                    PreparedStatement pstmt2 = DB.prepareStatement(sql);
                    ResultSet rs2 = pstmt2.executeQuery();
                    if(rs2.next()){
                        total = rs2.getDouble(5) - rs2.getDouble(1) - rs2.getDouble(2) - rs2.getDouble(3) - rs2.getDouble(4);
                    }
                    rs2.close();
                    pstmt2.close();
                    
                    double aux = total - (int)total;
                    aux = aux * 100;
                    if(total == 0)
                        importe=" de "+ moneda + " cero en concepto";
                    else{
                        if((int)aux==0)
                            importe=" de "+ moneda + " " + conver.convertirLetras((int)total) + " en concepto";
                        else
                            importe=" de "+ moneda + " " + conver.convertirLetras((int)total) + " con "+ conver.convertirLetras((int)aux)+ " centavos en concepto";
                    }
                    pstmtInsert.setString(20, importe);
                    pstmtInsert.setString(21, rs.getString(20));
                    pstmtInsert.executeQuery();
                    DB.commit(true, get_TrxName());
                
            }
            catch (Exception exception) {
                exception.printStackTrace();
            } 
        //}
    	//catch (SQLException ex) {
//    		Logger.getLogger(GeneratePagoCompRet.class.getName()).log(Level.SEVERE, null, ex);
    	//}
    }
     
    protected void pie(Long c_payment_id){
        try {
            String sqlQuery = "",sqlInsert = "";
            PreparedStatement pstmtInsert = null;
            String sqlRemove = "delete from T_PAGO_PIE";
            DB.executeUpdate(sqlRemove, null);
            sqlQuery = "select ad_client_id,ad_org_id,c_payment_id,RETENCIONIVA,RETENCIONGANANCIAS,RETENCIONSUSS,RETENCIONIB,TOTAL from RV_PAGO_PIE where c_payment_id = " + c_payment_id.toString();
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery);
            ResultSet rs = pstmt.executeQuery();
            try {
                if (rs.next()) {
                    sqlInsert = "insert into T_PAGO_PIE values(?,?,'Y',?,?,?,?,?,?,?)";
                    pstmtInsert = DB.prepareStatement(sqlInsert);
                    pstmtInsert.setLong(1, rs.getLong(1));
                    pstmtInsert.setLong(2, rs.getLong(2));
                    pstmtInsert.setInt(3, p_instance);
                    pstmtInsert.setLong(4, rs.getLong(3));
                    pstmtInsert.setDouble(5, rs.getDouble(4));
                    pstmtInsert.setDouble(6, rs.getDouble(7));                        
                    pstmtInsert.setDouble(7, rs.getDouble(5));
                    pstmtInsert.setDouble(8, rs.getDouble(6));
                    pstmtInsert.setDouble(9, rs.getDouble(8));
                    pstmtInsert.executeQuery();
                    DB.commit(true, get_TrxName());
                }
                else	{
                	sqlQuery = "select ad_client_id,ad_org_id,payamt from c_payment where c_payment_id = " + c_payment_id.toString();
                	pstmt = DB.prepareStatement(sqlQuery);
                	rs = pstmt.executeQuery();
                	if (rs.next()){
                        sqlInsert = "insert into T_PAGO_PIE values(?,?,'Y',?,?,?,?,?,?,?)";
                        pstmtInsert = DB.prepareStatement(sqlInsert);
                        pstmtInsert.setLong(1, rs.getLong(1));
                        pstmtInsert.setLong(2, rs.getLong(2));
                        pstmtInsert.setInt(3, p_instance);
                        pstmtInsert.setLong(4, c_payment_id);
                        pstmtInsert.setDouble(5, 0);
                        pstmtInsert.setDouble(6, 0);                        
                        pstmtInsert.setDouble(7, 0);
                        pstmtInsert.setDouble(8, 0);
                        pstmtInsert.setDouble(9, rs.getDouble(3));
                        pstmtInsert.executeQuery();
                        DB.commit(true, get_TrxName());
                	}
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }   
            
        } catch (SQLException ex) {
            Logger.getLogger(GeneratePagoCompRet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    protected void compGan(Long c_payment_id){
    	try {
        	String sqlQuery = "",sqlInsert = "";
            PreparedStatement pstmtInsert = null;

            sqlQuery = "select AD_CLIENT_ID,AD_ORG_ID,C_PAYMENT_ID,NOMBRE,DIR,CPCUI,CUIT,IIBB,NROOP,FECHA,PROVEEDOR,DIRPROV,CPCUIPROV,CUITPROV,IMPORTEBASE,IMPORTE,IMPORTETXT,RESOLUCION,C_PAYMENTRET_ID from RV_PAGO_RETENCIONESGAN where c_payment_id = " + c_payment_id.toString();
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery);
            ResultSet rs = pstmt.executeQuery();
            try {
                while (rs.next()) {
                    String sqlRemove = "delete from T_PAGO_RETENCIONESGAN";
                    DB.executeUpdate(sqlRemove, null);
                	
                	sqlInsert = "insert into T_PAGO_RETENCIONESGAN values(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pstmtInsert = DB.prepareStatement(sqlInsert);
                    pstmtInsert.setLong(1, rs.getLong(1));
                    pstmtInsert.setLong(2, rs.getLong(2));
                    pstmtInsert.setInt(3, p_instance);
                    pstmtInsert.setLong(4, rs.getLong(3));
                    pstmtInsert.setString(5, rs.getString(4));
                    pstmtInsert.setString(6, rs.getString(5));
                    pstmtInsert.setString(7, rs.getString(6));                        
                    pstmtInsert.setString(8, rs.getString(7));
                    pstmtInsert.setString(9, rs.getString(8));
                    pstmtInsert.setString(10, rs.getString(9));
                    pstmtInsert.setString(11, parserFecha(rs.getString(10)));
                    pstmtInsert.setString(12, rs.getString(11));
                    pstmtInsert.setString(13, rs.getString(12));
                    pstmtInsert.setString(14, rs.getString(13));
                    pstmtInsert.setString(15, rs.getString(14));
                    pstmtInsert.setDouble(16, rs.getDouble(15));
                    pstmtInsert.setDouble(17, rs.getDouble(16));
                    pstmtInsert.setString(18, rs.getString(17));
                    pstmtInsert.setString(19, rs.getString(18));
                    pstmtInsert.setLong(20, rs.getLong(19));
                    
                    Date fecha;
                    if (fromDate!=null)
                    	fecha = new Date(fromDate.getTime());
                    else
                    	if (toDate!=null)
                        	fecha = new Date(toDate.getTime());
                    	else
                    		fecha = (Date) Calendar.getInstance().getTime();
                    
                    pstmtInsert.setDate(21, fecha);
                                       
                    pstmtInsert.executeQuery();
                    DB.commit(true, get_TrxName());
                    
                   	UtilProcess.initPrint("COMP_RETGAN",p_instance,getProcessInfo());
                    
                } //while
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }   
            
        } catch (SQLException ex) {
            Logger.getLogger(GeneratePagoCompRet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String parserFecha(String fecha){

    	if (fecha.contains("JAN"))
    		fecha = fecha.replace("JAN", "01");
    	
    	if (fecha.contains("FEB"))
    		fecha = fecha.replace("FEB", "02");
    	
    	if (fecha.contains("MAR"))
    		fecha = fecha.replace("MAR", "03");
    	
    	if (fecha.contains("APR"))
    		fecha = fecha.replace("APR", "04");
    	
    	if (fecha.contains("MAY"))
    		fecha = fecha.replace("MAY", "05");
    	
    	if (fecha.contains("JUN"))
    		fecha = fecha.replace("JUN", "06");
    	
    	if (fecha.contains("JUL"))
    		fecha = fecha.replace("JUL", "07");
    	
    	if (fecha.contains("AUG"))
    		fecha = fecha.replace("AUG", "08");
    	
    	if (fecha.contains("SEP"))
    		fecha = fecha.replace("SEP", "09");
    	
    	if (fecha.contains("OCT"))
    		fecha = fecha.replace("OCT", "10");
    	
    	if (fecha.contains("NOV"))
    		fecha = fecha.replace("NOV", "11");
    	
    	if (fecha.contains("DEC"))
    		fecha = fecha.replace("DEC", "12");
    	
    	return fecha;
    }
    
    protected void compIB(Long c_payment_id){
    	try {
        	String sqlQuery = "",sqlInsert = "";
            PreparedStatement pstmtInsert = null;

            sqlQuery = "select AD_CLIENT_ID,AD_ORG_ID,C_PAYMENT_ID,NOMBRE,DIR,CPCUI,CUIT,IIBB,NROOP,FECHA,PROVEEDOR,DIRPROV,CPCUIPROV,CUITPROV,IMPORTEBASE,IMPORTE,IMPORTETXT from RV_PAGO_RETENCIONESIB where c_payment_id = " + c_payment_id.toString();
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery);
            ResultSet rs = pstmt.executeQuery();
            try {
                while (rs.next()) {
                    String sqlRemove = "delete from T_PAGO_RETENCIONESIB";
                    DB.executeUpdate(sqlRemove, null);
                    
                    sqlInsert = "insert into T_PAGO_RETENCIONESIB values(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pstmtInsert = DB.prepareStatement(sqlInsert);
                    pstmtInsert.setLong(1, rs.getLong(1));
                    pstmtInsert.setLong(2, rs.getLong(2));
                    pstmtInsert.setInt(3, p_instance);
                    pstmtInsert.setLong(4, rs.getLong(3));
                    pstmtInsert.setString(5, rs.getString(4));
                    pstmtInsert.setString(6, rs.getString(5));
                    pstmtInsert.setString(7, rs.getString(6));                        
                    pstmtInsert.setString(8, rs.getString(7));
                    pstmtInsert.setString(9, rs.getString(8));
                    pstmtInsert.setString(10, rs.getString(9));
                    pstmtInsert.setString(11, parserFecha(rs.getString(10)));
                    pstmtInsert.setString(12, rs.getString(11));
                    pstmtInsert.setString(13, rs.getString(12));
                    pstmtInsert.setString(14, rs.getString(13));
                    pstmtInsert.setString(15, rs.getString(14));
                    pstmtInsert.setDouble(16, rs.getDouble(15));
                    pstmtInsert.setDouble(17, rs.getDouble(16));
                    pstmtInsert.setString(18, rs.getString(17));
                    
                    Date fecha;
                    if (fromDate!=null)
                    	fecha = new Date(fromDate.getTime());
                    else
                    	if (toDate!=null)
                        	fecha = new Date(toDate.getTime());
                    	else
                    		fecha = (Date) Calendar.getInstance().getTime();
                    
                    pstmtInsert.setDate(19, fecha);
                    
                    pstmtInsert.executeQuery();
                    DB.commit(true, get_TrxName());
                    
                    UtilProcess.initPrint("COMP_RETIB",p_instance,getProcessInfo());

                } //while
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }   
            
        } catch (SQLException ex) {
            Logger.getLogger(GeneratePagoCompRet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
    	/*fromDate = new Timestamp(0);
    	toDate = new Timestamp(0);*/
   		if (para.length > 0)
   		{	
   			fromDate = (Timestamp)para[0].getParameter();
       	   	toDate=(Timestamp)para[0].getParameter_To();
       	}
       	p_instance = getAD_PInstance_ID();         
    }    

}
