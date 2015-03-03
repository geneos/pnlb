/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_PRODUCTCHECK
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.125
 */
public class X_T_PRODUCTCHECK extends PO {
	/** Standard Constructor */
	public X_T_PRODUCTCHECK(Properties ctx, int T_PRODUCTCHECK_ID,
			String trxName) {
		super(ctx, T_PRODUCTCHECK_ID, trxName);
		/**
		 * if (T_PRODUCTCHECK_ID == 0) { setAD_PInstance_ID (0); setM_Locator_ID
		 * (0); setM_Product_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_T_PRODUCTCHECK(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_PRODUCTCHECK */
	public static final String Table_Name = "T_PRODUCTCHECK";

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
		StringBuffer sb = new StringBuffer("X_T_PRODUCTCHECK[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Process Instance. Instance of the process
	 */
	public void setAD_PInstance_ID(int AD_PInstance_ID) {
		if (AD_PInstance_ID < 1)
			throw new IllegalArgumentException("AD_PInstance_ID is mandatory.");
		set_Value("AD_PInstance_ID", new Integer(AD_PInstance_ID));
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
	 * Set Locator. Warehouse Locator
	 */
	public void setM_Locator_ID(int M_Locator_ID) {
		if (M_Locator_ID < 1)
			throw new IllegalArgumentException("M_Locator_ID is mandatory.");
		set_Value("M_Locator_ID", new Integer(M_Locator_ID));
	}

	/**
	 * Get Locator. Warehouse Locator
	 */
	public int getM_Locator_ID() {
		Integer ii = (Integer) get_Value("M_Locator_ID");
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
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name != null && Name.length() > 120) {
			log.warning("Length > 120 - truncated");
			Name = Name.substring(0, 119);
		}
		set_Value("Name", Name);
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

	/** Set QTYCUARENTENA */
	public void setQTYCUARENTENA(BigDecimal QTYCUARENTENA) {
		set_Value("QTYCUARENTENA", QTYCUARENTENA);
	}

	/** Get QTYCUARENTENA */
	public BigDecimal getQTYCUARENTENA() {
		BigDecimal bd = (BigDecimal) get_Value("QTYCUARENTENA");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set QUANTITY */
	public void setQUANTITY(BigDecimal QUANTITY) {
		set_Value("QUANTITY", QUANTITY);
	}

	/** Get QUANTITY */
	public BigDecimal getQUANTITY() {
		BigDecimal bd = (BigDecimal) get_Value("QUANTITY");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Available Quantity. Available Quantity (On Hand - Reserved)
	 */
	public void setQtyAvailable(BigDecimal QtyAvailable) {
		set_Value("QtyAvailable", QtyAvailable);
	}

	/**
	 * Get Available Quantity. Available Quantity (On Hand - Reserved)
	 */
	public BigDecimal getQtyAvailable() {
		BigDecimal bd = (BigDecimal) get_Value("QtyAvailable");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set On Hand Quantity. On Hand Quantity
	 */
	public void setQtyOnHand(BigDecimal QtyOnHand) {
		set_Value("QtyOnHand", QtyOnHand);
	}

	/**
	 * Get On Hand Quantity. On Hand Quantity
	 */
	public BigDecimal getQtyOnHand() {
		BigDecimal bd = (BigDecimal) get_Value("QtyOnHand");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Qty Requiered */
	public void setQtyRequiered(BigDecimal QtyRequiered) {
		set_Value("QtyRequiered", QtyRequiered);
	}

	/** Get Qty Requiered */
	public BigDecimal getQtyRequiered() {
		BigDecimal bd = (BigDecimal) get_Value("QtyRequiered");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Reserved Quantity. Reserved Quantity
	 */
	public void setQtyReserved(BigDecimal QtyReserved) {
		set_Value("QtyReserved", QtyReserved);
	}

	/**
	 * Get Reserved Quantity. Reserved Quantity
	 */
	public BigDecimal getQtyReserved() {
		BigDecimal bd = (BigDecimal) get_Value("QtyReserved");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
