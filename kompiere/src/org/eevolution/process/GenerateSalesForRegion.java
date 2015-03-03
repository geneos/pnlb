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
public class GenerateSalesForRegion extends SvrProcess
{
	
	private int     p_PInstance_ID;
        
        private Timestamp fromDate;
        private Timestamp toDate;
        private Long client;
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
                        p_PInstance_ID = getAD_PInstance_ID();
                        client=new Long(this.getAD_Client_ID());
                        System.out.println("user ="+this.getAD_User_ID()); 
                        fromDate=(Timestamp)para[i].getParameter();
                        toDate=(Timestamp)para[i].getParameter_To();
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
                String sqlQuery;
                String sqlInsert;

                
                List queries=new ArrayList();
            
                log.info("Comienzo del proceso de generación de ventas por region ");
       
                log.info("borrado de la tabla temporal T_SALES_FOR_REGION");
                sqlRemove="delete from T_SALES_FOR_REGION";
                DB.executeUpdate(sqlRemove,null);
                
 
                log.info("Consulta de la vista RV_RANKING_SALE, se filtra por el rango indicado y se agrupa por clientes");
                sqlQuery="select   AD_CLIENT_ID,AD_ORG_ID ,C_BPARTNER_LOCATION_ID,C_BPARTNER_LOCATION , C_BPARTNER_GROUP_ID,"+
                        "C_BPARTNER_GROUP ,SUM(UNITS) ,  SUM(NETVALUE)  FROM rv_sales_for_region WHERE (C_INVOICE_DATE BETWEEN ? AND ?)"+
                        
                         "GROUP BY ( AD_CLIENT_ID,AD_ORG_ID , C_BPARTNER_LOCATION_ID, C_BPARTNER_LOCATION ,C_BPARTNER_GROUP_ID ,C_BPARTNER_GROUP )"+
                         "ORDER BY C_BPARTNER_LOCATION,C_BPARTNER_GROUP";
   
                 PreparedStatement pstmt = null;
                 PreparedStatement pstmtInsert = null;   

                

		 try {
                        System.out.println(sqlQuery);
                        pstmt=DB.prepareStatement(sqlQuery, get_TrxName());
                        pstmt.setTimestamp(1, fromDate);
                        pstmt.setTimestamp(2, toDate);     
                        //pstmt.setLong(3,client);
                        ResultSet rs = pstmt.executeQuery();
                       
                        while (rs.next() )
			{
                        
                            sqlInsert="insert into T_SALES_FOR_REGION values(?,?,'y',?,?,?,?,?,?,?,?)";
                            pstmtInsert=DB.prepareStatement(sqlInsert, get_TrxName());    
                            pstmtInsert.setLong(1,client);
                            pstmtInsert.setLong(2,rs.getLong(2));                        
                            pstmtInsert.setLong(3,p_PInstance_ID);      
                            pstmtInsert.setLong(4,rs.getLong(3));      
                            pstmtInsert.setString(5,rs.getString(4));      
                            pstmtInsert.setLong(6,rs.getLong(5));      
                            pstmtInsert.setString(7,rs.getString(6));  
                            pstmtInsert.setDouble(8,rs.getDouble(7));      
                            pstmtInsert.setDouble(9,rs.getDouble(8));    
                            pstmtInsert.setDate(10, new Date(fromDate.getTime()+1000));
                            pstmtInsert.executeQuery();
                            pstmtInsert.close();                            
                                                       
                        }
                            DB.commit(true, get_TrxName());  
                  
                   }
                 
                   catch (Exception exception) {
                       log.info("Se produsco un error en org.compiere.process.GenerateSalesForRegion.doIt "+exception.getMessage());
                       
                       exception.printStackTrace();
                   }                            
                        
                UtilProcess.initViewer("Informe de participación de ventas por zona",p_PInstance_ID,this.getProcessInfo()); 
                return "success";

	}	//	doIt

}	//	CopyFromOrder
