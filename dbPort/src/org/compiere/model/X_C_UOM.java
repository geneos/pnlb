/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_UOM
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.781
 */
public class X_C_UOM extends PO {
	/** Standard Constructor */
	public X_C_UOM(Properties ctx, int C_UOM_ID, String trxName) {
		super(ctx, C_UOM_ID, trxName);
		/**
		 * if (C_UOM_ID == 0) { setC_UOM_ID (0); setCostingPrecision (0);
		 * setIsDefault (false); setName (null); setStdPrecision (0);
		 * setX12DE355 (null); }
		 */
	}

	/** Load Constructor */
	public X_C_UOM(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_UOM */
	public static final String Table_Name = "C_UOM";

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
		StringBuffer sb = new StringBuffer("X_C_UOM[").append(get_ID()).append(
				"]");
		return sb.toString();
	}

	/**
	 * Set UOM. Unit of Measure
	 */
	public void setC_UOM_ID(int C_UOM_ID) {
		if (C_UOM_ID < 1)
			throw new IllegalArgumentException("C_UOM_ID is mandatory.");
		set_ValueNoCheck("C_UOM_ID", new Integer(C_UOM_ID));
	}

	/**
	 * Get UOM. Unit of Measure
	 */
	public int getC_UOM_ID() {
		Integer ii = (Integer) get_Value("C_UOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Costing Precision. Rounding used costing calculations
	 */
	public void setCostingPrecision(int CostingPrecision) {
		set_Value("CostingPrecision", new Integer(CostingPrecision));
	}

	/**
	 * Get Costing Precision. Rounding used costing calculations
	 */
	public int getCostingPrecision() {
		Integer ii = (Integer) get_Value("CostingPrecision");
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

	/**
	 * Set Standard Precision. Rule for rounding calculated amounts
	 */
	public void setStdPrecision(int StdPrecision) {
		set_Value("StdPrecision", new Integer(StdPrecision));
	}

	/**
	 * Get Standard Precision. Rule for rounding calculated amounts
	 */
	public int getStdPrecision() {
		Integer ii = (Integer) get_Value("StdPrecision");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Symbol. Symbol for a Unit of Measure
	 */
	public void setUOMSymbol(String UOMSymbol) {
		if (UOMSymbol != null && UOMSymbol.length() > 10) {
			log.warning("Length > 10 - truncated");
			UOMSymbol = UOMSymbol.substring(0, 9);
		}
		set_Value("UOMSymbol", UOMSymbol);
	}

	/**
	 * Get Symbol. Symbol for a Unit of Measure
	 */
	public String getUOMSymbol() {
		return (String) get_Value("UOMSymbol");
	}

	/**
	 * Set UOM Code. UOM EDI X12 Code
	 */
	public void setX12DE355(String X12DE355) {
		if (X12DE355 == null)
			throw new IllegalArgumentException("X12DE355 is mandatory.");
		if (X12DE355.length() > 4) {
			log.warning("Length > 4 - truncated");
			X12DE355 = X12DE355.substring(0, 3);
		}
		set_Value("X12DE355", X12DE355);
	}

	/**
	 * Get UOM Code. UOM EDI X12 Code
	 */
	public String getX12DE355() {
		return (String) get_Value("X12DE355");
	}
}
