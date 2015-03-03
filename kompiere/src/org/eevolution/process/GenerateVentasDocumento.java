/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import org.compiere.model.MCurrency;
import org.compiere.model.M_Column;
import org.compiere.process.*;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.logging.*;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *  Esta clase inserta tuplas en la tabla temporal T_VentasCliente luego de un previo filtrado por fecha y socio de Negocio
 *  y calculos posteriores.
 *
 *	@author DANIEL GINI
 *	@version 1.0
 *  Date 20/09/2009
 */
public class GenerateVentasDocumento extends SvrProcess{

    private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    
    private boolean concepto = false;
    private Integer AD_CLIENT_ID = 0;
    private Integer AD_ORG_ID = 0;
    private List<Integer> docs = new ArrayList<Integer>();
    private BigDecimal C_Currency_ID = BigDecimal.ZERO;
    
    /*
     * 18-01-2011
     */
    private Boolean convertirPesos = false;
    private BigDecimal tasaConversion = BigDecimal.ZERO;
    private int currencyPesos = 0;
    
    
    protected void prepare() {
        p_PInstance_ID = getAD_PInstance_ID();
    }

    public Integer getAD_CLIENT_ID() {
		return AD_CLIENT_ID;
	}
	public void setAD_CLIENT_ID(Integer ad_client_id) {
		AD_CLIENT_ID = ad_client_id;
	}
	public Integer getAD_ORG_ID() {
		return AD_ORG_ID;
	}
	public void setAD_ORG_ID(Integer ad_org_id) {
		AD_ORG_ID = ad_org_id;
	}
	public BigDecimal getC_Currency_ID() {
		return C_Currency_ID;
	}
	public void setC_Currency_ID(BigDecimal currency_ID) {
		C_Currency_ID = currency_ID;
	}
	public List<Integer> getDocs() {
		return docs;
	}
	public void setDocs(List<Integer> docs) {
		this.docs = docs;
	}
	public boolean isConcepto() {
		return concepto;
	}
	public void setConcepto(boolean concepto) {
		this.concepto = concepto;
	}
	public Timestamp getFromDate() {
		return fromDate;
	}
	public void setFromDate(Timestamp fromDate) {
		this.fromDate = fromDate;
	}
	public Timestamp getToDate() {
		return toDate;
	}
	public void setToDate(Timestamp toDate) {
		this.toDate = toDate;
	}

	
	public BigDecimal getTasaConversion() {
		return tasaConversion;
	}
	public void setTasaConversion(BigDecimal tasaConversion) {
		this.tasaConversion = tasaConversion;
	}

	public Boolean getConvertirPesos() {
		return convertirPesos;
	}
	public void setConvertirPesos(Boolean convertirPesos) {
		this.convertirPesos = convertirPesos;
	}

	
	
	protected String doIt() {
        try {
        	currencyPesos = mcurrency_id();
        	loadReporte();
        	
        	DB.commit(true, get_TrxName());
            UtilProcess.initViewer("Ventas por Tipo de Documento",p_PInstance_ID,getProcessInfo());
        } catch (SQLException ex) {
            Logger.getLogger(GenerateVentasDocumento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "El proceso ha finalizado con éxito.";
    }

	private String getFechasPara(Timestamp fromDate, Timestamp toDate)  {
    	return parserFecha(fromDate.toString().substring(0, 10)) + "   -   " + parserFecha(toDate.toString().substring(0, 10));
    }
    
    private String parserFecha(String fecha){

     	String ano = fecha.substring(0, fecha.indexOf('-'));
     	String mes = fecha.substring(fecha.indexOf('-')+1, fecha.lastIndexOf('-'));
     	String dia = fecha.substring(fecha.lastIndexOf('-')+1);     	
     	
     	return dia + "/" + mes + "/" + ano;
     }
    
    /** BISion - 20/10/2009 - Daniel Gini
     * Metodo que retorna los productos facturados para los socios de negocios filtrados
     * 
     * @return ResultSet
     * @throws Exception
     */
	private ResultSet getProductos()		throws Exception{
        //CONSULTA PARA OBTENER LOS DATOS
		String sql =
			" SELECT AD_CLIENT_ID, AD_ORG_ID, ID, NAME, sum(QTY), sum(NETO), CONCEPTO, C_Currency_ID" +
			" FROM (" +
			"     SELECT i.AD_CLIENT_ID, i.AD_ORG_ID, il.M_Product_ID as ID, p.name as NAME," +
			"	  CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED)" +
			" 	  ELSE sum(il.QTYINVOICED) END as QTY," +
			"     CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED * il.PRICEACTUAL)" +
			"     ELSE sum(il.QTYINVOICED * il.PRICEACTUAL) END as NETO," +
			"     0 AS CONCEPTO, i.C_Currency_ID" +
			"     FROM C_InvoiceLine il" +
			"     INNER JOIN C_Invoice i ON (i.C_Invoice_ID = il.C_Invoice_ID)" +
			"     INNER JOIN C_BPartner b ON (i.C_BPartner_ID = b.C_BPartner_ID)" +
			"     INNER JOIN M_PRODUCT p ON (il.M_PRODUCT_ID = p.M_PRODUCT_ID)" +
			"     INNER JOIN C_DocType d ON (d.C_DocType_ID = i.C_DocType_ID) " +
 		   		  getSqlWhere("i.ISSOTRX","i.DATEINVOICED","i.C_DocTypeTarget_ID","i.DocStatus","i.C_Currency_ID") +
			"	  GROUP BY i.AD_CLIENT_ID, i.AD_ORG_ID, il.M_PRODUCT_ID, p.name, i.C_Currency_ID, d.DOCBASETYPE" +
			"		";
			if (concepto)
			{
				sql = sql +
				"	  UNION" +
				"     	(" +
				"          SELECT i.AD_CLIENT_ID, i.AD_ORG_ID, il.C_Charge_ID as ID, h.name as NAME," +
				"		   CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED)" +
				" 		   ELSE sum(il.QTYINVOICED) END as QTY," +
				"     	   CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED * il.PRICEACTUAL)" +
				"    	   ELSE sum(il.QTYINVOICED * il.PRICEACTUAL) END as NETO," +
				"		   1 as CONCEPTO, i.C_Currency_ID" +
				"          FROM C_InvoiceLine il" +
				"          INNER JOIN C_Invoice i ON (i.C_Invoice_ID = il.C_Invoice_ID)" +
				"          INNER JOIN C_BPartner b ON (i.C_BPartner_ID = b.C_BPartner_ID)" +
				"          INNER JOIN C_Charge h ON (il.C_CHARGE_ID = h.C_CHARGE_ID)" +
				"		   INNER JOIN C_DocType d ON (d.C_DocType_ID = i.C_DocType_ID) " +
			    		   getSqlWhere("i.ISSOTRX","i.DATEINVOICED","i.C_DocTypeTarget_ID","i.DocStatus","i.C_Currency_ID") +
				"          GROUP BY i.AD_CLIENT_ID, i.AD_ORG_ID, il.C_Charge_ID, h.name, i.C_Currency_ID, d.DOCBASETYPE" +
				"     	)";
			}
			sql = sql +
				"	  )" +
				" GROUP BY AD_CLIENT_ID, AD_ORG_ID, ID, name, CONCEPTO, C_Currency_ID " +
				"       HAVING sum(QTY)<>0 " +
				" ORDER BY NAME";
		
        PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
        int paramIndex = 1;
        
        if (fromDate!=null){
            pstmt.setTimestamp(paramIndex, fromDate);
            paramIndex++;
        }
        if (toDate!=null){
            pstmt.setTimestamp(paramIndex, toDate);
            paramIndex++;
        }
        
        if (C_Currency_ID.compareTo(BigDecimal.ZERO)!=0){
            pstmt.setInt(paramIndex, C_Currency_ID.intValue());
            paramIndex++;
        }
        
        if (concepto)
        {
        	if (fromDate!=null){
                pstmt.setTimestamp(paramIndex, fromDate);
                paramIndex++;
            }
            if (toDate!=null){
                pstmt.setTimestamp(paramIndex, toDate);
                paramIndex++;
            }
            
            if (C_Currency_ID.compareTo(BigDecimal.ZERO)!=0){
                pstmt.setInt(paramIndex, C_Currency_ID.intValue());
                paramIndex++;
            }
        }
        
        return pstmt.executeQuery();
    }
	
	/** BISion - 08/05/2009 - Daniel Gini
     * Metodo que retorna la clausula "where" dependiendo de los parametros ingresados.
     * @return String
     */
    private String getSqlWhere(String param1, String param2, String param3, String param4, String param5){
    	String sqlWhere = " WHERE "+param1+" = 'Y'";
      
		if (toDate!=null && fromDate!=null)
			sqlWhere+= " AND "+param2+" BETWEEN ? AND ?";
		else if (toDate!=null && fromDate==null)
			sqlWhere+= " AND "+param2+" <= ?";
		else if (toDate==null && fromDate!=null)
			sqlWhere+= " AND "+param2+" >= ?";
	
		String DocType = "";
		int i = 0;
		for (i = 0; i< docs.size()-1; i++)
		{
			DocType = DocType + docs.get(i).intValue() + ",";
		}
		if (docs.size()>0)
		{
			DocType = DocType + docs.get(i).intValue();
			sqlWhere+= " AND "+param3+" IN ("+DocType+")";
		}
		else
			sqlWhere+= " AND "+param3+" IN (0)";
		
		sqlWhere+= " AND "+param4+" IN ('CO','CL')";
    	
		if (C_Currency_ID.compareTo(BigDecimal.ZERO)!=0)
	        sqlWhere+= " AND "+param5+" = ?";
		
		return sqlWhere;
    }
	
    /**
     * 18-01-2010 Camarzana Mariano
     * Metodo usado para setear el valor del identificador de la moneda en pesos
     */
    private int mcurrency_id(){
    	try {
    		String sql = "select c_currency_id " +
    					 "from c_currency " +
    					 "where iso_code like 'ARS'";
    		PreparedStatement pstmt; 
    		pstmt = DB.prepareStatement(sql,get_TrxName());
        	ResultSet rs = pstmt.executeQuery();
        	try {
        		if (rs.next())
        			return rs.getInt("c_currency_id");
        		rs.close();
        		pstmt.close();
        		}
        	catch (Exception exception) {
        			exception.printStackTrace();
        		}
   	
    		}
    	catch (SQLException ex) {
          	 Logger.getLogger(GenerateVentasDocumento.class.getName()).log(Level.SEVERE, null, ex);
    	}
    	return 0;
    }
    
    private void loadReporte()
    {
    	//	BORRADO DE LA TABLA T_VENTASDOCUMENTO
    	String sql = "Delete from T_VentasDocumento";
    	
	    log.info("Borrado de la tabla temporal T_VentasDocumento");
	    DB.executeUpdate(sql, null);
	    
    	try {
			ResultSet rs = getProductos();
			
		    int T_VentasDocumento_ID=1000000;
		    
		    Integer acumCant = 0;
		    BigDecimal acumNeto = BigDecimal.ZERO;
		    
		    if (rs.next())
		    {
		    	AD_ORG_ID = rs.getInt(2);

		    	sql = "INSERT INTO T_VentasDocumento_HDR VALUES(?,?,'Y',?,?,?,?,?)";
		    	PreparedStatement ps = DB.prepareStatement(sql, null);
		        ps.setInt(1, getAD_Client_ID());				//	CLIENTE
		        ps.setInt(2, rs.getInt(2));						//	ORGANIZACION
		        ps.setInt(3, p_PInstance_ID);					//	INSTANCE
		        ps.setString(4, getFechasPara(fromDate, toDate));//	FECHAS
		        ps.setTimestamp(5, fromDate);					// 	FECHA
		        ps.setString(6, (concepto==true ? "Y" : "N"));	//	CONCEPTO
		        
		        /*
		         *18-01-2010 Camarzana Mariano 
		         */
		        if (convertirPesos)
		        	ps.setInt(7, currencyPesos);			//	C_Currency_ID
		        else
		        	ps.setInt(7, C_Currency_ID.intValue());			//	C_Currency_ID
		        ps.executeUpdate();
		        ps.close();
		        DB.commit(true, get_TrxName());
		        
		    	while (!rs.isAfterLast()){
			    	//  Ingreso en la tabla cabecera
			    	sql = "INSERT INTO T_VentasDocumento VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?)";
			    	ps = DB.prepareStatement(sql, null);
			        ps.setInt(1, getAD_Client_ID());			//	CLIENTE
			        ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
			        ps.setInt(3, T_VentasDocumento_ID);			//	C_VENTASDOCUMENTO_ID
			        ps.setInt(4, p_PInstance_ID);				//	INSTANCE
			        ps.setInt(5, new Integer(0));				//	C_DOCTYPE_ID
			        ps.setInt(6, rs.getInt(3));					//	C_PRODUCT_ID
			        ps.setString(7, rs.getString(4));			//	NAME PRODUCT
			        ps.setInt(8, rs.getBigDecimal(5).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());	// 	CANTIDAD
			        
			        /*
			         * 18-01-2011 Camarzana Mariano
			         */
			        if (convertirPesos)
			        	ps.setBigDecimal(9, rs.getBigDecimal(6).multiply(tasaConversion).setScale(2, BigDecimal.ROUND_HALF_UP));	// 	IMPORTE NETO
			        else
			        	ps.setBigDecimal(9, rs.getBigDecimal(6).setScale(2, BigDecimal.ROUND_HALF_UP));	// 	IMPORTE NETO
			        ps.setTimestamp(10, fromDate);				// 	FECHA
			        if (concepto)
			        	ps.setString(11, "Y");
			        else
			        	ps.setString(11, "N");					// 	CONCEPTO
			        
			        /*
			         *18-01-2010 Camarzana Mariano 
			         */
			        if (convertirPesos)
			        	ps.setInt(12, currencyPesos);
			        else
			        	ps.setInt(12, rs.getInt(8));				//	C_CURRENCY_ID
			        ps.executeUpdate();
			        ps.close();

			        if (convertirPesos)
			            acumNeto = acumNeto.add(rs.getBigDecimal(6).multiply(tasaConversion));
			        else
			            acumNeto = acumNeto.add(rs.getBigDecimal(6));

			        acumCant = acumCant + rs.getInt(5);	
			            
			        T_VentasDocumento_ID++;
			        
			        rs.next();
			    }
			    
		    	sql = "INSERT INTO T_VentasDocumento VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?)";
		    	ps = DB.prepareStatement(sql, null);
		        ps.setInt(1, getAD_Client_ID());			//	CLIENTE
		        ps.setInt(2, AD_ORG_ID);					//	ORGANIZACION
		        ps.setInt(3, T_VentasDocumento_ID);			//	C_VENTASCLIENTE_ID
		        ps.setInt(4, p_PInstance_ID);				//	INSTANCE
		        ps.setInt(5, new Integer(0));				//	C_DOCTYPE_ID
		        ps.setInt(6, 0);							//	C_PRODUCT_ID
		        ps.setString(7, "TOTAL");					//	NAME PRODUCT
		        ps.setInt(8, new BigDecimal(acumCant).setScale(2, BigDecimal.ROUND_HALF_UP).intValue()); // 	CANTIDAD
		        ps.setBigDecimal(9, acumNeto.setScale(2, BigDecimal.ROUND_HALF_UP));	// 	IMPORTE NETO
		        ps.setTimestamp(10, fromDate);				// 	FECHA
		        if (concepto)
		        	ps.setString(11, "Y");
		        else
		        	ps.setString(11, "N");					// 	CONCEPTO
		        if (C_Currency_ID!=null)
		        	 /*
			         *18-01-2010 Camarzana Mariano 
			         */
			        if (convertirPesos)
			        	ps.setInt(12, currencyPesos);	
			        else
			        	ps.setInt(12, C_Currency_ID.intValue());			
		        else
		        	ps.setInt(12, 0);						//	C_CURRENCY_ID
		        
		        ps.executeUpdate();
		        ps.close();
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
