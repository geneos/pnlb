/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_CostQueue
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.562
 */
public class X_M_CostQueue extends PO {
	/** Standard Constructor */
	public X_M_CostQueue(Properties ctx, int M_CostQueue_ID, String trxName) {
		super(ctx, M_CostQueue_ID, trxName);
		/**
		 * if (M_CostQueue_ID == 0) { setC_AcctSchema_ID (0);
		 * setCurrentCostPrice (Env.ZERO); setCurrentQty (Env.ZERO);
		 * setM_AttributeSetInstance_ID (0); setM_CostElement_ID (0);
		 * setM_CostQueue_ID (0); setM_CostType_ID (0); setM_Product_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_M_CostQueue(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_CostQueue */
	public static final String Table_Name = "M_CostQueue";

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
		StringBuffer sb = new StringBuffer("X_M_CostQueue[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Accounting Schema. Rules for accounting
	 */
	public void setC_AcctSchema_ID(int C_AcctSchema_ID) {
		if (C_AcctSchema_ID < 1)
			throw new IllegalArgumentException("C_AcctSchema_ID is mandatory.");
		set_ValueNoCheck("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
	}

	/**
	 * Get Accounting Schema. Rules for accounting
	 */
	public int getC_AcctSchema_ID() {
		Integer ii = (Integer) get_Value("C_AcctSchema_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Current Cost Price. The currently used cost price
	 */
	public void setCurrentCostPrice(BigDecimal CurrentCostPrice) {
		if (CurrentCostPrice == null)
			throw new IllegalArgumentException("CurrentCostPrice is mandatory.");
		set_Value("CurrentCostPrice", CurrentCostPrice);
	}

	/**
	 * Get Current Cost Price. The currently used cost price
	 */
	public BigDecimal getCurrentCostPrice() {
		BigDecimal bd = (BigDecimal) get_Value("CurrentCostPrice");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Current Quantity. Current Quantity
	 */
	public void setCurrentQty(BigDecimal CurrentQty) {
		if (CurrentQty == null)
			throw new IllegalArgumentException("CurrentQty is mandatory.");
		set_Value("CurrentQty", CurrentQty);
	}

	/**
	 * Get Current Quantity. Current Quantity
	 */
	public BigDecimal getCurrentQty() {
		BigDecimal bd = (BigDecimal) get_Value("CurrentQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
	 * Set Cost Element. Product Cost Element
	 */
	public void setM_CostElement_ID(int M_CostElement_ID) {
		if (M_CostElement_ID < 1)
			throw new IllegalArgumentException("M_CostElement_ID is mandatory.");
		set_ValueNoCheck("M_CostElement_ID", new Integer(M_CostElement_ID));
	}

	/**
	 * Get Cost Element. Product Cost Element
	 */
	public int getM_CostElement_ID() {
		Integer ii = (Integer) get_Value("M_CostElement_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Cost Queue. FiFo/LiFo Cost Queue
	 */
	public void setM_CostQueue_ID(int M_CostQueue_ID) {
		if (M_CostQueue_ID < 1)
			throw new IllegalArgumentException("M_CostQueue_ID is mandatory.");
		set_ValueNoCheck("M_CostQueue_ID", new Integer(M_CostQueue_ID));
	}

	/**
	 * Get Cost Queue. FiFo/LiFo Cost Queue
	 */
	public int getM_CostQueue_ID() {
		Integer ii = (Integer) get_Value("M_CostQueue_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Cost Type. Type of Cost (e.g. Current, Plan, Future)
	 */
	public void setM_CostType_ID(int M_CostType_ID) {
		if (M_CostType_ID < 1)
			throw new IllegalArgumentException("M_CostType_ID is mandatory.");
		set_ValueNoCheck("M_CostType_ID", new Integer(M_CostType_ID));
	}

	/**
	 * Get Cost Type. Type of Cost (e.g. Current, Plan, Future)
	 */
	public int getM_CostType_ID() {
		Integer ii = (Integer) get_Value("M_CostType_ID");
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
}
