/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
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
public class X_C_PAYMENTVALORES extends PO {
	/** Standard Constructor */
	public X_C_PAYMENTVALORES(Properties ctx, int C_PAYMENTVALORES_ID,
			String trxName) {
		super(ctx, C_PAYMENTVALORES_ID, trxName);
		/**
		 * if (C_PAYMENTVALORES_ID == 0) { setC_PAYMENTVALORES_ID (0);
		 * setC_Payment_ID (0); setIMPORTE (Env.ZERO); setTIPO (null); }
		 */
	}

	/** Load Constructor */
	public X_C_PAYMENTVALORES(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_PAYMENTVALORES */
	public static final String Table_Name = "C_PAYMENTVALORES";

	/** AD_Table_ID */
	public int Table_ID;

	protected KeyNamePair Model;

	/** Load Meta Data */
	protected POInfo initPO(Properties ctx) {
		POInfo info = initPO(ctx, Table_Name);
		Table_ID = info.getAD_Table_ID();
		Model = new KeyNamePair(Table_ID, Table_Name);
		return info;
	}

	protected BigDecimal accessLevel = new BigDecimal(3);

	/** AccessLevel 3 - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_PAYMENTVALORES[").append(
				get_ID()).append("]");
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
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set CONCEPTO */
	public void setCONCEPTO(String CONCEPTO) {
		if (CONCEPTO != null && CONCEPTO.length() > 255) {
			log.warning("Length > 255 - truncated");
			CONCEPTO = CONCEPTO.substring(0, 254);
		}
		set_Value("CONCEPTO", CONCEPTO);
	}

	/** Get CONCEPTO */
	public String getCONCEPTO() {
		return (String) get_Value("CONCEPTO");
	}

	/** Set C_PAYMENTVALORES_ID */
	public void setC_PAYMENTVALORES_ID(int C_PAYMENTVALORES_ID) {
		if (C_PAYMENTVALORES_ID < 1)
			throw new IllegalArgumentException(
					"C_PAYMENTVALORES_ID is mandatory.");
		set_ValueNoCheck("C_PAYMENTVALORES_ID",
				new Integer(C_PAYMENTVALORES_ID));
	}

	/** Get C_PAYMENTVALORES_ID */
	public int getC_PAYMENTVALORES_ID() {
		Integer ii = (Integer) get_Value("C_PAYMENTVALORES_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getC_PAYMENTVALORES_ID()));
	}

	/**
	 * Set Payment. Payment identifier
	 */
	public void setC_Payment_ID(int C_Payment_ID) {
		if (C_Payment_ID < 1)
			throw new IllegalArgumentException("C_Payment_ID is mandatory.");
		set_ValueNoCheck("C_Payment_ID", new Integer(C_Payment_ID));
	}

	/**
	 * Get Payment. Payment identifier
	 */
	public int getC_Payment_ID() {
		Integer ii = (Integer) get_Value("C_Payment_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Movimiento Fondos. Movimiento Fondos identifier
	 */
	public void setC_MOVIMIENTOFONDOS_ID(int C_MOVIMIENTOFONDOS_ID) {
		if (C_MOVIMIENTOFONDOS_ID < 0)
			throw new IllegalArgumentException(
					"C_MOVIMIENTOFONDOS_ID no puede ser negativo.");
		set_ValueNoCheck("C_MOVIMIENTOFONDOS_ID", new Integer(
				C_MOVIMIENTOFONDOS_ID));
	}

	public int getC_MOVIMIENTOFONDOS_ID() {
		Integer ii = (Integer) get_Value("C_MOVIMIENTOFONDOS_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Get Charge. Charge identifier
	 */
	public int getC_Charge_ID() {
		Integer ii = (Integer) get_Value("C_Charge_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set IMPORTE */
	public void setIMPORTE(BigDecimal IMPORTE) {
		if (IMPORTE == null)
			throw new IllegalArgumentException("IMPORTE is mandatory.");
		set_Value("IMPORTE", IMPORTE);
	}

	/** Get IMPORTE */
	public BigDecimal getIMPORTE() {
		BigDecimal bd = (BigDecimal) get_Value("IMPORTE");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** TIPO AD_Reference_ID=1000061 */
	public static final int TIPO_AD_Reference_ID = 1000061;

	/** Banco = B */
	public static final String TIPO_Banco = "B";

	/** Caja = C */
	public static final String TIPO_Caja = "C";

	/** Valores a depositar = D */
	public static final String TIPO_ValoresADepositar = "D";

	/** Moneda extranjera = M */
	public static final String TIPO_MonedaExtranjera = "M";

	/** Bono Obra Social = O */
	public static final String TIPO_BonoObraSocial = "O";

	/** Set TIPO */
	public void setTIPO(String TIPO) {
		if (TIPO == null)
			throw new IllegalArgumentException("TIPO is mandatory");
        MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), TIPO);
		if (TIPO.equals("B") || TIPO.equals("C") || TIPO.equals("D")
				|| TIPO.equals("M") || TIPO.equals("O") || dynMovFondos != null)
			;
		else
			throw new IllegalArgumentException("TIPO Invalid value - " + TIPO
					+ " - Reference_ID=1000061 - B - C - D - M - O");
//		if (TIPO.length() > 1) {
//			log.warning("Length > 1 - truncated");
//			TIPO = TIPO.substring(0, 0);
//		}
		set_Value("TIPO", TIPO);
	}

	/** Get TIPO */
	public String getTIPO() {
		return (String) get_Value("TIPO");
	}

	/** REQ-035 - Modificado por Daniel Gini. */

	/** Set SUCURSAL */
	public void setSucursal(String suc) {
		set_Value("SUCURSAL", suc);
	}

	public Integer getC_BankAccount_ID() {
		return (Integer) get_Value("C_BankAccount_ID");
	}

	public String getBank() {
		return (String) get_Value("BANCO");
	}

	public String getNroCheque() {
		return (String) get_Value("NROCHEQUE");
	}

	public String getTipoCheque() {
		return (String) get_Value("TIPOCHEQUE");
	}

	public String getClearing() {
		return (String) get_Value("CLEARING");
	}

	public String getCuitFirmante() {
		return (String) get_Value("CUITFIRM");
	}

	public String getSucursal() {
		return (String) get_Value("SUCURSAL");
	}

	public Timestamp getReleasedDate() {
		return (Timestamp) get_Value("REALEASEDDATE");
	}

	public Timestamp getPaymentDate() {
		return (Timestamp) get_Value("PAYMENTDATE");
	}

	public String getNroTransferencia() {
		return (String) get_Value("NROTRANSFERENCIA");
	}

	public Integer getMoneda() {
		return (Integer) get_Value("C_Currency_ID");
	}

	public String getEstado() {
		return (String) get_Value("STATE");
	}

	public void setEstado(String state) {
		set_Value("STATE", state);
	}

	public Timestamp getDebitoDate() {
		return (Timestamp) get_Value("DEBITODATE");
	}

	public void setDebitoDate(Timestamp ts) {
		set_Value("DEBITODATE", ts);
	}

	public Timestamp getVencimientoDate() {
		return (Timestamp) get_Value("VENCIMIENTODATE");
	}

	public void setVencimientoDate(Timestamp ts) {
		set_Value("VENCIMIENTODATE", ts);
	}

	/**
	 * FIN
	 */

	/** Set Convertido */
	public void setConvertido(BigDecimal Conversion) {
		set_Value("CONVERTIDO", Conversion);
	}

	/** Get Conversion */
	public BigDecimal getConvertido() {
		BigDecimal bd = (BigDecimal) get_Value("CONVERTIDO");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Convertido */
	public void setExtranjera(BigDecimal Extranjera) {
		set_Value("EXTRANJERA", Extranjera);
	}

	/** Get Conversion */
	public BigDecimal getExtranjera() {
		BigDecimal bd = (BigDecimal) get_Value("EXTRANJERA");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

}
