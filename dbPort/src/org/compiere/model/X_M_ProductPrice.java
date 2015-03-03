/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_ProductPrice
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.421
 */
public class X_M_ProductPrice extends PO {
	/** Standard Constructor */
	public X_M_ProductPrice(Properties ctx, int M_ProductPrice_ID,
			String trxName) {
		super(ctx, M_ProductPrice_ID, trxName);
		/**
		 * if (M_ProductPrice_ID == 0) { setM_PriceList_Version_ID (0);
		 * setM_Product_ID (0); setPriceLimit (Env.ZERO); // 0 setPriceList
		 * (Env.ZERO); // 1 setPriceStd (Env.ZERO); // 1 }
		 */
	}

	/** Load Constructor */
	public X_M_ProductPrice(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_ProductPrice */
	public static final String Table_Name = "M_ProductPrice";

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
		StringBuffer sb = new StringBuffer("X_M_ProductPrice[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Price List Version. Identifies a unique instance of a Price List
	 */
	public void setM_PriceList_Version_ID(int M_PriceList_Version_ID) {
		if (M_PriceList_Version_ID < 1)
			throw new IllegalArgumentException(
					"M_PriceList_Version_ID is mandatory.");
		set_ValueNoCheck("M_PriceList_Version_ID", new Integer(
				M_PriceList_Version_ID));
	}

	/**
	 * Get Price List Version. Identifies a unique instance of a Price List
	 */
	public int getM_PriceList_Version_ID() {
		Integer ii = (Integer) get_Value("M_PriceList_Version_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID < 1)
			throw new IllegalArgumentException("M_Product_ID is mandatory.");
		set_ValueNoCheck("M_Product_ID", new Integer(M_Product_ID));
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
	 * Set Limit Price. Lowest price for a product
	 */
	public void setPriceLimit(BigDecimal PriceLimit) {
		if (PriceLimit == null)
			throw new IllegalArgumentException("PriceLimit is mandatory.");
		set_Value("PriceLimit", PriceLimit);
	}

	/**
	 * Get Limit Price. Lowest price for a product
	 */
	public BigDecimal getPriceLimit() {
		BigDecimal bd = (BigDecimal) get_Value("PriceLimit");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set List Price. List Price
	 */
	public void setPriceList(BigDecimal PriceList) {
		if (PriceList == null)
			throw new IllegalArgumentException("PriceList is mandatory.");
		set_Value("PriceList", PriceList);
	}

	/**
	 * Get List Price. List Price
	 */
	public BigDecimal getPriceList() {
		BigDecimal bd = (BigDecimal) get_Value("PriceList");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Standard Price. Standard Price
	 */
	public void setPriceStd(BigDecimal PriceStd) {
		if (PriceStd == null)
			throw new IllegalArgumentException("PriceStd is mandatory.");
		set_Value("PriceStd", PriceStd);
	}

	/**
	 * Get Standard Price. Standard Price
	 */
	public BigDecimal getPriceStd() {
		BigDecimal bd = (BigDecimal) get_Value("PriceStd");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
