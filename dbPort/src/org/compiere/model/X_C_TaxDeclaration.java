/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_TaxDeclaration
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.656
 */
public class X_C_TaxDeclaration extends PO {
	/** Standard Constructor */
	public X_C_TaxDeclaration(Properties ctx, int C_TaxDeclaration_ID,
			String trxName) {
		super(ctx, C_TaxDeclaration_ID, trxName);
		/**
		 * if (C_TaxDeclaration_ID == 0) { setC_TaxDeclaration_ID (0);
		 * setDateFrom (new Timestamp(System.currentTimeMillis())); setDateTo
		 * (new Timestamp(System.currentTimeMillis())); setDateTrx (new
		 * Timestamp(System.currentTimeMillis())); setName (null); setProcessed
		 * (false); }
		 */
	}

	/** Load Constructor */
	public X_C_TaxDeclaration(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_TaxDeclaration */
	public static final String Table_Name = "C_TaxDeclaration";

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
		StringBuffer sb = new StringBuffer("X_C_TaxDeclaration[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Tax Declaration. Define the declaration to the tax authorities
	 */
	public void setC_TaxDeclaration_ID(int C_TaxDeclaration_ID) {
		if (C_TaxDeclaration_ID < 1)
			throw new IllegalArgumentException(
					"C_TaxDeclaration_ID is mandatory.");
		set_ValueNoCheck("C_TaxDeclaration_ID",
				new Integer(C_TaxDeclaration_ID));
	}

	/**
	 * Get Tax Declaration. Define the declaration to the tax authorities
	 */
	public int getC_TaxDeclaration_ID() {
		Integer ii = (Integer) get_Value("C_TaxDeclaration_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
		if (DateTo == null)
			throw new IllegalArgumentException("DateTo is mandatory.");
		set_Value("DateTo", DateTo);
	}

	/**
	 * Get Date To. End date of a date range
	 */
	public Timestamp getDateTo() {
		return (Timestamp) get_Value("DateTo");
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
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 120) {
			log.warning("Length > 120 - truncated");
			Name = Name.substring(0, 119);
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
}
