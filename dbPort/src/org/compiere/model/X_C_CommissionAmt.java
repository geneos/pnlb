/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_CommissionAmt
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.125
 */
public class X_C_CommissionAmt extends PO {
	/** Standard Constructor */
	public X_C_CommissionAmt(Properties ctx, int C_CommissionAmt_ID,
			String trxName) {
		super(ctx, C_CommissionAmt_ID, trxName);
		/**
		 * if (C_CommissionAmt_ID == 0) { setActualQty (Env.ZERO);
		 * setC_CommissionAmt_ID (0); setC_CommissionLine_ID (0);
		 * setC_CommissionRun_ID (0); setCommissionAmt (Env.ZERO);
		 * setConvertedAmt (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_CommissionAmt(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_CommissionAmt */
	public static final String Table_Name = "C_CommissionAmt";

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
		StringBuffer sb = new StringBuffer("X_C_CommissionAmt[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Actual Quantity. The actual quantity
	 */
	public void setActualQty(BigDecimal ActualQty) {
		if (ActualQty == null)
			throw new IllegalArgumentException("ActualQty is mandatory.");
		set_Value("ActualQty", ActualQty);
	}

	/**
	 * Get Actual Quantity. The actual quantity
	 */
	public BigDecimal getActualQty() {
		BigDecimal bd = (BigDecimal) get_Value("ActualQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Commission Amount. Generated Commission Amount
	 */
	public void setC_CommissionAmt_ID(int C_CommissionAmt_ID) {
		if (C_CommissionAmt_ID < 1)
			throw new IllegalArgumentException(
					"C_CommissionAmt_ID is mandatory.");
		set_ValueNoCheck("C_CommissionAmt_ID", new Integer(C_CommissionAmt_ID));
	}

	/**
	 * Get Commission Amount. Generated Commission Amount
	 */
	public int getC_CommissionAmt_ID() {
		Integer ii = (Integer) get_Value("C_CommissionAmt_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Commission Line. Commission Line
	 */
	public void setC_CommissionLine_ID(int C_CommissionLine_ID) {
		if (C_CommissionLine_ID < 1)
			throw new IllegalArgumentException(
					"C_CommissionLine_ID is mandatory.");
		set_Value("C_CommissionLine_ID", new Integer(C_CommissionLine_ID));
	}

	/**
	 * Get Commission Line. Commission Line
	 */
	public int getC_CommissionLine_ID() {
		Integer ii = (Integer) get_Value("C_CommissionLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Commission Run. Commission Run or Process
	 */
	public void setC_CommissionRun_ID(int C_CommissionRun_ID) {
		if (C_CommissionRun_ID < 1)
			throw new IllegalArgumentException(
					"C_CommissionRun_ID is mandatory.");
		set_ValueNoCheck("C_CommissionRun_ID", new Integer(C_CommissionRun_ID));
	}

	/**
	 * Get Commission Run. Commission Run or Process
	 */
	public int getC_CommissionRun_ID() {
		Integer ii = (Integer) get_Value("C_CommissionRun_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getC_CommissionRun_ID()));
	}

	/**
	 * Set Commission Amount. Commission Amount
	 */
	public void setCommissionAmt(BigDecimal CommissionAmt) {
		if (CommissionAmt == null)
			throw new IllegalArgumentException("CommissionAmt is mandatory.");
		set_Value("CommissionAmt", CommissionAmt);
	}

	/**
	 * Get Commission Amount. Commission Amount
	 */
	public BigDecimal getCommissionAmt() {
		BigDecimal bd = (BigDecimal) get_Value("CommissionAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
}
