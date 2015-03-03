/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_JobRemuneration
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.64
 */
public class X_C_JobRemuneration extends PO {
	/** Standard Constructor */
	public X_C_JobRemuneration(Properties ctx, int C_JobRemuneration_ID,
			String trxName) {
		super(ctx, C_JobRemuneration_ID, trxName);
		/**
		 * if (C_JobRemuneration_ID == 0) { setC_JobRemuneration_ID (0);
		 * setC_Job_ID (0); setC_Remuneration_ID (0); setValidFrom (new
		 * Timestamp(System.currentTimeMillis())); }
		 */
	}

	/** Load Constructor */
	public X_C_JobRemuneration(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_JobRemuneration */
	public static final String Table_Name = "C_JobRemuneration";

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
		StringBuffer sb = new StringBuffer("X_C_JobRemuneration[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Position Remuneration. Remuneration for the Position
	 */
	public void setC_JobRemuneration_ID(int C_JobRemuneration_ID) {
		if (C_JobRemuneration_ID < 1)
			throw new IllegalArgumentException(
					"C_JobRemuneration_ID is mandatory.");
		set_ValueNoCheck("C_JobRemuneration_ID", new Integer(
				C_JobRemuneration_ID));
	}

	/**
	 * Get Position Remuneration. Remuneration for the Position
	 */
	public int getC_JobRemuneration_ID() {
		Integer ii = (Integer) get_Value("C_JobRemuneration_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Position. Job Position
	 */
	public void setC_Job_ID(int C_Job_ID) {
		if (C_Job_ID < 1)
			throw new IllegalArgumentException("C_Job_ID is mandatory.");
		set_ValueNoCheck("C_Job_ID", new Integer(C_Job_ID));
	}

	/**
	 * Get Position. Job Position
	 */
	public int getC_Job_ID() {
		Integer ii = (Integer) get_Value("C_Job_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_Job_ID()));
	}

	/**
	 * Set Remuneration. Wage or Salary
	 */
	public void setC_Remuneration_ID(int C_Remuneration_ID) {
		if (C_Remuneration_ID < 1)
			throw new IllegalArgumentException(
					"C_Remuneration_ID is mandatory.");
		set_ValueNoCheck("C_Remuneration_ID", new Integer(C_Remuneration_ID));
	}

	/**
	 * Get Remuneration. Wage or Salary
	 */
	public int getC_Remuneration_ID() {
		Integer ii = (Integer) get_Value("C_Remuneration_ID");
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
	 * Set Valid from. Valid from including this date (first day)
	 */
	public void setValidFrom(Timestamp ValidFrom) {
		if (ValidFrom == null)
			throw new IllegalArgumentException("ValidFrom is mandatory.");
		set_Value("ValidFrom", ValidFrom);
	}

	/**
	 * Get Valid from. Valid from including this date (first day)
	 */
	public Timestamp getValidFrom() {
		return (Timestamp) get_Value("ValidFrom");
	}

	/**
	 * Set Valid to. Valid to including this date (last day)
	 */
	public void setValidTo(Timestamp ValidTo) {
		set_Value("ValidTo", ValidTo);
	}

	/**
	 * Get Valid to. Valid to including this date (last day)
	 */
	public Timestamp getValidTo() {
		return (Timestamp) get_Value("ValidTo");
	}

}
