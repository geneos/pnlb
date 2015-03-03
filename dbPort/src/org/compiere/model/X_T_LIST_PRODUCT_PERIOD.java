/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_LIST_PRODUCT_PERIOD
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.031
 */
public class X_T_LIST_PRODUCT_PERIOD extends PO {
	/** Standard Constructor */
	public X_T_LIST_PRODUCT_PERIOD(Properties ctx,
			int T_LIST_PRODUCT_PERIOD_ID, String trxName) {
		super(ctx, T_LIST_PRODUCT_PERIOD_ID, trxName);
		/**
		 * if (T_LIST_PRODUCT_PERIOD_ID == 0) { setAD_PInstance_ID (0);
		 * setCOD_MARK (null); setM_PRODUCT_NAME (null); setPRODUCT_CODE (null);
		 * setTEMPDATE (new Timestamp(System.currentTimeMillis())); }
		 */
	}

	/** Load Constructor */
	public X_T_LIST_PRODUCT_PERIOD(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_LIST_PRODUCT_PERIOD */
	public static final String Table_Name = "T_LIST_PRODUCT_PERIOD";

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
		StringBuffer sb = new StringBuffer("X_T_LIST_PRODUCT_PERIOD[").append(
				get_ID()).append("]");
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

	/** Set COD_MARK */
	public void setCOD_MARK(String COD_MARK) {
		if (COD_MARK == null)
			throw new IllegalArgumentException("COD_MARK is mandatory.");
		if (COD_MARK.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			COD_MARK = COD_MARK.substring(0, 3999);
		}
		set_Value("COD_MARK", COD_MARK);
	}

	/** Get COD_MARK */
	public String getCOD_MARK() {
		return (String) get_Value("COD_MARK");
	}

	/** Set M_PRODUCT_NAME */
	public void setM_PRODUCT_NAME(String M_PRODUCT_NAME) {
		if (M_PRODUCT_NAME == null)
			throw new IllegalArgumentException("M_PRODUCT_NAME is mandatory.");
		if (M_PRODUCT_NAME.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			M_PRODUCT_NAME = M_PRODUCT_NAME.substring(0, 3999);
		}
		set_Value("M_PRODUCT_NAME", M_PRODUCT_NAME);
	}

	/** Get M_PRODUCT_NAME */
	public String getM_PRODUCT_NAME() {
		return (String) get_Value("M_PRODUCT_NAME");
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
