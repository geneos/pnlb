/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_DunningRunLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.375
 */
public class X_C_DunningRunLine extends PO {
	/** Standard Constructor */
	public X_C_DunningRunLine(Properties ctx, int C_DunningRunLine_ID,
			String trxName) {
		super(ctx, C_DunningRunLine_ID, trxName);
		/**
		 * if (C_DunningRunLine_ID == 0) { setAmt (Env.ZERO);
		 * setC_DunningRunEntry_ID (0); setC_DunningRunLine_ID (0);
		 * setConvertedAmt (Env.ZERO); setDaysDue (0); setFeeAmt (Env.ZERO);
		 * setInterestAmt (Env.ZERO); setIsInDispute (false); setOpenAmt
		 * (Env.ZERO); setProcessed (false); setTimesDunned (0); setTotalAmt
		 * (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_DunningRunLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_DunningRunLine */
	public static final String Table_Name = "C_DunningRunLine";

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
		StringBuffer sb = new StringBuffer("X_C_DunningRunLine[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Amount. Amount
	 */
	public void setAmt(BigDecimal Amt) {
		if (Amt == null)
			throw new IllegalArgumentException("Amt is mandatory.");
		set_Value("Amt", Amt);
	}

	/**
	 * Get Amount. Amount
	 */
	public BigDecimal getAmt() {
		BigDecimal bd = (BigDecimal) get_Value("Amt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Dunning Run Entry. Dunning Run Entry
	 */
	public void setC_DunningRunEntry_ID(int C_DunningRunEntry_ID) {
		if (C_DunningRunEntry_ID < 1)
			throw new IllegalArgumentException(
					"C_DunningRunEntry_ID is mandatory.");
		set_ValueNoCheck("C_DunningRunEntry_ID", new Integer(
				C_DunningRunEntry_ID));
	}

	/**
	 * Get Dunning Run Entry. Dunning Run Entry
	 */
	public int getC_DunningRunEntry_ID() {
		Integer ii = (Integer) get_Value("C_DunningRunEntry_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Dunning Run Line. Dunning Run Line
	 */
	public void setC_DunningRunLine_ID(int C_DunningRunLine_ID) {
		if (C_DunningRunLine_ID < 1)
			throw new IllegalArgumentException(
					"C_DunningRunLine_ID is mandatory.");
		set_ValueNoCheck("C_DunningRunLine_ID",
				new Integer(C_DunningRunLine_ID));
	}

	/**
	 * Get Dunning Run Line. Dunning Run Line
	 */
	public int getC_DunningRunLine_ID() {
		Integer ii = (Integer) get_Value("C_DunningRunLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Invoice. Invoice Identifier
	 */
	public void setC_Invoice_ID(int C_Invoice_ID) {
		if (C_Invoice_ID <= 0)
			set_Value("C_Invoice_ID", null);
		else
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_Invoice_ID()));
	}

	/**
	 * Set Payment. Payment identifier
	 */
	public void setC_Payment_ID(int C_Payment_ID) {
		if (C_Payment_ID <= 0)
			set_Value("C_Payment_ID", null);
		else
			set_Value("C_Payment_ID", new Integer(C_Payment_ID));
	}

	/**
	 * Get Payment. Payment identifier
	 */
	public int getC_Payment_ID() {
		Integer ii = (Integer) get_Value("C_Payment_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Converted Amount. Converted Amount
	 */
	public void setConvertedAmt(BigDecimal ConvertedAmt) {
		if (ConvertedAmt == null)
			throw new IllegalArgumentException("ConvertedAmt is mandatory.");
		set_Value("ConvertedAmt", ConvertedAmt);
	}

	/**
	 * Get Converted Amount. Converted Amount
	 */
	public BigDecimal getConvertedAmt() {
		BigDecimal bd = (BigDecimal) get_Value("ConvertedAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Days due. Number of days due (negative: due in number of days)
	 */
	public void setDaysDue(int DaysDue) {
		set_Value("DaysDue", new Integer(DaysDue));
	}

	/**
	 * Get Days due. Number of days due (negative: due in number of days)
	 */
	public int getDaysDue() {
		Integer ii = (Integer) get_Value("DaysDue");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Fee Amount. Fee amount in invoice currency
	 */
	public void setFeeAmt(BigDecimal FeeAmt) {
		if (FeeAmt == null)
			throw new IllegalArgumentException("FeeAmt is mandatory.");
		set_Value("FeeAmt", FeeAmt);
	}

	/**
	 * Get Fee Amount. Fee amount in invoice currency
	 */
	public BigDecimal getFeeAmt() {
		BigDecimal bd = (BigDecimal) get_Value("FeeAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Interest Amount. Interest Amount
	 */
	public void setInterestAmt(BigDecimal InterestAmt) {
		if (InterestAmt == null)
			throw new IllegalArgumentException("InterestAmt is mandatory.");
		set_Value("InterestAmt", InterestAmt);
	}

	/**
	 * Get Interest Amount. Interest Amount
	 */
	public BigDecimal getInterestAmt() {
		BigDecimal bd = (BigDecimal) get_Value("InterestAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set In Dispute. Document is in dispute
	 */
	public void setIsInDispute(boolean IsInDispute) {
		set_Value("IsInDispute", new Boolean(IsInDispute));
	}

	/**
	 * Get In Dispute. Document is in dispute
	 */
	public boolean isInDispute() {
		Object oo = get_Value("IsInDispute");
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
	 * Set Times Dunned. Number of times dunned previously
	 */
	public void setTimesDunned(int TimesDunned) {
		set_Value("TimesDunned", new Integer(TimesDunned));
	}

	/**
	 * Get Times Dunned. Number of times dunned previously
	 */
	public int getTimesDunned() {
		Integer ii = (Integer) get_Value("TimesDunned");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Total Amount. Total Amount
	 */
	public void setTotalAmt(BigDecimal TotalAmt) {
		if (TotalAmt == null)
			throw new IllegalArgumentException("TotalAmt is mandatory.");
		set_Value("TotalAmt", TotalAmt);
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
}
