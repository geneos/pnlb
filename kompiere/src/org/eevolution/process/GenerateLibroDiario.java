/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.compiere.model.MAllocationHdr;
import org.compiere.model.MDocType;
import org.compiere.model.MFactAcct;
import org.compiere.model.MJournal;
import org.compiere.model.MMOVIMIENTOFONDOS;
import org.compiere.model.MPayment;
import org.compiere.model.MInvoice;
import org.compiere.process.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.logging.*;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;


/**
 *
 * @author Daniel Gini Bision
 */
public class GenerateLibroDiario extends SvrProcess{
    
    int p_instance;
    private Timestamp fromDate;
    private Timestamp toDate;
    private BigDecimal schema;
    private int table_id = -1;
    private Long num_hoja = new Long(0);
    long org;
        
     protected String doIt() throws Exception{
    	try {
    		
    		String sqlQuery = "";  
        	PreparedStatement pstmt;
        		
    		if (table_id != -1)
        	{
    			sqlQuery = "select Distinct AD_CLIENT_ID,AD_ORG_ID,C_ACCTSCHEMA_ID,DATEACCT,RECORD_ID,COMPANIA,ANO,AD_TABLE_ID,FACTNO from RV_LIBRODIARIO where C_ACCTSCHEMA_ID = ? AND DATEACCT between ? and ? AND AD_TABLE_ID = ? Order By DATEACCT, FACTNO";
    			pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
    			pstmt.setInt(1, schema.intValue());
    			pstmt.setTimestamp(2, fromDate);
    			pstmt.setTimestamp(3, toDate);
    			pstmt.setInt(4, table_id);
        	}
    		else
    		{
    			sqlQuery = "select Distinct AD_CLIENT_ID,AD_ORG_ID,C_ACCTSCHEMA_ID,DATEACCT,RECORD_ID,COMPANIA,ANO,AD_TABLE_ID,FACTNO from RV_LIBRODIARIO where C_ACCTSCHEMA_ID = ? AND DATEACCT between ? and ? Order By DATEACCT, FACTNO";
    			pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
    			pstmt.setInt(1, schema.intValue());
    			pstmt.setTimestamp(2, fromDate);
    			pstmt.setTimestamp(3, toDate);
    		}
    		
        	ResultSet rs = pstmt.executeQuery();
        	
        	try {
        		
        		if (rs.next())
        		{
        		//	cabecera(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(6), rs.getInt(7));
        		
        			DB.executeUpdate("DELETE from T_LIBRODIARIOASIENTO", null);
        			DB.executeUpdate("DELETE from T_LIBRODIARIO_LINE", null);
        			int secuencia = 100;
        			
        			do	{
        				asiento(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(5), rs.getDate(4), rs.getInt(8),rs.getString(9));
        				secuencia = renglon(rs.getInt(1), rs.getInt(2), rs.getInt(5),secuencia,rs.getInt(8));
        			}	while (rs.next());
        			
        			UtilProcess.initViewer("Libro Diario General",p_instance,getProcessInfo());
        		}
        		
        		rs.close();
        		pstmt.close();
        	}
        	catch (Exception exception) {
        		exception.printStackTrace();
        	} 
     	}	
     	catch (SQLException ex) {
    	 Logger.getLogger(GenerateLibroDiario.class.getName()).log(Level.SEVERE, null, ex);
    	}
      
        return "success"; 
    }

     protected void asiento(int ad_client_id, int ad_org_id, int c_acctschema_id, int record_id, Date date, int AD_Table_ID, String nroAsiento)
     {
        try
        {
        	if (nroAsiento==null)
        		nroAsiento="00000000";
        	
        	String detalle = null;
        	String sqlInsert = "";
            
        	String sqlQuery = "SELECT CONCEPTO,AD_TABLE_ID,RECORD_ID,DESCRIPTION FROM RV_LIBRODIARIO WHERE RECORD_ID = ? ORDER BY DATEACCT";
        	PreparedStatement pstmtInsert = DB.prepareStatement(sqlQuery,get_TrxName());
        	pstmtInsert.setInt(1, record_id);
				
			ResultSet rs = pstmtInsert.executeQuery();
				
			if (rs.next())
			{
				if (rs.getInt(2)==MJournal.getTableId(MJournal.Table_Name))
				{	
					String tableName = MJournal.Table_Name;
					sqlQuery = "SELECT DESCRIPTION FROM " + tableName +" WHERE " + tableName +"_ID = ?";
					PreparedStatement ps = DB.prepareStatement(sqlQuery,get_TrxName());
					ps.setInt(1, rs.getInt(3));
					ResultSet set = ps.executeQuery();
					
					if (set.next())
						detalle = set.getString(1);
				}
				else					
					if (rs.getInt(2)==MInvoice.getTableId(MInvoice.Table_Name))
					{	
						MInvoice invoice = new MInvoice(getCtx(),rs.getInt(3),null);
						MDocType docType = new MDocType(getCtx(),invoice.getC_DocTypeTarget_ID(),null);
						detalle = docType.getName() + " - " + invoice.getDocumentNo();
					}
					else
						if (rs.getInt(2)==MFactAcct.getTableId(MFactAcct.Table_Name))
						{	
							detalle = rs.getString(4);
							
							sqlQuery = "SELECT RECORD_FACT_ID FROM FACT_ACCT_RESUMEN WHERE RECORD_RES_ID = ? AND TABLE_FACT_ID = " + MPayment.getTableId(MPayment.Table_Name);
							PreparedStatement ps = DB.prepareStatement(sqlQuery,get_TrxName());
							ps.setInt(1, rs.getInt(3));
							ResultSet set = ps.executeQuery();
							
							if (set.next())
							{	MPayment payment = new MPayment(getCtx(),set.getInt(1),null);
								if (detalle!=null)
									detalle += " - " + payment.getDocumentNo();
								else
									detalle = payment.getDocumentNo();
							}
							else
							{	sqlQuery = "SELECT RECORD_FACT_ID FROM FACT_ACCT_RESUMEN WHERE RECORD_RES_ID = ? AND TABLE_FACT_ID = " + MInvoice.getTableId(MInvoice.Table_Name);
								ps = DB.prepareStatement(sqlQuery,get_TrxName());
								ps.setInt(1, rs.getInt(3));
								set = ps.executeQuery();
								
								if (set.next())
								{	MInvoice invoice = new MInvoice(getCtx(),set.getInt(1),null);
									if (detalle!=null)
										detalle += " - " + invoice.getDocumentNo();
									else
										detalle = invoice.getDocumentNo();
								}
							}		
						}
						else
							if (rs.getInt(2)==MMOVIMIENTOFONDOS.getTableId(MMOVIMIENTOFONDOS.Table_Name))
							{	
								MMOVIMIENTOFONDOS movFondos = new MMOVIMIENTOFONDOS(getCtx(),rs.getInt(3),null);
								movFondos.getStringTipo();
								detalle = movFondos.getStringTipo() + "_" + movFondos.getDocumentNo();
							}
							else
								if (rs.getInt(2)==MPayment.getTableId(MPayment.Table_Name))
								{
									MPayment payment = new MPayment(getCtx(),rs.getInt(3),null);
									if (payment.isReceipt())
										detalle = "Cobranza / " + payment.getDocumentNo();
									else
										detalle = "Pago / " + payment.getDocumentNo();
								}
								else
									if (rs.getInt(2)==MAllocationHdr.getTableId(MAllocationHdr.Table_Name))
									{
										MAllocationHdr allocate = new MAllocationHdr(getCtx(),rs.getInt(3),null);
										detalle = "Asignación / " + allocate.getDocumentNo();
									}
									else
										detalle = rs.getString(1);
			}
			rs.close();
			pstmtInsert.close();
			
			detalle = rellenarNro(nroAsiento) + " - " + detalle + " - " + ValueFormat.getFechaARG(date);
			
			try
            {
    			Timestamp ts = new Timestamp(date.getTime());
    			
    			sqlInsert = "insert into T_LIBRODIARIOASIENTO values(?,?,?,?,?,?,?,'Y',?,?,?)";
  				pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
  				
    			pstmtInsert.setInt(1, record_id);
    			pstmtInsert.setInt(2, ad_client_id);
    			pstmtInsert.setInt(3, ad_org_id);
    			pstmtInsert.setInt(4, p_instance);
    			pstmtInsert.setString(5, detalle);
    			pstmtInsert.setTimestamp(6, ts);
    			pstmtInsert.setInt(7, c_acctschema_id);
    			pstmtInsert.setString(8, rellenarNro(nroAsiento));
    			pstmtInsert.setLong(9, num_hoja);
    			pstmtInsert.setInt(10, AD_Table_ID);
    			
    			pstmtInsert.executeQuery();
    			DB.commit(true, get_TrxName());
    			
    			pstmtInsert.close();
    			
            } catch (Exception exception) {
                exception.printStackTrace();
            }            
    		
        } catch (SQLException ex) {
            Logger.getLogger(GenerateLibroDiario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     }
     
     protected String rellenarNro(String nro)
     {
     	if (nro!=null) {
 	    	switch (nro.length()) {
 	        case 1:
 	        	return "0000000" + nro;
 	        case 2:
 	        	return "000000" + nro;
 	        case 3:
 	        	return "00000" + nro;
 	        case 4:
 	        	return "0000" + nro;
 	        case 5:
 	        	return "000" + nro;
 	        case 6:
 	        	return "00" + nro;
 	        case 7:
 	        	return "0" + nro;
 	        default:
 	        	return nro;
 	    	}
     	}	
     	else return "00000000";
     }
     
    protected int renglon(int ad_client_id, int ad_org_id, int record_id, int secuencia, int ad_table_id)
     {
         try {
              String sqlQuery = "",sqlInsert = "";
            
              BigDecimal sumaDR = new BigDecimal(0);
              BigDecimal sumaCR = new BigDecimal(0);
              PreparedStatement pstmtInsert = null;
            
              sqlQuery = "SELECT NROCUENTA,CUENTA,DEBITO,DESCRIPTION FROM RV_LIBRODIARIO WHERE RECORD_ID = ? AND CREDITO = 0";
              PreparedStatement pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
              pstmt.setInt(1, record_id);
			
              ResultSet rs = pstmt.executeQuery();
			
              if (rs.next())
              {
				do	{
					String descripcion = "";
					if (ad_table_id==MPayment.getTableId(MPayment.Table_Name) && rs.getString(4)!=null)
					{
						MPayment pay = new MPayment(getCtx(),record_id,null);
						if (!rs.getString(4).equals(pay.getDocumentNo()))
							descripcion = rs.getString(4);
					}
					else
						if (ad_table_id==MMOVIMIENTOFONDOS.getTableId(MMOVIMIENTOFONDOS.Table_Name) && rs.getString(4)!=null)
							descripcion = rs.getString(4);

					sqlInsert = "insert into T_LIBRODIARIO_LINE values(?,?,?,?,?,?,?,?,?,?,'Y',?)";
					pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
					
					pstmtInsert.setInt(1, secuencia);
					pstmtInsert.setInt(2, record_id);
					pstmtInsert.setInt(3, record_id);
					pstmtInsert.setString(4, rs.getString(1));
					pstmtInsert.setString(5, rs.getString(2));
					pstmtInsert.setBigDecimal(6, rs.getBigDecimal(3));
					pstmtInsert.setBigDecimal(7, null);
					pstmtInsert.setInt(8, p_instance);
					pstmtInsert.setInt(9, ad_client_id);
					pstmtInsert.setInt(10, ad_org_id);
					pstmtInsert.setString(11, descripcion);
					
					pstmtInsert.executeQuery();
	    			DB.commit(true, get_TrxName());
				
	    			pstmtInsert.close();
	    			
	    			secuencia++;
	    			sumaDR = sumaDR.add(rs.getBigDecimal(3));
	    			
				}	while (rs.next());
              }
			
              rs.close();
              pstmt.close();
			
              sqlQuery = "SELECT NROCUENTA,CUENTA,CREDITO,DESCRIPTION FROM RV_LIBRODIARIO WHERE RECORD_ID = ? AND DEBITO = 0";
              pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
              pstmt.setInt(1, record_id);
				
              rs = pstmt.executeQuery();
			
			if (rs.next())
    		{
				do	{
					String descripcion = "";
					if (ad_table_id==MPayment.getTableId(MPayment.Table_Name) && rs.getString(4)!=null)
					{
						MPayment pay = new MPayment(getCtx(),record_id,null);
						if (!rs.getString(4).equals(pay.getDocumentNo()))
							descripcion = rs.getString(4);
					}
					else
						if (ad_table_id==MMOVIMIENTOFONDOS.getTableId(MMOVIMIENTOFONDOS.Table_Name) && rs.getString(4)!=null)
							descripcion = rs.getString(4);
					
					sqlInsert = "insert into T_LIBRODIARIO_LINE values(?,?,?,?,?,?,?,?,?,?,'Y',?)";
					pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
					
					pstmtInsert.setInt(1, secuencia);
					pstmtInsert.setInt(2, record_id);
					pstmtInsert.setInt(3, record_id);
					pstmtInsert.setString(4, rs.getString(1));
					pstmtInsert.setString(5, rs.getString(2));
					pstmtInsert.setBigDecimal(6, null);
					pstmtInsert.setBigDecimal(7, rs.getBigDecimal(3));
					pstmtInsert.setInt(8, p_instance);
					pstmtInsert.setInt(9, ad_client_id);
					pstmtInsert.setInt(10, ad_org_id);
					pstmtInsert.setString(11, descripcion);
						            
	    			pstmtInsert.executeQuery();
	    			DB.commit(true, get_TrxName());

	    			pstmtInsert.close();
	    			
	    			secuencia++;
	    			sumaCR = sumaCR.add(rs.getBigDecimal(3));
	    			
				}	while (rs.next());
    		}
			
			rs.close();
			pstmt.close();
			
			sqlInsert = "insert into T_LIBRODIARIO_LINE values(?,?,?,?,?,?,?,?,?,?,'Y',?)";
			pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
			
			pstmtInsert.setInt(1, secuencia);
			pstmtInsert.setInt(2, record_id);
			pstmtInsert.setInt(3, record_id);
			pstmtInsert.setString(4, null);
			pstmtInsert.setString(5, null);
			pstmtInsert.setBigDecimal(6, sumaDR);
			pstmtInsert.setBigDecimal(7, sumaCR);
			pstmtInsert.setInt(8, p_instance);
			pstmtInsert.setInt(9, ad_client_id);
			pstmtInsert.setInt(10, ad_org_id);
			pstmtInsert.setString(11, "");
			
			pstmtInsert.executeQuery();
			DB.commit(true, get_TrxName());

			secuencia++;
			
			pstmtInsert.close();
				
        } catch (SQLException ex) {
            ex.printStackTrace();
        	Logger.getLogger(GenerateLibroDiario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return secuencia;
     }

     protected void cabecera(int ad_client_id, int ad_org_id, int c_acctschema_id, String compania, int ano)
     {
    	 	String sqlInsert = "";
            PreparedStatement pstmtInsert = null;
            String sqlRemove = "delete from T_LIBRODIARIO_HDR";
            DB.executeUpdate(sqlRemove, null);
            
            org = ad_org_id;
            
            try {
                    sqlInsert = "insert into T_LIBRODIARIO_HDR values(?,?,?,?,?,?,'Y')";
                    pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
                    
                    pstmtInsert.setInt(1, c_acctschema_id);
                    pstmtInsert.setString(2, compania);
                    pstmtInsert.setInt(3, ano);
                    pstmtInsert.setInt(4, p_instance);
                    pstmtInsert.setInt(5, ad_client_id);
                    pstmtInsert.setInt(6, ad_org_id);
                    
                    pstmtInsert.executeQuery();
                    DB.commit(true, get_TrxName());
                
            }
            catch (SQLException ex) {
        		Logger.getLogger(GeneratePagoCompRet.class.getName()).log(Level.SEVERE, null, ex);
        	} 
    }

    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
    	
        schema = (BigDecimal)para[0].getParameter();
        for (int i = 0; i < para.length; i++)
        {
            String name = para[i].getParameterName();
            if(name.equals("FECHA")){
            	fromDate = (Timestamp)para[i].getParameter();
           	   	toDate=(Timestamp)para[i].getParameter_To();
            }
            else
            	if(name.equals("TIPO")){
            		table_id = ((BigDecimal) para[i].getParameter()).intValue();
                }
            	else
            		if(name.equals("PAGINA")){
                   	   	num_hoja= ((BigDecimal) para[i].getParameter()).longValue();
                   	}
        }
       	Env.getCtx().put("typePrint", "LIBRO");
   	   	Env.getCtx().put("startPage", num_hoja);
   	   	
       	p_instance = getAD_PInstance_ID();
    }    

}
