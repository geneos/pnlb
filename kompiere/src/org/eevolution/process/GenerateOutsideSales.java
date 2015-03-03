/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import org.eevolution.tools.UtilProcess;
import java.math.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import org.compiere.util.*;

/**
 *
 * @author Nadia
 */
public class GenerateOutsideSales extends SvrProcess {
        
        private int     p_PInstance_ID;
        
        private Timestamp fromDate;
        private Timestamp toDate;
        private String prodDrug="sin droga";

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();

               
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
                        fromDate=(Timestamp)para[i].getParameter();
                        toDate=(Timestamp)para[i].getParameter_To();
                        p_PInstance_ID = getAD_PInstance_ID();

		}
	}	

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
                String sqlRemove;
               
                sqlRemove="delete from T_List_Outside_Sale";
                DB.executeUpdate(sqlRemove,null);
               
                String sqlQuery;
                String sqlInsert;
                
                sqlQuery= "SELECT AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,C_COUNTRY_ID,COUNTRY,M_PRODUCT_ID,COD_PROD,PROD_DESC,SUM(UNITS)AS UNITS ,SUM(NETVALUE) AS NETVALUE " +
                          "FROM RV_LIST_OUTSIDE_SALES " +
                          "WHERE C_INVOICE_DATE BETWEEN ? AND ? " +
                          "GROUP BY AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,C_COUNTRY_ID,COUNTRY,M_PRODUCT_ID,COD_PROD,PROD_DESC " +
                          "order by UNITS asc,NETVALUE ASC";
                 
                 PreparedStatement pstmt = null;
                 PreparedStatement pstmtInsert = null;   

                

		 try {
                        System.out.println(sqlQuery);
                        pstmt=DB.prepareStatement(sqlQuery, get_TrxName());
                        pstmt.setTimestamp(1, fromDate);
                        pstmt.setTimestamp(2, toDate);     
                       
                        ResultSet rs = pstmt.executeQuery();
                        String drug;
                        while (rs.next() )
			{
                        
                            sqlInsert="insert into T_LIST_OUTSIDE_SALE values(?,?,'y',?,?,?,?,?,?,?,?)";
                            pstmtInsert=DB.prepareStatement(sqlInsert, get_TrxName());    
                            pstmtInsert.setLong(1,rs.getLong(1));
                            pstmtInsert.setLong(2,rs.getLong(2)); 
                            
                               
                            pstmtInsert.setString(3,(rs.getString(5)!=null) ? rs.getString(5):" ");
                            getDrug(rs.getLong(6));
                            drug=prodDrug;
                            pstmtInsert.setString(4,drug);      
                            pstmtInsert.setString(5,(rs.getString(7)!=null) ? rs.getString(7):" ");
                            
                            pstmtInsert.setString(6,(rs.getString(8)!=null) ? rs.getString(8):" ");  
                            pstmtInsert.setDouble(7,rs.getLong(9));      
                            pstmtInsert.setDouble(8,rs.getLong(10));    
                            pstmtInsert.setLong(9,p_PInstance_ID); 
                            pstmtInsert.setDate(10, new Date(fromDate.getTime()+1000));
                             
                            pstmtInsert.executeQuery();
                            pstmtInsert.close();                            
                                                       
                        }
                        DB.commit(true, get_TrxName());  
                        
               }
                   catch (Exception exception) {
                       log.info("Se produsco un error en org.compiere.process.GenerateOutsideSales.doIt "+exception.getMessage());
                       
                       exception.printStackTrace();
            }                            
            UtilProcess.initViewer("Informe de ventas al exterior",p_PInstance_ID,this.getProcessInfo()); 
            return "success";
	}

    private void getDrug(long m_prod_id) {
       Long l= new Long(m_prod_id);
       ResultSet rs,rs1;
       Long cat;
       Long catT = new Long (1000059);
       String sqlQuery="select M_Product.M_PRODUCT_CATEGORY_ID,M_Product.NAME " +
                "from M_Product " +
                "where M_Product_id= ?"  ;
                
        PreparedStatement pstmt,pstmt1;
        pstmt=DB.prepareStatement(sqlQuery, get_TrxName());
        try {
        
            pstmt.setLong(1, m_prod_id);
            rs = pstmt.executeQuery();
            if (rs.next() ){
                cat=rs.getLong(1);
                if ( cat.equals(catT)  ){
                    //return rs.getString(2);
                    //String.valueOf(m_product_id)),rs.getString(2)
                    //prod.put(l,rs.getString(2));
                    prodDrug=rs.getString(2);
                    //return;
                }else{
                    sqlQuery="select * from MPC_Product_BOMline " +
                        "join M_PRODUCT using (M_PRODUCT_ID) " +
                        "join MPC_Product_BOM using (MPC_Product_BOM_ID) " +
                        "join MPC_Product_Planning using (MPC_Product_BOM_id) " +
                        "where (M_Product_ID = ? )";
                    pstmt1=DB.prepareStatement(sqlQuery, get_TrxName());
                    pstmt1.setLong(1, m_prod_id);
                    rs1 = pstmt1.executeQuery();
                    while (rs1.next() ){
                        getDrug(rs1.getLong(2));
                
                  }
                }
                
                
               }
            }
        catch (Exception exception) {
           log.info("Se produsco un error en org.compiere.process.GenerateSalesForRegion.doIt "+exception.getMessage());

           exception.printStackTrace();
        
        
        } 
         
        
        
        
    }

                   
             
               
    }

   
    
    
    
    
    
    

