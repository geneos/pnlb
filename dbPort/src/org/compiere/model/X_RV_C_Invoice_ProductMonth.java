/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for RV_C_Invoice_ProductMonth
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.312
 */
public class X_RV_C_Invoice_ProductMonth extends PO {
	/** Standard Constructor */
	public X_RV_C_Invoice_ProductMonth(Properties ctx,
			int RV_C_Invoice_ProductMonth_ID, String trxName) {
		super(ctx, RV_C_Invoice_ProductMonth_ID, trxName);
		/**
		 * if (RV_C_Invoice_ProductMonth_ID == 0) { setIsSOTrx (false); }
		 */
	}

	/** Load Constructor */
	public X_RV_C_Invoice_ProductMonth(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=RV_C_Invoice_ProductMonth */
	public static final String Table_Name = "RV_C_Invoice_ProductMonth";

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
		StringBuffer sb = new StringBuffer("X_RV_C_Invoice_ProductMonth[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Date Invoiced. Date printed on Invoice
	 */
	public void setDateInvoiced(Timestamp DateInvoiced) {
		set_Value("DateInvoiced", DateInvoiced);
	}

	/**
	 * Get Date Invoiced. Date printed on Invoice
	 */
	public Timestamp getDateInvoiced() {
		return (Timestamp) get_Value("DateInvoiced");
	}

	/**
	 * Set Sales Transaction. This is a Sales Transaction
	 */
	public void setIsSOTrx(boolean IsSOTrx) {
		set_Value("IsSOTrx", new Boolean(IsSOTrx));
	}

	/**
	 * Get Sales Transaction. This is a Sales Transaction
	 */
	public boolean isSOTrx() {
		Object oo = get_Value("IsSOTrx");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Line Discount %. Line Discount as a percentage
	 */
	public void setLineDiscount(BigDecimal LineDiscount) {
		set_Value("LineDiscount", LineDiscount);
	}

	/**
	 * Get Line Discount %. Line Discount as a percentage
	 */
	public BigDecimal getLineDiscount() {
		BigDecimal bd = (BigDecimal) get_Value("LineDiscount");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Line Discount. Line Discount Amount
	 */
	public void setLineDiscountAmt(BigDecimal LineDiscountAmt) {
		set_Value("LineDiscountAmt", LineDiscountAmt);
	}

	/**
	 * Get Line Discount. Line Discount Amount
	 */
	public BigDecimal getLineDiscountAmt() {
		BigDecimal bd = (BigDecimal) get_Value("LineDiscountAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Line Limit Amount */
	public void setLineLimitAmt(BigDecimal LineLimitAmt) {
		set_Value("LineLimitAmt", LineLimitAmt);
	}

	/** Get Line Limit Amount */
	public BigDecimal getLineLimitAmt() {
		BigDecimal bd = (BigDecimal) get_Value("LineLimitAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Line List Amount */
	public void setLineListAmt(BigDecimal LineListAmt) {
		set_Value("LineListAmt", LineListAmt);
	}

	/** Get Line List Amount */
	public BigDecimal getLineListAmt() {
		BigDecimal bd = (BigDecimal) get_Value("LineListAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Line Amount. Line Extended Amount (Quantity * Actual Price) without
	 * Freight and Charges
	 */
	public void setLineNetAmt(BigDecimal LineNetAmt) {
		set_Value("LineNetAmt", LineNetAmt);
	}

	/**
	 * Get Line Amount. Line Extended Amount (Quantity * Actual Price) without
	 * Freight and Charges
	 */
	public BigDecimal getLineNetAmt() {
		BigDecimal bd = (BigDecimal) get_Value("LineNetAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Gross margin % */
	public void setLineOverLimit(BigDecimal LineOverLimit) {
		set_Value("LineOverLimit", LineOverLimit);
	}

	/** Get Gross margin % */
	public BigDecimal getLineOverLimit() {
		BigDecimal bd = (BigDecimal) get_Value("LineOverLimit");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Gross Margin */
	public void setLineOverLimitAmt(BigDecimal LineOverLimitAmt) {
		set_Value("LineOverLimitAmt", LineOverLimitAmt);
	}

	/** Get Gross Margin */
	public BigDecimal getLineOverLimitAmt() {
		BigDecimal bd = (BigDecimal) get_Value("LineOverLimitAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID <= 0)
			set_Value("M_Product_ID", null);
		else
			set_Value("M_Product_ID", new Integer(M_Product_ID));
	}

	/**
	 * Get Product. Product, Service, Item
	 */
	public int getM_Product_ID() {
		Integer ii = (Integer) get_Value("M_Product_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Quantity Invoiced. Invoiced Quantity
	 */
	public void setQtyInvoiced(BigDecimal QtyInvoiced) {
		set_Value("QtyInvoiced", QtyInvoiced);
	}

	/**
	 * Get Quantity Invoiced. Invoiced Quantity
	 */
	public BigDecimal getQtyInvoiced() {
		BigDecimal bd = (BigDecimal) get_Value("QtyInvoiced");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
