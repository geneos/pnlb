/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_RET_IVA_COBRANZAS
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.171
 */
public class X_T_RET_IVA_COBRANZAS extends PO {
	/** Standard Constructor */
	public X_T_RET_IVA_COBRANZAS(Properties ctx, int T_RET_IVA_COBRANZAS_ID,
			String trxName) {
		super(ctx, T_RET_IVA_COBRANZAS_ID, trxName);
		/**
		 * if (T_RET_IVA_COBRANZAS_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_RET_IVA_COBRANZAS(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_RET_IVA_COBRANZAS */
	public static final String Table_Name = "T_RET_IVA_COBRANZAS";

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
		StringBuffer sb = new StringBuffer("X_T_RET_IVA_COBRANZAS[").append(
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

	/**
	 * Set Base. Calculation Base
	 */
	public void setBase(String Base) {
		if (Base != null && Base.length() > 1) {
			log.warning("Length > 1 - truncated");
			Base = Base.substring(0, 0);
		}
		set_Value("Base", Base);
	}

	/**
	 * Get Base. Calculation Base
	 */
	public String getBase() {
		return (String) get_Value("Base");
	}

	/** Set CLIENTE */
	public void setCLIENTE(String CLIENTE) {
		if (CLIENTE != null && CLIENTE.length() > 1000) {
			log.warning("Length > 1000 - truncated");
			CLIENTE = CLIENTE.substring(0, 999);
		}
		set_Value("CLIENTE", CLIENTE);
	}

	/** Get CLIENTE */
	public String getCLIENTE() {
		return (String) get_Value("CLIENTE");
	}

	/** Set COMPROBANTE */
	public void setCOMPROBANTE(String COMPROBANTE) {
		if (COMPROBANTE != null && COMPROBANTE.length() > 1000) {
			log.warning("Length > 1000 - truncated");
			COMPROBANTE = COMPROBANTE.substring(0, 999);
		}
		set_Value("COMPROBANTE", COMPROBANTE);
	}

	/** Get COMPROBANTE */
	public String getCOMPROBANTE() {
		return (String) get_Value("COMPROBANTE");
	}

	/** Set CUIT */
	public void setCUIT(String CUIT) {
		if (CUIT != null && CUIT.length() > 1000) {
			log.warning("Length > 1000 - truncated");
			CUIT = CUIT.substring(0, 999);
		}
		set_Value("CUIT", CUIT);
	}

	/** Get CUIT */
	public String getCUIT() {
		return (String) get_Value("CUIT");
	}

	/** Set FECHA */
	public void setFECHA(Timestamp FECHA) {
		set_Value("FECHA", FECHA);
	}

	/** Get FECHA */
	public Timestamp getFECHA() {
		return (Timestamp) get_Value("FECHA");
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

	/**
	 * Set Line ID. Transaction line ID (internal)
	 */
	public void setLine_ID(int Line_ID) {
		if (Line_ID <= 0)
			set_Value("Line_ID", null);
		else
			set_Value("Line_ID", new Integer(Line_ID));
	}

	/**
	 * Get Line ID. Transaction line ID (internal)
	 */
	public int getLine_ID() {
		Integer ii = (Integer) get_Value("Line_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set TIPO_RET */
	public void setTIPO_RET(String TIPO_RET) {
		if (TIPO_RET != null && TIPO_RET.length() > 1) {
			log.warning("Length > 1 - truncated");
			TIPO_RET = TIPO_RET.substring(0, 0);
		}
		set_Value("TIPO_RET", TIPO_RET);
	}

	/** Get TIPO_RET */
	public String getTIPO_RET() {
		return (String) get_Value("TIPO_RET");
	}
}
