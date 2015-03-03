/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_Replenish
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:05.203
 */
public class X_T_Replenish extends PO {
	/** Standard Constructor */
	public X_T_Replenish(Properties ctx, int T_Replenish_ID, String trxName) {
		super(ctx, T_Replenish_ID, trxName);
		/**
		 * if (T_Replenish_ID == 0) { setAD_PInstance_ID (0); setC_BPartner_ID
		 * (0); setLevel_Max (Env.ZERO); setLevel_Min (Env.ZERO);
		 * setM_Product_ID (0); setM_Warehouse_ID (0); setReplenishType (null); }
		 */
	}

	/** Load Constructor */
	public X_T_Replenish(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_Replenish */
	public static final String Table_Name = "T_Replenish";

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
		StringBuffer sb = new StringBuffer("X_T_Replenish[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Process Instance. Instance of the process
	 */
	public void setAD_PInstance_ID(int AD_PInstance_ID) {
		if (AD_PInstance_ID < 1)
			throw new IllegalArgumentException("AD_PInstance_ID is mandatory.");
		set_ValueNoCheck("AD_PInstance_ID", new Integer(AD_PInstance_ID));
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
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID < 1)
			throw new IllegalArgumentException("C_BPartner_ID is mandatory.");
		set_Value("C_BPartner_ID", new Integer(C_BPartner_ID));
	}

	/**
	 * Get Business Partner . Identifies a Business Partner
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Document Type. Document type or rules
	 */
	public void setC_DocType_ID(int C_DocType_ID) {
		if (C_DocType_ID <= 0)
			set_Value("C_DocType_ID", null);
		else
			set_Value("C_DocType_ID", new Integer(C_DocType_ID));
	}

	/**
	 * Get Document Type. Document type or rules
	 */
	public int getC_DocType_ID() {
		Integer ii = (Integer) get_Value("C_DocType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Maximum Level. Maximum Inventory level for this product
	 */
	public void setLevel_Max(BigDecimal Level_Max) {
		if (Level_Max == null)
			throw new IllegalArgumentException("Level_Max is mandatory.");
		set_Value("Level_Max", Level_Max);
	}

	/**
	 * Get Maximum Level. Maximum Inventory level for this product
	 */
	public BigDecimal getLevel_Max() {
		BigDecimal bd = (BigDecimal) get_Value("Level_Max");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Minimum Level. Minimum Inventory level for this product
	 */
	public void setLevel_Min(BigDecimal Level_Min) {
		if (Level_Min == null)
			throw new IllegalArgumentException("Level_Min is mandatory.");
		set_Value("Level_Min", Level_Min);
	}

	/**
	 * Get Minimum Level. Minimum Inventory level for this product
	 */
	public BigDecimal getLevel_Min() {
		BigDecimal bd = (BigDecimal) get_Value("Level_Min");
		if (bd == null)
			return Env.ZERO;
		return bd;
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

	/** M_WarehouseSource_ID AD_Reference_ID=197 */
	public static final int M_WAREHOUSESOURCE_ID_AD_Reference_ID = 197;

	/**
	 * Set Source Warehouse. Optional Warehouse to replenish from
	 */
	public void setM_WarehouseSource_ID(int M_WarehouseSource_ID) {
		if (M_WarehouseSource_ID <= 0)
			set_Value("M_WarehouseSource_ID", null);
		else
			set_Value("M_WarehouseSource_ID", new Integer(M_WarehouseSource_ID));
	}

	/**
	 * Get Source Warehouse. Optional Warehouse to replenish from
	 */
	public int getM_WarehouseSource_ID() {
		Integer ii = (Integer) get_Value("M_WarehouseSource_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Warehouse. Storage Warehouse and Service Point
	 */
	public void setM_Warehouse_ID(int M_Warehouse_ID) {
		if (M_Warehouse_ID < 1)
			throw new IllegalArgumentException("M_Warehouse_ID is mandatory.");
		set_ValueNoCheck("M_Warehouse_ID", new Integer(M_Warehouse_ID));
	}

	/**
	 * Get Warehouse. Storage Warehouse and Service Point
	 */
	public int getM_Warehouse_ID() {
		Integer ii = (Integer) get_Value("M_Warehouse_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Minimum Order Qty. Minimum order quantity in UOM
	 */
	public void setOrder_Min(BigDecimal Order_Min) {
		set_Value("Order_Min", Order_Min);
	}

	/**
	 * Get Minimum Order Qty. Minimum order quantity in UOM
	 */
	public BigDecimal getOrder_Min() {
		BigDecimal bd = (BigDecimal) get_Value("Order_Min");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Order Pack Qty. Package order size in UOM (e.g. order set of 5 units)
	 */
	public void setOrder_Pack(BigDecimal Order_Pack) {
		set_Value("Order_Pack", Order_Pack);
	}

	/**
	 * Get Order Pack Qty. Package order size in UOM (e.g. order set of 5 units)
	 */
	public BigDecimal getOrder_Pack() {
		BigDecimal bd = (BigDecimal) get_Value("Order_Pack");
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

	/**
	 * Set Ordered Quantity. Ordered Quantity
	 */
	public void setQtyOrdered(BigDecimal QtyOrdered) {
		set_Value("QtyOrdered", QtyOrdered);
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

	/** Set Quantity to Order */
	public void setQtyToOrder(BigDecimal QtyToOrder) {
		set_Value("QtyToOrder", QtyToOrder);
	}

	/** Get Quantity to Order */
	public BigDecimal getQtyToOrder() {
		BigDecimal bd = (BigDecimal) get_Value("QtyToOrder");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** ReplenishType AD_Reference_ID=164 */
	public static final int REPLENISHTYPE_AD_Reference_ID = 164;

	/** Manual = 0 */
	public static final String REPLENISHTYPE_Manual = "0";

	/** Reorder below Minimum Level = 1 */
	public static final String REPLENISHTYPE_ReorderBelowMinimumLevel = "1";

	/** Maintain Maximum Level = 2 */
	public static final String REPLENISHTYPE_MaintainMaximumLevel = "2";

	/** Custom = 9 */
	public static final String REPLENISHTYPE_Custom = "9";

	/**
	 * Set Replenish Type. Method for re-ordering a product
	 */
	public void setReplenishType(String ReplenishType) {
		if (ReplenishType == null)
			throw new IllegalArgumentException("ReplenishType is mandatory");
		if (ReplenishType.equals("0") || ReplenishType.equals("1")
				|| ReplenishType.equals("2") || ReplenishType.equals("9"))
			;
		else
			throw new IllegalArgumentException("ReplenishType Invalid value - "
					+ ReplenishType + " - Reference_ID=164 - 0 - 1 - 2 - 9");
		if (ReplenishType.length() > 1) {
			log.warning("Length > 1 - truncated");
			ReplenishType = ReplenishType.substring(0, 0);
		}
		set_Value("ReplenishType", ReplenishType);
	}

	/**
	 * Get Replenish Type. Method for re-ordering a product
	 */
	public String getReplenishType() {
		return (String) get_Value("ReplenishType");
	}

	/** ReplenishmentCreate AD_Reference_ID=329 */
	public static final int REPLENISHMENTCREATE_AD_Reference_ID = 329;

	/** Inventory Move = MMM */
	public static final String REPLENISHMENTCREATE_InventoryMove = "MMM";

	/** Purchase Order = POO */
	public static final String REPLENISHMENTCREATE_PurchaseOrder = "POO";

	/** Requisition = POR */
	public static final String REPLENISHMENTCREATE_Requisition = "POR";

	/**
	 * Set Create. Create from Replenishment
	 */
	public void setReplenishmentCreate(String ReplenishmentCreate) {
		if (ReplenishmentCreate != null && ReplenishmentCreate.length() > 1) {
			log.warning("Length > 1 - truncated");
			ReplenishmentCreate = ReplenishmentCreate.substring(0, 0);
		}
		set_Value("ReplenishmentCreate", ReplenishmentCreate);
	}

	/**
	 * Get Create. Create from Replenishment
	 */
	public String getReplenishmentCreate() {
		return (String) get_Value("ReplenishmentCreate");
	}
}
