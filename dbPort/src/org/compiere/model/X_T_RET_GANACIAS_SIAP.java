/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_RET_GANACIAS_SIAP
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.125
 */
public class X_T_RET_GANACIAS_SIAP extends PO {
	/** Standard Constructor */
	public X_T_RET_GANACIAS_SIAP(Properties ctx, int T_RET_GANACIAS_SIAP_ID,
			String trxName) {
		super(ctx, T_RET_GANACIAS_SIAP_ID, trxName);
		/**
		 * if (T_RET_GANACIAS_SIAP_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_RET_GANACIAS_SIAP(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

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

	/** TableName=T_RET_GANACIAS_SIAP */
	public static final String Table_Name = "T_RET_GANACIAS_SIAP";

	protected BigDecimal accessLevel = new BigDecimal(3);

	/** AccessLevel 3 - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_T_RET_GANACIAS_SIAP[").append(
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
		if (CUIT != null && CUIT.length() > 13) {
			log.warning("Length > 13 - truncated");
			CUIT = CUIT.substring(0, 12);
		}
		set_Value("CUIT", CUIT);
	}

	/** Get CUIT */
	public String getCUIT() {
		return (String) get_Value("CUIT");
	}

	/** Set FECHA */
	public void setFECHA(String FECHA) {
		if (FECHA != null && FECHA.length() > 8) {
			log.warning("Length > 8 - truncated");
			FECHA = FECHA.substring(0, 7);
		}
		set_Value("FECHA", FECHA);
	}

	/** Get FECHA */
	public String getFECHA() {
		return (String) get_Value("FECHA");
	}

	/** Set IMPORTE */
	public void setIMPORTE(String IMPORTE) {
		if (IMPORTE != null && IMPORTE.length() > 11) {
			log.warning("Length > 11 - truncated");
			IMPORTE = IMPORTE.substring(0, 10);
		}
		set_Value("IMPORTE", IMPORTE);
	}

	/** Get IMPORTE */
	public String getIMPORTE() {
		return (String) get_Value("IMPORTE");
	}

	/** Set NRO */
	public void setNRO(String NRO) {
		if (NRO != null && NRO.length() > 8) {
			log.warning("Length > 8 - truncated");
			NRO = NRO.substring(0, 7);
		}
		set_Value("NRO", NRO);
	}

	/** Get NRO */
	public String getNRO() {
		return (String) get_Value("NRO");
	}

	/** Set REGIMEN */
	public void setREGIMEN(String REGIMEN) {
		if (REGIMEN != null && REGIMEN.length() > 3) {
			log.warning("Length > 3 - truncated");
			REGIMEN = REGIMEN.substring(0, 2);
		}
		set_Value("REGIMEN", REGIMEN);
	}

	/** Get REGIMEN */
	public String getREGIMEN() {
		return (String) get_Value("REGIMEN");
	}

	/** Set TEMPDATE */
	public void setTEMPDATE(Timestamp TEMPDATE) {
		set_Value("TEMPDATE", TEMPDATE);
	}

	/** Get TEMPDATE */
	public Timestamp getTEMPDATE() {
		return (Timestamp) get_Value("TEMPDATE");
	}
}
