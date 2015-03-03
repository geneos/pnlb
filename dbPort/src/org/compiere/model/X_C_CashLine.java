/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_CashLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.046
 */
public class X_C_CashLine extends PO {
	/** Standard Constructor */
	public X_C_CashLine(Properties ctx, int C_CashLine_ID, String trxName) {
		super(ctx, C_CashLine_ID, trxName);
		/**
		 * if (C_CashLine_ID == 0) { setAmount (Env.ZERO); setC_CashLine_ID (0);
		 * setC_Cash_ID (0); setCashType (null); // E setLine (0); //
		 * 
		 * @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM C_CashLine
		 *             WHERE C_Cash_ID=@C_Cash_ID@ setProcessed (false); }
		 */
	}

	/** Load Constructor */
	public X_C_CashLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_CashLine */
	public static final String Table_Name = "C_CashLine";

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
		StringBuffer sb = new StringBuffer("X_C_CashLine[").append(get_ID())
				.append("]");
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
	 * Set Bank Account. Account at the Bank
	 */
	public void setC_BankAccount_ID(int C_BankAccount_ID) {
		if (C_BankAccount_ID <= 0)
			set_Value("C_BankAccount_ID", null);
		else
			set_Value("C_BankAccount_ID", new Integer(C_BankAccount_ID));
	}

	/**
	 * Get Bank Account. Account at the Bank
	 */
	public int getC_BankAccount_ID() {
		Integer ii = (Integer) get_Value("C_BankAccount_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Cash Journal Line. Cash Journal Line
	 */
	public void setC_CashLine_ID(int C_CashLine_ID) {
		if (C_CashLine_ID < 1)
			throw new IllegalArgumentException("C_CashLine_ID is mandatory.");
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
	 * Set Cash Journal. Cash Journal
	 */
	public void setC_Cash_ID(int C_Cash_ID) {
		if (C_Cash_ID < 1)
			throw new IllegalArgumentException("C_Cash_ID is mandatory.");
		set_ValueNoCheck("C_Cash_ID", new Integer(C_Cash_ID));
	}

	/**
	 * Get Cash Journal. Cash Journal
	 */
	public int getC_Cash_ID() {
		Integer ii = (Integer) get_Value("C_Cash_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_Cash_ID()));
	}

	/**
	 * Set Charge. Additional document charges
	 */
	public void setC_Charge_ID(int C_Charge_ID) {
		if (C_Charge_ID <= 0)
			set_Value("C_Charge_ID", null);
		else
			set_Value("C_Charge_ID", new Integer(C_Charge_ID));
	}

	/**
	 * Get Charge. Additional document charges
	 */
	public int getC_Charge_ID() {
		Integer ii = (Integer) get_Value("C_Charge_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Currency. The Currency for this record
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		if (C_Currency_ID <= 0)
			set_ValueNoCheck("C_Currency_ID", null);
		else
			set_ValueNoCheck("C_Currency_ID", new Integer(C_Currency_ID));
	}

	/**
	 * Get Currency. The Currency for this record
	 */
	public int getC_Currency_ID() {
		Integer ii = (Integer) get_Value("C_Currency_ID");
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

	/** CashType AD_Reference_ID=217 */
	public static final int CASHTYPE_AD_Reference_ID = 217;

	/** Charge = C */
	public static final String CASHTYPE_Charge = "C";

	/** Difference = D */
	public static final String CASHTYPE_Difference = "D";

	/** General Expense = E */
	public static final String CASHTYPE_GeneralExpense = "E";

	/** Invoice = I */
	public static final String CASHTYPE_Invoice = "I";

	/** General Receipts = R */
	public static final String CASHTYPE_GeneralReceipts = "R";

	/** Bank Account Transfer = T */
	public static final String CASHTYPE_BankAccountTransfer = "T";

	/**
	 * Set Cash Type. Source of Cash
	 */
	public void setCashType(String CashType) {
		if (CashType == null)
			throw new IllegalArgumentException("CashType is mandatory");
		if (CashType.equals("C") || CashType.equals("D")
				|| CashType.equals("E") || CashType.equals("I")
				|| CashType.equals("R") || CashType.equals("T"))
			;
		else
			throw new IllegalArgumentException("CashType Invalid value - "
					+ CashType + " - Reference_ID=217 - C - D - E - I - R - T");
		if (CashType.length() > 1) {
			log.warning("Length > 1 - truncated");
			CashType = CashType.substring(0, 0);
		}
		set_ValueNoCheck("CashType", CashType);
	}

	/**
	 * Get Cash Type. Source of Cash
	 */
	public String getCashType() {
		return (String) get_Value("CashType");
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
	 * Set Discount Amount. Calculated amount of discount
	 */
	public void setDiscountAmt(BigDecimal DiscountAmt) {
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
	 * Set Generated. This Line is generated
	 */
	public void setIsGenerated(boolean IsGenerated) {
		set_ValueNoCheck("IsGenerated", new Boolean(IsGenerated));
	}

	/**
	 * Get Generated. This Line is generated
	 */
	public boolean isGenerated() {
		Object oo = get_Value("IsGenerated");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Line No. Unique line for this document
	 */
	public void setLine(int Line) {
		set_Value("Line", new Integer(Line));
	}

	/**
	 * Get Line No. Unique line for this document
	 */
	public int getLine() {
		Integer ii = (Integer) get_Value("Line");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/**
	 * Set Write-off Amount. Amount to write-off
	 */
	public void setWriteOffAmt(BigDecimal WriteOffAmt) {
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
