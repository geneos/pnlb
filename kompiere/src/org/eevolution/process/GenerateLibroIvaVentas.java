/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import org.compiere.model.MDocType;
import org.compiere.process.*;
import java.math.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
public class GenerateLibroIvaVentas extends SvrProcess{

    //Máximo que permite la base de datos para los campos String
	private static int N = 30;
	
	private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    private Long num_hoja;    
    SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
    private Long AD_CLIENT_ID;
    private Long AD_ORG_ID;
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++)
        {
            String name = para[i].getParameterName();
            if(name.equals("NRO_HOJA")){
                num_hoja= ((BigDecimal) para[i].getParameter()).longValue();
                Env.getCtx().put("typePrint", "LIBRO");
                Env.getCtx().put("startPage", num_hoja);
            }
            else{
                fromDate=(Timestamp)para[i].getParameter();
                toDate=(Timestamp)para[i].getParameter_To();
            }
            p_PInstance_ID = getAD_PInstance_ID();            
        }
        AD_CLIENT_ID = new Long(getAD_Client_ID());
        AD_ORG_ID = new Long(0);
    }

    protected String doIt() {
        try {
            loadTrasnporte();
        	loadLibro();
            setearTemp();
            //tablaBase();
            loadDebitoFiscal();
            loadCreditoFiscal();
            loadRetenciones();
            loadPercepciones();
            //sector2();
            DB.commit(true, get_TrxName());
            UtilProcess.initViewer("Libro IVA Ventas",p_PInstance_ID,getProcessInfo());
        } catch (SQLException ex) {
            Logger.getLogger(GenerateLibroIvaVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private Timestamp getFechaInicial()
    {
    	Date date = new Date(fromDate.getYear(), fromDate.getMonth(), 1);
    	Timestamp ts = new Timestamp(date.getTime());
    	return ts;
    }
    
    BigDecimal transGravado = BigDecimal.ZERO, transIVADebito = BigDecimal.ZERO;
    BigDecimal transOtros = BigDecimal.ZERO, transExento = BigDecimal.ZERO;
    BigDecimal transTotal = BigDecimal.ZERO;
    
	protected void loadTrasnporte()
	{
		// Verificar que no se solicite desde el princiopio de período
		 
		if (!TimeUtil.isSameDay(fromDate, getFechaInicial()))
		{
			BigDecimal grabado=BigDecimal.ZERO,exento=BigDecimal.ZERO, total= BigDecimal.ZERO,ivaDebito=BigDecimal.ZERO,otros=BigDecimal.ZERO;
			
			String sqlQuery = " Select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,0,DATETRX,C_DOCTYPE_ID,COMPROBANTE_ID,0,COMPROBANTE,NRO_COMPROBANTE,RAZON_SOCIAL,CUIT,REGRETEN,REGESPECIAL,NETLINE,DOCSTATUS from RV_LIBRO_IVA" +
		    " where ( DATEACCT between ? and ? )";
			 // Realice la consulta y guardo los resultados en la tabla
		    try {
		    	PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
		        pstmt.setTimestamp(1, getFechaInicial());
		        pstmt.setTimestamp(2, TimeUtil.addDays(fromDate, -1));
		        ResultSet rs = pstmt.executeQuery();
		        while (rs.next()) {
		        	AD_CLIENT_ID = rs.getLong(1);
		            AD_ORG_ID = rs.getLong(2);
		        	BigDecimal impuesto_iva=BigDecimal.ZERO,impuesto_exento=BigDecimal.ZERO,NETO = rs.getBigDecimal(15),PERCEPCIONIB = rs.getBigDecimal(14),iva_parcial=BigDecimal.ZERO;
		            String STATUS = rs.getString(16);
		            if (PERCEPCIONIB==null)
		            	PERCEPCIONIB=BigDecimal.ZERO; 
		            	
		            //	Iva Debito Fiscal
		            String sqlQuery3 ="select taxamt,c_tax_id,TAXBASEAMT from c_invoicetax where c_invoice_id="+rs.getLong(7);
		            PreparedStatement pstmt3 = DB.prepareStatement(sqlQuery3,null);
		            ResultSet rs3 = pstmt3.executeQuery();
		            MDocType doctype = MDocType.get(Env.getCtx(), rs.getInt(6));
		            while(rs3.next()){
		                if(doctype.getDocBaseType().equals(MDocType.DOCBASETYPE_ARInvoice)){
		                    if(rs3.getLong(2)==1000052 || rs3.getLong(2)==1000053 || rs3.getLong(2)==1000054)
		                    {    iva_parcial = iva_parcial.add(rs3.getBigDecimal(1));
		                    	 impuesto_iva = rs3.getBigDecimal(3);
		                    }
		                }
		                else
		                if(doctype.getDocBaseType().equals(MDocType.DOCBASETYPE_ARCreditMemo)){
		                    if(rs3.getLong(2)==1000052 || rs3.getLong(2)==1000053 || rs3.getLong(2)==1000054){
		                        iva_parcial = iva_parcial.subtract(rs3.getBigDecimal(1));
		                        PERCEPCIONIB = PERCEPCIONIB.multiply(new BigDecimal(-1));
		                        impuesto_iva = rs3.getBigDecimal(3).multiply(new BigDecimal(-1));
		                    }
		                }
		            }
		            impuesto_exento = rs.getBigDecimal(15).subtract(impuesto_iva);
		            if (!STATUS.equals("VO"))
		            {  if (rs.getString(13)==null)
		            	   NETO = PERCEPCIONIB.add(iva_parcial).add(impuesto_exento).add(impuesto_iva);
		            }
		            else
		            {	
		         	    impuesto_iva=BigDecimal.ZERO;
		                impuesto_exento=BigDecimal.ZERO;
		                iva_parcial=BigDecimal.ZERO;
		                PERCEPCIONIB=BigDecimal.ZERO;
		                NETO=BigDecimal.ZERO;
		            }
		            
		            grabado = grabado.add(impuesto_iva);
                    exento = exento.add(impuesto_exento); 
                    ivaDebito = ivaDebito.add(iva_parcial);
                    otros = otros.add(PERCEPCIONIB);
                    total = total.add(NETO);
		            
		        }
		        
		        total = grabado.add(exento.add(ivaDebito.add(otros)));
		        
		        transExento = exento.setScale(2, BigDecimal.ROUND_HALF_UP);
		        transOtros = otros.setScale(2, BigDecimal.ROUND_HALF_UP);
		        transGravado = grabado.setScale(2, BigDecimal.ROUND_HALF_UP);
		        transIVADebito = ivaDebito.setScale(2, BigDecimal.ROUND_HALF_UP);
		        transTotal = total.setScale(2, BigDecimal.ROUND_HALF_UP);
		    } catch (SQLException ex) {
		        Logger.getLogger(GenerateLibroIvaVentas.class.getName()).log(Level.SEVERE, null, ex);
		    }
		}
	}
	
    protected void loadLibro(){

            String sqlQuery3="";
            PreparedStatement pstmt=null,pstmt3=null;
            PreparedStatement pstmtInsert = null;
            String sqlInsert;
            BigDecimal NETO=BigDecimal.ZERO,grabado=BigDecimal.ZERO,exento=BigDecimal.ZERO,ivaDebito=BigDecimal.ZERO,otros=BigDecimal.ZERO,total=BigDecimal.ZERO;
             BigDecimal iva_parcial = BigDecimal.ZERO, PERCEPCIONIB=BigDecimal.ZERO,impuesto_iva=BigDecimal.ZERO,impuesto_exento=BigDecimal.ZERO;
            //auxiliares
            Date DATETRX = null;
            String RAZON_SOCIAL="",STATUS="";

            //List queries = new ArrayList();

            log.info("Comienzo del proceso de generación de LIBRO IVA DE VENTAS");
            log.info("borrado de la tabla temporal T_LIBRO_IVA_VENTA");
            String sqlRemove = "delete from T_LIBRO_IVA_VENTA";
            DB.executeUpdate(sqlRemove, null);
            boolean ok=false;

            if (transTotal.compareTo(BigDecimal.ZERO)!=0)
            {
            	Timestamp transp = new Timestamp(fromDate.getTime()); 
            	transp = TimeUtil.addDays(transp, -1);
            	String fecha = "";
            	if(transp.getDate()<10) fecha = "0"+transp.getDate(); else fecha += transp.getDate();
            	if(transp.getMonth()+1 < 10)  fecha+= "/0"+(transp.getMonth()+1); else fecha+= "/"+(transp.getMonth()+1);
            	fecha +="/"+(transp.getYear()+1900);
            
	            sqlInsert = "INSERT into T_LIBRO_IVA_VENTA values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
	            try {
					pstmtInsert.setLong(1, AD_CLIENT_ID);
					pstmtInsert.setLong(2, AD_ORG_ID);
		            pstmtInsert.setString(3, "Y");
		            pstmtInsert.setLong(4, p_PInstance_ID);
		            pstmtInsert.setDate(5, new Date(fromDate.getTime() + 1000));
		            pstmtInsert.setString(6, null);
		            pstmtInsert.setString(7, null);
		            pstmtInsert.setString(8, null);
		            pstmtInsert.setString(9, "TRASNPORTE");
		            
		            pstmtInsert.setBigDecimal(10, transGravado);
		            pstmtInsert.setBigDecimal(11, transExento);
		            pstmtInsert.setBigDecimal(12, transIVADebito);
		            pstmtInsert.setString(13, null);
		            pstmtInsert.setBigDecimal(14, transOtros);
		            pstmtInsert.setBigDecimal(15, transTotal);
		            pstmtInsert.setTimestamp(16, transp);
		            //pstmtInsert.setString(16, fecha);
		
		            pstmtInsert.executeQuery();
		            DB.commit(true, null);
		            pstmtInsert.close();
	            } catch (SQLException e) {
					e.printStackTrace();
				}
            }

            log.info("Consulta de la vista RV_LIBRO_IVA, se filtra por el rango indicado");
            String sqlQuery = " Select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,0,DATETRX,C_DOCTYPE_ID,COMPROBANTE_ID,0,COMPROBANTE,NRO_COMPROBANTE,RAZON_SOCIAL,CUIT,REGRETEN,REGESPECIAL,NETLINE,DOCSTATUS,DATEACCT from RV_LIBRO_IVA" +
                       " where ( DATEACCT between ? and ? )";
            
            // Realice la consulta y guardo los resultados en la tabla
            try {
                pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
                pstmt.setTimestamp(1, fromDate);
                pstmt.setTimestamp(2, toDate);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
	                    ok=true;
	                    impuesto_iva=BigDecimal.ZERO;impuesto_exento=BigDecimal.ZERO;     
	                   	// 	Para obtener el cliente y la organizacion
	                    AD_CLIENT_ID = rs.getLong(1);
	                    AD_ORG_ID = rs.getLong(2);
	                    
	                    RAZON_SOCIAL = rs.getString(11);
	                    NETO = rs.getBigDecimal(15);
	                    STATUS = rs.getString(16);
	                    DATETRX = rs.getDate(17);
	                    PERCEPCIONIB = rs.getBigDecimal(14);
	                    if (PERCEPCIONIB==null)
	                    	PERCEPCIONIB=BigDecimal.ZERO; 
	                    	
	                    //	Debido a que no anda el dateformat
	                    String fecha = "";
	                    if(DATETRX.getDate()<10) fecha = "0"+DATETRX.getDate(); else fecha += DATETRX.getDate();
	                    if(DATETRX.getMonth()+1 < 10)  fecha+= "/0"+(DATETRX.getMonth()+1); else fecha+= "/"+(DATETRX.getMonth()+1);
	                    fecha +="/"+(DATETRX.getYear()+1900);
	                    
	                    /*ISTAXEXEMPT
	                    if(rs.getLong(4)==1000052 || rs.getLong(4)==1000053 || rs.getLong(4)==1000054){
	                    	impuesto_iva = impuesto_iva.add(rs.getBigDecimal(15));
	                    }
	                    else
	                    	if(rs.getLong(4)==1000055 || rs.getLong(4)==1000056)
	                    		impuesto_exento = impuesto_exento.add(rs.getBigDecimal(15));
	                    */
	                    
	                    //	Iva Debito Fiscal
                        iva_parcial=BigDecimal.ZERO;
                        
                        Calendar cal = new GregorianCalendar(); 
                        cal.setTime(new Date(toDate.getTime()));
                        cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        
                        sqlQuery3 ="select taxamt,c_tax_id,TAXBASEAMT from c_invoicetax where c_invoice_id="+rs.getLong(7);
                        pstmt3 = DB.prepareStatement(sqlQuery3,null);
                        ResultSet rs3 = pstmt3.executeQuery();
                        
                        MDocType doctype = MDocType.get(Env.getCtx(), rs.getInt(6));
                        
                        while(rs3.next()){
                            if(doctype.getDocBaseType().equals(MDocType.DOCBASETYPE_ARInvoice)){
                                if(rs3.getLong(2)==1000052 || rs3.getLong(2)==1000053 || rs3.getLong(2)==1000054)
                                {   iva_parcial = iva_parcial.add(rs3.getBigDecimal(1));
                                	impuesto_iva = rs3.getBigDecimal(3);
                                }
                                //else
                                	//if(rs.getLong(2)==1000055 || rs.getLong(2)==1000056)
                                		//impuesto_exento = rs3.getBigDecimal(3);
                            }
                            else
	                            if(doctype.getDocBaseType().equals(MDocType.DOCBASETYPE_ARCreditMemo))
	                            {
	                            	if(rs3.getLong(2)==1000052 || rs3.getLong(2)==1000053 || rs3.getLong(2)==1000054){
	                                    iva_parcial = iva_parcial.subtract(rs3.getBigDecimal(1));
	                                    impuesto_iva = rs3.getBigDecimal(3).multiply(new BigDecimal(-1));
	                                }
	                            	PERCEPCIONIB = PERCEPCIONIB.multiply(new BigDecimal(-1));
	                            }
                        }
                        if (rs.getString(13)==null)
                        	impuesto_exento = rs.getBigDecimal(15).subtract(impuesto_iva);
                        
                        sqlInsert = "INSERT into T_LIBRO_IVA_VENTA values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                        pstmtInsert.setLong(1, AD_CLIENT_ID);
                        pstmtInsert.setLong(2, AD_ORG_ID);
                        pstmtInsert.setString(3, rs.getString(3));
                        pstmtInsert.setLong(4, p_PInstance_ID);
                        pstmtInsert.setDate(5, new Date(fromDate.getTime() + 1000));
                        pstmtInsert.setString(6, rs.getString(9));
                        pstmtInsert.setString(7, rs.getString(10));
                        if (!STATUS.equals("VO"))
                        {
                     	   if (RAZON_SOCIAL.length()>N)
                     		   pstmtInsert.setString(8, RAZON_SOCIAL.substring(0, N-1));
                            else
                         	   pstmtInsert.setString(8, RAZON_SOCIAL);
                     	   
                     	   pstmtInsert.setString(9, rs.getString(12));
                           pstmtInsert.setBigDecimal(10, impuesto_iva);
                           pstmtInsert.setBigDecimal(11, impuesto_exento);
                           pstmtInsert.setBigDecimal(12, iva_parcial);
                            
                           pstmtInsert.setString(13, rs.getString(13));
                           
                           pstmtInsert.setBigDecimal(14, PERCEPCIONIB);
                           if (rs.getString(13)==null)
                        	   NETO = PERCEPCIONIB.add(iva_parcial).add(impuesto_exento).add(impuesto_iva);
                           pstmtInsert.setBigDecimal(15, NETO);      

                        }
                        else
                        {	
                     	   pstmtInsert.setString(8, "ANULADO");
                     	   pstmtInsert.setString(9, null);
                            pstmtInsert.setBigDecimal(10, null);
                            pstmtInsert.setBigDecimal(11, null);
                            pstmtInsert.setBigDecimal(12, null);
                            pstmtInsert.setString(13, null);
                            pstmtInsert.setBigDecimal(14, null);
                            pstmtInsert.setBigDecimal(15, null);    
                            impuesto_iva=BigDecimal.ZERO;
                            impuesto_exento=BigDecimal.ZERO;
                            iva_parcial=BigDecimal.ZERO;
                            PERCEPCIONIB=BigDecimal.ZERO;
                        }
                        pstmtInsert.setTimestamp(16, new Timestamp(DATETRX.getTime()));
                        //pstmtInsert.setString(16, fecha);
                        
                        pstmtInsert.executeQuery();
                        
                        grabado = grabado.add(impuesto_iva);
                        exento = exento.add(impuesto_exento); 
                        ivaDebito = ivaDebito.add(iva_parcial);
                        otros = otros.add(PERCEPCIONIB);
                        total = total.add(NETO);
                        DB.commit(true, get_TrxName());
                }

                rs.close();
                pstmt.close();       
                total = grabado.add(exento.add(ivaDebito.add(otros)));
                
		        if(ok){
		        // ingreso la ultima fila de totales
		        	sqlInsert = "delete from T_LIVAVENTAS_TOTAL";
	                DB.executeUpdate(sqlInsert, null);
	                DB.commit(true, get_TrxName());         
	                String sqlUpdate="insert into T_LIVAVENTAS_TOTAL VALUES(?,?,'Y',?,?,?,?,?,?,?)"; 
	                pstmtInsert = DB.prepareStatement(sqlUpdate.toString(), get_TrxName());
	                pstmtInsert.setLong(1,AD_CLIENT_ID);
	                pstmtInsert.setLong(2,AD_ORG_ID);
	                pstmtInsert.setInt(3,p_PInstance_ID);
	                pstmtInsert.setString(4,"Total al " + parserFecha(new Date(toDate.getTime()).toString()));
	                pstmtInsert.setBigDecimal(5, exento.add(transExento));
	                pstmtInsert.setBigDecimal(6, otros.add(transOtros));
	                pstmtInsert.setBigDecimal(7, grabado.add(transGravado));
	                pstmtInsert.setBigDecimal(8, ivaDebito.add(transIVADebito));
	                pstmtInsert.setBigDecimal(9, total.add(transTotal));
	                pstmtInsert.executeQuery();
	                DB.commit(true, get_TrxName());   
	                
		            /*sqlInsert = "insert into T_LIBRO_IVA_VENTA values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
		            pstmtInsert.setLong(1, AD_CLIENT_ID);
		            pstmtInsert.setLong(2, AD_ORG_ID);
		            pstmtInsert.setString(3, "Y");
		            pstmtInsert.setLong(4, p_PInstance_ID);
		            pstmtInsert.setDate(5, null);
		            pstmtInsert.setString(6, null);
		            pstmtInsert.setString(7, null);
		            pstmtInsert.setString(8, null);
		            pstmtInsert.setString(9, null);
		            pstmtInsert.setBigDecimal(10, grabado.add(transGravado));
		            pstmtInsert.setBigDecimal(11, exento.add(transExento));
		            pstmtInsert.setBigDecimal(12, ivaDebito.add(transIVADebito));
		            pstmtInsert.setString(13, null);
		            pstmtInsert.setBigDecimal(14, otros.add(transOtros));               
		            pstmtInsert.setBigDecimal(15, total.add(transTotal));
		            pstmtInsert.setString(16, "Total");
		            pstmtInsert.executeQuery();
		            DB.commit(true, get_TrxName());
		            */ 
		        }
        } catch (SQLException ex) {
            Logger.getLogger(GenerateLibroIvaVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void loadDebitoFiscal(){
    	String sqlRemove = "DELETE FROM T_LIVAVENTAS_DEBFIS";
        DB.executeUpdate(sqlRemove, null);
        
        BigDecimal totalExe = BigDecimal.ZERO;
        BigDecimal totalExp = BigDecimal.ZERO;
        BigDecimal totalNet = BigDecimal.ZERO;
        BigDecimal totalIVA = BigDecimal.ZERO;
        int cliente = 0;
        int org = 0;    
        
        // Recuperar las notas de debito realizadas entre la fecha indicada
        String sqlQuery =
        	 " SELECT C_InvoiceTax.AD_CLIENT_ID as AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID as AD_ORG_ID, " +
        	 " 		  to_Char(C_Tax.NAME) as CATEGORIA, 0 as EXENTO, 0 as EXPORTACION, SUM(C_InvoiceTax.TAXBASEAMT*C_Invoice.COTIZACION) as NETO_GRAVADO, SUM(C_InvoiceTax.TAXAMT*C_Invoice.COTIZACION) as IVA" +
        	 " FROM C_Tax" +
        	 " INNER JOIN C_InvoiceTax ON (C_InvoiceTax.C_Tax_ID = C_Tax.C_Tax_ID)" +
        	 " INNER JOIN C_Invoice ON (C_InvoiceTax.C_Invoice_ID = C_Invoice.C_Invoice_ID)" +
        	 " INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_Invoice.C_DocType_ID)" +
        	 " WHERE C_Invoice.DATEINVOICED between ? and ? AND C_InvoiceTax.IsActive = 'Y' AND C_Tax.RATE <>0 and C_Tax.IsActive = 'Y' and C_DocType.DocBaseType in ('ARI','ARF') AND C_Invoice.DOCSTATUS IN ('CO','CL')" +
        	 " GROUP BY C_InvoiceTax.AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID,C_Tax.NAME" +
        	 " " +
        	 " UNION" +
        	 " " +
        	 " SELECT C_InvoiceTax.AD_CLIENT_ID as AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID as AD_ORG_ID," +
        	 "        'EXENTO' as CATEGORIA, SUM(C_InvoiceTax.TAXBASEAMT*C_Invoice.COTIZACION) as EXENTO, 0 as EXPORTACION, 0 as NETO_GRAVADO, SUM(C_InvoiceTax.TAXAMT*C_Invoice.COTIZACION) as IVA" +
        	 " FROM C_Tax" +
        	 " INNER JOIN C_InvoiceTax ON (C_InvoiceTax.C_Tax_ID = C_Tax.C_Tax_ID)" +
        	 " INNER JOIN C_Invoice ON (C_InvoiceTax.C_Invoice_ID = C_Invoice.C_Invoice_ID)" +
        	 " INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_Invoice.C_DocType_ID)" +
        	 " WHERE C_Invoice.DATEINVOICED between ? and ? AND C_InvoiceTax.IsActive = 'Y' AND C_Tax.RATE = 0 and C_Tax.IsActive = 'Y' and C_Tax.NAME <> 'EXPORTACIÓN' and C_DocType.DocBaseType in ('ARI','ARF') AND C_Invoice.DOCSTATUS IN ('CO','CL')" +
        	 " GROUP BY C_InvoiceTax.AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID" +
        	 " " +
        	 " UNION" +
        	 " " +
        	 " SELECT C_InvoiceTax.AD_CLIENT_ID as AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID as AD_ORG_ID," +
        	 "        'EXPORTACION' as CATEGORIA, 0 as EXENTO, SUM(C_InvoiceTax.TAXBASEAMT*C_Invoice.COTIZACION) as EXPORTACION, 0 as NETO_GRAVADO, SUM(C_InvoiceTax.TAXAMT*C_Invoice.COTIZACION) as IVA" +
        	 " FROM C_Tax" +
        	 " INNER JOIN C_InvoiceTax ON (C_InvoiceTax.C_Tax_ID = C_Tax.C_Tax_ID)" +
        	 " INNER JOIN C_Invoice ON (C_InvoiceTax.C_Invoice_ID = C_Invoice.C_Invoice_ID)" +
        	 " INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_Invoice.C_DocType_ID)" +
        	 " WHERE C_Invoice.DATEINVOICED between ? and ? AND C_InvoiceTax.IsActive = 'Y' AND C_Tax.RATE = 0 and C_Tax.IsActive = 'Y' and C_Tax.NAME = 'EXPORTACIÓN' and C_DocType.DocBaseType in ('ARI','ARF') AND C_Invoice.DOCSTATUS IN ('CO','CL')" +
        	 " GROUP BY C_InvoiceTax.AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID";
        try{
	        PreparedStatement pstmt=DB.prepareStatement(sqlQuery, get_TrxName());
	        pstmt.setTimestamp(1, fromDate);
	        pstmt.setTimestamp(2, toDate);     
	        pstmt.setTimestamp(3, fromDate);
	        pstmt.setTimestamp(4, toDate);
	        pstmt.setTimestamp(5, fromDate);
	        pstmt.setTimestamp(6, toDate);
	        ResultSet rs = pstmt.executeQuery();
	        while(rs.next()){
	        	
	        	sqlQuery = "INSERT INTO T_LIVAVENTAS_DEBFIS VALUES(?,?,'Y',?,?,?,?,?,?)";
		    	PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());
		        ps.setInt(1, rs.getInt(1));					//	CLIENTE
		        ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
		        ps.setInt(3, p_PInstance_ID);				//	INSTANCE
		        ps.setString(4, rs.getString(3));			//	CATEGORIA
		        ps.setBigDecimal(5, rs.getBigDecimal(4));	//	EXENTO
		        ps.setBigDecimal(6, rs.getBigDecimal(5));	//	EXPORTACION
		        ps.setBigDecimal(7, rs.getBigDecimal(6));	//	NETO_GRAVADO
		        ps.setBigDecimal(8, rs.getBigDecimal(7));	//	IVA
		        	    
		        cliente = rs.getInt(1);
		    	org = rs.getInt(2);
		        totalExe = totalExe.add(rs.getBigDecimal(4));
		        totalExp = totalExp.add(rs.getBigDecimal(5));
		        totalNet = totalNet.add(rs.getBigDecimal(6));
		        totalIVA = totalIVA.add(rs.getBigDecimal(7));
		        
		        ps.executeQuery();
		        ps.close();
		        ps=null;
	        }
	        
	        sqlQuery = "INSERT INTO T_LIVAVENTAS_DEBFIS VALUES(?,?,'Y',?,?,?,?,?,?)";
	    	PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());
	        ps.setInt(1, cliente);			//	CLIENTE
	        ps.setInt(2, org);				//	ORGANIZACION
	        ps.setInt(3, p_PInstance_ID);	//	INSTANCE
	        ps.setString(4, "TOTAL");		//	CATEGORIA
	        ps.setBigDecimal(5, totalExe);	//	EXENTO
	        ps.setBigDecimal(6, totalExp);	//	EXPORTACION
	        ps.setBigDecimal(7, totalNet);	//	NETO_GRAVADO
	        ps.setBigDecimal(8, totalIVA);	//	IVA
	        
	        ps.executeQuery();
	        ps.close();
	        ps=null;
	        
        }
        catch (Exception exception) {
           exception.printStackTrace();
           log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaVentas.doIt.loadPercepciones "+exception.getMessage());
        }
    }
    
    protected void loadCreditoFiscal(){
    	String sqlRemove = "DELETE FROM T_LIVAVENTAS_CREDFIS";
        DB.executeUpdate(sqlRemove, null);
        
        BigDecimal totalExe = BigDecimal.ZERO;
        BigDecimal totalExp = BigDecimal.ZERO;
        BigDecimal totalNet = BigDecimal.ZERO;
        BigDecimal totalIVA = BigDecimal.ZERO;
        int cliente = 0;
        int org = 0;    
                     
        // Recuperar las notas de credito realizadas entre la fecha indicada
        String sqlQuery =
        	 " SELECT C_InvoiceTax.AD_CLIENT_ID as AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID as AD_ORG_ID, " +
        	 " 		  to_Char(C_Tax.NAME) as CATEGORIA, 0 as EXENTO, 0 as EXPORTACION, SUM(C_InvoiceTax.TAXBASEAMT*C_Invoice.COTIZACION) as NETO_GRAVADO, SUM(C_InvoiceTax.TAXAMT*C_Invoice.COTIZACION) as IVA" +
        	 " FROM C_Tax" +
        	 " INNER JOIN C_InvoiceTax ON (C_InvoiceTax.C_Tax_ID = C_Tax.C_Tax_ID)" +
        	 " INNER JOIN C_Invoice ON (C_InvoiceTax.C_Invoice_ID = C_Invoice.C_Invoice_ID)" +
        	 " INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_Invoice.C_DocType_ID)" +
        	 " WHERE C_Invoice.DATEINVOICED between ? and ? AND C_InvoiceTax.IsActive = 'Y' AND C_Tax.RATE <>0 and C_Tax.IsActive = 'Y' and C_DocType.DocBaseType in ('ARC') AND C_Invoice.DOCSTATUS IN ('CO','CL')" +
        	 " GROUP BY C_InvoiceTax.AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID,C_Tax.NAME" +
        	 " " +
        	 " UNION" +
        	 " " +
        	 " SELECT C_InvoiceTax.AD_CLIENT_ID as AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID as AD_ORG_ID," +
        	 "        'EXENTO' as CATEGORIA, SUM(C_InvoiceTax.TAXBASEAMT*C_Invoice.COTIZACION) as EXENTO, 0 as EXPORTACION, 0 as NETO_GRAVADO, SUM(C_InvoiceTax.TAXAMT*C_Invoice.COTIZACION) as IVA" +
        	 " FROM C_Tax" +
        	 " INNER JOIN C_InvoiceTax ON (C_InvoiceTax.C_Tax_ID = C_Tax.C_Tax_ID)" +
        	 " INNER JOIN C_Invoice ON (C_InvoiceTax.C_Invoice_ID = C_Invoice.C_Invoice_ID)" +
        	 " INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_Invoice.C_DocType_ID)" +
        	 " WHERE C_Invoice.DATEINVOICED between ? and ? AND C_InvoiceTax.IsActive = 'Y' AND C_Tax.RATE = 0 and C_Tax.IsActive = 'Y' and C_Tax.NAME <> 'EXPORTACIÓN' and C_DocType.DocBaseType in ('ARC') AND C_Invoice.DOCSTATUS IN ('CO','CL')" +
        	 " GROUP BY C_InvoiceTax.AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID" +
        	 " " +
        	 " UNION" +
        	 " " +
        	 " SELECT C_InvoiceTax.AD_CLIENT_ID as AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID as AD_ORG_ID," +
        	 "        'EXPORTACION' as CATEGORIA, 0 as EXENTO, SUM(C_InvoiceTax.TAXBASEAMT*C_Invoice.COTIZACION) as EXPORTACION, 0 as NETO_GRAVADO, SUM(C_InvoiceTax.TAXAMT*C_Invoice.COTIZACION) as IVA" +
        	 " FROM C_Tax" +
        	 " INNER JOIN C_InvoiceTax ON (C_InvoiceTax.C_Tax_ID = C_Tax.C_Tax_ID)" +
        	 " INNER JOIN C_Invoice ON (C_InvoiceTax.C_Invoice_ID = C_Invoice.C_Invoice_ID)" +
        	 " INNER JOIN C_DocType ON (C_DocType.C_DocType_ID = C_Invoice.C_DocType_ID)" +
        	 " WHERE C_Invoice.DATEINVOICED between ? and ? AND C_InvoiceTax.IsActive = 'Y' AND C_Tax.RATE = 0 and C_Tax.IsActive = 'Y' and C_Tax.NAME = 'EXPORTACIÓN' and C_DocType.DocBaseType in ('ARC') AND C_Invoice.DOCSTATUS IN ('CO','CL')" +
        	 " GROUP BY C_InvoiceTax.AD_CLIENT_ID, C_InvoiceTax.AD_ORG_ID";
        try{
	        PreparedStatement pstmt=DB.prepareStatement(sqlQuery, get_TrxName());
	        pstmt.setTimestamp(1, fromDate);
	        pstmt.setTimestamp(2, toDate);     
	        pstmt.setTimestamp(3, fromDate);
	        pstmt.setTimestamp(4, toDate);
	        pstmt.setTimestamp(5, fromDate);
	        pstmt.setTimestamp(6, toDate);
	        ResultSet rs = pstmt.executeQuery();
	        while(rs.next()){
	        	
	        	sqlQuery = "INSERT INTO T_LIVAVENTAS_CREDFIS VALUES(?,?,'Y',?,?,?,?,?,?)";
		    	PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());
		        ps.setInt(1, rs.getInt(1));					//	CLIENTE
		        ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
		        ps.setInt(3, p_PInstance_ID);				//	INSTANCE
		        ps.setString(4, rs.getString(3));			//	CATEGORIA
		        ps.setBigDecimal(5, rs.getBigDecimal(4));	//	EXENTO
		        ps.setBigDecimal(6, rs.getBigDecimal(5));	//	EXPORTACION
		        ps.setBigDecimal(7, rs.getBigDecimal(6));	//	NETO_GRAVADO
		        ps.setBigDecimal(8, rs.getBigDecimal(7));	//	IVA
		        	        
		        cliente = rs.getInt(1);
		    	org = rs.getInt(2);
		        totalExe = totalExe.add(rs.getBigDecimal(4));
		        totalExp = totalExp.add(rs.getBigDecimal(5));
		        totalNet = totalNet.add(rs.getBigDecimal(6));
		        totalIVA = totalIVA.add(rs.getBigDecimal(7));
		        
		        ps.executeQuery();
		        ps.close();
		        ps=null;
	        }
	        
        	sqlQuery = "INSERT INTO T_LIVAVENTAS_CREDFIS VALUES(?,?,'Y',?,?,?,?,?,?)";
	    	PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());
	        ps.setInt(1, cliente);			//	CLIENTE
	        ps.setInt(2, org);				//	ORGANIZACION
	        ps.setInt(3, p_PInstance_ID);	//	INSTANCE
	        ps.setString(4, "TOTAL");		//	CATEGORIA
	        ps.setBigDecimal(5, totalExe);	//	EXENTO
	        ps.setBigDecimal(6, totalExp);	//	EXPORTACION
	        ps.setBigDecimal(7, totalNet);	//	NETO_GRAVADO
	        ps.setBigDecimal(8, totalIVA);	//	IVA
	        
	        ps.executeQuery();
	        ps.close();
	        ps=null;
	        
        }
        catch (Exception exception) {
           exception.printStackTrace();
           log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaVentas.doIt.loadPercepciones "+exception.getMessage());
        }
    }

    protected void loadRetenciones(){
    	String sqlRemove = "DELETE FROM T_LIVAVENTAS_RETEN";
        DB.executeUpdate(sqlRemove, null);
        
        BigDecimal total = BigDecimal.ZERO;
        int cliente = Env.getAD_Client_ID(Env.getCtx());
        int org = Env.getAD_Org_ID(Env.getCtx());
        
        boolean ok = false;
        
        // Recuperar las retenciones realizadas entre la fecha indicada
        String sqlQuery =
        		" SELECT C_Cobranza_RET.AD_CLIENT_ID, C_Cobranza_RET.AD_ORG_ID, C_REGRETEN_RECIB.NAME as CONCEPTO, SUM(C_Cobranza_RET.IMPORTE) as IMPORTE " +
        		" FROM C_Cobranza_RET " +
        		" INNER JOIN C_REGRETEN_RECIB ON (C_REGRETEN_RECIB.C_REGRETEN_RECIB_ID = C_Cobranza_RET.C_REGRETEN_RECIB_ID)" +
        		" INNER JOIN C_Payment ON (C_Payment.C_Payment_ID = C_Cobranza_RET.C_Payment_ID)" +
        		" WHERE C_Cobranza_RET.IsActive = 'Y' AND C_Payment.DATETRX between ? and ? AND C_Payment.docstatus in ('CO','CL')" +
        		" GROUP BY C_Cobranza_RET.AD_CLIENT_ID, C_Cobranza_RET.AD_ORG_ID, C_REGRETEN_RECIB.NAME";
        try{
	        PreparedStatement pstmt=DB.prepareStatement(sqlQuery, get_TrxName());
	        pstmt.setTimestamp(1, fromDate);
	        pstmt.setTimestamp(2, toDate);     
	        ResultSet rs = pstmt.executeQuery();
	        while(rs.next()){
	        	ok=true;
	        	sqlQuery = "INSERT INTO T_LIVAVENTAS_RETEN VALUES(?,?,'Y',?,?,?)";
		    	PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());
		        ps.setInt(1, rs.getInt(1));					//	CLIENTE
		        ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
		        ps.setInt(3, p_PInstance_ID);				//	INSTANCE
		        ps.setString(4, rs.getString(3));			//	CONCEPTO
		        ps.setBigDecimal(5, rs.getBigDecimal(4).setScale(2, BigDecimal.ROUND_HALF_UP));	//	IMPORTE
		        
		        cliente = rs.getInt(1);
		    	org = rs.getInt(2);
		        total = total.add(rs.getBigDecimal(4));
		        
		        ps.executeUpdate();
		        ps.close();
		        ps=null;
	        }
	        
	        if (ok) 
	        {
	        	sqlQuery = "INSERT INTO T_LIVAVENTAS_RETEN VALUES(?,?,'Y',?,?,?)";
		        PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());
		        ps.setInt(1, cliente);				//	CLIENTE
		        ps.setInt(2, org);					//	ORGANIZACION
		        ps.setInt(3, p_PInstance_ID);		//	INSTANCE
		        ps.setString(4, "TOTAL");			//	CONCEPTO
		        ps.setBigDecimal(5, total.setScale(2, BigDecimal.ROUND_HALF_UP));			//	IMPORTE
		        
		        ps.executeUpdate();
		        ps.close();
		        ps=null;
	        }
        }
        catch (Exception exception) {
           exception.printStackTrace();
           log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaVentas.doIt.loadPercepciones "+exception.getMessage());
        }
    }

    protected void loadPercepciones(){
    	String sqlRemove = "DELETE FROM T_LIVAVENTAS_PERCEP";
        DB.executeUpdate(sqlRemove, null);
        
        BigDecimal total = BigDecimal.ZERO;
        int cliente = 0;
        int org = 0;    
        
        // Recuperar las percepciones realizadas entre la fecha indicada
        String sqlQuery =
        	" Select AD_CLIENT_ID,AD_ORG_ID,CONCEPTO, ALICUOTA, SUM(IMPORTE)" +
        	" FROM (" +
        	"           SELECT ip.AD_CLIENT_ID as AD_CLIENT_ID, ip.AD_ORG_ID as AD_ORG_ID, 'Perc. IB ' || j.NAME as CONCEPTO, ip.ALICUOTA, SUM(ip.Monto) as IMPORTE" +
        	"           FROM C_InvoicePercep_SOTrx ip" +
        	"           INNER JOIN C_Jurisdiccion j ON (j.C_Jurisdiccion_ID = ip.C_Jurisdiccion_ID)" +
        	"           INNER JOIN C_Invoice i ON (i.C_Invoice_ID = ip.C_Invoice_ID)" +
        	"           INNER JOIN C_DocType dt ON (dt.C_Doctype_ID = i.c_doctype_id)" +
        	"           WHERE ip.IsActive = 'Y' AND i.DATEINVOICED between ? and ?" +
        	"		          AND i.docstatus in ('CO','CL') AND dt.docbasetype = 'ARI'" +
        	"           GROUP BY ip.AD_CLIENT_ID, ip.AD_ORG_ID, j.name, ip.ALICUOTA" +
        	"        UNION" +
        	"           SELECT ip.AD_CLIENT_ID as AD_CLIENT_ID, ip.AD_ORG_ID as AD_ORG_ID, 'Perc. IB ' || j.NAME as CONCEPTO, ip.ALICUOTA, -SUM(ip.Monto) as IMPORTE" +
        	"           FROM C_InvoicePercep_SOTrx ip" +
        	"           INNER JOIN C_Jurisdiccion j ON (j.C_Jurisdiccion_ID = ip.C_Jurisdiccion_ID)" +
        	"           INNER JOIN C_Invoice i ON (i.C_Invoice_ID = ip.C_Invoice_ID)" +
        	"           INNER JOIN C_DocType dt ON (dt.C_Doctype_ID = i.c_doctype_id)" +
        	"           WHERE ip.IsActive = 'Y' AND i.DATEINVOICED between ? and ?" +
        	"                 AND i.docstatus in ('CO','CL') AND dt.docbasetype = 'ARC'" +
        	"           GROUP BY ip.AD_CLIENT_ID, ip.AD_ORG_ID, j.name, ip.ALICUOTA" +
        	"      )" +
        	" GROUP BY AD_CLIENT_ID, AD_ORG_ID, CONCEPTO, ALICUOTA";
        try{
	        PreparedStatement pstmt=DB.prepareStatement(sqlQuery, get_TrxName());
	        pstmt.setTimestamp(1, fromDate);
	        pstmt.setTimestamp(2, toDate);
	        pstmt.setTimestamp(3, fromDate);
	        pstmt.setTimestamp(4, toDate);     
	        ResultSet rs = pstmt.executeQuery();
	        
	        while(rs.next()){
	        	
	        	sqlQuery = "INSERT INTO T_LIVAVENTAS_PERCEP VALUES(?,?,'Y',?,?,?,?)";
		    	PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());
		    	ps.setInt(1, rs.getInt(1));					//	CLIENTE
		        ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
		        ps.setInt(3, p_PInstance_ID);				//	INSTANCE
		        ps.setString(4, rs.getString(3));			//	CONCEPTO
		        ps.setBigDecimal(5, rs.getBigDecimal(4));	//	ALICUOTA
		        ps.setBigDecimal(6, rs.getBigDecimal(5).setScale(2, BigDecimal.ROUND_HALF_UP));	//	IMPORTE
		        
		        cliente = rs.getInt(1);
		    	org = rs.getInt(2);
		        total = total.add(rs.getBigDecimal(5));
		        	        
		        ps.executeQuery();
		        ps.close();
		        ps=null;
	        }
	        
	        sqlQuery = "INSERT INTO T_LIVAVENTAS_PERCEP VALUES(?,?,'Y',?,?,?,?)";
	    	PreparedStatement ps = DB.prepareStatement(sqlQuery, get_TrxName());
	        ps.setInt(1, cliente);					//	CLIENTE
	        ps.setInt(2, org);					//	ORGANIZACION
	        ps.setInt(3, p_PInstance_ID);				//	INSTANCE
	        ps.setString(4, "TOTAL");					//	CONCEPTO
	        ps.setBigDecimal(5, null);					//	ALICUOTA
	        ps.setBigDecimal(6, total.setScale(2, BigDecimal.ROUND_HALF_UP));					//	IMPORTE
	        	        
	        ps.executeUpdate();
	        ps.close();
	        ps=null;
        }
        catch (Exception exception) {
           exception.printStackTrace();
           log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaVentas.doIt.loadPercepciones "+exception.getMessage());
        }
        
    }

     protected void setearTemp(){
        try{
            if(AD_CLIENT_ID!=null){
                String sqlRemove2 = "delete from TEMP";
                DB.executeUpdate(sqlRemove2, null);
                DB.commit(true, null);         
                String sqlUpdate="insert into TEMP VALUES(?,?,'Y',?,?,?,?)"; 
                PreparedStatement pstmt=DB.prepareStatement(sqlUpdate.toString(), get_TrxName());
                pstmt.setLong(1,AD_CLIENT_ID);
                pstmt.setLong(2,AD_ORG_ID);
                pstmt.setTimestamp(3, toDate);
                pstmt.setLong(4, num_hoja);
                pstmt.setBigDecimal(5, new BigDecimal(0));
                pstmt.setTimestamp(6, fromDate);
                pstmt.executeQuery();
                DB.commit(true, null);         
            }
        } catch (Exception exception) {
            log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaVentas.doIt.Sector1 " + exception.getMessage());
            exception.printStackTrace();
        }
     }
     
     private String parserFecha(String fecha){

     	String ano = fecha.substring(0, fecha.indexOf('-'));
     	String mes = fecha.substring(fecha.indexOf('-')+1, fecha.lastIndexOf('-'));
     	String dia = fecha.substring(fecha.lastIndexOf('-')+1);     	
     	
     	return dia + "/" + mes + "/" + ano;
     }
}
