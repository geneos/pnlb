/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_TaxDeclarationAcct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.671
 */
public class X_C_TaxDeclarationAcct extends PO {
	/** Standard Constructor */
	public X_C_TaxDeclarationAcct(Properties ctx, int C_TaxDeclarationAcct_ID,
			String trxName) {
		super(ctx, C_TaxDeclarationAcct_ID, trxName);
		/**
		 * if (C_TaxDeclarationAcct_ID == 0) { setC_AcctSchema_ID (0);
		 * setC_TaxDeclarationAcct_ID (0); setC_TaxDeclaration_ID (0);
		 * setFact_Acct_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_C_TaxDeclarationAcct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_TaxDeclarationAcct */
	public static final String Table_Name = "C_TaxDeclarationAcct";

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
		StringBuffer sb = new StringBuffer("X_C_TaxDeclarationAcct[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** Account_ID AD_Reference_ID=331 */
	public static final int ACCOUNT_ID_AD_Reference_ID = 331;

	/**
	 * Set Account. Account used
	 */
	public void setAccount_ID(int Account_ID) {
		throw new IllegalArgumentException("Account_ID is virtual column");
	}

	/**
	 * Get Account. Account used
	 */
	public int getAccount_ID() {
		Integer ii = (Integer) get_Value("Account_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Accounted Credit. Accounted Credit Amount
	 */
	public void setAmtAcctCr(BigDecimal AmtAcctCr) {
		throw new IllegalArgumentException("AmtAcctCr is virtual column");
	}

	/**
	 * Get Accounted Credit. Accounted Credit Amount
	 */
	public BigDecimal getAmtAcctCr() {
		BigDecimal bd = (BigDecimal) get_Value("AmtAcctCr");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Accounted Debit. Accounted Debit Amount
	 */
	public void setAmtAcctDr(BigDecimal AmtAcctDr) {
		throw new IllegalArgumentException("AmtAcctDr is virtual column");
	}

	/**
	 * Get Accounted Debit. Accounted Debit Amount
	 */
	public BigDecimal getAmtAcctDr() {
		BigDecimal bd = (BigDecimal) get_Value("AmtAcctDr");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Source Credit. Source Credit Amount
	 */
	public void setAmtSourceCr(BigDecimal AmtSourceCr) {
		throw new IllegalArgumentException("AmtSourceCr is virtual column");
	}

	/**
	 * Get Source Credit. Source Credit Amount
	 */
	public BigDecimal getAmtSourceCr() {
		BigDecimal bd = (BigDecimal) get_Value("AmtSourceCr");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Source Debit. Source Debit Amount
	 */
	public void setAmtSourceDr(BigDecimal AmtSourceDr) {
		throw new IllegalArgumentException("AmtSourceDr is virtual column");
	}

	/**
	 * Get Source Debit. Source Debit Amount
	 */
	public BigDecimal getAmtSourceDr() {
		BigDecimal bd = (BigDecimal) get_Value("AmtSourceDr");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
		throw new IllegalArgumentException("C_BPartner_ID is virtual column");
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
	 * Set Currency. The Currency for this record
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		throw new IllegalArgumentException("C_Currency_ID is virtual column");
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
	 * Set Tax Declaration Accounting. Tax Accounting Reconciliation
	 */
	public void setC_TaxDeclarationAcct_ID(int C_TaxDeclarationAcct_ID) {
		if (C_TaxDeclarationAcct_ID < 1)
			throw new IllegalArgumentException(
					"C_TaxDeclarationAcct_ID is mandatory.");
		set_ValueNoCheck("C_TaxDeclarationAcct_ID", new Integer(
				C_TaxDeclarationAcct_ID));
	}

	/**
	 * Get Tax Declaration Accounting. Tax Accounting Reconciliation
	 */
	public int getC_TaxDeclarationAcct_ID() {
		Integer ii = (Integer) get_Value("C_TaxDeclarationAcct_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Tax Declaration. Define the declaration to the tax authorities
	 */
	public void setC_TaxDeclaration_ID(int C_TaxDeclaration_ID) {
		if (C_TaxDeclaration_ID < 1)
			throw new IllegalArgumentException(
					"C_TaxDeclaration_ID is mandatory.");
		set_ValueNoCheck("C_TaxDeclaration_ID",
				new Integer(C_TaxDeclaration_ID));
	}

	/**
	 * Get Tax Declaration. Define the declaration to the tax authorities
	 */
	public int getC_TaxDeclaration_ID() {
		Integer ii = (Integer) get_Value("C_TaxDeclaration_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Tax. Tax identifier
	 */
	public void setC_Tax_ID(int C_Tax_ID) {
		throw new IllegalArgumentException("C_Tax_ID is virtual column");
	}

	/**
	 * Get Tax. Tax identifier
	 */
	public int getC_Tax_ID() {
		Integer ii = (Integer) get_Value("C_Tax_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Account Date. Accounting Date
	 */
	public void setDateAcct(Timestamp DateAcct) {
		throw new IllegalArgumentException("DateAcct is virtual column");
	}

	/**
	 * Get Account Date. Accounting Date
	 */
	public Timestamp getDateAcct() {
		return (Timestamp) get_Value("DateAcct");
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
	 * Set Line No. Unique line for this document
	 */
	public void setLine(int Line) {
		set_Value("Line", new Integer(Line));
	}

	/**
	 * Get Line No. Unique line for this document
	 */
	public int getLine() {
		Integer ii = (Integer) get_Value("Line");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
