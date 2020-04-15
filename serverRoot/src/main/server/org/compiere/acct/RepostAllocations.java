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
package org.compiere.acct;

import org.compiere.process.*;
import java.awt.geom.*;
import java.math.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.acct.Doc;

public class RepostAllocations extends SvrProcess {

    /**	Order Date From		*/
    private Timestamp p_DateAcct_From;
    /**	Order Date To		*/
    private Timestamp p_DateAcct_To;
    private boolean p_allWithError;
    private boolean p_fixPayments;
    
    int m_postCount = 0;
    int m_postCount_Err = 0;

    /**
     *  Prepare - e.g., get Parameters.
     */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null)
                                            ; else if (name.equals("DateAcct")) {
                p_DateAcct_From = (Timestamp) para[i].getParameter();
                p_DateAcct_To = (Timestamp) para[i].getParameter_To();
            } else if (name.equals("AllAllocationsWithError")) {
                p_allWithError = para[i].getParameter().equals("Y");
            } else if (name.equals("FixPayments")) {
                p_fixPayments = para[i].getParameter().equals("Y");
            } else if (name.equals("DateAcct")) {
                p_DateAcct_From = (Timestamp) para[i].getParameter();
                p_DateAcct_To = (Timestamp) para[i].getParameter_To();
            } else {
                log.log(Level.SEVERE, "Unknown Parameter: " + name);
            }
        }
    }	//	prepare

    /**
     *  Perrform process.
     *  @return Message 
     *  @throws Exception if not successful
     */
    protected String doIt() throws Exception {
        Env.setContext(Env.getCtx(), "OmitPeriod", "Y");
         String sql = null; 
         
        if (p_fixPayments) {
            sql = "SELECT p.c_payment_id, SUM(pv.importe),SUM(pv.mextranjera) "
                    + "FROM c_payment p "
                    + "JOIN c_valorpago pv on pv.c_payment_id = p.c_payment_id "
                    + "WHERE p.c_currency_id != 118 "
                    + "AND p.dateacct between ? AND ? "
                    + "AND p.docstatus in ('CO','CL') "
                    + "AND p.posted = 'Y' "
                    + "GROUP BY  p.c_payment_id ";
            postPayments(sql);
       }
        
       sql = "SELECT distinct(alloc.c_allocationhdr_id) from c_allocationhdr alloc "
                + "JOIN c_allocationline allocl on alloc.c_allocationhdr_id =  allocl.c_allocationhdr_id "
                + "LEFT JOIN c_payment p on p.c_payment_id = allocl.c_payment_id "
                + "LEFT JOIN c_invoice inv on inv.c_invoice_id = allocl.c_invoice_id "
                + "WHERE alloc.dateacct between ? AND ? "
                + "AND alloc.posted <> 'N'  "
                + "AND alloc.docstatus in ('CO','CL') "
                + "AND (p.isprepayment = 'N' OR p.C_Currency_ID <> 118 OR inv.C_Currency_ID <> 118) ";
         
        postAllocations(sql);

        if (p_allWithError) {
            sql = "SELECT distinct(alloc.c_allocationhdr_id) from c_allocationhdr alloc "
                    + "JOIN c_allocationline allocl on alloc.c_allocationhdr_id =  allocl.c_allocationhdr_id "
                    + "LEFT JOIN c_payment p on p.c_payment_id = allocl.c_payment_id "
                    + "LEFT JOIN c_invoice inv on inv.c_invoice_id = allocl.c_invoice_id "
                    + "WHERE alloc.dateacct between ? AND ? "
                    + "AND alloc.posted <> 'N'  "
                    + "AND alloc.posted <> 'Y'  "
                    + "AND alloc.docstatus in ('CO','CL') ";
             postAllocations(sql);
        }

        Env.setContext(Env.getCtx(), "OmitPeriod", 'N');
        return "@Posted@ " + m_postCount + "; @Error@ " + m_postCount_Err;
    }	//	doIt

    private void postAllocations(String sql) {
        PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);

        try {
            pstmt.setTimestamp(1, p_DateAcct_From);
            pstmt.setTimestamp(2, p_DateAcct_To);


            ResultSet rs = pstmt.executeQuery();
            MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(getCtx(), Env.getAD_Client_ID(getCtx()));

            while (rs.next()) {
                //MAllocationHdr allocation = new MAllocationHdr(getCtx(),rs,get_TrxName());
                log.info("[ Recontabilizando " + m_postCount + "] Allocation=" + rs.getInt(1));
                String ret = Doc.postImmediate(ass, 735, rs.getInt(1), true, null);
                if (ret != null) {
                    m_postCount_Err++;
                    log.log(Level.SEVERE, "Error: " + ret);
                    System.out.println("Error: " + ret);
                } else {
                    m_postCount++;
                }
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
    }
    
    private void postPayments(String sql) {
        PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);

        try {
            pstmt.setTimestamp(1, p_DateAcct_From);
            pstmt.setTimestamp(2, p_DateAcct_To);


            ResultSet rs = pstmt.executeQuery();
            MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(getCtx(), Env.getAD_Client_ID(getCtx()));

            while (rs.next()) {
                
                MPayment payment = new MPayment(getCtx(),rs.getInt(1),null);
                if (!rs.getBigDecimal(3).equals(BigDecimal.ZERO)) {
                    log.info("[ Recalculando tasa de cotizacion " + m_postCount + "] Payment=" + rs.getInt(1));
                    BigDecimal cotizacion = rs.getBigDecimal(2).divide(rs.getBigDecimal(3),3, RoundingMode.HALF_UP);
                     System.out.println("OP"+payment.getDocumentNo()+": "+cotizacion);
                    payment.setCotizacion(cotizacion);
                    payment.save();
                }
                
                log.info("[ Recontabilizando " + m_postCount + "] Payment=" + rs.getInt(1));
                String ret = Doc.postImmediate(ass, 335, rs.getInt(1), true, null);
                if (ret != null) {
                    m_postCount_Err++;
                    log.log(Level.SEVERE, "Error: " + ret);
                    System.out.println("Error: " + ret);
                } else {
                    m_postCount++;
                }
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
    }
}	//	doIt
