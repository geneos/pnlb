/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_AllocationLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.578
 */
public class X_C_AllocationLine extends PO {
	/** Standard Constructor */
	public X_C_AllocationLine(Properties ctx, int C_AllocationLine_ID,
			String trxName) {
		super(ctx, C_AllocationLine_ID, trxName);
		/**
		 * if (C_AllocationLine_ID == 0) { setAmount (Env.ZERO);
		 * setC_AllocationHdr_ID (0); setC_AllocationLine_ID (0); setDiscountAmt
		 * (Env.ZERO); setWriteOffAmt (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_AllocationLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_AllocationLine */
	public static final String Table_Name = "C_AllocationLine";

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
		StringBuffer sb = new StringBuffer("X_C_AllocationLine[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Amount. Amount in a defined currency
	 */
	public void setAmount(BigDecimal Amount) {
		if (Amount == null)
			throw new IllegalArgumentException("Amount is mandatory.");
		set_ValueNoCheck("Amount", Amount);
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
	 * Set Allocation. Payment allocation
	 */
	public void setC_AllocationHdr_ID(int C_AllocationHdr_ID) {
		if (C_AllocationHdr_ID < 1)
			throw new IllegalArgumentException(
					"C_AllocationHdr_ID is mandatory.");
		set_ValueNoCheck("C_AllocationHdr_ID", new Integer(C_AllocationHdr_ID));
	}

	/**
	 * Get Allocation. Payment allocation
	 */
	public int getC_AllocationHdr_ID() {
		Integer ii = (Integer) get_Value("C_AllocationHdr_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Allocation Line. Allocation Line
	 */
	public void setC_AllocationLine_ID(int C_AllocationLine_ID) {
		if (C_AllocationLine_ID < 1)
			throw new IllegalArgumentException(
					"C_AllocationLine_ID is mandatory.");
		set_ValueNoCheck("C_AllocationLine_ID",
				new Integer(C_AllocationLine_ID));
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
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID <= 0)
			set_ValueNoCheck("C_BPartner_ID", null);
		else
			set_ValueNoCheck("C_BPartner_ID", new Integer(C_BPartner_ID));
	}

	/**
	 * Get Business Partner . Identifies a Business Partner
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Cash Journal Line. Cash Journal Line
	 */
	public void setC_CashLine_ID(int C_CashLine_ID) {
		if (C_CashLine_ID <= 0)
			set_ValueNoCheck("C_CashLine_ID", null);
		else
			set_ValueNoCheck("C_CashLine_ID", new Integer(C_CashLine_ID));
	}

	/**
	 * Get Cash Journal Line. Cash Journal Line
	 */
	public int getC_CashLine_ID() {
		Integer ii = (Integer) get_Value("C_CashLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Invoice. Invoice Identifier
	 */
	public void setC_Invoice_ID(int C_Invoice_ID) {
		if (C_Invoice_ID <= 0)
			set_ValueNoCheck("C_Invoice_ID", null);
		else
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_Invoice_ID()));
	}

	/**
	 * Set Order. Order
	 */
	public void setC_Order_ID(int C_Order_ID) {
		if (C_Order_ID <= 0)
			set_ValueNoCheck("C_Order_ID", null);
		else
			set_ValueNoCheck("C_Order_ID", new Integer(C_Order_ID));
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
			set_ValueNoCheck("C_Payment_ID", null);
		else
			set_ValueNoCheck("C_Payment_ID", new Integer(C_Payment_ID));
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
	 * Set Transaction Date. Transaction Date
	 */
	public void setDateTrx(Timestamp DateTrx) {
		set_ValueNoCheck("DateTrx", DateTrx);
	}

	/**
	 * Get Transaction Date. Transaction Date
	 */
	public Timestamp getDateTrx() {
		return (Timestamp) get_Value("DateTrx");
	}

	/**
	 * Set Discount Amount. Calculated amount of discount
	 */
	public void setDiscountAmt(BigDecimal DiscountAmt) {
		if (DiscountAmt == null)
			throw new IllegalArgumentException("DiscountAmt is mandatory.");
		set_ValueNoCheck("DiscountAmt", DiscountAmt);
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
	 * Set Manual. This is a manual process
	 */
	public void setIsManual(boolean IsManual) {
		set_ValueNoCheck("IsManual", new Boolean(IsManual));
	}

	/**
	 * Get Manual. This is a manual process
	 */
	public boolean isManual() {
		Object oo = get_Value("IsManual");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Over/Under Payment. Over-Payment (unallocated) or Under-Payment
	 * (partial payment) Amount
	 */
	public void setOverUnderAmt(BigDecimal OverUnderAmt) {
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
	 * Set Write-off Amount. Amount to write-off
	 */
	public void setWriteOffAmt(BigDecimal WriteOffAmt) {
		if (WriteOffAmt == null)
			throw new IllegalArgumentException("WriteOffAmt is mandatory.");
		set_ValueNoCheck("WriteOffAmt", WriteOffAmt);
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
