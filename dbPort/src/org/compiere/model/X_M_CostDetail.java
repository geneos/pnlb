/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_CostDetail
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.531
 */
public class X_M_CostDetail extends PO {
	/** Standard Constructor */
	public X_M_CostDetail(Properties ctx, int M_CostDetail_ID, String trxName) {
		super(ctx, M_CostDetail_ID, trxName);
		/**
		 * if (M_CostDetail_ID == 0) { setAmt (Env.ZERO); setC_AcctSchema_ID
		 * (0); setIsSOTrx (false); setM_AttributeSetInstance_ID (0);
		 * setM_CostDetail_ID (0); setM_Product_ID (0); setProcessed (false);
		 * setQty (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_M_CostDetail(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_CostDetail */
	public static final String Table_Name = "M_CostDetail";

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
		StringBuffer sb = new StringBuffer("X_M_CostDetail[").append(get_ID())
				.append("]");
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
	 * Set Invoice Line. Invoice Detail Line
	 */
	public void setC_InvoiceLine_ID(int C_InvoiceLine_ID) {
		if (C_InvoiceLine_ID <= 0)
			set_ValueNoCheck("C_InvoiceLine_ID", null);
		else
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

	/**
	 * Set Sales Order Line. Sales Order Line
	 */
	public void setC_OrderLine_ID(int C_OrderLine_ID) {
		if (C_OrderLine_ID <= 0)
			set_ValueNoCheck("C_OrderLine_ID", null);
		else
			set_ValueNoCheck("C_OrderLine_ID", new Integer(C_OrderLine_ID));
	}

	/**
	 * Get Sales Order Line. Sales Order Line
	 */
	public int getC_OrderLine_ID() {
		Integer ii = (Integer) get_Value("C_OrderLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Project Issue. Project Issues (Material, Labor)
	 */
	public void setC_ProjectIssue_ID(int C_ProjectIssue_ID) {
		if (C_ProjectIssue_ID <= 0)
			set_Value("C_ProjectIssue_ID", null);
		else
			set_Value("C_ProjectIssue_ID", new Integer(C_ProjectIssue_ID));
	}

	/**
	 * Get Project Issue. Project Issues (Material, Labor)
	 */
	public int getC_ProjectIssue_ID() {
		Integer ii = (Integer) get_Value("C_ProjectIssue_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Delta Amount. Difference Amount
	 */
	public void setDeltaAmt(BigDecimal DeltaAmt) {
		set_Value("DeltaAmt", DeltaAmt);
	}

	/**
	 * Get Delta Amount. Difference Amount
	 */
	public BigDecimal getDeltaAmt() {
		BigDecimal bd = (BigDecimal) get_Value("DeltaAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Delta Quantity. Quantity Difference
	 */
	public void setDeltaQty(BigDecimal DeltaQty) {
		set_Value("DeltaQty", DeltaQty);
	}

	/**
	 * Get Delta Quantity. Quantity Difference
	 */
	public BigDecimal getDeltaQty() {
		BigDecimal bd = (BigDecimal) get_Value("DeltaQty");
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
	 * Set Cost Detail. Cost Detail Information
	 */
	public void setM_CostDetail_ID(int M_CostDetail_ID) {
		if (M_CostDetail_ID < 1)
			throw new IllegalArgumentException("M_CostDetail_ID is mandatory.");
		set_ValueNoCheck("M_CostDetail_ID", new Integer(M_CostDetail_ID));
	}

	/**
	 * Get Cost Detail. Cost Detail Information
	 */
	public int getM_CostDetail_ID() {
		Integer ii = (Integer) get_Value("M_CostDetail_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Cost Element. Product Cost Element
	 */
	public void setM_CostElement_ID(int M_CostElement_ID) {
		if (M_CostElement_ID <= 0)
			set_ValueNoCheck("M_CostElement_ID", null);
		else
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
	 * Set Shipment/Receipt Line. Line on Shipment or Receipt document
	 */
	public void setM_InOutLine_ID(int M_InOutLine_ID) {
		if (M_InOutLine_ID <= 0)
			set_ValueNoCheck("M_InOutLine_ID", null);
		else
			set_ValueNoCheck("M_InOutLine_ID", new Integer(M_InOutLine_ID));
	}

	/**
	 * Get Shipment/Receipt Line. Line on Shipment or Receipt document
	 */
	public int getM_InOutLine_ID() {
		Integer ii = (Integer) get_Value("M_InOutLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Phys.Inventory Line. Unique line in an Inventory document
	 */
	public void setM_InventoryLine_ID(int M_InventoryLine_ID) {
		if (M_InventoryLine_ID <= 0)
			set_Value("M_InventoryLine_ID", null);
		else
			set_Value("M_InventoryLine_ID", new Integer(M_InventoryLine_ID));
	}

	/**
	 * Get Phys.Inventory Line. Unique line in an Inventory document
	 */
	public int getM_InventoryLine_ID() {
		Integer ii = (Integer) get_Value("M_InventoryLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Move Line. Inventory Move document Line
	 */
	public void setM_MovementLine_ID(int M_MovementLine_ID) {
		if (M_MovementLine_ID <= 0)
			set_Value("M_MovementLine_ID", null);
		else
			set_Value("M_MovementLine_ID", new Integer(M_MovementLine_ID));
	}

	/**
	 * Get Move Line. Inventory Move document Line
	 */
	public int getM_MovementLine_ID() {
		Integer ii = (Integer) get_Value("M_MovementLine_ID");
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
	 * Set Production Line. Document Line representing a production
	 */
	public void setM_ProductionLine_ID(int M_ProductionLine_ID) {
		if (M_ProductionLine_ID <= 0)
			set_Value("M_ProductionLine_ID", null);
		else
			set_Value("M_ProductionLine_ID", new Integer(M_ProductionLine_ID));
	}

	/**
	 * Get Production Line. Document Line representing a production
	 */
	public int getM_ProductionLine_ID() {
		Integer ii = (Integer) get_Value("M_ProductionLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Price. Price
	 */
	public void setPrice(BigDecimal Price) {
		throw new IllegalArgumentException("Price is virtual column");
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
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_Value("Processed", new Boolean(Processed));
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
