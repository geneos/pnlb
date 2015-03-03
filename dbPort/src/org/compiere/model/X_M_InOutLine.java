/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_InOutLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.89
 */
public class X_M_InOutLine extends PO {
	/** Standard Constructor */
	public X_M_InOutLine(Properties ctx, int M_InOutLine_ID, String trxName) {
		super(ctx, M_InOutLine_ID, trxName);
		/**
		 * if (M_InOutLine_ID == 0) { setC_UOM_ID (0); //
		 * 
		 * @#C_UOM_ID@ setIsDescription (false); // N setIsInvoiced (false);
		 *             setLine (0); //
		 * @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_InOutLine
		 *             WHERE M_InOut_ID=@M_InOut_ID@
		 *             setM_AttributeSetInstance_ID (0); setM_InOutLine_ID (0);
		 *             setM_InOut_ID (0); setM_Locator_ID (0); //
		 * @M_Locator_ID@ setM_Product_ID (0); setMovementQty (Env.ZERO); // 1
		 *                setProcessed (false); setQtyEntered (Env.ZERO); // 1 }
		 */
	}

	/** Load Constructor */
	public X_M_InOutLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_InOutLine */
	public static final String Table_Name = "M_InOutLine";

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
		StringBuffer sb = new StringBuffer("X_M_InOutLine[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Charge. Additional document charges
	 */
	public void setC_Charge_ID(int C_Charge_ID) {
		if (C_Charge_ID <= 0)
			set_Value("C_Charge_ID", null);
		else
			set_Value("C_Charge_ID", new Integer(C_Charge_ID));
	}

	/**
	 * Get Charge. Additional document charges
	 */
	public int getC_Charge_ID() {
		Integer ii = (Integer) get_Value("C_Charge_ID");
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
	 * Set Project. Financial Project
	 */
	public void setC_Project_ID(int C_Project_ID) {
		if (C_Project_ID <= 0)
			set_Value("C_Project_ID", null);
		else
			set_Value("C_Project_ID", new Integer(C_Project_ID));
	}

	/**
	 * Get Project. Financial Project
	 */
	public int getC_Project_ID() {
		Integer ii = (Integer) get_Value("C_Project_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set UOM. Unit of Measure
	 */
	public void setC_UOM_ID(int C_UOM_ID) {
		if (C_UOM_ID < 1)
			throw new IllegalArgumentException("C_UOM_ID is mandatory.");
		set_ValueNoCheck("C_UOM_ID", new Integer(C_UOM_ID));
	}

	/**
	 * Get UOM. Unit of Measure
	 */
	public int getC_UOM_ID() {
		Integer ii = (Integer) get_Value("C_UOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Confirmed Quantity. Confirmation of a received quantity
	 */
	public void setConfirmedQty(BigDecimal ConfirmedQty) {
		set_Value("ConfirmedQty", ConfirmedQty);
	}

	/**
	 * Get Confirmed Quantity. Confirmation of a received quantity
	 */
	public BigDecimal getConfirmedQty() {
		BigDecimal bd = (BigDecimal) get_Value("ConfirmedQty");
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
	 * Set Description Only. if true, the line is just description and no
	 * transaction
	 */
	public void setIsDescription(boolean IsDescription) {
		set_Value("IsDescription", new Boolean(IsDescription));
	}

	/**
	 * Get Description Only. if true, the line is just description and no
	 * transaction
	 */
	public boolean isDescription() {
		Object oo = get_Value("IsDescription");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Invoiced. Is this invoiced?
	 */
	public void setIsInvoiced(boolean IsInvoiced) {
		set_Value("IsInvoiced", new Boolean(IsInvoiced));
	}

	/**
	 * Get Invoiced. Is this invoiced?
	 */
	public boolean isInvoiced() {
		Object oo = get_Value("IsInvoiced");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
		if (M_InOutLine_ID < 1)
			throw new IllegalArgumentException("M_InOutLine_ID is mandatory.");
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
	 * Set Shipment/Receipt. Material Shipment Document
	 */
	public void setM_InOut_ID(int M_InOut_ID) {
		if (M_InOut_ID < 1)
			throw new IllegalArgumentException("M_InOut_ID is mandatory.");
		set_ValueNoCheck("M_InOut_ID", new Integer(M_InOut_ID));
	}

	/**
	 * Get Shipment/Receipt. Material Shipment Document
	 */
	public int getM_InOut_ID() {
		Integer ii = (Integer) get_Value("M_InOut_ID");
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
	 * Set Movement Quantity. Quantity of a product moved.
	 */
	public void setMovementQty(BigDecimal MovementQty) {
		if (MovementQty == null)
			throw new IllegalArgumentException("MovementQty is mandatory.");
		set_Value("MovementQty", MovementQty);
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

	/** Set Picked Quantity */
	public void setPickedQty(BigDecimal PickedQty) {
		set_Value("PickedQty", PickedQty);
	}

	/** Get Picked Quantity */
	public BigDecimal getPickedQty() {
		BigDecimal bd = (BigDecimal) get_Value("PickedQty");
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
	 * Set Quantity. The Quantity Entered is based on the selected UoM
	 */
	public void setQtyEntered(BigDecimal QtyEntered) {
		if (QtyEntered == null)
			throw new IllegalArgumentException("QtyEntered is mandatory.");
		set_Value("QtyEntered", QtyEntered);
	}

	/**
	 * Get Quantity. The Quantity Entered is based on the selected UoM
	 */
	public BigDecimal getQtyEntered() {
		BigDecimal bd = (BigDecimal) get_Value("QtyEntered");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Referenced Shipment Line */
	public void setRef_InOutLine_ID(int Ref_InOutLine_ID) {
		if (Ref_InOutLine_ID <= 0)
			set_Value("Ref_InOutLine_ID", null);
		else
			set_Value("Ref_InOutLine_ID", new Integer(Ref_InOutLine_ID));
	}

	/** Get Referenced Shipment Line */
	public int getRef_InOutLine_ID() {
		Integer ii = (Integer) get_Value("Ref_InOutLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Scrapped Quantity. The Quantity scrapped due to QA issues
	 */
	public void setScrappedQty(BigDecimal ScrappedQty) {
		set_Value("ScrappedQty", ScrappedQty);
	}

	/**
	 * Get Scrapped Quantity. The Quantity scrapped due to QA issues
	 */
	public BigDecimal getScrappedQty() {
		BigDecimal bd = (BigDecimal) get_Value("ScrappedQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Target Quantity. Target Movement Quantity
	 */
	public void setTargetQty(BigDecimal TargetQty) {
		set_Value("TargetQty", TargetQty);
	}

	/**
	 * Get Target Quantity. Target Movement Quantity
	 */
	public BigDecimal getTargetQty() {
		BigDecimal bd = (BigDecimal) get_Value("TargetQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
