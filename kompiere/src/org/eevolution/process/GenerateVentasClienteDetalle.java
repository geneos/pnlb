/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import org.compiere.model.MBPartner;
import org.compiere.process.*;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
public class GenerateVentasClienteDetalle extends SvrProcess{

    private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    private String fromValue = null;
    private String toValue = null;
    
    private Integer AD_ORG_ID = 0;
    private Integer C_BPARTNER_ID = 0;
    private Boolean concepto = false;
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        boolean flagCliente = false;
        for (int i = 0; i < para.length; i++)
        {
            String name = para[i].getParameterName();
            if(name.equals("FECHA")){
            	fromDate=(Timestamp)para[i].getParameter();
                toDate=(Timestamp)para[i].getParameter_To();
            }
            else
            	if(name.equals("C_BPartner_ID")){
            		if (flagCliente == false){
            			fromValue=para[i].getParameter().toString();
    	                flagCliente = true;
            			}
    	            else
    	            	toValue=para[i].getParameter().toString();
                    }
            		
            	else
            		if(name.equals("CONCEPTO")){
            			if (((String)para[i].getParameter()).equals("Y"))
            				concepto = true;
	                	else
	                		concepto = false;
	                    }
        }
        
        /*
         * 03-02-2011 Camarzana Mariano
         * Seteo el p_PInstance_ID (la instancia) en 0, debido a que cuando se trabaja con rango toma mal 
         * la cantidad de parametros
         */ 
        p_PInstance_ID =  0;
        //p_PInstance_ID = getAD_PInstance_ID();
        C_BPARTNER_ID = getC_BPartner_ID(fromValue);
    }
   
    protected String doIt() {
        try {
        	loadReporte();
        	
        	DB.commit(true, get_TrxName());
       		UtilProcess.initViewer("Ventas por Cliente Detallado",p_PInstance_ID,getProcessInfo());
        } catch (SQLException ex) {
            Logger.getLogger(GenerateVentasClienteDetalle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "El proceso ha finalizado con éxito.";
    }
    
    protected int getC_BPartner_ID(String value)
    {
	    try	{	
    		String sql = 
			    	" SELECT C_BPARTNER_ID " +
			    	" FROM C_BPARTNER" +
			    	" WHERE AD_CLIENT_ID = ? AND VALUE = ?";
	    	
	    	PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
	    	pstmt.setLong(1, getAD_Client_ID());
	    	pstmt.setString(2, value);
			    	
		    ResultSet rs = pstmt.executeQuery();
		    
		    if (rs.next())
		    	return rs.getInt(1);
		    
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return 0;
    }
    
    /** BISion - 20/10/2009 - Daniel Gini
     * Metodo que retorna los productos facturados para cada vendedor
     * 
     * @return ResultSet
     * @throws Exception
     */
	private ResultSet getProductos()		throws Exception{
        //CONSULTA PARA OBTENER LOS DATOS
		String sql =
			" SELECT AD_CLIENT_ID, AD_ORG_ID, CLIENTE_ID, CLIENTE_NAME, PROD_ID, PROD_NAME, sum(QTY), sum(NETO), VALUE " +
			" FROM ( " +
			" 		SELECT i.AD_CLIENT_ID, i.AD_ORG_ID, b.C_BPartner_ID as CLIENTE_ID, b.name as CLIENTE_NAME, m.M_Product_ID as PROD_ID, m.NAME as PROD_NAME, " +
			" 		CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED) " +
			" 			 ELSE sum(il.QTYINVOICED) END as QTY, " +
			" 		CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED * il.PRICEACTUAL * i.COTIZACION) " +
			" 			 ELSE sum(il.QTYINVOICED * il.PRICEACTUAL) END as NETO, " +
			" 		b.VALUE as VALUE  " +
			" 		FROM C_InvoiceLine il " +
			" 		INNER JOIN C_Invoice i ON (i.C_Invoice_ID = il.C_Invoice_ID) " +
			" 		INNER JOIN C_BPartner b ON (i.C_BPartner_ID = b.C_BPartner_ID) " +
			" 		INNER JOIN C_DocType d ON (d.C_DocType_ID = i.C_DocType_ID) " +
			"       INNER JOIN M_Product m ON (m.M_Product_ID = il.M_Product_ID) " +
			getSqlWhere("i.ISSOTRX","i.DATEINVOICED","i.DocStatus") +
			" 		GROUP BY i.AD_CLIENT_ID, i.AD_ORG_ID, b.C_BPartner_ID, b.name, m.M_Product_ID, m.NAME, b.VALUE, d.DOCBASETYPE " +
			"		";
		 	if (concepto)
		 	{
		 		sql = sql +
				"  UNION" +
				"     	(  SELECT i.AD_CLIENT_ID, i.AD_ORG_ID, b.C_BPartner_ID as CLIENTE_ID, b.name as CLIENTE_NAME, h.C_CHARGE_ID as PROD_ID, h.NAME as PROD_NAME, " +
				" 		   CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED) " +
				" 				ELSE sum(il.QTYINVOICED) END as QTY, " +
				"	 	   CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED * il.PRICEACTUAL * i.COTIZACION) " +
				" 				ELSE sum(il.QTYINVOICED * il.PRICEACTUAL) END as NETO, " +
				" 		   b.VALUE as VALUE  " +
				" 		   FROM C_InvoiceLine il " +
				" 		   INNER JOIN C_Invoice i ON (i.C_Invoice_ID = il.C_Invoice_ID) " +
				" 		   INNER JOIN C_BPartner b ON (i.C_BPartner_ID = b.C_BPartner_ID) " +
				" 		   INNER JOIN C_DocType d ON (d.C_DocType_ID = i.C_DocType_ID) " +
		        "          INNER JOIN C_Charge h ON (il.C_CHARGE_ID = h.C_CHARGE_ID) " +
		        getSqlWhere("i.ISSOTRX","i.DATEINVOICED","i.DocStatus") +
				" 		   GROUP BY i.AD_CLIENT_ID, i.AD_ORG_ID, b.C_BPartner_ID, b.name, h.C_CHARGE_ID, h.NAME, b.VALUE, d.DOCBASETYPE " +	
				"     	)";
		 	}
			sql = sql +
			" 	) " +
			" GROUP BY AD_CLIENT_ID, AD_ORG_ID, CLIENTE_ID, CLIENTE_NAME, PROD_ID, PROD_NAME, VALUE " +
			"       HAVING sum(QTY)<>0 " +
			" ORDER BY CLIENTE_NAME, CLIENTE_ID, PROD_NAME";
			
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
        if (fromValue!=null){
        	MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(fromValue),null);
    		pstmt.setString(paramIndex, partner.getName());
            paramIndex++;
        }
        if (toValue!=null){
        	MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(toValue),null);
        	pstmt.setString(paramIndex, partner.getName());
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
            if (fromValue!=null){
            	MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(fromValue),null);
        		pstmt.setString(paramIndex, partner.getName());
                paramIndex++;
            }
            if (toValue!=null){
            	MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(toValue),null);
            	pstmt.setString(paramIndex, partner.getName());
                paramIndex++;
            }
        }
		
		return pstmt.executeQuery();
    }

	
	/** BISion - 08/05/2009 - Daniel Gini
     * Metodo que retorna la clausula "where" dependiendo de los parametros ingresados.
     * @return String
     */
	 private String getSqlWhere(String param1, String param2, String param3){
	    	String sqlWhere = " WHERE "+param1+" = 'Y'";
	      
			if (toDate!=null && fromDate!=null)
				sqlWhere+= " AND "+param2+" BETWEEN ? AND ?";
			else if (toDate!=null && fromDate==null)
				sqlWhere+= " AND "+param2+" <= ?";
			else if (toDate==null && fromDate!=null)
				sqlWhere+= " AND "+param2+" >= ?";
		
			if (toValue!=null && fromValue!=null)
				sqlWhere+= " AND b.name >= ? AND b.name <= ?";
			else if (toValue!=null && fromValue==null)
				sqlWhere+= " AND b.name <= ?";
			else if (toValue==null && fromValue!=null)
				sqlWhere+= " AND b.name >= ?";
			
			sqlWhere+= " AND "+param3+" IN ('CO','CL')";
	    	
			return sqlWhere;
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
    
    private void loadReporte()
    {
    	log.info("Borrado de la tabla temporal T_VentasClienteDetalle");
    	DB.executeUpdate("Delete from T_VentasClienteDetalle", null);
    	log.info("Borrado de la tabla temporal T_VentasClienteDetalle_HDR");
	    DB.executeUpdate("Delete from T_VentasClienteDetalle_HDR", null);
	    
    	String sql = "";
    	try {

    		ResultSet rs = getProductos();

		    int T_VentasClienteDetalle_ID=1000000;
		    
		    
		    
		    Integer acumCant = 0;
		    BigDecimal acumNeto = BigDecimal.ZERO;
		    
		    if (rs.next())
		    {
		    	int C_BPartner_ID=rs.getInt(3);
		    	int cantTotal = 0;
		        BigDecimal netoTotal = BigDecimal.ZERO;
		    	AD_ORG_ID = rs.getInt(2);
		    	
		        sql = "INSERT INTO T_VentasClienteDetalle_HDR VALUES(?,?,'Y',?,?,?,?,?,?,?)";
		    	PreparedStatement ps = DB.prepareStatement(sql, null);
		        ps.setInt(1, getAD_Client_ID());				//	CLIENTE
		        ps.setInt(2, rs.getInt(2));						//	ORGANIZACION
		        ps.setInt(3, p_PInstance_ID);					//	INSTANCE
		        ps.setString(4, getFechasPara(fromDate, toDate));//	FECHAS
		        ps.setString(5, fromValue + " - " + toValue);	//	CLAVES
		        ps.setTimestamp(6, fromDate);					// 	FECHA
		        
		        
		        if (fromValue!=null)						// 	VALUE
		        {	
		        	MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(fromValue),null);
		    		ps.setString(7, partner.getValue());
		        }	
		        else
		        	if (toValue!=null)
		        	{	
		        		MBPartner partner = new MBPartner(getCtx(),Integer.parseInt(toValue),null);
			    		ps.setString(7, partner.getValue());
			        }			
		        	else
	        			ps.setString(7, rs.getString(9));	// 	VALUE
		        
		        
		        //ps.setString(7, fromValue);						// 	VALUE
		        
		        
		        ps.setString(8, (concepto==true ? "Y" : "N"));	//	CONCEPTO
		        
		        
		        if (fromValue!=null)						// 	C_BPARTNER_ID
		        	ps.setInt(9, Integer.parseInt(fromValue));			
		        else
		        	if (toValue!=null)
		        		ps.setInt(9, Integer.parseInt(toValue));			
		        	else
		        		ps.setInt(9, C_BPartner_ID);		//	C_BPARTNER_ID
		        
		        ps.executeUpdate();
		        ps.close();
		        DB.commit(true, get_TrxName());
		    	
		    	while (!rs.isAfterLast())
		    	{
		    		int C_BPARTNER_ID = rs.getInt(3);

			        //Ingreso en la tabla detalle
			    	sql = "INSERT INTO T_VentasClienteDetalle VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?)";
			    	ps = DB.prepareStatement(sql, null);
			        ps.setInt(1, getAD_Client_ID());			//	CLIENTE
			        ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
			        ps.setInt(3, T_VentasClienteDetalle_ID);	//	C_VENTASZONADETALLE_ID
			        ps.setInt(4, p_PInstance_ID);				//	INSTANCE
			        ps.setInt(5, rs.getInt(3));					//	C_BPARTNER_ID
			        ps.setString(6, rs.getString(4));			//	NAME BPARTNER
			        ps.setInt(7, rs.getInt(5));					//	M_PRODUCT_ID
			        ps.setString(8, rs.getString(6));			//	NAME PRODUCTO
			        ps.setInt(9, rs.getInt(7));					// 	CANTIDAD
			        ps.setBigDecimal(10, rs.getBigDecimal(8));	// 	IMPORTE NETO
			        ps.setTimestamp(11, fromDate);				// 	FECHA
			        ps.setString(12, fromValue);				// 	VALUE
			        
			        ps.executeUpdate();
			        DB.commit(true, get_TrxName());
			        
			        ps.close();
			        
			        acumCant = rs.getInt(7);
			        acumNeto = rs.getBigDecimal(8);
			        T_VentasClienteDetalle_ID++;
			        rs.next();
       
			        while (!rs.isAfterLast() && C_BPARTNER_ID==rs.getInt(3))
			    	{
		    			//  Ingreso en la tabla detalle
			        	sql = "INSERT INTO T_VentasClienteDetalle VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?)";
				    	ps = DB.prepareStatement(sql, null);
				        ps.setInt(1, getAD_Client_ID());			//	CLIENTE
				        ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
				        ps.setInt(3, T_VentasClienteDetalle_ID);	//	C_VENTASZONADETALLE_ID
				        ps.setInt(4, p_PInstance_ID);				//	INSTANCE
				        ps.setInt(5, rs.getInt(3));					//	C_BPARTNER_ID
				        ps.setString(6, "");						//	NAME BPARTNER
				        ps.setInt(7, rs.getInt(5));					//	M_PRODUCT_ID
				        ps.setString(8, rs.getString(6));			//	NAME PRODUCTO
				        ps.setInt(9, rs.getInt(7));					// 	CANTIDAD
				        ps.setBigDecimal(10, rs.getBigDecimal(8));	// 	IMPORTE NETO
				        ps.setTimestamp(11, fromDate);				// 	FECHA
				        ps.setString(12, fromValue);				// 	VALUE
				        
				        ps.executeUpdate();
				        DB.commit(true, get_TrxName());
				        
				        ps.close();
	
				        acumCant = acumCant + rs.getInt(7);
				        acumNeto = acumNeto.add(rs.getBigDecimal(8));
				        T_VentasClienteDetalle_ID++;
				        
				        rs.next();

				    }
		
			        sql = "INSERT INTO T_VentasClienteDetalle VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?)";
			    	ps = DB.prepareStatement(sql, null);
			        ps.setInt(1, getAD_Client_ID());			//	CLIENTE
			        ps.setInt(2, AD_ORG_ID);					//	ORGANIZACION
			        ps.setInt(3, T_VentasClienteDetalle_ID);	//	C_VENTASZONADETALLE_ID
			        ps.setInt(4, p_PInstance_ID);				//	INSTANCE
			        ps.setInt(5, 0);							//	C_BPARTNER_ID
			        ps.setString(6, "");						//	NAME BPARTNER
			        ps.setInt(7, 0);							//	M_PRODUCT_ID
			        ps.setString(8, "TOTAL");					//	NAME PRODUCTO
			        ps.setInt(9, acumCant);						// 	CANTIDAD
			        ps.setBigDecimal(10, acumNeto);				// 	IMPORTE NETO
			        ps.setTimestamp(11, fromDate);				// 	FECHA
			        ps.setString(12, fromValue);				// 	VALUE
			        
			        ps.executeUpdate();
			        DB.commit(true, get_TrxName());
			        
			        ps.close();
			        
			        cantTotal = cantTotal + acumCant;
			        netoTotal = netoTotal.add(acumNeto);
			        T_VentasClienteDetalle_ID++;
		    	}
		    	
		    	sql = "INSERT INTO T_VentasClienteDetalle VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?)";
		    	ps = DB.prepareStatement(sql, null);
		        ps.setInt(1, getAD_Client_ID());			//	CLIENTE
		        ps.setInt(2, AD_ORG_ID);					//	ORGANIZACION
		        ps.setInt(3, T_VentasClienteDetalle_ID);	//	C_VENTASZONADETALLE_ID
		        ps.setInt(4, p_PInstance_ID);				//	INSTANCE
		        ps.setInt(5, 0);							//	C_BPARTNER_ID
		        ps.setString(6, "TOTAL");					//	NAME BPARTNER
		        ps.setInt(7, 0);							//	M_PRODUCT_ID
		        ps.setString(8, "");						//	NAME PRODUCTO
		        ps.setInt(9, cantTotal);					// 	CANTIDAD
		        ps.setBigDecimal(10, netoTotal);			// 	IMPORTE NETO
		        ps.setTimestamp(11, fromDate);				// 	FECHA
		        ps.setString(12, fromValue);				// 	VALUE
		        
		        ps.executeUpdate();
		        DB.commit(true, get_TrxName());
		        
		        ps.close();
		        
		        T_VentasClienteDetalle_ID++;
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}