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
 *	Import GL Journal Batch/JournalLine from I_Journal
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImportGLJournal.java,v 1.23 2005/11/25 21:57:27 jjanke Exp $
 */
public class ImportGLJournalLine extends SvrProcess {

    /**	Journal to be imported to		*/
    private int m_GL_Journal_ID = 0;
    /**	Delete old Imported				*/
    private boolean m_DeleteOldImported = false;
    /**	Delete inactive			*/
    private boolean m_DeleteInactive = false;
    /**	Don't import					*/
    private boolean m_IsValidateOnly = false;
    /** Import if no Errors				*/
    private boolean m_IsImportOnlyNoErrors = true;

    /**
     *  Prepare - e.g., get Parameters.
     */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null)
            ; else if (name.equals("GL_Journal_ID")) {
                m_GL_Journal_ID = ((BigDecimal) para[i].getParameter()).intValue();
            } else if (name.equals("IsImportOnlyNoErrors")) {
                m_IsImportOnlyNoErrors = "Y".equals(para[i].getParameter());
            } else if (name.equals("IsValidateOnly")) {
                m_IsValidateOnly = "Y".equals(para[i].getParameter());
            } else if (name.equals("DeleteOldImported")) {
                m_DeleteOldImported = "Y".equals(para[i].getParameter());
            } else if (name.equals("DeleteInactive")) {
                m_DeleteInactive = "Y".equals(para[i].getParameter());
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
        log.info("IsValidateOnly=" + m_IsValidateOnly + ", IsImportOnlyNoErrors=" + m_IsImportOnlyNoErrors);
        StringBuffer sql = null;
        String clientCheck = " AND AD_Client_ID=" + getAD_Client_ID();
        String isActive = " AND i.IsActive= 'Y'";

        int no = 0;

        //	****	Prepare	****

        //	Delete Old Imported
        if (m_DeleteOldImported) {
            sql = new StringBuffer("DELETE I_GLJournalLine "
                    + "WHERE I_IsImported='Y'");
            no = DB.executeUpdate(sql.toString(), get_TrxName());
            log.fine("Delete Old Impored =" + no);
        }
        
        //	Delete Inactive record
        if (m_DeleteInactive) {
            sql = new StringBuffer("DELETE I_GLJournalLine "
                    + "WHERE isActive is null OR isActive='N'");
            no = DB.executeUpdate(sql.toString(), get_TrxName());
            log.fine("Delete Inactive =" + no);
        }

        //	Set IsActive, Created/Updated
        sql = new StringBuffer("UPDATE I_GLJournalLine "
                + "SET IsActive = COALESCE (IsActive, 'Y'),"
                + " Created = COALESCE (Created, SysDate),"
                + " CreatedBy = COALESCE (CreatedBy, 0),"
                + " Updated = COALESCE (Updated, SysDate),"
                + " UpdatedBy = COALESCE (UpdatedBy, 0),"
                + " I_ErrorMsg = NULL,"
                + " I_IsImported = 'N' "
                + "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        log.info("Reset=" + no);

        //	Set Account
        sql = new StringBuffer("UPDATE I_GLJournalLine i "
                + "SET Account_ID=(SELECT ev.C_ElementValue_ID FROM C_ElementValue ev"
                + " INNER JOIN C_Element e ON (e.C_Element_ID=ev.C_Element_ID)"
                + " INNER JOIN C_AcctSchema_Element ase ON (e.C_Element_ID=ase.C_Element_ID AND ase.ElementType='AC')"
                + " WHERE ev.Value=i.AccountValue AND ev.IsSummary='N'"
                + " AND i.AD_Client_ID=ev.AD_Client_ID AND ROWNUM=1) "
                + "WHERE Account_ID IS NULL AND AccountValue IS NOT NULL"
                + " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        log.fine("Set Account from Value=" + no);
        sql = new StringBuffer("UPDATE I_GLJournalLine i "
                + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Account, '"
                + "WHERE (Account_ID IS NULL OR Account_ID=0)"
                + " AND (C_ValidCombination_ID IS NULL OR C_ValidCombination_ID=0) AND I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        if (no != 0) {
            log.warning("Invalid Account=" + no);
        }


        //	Source Amounts
        sql = new StringBuffer("UPDATE I_GLJournalLine i "
                + "SET AmtSourceDr = 0 "
                + "WHERE AmtSourceDr IS NULL"
                + " AND I_IsImported<>'Y'").append(clientCheck).append(isActive);;
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        log.fine("Set 0 Source Dr=" + no);
        sql = new StringBuffer("UPDATE I_GLJournalLine i "
                + "SET AmtSourceCr = 0 "
                + "WHERE AmtSourceCr IS NULL"
                + " AND I_IsImported<>'Y'").append(clientCheck).append(isActive);;
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        log.fine("Set 0 Source Cr=" + no);
        sql = new StringBuffer("UPDATE I_GLJournalLine i "
                + "SET I_ErrorMsg=I_ErrorMsg||'WARN=Zero Source Balance, ' "
                + "WHERE (AmtSourceDr-AmtSourceCr)=0"
                + " AND I_IsImported<>'Y'").append(clientCheck).append(isActive);;
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        if (no != 0) {
            log.warning("Zero Source Balance=" + no);
        }

        //	Accounted Amounts (Only if No Error) => (Currency Rate always 1, just ARS)
        sql = new StringBuffer("UPDATE I_GLJournalLine i "
                + "SET AmtAcctDr = ROUND(AmtSourceDr * 1, 2) " //	HARDCODED rounding
                + "WHERE  I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        log.fine("Calculate Acct Dr=" + no);
        sql = new StringBuffer("UPDATE I_GLJournalLine i "
                + "SET AmtAcctCr = ROUND(AmtSourceCr * 1, 2) "
                + "WHERE I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        log.fine("Calculate Acct Cr=" + no);
        sql = new StringBuffer("UPDATE I_GLJournalLine i "
                + "SET I_ErrorMsg=I_ErrorMsg||'WARN=Zero Acct Balance, ' "
                + "WHERE (AmtSourceDr-AmtSourceCr)<>0 AND (AmtAcctDr-AmtAcctCr)=0"
                + " AND I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        if (no != 0) {
            log.warning("Zero Acct Balance=" + no);
        }

        /*********************************************************************/
        //	Get Balance (Ony from NON IMPORTED NON ERROR records)
        sql = new StringBuffer("SELECT SUM(AmtSourceDr)-SUM(AmtSourceCr), SUM(AmtAcctDr)-SUM(AmtAcctCr) "
                + "FROM I_GLJournalLine i "
                + "WHERE I_IsImported = 'N'").append(clientCheck).append(isActive);
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                BigDecimal source = rs.getBigDecimal(1);
                BigDecimal acct = rs.getBigDecimal(2);
                if (source != null && source.signum() == 0
                        && acct != null && acct.signum() == 0) {
                    log.info("Import Balance = 0");
                } else {
                    log.warning("Balance Source=" + source + ", Acct=" + acct);
                }
                if (source != null) {
                    addLog(0, null, source, "@AmtSourceDr@ - @AmtSourceCr@");
                }
                if (acct != null) {
                    addLog(0, null, acct, "@AmtAcctDr@ - @AmtAcctCr@");
                }
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (SQLException ex) {
            log.log(Level.SEVERE, sql.toString(), ex);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException ex1) {
        }
        pstmt = null;

        //	Count Errors
        int errors = DB.getSQLValue(get_TrxName(),
                "SELECT COUNT(*) FROM I_GLJournalLine i WHERE I_IsImported NOT IN ('Y','N')" + clientCheck + isActive);

        log.info("Validation Errors=" + errors);
        commit();
        
        if (errors != 0) {
            if (m_IsValidateOnly || m_IsImportOnlyNoErrors) {
                throw new Exception("@Errors@=" + errors);
            }
        } else if (m_IsValidateOnly) {
            return "@Errors@=" + errors;
        }

    

        /*********************************************************************/
        int noInsertLine = 0;

        //	Go through Journal Records
        sql = new StringBuffer("SELECT * FROM I_GLJournalLine i "
                + "WHERE I_IsImported='N'").append(clientCheck).append(isActive);
        try {
            pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
            ResultSet rs = pstmt.executeQuery();

            //
            while (rs.next()) {
                X_I_GLJournalLine imp = new X_I_GLJournalLine(getCtx(), rs, get_TrxName());

                MJournal journal = new MJournal(getCtx(), m_GL_Journal_ID, get_TrxName());

                //	Lines
                MJournalLine line = new MJournalLine(journal);
                //
                line.setDescription(imp.getDescription());

                //Always in ARS
                line.setCurrency(118, 114, BigDecimal.ONE);

                //	Get Account Combination

                MAccount acct = MAccount.get(getCtx(), journal.getAD_Client_ID(), journal.getAD_Org_ID(),
                        journal.getC_AcctSchema_ID(), imp.getAccount_ID(), 0,
                        0, 0, 0,
                        0, 0, 0,
                        0, 0, 0,
                        0, 0, 0, 0);
                if (acct != null && acct.get_ID() == 0) {
                    acct.save();
                }
                if (acct == null || acct.get_ID() == 0) {
                    imp.setI_ErrorMsg("ERROR finding/creating Valid combination (Check if is active) ");
                    imp.setI_IsImported(false);
                    imp.save();
                    continue;
                } else {
                    line.setC_ValidCombination_ID(acct.get_ID());
                    imp.setC_ValidCombination_ID(acct.get_ID());
                }

                line.setLine(imp.getLine());
                line.setAmtSourceCr(imp.getAmtSourceCr().setScale(2));
                line.setAmtSourceDr(imp.getAmtSourceDr().setScale(2));
                line.setAmtAcct(imp.getAmtAcctDr(), imp.getAmtAcctCr().setScale(2));
                //	only if not 0
                line.setDateAcct(journal.getDateAcct());

                if (line.save()) {
                    imp.setGL_Journal_ID(journal.getGL_Journal_ID());
                    imp.setGL_JournalLine_ID(line.getGL_JournalLine_ID());
                    imp.setI_IsImported(true);
                    imp.setProcessed(true);
                    if (imp.save()) {
                        noInsertLine++;
                    }
                }
            }	//	while records
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
        }
        //	clean up
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException ex1) {
        }

        pstmt = null;

        //	Set Error to indicator to not imported
        sql = new StringBuffer("UPDATE I_GLJournalLine i "
                + "SET I_IsImported='N', Updated=SysDate "
                + "WHERE I_IsImported<>'Y'").append(clientCheck).append(isActive);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        addLog(0, null, new BigDecimal(no), "@Errors@");
        //
        addLog(0, null, new BigDecimal(noInsertLine), "@GL_JournalLine_ID@: @Inserted@");
        return "";
    }	//	doIt
}	//	ImportGLJournalLines