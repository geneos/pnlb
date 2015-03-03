/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Period
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.187
 */
public class X_C_Period extends PO {
	/** Standard Constructor */
	public X_C_Period(Properties ctx, int C_Period_ID, String trxName) {
		super(ctx, C_Period_ID, trxName);
		/**
		 * if (C_Period_ID == 0) { setC_Period_ID (0); setC_Year_ID (0); setName
		 * (null); setPeriodNo (0); setPeriodType (null); // S setStartDate (new
		 * Timestamp(System.currentTimeMillis())); }
		 */
	}

	/** Load Constructor */
	public X_C_Period(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Period */
	public static final String Table_Name = "C_Period";

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

	protected BigDecimal accessLevel = new BigDecimal(2);

	/** AccessLevel 2 - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_Period[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Period. Period of the Calendar
	 */
	public void setC_Period_ID(int C_Period_ID) {
		if (C_Period_ID < 1)
			throw new IllegalArgumentException("C_Period_ID is mandatory.");
		set_ValueNoCheck("C_Period_ID", new Integer(C_Period_ID));
	}

	/**
	 * Get Period. Period of the Calendar
	 */
	public int getC_Period_ID() {
		Integer ii = (Integer) get_Value("C_Period_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Year. Calendar Year
	 */
	public void setC_Year_ID(int C_Year_ID) {
		if (C_Year_ID < 1)
			throw new IllegalArgumentException("C_Year_ID is mandatory.");
		set_ValueNoCheck("C_Year_ID", new Integer(C_Year_ID));
	}

	/**
	 * Get Year. Calendar Year
	 */
	public int getC_Year_ID() {
		Integer ii = (Integer) get_Value("C_Year_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set End Date. Last effective date (inclusive)
	 */
	public void setEndDate(Timestamp EndDate) {
		set_Value("EndDate", EndDate);
	}

	/**
	 * Get End Date. Last effective date (inclusive)
	 */
	public Timestamp getEndDate() {
		return (Timestamp) get_Value("EndDate");
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 60) {
			log.warning("Length > 60 - truncated");
			Name = Name.substring(0, 59);
		}
		set_Value("Name", Name);
	}

	/**
	 * Get Name. Alphanumeric identifier of the entity
	 */
	public String getName() {
		return (String) get_Value("Name");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
	}

	/**
	 * Set Period No. Unique Period Number
	 */
	public void setPeriodNo(int PeriodNo) {
		set_Value("PeriodNo", new Integer(PeriodNo));
	}

	/**
	 * Get Period No. Unique Period Number
	 */
	public int getPeriodNo() {
		Integer ii = (Integer) get_Value("PeriodNo");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** PeriodType AD_Reference_ID=115 */
	public static final int PERIODTYPE_AD_Reference_ID = 115;

	/** Adjustment Period = A */
	public static final String PERIODTYPE_AdjustmentPeriod = "A";

	/** Standard Calendar Period = S */
	public static final String PERIODTYPE_StandardCalendarPeriod = "S";

	/**
	 * Set Period Type. Period Type
	 */
	public void setPeriodType(String PeriodType) {
		if (PeriodType == null)
			throw new IllegalArgumentException("PeriodType is mandatory");
		if (PeriodType.equals("A") || PeriodType.equals("S"))
			;
		else
			throw new IllegalArgumentException("PeriodType Invalid value - "
					+ PeriodType + " - Reference_ID=115 - A - S");
		if (PeriodType.length() > 1) {
			log.warning("Length > 1 - truncated");
			PeriodType = PeriodType.substring(0, 0);
		}
		set_ValueNoCheck("PeriodType", PeriodType);
	}

	/**
	 * Get Period Type. Period Type
	 */
	public String getPeriodType() {
		return (String) get_Value("PeriodType");
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
	 * Set Start Date. First effective day (inclusive)
	 */
	public void setStartDate(Timestamp StartDate) {
		if (StartDate == null)
			throw new IllegalArgumentException("StartDate is mandatory.");
		set_Value("StartDate", StartDate);
	}

	/**
	 * Get Start Date. First effective day (inclusive)
	 */
	public Timestamp getStartDate() {
		return (Timestamp) get_Value("StartDate");
	}
}
