/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BankAccount
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.859
 */
public class X_C_BankAccount extends PO {
	/** Standard Constructor */
	public X_C_BankAccount(Properties ctx, int C_BankAccount_ID, String trxName) {
		super(ctx, C_BankAccount_ID, trxName);
		/**
		 * if (C_BankAccount_ID == 0) { setAccountNo (null); setBankAccountType
		 * (null); setC_BankAccount_ID (0); setC_Bank_ID (0); setC_Currency_ID
		 * (0); setCreditLimit (Env.ZERO); setCurrentBalance (Env.ZERO);
		 * setIsDefault (false); }
		 */
	}

	/** Load Constructor */
	public X_C_BankAccount(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BankAccount */
	public static final String Table_Name = "C_BankAccount";

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
		StringBuffer sb = new StringBuffer("X_C_BankAccount[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Account No. Account Number
	 */
	public void setAccountNo(String AccountNo) {
		if (AccountNo == null)
			throw new IllegalArgumentException("AccountNo is mandatory.");
		if (AccountNo.length() > 20) {
			log.warning("Length > 20 - truncated");
			AccountNo = AccountNo.substring(0, 19);
		}
		set_Value("AccountNo", AccountNo);
	}

	/**
	 * Get Account No. Account Number
	 */
	public String getAccountNo() {
		return (String) get_Value("AccountNo");
	}

	/**
	 * Set BBAN. Basic Bank Account Number
	 */
	public void setBBAN(String BBAN) {
		if (BBAN != null && BBAN.length() > 40) {
			log.warning("Length > 40 - truncated");
			BBAN = BBAN.substring(0, 39);
		}
		set_Value("BBAN", BBAN);
	}

	/**
	 * Get BBAN. Basic Bank Account Number
	 */
	public String getBBAN() {
		return (String) get_Value("BBAN");
	}

	/** BankAccountType AD_Reference_ID=216 */
	public static final int BANKACCOUNTTYPE_AD_Reference_ID = 216;

	/** Checking = C */
	public static final String BANKACCOUNTTYPE_Checking = "C";

	/** Savings = S */
	public static final String BANKACCOUNTTYPE_Savings = "S";

	/**
	 * Set Bank Account Type. Bank Account Type
	 */
	public void setBankAccountType(String BankAccountType) {
		if (BankAccountType == null)
			throw new IllegalArgumentException("BankAccountType is mandatory");
		if (BankAccountType.equals("C") || BankAccountType.equals("S"))
			;
		else
			throw new IllegalArgumentException(
					"BankAccountType Invalid value - " + BankAccountType
							+ " - Reference_ID=216 - C - S");
		if (BankAccountType.length() > 1) {
			log.warning("Length > 1 - truncated");
			BankAccountType = BankAccountType.substring(0, 0);
		}
		set_Value("BankAccountType", BankAccountType);
	}

	/**
	 * Get Bank Account Type. Bank Account Type
	 */
	public String getBankAccountType() {
		return (String) get_Value("BankAccountType");
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

	/**
	 * Set Bank. Bank
	 */
	public void setC_Bank_ID(int C_Bank_ID) {
		if (C_Bank_ID < 1)
			throw new IllegalArgumentException("C_Bank_ID is mandatory.");
		set_ValueNoCheck("C_Bank_ID", new Integer(C_Bank_ID));
	}

	/**
	 * Get Bank. Bank
	 */
	public int getC_Bank_ID() {
		Integer ii = (Integer) get_Value("C_Bank_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_Bank_ID()));
	}

	/**
	 * Set Currency. The Currency for this record
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		if (C_Currency_ID < 1)
			throw new IllegalArgumentException("C_Currency_ID is mandatory.");
		set_Value("C_Currency_ID", new Integer(C_Currency_ID));
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
	 * Set Credit limit. Amount of Credit allowed
	 */
	public void setCreditLimit(BigDecimal CreditLimit) {
		if (CreditLimit == null)
			throw new IllegalArgumentException("CreditLimit is mandatory.");
		set_Value("CreditLimit", CreditLimit);
	}

	/**
	 * Get Credit limit. Amount of Credit allowed
	 */
	public BigDecimal getCreditLimit() {
		BigDecimal bd = (BigDecimal) get_Value("CreditLimit");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Current balance. Current Balance
	 */
	public void setCurrentBalance(BigDecimal CurrentBalance) {
		if (CurrentBalance == null)
			throw new IllegalArgumentException("CurrentBalance is mandatory.");
		set_Value("CurrentBalance", CurrentBalance);
	}

	/**
	 * Get Current balance. Current Balance
	 */
	public BigDecimal getCurrentBalance() {
		BigDecimal bd = (BigDecimal) get_Value("CurrentBalance");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
	 * Set IBAN. International Bank Account Number
	 */
	public void setIBAN(String IBAN) {
		if (IBAN != null && IBAN.length() > 40) {
			log.warning("Length > 40 - truncated");
			IBAN = IBAN.substring(0, 39);
		}
		set_Value("IBAN", IBAN);
	}

	/**
	 * Get IBAN. International Bank Account Number
	 */
	public String getIBAN() {
		return (String) get_Value("IBAN");
	}

	/**
	 * Set Default. Default value
	 */
	public void setIsDefault(boolean IsDefault) {
		set_Value("IsDefault", new Boolean(IsDefault));
	}

	/**
	 * Get Default. Default value
	 */
	public boolean isDefault() {
		Object oo = get_Value("IsDefault");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
}
