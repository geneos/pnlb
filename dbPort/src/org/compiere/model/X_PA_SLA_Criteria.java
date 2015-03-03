/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_SLA_Criteria
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.125
 */
public class X_PA_SLA_Criteria extends PO {
	/** Standard Constructor */
	public X_PA_SLA_Criteria(Properties ctx, int PA_SLA_Criteria_ID,
			String trxName) {
		super(ctx, PA_SLA_Criteria_ID, trxName);
		/**
		 * if (PA_SLA_Criteria_ID == 0) { setIsManual (true); // Y setName
		 * (null); setPA_SLA_Criteria_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_PA_SLA_Criteria(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_SLA_Criteria */
	public static final String Table_Name = "PA_SLA_Criteria";

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
		StringBuffer sb = new StringBuffer("X_PA_SLA_Criteria[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Classname. Java Classname
	 */
	public void setClassname(String Classname) {
		if (Classname != null && Classname.length() > 60) {
			log.warning("Length > 60 - truncated");
			Classname = Classname.substring(0, 59);
		}
		set_Value("Classname", Classname);
	}

	/**
	 * Get Classname. Java Classname
	 */
	public String getClassname() {
		return (String) get_Value("Classname");
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
	 * Set Manual. This is a manual process
	 */
	public void setIsManual(boolean IsManual) {
		set_Value("IsManual", new Boolean(IsManual));
	}

	/**
	 * Get Manual. This is a manual process
	 */
	public boolean isManual() {
		Object oo = get_Value("IsManual");
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

	/**
	 * Set SLA Criteria. Service Level Agreement Criteria
	 */
	public void setPA_SLA_Criteria_ID(int PA_SLA_Criteria_ID) {
		if (PA_SLA_Criteria_ID < 1)
			throw new IllegalArgumentException(
					"PA_SLA_Criteria_ID is mandatory.");
		set_ValueNoCheck("PA_SLA_Criteria_ID", new Integer(PA_SLA_Criteria_ID));
	}

	/**
	 * Get SLA Criteria. Service Level Agreement Criteria
	 */
	public int getPA_SLA_Criteria_ID() {
		Integer ii = (Integer) get_Value("PA_SLA_Criteria_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
