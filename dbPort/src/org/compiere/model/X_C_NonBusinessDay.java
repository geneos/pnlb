/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_NonBusinessDay
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.687
 */
public class X_C_NonBusinessDay extends PO {
	/** Standard Constructor */
	public X_C_NonBusinessDay(Properties ctx, int C_NonBusinessDay_ID,
			String trxName) {
		super(ctx, C_NonBusinessDay_ID, trxName);
		/**
		 * if (C_NonBusinessDay_ID == 0) { setC_Calendar_ID (0);
		 * setC_NonBusinessDay_ID (0); setDate1 (new
		 * Timestamp(System.currentTimeMillis())); }
		 */
	}

	/** Load Constructor */
	public X_C_NonBusinessDay(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_NonBusinessDay */
	public static final String Table_Name = "C_NonBusinessDay";

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

	protected BigDecimal accessLevel = new BigDecimal(6);

	/** AccessLevel 6 - System - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_NonBusinessDay[").append(
				get_ID()).append("]");
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
	 * Set Non Business Day. Day on which business is not transacted
	 */
	public void setC_NonBusinessDay_ID(int C_NonBusinessDay_ID) {
		if (C_NonBusinessDay_ID < 1)
			throw new IllegalArgumentException(
					"C_NonBusinessDay_ID is mandatory.");
		set_ValueNoCheck("C_NonBusinessDay_ID",
				new Integer(C_NonBusinessDay_ID));
	}

	/**
	 * Get Non Business Day. Day on which business is not transacted
	 */
	public int getC_NonBusinessDay_ID() {
		Integer ii = (Integer) get_Value("C_NonBusinessDay_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Date. Date when business is not conducted
	 */
	public void setDate1(Timestamp Date1) {
		if (Date1 == null)
			throw new IllegalArgumentException("Date1 is mandatory.");
		set_Value("Date1", Date1);
	}

	/**
	 * Get Date. Date when business is not conducted
	 */
	public Timestamp getDate1() {
		return (Timestamp) get_Value("Date1");
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name != null && Name.length() > 60) {
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
}
