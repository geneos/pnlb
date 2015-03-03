/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.compiere.model.MBPartner;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

/**
 *
 * @author José Fantasia
 * 
 * 
 */


public class ProcesarFacturasCtaCte extends SvrProcess{
    
    int p_instance;
    private String p_fecha = "";
    private Timestamp fromDate;
    
        
    protected String doIt() throws Exception{   	     
        
	Date fecha = new Date();
	System.out.println (fecha);
        System.out.println (p_fecha);
        
        MBPartner pat;
        int[] allPartner = MBPartner.getAllIDs("C_BPartner", "isvendor='Y'", null);
        
        BigDecimal suma;
        BigDecimal resta;
        
        PreparedStatement pstmtSelectBP = null;
        PreparedStatement pstmtUpdateBP = null;
        
        String sqlSelectBP = "";
        String sqlUpdateBP = "";
        ResultSet rsSelectBP = null;
        BigDecimal totalopenbalance = Env.ZERO;
        

        for(int ind=0;ind<allPartner.length;ind++){
            
            totalopenbalance = Env.ZERO;
            
            // Obtengo el saldo para el socio de negocio

            try
            {
                sqlSelectBP = "select totalopenbalance, name from c_bpartner where c_bpartner_id = " + allPartner[ind];    
                pstmtSelectBP = DB.prepareStatement(sqlSelectBP, null);
                    
                rsSelectBP = pstmtSelectBP.executeQuery();
                
                while (rsSelectBP.next()){
                    totalopenbalance = rsSelectBP.getBigDecimal("totalopenbalance");
                    System.out.println("totalopenbalance de " + rsSelectBP.getString("name") + totalopenbalance);
                }
                
                rsSelectBP.close();
                pstmtSelectBP.close();
                pstmtSelectBP = null;
                
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                pstmtSelectBP = null;
            }                 
            
            //pat = new MBPartner(Env.getCtx(),allPartner[ind], null);
            String sql = "SELECT * FROM C_Invoice WHERE C_BPartner_ID = " + allPartner[ind] + " and (dateinvoiced >= to_date('"
				+ getFechaFromTimeStamp(fromDate, "/") + "', 'dd,mm,yyyy') or dateacct = to_date('01/09/2011', 'dd,mm,yyyy'))";
            
            PreparedStatement pstmt = null;

            try
            {
                    pstmt = DB.prepareStatement(sql, null);
                    
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()){
                        int c_doctype_id = rs.getInt("c_doctype_id");
                        
                        // sumo al saldo if resto else if
                        
                        if(c_doctype_id==1000246 || c_doctype_id==1000134 ||
                           c_doctype_id==1000184 || c_doctype_id==1000185 ||
                           c_doctype_id==1000186 || c_doctype_id==1000250 ||
                           c_doctype_id==5000014 || c_doctype_id==1000248 ||
                           c_doctype_id==1000247 || c_doctype_id==1000242 ||
                           c_doctype_id==5000010 || c_doctype_id==5000004 ||
                           c_doctype_id==5000005 || c_doctype_id==1000135 ||
                           c_doctype_id==1000187 || c_doctype_id==1000188 ||
                           c_doctype_id==1000199 || c_doctype_id==5000006){
                        
                            System.out.println("Anterior:" + totalopenbalance);
                            totalopenbalance = totalopenbalance.add(rs.getBigDecimal("grandtotal"));
                            System.out.println("Suma:" + totalopenbalance);
                            
                            sqlUpdateBP = "update c_bpartner set totalopenbalance = " + totalopenbalance + " where c_bpartner_id = " + allPartner[ind];
                            pstmtUpdateBP = DB.prepareStatement(sqlUpdateBP, null);
                            pstmtUpdateBP.executeUpdate();
                            
                            pstmtUpdateBP.close();
                            pstmtUpdateBP = null;
                            
                            //pat.setTotalOpenBalance(suma);
                            //pat.save();
                            
                        } else if(c_doctype_id==1000245 || c_doctype_id==1000138 ||
                           c_doctype_id==1000243 || c_doctype_id==1000244 ||
                           c_doctype_id==5000009 || c_doctype_id==1000189 ||
                           c_doctype_id==1000190 || c_doctype_id==1000191 ||
                           c_doctype_id==1000200){
                        
                            /*
                            pat.setTotalOpenBalance(pat.getTotalOpenBalance().subtract(rs.getBigDecimal("grandtotal")));
                            System.out.println("Anterior:" + pat.getTotalOpenBalance());
                            resta = pat.getTotalOpenBalance().subtract(rs.getBigDecimal("grandtotal"));
                            System.out.println("Resta:" + resta);
                            pat.setTotalOpenBalance(resta);
                            pat.save();
                            System.out.println("Actual:" + pat.getTotalOpenBalance());
                            */
                        
                            System.out.println("Anterior:" + totalopenbalance);
                            totalopenbalance = totalopenbalance.subtract(rs.getBigDecimal("grandtotal"));
                            System.out.println("Suma:" + totalopenbalance);
                            
                            sqlUpdateBP = "update c_bpartner set totalopenbalance = " + totalopenbalance + " where c_bpartner_id = " + allPartner[ind];
                            pstmtUpdateBP = DB.prepareStatement(sqlUpdateBP, null);
                            pstmtUpdateBP.executeUpdate();
                            
                            pstmtUpdateBP.close();
                            pstmtUpdateBP = null;
                        
                        
                        
                        
                        
                        
                        } else 
                            System.out.println("Tipo no asignado !!" + c_doctype_id + sql);
                            
                        
                        
                        
                    }
                    rs.close();
                    pstmt.close();
                    pstmt = null;
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                pstmt = null;
            }
            try
            {
                    if (pstmt != null)
                            pstmt.close();
                    pstmt = null;
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());  
                pstmt = null;
            }
            
            
        /*
        }
        
        for(int ind=0;ind<allPartner.length;ind++){
            pat = new MBPartner(Env.getCtx(),allPartner[ind], null);
            String sql = "SELECT * FROM C_Payment WHERE C_BPartner_ID = " + allPartner[ind] + " and datetrx > to_date('"
				+ getFechaFromTimeStamp(fromDate, "/") + "', 'dd,mm,yyyy')";
        */
            
            
            
            sql = "SELECT * FROM C_Payment WHERE C_BPartner_ID = " + allPartner[ind] + " and datetrx > to_date('"
				+ getFechaFromTimeStamp(fromDate, "/") + "', 'dd,mm,yyyy')";
            
            //PreparedStatement pstmt = null;
            try
            {
                    pstmt = DB.prepareStatement(sql, null);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()){
                        int c_doctype_id = rs.getInt("c_doctype_id");
                        
                        // sumo al saldo
                        
                        if(c_doctype_id==1000246 || c_doctype_id==1000134 ||
                           c_doctype_id==1000184 || c_doctype_id==1000185 ||
                           c_doctype_id==1000186 || c_doctype_id==1000250 ||
                           c_doctype_id==5000014 || c_doctype_id==1000248 ||
                           c_doctype_id==1000247 || c_doctype_id==1000242 ||
                           c_doctype_id==5000010 || c_doctype_id==5000004 ||
                           c_doctype_id==5000005 || c_doctype_id==1000135 ||
                           c_doctype_id==1000187 || c_doctype_id==1000188 ||
                           c_doctype_id==1000199 || c_doctype_id==5000006){
                        
                            /*
                            System.out.println("Anterior:" + pat.getTotalOpenBalance());
                            suma = pat.getTotalOpenBalance().add(rs.getBigDecimal("payamt"));
                            System.out.println("Suma:" + suma);
                            pat.setTotalOpenBalance(suma);
                            pat.save();
                            System.out.println("Actual:" + pat.getTotalOpenBalance());
                            */
                            
                            System.out.println("Anterior:" + totalopenbalance);
                            totalopenbalance = totalopenbalance.add(rs.getBigDecimal("payamt"));
                            System.out.println("Suma:" + totalopenbalance);
                            
                            sqlUpdateBP = "update c_bpartner set totalopenbalance = " + totalopenbalance + " where c_bpartner_id = " + allPartner[ind];
                            pstmtUpdateBP = DB.prepareStatement(sqlUpdateBP, null);
                            pstmtUpdateBP.executeUpdate();
                            
                            pstmtUpdateBP.close();
                            pstmtUpdateBP = null;                            
                            
                        }else if(c_doctype_id==1000245 || c_doctype_id==1000138 ||
                           c_doctype_id==1000243 || c_doctype_id==1000244 ||
                           c_doctype_id==5000009 || c_doctype_id==1000189 ||
                           c_doctype_id==1000190 || c_doctype_id==1000191 ||
                           c_doctype_id==1000200){
                        
                            /*
                            pat.setTotalOpenBalance(pat.getTotalOpenBalance().subtract(rs.getBigDecimal("payamt")));
                            System.out.println("Anterior:" + pat.getTotalOpenBalance());
                            resta = pat.getTotalOpenBalance().subtract(rs.getBigDecimal("payamt"));
                            System.out.println("Resta:" + resta);
                            pat.setTotalOpenBalance(resta);
                            pat.save();
                            System.out.println("Actual:" + pat.getTotalOpenBalance());
                             */
                            
                            System.out.println("Anterior:" + totalopenbalance);
                            totalopenbalance = totalopenbalance.subtract(rs.getBigDecimal("payamt"));
                            System.out.println("Suma:" + totalopenbalance);
                            
                            sqlUpdateBP = "update c_bpartner set totalopenbalance = " + totalopenbalance + " where c_bpartner_id = " + allPartner[ind];
                            pstmtUpdateBP = DB.prepareStatement(sqlUpdateBP, null);
                            pstmtUpdateBP.executeUpdate();
                            
                            pstmtUpdateBP.close();
                            pstmtUpdateBP = null;                            
                            
                            
                            
                        } else
                            System.out.println("Tipo no asignado !!" + c_doctype_id + sql);
                        
                        
                    }
                    rs.close();
                    pstmt.close();
                    pstmt = null;
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());    
                pstmt = null;
            }
            try
            {
                    if (pstmt != null)
                            pstmt.close();
                    pstmt = null;
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());    
                pstmt = null;
            }
            
            
        }
        
    	return "success";
        
    }

    /**
     * 
     * @param ts
     * @return
     */
    private String getFechaFromTimeStamp(Timestamp ts, String separador) {
            Calendar gc = new GregorianCalendar();
            gc.setTimeInMillis(ts.getTime());
            String fecha = "";
            int mes = gc.get(Calendar.MONTH) + 1;
            if (gc.get(Calendar.DAY_OF_MONTH) < 10)
                    fecha = "0" + gc.get(Calendar.DAY_OF_MONTH);
            else
                    fecha += gc.get(Calendar.DAY_OF_MONTH);
            fecha += separador;
            if (mes < 10)
                    fecha += "0" + mes;
            else
                    fecha += mes;
            fecha += separador;
            fecha += gc.get(Calendar.YEAR);
            return fecha;
    }    

    protected void prepare() {
        
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++)
        {
            String name = para[i].getParameterName();
            if (name.equals("Datetrx"))
            {
                //p_fecha = para[i].getParameter().toString().substring(0, para[i].getParameter().toString().length()-2);
                fromDate = (Timestamp)para[i].getParameter();
                    
            }
        }
        
        
    	p_instance = getAD_PInstance_ID();
    }
 

}
