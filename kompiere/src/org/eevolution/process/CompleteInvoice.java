/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is             Compiere  ERP & CRM Smart Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
* created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/
package org.eevolution.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.*;
import java.math.BigDecimal;


import org.compiere.model.MInvoice;
import org.compiere.model.MQuery;
import org.compiere.model.PrintInfo;
import org.compiere.model.X_T_BOMLine;
import org.compiere.model.X_RV_MPC_Product_BOMLine;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;


/**
 *  BOM lines explosion for print
 *
 *        @author Sergio Ramazzina,Victor Perez
 *        @version $Id: PrintBOM.java,v 1.2 2005/04/19 12:54:30 srama Exp $
 */
public class CompleteInvoice extends SvrProcess {
	
	
    private static final Properties ctx = Env.getCtx();
    //private static final String AD_Client_ID = ctx.getProperty("#AD_Client_ID");
    //private static final String AD_Org_ID = ctx.getProperty("#AD_Org_ID");


    /**
     *  Prepare - e.g., get Parameters.
     */
    protected void prepare() 
    {
    	ProcessInfoParameter[] para = getParameter();
    	
    } //        prepare

    /**
     *  Perform process.
     *  @return Message (clear text)
     *  @throws Exception if not successful
     */
    protected String doIt() throws Exception {
        //info = getProcessInfo();
        //AD_PInstance_ID = getAD_PInstance_ID();
        String sql = "SELECT * FROM C_Invoice i INNER JOIN C_Invoice imp ON (i.DocumentNo=SUBSTR(imp.DocumentNo,3)) WHERE  i.AD_Client_ID = 1000001 AND i.IsSOTrx='N' AND i.DocStatus='CO'";              
		
		PreparedStatement pstmt = null;
		try
		{			
                        pstmt = DB.prepareStatement (sql);
                        ResultSet rs = pstmt.executeQuery ();
                        
                        while (rs.next())
                        {
                        	org.eevolution.model.MInvoice invoice = new org.eevolution.model.MInvoice(Env.getCtx(), rs, null);
                        	//invoice.completeIt();
                        	invoice.reverseCorrectIt();
                        	invoice.save(null);
                        }
                        rs.close();
                        pstmt.close();
		}
		catch (Exception e)
		{
			//log.log(Level.SEVERE,"doIt - " + sql, e);
			
			System.out.println("Compiere " + e);
		}

        return "@ProcessOK@";
    } //        doIt
        
}
