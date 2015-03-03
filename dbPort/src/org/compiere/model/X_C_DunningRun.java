/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_DunningRun
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.359
 */
public class X_C_DunningRun extends PO {
	/** Standard Constructor */
	public X_C_DunningRun(Properties ctx, int C_DunningRun_ID, String trxName) {
		super(ctx, C_DunningRun_ID, trxName);
		/**
		 * if (C_DunningRun_ID == 0) { setC_DunningLevel_ID (0);
		 * setC_DunningRun_ID (0); setDunningDate (new
		 * Timestamp(System.currentTimeMillis())); //
		 * 
		 * @#Date@ setProcessed (false); }
		 */
	}

	/** Load Constructor */
	public X_C_DunningRun(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_DunningRun */
	public static final String Table_Name = "C_DunningRun";

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
		StringBuffer sb = new StringBuffer("X_C_DunningRun[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** Set Dunning Level */
	public void setC_DunningLevel_ID(int C_DunningLevel_ID) {
		if (C_DunningLevel_ID < 1)
			throw new IllegalArgumentException(
					"C_DunningLevel_ID is mandatory.");
		set_ValueNoCheck("C_DunningLevel_ID", new Integer(C_DunningLevel_ID));
	}

	/** Get Dunning Level */
	public int getC_DunningLevel_ID() {
		Integer ii = (Integer) get_Value("C_DunningLevel_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Dunning Run. Dunning Run
	 */
	public void setC_DunningRun_ID(int C_DunningRun_ID) {
		if (C_DunningRun_ID < 1)
			throw new IllegalArgumentException("C_DunningRun_ID is mandatory.");
		set_ValueNoCheck("C_DunningRun_ID", new Integer(C_DunningRun_ID));
	}

	/**
	 * Get Dunning Run. Dunning Run
	 */
	public int getC_DunningRun_ID() {
		Integer ii = (Integer) get_Value("C_DunningRun_ID");
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
	 * Set Dunning Date. Date of Dunning
	 */
	public void setDunningDate(Timestamp DunningDate) {
		if (DunningDate == null)
			throw new IllegalArgumentException("DunningDate is mandatory.");
		set_Value("DunningDate", DunningDate);
	}

	/**
	 * Get Dunning Date. Date of Dunning
	 */
	public Timestamp getDunningDate() {
		return (Timestamp) get_Value("DunningDate");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getDunningDate()));
	}

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_Value("Processed", new Boolean(Processed));
	}

	/**
	 * Get Processed. The document has been processed
	 */
	public boolean isProcessed() {
		Object oo = get_Value("Processed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Send */
	public void setSendIt(String SendIt) {
		if (SendIt != null && SendIt.length() > 1) {
			log.warning("Length > 1 - truncated");
			SendIt = SendIt.substring(0, 0);
		}
		set_Value("SendIt", SendIt);
	}

	/** Get Send */
	public String getSendIt() {
		return (String) get_Value("SendIt");
	}
}
