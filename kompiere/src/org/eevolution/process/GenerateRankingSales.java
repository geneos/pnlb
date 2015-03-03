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

import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;
import org.compiere.util.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;


/**
 *  Esta clase inserta tuplas en la tabla temporal T_RANKING_SALE luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
 *	@author BISion
 *	@version 1.0
 */
public class GenerateRankingSales extends SvrProcess
{
	
	private int     p_PInstance_ID;
        
        private Timestamp fromDate;
        private Timestamp toDate;
        private Long client=new Long(-1);
        private Long org=new Long(-1);
        

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
                // Definición de variables para crear los script de insert, consulta y borrado
                String sqlRemove;
                StringBuffer sqlQuery=new StringBuffer();
                String sqlInsert;

                
                List queries=new ArrayList();
            
                log.info("Comienzo del proceso de generación de ranking de ventas");
       
                log.info("borrado de la tabla temporal T_RANKING_SALE");
                sqlRemove="delete from T_Ranking_Sale";
                DB.executeUpdate(sqlRemove,null);
                
 
                log.info("Consulta de la vista RV_RANKING_SALE, se filtra por el rango indicado y se agrupa por clientes");
                sqlQuery.append("select ad_client_id,ad_org_id,c_bpartner_id, C_BPARTNER_NAME, sum(units) as unidades, sum(netvalue) from rv_ranking_sales" +
                                " where  c_invoice_date between ? and ? group by c_bpartner_id, C_BPARTNER_NAME,ad_client_id,ad_org_id order by unidades desc");
                
                
                /* Una vez realizada la consulta almaceno los valores en una lista de mapas con los valores intermedios 
                 y calculo los totales, estos totales son necesarios para calcular los promedios pedidos */
                
                          
                 PreparedStatement pstmt = null;
                 PreparedStatement pstmtInsert = null;   
                 long sumQtyInvoiced=0;
                 double sumNetAmt=0;
                 double sumAverageQtyInvoiced=0;
                 double sumAverageNetAmt=0;
                 double sumAverageNetAmtQtyInvoided=0;

                 long sumPartialQtyInvoiced=0;
                 double sumPartialNetAmt=0;


		 try {
                        pstmt=DB.prepareStatement(sqlQuery.toString(), get_TrxName());
                        pstmt.setTimestamp(1, fromDate);
                        pstmt.setTimestamp(2, toDate);     
                        
                        //Primero calculo los totales 
                                         
     
			ResultSet rs = pstmt.executeQuery();
                       
                        while (rs.next() )
			{
                            HashMap values=new HashMap();
                            values.put("ad_client_id",rs.getLong(1));
                            values.put("ad_org_id",rs.getLong(2));                            
                            values.put("c_bpartner_id",rs.getLong(3));                                                        
                            values.put("bpartner_name",rs.getString(4));                                                        
                            values.put("qtyInvoiced",rs.getLong(5));                                                                                    
                            values.put("netAmt",rs.getDouble(6)); 
                            sumQtyInvoiced=sumQtyInvoiced+rs.getLong(5);
                            sumNetAmt=sumNetAmt+rs.getLong(6);    
                            queries.add(values);
                        }
                  
                   }
                   catch (Exception exception) {
                       log.info("Se produsco un error en org.compiere.process.GenerateRankingSales.doIt "+exception.getMessage());
                       exception.printStackTrace();
                   }                            
                               
                 
                 /* Recorro la lista y realizo las inserciones con los calculos, el resultado ya esta filtrado por fecha, ingreso una fecha
                   por el comportamiento del framework COMPIERE, me aseguro que esta fecha no sera filtrada.*/
                   int register=1;
                  for (Iterator it=queries.iterator();it.hasNext();)
                  { 
                        try{
                               HashMap values=(HashMap) it.next();
                            if (register==1)
                            {
                                   client=(Long)values.get("ad_client_id");
                                   org=(Long)values.get("ad_org_id");
                            }   
                            

                            sqlInsert="insert into T_RANKING_SALE values(?,?,'y',?,?,?,?,?,?,?,?,?)";
                            pstmtInsert=DB.prepareStatement(sqlInsert, get_TrxName());    
                            pstmtInsert.setLong(1,(Long)values.get("ad_client_id"));
                            pstmtInsert.setLong(2,(Long)values.get("ad_org_id"));                        
                            pstmtInsert.setString(3,(String)values.get("bpartner_name"));                                                
                            pstmtInsert.setLong(4,(Long)values.get("qtyInvoiced"));                                                
                            pstmtInsert.setDouble(5,(Double)values.get("netAmt"));                                                                        
                            
                            Double averageNetAmt=new Double(0);
                            if (sumNetAmt!=0)
                            {    
                                averageNetAmt=new Double(((Double)values.get("netAmt")).doubleValue()/sumNetAmt);
                                pstmtInsert.setDouble(6,averageNetAmt*100);                                                                                                
                            }    
                            
                            Double averageQtyInvoiced=new Double(0);
                            if (sumQtyInvoiced!=0){
                                averageQtyInvoiced=new Double(((Long)values.get("qtyInvoiced")).doubleValue()/sumQtyInvoiced);
                                pstmtInsert.setDouble(7,averageQtyInvoiced*100);                                           
                            }    
                          
                            Double averageNetAmtQtyInvoided=new Double(0);
                            if ((Long)values.get("qtyInvoiced")!=0){
                                averageNetAmtQtyInvoided=(Double)values.get("netAmt")/(Long)values.get("qtyInvoiced");                            
                                pstmtInsert.setDouble(8,averageNetAmtQtyInvoided);                                                                                                                                                
                            }    
                       
                            pstmtInsert.setLong(9, p_PInstance_ID);                                                                                    
                            pstmtInsert.setDate(10, new Date(fromDate.getTime()+1000)); 
                            pstmtInsert.setLong(11, register);                                                                                    
                            
                            if (register <=20)
                            {
                               sumPartialQtyInvoiced=sumPartialQtyInvoiced+(Long)values.get("qtyInvoiced");
                               sumPartialNetAmt=sumPartialNetAmt+(Double)values.get("netAmt");
                               sumAverageNetAmt=sumAverageNetAmt+averageNetAmt;
                               sumAverageQtyInvoiced=sumAverageQtyInvoiced+averageQtyInvoiced;
                               sumAverageNetAmtQtyInvoided=sumAverageNetAmtQtyInvoided+averageNetAmtQtyInvoided; 
                               pstmtInsert.executeQuery();                                  
                               register++;
                           }    
                
                            
                                      
                            
                        }
                        catch (Exception exception) {
                               exception.printStackTrace();
                               log.info("Se produsco un error en org.compiere.process.GenerateRankingSales.doIt "+exception.getMessage());
                        }       
                        
                  }    
                DB.commit(true, get_TrxName());   

                log.info("Fin de Generación de tuplas para la tabla temporal T_RANKING_SALE");
                
                
               try{ 
                
                if (client!=-1 && org!=-1)
                {   
                    
                    // Cargo los datos en la tabla T_RANKING_SALE_FOOT QUE SERVIRAN COMO SUBTOTALES    
                    sqlInsert="insert into T_RANKING_SALE values(?,?,'y','',?,?,null,null,?,?,?,21)";
                    pstmtInsert=DB.prepareStatement(sqlInsert, get_TrxName()); 
                    pstmtInsert.setLong(1,client);
                    pstmtInsert.setLong(2,org);
                    pstmtInsert.setLong(3,sumPartialQtyInvoiced);
                    pstmtInsert.setDouble(4,sumPartialNetAmt);
                    pstmtInsert.setDouble(5,sumPartialNetAmt/sumPartialQtyInvoiced);
                    pstmtInsert.setLong(6, p_PInstance_ID);                                                                                    
                    pstmtInsert.setDate(7, new Date(fromDate.getTime()+1000));
                    pstmtInsert.executeQuery();
                    pstmtInsert.close();
                                        
                    pstmt=null;
                    
                    // Cargo los datos en la tabla T_RANKING_SALE_FOOT QUE SERVIRAN COMO TOTALES    
                    sqlInsert="insert into T_RANKING_SALE values(?,?,'y','Total',?,?,null,null,?,?,?,22)";
                    pstmt=DB.prepareStatement(sqlInsert, get_TrxName());                     
                    pstmt.setLong(1,client);
                    pstmt.setLong(2,org);
                    pstmt.setLong(3,sumQtyInvoiced);
                    pstmt.setDouble(4,sumNetAmt);
                    pstmt.setDouble(5,(sumNetAmt/sumQtyInvoiced));
                    pstmt.setLong(6, p_PInstance_ID);                                                                                    
                    pstmt.setDate(7, new Date(fromDate.getTime()+1000));
                    pstmt.executeQuery();
                    pstmt.close();
                   
                
               }
               }
              catch (Exception exception) {
                               exception.printStackTrace();
                               log.info("Se produsco un error en org.compiere.process.GenerateRankingSales.doIt "+exception.getMessage());
              }   
                
              DB.commit(true, get_TrxName());     
              UtilProcess.initViewer("Ranking de Ventas",p_PInstance_ID,this.getProcessInfo());
           
              
              
                return "success";
	}	//	doIt

}	//	CopyFromOrder
