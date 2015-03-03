/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_LabelPrinterFunction
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.562
 */
public class X_AD_LabelPrinterFunction extends PO {
	/** Standard Constructor */
	public X_AD_LabelPrinterFunction(Properties ctx,
			int AD_LabelPrinterFunction_ID, String trxName) {
		super(ctx, AD_LabelPrinterFunction_ID, trxName);
		/**
		 * if (AD_LabelPrinterFunction_ID == 0) { setAD_LabelPrinterFunction_ID
		 * (0); setAD_LabelPrinter_ID (0); setIsXYPosition (false); setName
		 * (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_LabelPrinterFunction(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_LabelPrinterFunction */
	public static final String Table_Name = "AD_LabelPrinterFunction";

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
		StringBuffer sb = new StringBuffer("X_AD_LabelPrinterFunction[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Label printer Function. Function of Label Printer
	 */
	public void setAD_LabelPrinterFunction_ID(int AD_LabelPrinterFunction_ID) {
		if (AD_LabelPrinterFunction_ID < 1)
			throw new IllegalArgumentException(
					"AD_LabelPrinterFunction_ID is mandatory.");
		set_ValueNoCheck("AD_LabelPrinterFunction_ID", new Integer(
				AD_LabelPrinterFunction_ID));
	}

	/**
	 * Get Label printer Function. Function of Label Printer
	 */
	public int getAD_LabelPrinterFunction_ID() {
		Integer ii = (Integer) get_Value("AD_LabelPrinterFunction_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Label printer. Label Printer Definition
	 */
	public void setAD_LabelPrinter_ID(int AD_LabelPrinter_ID) {
		if (AD_LabelPrinter_ID < 1)
			throw new IllegalArgumentException(
					"AD_LabelPrinter_ID is mandatory.");
		set_ValueNoCheck("AD_LabelPrinter_ID", new Integer(AD_LabelPrinter_ID));
	}

	/**
	 * Get Label printer. Label Printer Definition
	 */
	public int getAD_LabelPrinter_ID() {
		Integer ii = (Integer) get_Value("AD_LabelPrinter_ID");
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
	 * Set Function Prefix. Data sent before the function
	 */
	public void setFunctionPrefix(String FunctionPrefix) {
		if (FunctionPrefix != null && FunctionPrefix.length() > 40) {
			log.warning("Length > 40 - truncated");
			FunctionPrefix = FunctionPrefix.substring(0, 39);
		}
		set_Value("FunctionPrefix", FunctionPrefix);
	}

	/**
	 * Get Function Prefix. Data sent before the function
	 */
	public String getFunctionPrefix() {
		return (String) get_Value("FunctionPrefix");
	}

	/**
	 * Set Function Suffix. Data sent after the function
	 */
	public void setFunctionSuffix(String FunctionSuffix) {
		if (FunctionSuffix != null && FunctionSuffix.length() > 40) {
			log.warning("Length > 40 - truncated");
			FunctionSuffix = FunctionSuffix.substring(0, 39);
		}
		set_Value("FunctionSuffix", FunctionSuffix);
	}

	/**
	 * Get Function Suffix. Data sent after the function
	 */
	public String getFunctionSuffix() {
		return (String) get_Value("FunctionSuffix");
	}

	/**
	 * Set XY Position. The Function is XY position
	 */
	public void setIsXYPosition(boolean IsXYPosition) {
		set_Value("IsXYPosition", new Boolean(IsXYPosition));
	}

	/**
	 * Get XY Position. The Function is XY position
	 */
	public boolean isXYPosition() {
		Object oo = get_Value("IsXYPosition");
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
	 * Set XY Separator. The separator between the X and Y function.
	 */
	public void setXYSeparator(String XYSeparator) {
		if (XYSeparator != null && XYSeparator.length() > 20) {
			log.warning("Length > 20 - truncated");
			XYSeparator = XYSeparator.substring(0, 19);
		}
		set_Value("XYSeparator", XYSeparator);
	}

	/**
	 * Get XY Separator. The separator between the X and Y function.
	 */
	public String getXYSeparator() {
		return (String) get_Value("XYSeparator");
	}
}
