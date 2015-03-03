/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_LIBRO_IVA_VENTA2
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.015
 */
public class X_T_LIBRO_IVA_VENTA2 extends PO {
	/** Standard Constructor */
	public X_T_LIBRO_IVA_VENTA2(Properties ctx, int T_LIBRO_IVA_VENTA2_ID,
			String trxName) {
		super(ctx, T_LIBRO_IVA_VENTA2_ID, trxName);
		/**
		 * if (T_LIBRO_IVA_VENTA2_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_LIBRO_IVA_VENTA2(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_LIBRO_IVA_VENTA2 */
	public static final String Table_Name = "T_LIBRO_IVA_VENTA2";

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
		StringBuffer sb = new StringBuffer("X_T_LIBRO_IVA_VENTA2[").append(
				get_ID()).append("]");
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

	/** Set CONCEPTO */
	public void setCONCEPTO(String CONCEPTO) {
		if (CONCEPTO != null && CONCEPTO.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			CONCEPTO = CONCEPTO.substring(0, 3999);
		}
		set_Value("CONCEPTO", CONCEPTO);
	}

	/** Get CONCEPTO */
	public String getCONCEPTO() {
		return (String) get_Value("CONCEPTO");
	}

	/** Set EXENTO */
	public void setEXENTO(BigDecimal EXENTO) {
		set_Value("EXENTO", EXENTO);
	}

	/** Get EXENTO */
	public BigDecimal getEXENTO() {
		BigDecimal bd = (BigDecimal) get_Value("EXENTO");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set IVA_DEBITO */
	public void setIVA_DEBITO(BigDecimal IVA_DEBITO) {
		set_Value("IVA_DEBITO", IVA_DEBITO);
	}

	/** Get IVA_DEBITO */
	public BigDecimal getIVA_DEBITO() {
		BigDecimal bd = (BigDecimal) get_Value("IVA_DEBITO");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set IVA_PERCEPCION */
	public void setIVA_PERCEPCION(BigDecimal IVA_PERCEPCION) {
		set_Value("IVA_PERCEPCION", IVA_PERCEPCION);
	}

	/** Get IVA_PERCEPCION */
	public BigDecimal getIVA_PERCEPCION() {
		BigDecimal bd = (BigDecimal) get_Value("IVA_PERCEPCION");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set LIBRO_ID */
	public void setLIBRO_ID(int LIBRO_ID) {
		if (LIBRO_ID <= 0)
			set_Value("LIBRO_ID", null);
		else
			set_Value("LIBRO_ID", new Integer(LIBRO_ID));
	}

	/** Get LIBRO_ID */
	public int getLIBRO_ID() {
		Integer ii = (Integer) get_Value("LIBRO_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set NETO_GRABADO */
	public void setNETO_GRABADO(BigDecimal NETO_GRABADO) {
		set_Value("NETO_GRABADO", NETO_GRABADO);
	}

	/** Get NETO_GRABADO */
	public BigDecimal getNETO_GRABADO() {
		BigDecimal bd = (BigDecimal) get_Value("NETO_GRABADO");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set OTROS */
	public void setOTROS(BigDecimal OTROS) {
		set_Value("OTROS", OTROS);
	}

	/** Get OTROS */
	public BigDecimal getOTROS() {
		BigDecimal bd = (BigDecimal) get_Value("OTROS");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
