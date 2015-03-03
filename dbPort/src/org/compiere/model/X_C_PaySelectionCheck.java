/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_PaySelectionCheck
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.968
 */
public class X_C_PaySelectionCheck extends PO {
	/** Standard Constructor */
	public X_C_PaySelectionCheck(Properties ctx, int C_PaySelectionCheck_ID,
			String trxName) {
		super(ctx, C_PaySelectionCheck_ID, trxName);
		/**
		 * if (C_PaySelectionCheck_ID == 0) { setC_BPartner_ID (0);
		 * setC_PaySelectionCheck_ID (0); setC_PaySelection_ID (0);
		 * setDiscountAmt (Env.ZERO); setIsPrinted (false); setIsReceipt
		 * (false); setPayAmt (Env.ZERO); setPaymentRule (null); setProcessed
		 * (false); // N setQty (0); }
		 */
	}

	/** Load Constructor */
	public X_C_PaySelectionCheck(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_PaySelectionCheck */
	public static final String Table_Name = "C_PaySelectionCheck";

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
		StringBuffer sb = new StringBuffer("X_C_PaySelectionCheck[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID < 1)
			throw new IllegalArgumentException("C_BPartner_ID is mandatory.");
		set_Value("C_BPartner_ID", new Integer(C_BPartner_ID));
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
	 * Set Pay Selection Check. Payment Selection Check
	 */
	public void setC_PaySelectionCheck_ID(int C_PaySelectionCheck_ID) {
		if (C_PaySelectionCheck_ID < 1)
			throw new IllegalArgumentException(
					"C_PaySelectionCheck_ID is mandatory.");
		set_ValueNoCheck("C_PaySelectionCheck_ID", new Integer(
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

	/** Set CurrencyInWord */
	public void setCurrencyInWord(String CurrencyInWord) {
		if (CurrencyInWord != null && CurrencyInWord.length() > 22) {
			log.warning("Length > 22 - truncated");
			CurrencyInWord = CurrencyInWord.substring(0, 21);
		}
		set_Value("CurrencyInWord", CurrencyInWord);
	}

	/** Get CurrencyInWord */
	public String getCurrencyInWord() {
		return (String) get_Value("CurrencyInWord");
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
	 * Set Document No. Document sequence number of the document
	 */
	public void setDocumentNo(String DocumentNo) {
		if (DocumentNo != null && DocumentNo.length() > 30) {
			log.warning("Length > 30 - truncated");
			DocumentNo = DocumentNo.substring(0, 29);
		}
		set_Value("DocumentNo", DocumentNo);
	}

	/**
	 * Get Document No. Document sequence number of the document
	 */
	public String getDocumentNo() {
		return (String) get_Value("DocumentNo");
	}

	/**
	 * Set Printed. Indicates if this document / line is printed
	 */
	public void setIsPrinted(boolean IsPrinted) {
		set_Value("IsPrinted", new Boolean(IsPrinted));
	}

	/**
	 * Get Printed. Indicates if this document / line is printed
	 */
	public boolean isPrinted() {
		Object oo = get_Value("IsPrinted");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Receipt. This is a sales transaction (receipt)
	 */
	public void setIsReceipt(boolean IsReceipt) {
		set_Value("IsReceipt", new Boolean(IsReceipt));
	}

	/**
	 * Get Receipt. This is a sales transaction (receipt)
	 */
	public boolean isReceipt() {
		Object oo = get_Value("IsReceipt");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/**
	 * Set Quantity. Quantity
	 */
	public void setQty(int Qty) {
		set_Value("Qty", new Integer(Qty));
	}

	/**
	 * Get Quantity. Quantity
	 */
	public int getQty() {
		Integer ii = (Integer) get_Value("Qty");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
