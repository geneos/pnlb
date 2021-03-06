/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_InvoicePaySchedule
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.578
 */
public class X_C_InvoicePaySchedule extends PO {
	/** Standard Constructor */
	public X_C_InvoicePaySchedule(Properties ctx, int C_InvoicePaySchedule_ID,
			String trxName) {
		super(ctx, C_InvoicePaySchedule_ID, trxName);
		/**
		 * if (C_InvoicePaySchedule_ID == 0) { setC_InvoicePaySchedule_ID (0);
		 * setC_Invoice_ID (0); setDiscountAmt (Env.ZERO); setDiscountDate (new
		 * Timestamp(System.currentTimeMillis())); setDueAmt (Env.ZERO);
		 * setDueDate (new Timestamp(System.currentTimeMillis())); setIsValid
		 * (false); setProcessed (false); }
		 */
	}

	/** Load Constructor */
	public X_C_InvoicePaySchedule(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_InvoicePaySchedule */
	public static final String Table_Name = "C_InvoicePaySchedule";

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

	protected BigDecimal accessLevel = new BigDecimal(1);

	/** AccessLevel 1 - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_InvoicePaySchedule[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Invoice Payment Schedule. Invoice Payment Schedule
	 */
	public void setC_InvoicePaySchedule_ID(int C_InvoicePaySchedule_ID) {
		if (C_InvoicePaySchedule_ID < 1)
			throw new IllegalArgumentException(
					"C_InvoicePaySchedule_ID is mandatory.");
		set_ValueNoCheck("C_InvoicePaySchedule_ID", new Integer(
				C_InvoicePaySchedule_ID));
	}

	/**
	 * Get Invoice Payment Schedule. Invoice Payment Schedule
	 */
	public int getC_InvoicePaySchedule_ID() {
		Integer ii = (Integer) get_Value("C_InvoicePaySchedule_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Invoice. Invoice Identifier
	 */
	public void setC_Invoice_ID(int C_Invoice_ID) {
		if (C_Invoice_ID < 1)
			throw new IllegalArgumentException("C_Invoice_ID is mandatory.");
		set_ValueNoCheck("C_Invoice_ID", new Integer(C_Invoice_ID));
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
	 * Set Payment Schedule. Payment Schedule Template
	 */
	public void setC_PaySchedule_ID(int C_PaySchedule_ID) {
		if (C_PaySchedule_ID <= 0)
			set_ValueNoCheck("C_PaySchedule_ID", null);
		else
			set_ValueNoCheck("C_PaySchedule_ID", new Integer(C_PaySchedule_ID));
	}

	/**
	 * Get Payment Schedule. Payment Schedule Template
	 */
	public int getC_PaySchedule_ID() {
		Integer ii = (Integer) get_Value("C_PaySchedule_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Discount Amount. Calculated amount of discount
	 */
	public void setDiscountAmt(BigDecimal DiscountAmt) {
		if (DiscountAmt == null)
			throw new IllegalArgumentException("DiscountAmt is mandatory.");
		set_Value("DiscountAmt", DiscountAmt);
	}

	/**
	 * Get Discount Amount. Calculated amount of discount
	 */
	public BigDecimal getDiscountAmt() {
		BigDecimal bd = (BigDecimal) get_Value("DiscountAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Discount Date. Last Date for payments with discount
	 */
	public void setDiscountDate(Timestamp DiscountDate) {
		if (DiscountDate == null)
			throw new IllegalArgumentException("DiscountDate is mandatory.");
		set_Value("DiscountDate", DiscountDate);
	}

	/**
	 * Get Discount Date. Last Date for payments with discount
	 */
	public Timestamp getDiscountDate() {
		return (Timestamp) get_Value("DiscountDate");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getDiscountDate()));
	}

	/**
	 * Set Amount due. Amount of the payment due
	 */
	public void setDueAmt(BigDecimal DueAmt) {
		if (DueAmt == null)
			throw new IllegalArgumentException("DueAmt is mandatory.");
		set_Value("DueAmt", DueAmt);
	}

	/**
	 * Get Amount due. Amount of the payment due
	 */
	public BigDecimal getDueAmt() {
		BigDecimal bd = (BigDecimal) get_Value("DueAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Due Date. Date when the payment is due
	 */
	public void setDueDate(Timestamp DueDate) {
		if (DueDate == null)
			throw new IllegalArgumentException("DueDate is mandatory.");
		set_Value("DueDate", DueDate);
	}

	/**
	 * Get Due Date. Date when the payment is due
	 */
	public Timestamp getDueDate() {
		return (Timestamp) get_Value("DueDate");
	}

	/**
	 * Set Valid. Element is valid
	 */
	public void setIsValid(boolean IsValid) {
		set_Value("IsValid", new Boolean(IsValid));
	}

	/**
	 * Get Valid. Element is valid
	 */
	public boolean isValid() {
		Object oo = get_Value("IsValid");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
}
