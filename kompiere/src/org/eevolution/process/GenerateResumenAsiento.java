/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MDocType;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.model.MSequence;
import org.compiere.process.*;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.logging.*;

import org.compiere.util.*;
/**
 *  Esta clase inserta tuplas en la tabla temporal T_LIBRO_IVA_VENTA luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
 *	@author BISion
 *	@version 1.0
 */
public class GenerateResumenAsiento extends SvrProcess{

    //private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    private Integer AD_CLIENT_ID = 0;
    private Integer AD_ORG_ID = 0;
    private Integer C_ACCTSCHEMA_ID = 0;
    private Boolean invoice = false;
    private Integer AD_TABLE_ID = 0;
    private Integer RECORD_ID = 0;
    private String msg = null;
    private BigDecimal C_Period_ID = BigDecimal.ZERO;
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        BigDecimal SCHEMA = BigDecimal.ZERO;
        for (int i = 0; i < para.length; i++)
        {
            String name = para[i].getParameterName();
            if(name.equals("DATEACCT")){
            	fromDate=(Timestamp)para[i].getParameter();
            }
            else
            	if(name.equals("IsActive")){
            		if (((String)para[i].getParameter()).equals("Y"))
            			invoice = true;
            		else
            			invoice = false;
                }
            	else
            		if(name.equals("C_Period_ID")){
            			C_Period_ID = (BigDecimal)para[i].getParameter();
            			MPeriod per = MPeriod.get(Env.getCtx(), C_Period_ID.intValue(), get_TrxName());
            			fromDate=per.getStartDate();
            			toDate = per.getEndDate();
                    }
                	else
                		SCHEMA = ((BigDecimal)para[i].getParameter());
        }
  	  
    	if (toDate == null)
    		toDate = fromDate;
    	
        C_ACCTSCHEMA_ID = SCHEMA.intValue();
    }

   
    protected String doIt() {
        try {
        	
    		if (invoice && (!MPeriod.isOpen(Env.getCtx(), fromDate, MDocType.DOCBASETYPE_ARCreditMemo) || !MPeriod.isOpen(Env.getCtx(), toDate, MDocType.DOCBASETYPE_ARInvoice) || !MPeriod.isOpen(Env.getCtx(), toDate, MDocType.DOCBASETYPE_ARProFormaInvoice)))
        		msg = "El proceso ha sido finalizado. No se encuentra abierto el per�odo para todos los tipos de documentos en Factura de Ventas."; 
    		else
    		{
	        	loadRecord();
                        /*
                         *  13/12/12 Zynnia
                         *  Se anula este metodo para que no cree asientos de resumen en FACT_ACCT
                         * 
                         */
	        	//createFacts(loadResumen());
	        	DB.commit(true, get_TrxName());
        	}
        } catch (SQLException ex) {
            Logger.getLogger(GenerateResumenAsiento.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (msg!=null)
        	return msg;
        
        return "El proceso ha finalizado con �xito.";
    }

    protected void createFacts(ResultSet rs)
    {
    	if (invoice)
    	{	char ch = revisarSuperposicion();
    		switch (ch) {
    			case 'S': {	msg = "El proceso ha sido finalizado. Superposici�n de Resumenes";
    						break;	}
    			case 'A': {	msg = "El proceso ha sido finalizado. El resumen solicitado se encuentra inclu�do en otro.";
							break;	}
    			case 'I': {	
                                        /*
                                         *  13/12/12 Zynnia
                                         *  Se anula este metodo para que no cree asientos de resumen en FACT_ACCT
                                         * 
                                         */
                                        deleteResumenes();	
                                        //createFactsInvoice(rs);
    						break;	}
    			case 'O': {	
                                        /*
                                         *  13/12/12 Zynnia
                                         *  Se anula este metodo para que no cree asientos de resumen en FACT_ACCT
                                         * 
                                         */
                                        //createFactsInvoice(rs);
    						break;	}
    			case 'P': {break;}
    		}
    	}
    	else
    	{	
                deleteResumenes();
                /*
                 *  13/12/12 Zynnia
                 *  Se anula este metodo para que no cree asientos de resumen en FACT_ACCT
                 * 
                 */
    		//createFactsReceipt(rs);
    	}
    	
	    try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    protected char revisarSuperposicion()
    {
    	try {
    		String sql = " SELECT FACT_ACCT_RESUMEN_ID " +
    					 " FROM FACT_ACCT_RESUMEN " +
    					 " WHERE ((? >= FROMDATE AND ? <= TODATE AND ? > TODATE) " +
    					 "	  OR (? >= FROMDATE AND ? <= TODATE AND ? < FROMDATE)) " +
    					 "	  AND INVOICE = 'Y'";	    	
    		
	    	PreparedStatement ps = DB.prepareStatement(sql,null);
	    	ps.setTimestamp(1, fromDate);
	    	ps.setTimestamp(2, fromDate);
	    	ps.setTimestamp(3, toDate);
	    	
	    	ps.setTimestamp(4, toDate);
	    	ps.setTimestamp(5, toDate);
	    	ps.setTimestamp(6, fromDate);
	    	
	    	ResultSet rs;
			rs = ps.executeQuery();
			if (rs.next())
	    		return 'S';
	    	
	    	rs.close();
	    	ps.close();

	    	sql = " SELECT FACT_ACCT_RESUMEN_ID " +
			  " FROM FACT_ACCT_RESUMEN " +
			  " WHERE ? <= FROMDATE AND ? >= TODATE " +
			  "	  AND INVOICE = 'Y'";	    	

		  	ps = DB.prepareStatement(sql,null);
		  	ps.setTimestamp(1, fromDate);
		  	ps.setTimestamp(2, toDate);
		  	
		  	rs = ps.executeQuery();
		  	if (rs.next())
		  		return 'I';
		  	
		  	rs.close();
		  	ps.close();
		  	
	    	sql = " SELECT FACT_ACCT_RESUMEN_ID " +
				  " FROM FACT_ACCT_RESUMEN " +
				  " WHERE ? >= FROMDATE AND ? <= TODATE " +
				  "	  AND ? >= FROMDATE AND ? <= TODATE " +
  		  		  "	  AND INVOICE = 'Y'";	    	

	    	ps = DB.prepareStatement(sql,null);
	    	ps.setTimestamp(1, fromDate);
	    	ps.setTimestamp(2, fromDate);
	    	ps.setTimestamp(3, toDate);
	    	ps.setTimestamp(4, toDate);
	    	
	    	rs = ps.executeQuery();
	    	if (rs.next())
	    		return 'A';
	    	
	    	rs.close();
	    	ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return 'P';
		}

		return 'O';
    }
    
    protected void deleteResumenes()
    {
    	try {
    		
    		String sql;
    		
    		if (invoice)
		   		sql = " SELECT DISTINCT TABLE_RES_ID, RECORD_RES_ID " +
			  		  " FROM FACT_ACCT_RESUMEN " +
			  		  " WHERE FROMDATE >= ? AND TODATE <= ? " +
			  		  "	  AND INVOICE = 'Y' AND TABLE_FACT_ID <> " + MPayment.getTableId(MPayment.Table_Name);
    		else
		   		sql = " SELECT DISTINCT TABLE_RES_ID, RECORD_RES_ID " +
		  		  " FROM FACT_ACCT_RESUMEN " +
		  		  " WHERE FROMDATE >= ? AND TODATE <= ? " +
		  		  "	  AND INVOICE = 'N' AND TABLE_FACT_ID = " + MPayment.getTableId(MPayment.Table_Name);
    			

	    	PreparedStatement ps = DB.prepareStatement(sql,null);
	    	ps.setTimestamp(1, fromDate);
	    	ps.setTimestamp(2, toDate);
	    	
	    	ResultSet rs = ps.executeQuery();
	    	while (rs.next())
	    	{
	    		sql = " DELETE FROM FACT_ACCT " +
	    			  " WHERE AD_TABLE_ID = "+ rs.getInt(1) +" AND RECORD_ID = " + rs.getInt(2);
				DB.executeUpdate(sql,null);
	    	}
	    	
	    	sql = " DELETE FROM FACT_ACCT_RESUMEN " +
	    		  " WHERE FROMDATE >= ? AND TODATE <= ? " +
  		  		  "	  AND INVOICE = 'Y'";	    	
	    	
	    	
	    	ps = DB.prepareStatement(sql,null);
	    	ps.setTimestamp(1, fromDate);
	    	ps.setTimestamp(2, toDate);
	    	ps.executeUpdate();
	    	
	    	rs.close();
	    	ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

	protected void createFactsInvoice(ResultSet rs)
	{
    	MAcctSchema as = MAcctSchema.get(Env.getCtx(), C_ACCTSCHEMA_ID);
		int LINE_ID = 1;
		try{
			while (rs.next())
			{	
				if (!rs.getBigDecimal(2).equals(BigDecimal.ZERO) || !rs.getBigDecimal(3).equals(BigDecimal.ZERO))
				{	
                                        /*
                                         *  13/12/12 Zynnia
                                         *  Se anula este metodo para que no cree asientos de resumen en FACT_ACCT
                                         * 
                                         */
                                        //createFactLine(LINE_ID,rs.getInt(1),"Resumen de Ventas",rs.getBigDecimal(2),rs.getBigDecimal(3), as.getC_Currency_ID(),toDate);
					LINE_ID++;
				}	
                        }
                /*
                 *  13/12/12 Zynnia
                 *  Se anula este metodo para que no cree asientos de resumen en FACT_ACCT
                 * 
                 */
		//mappingInvoice();
		}catch(SQLException e){
    		e.printStackTrace();
    	}
	}
	
	protected void createFactsReceipt(ResultSet rs)
	{
		MAcctSchema as = MAcctSchema.get(Env.getCtx(), C_ACCTSCHEMA_ID);
		try{
			while (rs.next())
			{	ResultSet rst = getResumenCobranza(rs.getInt(1));
				int LINE_ID = 1;
				while (rst.next()) 
				{	
					if (!rst.getBigDecimal(2).equals(BigDecimal.ZERO) || !rst.getBigDecimal(3).equals(BigDecimal.ZERO))
					{	
                                                /*
                                                 *  13/12/12 Zynnia
                                                 *  Se anula este metodo para que no cree asientos de resumen en FACT_ACCT
                                                 * 
                                                 */
                                                //createFactLine(LINE_ID,rst.getInt(1),"Resumen de Cobranza",rst.getBigDecimal(2),rst.getBigDecimal(3),as.getC_Currency_ID(),rst.getTimestamp(4));
						LINE_ID++;
					}	
				}
                                /*
                                 *  13/12/12 Zynnia
                                 *  Se anula este metodo para que no cree asientos de resumen en FACT_ACCT
                                 * 
                                 */
				//mappingReceipt(rs.getInt(1));
				RECORD_ID++;
				rst.close();
			}
		}catch(SQLException e){
    		e.printStackTrace();
    	}
	}
    
    private void createFactLine(int LINE_ID,int account_id,String descripcion,BigDecimal cargo, BigDecimal abono, int currency_id, Timestamp dateacct)
    {
        /*
         *  13/12/12 Zynnia
         *  Se anula este metodo dado que crea asientos de resumen en FACT_ACCT que no tienen que salir.
         * 
         */
        
      /*  String sqlInsert = 
			" INSERT INTO FACT_ACCT (FACT_ACCT_ID, AD_CLIENT_ID, AD_ORG_ID, ISACTIVE, CREATED, CREATEDBY, UPDATED, " +
	    	"		UPDATEDBY, C_ACCTSCHEMA_ID, ACCOUNT_ID, DATETRX, DATEACCT, C_PERIOD_ID, AD_TABLE_ID, RECORD_ID, DESCRIPTION," +
	    	"		LINE_ID, GL_CATEGORY_ID, POSTINGTYPE, C_CURRENCY_ID, AMTSOURCEDR, AMTSOURCECR, AMTACCTDR, AMTACCTCR) " +
	    	" VALUES (?,?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    	PreparedStatement pstmt = DB.prepareStatement(sqlInsert, get_TrxName());
    	
    	try	{
    		pstmt.setLong(1, MSequence.getNextID(AD_CLIENT_ID, "Fact_Acct", get_TrxName()));
	    	pstmt.setLong(2, AD_CLIENT_ID);
	    	pstmt.setLong(3, AD_ORG_ID);
	    	pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
	    	pstmt.setLong(5, getAD_User_ID());
	    	pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
	    	pstmt.setLong(7, getAD_User_ID());
	    	pstmt.setLong(8, C_ACCTSCHEMA_ID);
	    	pstmt.setLong(9, account_id);
	    	pstmt.setTimestamp(10, dateacct);
	    	pstmt.setTimestamp(11, dateacct);
	    	pstmt.setLong(12, MPeriod.getC_Period_ID(Env.getCtx(), dateacct));
	    	pstmt.setLong(13, AD_TABLE_ID);
	    	pstmt.setLong(14, RECORD_ID);
	    	pstmt.setString(15, descripcion);
			pstmt.setInt(16, LINE_ID);
	    	pstmt.setLong(17, 0);
	    	pstmt.setString(18, "A");
	    	pstmt.setLong(19, currency_id);
	    	pstmt.setBigDecimal(20, cargo);
	    	pstmt.setBigDecimal(21, abono);
	    	pstmt.setBigDecimal(22, cargo);
	    	pstmt.setBigDecimal(23, abono);
    	
	    	pstmt.executeQuery();
	        DB.commit(true, get_TrxName());
    	}catch (SQLException e)	{
    		e.printStackTrace();
    	}
      */
    }
    
    protected ResultSet loadResumen()
	{
    	String sqlQuery = "";
    	PreparedStatement pstmt = null;
    	
    	try {
	    	if (invoice)
			{
	    		sqlQuery = 
	    			" SELECT  ACCOUNT_ID," +
	    			" CASE WHEN SUM(AMTACCTDR)>SUM(AMTACCTCR) THEN SUM(AMTACCTDR)-SUM(AMTACCTCR) ELSE 0 END AS DEBITO," +
	    			" CASE WHEN SUM(AMTACCTDR)<SUM(AMTACCTCR) THEN SUM(AMTACCTCR)-SUM(AMTACCTDR) ELSE 0 END AS CREDITO" +
	    			" FROM FACT_ACCT F" +
	    			" INNER JOIN AD_TABLE T  ON (T.AD_TABLE_ID=F.AD_TABLE_ID)" +
	    			" INNER JOIN C_INVOICE I  ON (I.C_INVOICE_ID=F.RECORD_ID)" +
	    			" WHERE T.TABLENAME = 'C_Invoice' AND I.ISSOTRX = 'Y' AND DATEACCT BETWEEN ? AND ?" +
	    			" AND I.ISACTIVE = 'Y' AND F.ISACTIVE = 'Y' AND C_ACCTSCHEMA_ID = ?" +
	    			" GROUP BY ACCOUNT_ID";
	    		
	    		pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
		        pstmt.setTimestamp(1, fromDate);
		        pstmt.setTimestamp(2, toDate);
		        pstmt.setLong(3, C_ACCTSCHEMA_ID);
			}
			else
			{
				/*sqlQuery = 
					" SELECT ACCOUNT_ID," +
	    			"        CASE WHEN SUM(DEBITO)>SUM(CREDITO) THEN SUM(DEBITO)-SUM(CREDITO) ELSE 0 END AS DEBITO," +
	    			"        CASE WHEN SUM(DEBITO)<SUM(CREDITO) THEN SUM(CREDITO)-SUM(DEBITO) ELSE 0 END AS CREDITO" +
	    			" FROM( " +
	    			" 	SELECT  ACCOUNT_ID," +
	    			" 	CASE WHEN SUM(AMTACCTDR)>SUM(AMTACCTCR) THEN SUM(AMTACCTDR)-SUM(AMTACCTCR) ELSE 0 END AS DEBITO," +
	    			" 	CASE WHEN SUM(AMTACCTDR)<SUM(AMTACCTCR) THEN SUM(AMTACCTCR)-SUM(AMTACCTDR) ELSE 0 END AS CREDITO" +
	    			" 	FROM FACT_ACCT F" +
	    			" 	INNER JOIN AD_TABLE T  ON (T.AD_TABLE_ID=F.AD_TABLE_ID)" +
	    			" 	INNER JOIN C_PAYMENT P  ON (P.C_PAYMENT_ID=F.RECORD_ID)" +
	    			" 	WHERE T.TABLENAME = 'C_Payment' AND P.ISRECEIPT = 'Y' AND DATEACCT BETWEEN ? AND ?" +
	    			" 	AND P.ISACTIVE = 'Y' AND F.ISACTIVE = 'Y' AND C_ACCTSCHEMA_ID = ?" +
	    			" 	GROUP BY ACCOUNT_ID" +
	    			" " +
	    			"  UNION" +
	    			" 	SELECT  ACCOUNT_ID," +
	    			" 	CASE WHEN SUM(AMTACCTDR)>SUM(AMTACCTCR) THEN SUM(AMTACCTDR)-SUM(AMTACCTCR) ELSE 0 END AS DEBITO," +
	    			" 	CASE WHEN SUM(AMTACCTDR)<SUM(AMTACCTCR) THEN SUM(AMTACCTCR)-SUM(AMTACCTDR) ELSE 0 END AS CREDITO" +
	    			" 	FROM FACT_ACCT F" +
	    			" 	INNER JOIN AD_TABLE T  ON (T.AD_TABLE_ID=F.AD_TABLE_ID)" +
	    			" 	INNER JOIN C_ALLOCATIONHDR H  ON (H.C_ALLOCATIONHDR_ID=F.RECORD_ID)" +
	    			" 	WHERE T.TABLENAME = 'C_AllocationHdr' AND DATEACCT BETWEEN ? AND ?" +
	    			" 	AND H.ISACTIVE = 'Y' AND F.ISACTIVE = 'Y' AND C_ACCTSCHEMA_ID = ?" +
	    			" 	GROUP BY ACCOUNT_ID" +
	    			" 	)" +
	    			" GROUP BY ACCOUNT_ID";
	    			
				pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
		        pstmt.setTimestamp(1, fromDate);
		        pstmt.setTimestamp(2, toDate);
		        pstmt.setLong(3, C_ACCTSCHEMA_ID);
		        pstmt.setTimestamp(4, fromDate);
		        pstmt.setTimestamp(5, toDate);
		        pstmt.setLong(6, C_ACCTSCHEMA_ID);
		        */
				sqlQuery = 
					" 	SELECT DISTINCT RECORD_ID " +
	    			" 	FROM FACT_ACCT F" +
	    			" 	INNER JOIN AD_TABLE T ON (T.AD_TABLE_ID=F.AD_TABLE_ID)" +
	    			" 	INNER JOIN C_PAYMENT P ON (P.C_PAYMENT_ID=F.RECORD_ID)" +
	    			" 	WHERE T.TABLENAME = 'C_Payment' AND P.ISRECEIPT = 'Y' AND DATEACCT BETWEEN ? AND ?" +
	    			" 	AND P.ISACTIVE = 'Y' AND F.ISACTIVE = 'Y' AND C_ACCTSCHEMA_ID = ?";
	    			
				pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
		        pstmt.setTimestamp(1, fromDate);
		        pstmt.setTimestamp(2, toDate);
		        pstmt.setLong(3, C_ACCTSCHEMA_ID);
			}	
	    	
	    	return pstmt.executeQuery();
    	}catch (SQLException e){
    		e.printStackTrace();
    	}
    	
    	return null;
	}
    
    private void mappingInvoice()
    {
        /*
         *  13/12/12 Zynnia
         *  Se anula este metodo para que no cree asientos de resumen en FACT_ACCT
         * 
         */
        
    /*    
    	String sqlQuery = "";
    	PreparedStatement pstmt = null;
    	long FACT_ACCT_RESUMEN_ID = 0;
    	
    	try {
    		sqlQuery = "SELECT MAX(FACT_ACCT_RESUMEN_ID) FROM FACT_ACCT_RESUMEN";
    		pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
    		ResultSet rs = pstmt.executeQuery();
    		if (rs.next())
    			FACT_ACCT_RESUMEN_ID = rs.getLong(1);
    		rs.close();
    		pstmt.close();
    		
    		sqlQuery = 
    			" SELECT DISTINCT F.AD_TABLE_ID, F.RECORD_ID" +
    			" FROM FACT_ACCT F" +
    			" INNER JOIN AD_TABLE T  ON (T.AD_TABLE_ID=F.AD_TABLE_ID)" +
    			" INNER JOIN C_INVOICE I  ON (I.C_INVOICE_ID=F.RECORD_ID)" +
    			" WHERE T.TABLENAME = 'C_Invoice' AND I.ISSOTRX = 'Y' AND F.DATEACCT BETWEEN ? AND ?" +
    			" AND I.ISACTIVE = 'Y' AND F.ISACTIVE = 'Y' AND C_ACCTSCHEMA_ID = ?";
    		
    		pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
	        pstmt.setTimestamp(1, fromDate);
	        pstmt.setTimestamp(2, toDate);
	        pstmt.setLong(3, C_ACCTSCHEMA_ID);
			
	    	rs = pstmt.executeQuery();
	    	while (rs.next()) {
	    		FACT_ACCT_RESUMEN_ID++;
	    		sqlQuery = "Insert into FACT_ACCT_RESUMEN values(?,?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,'Y','Y')";
	    		pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
	    		pstmt.setLong(1, FACT_ACCT_RESUMEN_ID);
	            pstmt.setLong(2, AD_CLIENT_ID);
	            pstmt.setLong(3, AD_ORG_ID);
	            
	            pstmt.setLong(4, C_ACCTSCHEMA_ID);
	            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
		    	pstmt.setLong(6, getAD_User_ID());
		    	pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
		    	pstmt.setLong(8, getAD_User_ID());
		    	pstmt.setLong(9, AD_TABLE_ID);
	            pstmt.setLong(10, RECORD_ID);
	            pstmt.setLong(11, rs.getLong(1));
	            pstmt.setLong(12, rs.getLong(2));
	            pstmt.setTimestamp(13, fromDate);
	            pstmt.setTimestamp(14, toDate);
	            
	            pstmt.executeQuery();
	            DB.commit(true, get_TrxName());
	    	}
	    	
    	}catch (SQLException e){
    		e.printStackTrace();
    	}
     */
    }
    
    private void mappingReceipt(int c_payment_id)
    {
        /*
         * 13/12/12 Zynnia 
         * Se anula este metodo para que no se creen asientos de resumen en FACT_ACCT.
         * 
         */
     /*   
    	String sqlQuery = "";
    	PreparedStatement pstmt = null;
    	long FACT_ACCT_RESUMEN_ID = 0;
    	
    	try {
    		sqlQuery = "SELECT MAX(FACT_ACCT_RESUMEN_ID) FROM FACT_ACCT_RESUMEN";
    		pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
    		ResultSet rs = pstmt.executeQuery();
    		if (rs.next())
    			FACT_ACCT_RESUMEN_ID = rs.getLong(1);
    		rs.close();
    		pstmt.close();
    		
	    	sqlQuery = 
    			" SELECT DISTINCT F.AD_TABLE_ID, F.RECORD_ID" +
    			" FROM FACT_ACCT F" +
    			" INNER JOIN AD_TABLE T  ON (T.AD_TABLE_ID=F.AD_TABLE_ID)" +
    			" INNER JOIN C_PAYMENT P  ON (P.C_PAYMENT_ID=F.RECORD_ID)" +
    			" WHERE T.TABLENAME = 'C_Payment' AND P.ISRECEIPT = 'Y' AND F.DATEACCT BETWEEN ? AND ?" +
    			"   AND P.ISACTIVE = 'Y' AND F.ISACTIVE = 'Y' AND F.C_ACCTSCHEMA_ID = ? AND F.RECORD_ID = ?" +
    			" " +
    			" UNION" +
    			" " +
    			" SELECT DISTINCT F.AD_TABLE_ID, F.RECORD_ID" +
    			" FROM FACT_ACCT F" +
    			" INNER JOIN AD_TABLE T  ON (T.AD_TABLE_ID=F.AD_TABLE_ID)" +
    			" WHERE T.TABLENAME = 'C_AllocationHdr' AND F.DATEACCT BETWEEN ? AND ?" +
    			"   AND F.ISACTIVE = 'Y' AND F.C_ACCTSCHEMA_ID = ?" +
    			"   AND F.record_id IN ( SELECT distinct al.C_ALLOCATIONHDR_ID" +
    			"		    			 FROM C_ALLOCATIONLINE al" +
    			"			   			 INNER JOIN C_ALLOCATIONHDR ah ON (ah.C_ALLOCATIONHDR_ID = al.C_ALLOCATIONHDR_ID)" +
    			"		    			 WHERE al.c_Payment_ID = ? )"; 
    			    			
			pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
	        pstmt.setTimestamp(1, fromDate);
	        pstmt.setTimestamp(2, toDate);
	        pstmt.setLong(3, C_ACCTSCHEMA_ID);
	        pstmt.setLong(4, c_payment_id);
	        pstmt.setTimestamp(5, fromDate);
	        pstmt.setTimestamp(6, toDate);
	        pstmt.setLong(7, C_ACCTSCHEMA_ID);
	        pstmt.setLong(8, c_payment_id);
	    	
	    	rs = pstmt.executeQuery();
	    	while (rs.next()) {
	    		FACT_ACCT_RESUMEN_ID++;
	    		sqlQuery = "Insert into FACT_ACCT_RESUMEN values(?,?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,'N','Y')";
	    		pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
	    		pstmt.setLong(1, FACT_ACCT_RESUMEN_ID);
	            pstmt.setLong(2, AD_CLIENT_ID);
	            pstmt.setLong(3, AD_ORG_ID);
	            
	            pstmt.setLong(4, C_ACCTSCHEMA_ID);
	            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
		    	pstmt.setLong(6, getAD_User_ID());
		    	pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
		    	pstmt.setLong(8, getAD_User_ID());
		    	pstmt.setLong(9, AD_TABLE_ID);
	            pstmt.setLong(10, RECORD_ID);
	            pstmt.setLong(11, rs.getLong(1));
	            pstmt.setLong(12, rs.getLong(2));
	            pstmt.setTimestamp(13, fromDate);
	            pstmt.setTimestamp(14, toDate);
	            
	            pstmt.executeQuery();
	            DB.commit(true, get_TrxName());
	    	}
	    	
    	}catch (SQLException e){
    		e.printStackTrace();
    	}
      */
    }
    
    private void loadRecord()
    {
		MAcctSchema as = MAcctSchema.get(Env.getCtx(), C_ACCTSCHEMA_ID);
    	AD_CLIENT_ID = as.getAD_Client_ID();
        AD_ORG_ID = as.getAD_Org_ID();

    	try {
    		String sqlQuery = 
	   			" SELECT AD_TABLE_ID FROM AD_TABLE WHERE TABLENAME = 'Fact_Acct'";// AND AD_CLIENT_ID = ?";
	
		    PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
		    //pstmt.setLong(1, AD_CLIENT_ID);
			ResultSet rs = pstmt.executeQuery();
	    	
	    	if (rs.next())
	    		AD_TABLE_ID = rs.getInt(1);
	    	
	    	sqlQuery = 
	   			" SELECT" +
	   			" CASE WHEN MAX(F.RECORD_ID) is null THEN 1" +
	   			"      ELSE MAX(F.RECORD_ID) + 1" +
	   			" END as NEXT_RECORD" +
	   			" FROM FACT_ACCT F" +
	   			" INNER JOIN AD_TABLE T  ON (T.AD_TABLE_ID=F.AD_TABLE_ID)" +
	   			" WHERE T.TABLENAME = 'Fact_Acct' AND F.C_ACCTSCHEMA_ID = ?";

	    	pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
	    	pstmt.setLong(1, C_ACCTSCHEMA_ID);
			rs = pstmt.executeQuery();
	    	
	    	if (rs.next())
	    		RECORD_ID = rs.getInt(1);
	    	
    	}
    	catch (SQLException e){
    		e.printStackTrace();
    	}
    }
    
    private ResultSet getResumenCobranza(int C_Payment_ID)
    {
    	String sql = 
			    " SELECT ACCOUNT_ID," +
			    "    CASE WHEN SUM(DEBITO)>SUM(CREDITO) THEN SUM(DEBITO)-SUM(CREDITO) ELSE 0 END AS DEBITO," +
			    "    CASE WHEN SUM(DEBITO)<SUM(CREDITO) THEN SUM(CREDITO)-SUM(DEBITO) ELSE 0 END AS CREDITO," +
			    "    DATEACCT " +
			    " FROM (" +
			    " 		SELECT ACCOUNT_ID," +
			    "			   CASE WHEN SUM(AMTACCTDR)>SUM(AMTACCTCR) THEN SUM(AMTACCTDR)-SUM(AMTACCTCR) ELSE 0 END AS DEBITO," +
			    "			   CASE WHEN SUM(AMTACCTDR)<SUM(AMTACCTCR) THEN SUM(AMTACCTCR)-SUM(AMTACCTDR) ELSE 0 END AS CREDITO," +
			    "	    DATEACCT " +
			    "		FROM Fact_Acct fa" +
			    "		INNER JOIN AD_TABLE t ON (t.AD_TABLE_ID = fa.AD_TABLE_ID)" +
			    "		WHERE  t.TABLENAME = 'C_AllocationHdr' AND DATEACCT BETWEEN ? AND ?" +
			    "		  AND fa.ISACTIVE = 'Y' AND C_ACCTSCHEMA_ID = ?" +
			    "		  AND record_id IN (  SELECT distinct al.C_ALLOCATIONHDR_ID" +
			    "						      FROM C_ALLOCATIONLINE al" +
			    "			                  INNER JOIN C_ALLOCATIONHDR ah ON (ah.C_ALLOCATIONHDR_ID = al.C_ALLOCATIONHDR_ID)" +
			    "						      WHERE al.c_Payment_ID = ? )" +
			    "		GROUP BY ACCOUNT_ID,DATEACCT " +
			    "		UNION" +
			    "		SELECT ACCOUNT_ID," +
			    "			   CASE WHEN SUM(AMTACCTDR)>SUM(AMTACCTCR) THEN SUM(AMTACCTDR)-SUM(AMTACCTCR) ELSE 0 END AS DEBITO," +
			    "			   CASE WHEN SUM(AMTACCTDR)<SUM(AMTACCTCR) THEN SUM(AMTACCTCR)-SUM(AMTACCTDR) ELSE 0 END AS CREDITO," +
			    "	    DATEACCT " +
			    "		FROM Fact_Acct fa" +
			    "		INNER JOIN AD_TABLE t ON (t.AD_TABLE_ID = fa.AD_TABLE_ID)" +
			    "		WHERE t.TABLENAME = 'C_Payment' AND DATEACCT BETWEEN ? AND ?" +
			    "		  AND fa.ISACTIVE = 'Y' AND C_ACCTSCHEMA_ID = ? AND fa.record_id = ?" +
			    "		GROUP BY ACCOUNT_ID,DATEACCT " +
			    "	 )" +
			    " GROUP BY ACCOUNT_ID,DATEACCT ";
    	
    	PreparedStatement pst = DB.prepareStatement(sql, get_TrxName());
    	try {
			pst.setTimestamp(1, fromDate);
			pst.setTimestamp(2, toDate);
	    	pst.setLong(3, C_ACCTSCHEMA_ID);
	    	pst.setLong(4, C_Payment_ID);
	    	pst.setTimestamp(5, fromDate);
	    	pst.setTimestamp(6, toDate);
	    	pst.setLong(7, C_ACCTSCHEMA_ID);
	    	pst.setLong(8, C_Payment_ID);
	    	
	    	return pst.executeQuery();
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return null;
    }
}
