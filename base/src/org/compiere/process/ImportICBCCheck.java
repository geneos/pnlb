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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Import ICBC Check from I_ICBC_CHECK
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImportGLJournal.java,v 1.23 2005/11/25 21:57:27 jjanke Exp $
 */
public class ImportICBCCheck extends SvrProcess {

    /**	Delete old Imported				*/
    private boolean m_DeleteOldImported = false;
    /**	Delete inactive			*/
    private boolean m_DeleteInactive = true;
    /**	Complete Payments			*/
    private boolean m_CompletePayments = true;
    /**	Complete Payments			*/
    private int ICBC_Account = 1000029;
    final String tableName = "I_ICBC_CHECK";

    /**
     *  Prepare - e.g., get Parameters.
     */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null)
            ; else if (name.equals("DeleteOldImported")) {
                m_DeleteOldImported = "Y".equals(para[i].getParameter());
            } else if (name.equals("DeleteInactive")) {
                m_DeleteInactive = "Y".equals(para[i].getParameter());
            } else if (name.equals("CompletePayments")) {
                m_CompletePayments = "Y".equals(para[i].getParameter());
            } else if (name.equals("ICBC_Account")) {
                ICBC_Account = para[i].getParameterAsInt();
            } else {
                log.log(Level.SEVERE, "Unknown Parameter: " + name);
            }
        }
    }	//	prepare

    /**
     *  Perrform process.
     *  @return Message
     *  @throws Exception
     */
    protected String doIt() throws java.lang.Exception {
        log.info("DeleteOldImported=" + m_DeleteOldImported + " + DeleteInactive" + m_DeleteInactive);
        StringBuffer sql = null;
        String clientCheck = " AND AD_Client_ID=" + getAD_Client_ID();
        String isActive = " AND i.IsActive= 'Y'";

        int no = 0;
        int maxNroCheque= 0;



        //	****	Prepare	****

        //	Delete Old Imported
        if (m_DeleteOldImported) {
            sql = new StringBuffer("DELETE FROM " + tableName
                    + " WHERE I_IsImported='Y'");
            no = DB.executeUpdate(sql.toString(), get_TrxName());
            log.fine("Delete Old Impored =" + no);
        }

        //	Delete Inactive record
        if (m_DeleteInactive) {
            sql = new StringBuffer("DELETE FROM " + tableName
                    + " WHERE isActive is null OR isActive='N'");
            no = DB.executeUpdate(sql.toString(), get_TrxName());
            log.fine("Delete Inactive =" + no);
        }

        //	Set IsActive, Created/Updated
        sql = new StringBuffer("UPDATE " + tableName
                + " SET IsActive = COALESCE (IsActive, 'Y'),"
                + " Created = COALESCE (Created, SysDate),"
                + " CreatedBy = COALESCE (CreatedBy, 0),"
                + " Updated = COALESCE (Updated, SysDate),"
                + " UpdatedBy = COALESCE (UpdatedBy, 0),"
                + " I_ErrorMsg = NULL,"
                + " I_IsImported = 'N' "
                + "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        log.info("Reset=" + no);

        //	Set Business Partner
        sql = new StringBuffer("UPDATE " + tableName + " i "
                + "SET C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner bp"
                + " WHERE REPLACE(bp.taxid,'-','')=i.NUMERO_DOCUMENTO_BENEFICIARIO AND ROWNUM=1) "
                + "WHERE C_BPartner_ID IS NULL AND NUMERO_DOCUMENTO_BENEFICIARIO IS NOT NULL"
                + " AND I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        log.fine("Set Business Partner=" + no);


        sql = new StringBuffer("UPDATE " + tableName + " i "
                + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Business Partner, '"
                + "WHERE (C_BPartner_ID IS NULL OR C_BPartner_ID=0)"
                + " AND I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        if (no != 0) {
            log.warning("Invalid Business Partner=" + no);
        }

        //Validate nro cheque
        sql = new StringBuffer("UPDATE " + tableName + " i "
                + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Duplicated Check Number, '"
                + "WHERE EXISTS (SELECT 1 from C_ValorPago where NROCHEQUE = i.NUMERO_CHEQUE AND tipo = 'P' AND C_BankAccount_id = " + ICBC_Account + ")"
                + " AND I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        if (no != 0) {
            log.warning("Duplicated Check Number=" + no);
        }

        //	Count Errors
        int errors = DB.getSQLValue(get_TrxName(),
                "SELECT COUNT(*) FROM " + tableName + " i WHERE I_IsImported NOT IN ('Y','N')" + clientCheck + isActive);

        log.info("Validation Errors=" + errors);
        commit();

        /*********************************************************************/
        int noInsertLine = 0;
        int noInsertPayment = 0;
        int noProcessPayment = 0;

        //	Go through ICBC Check Records
        sql = new StringBuffer("SELECT * FROM " + tableName + " i "
                + "WHERE I_IsImported='N' ").append(clientCheck).append(isActive);

        PreparedStatement pstmt = null;

        try {
            pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
            ResultSet rs = pstmt.executeQuery();

            //
            while (rs.next()) {
                X_I_ICBC_CHECK imp = new X_I_ICBC_CHECK(getCtx(), rs, get_TrxName());
                MPayment payment = null;
                System.out.println(imp.getBENEFICIARIO());

                if (imp.getC_Payment_ID() == 0) //Create new Payment
                {
                    payment = new MPayment(getCtx(), 0, get_TrxName());
                } else {
                    payment = new MPayment(getCtx(), imp.getC_Payment_ID(), get_TrxName());
                }

                payment.setAD_Org_ID(imp.getAD_Org_ID());
                payment.setC_DocType_ID(false);
                payment.setTrxType(MPayment.TRXTYPE_CreditPayment);
                payment.setAmount(118, imp.getMONTO());
                payment.setPAYNET(imp.getMONTO());
                payment.setDateTrx(imp.getUpdated());
                payment.setDateAcct(imp.getUpdated());
                payment.setIsPrepayment(true);
                payment.setDescription("Pago generado automaticamente desde proceso \"Importar Cheques ICBC\"");
                payment.setC_BPartner_ID(imp.getC_BPartner_ID());

                MBPartnerLocation[] locations = MBPartnerLocation.getForBPartner(getCtx(), imp.getC_BPartner_ID());

                if (locations.length == 0) {
                    imp.setI_ErrorMsg("ERROR creating Payment: No Location por BusinessPartner");
                    imp.setI_IsImported(false);
                    imp.save();
                    continue;
                }

                payment.setC_BPartner_Location_ID(locations[0].getC_BPartner_Location_ID());

                try {
                    PreparedStatement pstm = DB.prepareStatement("SELECT C_JURISDICCION_ID FROM C_BPartner_Jurisdiccion"
                            + " WHERE C_BPartner_ID = ? ORDER BY C_JURISDICCION_ID", get_TrxName());
                    pstm.setInt(1, payment.getC_BPartner_ID());
                    ResultSet rs2 = pstm.executeQuery();

                    if (rs2.next()) {
                        payment.setC_JURISDICCION_ID(rs2.getInt(1));
                    } else {
                        payment.setC_JURISDICCION_ID(0);
                    }

                    pstm.close();
                    rs2.close();
                } catch (Exception e) {
                }

                MBPartner bPartner = new MBPartner(getCtx(), imp.getC_BPartner_ID(), get_TrxName());
                if (!bPartner.isEXENCIONGANANCIAS()) {
                    payment.setC_REGIMENGANANCIAS_V_ID("116 Profesionales");
                }

                if (!payment.save()) {
                    imp.setI_ErrorMsg("ERROR creating Payment");
                    imp.setI_IsImported(false);
                    imp.save();
                    continue;
                } else if (imp.getC_Payment_ID() == 0) {
                    noInsertPayment++;
                }

                imp.setC_Payment_ID(payment.getC_Payment_ID());
                imp.save();

                //	Lines
                MVALORPAGO line = null;
                if (imp.getC_VALORPAGO_ID() == 0) {
                    line = new MVALORPAGO(getCtx(), 0, get_TrxName());
                } else {
                    line = new MVALORPAGO(getCtx(), imp.getC_VALORPAGO_ID(), get_TrxName());
                }

                line.setC_Payment_ID(payment.getC_Payment_ID());
                line.setIMPORTE(imp.getMONTO());
                line.setAFavor(imp.getBENEFICIARIO());
                line.setC_BankAccount_ID(ICBC_Account); //ICBC Hardcoded
                String nroCheque = removePrefix(imp.getNUMERO_CHEQUE());
                line.setNroCheque(nroCheque);
                int currentNumeroCheque = Integer.parseInt(nroCheque);
                if (currentNumeroCheque > maxNroCheque)
                    maxNroCheque = currentNumeroCheque;
                
                line.setOrden(true);
                line.setTipoCheque(MVALORPAGO.DIFERIDO);
                line.setTIPO(MVALORPAGO.PROPIO);
                line.setEstado(MVALORPAGO.EMITIDO);
              
                line.setPaymentDate(imp.getFECHA_PAGO());
                //line.setReleasedDate(imp.getFECHA_EMICION_LISTA());
                line.setReleasedDate(payment.getDateTrx());

                if (!line.save()) {
                    imp.setI_ErrorMsg("ERROR creating Check");
                    imp.setI_IsImported(false);
                    imp.save();
                    continue;
                } else if (imp.getC_VALORPAGO_ID() == 0) {
                    noInsertLine++;
                }

                if (m_CompletePayments) { //CHeck for process payment
                    if (!payment.processIt(DocAction.ACTION_Complete)) {
                        imp.setI_ErrorMsg("ERROR processing payment: " + payment.getProcessMsg());
                        imp.setI_IsImported(false);
                        imp.save();
                        continue;
                    } else {
                        noProcessPayment++;
                    }
                }

                payment.save();
                imp.setI_IsImported(true);
                imp.setI_ErrorMsg(null);
                imp.setProcessed(true);

                imp.save();

            }	//	while records
            
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //	clean up
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException ex1) {
        }

        pstmt = null;
        
        //Finally update Sequence for BankAccount
        PreparedStatement pstm = DB.prepareStatement("SELECT * FROM C_BankAccountDoc"
                + " WHERE C_BankAccount_ID = ? ", get_TrxName());
        pstm.setInt(1, ICBC_Account);
        ResultSet rs2 = pstm.executeQuery();

        if (rs2.next()) {
            X_C_BankAccountDoc doc = new X_C_BankAccountDoc(getCtx(), rs2, get_TrxName());
            int currentNext = doc.getCurrentNext();
            if (maxNroCheque+1 > currentNext){
                 doc.setCurrentNext(maxNroCheque+1);
                 doc.save();
            }
           
        } 

        //	Set Error to indicator to not imported
        sql = new StringBuffer("UPDATE " + tableName + " i "
                + "SET I_IsImported='N', Updated=SysDate "
                + "WHERE I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        addLog(0, null, new BigDecimal(no), "@Errors@");
        //
        addLog(0, null, new BigDecimal(noInsertPayment), "Pagos creados");
        addLog(0, null, new BigDecimal(noInsertLine), "Cheques creados");
        addLog(0, null, new BigDecimal(noProcessPayment), "Pagos Procesados");

        return "";
    }	//	doIt

    private String removePrefix(String value) {
        String regex = "^0+";
        return value.replaceAll(regex, "");
    }
}	//	ImportGLJournalLines