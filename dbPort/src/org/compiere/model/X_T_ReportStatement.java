/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_ReportStatement
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.234
 */
public class X_T_ReportStatement extends PO {
	/** Standard Constructor */
	public X_T_ReportStatement(Properties ctx, int T_ReportStatement_ID,
			String trxName) {
		super(ctx, T_ReportStatement_ID, trxName);
		/**
		 * if (T_ReportStatement_ID == 0) { setAD_PInstance_ID (0); setDateAcct
		 * (new Timestamp(System.currentTimeMillis())); setFact_Acct_ID (0);
		 * setLevelNo (0); }
		 */
	}

	/** Load Constructor */
	public X_T_ReportStatement(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_ReportStatement */
	public static final String Table_Name = "T_ReportStatement";

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

	protected BigDecimal accessLevel = new BigDecimal(4);

	/** AccessLevel 4 - System */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_T_ReportStatement[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Process Instance. Instance of the process
	 */
	public void setAD_PInstance_ID(int AD_PInstance_ID) {
		if (AD_PInstance_ID < 1)
			throw new IllegalArgumentException("AD_PInstance_ID is mandatory.");
		set_ValueNoCheck("AD_PInstance_ID", new Integer(AD_PInstance_ID));
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

	/**
	 * Set Accounted Credit. Accounted Credit Amount
	 */
	public void setAmtAcctCr(BigDecimal AmtAcctCr) {
		set_ValueNoCheck("AmtAcctCr", AmtAcctCr);
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
		set_ValueNoCheck("AmtAcctDr", AmtAcctDr);
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

	/** Set Balance */
	public void setBalance(BigDecimal Balance) {
		set_ValueNoCheck("Balance", Balance);
	}

	/** Get Balance */
	public BigDecimal getBalance() {
		BigDecimal bd = (BigDecimal) get_Value("Balance");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Account Date. Accounting Date
	 */
	public void setDateAcct(Timestamp DateAcct) {
		if (DateAcct == null)
			throw new IllegalArgumentException("DateAcct is mandatory.");
		set_ValueNoCheck("DateAcct", DateAcct);
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
		set_ValueNoCheck("Description", Description);
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

	/** Set Level no */
	public void setLevelNo(int LevelNo) {
		set_ValueNoCheck("LevelNo", new Integer(LevelNo));
	}

	/** Get Level no */
	public int getLevelNo() {
		Integer ii = (Integer) get_Value("LevelNo");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name != null && Name.length() > 60) {
			log.warning("Length > 60 - truncated");
			Name = Name.substring(0, 59);
		}
		set_ValueNoCheck("Name", Name);
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

	/**
	 * Set Quantity. Quantity
	 */
	public void setQty(BigDecimal Qty) {
		set_ValueNoCheck("Qty", Qty);
	}

	/**
	 * Get Quantity. Quantity
	 */
	public BigDecimal getQty() {
		BigDecimal bd = (BigDecimal) get_Value("Qty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
