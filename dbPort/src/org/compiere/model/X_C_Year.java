/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Year
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.89
 */
public class X_C_Year extends PO {
	/** Standard Constructor */
	public X_C_Year(Properties ctx, int C_Year_ID, String trxName) {
		super(ctx, C_Year_ID, trxName);
		/**
		 * if (C_Year_ID == 0) { setC_Calendar_ID (0); setC_Year_ID (0); setYear
		 * (null); }
		 */
	}

	/** Load Constructor */
	public X_C_Year(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Year */
	public static final String Table_Name = "C_Year";

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
		StringBuffer sb = new StringBuffer("X_C_Year[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Calendar. Accounting Calendar Name
	 */
	public void setC_Calendar_ID(int C_Calendar_ID) {
		if (C_Calendar_ID < 1)
			throw new IllegalArgumentException("C_Calendar_ID is mandatory.");
		set_ValueNoCheck("C_Calendar_ID", new Integer(C_Calendar_ID));
	}

	/**
	 * Get Calendar. Accounting Calendar Name
	 */
	public int getC_Calendar_ID() {
		Integer ii = (Integer) get_Value("C_Calendar_ID");
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
	 * Set Year. Calendar Year
	 */
	public void setYear(String Year) {
		if (Year == null)
			throw new IllegalArgumentException("Year is mandatory.");
		if (Year.length() > 10) {
			log.warning("Length > 10 - truncated");
			Year = Year.substring(0, 9);
		}
		set_Value("Year", Year);
	}

	/**
	 * Get Year. Calendar Year
	 */
	public String getYear() {
		return (String) get_Value("Year");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getYear());
	}
}
