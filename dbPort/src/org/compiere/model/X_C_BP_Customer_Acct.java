/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BP_Customer_Acct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.625
 */
public class X_C_BP_Customer_Acct extends PO {
	/** Standard Constructor */
	public X_C_BP_Customer_Acct(Properties ctx, int C_BP_Customer_Acct_ID,
			String trxName) {
		super(ctx, C_BP_Customer_Acct_ID, trxName);
		/**
		 * if (C_BP_Customer_Acct_ID == 0) { setC_AcctSchema_ID (0);
		 * setC_BPartner_ID (0); setC_Prepayment_Acct (0); setC_Receivable_Acct
		 * (0); setC_Receivable_Services_Acct (0); }
		 */
	}

	/** Load Constructor */
	public X_C_BP_Customer_Acct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BP_Customer_Acct */
	public static final String Table_Name = "C_BP_Customer_Acct";

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
		StringBuffer sb = new StringBuffer("X_C_BP_Customer_Acct[").append(
				get_ID()).append("]");
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
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID < 1)
			throw new IllegalArgumentException("C_BPartner_ID is mandatory.");
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
	 * Set Customer Prepayment. Account for customer prepayments
	 */
	public void setC_Prepayment_Acct(int C_Prepayment_Acct) {
		set_Value("C_Prepayment_Acct", new Integer(C_Prepayment_Acct));
	}

	/**
	 * Get Customer Prepayment. Account for customer prepayments
	 */
	public int getC_Prepayment_Acct() {
		Integer ii = (Integer) get_Value("C_Prepayment_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Customer Receivables. Account for Customer Receivables
	 */
	public void setC_Receivable_Acct(int C_Receivable_Acct) {
		set_Value("C_Receivable_Acct", new Integer(C_Receivable_Acct));
	}

	/**
	 * Get Customer Receivables. Account for Customer Receivables
	 */
	public int getC_Receivable_Acct() {
		Integer ii = (Integer) get_Value("C_Receivable_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Receivable Services. Customer Accounts Receivables Services Account
	 */
	public void setC_Receivable_Services_Acct(int C_Receivable_Services_Acct) {
		set_Value("C_Receivable_Services_Acct", new Integer(
				C_Receivable_Services_Acct));
	}

	/**
	 * Get Receivable Services. Customer Accounts Receivables Services Account
	 */
	public int getC_Receivable_Services_Acct() {
		Integer ii = (Integer) get_Value("C_Receivable_Services_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
