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
package org.compiere.acct.manualProcess;

import org.compiere.process.*;
import java.awt.geom.*;
import java.math.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.acct.Doc;
import org.compiere.acct.Doc;

public class RepostAllocations {

    private static boolean omitPeriod = true;
    private static boolean omitRetentionsCalculate = true;
    
    int m_postCount = 0;
    int m_postCount_Err = 0;
    
    private static CLogger log = CLogger.getCLogger(RepostAllocations.class);

    private static String sqlEspecificos = "select C_AllocationHdr_ID from C_AllocationHdr   "
                                   + "WHERE C_AllocationHdr_ID in (5153150,5153153)" ;
        

    /**************************************************************************
     * 	Repost Payment Class
     *  
     */
    public static void main(String[] args) {
        
        String sql = sqlEspecificos;
        
        org.compiere.Compiere.startupEnvironment(true);
        CLogMgt.setLevel(Level.FINE);
        Properties ctx = Env.getCtx();
        ctx.setProperty("#AD_Client_ID", "1000002");
       
        log.info("Repost Allocations   $Revision: 1.0 $");
        log.info("----------------------------------");
           
        Trx trx = Trx.get(Trx.createTrxName("RepostPayments"), true);

        int count = 0;
        int countE = 0;
        int countS = 0;
        
        Env.setContext(Env.getCtx(), "OmitPeriod", omitPeriod ?  "Y" : "N");
        
        PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
        MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(ctx, Env.getAD_Client_ID(ctx));
            
         try {
             ResultSet rs = pstmt.executeQuery();

             while (rs.next()) {
                //MAllocationHdr allocation = new MAllocationHdr(getCtx(),rs,get_TrxName());
                log.info("[ Recontabilizando " + count + "] Allocation=" + rs.getInt(1));
                String ret = Doc.postImmediate(ass, 735, rs.getInt(1), true, null);
                if (ret != null) {
                    countE++;
                    log.log(Level.SEVERE, "Error: " + ret);
                    System.out.println("Error: " + ret);
                } else {
                    count++;
                }
            }
        } catch (Exception e) {
            log.severe("main - " + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            pstmt = null;
        }

        Env.setContext(Env.getCtx(), "OmitPeriod", 'N');
        trx.commit();
        log.info("Generated = " + count);
        log.info("Errors = " + countE);
        log.info("Skipped = " + countS);
    }	//	doIt
    
}	
