/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for R_RequestUpdate
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.656
 */
public class X_R_RequestUpdate extends PO {
	/** Standard Constructor */
	public X_R_RequestUpdate(Properties ctx, int R_RequestUpdate_ID,
			String trxName) {
		super(ctx, R_RequestUpdate_ID, trxName);
		/**
		 * if (R_RequestUpdate_ID == 0) { setConfidentialTypeEntry (null);
		 * setR_RequestUpdate_ID (0); setR_Request_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_R_RequestUpdate(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=R_RequestUpdate */
	public static final String Table_Name = "R_RequestUpdate";

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

	protected BigDecimal accessLevel = new BigDecimal(7);

	/** AccessLevel 7 - System - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_R_RequestUpdate[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** ConfidentialTypeEntry AD_Reference_ID=340 */
	public static final int CONFIDENTIALTYPEENTRY_AD_Reference_ID = 340;

	/** Public Information = A */
	public static final String CONFIDENTIALTYPEENTRY_PublicInformation = "A";

	/** Customer Confidential = C */
	public static final String CONFIDENTIALTYPEENTRY_CustomerConfidential = "C";

	/** Internal = I */
	public static final String CONFIDENTIALTYPEENTRY_Internal = "I";

	/** Private Information = P */
	public static final String CONFIDENTIALTYPEENTRY_PrivateInformation = "P";

	/**
	 * Set Entry Confidentiality. Confidentiality of the individual entry
	 */
	public void setConfidentialTypeEntry(String ConfidentialTypeEntry) {
		if (ConfidentialTypeEntry == null)
			throw new IllegalArgumentException(
					"ConfidentialTypeEntry is mandatory");
		if (ConfidentialTypeEntry.equals("A")
				|| ConfidentialTypeEntry.equals("C")
				|| ConfidentialTypeEntry.equals("I")
				|| ConfidentialTypeEntry.equals("P"))
			;
		else
			throw new IllegalArgumentException(
					"ConfidentialTypeEntry Invalid value - "
							+ ConfidentialTypeEntry
							+ " - Reference_ID=340 - A - C - I - P");
		if (ConfidentialTypeEntry.length() > 1) {
			log.warning("Length > 1 - truncated");
			ConfidentialTypeEntry = ConfidentialTypeEntry.substring(0, 0);
		}
		set_Value("ConfidentialTypeEntry", ConfidentialTypeEntry);
	}

	/**
	 * Get Entry Confidentiality. Confidentiality of the individual entry
	 */
	public String getConfidentialTypeEntry() {
		return (String) get_Value("ConfidentialTypeEntry");
	}

	/**
	 * Set End Time. End of the time span
	 */
	public void setEndTime(Timestamp EndTime) {
		set_Value("EndTime", EndTime);
	}

	/**
	 * Get End Time. End of the time span
	 */
	public Timestamp getEndTime() {
		return (Timestamp) get_Value("EndTime");
	}

	/** M_ProductSpent_ID AD_Reference_ID=162 */
	public static final int M_PRODUCTSPENT_ID_AD_Reference_ID = 162;

	/**
	 * Set Product Used. Product/Resource/Service used in Request
	 */
	public void setM_ProductSpent_ID(int M_ProductSpent_ID) {
		if (M_ProductSpent_ID <= 0)
			set_Value("M_ProductSpent_ID", null);
		else
			set_Value("M_ProductSpent_ID", new Integer(M_ProductSpent_ID));
	}

	/**
	 * Get Product Used. Product/Resource/Service used in Request
	 */
	public int getM_ProductSpent_ID() {
		Integer ii = (Integer) get_Value("M_ProductSpent_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Quantity Invoiced. Invoiced Quantity
	 */
	public void setQtyInvoiced(BigDecimal QtyInvoiced) {
		set_Value("QtyInvoiced", QtyInvoiced);
	}

	/**
	 * Get Quantity Invoiced. Invoiced Quantity
	 */
	public BigDecimal getQtyInvoiced() {
		BigDecimal bd = (BigDecimal) get_Value("QtyInvoiced");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Quantity Used. Quantity used for this event
	 */
	public void setQtySpent(BigDecimal QtySpent) {
		set_Value("QtySpent", QtySpent);
	}

	/**
	 * Get Quantity Used. Quantity used for this event
	 */
	public BigDecimal getQtySpent() {
		BigDecimal bd = (BigDecimal) get_Value("QtySpent");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Request Update. Request Updates
	 */
	public void setR_RequestUpdate_ID(int R_RequestUpdate_ID) {
		if (R_RequestUpdate_ID < 1)
			throw new IllegalArgumentException(
					"R_RequestUpdate_ID is mandatory.");
		set_ValueNoCheck("R_RequestUpdate_ID", new Integer(R_RequestUpdate_ID));
	}

	/**
	 * Get Request Update. Request Updates
	 */
	public int getR_RequestUpdate_ID() {
		Integer ii = (Integer) get_Value("R_RequestUpdate_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getR_RequestUpdate_ID()));
	}

	/**
	 * Set Request. Request from a Business Partner or Prospect
	 */
	public void setR_Request_ID(int R_Request_ID) {
		if (R_Request_ID < 1)
			throw new IllegalArgumentException("R_Request_ID is mandatory.");
		set_ValueNoCheck("R_Request_ID", new Integer(R_Request_ID));
	}

	/**
	 * Get Request. Request from a Business Partner or Prospect
	 */
	public int getR_Request_ID() {
		Integer ii = (Integer) get_Value("R_Request_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Result. Result of the action taken
	 */
	public void setResult(String Result) {
		if (Result != null && Result.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Result = Result.substring(0, 1999);
		}
		set_ValueNoCheck("Result", Result);
	}

	/**
	 * Get Result. Result of the action taken
	 */
	public String getResult() {
		return (String) get_Value("Result");
	}

	/**
	 * Set Start Time. Time started
	 */
	public void setStartTime(Timestamp StartTime) {
		set_Value("StartTime", StartTime);
	}

	/**
	 * Get Start Time. Time started
	 */
	public Timestamp getStartTime() {
		return (Timestamp) get_Value("StartTime");
	}
}
