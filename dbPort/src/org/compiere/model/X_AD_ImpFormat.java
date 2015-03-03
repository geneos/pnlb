/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_ImpFormat
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.5
 */
public class X_AD_ImpFormat extends PO {
	/** Standard Constructor */
	public X_AD_ImpFormat(Properties ctx, int AD_ImpFormat_ID, String trxName) {
		super(ctx, AD_ImpFormat_ID, trxName);
		/**
		 * if (AD_ImpFormat_ID == 0) { setAD_ImpFormat_ID (0); setAD_Table_ID
		 * (0); setFormatType (null); setName (null); setProcessing (false); }
		 */
	}

	/** Load Constructor */
	public X_AD_ImpFormat(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_ImpFormat */
	public static final String Table_Name = "AD_ImpFormat";

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
		StringBuffer sb = new StringBuffer("X_AD_ImpFormat[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** Set Import Format */
	public void setAD_ImpFormat_ID(int AD_ImpFormat_ID) {
		if (AD_ImpFormat_ID < 1)
			throw new IllegalArgumentException("AD_ImpFormat_ID is mandatory.");
		set_ValueNoCheck("AD_ImpFormat_ID", new Integer(AD_ImpFormat_ID));
	}

	/** Get Import Format */
	public int getAD_ImpFormat_ID() {
		Integer ii = (Integer) get_Value("AD_ImpFormat_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID < 1)
			throw new IllegalArgumentException("AD_Table_ID is mandatory.");
		set_Value("AD_Table_ID", new Integer(AD_Table_ID));
	}

	/**
	 * Get Table. Table for the Fields
	 */
	public int getAD_Table_ID() {
		Integer ii = (Integer) get_Value("AD_Table_ID");
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

	/** FormatType AD_Reference_ID=209 */
	public static final int FORMATTYPE_AD_Reference_ID = 209;

	/** Comma Separated = C */
	public static final String FORMATTYPE_CommaSeparated = "C";

	/** Fixed Position = F */
	public static final String FORMATTYPE_FixedPosition = "F";

	/** Tab Separated = T */
	public static final String FORMATTYPE_TabSeparated = "T";

	/** XML = X */
	public static final String FORMATTYPE_XML = "X";

	/**
	 * Set Format. Format of the data
	 */
	public void setFormatType(String FormatType) {
		if (FormatType == null)
			throw new IllegalArgumentException("FormatType is mandatory");
		if (FormatType.equals("C") || FormatType.equals("F")
				|| FormatType.equals("T") || FormatType.equals("X"))
			;
		else
			throw new IllegalArgumentException("FormatType Invalid value - "
					+ FormatType + " - Reference_ID=209 - C - F - T - X");
		if (FormatType.length() > 1) {
			log.warning("Length > 1 - truncated");
			FormatType = FormatType.substring(0, 0);
		}
		set_Value("FormatType", FormatType);
	}

	/**
	 * Get Format. Format of the data
	 */
	public String getFormatType() {
		return (String) get_Value("FormatType");
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
