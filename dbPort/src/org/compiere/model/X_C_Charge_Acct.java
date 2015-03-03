/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Charge_Acct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.078
 */
public class X_C_Charge_Acct extends PO {
	/** Standard Constructor */
	public X_C_Charge_Acct(Properties ctx, int C_Charge_Acct_ID, String trxName) {
		super(ctx, C_Charge_Acct_ID, trxName);
		/**
		 * if (C_Charge_Acct_ID == 0) { setC_AcctSchema_ID (0); setC_Charge_ID
		 * (0); setCh_Expense_Acct (0); setCh_Revenue_Acct (0); }
		 */
	}

	/** Load Constructor */
	public X_C_Charge_Acct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Charge_Acct */
	public static final String Table_Name = "C_Charge_Acct";

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
		StringBuffer sb = new StringBuffer("X_C_Charge_Acct[").append(get_ID())
				.append("]");
		return sb.toString();
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
	 * Set Charge. Additional document charges
	 */
	public void setC_Charge_ID(int C_Charge_ID) {
		if (C_Charge_ID < 1)
			throw new IllegalArgumentException("C_Charge_ID is mandatory.");
		set_ValueNoCheck("C_Charge_ID", new Integer(C_Charge_ID));
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
	 * Set Charge Expense. Charge Expense Account
	 */
	public void setCh_Expense_Acct(int Ch_Expense_Acct) {
		set_Value("Ch_Expense_Acct", new Integer(Ch_Expense_Acct));
	}

	/**
	 * Get Charge Expense. Charge Expense Account
	 */
	public int getCh_Expense_Acct() {
		Integer ii = (Integer) get_Value("Ch_Expense_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Charge Revenue. Charge Revenue Account
	 */
	public void setCh_Revenue_Acct(int Ch_Revenue_Acct) {
		set_Value("Ch_Revenue_Acct", new Integer(Ch_Revenue_Acct));
	}

	/**
	 * Get Charge Revenue. Charge Revenue Account
	 */
	public int getCh_Revenue_Acct() {
		Integer ii = (Integer) get_Value("Ch_Revenue_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
