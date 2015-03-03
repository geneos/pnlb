/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import org.compiere.model.MProcess;
import org.compiere.model.X_AD_Process;
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
public class GenerateVentasZonaDetalle extends SvrProcess{

    private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    private Boolean concepto = false;
    
    private Integer AD_ORG_ID = 0;
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++)
        {
            String name = para[i].getParameterName();
            if(name.equals("FECHA")){
            	fromDate=(Timestamp)para[i].getParameter();
                toDate=(Timestamp)para[i].getParameter_To();
            }
            else
            	if(name.equals("CONCEPTO")){
            		if (((String)para[i].getParameter()).equals("Y"))
            			concepto = true;
            		else
            			concepto = false;
                }
        }
        p_PInstance_ID = getAD_PInstance_ID();
    }
   
    protected String doIt() {
        try {
        	loadReporte();
        	
        	DB.commit(true, get_TrxName());
        	MProcess process = MProcess.get(getCtx(), getProcessInfo().getAD_Process_ID());
        	/*
        	 * 19-01-2011 Camarzana Mariano
        	 */
        	if (process.getName().equals("Reporte de Ventas por Zona Detallada"))
        		UtilProcess.initViewer("Ventas por Zona Detallada",p_PInstance_ID,getProcessInfo());
        	else
        		UtilProcess.initViewer("Ventas por Zona y Socio Detallada",p_PInstance_ID,getProcessInfo());
        } catch (SQLException ex) {
            Logger.getLogger(GenerateVentasZonaDetalle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "El proceso ha finalizado con éxito.";
    }

    /** 19-01-2010 Camarzana Mariano
     * Metodo que retorna los productos facturados para cada vendedor (Ordenado por zona y cliente)
     * 
     * @return ResultSet
     * @throws Exception
     */
	private ResultSet getProductosCliente()		throws Exception{
        //CONSULTA PARA OBTENER LOS DATOS
		String sql =
			 " SELECT AD_CLIENT_ID, AD_ORG_ID, ZONA_ID, ZONA_NAME, PROD_ID, PROD_NAME, sum(QTY), sum(NETO) " +
			 " FROM ( " +
			 "       SELECT i.AD_CLIENT_ID, i.AD_ORG_ID, j.C_Jurisdiccion_ID as ZONA_ID, j.NAME   || ' - ' ||  cb.name as ZONA_NAME, m.M_Product_ID as PROD_ID, m.NAME as PROD_NAME," +
			 "		 CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED) " +
			 "	   	      ELSE sum(il.QTYINVOICED) END as QTY, " +
			 "   	 CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED * il.PRICEACTUAL * i.COTIZACION) " +
			 " 	 	      ELSE sum(il.QTYINVOICED * il.PRICEACTUAL) END as NETO " +
			 "       FROM C_InvoiceLine il " +
			 " 	 	 INNER JOIN C_Invoice i ON (i.C_Invoice_ID = il.C_Invoice_ID) " +
			 " 	 	 INNER JOIN c_bpartner cb ON (i.c_bpartner_id = cb.c_bpartner_id) " +
			 "       INNER JOIN C_DocType d ON (d.C_DocType_ID = i.C_DocType_ID) " +
			 "       INNER JOIN C_Location l ON (l.C_Location_ID = i.Bill_Location_ID) " +
			 "       INNER JOIN C_JURISDICCION j ON (j.C_JURISDICCION_ID = l.C_JURISDICCION_ID) " +
			 "       INNER JOIN M_Product m ON (m.M_Product_ID = il.M_Product_ID) " +
			 getSqlWhere("i.ISSOTRX","i.DATEINVOICED","i.DocStatus") +                                 
			 " 	 	 GROUP BY i.AD_CLIENT_ID, i.AD_ORG_ID, j.C_Jurisdiccion_ID, j.NAME  || ' - '   || cb.name, m.M_Product_ID, m.NAME, d.DOCBASETYPE " +
			 "		";
			 if (concepto)
		 	 {
				sql = sql +
				"  UNION" +
				"     	(  SELECT i.AD_CLIENT_ID, i.AD_ORG_ID, j.C_Jurisdiccion_ID as ZONA_ID, jj.NAME  || ' - '   || cb.name as ZONA_NAME, h.C_CHARGE_ID as PROD_ID, h.NAME as PROD_NAME, " +
				"		   CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED) " +
				"	   	      	ELSE sum(il.QTYINVOICED) END as QTY, " +
				"   	   CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED * il.PRICEACTUAL * i.COTIZACION) " +
				" 	 	      	ELSE sum(il.QTYINVOICED * il.PRICEACTUAL) END as NETO " +
				"          FROM C_InvoiceLine il " +
				"          INNER JOIN C_Invoice i ON (i.C_Invoice_ID = il.C_Invoice_ID)" +
				" 	 	   INNER JOIN c_bpartner cb ON (i.c_bpartner_id = cb.c_bpartner_id) " +
	 	        "		   INNER JOIN C_Location l ON (l.C_Location_ID = i.Bill_Location_ID)" +
	 	        "   	   INNER JOIN C_JURISDICCION j ON (j.C_JURISDICCION_ID = l.C_JURISDICCION_ID) " +
	 	        "          INNER JOIN C_Charge h ON (il.C_CHARGE_ID = h.C_CHARGE_ID) " +
				"		   INNER JOIN C_DocType d ON (d.C_DocType_ID = i.C_DocType_ID) " +
				getSqlWhere("i.ISSOTRX","i.DATEINVOICED","i.DocStatus") +
				"          GROUP BY i.AD_CLIENT_ID, i.AD_ORG_ID, j.C_Jurisdiccion_ID, j.NAME  || ' - '   || cb.name, h.C_CHARGE_ID, h.NAME, d.DOCBASETYPE" +
				"     	)";
			 } 
			 sql = sql +
 			 "	  )" +
			 " GROUP BY AD_CLIENT_ID, AD_ORG_ID, ZONA_ID, ZONA_NAME, PROD_ID, PROD_NAME " +
			 "       HAVING sum(QTY)<>0 " +
			 " ORDER BY ZONA_NAME, PROD_NAME"; 

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
        }
		
        return pstmt.executeQuery();
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
			 " SELECT AD_CLIENT_ID, AD_ORG_ID, ZONA_ID, ZONA_NAME, PROD_ID, PROD_NAME, sum(QTY), sum(NETO) " +
			 " FROM ( " +
			 "       SELECT i.AD_CLIENT_ID, i.AD_ORG_ID, j.C_Jurisdiccion_ID as ZONA_ID, j.NAME as ZONA_NAME, m.M_Product_ID as PROD_ID, m.NAME as PROD_NAME," +
			 "		 CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED) " +
			 "	   	      ELSE sum(il.QTYINVOICED) END as QTY, " +
			 "   	 CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED * il.PRICEACTUAL * i.COTIZACION) " +
			 " 	 	      ELSE sum(il.QTYINVOICED * il.PRICEACTUAL) END as NETO " +
			 "       FROM C_InvoiceLine il " +
			 " 	 	 INNER JOIN C_Invoice i ON (i.C_Invoice_ID = il.C_Invoice_ID) " +
			 "       INNER JOIN C_DocType d ON (d.C_DocType_ID = i.C_DocType_ID) " +
			 "       INNER JOIN C_Location l ON (l.C_Location_ID = i.Bill_Location_ID) " +
			 "       INNER JOIN C_JURISDICCION j ON (j.C_JURISDICCION_ID = l.C_JURISDICCION_ID) " +
			 "       INNER JOIN M_Product m ON (m.M_Product_ID = il.M_Product_ID) " +
			 getSqlWhere("i.ISSOTRX","i.DATEINVOICED","i.DocStatus") +                                 
			 " 	 	 GROUP BY i.AD_CLIENT_ID, i.AD_ORG_ID, j.C_Jurisdiccion_ID, j.NAME, m.M_Product_ID, m.NAME, d.DOCBASETYPE " +
			 "		";
			 if (concepto)
		 	 {
				sql = sql +
				"  UNION" +
				"     	(  SELECT i.AD_CLIENT_ID, i.AD_ORG_ID, j.C_Jurisdiccion_ID as ZONA_ID, j.NAME as ZONA_NAME, h.C_CHARGE_ID as PROD_ID, h.NAME as PROD_NAME, " +
				"		   CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED) " +
				"	   	      	ELSE sum(il.QTYINVOICED) END as QTY, " +
				"   	   CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED * il.PRICEACTUAL * i.COTIZACION) " +
				" 	 	      	ELSE sum(il.QTYINVOICED * il.PRICEACTUAL) END as NETO " +
				"          FROM C_InvoiceLine il " +
				"          INNER JOIN C_Invoice i ON (i.C_Invoice_ID = il.C_Invoice_ID)" +
	 	        "		   INNER JOIN C_Location l ON (l.C_Location_ID = i.Bill_Location_ID)" +
	 	        "   	   INNER JOIN C_JURISDICCION j ON (j.C_JURISDICCION_ID = l.C_JURISDICCION_ID) " +
	 	        "          INNER JOIN C_Charge h ON (il.C_CHARGE_ID = h.C_CHARGE_ID) " +
				"		   INNER JOIN C_DocType d ON (d.C_DocType_ID = i.C_DocType_ID) " +
				getSqlWhere("i.ISSOTRX","i.DATEINVOICED","i.DocStatus") +
				"          GROUP BY i.AD_CLIENT_ID, i.AD_ORG_ID, j.C_Jurisdiccion_ID, j.NAME, h.C_CHARGE_ID, h.NAME, d.DOCBASETYPE" +
				"     	)";
			 } 
			 sql = sql +
 			 "	  )" +
			 " GROUP BY AD_CLIENT_ID, AD_ORG_ID, ZONA_ID, ZONA_NAME, PROD_ID, PROD_NAME " +
			 "       HAVING sum(QTY)<>0 " +
			 " ORDER BY ZONA_NAME, PROD_NAME"; 

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
    	log.info("Borrado de la tabla temporal T_VentasZonaDetalle");
    	DB.executeUpdate("Delete from T_VentasZonaDetalle", null);
    	log.info("Borrado de la tabla temporal T_VentasZonaDetalle_HDR");
	    DB.executeUpdate("Delete from T_VentasZonaDetalle_HDR", null);
	    
    	String sql = "";
    	try {
			
    		/*
    		 * 19 -01- 2011 Camarzana Mariano
    		 */
    		ResultSet rs;
           	MProcess process = MProcess.get(getCtx(), getProcessInfo().getAD_Process_ID());
        	if (process.getName().equals("Reporte de Ventas por Zona Detallada"))
        		rs = getProductos();
        	else
        		rs = getProductosCliente();
        	
        	/*
        	 * Fin modificacion
        	 */
		    int T_VentasZonaDetalle_ID=1000000;
		    
		    Integer acumCant = 0;
		    BigDecimal acumNeto = BigDecimal.ZERO;
		    
		    if (rs.next())
		    {
		    	AD_ORG_ID = rs.getInt(2);

		    	sql = "INSERT INTO T_VentasZonaDetalle_HDR VALUES(?,?,'Y',?,?,?,?)";
		    	PreparedStatement ps = DB.prepareStatement(sql, null);
		        ps.setInt(1, getAD_Client_ID());				//	CLIENTE
		        ps.setInt(2, rs.getInt(2));						//	ORGANIZACION
		        ps.setInt(3, p_PInstance_ID);					//	INSTANCE
		        ps.setString(4, getFechasPara(fromDate,toDate));//	FECHAS
		        ps.setString(5, (concepto==true ? "Y" : "N"));	//	CONCEPTO
		        ps.setTimestamp(6, fromDate);					// 	FECHA
		        
		        ps.executeUpdate();
		        ps.close();
		        DB.commit(true, get_TrxName());
		    	
		    	while (!rs.isAfterLast())
		    	{
		    		String jurisdiccion; 
		    		if (process.getName().equals("Reporte de Ventas por Zona Detallada"))
		    			jurisdiccion = rs.getString(3);
		    		else
		    			jurisdiccion = rs.getString(4);
			        //Ingreso en la tabla detalle
			    	sql = "INSERT INTO T_VentasZonaDetalle VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?)";
			    	ps = DB.prepareStatement(sql, null);
			        ps.setInt(1, getAD_Client_ID());			//	CLIENTE
			        ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
			        ps.setInt(3, T_VentasZonaDetalle_ID);		//	C_VENTASZONADETALLE_ID
			        ps.setInt(4, p_PInstance_ID);				//	INSTANCE
			        ps.setInt(5, rs.getInt(3));					//	C_JURISDICCION_ID
			        ps.setString(6, rs.getString(4));			//	NAME JURISDICCION
			        ps.setInt(7, rs.getInt(5));					//	M_PRODUCT_ID
			        ps.setString(8, rs.getString(6));			//	NAME PRODUCTO
			        ps.setInt(9, rs.getInt(7));					// 	CANTIDAD
			        ps.setBigDecimal(10, rs.getBigDecimal(8));	// 	IMPORTE NETO
			        ps.setTimestamp(11, fromDate);				// 	FECHA
			        
			        ps.executeUpdate();
			        DB.commit(true, get_TrxName());
			        
			        ps.close();
			        
			        acumCant = rs.getInt(7);
			        acumNeto = rs.getBigDecimal(8);
			        T_VentasZonaDetalle_ID++;

			        
			        
			        String busqueda=null;
			        
			        if(rs.next())
			        	if (process.getName().equals("Reporte de Ventas por Zona Detallada"))
			        		busqueda = rs.getString(3);
			        	else
			        		busqueda = rs.getString(4);
			        
			        
			        
			        		        

			        while (!rs.isAfterLast() && jurisdiccion.equals(busqueda))
			    	{
		    			//  Ingreso en la tabla detalle
				    	sql = "INSERT INTO T_VentasZonaDetalle VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?)";
				    	ps = DB.prepareStatement(sql, null);
				        ps.setInt(1, getAD_Client_ID());			//	CLIENTE
				        ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
				        ps.setInt(3, T_VentasZonaDetalle_ID);		//	C_VENTASZONADETALLE_ID
				        ps.setInt(4, p_PInstance_ID);				//	INSTANCE
				        ps.setInt(5, rs.getInt(3));					//	C_JURISDICCION_ID
				        ps.setString(6, "");						//	NAME JURISDICCION
				        ps.setInt(7, rs.getInt(5));					//	M_PRODUCT_ID
				        ps.setString(8, rs.getString(6));			//	NAME PRODUCTO
				        ps.setInt(9, rs.getInt(7));					// 	CANTIDAD
				        ps.setBigDecimal(10, rs.getBigDecimal(8));	// 	IMPORTE NETO
				        ps.setTimestamp(11, fromDate);				// 	FECHA
				        
				        ps.executeUpdate();
				        DB.commit(true, get_TrxName());
				        
				        ps.close();
	
				        acumCant = acumCant + rs.getInt(7);
				        acumNeto = acumNeto.add(rs.getBigDecimal(8));
				        T_VentasZonaDetalle_ID++;
		        
				        if (rs.next()){
					        if (process.getName().equals("Reporte de Ventas por Zona Detallada"))
					        	busqueda = rs.getString(3);
					        else
					        	busqueda = rs.getString(4);
				        	
				        }
				        	
				    }
		
			    	sql = "INSERT INTO T_VentasZonaDetalle VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?)";
			    	ps = DB.prepareStatement(sql, null);
			        ps.setInt(1, getAD_Client_ID());			//	CLIENTE
			        ps.setInt(2, AD_ORG_ID);					//	ORGANIZACION
			        ps.setInt(3, T_VentasZonaDetalle_ID);		//	C_VENTASZONADETALLE_ID
			        ps.setInt(4, p_PInstance_ID);				//	INSTANCE
			        ps.setInt(5, 0);							//	C_JURISDICCION_ID
			        ps.setString(6, "");						//	NAME JURISDICCION
			        ps.setInt(7, 0);							//	M_PRODUCT_ID
			        ps.setString(8, "TOTAL");					//	NAME PRODUCTO
			        ps.setInt(9, acumCant);						// 	CANTIDAD
			        ps.setBigDecimal(10, acumNeto);				// 	IMPORTE NETO
			        ps.setTimestamp(11, fromDate);				// 	FECHA
			        
			        ps.executeUpdate();
			        DB.commit(true, get_TrxName());
			        
			        ps.close();
			        
			        T_VentasZonaDetalle_ID++;
		    	}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
