/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_AcctSchema_GL
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.531
 */
public class X_C_AcctSchema_GL extends PO {
	/** Standard Constructor */
	public X_C_AcctSchema_GL(Properties ctx, int C_AcctSchema_GL_ID,
			String trxName) {
		super(ctx, C_AcctSchema_GL_ID, trxName);
		/**
		 * if (C_AcctSchema_GL_ID == 0) { setC_AcctSchema_ID (0);
		 * setCommitmentOffset_Acct (0); setIncomeSummary_Acct (0);
		 * setIntercompanyDueFrom_Acct (0); setIntercompanyDueTo_Acct (0);
		 * setPPVOffset_Acct (0); setRetainedEarning_Acct (0);
		 * setUseCurrencyBalancing (false); setUseSuspenseBalancing (false);
		 * setUseSuspenseError (false); }
		 */
	}

	/** Load Constructor */
	public X_C_AcctSchema_GL(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_AcctSchema_GL */
	public static final String Table_Name = "C_AcctSchema_GL";

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
		StringBuffer sb = new StringBuffer("X_C_AcctSchema_GL[").append(
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_AcctSchema_ID()));
	}

	/**
	 * Set Commitment Offset. Budgetary Commitment Offset Account
	 */
	public void setCommitmentOffset_Acct(int CommitmentOffset_Acct) {
		set_Value("CommitmentOffset_Acct", new Integer(CommitmentOffset_Acct));
	}

	/**
	 * Get Commitment Offset. Budgetary Commitment Offset Account
	 */
	public int getCommitmentOffset_Acct() {
		Integer ii = (Integer) get_Value("CommitmentOffset_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Currency Balancing Acct. Account used when a currency is out of
	 * balance
	 */
	public void setCurrencyBalancing_Acct(int CurrencyBalancing_Acct) {
		set_Value("CurrencyBalancing_Acct", new Integer(CurrencyBalancing_Acct));
	}

	/**
	 * Get Currency Balancing Acct. Account used when a currency is out of
	 * balance
	 */
	public int getCurrencyBalancing_Acct() {
		Integer ii = (Integer) get_Value("CurrencyBalancing_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Income Summary Acct. Income Summary Account
	 */
	public void setIncomeSummary_Acct(int IncomeSummary_Acct) {
		set_Value("IncomeSummary_Acct", new Integer(IncomeSummary_Acct));
	}

	/**
	 * Get Income Summary Acct. Income Summary Account
	 */
	public int getIncomeSummary_Acct() {
		Integer ii = (Integer) get_Value("IncomeSummary_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Intercompany Due From Acct. Intercompany Due From / Receivables
	 * Account
	 */
	public void setIntercompanyDueFrom_Acct(int IntercompanyDueFrom_Acct) {
		set_Value("IntercompanyDueFrom_Acct", new Integer(
				IntercompanyDueFrom_Acct));
	}

	/**
	 * Get Intercompany Due From Acct. Intercompany Due From / Receivables
	 * Account
	 */
	public int getIntercompanyDueFrom_Acct() {
		Integer ii = (Integer) get_Value("IntercompanyDueFrom_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Intercompany Due To Acct. Intercompany Due To / Payable Account
	 */
	public void setIntercompanyDueTo_Acct(int IntercompanyDueTo_Acct) {
		set_Value("IntercompanyDueTo_Acct", new Integer(IntercompanyDueTo_Acct));
	}

	/**
	 * Get Intercompany Due To Acct. Intercompany Due To / Payable Account
	 */
	public int getIntercompanyDueTo_Acct() {
		Integer ii = (Integer) get_Value("IntercompanyDueTo_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set PPV Offset. Purchase Price Variance Offset Account
	 */
	public void setPPVOffset_Acct(int PPVOffset_Acct) {
		set_Value("PPVOffset_Acct", new Integer(PPVOffset_Acct));
	}

	/**
	 * Get PPV Offset. Purchase Price Variance Offset Account
	 */
	public int getPPVOffset_Acct() {
		Integer ii = (Integer) get_Value("PPVOffset_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Retained Earning Acct */
	public void setRetainedEarning_Acct(int RetainedEarning_Acct) {
		set_Value("RetainedEarning_Acct", new Integer(RetainedEarning_Acct));
	}

	/** Get Retained Earning Acct */
	public int getRetainedEarning_Acct() {
		Integer ii = (Integer) get_Value("RetainedEarning_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Suspense Balancing Acct */
	public void setSuspenseBalancing_Acct(int SuspenseBalancing_Acct) {
		set_Value("SuspenseBalancing_Acct", new Integer(SuspenseBalancing_Acct));
	}

	/** Get Suspense Balancing Acct */
	public int getSuspenseBalancing_Acct() {
		Integer ii = (Integer) get_Value("SuspenseBalancing_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Suspense Error Acct */
	public void setSuspenseError_Acct(int SuspenseError_Acct) {
		set_Value("SuspenseError_Acct", new Integer(SuspenseError_Acct));
	}

	/** Get Suspense Error Acct */
	public int getSuspenseError_Acct() {
		Integer ii = (Integer) get_Value("SuspenseError_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Use Currency Balancing */
	public void setUseCurrencyBalancing(boolean UseCurrencyBalancing) {
		set_Value("UseCurrencyBalancing", new Boolean(UseCurrencyBalancing));
	}

	/** Get Use Currency Balancing */
	public boolean isUseCurrencyBalancing() {
		Object oo = get_Value("UseCurrencyBalancing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Use Suspense Balancing */
	public void setUseSuspenseBalancing(boolean UseSuspenseBalancing) {
		set_Value("UseSuspenseBalancing", new Boolean(UseSuspenseBalancing));
	}

	/** Get Use Suspense Balancing */
	public boolean isUseSuspenseBalancing() {
		Object oo = get_Value("UseSuspenseBalancing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Use Suspense Error */
	public void setUseSuspenseError(boolean UseSuspenseError) {
		set_Value("UseSuspenseError", new Boolean(UseSuspenseError));
	}

	/** Get Use Suspense Error */
	public boolean isUseSuspenseError() {
		Object oo = get_Value("UseSuspenseError");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
}
