/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_ServiceLevelLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.562
 */
public class X_C_ServiceLevelLine extends PO {
	/** Standard Constructor */
	public X_C_ServiceLevelLine(Properties ctx, int C_ServiceLevelLine_ID,
			String trxName) {
		super(ctx, C_ServiceLevelLine_ID, trxName);
		/**
		 * if (C_ServiceLevelLine_ID == 0) { setC_ServiceLevelLine_ID (0);
		 * setC_ServiceLevel_ID (0); setServiceDate (new
		 * Timestamp(System.currentTimeMillis())); setServiceLevelProvided
		 * (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_ServiceLevelLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_ServiceLevelLine */
	public static final String Table_Name = "C_ServiceLevelLine";

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

	protected BigDecimal accessLevel = new BigDecimal(1);

	/** AccessLevel 1 - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_ServiceLevelLine[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Service Level Line. Product Revenue Recognition Service Level Line
	 */
	public void setC_ServiceLevelLine_ID(int C_ServiceLevelLine_ID) {
		if (C_ServiceLevelLine_ID < 1)
			throw new IllegalArgumentException(
					"C_ServiceLevelLine_ID is mandatory.");
		set_ValueNoCheck("C_ServiceLevelLine_ID", new Integer(
				C_ServiceLevelLine_ID));
	}

	/**
	 * Get Service Level Line. Product Revenue Recognition Service Level Line
	 */
	public int getC_ServiceLevelLine_ID() {
		Integer ii = (Integer) get_Value("C_ServiceLevelLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Service Level. Product Revenue Recognition Service Level
	 */
	public void setC_ServiceLevel_ID(int C_ServiceLevel_ID) {
		if (C_ServiceLevel_ID < 1)
			throw new IllegalArgumentException(
					"C_ServiceLevel_ID is mandatory.");
		set_ValueNoCheck("C_ServiceLevel_ID", new Integer(C_ServiceLevel_ID));
	}

	/**
	 * Get Service Level. Product Revenue Recognition Service Level
	 */
	public int getC_ServiceLevel_ID() {
		Integer ii = (Integer) get_Value("C_ServiceLevel_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description != null && Description.length() > 255) {
			log.warning("Length > 255 - truncated");
			Description = Description.substring(0, 254);
		}
		set_Value("Description", Description);
	}

	/**
	 * Get Description. Optional short description of the record
	 */
	public String getDescription() {
		return (String) get_Value("Description");
	}

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_ValueNoCheck("Processed", new Boolean(Processed));
	}

	/**
	 * Get Processed. The document has been processed
	 */
	public boolean isProcessed() {
		Object oo = get_Value("Processed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Service date. Date service was provided
	 */
	public void setServiceDate(Timestamp ServiceDate) {
		if (ServiceDate == null)
			throw new IllegalArgumentException("ServiceDate is mandatory.");
		set_ValueNoCheck("ServiceDate", ServiceDate);
	}

	/**
	 * Get Service date. Date service was provided
	 */
	public Timestamp getServiceDate() {
		return (Timestamp) get_Value("ServiceDate");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getServiceDate()));
	}

	/**
	 * Set Quantity Provided. Quantity of service or product provided
	 */
	public void setServiceLevelProvided(BigDecimal ServiceLevelProvided) {
		if (ServiceLevelProvided == null)
			throw new IllegalArgumentException(
					"ServiceLevelProvided is mandatory.");
		set_ValueNoCheck("ServiceLevelProvided", ServiceLevelProvided);
	}

	/**
	 * Get Quantity Provided. Quantity of service or product provided
	 */
	public BigDecimal getServiceLevelProvided() {
		BigDecimal bd = (BigDecimal) get_Value("ServiceLevelProvided");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
