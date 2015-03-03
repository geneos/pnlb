/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for GL_JournalLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.187
 */
public class X_GL_JournalLine extends PO {
	/** Standard Constructor */
	public X_GL_JournalLine(Properties ctx, int GL_JournalLine_ID,
			String trxName) {
		super(ctx, GL_JournalLine_ID, trxName);
		/**
		 * if (GL_JournalLine_ID == 0) { setAmtAcctCr (Env.ZERO); setAmtAcctDr
		 * (Env.ZERO); setAmtSourceCr (Env.ZERO); setAmtSourceDr (Env.ZERO);
		 * setC_ConversionType_ID (0); setC_Currency_ID (0); //
		 * 
		 * @C_Currency_ID@ setC_ValidCombination_ID (0); setCurrencyRate
		 *                 (Env.ZERO); //
		 * @CurrencyRate@; 1 setDateAcct (new
		 *                 Timestamp(System.currentTimeMillis())); //
		 * @DateAcct@ setGL_JournalLine_ID (0); setGL_Journal_ID (0);
		 *            setIsGenerated (false); setLine (0); //
		 * @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM GL_JournalLine
		 *             WHERE GL_Journal_ID=@GL_Journal_ID@ setProcessed (false); }
		 */
	}

	/** Load Constructor */
	public X_GL_JournalLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=GL_JournalLine */
	public static final String Table_Name = "GL_JournalLine";

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
		StringBuffer sb = new StringBuffer("X_GL_JournalLine[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Accounted Credit. Accounted Credit Amount
	 */
	public void setAmtAcctCr(BigDecimal AmtAcctCr) {
		if (AmtAcctCr == null)
			throw new IllegalArgumentException("AmtAcctCr is mandatory.");
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
		if (AmtAcctDr == null)
			throw new IllegalArgumentException("AmtAcctDr is mandatory.");
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

	/**
	 * Set Source Credit. Source Credit Amount
	 */
	public void setAmtSourceCr(BigDecimal AmtSourceCr) {
		if (AmtSourceCr == null)
			throw new IllegalArgumentException("AmtSourceCr is mandatory.");
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
		if (AmtSourceDr == null)
			throw new IllegalArgumentException("AmtSourceDr is mandatory.");
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
	 * Set Currency Type. Currency Conversion Rate Type
	 */
	public void setC_ConversionType_ID(int C_ConversionType_ID) {
		if (C_ConversionType_ID < 1)
			throw new IllegalArgumentException(
					"C_ConversionType_ID is mandatory.");
		set_Value("C_ConversionType_ID", new Integer(C_ConversionType_ID));
	}

	/**
	 * Get Currency Type. Currency Conversion Rate Type
	 */
	public int getC_ConversionType_ID() {
		Integer ii = (Integer) get_Value("C_ConversionType_ID");
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
	 * Set UOM. Unit of Measure
	 */
	public void setC_UOM_ID(int C_UOM_ID) {
		if (C_UOM_ID <= 0)
			set_Value("C_UOM_ID", null);
		else
			set_Value("C_UOM_ID", new Integer(C_UOM_ID));
	}

	/**
	 * Get UOM. Unit of Measure
	 */
	public int getC_UOM_ID() {
		Integer ii = (Integer) get_Value("C_UOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Combination. Valid Account Combination
	 */
	public void setC_ElementValue_ID(int C_ElementValue_ID) {
		if (C_ElementValue_ID < 1)
			throw new IllegalArgumentException(
					"C_ElementValue_ID is mandatory.");
		set_Value("C_ElementValue_ID", new Integer(C_ElementValue_ID));
	}

	/**
	 * Get Combination. Valid Account Combination
	 */
	public int getC_ElementValue_ID() {
		Integer ii = (Integer) get_Value("C_ElementValue_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Rate. Currency Conversion Rate
	 */
	public void setCurrencyRate(BigDecimal CurrencyRate) {
		if (CurrencyRate == null)
			throw new IllegalArgumentException("CurrencyRate is mandatory.");
		set_ValueNoCheck("CurrencyRate", CurrencyRate);
	}

	/**
	 * Get Rate. Currency Conversion Rate
	 */
	public BigDecimal getCurrencyRate() {
		BigDecimal bd = (BigDecimal) get_Value("CurrencyRate");
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
		set_Value("DateAcct", DateAcct);
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

	/**
	 * Set Journal Line. General Ledger Journal Line
	 */
	public void setGL_JournalLine_ID(int GL_JournalLine_ID) {
		if (GL_JournalLine_ID < 1)
			throw new IllegalArgumentException(
					"GL_JournalLine_ID is mandatory.");
		set_ValueNoCheck("GL_JournalLine_ID", new Integer(GL_JournalLine_ID));
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
		if (GL_Journal_ID < 1)
			throw new IllegalArgumentException("GL_Journal_ID is mandatory.");
		set_ValueNoCheck("GL_Journal_ID", new Integer(GL_Journal_ID));
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
	 * Set Generated. This Line is generated
	 */
	public void setIsGenerated(boolean IsGenerated) {
		set_ValueNoCheck("IsGenerated", new Boolean(IsGenerated));
	}

	/**
	 * Get Generated. This Line is generated
	 */
	public boolean isGenerated() {
		Object oo = get_Value("IsGenerated");
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getLine()));
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

	/**
	 * Set Quantity. Quantity
	 */
	public void setQty(BigDecimal Qty) {
		set_Value("Qty", Qty);
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
