/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_ReportView_Col
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.203
 */
public class X_AD_ReportView_Col extends PO {
	/** Standard Constructor */
	public X_AD_ReportView_Col(Properties ctx, int AD_ReportView_Col_ID,
			String trxName) {
		super(ctx, AD_ReportView_Col_ID, trxName);
		/**
		 * if (AD_ReportView_Col_ID == 0) { setAD_ReportView_Col_ID (0);
		 * setAD_ReportView_ID (0); setFunctionColumn (null); setIsGroupFunction
		 * (false); }
		 */
	}

	/** Load Constructor */
	public X_AD_ReportView_Col(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_ReportView_Col */
	public static final String Table_Name = "AD_ReportView_Col";

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

	protected BigDecimal accessLevel = new BigDecimal(4);

	/** AccessLevel 4 - System */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_ReportView_Col[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Column. Column in the table
	 */
	public void setAD_Column_ID(int AD_Column_ID) {
		if (AD_Column_ID <= 0)
			set_Value("AD_Column_ID", null);
		else
			set_Value("AD_Column_ID", new Integer(AD_Column_ID));
	}

	/**
	 * Get Column. Column in the table
	 */
	public int getAD_Column_ID() {
		Integer ii = (Integer) get_Value("AD_Column_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Report view Column */
	public void setAD_ReportView_Col_ID(int AD_ReportView_Col_ID) {
		if (AD_ReportView_Col_ID < 1)
			throw new IllegalArgumentException(
					"AD_ReportView_Col_ID is mandatory.");
		set_ValueNoCheck("AD_ReportView_Col_ID", new Integer(
				AD_ReportView_Col_ID));
	}

	/** Get Report view Column */
	public int getAD_ReportView_Col_ID() {
		Integer ii = (Integer) get_Value("AD_ReportView_Col_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Report View. View used to generate this report
	 */
	public void setAD_ReportView_ID(int AD_ReportView_ID) {
		if (AD_ReportView_ID < 1)
			throw new IllegalArgumentException("AD_ReportView_ID is mandatory.");
		set_ValueNoCheck("AD_ReportView_ID", new Integer(AD_ReportView_ID));
	}

	/**
	 * Get Report View. View used to generate this report
	 */
	public int getAD_ReportView_ID() {
		Integer ii = (Integer) get_Value("AD_ReportView_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_ReportView_ID()));
	}

	/**
	 * Set Function Column. Overwrite Column with Function
	 */
	public void setFunctionColumn(String FunctionColumn) {
		if (FunctionColumn == null)
			throw new IllegalArgumentException("FunctionColumn is mandatory.");
		if (FunctionColumn.length() > 60) {
			log.warning("Length > 60 - truncated");
			FunctionColumn = FunctionColumn.substring(0, 59);
		}
		set_Value("FunctionColumn", FunctionColumn);
	}

	/**
	 * Get Function Column. Overwrite Column with Function
	 */
	public String getFunctionColumn() {
		return (String) get_Value("FunctionColumn");
	}

	/**
	 * Set SQL Group Function. This function will generate a Group By Clause
	 */
	public void setIsGroupFunction(boolean IsGroupFunction) {
		set_Value("IsGroupFunction", new Boolean(IsGroupFunction));
	}

	/**
	 * Get SQL Group Function. This function will generate a Group By Clause
	 */
	public boolean isGroupFunction() {
		Object oo = get_Value("IsGroupFunction");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
}
