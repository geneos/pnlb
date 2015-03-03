/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_JobAssignment
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.625
 */
public class X_C_JobAssignment extends PO {
	/** Standard Constructor */
	public X_C_JobAssignment(Properties ctx, int C_JobAssignment_ID,
			String trxName) {
		super(ctx, C_JobAssignment_ID, trxName);
		/**
		 * if (C_JobAssignment_ID == 0) { setAD_User_ID (0);
		 * setC_JobAssignment_ID (0); setC_Job_ID (0); setValidFrom (new
		 * Timestamp(System.currentTimeMillis())); }
		 */
	}

	/** Load Constructor */
	public X_C_JobAssignment(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_JobAssignment */
	public static final String Table_Name = "C_JobAssignment";

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
		StringBuffer sb = new StringBuffer("X_C_JobAssignment[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID < 1)
			throw new IllegalArgumentException("AD_User_ID is mandatory.");
		set_ValueNoCheck("AD_User_ID", new Integer(AD_User_ID));
	}

	/**
	 * Get User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public int getAD_User_ID() {
		Integer ii = (Integer) get_Value("AD_User_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Position Assignment. Assignemt of Employee (User) to Job Position
	 */
	public void setC_JobAssignment_ID(int C_JobAssignment_ID) {
		if (C_JobAssignment_ID < 1)
			throw new IllegalArgumentException(
					"C_JobAssignment_ID is mandatory.");
		set_ValueNoCheck("C_JobAssignment_ID", new Integer(C_JobAssignment_ID));
	}

	/**
	 * Get Position Assignment. Assignemt of Employee (User) to Job Position
	 */
	public int getC_JobAssignment_ID() {
		Integer ii = (Integer) get_Value("C_JobAssignment_ID");
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
