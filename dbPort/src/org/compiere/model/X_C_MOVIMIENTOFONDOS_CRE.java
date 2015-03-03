/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;

import org.compiere.util.*;

/**
 * Generated Model for C_MOVIMIENTOFONDOS
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.906
 */
public class X_C_MOVIMIENTOFONDOS_CRE extends PO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Standard Constructor */
	public X_C_MOVIMIENTOFONDOS_CRE(Properties ctx,
			int C_MOVIMIENTOFONDOS_CRE_ID, String trxName) {
		super(ctx, C_MOVIMIENTOFONDOS_CRE_ID, trxName);
	}

	/** Load Constructor */
	public X_C_MOVIMIENTOFONDOS_CRE(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_MOVIMIENTOFONDOS_CRE */
	public static final String Table_Name = "C_MOVIMIENTOFONDOS_CRE";

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

	public int getC_MOVIMIENTOFONDOS_CRE_ID() {
		Integer ii = (Integer) get_Value("C_MOVIMIENTOFONDOS_CRE_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set C_MOVIMIENTOFONDOS_ID */
	public void setC_MOVIMIENTOFONDOS_ID(int C_MOVIMIENTOFONDOS_ID) {
		if (C_MOVIMIENTOFONDOS_ID < 1)
			throw new IllegalArgumentException(
					"C_MOVIMIENTOFONDOS_ID is mandatory.");
		set_ValueNoCheck("C_MOVIMIENTOFONDOS_ID", new Integer(
				C_MOVIMIENTOFONDOS_ID));
	}

	/**
	 * Set Accounting Schema. Rules for accounting
	 */
	public void setC_AcctSchema_ID(int C_AcctSchema_ID) {
		if (C_AcctSchema_ID < 1)
			throw new IllegalArgumentException("C_AcctSchema_ID is mandatory.");
		set_ValueNoCheck("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
	}

	/**
	 * Get Accounting Schema. Rules for accounting
	 */
	public int getC_AcctSchema_ID() {
		Integer ii = (Integer) get_Value("C_AcctSchema_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public int getC_MOVIMIENTOFONDOS_ID() {
		Integer ii = (Integer) get_Value("C_MOVIMIENTOFONDOS_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	protected BigDecimal accessLevel = new BigDecimal(3);

	/** AccessLevel 3 - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	/** Transferencia Bancaria = B */
	public static final String TIPO_TraBancaria = "B";

	/** Deposito de Cheque = D */
	public static final String TIPO_DepCheque = "D";

	/** Emision de Cheque Propio = E */
	public static final String TIPO_EmiCheque = "E";

	/** Movimiento de Efectivo = M */
	public static final String TIPO_MovEfectivo = "M";

	/** Rechazo de Cheque Propio = P */
	public static final String TIPO_RechCqPropio = "P";

	/** Rechazo de Cheque de Terceros = T */
	public static final String TIPO_RechCqTercero = "T";

	/** Vencimiento de Cheque Propio = X */
	public static final String TIPO_VencCqPropio = "X";

	/** Vencimiento de Cheque de Terceros = V */
	public static final String TIPO_VencCqTercero = "V";

	/** Valores Negociados = N */
	public static final String TIPO_ValNegociados = "N";

	/** Cambio de Cheques Recibidos = C */
	public static final String TIPO_CambioCheque = "C";

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

	/** Set C_VALORPAGO_ID */
	public void setC_VALORPAGO_ID(int C_VALORPAGO_ID) {
		if (C_VALORPAGO_ID < 1)
			throw new IllegalArgumentException("C_VALORPAGO_ID is mandatory.");
		set_ValueNoCheck("C_VALORPAGO_ID", new Integer(C_VALORPAGO_ID));
	}

	/** Get C_VALORPAGO_ID */
	public int getC_VALORPAGO_ID() {
		Integer ii = (Integer) get_Value("C_VALORPAGO_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_VALORPAGO_ID()));
	}

	/** Set CREDITO */
	public void setCREDITO(BigDecimal CREDITO) {
		if (CREDITO == null)
			throw new IllegalArgumentException("CREDITO is mandatory.");
		set_Value("CREDITO", CREDITO);
	}

	/** Get CREDITO */
	public BigDecimal getCREDITO() {
		BigDecimal bd = (BigDecimal) get_Value("CREDITO");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	public void setNroCheque(String nro) {
		set_Value("NROCHEQUE", nro);
	}

	public Integer getMV_CREDITO_ACCT() {
		return (Integer) get_Value("MV_CREDITO_ACCT");
	}

	public void setMV_CREDITO_ACCT(int account) {
		if (account < 1)
			throw new IllegalArgumentException("MV_CREDITO_ACCT is mandatory.");
		set_ValueNoCheck("MV_CREDITO_ACCT", new Integer(account));
	}

	public void setC_BankAccount_ID(int C_BankAccount_ID) {
		set_ValueNoCheck("C_BankAccount_ID", new Integer(C_BankAccount_ID));
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

	public Timestamp getReleasedDate() {
		return (Timestamp) get_Value("REALEASEDDATE");
	}

	public Timestamp getPaymentDate() {
		return (Timestamp) get_Value("PAYMENTDATE");
	}

	public String getNroTransferencia() {
		return (String) get_Value("NROTRANSFERENCIA");
	}

	public String getAFavor() {
		return (String) get_Value("FAVOR");
	}

	public String getEstado() {
		return (String) get_Value("STATE");
	}

	public void setEstado(String state) {
		set_Value("STATE", state);
	}

	public String getDescription()
	{
		return (String)get_Value("DESCRIPTION");
	}
	
	public Timestamp getDebitoDate() {
		return (Timestamp) get_Value("DEBITODATE");
	}

	public void setDebitoDate(Timestamp ts) {
		set_Value("DEBITODATE", ts);
	}

	public boolean isOrden() {
		Object oo = get_Value("ORDEN");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	public String getReleasedLocation() {
		return (String) get_Value("REALEASEDLOCATION");
	}
	/**
	 * FIN
	 */

}
