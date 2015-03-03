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
import java.util.logging.*;
import org.compiere.model.*;

import org.compiere.print.MPrintFormat;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;
import org.compiere.util.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;


/**
 *  Esta clase inserta tuplas en la tabla temporal T_LIST_SALES_REGION luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
 *	@author BISion
 *	@version 1.0
 */
public class GeneratedListSalesRegion extends SvrProcess{

    private int     p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;    
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++)
	{
            String name = para[i].getParameterName();
            fromDate=(Timestamp)para[i].getParameter();
            toDate=(Timestamp)para[i].getParameter_To();
            p_PInstance_ID = getAD_PInstance_ID();
	}
    }

    
    protected String doIt() throws Exception {
         // Definición de variables para crear los script de insert, consulta y borrado
        String sqlRemove;
        StringBuffer sqlQuery=new StringBuffer();
        String sqlInsert;

        List queries=new ArrayList();
        
        log.info("Comienzo del proceso de generación de listado por periodo de las unidades y montos de productos");
        log.info("borrado de la tabla temporal T_LIST_SALES_REGION");
        sqlRemove="delete from T_LIST_SALES_REGION";
        DB.executeUpdate(sqlRemove,null);
        
        log.info("Consulta de la vista RV_LIST_SALES_REGION, se filtra por el rango indicado y se agrupa por productos");
                sqlQuery.append("select ad_client_id,ad_org_id,isactive,m_product_description,product_code,mark_code,salesregion,especiality,sum(units),sum(netvalue) from RV_LIST_SALES_REGION" +
                                " where  dateinvoiced between ? and ? group by (ad_client_id,ad_org_id,isactive,m_product_description,product_code,mark_code,salesregion,especiality)");

       // Realice la consulta y guardo los resultados en la tabla
        
         PreparedStatement pstmt = null;
         PreparedStatement pstmtInsert = null;   
         try {
            pstmt=DB.prepareStatement(sqlQuery.toString(), get_TrxName());
            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate);     
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                try{
                sqlInsert="insert into T_LIST_SALES_REGION values(?,?,?,?,?,?,?,?,?,?,?,?)";
                pstmtInsert=DB.prepareStatement(sqlInsert, get_TrxName());    
                pstmtInsert.setLong(1,rs.getLong(1));
                pstmtInsert.setLong(2,rs.getLong(2));                        
                pstmtInsert.setString(3,rs.getString(3));                                                
                pstmtInsert.setLong(4,p_PInstance_ID);                                                
                pstmtInsert.setString(5,(rs.getString(6)!=null) ? rs.getString(6):" ");                                                                        
                pstmtInsert.setString(6,(rs.getString(4)!=null) ? rs.getString(4):" ");                                                                                                
                pstmtInsert.setString(7,(rs.getString(5)!=null) ? rs.getString(5):" ");          
                pstmtInsert.setString(8,(rs.getString(8)!=null) ? rs.getString(8):" ");
                pstmtInsert.setString(9,(rs.getString(7)!=null) ? rs.getString(7):" ");
                pstmtInsert.setDate(10, new Date(fromDate.getTime()+1000));
                pstmtInsert.setLong(11,rs.getLong(9));
                pstmtInsert.setDouble(12,rs.getDouble(10));               
                pstmtInsert.executeQuery();
                 }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        log.info("Se produsco un error en org.compiere.process.GenerateListSalesRegion.doIt "+exception.getMessage());
                 } 
            }
            DB.commit(true, get_TrxName());      
         }
         catch (Exception exception) {
            log.info("Se produsco un error en org.compiere.process.GenerateListSalesRegion.doIt "+exception.getMessage());
             exception.printStackTrace();
         } 
        UtilProcess.initViewer("Informe de ventas zona por zona",p_PInstance_ID,this.getProcessInfo());
        return "success";
    }

}
