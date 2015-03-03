/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Transaction
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.718
 */
public class X_M_Transaction extends PO {
	/** Standard Constructor */
	public X_M_Transaction(Properties ctx, int M_Transaction_ID, String trxName) {
		super(ctx, M_Transaction_ID, trxName);
		/**
		 * if (M_Transaction_ID == 0) { setM_AttributeSetInstance_ID (0);
		 * setM_Locator_ID (0); setM_Product_ID (0); setM_Transaction_ID (0);
		 * setMovementDate (new Timestamp(System.currentTimeMillis()));
		 * setMovementQty (Env.ZERO); setMovementType (null); }
		 */
	}

	/** Load Constructor */
	public X_M_Transaction(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Transaction */
	public static final String Table_Name = "M_Transaction";

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
		StringBuffer sb = new StringBuffer("X_M_Transaction[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Project Issue. Project Issues (Material, Labor)
	 */
	public void setC_ProjectIssue_ID(int C_ProjectIssue_ID) {
		if (C_ProjectIssue_ID <= 0)
			set_ValueNoCheck("C_ProjectIssue_ID", null);
		else
			set_ValueNoCheck("C_ProjectIssue_ID",
					new Integer(C_ProjectIssue_ID));
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

	/** Set Order BOM Line ID */
	public void setMPC_Order_BOMLine_ID(int MPC_Order_BOMLine_ID) {
		if (MPC_Order_BOMLine_ID <= 0)
			set_Value("MPC_Order_BOMLine_ID", null);
		else
			set_Value("MPC_Order_BOMLine_ID", new Integer(MPC_Order_BOMLine_ID));
	}

	/** Get Order BOM Line ID */
	public int getMPC_Order_BOMLine_ID() {
		Integer ii = (Integer) get_Value("MPC_Order_BOMLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** MPC_Order_ID AD_Reference_ID=1000045 */
	public static final int MPC_ORDER_ID_AD_Reference_ID = 1000045;

	/**
	 * Set Manufacturing Order. Manufacturing Order
	 */
	public void setMPC_Order_ID(int MPC_Order_ID) {
		if (MPC_Order_ID <= 0)
			set_Value("MPC_Order_ID", null);
		else
			set_Value("MPC_Order_ID", new Integer(MPC_Order_ID));
	}

	/**
	 * Get Manufacturing Order. Manufacturing Order
	 */
	public int getMPC_Order_ID() {
		Integer ii = (Integer) get_Value("MPC_Order_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
			set_ValueNoCheck("M_InventoryLine_ID", null);
		else
			set_ValueNoCheck("M_InventoryLine_ID", new Integer(
					M_InventoryLine_ID));
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
	 * Set Move Line. Inventory Move document Line
	 */
	public void setM_MovementLine_ID(int M_MovementLine_ID) {
		if (M_MovementLine_ID <= 0)
			set_ValueNoCheck("M_MovementLine_ID", null);
		else
			set_ValueNoCheck("M_MovementLine_ID",
					new Integer(M_MovementLine_ID));
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
			set_ValueNoCheck("M_ProductionLine_ID", null);
		else
			set_ValueNoCheck("M_ProductionLine_ID", new Integer(
					M_ProductionLine_ID));
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

	/** Set Inventory Transaction */
	public void setM_Transaction_ID(int M_Transaction_ID) {
		if (M_Transaction_ID < 1)
			throw new IllegalArgumentException("M_Transaction_ID is mandatory.");
		set_ValueNoCheck("M_Transaction_ID", new Integer(M_Transaction_ID));
	}

	/** Get Inventory Transaction */
	public int getM_Transaction_ID() {
		Integer ii = (Integer) get_Value("M_Transaction_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Movement Date. Date a product was moved in or out of inventory
	 */
	public void setMovementDate(Timestamp MovementDate) {
		if (MovementDate == null)
			throw new IllegalArgumentException("MovementDate is mandatory.");
		set_ValueNoCheck("MovementDate", MovementDate);
	}

	/**
	 * Get Movement Date. Date a product was moved in or out of inventory
	 */
	public Timestamp getMovementDate() {
		return (Timestamp) get_Value("MovementDate");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getMovementDate()));
	}

	/**
	 * Set Movement Quantity. Quantity of a product moved.
	 */
	public void setMovementQty(BigDecimal MovementQty) {
		if (MovementQty == null)
			throw new IllegalArgumentException("MovementQty is mandatory.");
		set_ValueNoCheck("MovementQty", MovementQty);
	}

	/**
	 * Get Movement Quantity. Quantity of a product moved.
	 */
	public BigDecimal getMovementQty() {
		BigDecimal bd = (BigDecimal) get_Value("MovementQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** MovementType AD_Reference_ID=189 */
	public static final int MOVEMENTTYPE_AD_Reference_ID = 189;

	/** Customer Returns = C+ */
	public static final String MOVEMENTTYPE_CustomerReturns = "C+";

	/** Customer Shipment = C- */
	public static final String MOVEMENTTYPE_CustomerShipment = "C-";

	/** Inventory In = I+ */
	public static final String MOVEMENTTYPE_InventoryIn = "I+";

	/** Inventory Out = I- */
	public static final String MOVEMENTTYPE_InventoryOut = "I-";

	/** Movement To = M+ */
	public static final String MOVEMENTTYPE_MovementTo = "M+";

	/** Movement From = M- */
	public static final String MOVEMENTTYPE_MovementFrom = "M-";

	/** Production + = P+ */
	public static final String MOVEMENTTYPE_ProductionPlus = "P+";

	/** Production - = P- */
	public static final String MOVEMENTTYPE_Production_ = "P-";

	/** Vendor Receipts = V+ */
	public static final String MOVEMENTTYPE_VendorReceipts = "V+";

	/** Vendor Returns = V- */
	public static final String MOVEMENTTYPE_VendorReturns = "V-";

	/** Work Order + = W+ */
	public static final String MOVEMENTTYPE_WorkOrderPlus = "W+";

	/** Work Order - = W- */
	public static final String MOVEMENTTYPE_WorkOrder_ = "W-";

	/**
	 * Set Movement Type. Method of moving the inventory
	 */
	public void setMovementType(String MovementType) {
		if (MovementType == null)
			throw new IllegalArgumentException("MovementType is mandatory");
		if (MovementType.equals("C+") || MovementType.equals("C-")
				|| MovementType.equals("I+") || MovementType.equals("I-")
				|| MovementType.equals("M+") || MovementType.equals("M-")
				|| MovementType.equals("P+") || MovementType.equals("P-")
				|| MovementType.equals("V+") || MovementType.equals("V-")
				|| MovementType.equals("W+") || MovementType.equals("W-"))
			;
		else
			throw new IllegalArgumentException(
					"MovementType Invalid value - "
							+ MovementType
							+ " - Reference_ID=189 - C+ - C- - I+ - I- - M+ - M- - P+ - P- - V+ - V- - W+ - W-");
		if (MovementType.length() > 2) {
			log.warning("Length > 2 - truncated");
			MovementType = MovementType.substring(0, 1);
		}
		set_ValueNoCheck("MovementType", MovementType);
	}

	/**
	 * Get Movement Type. Method of moving the inventory
	 */
	public String getMovementType() {
		return (String) get_Value("MovementType");
	}
}
