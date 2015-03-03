/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_RfQResponseLineQty
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.5
 */
public class X_C_RfQResponseLineQty extends PO {
	/** Standard Constructor */
	public X_C_RfQResponseLineQty(Properties ctx, int C_RfQResponseLineQty_ID,
			String trxName) {
		super(ctx, C_RfQResponseLineQty_ID, trxName);
		/**
		 * if (C_RfQResponseLineQty_ID == 0) { setC_RfQLineQty_ID (0);
		 * setC_RfQResponseLineQty_ID (0); setC_RfQResponseLine_ID (0); setPrice
		 * (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_RfQResponseLineQty(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_RfQResponseLineQty */
	public static final String Table_Name = "C_RfQResponseLineQty";

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
		StringBuffer sb = new StringBuffer("X_C_RfQResponseLineQty[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set RfQ Line Quantity. Request for Quotation Line Quantity
	 */
	public void setC_RfQLineQty_ID(int C_RfQLineQty_ID) {
		if (C_RfQLineQty_ID < 1)
			throw new IllegalArgumentException("C_RfQLineQty_ID is mandatory.");
		set_ValueNoCheck("C_RfQLineQty_ID", new Integer(C_RfQLineQty_ID));
	}

	/**
	 * Get RfQ Line Quantity. Request for Quotation Line Quantity
	 */
	public int getC_RfQLineQty_ID() {
		Integer ii = (Integer) get_Value("C_RfQLineQty_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set RfQ Response Line Qty. Request for Quotation Response Line Quantity
	 */
	public void setC_RfQResponseLineQty_ID(int C_RfQResponseLineQty_ID) {
		if (C_RfQResponseLineQty_ID < 1)
			throw new IllegalArgumentException(
					"C_RfQResponseLineQty_ID is mandatory.");
		set_ValueNoCheck("C_RfQResponseLineQty_ID", new Integer(
				C_RfQResponseLineQty_ID));
	}

	/**
	 * Get RfQ Response Line Qty. Request for Quotation Response Line Quantity
	 */
	public int getC_RfQResponseLineQty_ID() {
		Integer ii = (Integer) get_Value("C_RfQResponseLineQty_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set RfQ Response Line. Request for Quotation Response Line
	 */
	public void setC_RfQResponseLine_ID(int C_RfQResponseLine_ID) {
		if (C_RfQResponseLine_ID < 1)
			throw new IllegalArgumentException(
					"C_RfQResponseLine_ID is mandatory.");
		set_ValueNoCheck("C_RfQResponseLine_ID", new Integer(
				C_RfQResponseLine_ID));
	}

	/**
	 * Get RfQ Response Line. Request for Quotation Response Line
	 */
	public int getC_RfQResponseLine_ID() {
		Integer ii = (Integer) get_Value("C_RfQResponseLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getC_RfQResponseLine_ID()));
	}

	/**
	 * Set Discount %. Discount in percent
	 */
	public void setDiscount(BigDecimal Discount) {
		set_Value("Discount", Discount);
	}

	/**
	 * Get Discount %. Discount in percent
	 */
	public BigDecimal getDiscount() {
		BigDecimal bd = (BigDecimal) get_Value("Discount");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Price. Price
	 */
	public void setPrice(BigDecimal Price) {
		if (Price == null)
			throw new IllegalArgumentException("Price is mandatory.");
		set_Value("Price", Price);
	}

	/**
	 * Get Price. Price
	 */
	public BigDecimal getPrice() {
		BigDecimal bd = (BigDecimal) get_Value("Price");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Ranking. Relative Rank Number
	 */
	public void setRanking(int Ranking) {
		set_Value("Ranking", new Integer(Ranking));
	}

	/**
	 * Get Ranking. Relative Rank Number
	 */
	public int getRanking() {
		Integer ii = (Integer) get_Value("Ranking");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
