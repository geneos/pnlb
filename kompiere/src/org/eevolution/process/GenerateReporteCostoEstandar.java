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
 *  Clase que ingresa las filas a la tabla T_COSTOS_ESTANDAR_RESUMEN
 * 
 */
public class GenerateReporteCostoEstandar extends SvrProcess{

//PARAMETROS DE ENTRADA
int p_instance;
BigDecimal costoRecursos = BigDecimal.ZERO;
BigDecimal costoIndirecto = BigDecimal.ZERO;
BigDecimal costoMateriales = BigDecimal.ZERO;


protected void prepare(){

    
    p_instance = 0;
}

protected String doIt() throws Exception {
    //BORRADO DE LA TABLA T_CTACTE_BPARTNER
    String sql;
    log.info("Comienzo del proceso de Costos Estandar");
    
    log.info("Borrado de la tabla temporal T_COSTOS_ESTANDAR_RESUMEN");
    DB.executeUpdate("Delete from T_COSTOS_ESTANDAR_RESUMEN", null);
    

    
    ResultSet rs = getCostos();
    
    int T_COSTOS_ESTANDAR_RESUMEN_ID=1000000;
    
    
    sql = "INSERT INTO T_COSTOS_ESTANDAR_RESUMEN values(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?)";
	PreparedStatement ps = DB.prepareStatement(sql, null);    
	if (rs.next())
    {
		 while (!rs.isAfterLast()){
	    	int cliente = rs.getInt(1);
	    	int organizacion = rs.getInt(2);
	        String value = rs.getString(8);
	        boolean duplicado = false; 
	    	ps.setInt(1, cliente);						// CLIENTE
	        ps.setInt(2, organizacion);					// ORGANIZACION
	        ps.setInt(3, rs.getInt(16));              // PRODUCT_ID
	        ps.setString(4, rs.getString(8));	// VALUE
	        ps.setString(5, rs.getString(9));   // NAME
	        ps.setInt(14, p_instance);					// INSTANCE
	        ps.setInt(6, rs.getInt(10));		// M_PRODUCT_MARKET_ID
	        ps.setInt(7, rs.getInt(11));	// M_PRODUCT_CATEGORY_ID
	        ps.setDate(8, rs.getDate(12));				// FECHA
	        ps.setInt(13, T_COSTOS_ESTANDAR_RESUMEN_ID);
	        ps.setString(12, rs.getString(14)); // UNIDADMEDIDA
	        
	        while (!rs.isAfterLast() && value.equals(rs.getString(8)))	{
	        if (rs.getString(15).equals("R"))
	        	costoRecursos = rs.getBigDecimal(13);
	        else
	        	if (rs.getString(15).equals("M"))
	        			costoMateriales = rs.getBigDecimal(13);
	        	else
	        		costoIndirecto = rs.getBigDecimal(13);
	        duplicado = true;
	        rs.next();
	        }
	        if (!duplicado)
	        	rs.next();
	        ps.setBigDecimal(9, costoMateriales); // COSTOMATERIAL
	        ps.setBigDecimal(10, costoRecursos); // COSTORECURSO
	        ps.setBigDecimal(11, costoIndirecto); // COSTOINDIRECTO
	        costoRecursos = costoMateriales = costoIndirecto = BigDecimal.ZERO;
  	        T_COSTOS_ESTANDAR_RESUMEN_ID++;
  	        ps.executeQuery();
  	}
    	
    }
    	rs.close();
	    ps.close();
	    UtilProcess.initViewer("Resumen Costo Estandar Productos",0,getProcessInfo());
	    return "";
}



	private ResultSet getCostos()		throws Exception{
        //CONSULTA PARA OBTENER LOS DATOS
		String sql = "select p.ad_client_id, p.ad_org_id, p.isactive, p.created, p.createdby," +
					 " p.updated, p.updatedby, p.value, p.name, p.m_product_market_id," +
					 " p.m_product_category_id,sysdate, nvl(mcM.CURRENTCOSTPRICE,0) as COSTO_MATERIALES,c.name," +
					 " 'M' as tipoCosto,p.m_product_id" +
					 " from m_product p" +
					 "  join m_cost mcM on (mcM.m_product_id = p.m_product_id)" +
					 "  join m_costelement ceM on (mcM.m_costelement_id = ceM.m_costelement_id)" +
					 "  join c_uom c on (c.c_uom_id = p.c_uom_id)" +
					 "  where ceM.name = 'Costo Estandar de Materiales' and p.isactive like 'Y' " +
					 
					 "  UNION" +
					 "  (select p.ad_client_id, p.ad_org_id, p.isactive, p.created, p.createdby," +
					 "  p.updated, p.updatedby, p.value, p.name, p.m_product_market_id," +
					 "  p.m_product_category_id,sysdate," +
					 "  nvl(mcR.CURRENTCOSTPRICE,0) as COSTO_MATERIALES,c.name," +
					 "  'R' as tipoCosto,p.m_product_id" +
					 "  from m_product p" +
					 "  join m_cost mcR on (mcR.m_product_id = p.m_product_id)" +
					 "  join m_costelement ceR on (mcR.m_costelement_id = ceR.m_costelement_id)" +
					 "  join c_uom c on (c.c_uom_id = p.c_uom_id)" +
					 "  where ceR.name = 'Costo Estandar de Recursos' and p.isactive like 'Y') " +
					 
					 "UNION" +
					 " (select p.ad_client_id, p.ad_org_id, p.isactive, p.created, p.createdby," +
					 " p.updated, p.updatedby, p.value, p.name, p.m_product_market_id," +
					 " p.m_product_category_id,sysdate," +
					 " nvl(mcI.CURRENTCOSTPRICE,0) as COSTO_MATERIALES,c.name," +
					 " 'I' as tipoCosto,p.m_product_id" +
					 " from m_product p" +
					 " join m_cost mcI on (mcI.m_product_id = p.m_product_id)" +
					 " join m_costelement ceI on (mcI.m_costelement_id = ceI.m_costelement_id)" +
					 " join c_uom c on (c.c_uom_id = p.c_uom_id)" +
					 "  where ceI.name = 'Costo Estandar Indirecto' and p.isactive like 'Y') " +
					 "  order by value";

 

			
		
        PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
 
    	
        return pstmt.executeQuery();
    }
    
}
