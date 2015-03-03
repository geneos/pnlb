/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Recurring
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.328
 */
public class X_C_Recurring extends PO {
	/** Standard Constructor */
	public X_C_Recurring(Properties ctx, int C_Recurring_ID, String trxName) {
		super(ctx, C_Recurring_ID, trxName);
		/**
		 * if (C_Recurring_ID == 0) { setC_Recurring_ID (0); setDateNextRun (new
		 * Timestamp(System.currentTimeMillis())); setFrequencyType (null); // M
		 * setName (null); setRecurringType (null); setRunsMax (0);
		 * setRunsRemaining (0); }
		 */
	}

	/** Load Constructor */
	public X_C_Recurring(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Recurring */
	public static final String Table_Name = "C_Recurring";

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
		StringBuffer sb = new StringBuffer("X_C_Recurring[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Invoice. Invoice Identifier
	 */
	public void setC_Invoice_ID(int C_Invoice_ID) {
		if (C_Invoice_ID <= 0)
			set_Value("C_Invoice_ID", null);
		else
			set_Value("C_Invoice_ID", new Integer(C_Invoice_ID));
	}

	/**
	 * Get Invoice. Invoice Identifier
	 */
	public int getC_Invoice_ID() {
		Integer ii = (Integer) get_Value("C_Invoice_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Order. Order
	 */
	public void setC_Order_ID(int C_Order_ID) {
		if (C_Order_ID <= 0)
			set_Value("C_Order_ID", null);
		else
			set_Value("C_Order_ID", new Integer(C_Order_ID));
	}

	/**
	 * Get Order. Order
	 */
	public int getC_Order_ID() {
		Integer ii = (Integer) get_Value("C_Order_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment. Payment identifier
	 */
	public void setC_Payment_ID(int C_Payment_ID) {
		if (C_Payment_ID <= 0)
			set_Value("C_Payment_ID", null);
		else
			set_Value("C_Payment_ID", new Integer(C_Payment_ID));
	}

	/**
	 * Get Payment. Payment identifier
	 */
	public int getC_Payment_ID() {
		Integer ii = (Integer) get_Value("C_Payment_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Project. Financial Project
	 */
	public void setC_Project_ID(int C_Project_ID) {
		if (C_Project_ID <= 0)
			set_Value("C_Project_ID", null);
		else
			set_Value("C_Project_ID", new Integer(C_Project_ID));
	}

	/**
	 * Get Project. Financial Project
	 */
	public int getC_Project_ID() {
		Integer ii = (Integer) get_Value("C_Project_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Recurring. Recurring Document
	 */
	public void setC_Recurring_ID(int C_Recurring_ID) {
		if (C_Recurring_ID < 1)
			throw new IllegalArgumentException("C_Recurring_ID is mandatory.");
		set_ValueNoCheck("C_Recurring_ID", new Integer(C_Recurring_ID));
	}

	/**
	 * Get Recurring. Recurring Document
	 */
	public int getC_Recurring_ID() {
		Integer ii = (Integer) get_Value("C_Recurring_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Date last run. Date the process was last run.
	 */
	public void setDateLastRun(Timestamp DateLastRun) {
		set_ValueNoCheck("DateLastRun", DateLastRun);
	}

	/**
	 * Get Date last run. Date the process was last run.
	 */
	public Timestamp getDateLastRun() {
		return (Timestamp) get_Value("DateLastRun");
	}

	/**
	 * Set Date next run. Date the process will run next
	 */
	public void setDateNextRun(Timestamp DateNextRun) {
		if (DateNextRun == null)
			throw new IllegalArgumentException("DateNextRun is mandatory.");
		set_Value("DateNextRun", DateNextRun);
	}

	/**
	 * Get Date next run. Date the process will run next
	 */
	public Timestamp getDateNextRun() {
		return (Timestamp) get_Value("DateNextRun");
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
	 * Set Frequency. Frequency of events
	 */
	public void setFrequency(int Frequency) {
		set_Value("Frequency", new Integer(Frequency));
	}

	/**
	 * Get Frequency. Frequency of events
	 */
	public int getFrequency() {
		Integer ii = (Integer) get_Value("Frequency");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** FrequencyType AD_Reference_ID=283 */
	public static final int FREQUENCYTYPE_AD_Reference_ID = 283;

	/** Daily = D */
	public static final String FREQUENCYTYPE_Daily = "D";

	/** Monthly = M */
	public static final String FREQUENCYTYPE_Monthly = "M";

	/** Quarterly = Q */
	public static final String FREQUENCYTYPE_Quarterly = "Q";

	/** Weekly = W */
	public static final String FREQUENCYTYPE_Weekly = "W";

	/**
	 * Set Frequency Type. Frequency of event
	 */
	public void setFrequencyType(String FrequencyType) {
		if (FrequencyType == null)
			throw new IllegalArgumentException("FrequencyType is mandatory");
		if (FrequencyType.equals("D") || FrequencyType.equals("M")
				|| FrequencyType.equals("Q") || FrequencyType.equals("W"))
			;
		else
			throw new IllegalArgumentException("FrequencyType Invalid value - "
					+ FrequencyType + " - Reference_ID=283 - D - M - Q - W");
		if (FrequencyType.length() > 1) {
			log.warning("Length > 1 - truncated");
			FrequencyType = FrequencyType.substring(0, 0);
		}
		set_Value("FrequencyType", FrequencyType);
	}

	/**
	 * Get Frequency Type. Frequency of event
	 */
	public String getFrequencyType() {
		return (String) get_Value("FrequencyType");
	}

	/**
	 * Set Journal Batch. General Ledger Journal Batch
	 */
	public void setGL_JournalBatch_ID(int GL_JournalBatch_ID) {
		if (GL_JournalBatch_ID <= 0)
			set_Value("GL_JournalBatch_ID", null);
		else
			set_Value("GL_JournalBatch_ID", new Integer(GL_JournalBatch_ID));
	}

	/**
	 * Get Journal Batch. General Ledger Journal Batch
	 */
	public int getGL_JournalBatch_ID() {
		Integer ii = (Integer) get_Value("GL_JournalBatch_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/** RecurringType AD_Reference_ID=282 */
	public static final int RECURRINGTYPE_AD_Reference_ID = 282;

	/** GL Journal = G */
	public static final String RECURRINGTYPE_GLJournal = "G";

	/** Invoice = I */
	public static final String RECURRINGTYPE_Invoice = "I";

	/** Project = J */
	public static final String RECURRINGTYPE_Project = "J";

	/** Order = O */
	public static final String RECURRINGTYPE_Order = "O";

	/**
	 * Set Recurring Type. Type of Recurring Document
	 */
	public void setRecurringType(String RecurringType) {
		if (RecurringType == null)
			throw new IllegalArgumentException("RecurringType is mandatory");
		if (RecurringType.equals("G") || RecurringType.equals("I")
				|| RecurringType.equals("J") || RecurringType.equals("O"))
			;
		else
			throw new IllegalArgumentException("RecurringType Invalid value - "
					+ RecurringType + " - Reference_ID=282 - G - I - J - O");
		if (RecurringType.length() > 1) {
			log.warning("Length > 1 - truncated");
			RecurringType = RecurringType.substring(0, 0);
		}
		set_Value("RecurringType", RecurringType);
	}

	/**
	 * Get Recurring Type. Type of Recurring Document
	 */
	public String getRecurringType() {
		return (String) get_Value("RecurringType");
	}

	/**
	 * Set Maximum Runs. Number of recurring runs
	 */
	public void setRunsMax(int RunsMax) {
		set_Value("RunsMax", new Integer(RunsMax));
	}

	/**
	 * Get Maximum Runs. Number of recurring runs
	 */
	public int getRunsMax() {
		Integer ii = (Integer) get_Value("RunsMax");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Remaining Runs. Number of recurring runs remaining
	 */
	public void setRunsRemaining(int RunsRemaining) {
		set_ValueNoCheck("RunsRemaining", new Integer(RunsRemaining));
	}

	/**
	 * Get Remaining Runs. Number of recurring runs remaining
	 */
	public int getRunsRemaining() {
		Integer ii = (Integer) get_Value("RunsRemaining");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
