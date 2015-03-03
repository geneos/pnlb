/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_MovementLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.171
 */
public class X_M_MovementLine extends PO {
	/** Standard Constructor */
	public X_M_MovementLine(Properties ctx, int M_MovementLine_ID,
			String trxName) {
		super(ctx, M_MovementLine_ID, trxName);
		/**
		 * if (M_MovementLine_ID == 0) { setLine (0); //
		 * 
		 * @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_MovementLine
		 *             WHERE M_Movement_ID=@M_Movement_ID@
		 *             setM_AttributeSetInstance_ID (0); setM_LocatorTo_ID (0); //
		 * @M_LocatorTo_ID@ setM_Locator_ID (0); //
		 * @M_Locator_ID@ setM_MovementLine_ID (0); setM_Movement_ID (0);
		 *                setM_Product_ID (0); setMovementQty (Env.ZERO); // 0
		 *                setProcessed (false); setTargetQty (Env.ZERO); // 0 }
		 */
	}

	/** Load Constructor */
	public X_M_MovementLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_MovementLine */
	public static final String Table_Name = "M_MovementLine";

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
		StringBuffer sb = new StringBuffer("X_M_MovementLine[")
				.append(get_ID()).append("]");
		return sb.toString();
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
	 * Set Attribute Set Instance To. Target Product Attribute Set Instance
	 */
	public void setM_AttributeSetInstanceTo_ID(int M_AttributeSetInstanceTo_ID) {
		if (M_AttributeSetInstanceTo_ID <= 0)
			set_ValueNoCheck("M_AttributeSetInstanceTo_ID", null);
		else
			set_ValueNoCheck("M_AttributeSetInstanceTo_ID", new Integer(
					M_AttributeSetInstanceTo_ID));
	}

	/**
	 * Get Attribute Set Instance To. Target Product Attribute Set Instance
	 */
	public int getM_AttributeSetInstanceTo_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSetInstanceTo_ID");
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

	/** M_LocatorTo_ID AD_Reference_ID=191 */
	public static final int M_LOCATORTO_ID_AD_Reference_ID = 191;

	/**
	 * Set Locator To. Location inventory is moved to
	 */
	public void setM_LocatorTo_ID(int M_LocatorTo_ID) {
		if (M_LocatorTo_ID < 1)
			throw new IllegalArgumentException("M_LocatorTo_ID is mandatory.");
		set_Value("M_LocatorTo_ID", new Integer(M_LocatorTo_ID));
	}

	/**
	 * Get Locator To. Location inventory is moved to
	 */
	public int getM_LocatorTo_ID() {
		Integer ii = (Integer) get_Value("M_LocatorTo_ID");
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
	 * Set Move Line. Inventory Move document Line
	 */
	public void setM_MovementLine_ID(int M_MovementLine_ID) {
		if (M_MovementLine_ID < 1)
			throw new IllegalArgumentException(
					"M_MovementLine_ID is mandatory.");
		set_ValueNoCheck("M_MovementLine_ID", new Integer(M_MovementLine_ID));
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
	 * Set Inventory Move. Movement of Inventory
	 */
	public void setM_Movement_ID(int M_Movement_ID) {
		if (M_Movement_ID < 1)
			throw new IllegalArgumentException("M_Movement_ID is mandatory.");
		set_ValueNoCheck("M_Movement_ID", new Integer(M_Movement_ID));
	}

	/**
	 * Get Inventory Move. Movement of Inventory
	 */
	public int getM_Movement_ID() {
		Integer ii = (Integer) get_Value("M_Movement_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** M_Product_ID AD_Reference_ID=171 */
	public static final int M_PRODUCT_ID_AD_Reference_ID = 171;

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
		if (TargetQty == null)
			throw new IllegalArgumentException("TargetQty is mandatory.");
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
