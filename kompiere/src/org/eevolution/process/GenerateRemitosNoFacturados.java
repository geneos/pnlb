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
import java.sql.Timestamp;
import java.sql.Date;
import java.util.logging.*;
import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;


/**
 *
 * @author Daniel Gini Bision
 */
public class GenerateRemitosNoFacturados extends SvrProcess{
    
    int p_instance;
    private Timestamp fromDate;
    private Timestamp toDate;
    private BigDecimal bpartner = null;
    long org;
        
     protected String doIt() throws Exception{
    	try {
    		
    		PreparedStatement pstmt;
    		StringBuffer sqlQuery;
    		
        	if (bpartner !=null)
    		{	sqlQuery = new StringBuffer("SELECT s.M_InOut_ID, s.MovementDate, s.DocumentNo, s.AD_Client_ID, s.AD_Org_ID, s.C_BPartner_ID ")
				.append("FROM M_InOut s "
					  + "WHERE s.C_BPartner_ID=? AND s.IsSOTrx='Y' AND s.DocStatus IN ('CL','CO') AND s.MovementDate between ? and ? AND s.M_InOut_ID IN "
					  		+ "(SELECT sl.M_InOut_ID FROM M_InOutLine sl"
					  		+ " LEFT OUTER JOIN M_MatchInv mi ON (sl.M_InOutLine_ID=mi.M_InOutLine_ID)"
					  		+ " GROUP BY sl.M_InOut_ID,mi.M_InOutLine_ID,sl.MovementQty"
					  		+ " HAVING (sl.MovementQty<>SUM(mi.Qty) AND mi.M_InOutLine_ID IS NOT NULL) OR mi.M_InOutLine_ID IS NULL) "
					  + "ORDER BY s.MovementDate");
    		
	    		pstmt = DB.prepareStatement(sqlQuery.toString(),get_TrxName());
	        	pstmt.setInt(1, bpartner.intValue());
	        	pstmt.setTimestamp(2, fromDate);
	        	pstmt.setTimestamp(3, toDate);
    		}
        	else
        	{
        		sqlQuery = new StringBuffer("SELECT s.M_InOut_ID, s.MovementDate, s.DocumentNo, s.AD_Client_ID, s.AD_Org_ID, s.C_BPartner_ID ")
				.append("FROM M_InOut s "
					  + "WHERE s.IsSOTrx='Y' AND s.DocStatus IN ('CL','CO') AND s.MovementDate between ? and ? AND s.M_InOut_ID IN "
					  		+ "(SELECT sl.M_InOut_ID FROM M_InOutLine sl"
					  		+ " LEFT OUTER JOIN M_MatchInv mi ON (sl.M_InOutLine_ID=mi.M_InOutLine_ID)"
					  		+ " GROUP BY sl.M_InOut_ID,mi.M_InOutLine_ID,sl.MovementQty"
					  		+ " HAVING (sl.MovementQty<>SUM(mi.Qty) AND mi.M_InOutLine_ID IS NOT NULL) OR mi.M_InOutLine_ID IS NULL) "
					  + "ORDER BY s.MovementDate");
        		
        		pstmt = DB.prepareStatement(sqlQuery.toString(),get_TrxName());
            	pstmt.setTimestamp(1, fromDate);
            	pstmt.setTimestamp(2, toDate);
        	}
        	
        	ResultSet rs = pstmt.executeQuery();
        	
        	try {
        		
        		DB.executeUpdate("DELETE from T_REMITOSNOFACTURADOS", null);
    			
        		while (rs.next())
        			insertarRemito(rs.getInt(1), rs.getDate(2), rs.getString(3), verificarParcialidad(rs.getInt(1),rs.getInt(4),rs.getInt(5)),rs.getInt(4),rs.getInt(5),rs.getInt(6));
        		
        		rs.close();
        		pstmt.close();
        		
        		Env.getCtx().setProperty("IsSOTrx", "Y");
        		
    			UtilProcess.initViewer("Remitos No Facturados",p_instance,getProcessInfo());

        	}
        	catch (Exception exception) {
        		exception.printStackTrace();
        	} 
     	}	
     	catch (SQLException ex) {
    	 Logger.getLogger(GenerateRemitosNoFacturados.class.getName()).log(Level.SEVERE, null, ex);
    	}
      
        return "success"; 
    }

     protected void insertarRemito(int M_InOut_ID, Date date, String docNro, boolean parcial, int ad_client_id, int ad_org_id, int C_BPartner_ID) throws SQLException
     {
        String sqlInsert = "";
		
		try
		{
			sqlInsert = "insert into T_REMITOSNOFACTURADOS values(?,?,?,?,?,?,?,'Y',?,'Y')";
			PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
			
			pstmtInsert.setInt(1, M_InOut_ID);
			pstmtInsert.setInt(2, ad_client_id);
			pstmtInsert.setInt(3, ad_org_id);
			pstmtInsert.setInt(4, p_instance);
			pstmtInsert.setDate(5, date);
			pstmtInsert.setString(6, docNro);
			pstmtInsert.setString(7, "N");
			if (parcial)
				pstmtInsert.setString(7, "Y");
			pstmtInsert.setInt(8, C_BPartner_ID);
			
			pstmtInsert.executeQuery();
			
			DB.commit(true, get_TrxName());
		
			pstmtInsert.close();
			
		} catch (Exception exception) {
		    exception.printStackTrace();
		}
     }
     
     protected boolean verificarParcialidad(int M_InOut_ID, int ad_client_id, int ad_org_id)
     {
         try {
            
        	String sqlQuery = "";
            
            sqlQuery = 	"SELECT il.M_InOutLine_ID " +
            			"FROM M_InOutLine il LEFT JOIN M_MatchInv mi ON (il.M_InOutLine_ID=mi.M_InOutLine_ID) " +
            			"WHERE il.M_InOut_ID = ? AND il.AD_Client_ID = ? AND il.AD_ORG_ID = ? " +
            				  "AND mi.M_MatchInv_Id IS NOT NULL AND mi.qty <> il.movementqty";
            
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery,get_TrxName());
            pstmt.setInt(1, M_InOut_ID);
            pstmt.setInt(2, ad_client_id);
            pstmt.setInt(3, ad_org_id);
            
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next())
    			return true;
    		
			rs.close();
			pstmt.close();
			
        } catch (SQLException ex) {
            Logger.getLogger(GenerateRemitosNoFacturados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return false;
     }
     
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
    	
        fromDate = (Timestamp)para[0].getParameter();
       	toDate=(Timestamp)para[0].getParameter_To();
       	
        if (para.length > 1)
       		bpartner = (BigDecimal)para[1].getParameter();
        
       	p_instance = getAD_PInstance_ID();
    }    

}
