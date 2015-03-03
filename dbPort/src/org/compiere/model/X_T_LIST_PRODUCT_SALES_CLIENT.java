/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_LIST_PRODUCT_SALES_CLIENT
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.046
 */
public class X_T_LIST_PRODUCT_SALES_CLIENT extends PO {
	/** Standard Constructor */
	public X_T_LIST_PRODUCT_SALES_CLIENT(Properties ctx,
			int T_LIST_PRODUCT_SALES_CLIENT_ID, String trxName) {
		super(ctx, T_LIST_PRODUCT_SALES_CLIENT_ID, trxName);
		/**
		 * if (T_LIST_PRODUCT_SALES_CLIENT_ID == 0) { setAD_PInstance_ID (0);
		 * setCLIENT (null); setDescription (null); setMARK_CODE (null);
		 * setPARTNER (null); setPRODUCT_CODE (null); setTEMPDATE (new
		 * Timestamp(System.currentTimeMillis())); }
		 */
	}

	/** Load Constructor */
	public X_T_LIST_PRODUCT_SALES_CLIENT(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_LIST_PRODUCT_SALES_CLIENT */
	public static final String Table_Name = "T_LIST_PRODUCT_SALES_CLIENT";

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

	protected BigDecimal accessLevel = new BigDecimal(4);

	/** AccessLevel 4 - System */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_T_LIST_PRODUCT_SALES_CLIENT[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Process Instance. Instance of the process
	 */
	public void setAD_PInstance_ID(int AD_PInstance_ID) {
		if (AD_PInstance_ID < 1)
			throw new IllegalArgumentException("AD_PInstance_ID is mandatory.");
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

	/** Set CLIENT */
	public void setCLIENT(String CLIENT) {
		if (CLIENT == null)
			throw new IllegalArgumentException("CLIENT is mandatory.");
		if (CLIENT.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			CLIENT = CLIENT.substring(0, 3999);
		}
		set_Value("CLIENT", CLIENT);
	}

	/** Get CLIENT */
	public String getCLIENT() {
		return (String) get_Value("CLIENT");
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description == null)
			throw new IllegalArgumentException("Description is mandatory.");
		if (Description.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			Description = Description.substring(0, 3999);
		}
		set_Value("Description", Description);
	}

	/**
	 * Get Description. Optional short description of the record
	 */
	public String getDescription() {
		return (String) get_Value("Description");
	}

	/** Set MARK_CODE */
	public void setMARK_CODE(String MARK_CODE) {
		if (MARK_CODE == null)
			throw new IllegalArgumentException("MARK_CODE is mandatory.");
		if (MARK_CODE.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			MARK_CODE = MARK_CODE.substring(0, 3999);
		}
		set_Value("MARK_CODE", MARK_CODE);
	}

	/** Get MARK_CODE */
	public String getMARK_CODE() {
		return (String) get_Value("MARK_CODE");
	}

	/** Set NETVALUE */
	public void setNETVALUE(BigDecimal NETVALUE) {
		set_Value("NETVALUE", NETVALUE);
	}

	/** Get NETVALUE */
	public BigDecimal getNETVALUE() {
		BigDecimal bd = (BigDecimal) get_Value("NETVALUE");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set PARTNER */
	public void setPARTNER(String PARTNER) {
		if (PARTNER == null)
			throw new IllegalArgumentException("PARTNER is mandatory.");
		if (PARTNER.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			PARTNER = PARTNER.substring(0, 3999);
		}
		set_Value("PARTNER", PARTNER);
	}

	/** Get PARTNER */
	public String getPARTNER() {
		return (String) get_Value("PARTNER");
	}

	/** Set PRODUCT_CODE */
	public void setPRODUCT_CODE(String PRODUCT_CODE) {
		if (PRODUCT_CODE == null)
			throw new IllegalArgumentException("PRODUCT_CODE is mandatory.");
		if (PRODUCT_CODE.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			PRODUCT_CODE = PRODUCT_CODE.substring(0, 3999);
		}
		set_Value("PRODUCT_CODE", PRODUCT_CODE);
	}

	/** Get PRODUCT_CODE */
	public String getPRODUCT_CODE() {
		return (String) get_Value("PRODUCT_CODE");
	}

	/** Set TEMPDATE */
	public void setTEMPDATE(Timestamp TEMPDATE) {
		if (TEMPDATE == null)
			throw new IllegalArgumentException("TEMPDATE is mandatory.");
		set_Value("TEMPDATE", TEMPDATE);
	}

	/** Get TEMPDATE */
	public Timestamp getTEMPDATE() {
		return (Timestamp) get_Value("TEMPDATE");
	}

	/** Set UNITS */
	public void setUNITS(BigDecimal UNITS) {
		set_Value("UNITS", UNITS);
	}

	/** Get UNITS */
	public BigDecimal getUNITS() {
		BigDecimal bd = (BigDecimal) get_Value("UNITS");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
