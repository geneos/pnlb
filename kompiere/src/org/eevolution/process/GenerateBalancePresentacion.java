/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.SQLException;



import org.compiere.model.MPeriod;
import org.compiere.process.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Stack;
import java.util.logging.*;

import javax.swing.JOptionPane;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;


/**
 *
 * @author Daniel Gini Bision
 */
public class GenerateBalancePresentacion extends SvrProcess{
    
    int p_instance;
    private Timestamp fromDate;
    private Timestamp toDate;
    private BigDecimal schema;
    private Long num_hoja;
    private String check;
    int org;
        
     protected String doIt() throws Exception{
    	try {
    		
    		String sqlQuery = "";  
        	PreparedStatement pstmt;
        	
        	MPeriod perFrom = MPeriod.get(Env.getCtx(), fromDate);
        	MPeriod perTo = MPeriod.get(Env.getCtx(), toDate);
        	
        	if (perFrom.getC_Year_ID()==perTo.getC_Year_ID())
        	{
        		sqlQuery = "select Distinct AD_CLIENT_ID,0,C_ACCTSCHEMA_ID,NROCUENTA,CUENTA,COMPANIA,ANO from RV_BALANCE where C_ACCTSCHEMA_ID = ? AND DATEACCT between ? and ?";
        		
        		sqlQuery = "SELECT Value, Name , isSummary, ad_org_id " +
        				"  	FROM c_elementvalue " +
        				" 	WHERE length(value) = 10 and ad_client_id = ?" +
        				" 	ORDER BY Value";
        		
        		String anterior = "$";
        		
        		pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
	        	pstmt.setInt(1, getAD_Client_ID());
	        	
	        	ResultSet rs = pstmt.executeQuery();
	        	
	        	try {
	        		
	        		if (rs.next())
	        		{
	        			cabecera(getAD_Client_ID(), rs.getInt(4), 100);
	        		
	        			DB.executeUpdate("DELETE from T_PRESENTACION_LINE", null);
	        			int secuencia=100;
	        			int nivel = -1;
	        			
	        			Stack<BigDecimal> acumulado = new Stack<BigDecimal>();
	        			Stack<Integer> posicion = new Stack<Integer>();
	        			Stack<Boolean> flag = new Stack<Boolean>();
	        			
	        			do	{
	
	        				if (rs.getString(3).equals("Y"))
		        			{
		        				if (!anterior.equals("$"))
		        					nivel = getNroNiveles(anterior, rs.getString(1));
		        				anterior = rs.getString(1);
		        				int pos = nivel;
		        				while (nivel>=0)
	        					{
        							BigDecimal acum = acumulado.remove(acumulado.size()-1);
	        						int seq = posicion.remove(posicion.size()-1);
	        						boolean act = flag.remove(flag.size()-1);
	        						actualizarPresentacion(seq, acum, act, pos-nivel+1);
	        						
	        						if (acumulado.size()>0)		//ACUMULAR PADRE
	        						{	acum = acum.add(acumulado.remove(acumulado.size()-1));
	        							acumulado.add(acum);
	        							if (act)
	        							{
		        							flag.remove(flag.size()-1);
		        							flag.add(act);
	        							}
	        						}
	        						
		        					nivel--;
	        					}
	        					
	        					acumulado.add(BigDecimal.ZERO);
	        					posicion.add(secuencia);
	        					flag.add(false);
	        					secuencia = insertarPresentacion(secuencia, rs.getString(1), rs.getString(2), null, rs.getInt(4));
		        			}
	        				else
	        				{
	        					BigDecimal suma = getBalanceCuenta(rs.getString(1),rs.getString(2));
	        					
	        			    	if (suma.compareTo(BigDecimal.ZERO)!=0)
	        			    	{	
	        			    		secuencia = insertarPresentacion(secuencia, rs.getString(1), rs.getString(2), suma, rs.getInt(4));
	        			    		// ACTUALIZAR PADRE
		        			    	BigDecimal acum = acumulado.remove(acumulado.size()-1);
		        			    	acum = acum.add(suma);
		        			    	acumulado.add(acum);
	        			    		flag.remove(flag.size()-1);
	        			    		flag.add(true);
	        			    	}
	        			    	else
	        			    		if (check.equals("Y"))
	        			    		{	
	        			    			secuencia = insertarPresentacion(secuencia, rs.getString(1), rs.getString(2), suma, rs.getInt(4));
	        			    			//	 ACTUALIZAR PADRE
			        			    	flag.remove(flag.size()-1);
		        			    		flag.add(true);
	        			    		}
	        				}
	        				
	        				DB.commit(false, null);
	        				
	        			}	while (rs.next());
	        		    			
	        			nivel = getNroNiveles(anterior, "0");
	        			int pos = nivel;
	        			while (nivel>0)
    					{
							BigDecimal acum = acumulado.remove(acumulado.size()-1);
    						int seq = posicion.remove(posicion.size()-1);
    						boolean act = flag.remove(flag.size()-1);
    						actualizarPresentacion(seq, acum, act, pos-nivel+1);
    						
    						if (acumulado.size()>0)		//ACUMULAR PADRE
    						{	acum = acum.add(acumulado.remove(acumulado.size()-1));
    							acumulado.add(acum);
    							if (act)
    							{
        							flag.remove(flag.size()-1);
        							flag.add(act);
    							}
    						}
    						
        					nivel--;
    					}
	        		}
	        		
	        		UtilProcess.initViewer("Balance de Presentacion",p_instance,getProcessInfo());
	        		
	        		rs.close();
	        		pstmt.close();
	        	}
	        	catch (Exception exception) {
	        		exception.printStackTrace();
	        	}
        	}
        	else
        		JOptionPane.showMessageDialog(null,"El rango de fechas no pertenece a un único ejercicio comercial.","Parámetros",JOptionPane.INFORMATION_MESSAGE);
    
     	}	
     	catch (SQLException ex) {
    	 Logger.getLogger(GenerateBalancePresentacion.class.getName()).log(Level.SEVERE, null, ex);
    	}
      
        return "success"; 
    }
     
     protected void cabecera(int ad_client_id, int ad_org_id, int secuencia)
     {
    	 	String sqlInsert = "";
            PreparedStatement pstmtInsert = null;
            String sqlRemove = "delete from T_PRESENTACION_HDR";
            DB.executeUpdate(sqlRemove, null);
            
            org = ad_org_id;
            
            try {
                sqlInsert = "insert into T_PRESENTACION_HDR values(?,?,?,?,'Y',?,?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
                
                pstmtInsert.setInt(1, secuencia);
                pstmtInsert.setInt(2, schema.intValue());
                pstmtInsert.setInt(3, ad_client_id);
                pstmtInsert.setInt(4, ad_org_id);
                pstmtInsert.setTimestamp(5, fromDate);
                pstmtInsert.setTimestamp(6, toDate);
                pstmtInsert.setInt(7, p_instance);
                pstmtInsert.setLong(8, num_hoja);
                pstmtInsert.setString(9, check);
                	
                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
            }
            catch (SQLException ex) {
        		Logger.getLogger(GeneratePagoCompRet.class.getName()).log(Level.SEVERE, null, ex);
        	} 
    }
     
	protected BigDecimal getBalanceCuenta(String nroCuenta, String cuenta)
	{
		 BigDecimal suma = new BigDecimal(0);
		 
		 try {
			 
			 String sqlQuery = "SELECT SUM(DEBITO),SUM(CREDITO) FROM RV_BALANCE WHERE NROCUENTA = ? AND CUENTA = ? AND DATEACCT between ? and ?";
			 PreparedStatement pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
			 pstmt.setString(1, nroCuenta);
			 pstmt.setString(2, cuenta);
			 pstmt.setTimestamp(3, fromDate);
			 pstmt.setTimestamp(4, toDate);
	        
			 ResultSet rs = pstmt.executeQuery();
			
			 if (rs.next())
			 {
				 if (rs.getBigDecimal(2)!=null && rs.getBigDecimal(1)!=null)
					 suma = rs.getBigDecimal(1).subtract(rs.getBigDecimal(2));
				 else
					 if (rs.getBigDecimal(1)!=null)
						 suma = rs.getBigDecimal(1);
					 else
						 if (rs.getBigDecimal(2)!=null)
							 suma = rs.getBigDecimal(2).negate();
			}
			
			rs.close();
			pstmt.close();
	
	    } catch (SQLException ex) {
	        Logger.getLogger(GenerateBalancePresentacion.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    
		return suma;
	}
    
    protected int getNroNiveles(String summary1, String summary2)	{
     	
     	int index = summary1.indexOf("0");
     	summary1 = summary1.substring(0, index);
     	summary1 = summary1.replace(".", "");
     	
     	index = summary2.indexOf("0");
     	summary2 = summary2.substring(0, index);
     	summary2 = summary2.replace(".", "");
     	
     	return summary1.length()-summary2.length();
    }
     
    protected void actualizarPresentacion(int secuencia, BigDecimal suma, boolean actualizar, int nivel){
    	
    	if (actualizar)
    	{
    		switch (nivel)	{
    		case 1: {	DB.executeUpdate("UPDATE T_PRESENTACION_LINE SET NIVEL1 = "+suma+" WHERE T_PRESENTACION_LINE_ID = " + secuencia, null);
    				 	break;}
    		case 2: {	DB.executeUpdate("UPDATE T_PRESENTACION_LINE SET NIVEL2 = "+suma+" WHERE T_PRESENTACION_LINE_ID = " + secuencia, null);
    					break;}
    		case 3: {	DB.executeUpdate("UPDATE T_PRESENTACION_LINE SET NIVEL3 = "+suma+" WHERE T_PRESENTACION_LINE_ID = " + secuencia, null);
    					break;}
    		case 4: {	DB.executeUpdate("UPDATE T_PRESENTACION_LINE SET NIVEL4 = "+suma+" WHERE T_PRESENTACION_LINE_ID = " + secuencia, null);
    					break;}
    		default:{ 	break;} 
    		}
    	}
    	else
    		DB.executeUpdate("DELETE FROM T_PRESENTACION_LINE WHERE T_PRESENTACION_LINE_ID = " + secuencia, null);
    }
    
    protected int insertarPresentacion(int secuencia, String nroCuenta, String cuenta, BigDecimal suma, int ad_org_id) {
    	try
	    {		
			String sqlInsert = "insert into T_PRESENTACION_LINE values(?,?,?,?,?,'Y',?,?,?,null,null,null,null)";
			PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
			
			pstmtInsert.setInt(1, secuencia);
			pstmtInsert.setInt(2, schema.intValue());
			pstmtInsert.setInt(3, getAD_Client_ID());
			pstmtInsert.setInt(4, ad_org_id);
			pstmtInsert.setInt(5, getAD_PInstance_ID());
			pstmtInsert.setString(6, nroCuenta);
			pstmtInsert.setString(7, cuenta);
			pstmtInsert.setBigDecimal(8, suma);
			
			pstmtInsert.executeQuery();
			DB.commit(true, get_TrxName());
		
			pstmtInsert.close();
			
			secuencia++;
		}
		catch (Exception exception) {
			exception.printStackTrace(); 
		}
		
		return secuencia;
    }
     
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
    	
        schema = (BigDecimal)para[0].getParameter();
        
        for (int i = 0; i < para.length; i++)
		{
            String name = para[i].getParameterName();
            if(name.equals("PAGINA")){
                num_hoja= ((BigDecimal) para[i].getParameter()).longValue();
                Env.getCtx().put("typePrint", "LIBRO");
                Env.getCtx().put("startPage", num_hoja);
            }
            else	{
            	if (name.equals("FECHAD"))
           		{	
           			fromDate = (Timestamp)para[i].getParameter();
               	   	toDate=(Timestamp)para[i].getParameter_To();
              	}
            	else
            		if (name.equals("CHEK"))
               			check = (String)para[i].getParameter();
            }
		}
        
        if (fromDate == null && toDate == null)
		{
			Calendar cal = TimeUtil.getToday();
			fromDate = new Timestamp(cal.getTime().getYear(),0,1,0,0,0,0);
			toDate = new Timestamp(cal.getTime().getYear(),11,31,0,0,0,0);
		}
		else
			if (fromDate == null)
				fromDate = new Timestamp(toDate.getYear(),0,1,0,0,0,0);
    		else
				if (toDate == null)
	    			toDate = new Timestamp(fromDate.getYear(),11,31,0,0,0,0);
    	
       	p_instance = getAD_PInstance_ID();         
    }
}
