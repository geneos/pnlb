/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_RET_GAN_COBRANZAS_CAB
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.14
 */
public class X_T_RET_GAN_COBRANZAS_CAB extends PO {
	/** Standard Constructor */
	public X_T_RET_GAN_COBRANZAS_CAB(Properties ctx,
			int T_RET_GAN_COBRANZAS_CAB_ID, String trxName) {
		super(ctx, T_RET_GAN_COBRANZAS_CAB_ID, trxName);
		/**
		 * if (T_RET_GAN_COBRANZAS_CAB_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_RET_GAN_COBRANZAS_CAB(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_RET_GAN_COBRANZAS_CAB */
	public static final String Table_Name = "T_RET_GAN_COBRANZAS_CAB";

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
		StringBuffer sb = new StringBuffer("X_T_RET_GAN_COBRANZAS_CAB[")
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

	/** Set CLIENTE */
	public void setCLIENTE(String CLIENTE) {
		if (CLIENTE != null && CLIENTE.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			CLIENTE = CLIENTE.substring(0, 3999);
		}
		set_Value("CLIENTE", CLIENTE);
	}

	/** Get CLIENTE */
	public String getCLIENTE() {
		return (String) get_Value("CLIENTE");
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

	/** Set TITULO */
	public void setTITULO(String TITULO) {
		if (TITULO != null && TITULO.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			TITULO = TITULO.substring(0, 3999);
		}
		set_Value("TITULO", TITULO);
	}

	/** Get TITULO */
	public String getTITULO() {
		return (String) get_Value("TITULO");
	}
}
