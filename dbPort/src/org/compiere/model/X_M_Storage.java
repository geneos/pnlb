/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Storage
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.703
 */
public class X_M_Storage extends PO {
	/** Standard Constructor */
	public X_M_Storage(Properties ctx, int M_Storage_ID, String trxName) {
		super(ctx, M_Storage_ID, trxName);
		/**
		 * if (M_Storage_ID == 0) { setM_AttributeSetInstance_ID (0);
		 * setM_Locator_ID (0); setM_Product_ID (0); setQtyOnHand (Env.ZERO);
		 * setQtyOrdered (Env.ZERO); setQtyReserved (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_M_Storage(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Storage */
	public static final String Table_Name = "M_Storage";

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
		StringBuffer sb = new StringBuffer("X_M_Storage[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Date last inventory count. Date of Last Inventory Count
	 */
	public void setDateLastInventory(Timestamp DateLastInventory) {
		set_Value("DateLastInventory", DateLastInventory);
	}

	/**
	 * Get Date last inventory count. Date of Last Inventory Count
	 */
	public Timestamp getDateLastInventory() {
		return (Timestamp) get_Value("DateLastInventory");
	}

	/**
	 * Set Attribute Set Instance. Product Attribute Set Instance
	 */
	public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
		if (M_AttributeSetInstance_ID < 0)
			throw new IllegalArgumentException(
					"M_AttributeSetInstance_ID is mandatory.");
		set_ValueNoCheck("M_AttributeSetInstance_ID", new Integer(
				M_AttributeSetInstance_ID));
	}

	/**
	 * Get Attribute Set Instance. Product Attribute Set Instance
	 */
	public int getM_AttributeSetInstance_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSetInstance_ID");
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
		set_ValueNoCheck("M_Locator_ID", new Integer(M_Locator_ID));
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
	 * Set On Hand Quantity. On Hand Quantity
	 */
	public void setQtyOnHand(BigDecimal QtyOnHand) {
		if (QtyOnHand == null)
			throw new IllegalArgumentException("QtyOnHand is mandatory.");
		set_ValueNoCheck("QtyOnHand", QtyOnHand);
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

	/**
	 * Set Ordered Quantity. Ordered Quantity
	 */
	public void setQtyOrdered(BigDecimal QtyOrdered) {
		if (QtyOrdered == null)
			throw new IllegalArgumentException("QtyOrdered is mandatory.");
		set_ValueNoCheck("QtyOrdered", QtyOrdered);
	}

	/**
	 * Get Ordered Quantity. Ordered Quantity
	 */
	public BigDecimal getQtyOrdered() {
		BigDecimal bd = (BigDecimal) get_Value("QtyOrdered");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Reserved Quantity. Reserved Quantity
	 */
	public void setQtyReserved(BigDecimal QtyReserved) {
		if (QtyReserved == null)
			throw new IllegalArgumentException("QtyReserved is mandatory.");
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
