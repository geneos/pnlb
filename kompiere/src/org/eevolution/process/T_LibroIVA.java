/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Portions created by Carlos Ruiz are Copyright (C) 2005 QSS Ltda.
 * Add e-Evolution by Perez Juarez
 * Contributor(s): Carlos Ruiz (globalqss)
 *****************************************************************************/
package org.eevolution.process;


import java.sql.*;
import java.math.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.*;
import org.compiere.model.MStorage;
import java.util.*;
import org.compiere.util.*;
import org.compiere.process.*;
import org.compiere.model.*;
import org.eevolution.model.MMPCProductBOM;
import org.eevolution.model.MMPCProductBOMLine;
import org.eevolution.model.MMPCProductPlanning;
import org.eevolution.tools.UtilProcess;

/**
 * Title:	Inventory Valuation Temporary Table
 *
 *  @author Carlos Ruiz (globalqss)
 *  @version $Id: T_InventoryValue_Create.java,v 1.0 2005/09/21 20:29:00 globalqss Exp $
 */
public class T_LibroIVA extends SvrProcess
{

	/** The Parameters */
    
	private String p_fecha = "";
        private String p_fecha_to = "";
        private Long num_hoja;  
        private int p_M_Locator_ID = 0;
        private int p_Quantity = 0;
        
	/** The Record */
	private int		p_Record_ID = 0;

        /** The Instance */
	private int     p_PInstance_ID;

    
	/**
	 *  Prepare - e.g., get Parameters.
	 */

        protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
                    String name = para[i].getParameterName();
                       if(name.equals("PAGINA")){
                            num_hoja= ((BigDecimal) para[i].getParameter()).longValue();
                            Env.getCtx().put("typePrint", "LIBRO");
                            Env.getCtx().put("startPage", num_hoja);
                        }
			else if (name.equals("FECHA"))
                        {
                        	p_fecha = para[i].getInfo();
                                p_fecha_to = para[i].getInfo_To();
                        }
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_Record_ID = getRecord_ID();
		p_PInstance_ID = getAD_PInstance_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
                
                int cntu = 0;
		int cnti = 0;

                
                
                
		log.info("Inventory Product Valuation Temporary Table (T_ProductCheck)");
                
                log.info("Delete TMP_PIE_LIBRO_IVA_COMPRAS");
                String sqldel ="delete from TMP_PIE_LIBRO_IVA_COMPRAS";
                cntu = DB.executeUpdate(sqldel,null);
                String sqlQuery = "UPDATE AD_SEQUENCE SET CURRENTNEXT="+num_hoja+" WHERE AD_CLIENT_ID=0 and AD_ORG_ID=0 AND NAME='RV_IVA_COMPRAS_BASE'";
                PreparedStatement pstmtInsert=DB.prepareStatement(sqlQuery);                    
                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
                
                
                String  sqlins = "DECLARE " 
                              + "RetVal NUMBER;"
                              + "P_DESDE DATE;"
                              + "P_HASTA DATE;"
                              + "BEGIN "
                              + "RetVal := COMPIERE.CARGA_PIE_LIBRO_IVA_COMPRAS (P_DESDE=>'"+p_fecha+"',P_HASTA=>'"+ p_fecha_to+"');"

                               + "COMMIT; END;";
                System.out.println(sqlins);
                cntu = DB.executeUpdate(sqlins,null);
                
                
                
              
                  /*String sql="update TMP_PIE_LIBRO_IVA_COMPRAS set EXEN=NULL where EXEN=0;" +
                           "update TMP_PIE_LIBRO_IVA_COMPRAS set NG=NULL where NG=0;" +
                           "update TMP_PIE_LIBRO_IVA_COMPRAS set IVA=NULL where IVA=0;" +
                           "update TMP_PIE_LIBRO_IVA_COMPRAS set TOTAL=NULL where TOTAL=0;";
                   
                   System.out.println(sql);
                   DB.executeUpdate(sql,null);
                   DB.commit(true, get_TrxName()); */ 
                
              
                
                
                
                
                UtilProcess.initViewer("Libro de IVA Compras",this.p_PInstance_ID,this.getProcessInfo());
                return "success";
                	
	}	//	doIt


}	//	T_ProductCheck

