/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_COMPOSICION_SALDOS
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.921
 */
public class X_T_COMPOSICION_SALDOS extends PO {
	/** Standard Constructor */
	public X_T_COMPOSICION_SALDOS(Properties ctx, int T_COMPOSICION_SALDOS_ID,
			String trxName) {
		super(ctx, T_COMPOSICION_SALDOS_ID, trxName);
		/**
		 * if (T_COMPOSICION_SALDOS_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_COMPOSICION_SALDOS(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_COMPOSICION_SALDOS */
	public static final String Table_Name = "T_COMPOSICION_SALDOS";

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
		StringBuffer sb = new StringBuffer("X_T_COMPOSICION_SALDOS[").append(
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

	/** Set COMPOSICION_ID */
	public void setCOMPOSICION_ID(int COMPOSICION_ID) {
		if (COMPOSICION_ID <= 0)
			set_Value("COMPOSICION_ID", null);
		else
			set_Value("COMPOSICION_ID", new Integer(COMPOSICION_ID));
	}

	/** Get COMPOSICION_ID */
	public int getCOMPOSICION_ID() {
		Integer ii = (Integer) get_Value("COMPOSICION_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set COMPROBANTE */
	public void setCOMPROBANTE(String COMPROBANTE) {
		if (COMPROBANTE != null && COMPROBANTE.length() > 1) {
			log.warning("Length > 1 - truncated");
			COMPROBANTE = COMPROBANTE.substring(0, 0);
		}
		set_Value("COMPROBANTE", COMPROBANTE);
	}

	/** Get COMPROBANTE */
	public String getCOMPROBANTE() {
		return (String) get_Value("COMPROBANTE");
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

	/** Set FECHA */
	public void setFECHA(String FECHA) {
		if (FECHA != null && FECHA.length() > 10) {
			log.warning("Length > 10 - truncated");
			FECHA = FECHA.substring(0, 9);
		}
		set_Value("FECHA", FECHA);
	}

	/** Get FECHA */
	public String getFECHA() {
		return (String) get_Value("FECHA");
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

	/** Set MONEDA */
	public void setMONEDA(String MONEDA) {
		if (MONEDA != null && MONEDA.length() > 8) {
			log.warning("Length > 8 - truncated");
			MONEDA = MONEDA.substring(0, 7);
		}
		set_Value("MONEDA", MONEDA);
	}

	/** Get MONEDA */
	public String getMONEDA() {
		return (String) get_Value("MONEDA");
	}

	/** Set MORA */
	public void setMORA(BigDecimal MORA) {
		set_Value("MORA", MORA);
	}

	/** Get MORA */
	public BigDecimal getMORA() {
		BigDecimal bd = (BigDecimal) get_Value("MORA");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set NRO_COMP */
	public void setNRO_COMP(String NRO_COMP) {
		if (NRO_COMP != null && NRO_COMP.length() > 15) {
			log.warning("Length > 15 - truncated");
			NRO_COMP = NRO_COMP.substring(0, 14);
		}
		set_Value("NRO_COMP", NRO_COMP);
	}

	/** Get NRO_COMP */
	public String getNRO_COMP() {
		return (String) get_Value("NRO_COMP");
	}

	/** Set SALDO */
	public void setSALDO(BigDecimal SALDO) {
		set_Value("SALDO", SALDO);
	}

	/** Get SALDO */
	public BigDecimal getSALDO() {
		BigDecimal bd = (BigDecimal) get_Value("SALDO");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set TEMPDATE */
	public void setTEMPDATE(Timestamp TEMPDATE) {
		set_Value("TEMPDATE", TEMPDATE);
	}

	/** Get TEMPDATE */
	public Timestamp getTEMPDATE() {
		return (Timestamp) get_Value("TEMPDATE");
	}

	/** Set TIPO_COMP */
	public void setTIPO_COMP(String TIPO_COMP) {
		if (TIPO_COMP != null && TIPO_COMP.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			TIPO_COMP = TIPO_COMP.substring(0, 3999);
		}
		set_Value("TIPO_COMP", TIPO_COMP);
	}

	/** Get TIPO_COMP */
	public String getTIPO_COMP() {
		return (String) get_Value("TIPO_COMP");
	}

	/** Set VENCIMIENTO */
	public void setVENCIMIENTO(Timestamp VENCIMIENTO) {
		set_Value("VENCIMIENTO", VENCIMIENTO);
	}

	/** Get VENCIMIENTO */
	public Timestamp getVENCIMIENTO() {
		return (Timestamp) get_Value("VENCIMIENTO");
	}
}
