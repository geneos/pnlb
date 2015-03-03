/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Currency
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.203
 */
public class X_C_Currency extends PO {
	/** Standard Constructor */
	public X_C_Currency(Properties ctx, int C_Currency_ID, String trxName) {
		super(ctx, C_Currency_ID, trxName);
		/**
		 * if (C_Currency_ID == 0) { setC_Currency_ID (0); setCostingPrecision
		 * (0); // 4 setDescription (null); setISO_Code (null); setIsEMUMember
		 * (false); // N setIsEuro (false); // N setStdPrecision (0); // 2 }
		 */
	}

	/** Load Constructor */
	public X_C_Currency(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Currency */
	public static final String Table_Name = "C_Currency";

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
		StringBuffer sb = new StringBuffer("X_C_Currency[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Currency. The Currency for this record
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		if (C_Currency_ID < 1)
			throw new IllegalArgumentException("C_Currency_ID is mandatory.");
		set_ValueNoCheck("C_Currency_ID", new Integer(C_Currency_ID));
	}

	/**
	 * Get Currency. The Currency for this record
	 */
	public int getC_Currency_ID() {
		Integer ii = (Integer) get_Value("C_Currency_ID");
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
	 * Set Symbol. Symbol of the currency (opt used for printing only)
	 */
	public void setCurSymbol(String CurSymbol) {
		if (CurSymbol != null && CurSymbol.length() > 10) {
			log.warning("Length > 10 - truncated");
			CurSymbol = CurSymbol.substring(0, 9);
		}
		set_Value("CurSymbol", CurSymbol);
	}

	/**
	 * Get Symbol. Symbol of the currency (opt used for printing only)
	 */
	public String getCurSymbol() {
		return (String) get_Value("CurSymbol");
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description == null)
			throw new IllegalArgumentException("Description is mandatory.");
		if (Description.length() > 255) {
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
	 * Set EMU Entry Date. Date when the currency joined / will join the EMU
	 */
	public void setEMUEntryDate(Timestamp EMUEntryDate) {
		set_Value("EMUEntryDate", EMUEntryDate);
	}

	/**
	 * Get EMU Entry Date. Date when the currency joined / will join the EMU
	 */
	public Timestamp getEMUEntryDate() {
		return (Timestamp) get_Value("EMUEntryDate");
	}

	/**
	 * Set EMU Rate. Official rate to the Euro
	 */
	public void setEMURate(BigDecimal EMURate) {
		set_Value("EMURate", EMURate);
	}

	/**
	 * Get EMU Rate. Official rate to the Euro
	 */
	public BigDecimal getEMURate() {
		BigDecimal bd = (BigDecimal) get_Value("EMURate");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set ISO Currency Code. Three letter ISO 4217 Code of the Currency
	 */
	public void setISO_Code(String ISO_Code) {
		if (ISO_Code == null)
			throw new IllegalArgumentException("ISO_Code is mandatory.");
		if (ISO_Code.length() > 3) {
			log.warning("Length > 3 - truncated");
			ISO_Code = ISO_Code.substring(0, 2);
		}
		set_Value("ISO_Code", ISO_Code);
	}

	/**
	 * Get ISO Currency Code. Three letter ISO 4217 Code of the Currency
	 */
	public String getISO_Code() {
		return (String) get_Value("ISO_Code");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getISO_Code());
	}

	/**
	 * Set EMU Member. This currency is member if the European Monetary Union
	 */
	public void setIsEMUMember(boolean IsEMUMember) {
		set_Value("IsEMUMember", new Boolean(IsEMUMember));
	}

	/**
	 * Get EMU Member. This currency is member if the European Monetary Union
	 */
	public boolean isEMUMember() {
		Object oo = get_Value("IsEMUMember");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set The Euro Currency. This currency is the Euro
	 */
	public void setIsEuro(boolean IsEuro) {
		set_Value("IsEuro", new Boolean(IsEuro));
	}

	/**
	 * Get The Euro Currency. This currency is the Euro
	 */
	public boolean isEuro() {
		Object oo = get_Value("IsEuro");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
}
