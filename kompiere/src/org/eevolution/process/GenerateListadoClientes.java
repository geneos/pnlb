/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.SQLException;
import org.compiere.process.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.*;
import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *
 * @author Daniel Gini Bision
 */
public class GenerateListadoClientes extends SvrProcess{
    
    int p_instance;
    private BigDecimal bpartner = null;
    long org;
        
     protected String doIt() throws Exception{
 		String mje = "success";
    	 try {
    		
    		PreparedStatement pstmt;
    		StringBuffer sqlQuery;
    		
        	if (bpartner !=null)
    			sqlQuery = new StringBuffer("SELECT * FROM RV_BPARTNER_CUSTOMER WHERE C_BPartner_ID = " + bpartner.longValue());
    		else
    			sqlQuery = new StringBuffer("SELECT * FROM RV_BPARTNER_CUSTOMER");
        	
    		pstmt = DB.prepareStatement(sqlQuery.toString(),get_TrxName());
        	ResultSet rs = pstmt.executeQuery();
        	
        	DB.executeUpdate("DELETE from T_BPARTNER_CUS_LIST", null);
        	int ID = 0; 
    		boolean enc = false;
    		while (rs.next())
    		{	
    			ID = insertar(rs, ID);
    			enc = true;
    		}
    		
    		rs.close();
    		pstmt.close();

    		DB.commit(false, get_TrxName());
    		if (!enc)
    			mje = "No hay datos para el Socio de Negocio";
    		else
    			UtilProcess.initViewer("Listado de Clientes",p_instance,getProcessInfo());	
    	}	
     	catch (SQLException ex) {
    	 Logger.getLogger(GenerateListadoClientes.class.getName()).log(Level.SEVERE, null, ex);
    	}
      
        return mje; 
    }
     
    private static final int MAXROWS = 10;
    
    private int insertar(ResultSet rs, int ID)
    {
    	String sqlInsert = "INSERT into T_BPARTNER_CUS_LIST values(?,?,'Y',?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?, ?)";
        PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
        try {
    		
        	pstmtInsert.setLong(1, rs.getLong("AD_Client_ID"));
	        pstmtInsert.setLong(2, rs.getLong("AD_Org_ID"));
	        pstmtInsert.setLong(3, rs.getLong("C_BPARTNER_ID"));
	        pstmtInsert.setString(4, rs.getString("LEY1"));
	        pstmtInsert.setString(5, rs.getString("LEY2"));
	        pstmtInsert.setString(6, rs.getString("LEY3"));
	        pstmtInsert.setString(7, rs.getString("LEY4"));
	        pstmtInsert.setString(8, rs.getString("LEY5"));
	        pstmtInsert.setInt(9, p_instance);
	        
	        String sqlQuery = " SELECT * " +
							  " FROM RV_CUSTOMER_DETAIL " +
							  " WHERE C_BPARTNER_ID = " + rs.getLong("C_BPARTNER_ID");
	        PreparedStatement psq = DB.prepareStatement(sqlQuery, get_TrxName());
	        ResultSet set = psq.executeQuery();
	        int i=10;
	        int count = 1;
	        while (set.next() && count<=MAXROWS)
        	{
        		String localizacion = "Domicilio: ";
        		if (set.getString("ADDRESS")!=null)
        			localizacion += set.getString("ADDRESS");
        		if (set.getString("CITY")!=null)
        			localizacion += " - " + set.getString("CITY");
        		if (set.getString("POSTAL")!=null)
        			localizacion += " - " + set.getString("POSTAL");
        		if (set.getString("COUNTRY")!=null)
        			localizacion += " - " + set.getString("COUNTRY");
        		if (set.getBigDecimal("ALIPER")!=null)
        			localizacion += " - Alícuota IB " + set.getBigDecimal("ALIPER") + "%";
        		else
        			localizacion += " - Alícuota IB 0%";
        		if (set.getString("ISBILLTO").equals("Y"))
        			localizacion += " - Facturar";
        		if (set.getString("ISSHIPTO").equals("Y"))
        			localizacion += " - Entregar";
        		
                pstmtInsert.setString(i, localizacion);
                count++;
                i++;
        	}
	        set.close();
	        psq.close();
	        while (i<10+MAXROWS)
	        {
	        	pstmtInsert.setString(i, null);
	        	i++;
	        }
	        
	        pstmtInsert.setInt(i, ID);
	        
	        pstmtInsert.executeQuery();
	        pstmtInsert.close();
	        
	        DB.commit(false, get_TrxName());
        }	
     	catch (SQLException ex) {
     		Logger.getLogger(GenerateListadoClientes.class.getName()).log(Level.SEVERE, null, ex);
     		return ID + 1;
    	}
     	return ID + 1;
    }
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
    	
        if (para.length > 0)
       		bpartner = (BigDecimal)para[0].getParameter();
        
       	p_instance = getAD_PInstance_ID();
    }    

}
