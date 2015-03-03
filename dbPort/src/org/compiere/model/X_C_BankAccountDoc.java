/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BankAccountDoc
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.875
 */
public class X_C_BankAccountDoc extends PO {
	/** Standard Constructor */
	public X_C_BankAccountDoc(Properties ctx, int C_BankAccountDoc_ID,
			String trxName) {
		super(ctx, C_BankAccountDoc_ID, trxName);
		/**
		 * if (C_BankAccountDoc_ID == 0) { setC_BankAccountDoc_ID (0);
		 * setC_BankAccount_ID (0); setCurrentNext (0); setName (null);
		 * setPaymentRule (null); }
		 */
	}

	/** Load Constructor */
	public X_C_BankAccountDoc(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BankAccountDoc */
	public static final String Table_Name = "C_BankAccountDoc";

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
		StringBuffer sb = new StringBuffer("X_C_BankAccountDoc[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Bank Account Document. Checks, Transfers, etc.
	 */
	public void setC_BankAccountDoc_ID(int C_BankAccountDoc_ID) {
		if (C_BankAccountDoc_ID < 1)
			throw new IllegalArgumentException(
					"C_BankAccountDoc_ID is mandatory.");
		set_ValueNoCheck("C_BankAccountDoc_ID",
				new Integer(C_BankAccountDoc_ID));
	}

	/**
	 * Get Bank Account Document. Checks, Transfers, etc.
	 */
	public int getC_BankAccountDoc_ID() {
		Integer ii = (Integer) get_Value("C_BankAccountDoc_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Bank Account. Account at the Bank
	 */
	public void setC_BankAccount_ID(int C_BankAccount_ID) {
		if (C_BankAccount_ID < 1)
			throw new IllegalArgumentException("C_BankAccount_ID is mandatory.");
		set_ValueNoCheck("C_BankAccount_ID", new Integer(C_BankAccount_ID));
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

	/** Check_PrintFormat_ID AD_Reference_ID=268 */
	public static final int CHECK_PRINTFORMAT_ID_AD_Reference_ID = 268;

	/**
	 * Set Check Print Format. Print Format for printing Checks
	 */
	public void setCheck_PrintFormat_ID(int Check_PrintFormat_ID) {
		if (Check_PrintFormat_ID <= 0)
			set_Value("Check_PrintFormat_ID", null);
		else
			set_Value("Check_PrintFormat_ID", new Integer(Check_PrintFormat_ID));
	}

	/**
	 * Get Check Print Format. Print Format for printing Checks
	 */
	public int getCheck_PrintFormat_ID() {
		Integer ii = (Integer) get_Value("Check_PrintFormat_ID");
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
}
