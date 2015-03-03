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
public class GenerateVentasZona extends SvrProcess{

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
            UtilProcess.initViewer("Ventas por Zona",p_PInstance_ID,getProcessInfo());
        } catch (SQLException ex) {
            Logger.getLogger(GenerateVentasZona.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "El proceso ha finalizado con éxito.";
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
			" SELECT AD_CLIENT_ID, AD_ORG_ID, ID, NAME, sum(QTY), sum(NETO) " +
			" FROM ( " +
			"    SELECT i.AD_CLIENT_ID, i.AD_ORG_ID, j.C_JURISDICCION_ID as ID, j.NAME, " +
			"    CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED) " +
			"    ELSE sum(il.QTYINVOICED) END as QTY, " +
			"    CASE WHEN d.DOCBASETYPE = 'ARC' THEN -sum(il.QTYINVOICED * il.PRICEACTUAL * i.COTIZACION) " +
			"    ELSE sum(il.QTYINVOICED * il.PRICEACTUAL) END as NETO " +
			"    FROM C_InvoiceLine il " +
			"	 INNER JOIN C_Invoice i ON (i.C_Invoice_ID = il.C_Invoice_ID)" +
			" 	 INNER JOIN C_Location l ON (l.C_Location_ID = i.Bill_Location_ID)" +
			"	 INNER JOIN C_JURISDICCION j ON (j.C_JURISDICCION_ID = l.C_JURISDICCION_ID)" +
			"	 INNER JOIN C_DocType d ON (d.C_DocType_ID = i.C_DocType_ID)" +
			getSqlWhere("i.ISSOTRX","i.DATEINVOICED","i.DocStatus") +
			"	 GROUP BY i.AD_CLIENT_ID, i.AD_ORG_ID, j.C_JURISDICCION_ID, j.NAME, d.DOCBASETYPE" +
			" ) " +
			" GROUP BY AD_CLIENT_ID, AD_ORG_ID, ID, NAME " +
			"		HAVING sum(QTY)<>0 " + 
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
		
		if (!concepto)
			sqlWhere+= " AND il.M_PRODUCT_ID is NOT null AND il.C_CHARGE_ID is null";
    	
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
    	log.info("Borrado de la tabla temporal T_VentasZona");
    	DB.executeUpdate("Delete from T_VentasZona", null);
    	log.info("Borrado de la tabla temporal T_VentasZona_HDR");
	    DB.executeUpdate("Delete from T_VentasZona_HDR", null);
	    
    	String sql = "";
    	try {
			ResultSet rs = getProductos();
			
		    int T_VentasZona_ID=1000000;
		    
		    Integer acumCant = 0;
		    BigDecimal acumNeto = BigDecimal.ZERO;
		    
		    if (rs.next())
		    {
		    	AD_ORG_ID = rs.getInt(2);

		    	sql = "INSERT INTO T_VentasZona_HDR VALUES(?,?,'Y',?,?,?,?)";
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
		    	
		    	while (!rs.isAfterLast()){
			    	//  Ingreso en la tabla cabecera
			    	sql = "INSERT INTO T_VentasZona VALUES(?,?,'Y',?,?,?,?,?,?,?)";
			    	ps = DB.prepareStatement(sql, null);
			        ps.setInt(1, getAD_Client_ID());			//	CLIENTE
			        ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
			        ps.setInt(3, T_VentasZona_ID);				//	C_VENTASVENDEDOR_ID
			        ps.setInt(4, p_PInstance_ID);				//	INSTANCE
			        ps.setInt(5, rs.getInt(3));					//	C_JURISDICCION_ID
			        ps.setString(6, rs.getString(4));			//	NAME JURISDICCION
			        ps.setInt(7, rs.getInt(5));					// 	CANTIDAD
			        ps.setBigDecimal(8, rs.getBigDecimal(6));	// 	IMPORTE NETO
			        ps.setTimestamp(9, fromDate);				// 	FECHA
			        
			        ps.executeUpdate();
			        DB.commit(true, get_TrxName());
			        
			        ps.close();

			        acumCant = acumCant + rs.getInt(5);
			        acumNeto = acumNeto.add(rs.getBigDecimal(6));
			        T_VentasZona_ID++;
			        
			        rs.next();
			    }
			    
		    	sql = "INSERT INTO T_VentasZona VALUES(?,?,'Y',?,?,?,?,?,?,?)";
		    	ps = DB.prepareStatement(sql, null);
		        ps.setInt(1, getAD_Client_ID());			//	CLIENTE
		        ps.setInt(2, AD_ORG_ID);					//	ORGANIZACION
		        ps.setInt(3, T_VentasZona_ID);				//	C_VENTASVENDEDOR_ID
		        ps.setInt(4, p_PInstance_ID);				//	INSTANCE
		        ps.setInt(5, 0);							//	C_JURISDICCION_ID
		        ps.setString(6, "TOTAL");					//	NAME
		        ps.setInt(7, acumCant);						// 	CANTIDAD
		        ps.setBigDecimal(8, acumNeto);				// 	IMPORTE NETO
		        ps.setTimestamp(9, fromDate);				// 	FECHA
		        
		        ps.executeUpdate();
		        DB.commit(true, get_TrxName());
		        
		        ps.close();
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
