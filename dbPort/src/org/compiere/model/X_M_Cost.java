/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Cost
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.515
 */
public class X_M_Cost extends PO {
	/** Standard Constructor */
	public X_M_Cost(Properties ctx, int M_Cost_ID, String trxName) {
		super(ctx, M_Cost_ID, trxName);
		/**
		 * if (M_Cost_ID == 0) { setC_AcctSchema_ID (0); setCurrentCostPrice
		 * (Env.ZERO); setCurrentQty (Env.ZERO); setFutureCostPrice (Env.ZERO);
		 * setM_AttributeSetInstance_ID (0); setM_CostElement_ID (0);
		 * setM_CostType_ID (0); setM_Product_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_M_Cost(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Cost */
	public static final String Table_Name = "M_Cost";

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
		StringBuffer sb = new StringBuffer("X_M_Cost[").append(get_ID())
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

	/** CostingMethod AD_Reference_ID=122 */
	public static final int COSTINGMETHOD_AD_Reference_ID = 122;

	/** Average PO = A */
	public static final String COSTINGMETHOD_AveragePO = "A";

	/** Fifo = F */
	public static final String COSTINGMETHOD_Fifo = "F";

	/** Average Invoice = I */
	public static final String COSTINGMETHOD_AverageInvoice = "I";

	/** Lifo = L */
	public static final String COSTINGMETHOD_Lifo = "L";

	/** Standard Costing = S */
	public static final String COSTINGMETHOD_StandardCosting = "S";

	/** User Defined = U */
	public static final String COSTINGMETHOD_UserDefined = "U";

	/** Last Invoice = i */
	public static final String COSTINGMETHOD_LastInvoice = "i";

	/** Last PO Price = p */
	public static final String COSTINGMETHOD_LastPOPrice = "p";

	/** _ = x */
	public static final String COSTINGMETHOD__ = "x";

	/**
	 * Set Costing Method. Indicates how Costs will be calculated
	 */
	public void setCostingMethod(String CostingMethod) {
		throw new IllegalArgumentException("CostingMethod is virtual column");
	}

	/**
	 * Get Costing Method. Indicates how Costs will be calculated
	 */
	public String getCostingMethod() {
		return (String) get_Value("CostingMethod");
	}

	/**
	 * Set Accumulated Amt. Total Amount
	 */
	public void setCumulatedAmt(BigDecimal CumulatedAmt) {
		set_ValueNoCheck("CumulatedAmt", CumulatedAmt);
	}

	/**
	 * Get Accumulated Amt. Total Amount
	 */
	public BigDecimal getCumulatedAmt() {
		BigDecimal bd = (BigDecimal) get_Value("CumulatedAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Accumulated Qty. Total Quantity
	 */
	public void setCumulatedQty(BigDecimal CumulatedQty) {
		set_ValueNoCheck("CumulatedQty", CumulatedQty);
	}

	/**
	 * Get Accumulated Qty. Total Quantity
	 */
	public BigDecimal getCumulatedQty() {
		BigDecimal bd = (BigDecimal) get_Value("CumulatedQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
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

	/** Set Future Cost Price */
	public void setFutureCostPrice(BigDecimal FutureCostPrice) {
		if (FutureCostPrice == null)
			throw new IllegalArgumentException("FutureCostPrice is mandatory.");
		set_Value("FutureCostPrice", FutureCostPrice);
	}

	/** Get Future Cost Price */
	public BigDecimal getFutureCostPrice() {
		BigDecimal bd = (BigDecimal) get_Value("FutureCostPrice");
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

	/**
	 * Set Percent. Percentage
	 */
	public void setPercent(int Percent) {
		set_Value("Percent", new Integer(Percent));
	}

	/**
	 * Get Percent. Percentage
	 */
	public int getPercent() {
		Integer ii = (Integer) get_Value("Percent");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		throw new IllegalArgumentException("Processed is virtual column");
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
}
