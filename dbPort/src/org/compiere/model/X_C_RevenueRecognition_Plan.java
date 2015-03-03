/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_RevenueRecognition_Plan
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.421
 */
public class X_C_RevenueRecognition_Plan extends PO {
	/** Standard Constructor */
	public X_C_RevenueRecognition_Plan(Properties ctx,
			int C_RevenueRecognition_Plan_ID, String trxName) {
		super(ctx, C_RevenueRecognition_Plan_ID, trxName);
		/**
		 * if (C_RevenueRecognition_Plan_ID == 0) { setC_AcctSchema_ID (0);
		 * setC_Currency_ID (0); setC_InvoiceLine_ID (0);
		 * setC_RevenueRecognition_ID (0); setC_RevenueRecognition_Plan_ID (0);
		 * setP_Revenue_Acct (0); setRecognizedAmt (Env.ZERO); setTotalAmt
		 * (Env.ZERO); setUnEarnedRevenue_Acct (0); }
		 */
	}

	/** Load Constructor */
	public X_C_RevenueRecognition_Plan(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_RevenueRecognition_Plan */
	public static final String Table_Name = "C_RevenueRecognition_Plan";

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
		StringBuffer sb = new StringBuffer("X_C_RevenueRecognition_Plan[")
				.append(get_ID()).append("]");
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
	 * Set Currency. The Currency for this record
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		if (C_Currency_ID < 1)
			throw new IllegalArgumentException("C_Currency_ID is mandatory.");
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
	 * Set Invoice Line. Invoice Detail Line
	 */
	public void setC_InvoiceLine_ID(int C_InvoiceLine_ID) {
		if (C_InvoiceLine_ID < 1)
			throw new IllegalArgumentException("C_InvoiceLine_ID is mandatory.");
		set_ValueNoCheck("C_InvoiceLine_ID", new Integer(C_InvoiceLine_ID));
	}

	/**
	 * Get Invoice Line. Invoice Detail Line
	 */
	public int getC_InvoiceLine_ID() {
		Integer ii = (Integer) get_Value("C_InvoiceLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Revenue Recognition. Method for recording revenue
	 */
	public void setC_RevenueRecognition_ID(int C_RevenueRecognition_ID) {
		if (C_RevenueRecognition_ID < 1)
			throw new IllegalArgumentException(
					"C_RevenueRecognition_ID is mandatory.");
		set_ValueNoCheck("C_RevenueRecognition_ID", new Integer(
				C_RevenueRecognition_ID));
	}

	/**
	 * Get Revenue Recognition. Method for recording revenue
	 */
	public int getC_RevenueRecognition_ID() {
		Integer ii = (Integer) get_Value("C_RevenueRecognition_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getC_RevenueRecognition_ID()));
	}

	/**
	 * Set Revenue Recognition Plan. Plan for recognizing or recording revenue
	 */
	public void setC_RevenueRecognition_Plan_ID(int C_RevenueRecognition_Plan_ID) {
		if (C_RevenueRecognition_Plan_ID < 1)
			throw new IllegalArgumentException(
					"C_RevenueRecognition_Plan_ID is mandatory.");
		set_ValueNoCheck("C_RevenueRecognition_Plan_ID", new Integer(
				C_RevenueRecognition_Plan_ID));
	}

	/**
	 * Get Revenue Recognition Plan. Plan for recognizing or recording revenue
	 */
	public int getC_RevenueRecognition_Plan_ID() {
		Integer ii = (Integer) get_Value("C_RevenueRecognition_Plan_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product Revenue. Account for Product Revenue (Sales Account)
	 */
	public void setP_Revenue_Acct(int P_Revenue_Acct) {
		set_ValueNoCheck("P_Revenue_Acct", new Integer(P_Revenue_Acct));
	}

	/**
	 * Get Product Revenue. Account for Product Revenue (Sales Account)
	 */
	public int getP_Revenue_Acct() {
		Integer ii = (Integer) get_Value("P_Revenue_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Recognized Amount */
	public void setRecognizedAmt(BigDecimal RecognizedAmt) {
		if (RecognizedAmt == null)
			throw new IllegalArgumentException("RecognizedAmt is mandatory.");
		set_ValueNoCheck("RecognizedAmt", RecognizedAmt);
	}

	/** Get Recognized Amount */
	public BigDecimal getRecognizedAmt() {
		BigDecimal bd = (BigDecimal) get_Value("RecognizedAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Total Amount. Total Amount
	 */
	public void setTotalAmt(BigDecimal TotalAmt) {
		if (TotalAmt == null)
			throw new IllegalArgumentException("TotalAmt is mandatory.");
		set_ValueNoCheck("TotalAmt", TotalAmt);
	}

	/**
	 * Get Total Amount. Total Amount
	 */
	public BigDecimal getTotalAmt() {
		BigDecimal bd = (BigDecimal) get_Value("TotalAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Unearned Revenue. Account for unearned revenue
	 */
	public void setUnEarnedRevenue_Acct(int UnEarnedRevenue_Acct) {
		set_ValueNoCheck("UnEarnedRevenue_Acct", new Integer(
				UnEarnedRevenue_Acct));
	}

	/**
	 * Get Unearned Revenue. Account for unearned revenue
	 */
	public int getUnEarnedRevenue_Acct() {
		Integer ii = (Integer) get_Value("UnEarnedRevenue_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
