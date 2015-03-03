/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_CashBook_Acct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.015
 */
public class X_C_CashBook_Acct extends PO {
	/** Standard Constructor */
	public X_C_CashBook_Acct(Properties ctx, int C_CashBook_Acct_ID,
			String trxName) {
		super(ctx, C_CashBook_Acct_ID, trxName);
		/**
		 * if (C_CashBook_Acct_ID == 0) { setCB_Asset_Acct (0);
		 * setCB_CashTransfer_Acct (0); setCB_Differences_Acct (0);
		 * setCB_Expense_Acct (0); setCB_Receipt_Acct (0); setC_AcctSchema_ID
		 * (0); setC_CashBook_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_C_CashBook_Acct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_CashBook_Acct */
	public static final String Table_Name = "C_CashBook_Acct";

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
		StringBuffer sb = new StringBuffer("X_C_CashBook_Acct[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Cash Book Asset. Cash Book Asset Account
	 */
	public void setCB_Asset_Acct(int CB_Asset_Acct) {
		set_Value("CB_Asset_Acct", new Integer(CB_Asset_Acct));
	}

	/**
	 * Get Cash Book Asset. Cash Book Asset Account
	 */
	public int getCB_Asset_Acct() {
		Integer ii = (Integer) get_Value("CB_Asset_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Cash Transfer. Cash Transfer Clearing Account
	 */
	public void setCB_CashTransfer_Acct(int CB_CashTransfer_Acct) {
		set_Value("CB_CashTransfer_Acct", new Integer(CB_CashTransfer_Acct));
	}

	/**
	 * Get Cash Transfer. Cash Transfer Clearing Account
	 */
	public int getCB_CashTransfer_Acct() {
		Integer ii = (Integer) get_Value("CB_CashTransfer_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Cash Book Differences. Cash Book Differences Account
	 */
	public void setCB_Differences_Acct(int CB_Differences_Acct) {
		set_Value("CB_Differences_Acct", new Integer(CB_Differences_Acct));
	}

	/**
	 * Get Cash Book Differences. Cash Book Differences Account
	 */
	public int getCB_Differences_Acct() {
		Integer ii = (Integer) get_Value("CB_Differences_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Cash Book Expense. Cash Book Expense Account
	 */
	public void setCB_Expense_Acct(int CB_Expense_Acct) {
		set_Value("CB_Expense_Acct", new Integer(CB_Expense_Acct));
	}

	/**
	 * Get Cash Book Expense. Cash Book Expense Account
	 */
	public int getCB_Expense_Acct() {
		Integer ii = (Integer) get_Value("CB_Expense_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Cash Book Receipt. Cash Book Receipts Account
	 */
	public void setCB_Receipt_Acct(int CB_Receipt_Acct) {
		set_Value("CB_Receipt_Acct", new Integer(CB_Receipt_Acct));
	}

	/**
	 * Get Cash Book Receipt. Cash Book Receipts Account
	 */
	public int getCB_Receipt_Acct() {
		Integer ii = (Integer) get_Value("CB_Receipt_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Accounting Schema. Rules for accounting
	 */
	public void setC_AcctSchema_ID(int C_AcctSchema_ID) {
		if (C_AcctSchema_ID < 1)
			throw new IllegalArgumentException("C_AcctSchema_ID is mandatory.");
		set_ValueNoCheck("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
	}

	/**
	 * Get Accounting Schema. Rules for accounting
	 */
	public int getC_AcctSchema_ID() {
		Integer ii = (Integer) get_Value("C_AcctSchema_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Cash Book. Cash Book for recording petty cash transactions
	 */
	public void setC_CashBook_ID(int C_CashBook_ID) {
		if (C_CashBook_ID < 1)
			throw new IllegalArgumentException("C_CashBook_ID is mandatory.");
		set_ValueNoCheck("C_CashBook_ID", new Integer(C_CashBook_ID));
	}

	/**
	 * Get Cash Book. Cash Book for recording petty cash transactions
	 */
	public int getC_CashBook_ID() {
		Integer ii = (Integer) get_Value("C_CashBook_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
