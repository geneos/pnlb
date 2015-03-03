/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
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
 *  Esta clase inserta tuplas en la tabla temporal T_LIST_SALE luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
 *	@author BISion
 *	@version 1.0
 */
public class GenerateListSales extends SvrProcess {
        
        private int     p_PInstance_ID;
        
        private Timestamp fromDate;
        private Timestamp toDate;
        

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
               
                sqlRemove="delete from T_List_Sale";
                DB.executeUpdate(sqlRemove,null);
                generateFacturacion();
                generateBonificacion();
                generateDevolucion();
                generateFacturacionAhorro();
                generateBonificacionAhorro();
                DB.commit(true, get_TrxName());  
              
                UtilProcess.initViewer("Informe de ventas por período",p_PInstance_ID,this.getProcessInfo());
                return "success";

	}	

    
        

    private void generateFacturacion() {
     String sqlFacturacion;
     sqlFacturacion=getStringFacturacion();
     this.executeQuery(sqlFacturacion,"1_Facturacion");
    }
    
    public void generateBonificacion(){
                 
     String sqlBonificacion;
     sqlBonificacion=getStringBonificacion();
     this.executeQuery(sqlBonificacion,"2_Bonificacion");
             
    } 
    private void generateDevolucion() {
        
     String sqlDevolucion;
     sqlDevolucion=getStringDevolucion();
     this.executeQuery(sqlDevolucion,"3_Devoluvcion");
       
    }

    private void generateFacturacionAhorro() {
     String sqlFacturacionAhorro;
     sqlFacturacionAhorro=getStringFacturacionAhorro();
     this.executeQuery(sqlFacturacionAhorro,"4_Facturacion_Ahorro");
       
    }
    
    private void generateBonificacionAhorro() {
     String sqlBonificacionAhorro;
     sqlBonificacionAhorro=getStringBonificacionAhorro();
     this.executeQuery(sqlBonificacionAhorro,"5_BonificacionAhorro");
    }
    
    private String getStringBonificacionAhorro() {
      String q= "select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,M_Product_id,CODE_SP,CODE_FAMILY,CODE_MARK,DESCRIPTION,CODE_PROD,sum(Units),sum (NetValue) " +
                "from ( select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,RV_LIST_SALES.c_doctype_id,RV_LIST_SALES.M_Product_ID,RV_LIST_SALES.CODE_SP,RV_LIST_SALES.CODE_FAMILY,RV_LIST_SALES.CODE_MARK,RV_LIST_SALES.DESCRIPTION,RV_LIST_SALES.CODE_PROD, " +
                "case RV_LIST_SALES.C_DOCTYPE_ID " +
                "when 1000171 then sum(UNITS)  " +
                "when 1000203 then sum(UNITS) " +
                "when 1000172 then sum(UNITS) " +
                "when 1000207 then sum(UNITS) " +
                "end UNITS, " +
                "case RV_LIST_SALES.C_DOCTYPE_ID " +
                "when 1000171 then sum(NETVALUE) " +
                "when 1000203 then sum(NETVALUE) " +
                "when 1000172 then sum(NETVALUE) " +
                "when 1000207 then sum(NETVALUE) " +
                "end NETVALUE " +
                "from RV_LIST_SALES " +
                "where (RV_LIST_SALES.C_DOCTYPE_ID = 1000172 or RV_LIST_SALES.C_DOCTYPE_ID = 1000207 OR RV_LIST_SALES.C_DOCTYPE_ID = 1000203 or RV_LIST_SALES.C_DOCTYPE_ID = 1000171 ) and (RV_LIST_SALES.C_INVOICE_DATE between ? and ? ) " +
                "and (RV_LIST_SALES.CODE_CL=500725 and (RV_LIST_SALES.C_BPARTNER_ID = 1005252 OR RV_LIST_SALES.C_BPARTNER_ID = 1005259 OR RV_LIST_SALES.C_BPARTNER_ID = 1005274 OR RV_LIST_SALES.C_BPARTNER_ID = 1005283 " +
                "OR RV_LIST_SALES.C_BPARTNER_ID = 1005284 OR RV_LIST_SALES.C_BPARTNER_ID = 1005254 OR RV_LIST_SALES.C_BPARTNER_ID = 1005298 " +
                "OR RV_LIST_SALES.C_BPARTNER_ID = 1005313 OR RV_LIST_SALES.C_BPARTNER_ID = 1005326 OR RV_LIST_SALES.C_BPARTNER_ID = 1005216 OR " +
                "RV_LIST_SALES.C_BPARTNER_ID = 1005328 OR RV_LIST_SALES.C_BPARTNER_ID = 1005258 OR RV_LIST_SALES.C_BPARTNER_ID = 1005335 OR " +
                "RV_LIST_SALES.C_BPARTNER_ID = 1005350 OR RV_LIST_SALES.C_BPARTNER_ID = 1005351 OR RV_LIST_SALES.C_BPARTNER_ID = 1005366 OR " +
                "RV_LIST_SALES.C_BPARTNER_ID = 1005367 OR RV_LIST_SALES.C_BPARTNER_ID = 1005257 OR RV_LIST_SALES.C_BPARTNER_ID = 1005255 OR " +
                "RV_LIST_SALES.C_BPARTNER_ID = 1005256 OR RV_LIST_SALES.C_BPARTNER_ID = 1005253 OR RV_LIST_SALES.C_BPARTNER_ID = 1005270 OR " +
                "RV_LIST_SALES.C_BPARTNER_ID = 1005273))  " +
                "group by AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,c_doctype_id,M_Product_ID,RV_LIST_SALES.CODE_SP,RV_LIST_SALES.CODE_FAMILY,RV_LIST_SALES.CODE_MARK,RV_LIST_SALES.DESCRIPTION,RV_LIST_SALES.CODE_PROD) " +
                "group by AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,M_Product_id,CODE_SP,CODE_FAMILY,CODE_MARK,DESCRIPTION,CODE_PROD  " +
                "order by sum(Units) asc, sum (NetValue) asc " ;
                return q;
    }

    private String getStringFacturacionAhorro() {
      String q="select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,M_Product_id,CODE_SP,CODE_FAMILY,CODE_MARK,DESCRIPTION,CODE_PROD,sum(Units),sum (NetValue) " +
              "from ( " +
              "select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,RV_LIST_SALES.c_doctype_id,RV_LIST_SALES.M_Product_ID,RV_LIST_SALES.CODE_SP,RV_LIST_SALES.CODE_FAMILY,RV_LIST_SALES.CODE_MARK,RV_LIST_SALES.DESCRIPTION,RV_LIST_SALES.CODE_PROD, " +
              "case RV_LIST_SALES.C_DOCTYPE_ID  " +
              "when 1000131 then sum(UNITS)   " +
              "when 1000204 then sum(UNITS)  " +
              "when 1000175 then sum(UNITS)  " +
              "when 1000197 then sum(UNITS)  " +
              "when 1000205 then sum(UNITS)  " +
              "when 1000170 then sum(UNITS)  " +
              
              
              "when 1000133 then -sum(UNITS)  " +
              "when 1000206 then -sum(UNITS) " +
              "when 1000203 then -sum(UNITS)  " +
              " when 1000171 then -sum(UNITS) " +
              "when 1000178 then -sum(UNITS)  " +
              "when 1000198 then -sum(UNITS)  " +
              "when 1000172 then -sum(UNITS)  " +
              "when 1000207 then -sum(UNITS)  " +
              "end UNITS," +
              "case RV_LIST_SALES.C_DOCTYPE_ID  " +
              "when 1000131 then sum(NETVALUE)   " +
              "when 1000204 then sum(NETVALUE)  " +
              " when 1000175 then sum(NETVALUE) " +
              "when 1000197 then sum(NETVALUE)  " +
              "when 1000205 then sum(NETVALUE) " +
              "when 1000170 then sum(NETVALUE)  " +
  
              "when 1000133 then -sum(NETVALUE)  " +
              "when 1000206 then -sum(NETVALUE)  " +
              "when 1000203 then -sum(NETVALUE)  " +
              "when 1000171 then -sum(NETVALUE)  " +
              "when 1000178 then -sum(NETVALUE)  " +
              "when 1000198 then -sum(NETVALUE)  " +
              "when 1000172 then -sum(NETVALUE)  " +
              "when 1000207 then -sum(NETVALUE)  " +
              "end NETVALUE " +
              "from RV_LIST_SALES  " +
              "where (RV_LIST_SALES.C_DOCTYPE_ID = 1000131 or RV_LIST_SALES.C_DOCTYPE_ID = 1000204 " +
              "or RV_LIST_SALES.C_DOCTYPE_ID = 1000175  or RV_LIST_SALES.C_DOCTYPE_ID = 1000205 or RV_LIST_SALES.C_DOCTYPE_ID = 1000170" +
              "or RV_LIST_SALES.C_DOCTYPE_ID = 1000197  or RV_LIST_SALES.C_DOCTYPE_ID = 1000133 or RV_LIST_SALES.C_DOCTYPE_ID = 1000206 " +
              "or RV_LIST_SALES.C_DOCTYPE_ID = 1000203 or RV_LIST_SALES.C_DOCTYPE_ID = 1000171  or RV_LIST_SALES.C_DOCTYPE_ID = 1000178 " +
              "or RV_LIST_SALES.C_DOCTYPE_ID = 1000198 or RV_LIST_SALES.C_DOCTYPE_ID = 1000172 or RV_LIST_SALES.C_DOCTYPE_ID = 1000207)" +
              "and (RV_LIST_SALES.C_INVOICE_DATE between ? and ? ) " +
              "and (RV_LIST_SALES.CODE_CL=500725  and  " +
              "(RV_LIST_SALES.C_BPARTNER_ID = 1005252 OR RV_LIST_SALES.C_BPARTNER_ID = 1005259 OR RV_LIST_SALES.C_BPARTNER_ID = 1005274 OR RV_LIST_SALES.C_BPARTNER_ID = 1005283  " +
              "OR RV_LIST_SALES.C_BPARTNER_ID = 1005284 OR RV_LIST_SALES.C_BPARTNER_ID = 1005254 OR RV_LIST_SALES.C_BPARTNER_ID = 1005298 " +
              "OR RV_LIST_SALES.C_BPARTNER_ID = 1005313 OR RV_LIST_SALES.C_BPARTNER_ID = 1005326 OR RV_LIST_SALES.C_BPARTNER_ID = 1005216 OR " +
              "RV_LIST_SALES.C_BPARTNER_ID = 1005328 OR RV_LIST_SALES.C_BPARTNER_ID = 1005258 OR RV_LIST_SALES.C_BPARTNER_ID = 1005335 OR " +
              "RV_LIST_SALES.C_BPARTNER_ID = 1005350 OR RV_LIST_SALES.C_BPARTNER_ID = 1005351 OR RV_LIST_SALES.C_BPARTNER_ID = 1005366 OR " +
              "RV_LIST_SALES.C_BPARTNER_ID = 1005367 OR RV_LIST_SALES.C_BPARTNER_ID = 1005257 OR RV_LIST_SALES.C_BPARTNER_ID = 1005255 OR " +
              "RV_LIST_SALES.C_BPARTNER_ID = 1005256 OR RV_LIST_SALES.C_BPARTNER_ID = 1005253 OR RV_LIST_SALES.C_BPARTNER_ID = 1005270 OR " +
              "RV_LIST_SALES.C_BPARTNER_ID = 1005273 ) ) " +
              "group by AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,c_doctype_id,M_Product_ID,RV_LIST_SALES.CODE_SP,RV_LIST_SALES.CODE_FAMILY,RV_LIST_SALES.CODE_MARK,RV_LIST_SALES.DESCRIPTION,RV_LIST_SALES.CODE_PROD) " +
              "group by AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,M_Product_id,CODE_SP,CODE_FAMILY,CODE_MARK,DESCRIPTION,CODE_PROD " +
              "order by sum(Units) asc, sum (NetValue) asc";
        return q;        
    }
    private String getStringDevolucion(){
        String q= "select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,M_Product_id,CODE_SP,CODE_FAMILY,CODE_MARK,DESCRIPTION,CODE_PROD,sum(Units),sum (NetValue) " +
                "from( select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,RV_LIST_SALES.c_doctype_id,RV_LIST_SALES.M_Product_ID,RV_LIST_SALES.CODE_SP,RV_LIST_SALES.CODE_FAMILY,RV_LIST_SALES.CODE_MARK,RV_LIST_SALES.DESCRIPTION,RV_LIST_SALES.CODE_PROD, " +
                "case RV_LIST_SALES.C_DOCTYPE_ID " +
                "when 1000208 then sum(UNITS) " +
                "when 1000173 then sum(UNITS) " +
                "end UNITS, " +
                "case RV_LIST_SALES.C_DOCTYPE_ID " +
                "when 1000208 then sum(NETVALUE) " +
                "when 1000173 then sum(NETVALUE) " +
                "end NETVALUE " +
                "from RV_LIST_SALES " +
                "where (RV_LIST_SALES.C_DOCTYPE_ID = 1000208 or RV_LIST_SALES.C_DOCTYPE_ID = 1000173)  " +
                "and (RV_LIST_SALES.C_INVOICE_DATE between ? and ? ) " +
                "group by AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,c_doctype_id,M_Product_ID,RV_LIST_SALES.CODE_SP,RV_LIST_SALES.CODE_FAMILY,RV_LIST_SALES.CODE_MARK,RV_LIST_SALES.DESCRIPTION,RV_LIST_SALES.CODE_PROD) " +
                "group by AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,M_Product_id,CODE_SP,CODE_FAMILY,CODE_MARK,DESCRIPTION,CODE_PROD "+      
                "order by sum(Units)asc ,sum (NetValue) asc ";
        return q;
    
    }
    
    
    
    private String getStringBonificacion() {
      String q="select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,M_Product_id,CODE_SP,CODE_FAMILY,CODE_MARK,DESCRIPTION,CODE_PROD,sum(Units),sum(NetValue) " +
              "from ( " +
                  "select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,RV_LIST_SALES.c_doctype_id,RV_LIST_SALES.M_Product_ID,RV_LIST_SALES.CODE_SP,RV_LIST_SALES.CODE_FAMILY,RV_LIST_SALES.CODE_MARK,RV_LIST_SALES.DESCRIPTION,RV_LIST_SALES.CODE_PROD, " +
                  "case RV_LIST_SALES.C_DOCTYPE_ID " +
                      "when 1000171 then sum(UNITS) " +
                      "when 1000203 then sum(UNITS) " +
                      "when 1000172 then sum(UNITS) " +
                      "when 1000207 then sum(UNITS) " +
                      "end UNITS, " +
                  "case RV_LIST_SALES.C_DOCTYPE_ID " +
                      "when 1000171 then sum(NETVALUE) " +
                      "when 1000203 then sum(NETVALUE) " +
                      "when 1000172 then sum(NETVALUE) " +
                      "when 1000207 then sum(NETVALUE) " +
                      "end NETVALUE " +
                  "from RV_LIST_SALES " +
                  "where (RV_LIST_SALES.C_DOCTYPE_ID = 1000171 or RV_LIST_SALES.C_DOCTYPE_ID = 1000172 " +
                  "or RV_LIST_SALES.C_DOCTYPE_ID = 1000203 or RV_LIST_SALES.C_DOCTYPE_ID = 1000207) " +
                  " and (RV_LIST_SALES.C_INVOICE_DATE between ? and ? ) " +
                  "group by AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,c_doctype_id,M_Product_ID,RV_LIST_SALES.CODE_SP,RV_LIST_SALES.CODE_FAMILY,RV_LIST_SALES.CODE_MARK,RV_LIST_SALES.DESCRIPTION,RV_LIST_SALES.CODE_PROD) " +
               "group by AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,M_Product_id,CODE_SP,CODE_FAMILY,CODE_MARK,DESCRIPTION,CODE_PROD "+
               "order by sum(Units)asc ,sum (NetValue) asc  " ;
      return q;
    
    }
    
    
    
    private void executeQuery(String sql,String label){
           
                
     PreparedStatement pstmt = null;
     ResultSet rs; 
     
		
     try {

            pstmt=DB.prepareStatement(sql, get_TrxName());
            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate);     

            rs = pstmt.executeQuery();
            insertar(rs,label);

        }catch (Exception exception) {
               log.info("Se produsco un error en org.compiere.process.GenerateRankingSales.doIt "+exception.getMessage());
               exception.printStackTrace();
        } 
    }
    
    
    
   

    private String getStringFacturacion() {
      String q="select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,M_Product_id,CODE_SP,CODE_FAMILY,CODE_MARK,DESCRIPTION,CODE_PROD,sum(Units),sum (NetValue) " +
              "from( select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,RV_LIST_FACT_SALES.c_doctype_id,RV_LIST_FACT_SALES.M_Product_ID,RV_LIST_FACT_SALES.CODE_SP,RV_LIST_FACT_SALES.CODE_FAMILY,RV_LIST_FACT_SALES.CODE_MARK,RV_LIST_FACT_SALES.DESCRIPTION,RV_LIST_FACT_SALES.CODE_PROD, " +
              "case RV_LIST_FACT_SALES.C_DOCTYPE_ID  " +
              "when 1000131 then sum(UNITS) " +
              "when 1000204 then sum(UNITS) " +
              "when 1000175 then sum(UNITS)  " +
              "when 1000197 then sum(UNITS)  " +
              "when 1000205 then sum(UNITS)  " +
              "when 1000170 then sum(UNITS)  " +
              "when 1000133 then -sum(UNITS) " +
              "when 1000206 then -sum(UNITS)  " +
              "when 1000203 then -sum(UNITS)  " +
              "when 1000171 then -sum(UNITS)  " +
              "when 1000178 then -sum(UNITS)  " +
              "when 1000198 then -sum(UNITS)  " +
              "when 1000172 then -sum(UNITS)  " +
              "when 1000207 then -sum(UNITS) " +
              "when 1000174 then  sum(UNITS) " +
              "when 1000177 then  sum(UNITS) " +
              "when 1000176 then  -sum(UNITS) " +
              "end UNITS, " +
              "case RV_LIST_FACT_SALES.C_DOCTYPE_ID " +
              "when 1000131 then sum(NETVALUE)  " +
              "when 1000204 then sum(NETVALUE)  " +
              "when 1000175 then sum(NETVALUE)  " +
              "when 1000197 then sum(NETVALUE)  " +
              "when 1000205 then sum(NETVALUE)  " +
              "when 1000170 then sum(NETVALUE)  " +
              "when 1000133 then -sum(NETVALUE) " +
              "when 1000206 then -sum(NETVALUE) " +
              "when 1000203 then -sum(NETVALUE) " +
              "when 1000171 then -sum(NETVALUE) " +
              "when 1000178 then -sum(NETVALUE) " +
              "when 1000198 then -sum(NETVALUE) " +
              "when 1000172 then -sum(NETVALUE) " +
              "when 1000207 then -sum(NETVALUE) " +
              "when 1000174 then  sum(NETVALUE) " +
              "when 1000177 then  sum(NETVALUE) " +
              "when 1000176 then  -sum(NETVALUE) " +
              "end NETVALUE " +
              "from RV_LIST_FACT_SALES " +
              "where (RV_LIST_FACT_SALES.C_DOCTYPE_ID = 1000131 or RV_LIST_FACT_SALES.C_DOCTYPE_ID = 1000204 or RV_LIST_FACT_SALES.C_DOCTYPE_ID =1000175 " +
              "or RV_LIST_FACT_SALES.C_DOCTYPE_ID =1000197 or RV_LIST_FACT_SALES.C_DOCTYPE_ID =1000205  or RV_LIST_FACT_SALES.C_DOCTYPE_ID = 1000170  " +
              "or RV_LIST_FACT_SALES.C_DOCTYPE_ID =1000133  or RV_LIST_FACT_SALES.C_DOCTYPE_ID = 1000206 " +
              "or RV_LIST_FACT_SALES.C_DOCTYPE_ID = 1000203 or RV_LIST_FACT_SALES.C_DOCTYPE_ID = 1000171 " +
              "or RV_LIST_FACT_SALES.C_DOCTYPE_ID =1000178  or RV_LIST_FACT_SALES.C_DOCTYPE_ID =1000198  " +
              "or RV_LIST_FACT_SALES.C_DOCTYPE_ID =1000172  or RV_LIST_FACT_SALES.C_DOCTYPE_ID =1000207  " +
              "or RV_LIST_FACT_SALES.C_DOCTYPE_ID =1000174  or RV_LIST_FACT_SALES.C_DOCTYPE_ID =1000176 or RV_LIST_FACT_SALES.C_DOCTYPE_ID = 1000177)" +
              "and (RV_LIST_FACT_SALES.C_INVOICE_DATE between ? and ? ) " +
              "group by AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,c_doctype_id,M_Product_ID,RV_LIST_FACT_SALES.CODE_SP,RV_LIST_FACT_SALES.CODE_FAMILY,RV_LIST_FACT_SALES.CODE_MARK,RV_LIST_FACT_SALES.DESCRIPTION,RV_LIST_FACT_SALES.CODE_PROD) " +
              "group by AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,M_Product_id,CODE_SP,CODE_FAMILY,CODE_MARK,DESCRIPTION,CODE_PROD " +
              "order by sum(Units) asc ,sum (NetValue) asc";
      
      return q;
    }

    
    

    private void insertar(ResultSet resultSet, String label) {
      String sqlInsert;
       
     PreparedStatement pstmtInsert = null;   
     
	try {
	
        
            while (resultSet.next())
                 {
                    sqlInsert="insert into T_LIST_SALE values(?,?,'y',?,?,?,?,?,?,?,?,?,?)";
                    pstmtInsert=DB.prepareStatement(sqlInsert, get_TrxName());    
                    pstmtInsert.setLong(1,resultSet.getLong(1));
                    pstmtInsert.setLong(2,resultSet.getLong(2));
                    pstmtInsert.setString(3,label);

                    pstmtInsert.setString(4, (resultSet.getString(5)!=null) ? resultSet.getString(5):" "); 
                    pstmtInsert.setString(5, (resultSet.getString(6)!=null) ? resultSet.getString(6):" "); 
                    pstmtInsert.setString(6, (resultSet.getString(7)!=null) ? resultSet.getString(7):" "); 
                    pstmtInsert.setString(7,(resultSet.getString(8)!=null) ?  resultSet.getString(8):" ");  
                    pstmtInsert.setString(8, (resultSet.getString(9)!=null) ? resultSet.getString(9):" "); 

                    pstmtInsert.setLong(9, resultSet.getLong(10));
                    pstmtInsert.setDouble(10, resultSet.getDouble(11));

                    pstmtInsert.setLong(11, p_PInstance_ID);
                    pstmtInsert.setDate(12, new Date(fromDate.getTime()+1000));
                    pstmtInsert.executeQuery();



            }
           }
           catch (Exception exception) {
               log.info("Se produsco un error en org.compiere.process.GenerateRankingSales.doIt "+exception.getMessage());
               exception.printStackTrace();
           }                            
             
               
    }

   
    
    
    
    
    
    
}
