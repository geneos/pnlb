/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_SLA_Measure
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.14
 */
public class X_PA_SLA_Measure extends PO {
	/** Standard Constructor */
	public X_PA_SLA_Measure(Properties ctx, int PA_SLA_Measure_ID,
			String trxName) {
		super(ctx, PA_SLA_Measure_ID, trxName);
		/**
		 * if (PA_SLA_Measure_ID == 0) { setDateTrx (new
		 * Timestamp(System.currentTimeMillis())); setMeasureActual (Env.ZERO);
		 * setPA_SLA_Goal_ID (0); setPA_SLA_Measure_ID (0); setProcessed
		 * (false); }
		 */
	}

	/** Load Constructor */
	public X_PA_SLA_Measure(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_SLA_Measure */
	public static final String Table_Name = "PA_SLA_Measure";

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
		StringBuffer sb = new StringBuffer("X_PA_SLA_Measure[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID <= 0)
			set_Value("AD_Table_ID", null);
		else
			set_Value("AD_Table_ID", new Integer(AD_Table_ID));
	}

	/**
	 * Get Table. Table for the Fields
	 */
	public int getAD_Table_ID() {
		Integer ii = (Integer) get_Value("AD_Table_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Transaction Date. Transaction Date
	 */
	public void setDateTrx(Timestamp DateTrx) {
		if (DateTrx == null)
			throw new IllegalArgumentException("DateTrx is mandatory.");
		set_Value("DateTrx", DateTrx);
	}

	/**
	 * Get Transaction Date. Transaction Date
	 */
	public Timestamp getDateTrx() {
		return (Timestamp) get_Value("DateTrx");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getDateTrx()));
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
	 * Set Measure Actual. Actual value that has been measured.
	 */
	public void setMeasureActual(BigDecimal MeasureActual) {
		if (MeasureActual == null)
			throw new IllegalArgumentException("MeasureActual is mandatory.");
		set_Value("MeasureActual", MeasureActual);
	}

	/**
	 * Get Measure Actual. Actual value that has been measured.
	 */
	public BigDecimal getMeasureActual() {
		BigDecimal bd = (BigDecimal) get_Value("MeasureActual");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set SLA Goal. Service Level Agreement Goal
	 */
	public void setPA_SLA_Goal_ID(int PA_SLA_Goal_ID) {
		if (PA_SLA_Goal_ID < 1)
			throw new IllegalArgumentException("PA_SLA_Goal_ID is mandatory.");
		set_ValueNoCheck("PA_SLA_Goal_ID", new Integer(PA_SLA_Goal_ID));
	}

	/**
	 * Get SLA Goal. Service Level Agreement Goal
	 */
	public int getPA_SLA_Goal_ID() {
		Integer ii = (Integer) get_Value("PA_SLA_Goal_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set SLA Measure. Service Level Agreement Measure
	 */
	public void setPA_SLA_Measure_ID(int PA_SLA_Measure_ID) {
		if (PA_SLA_Measure_ID < 1)
			throw new IllegalArgumentException(
					"PA_SLA_Measure_ID is mandatory.");
		set_ValueNoCheck("PA_SLA_Measure_ID", new Integer(PA_SLA_Measure_ID));
	}

	/**
	 * Get SLA Measure. Service Level Agreement Measure
	 */
	public int getPA_SLA_Measure_ID() {
		Integer ii = (Integer) get_Value("PA_SLA_Measure_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_Value("Processed", new Boolean(Processed));
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

	/** Set Process Now */
	public void setProcessing(boolean Processing) {
		set_Value("Processing", new Boolean(Processing));
	}

	/** Get Process Now */
	public boolean isProcessing() {
		Object oo = get_Value("Processing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Record ID. Direct internal record ID
	 */
	public void setRecord_ID(int Record_ID) {
		if (Record_ID <= 0)
			set_Value("Record_ID", null);
		else
			set_Value("Record_ID", new Integer(Record_ID));
	}

	/**
	 * Get Record ID. Direct internal record ID
	 */
	public int getRecord_ID() {
		Integer ii = (Integer) get_Value("Record_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
