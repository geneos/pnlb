/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_Report
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.015
 */
public class X_PA_Report extends PO {
	/** Standard Constructor */
	public X_PA_Report(Properties ctx, int PA_Report_ID, String trxName) {
		super(ctx, PA_Report_ID, trxName);
		/**
		 * if (PA_Report_ID == 0) { setC_AcctSchema_ID (0); setC_Calendar_ID
		 * (0); setListSources (false); setListTrx (false); setName (null);
		 * setPA_ReportColumnSet_ID (0); setPA_ReportLineSet_ID (0);
		 * setPA_Report_ID (0); setProcessing (false); }
		 */
	}

	/** Load Constructor */
	public X_PA_Report(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_Report */
	public static final String Table_Name = "PA_Report";

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
		StringBuffer sb = new StringBuffer("X_PA_Report[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Print Format. Data Print Format
	 */
	public void setAD_PrintFormat_ID(int AD_PrintFormat_ID) {
		if (AD_PrintFormat_ID <= 0)
			set_Value("AD_PrintFormat_ID", null);
		else
			set_Value("AD_PrintFormat_ID", new Integer(AD_PrintFormat_ID));
	}

	/**
	 * Get Print Format. Data Print Format
	 */
	public int getAD_PrintFormat_ID() {
		Integer ii = (Integer) get_Value("AD_PrintFormat_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Accounting Schema. Rules for accounting
	 */
	public void setC_AcctSchema_ID(int C_AcctSchema_ID) {
		if (C_AcctSchema_ID < 1)
			throw new IllegalArgumentException("C_AcctSchema_ID is mandatory.");
		set_Value("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
	}

	/**
	 * Get Accounting Schema. Rules for accounting
	 */
	public int getC_AcctSchema_ID() {
		Integer ii = (Integer) get_Value("C_AcctSchema_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Calendar. Accounting Calendar Name
	 */
	public void setC_Calendar_ID(int C_Calendar_ID) {
		if (C_Calendar_ID < 1)
			throw new IllegalArgumentException("C_Calendar_ID is mandatory.");
		set_Value("C_Calendar_ID", new Integer(C_Calendar_ID));
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
	 * Set List Sources. List Report Line Sources
	 */
	public void setListSources(boolean ListSources) {
		set_Value("ListSources", new Boolean(ListSources));
	}

	/**
	 * Get List Sources. List Report Line Sources
	 */
	public boolean isListSources() {
		Object oo = get_Value("ListSources");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set List Transactions. List the report transactions
	 */
	public void setListTrx(boolean ListTrx) {
		set_Value("ListTrx", new Boolean(ListTrx));
	}

	/**
	 * Get List Transactions. List the report transactions
	 */
	public boolean isListTrx() {
		Object oo = get_Value("ListTrx");
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
	 * Set Report Column Set. Collection of Columns for Report
	 */
	public void setPA_ReportColumnSet_ID(int PA_ReportColumnSet_ID) {
		if (PA_ReportColumnSet_ID < 1)
			throw new IllegalArgumentException(
					"PA_ReportColumnSet_ID is mandatory.");
		set_Value("PA_ReportColumnSet_ID", new Integer(PA_ReportColumnSet_ID));
	}

	/**
	 * Get Report Column Set. Collection of Columns for Report
	 */
	public int getPA_ReportColumnSet_ID() {
		Integer ii = (Integer) get_Value("PA_ReportColumnSet_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Report Line Set */
	public void setPA_ReportLineSet_ID(int PA_ReportLineSet_ID) {
		if (PA_ReportLineSet_ID < 1)
			throw new IllegalArgumentException(
					"PA_ReportLineSet_ID is mandatory.");
		set_Value("PA_ReportLineSet_ID", new Integer(PA_ReportLineSet_ID));
	}

	/** Get Report Line Set */
	public int getPA_ReportLineSet_ID() {
		Integer ii = (Integer) get_Value("PA_ReportLineSet_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Financial Report. Financial Report
	 */
	public void setPA_Report_ID(int PA_Report_ID) {
		if (PA_Report_ID < 1)
			throw new IllegalArgumentException("PA_Report_ID is mandatory.");
		set_ValueNoCheck("PA_Report_ID", new Integer(PA_Report_ID));
	}

	/**
	 * Get Financial Report. Financial Report
	 */
	public int getPA_Report_ID() {
		Integer ii = (Integer) get_Value("PA_Report_ID");
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
