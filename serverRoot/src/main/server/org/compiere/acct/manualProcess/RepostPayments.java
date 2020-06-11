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

public class RepostPayments extends SvrProcess {

    private static boolean omitPeriod = true;
    private static boolean omitRetentionsCalculate = true;
    private static boolean recalculateCotizacion = false;
    
    int m_postCount = 0;
    int m_postCount_Err = 0;
    
    private static CLogger log = CLogger.getCLogger(RepostPayments.class);
    private static String sqlCheques = "select p.* from c_valorpago  vp "
                                   + " JOIN c_payment p on p.c_payment_id = vp.c_payment_id "
                                   + " WHERE vp.nrocheque in ('78043196','78043616','78043617','78043619','78043622','78043623','78043624','78043625','78043626','78043629','78043630','78043632','78043636','78043637','78043638','78043639','78043642','78043644','78043646','78043650','78043651','78043653','78043655','78043656','78043658','78043659','78043661','78043662','78043663','78043664','78043665','78043666','78043668','78043669','78043671','78043672','78043674','78043684','78043685','78043689','78043690','78043692','78043700','78043701','78043702','78043703','78043704','78043705','78043706','78043707','78043712','78043713','78043714','78043715','78043717','78043718','78043719','78043720','78043723','78043724','78043725','78043727','78043728','78043729','78043730','78043731','78043732','78043733','78043734','78043736','78043739','78043742','78043745','78043746','78043747','78043748','78043749','78043755','78043757','78043760','78043762','78043763','78043764','78043765','78043767','78043768','78043769','78043770','78043771','78043772','78043773','78043774','78043775','78043776','78043779','78043780','78043781','78043782','78043783','78043784','78043785','78043787','78043788','78043792','78043793','78043794','78043796','78043797','78043798','78043799','78043800','78043824','78043825','78043832','78043834','78043843','78043845','78043846','78043855','78043857','78043858','78043871','78043877','78043881','78043883','78043890','78043895','78043898','78043901','78043913','78043919','78043927','78043930','78043932','78043950','78043951','78043956','78043967','78043984','78043986','78043988','78043989','78043993','78043994','78044000','78044001','78044005','78044007','78044008','78044012','78044014','78044018','78044020','78044021','78044023','78044034','78044036','78044038','78044040','78044045','78044066','78044067','78044087','78044088','78044090','78044093','78044098','78044099','78044114','78044115','78044123','78044126','78044128','78044129','78044130','78044131','78044132','78044133','78044137','78044138','78044139','78044141','78044143','78044144','78044146','78044147','78044148','78044150','78044162','78044167','78044201','78044203','78044205','78044207','78044212','78044214','78044216','78044240','78044243','78044244','78044248','78044249','78044265','78044266','78044268','78044269','78044270','78044271','78044273','78044277','78044278','78044279','78044280','78044281','78044282','78044284','78044285','78044286','78044287','78044288','78044291','78044293','78044295','78044296','78044297','78044298','78044300','78044301','78044302','78044307','78044308','78044309','78044310','78044316','78044318','78044319','78044321','78044322','78044325','78044328','78044333','78044335','78044338','78044339','78044341','78044343','78044344','78044345','78044346','78044349','78044353','78044354','78044355','78044356','78044357','78044360','78044361','78044362','78044364','78044365','78044366','78044367','78044371','78044377','78044381','78044383','78044389','78044391','78044393','78044397','78044404','78044405','78044406','78044408','78044409','78044414','78044416','78044421','78044422','78044423','78044424','78044425','78044426','78044427','78044429','78044431','78044432','78044433','78044434','78044436','78044437','78044439','78044440','78044441','78044442','78044443','78044444','78044445','78044446','78044450','78044462','78044465','78044466','78044469','78044471','78044475','78044476','78044477','78044478','78044479','78044480','78044481','78044482','78044484','78044486','78044487','78044493','78044496','78044498','78044505','78044507','78044508','78044509','78044511','78044512','78044513','78044514','78044515','78044517','78044518','78044519','78044521','78044527','78044528','78044320')";
   
    private static String sqlPeriodoCerrado = "select C_Payment_ID from C_Payment   "
                                   + "WHERE posted = 'p' and extract(YEAR from dateacct) = 2020 and docstatus = 'CO' " ;
        
    private static String sqlPagosEspecificosConCotizacion = "select p.C_Payment_ID,SUM(pv.importe),SUM(pv.mextranjera) "
                    + "FROM c_payment p "
                    + "JOIN c_valorpago pv on pv.c_payment_id = p.c_payment_id   "
                    + "WHERE p.c_payment_id in ( 5182437) "
                    + "GROUP BY  p.c_payment_id ";
    
     private static String sqlPagosEspecificos = "select C_Payment_ID FROM c_payment "
                    + "WHERE c_payment_id in (5183408) ";   
     //  (5180745,5181998,5183408)
    
    private static String sqlRecibos2020 = "select C_Payment_ID from C_Payment   "
                                   + "WHERE IsReceipt='Y' and EXTRACT(YEAR from dateacct) = 2020" ;
    
        

    /**************************************************************************
     * 	Repost Payment Class
     *  
     */
    public static void main(String[] args) {
        
        String sql = sqlPagosEspecificos;
        
        org.compiere.Compiere.startupEnvironment(true);
        CLogMgt.setLevel(Level.FINE);
        Properties ctx = Env.getCtx();
        ctx.setProperty("#AD_Client_ID", "1000002");
       
        log.info("Repost Payments   $Revision: 1.0 $");
        log.info("----------------------------------");
           
        Trx trx = Trx.get(Trx.createTrxName("RepostPayments"), true);

        int count = 0;
        int countE = 0;
        int countS = 0;
        
        Env.setContext(Env.getCtx(), "OmitPeriod", omitPeriod ?  "Y" : "N");
        Env.setContext(Env.getCtx(), "OmitRetentionCalculation", omitRetentionsCalculate ? "Y" : "N");
        
        PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
        MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(ctx, Env.getAD_Client_ID(ctx));
            
         try {
             ResultSet rs = pstmt.executeQuery();

             while (rs.next()) {
                 if (recalculateCotizacion) {
                    MPayment payment = new MPayment(ctx,rs.getInt(1),null);
                    if (!rs.getBigDecimal(3).equals(BigDecimal.ZERO)) {
                        log.info("[ Recalculando tasa de cotizacion] Payment=" + rs.getInt(1));
                        BigDecimal cotizacion = rs.getBigDecimal(2).divide(rs.getBigDecimal(3),3, RoundingMode.HALF_UP);
                         System.out.println("OP"+payment.getDocumentNo()+": "+cotizacion);
                        payment.setCotizacion(cotizacion);
                        if (!payment.save()){
                             countE++;
                            log.log(Level.SEVERE, "Error: " + payment.getErrorMessage());
                            System.out.println("Error: " + payment.getErrorMessage());
                        }
                           
                    }
                 }
                //MAllocationHdr allocation = new MAllocationHdr(getCtx(),rs,get_TrxName());
                log.info("[ Recontabilizando " + count + "] Payment=" + rs.getInt(1));
                String ret = Doc.postImmediate(ass, 335, rs.getInt(1), true, null);
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

    @Override
    protected void prepare() {
    }

    protected String doIt() throws Exception {
         String sql = sqlRecibos2020;
        
        org.compiere.Compiere.startupEnvironment(true);
        CLogMgt.setLevel(Level.FINE);
        Properties ctx = Env.getCtx();
        ctx.setProperty("#AD_Client_ID", "1000002");
       
        log.info("Repost Payments   $Revision: 1.0 $");
        log.info("----------------------------------");
           
        Trx trx = Trx.get(Trx.createTrxName("RepostPayments"), true);

        int count = 0;
        int countE = 0;
        int countS = 0;
        
        Env.setContext(Env.getCtx(), "OmitPeriod", omitPeriod ?  "Y" : "N");
        Env.setContext(Env.getCtx(), "OmitRetentionCalculation", omitRetentionsCalculate ? "Y" : "N");
        
        PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
        MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(ctx, Env.getAD_Client_ID(ctx));
            
         try {
             ResultSet rs = pstmt.executeQuery();

             while (rs.next()) {
                //MAllocationHdr allocation = new MAllocationHdr(getCtx(),rs,get_TrxName());
                log.info("[ Recontabilizando " + count + "] Payment=" + rs.getInt(1));
                String ret = Doc.postImmediate(ass, 335, rs.getInt(1), true, null);
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
        return "Generated = " + count + "Errors = " + countE + "Skipped = " + countS;
    }
    
}	
