/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Job
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.609
 */
public class X_C_Job extends PO {
	/** Standard Constructor */
	public X_C_Job(Properties ctx, int C_Job_ID, String trxName) {
		super(ctx, C_Job_ID, trxName);
		/**
		 * if (C_Job_ID == 0) { setC_JobCategory_ID (0); setC_Job_ID (0);
		 * setIsEmployee (true); // Y setName (null); }
		 */
	}

	/** Load Constructor */
	public X_C_Job(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Job */
	public static final String Table_Name = "C_Job";

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
		StringBuffer sb = new StringBuffer("X_C_Job[").append(get_ID()).append(
				"]");
		return sb.toString();
	}

	/**
	 * Set Position Category. Job Position Category
	 */
	public void setC_JobCategory_ID(int C_JobCategory_ID) {
		if (C_JobCategory_ID < 1)
			throw new IllegalArgumentException("C_JobCategory_ID is mandatory.");
		set_Value("C_JobCategory_ID", new Integer(C_JobCategory_ID));
	}

	/**
	 * Get Position Category. Job Position Category
	 */
	public int getC_JobCategory_ID() {
		Integer ii = (Integer) get_Value("C_JobCategory_ID");
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
	 * Set Comment/Help. Comment or Hint
	 */
	public void setHelp(String Help) {
		if (Help != null && Help.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Help = Help.substring(0, 1999);
		}
		set_Value("Help", Help);
	}

	/**
	 * Get Comment/Help. Comment or Hint
	 */
	public String getHelp() {
		return (String) get_Value("Help");
	}

	/**
	 * Set Employee. Indicates if this Business Partner is an employee
	 */
	public void setIsEmployee(boolean IsEmployee) {
		set_Value("IsEmployee", new Boolean(IsEmployee));
	}

	/**
	 * Get Employee. Indicates if this Business Partner is an employee
	 */
	public boolean isEmployee() {
		Object oo = get_Value("IsEmployee");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
}
