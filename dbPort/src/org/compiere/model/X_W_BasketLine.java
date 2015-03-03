/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for W_BasketLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.328
 */
public class X_W_BasketLine extends PO {
	/** Standard Constructor */
	public X_W_BasketLine(Properties ctx, int W_BasketLine_ID, String trxName) {
		super(ctx, W_BasketLine_ID, trxName);
		/**
		 * if (W_BasketLine_ID == 0) { setDescription (null); setLine (0);
		 * setPrice (Env.ZERO); setProduct (null); setQty (Env.ZERO);
		 * setW_BasketLine_ID (0); setW_Basket_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_W_BasketLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=W_BasketLine */
	public static final String Table_Name = "W_BasketLine";

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
		StringBuffer sb = new StringBuffer("X_W_BasketLine[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description == null)
			throw new IllegalArgumentException("Description is mandatory.");
		if (Description.length() > 255) {
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

	/** Set Product */
	public void setProduct(String Product) {
		if (Product == null)
			throw new IllegalArgumentException("Product is mandatory.");
		if (Product.length() > 40) {
			log.warning("Length > 40 - truncated");
			Product = Product.substring(0, 39);
		}
		set_Value("Product", Product);
	}

	/** Get Product */
	public String getProduct() {
		return (String) get_Value("Product");
	}

	/**
	 * Set Quantity. Quantity
	 */
	public void setQty(BigDecimal Qty) {
		if (Qty == null)
			throw new IllegalArgumentException("Qty is mandatory.");
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

	/**
	 * Set Basket Line. Web Basket Line
	 */
	public void setW_BasketLine_ID(int W_BasketLine_ID) {
		if (W_BasketLine_ID < 1)
			throw new IllegalArgumentException("W_BasketLine_ID is mandatory.");
		set_ValueNoCheck("W_BasketLine_ID", new Integer(W_BasketLine_ID));
	}

	/**
	 * Get Basket Line. Web Basket Line
	 */
	public int getW_BasketLine_ID() {
		Integer ii = (Integer) get_Value("W_BasketLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set W_Basket_ID. Web Basket
	 */
	public void setW_Basket_ID(int W_Basket_ID) {
		if (W_Basket_ID < 1)
			throw new IllegalArgumentException("W_Basket_ID is mandatory.");
		set_ValueNoCheck("W_Basket_ID", new Integer(W_Basket_ID));
	}

	/**
	 * Get W_Basket_ID. Web Basket
	 */
	public int getW_Basket_ID() {
		Integer ii = (Integer) get_Value("W_Basket_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
