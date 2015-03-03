/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_LIST_OUTSIDE_SALE
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.031
 */
public class X_T_LIST_OUTSIDE_SALE extends PO {
	/** Standard Constructor */
	public X_T_LIST_OUTSIDE_SALE(Properties ctx, int T_LIST_OUTSIDE_SALE_ID,
			String trxName) {
		super(ctx, T_LIST_OUTSIDE_SALE_ID, trxName);
		/**
		 * if (T_LIST_OUTSIDE_SALE_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_LIST_OUTSIDE_SALE(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_LIST_OUTSIDE_SALE */
	public static final String Table_Name = "T_LIST_OUTSIDE_SALE";

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
		StringBuffer sb = new StringBuffer("X_T_LIST_OUTSIDE_SALE[").append(
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

	/** Set CODE_PROD */
	public void setCODE_PROD(String CODE_PROD) {
		if (CODE_PROD != null && CODE_PROD.length() > 60) {
			log.warning("Length > 60 - truncated");
			CODE_PROD = CODE_PROD.substring(0, 59);
		}
		set_Value("CODE_PROD", CODE_PROD);
	}

	/** Get CODE_PROD */
	public String getCODE_PROD() {
		return (String) get_Value("CODE_PROD");
	}

	/** Set COUNTRY */
	public void setCOUNTRY(String COUNTRY) {
		if (COUNTRY != null && COUNTRY.length() > 60) {
			log.warning("Length > 60 - truncated");
			COUNTRY = COUNTRY.substring(0, 59);
		}
		set_Value("COUNTRY", COUNTRY);
	}

	/** Get COUNTRY */
	public String getCOUNTRY() {
		return (String) get_Value("COUNTRY");
	}

	/** Set DRUG */
	public void setDRUG(String DRUG) {
		if (DRUG != null && DRUG.length() > 60) {
			log.warning("Length > 60 - truncated");
			DRUG = DRUG.substring(0, 59);
		}
		set_Value("DRUG", DRUG);
	}

	/** Get DRUG */
	public String getDRUG() {
		return (String) get_Value("DRUG");
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description != null && Description.length() > 60) {
			log.warning("Length > 60 - truncated");
			Description = Description.substring(0, 59);
		}
		set_Value("Description", Description);
	}

	/**
	 * Get Description. Optional short description of the record
	 */
	public String getDescription() {
		return (String) get_Value("Description");
	}

	/** Set NETAMTUNIT */
	public void setNETAMTUNIT(BigDecimal NETAMTUNIT) {
		set_Value("NETAMTUNIT", NETAMTUNIT);
	}

	/** Get NETAMTUNIT */
	public BigDecimal getNETAMTUNIT() {
		BigDecimal bd = (BigDecimal) get_Value("NETAMTUNIT");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Quantity Invoiced. Invoiced Quantity
	 */
	public void setQtyInvoiced(BigDecimal QtyInvoiced) {
		set_Value("QtyInvoiced", QtyInvoiced);
	}

	/**
	 * Get Quantity Invoiced. Invoiced Quantity
	 */
	public BigDecimal getQtyInvoiced() {
		BigDecimal bd = (BigDecimal) get_Value("QtyInvoiced");
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
}
