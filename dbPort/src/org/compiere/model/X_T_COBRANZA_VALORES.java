/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_COBRANZA_VALORES
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.906
 */
public class X_T_COBRANZA_VALORES extends PO {
	/** Standard Constructor */
	public X_T_COBRANZA_VALORES(Properties ctx, int T_COBRANZA_VALORES_ID,
			String trxName) {
		super(ctx, T_COBRANZA_VALORES_ID, trxName);
		/**
		 * if (T_COBRANZA_VALORES_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_COBRANZA_VALORES(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_COBRANZA_VALORES */
	public static final String Table_Name = "T_COBRANZA_VALORES";

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
		StringBuffer sb = new StringBuffer("X_T_COBRANZA_VALORES[").append(
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

	/** Set CONCEPTO */
	public void setCONCEPTO(String CONCEPTO) {
		if (CONCEPTO != null && CONCEPTO.length() > 400) {
			log.warning("Length > 400 - truncated");
			CONCEPTO = CONCEPTO.substring(0, 399);
		}
		set_Value("CONCEPTO", CONCEPTO);
	}

	/** Get CONCEPTO */
	public String getCONCEPTO() {
		return (String) get_Value("CONCEPTO");
	}

	/** Set IMPORTE */
	public void setIMPORTE(BigDecimal IMPORTE) {
		set_Value("IMPORTE", IMPORTE);
	}

	/** Get IMPORTE */
	public BigDecimal getIMPORTE() {
		BigDecimal bd = (BigDecimal) get_Value("IMPORTE");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set TIPO */
	public void setTIPO(String TIPO) {
		if (TIPO != null && TIPO.length() > 200) {
			log.warning("Length > 200 - truncated");
			TIPO = TIPO.substring(0, 199);
		}
		set_Value("TIPO", TIPO);
	}

	/** Get TIPO */
	public String getTIPO() {
		return (String) get_Value("TIPO");
	}
}
