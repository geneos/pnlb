/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_SALES_FOR_REGION
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.25
 */
public class X_T_SALES_FOR_REGION extends PO {
	/** Standard Constructor */
	public X_T_SALES_FOR_REGION(Properties ctx, int T_SALES_FOR_REGION_ID,
			String trxName) {
		super(ctx, T_SALES_FOR_REGION_ID, trxName);
		/**
		 * if (T_SALES_FOR_REGION_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_SALES_FOR_REGION(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_SALES_FOR_REGION */
	public static final String Table_Name = "T_SALES_FOR_REGION";

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
		StringBuffer sb = new StringBuffer("X_T_SALES_FOR_REGION[").append(
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

	/** Set C_BPARTNER_GROUP */
	public void setC_BPARTNER_GROUP(String C_BPARTNER_GROUP) {
		if (C_BPARTNER_GROUP != null && C_BPARTNER_GROUP.length() > 60) {
			log.warning("Length > 60 - truncated");
			C_BPARTNER_GROUP = C_BPARTNER_GROUP.substring(0, 59);
		}
		set_Value("C_BPARTNER_GROUP", C_BPARTNER_GROUP);
	}

	/** Get C_BPARTNER_GROUP */
	public String getC_BPARTNER_GROUP() {
		return (String) get_Value("C_BPARTNER_GROUP");
	}

	/** Set C_BPARTNER_GROUP_ID */
	public void setC_BPARTNER_GROUP_ID(int C_BPARTNER_GROUP_ID) {
		if (C_BPARTNER_GROUP_ID <= 0)
			set_Value("C_BPARTNER_GROUP_ID", null);
		else
			set_Value("C_BPARTNER_GROUP_ID", new Integer(C_BPARTNER_GROUP_ID));
	}

	/** Get C_BPARTNER_GROUP_ID */
	public int getC_BPARTNER_GROUP_ID() {
		Integer ii = (Integer) get_Value("C_BPARTNER_GROUP_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set C_BPARTNER_LOCATION */
	public void setC_BPARTNER_LOCATION(String C_BPARTNER_LOCATION) {
		if (C_BPARTNER_LOCATION != null && C_BPARTNER_LOCATION.length() > 60) {
			log.warning("Length > 60 - truncated");
			C_BPARTNER_LOCATION = C_BPARTNER_LOCATION.substring(0, 59);
		}
		set_Value("C_BPARTNER_LOCATION", C_BPARTNER_LOCATION);
	}

	/** Get C_BPARTNER_LOCATION */
	public String getC_BPARTNER_LOCATION() {
		return (String) get_Value("C_BPARTNER_LOCATION");
	}

	/**
	 * Set Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public void setC_BPartner_Location_ID(int C_BPartner_Location_ID) {
		if (C_BPartner_Location_ID <= 0)
			set_Value("C_BPartner_Location_ID", null);
		else
			set_Value("C_BPartner_Location_ID", new Integer(
					C_BPartner_Location_ID));
	}

	/**
	 * Get Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public int getC_BPartner_Location_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_Location_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/** Set TEMPDATE */
	public void setTEMPDATE(Timestamp TEMPDATE) {
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
