/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import org.compiere.model.MAllocationHdr;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.process.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.logging.*;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;
/**
 *  Esta clase inserta tuplas en la tabla temporal T_LIBRO_IVA_VENTA luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
 *	@author BISion
 *	@version 1.0
 */

public class ListarResumen extends SvrProcess{

	int p_instance;
	private int RECORD_ID = 0;
    private String msg = null;
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        RECORD_ID = ((BigDecimal)para[0].getParameter()).intValue();
        p_instance = getAD_PInstance_ID();
    }

    protected String doIt() {
        try {
        	
			DB.executeUpdate("DELETE from T_LIBRODIARIO_ASIENTO", null);
			DB.executeUpdate("DELETE from T_LIBRODIARIO_LINE", null);
            
        	ResultSet rs = getDetalle();
			int secuencia = 100;        	

			while (rs.next())
        	{
				asiento(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(5), rs.getDate(4), rs.getInt(8), rs.getString(9));
				secuencia = renglon(rs.getInt(1), rs.getInt(2), rs.getInt(5),secuencia, rs.getInt(8));
        	}
        	
        	DB.commit(true, get_TrxName());

        } catch (SQLException ex) {
            Logger.getLogger(ListarResumen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        UtilProcess.initViewer("Listado Resumen",p_instance,getProcessInfo());
        
        if (msg!=null)
        	return msg;
        
        return "El proceso ha finalizado con éxito.";
    }

    protected ResultSet getDetalle()
    {
		String sql = " SELECT distinct far.AD_Client_ID,far.AD_Org_ID,fa.C_ACCTSCHEMA_ID,fa.dateacct,far.RECORD_FACT_ID, ac.NAME, y.YEAR, far.TABLE_FACT_ID, fa.FACTNO " +
					 " FROM fact_acct_resumen far " +
					 " INNER JOIN fact_acct fa ON (fa.ad_table_id= far.table_fact_id and fa.record_id= far.record_fact_id) " +
					 " left join C_PERIOD p ON fa.C_PERIOD_ID=p.C_PERIOD_ID " +
					 " left join C_YEAR y ON p.C_YEAR_ID=y.C_YEAR_ID " +
					 " left join AD_CLIENT ac ON fa.AD_CLIENT_ID = ac.AD_CLIENT_ID " +
					 " WHERE far.record_res_id = " + RECORD_ID
    			   + " ORDER BY RECORD_FACT_ID ";	
    	PreparedStatement pstmt = DB.prepareStatement(sql, null);
        
        try {
			ResultSet rs;
			rs = pstmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
		return null;
    }
    
    protected void asiento(int ad_client_id, int ad_org_id, int c_acctschema_id, int record_id, Date date, int AD_Table_ID, String nroAsiento)
    {
       try
       {
    	  if (nroAsiento==null)
    		  nroAsiento="0";
	
    	  String detalle = "";
    	  String sqlInsert = "";
    
	      if (AD_Table_ID==MInvoice.getTableId(MInvoice.Table_Name))
		  {	
				MInvoice invoice = new MInvoice(getCtx(),record_id,null);
				MDocType docType = new MDocType(getCtx(),invoice.getC_DocTypeTarget_ID(),null);
				detalle = docType.getName() + " - " + invoice.getDocumentNo();
		  }
		  else
				if (AD_Table_ID==MPayment.getTableId(MPayment.Table_Name))
				{
					MPayment payment = new MPayment(getCtx(),record_id,null);
					if (payment.isReceipt())
						detalle = "Cobranza / " + payment.getDocumentNo();
					else
						detalle = "Pago / " + payment.getDocumentNo();
				}
				else
					if (AD_Table_ID==MAllocationHdr.getTableId(MAllocationHdr.Table_Name))
					{
						MAllocationHdr allocate = new MAllocationHdr(getCtx(),record_id,null);
						detalle = "Asignación / " + allocate.getDocumentNo();
					}

	       detalle = nroAsiento + " - " + detalle + " - " + ValueFormat.getFechaARG(date);
 	       Timestamp ts = new Timestamp(date.getTime());
	
		   sqlInsert = "insert into T_LIBRODIARIO_ASIENTO values(?,?,?,?,?,?,?,'Y',?,?,?,?)";
		   PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
				
		   pstmtInsert.setInt(1, record_id);
		   pstmtInsert.setInt(2, ad_client_id);
		   pstmtInsert.setInt(3, ad_org_id);
		   pstmtInsert.setInt(4, p_instance);
		   pstmtInsert.setString(5, detalle);
		   pstmtInsert.setTimestamp(6, ts);
		   pstmtInsert.setInt(7, c_acctschema_id);
		   pstmtInsert.setString(8, nroAsiento);
		   pstmtInsert.setLong(9, 1);
		   pstmtInsert.setInt(10, AD_Table_ID);
		   pstmtInsert.setInt(11, RECORD_ID);
			
		   pstmtInsert.executeQuery();
		   DB.commit(true, get_TrxName());
			
		   pstmtInsert.close();
				
		} catch (SQLException ex) {
		    Logger.getLogger(GenerateLibroDiario.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
    
    protected int renglon(int ad_client_id, int ad_org_id, int record_id, int secuencia, int ad_table_id)
    {
        try {
        	String sqlQuery = "",sqlInsert = "";
           
            BigDecimal sumaDR = new BigDecimal(0);
            BigDecimal sumaCR = new BigDecimal(0);
            PreparedStatement pstmtInsert = null;
           
            sqlQuery = 
		            " SELECT ce.value AS NROCUENTA, " +
		            " 		 ce.name AS CUENTA, " +
		            " 		 fa.AMTACCTDR AS DEBITO, " +
		            "		 fa.description " +
		            " FROM Fact_Acct fa " +
		            " INNER JOIN c_elementvalue ce ON (ce.c_elementvalue_id = fa.account_id) " +
		            " WHERE fa.RECORD_ID = ? and fa.ad_table_id=? AND fa.AMTACCTCR = 0 " +
		            " ORDER BY fa.DATEACCT, fa.RECORD_ID";
            
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
            pstmt.setInt(1, record_id);
            pstmt.setInt(2, ad_table_id);
				
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
			
			sqlQuery = 
		            " SELECT ce.value AS NROCUENTA, " +
		            " 		 ce.name AS CUENTA, " +
		            " 		 fa.AMTACCTCR AS CREDITO, " +
		            "		 fa.description " +
		            " FROM Fact_Acct fa " +
		            " INNER JOIN c_elementvalue ce ON (ce.c_elementvalue_id = fa.account_id) " +
		            " WHERE fa.RECORD_ID = ? and fa.ad_table_id=? AND fa.AMTACCTDR = 0 " +
		            " ORDER BY fa.DATEACCT, fa.RECORD_ID";
			
			pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
			pstmt.setInt(1, record_id);
			pstmt.setInt(2, ad_table_id);
				
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
			secuencia++;
			
			pstmtInsert.close();
				
       } catch (SQLException ex) {
           Logger.getLogger(GenerateLibroDiario.class.getName()).log(Level.SEVERE, null, ex);
       }
       return secuencia;
    }
}
