/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_ElementValue
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.406
 */
public class X_C_ElementValue extends PO {
	/** Standard Constructor */
	public X_C_ElementValue(Properties ctx, int C_ElementValue_ID,
			String trxName) {
		super(ctx, C_ElementValue_ID, trxName);
		/**
		 * if (C_ElementValue_ID == 0) { setAccountSign (null); // N
		 * setAccountType (null); // E setC_ElementValue_ID (0); setC_Element_ID
		 * (0); setIsSummary (false); setName (null); setPostActual (true); // Y
		 * setPostBudget (true); // Y setPostEncumbrance (true); // Y
		 * setPostStatistical (true); // Y setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_C_ElementValue(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_ElementValue */
	public static final String Table_Name = "C_ElementValue";

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

	protected BigDecimal accessLevel = new BigDecimal(2);

	/** AccessLevel 2 - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_ElementValue[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/** AccountSign AD_Reference_ID=118 */
	public static final int ACCOUNTSIGN_AD_Reference_ID = 118;

	/** Credit = C */
	public static final String ACCOUNTSIGN_Credit = "C";

	/** Debit = D */
	public static final String ACCOUNTSIGN_Debit = "D";

	/** Natural = N */
	public static final String ACCOUNTSIGN_Natural = "N";

	/**
	 * Set Account Sign. Indicates the Natural Sign of the Account as a Debit or
	 * Credit
	 */
	public void setAccountSign(String AccountSign) {
		if (AccountSign == null)
			throw new IllegalArgumentException("AccountSign is mandatory");
		if (AccountSign.equals("C") || AccountSign.equals("D")
				|| AccountSign.equals("N"))
			;
		else
			throw new IllegalArgumentException("AccountSign Invalid value - "
					+ AccountSign + " - Reference_ID=118 - C - D - N");
		if (AccountSign.length() > 1) {
			log.warning("Length > 1 - truncated");
			AccountSign = AccountSign.substring(0, 0);
		}
		set_Value("AccountSign", AccountSign);
	}

	/**
	 * Get Account Sign. Indicates the Natural Sign of the Account as a Debit or
	 * Credit
	 */
	public String getAccountSign() {
		return (String) get_Value("AccountSign");
	}

	/** AccountType AD_Reference_ID=117 */
	public static final int ACCOUNTTYPE_AD_Reference_ID = 117;

	/** Asset = A */
	public static final String ACCOUNTTYPE_Asset = "A";

	/** Expense = E */
	public static final String ACCOUNTTYPE_Expense = "E";

	/** Liability = L */
	public static final String ACCOUNTTYPE_Liability = "L";

	/** Memo = M */
	public static final String ACCOUNTTYPE_Memo = "M";

	/** Owner's Equity = O */
	public static final String ACCOUNTTYPE_OwnerSEquity = "O";

	/** Revenue = R */
	public static final String ACCOUNTTYPE_Revenue = "R";

	/**
	 * Set Account Type. Indicates the type of account
	 */
	public void setAccountType(String AccountType) {
		if (AccountType == null)
			throw new IllegalArgumentException("AccountType is mandatory");
		if (AccountType.equals("A") || AccountType.equals("E")
				|| AccountType.equals("L") || AccountType.equals("M")
				|| AccountType.equals("O") || AccountType.equals("R"))
			;
		else
			throw new IllegalArgumentException("AccountType Invalid value - "
					+ AccountType
					+ " - Reference_ID=117 - A - E - L - M - O - R");
		if (AccountType.length() > 1) {
			log.warning("Length > 1 - truncated");
			AccountType = AccountType.substring(0, 0);
		}
		set_Value("AccountType", AccountType);
	}

	/**
	 * Get Account Type. Indicates the type of account
	 */
	public String getAccountType() {
		return (String) get_Value("AccountType");
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
	 * Set Currency. The Currency for this record
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		if (C_Currency_ID <= 0)
			set_Value("C_Currency_ID", null);
		else
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
	 * Set Account Element. Account Element
	 */
	public void setC_ElementValue_ID(int C_ElementValue_ID) {
		if (C_ElementValue_ID < 1)
			throw new IllegalArgumentException(
					"C_ElementValue_ID is mandatory.");
		set_ValueNoCheck("C_ElementValue_ID", new Integer(C_ElementValue_ID));
	}

	/**
	 * Get Account Element. Account Element
	 */
	public int getC_ElementValue_ID() {
		Integer ii = (Integer) get_Value("C_ElementValue_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Element. Accounting Element
	 */
	public void setC_Element_ID(int C_Element_ID) {
		if (C_Element_ID < 1)
			throw new IllegalArgumentException("C_Element_ID is mandatory.");
		set_ValueNoCheck("C_Element_ID", new Integer(C_Element_ID));
	}

	/**
	 * Get Element. Accounting Element
	 */
	public int getC_Element_ID() {
		Integer ii = (Integer) get_Value("C_Element_ID");
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
	 * Set Bank Account. Indicates if this is the Bank Account
	 */
	public void setIsBankAccount(boolean IsBankAccount) {
		set_Value("IsBankAccount", new Boolean(IsBankAccount));
	}

	/**
	 * Get Bank Account. Indicates if this is the Bank Account
	 */
	public boolean isBankAccount() {
		Object oo = get_Value("IsBankAccount");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Document Controlled. Control account - If an account is controlled by
	 * a document, you cannot post manually to it
	 */
	public void setIsDocControlled(boolean IsDocControlled) {
		set_Value("IsDocControlled", new Boolean(IsDocControlled));
	}

	/**
	 * Get Document Controlled. Control account - If an account is controlled by
	 * a document, you cannot post manually to it
	 */
	public boolean isDocControlled() {
		Object oo = get_Value("IsDocControlled");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Foreign Currency Account. Balances in foreign currency accounts are
	 * held in the nominated currency
	 */
	public void setIsForeignCurrency(boolean IsForeignCurrency) {
		set_Value("IsForeignCurrency", new Boolean(IsForeignCurrency));
	}

	/**
	 * Get Foreign Currency Account. Balances in foreign currency accounts are
	 * held in the nominated currency
	 */
	public boolean isForeignCurrency() {
		Object oo = get_Value("IsForeignCurrency");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Summary Level. This is a summary entity
	 */
	public void setIsSummary(boolean IsSummary) {
		set_Value("IsSummary", new Boolean(IsSummary));
	}

	/**
	 * Get Summary Level. This is a summary entity
	 */
	public boolean isSummary() {
		Object oo = get_Value("IsSummary");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/**
	 * Set Post Actual. Actual Values can be posted
	 */
	public void setPostActual(boolean PostActual) {
		set_Value("PostActual", new Boolean(PostActual));
	}

	/**
	 * Get Post Actual. Actual Values can be posted
	 */
	public boolean isPostActual() {
		Object oo = get_Value("PostActual");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Post Budget. Budget values can be posted
	 */
	public void setPostBudget(boolean PostBudget) {
		set_Value("PostBudget", new Boolean(PostBudget));
	}

	/**
	 * Get Post Budget. Budget values can be posted
	 */
	public boolean isPostBudget() {
		Object oo = get_Value("PostBudget");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Post Encumbrance. Post commitments to this account
	 */
	public void setPostEncumbrance(boolean PostEncumbrance) {
		set_Value("PostEncumbrance", new Boolean(PostEncumbrance));
	}

	/**
	 * Get Post Encumbrance. Post commitments to this account
	 */
	public boolean isPostEncumbrance() {
		Object oo = get_Value("PostEncumbrance");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Post Statistical. Post statistical quantities to this account?
	 */
	public void setPostStatistical(boolean PostStatistical) {
		set_Value("PostStatistical", new Boolean(PostStatistical));
	}

	/**
	 * Get Post Statistical. Post statistical quantities to this account?
	 */
	public boolean isPostStatistical() {
		Object oo = get_Value("PostStatistical");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Valid from. Valid from including this date (first day)
	 */
	public void setValidFrom(Timestamp ValidFrom) {
		set_Value("ValidFrom", ValidFrom);
	}

	/**
	 * Get Valid from. Valid from including this date (first day)
	 */
	public Timestamp getValidFrom() {
		return (Timestamp) get_Value("ValidFrom");
	}

	/**
	 * Set Valid to. Valid to including this date (last day)
	 */
	public void setValidTo(Timestamp ValidTo) {
		set_Value("ValidTo", ValidTo);
	}

	/**
	 * Get Valid to. Valid to including this date (last day)
	 */
	public Timestamp getValidTo() {
		return (Timestamp) get_Value("ValidTo");
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value = Value.substring(0, 39);
		}
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public String getValue() {
		return (String) get_Value("Value");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getValue());
	}
}
