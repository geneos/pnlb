/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

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
public class GenerateVentasCliente extends SvrProcess{

    private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    private String fromValue = "";
    private String toValue = "";
    
    private Integer AD_CLIENT_ID = 0;
    private Integer AD_ORG_ID = 0;
    private Integer C_BPARTNER_ID = 0;
    private String VALUE = "";
    private Boolean concepto = false;
    
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
            	if(name.equals("VALUE")){
                	fromValue=(String)para[i].getParameter();
                    toValue=(String)para[i].getParameter_To();
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
        C_BPARTNER_ID = getC_BPartner_ID(fromValue);
        VALUE = fromValue;
    }

   
    protected String doIt() {
        try {
        	loadReporte();
        	
        	DB.commit(true, get_TrxName());
            UtilProcess.initViewer("Ventas por Cliente",p_PInstance_ID,getProcessInfo());
        } catch (SQLException ex) {
            Logger.getLogger(GenerateVentasCliente.class.getName()).log(Level.SEVERE, null, ex);
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
    
    protected int getC_BPartner_ID(String value)
    {
	    try	{	
    		String sql = 
			    	" SELECT C_BPARTNER_ID " +
			    	" FROM C_BPARTNER" +
			    	" WHERE AD_CLIENT_ID = ? AND VALUE = ?";
	    	
	    	PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
	    	pstmt.setLong(1, AD_CLIENT_ID);
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
     * Metodo que retorna los productos facturados para los socios de negocios filtrados
     * 
     * @return ResultSet
     * @throws Exception
     */
	private ResultSet getProductos()		throws Exception{
        //CONSULTA PARA OBTENER LOS DATOS
		String sql =
			" SELECT AD_CLIENT_ID, AD_ORG_ID, ID, NAME, sum(QTY), sum(NETO), VALUE " +
			" FROM ( " +
			" 	SELECT i.AD_CLIENT_ID, i.AD_ORG_ID, b.C_BPartner_ID as ID, b.name as NAME, " +
			" 	CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED) " +
			" 	ELSE sum(il.QTYINVOICED) END as QTY, " +
			" 	CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED * il.PRICEACTUAL * i.COTIZACION) " +
			" 	ELSE sum(il.QTYINVOICED * il.PRICEACTUAL) END as NETO, " +
			" 	b.VALUE as VALUE  " +
			" 	FROM C_InvoiceLine il " +
			" 	INNER JOIN C_Invoice i ON (i.C_Invoice_ID = il.C_Invoice_ID) " +
			" 	INNER JOIN C_BPartner b ON (i.C_BPartner_ID = b.C_BPartner_ID) " +
			" 	INNER JOIN C_DocType d ON (d.C_DocType_ID = i.C_DocType_ID) " +
				getSqlWhere("i.ISSOTRX","i.DATEINVOICED","i.DocStatus") +
			" 	GROUP BY i.AD_CLIENT_ID, i.AD_ORG_ID, b.C_BPartner_ID, b.name, b.VALUE,d.DOCBASETYPE " +
			" ) " +
			" GROUP BY AD_CLIENT_ID, AD_ORG_ID, ID, name, VALUE " +
			"       HAVING sum(QTY)<>0 " +
			" ORDER BY name ";
			
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
            pstmt.setString(paramIndex, fromValue);
            paramIndex++;
        }
        if (toValue!=null){
            pstmt.setString(paramIndex, toValue);
            paramIndex++;
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
			sqlWhere+= " AND b.VALUE >= ? AND b.VALUE <= ?";
		else if (toValue!=null && fromValue==null)
			sqlWhere+= " AND b.VALUE <= ?";
		else if (toValue==null && fromValue!=null)
			sqlWhere+= " AND b.VALUE >= ?";
		
		if (!concepto)
			sqlWhere+= " AND il.M_PRODUCT_ID is NOT null AND il.C_CHARGE_ID is null";
    	
		sqlWhere+= " AND "+param3+" IN ('CO','CL')";
    	
		return sqlWhere;
    }
	
    private void loadReporte()
    {
    	log.info("Borrado de la tabla temporal T_VentasCliente");
    	DB.executeUpdate("Delete from T_VentasCliente", null);
    	log.info("Borrado de la tabla temporal T_VentasCliente_HDR");
	    DB.executeUpdate("Delete from T_VentasCliente_HDR", null);

	    String sql="";
	    try {
			ResultSet rs = getProductos();
			
		    int T_VentasCliente_ID=1000000;
		    
		    Integer acumCant = 0;
		    BigDecimal acumNeto = BigDecimal.ZERO;
		    
		    if (rs.next())
		    {
		    	AD_ORG_ID = rs.getInt(2);
		    	
		    	sql = "INSERT INTO T_VentasCliente_HDR VALUES(?,?,'Y',?,?,?,?,?,?)";
		    	PreparedStatement ps = DB.prepareStatement(sql, null);
		        ps.setInt(1, getAD_Client_ID());				//	CLIENTE
		        ps.setInt(2, rs.getInt(2));						//	ORGANIZACION
		        ps.setInt(3, p_PInstance_ID);					//	INSTANCE
		        ps.setString(4, getFechasPara(fromDate, toDate));//	FECHAS
		        ps.setString(5, fromValue + " - " + toValue);	//	CLAVES
		        ps.setTimestamp(6, fromDate);					// 	FECHA
		        ps.setString(7, fromValue);						// 	VALUE
		        ps.setString(8, (concepto==true ? "Y" : "N"));	//	CONCEPTO
		        
		        ps.executeUpdate();
		        ps.close();
		        DB.commit(true, get_TrxName());
		        
		    	while (!rs.isAfterLast()){
			    	//  Ingreso en la tabla cabecera
			    	sql = "INSERT INTO T_VentasCliente VALUES(?,?,'Y',?,?,?,?,?,?,?,?)";
			    	ps = DB.prepareStatement(sql, null);
			        ps.setInt(1, getAD_Client_ID());			//	CLIENTE
			        ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
			        ps.setInt(3, T_VentasCliente_ID);			//	C_VENTASCLIENTE_ID
			        ps.setInt(4, p_PInstance_ID);				//	INSTANCE
			        ps.setInt(5, rs.getInt(3));					//	C_BPARTNER_ID
			        ps.setString(6, rs.getString(4));			//	NAME PRODUCT
			        ps.setInt(7, rs.getInt(5));					// 	CANTIDAD
			        ps.setBigDecimal(8, rs.getBigDecimal(6));	// 	IMPORTE NETO
			        ps.setTimestamp(9, fromDate);				// 	FECHA
			        ps.setString(10, fromValue);				// 	VALUE
			        
			        ps.executeUpdate();
			        DB.commit(true, get_TrxName());
			        
			        ps.close();

			        acumCant = acumCant + rs.getInt(5);
			        acumNeto = acumNeto.add(rs.getBigDecimal(6));
			        T_VentasCliente_ID++;
			        
			        rs.next();
			    }
			    
		    	sql = "INSERT INTO T_VentasCliente VALUES(?,?,'Y',?,?,?,?,?,?,?,?)";
		    	ps = DB.prepareStatement(sql, null);
		        ps.setInt(1, getAD_Client_ID());			//	CLIENTE
		        ps.setInt(2, AD_ORG_ID);					//	ORGANIZACION
		        ps.setInt(3, T_VentasCliente_ID);			//	C_VENTASCLIENTE_ID
		        ps.setInt(4, p_PInstance_ID);				//	INSTANCE
		        ps.setInt(5, C_BPARTNER_ID);				//	C_BPARTNER_ID
		        ps.setString(6, "TOTAL");					//	NAME PRODUCT
		        ps.setInt(7, acumCant);						// 	CANTIDAD
		        ps.setBigDecimal(8, acumNeto);				// 	IMPORTE NETO
		        ps.setTimestamp(9, fromDate);				// 	FECHA
		        ps.setString(10, VALUE);					// 	VALUE
		        
		        ps.executeUpdate();
		        DB.commit(true, get_TrxName());
		        
		        ps.close();
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
