/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_ReportLineSet
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.093
 */
public class X_PA_ReportLineSet extends PO {
	/** Standard Constructor */
	public X_PA_ReportLineSet(Properties ctx, int PA_ReportLineSet_ID,
			String trxName) {
		super(ctx, PA_ReportLineSet_ID, trxName);
		/**
		 * if (PA_ReportLineSet_ID == 0) { setName (null);
		 * setPA_ReportLineSet_ID (0); setProcessing (false); }
		 */
	}

	/** Load Constructor */
	public X_PA_ReportLineSet(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_ReportLineSet */
	public static final String Table_Name = "PA_ReportLineSet";

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
		StringBuffer sb = new StringBuffer("X_PA_ReportLineSet[").append(
				get_ID()).append("]");
		return sb.toString();
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

	/** Set Report Line Set */
	public void setPA_ReportLineSet_ID(int PA_ReportLineSet_ID) {
		if (PA_ReportLineSet_ID < 1)
			throw new IllegalArgumentException(
					"PA_ReportLineSet_ID is mandatory.");
		set_ValueNoCheck("PA_ReportLineSet_ID",
				new Integer(PA_ReportLineSet_ID));
	}

	/** Get Report Line Set */
	public int getPA_ReportLineSet_ID() {
		Integer ii = (Integer) get_Value("PA_ReportLineSet_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
