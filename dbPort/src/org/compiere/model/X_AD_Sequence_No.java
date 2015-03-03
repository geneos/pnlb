/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Sequence_No
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.328
 */
public class X_AD_Sequence_No extends PO {
	/** Standard Constructor */
	public X_AD_Sequence_No(Properties ctx, int AD_Sequence_No_ID,
			String trxName) {
		super(ctx, AD_Sequence_No_ID, trxName);
		/**
		 * if (AD_Sequence_No_ID == 0) { setAD_Sequence_ID (0); setCurrentNext
		 * (0); setYear (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Sequence_No(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Sequence_No */
	public static final String Table_Name = "AD_Sequence_No";

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
		StringBuffer sb = new StringBuffer("X_AD_Sequence_No[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Sequence. Document Sequence
	 */
	public void setAD_Sequence_ID(int AD_Sequence_ID) {
		if (AD_Sequence_ID < 1)
			throw new IllegalArgumentException("AD_Sequence_ID is mandatory.");
		set_ValueNoCheck("AD_Sequence_ID", new Integer(AD_Sequence_ID));
	}

	/**
	 * Get Sequence. Document Sequence
	 */
	public int getAD_Sequence_ID() {
		Integer ii = (Integer) get_Value("AD_Sequence_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Current Next. The next number to be used
	 */
	public void setCurrentNext(int CurrentNext) {
		set_Value("CurrentNext", new Integer(CurrentNext));
	}

	/**
	 * Get Current Next. The next number to be used
	 */
	public int getCurrentNext() {
		Integer ii = (Integer) get_Value("CurrentNext");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Year. Calendar Year
	 */
	public void setYear(String Year) {
		if (Year == null)
			throw new IllegalArgumentException("Year is mandatory.");
		if (Year.length() > 4) {
			log.warning("Length > 4 - truncated");
			Year = Year.substring(0, 3);
		}
		set_ValueNoCheck("Year", Year);
	}

	/**
	 * Get Year. Calendar Year
	 */
	public String getYear() {
		return (String) get_Value("Year");
	}
}
