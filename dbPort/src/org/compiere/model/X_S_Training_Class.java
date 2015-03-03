/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for S_Training_Class
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.828
 */
public class X_S_Training_Class extends PO {
	/** Standard Constructor */
	public X_S_Training_Class(Properties ctx, int S_Training_Class_ID,
			String trxName) {
		super(ctx, S_Training_Class_ID, trxName);
		/**
		 * if (S_Training_Class_ID == 0) { setEndDate (new
		 * Timestamp(System.currentTimeMillis())); setM_Product_ID (0);
		 * setS_Training_Class_ID (0); setS_Training_ID (0); setStartDate (new
		 * Timestamp(System.currentTimeMillis())); }
		 */
	}

	/** Load Constructor */
	public X_S_Training_Class(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=S_Training_Class */
	public static final String Table_Name = "S_Training_Class";

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
		StringBuffer sb = new StringBuffer("X_S_Training_Class[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set End Date. Last effective date (inclusive)
	 */
	public void setEndDate(Timestamp EndDate) {
		if (EndDate == null)
			throw new IllegalArgumentException("EndDate is mandatory.");
		set_Value("EndDate", EndDate);
	}

	/**
	 * Get End Date. Last effective date (inclusive)
	 */
	public Timestamp getEndDate() {
		return (Timestamp) get_Value("EndDate");
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID < 1)
			throw new IllegalArgumentException("M_Product_ID is mandatory.");
		set_ValueNoCheck("M_Product_ID", new Integer(M_Product_ID));
	}

	/**
	 * Get Product. Product, Service, Item
	 */
	public int getM_Product_ID() {
		Integer ii = (Integer) get_Value("M_Product_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Training Class. The actual training class instance
	 */
	public void setS_Training_Class_ID(int S_Training_Class_ID) {
		if (S_Training_Class_ID < 1)
			throw new IllegalArgumentException(
					"S_Training_Class_ID is mandatory.");
		set_ValueNoCheck("S_Training_Class_ID",
				new Integer(S_Training_Class_ID));
	}

	/**
	 * Get Training Class. The actual training class instance
	 */
	public int getS_Training_Class_ID() {
		Integer ii = (Integer) get_Value("S_Training_Class_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Training. Repeated Training
	 */
	public void setS_Training_ID(int S_Training_ID) {
		if (S_Training_ID < 1)
			throw new IllegalArgumentException("S_Training_ID is mandatory.");
		set_ValueNoCheck("S_Training_ID", new Integer(S_Training_ID));
	}

	/**
	 * Get Training. Repeated Training
	 */
	public int getS_Training_ID() {
		Integer ii = (Integer) get_Value("S_Training_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getStartDate()));
	}
}
