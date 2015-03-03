/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_COBRANZA_LISTADO
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.89
 */
public class X_T_COBRANZA_LISTADO extends PO {
	/** Standard Constructor */
	public X_T_COBRANZA_LISTADO(Properties ctx, int T_COBRANZA_LISTADO_ID,
			String trxName) {
		super(ctx, T_COBRANZA_LISTADO_ID, trxName);
		/**
		 * if (T_COBRANZA_LISTADO_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_COBRANZA_LISTADO(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_COBRANZA_LISTADO */
	public static final String Table_Name = "T_COBRANZA_LISTADO";

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
		StringBuffer sb = new StringBuffer("X_T_COBRANZA_LISTADO[").append(
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

	/** Set COBRANZA_ID */
	public void setCOBRANZA_ID(int COBRANZA_ID) {
		if (COBRANZA_ID <= 0)
			set_Value("COBRANZA_ID", null);
		else
			set_Value("COBRANZA_ID", new Integer(COBRANZA_ID));
	}

	/** Get COBRANZA_ID */
	public int getCOBRANZA_ID() {
		Integer ii = (Integer) get_Value("COBRANZA_ID");
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

	/** Set FECHA_RET */
	public void setFECHA_RET(Timestamp FECHA_RET) {
		set_Value("FECHA_RET", FECHA_RET);
	}

	/** Get FECHA_RET */
	public Timestamp getFECHA_RET() {
		return (Timestamp) get_Value("FECHA_RET");
	}

	/** Set NRO */
	public void setNRO(String NRO) {
		if (NRO != null && NRO.length() > 255) {
			log.warning("Length > 255 - truncated");
			NRO = NRO.substring(0, 254);
		}
		set_Value("NRO", NRO);
	}

	/** Get NRO */
	public String getNRO() {
		return (String) get_Value("NRO");
	}

	/** Set NRO_RET */
	public void setNRO_RET(String NRO_RET) {
		if (NRO_RET != null && NRO_RET.length() > 400) {
			log.warning("Length > 400 - truncated");
			NRO_RET = NRO_RET.substring(0, 399);
		}
		set_Value("NRO_RET", NRO_RET);
	}

	/** Get NRO_RET */
	public String getNRO_RET() {
		return (String) get_Value("NRO_RET");
	}

	/** Set TIPO */
	public void setTIPO(String TIPO) {
		if (TIPO != null && TIPO.length() > 255) {
			log.warning("Length > 255 - truncated");
			TIPO = TIPO.substring(0, 254);
		}
		set_Value("TIPO", TIPO);
	}

	/** Get TIPO */
	public String getTIPO() {
		return (String) get_Value("TIPO");
	}

	/** Set TIPO_RET */
	public void setTIPO_RET(String TIPO_RET) {
		if (TIPO_RET != null && TIPO_RET.length() > 400) {
			log.warning("Length > 400 - truncated");
			TIPO_RET = TIPO_RET.substring(0, 399);
		}
		set_Value("TIPO_RET", TIPO_RET);
	}

	/** Get TIPO_RET */
	public String getTIPO_RET() {
		return (String) get_Value("TIPO_RET");
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

	/** Set TOTAL_RET */
	public void setTOTAL_RET(BigDecimal TOTAL_RET) {
		set_Value("TOTAL_RET", TOTAL_RET);
	}

	/** Get TOTAL_RET */
	public BigDecimal getTOTAL_RET() {
		BigDecimal bd = (BigDecimal) get_Value("TOTAL_RET");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
