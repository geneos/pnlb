/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_PAGO_LISTADO
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.093
 */
public class X_T_PAGO_LISTADO extends PO {
	/** Standard Constructor */
	public X_T_PAGO_LISTADO(Properties ctx, int T_PAGO_LISTADO_ID,
			String trxName) {
		super(ctx, T_PAGO_LISTADO_ID, trxName);
		/**
		 * if (T_PAGO_LISTADO_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_PAGO_LISTADO(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_PAGO_LISTADO */
	public static final String Table_Name = "T_PAGO_LISTADO";

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
		StringBuffer sb = new StringBuffer("X_T_PAGO_LISTADO[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Process Instance. Instance of the process
	 */
	public void setAD_PInstance_ID(int AD_PInstance_ID) {
		if (AD_PInstance_ID <= 0)
			set_Value("AD_PInstance_ID", null);
		else
			set_Value("AD_PInstance_ID", new Integer(AD_PInstance_ID));
	}

	/**
	 * Get Process Instance. Instance of the process
	 */
	public int getAD_PInstance_ID() {
		Integer ii = (Integer) get_Value("AD_PInstance_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Bank Account. Account at the Bank
	 */
	public void setC_BankAccount_ID(int C_BankAccount_ID) {
		if (C_BankAccount_ID <= 0)
			set_Value("C_BankAccount_ID", null);
		else
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

	/** Set FECHA */
	public void setFECHA(Timestamp FECHA) {
		set_Value("FECHA", FECHA);
	}

	/** Get FECHA */
	public Timestamp getFECHA() {
		return (Timestamp) get_Value("FECHA");
	}

	/** Set FORMAPAGO */
	public void setFORMAPAGO(String FORMAPAGO) {
		if (FORMAPAGO != null && FORMAPAGO.length() > 100) {
			log.warning("Length > 100 - truncated");
			FORMAPAGO = FORMAPAGO.substring(0, 99);
		}
		set_Value("FORMAPAGO", FORMAPAGO);
	}

	/** Get FORMAPAGO */
	public String getFORMAPAGO() {
		return (String) get_Value("FORMAPAGO");
	}

	/** Set NRO */
	public void setNRO(String NRO) {
		if (NRO != null && NRO.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			NRO = NRO.substring(0, 3999);
		}
		set_Value("NRO", NRO);
	}

	/** Get NRO */
	public String getNRO() {
		return (String) get_Value("NRO");
	}

	/** Set PAGO */
	public void setPAGO(BigDecimal PAGO) {
		set_Value("PAGO", PAGO);
	}

	/** Get PAGO */
	public BigDecimal getPAGO() {
		BigDecimal bd = (BigDecimal) get_Value("PAGO");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set TIPO */
	public void setTIPO(String TIPO) {
		if (TIPO != null && TIPO.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			TIPO = TIPO.substring(0, 3999);
		}
		set_Value("TIPO", TIPO);
	}

	/** Get TIPO */
	public String getTIPO() {
		return (String) get_Value("TIPO");
	}

	/** Set TOTAL */
	public void setTOTAL(BigDecimal TOTAL) {
		set_Value("TOTAL", TOTAL);
	}

	/** Get TOTAL */
	public BigDecimal getTOTAL() {
		BigDecimal bd = (BigDecimal) get_Value("TOTAL");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
