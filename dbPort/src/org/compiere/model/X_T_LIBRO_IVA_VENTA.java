/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_LIBRO_IVA_VENTA
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.0
 */
public class X_T_LIBRO_IVA_VENTA extends PO {
	/** Standard Constructor */
	public X_T_LIBRO_IVA_VENTA(Properties ctx, int T_LIBRO_IVA_VENTA_ID,
			String trxName) {
		super(ctx, T_LIBRO_IVA_VENTA_ID, trxName);
		/**
		 * if (T_LIBRO_IVA_VENTA_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_LIBRO_IVA_VENTA(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_LIBRO_IVA_VENTA */
	public static final String Table_Name = "T_LIBRO_IVA_VENTA";

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
		StringBuffer sb = new StringBuffer("X_T_LIBRO_IVA_VENTA[").append(
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

	/** Set CUIT */
	public void setCUIT(String CUIT) {
		if (CUIT != null && CUIT.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			CUIT = CUIT.substring(0, 3999);
		}
		set_Value("CUIT", CUIT);
	}

	/** Get CUIT */
	public String getCUIT() {
		return (String) get_Value("CUIT");
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

	/** Set FECHA */
	public void setFECHA(String FECHA) {
		if (FECHA != null && FECHA.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			FECHA = FECHA.substring(0, 3999);
		}
		set_Value("FECHA", FECHA);
	}

	/** Get FECHA */
	public String getFECHA() {
		return (String) get_Value("FECHA");
	}

	/** Set IVA_DEBITO_FISCAL */
	public void setIVA_DEBITO_FISCAL(BigDecimal IVA_DEBITO_FISCAL) {
		set_Value("IVA_DEBITO_FISCAL", IVA_DEBITO_FISCAL);
	}

	/** Get IVA_DEBITO_FISCAL */
	public BigDecimal getIVA_DEBITO_FISCAL() {
		BigDecimal bd = (BigDecimal) get_Value("IVA_DEBITO_FISCAL");
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

	/** Set NUMERO_COMPROBANTE */
	public void setNUMERO_COMPROBANTE(String NUMERO_COMPROBANTE) {
		if (NUMERO_COMPROBANTE != null && NUMERO_COMPROBANTE.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			NUMERO_COMPROBANTE = NUMERO_COMPROBANTE.substring(0, 3999);
		}
		set_Value("NUMERO_COMPROBANTE", NUMERO_COMPROBANTE);
	}

	/** Get NUMERO_COMPROBANTE */
	public String getNUMERO_COMPROBANTE() {
		return (String) get_Value("NUMERO_COMPROBANTE");
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

	/** Set RAZON_SOCIAL */
	public void setRAZON_SOCIAL(String RAZON_SOCIAL) {
		if (RAZON_SOCIAL != null && RAZON_SOCIAL.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			RAZON_SOCIAL = RAZON_SOCIAL.substring(0, 3999);
		}
		set_Value("RAZON_SOCIAL", RAZON_SOCIAL);
	}

	/** Get RAZON_SOCIAL */
	public String getRAZON_SOCIAL() {
		return (String) get_Value("RAZON_SOCIAL");
	}

	/** Set TEMPDATE */
	public void setTEMPDATE(Timestamp TEMPDATE) {
		set_Value("TEMPDATE", TEMPDATE);
	}

	/** Get TEMPDATE */
	public Timestamp getTEMPDATE() {
		return (Timestamp) get_Value("TEMPDATE");
	}

	/** Set TIPO_COMPROBANTE */
	public void setTIPO_COMPROBANTE(String TIPO_COMPROBANTE) {
		if (TIPO_COMPROBANTE != null && TIPO_COMPROBANTE.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			TIPO_COMPROBANTE = TIPO_COMPROBANTE.substring(0, 3999);
		}
		set_Value("TIPO_COMPROBANTE", TIPO_COMPROBANTE);
	}

	/** Get TIPO_COMPROBANTE */
	public String getTIPO_COMPROBANTE() {
		return (String) get_Value("TIPO_COMPROBANTE");
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
