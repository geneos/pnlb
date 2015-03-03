/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Region
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.375
 */
public class X_C_Region extends PO {
	/** Standard Constructor */
	public X_C_Region(Properties ctx, int C_Region_ID, String trxName) {
		super(ctx, C_Region_ID, trxName);
		/**
		 * if (C_Region_ID == 0) { setC_Country_ID (0); setC_Region_ID (0);
		 * setName (null); }
		 */
	}

	/** Load Constructor */
	public X_C_Region(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Region */
	public static final String Table_Name = "C_Region";

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
		StringBuffer sb = new StringBuffer("X_C_Region[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** Set COEFICIENTEJURISDICCION */
	public void setCOEFICIENTEJURISDICCION(BigDecimal COEFICIENTEJURISDICCION) {
		set_Value("COEFICIENTEJURISDICCION", COEFICIENTEJURISDICCION);
	}

	/** Get COEFICIENTEJURISDICCION */
	public BigDecimal getCOEFICIENTEJURISDICCION() {
		BigDecimal bd = (BigDecimal) get_Value("COEFICIENTEJURISDICCION");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Country. Country
	 */
	public void setC_Country_ID(int C_Country_ID) {
		if (C_Country_ID < 1)
			throw new IllegalArgumentException("C_Country_ID is mandatory.");
		set_ValueNoCheck("C_Country_ID", new Integer(C_Country_ID));
	}

	/**
	 * Get Country. Country
	 */
	public int getC_Country_ID() {
		Integer ii = (Integer) get_Value("C_Country_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Region. Identifies a geographical Region
	 */
	public void setC_Region_ID(int C_Region_ID) {
		if (C_Region_ID < 1)
			throw new IllegalArgumentException("C_Region_ID is mandatory.");
		set_ValueNoCheck("C_Region_ID", new Integer(C_Region_ID));
	}

	/**
	 * Get Region. Identifies a geographical Region
	 */
	public int getC_Region_ID() {
		Integer ii = (Integer) get_Value("C_Region_ID");
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
