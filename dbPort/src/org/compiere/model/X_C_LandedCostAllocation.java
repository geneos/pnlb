/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_LandedCostAllocation
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.671
 */
public class X_C_LandedCostAllocation extends PO {
	/** Standard Constructor */
	public X_C_LandedCostAllocation(Properties ctx,
			int C_LandedCostAllocation_ID, String trxName) {
		super(ctx, C_LandedCostAllocation_ID, trxName);
		/**
		 * if (C_LandedCostAllocation_ID == 0) { setAmt (Env.ZERO); setBase
		 * (Env.ZERO); setC_InvoiceLine_ID (0); setC_LandedCostAllocation_ID
		 * (0); setM_CostElement_ID (0); setM_Product_ID (0); setQty (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_LandedCostAllocation(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_LandedCostAllocation */
	public static final String Table_Name = "C_LandedCostAllocation";

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
		StringBuffer sb = new StringBuffer("X_C_LandedCostAllocation[").append(
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
	 * Set Base. Calculation Base
	 */
	public void setBase(BigDecimal Base) {
		if (Base == null)
			throw new IllegalArgumentException("Base is mandatory.");
		set_Value("Base", Base);
	}

	/**
	 * Get Base. Calculation Base
	 */
	public BigDecimal getBase() {
		BigDecimal bd = (BigDecimal) get_Value("Base");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Invoice Line. Invoice Detail Line
	 */
	public void setC_InvoiceLine_ID(int C_InvoiceLine_ID) {
		if (C_InvoiceLine_ID < 1)
			throw new IllegalArgumentException("C_InvoiceLine_ID is mandatory.");
		set_ValueNoCheck("C_InvoiceLine_ID", new Integer(C_InvoiceLine_ID));
	}

	/**
	 * Get Invoice Line. Invoice Detail Line
	 */
	public int getC_InvoiceLine_ID() {
		Integer ii = (Integer) get_Value("C_InvoiceLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_InvoiceLine_ID()));
	}

	/**
	 * Set Landed Cost Allocation. Allocation for Land Costs
	 */
	public void setC_LandedCostAllocation_ID(int C_LandedCostAllocation_ID) {
		if (C_LandedCostAllocation_ID < 1)
			throw new IllegalArgumentException(
					"C_LandedCostAllocation_ID is mandatory.");
		set_ValueNoCheck("C_LandedCostAllocation_ID", new Integer(
				C_LandedCostAllocation_ID));
	}

	/**
	 * Get Landed Cost Allocation. Allocation for Land Costs
	 */
	public int getC_LandedCostAllocation_ID() {
		Integer ii = (Integer) get_Value("C_LandedCostAllocation_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Attribute Set Instance. Product Attribute Set Instance
	 */
	public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
		if (M_AttributeSetInstance_ID <= 0)
			set_ValueNoCheck("M_AttributeSetInstance_ID", null);
		else
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
		set_Value("M_CostElement_ID", new Integer(M_CostElement_ID));
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
}
