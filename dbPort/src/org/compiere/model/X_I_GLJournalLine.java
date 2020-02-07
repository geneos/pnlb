/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for I_GLJournalLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.312
 */
public class X_I_GLJournalLine extends PO {
	/** Standard Constructor */
	public X_I_GLJournalLine(Properties ctx, int I_GLJournalLine_ID, String trxName) {
		super(ctx, I_GLJournalLine_ID, trxName);
		/**
		 * if (I_GLJournal_ID == 0) { setI_GLJournal_ID (0); setI_IsImported
		 * (false); }
		 */
	}

	/** Load Constructor */
	public X_I_GLJournalLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=I_GLJournal */
	public static final String Table_Name = "I_GLJournalLine";

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
		StringBuffer sb = new StringBuffer("X_I_GLJournalLine[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Account Key. Key of Account Element
	 */
	public void setAccountValue(String AccountValue) {
		if (AccountValue != null && AccountValue.length() > 40) {
			log.warning("Length > 40 - truncated");
			AccountValue = AccountValue.substring(0, 39);
		}
		set_Value("AccountValue", AccountValue);
	}

	/**
	 * Get Account Key. Key of Account Element
	 */
	public String getAccountValue() {
		return (String) get_Value("AccountValue");
	}

	/** Account_ID AD_Reference_ID=132 */
	public static final int ACCOUNT_ID_AD_Reference_ID = 132;

	/**
	 * Set Account. Account used
	 */
	public void setAccount_ID(int Account_ID) {
		if (Account_ID <= 0)
			set_Value("Account_ID", null);
		else
			set_Value("Account_ID", new Integer(Account_ID));
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
		set_Value("AmtAcctCr", AmtAcctCr);
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
		set_Value("AmtAcctDr", AmtAcctDr);
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
		set_Value("AmtSourceCr", AmtSourceCr);
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
		set_Value("AmtSourceDr", AmtSourceDr);
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
	 * Set Journal Line. General Ledger Journal Line
	 */
	public void setGL_JournalLine_ID(int GL_JournalLine_ID) {
		if (GL_JournalLine_ID <= 0)
			set_Value("GL_JournalLine_ID", null);
		else
			set_Value("GL_JournalLine_ID", new Integer(GL_JournalLine_ID));
	}

	/**
	 * Get Journal Line. General Ledger Journal Line
	 */
	public int getGL_JournalLine_ID() {
		Integer ii = (Integer) get_Value("GL_JournalLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Journal. General Ledger Journal
	 */
	public void setGL_Journal_ID(int GL_Journal_ID) {
		if (GL_Journal_ID <= 0)
			set_Value("GL_Journal_ID", null);
		else
			set_Value("GL_Journal_ID", new Integer(GL_Journal_ID));
	}

	/**
	 * Get Journal. General Ledger Journal
	 */
	public int getGL_Journal_ID() {
		Integer ii = (Integer) get_Value("GL_Journal_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}


	/**
	 * Set Import Error Message. Messages generated from import process
	 */
	public void setI_ErrorMsg(String I_ErrorMsg) {
		if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			I_ErrorMsg = I_ErrorMsg.substring(0, 1999);
		}
		set_Value("I_ErrorMsg", I_ErrorMsg);
	}

	/**
	 * Get Import Error Message. Messages generated from import process
	 */
	public String getI_ErrorMsg() {
		return (String) get_Value("I_ErrorMsg");
	}

	/**
	 * Set Import GL Journal. Import General Ledger Journal
	 */
	public void setI_GLJournalLine_ID(int GLJournalLine_ID) {
		if (GLJournalLine_ID < 1)
			throw new IllegalArgumentException("I_GLJournalLine_ID is mandatory.");
		set_ValueNoCheck("I_GLJournalLine_ID", new Integer(GLJournalLine_ID));
	}

	/**
	 * Get Import GL Journal. Import General Ledger Journal
	 */
	public int getI_GLJournalLine_ID() {
		Integer ii = (Integer) get_Value("I_GLJournalLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getI_GLJournalLine_ID()));
	}

	/**
	 * Set Imported. Has this import been processed
	 */
	public void setI_IsImported(boolean I_IsImported) {
		set_Value("I_IsImported", new Boolean(I_IsImported));
	}

	/**
	 * Get Imported. Has this import been processed
	 */
	public boolean isI_IsImported() {
		Object oo = get_Value("I_IsImported");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_Value("Processed", new Boolean(Processed));
	}

	/**
	 * Get Processed. The document has been processed
	 */
	public boolean isProcessed() {
		Object oo = get_Value("Processed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now */
	public void setProcessing(boolean Processing) {
		set_Value("Processing", new Boolean(Processing));
	}

	/** Get Process Now */
	public boolean isProcessing() {
		Object oo = get_Value("Processing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
        
                /**
	 * Set Combination. Valid Account Combination
	 */
	public void setC_ValidCombination_ID(int C_ValidCombination_ID) {
		if (C_ValidCombination_ID <= 0)
			set_Value("C_ValidCombination_ID", null);
		else
			set_Value("C_ValidCombination_ID", new Integer(
					C_ValidCombination_ID));
	}

	/**
	 * Get Combination. Valid Account Combination
	 */
	public int getC_ValidCombination_ID() {
		Integer ii = (Integer) get_Value("C_ValidCombination_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

}
