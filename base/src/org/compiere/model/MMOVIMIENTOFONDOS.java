package org.compiere.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.util.logging.Level;
import javax.swing.JOptionPane;

import org.compiere.print.ReportEngine;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.ValueFormat;

public class MMOVIMIENTOFONDOS extends X_C_MOVIMIENTOFONDOS implements DocAction {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private boolean changeType = false;

    /**
     * ************************************************************************
     * Standard Constructor
     *
     * @param ctx                   context
     * @param C_MOVIMIENTOFONDOS_ID
     * @param trxName               rx name
     */
    public MMOVIMIENTOFONDOS(Properties ctx, int C_MOVIMIENTOFONDOS_ID, String trxName) {
        super(ctx, C_MOVIMIENTOFONDOS_ID, trxName);
        if (C_MOVIMIENTOFONDOS_ID == 0) {
            setDateTrx(new Timestamp(System.currentTimeMillis()));
            setDateAcct(new Timestamp(System.currentTimeMillis()));
            setDocAction(DOCACTION_Complete);	// CO
            setDocStatus(DOCSTATUS_Drafted);	// DR
            setPosted(false);
            setProcessed(false);
            setProcessing(false);
        }
    }
    public static String MOV_TRANSFERENCIA = "B";
    public static String MOV_CAMBIO_RECIBIDOS = "C";
    public static String MOV_DEPOSITO = "D";
    public static String MOV_EMISION_PROPIOS = "E";
    public static String MOV_EFECTIVO = "M";
    public static String MOV_NEGOCIADOS = "N";
    public static String MOV_RECHAZO_PROPIOS = "P";
    public static String MOV_RECHAZO_TERCEROS = "T";
    public static String MOV_TERCEROS_VENCIDOS = "V";
    public static String MOV_PROPIOS_VENCIDOS = "X";
    public static String MOV_DEPOSITO_PENDIENTE = "Y";
    public static String MOV_CREDITO_BANCARIO = "Z";
    public static String MOV_REINTEGRO_EXPORTACION = "R";

    /*
     * Agregado reintegro de expportacion @author Ezequiel Scott @ zynnia
     */
    private static String REFERENCE_NAME = "MovFondos";

    /**
     * Load Constructor
     *
     * @param ctx context
     * @param rs  result set record
     */
    public MMOVIMIENTOFONDOS(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }	//	MMOVIMIENTOFONDOS

    public ArrayList<MMOVIMIENTOFONDOSCRE> getC_MOVIMIENTOFONDOS_CRE_ID() {
        ArrayList<MMOVIMIENTOFONDOSCRE> cred = new ArrayList<MMOVIMIENTOFONDOSCRE>();
        try {
            String sql = "SELECT C_MOVIMIENTOFONDOS_CRE_ID "
                    + "FROM C_MOVIMIENTOFONDOS_CRE "
                    + "WHERE C_MOVIMIENTOFONDOS_ID = ?";

            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setLong(1, getC_MOVIMIENTOFONDOS_ID());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                if (cred == null) {
                    cred = new ArrayList<MMOVIMIENTOFONDOSCRE>();
                }
                MMOVIMIENTOFONDOSCRE c = new MMOVIMIENTOFONDOSCRE(getCtx(), rs.getInt(1), get_TrxName());
                cred.add(c);
            }
        } catch (Exception e) {
        }

        return cred;
    }

    public ArrayList<MMOVIMIENTOFONDOSDEB> getC_MOVIMIENTOFONDOS_DEB_ID() {
        ArrayList<MMOVIMIENTOFONDOSDEB> deb = new ArrayList<MMOVIMIENTOFONDOSDEB>();;
        try {
            String sql = "SELECT C_MOVIMIENTOFONDOS_DEB_ID "
                    + "FROM C_MOVIMIENTOFONDOS_DEB "
                    + "WHERE C_MOVIMIENTOFONDOS_ID = ?";

            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setLong(1, getC_MOVIMIENTOFONDOS_ID());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                if (deb == null) {
                    deb = new ArrayList<MMOVIMIENTOFONDOSDEB>();
                }
                MMOVIMIENTOFONDOSDEB d = new MMOVIMIENTOFONDOSDEB(getCtx(), rs.getInt(1), get_TrxName());
                deb.add(d);
            }
        } catch (Exception e) {
        }

        return deb;
    }

    private List getContabilidad() {
        List<MFactAcct> list = new ArrayList<MFactAcct>();

        String sql = "SELECT Fact_Acct_ID "
                + " FROM Fact_Acct "
                + " WHERE Record_ID=? AND AD_Table_ID=? ";

        try {
            PreparedStatement pstm = DB.prepareStatement(sql, get_TrxName());
            pstm.setInt(1, getC_MOVIMIENTOFONDOS_ID());
            pstm.setInt(2, get_Table_ID());
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                MFactAcct cont = new MFactAcct(getCtx(), rs.getInt(1), get_TrxName());
                list.add(cont);
            }
        } catch (SQLException e) {
        }

        return list;
    }

    protected void anularContabilidad() {
        //	Acct Reversed (this)
        List fAcct = getContabilidad();
        for (int i = 0; i < fAcct.size(); i++) {
            ((MFactAcct) fAcct.get(i)).delete(true, get_TrxName());
        }
    }

    /**
     * ***********************************************************************
     * Process document
     *
     * @param action document action
     * @return true if performed
     */
    public boolean processIt(String processAction) throws Exception {
        m_processMsg = null;
        DocumentEngine engine = new DocumentEngine(this, getDocStatus());
        return engine.processIt(processAction, getDocAction());
    }

    /**
     * Unlock Document.
     *
     * @return true if success
     */
    public boolean unlockIt() {
        log.info(toString());
        setProcessing(false);
        return true;
    }	//	unlockIt

    /**
     * Invalidate Document
     *
     * @return true if success
     */
    public boolean invalidateIt() {
        log.info(toString());
        setDocAction(DOCACTION_None);
        return false;
    }	//	invalidateIt

    /**
     * Prepare Document
     *
     * @return new status (In Progress or Invalid)
     */
    public String prepareIt() {
        log.info(toString());
        setDocAction(DOCACTION_Complete);
        return DocAction.STATUS_InProgress;
    }	//	prepareIt

    /**
     * Approve Document
     *
     * @return true if success
     */
    public boolean approveIt() {
        log.info(toString());
        setIsApproved(true);
        return true;
    }	//	approveIt

    /**
     * Reject Approval
     *
     * @return true if success
     */
    public boolean rejectIt() {
        log.info(toString());
        return true;
    }	//	rejectIt

    /**
     * Complete Document
     *
     * @return new status (Complete, In Progress, Invalid, Waiting ..)
     */
    public String completeIt() {
        /**
         * Agregado por DANIEL 23/01/2009 - REQ-040 (Parte 2).
         *
         * VERIFICACIÓN DE BALANCE
         */
        BigDecimal debito = getDebitoTotal();
        BigDecimal credito = getCreditoTotal();


        if (!credito.equals(debito)) {
            JOptionPane.showMessageDialog(null, "La contabilidad generada en esta ventana no balancea correctamente", "ERROR - Verificación de balance", JOptionPane.ERROR_MESSAGE);
            m_processMsg = "@NotBalanced@";
            return DocAction.STATUS_Invalid;
        }


        //	Implicit Approval
        if (!isApproved()) {
            approveIt();
        }

        //---------------------------------------------------------------------
        /**
         * CAMBIOS DE ESTADO - LOGICA DEPENDIENDO DEL TIPO DE MOVIMIENTO
         */
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        if (!MPeriod.isOpen(getCtx(), getDateTrx(), dt.getDocBaseType())) {
            m_processMsg = "@PeriodClosed@";
            setDocAction(DOCACTION_Complete);
            return DocAction.STATUS_Invalid;
        }

        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), getTIPO());
        /*
        if (TIPO_EmiCheque.equals(getTIPO())
        || dynMovFondos.isDEB_CUENTA_DEBITO()
        || dynMovFondos.isCRE_CHEQUE_PROPIO()) {
         */

        if (dynMovFondos == null) {

            if (TIPO_EmiCheque.equals(getTIPO())) {

                String sql = "SELECT C_MOVIMIENTOFONDOS_CRE_ID "
                        + "FROM C_MOVIMIENTOFONDOS_CRE "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);

                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());

                    ResultSet rs = pstmt.executeQuery();

                    //Modificaciones por errores de manejo de secuecias
                    //José Fantasia

                    // 1001052 es la secuencia de C_VALORPAGO

                    MSequence seq = new MSequence(getCtx(), 1001052, null);

                    while (rs.next()) {
                        MMOVIMIENTOFONDOSCRE mfcre = new MMOVIMIENTOFONDOSCRE(getCtx(), rs.getInt(1), get_TrxName());

                        char orden = 'N';
                        if (mfcre.isOrden() == true) {
                            orden = 'Y';
                        }


                        String RDate = ValueFormat.getFechaFormateada(mfcre.getReleasedDate());
                        String PDate = ValueFormat.getFechaFormateada(mfcre.getPaymentDate());
                        String CDate = ValueFormat.getFechaFormateada(getCreated());
                        String UDate = ValueFormat.getFechaFormateada(getUpdated());



                        String lugar = "";
                        if (mfcre.getReleasedLocation() != null) {
                            lugar = mfcre.getReleasedLocation();
                        }


                        //Se Obtiene numero de cheque
                        String unNumeroCheque = mfcre.getNroCheque();
                        int key = mfcre.getC_BankAccount_ID();
                        //Si el numero de cheque todavia no se genero, se genera.
                        if (unNumeroCheque.equals("00000000")) {
                            unNumeroCheque = generateCheck(key);
                            if (unNumeroCheque.equals("00000000")) {
                                JOptionPane.showMessageDialog(null, "Actualice Documento de Cuenta Bancaria, cuenta: " + key, "Chequera Completa", JOptionPane.INFORMATION_MESSAGE);
                                System.out.println("No se pudo generar numero de cheque para la cuenta: " + key);
                                return DocAction.STATUS_Drafted;
                            }
                            if (unNumeroCheque == null) {
                                JOptionPane.showMessageDialog(null, "No se pudo generar el numero de cheque, cuenta: " + key, "Número de Cheque Incorrecto", JOptionPane.INFORMATION_MESSAGE);
                                System.out.println("No se pudo generar numero de cheque para la cuenta: " + key);
                                return DocAction.STATUS_Drafted;
                            }
                            mfcre.setNroCheque(unNumeroCheque);
                            if (mfcre.save()) {
                                //Incremento Cheque
                                int nextNroCheque = Integer.valueOf(unNumeroCheque).intValue() + 1;
                                try {
                                    DB.executeUpdate("UPDATE C_BankAccountDoc SET CURRENTNEXT = " + nextNroCheque + " WHERE C_BankAccount_ID = " + key, null);
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, "No se pudo generar el siguiente numero de cheque para: " + nextNroCheque + ", cuenta: " + key, "Error al Actualizar", JOptionPane.INFORMATION_MESSAGE);
                                    System.out.println("No se pudo generar el siguiente numero de cheque para: " + nextNroCheque + ", cuenta: " + key);
                                    return DocAction.STATUS_Drafted;
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No se pudo actualizar Nro. de Cheque", "Error al Actualizar", JOptionPane.INFORMATION_MESSAGE);
                                System.out.println("No se pudo actualizar Nro. de Cheque.");
                                return DocAction.STATUS_Drafted;
                            }

                        }

                        sql = "INSERT INTO C_VALORPAGO "
                                + "(AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,C_ValorPago_Id,C_Payment_ID,C_MOVIMIENTOFONDOS_ID,TIPOMOV,NROCOMP,TIPO,C_BankAccount_Id,TIPOCHEQUE,NROCHEQUE,REALEASEDLOCATION,REALEASEDDATE,PAYMENTDATE,FAVOR,ORDEN,STATE,IMPORTE,Processed) "
                                + "VALUES (" + getAD_Client_ID() + "," + getAD_Org_ID() + ",'Y',to_date('" + CDate + "','ddmmyy')," + getCreatedBy() + ",to_date('" + UDate + "','ddmmyy')," + getUpdatedBy() + "," + seq.getCurrentNext() + ",0," + getC_MOVIMIENTOFONDOS_ID() + ",'" + getTIPO() + "','" + getDocumentNo() + "','P'," + mfcre.getC_BankAccount_ID() + ",'" + mfcre.getTipoCheque() + "','" + mfcre.getNroCheque() + "','" + lugar + "',to_date('" + RDate + "','ddmmyy'),to_date('" + PDate + "','ddmmyy'),'" + mfcre.getAFavor() + "','" + orden + "','E'," + mfcre.getCREDITO() + ",'Y')";

                        int n = DB.executeUpdate(sql, get_TrxName());

                        if (!(n < 0)) {
                            int next = seq.getCurrentNext();
                            seq.setCurrentNext(next + 1);
                            seq.save(get_TrxName());
                        } else {
                            return DocAction.STATUS_Invalid;
                        }

                    }

                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque";
                    return DocAction.STATUS_Invalid;
                }

            }

            //		if (TIPO_TraBancaria.equals(getTIPO()))	{

            //		}
            /*
            if (TIPO_RechCqPropio.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_PROPIO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (TIPO_RechCqPropio.equals(getTIPO())) {
                String sql = "SELECT C_VALORPAGO_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);

                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());

                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MVALORPAGO vpay = new MVALORPAGO(getCtx(), rs.getInt(1), get_TrxName());
                        vpay.setEstado(MVALORPAGO.RECHAZADO);
                        if (!vpay.save(get_TrxName())) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }
                    }

                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Rechazado";
                    return DocAction.STATUS_Invalid;
                }
            }

            /*
            if (TIPO_DepCheque.equals(getTIPO())
            || dynMovFondos.isDEB_CUENTA_BANCO()
            || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
             */
            if (TIPO_DepCheque.equals(getTIPO())) {
                String sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_CRE "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);

                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());

                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.DEPOSITADO);
                        if (!pval.save(get_TrxName())) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }

                    }

                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Depositado";
                    return DocAction.STATUS_Invalid;
                }
            }

            /*
            if (TIPO_RechCqTercero.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_TERCERO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (TIPO_RechCqTercero.equals(getTIPO())) {
                String sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.RECHAZADO);
                        if (!pval.save(get_TrxName())) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Rechazado";
                    return DocAction.STATUS_Invalid;
                }
            }

            /*
            if (TIPO_VencCqPropio.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_TERCERO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (TIPO_VencCqPropio.equals(getTIPO())) {
                String sql = "SELECT C_VALORPAGO_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MVALORPAGO vpay = new MVALORPAGO(getCtx(), rs.getInt(1), get_TrxName());
                        vpay.setEstado(MVALORPAGO.VENCIDO);
                        if (!vpay.save()) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Vencido";
                    return DocAction.STATUS_Invalid;
                }
            }

            /*
            if (TIPO_VencCqTercero.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_TERCERO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (TIPO_VencCqTercero.equals(getTIPO())) {
                String sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.VENCIDO);
                        if (!pval.save()) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Vencido";
                    return DocAction.STATUS_Invalid;
                }
            }

            /*
            if (TIPO_ValNegociados.equals(getTIPO())
            || dynMovFondos.isDEB_CUENTA_BANCO()
            || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
             */
            if (TIPO_ValNegociados.equals(getTIPO())) {

                String sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_CRE "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.DEPOSITADO);
                        if (!pval.save()) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Depositado";
                    return DocAction.STATUS_Invalid;
                }
            }

            /*
             * Anexo para la funcionalidad de mov de fondos por cesion de facturas Debe poner los cheques como depositados
             *
             * Zynnia 02/05/2012
             *
             */
            /*
            if (TIPO_CancelacionCesionFacturas.equals(getTIPO())
            || dynMovFondos.isDEB_CUENTA_DEBITO()
            || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
             */
            if (TIPO_CancelacionCesionFacturas.equals(getTIPO())) {
                String sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_CRE "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.DEPOSITADO);
                        if (!pval.save()) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Depositado";
                    return DocAction.STATUS_Invalid;
                }
            }
            /*
            if (TIPO_CambioCheque.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_REC()
            || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
             */
            if (TIPO_CambioCheque.equals(getTIPO())) {
                //--------------------------------------------------------
                // Débito
                String sql = "SELECT C_MOVIMIENTOFONDOS_DEB_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();

                    //Modificaciones por errores de manejo de secuecias
                    //José Fantasia

                    // 1000959 es la secuencia de C_PAYMENTVALORES
                    MSequence seq = new MSequence(getCtx(), 1000959, null);

                    while (rs.next()) {
                        MMOVIMIENTOFONDOSDEB mfdeb = new MMOVIMIENTOFONDOSDEB(getCtx(), rs.getInt(1), get_TrxName());
                        char terceros = 'N';
                        if (mfdeb.isTerceros() == true) {
                            terceros = 'Y';
                        }

                        String RDate = ValueFormat.getFechaFormateada(mfdeb.getReleasedDate());
                        String PDate = ValueFormat.getFechaFormateada(mfdeb.getPaymentDate());
                        String CDate = ValueFormat.getFechaFormateada(getCreated());
                        String UDate = ValueFormat.getFechaFormateada(getUpdated());

                        //MSequence seq = MSequence.get(getCtx(), "C_PAYMENTVALORES");

                        sql = "INSERT INTO C_PAYMENTVALORES "
                                + "(AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,C_PaymentValores_Id,C_Payment_ID,C_MOVIMIENTOFONDOS_ID,TIPOMOV,NROCOMP,TIPO,BANCO,TIPOCHEQUE,NROCHEQUE,REALEASEDDATE,PAYMENTDATE,TERCEROS,STATE,IMPORTE,CLEARING,CUITFIRM,Processed) "
                                + "VALUES (" + getAD_Client_ID() + "," + getAD_Org_ID() + ",'Y',to_date('" + CDate + "','ddmmyy')," + getCreatedBy() + ",to_date('" + UDate + "','ddmmyy')," + getUpdatedBy() + "," + seq.getCurrentNext() + ",0," + getC_MOVIMIENTOFONDOS_ID() + ",'" + getTIPO() + "','" + getDocumentNo() + "','Q','" + mfdeb.getBank() + "','" + mfdeb.getTipoCheque() + "','" + mfdeb.getNroCheque() + "',to_date('" + RDate + "','ddmmyy'),to_date('" + PDate + "','ddmmyy'),'" + terceros + "','" + MPAYMENTVALORES.CARTERA + "'," + mfdeb.getDEBITO() + ",'" + mfdeb.getClearing() + "','" + mfdeb.getCuitFirmante() + "','Y')";

                        int n = DB.executeUpdate(sql, get_TrxName());

                        if (!(n < 0)) {
                            int next = seq.getCurrentNext();
                            seq.setCurrentNext(next + 1);
                            seq.save(get_TrxName());

                        } else {
                            return DocAction.STATUS_Invalid;
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque";
                    return DocAction.STATUS_Invalid;
                }

                //--------------------------------------------------------
                // Credito
                sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_CRE "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        int idPayment = rs.getInt(1);
                        if (idPayment > 0) {
                            MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                            pval.setEstado(MPAYMENTVALORES.DEVUELTOCLIENTE);
                            if (!pval.save()) {
                                m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                                return DocAction.STATUS_Invalid;
                            }
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Devuelto al Cliente";
                    return DocAction.STATUS_Invalid;
                }

            }

        } else {


            if (dynMovFondos.isCRE_CHEQUE_PROPIO()) {
                String sql = "SELECT C_MOVIMIENTOFONDOS_CRE_ID "
                        + "FROM C_MOVIMIENTOFONDOS_CRE "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);

                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());

                    ResultSet rs = pstmt.executeQuery();

                    //Modificaciones por errores de manejo de secuecias
                    //José Fantasia

                    // 1001052 es la secuencia de C_VALORPAGO

                    MSequence seq = new MSequence(getCtx(), 1001052, null);

                    while (rs.next()) {
                        MMOVIMIENTOFONDOSCRE mfcre = new MMOVIMIENTOFONDOSCRE(getCtx(), rs.getInt(1), get_TrxName());

                        char orden = 'N';
                        if (mfcre.isOrden() == true) {
                            orden = 'Y';
                        }


                        String RDate = ValueFormat.getFechaFormateada(mfcre.getReleasedDate());
                        String PDate = ValueFormat.getFechaFormateada(mfcre.getPaymentDate());
                        String CDate = ValueFormat.getFechaFormateada(getCreated());
                        String UDate = ValueFormat.getFechaFormateada(getUpdated());



                        String lugar = "";
                        if (mfcre.getReleasedLocation() != null) {
                            lugar = mfcre.getReleasedLocation();
                        }

                        //Se Obtiene numero de cheque
                        String unNumeroCheque = mfcre.getNroCheque();
                        int key = mfcre.getC_BankAccount_ID();
                        //Si el numero de cheque todavia no se genero, se genera.
                        if (unNumeroCheque.equals("00000000")) {
                            unNumeroCheque = generateCheck(key);
                            if (unNumeroCheque.equals("00000000")) {
                                JOptionPane.showMessageDialog(null, "Actualice Documento de Cuenta Bancaria, cuenta: " + key, "Chequera Completa", JOptionPane.INFORMATION_MESSAGE);
                                System.out.println("No se pudo generar numero de cheque para la cuenta: " + key);
                                return DocAction.STATUS_Drafted;
                            }
                            if (unNumeroCheque == null) {
                                JOptionPane.showMessageDialog(null, "No se pudo generar el numero de cheque, cuenta: " + key, "Número de Cheque Incorrecto", JOptionPane.INFORMATION_MESSAGE);
                                System.out.println("No se pudo generar numero de cheque para la cuenta: " + key);
                                return DocAction.STATUS_Drafted;
                            }
                            mfcre.setNroCheque(unNumeroCheque);
                            if (mfcre.save()) {
                                //Incremento Cheque
                                int nextNroCheque = Integer.valueOf(unNumeroCheque).intValue() + 1;
                                try {
                                    DB.executeUpdate("UPDATE C_BankAccountDoc SET CURRENTNEXT = " + nextNroCheque + " WHERE C_BankAccount_ID = " + key, null);


                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, "No se pudo generar el siguiente numero de cheque para: " + nextNroCheque + ", cuenta: " + key, "Error al Actualizar", JOptionPane.INFORMATION_MESSAGE);
                                    System.out.println("No se pudo generar el siguiente numero de cheque para: " + nextNroCheque + ", cuenta: " + key);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No se pudo actualizar Nro. de Cheque", "Error al Actualizar", JOptionPane.INFORMATION_MESSAGE);
                                System.out.println("No se pudo actualizar Nro. de Cheque.");
                                return DocAction.STATUS_Drafted;
                            }

                        }



                        sql = "INSERT INTO C_VALORPAGO "
                                + "(AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,C_ValorPago_Id,C_Payment_ID,C_MOVIMIENTOFONDOS_ID,TIPOMOV,NROCOMP,TIPO,C_BankAccount_Id,TIPOCHEQUE,NROCHEQUE,REALEASEDLOCATION,REALEASEDDATE,PAYMENTDATE,FAVOR,ORDEN,STATE,IMPORTE,Processed) "
                                + "VALUES (" + getAD_Client_ID() + "," + getAD_Org_ID() + ",'Y',to_date('" + CDate + "','ddmmyy')," + getCreatedBy() + ",to_date('" + UDate + "','ddmmyy')," + getUpdatedBy() + "," + seq.getCurrentNext() + ",0," + getC_MOVIMIENTOFONDOS_ID() + ",'" + getTIPO() + "','" + getDocumentNo() + "','P'," + mfcre.getC_BankAccount_ID() + ",'" + mfcre.getTipoCheque() + "','" + mfcre.getNroCheque() + "','" + lugar + "',to_date('" + RDate + "','ddmmyy'),to_date('" + PDate + "','ddmmyy'),'" + mfcre.getAFavor() + "','" + orden + "','E'," + mfcre.getCREDITO() + ",'Y')";

                        int n = DB.executeUpdate(sql, get_TrxName());

                        if (!(n < 0)) {
                            int next = seq.getCurrentNext();
                            seq.setCurrentNext(next + 1);
                            seq.save(get_TrxName());
                        } else {
                            return DocAction.STATUS_Invalid;
                        }

                    }

                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque";
                    return DocAction.STATUS_Invalid;
                }

            }

            //		if (TIPO_TraBancaria.equals(getTIPO()))	{

            //		}
            /*
            if (TIPO_RechCqPropio.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_PROPIO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (dynMovFondos.isDEB_CHEQUE_PRO_RECH()) {
                String sql = "SELECT C_VALORPAGO_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);

                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());

                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MVALORPAGO vpay = new MVALORPAGO(getCtx(), rs.getInt(1), get_TrxName());
                        vpay.setEstado(MVALORPAGO.RECHAZADO);
                        if (!vpay.save(get_TrxName())) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }
                    }

                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Rechazado";
                    return DocAction.STATUS_Invalid;
                }
            }

            /*
            if (TIPO_DepCheque.equals(getTIPO())
            || dynMovFondos.isDEB_CUENTA_BANCO()
            || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
             */
            if (dynMovFondos.isCRE_CHEQUE_DEPO()) {
                String sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_CRE "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);

                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());

                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.DEPOSITADO);
                        if (!pval.save(get_TrxName())) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }

                    }

                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Depositado";
                    return DocAction.STATUS_Invalid;
                }
            }

            /*
            if (TIPO_RechCqTercero.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_TERCERO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (dynMovFondos.isDEB_CHEQUE_TER_RECH()) {
                String sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.RECHAZADO);
                        if (!pval.save(get_TrxName())) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Rechazado";
                    return DocAction.STATUS_Invalid;
                }
            }

            /*
            if (TIPO_VencCqPropio.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_TERCERO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (dynMovFondos.isDEB_CHEQUE_PRO_VENC()) {
                String sql = "SELECT C_VALORPAGO_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MVALORPAGO vpay = new MVALORPAGO(getCtx(), rs.getInt(1), get_TrxName());
                        vpay.setEstado(MVALORPAGO.VENCIDO);
                        if (!vpay.save()) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Vencido";
                    return DocAction.STATUS_Invalid;
                }
            }

            /*
            if (TIPO_VencCqTercero.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_TERCERO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (dynMovFondos.isDEB_CHEQUE_TER_VENC()) {
                String sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.VENCIDO);
                        if (!pval.save()) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Vencido";
                    return DocAction.STATUS_Invalid;
                }
            }

            /*
            if (TIPO_ValNegociados.equals(getTIPO())
            || dynMovFondos.isDEB_CUENTA_BANCO()
            || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
             */
            if (dynMovFondos.isCRE_VALORES_NEG()) {

                String sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_CRE "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.DEPOSITADO);
                        if (!pval.save()) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Depositado";
                    return DocAction.STATUS_Invalid;
                }
            }

            /*
             * Anexo para la funcionalidad de mov de fondos por cesion de facturas Debe poner los cheques como depositados
             *
             * Zynnia 02/05/2012
             *
             */
            /*
            if (TIPO_CancelacionCesionFacturas.equals(getTIPO())
            || dynMovFondos.isDEB_CUENTA_DEBITO()
            || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
             */
            if (dynMovFondos.isCRE_CANCEL_FACT()) {
                String sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_CRE "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.DEPOSITADO);
                        if (!pval.save()) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return DocAction.STATUS_Invalid;
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Depositado";
                    return DocAction.STATUS_Invalid;
                }
            }
            /*
            if (TIPO_CambioCheque.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_REC()
            || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
             */
            if (dynMovFondos.isDEB_CHEQUE_REC()) {
                //--------------------------------------------------------
                // Débito
                String sql = "SELECT C_MOVIMIENTOFONDOS_DEB_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();

                    //Modificaciones por errores de manejo de secuecias
                    //José Fantasia

                    // 1000959 es la secuencia de C_PAYMENTVALORES
                    MSequence seq = new MSequence(getCtx(), 1000959, null);

                    while (rs.next()) {
                        MMOVIMIENTOFONDOSDEB mfdeb = new MMOVIMIENTOFONDOSDEB(getCtx(), rs.getInt(1), get_TrxName());
                        char terceros = 'N';
                        if (mfdeb.isTerceros() == true) {
                            terceros = 'Y';
                        }

                        String RDate = ValueFormat.getFechaFormateada(mfdeb.getReleasedDate());
                        String PDate = ValueFormat.getFechaFormateada(mfdeb.getPaymentDate());
                        String CDate = ValueFormat.getFechaFormateada(getCreated());
                        String UDate = ValueFormat.getFechaFormateada(getUpdated());

                        //MSequence seq = MSequence.get(getCtx(), "C_PAYMENTVALORES");

                        sql = "INSERT INTO C_PAYMENTVALORES "
                                + "(AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,C_PaymentValores_Id,C_Payment_ID,C_MOVIMIENTOFONDOS_ID,TIPOMOV,NROCOMP,TIPO,BANCO,TIPOCHEQUE,NROCHEQUE,REALEASEDDATE,PAYMENTDATE,TERCEROS,STATE,IMPORTE,CLEARING,CUITFIRM,Processed) "
                                + "VALUES (" + getAD_Client_ID() + "," + getAD_Org_ID() + ",'Y',to_date('" + CDate + "','ddmmyy')," + getCreatedBy() + ",to_date('" + UDate + "','ddmmyy')," + getUpdatedBy() + "," + seq.getCurrentNext() + ",0," + getC_MOVIMIENTOFONDOS_ID() + ",'" + getTIPO() + "','" + getDocumentNo() + "','Q','" + mfdeb.getBank() + "','" + mfdeb.getTipoCheque() + "','" + mfdeb.getNroCheque() + "',to_date('" + RDate + "','ddmmyy'),to_date('" + PDate + "','ddmmyy'),'" + terceros + "','" + MPAYMENTVALORES.CARTERA + "'," + mfdeb.getDEBITO() + ",'" + mfdeb.getClearing() + "','" + mfdeb.getCuitFirmante() + "','Y')";

                        int n = DB.executeUpdate(sql, get_TrxName());

                        if (!(n < 0)) {
                            int next = seq.getCurrentNext();
                            seq.setCurrentNext(next + 1);
                            seq.save(get_TrxName());

                        } else {
                            return DocAction.STATUS_Invalid;
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque";
                    return DocAction.STATUS_Invalid;
                }

                //--------------------------------------------------------
                // Credito
                sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_CRE "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        int idPayment = rs.getInt(1);
                        if (idPayment > 0) {
                            MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                            pval.setEstado(MPAYMENTVALORES.DEVUELTOCLIENTE);
                            if (!pval.save()) {
                                m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                                return DocAction.STATUS_Invalid;
                            }
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Devuelto al Cliente";
                    return DocAction.STATUS_Invalid;
                }

            }

        }

        /**
         * INCREMENTO EN SECUENCIA SEGUN TIPO DE MOVIMIENTO
         */
        String sql = "SELECT DocNoSequence_ID "
                + "FROM C_DocType "
                + "WHERE C_DocType_ID = " + getC_DocType_ID();
        try {
            PreparedStatement pstm = DB.prepareStatement(sql, null);
            ResultSet rs = pstm.executeQuery();

            int AD_Sequence_ID = 0;

            if (rs.next()) {
                AD_Sequence_ID = rs.getInt(1);

                MSequence seq = new MSequence(getCtx(), AD_Sequence_ID, get_TrxName());

                setDocumentNo(Integer.toString(seq.getCurrentNext()));
                seq.setCurrentNext(seq.getCurrentNext() + 1);
                seq.save();

            } else {
                m_processMsg = "No Existe Secuencia para el Tipo de Movimiento";
                return DocAction.STATUS_Invalid;
            }
        } catch (Exception e) {
            m_processMsg = "Error buscando Secuencia para el Tipo de Movimiento";
            return DocAction.STATUS_Invalid;
        }

        setProcessed(true);
        setDocAction(DOCACTION_Close);
        return DocAction.STATUS_Completed;
    }

    /**
     * Void Document
     *
     * @return true if success
     */
    public boolean voidIt() {

        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        if (!MPeriod.isOpen(getCtx(), getDateTrx(), dt.getDocBaseType())) {
            m_processMsg = "@PeriodClosed@";
            setDocAction(DOCACTION_Close);
            return false;
        }

        /**
         * CAMBIOS DE ESTADO - LOGICA DEPENDIENDO DEL TIPO DE MOVIMIENTO
         */
        String sql = " SELECT DISTINCT mc.REG_MOVIMIENTOFONDOS "
                + " FROM C_MOVIMIENTOCONCILIACION mc "
                + " INNER JOIN C_CONCILIACIONBANCARIA cb ON (mc.C_CONCILIACIONBANCARIA_ID = cb.C_CONCILIACIONBANCARIA_ID) "
                + " WHERE cb.IsActive = 'Y' and cb.DocStatus in ('CO','CL') and mc.conciliado = 'Y' AND mc.C_MOVIMIENTOFONDOS_ID = ?";

        try {
            PreparedStatement pss = DB.prepareStatement(sql, null);
            pss.setInt(1, getC_MOVIMIENTOFONDOS_ID());
            ResultSet rstmt = pss.executeQuery();

            if (rstmt.next()) {
                m_processMsg = "No es posible anular el movimiento, se encuentra conciliado";
                return false;
            }
        } catch (Exception e) {
            m_processMsg = "Error al verificar las conciliaciones del movimiento.";
            return false;
        }

        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), getTIPO());
        /*
        if (TIPO_EmiCheque.equals(getTIPO())
        || dynMovFondos.isDEB_CUENTA_DEBITO()
        || dynMovFondos.isCRE_CHEQUE_PROPIO()) {
         */

        if (dynMovFondos == null) {

            if (TIPO_EmiCheque.equals(getTIPO())) {
                sql = " SELECT C_MOVIMIENTOFONDOS_CRE_ID "
                        + " FROM C_MOVIMIENTOFONDOS_CRE "
                        + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MMOVIMIENTOFONDOSCRE mfcre = new MMOVIMIENTOFONDOSCRE(getCtx(), rs.getInt(1), get_TrxName());
                        mfcre.setEstado(MVALORPAGO.ANULADO);
                        mfcre.setSaveForce(true);
                        mfcre.save(get_TrxName());

                        // ACTUALIZAR EL CHEQUE EN SI.
                        sql = " SELECT C_VALORPAGO_ID "
                                + " FROM C_VALORPAGO "
                                + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y' "
                                + "	 AND C_BankAccount_ID=? AND NROCHEQUE=?";

                        PreparedStatement ps = DB.prepareStatement(sql, null);
                        ps.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                        ps.setInt(2, mfcre.getC_BankAccount_ID());
                        ps.setString(3, mfcre.getNroCheque());
                        ResultSet rst = ps.executeQuery();

                        if (rst.next()) {
                            MVALORPAGO valpay = new MVALORPAGO(getCtx(), rst.getInt(1), get_TrxName());
                            valpay.setEstado(MVALORPAGO.ANULADO);
                            valpay.save(get_TrxName());
                        } else {
                            m_processMsg = "Error al pasar el estado del Cheque a Anulado";
                            return false;
                        }
                        // FIN
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al pasar el estado del Cheque a Anulado";
                    return false;
                }
            }

            /*
            if (TIPO_RechCqPropio.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_PROPIO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (TIPO_RechCqPropio.equals(getTIPO())) {
                sql = "SELECT C_VALORPAGO_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MVALORPAGO vpay = new MVALORPAGO(getCtx(), rs.getInt(1), get_TrxName());
                        vpay.setEstado(MVALORPAGO.EMITIDO);
                        vpay.save(get_TrxName());
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Emitido";
                    return false;
                }
            }

            /*
            if (TIPO_DepCheque.equals(getTIPO())
            || TIPO_ValNegociados.equals(getTIPO())
            || dynMovFondos.isDEB_CUENTA_BANCO()
            || dynMovFondos.isCRE_CHEQUE_TERCERO()
            || dynMovFondos.isDEB_CUENTA_BANCO()) {
             */
            if (TIPO_DepCheque.equals(getTIPO())
                    || TIPO_ValNegociados.equals(getTIPO())) {

                sql = " SELECT C_PAYMENTVALORES_ID "
                        + " FROM C_MOVIMIENTOFONDOS_CRE "
                        + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        if (!pval.getEstado().equals(MPAYMENTVALORES.DEPOSITADO)) {
                            m_processMsg = "Los cheques acreditados deben estar Depositados.";
                            return false;
                        }
                    }

                    rs.close();
                    rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.CARTERA);
                        pval.save(get_TrxName());
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a En Cartera";
                    return false;
                }
            }
            /*
            if (TIPO_RechCqTercero.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_TERCERO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (TIPO_RechCqTercero.equals(getTIPO())) {
                sql = " SELECT C_PAYMENTVALORES_ID "
                        + " FROM C_MOVIMIENTOFONDOS_DEB "
                        + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.DEPOSITADO);
                        pval.save(get_TrxName());
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Depositado";
                    return false;
                }
            }

            /*
            if (TIPO_VencCqPropio.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_TERCERO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (TIPO_VencCqPropio.equals(getTIPO())) {
                sql = " SELECT C_VALORPAGO_ID "
                        + " FROM C_MOVIMIENTOFONDOS_DEB "
                        + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MVALORPAGO vpay = new MVALORPAGO(getCtx(), rs.getInt(1), get_TrxName());
                        vpay.setEstado(MVALORPAGO.EMITIDO);
                        vpay.save(get_TrxName());
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Emitido";
                    return false;
                }
            }
            /*
            if (TIPO_VencCqTercero.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_TERCERO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (TIPO_VencCqTercero.equals(getTIPO())) {
                sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.CARTERA);
                        pval.save(get_TrxName());
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a En Cartera";
                    return false;
                }
            }

            /*
             * if (TIPO_ValNegociados.equals(getTIPO()))	{ sql = " SELECT C_PAYMENTVALORES_ID " + " FROM
             * C_MOVIMIENTOFONDOS_CRE " + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'"; try { PreparedStatement
             * pstmt = DB.prepareStatement(sql, null); pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID()); ResultSet rs =
             * pstmt.executeQuery();
             *
             * while (rs.next()) { MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(),rs.getInt(1),get_TrxName());
             * pval.setEstado(MPAYMENTVALORES.CARTERA); pval.save(get_TrxName()); } } catch(Exception e) { m_processMsg =
             * "Error al cambiar el estado del Cheque a En Cartera"; return false; } }
             */
            /*
            if (TIPO_CambioCheque.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_REC()
            || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
             */
            if (TIPO_CambioCheque.equals(getTIPO())) {
                //--------------------------------------------------------
                // Débito

                sql = "SELECT C_MOVIMIENTOFONDOS_DEB_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MMOVIMIENTOFONDOSDEB mfdeb = new MMOVIMIENTOFONDOSDEB(getCtx(), rs.getInt(1), get_TrxName());
                        if (!mfdeb.getEstado().equals("En Cartera")) {
                            m_processMsg = "Los cheques Debitados deben estar en Cartera.";
                            return false;
                        }
                    }

                    rs.close();
                    rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MMOVIMIENTOFONDOSDEB mfdeb = new MMOVIMIENTOFONDOSDEB(getCtx(), rs.getInt(1), get_TrxName());
                        //	ELIMINAR CHEQUES NUEVOS EN CARTERA.
                        sql = " SELECT C_PAYMENTVALORES_ID "
                                + " FROM C_PAYMENTVALORES "
                                + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y' "
                                + "	 AND BANCO=? AND NROCHEQUE=?";

                        PreparedStatement ps = DB.prepareStatement(sql, null);
                        ps.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                        ps.setString(2, mfdeb.getBank());
                        ps.setString(3, mfdeb.getNroCheque());
                        ResultSet rst = ps.executeQuery();

                        if (rst.next()) {
                            MPAYMENTVALORES valpay = new MPAYMENTVALORES(getCtx(), rst.getInt(1), get_TrxName());
                            valpay.delete(false, get_TrxName());
                        } else {
                            m_processMsg = "Error al eliminar el Cheque Nuevo";
                            return false;
                        }
                        // FIN
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a En Cartera";
                    return false;
                }

                //--------------------------------------------------------
                // Credito
                sql = " SELECT C_PAYMENTVALORES_ID "
                        + " FROM C_MOVIMIENTOFONDOS_CRE "
                        + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.CARTERA);
                        pval.save(get_TrxName());
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a En Cartera";
                    return false;
                }
            }

            /*
             * GENEOS - Pablo Velazquez
             * 24/06/2013
             * Se agrega el comportamiento para soportar cancelacion de movimientos de fondos
             * del tipo CancelacionCesionFacturas
             */
            if (TIPO_CancelacionCesionFacturas.equals(getTIPO())) {
                sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_CRE "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.CARTERA);
                        if (!pval.save()) {
                            m_processMsg = "No ha sido posible actualizar el estado del cheque.";
                            return false;
                        }
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a En Cartera";
                    return false;
                }
            }

        } else {

            if (dynMovFondos.isCRE_CHEQUE_PROPIO()) {
                sql = " SELECT C_MOVIMIENTOFONDOS_CRE_ID "
                        + " FROM C_MOVIMIENTOFONDOS_CRE "
                        + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MMOVIMIENTOFONDOSCRE mfcre = new MMOVIMIENTOFONDOSCRE(getCtx(), rs.getInt(1), get_TrxName());
                        mfcre.setEstado(MVALORPAGO.ANULADO);
                        mfcre.setSaveForce(true);
                        mfcre.save(get_TrxName());

                        // ACTUALIZAR EL CHEQUE EN SI.
                        sql = " SELECT C_VALORPAGO_ID "
                                + " FROM C_VALORPAGO "
                                + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y' "
                                + "	 AND C_BankAccount_ID=? AND NROCHEQUE=?";

                        PreparedStatement ps = DB.prepareStatement(sql, null);
                        ps.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                        ps.setInt(2, mfcre.getC_BankAccount_ID());
                        ps.setString(3, mfcre.getNroCheque());
                        ResultSet rst = ps.executeQuery();

                        if (rst.next()) {
                            MVALORPAGO valpay = new MVALORPAGO(getCtx(), rst.getInt(1), get_TrxName());
                            valpay.setEstado(MVALORPAGO.ANULADO);
                            valpay.save(get_TrxName());
                        } else {
                            m_processMsg = "Error al pasar el estado del Cheque a Anulado";
                            return false;
                        }
                        // FIN
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al pasar el estado del Cheque a Anulado";
                    return false;
                }
            }

            /*
            if (TIPO_RechCqPropio.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_PROPIO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (dynMovFondos.isDEB_CHEQUE_PRO_RECH()) {
                sql = "SELECT C_VALORPAGO_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MVALORPAGO vpay = new MVALORPAGO(getCtx(), rs.getInt(1), get_TrxName());
                        vpay.setEstado(MVALORPAGO.EMITIDO);
                        vpay.save(get_TrxName());
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Emitido";
                    return false;
                }
            }

            /*
            if (TIPO_DepCheque.equals(getTIPO())
            || TIPO_ValNegociados.equals(getTIPO())
            || dynMovFondos.isDEB_CUENTA_BANCO()
            || dynMovFondos.isCRE_CHEQUE_TERCERO()
            || dynMovFondos.isDEB_CUENTA_BANCO()) {
             */
            if (dynMovFondos.isCRE_CHEQUE_DEPO() || dynMovFondos.isCRE_VALORES_NEG()) {

                sql = " SELECT C_PAYMENTVALORES_ID "
                        + " FROM C_MOVIMIENTOFONDOS_CRE "
                        + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        if (!pval.getEstado().equals(MPAYMENTVALORES.DEPOSITADO)) {
                            m_processMsg = "Los cheques acreditados deben estar Depositados.";
                            return false;
                        }
                    }

                    rs.close();
                    rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.CARTERA);
                        pval.save(get_TrxName());
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a En Cartera";
                    return false;
                }
            }
            /*
            if (TIPO_RechCqTercero.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_TERCERO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (dynMovFondos.isDEB_CHEQUE_TER_RECH()) {
                sql = " SELECT C_PAYMENTVALORES_ID "
                        + " FROM C_MOVIMIENTOFONDOS_DEB "
                        + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.DEPOSITADO);
                        pval.save(get_TrxName());
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Depositado";
                    return false;
                }
            }

            /*
            if (TIPO_VencCqPropio.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_TERCERO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (dynMovFondos.isDEB_CHEQUE_PRO_VENC()) {
                sql = " SELECT C_VALORPAGO_ID "
                        + " FROM C_MOVIMIENTOFONDOS_DEB "
                        + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        MVALORPAGO vpay = new MVALORPAGO(getCtx(), rs.getInt(1), get_TrxName());
                        vpay.setEstado(MVALORPAGO.EMITIDO);
                        vpay.save(get_TrxName());
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a Emitido";
                    return false;
                }
            }
            /*
            if (TIPO_VencCqTercero.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_TERCERO()
            || dynMovFondos.isCRE_CUENTA_CREDITO()) {
             */
            if (dynMovFondos.isDEB_CHEQUE_TER_VENC()) {
                sql = "SELECT C_PAYMENTVALORES_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.CARTERA);
                        pval.save(get_TrxName());
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a En Cartera";
                    return false;
                }
            }

            /*
             * if (TIPO_ValNegociados.equals(getTIPO()))	{ sql = " SELECT C_PAYMENTVALORES_ID " + " FROM
             * C_MOVIMIENTOFONDOS_CRE " + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'"; try { PreparedStatement
             * pstmt = DB.prepareStatement(sql, null); pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID()); ResultSet rs =
             * pstmt.executeQuery();
             *
             * while (rs.next()) { MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(),rs.getInt(1),get_TrxName());
             * pval.setEstado(MPAYMENTVALORES.CARTERA); pval.save(get_TrxName()); } } catch(Exception e) { m_processMsg =
             * "Error al cambiar el estado del Cheque a En Cartera"; return false; } }
             */
            /*
            if (TIPO_CambioCheque.equals(getTIPO())
            || dynMovFondos.isDEB_CHEQUE_REC()
            || dynMovFondos.isCRE_CHEQUE_TERCERO()) {
             */
            if (dynMovFondos.isDEB_CHEQUE_REC()) {
                //--------------------------------------------------------
                // Débito

                sql = "SELECT C_MOVIMIENTOFONDOS_DEB_ID "
                        + "FROM C_MOVIMIENTOFONDOS_DEB "
                        + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MMOVIMIENTOFONDOSDEB mfdeb = new MMOVIMIENTOFONDOSDEB(getCtx(), rs.getInt(1), get_TrxName());
                        if (!mfdeb.getEstado().equals("En Cartera")) {
                            m_processMsg = "Los cheques Debitados deben estar en Cartera.";
                            return false;
                        }
                    }

                    rs.close();
                    rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MMOVIMIENTOFONDOSDEB mfdeb = new MMOVIMIENTOFONDOSDEB(getCtx(), rs.getInt(1), get_TrxName());
                        //	ELIMINAR CHEQUES NUEVOS EN CARTERA.
                        sql = " SELECT C_PAYMENTVALORES_ID "
                                + " FROM C_PAYMENTVALORES "
                                + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y' "
                                + "	 AND BANCO=? AND NROCHEQUE=?";

                        PreparedStatement ps = DB.prepareStatement(sql, null);
                        ps.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                        ps.setString(2, mfdeb.getBank());
                        ps.setString(3, mfdeb.getNroCheque());
                        ResultSet rst = ps.executeQuery();

                        if (rst.next()) {
                            MPAYMENTVALORES valpay = new MPAYMENTVALORES(getCtx(), rst.getInt(1), get_TrxName());
                            valpay.delete(false, get_TrxName());
                        } else {
                            m_processMsg = "Error al eliminar el Cheque Nuevo";
                            return false;
                        }
                        // FIN
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a En Cartera";
                    return false;
                }

                //--------------------------------------------------------
                // Credito
                sql = " SELECT C_PAYMENTVALORES_ID "
                        + " FROM C_MOVIMIENTOFONDOS_CRE "
                        + " WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                    pstmt.setInt(1, getC_MOVIMIENTOFONDOS_ID());
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        MPAYMENTVALORES pval = new MPAYMENTVALORES(getCtx(), rs.getInt(1), get_TrxName());
                        pval.setEstado(MPAYMENTVALORES.CARTERA);
                        pval.save(get_TrxName());
                    }
                } catch (Exception e) {
                    m_processMsg = "Error al cambiar el estado del Cheque a En Cartera";
                    return false;
                }
            }

        }



        /**
         * ELIMINAR REGISTROS DE CONTABILIDAD
         */
        anularContabilidad();

        /*
         *  Zynnia 24/05/2012
         *  Anexado para eliminar los registros de movoimientos de concialición asociados
         *  ya que no deben aparecer para conciliar una vez anulados los movimientos.
         *
         */

        eliminarMovimientosConciliacion(getC_MOVIMIENTOFONDOS_ID());

        setPosted(true);

        log.info(toString());
        setDocAction(DOCACTION_None);
        return true;
    }	//	voidIt

    /**
     * Close Document
     *
     * @return true if success
     */
    public boolean closeIt() {
        log.info(toString());
        setDocAction(DOCACTION_None);
        return true;
    }	//	closeIt

    /**
     * Reverse Correction
     *
     * @return true if success
     */
    public boolean reverseCorrectIt() {
        log.info(toString());
        setDocAction(DOCACTION_None);
        return false;
    }	//	reverseCorrectionIt

    /**
     * Reverse Accrual
     *
     * @return true if success
     */
    public boolean reverseAccrualIt() {
        log.info(toString());
        setDocAction(DOCACTION_None);
        return false;
    }	//	reverseAccrualIt

    /**
     * Re-activate
     *
     * @return true if success
     */
    public boolean reActivateIt() {
        log.info(toString());
        return false;
    }	//	reActivateIt
    private String m_processMsg = null;

    /**
     * ***********************************************************************
     */
    /**
     * Set Processed
     *
     * @param processed Processed
     */
    public void setProcessed(boolean processed) {
        super.setProcessed(processed);
        if (get_ID() == 0) {
            return;
        }
        String sql = "UPDATE C_MovimientoFondos SET Processed='"
                + (processed ? "Y" : "N")
                + "' WHERE C_MovimientoFondos_ID=" + getC_MOVIMIENTOFONDOS_ID();
        int no = DB.executeUpdate(sql, get_TrxName());
        log.fine(processed + " - #" + no);
    }	//	setProcessed

    /**
     * Get Summary
     *
     * @return Summary of Document
     */
    public String getSummary() {
        return "";
    }

    /**
     * Get Document Info
     *
     * @return Type and Document No
     */
    public String getDocumentInfo() {
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        return dt.getName() + " " + getDocumentNo();
    }	//	getDocumentInfo

    /**
     * Create PDF
     *
     * @return file
     */
    public File createPDF() {
        try {
            File temp = File.createTempFile(get_TableName() + get_ID() + "_", ".pdf");
            return createPDF(temp);
        } catch (Exception e) {
            log.severe("Could not create PDF - " + e.getMessage());
        }
        return null;
    }	//	createPDF

    /**
     * Create PDF file
     *
     * @param file output file
     * @return file if success
     */
    public File createPDF(File file) {
        ReportEngine re = ReportEngine.get(getCtx(), ReportEngine.SHIPMENT, getC_MOVIMIENTOFONDOS_ID());
        if (re == null) {
            return null;
        }
        return re.getPDF(file);
    }	//	createPDF

    /**
     * Get Process Message
     *
     * @return clear text message
     */
    public String getProcessMsg() {
        return m_processMsg;
    }

    /**
     * Get Document Owner
     *
     * @return AD_User_ID
     */
    public int getDoc_User_ID() {
        return 0;
    }

    /**
     * Get Document Currency
     *
     * @return C_Currency_ID
     */
    @Override
    public int getC_Currency_ID() {
        if (super.getC_Currency_ID() == 0) {
            return Env.getContextAsInt(getCtx(), "$C_Currency_ID ");
        } else {
            return super.getC_Currency_ID();
        }
    }

    /**
     * Get Document Approval Amount
     *
     * @return amount
     */
    public BigDecimal getApprovalAmt() {
        return Env.ZERO;
    }	//	getApprovalAmt

    /**
     * ******************************************************
     *
     */

    /*
     * public boolean beforeSave(boolean newRecord) { String sql = "SELECT DocNoSequence_ID " + "FROM C_DocType " +
     * "WHERE C_DocType_ID = " + getC_DocType_ID(); try { PreparedStatement pstm = DB.prepareStatement(sql, null);
     * ResultSet rs = pstm.executeQuery();
     *
     * int AD_Sequence_ID = 0;
     *
     * if (rs.next()) { AD_Sequence_ID = rs.getInt(1);
     *
     * MSequence seq = new MSequence(getCtx(),AD_Sequence_ID,get_TrxName());
     *
     * setDocumentNo(Integer.toString(seq.getCurrentNext())); seq.setCurrentNext(seq.getCurrentNext() + 1); seq.save();
     *
     * }
     * else return false; } catch (Exception e){ return false; } return true; }
     */
    public boolean afterSave(boolean newRecord, boolean sucess) {

        //Actualizo conversiones (Credito) si el movimiento es de:
        // - transferencia entre cuentas
        if ( (MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias.equals(getTIPO()) )
                && (getDocStatus().equals(DOCSTATUS_Drafted)
                || getDocStatus().equals(DOCSTATUS_InProgress))) {

            ArrayList<MMOVIMIENTOFONDOSCRE> lineasCred = getC_MOVIMIENTOFONDOS_CRE_ID();
            for (MMOVIMIENTOFONDOSCRE aLine : lineasCred) {
                aLine.setConvertido(getC_Currency_ID(), getCotizacion());
                if (!aLine.save()) {
                    m_processMsg = "Error al actualizar convertido para linea credito: " + aLine;
                    return false;
                }
            }
        }
        
         //Actualizo conversiones (Debito) si el movimiento es de:
        // - transferencia entre cuentas
        // - credito bancario
        if ( (MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias.equals(getTIPO()) 
                || MMOVIMIENTOFONDOS.MOV_CREDITO_BANCARIO.equals(getTIPO())  )
                && (getDocStatus().equals(DOCSTATUS_Drafted)
                || getDocStatus().equals(DOCSTATUS_InProgress))) {

            ArrayList<MMOVIMIENTOFONDOSDEB> lineasDeb = getC_MOVIMIENTOFONDOS_DEB_ID();
            for (MMOVIMIENTOFONDOSDEB aLine : lineasDeb) {
                aLine.setConvertido(getC_Currency_ID(), getCotizacion());
                if (!aLine.save()) {
                    m_processMsg = "Error al actualizar convertido para linea debito: " + aLine;
                    return false;
                }
            }

        }

        if (sucess && isChange()) {
            int nro;

            String sql = "DELETE FROM C_MOVIMIENTOFONDOS_DEB "
                    + "WHERE C_MOVIMIENTOFONDOS_ID = " + getC_MOVIMIENTOFONDOS_ID();
            nro = DB.executeUpdate(sql, get_TrxName());
            if (nro < 0) {
                setChange(false);
                return false;
            }

            sql = "DELETE FROM C_MOVIMIENTOFONDOS_CRE "
                    + "WHERE C_MOVIMIENTOFONDOS_ID = " + getC_MOVIMIENTOFONDOS_ID();
            nro = DB.executeUpdate(sql, get_TrxName());
            if (nro < 0) {
                setChange(false);
                return false;
            }
        }
        return sucess;
    }

    public boolean beforeSave(boolean newRecord) {
        if (!newRecord) {
            String tipo = getTIPO();

            String sql = "SELECT TIPO "
                    + "FROM C_MOVIMIENTOFONDOS "
                    + "WHERE C_MOVIMIENTOFONDOS_ID = " + getC_MOVIMIENTOFONDOS_ID();
            try {
                PreparedStatement pstm = DB.prepareStatement(sql, null);
                ResultSet rs = pstm.executeQuery();

                if (rs.next()) {
                    setChange(false);
                    if (!tipo.equals(rs.getString(1))) {
                        setChange(true);
                    }
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    public String getStringTipo() {
        return getStringTipo(getTIPO());
    }

    public static String getStringTipo(String tipo) {
        //MReference refer;

        String sql = " SELECT rlt.name "
                + " FROM ad_reference r "
                + " inner join ad_ref_list rl ON (rl.ad_reference_id= r.ad_reference_id)"
                + " inner join ad_ref_list_trl rlt ON (rl.ad_ref_list_id= rlt.ad_ref_list_id)"
                + " WHERE r.name = ? and rl.value = ?";
        try {
            PreparedStatement pstm = DB.prepareStatement(sql, null);
            pstm.setString(1, REFERENCE_NAME);
            pstm.setString(2, tipo);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public void setChange(boolean change) {
        changeType = change;
    }

    public boolean isChange() {
        return changeType;
    }

    private void eliminarMovimientosConciliacion(int c_MOVIMIENTOFONDOS_ID) {
        String sql = "DELETE FROM C_MOVIMIENTOCONCILIACION where C_MOVIMIENTOCONCILIACION_ID in "
                + "(SELECT C_MOVIMIENTOCONCILIACION_ID FROM C_MOVIMIENTOCONCILIACION mc "
                + "INNER JOIN C_CONCILIACIONBANCARIA c on (mc.C_CONCILIACIONBANCARIA_ID = c.C_CONCILIACIONBANCARIA_ID) "
                + "WHERE mc.C_MOVIMIENTOFONDOS_ID = " + c_MOVIMIENTOFONDOS_ID + " AND c.DOCSTATUS = 'DR')";
        DB.executeUpdate(sql, null);
    }

    /* Devuelve el numero de cheque correspondiente para una cuenta bancaria.
     * "00000000" si la chequera esta completa
     *  null en caso de error
     */
    public String generateCheck(int bankAccountID) {
        //	Get Info from DB
        int key = bankAccountID;

        String sql = "SELECT CURRENTNEXT,HASTA FROM C_BankAccountDoc WHERE C_BankAccount_ID = ? and IsActive='Y'";

        try {

            PreparedStatement pstm = DB.prepareStatement(sql, null);
            pstm.setInt(1, key);

            ResultSet rs = pstm.executeQuery();

            //	Set Info to Tab
            if (rs.next()) {
                int next = rs.getInt(1);
                int to = rs.getInt(2);

                if (next <= to) {
                    String prefix = rs.getString(1);

                    switch (prefix.length()) {
                        case 1:
                            prefix = "0000000" + prefix;
                            break;
                        case 2:
                            prefix = "000000" + prefix;
                            break;
                        case 3:
                            prefix = "00000" + prefix;
                            break;
                        case 4:
                            prefix = "0000" + prefix;
                            break;
                        case 5:
                            prefix = "000" + prefix;
                            break;
                        case 6:
                            prefix = "00" + prefix;
                            break;
                        case 7:
                            prefix = "0" + prefix;
                            break;
                        case 8:
                            break;
                        default:
                            prefix = null;
                        //JOptionPane.showMessageDialog(null,"Número de Cheque Incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    return prefix;
                    // mTab.setValue("NROCHEQUE", prefix);
                } else {
                    return "00000000";
                    /*JOptionPane.showMessageDialog(null,"Actualice Documento de Cuenta Bancaria", "Chequera Completa", JOptionPane.INFORMATION_MESSAGE);
                    mTab.setValue("NROCHEQUE", "00000000");*/
                }
            } else {
                return "00000000";
                /*JOptionPane.showMessageDialog(null,"Actualice Documento de Cuenta Bancaria", "Chequera Completa", JOptionPane.INFORMATION_MESSAGE);					
                mTab.setValue("NROCHEQUE", "00000000");*/
            }
        } catch (Exception e) {
            return null;
        }

    }

    private BigDecimal getCreditoTotal() {
        BigDecimal credito = BigDecimal.ZERO;

        try {
            String field = "CREDITO";
            if (MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias.equals(getTIPO())
                    && getC_Currency_ID() != 118) {
                field = "convertido";
            }

            String sql = "SELECT COALESCE(SUM(" + field + "),0) "
                    + "FROM C_MOVIMIENTOFONDOS_CRE "
                    + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setLong(1, getC_MOVIMIENTOFONDOS_ID());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                credito = rs.getBigDecimal(1);
            }

        } catch (SQLException esql) {
            log.log(Level.SEVERE, "Error calculando Credito", esql);
        }
        return credito;
    }

    private BigDecimal getDebitoTotal() {
        BigDecimal debito = BigDecimal.ZERO;

        try {
            String field = "DEBITO";
            if ( (MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias.equals(getTIPO()) 
                  || MMOVIMIENTOFONDOS.TIPO_CreditoBancario.equals(getTIPO()))
                    && getC_Currency_ID() != 118) {
                field = "convertido";
            }

            String sql = "SELECT COALESCE(SUM(" + field + "),0) "
                    + "FROM C_MOVIMIENTOFONDOS_DEB "
                    + "WHERE C_MOVIMIENTOFONDOS_ID = ? AND IsActive = 'Y'";

            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setLong(1, getC_MOVIMIENTOFONDOS_ID());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                debito = rs.getBigDecimal(1);
            }

        } catch (SQLException esql) {
            log.log(Level.SEVERE, "Error calculando Debito", esql);
        }
        return debito;
    }
}
