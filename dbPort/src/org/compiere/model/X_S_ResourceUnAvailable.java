/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for S_ResourceUnAvailable
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.765
 */
public class X_S_ResourceUnAvailable extends PO {
	/** Standard Constructor */
	public X_S_ResourceUnAvailable(Properties ctx,
			int S_ResourceUnAvailable_ID, String trxName) {
		super(ctx, S_ResourceUnAvailable_ID, trxName);
		/**
		 * if (S_ResourceUnAvailable_ID == 0) { setDateFrom (new
		 * Timestamp(System.currentTimeMillis())); setS_ResourceUnAvailable_ID
		 * (0); setS_Resource_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_S_ResourceUnAvailable(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=S_ResourceUnAvailable */
	public static final String Table_Name = "S_ResourceUnAvailable";

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
		StringBuffer sb = new StringBuffer("X_S_ResourceUnAvailable[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Date From. Starting date for a range
	 */
	public void setDateFrom(Timestamp DateFrom) {
		if (DateFrom == null)
			throw new IllegalArgumentException("DateFrom is mandatory.");
		set_Value("DateFrom", DateFrom);
	}

	/**
	 * Get Date From. Starting date for a range
	 */
	public Timestamp getDateFrom() {
		return (Timestamp) get_Value("DateFrom");
	}

	/**
	 * Set Date To. End date of a date range
	 */
	public void setDateTo(Timestamp DateTo) {
		set_Value("DateTo", DateTo);
	}

	/**
	 * Get Date To. End date of a date range
	 */
	public Timestamp getDateTo() {
		return (Timestamp) get_Value("DateTo");
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

	/** Set Resource Unavailability */
	public void setS_ResourceUnAvailable_ID(int S_ResourceUnAvailable_ID) {
		if (S_ResourceUnAvailable_ID < 1)
			throw new IllegalArgumentException(
					"S_ResourceUnAvailable_ID is mandatory.");
		set_ValueNoCheck("S_ResourceUnAvailable_ID", new Integer(
				S_ResourceUnAvailable_ID));
	}

	/** Get Resource Unavailability */
	public int getS_ResourceUnAvailable_ID() {
		Integer ii = (Integer) get_Value("S_ResourceUnAvailable_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Resource. Resource
	 */
	public void setS_Resource_ID(int S_Resource_ID) {
		if (S_Resource_ID < 1)
			throw new IllegalArgumentException("S_Resource_ID is mandatory.");
		set_ValueNoCheck("S_Resource_ID", new Integer(S_Resource_ID));
	}

	/**
	 * Get Resource. Resource
	 */
	public int getS_Resource_ID() {
		Integer ii = (Integer) get_Value("S_Resource_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getS_Resource_ID()));
	}
}
