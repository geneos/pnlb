/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_PaySelectionLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.984
 */
public class X_C_PaySelectionLine extends PO {
	/** Standard Constructor */
	public X_C_PaySelectionLine(Properties ctx, int C_PaySelectionLine_ID,
			String trxName) {
		super(ctx, C_PaySelectionLine_ID, trxName);
		/**
		 * if (C_PaySelectionLine_ID == 0) { setC_Invoice_ID (0);
		 * setC_PaySelectionLine_ID (0); setC_PaySelection_ID (0);
		 * setDifferenceAmt (Env.ZERO); setDiscountAmt (Env.ZERO); setIsManual
		 * (false); setIsSOTrx (false); setLine (0); //
		 * 
		 * @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM
		 *             C_PaySelectionLine WHERE
		 *             C_PaySelection_ID=@C_PaySelection_ID@ setOpenAmt
		 *             (Env.ZERO); setPayAmt (Env.ZERO); setPaymentRule (null); //
		 *             S setProcessed (false); // N }
		 */
	}

	/** Load Constructor */
	public X_C_PaySelectionLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_PaySelectionLine */
	public static final String Table_Name = "C_PaySelectionLine";

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
		StringBuffer sb = new StringBuffer("X_C_PaySelectionLine[").append(
				get_ID()).append("]");
		return sb.toString();
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

	/**
	 * Set Pay Selection Check. Payment Selection Check
	 */
	public void setC_PaySelectionCheck_ID(int C_PaySelectionCheck_ID) {
		if (C_PaySelectionCheck_ID <= 0)
			set_Value("C_PaySelectionCheck_ID", null);
		else
			set_Value("C_PaySelectionCheck_ID", new Integer(
					C_PaySelectionCheck_ID));
	}

	/**
	 * Get Pay Selection Check. Payment Selection Check
	 */
	public int getC_PaySelectionCheck_ID() {
		Integer ii = (Integer) get_Value("C_PaySelectionCheck_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Payment Selection Line. Payment Selection Line
	 */
	public void setC_PaySelectionLine_ID(int C_PaySelectionLine_ID) {
		if (C_PaySelectionLine_ID < 1)
			throw new IllegalArgumentException(
					"C_PaySelectionLine_ID is mandatory.");
		set_ValueNoCheck("C_PaySelectionLine_ID", new Integer(
				C_PaySelectionLine_ID));
	}

	/**
	 * Get Payment Selection Line. Payment Selection Line
	 */
	public int getC_PaySelectionLine_ID() {
		Integer ii = (Integer) get_Value("C_PaySelectionLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getC_PaySelectionLine_ID()));
	}

	/**
	 * Set Payment Selection. Payment Selection
	 */
	public void setC_PaySelection_ID(int C_PaySelection_ID) {
		if (C_PaySelection_ID < 1)
			throw new IllegalArgumentException(
					"C_PaySelection_ID is mandatory.");
		set_ValueNoCheck("C_PaySelection_ID", new Integer(C_PaySelection_ID));
	}

	/**
	 * Get Payment Selection. Payment Selection
	 */
	public int getC_PaySelection_ID() {
		Integer ii = (Integer) get_Value("C_PaySelection_ID");
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
	 * Set Difference. Difference Amount
	 */
	public void setDifferenceAmt(BigDecimal DifferenceAmt) {
		if (DifferenceAmt == null)
			throw new IllegalArgumentException("DifferenceAmt is mandatory.");
		set_ValueNoCheck("DifferenceAmt", DifferenceAmt);
	}

	/**
	 * Get Difference. Difference Amount
	 */
	public BigDecimal getDifferenceAmt() {
		BigDecimal bd = (BigDecimal) get_Value("DifferenceAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
		set_Value("IsManual", new Boolean(IsManual));
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
	 * Set Sales Transaction. This is a Sales Transaction
	 */
	public void setIsSOTrx(boolean IsSOTrx) {
		set_Value("IsSOTrx", new Boolean(IsSOTrx));
	}

	/**
	 * Get Sales Transaction. This is a Sales Transaction
	 */
	public boolean isSOTrx() {
		Object oo = get_Value("IsSOTrx");
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
	 * Set Open Amount. Open item amount
	 */
	public void setOpenAmt(BigDecimal OpenAmt) {
		if (OpenAmt == null)
			throw new IllegalArgumentException("OpenAmt is mandatory.");
		set_ValueNoCheck("OpenAmt", OpenAmt);
	}

	/**
	 * Get Open Amount. Open item amount
	 */
	public BigDecimal getOpenAmt() {
		BigDecimal bd = (BigDecimal) get_Value("OpenAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Payment amount. Amount being paid
	 */
	public void setPayAmt(BigDecimal PayAmt) {
		if (PayAmt == null)
			throw new IllegalArgumentException("PayAmt is mandatory.");
		set_Value("PayAmt", PayAmt);
	}

	/**
	 * Get Payment amount. Amount being paid
	 */
	public BigDecimal getPayAmt() {
		BigDecimal bd = (BigDecimal) get_Value("PayAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** PaymentRule AD_Reference_ID=195 */
	public static final int PAYMENTRULE_AD_Reference_ID = 195;

	/** Cash = B */
	public static final String PAYMENTRULE_Cash = "B";

	/** Direct Debit = D */
	public static final String PAYMENTRULE_DirectDebit = "D";

	/** Credit Card = K */
	public static final String PAYMENTRULE_CreditCard = "K";

	/** On Credit = P */
	public static final String PAYMENTRULE_OnCredit = "P";

	/** Check = S */
	public static final String PAYMENTRULE_Check = "S";

	/** Direct Deposit = T */
	public static final String PAYMENTRULE_DirectDeposit = "T";

	/**
	 * Set Payment Rule. How you pay the invoice
	 */
	public void setPaymentRule(String PaymentRule) {
		if (PaymentRule == null)
			throw new IllegalArgumentException("PaymentRule is mandatory");
		if (PaymentRule.equals("B") || PaymentRule.equals("D")
				|| PaymentRule.equals("K") || PaymentRule.equals("P")
				|| PaymentRule.equals("S") || PaymentRule.equals("T"))
			;
		else
			throw new IllegalArgumentException("PaymentRule Invalid value - "
					+ PaymentRule
					+ " - Reference_ID=195 - B - D - K - P - S - T");
		if (PaymentRule.length() > 1) {
			log.warning("Length > 1 - truncated");
			PaymentRule = PaymentRule.substring(0, 0);
		}
		set_Value("PaymentRule", PaymentRule);
	}

	/**
	 * Get Payment Rule. How you pay the invoice
	 */
	public String getPaymentRule() {
		return (String) get_Value("PaymentRule");
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
}
