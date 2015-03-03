/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_ADVANTAGE
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.359
 */
public class X_M_ADVANTAGE extends PO {
	/** Standard Constructor */
	public X_M_ADVANTAGE(Properties ctx, int M_ADVANTAGE_ID, String trxName) {
		super(ctx, M_ADVANTAGE_ID, trxName);
		/**
		 * if (M_ADVANTAGE_ID == 0) { setDateFrom (new
		 * Timestamp(System.currentTimeMillis())); setDateTo (new
		 * Timestamp(System.currentTimeMillis())); setM_ADVANTAGE_ID (0);
		 * setQUANTITY (0); setQUANTITY_ADVANTAGE (0); }
		 */
	}

	/** Load Constructor */
	public X_M_ADVANTAGE(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_ADVANTAGE */
	public static final String Table_Name = "M_ADVANTAGE";

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
		StringBuffer sb = new StringBuffer("X_M_ADVANTAGE[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID <= 0)
			set_Value("C_BPartner_ID", null);
		else
			set_Value("C_BPartner_ID", new Integer(C_BPartner_ID));
	}

	/**
	 * Get Business Partner . Identifies a Business Partner
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
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

	/** Set M_ADVANTAGE_ID */
	public void setM_ADVANTAGE_ID(int M_ADVANTAGE_ID) {
		if (M_ADVANTAGE_ID < 1)
			throw new IllegalArgumentException("M_ADVANTAGE_ID is mandatory.");
		set_ValueNoCheck("M_ADVANTAGE_ID", new Integer(M_ADVANTAGE_ID));
	}

	/** Get M_ADVANTAGE_ID */
	public int getM_ADVANTAGE_ID() {
		Integer ii = (Integer) get_Value("M_ADVANTAGE_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID <= 0)
			set_Value("M_Product_ID", null);
		else
			set_Value("M_Product_ID", new Integer(M_Product_ID));
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

	/** Set QUANTITY */
	public void setQUANTITY(int QUANTITY) {
		set_Value("QUANTITY", new Integer(QUANTITY));
	}

	/** Get QUANTITY */
	public int getQUANTITY() {
		Integer ii = (Integer) get_Value("QUANTITY");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set QUANTITY_ADVANTAGE */
	public void setQUANTITY_ADVANTAGE(int QUANTITY_ADVANTAGE) {
		set_Value("QUANTITY_ADVANTAGE", new Integer(QUANTITY_ADVANTAGE));
	}

	/** Get QUANTITY_ADVANTAGE */
	public int getQUANTITY_ADVANTAGE() {
		Integer ii = (Integer) get_Value("QUANTITY_ADVANTAGE");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
