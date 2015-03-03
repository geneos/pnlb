/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_PaymentAllocate
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.125
 */
public class X_C_PaymentAllocate extends PO {
	/** Standard Constructor */
	public X_C_PaymentAllocate(Properties ctx, int C_PaymentAllocate_ID,
			String trxName) {
		super(ctx, C_PaymentAllocate_ID, trxName);
		/**
		 * if (C_PaymentAllocate_ID == 0) { setAmount (Env.ZERO);
		 * setC_Invoice_ID (0); setC_PaymentAllocate_ID (0); setC_Payment_ID
		 * (0); setDiscountAmt (Env.ZERO); setOverUnderAmt (Env.ZERO);
		 * setWriteOffAmt (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_PaymentAllocate(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_PaymentAllocate */
	public static final String Table_Name = "C_PaymentAllocate";

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
		StringBuffer sb = new StringBuffer("X_C_PaymentAllocate[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Amount. Amount in a defined currency
	 */
	public void setAmount(BigDecimal Amount) {
		if (Amount == null)
			throw new IllegalArgumentException("Amount is mandatory.");
		set_Value("Amount", Amount);
	}

	/**
	 * Get Amount. Amount in a defined currency
	 */
	public BigDecimal getAmount() {
		BigDecimal bd = (BigDecimal) get_Value("Amount");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Allocation Line. Allocation Line
	 */
	public void setC_AllocationLine_ID(int C_AllocationLine_ID) {
		if (C_AllocationLine_ID <= 0)
			set_Value("C_AllocationLine_ID", null);
		else
			set_Value("C_AllocationLine_ID", new Integer(C_AllocationLine_ID));
	}

	/**
	 * Get Allocation Line. Allocation Line
	 */
	public int getC_AllocationLine_ID() {
		Integer ii = (Integer) get_Value("C_AllocationLine_ID");
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_Invoice_ID()));
	}

	/**
	 * Set Allocate Payment. Allocate Payment to Invoices
	 */
	public void setC_PaymentAllocate_ID(int C_PaymentAllocate_ID) {
		if (C_PaymentAllocate_ID < 1)
			throw new IllegalArgumentException(
					"C_PaymentAllocate_ID is mandatory.");
		set_ValueNoCheck("C_PaymentAllocate_ID", new Integer(
				C_PaymentAllocate_ID));
	}

	/**
	 * Get Allocate Payment. Allocate Payment to Invoices
	 */
	public int getC_PaymentAllocate_ID() {
		Integer ii = (Integer) get_Value("C_PaymentAllocate_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment. Payment identifier
	 */
	public void setC_Payment_ID(int C_Payment_ID) {
		if (C_Payment_ID < 1)
			throw new IllegalArgumentException("C_Payment_ID is mandatory.");
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

	/** Set Invoice Amt */
	public void setInvoiceAmt(BigDecimal InvoiceAmt) {
		set_Value("InvoiceAmt", InvoiceAmt);
	}

	/** Get Invoice Amt */
	public BigDecimal getInvoiceAmt() {
		BigDecimal bd = (BigDecimal) get_Value("InvoiceAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Over/Under Payment. Over-Payment (unallocated) or Under-Payment
	 * (partial payment) Amount
	 */
	public void setOverUnderAmt(BigDecimal OverUnderAmt) {
		if (OverUnderAmt == null)
			throw new IllegalArgumentException("OverUnderAmt is mandatory.");
		set_Value("OverUnderAmt", OverUnderAmt);
	}

	/**
	 * Get Over/Under Payment. Over-Payment (unallocated) or Under-Payment
	 * (partial payment) Amount
	 */
	public BigDecimal getOverUnderAmt() {
		BigDecimal bd = (BigDecimal) get_Value("OverUnderAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Remaining Amt. Remaining Amount
	 */
	public void setRemainingAmt(BigDecimal RemainingAmt) {
		throw new IllegalArgumentException("RemainingAmt is virtual column");
	}

	/**
	 * Get Remaining Amt. Remaining Amount
	 */
	public BigDecimal getRemainingAmt() {
		BigDecimal bd = (BigDecimal) get_Value("RemainingAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Write-off Amount. Amount to write-off
	 */
	public void setWriteOffAmt(BigDecimal WriteOffAmt) {
		if (WriteOffAmt == null)
			throw new IllegalArgumentException("WriteOffAmt is mandatory.");
		set_Value("WriteOffAmt", WriteOffAmt);
	}

	/**
	 * Get Write-off Amount. Amount to write-off
	 */
	public BigDecimal getWriteOffAmt() {
		BigDecimal bd = (BigDecimal) get_Value("WriteOffAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
