/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import org.compiere.process.*;
import java.math.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *  Clase que ingresa las filas a la tabla T_CC_BARTNER_DETALLE
 * @author santiago
 */
public class GenerateReporteResumenCuentas extends SvrProcess{

//PARAMETROS DE ENTRADA
int p_instance;
private Timestamp fromDate = null;
private Timestamp toDate = null;
private String fromBPartner = null;
private String toBPartner = null;

protected void prepare(){
    ProcessInfoParameter[] para = getParameter();
    for (int i = 0; i < para.length; i++)
	{
        String name = para[i].getParameterName();
        if(name.equals("DateTrx")){
            fromDate=(Timestamp)para[i].getParameter();
            toDate=(Timestamp)para[i].getParameter_To();
        }
        else
        	if(name.equals("VALUE")){
    			fromBPartner=(String)para[i].getParameter();
    			toBPartner=(String)para[i].getParameter_To();
        	}
        /*
        else
        {	
    		if(name.equals("C_BPartner_ID"))
    			fromBPartner=getBPValue((BigDecimal)para[i].getParameter());
    		else
	    		if(name.equals("C_BPartner_2"))
	    			toBPartner=getBPValue((BigDecimal)para[i].getParameter_To());
        }
        */
    }
    
    p_instance = getAD_PInstance_ID();
}

protected String doIt() throws Exception {
    //BORRADO DE LA TABLA T_LIBROMAYORDETALLE
    String sql;
    log.info("Comienzo del proceso de Resumen Cuenta Corriente");
    
    log.info("Borrado de la tabla temporal T_CTACTE_LOCATION");
    DB.executeUpdate("Delete from T_CTACTE_LOCATION", null);
    
    ResultSet rs = getComprobantes();
    
    BigDecimal TDebe = BigDecimal.ZERO;
    BigDecimal THaber = BigDecimal.ZERO;
    BigDecimal TTotal = BigDecimal.ZERO;
    BigDecimal TVencido = BigDecimal.ZERO;
    BigDecimal TVence = BigDecimal.ZERO;
    
    Timestamp ts = new Timestamp(System.currentTimeMillis());
    if (fromDate!=null)
    	ts = fromDate;
    else if (toDate!=null)
    		ts = toDate;
    String val = "";
    if (fromBPartner!=null)
    	val = fromBPartner;
    else if (toDate!=null)
    		val = toBPartner;
    
    int T_CTACTE_LOCATION_ID=1000000;
    
    if (rs.next())
    {
    	int cliente = rs.getInt(1);
    	int org = rs.getInt(2);
        int bpartner = rs.getInt(3);
        		
    	while (!rs.isAfterLast()){
	    	sql = "INSERT INTO T_CTACTE_LOCATION VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	    	PreparedStatement ps = DB.prepareStatement(sql, null);
	        ps.setInt(1, rs.getInt(1));					//	CLIENTE
	        ps.setInt(2, rs.getInt(2));					//	ORGANIZACION
	        ps.setInt(3, rs.getInt(3));					//	C_BPARTNER_ID
	        ps.setInt(4, p_instance);					//	INSTANCE
	        
	        ps.setString(5, rs.getString(4));			// 	VALUE
	        ps.setString(6, rs.getString(5));			//	NAME
	        
	        if (rs.getBigDecimal(6)!=null)
	        	ps.setBigDecimal(7, rs.getBigDecimal(6));	//	DEBE
	        else
	        	ps.setBigDecimal(7, BigDecimal.ZERO);
	        if (rs.getBigDecimal(7)!=null)
	        	ps.setBigDecimal(8, rs.getBigDecimal(7));	//	HABER
	        else
	        	ps.setBigDecimal(8, BigDecimal.ZERO);
	        if (rs.getBigDecimal(8)!=null)
	        	ps.setBigDecimal(10, rs.getBigDecimal(8));	//	VENCIDO
	        else
	        	ps.setBigDecimal(10, BigDecimal.ZERO);
	        if (rs.getBigDecimal(9)!=null)
	        	ps.setBigDecimal(11, rs.getBigDecimal(9));	//	AVENCER
	        else
	        	ps.setBigDecimal(11, BigDecimal.ZERO);
	        
	        ps.setBigDecimal(9, rs.getBigDecimal(6).subtract(rs.getBigDecimal(7)));
	        											//	TOTAL
	        
	        ps.setInt(12, rs.getInt(10));				//	BP_LOCATION_ID
	        ps.setString(13, rs.getString(11));			// 	BP_LOCATION_NAME
	        
	        ps.setTimestamp(14, ts);					// 	DATETRX
	        ps.setInt(15, rs.getInt(3));				//	C_BPARTNER_2
	        ps.setInt(16, T_CTACTE_LOCATION_ID);		//	T_CTACTE_LOCATION_ID
	        									 
	        ps.executeUpdate();
	        ps.close();
	
	        if (rs.getBigDecimal(6)!=null)
	        	TDebe = TDebe.add(rs.getBigDecimal(6));
	        if (rs.getBigDecimal(7)!=null)
	        	THaber = THaber.add(rs.getBigDecimal(7));
	        if (rs.getBigDecimal(6)!=null && rs.getBigDecimal(7)!=null)
	        	TTotal = TTotal.add(rs.getBigDecimal(6).subtract(rs.getBigDecimal(7)));
	        if (rs.getBigDecimal(8)!=null)
	        	TVencido = TVencido.add(rs.getBigDecimal(8));
	        if (rs.getBigDecimal(9)!=null)
	        	TVence = TVence.add(rs.getBigDecimal(9));
	        
	        rs.next();
	        
	        // 	Incremento T_CTACTE_LOCATION_ID
	        T_CTACTE_LOCATION_ID++;
	    }
	    
	    sql = "INSERT INTO T_CTACTE_LOCATION VALUES(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    	PreparedStatement ps = DB.prepareStatement(sql, null);
        ps.setInt(1, cliente);				//	CLIENTE
        ps.setInt(2, org);					//	ORGANIZACION
        ps.setInt(3, bpartner);				//	C_BPARTNER_ID
        ps.setInt(4, p_instance);			//	INSTANCE
        ps.setString(5, val);				// 	VALUE
        ps.setString(6, "TOTAL");			//	NAME
        ps.setBigDecimal(7, TDebe);			//	DEBE
        ps.setBigDecimal(8, THaber);		//	HABER
        ps.setBigDecimal(9, TTotal);		//	TOTAL
        ps.setBigDecimal(10, TVencido);		//	VENCIDO
        ps.setBigDecimal(11, TVence);		//	AVENCER
        ps.setInt(12, 0);					//	BP_LOCATION_ID
        ps.setString(13, null);				// 	BP_LOCATION_NAME
        ps.setTimestamp(14, ts);			// 	DATETRX
        ps.setInt(15, bpartner);			//	C_BPARTNER_2
        ps.setInt(16, T_CTACTE_LOCATION_ID);//	T_CTACTE_LOCATION_ID
        
        ps.executeUpdate();
        ps.close();

	    UtilProcess.initViewer("Resumen de Cuentas Corrientes Deudoras",getAD_PInstance_ID(),getProcessInfo());
	    return "";
    } 
    
	return "El Reporte no arrojo Datos para los parámetros ingresados";
    
}
    /** BISion - 08/05/2009 - Daniel Gini
     * Metodo que retorna los comprobantes por socio de negocio, filtrando por los parametros ingresados
     * ordenados por fecha del comprobante.
     * @return ResultSet
     * @throws Exception
     */
	private ResultSet getComprobantes()		throws Exception{
        //CONSULTA PARA OBTENER LOS DATOS
		String sql =
		"	SELECT CLIENTE, ORG, BP_ID, CLAVE, NOMBRE, SUM(DEBE), SUM(HABER),amountvencido(BP_ID,C_BPartner_Location_ID), amountavencer(BP_ID,C_BPartner_Location_ID),C_BPartner_Location_ID, C_BPartner_Location_Name" +
		"	FROM RV_CTACTEXLOCALIDAD " +
			getSQLWhere() +
		"	GROUP BY CLIENTE, ORG, BP_ID,CLAVE, NOMBRE, amountvencido(BP_ID,C_BPartner_Location_ID), amountavencer(BP_ID,C_BPartner_Location_ID),C_BPartner_Location_ID, C_BPartner_Location_Name" +
		"	ORDER BY NOMBRE,C_BPartner_Location_ID";
		
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
        if (fromBPartner!=null){
            pstmt.setString(paramIndex, fromBPartner);
            paramIndex++;
        }
        if (toBPartner!=null){
            pstmt.setString(paramIndex, toBPartner);
            paramIndex++;
        }
        
        return pstmt.executeQuery();
    }
	
	/** BISion - 08/05/2009 - Daniel Gini
     * Metodo que retorna la clausula "WHERE"
     * @return String
     */
    private String getSQLWhere(){

    	// 	 WRITE WHERE
		String sqlDate = null;
		String sqlPartner = null;
		
		if (toDate!=null && fromDate!=null)
			sqlDate= " FECHA BETWEEN ? AND ? ";
		else if (toDate!=null && fromDate==null)
			sqlDate= " FECHA <= ? ";
		else if (toDate==null && fromDate!=null)
			sqlDate= " FECHA >= ? ";
		
		if (toBPartner!=null && fromBPartner!=null)
			sqlPartner= " CLAVE >= ? AND CLAVE <= ? ";
		else if (toBPartner!=null && fromBPartner==null)
			sqlPartner= " CLAVE <= ? ";
		else if (toBPartner==null && fromBPartner!=null)
			sqlPartner= " CLAVE >= ? ";
		
		if (sqlDate==null)
		{	if (sqlPartner==null)
				return "";
			return "WHERE " + sqlPartner;
		}	
		else
			if (sqlPartner==null)
				return "WHERE " + sqlDate;
		
		return "WHERE " + sqlDate + " AND " + sqlPartner;
	}
    
}
