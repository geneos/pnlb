/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_TransactionAllocation
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.734
 */
public class X_M_TransactionAllocation extends PO {
	/** Standard Constructor */
	public X_M_TransactionAllocation(Properties ctx,
			int M_TransactionAllocation_ID, String trxName) {
		super(ctx, M_TransactionAllocation_ID, trxName);
		/**
		 * if (M_TransactionAllocation_ID == 0) { setAllocationStrategyType
		 * (null); setIsAllocated (false); // N setIsManual (false); // N
		 * setM_AttributeSetInstance_ID (0); setM_Product_ID (0);
		 * setM_Transaction_ID (0); setQty (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_M_TransactionAllocation(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_TransactionAllocation */
	public static final String Table_Name = "M_TransactionAllocation";

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
		StringBuffer sb = new StringBuffer("X_M_TransactionAllocation[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/** AllocationStrategyType AD_Reference_ID=294 */
	public static final int ALLOCATIONSTRATEGYTYPE_AD_Reference_ID = 294;

	/** FiFo = F */
	public static final String ALLOCATIONSTRATEGYTYPE_FiFo = "F";

	/** LiFo = L */
	public static final String ALLOCATIONSTRATEGYTYPE_LiFo = "L";

	/**
	 * Set Allocation Strategy. Allocation Strategy
	 */
	public void setAllocationStrategyType(String AllocationStrategyType) {
		if (AllocationStrategyType == null)
			throw new IllegalArgumentException(
					"AllocationStrategyType is mandatory");
		if (AllocationStrategyType.equals("F")
				|| AllocationStrategyType.equals("L"))
			;
		else
			throw new IllegalArgumentException(
					"AllocationStrategyType Invalid value - "
							+ AllocationStrategyType
							+ " - Reference_ID=294 - F - L");
		if (AllocationStrategyType.length() > 1) {
			log.warning("Length > 1 - truncated");
			AllocationStrategyType = AllocationStrategyType.substring(0, 0);
		}
		set_Value("AllocationStrategyType", AllocationStrategyType);
	}

	/**
	 * Get Allocation Strategy. Allocation Strategy
	 */
	public String getAllocationStrategyType() {
		return (String) get_Value("AllocationStrategyType");
	}

	/**
	 * Set Allocated. Indicates if the payment has been allocated
	 */
	public void setIsAllocated(boolean IsAllocated) {
		set_Value("IsAllocated", new Boolean(IsAllocated));
	}

	/**
	 * Get Allocated. Indicates if the payment has been allocated
	 */
	public boolean isAllocated() {
		Object oo = get_Value("IsAllocated");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Manual. This is a manual process
	 */
	public void setIsManual(boolean IsManual) {
		set_Value("IsManual", new Boolean(IsManual));
	}

	/**
	 * Get Manual. This is a manual process
	 */
	public boolean isManual() {
		Object oo = get_Value("IsManual");
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
		set_Value("M_AttributeSetInstance_ID", new Integer(
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
			set_Value("M_InOutLine_ID", null);
		else
			set_Value("M_InOutLine_ID", new Integer(M_InOutLine_ID));
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

	/** Out_M_InOutLine_ID AD_Reference_ID=295 */
	public static final int OUT_M_INOUTLINE_ID_AD_Reference_ID = 295;

	/**
	 * Set Out Shipment Line. Outgoing Shipment/Receipt
	 */
	public void setOut_M_InOutLine_ID(int Out_M_InOutLine_ID) {
		if (Out_M_InOutLine_ID <= 0)
			set_Value("Out_M_InOutLine_ID", null);
		else
			set_Value("Out_M_InOutLine_ID", new Integer(Out_M_InOutLine_ID));
	}

	/**
	 * Get Out Shipment Line. Outgoing Shipment/Receipt
	 */
	public int getOut_M_InOutLine_ID() {
		Integer ii = (Integer) get_Value("Out_M_InOutLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Out_M_InventoryLine_ID AD_Reference_ID=296 */
	public static final int OUT_M_INVENTORYLINE_ID_AD_Reference_ID = 296;

	/**
	 * Set Out Inventory Line. Outgoing Inventory Line
	 */
	public void setOut_M_InventoryLine_ID(int Out_M_InventoryLine_ID) {
		if (Out_M_InventoryLine_ID <= 0)
			set_Value("Out_M_InventoryLine_ID", null);
		else
			set_Value("Out_M_InventoryLine_ID", new Integer(
					Out_M_InventoryLine_ID));
	}

	/**
	 * Get Out Inventory Line. Outgoing Inventory Line
	 */
	public int getOut_M_InventoryLine_ID() {
		Integer ii = (Integer) get_Value("Out_M_InventoryLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Out_M_ProductionLine_ID AD_Reference_ID=297 */
	public static final int OUT_M_PRODUCTIONLINE_ID_AD_Reference_ID = 297;

	/**
	 * Set Out Production Line. Outgoing Production Line
	 */
	public void setOut_M_ProductionLine_ID(int Out_M_ProductionLine_ID) {
		if (Out_M_ProductionLine_ID <= 0)
			set_Value("Out_M_ProductionLine_ID", null);
		else
			set_Value("Out_M_ProductionLine_ID", new Integer(
					Out_M_ProductionLine_ID));
	}

	/**
	 * Get Out Production Line. Outgoing Production Line
	 */
	public int getOut_M_ProductionLine_ID() {
		Integer ii = (Integer) get_Value("Out_M_ProductionLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Out_M_Transaction_ID AD_Reference_ID=298 */
	public static final int OUT_M_TRANSACTION_ID_AD_Reference_ID = 298;

	/**
	 * Set Out Transaction. Outgoing Transaction
	 */
	public void setOut_M_Transaction_ID(int Out_M_Transaction_ID) {
		if (Out_M_Transaction_ID <= 0)
			set_Value("Out_M_Transaction_ID", null);
		else
			set_Value("Out_M_Transaction_ID", new Integer(Out_M_Transaction_ID));
	}

	/**
	 * Get Out Transaction. Outgoing Transaction
	 */
	public int getOut_M_Transaction_ID() {
		Integer ii = (Integer) get_Value("Out_M_Transaction_ID");
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
