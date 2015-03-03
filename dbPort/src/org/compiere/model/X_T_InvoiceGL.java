/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_InvoiceGL
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.984
 */
public class X_T_InvoiceGL extends PO {
	/** Standard Constructor */
	public X_T_InvoiceGL(Properties ctx, int T_InvoiceGL_ID, String trxName) {
		super(ctx, T_InvoiceGL_ID, trxName);
		/**
		 * if (T_InvoiceGL_ID == 0) { setAD_PInstance_ID (0); setAmtAcctBalance
		 * (Env.ZERO); setAmtRevalCr (Env.ZERO); setAmtRevalCrDiff (Env.ZERO);
		 * setAmtRevalDr (Env.ZERO); setAmtRevalDrDiff (Env.ZERO);
		 * setAmtSourceBalance (Env.ZERO); setC_ConversionTypeReval_ID (0);
		 * setC_Invoice_ID (0); setDateReval (new
		 * Timestamp(System.currentTimeMillis())); setFact_Acct_ID (0);
		 * setGrandTotal (Env.ZERO); setIsAllCurrencies (false); setOpenAmt
		 * (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_T_InvoiceGL(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_InvoiceGL */
	public static final String Table_Name = "T_InvoiceGL";

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
		StringBuffer sb = new StringBuffer("X_T_InvoiceGL[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Process Instance. Instance of the process
	 */
	public void setAD_PInstance_ID(int AD_PInstance_ID) {
		if (AD_PInstance_ID < 1)
			throw new IllegalArgumentException("AD_PInstance_ID is mandatory.");
		set_Value("AD_PInstance_ID", new Integer(AD_PInstance_ID));
	}

	/**
	 * Get Process Instance. Instance of the process
	 */
	public int getAD_PInstance_ID() {
		Integer ii = (Integer) get_Value("AD_PInstance_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** APAR AD_Reference_ID=332 */
	public static final int APAR_AD_Reference_ID = 332;

	/** Receivables & Payables = A */
	public static final String APAR_ReceivablesPayables = "A";

	/** Payables only = P */
	public static final String APAR_PayablesOnly = "P";

	/** Receivables only = R */
	public static final String APAR_ReceivablesOnly = "R";

	/**
	 * Set AP - AR. Include Receivables and/or Payables transactions
	 */
	public void setAPAR(String APAR) {
		if (APAR != null && APAR.length() > 1) {
			log.warning("Length > 1 - truncated");
			APAR = APAR.substring(0, 0);
		}
		set_Value("APAR", APAR);
	}

	/**
	 * Get AP - AR. Include Receivables and/or Payables transactions
	 */
	public String getAPAR() {
		return (String) get_Value("APAR");
	}

	/**
	 * Set Accounted Balance. Accounted Balance Amount
	 */
	public void setAmtAcctBalance(BigDecimal AmtAcctBalance) {
		if (AmtAcctBalance == null)
			throw new IllegalArgumentException("AmtAcctBalance is mandatory.");
		set_Value("AmtAcctBalance", AmtAcctBalance);
	}

	/**
	 * Get Accounted Balance. Accounted Balance Amount
	 */
	public BigDecimal getAmtAcctBalance() {
		BigDecimal bd = (BigDecimal) get_Value("AmtAcctBalance");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Revaluated Amount Cr. Revaluated Cr Amount
	 */
	public void setAmtRevalCr(BigDecimal AmtRevalCr) {
		if (AmtRevalCr == null)
			throw new IllegalArgumentException("AmtRevalCr is mandatory.");
		set_Value("AmtRevalCr", AmtRevalCr);
	}

	/**
	 * Get Revaluated Amount Cr. Revaluated Cr Amount
	 */
	public BigDecimal getAmtRevalCr() {
		BigDecimal bd = (BigDecimal) get_Value("AmtRevalCr");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Revaluated Difference Cr. Revaluated Cr Amount Difference
	 */
	public void setAmtRevalCrDiff(BigDecimal AmtRevalCrDiff) {
		if (AmtRevalCrDiff == null)
			throw new IllegalArgumentException("AmtRevalCrDiff is mandatory.");
		set_Value("AmtRevalCrDiff", AmtRevalCrDiff);
	}

	/**
	 * Get Revaluated Difference Cr. Revaluated Cr Amount Difference
	 */
	public BigDecimal getAmtRevalCrDiff() {
		BigDecimal bd = (BigDecimal) get_Value("AmtRevalCrDiff");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Revaluated Amount Dr. Revaluated Dr Amount
	 */
	public void setAmtRevalDr(BigDecimal AmtRevalDr) {
		if (AmtRevalDr == null)
			throw new IllegalArgumentException("AmtRevalDr is mandatory.");
		set_Value("AmtRevalDr", AmtRevalDr);
	}

	/**
	 * Get Revaluated Amount Dr. Revaluated Dr Amount
	 */
	public BigDecimal getAmtRevalDr() {
		BigDecimal bd = (BigDecimal) get_Value("AmtRevalDr");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Revaluated Difference Dr. Revaluated Dr Amount Difference
	 */
	public void setAmtRevalDrDiff(BigDecimal AmtRevalDrDiff) {
		if (AmtRevalDrDiff == null)
			throw new IllegalArgumentException("AmtRevalDrDiff is mandatory.");
		set_Value("AmtRevalDrDiff", AmtRevalDrDiff);
	}

	/**
	 * Get Revaluated Difference Dr. Revaluated Dr Amount Difference
	 */
	public BigDecimal getAmtRevalDrDiff() {
		BigDecimal bd = (BigDecimal) get_Value("AmtRevalDrDiff");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Source Balance. Source Balance Amount
	 */
	public void setAmtSourceBalance(BigDecimal AmtSourceBalance) {
		if (AmtSourceBalance == null)
			throw new IllegalArgumentException("AmtSourceBalance is mandatory.");
		set_Value("AmtSourceBalance", AmtSourceBalance);
	}

	/**
	 * Get Source Balance. Source Balance Amount
	 */
	public BigDecimal getAmtSourceBalance() {
		BigDecimal bd = (BigDecimal) get_Value("AmtSourceBalance");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** C_ConversionTypeReval_ID AD_Reference_ID=352 */
	public static final int C_CONVERSIONTYPEREVAL_ID_AD_Reference_ID = 352;

	/**
	 * Set Revaluation Conversion Type. Revaluation Currency Conversion Type
	 */
	public void setC_ConversionTypeReval_ID(int C_ConversionTypeReval_ID) {
		if (C_ConversionTypeReval_ID < 1)
			throw new IllegalArgumentException(
					"C_ConversionTypeReval_ID is mandatory.");
		set_Value("C_ConversionTypeReval_ID", new Integer(
				C_ConversionTypeReval_ID));
	}

	/**
	 * Get Revaluation Conversion Type. Revaluation Currency Conversion Type
	 */
	public int getC_ConversionTypeReval_ID() {
		Integer ii = (Integer) get_Value("C_ConversionTypeReval_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** C_DocTypeReval_ID AD_Reference_ID=170 */
	public static final int C_DOCTYPEREVAL_ID_AD_Reference_ID = 170;

	/**
	 * Set Revaluation Document Type. Document Type for Revaluation Journal
	 */
	public void setC_DocTypeReval_ID(int C_DocTypeReval_ID) {
		if (C_DocTypeReval_ID <= 0)
			set_Value("C_DocTypeReval_ID", null);
		else
			set_Value("C_DocTypeReval_ID", new Integer(C_DocTypeReval_ID));
	}

	/**
	 * Get Revaluation Document Type. Document Type for Revaluation Journal
	 */
	public int getC_DocTypeReval_ID() {
		Integer ii = (Integer) get_Value("C_DocTypeReval_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Revaluation Date. Date of Revaluation
	 */
	public void setDateReval(Timestamp DateReval) {
		if (DateReval == null)
			throw new IllegalArgumentException("DateReval is mandatory.");
		set_Value("DateReval", DateReval);
	}

	/**
	 * Get Revaluation Date. Date of Revaluation
	 */
	public Timestamp getDateReval() {
		return (Timestamp) get_Value("DateReval");
	}

	/** Set Accounting Fact */
	public void setFact_Acct_ID(int Fact_Acct_ID) {
		if (Fact_Acct_ID < 1)
			throw new IllegalArgumentException("Fact_Acct_ID is mandatory.");
		set_ValueNoCheck("Fact_Acct_ID", new Integer(Fact_Acct_ID));
	}

	/** Get Accounting Fact */
	public int getFact_Acct_ID() {
		Integer ii = (Integer) get_Value("Fact_Acct_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Grand Total. Total amount of document
	 */
	public void setGrandTotal(BigDecimal GrandTotal) {
		if (GrandTotal == null)
			throw new IllegalArgumentException("GrandTotal is mandatory.");
		set_Value("GrandTotal", GrandTotal);
	}

	/**
	 * Get Grand Total. Total amount of document
	 */
	public BigDecimal getGrandTotal() {
		BigDecimal bd = (BigDecimal) get_Value("GrandTotal");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Include All Currencies. Report not just foreign currency Invoices
	 */
	public void setIsAllCurrencies(boolean IsAllCurrencies) {
		set_Value("IsAllCurrencies", new Boolean(IsAllCurrencies));
	}

	/**
	 * Get Include All Currencies. Report not just foreign currency Invoices
	 */
	public boolean isAllCurrencies() {
		Object oo = get_Value("IsAllCurrencies");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Open Amount. Open item amount
	 */
	public void setOpenAmt(BigDecimal OpenAmt) {
		if (OpenAmt == null)
			throw new IllegalArgumentException("OpenAmt is mandatory.");
		set_Value("OpenAmt", OpenAmt);
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
	 * Set Percent. Percentage
	 */
	public void setPercent(BigDecimal Percent) {
		set_Value("Percent", Percent);
	}

	/**
	 * Get Percent. Percentage
	 */
	public BigDecimal getPercent() {
		BigDecimal bd = (BigDecimal) get_Value("Percent");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
