/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Demand
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.578
 */
public class X_M_Demand extends PO {
	/** Standard Constructor */
	public X_M_Demand(Properties ctx, int M_Demand_ID, String trxName) {
		super(ctx, M_Demand_ID, trxName);
		/**
		 * if (M_Demand_ID == 0) { setC_Calendar_ID (0); setC_Year_ID (0);
		 * setIsDefault (false); setM_Demand_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_M_Demand(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Demand */
	public static final String Table_Name = "M_Demand";

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
		StringBuffer sb = new StringBuffer("X_M_Demand[").append(get_ID())
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
	 * Set Default. Default value
	 */
	public void setIsDefault(boolean IsDefault) {
		set_Value("IsDefault", new Boolean(IsDefault));
	}

	/**
	 * Get Default. Default value
	 */
	public boolean isDefault() {
		Object oo = get_Value("IsDefault");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Demand. Material Demand
	 */
	public void setM_Demand_ID(int M_Demand_ID) {
		if (M_Demand_ID < 1)
			throw new IllegalArgumentException("M_Demand_ID is mandatory.");
		set_ValueNoCheck("M_Demand_ID", new Integer(M_Demand_ID));
	}

	/**
	 * Get Demand. Material Demand
	 */
	public int getM_Demand_ID() {
		Integer ii = (Integer) get_Value("M_Demand_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
