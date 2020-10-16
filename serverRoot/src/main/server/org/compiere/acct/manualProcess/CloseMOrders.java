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

/**
 * Se puede ejecutar de manera directa o sino tambien desde el proeso  system RepostPayments. 
 * Esta segunda opcion es para corridas mas grandes, para que no tarde tanto.
 * @author pablo
 */

public class CloseMOrders extends SvrProcess {

    private static boolean omitPeriod = true;
    
    int m_postCount = 0;
    int m_postCount_Err = 0;
    
    private static CLogger log = CLogger.getCLogger(CloseMOrders.class);
    private static String sqlOCsFacturadas2019 = "select * from c_order where c_order_id in ( SELECT distinct(o.c_order_id) from c_order o "
                                                                      + " join c_orderline ol on ol.c_order_id = o.c_order_id "
                                                                      + " where ol.QTYINVOICED <> 0 "
                                                                     // + " and ol.QTYDELIVERED/ol.QTYINVOICED <= 1.1 --%5 tolerancia
                                                                      + " and extract(YEAR from o.dateordered) = 2019"
                                                                      + " and o.docstatus = 'CO')";

    /**************************************************************************
     * 	Close MOrders Process
     *  
     */
    public static void main(String[] args) {
        
        String sql = sqlOCsFacturadas2019;
        
        org.compiere.Compiere.startupEnvironment(true);
        CLogMgt.setLevel(Level.FINE);
        Properties ctx = Env.getCtx();
        ctx.setProperty("#AD_Client_ID", "1000002");
       
        log.info("Repost Payments   $Revision: 1.0 $");
        log.info("----------------------------------");
           
        Trx trx = Trx.get(Trx.createTrxName("ManualCloseMOrders"), true);

        int count = 0;
        int countE = 0;
        
        Env.setContext(Env.getCtx(), "OmitPeriod", omitPeriod ?  "Y" : "N");
        Env.setContext(Env.getCtx(), "OmitPriceListValidation", omitPeriod ?  "Y" : "N");

        
        PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
            
         try {
             ResultSet rs = pstmt.executeQuery();

             while (rs.next()) {
   
                //MAllocationHdr allocation = new MAllocationHdr(getCtx(),rs,get_TrxName());
                log.info("[ Cerrando Orden " + count + "] Order=" + rs.getInt(1));
                MOrder order = new MOrder(Env.getCtx(),rs,null);
                try {
                     if (order.processIt(MOrder.ACTION_Close))
                         count++;
                     else
                         countE++;
                 } catch (Exception e) {
                    countE++;
                    log.severe("main - " + e);
                }

            }
        } catch (Exception e) {
            log.severe("main - " + e);
            e.printStackTrace();
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
        log.info("Closed = " + count);
        log.info("Errors = " + countE);
    }	//	doIt

    @Override
    protected void prepare() {
    }

    @Override
    protected String doIt() throws Exception {
         return "";
    }
    
}	
