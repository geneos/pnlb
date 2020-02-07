/**
 * Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke
 */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_PAYMENTVALORES
 *
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.906
 */
public class X_C_VALORPAGO extends PO {

    /**
     * Standard Constructor
     */
    public X_C_VALORPAGO(Properties ctx, int C_VALORPAGO_ID, String trxName) {
        super(ctx, C_VALORPAGO_ID, trxName);
        /**
         * if (C_PAYMENTVALORES_ID == 0) { setC_PAYMENTVALORES_ID (0); setC_Payment_ID (0); setIMPORTE (Env.ZERO);
         * setTIPO (null); }
         */
    }

    /**
     * Load Constructor
     */
    public X_C_VALORPAGO(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
    /**
     * TableName=C_PAYMENTVALORES
     */
    public static final String Table_Name = "C_VALORPAGO";
    /**
     * AD_Table_ID
     */
    public int Table_ID;
    protected KeyNamePair Model;

    /**
     * Load Meta Data
     */
    protected POInfo initPO(Properties ctx) {
        POInfo info = initPO(ctx, Table_Name);
        Table_ID = info.getAD_Table_ID();
        Model = new KeyNamePair(Table_ID, Table_Name);
        return info;
    }
    protected BigDecimal accessLevel = new BigDecimal(3);

    /**
     * AccessLevel 3 - Client - Org
     */
    protected int get_AccessLevel() {
        return accessLevel.intValue();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("X_C_VALORPAGO[").append(get_ID()).append("]");
        return sb.toString();
    }

    /**
     * Set Processed. The document has been processed
     */
    public void setProcessed(boolean Processed) {
        set_Value("Processed", new Boolean(Processed));
    }

    /**
     * Get Processed. The document has been processed
     */
    public boolean isProcessed() {
        Object oo = get_Value("Processed");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /**
     * Set CONCEPTO
     */
    public void setCONCEPTO(String CONCEPTO) {
        if (CONCEPTO != null && CONCEPTO.length() > 255) {
            log.warning("Length > 255 - truncated");
            CONCEPTO = CONCEPTO.substring(0, 254);
        }
        set_Value("CONCEPTO", CONCEPTO);
    }

    /**
     * Get CONCEPTO
     */
    public String getCONCEPTO() {
        return (String) get_Value("CONCEPTO");
    }

    /**
     * Set C_VALORPAGO_ID
     */
    public void setgetC_VALORPAGO_ID(int C_VALORPAGO_ID) {
        if (C_VALORPAGO_ID < 1) {
            throw new IllegalArgumentException("C_VALORPAGO_ID is mandatory.");
        }
        set_ValueNoCheck("C_VALORPAGO_ID", new Integer(C_VALORPAGO_ID));
    }

    /**
     * Get C_VALORPAGO_ID
     */
    public int getC_VALORPAGO_ID() {
        Integer ii = (Integer) get_Value("C_VALORPAGO_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    public KeyNamePair getKeyNamePair() {
        return new KeyNamePair(get_ID(), String.valueOf(getC_VALORPAGO_ID()));
    }

    /**
     * Set Movimiento Fondos. Movimiento Fondos identifier
     */
    public void setC_MOVIMIENTOFONDOS_ID(int C_MOVIMIENTOFONDOS_ID) {
        if (C_MOVIMIENTOFONDOS_ID < 0) {
            throw new IllegalArgumentException(
                    "C_MOVIMIENTOFONDOS_ID no puede ser negativo.");
        }
        set_ValueNoCheck("C_MOVIMIENTOFONDOS_ID", new Integer(
                C_MOVIMIENTOFONDOS_ID));
    }

    /**
     * Get Payment. Payment identifier
     */
    public int getC_MOVIMIENTOFONDOS_ID() {
        Integer ii = (Integer) get_Value("C_MOVIMIENTOFONDOS_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Payment. Payment identifier
     */
    public void setC_Payment_ID(int C_Payment_ID) {
        if (C_Payment_ID < 0) {
            throw new IllegalArgumentException(
                    "C_Payment_ID no puede ser negativo.");
        }
        set_ValueNoCheck("C_Payment_ID", new Integer(C_Payment_ID));
    }

    /**
     * Get Payment. Payment identifier
     */
    public int getC_Payment_ID() {
        Integer ii = (Integer) get_Value("C_Payment_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set IMPORTE
     */
    public void setIMPORTE(BigDecimal IMPORTE) {
        if (IMPORTE == null) {
            throw new IllegalArgumentException("IMPORTE is mandatory.");
        }
        set_Value("IMPORTE", IMPORTE);
    }

    /**
     * Get IMPORTE
     */
    public BigDecimal getIMPORTE() {
        BigDecimal bd = (BigDecimal) get_Value("IMPORTE");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }

    /**
     * Set MEXTRANJERA
     */
    public void setMEXTRANJERA(BigDecimal MEXTRANJERA) {
        if (MEXTRANJERA == null) {
            throw new IllegalArgumentException("IMPORTE is mandatory.");
        }
        set_Value("MEXTRANJERA", MEXTRANJERA);
    }

    /**
     * Get MEXTRANJERA
     */
    public BigDecimal getMEXTRANJERA() {
        BigDecimal bd = (BigDecimal) get_Value("MEXTRANJERA");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }

    /**
     * Get C_PAYMENTVALORES_ID
     */
    public int getC_PAYMENTVALORES_ID() {
        Integer ii = (Integer) get_Value("C_PAYMENTVALORES_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Get Payment. Payment identifier
     */
    public int getC_CREDITCARD_ID() {
        Integer ii = (Integer) get_Value("C_CREDITCARD_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    public void setC_CREDITCARD_ID(int C_Creditcard_ID) {
        if (C_Creditcard_ID < 0) {
            throw new IllegalArgumentException(
                    "C_Creditcard_ID no puede ser negativo.");
        }
        set_ValueNoCheck("C_Creditcard_ID", new Integer(C_Creditcard_ID));
    }
    /**
     * TIPO AD_Reference_ID=1000061
     */
    public static final int TIPO_AD_Reference_ID = 1000061;
    /**
     * Transferencia Bancaria = T
     */
    public static final String TIPO_Transferencia = "T";
    /**
     * Cheque Propio = P
     */
    public static final String TIPO_ChequePropio = "P";
    /**
     * Cheque Recibido = R
     */
    public static final String TIPO_ChequeRecibido = "R";

    /**
     * Set TIPO
     */
    public void setTIPO(String TIPO) {
        if (TIPO == null) {
            throw new IllegalArgumentException("TIPO is mandatory");
        }
        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), TIPO);
        if (TIPO.equals("B") || TIPO.equals("C") || TIPO.equals("D") || TIPO.equals("Q")  || TIPO.equals("P")
                || TIPO.equals("M") || TIPO.equals("O") || dynMovFondos != null)
			; else {
            throw new IllegalArgumentException("TIPO Invalid value - " + TIPO
                    + " - Reference_ID=1000061 - B - C - D - M - O");
        }
//        if (TIPO.length() > 1) {
//            log.warning("Length > 1 - truncated");
//            TIPO = TIPO.substring(0, 0);
//        }
        set_Value("TIPO", TIPO);
    }

    /**
     * Get TIPO
     */
    public String getTIPO() {
        return (String) get_Value("TIPO");
    }

    /**
     * REQ-037 - Modificado por Daniel Gini.
     */
    public Integer getC_BankAccount_ID() {
        return (Integer) get_Value("C_BankAccount_ID");
    }

    public String getBank() {
        return (String) get_Value("BANCO");
    }

    public String getNroCheque() {
        return (String) get_Value("NROCHEQUE");
    }

    public void setNroCheque(String nro) {
        set_Value("NROCHEQUE", nro);
    }

    public String getTipoCheque() {
        return (String) get_Value("TIPOCHEQUE");
    }

    public Timestamp getReleasedDate() {
        return (Timestamp) get_Value("REALEASEDDATE");
    }

    public Timestamp getPaymentDate() {
        return (Timestamp) get_Value("PAYMENTDATE");
    }

    public Timestamp getDebitoDate() {
        return (Timestamp) get_Value("DEBITODATE");
    }

    public Timestamp getVencimientoDate() {
        return (Timestamp) get_Value("VENCIMIENTODATE");
    }

    public Timestamp getEntregaDate() {
        return (Timestamp) get_Value("ENTREGADATE");
    }

    public String getNroRecibo() {
        return (String) get_Value("NRORECIBO");
    }

    public String getNroTransferencia() {
        return (String) get_Value("NROTRANSFERENCIA");
    }
    
     /**
     * Set Numero Transferencia
     */
    public void setNroTransferencia(String nro) {
        set_Value("NROTRANSFERENCIA", nro);
    }

    public String getEstado() {
        return (String) get_Value("STATE");
    }

    public void setTipoCheque(String tipo) {
        set_Value("TIPOCHEQUE", tipo);
    }

    public void setC_BankAccount_ID(Integer bank) {
        set_Value("C_BankAccount_ID", bank);
    }

    public void setEntregaDate(Timestamp ts) {
        set_Value("ENTREGADATE", ts);
    }

    public void setDebitoDate(Timestamp ts) {
        set_Value("DEBITODATE", ts);
    }

    public void setVencimientoDate(Timestamp ts) {
        set_Value("VENCIMIENTODATE", ts);
    }

    public void setReleasedLocation(String location) {
        set_Value("REALEASEDLOCATION", location);
    }

    public void setReleasedDate(Timestamp ts) {
        set_Value("REALEASEDDATE", ts);
    }

    public void setPaymentDate(Timestamp ts) {
        set_Value("PAYMENTDATE", ts);
    }

    public void setNroRecibo(int nro) {
        set_Value("NRORECIBO", new Integer(nro));
    }

    public void setEstado(String state) {
        set_Value("STATE", state);
    }

    public void setAFavor(String favor) {
        set_Value("FAVOR", favor);
    }

    public void setOrden(boolean orden) {
        set_Value("ORDEN", new Boolean(orden));
    }

    public void setReadOnlyNR(boolean readonly) {
        set_Value("READONLYNR", new Boolean(readonly));
    }

    public void setReadOnlyED(boolean readonly) {
        set_Value("READONLYED", new Boolean(readonly));
    }

    public void setReadOnlyDD(boolean readonly) {
        set_Value("READONLYDD", new Boolean(readonly));
    }

    public void setReadOnlyVD(boolean readonly) {
        set_Value("READONLYVD", new Boolean(readonly));
    }

    public void setChange(boolean change) {
        set_Value("CHANGEVENCIMIENTO", new Boolean(change));
    }

    /**
     * Get Change Vencimiento. The document has been processed
     */
    public boolean isChange() {
        Object oo = get_Value("CHANGEVENCIMIENTO");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /**
     * Set Accounting Schema. Rules for accounting
     */
    public void setC_AcctSchema_ID(int C_AcctSchema_ID) {
        if (C_AcctSchema_ID <= 0) {
            set_Value("C_AcctSchema_ID", null);
        } else {
            set_Value("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
        }
    }
    
    /**
     * Get Accounting Schema. Rules for accounting
     */
    public int getC_AcctSchema_ID() {
        Integer ii = (Integer) get_Value("C_AcctSchema_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }
    /**
     * FIN
     */
}
