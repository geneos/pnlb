/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ActFact BV
 *****************************************************************************/
package org.compiere.model;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;

import org.compiere.process.*;
import org.compiere.util.*;

/**
 *	Bank Statement Model
 *
 *	@author Eldir Tomassen/Jorg Janke
 *	@version $Id: MBankStatement.java,v 1.20 2005/11/12 22:58:56 jjanke Exp $
 */
@SuppressWarnings("serial")
public class MCONCILIACIONBANCARIA extends X_C_CONCILIACIONBANCARIA implements DocAction {
    private boolean  isReactivate = false;
    /**
     * 	Standard Constructor
     *	@param ctx context
     *	@param C_BankStatement_ID id
     */
    public MCONCILIACIONBANCARIA(Properties ctx, int C_ConciliacionBancaria_ID, String trxName) {
        super(ctx, C_ConciliacionBancaria_ID, trxName);
        if (C_ConciliacionBancaria_ID == 0) {
            //	setC_BankAccount_ID (0);	//	parent
            setFromDate(new Timestamp(System.currentTimeMillis()));	// @Date@
            setToDate(new Timestamp(System.currentTimeMillis()));	// @Date@
            setDocAction(DOCACTION_Complete);	// CO
            setDocStatus(DOCSTATUS_Drafted);	// DR
            setSaldoInicial(Env.ZERO); //setBeginningBalance(account.getCurrentBalance());
            setSaldoCierre(Env.ZERO);
            setSaldoConciliado(Env.ZERO);
            setSaldoPendiente(Env.ZERO);
            setSaldoContable(Env.ZERO);
            setSaldoAConciliar(Env.ZERO);

            setPosted(false);	// N
            super.setProcessed(false);
        }
    }	//	MCONCILIACIONBANCARIA

    /**
     * 	Load Constructor
     * 	@param ctx Current context
     * 	@param rs result set
     */
    public MCONCILIACIONBANCARIA(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }	//	MCONCILIACIONBANCARIA

    /**
     * 	Set Processed.
     * 	Propergate to Lines/Taxes
     *	@param processed processed
     */
    public void setProcessed(boolean processed) {
        super.setProcessed(processed);
        if (get_ID() == 0) {
            return;
        }
        String sql = "UPDATE C_MovimientoConciliacion SET Processed='"
                + (processed ? "Y" : "N")
                + "' WHERE C_ConciliacionBancaria_ID=" + getC_ConciliacionBancaria_ID();
        int noLine = DB.executeUpdate(sql, get_TrxName());

        log.fine("setProcessed - " + processed + " - Lines=" + noLine);
    }	//	setProcessed

    /**
     * 	Get Bank Account
     *	@return bank Account
     */
    public MBankAccount getBankAccount() {
        return MBankAccount.get(getCtx(), getC_BankAccount_ID());
    }	//	getBankAccount

    /**
     * 	Get Document No
     *	@return name
     */
    public String getDocumentNo() {
        return getConciliacion();
    }	//	getDocumentNo

    /**
     * 	Get Document Info
     *	@return document info (untranslated)
     */
    public String getDocumentInfo() {
        return getBankAccount().getName() + " " + getDocumentNo();
    }	//	getDocumentInfo

    /**
     * 	Create PDF
     *	@return File or null
     */
    public File createPDF() {
        try {
            File temp = File.createTempFile(get_TableName() + get_ID() + "_", ".pdf");
            return createPDF(temp);
        } catch (Exception e) {
            log.severe("Could not create PDF - " + e.getMessage());
        }
        return null;
    }	//	getPDF

    /**
     * 	Create PDF file
     *	@param file output file
     *	@return file if success
     */
    public File createPDF(File file) {
        //	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
        //	if (re == null)
        return null;
        //	return re.getPDF(file);
    }	//	createPDF

    /**
     * 	Before Save
     *	@param newRecord new
     *	@return true
     */
    protected boolean beforeSave(boolean newRecord) {
        
        //if (DocAction.ACTION_ReActivate.equals(getDocAction()) && DocAction.STATUS_D.equals(getDocStatus()))
           
        
        String sql = "";
        if(!isReactivate) {
             // Verificación de Superposición de Fechas.

             sql = "SELECT 1 "
                    + " FROM C_CONCILIACIONBANCARIA "
                    + " WHERE C_BankAccount_Id = ? AND C_CONCILIACIONBANCARIA_ID !=? AND IsActive = 'Y'"
                    + " AND ( FROMDATE between ? and ?"
                    + " OR TODATE between ? AND ? "
                    + " OR (FROMDATE <= ? and TODATE >= ?) )";
    /*
            String sql = "SELECT FROMDATE, TODATE "
                    + " FROM C_CONCILIACIONBANCARIA "
                    + " WHERE C_BankAccount_Id = ? AND C_CONCILIACIONBANCARIA_ID !=? AND IsActive = 'Y'";*/

            try {
                PreparedStatement pstmt = DB.prepareStatement(sql, null);

                pstmt.setInt(1, getC_BankAccount_ID());
                pstmt.setInt(2, getC_ConciliacionBancaria_ID());
                pstmt.setTimestamp(3, getFromDate());
                pstmt.setTimestamp(4, getToDate());
                pstmt.setTimestamp(5, getFromDate());
                pstmt.setTimestamp(6, getToDate());
                pstmt.setTimestamp(7, getFromDate());
                pstmt.setTimestamp(8, getToDate());

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Existe Superpocición entre Conciliaciones para la misma Cuenta Bancaria", "ERROR - Superpocición de Fechas", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                pstmt.close();
                rs.close();
            } catch (SQLException sqlE) {
            }

        //	Verificación de Conciliaciones anteriores Completas.
       
            sql = "SELECT C_CONCILIACIONBANCARIA_ID"
                    + " FROM C_CONCILIACIONBANCARIA "
                    + " WHERE C_BankAccount_Id = ? AND C_CONCILIACIONBANCARIA_ID !=? AND DocStatus='DR' AND IsActive = 'Y'";

            try {
                PreparedStatement pstmt = DB.prepareStatement(sql, null);
                pstmt.setInt(1, getC_BankAccount_ID());
                pstmt.setInt(2, getC_ConciliacionBancaria_ID());

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Existe Conciliación Abierta", "ERROR - Validación Conciliaciones", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if (newRecord) {
                    sql = "SELECT TODATE, AMOUNTCIERRE "
                            + " FROM C_CONCILIACIONBANCARIA "
                            + " WHERE C_BankAccount_Id = ? AND C_CONCILIACIONBANCARIA_ID !=? AND IsActive = 'Y' AND TODATE < ? AND DocStatus IN ('CO','CL')"
                            + " Order By TODATE desc";

                    pstmt = DB.prepareStatement(sql, null);

                    pstmt.setInt(1, getC_BankAccount_ID());
                    pstmt.setInt(2, getC_ConciliacionBancaria_ID());
                    pstmt.setTimestamp(3, getFromDate());

                    rs = pstmt.executeQuery();

                    BigDecimal bd = getSaldoInicial();

                    if (rs.next()) {
                        bd = rs.getBigDecimal(2);
                    }

                    setSaldoInicial(bd);
                }

                pstmt.close();
                rs.close();
            } catch (SQLException sqlE) {
            }
        }


        try {
            /*
             *   Modificado para que tome el saldo inicial de las cuentas contables
             *   12/03/2012 por problema en la carga del saldo inicial de conciliaciones bancarias
             *   José Fantasia
             */

            sql = " SELECT sum(fa.amtacctdr)-sum(fa.amtacctcr) "
                    + " FROM c_bankaccount_acct ba "
                    + " INNER JOIN C_Validcombination vc ON (vc.C_Validcombination_id = ba.B_ASSET_ACCT) "
                    + "	INNER JOIN FACT_ACCT fa ON (fa.account_id = vc.account_id) "
                    + "	WHERE ba.C_BankAccount_Id=? and fa.dateacct >= to_date('01/01/2012', 'dd,mm,yyyy') and fa.dateacct <= ?";

            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, getC_BankAccount_ID());
            pstmt.setTimestamp(2, getToDate());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getBigDecimal(1) != null) {
                setSaldoContable(rs.getBigDecimal(1));
            } else {
                JOptionPane.showMessageDialog(null, "No fue posible extraer el Saldo Contable de la Cuanta Bancaria", "INFORMACIÓN - Saldo Registros Contables", JOptionPane.INFORMATION_MESSAGE);
                setSaldoContable(BigDecimal.ZERO);
            }

            pstmt.close();
            rs.close();
        } catch (SQLException sqlE) {
        }


        return true;
    }	//	beforeSave

    /*************************************************************************
     * 	Process document
     *	@param action document action
     *	@return true if performed
     */
    public boolean processIt(String processAction) throws Exception {
        m_processMsg = null;
        DocumentEngine engine = new DocumentEngine(this, getDocStatus());
        return engine.processIt(processAction, getDocAction());
    }

    /**
     * 	Unlock Document.
     * 	@return true if success
     */
    public boolean unlockIt() {
        log.info(toString());
        setProcessing(false);
        return true;
    }	//	unlockIt

    /**
     * 	Invalidate Document
     * 	@return true if success
     */
    public boolean invalidateIt() {
        log.info(toString());
        setDocAction(DOCACTION_None);
        return false;
    }	//	invalidateIt

    /**
     *	Prepare Document
     * 	@return new status (In Progress or Invalid)
     */
    public String prepareIt() {
        log.info(toString());
        setDocAction(DOCACTION_Complete);
        return DocAction.STATUS_InProgress;
    }	//	prepareIt

    /**
     * 	Approve Document
     * 	@return true if success
     */
    public boolean approveIt() {
        log.info(toString());
        setIsApproved(true);
        return true;
    }	//	approveIt

    /**
     * 	Reject Approval
     * 	@return true if success
     */
    public boolean rejectIt() {
        log.info(toString());
        return true;
    }	//	rejectIt

    /**
     * 	Complete Document
     * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
     */
    public String completeIt() {
        if (this.isActive()) {
            String sql = "SELECT C_ValorPago_ID, C_MovimientoConciliacion_Id "
                    + " FROM C_MOVIMIENTOCONCILIACION "
                    + " WHERE C_BankAccount_Id = ? AND C_CONCILIACIONBANCARIA_ID =? AND TIPO='E' AND IsActive = 'Y' AND CONCILIADO = 'Y'";

            try {
                PreparedStatement pstmt = DB.prepareStatement(sql, null);

                pstmt.setInt(1, getC_BankAccount_ID());
                pstmt.setInt(2, getC_ConciliacionBancaria_ID());

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    MVALORPAGO vpay = new MVALORPAGO(getCtx(), rs.getInt(1), get_TrxName());

                    //TODO PASAR A CONSTANTE
                    vpay.setEstado("D");

                    vpay.save();

                    MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION(getCtx(), rs.getInt(2), get_TrxName());

                    //TODO PASAR A CONSTANTE
                    movConc.setEstado("Debitado");

                    movConc.save();
                }

            } catch (Exception e) {
                m_processMsg = "Error al cambiar el estado del Cheque";
                return DocAction.STATUS_Invalid;
            }

            setProcessed(true);
            setDocAction(DOCACTION_Close);
            return DocAction.STATUS_Completed;
        } else {
            m_processMsg = "La Conciliacion debe estar en estado Activo para poder Completarse.";
            return DocAction.STATUS_Invalid;
        }
    }

    /**
     * 	Void Document
     * 	@return true if success
     */
    public boolean voidIt() {
        log.info(toString());
        setDocAction(DOCACTION_None);
        return false;
    }	//	voidIt

    /**
     * 	Close Document
     * 	@return true if success
     */
    public boolean closeIt() {
        log.info(toString());
        setDocAction(DOCACTION_None);
        return true;
    }	//	closeIt

    /**
     * 	Reverse Correction
     * 	@return true if success
     */
    public boolean reverseCorrectIt() {
        log.info(toString());
        setDocAction(DOCACTION_None);
        return false;
    }	//	reverseCorrectionIt

    /**
     * 	Reverse Accrual
     * 	@return true if success
     */
    public boolean reverseAccrualIt() {
        log.info(toString());
        setDocAction(DOCACTION_None);
        return false;
    }	//	reverseAccrualIt

    /**
     * 	Re-activate
     * 	@return true if success
     */
    public boolean reActivateIt() {
        this.isReactivate  = true;
        int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
        int[] roleid = MRole.getAllIDs(MRole.Table_Name, " name = 'Panalab Admin' ", get_TrxName());

        
        if (roleid.length > 0 && AD_Role_ID != roleid[0]) {
            m_processMsg = "Solo al rol Panalab Admin se le permite reactivar una conciliacion.";
            return false;
        }
        
        String sql = "SELECT C_ValorPago_ID, C_MovimientoConciliacion_Id "
                    + " FROM C_MOVIMIENTOCONCILIACION "
                    + " WHERE C_BankAccount_Id = ? AND C_CONCILIACIONBANCARIA_ID =? AND TIPO='E' AND IsActive = 'Y' AND CONCILIADO = 'Y'";

            try {
                PreparedStatement pstmt = DB.prepareStatement(sql, null);

                pstmt.setInt(1, getC_BankAccount_ID());
                pstmt.setInt(2, getC_ConciliacionBancaria_ID());

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    MVALORPAGO vpay = new MVALORPAGO(getCtx(), rs.getInt(1), get_TrxName());
                    
                    //Paso cheques propios a estado impreso
                    vpay.setEstado("I");
                    vpay.save();

                    MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION(getCtx(), rs.getInt(2), get_TrxName());

                    //Seteo lineas de conciliacion de cheques propios a estado impreso
                    movConc.setEstado("Impreso");
                    movConc.save();
                }

            } catch (Exception e) {
                m_processMsg = "Error al cambiar el estado del Cheque";
                return false;
            }
                  
        setProcessed(false);
        setPosted(false);
        setIsApproved(false);
        setDocAction(DocAction.STATUS_Completed);

        return true;
    }	//	reActivateIt
    
    private String m_processMsg = null;

    /*************************************************************************
     * 	Get Summary
     *	@return Summary of Document
     */
    public String getSummary() {
        StringBuffer sb = new StringBuffer();
        sb.append(getConciliacion());
        //	: Total Lines = 123.00 (#1)
        sb.append(": ").append("Movimiento Conciliado").append("=").append(getSaldoConciliado()).append(" (#").append(")");
        //	 - Description
        if (getDescription() != null && getDescription().length() > 0) {
            sb.append(" - ").append(getDescription());
        }
        return sb.toString();
    }	//	getSummary

    /**
     * 	Get Process Message
     *	@return clear text error message
     */
    public String getProcessMsg() {
        return m_processMsg;
    }	//	getProcessMsg

    /**
     * 	Get Document Owner (Responsible)
     *	@return AD_User_ID
     */
    public int getDoc_User_ID() {
        return getUpdatedBy();
    }	//	getDoc_User_ID

    /**
     * 	Get Document Approval Amount.
     * 	Statement Difference
     *	@return amount
     */
    /*	public BigDecimal getApprovalAmt()
    {
    return getStatementDifference();
    }	//	getApprovalAmt
     */
    /**
     * 	Get Document Currency
     *	@return C_Currency_ID
     */
    public int getC_Currency_ID() {
        //	MPriceList pl = MPriceList.get(getCtx(), getM_PriceList_ID());
        //	return pl.getC_Currency_ID();
        return 0;
    }	//	getC_Currency_ID

    public BigDecimal getApprovalAmt() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean beforeDelete() {

        try {
            int ConciliacionAnterior = 0;

            //Tomo la conciliación anterior.
            String sql = "SELECT C_CONCILIACIONBANCARIA_ID "
                    + " FROM C_CONCILIACIONBANCARIA "
                    + " WHERE C_BankAccount_Id = ? AND C_CONCILIACIONBANCARIA_ID !=? AND IsActive = 'Y' AND TODATE < ? AND DocStatus IN ('CO','CL')"
                    + " Order By TODATE desc";

            PreparedStatement pstmt = DB.prepareStatement(sql, null);

            pstmt.setInt(1, getC_BankAccount_ID());
            pstmt.setInt(2, getC_ConciliacionBancaria_ID());
            pstmt.setTimestamp(3, getFromDate());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                ConciliacionAnterior = rs.getInt(1);

                /*
                 *  Zynnia - 04/06/2012
                 *  Modificación para que tome todos los casos
                 *  JF
                 *
                 */

                // Tomo todos los movimientos no conciliados en la conciliación que estoy borrando

                sql = "SELECT C_MOVIMIENTOCONCILIACION_ID, REG_MOVIMIENTOFONDOS, C_VALORPAGO_ID, C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOCONCILIACION "
                        + "WHERE CONCILIADO = 'N' AND "
                        + "C_CONCILIACIONBANCARIA_ID = " + getC_ConciliacionBancaria_ID();

                pstmt = DB.prepareStatement(sql, null);
                rs = pstmt.executeQuery();

                PreparedStatement pstmt2 = null;
                ResultSet rs2 = null;

                while (rs.next()) {

                    if (rs.getInt(2) == 0) {

                        if (rs.getInt(3) == 0) {
                            sql = "SELECT C_MOVIMIENTOCONCILIACION_ID FROM C_MOVIMIENTOCONCILIACION WHERE CONCILIADO = 'N' AND C_CONCILIACIONBANCARIA_ID = ? AND C_PAYMENTVALORES_ID=?";
                            pstmt2 = DB.prepareStatement(sql, null);
                            pstmt2.setInt(1, ConciliacionAnterior);
                            pstmt2.setInt(2, rs.getInt(4));
                        } else {
                            sql = "SELECT C_MOVIMIENTOCONCILIACION_ID FROM C_MOVIMIENTOCONCILIACION WHERE CONCILIADO = 'N' AND C_CONCILIACIONBANCARIA_ID = ? AND C_VALORPAGO_ID=?";
                            pstmt2 = DB.prepareStatement(sql, null);
                            pstmt2.setInt(1, ConciliacionAnterior);
                            pstmt2.setInt(2, rs.getInt(3));
                        }

                    } else {

                        sql = "SELECT C_MOVIMIENTOCONCILIACION_ID FROM C_MOVIMIENTOCONCILIACION WHERE CONCILIADO = 'N' AND C_CONCILIACIONBANCARIA_ID = ? AND REG_MOVIMIENTOFONDOS=?";
                        pstmt2 = DB.prepareStatement(sql, null);
                        pstmt2.setInt(1, ConciliacionAnterior);
                        pstmt2.setInt(2, rs.getInt(2));

                    }

                    rs2 = pstmt2.executeQuery();

                    /**
                     *	Si existe un mismo movimiento pendiente anterior, se debe volver a habilitar.
                     *	Si no existe el movimiento en la conciliacion anterio,
                     */
                    if (rs2.next()) {
                        MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION(getCtx(), rs2.getInt(1), get_TrxName());
                        movConc.setOld(false);
                        movConc.save();
                    }



                }

                /*
                 *  Zynnia - 04/06/2012
                 *  Modificación para que tome todos los casos
                 *  JF
                 *
                 */

                // Tomo todos los movimientos conciliados en la conciliación que estoy borrando

                sql = "SELECT C_MOVIMIENTOCONCILIACION_ID, REG_MOVIMIENTOFONDOS, C_VALORPAGO_ID, C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOCONCILIACION "
                        + "WHERE CONCILIADO = 'Y' AND "
                        + "C_CONCILIACIONBANCARIA_ID = " + getC_ConciliacionBancaria_ID();

                pstmt = DB.prepareStatement(sql, null);
                rs = pstmt.executeQuery();

                while (rs.next()) {

                    if (rs.getInt(2) == 0) {

                        if (rs.getInt(3) == 0) {
                            sql = "SELECT C_MOVIMIENTOCONCILIACION_ID FROM C_MOVIMIENTOCONCILIACION WHERE CONCILIADO = 'N' AND C_CONCILIACIONBANCARIA_ID = ? AND C_PAYMENTVALORES_ID=?";
                            pstmt2 = DB.prepareStatement(sql, null);
                            pstmt2.setInt(1, ConciliacionAnterior);
                            pstmt2.setInt(2, rs.getInt(4));
                        } else {
                            sql = "SELECT C_MOVIMIENTOCONCILIACION_ID FROM C_MOVIMIENTOCONCILIACION WHERE CONCILIADO = 'N' AND C_CONCILIACIONBANCARIA_ID = ? AND C_VALORPAGO_ID=?";
                            pstmt2 = DB.prepareStatement(sql, null);
                            pstmt2.setInt(1, ConciliacionAnterior);
                            pstmt2.setInt(2, rs.getInt(3));
                        }

                    } else {

                        sql = "SELECT C_MOVIMIENTOCONCILIACION_ID FROM C_MOVIMIENTOCONCILIACION WHERE CONCILIADO = 'N' AND C_CONCILIACIONBANCARIA_ID = ? AND REG_MOVIMIENTOFONDOS=?";
                        pstmt2 = DB.prepareStatement(sql, null);
                        pstmt2.setInt(1, ConciliacionAnterior);
                        pstmt2.setInt(2, rs.getInt(2));

                    }

                    rs2 = pstmt2.executeQuery();

                    /**
                     *	Si existe un mismo movimiento pendiente anterior, se debe volver a habilitar.
                     */
                    if (rs2.next()) {
                        MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION(getCtx(), rs2.getInt(1), get_TrxName());
                        movConc.setOld(false);
                        movConc.save();
                    }

                }

            }
        } catch (SQLException e) {
        }

        if (!deleteMovConciliados()) {
            return false;
        }
        
        if (!deleteMovPendientes()) {
            return false;
        }
        
        if (!deleteMovPosteriores()) {
            return false;
        }

        return true;
    }

    public boolean deleteMovPosteriores() {
        String sql = " DELETE FROM C_MovimientoPosterior WHERE C_ConciliacionBancaria_ID=" + getC_ConciliacionBancaria_ID();
        
        int nro = DB.executeUpdate(sql, null);

        return nro >= 0;
    }

    public boolean deleteMovConciliados() {
        String sql = " DELETE FROM C_MOVIMIENTOCONCILIACION"
                + " WHERE C_ConciliacionBancaria_ID=" + getC_ConciliacionBancaria_ID()
                + "	 AND Conciliado = 'Y'";
        int nro = DB.executeUpdate(sql, get_TrxName());

       return nro >= 0;
    }

    public boolean deleteMovPendientes() {
        String sql = " DELETE FROM C_MOVIMIENTOCONCILIACION"
                + " WHERE C_ConciliacionBancaria_ID=" + getC_ConciliacionBancaria_ID()
                + "	 AND Conciliado = 'N'";
       int nro = DB.executeUpdate(sql, get_TrxName());

       return nro >= 0;
    }

    public List<MMOVIMIENTOCONCILIACION> getMovConciliados(boolean c) {
        List<MMOVIMIENTOCONCILIACION> listVal = new ArrayList<MMOVIMIENTOCONCILIACION>();
        try {

            String sql = "SELECT C_MOVIMIENTOCONCILIACION_ID "
                    + "FROM C_MOVIMIENTOCONCILIACION "
                    + "WHERE C_ConciliacionBancaria_ID = ? AND IsActive = 'Y'";
            if (c) {
                sql = sql + " AND Conciliado = 'Y'";
            } else {
                sql = sql + " AND Conciliado = 'N'";
            }

            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setLong(1, getC_ConciliacionBancaria_ID());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int C_MOVIMIENTOCONCILIACION_ID = rs.getInt(1);
                MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION(getCtx(), C_MOVIMIENTOCONCILIACION_ID, get_TrxName());
                listVal.add(movConc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        if (listVal.size() > 0) {
            return listVal;
        }

        return null;
    }	// getMovConciliados

    public List<MMOVIMIENTOPOSTERIOR> getMovPosteriores() {
        List<MMOVIMIENTOPOSTERIOR> listVal = new ArrayList<MMOVIMIENTOPOSTERIOR>();
        try {

            String sql = "SELECT C_MOVIMIENTOPOSTERIOR_ID "
                    + "FROM C_MOVIMIENTOPOSTERIOR "
                    + "WHERE C_ConciliacionBancaria_ID = ? AND IsActive = 'Y'";

            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setLong(1, getC_ConciliacionBancaria_ID());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int C_MOVIMIENTOPOSTERIOR_ID = rs.getInt(1);
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(getCtx(), C_MOVIMIENTOPOSTERIOR_ID, get_TrxName());
                listVal.add(movPost);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        if (listVal.size() > 0) {
            return listVal;
        }

        return null;
    }	// getMovPosteriores

    public BigDecimal getSaldoPendiente() {
        try {

            String sql = "SELECT COALESCE(sum(IMPORTE),0) "
                    + "FROM C_MOVIMIENTOCONCILIACION "
                    + "WHERE C_CONCILIACIONBANCARIA_ID = ? AND IsActive = 'Y' AND CONCILIADO = 'N'";

            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setLong(1, getC_ConciliacionBancaria_ID());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Env.ZERO;
        }
        return Env.ZERO;
    }

    public BigDecimal getSaldoConciliado() {
        try {

            String sql = "SELECT COALESCE(sum(IMPORTE),0) "
                    + "FROM C_MOVIMIENTOCONCILIACION "
                    + "WHERE C_CONCILIACIONBANCARIA_ID = ? AND IsActive = 'Y' AND CONCILIADO = 'Y'";

            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setLong(1, getC_ConciliacionBancaria_ID());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Env.ZERO;
        }
        return Env.ZERO;
    }
    public static int N = 1;
    public static int C = 2;
    public static int R = 3;
    public static int M = 4;
    public static int E = 5;
    public static int B = 6;
    public static int P = 7;
    public static int D = 8;
    public static int T = 9;
    public static int F = 10;
    public static int Z = 11;
    public static int H = 12;
    public static int S = 13;
    public static int K = 14;
    /**
     * Used for PC Banking an discriminate between Pagos (Y) y Emision (X)
     *
     * Zynnia - Alejandro Scot 17-03-2012
     */
    public static int X = 15;
    public static int Y = 16;
    /**
     *  Anexo para transferencia entre cuentas
     *
     *  Zynnia - Jose Fantasia 29-03-2012
     */
    public static int W = 17;
    /**
     *  Anexo para Cheques Propios Vencidos
     *
     *  Zynnia - Pablo Velazquez 09-01-2013
     */
    public static int V = 18;

    public static void completarMovPosteriores(int C_ConciliacionBancaria_ID) {
        MCONCILIACIONBANCARIA concBancaria = new MCONCILIACIONBANCARIA(Env.getCtx(), C_ConciliacionBancaria_ID, null);
        completarMovPosteriores(concBancaria);
    }

    public static void completarMovPosteriores(MCONCILIACIONBANCARIA concBancaria) {
        Timestamp tsTo = concBancaria.getToDate();

        /*
         *  Agregado para tipos dinamicos de movimientos de fondos
         *  Zynnia - 10/07/2012
         */

        try {
            // 1 of 9 <--> TIPO DE MOVIMIENTO: Movimiento de Efectivo (M) y Valores Negociados (N)
            String sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito, ba.C_BankAccount_Id, mf.tipo"
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
                    + " INNER JOIN C_BankAccount_Acct baa ON (baa.B_Asset_Acct = mfd.MV_DEBITO_ACCT)"
                    + " INNER JOIN C_BankAccount ba ON (baa.C_BankAccount_Id = ba.C_BankAccount_Id)"
                    + " WHERE (mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_EFECTIVO + "' OR "
                    + "        mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_NEGOCIADOS + "' OR "
                    + "        EXISTS (SELECT 1 "
                    + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                    + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                    + "                      (DEB_CUENTA_DEBITO = 'Y' OR DEB_DEPOSITO = 'Y'))) AND "
                    + "        ba.C_BankAccount_Id=? AND mf.datetrx > ? AND mf.DocStatus IN ('CO','CL') "
                    + " ORDER BY mfd.C_MovimientoFondos_Deb_Id";
            PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(concBancaria);
                movPost.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movPost.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movPost.setC_MovimientoFondos_ID(rs.getInt(1));
                movPost.setDocumentNo(rs.getString(2));
                movPost.setREG_MovimientoFondos(rs.getInt(3));
                movPost.setEfectivaDate(rs.getTimestamp(4));
                movPost.setAmt(rs.getBigDecimal(5));
                if (rs.getString(7).equals("N")) {
                    movPost.setTipo("N");
                    movPost.setMovimiento(getTexto(N));
                } else {
                    movPost.setTipo("M");
                    movPost.setMovimiento(getTexto(M));
                }
                movPost.setC_ValorPago_ID(0);
                movPost.setC_PaymentValores_ID(0);
                movPost.setC_Payment_ID(0);
                movPost.save();
            }
            rs.close();
            pstmt.close();

            //	2 of 9 <--> TIPO DE MOVIMIENTO: Transferencia Bancaria Cobro (Z)
            sql = " SELECT vp.C_PaymentValores_Id, vp.C_Payment_Id, p.documentno, p.datetrx, vp.importe, b.name"
                    + " FROM C_PaymentValores vp"
                    + " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
                    + " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
                    + "	WHERE vp.tipo='" + MPAYMENTVALORES.BANCO + "' AND p.docstatus IN ('CO','CL') AND vp.C_BankAccount_Id=? AND p.dateacct > ?";
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(concBancaria);
                movPost.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movPost.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movPost.setC_MovimientoFondos_ID(0);
                movPost.setDocumentNo(rs.getString(3));
                movPost.setREG_MovimientoFondos(0);
                movPost.setEfectivaDate(rs.getTimestamp(4));
                movPost.setAmt(rs.getBigDecimal(5));
                movPost.setC_ValorPago_ID(0);
                movPost.setC_PaymentValores_ID(rs.getInt(1));
                movPost.setC_Payment_ID(rs.getInt(2));
                movPost.setTipo("B");
                movPost.setMovimiento(getTexto(Z));
                movPost.setAFavor(rs.getString(6));
                movPost.save();
            }
            rs.close();
            pstmt.close();

            //	3 of 9 <--> TIPO DE MOVIMIENTO: Transferencia Bancaria Pago (F)
            sql = " SELECT vp.C_ValorPago_Id, vp.C_Payment_Id, p.documentno, p.datetrx, -vp.importe, b.name"
                    + " FROM C_ValorPago vp"
                    + " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
                    + " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
                    + " WHERE vp.C_BankAccount_Id=? AND vp.tipo='" + MVALORPAGO.BANCO + "' AND p.docstatus IN ('CO','CL') AND p.dateacct > ?";
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(concBancaria);
                movPost.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movPost.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movPost.setC_MovimientoFondos_ID(0);
                movPost.setDocumentNo(rs.getString(3));
                movPost.setREG_MovimientoFondos(0);
                movPost.setEfectivaDate(rs.getTimestamp(4));
                movPost.setAmt(rs.getBigDecimal(5));
                movPost.setC_ValorPago_ID(rs.getInt(1));
                movPost.setC_PaymentValores_ID(0);
                movPost.setC_Payment_ID(rs.getInt(2));
                movPost.setTipo("B");
                movPost.setMovimiento(getTexto(F));
                movPost.setAFavor(rs.getString(6));
                movPost.save();
            }
            rs.close();
            pstmt.close();

            //	4 of 9 <--> TIPO DE MOVIMIENTO: Emisión de Cheque Propio (E y C)
            sql = " SELECT vp.C_ValorPago_Id, vp.C_Payment_Id, p.documentno, mf.C_MovimientoFondos_Id, mf.documentno, vp.paymentdate, -vp.importe, vp.nrocheque, vp.favor, b.name, vp.STATE"
                    + " FROM C_ValorPago vp"
                    + " LEFT OUTER JOIN C_MovimientoFondos mf ON (mf.C_MovimientoFondos_Id=vp.C_MovimientoFondos_Id)"
                    + " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
                    + " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
                    + " WHERE vp.C_BankAccount_Id=? AND (vp.STATE = '" + MVALORPAGO.EMITIDO + "' OR vp.STATE = '" + MVALORPAGO.PENDIENTEDEBITO + "') AND vp.tipo='" + MVALORPAGO.CHEQUEPROPIO + "' AND (p.docstatus IN ('CO','CL') OR mf.DocStatus IN ('CO','CL')) "
                    + " AND (mf.datetrx > ? OR p.dateacct > ?)";
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            pstmt.setTimestamp(3, tsTo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(concBancaria);
                movPost.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movPost.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movPost.setC_MovimientoFondos_ID(rs.getInt(4));
                movPost.setREG_MovimientoFondos(0);
                movPost.setEfectivaDate(rs.getTimestamp(6));
                movPost.setAmt(rs.getBigDecimal(7));
                movPost.setC_ValorPago_ID(rs.getInt(1));
                movPost.setC_PaymentValores_ID(0);
                movPost.setC_Payment_ID(rs.getInt(2));
                if (rs.getInt(2) == 0) {
                    movPost.setMovimiento(getTexto(E));
                    movPost.setDocumentNo(rs.getString(5));
                } else {
                    movPost.setMovimiento(getTexto(C));
                    movPost.setDocumentNo(rs.getString(3));
                }
                movPost.setTipo("E");
                movPost.setNroCheque(rs.getString(8));
                movPost.setAFavor(rs.getString(10));
                if (rs.getString(11).equals("E")) {
                    movPost.setEstado("Emitido");
                }
                if (rs.getString(11).equals("P")) {
                    movPost.setEstado("Pendiente de Débito");
                }
                movPost.save();
            }
            rs.close();
            pstmt.close();

            //	5 of 9 <--> TIPO DE MOVIMIENTO: Cheque Propio Rechazado (P y H)
            sql = " SELECT vp.C_ValorPago_Id, vp.C_Payment_Id, p.documentno, mf.C_MovimientoFondos_Id, mf.documentno, vp.paymentdate, -vp.importe, vp.nrocheque, vp.favor, b.name"
                    + " FROM C_ValorPago vp"
                    + " LEFT OUTER JOIN C_MovimientoFondos mf ON (mf.C_MovimientoFondos_Id=vp.C_MovimientoFondos_Id)"
                    + " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
                    + " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
                    + " WHERE vp.C_BankAccount_Id=? AND vp.STATE = '" + MVALORPAGO.RECHAZADO + "' AND vp.tipo='" + MVALORPAGO.CHEQUEPROPIO + "' AND (p.docstatus IN ('CO','CL') OR mf.DocStatus IN ('CO','CL')) "
                    + " AND (mf.datetrx > ? OR p.dateacct > ?)";
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            pstmt.setTimestamp(3, tsTo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(concBancaria);
                movPost.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movPost.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movPost.setC_MovimientoFondos_ID(rs.getInt(4));
                movPost.setREG_MovimientoFondos(0);
                movPost.setEfectivaDate(rs.getTimestamp(6));
                movPost.setAmt(rs.getBigDecimal(7));
                movPost.setC_ValorPago_ID(rs.getInt(1));
                movPost.setC_PaymentValores_ID(0);
                movPost.setC_Payment_ID(rs.getInt(2));
                if (rs.getInt(2) == 0) {
                    movPost.setMovimiento(getTexto(H));
                    movPost.setDocumentNo(rs.getString(5));
                    movPost.setTipo("R");
                } else {
                    movPost.setMovimiento(getTexto(R));
                    movPost.setDocumentNo(rs.getString(3));
                    movPost.setTipo("P");
                }
                movPost.setNroCheque(rs.getString(8));
                movPost.setAFavor(rs.getString(10));
                movPost.setEstado("Rechazado");
                movPost.save();
            }
            rs.close();
            pstmt.close();

            //	6 of 9 <--> TIPO DE MOVIMIENTO: Transferencia Bancaria / Débito (B)
            sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfc.C_MovimientoFondos_Cre_Id, mf.datetrx, -mfc.credito"
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Cre mfc ON (mf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
                    + " WHERE (mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_TRANSFERENCIA + "' OR "
                    + "        EXISTS (SELECT 1 "
                    + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                    + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                    + "                      CRE_TRANSFERENCIA = 'Y')) AND "
                    + "        mfc.C_BankAccount_Id=? AND mf.datetrx > ? AND mf.DocStatus IN ('CO','CL')";
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(concBancaria);
                movPost.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movPost.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movPost.setC_MovimientoFondos_ID(rs.getInt(1));
                movPost.setDocumentNo(rs.getString(2));
                movPost.setREG_MovimientoFondos(rs.getInt(3));
                movPost.setEfectivaDate(rs.getTimestamp(4));
                movPost.setAmt(rs.getBigDecimal(5));
                movPost.setC_ValorPago_ID(0);
                movPost.setC_PaymentValores_ID(0);
                movPost.setC_Payment_ID(0);
                movPost.setTipo("B");
                movPost.setMovimiento(getTexto(B));
                movPost.save();
            }
            rs.close();
            pstmt.close();

            //	12 of 12 <--> TIPO DE MOVIMIENTO: Transferencia entre cuentas debito
            sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfc.C_MovimientoFondos_Cre_Id, mf.datetrx, -mfc.credito"
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Cre mfc ON (mf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
                    + " WHERE mf.TIPO = '" + MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias + "' AND mfc.C_BankAccount_Id=? AND mf.datetrx > ? AND mf.DocStatus IN ('CO','CL')";
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(concBancaria);
                movPost.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movPost.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movPost.setC_MovimientoFondos_ID(rs.getInt(1));
                movPost.setDocumentNo(rs.getString(2));
                movPost.setREG_MovimientoFondos(rs.getInt(3));
                movPost.setEfectivaDate(rs.getTimestamp(4));
                movPost.setAmt(rs.getBigDecimal(5));
                movPost.setC_ValorPago_ID(0);
                movPost.setC_PaymentValores_ID(0);
                movPost.setC_Payment_ID(0);
                movPost.setTipo("W");
                movPost.setMovimiento(getTexto(W));
                movPost.save();
            }
            rs.close();
            pstmt.close();

            //	13 of 13 <--> TIPO DE MOVIMIENTO: Transferencia entre cuentas credito
            sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito"
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
                    + " WHERE mf.TIPO = '" + MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias + "' AND mfd.C_BankAccount_Id=? AND mf.datetrx > ? AND mf.DocStatus IN ('CO','CL')";
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(concBancaria);
                movPost.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movPost.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movPost.setC_MovimientoFondos_ID(rs.getInt(1));
                movPost.setDocumentNo(rs.getString(2));
                movPost.setREG_MovimientoFondos(rs.getInt(3));
                movPost.setEfectivaDate(rs.getTimestamp(4));
                movPost.setAmt(rs.getBigDecimal(5));
                movPost.setC_ValorPago_ID(0);
                movPost.setC_PaymentValores_ID(0);
                movPost.setC_Payment_ID(0);
                movPost.setTipo("W");
                movPost.setMovimiento(getTexto(W));
                movPost.save();
            }
            rs.close();
            pstmt.close();


            //	7 of 9 <--> TIPO DE MOVIMIENTO: Rechazo de Cheques Propios (P)
            sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito, vp.C_ValorPago_Id, vp.nrocheque, vp.paymentdate, b.name, vp.C_Payment_id, mfc.favor "
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
                    + " INNER JOIN C_ValorPago vp ON (vp.C_ValorPago_id = mfd.C_ValorPago_id)"
                    + " LEFT OUTER JOIN C_Payment p ON (vp.C_Payment_id = p.C_Payment_id)"
                    + " LEFT OUTER JOIN C_BPartner b ON (b.C_BPartner_id = p.C_BPartner_id)"
                    + " LEFT OUTER JOIN C_MovimientoFondos vpmf ON (vp.C_MovimientoFondos_Id = vpmf.C_MovimientoFondos_Id)"
                    + " LEFT OUTER JOIN C_MovimientoFondos_Cre mfc ON (vpmf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
                    + " WHERE (mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_RECHAZO_PROPIOS + "' OR "
                    + "        EXISTS (SELECT 1 "
                    + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                    + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                    + "                      DEB_CHEQUE_PRO_RECH = 'Y')) AND "
                    + "        mf.DocStatus IN ('CO','CL') AND vp.STATE = '" + MVALORPAGO.RECHAZADO + "' AND "
                    + "        vp.C_BankAccount_Id=? AND mf.datetrx > ?";
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(concBancaria);
                movPost.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movPost.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movPost.setC_MovimientoFondos_ID(rs.getInt(1));
                movPost.setDocumentNo(rs.getString(2));
                movPost.setREG_MovimientoFondos(rs.getInt(3));
                movPost.setEfectivaDate(rs.getTimestamp(4));
                movPost.setAmt(rs.getBigDecimal(5));
                movPost.setC_ValorPago_ID(rs.getInt(6));
                movPost.setNroCheque(rs.getString(7));
                movPost.setC_PaymentValores_ID(0);
                movPost.setC_Payment_ID(rs.getInt(10));
                movPost.setAFavor(rs.getString(9));
                movPost.setTipo("P");
                movPost.setMovimiento(getTexto(P));
                movPost.setEstado("Rechazado");
                movPost.save();
            }
            rs.close();
            pstmt.close();

            //	8 of 9 <--> TIPO DE MOVIMIENTO: Depósito de Cheques (D)
            sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito"
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
                    + " WHERE (mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_DEPOSITO + "' OR "
                    + "        EXISTS (SELECT 1 "
                    + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                    + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                    + "                     (DEB_DEPOSITO = 'Y' OR DEB_CUENTA_BANCO = 'Y'))) AND "
                    + "        mf.DocStatus IN ('CO','CL') AND mfd.C_BankAccount_Id=? AND mf.datetrx > ? ";
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(concBancaria);
                movPost.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movPost.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movPost.setC_MovimientoFondos_ID(rs.getInt(1));
                movPost.setDocumentNo(rs.getString(2));
                movPost.setREG_MovimientoFondos(rs.getInt(3));
                movPost.setEfectivaDate(rs.getTimestamp(4));
                movPost.setAmt(rs.getBigDecimal(5));
                movPost.setC_ValorPago_ID(0);
                movPost.setC_PaymentValores_ID(0);
                movPost.setC_Payment_ID(0);
                movPost.setTipo("D");
                movPost.setMovimiento(getTexto(D));
                movPost.setEstado("Depositado");
                movPost.save();
            }
            rs.close();
            pstmt.close();

            //	9 of 9 <--> TIPO DE MOVIMIENTO: Rechazo de Cheques Terceros (T)
            sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, -mfd.debito"
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Cre mfc ON (mf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
                    + " INNER JOIN C_BankAccount_Acct baa ON (baa.B_Asset_Acct = mfc.MV_CREDITO_ACCT)"
                    + " INNER JOIN C_BankAccount ba ON (baa.C_BankAccount_Id = ba.C_BankAccount_Id)"
                    + " WHERE (mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_RECHAZO_TERCEROS + "' OR "
                    + "        EXISTS (SELECT 1 "
                    + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                    + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                    + "                      (DEB_CHEQUE_TER_RECH = 'Y' OR CRE_CHEQUE_TER_RECH = 'Y'))) AND "
                    + "        ba.C_BankAccount_Id=? AND mf.datetrx > ? AND mf.DocStatus IN ('CO','CL') "
                    + " ORDER BY mfc.C_MovimientoFondos_Cre_Id";
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(concBancaria);
                movPost.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movPost.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movPost.setC_MovimientoFondos_ID(rs.getInt(1));
                movPost.setDocumentNo(rs.getString(2));
                movPost.setREG_MovimientoFondos(rs.getInt(3));
                movPost.setEfectivaDate(rs.getTimestamp(4));
                movPost.setAmt(rs.getBigDecimal(5));
                movPost.setC_ValorPago_ID(0);
                movPost.setC_PaymentValores_ID(0);
                movPost.setC_Payment_ID(0);
                movPost.setTipo("T");
                movPost.setMovimiento(getTexto(T));
                movPost.setEstado("Rechazado");
                movPost.save();
            }
            rs.close();
            pstmt.close();

            // 10 of 11 <--> TIPO DE MOVIMIENTO: Depósitos Pendientes (Y) y Créditos Bancarios (Z)
            sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito, ba.C_BankAccount_Id, mf.tipo"
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
                    + " INNER JOIN C_BankAccount ba ON (ba.C_BankAccount_Id = mfd.C_BankAccount_Id)"
                    + " WHERE (mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_DEPOSITO_PENDIENTE + "' OR "
                    + "        mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_CREDITO_BANCARIO + "' OR "
                    + "        EXISTS (SELECT 1 "
                    + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                    + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                    + "                      (DEB_DEPOSITO_PEND = 'Y' OR DEB_CREDITO_BANCO = 'Y'))) AND ba.C_BankAccount_Id=? AND mf.datetrx > ? AND "
                    + "        mf.DocStatus IN ('CO','CL') "
                    + " ORDER BY mfd.C_MovimientoFondos_Deb_Id";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(concBancaria);
                movPost.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movPost.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movPost.setC_MovimientoFondos_ID(rs.getInt(1));
                movPost.setDocumentNo(rs.getString(2));
                movPost.setREG_MovimientoFondos(rs.getInt(3));
                movPost.setEfectivaDate(rs.getTimestamp(4));
                movPost.setAmt(rs.getBigDecimal(5));
                movPost.setC_ValorPago_ID(0);
                movPost.setC_PaymentValores_ID(0);
                movPost.setC_Payment_ID(0);
                if (rs.getString(7).equals("Y")) {
                    movPost.setTipo("S");
                    movPost.setMovimiento(getTexto(S));
                } else {
                    movPost.setTipo("K");
                    movPost.setMovimiento(getTexto(K));
                }
                movPost.save();
            }

            rs.close();
            pstmt.close();

            // 11 of 11 <--> TIPO DE MOVIMIENTO: Depósitos Pendientes (Y)
            sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfc.C_MovimientoFondos_Cre_Id, mf.datetrx, -mfc.credito, ba.C_BankAccount_Id, mf.tipo"
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Cre mfc ON (mf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
                    + " INNER JOIN C_BankAccount ba ON (ba.C_BankAccount_Id = mfc.C_BankAccount_Id)"
                    + " WHERE (mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_DEPOSITO_PENDIENTE + "' OR "
                    + "        EXISTS (SELECT 1 "
                    + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                    + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND CRE_DEPOSITO_PEND = 'Y')) AND "
                    + "        ba.C_BankAccount_Id=? AND mf.datetrx > ? AND mf.DocStatus IN ('CO','CL') "
                    + " ORDER BY mfc.C_MovimientoFondos_Cre_Id";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                MMOVIMIENTOPOSTERIOR movPost = new MMOVIMIENTOPOSTERIOR(concBancaria);
                movPost.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movPost.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movPost.setC_MovimientoFondos_ID(rs.getInt(1));
                movPost.setDocumentNo(rs.getString(2));
                movPost.setREG_MovimientoFondos(rs.getInt(3));
                movPost.setEfectivaDate(rs.getTimestamp(4));
                movPost.setAmt(rs.getBigDecimal(5));
                movPost.setC_ValorPago_ID(0);
                movPost.setC_PaymentValores_ID(0);
                movPost.setC_Payment_ID(0);
                movPost.setTipo("K");
                movPost.setMovimiento(getTexto(K));
                movPost.save();
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }	// completarMovPosteriores

    public static String getTexto(int constante) {
        String texto = null;
        if (constante == R || constante == C) {
            texto = "Cheque Propio Pagos";
        }
        /*
         *  Agregado para soportar transferencias entre cuentas
         *  Zynnia - 29/03/2012
         *
         */
        if (constante == W) {
            texto = "Transferencia entre cuentas";
        }
        if (constante == Y) {
            texto = "PC Banking Pagos";
        }
        if (constante == E || constante == H) {
            texto = "Emisión Cheque Propio";
        }
        if (constante == X) {
            texto = "Emisión PC Banking";
        }
        if (constante == N) {
            texto = "Valores Negociados";
        }
        if (constante == M) {
            texto = "Movimiento de Efectivo";
        }
        if (constante == B) {
            texto = "Transferencia / Débito";
        }
        if (constante == P) {
            texto = "Rechazo de Cheques Propios";
        }
        if (constante == D) {
            texto = "Depósito de Cheques";
        }
        if (constante == T) {
            texto = "Rechazo de Cheques Terceros";
        }
        if (constante == F) {
            texto = "Transferencia Bancaria Pago";
        }
        if (constante == Z) {
            texto = "Transferencia Bancaria Cobro";
        }
        if (constante == S) {
            texto = "Débitos Pendientes";
        }
        if (constante == K) {
            texto = "Créditos Bancarios";
        }
        if (constante == V) {
            texto = "Cheques Propios Vencidos";
        }

        return texto;
    }

    public void setSaldoAConciliar(BigDecimal inicial, BigDecimal conciliados, BigDecimal cierre) {
        setSaldoAConciliar(cierre.subtract(inicial).subtract(conciliados));
    }

    /**
     * Set Beginning Balance. Balance prior to any transactions
     */
    public void setSaldoCierre(BigDecimal AmountCierre) {

        MBankAccount account = new MBankAccount(Env.getCtx(), getC_BankAccount_ID(), null);

        /*
         *  Modificado para que haga el control de los decimales de presición (a dos decimales)
         *  Zynnia - José Fantasia
         *  19/03/2012
         *
         */

        MCurrency moneda = MCurrency.get(Env.getCtx(), account.getC_Currency_ID());
        if (moneda != null) {

            BigDecimal valueRound = AmountCierre.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            set_Value("AMOUNTCIERRE", valueRound);

        } else {
            set_Value("AMOUNTCIERRE", AmountCierre);
        }

    }

    /**
     * Set Beginning Balance. Balance prior to any transactions
     */
    public void setSaldoConciliado(BigDecimal AmountConciliado) {

        MBankAccount account = new MBankAccount(Env.getCtx(), getC_BankAccount_ID(), null);


        /*
         *  Modificado para que haga el control de los decimales de presición (a dos decimales)
         *  Zynnia - José Fantasia
         *  19/03/2012
         *
         */

        MCurrency moneda = MCurrency.get(Env.getCtx(), account.getC_Currency_ID());
        if (moneda != null) {

            BigDecimal valueRound = AmountConciliado.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            set_Value("AMOUNTCONCILIADO", valueRound);

        } else {
            set_Value("AMOUNTCONCILIADO", AmountConciliado);
        }

    }

    /**
     * Set Beginning Balance. Balance prior to any transactions
     */
    public void setSaldoAConciliar(BigDecimal AmountAConciliar) {

        MBankAccount account = new MBankAccount(Env.getCtx(), getC_BankAccount_ID(), null);

        /*
         *  Modificado para que haga el control de los decimales de presición (a dos decimales)
         *  Zynnia - José Fantasia
         *  19/03/2012
         *
         */

        MCurrency moneda = MCurrency.get(Env.getCtx(), account.getC_Currency_ID());
        if (moneda != null) {

            BigDecimal valueRound = AmountAConciliar.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            set_Value("AMOUNTACONCILIAR", valueRound);

        } else {
            set_Value("AMOUNTACONCILIAR", AmountAConciliar);
        }


    }

    /**
     * Set Beginning Balance. Balance prior to any transactions
     */
    public void setSaldoPendiente(BigDecimal AmountPendiente) {

        MBankAccount account = new MBankAccount(Env.getCtx(), getC_BankAccount_ID(), null);

        /*
         *  Modificado para que haga el control de los decimales de presición (a dos decimales)
         *  Zynnia - José Fantasia
         *  19/03/2012
         *
         */

        MCurrency moneda = MCurrency.get(Env.getCtx(), account.getC_Currency_ID());
        if (moneda != null) {

            BigDecimal valueRound = AmountPendiente.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            set_Value("AMOUNTPENDIENTE", valueRound);

        } else {
            set_Value("AMOUNTPENDIENTE", AmountPendiente);
        }

    }

    /**
     * Set Beginning Balance. Balance prior to any transactions
     */
    public void setSaldoContable(BigDecimal AmountRegContable) {

        MBankAccount account = new MBankAccount(Env.getCtx(), getC_BankAccount_ID(), null);

        /*
         *  Modificado para que haga el control de los decimales de presición (a dos decimales)
         *  Zynnia - José Fantasia
         *  19/03/2012
         *
         */

        MCurrency moneda = MCurrency.get(Env.getCtx(), account.getC_Currency_ID());
        if (moneda != null) {

            BigDecimal valueRound = AmountRegContable.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            set_Value("AMOUNTREGCONTABLE", valueRound);

        } else {
            set_Value("AMOUNTREGCONTABLE", AmountRegContable);
        }


    }

    /**
     * Set Beginning Balance. Balance prior to any transactions
     */
    public void setSaldoInicial(BigDecimal AmountInicial) {

        MBankAccount account = new MBankAccount(Env.getCtx(), getC_BankAccount_ID(), null);

        /*
         *  Modificado para que haga el control de los decimales de presición (a dos decimales)
         *  Zynnia - José Fantasia
         *  19/03/2012
         *
         */

        MCurrency moneda = MCurrency.get(Env.getCtx(), account.getC_Currency_ID());
        if (moneda != null) {

            BigDecimal valueRound = AmountInicial.setScale(moneda.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
            set_Value("AMOUNTINICIAL", valueRound);

        } else {
            set_Value("AMOUNTINICIAL", AmountInicial);
        }
    }

    public void refrescarSaldos() {
        BigDecimal saldoInicial = getSaldoInicial();
        BigDecimal saldoCierre = getSaldoCierre();
        BigDecimal saldoPendiente = getSaldoPendiente();
        BigDecimal saldoConciliado = getSaldoConciliado();


        setSaldoPendiente(saldoPendiente);
        setSaldoConciliado(saldoConciliado);
        setSaldoAConciliar(saldoInicial, saldoConciliado, saldoCierre);

        BigDecimal saldoContable = saldoCierre.add(saldoPendiente);
        setSaldoContable(saldoContable);

    }
}	//	MBankStatement
