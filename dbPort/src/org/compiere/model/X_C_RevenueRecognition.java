/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_RevenueRecognition
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.406
 */
public class X_C_RevenueRecognition extends PO {
	/** Standard Constructor */
	public X_C_RevenueRecognition(Properties ctx, int C_RevenueRecognition_ID,
			String trxName) {
		super(ctx, C_RevenueRecognition_ID, trxName);
		/**
		 * if (C_RevenueRecognition_ID == 0) { setC_RevenueRecognition_ID (0);
		 * setIsTimeBased (false); setName (null); setRecognitionFrequency
		 * (null); }
		 */
	}

	/** Load Constructor */
	public X_C_RevenueRecognition(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_RevenueRecognition */
	public static final String Table_Name = "C_RevenueRecognition";

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
		StringBuffer sb = new StringBuffer("X_C_RevenueRecognition[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Revenue Recognition. Method for recording revenue
	 */
	public void setC_RevenueRecognition_ID(int C_RevenueRecognition_ID) {
		if (C_RevenueRecognition_ID < 1)
			throw new IllegalArgumentException(
					"C_RevenueRecognition_ID is mandatory.");
		set_ValueNoCheck("C_RevenueRecognition_ID", new Integer(
				C_RevenueRecognition_ID));
	}

	/**
	 * Get Revenue Recognition. Method for recording revenue
	 */
	public int getC_RevenueRecognition_ID() {
		Integer ii = (Integer) get_Value("C_RevenueRecognition_ID");
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
	 * Set Time based. Time based Revenue Recognition rather than Service Level
	 * based
	 */
	public void setIsTimeBased(boolean IsTimeBased) {
		set_Value("IsTimeBased", new Boolean(IsTimeBased));
	}

	/**
	 * Get Time based. Time based Revenue Recognition rather than Service Level
	 * based
	 */
	public boolean isTimeBased() {
		Object oo = get_Value("IsTimeBased");
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

	/** Set Number of Months */
	public void setNoMonths(int NoMonths) {
		set_Value("NoMonths", new Integer(NoMonths));
	}

	/** Get Number of Months */
	public int getNoMonths() {
		Integer ii = (Integer) get_Value("NoMonths");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** RecognitionFrequency AD_Reference_ID=196 */
	public static final int RECOGNITIONFREQUENCY_AD_Reference_ID = 196;

	/** Month = M */
	public static final String RECOGNITIONFREQUENCY_Month = "M";

	/** Quarter = Q */
	public static final String RECOGNITIONFREQUENCY_Quarter = "Q";

	/** Year = Y */
	public static final String RECOGNITIONFREQUENCY_Year = "Y";

	/** Set Recognition frequency */
	public void setRecognitionFrequency(String RecognitionFrequency) {
		if (RecognitionFrequency == null)
			throw new IllegalArgumentException(
					"RecognitionFrequency is mandatory");
		if (RecognitionFrequency.equals("M")
				|| RecognitionFrequency.equals("Q")
				|| RecognitionFrequency.equals("Y"))
			;
		else
			throw new IllegalArgumentException(
					"RecognitionFrequency Invalid value - "
							+ RecognitionFrequency
							+ " - Reference_ID=196 - M - Q - Y");
		if (RecognitionFrequency.length() > 1) {
			log.warning("Length > 1 - truncated");
			RecognitionFrequency = RecognitionFrequency.substring(0, 0);
		}
		set_Value("RecognitionFrequency", RecognitionFrequency);
	}

	/** Get Recognition frequency */
	public String getRecognitionFrequency() {
		return (String) get_Value("RecognitionFrequency");
	}
}
