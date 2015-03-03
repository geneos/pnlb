/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BankStatementLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.921
 */
@SuppressWarnings("serial")
public class X_C_MOVIMIENTOPOSTERIOR extends PO {
	/** Standard Constructor */
	public X_C_MOVIMIENTOPOSTERIOR(Properties ctx,
			int C_MOVIMIENTOCONCILIACION_ID, String trxName) {
		super(ctx, C_MOVIMIENTOCONCILIACION_ID, trxName);
	}

	/** Load Constructor */
	public X_C_MOVIMIENTOPOSTERIOR(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BankStatementLine */
	public static final String Table_Name = "C_MOVIMIENTOPOSTERIOR";

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
		StringBuffer sb = new StringBuffer("X_C_MOVIMIENTOPOSTERIOR[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID <= 0)
			set_Value("C_BPartner_ID", null);
		else
			set_Value("C_BPartner_ID", new Integer(C_BPartner_ID));
	}

	/**
	 * Get Business Partner . Identifies a Business Partner
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Bank statement line. Line on a statement from this Bank
	 */
	public void setC_MOVIMIENTOPOSTERIOR_ID(int C_MOVIMIENTOPOSTERIOR_ID) {
		if (C_MOVIMIENTOPOSTERIOR_ID < 1)
			throw new IllegalArgumentException(
					"C_MOVIMIENTOPOSTERIOR_ID is mandatory.");
		set_ValueNoCheck("C_MOVIMIENTOPOSTERIOR_ID", new Integer(
				C_MOVIMIENTOPOSTERIOR_ID));
	}

	/**
	 * Get Bank statement line. Line on a statement from this Bank
	 */
	public int getC_MOVIMIENTOPOSTERIOR_ID() {
		Integer ii = (Integer) get_Value("C_MOVIMIENTOPOSTERIOR_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Bank Statement. Bank Statement of account
	 */
	public void setC_ConciliacionBancaria_ID(int C_ConciliacionBancaria_ID) {
		set_ValueNoCheck("C_CONCILIACIONBANCARIA_ID", new Integer(
				C_ConciliacionBancaria_ID));
	}

	/**
	 * Get Bank Statement. Bank Statement of account
	 */
	public int getC_ConciliacionBancaria_ID() {
		Integer ii = (Integer) get_Value("C_CONCILIACIONBANCARIA_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment. Payment identifier
	 */
	public void setC_Payment_ID(int C_Payment_ID) {
		set_Value("C_Payment_ID", new Integer(C_Payment_ID));
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
	 * Set Payment Values. Payment identifier
	 */
	public void setC_ValorPago_ID(int C_ValorPago_ID) {
		set_Value("C_VALORPAGO_ID", new Integer(C_ValorPago_ID));
	}

	/**
	 * Get Payment Values. Payment identifier
	 */
	public int getC_ValorPago_ID() {
		Integer ii = (Integer) get_Value("C_VALORPAGO_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment Values. Payment identifier
	 */
	public void setC_PaymentValores_ID(int C_PaymentValores_ID) {
		set_Value("C_PAYMENTVALORES_ID", new Integer(C_PaymentValores_ID));
	}

	/**
	 * Get Payment Values. Payment identifier
	 */
	public int getC_PaymentValores_ID() {
		Integer ii = (Integer) get_Value("C_PAYMENTVALORES_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment Values. Payment identifier
	 */
	public void setC_MovimientoFondos_ID(int C_MovimientoFondos_ID) {
		set_Value("C_MOVIMIENTOFONDOS_ID", new Integer(C_MovimientoFondos_ID));
	}

	/**
	 * Get Payment Values. Payment identifier
	 */
	public int getC_MovimientoFondos_ID() {
		Integer ii = (Integer) get_Value("C_MOVIMIENTOFONDOS_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Efectiva Date. Accounting Date
	 */
	public void setEfectivaDate(Timestamp EfectivaDate) {
		set_Value("EFECTIVADATE", EfectivaDate);
	}

	/**
	 * Get Efectiva Date. Accounting Date
	 */
	public Timestamp getEfectivaDate() {
		return (Timestamp) get_Value("EFECTIVADATE");
	}

	/**
	 * Set Vencimiento Date. Accounting Date
	 */
	public void setVencimientoDate(Timestamp VencimientoDate) {
		set_Value("VENCIMIENTODATE", VencimientoDate);
	}

	/**
	 * Get Vencimiento Date. Accounting Date
	 */
	public Timestamp getVencimientoDate() {
		return (Timestamp) get_Value("VENCIMIENTODATE");
	}

	/**
	 * Set Amount.
	 */
	public void setAmt(BigDecimal Amt) {
		set_Value("IMPORTE", Amt);
	}

	/**
	 * Get Amount.
	 */
	public BigDecimal getAmt() {
		BigDecimal bd = (BigDecimal) get_Value("IMPORTE");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set EFT Movement Type. Electronic Funds Transfer Transaction Type
	 */
	public void setMovimiento(String Movimiento) {
		if (Movimiento != null && Movimiento.length() > 30) {
			log.warning("Length > 30 - truncated");
			Movimiento = Movimiento.substring(0, 19);
		}
		set_Value("MOVIMIENTO", Movimiento);
	}

	/**
	 * Get EFT Movement Type. Electronic Funds Transfer Transaction Type
	 */
	public String getMovimiento() {
		return (String) get_Value("MOVIMIENTO");
	}

	public String getEstado() {
		return (String) get_Value("STATE");
	}

	public void setEstado(String state) {
		set_Value("STATE", state);
	}

	public String getDocumentNo() {
		return (String) get_Value("DocumentNo");
	}

	public void setDocumentNo(String documentNo) {
		set_Value("DocumentNo", documentNo);
	}

	public String getAFavor() {
		return (String) get_Value("FAVOR");
	}

	public void setAFavor(String favor) {
		set_Value("FAVOR", favor);
	}

	public String getNroCheque() {
		return (String) get_Value("NROCHEQUE");
	}

	public void setNroCheque(String nro) {
		set_Value("NROCHEQUE", nro);
	}

	public String getTipo() {
		return (String) get_Value("TIPO");
	}

	public void setTipo(String tipo) {
		set_Value("TIPO", tipo);
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

	/**
	 * Set Bank Account. Account at the Bank
	 */
	public void setC_BankAccount_ID(int C_BankAccount_ID) {
		if (C_BankAccount_ID < 1)
			throw new IllegalArgumentException("C_BankAccount_ID is mandatory.");
		set_Value("C_BankAccount_ID", new Integer(C_BankAccount_ID));
	}

	/**
	 * Get Bank Account. Account at the Bank
	 */
	public int getC_BankAccount_ID() {
		Integer ii = (Integer) get_Value("C_BankAccount_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public void setREG_MovimientoFondos(int REG_MovimientoFondos) {
		set_Value("REG_MOVIMIENTOFONDOS", new Integer(REG_MovimientoFondos));
	}

	/**
	 * Get Bank Account. Account at the Bank
	 */
	public int getREG_MovimientoFondos() {
		Integer ii = (Integer) get_Value("REG_MOVIMIENTOFONDOS");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

}
