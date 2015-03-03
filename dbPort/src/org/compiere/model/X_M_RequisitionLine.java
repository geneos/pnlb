/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_RequisitionLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.656
 */
public class X_M_RequisitionLine extends PO {
	/** Standard Constructor */
	public X_M_RequisitionLine(Properties ctx, int M_RequisitionLine_ID,
			String trxName) {
		super(ctx, M_RequisitionLine_ID, trxName);
		/**
		 * if (M_RequisitionLine_ID == 0) { setLine (0); //
		 * 
		 * @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM
		 *             M_RequisitionLine WHERE
		 *             M_Requisition_ID=@M_Requisition_ID@ setLineNetAmt
		 *             (Env.ZERO); setM_RequisitionLine_ID (0);
		 *             setM_Requisition_ID (0); setPriceActual (Env.ZERO);
		 *             setQty (Env.ZERO); // 1 }
		 */
	}

	/** Load Constructor */
	public X_M_RequisitionLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_RequisitionLine */
	public static final String Table_Name = "M_RequisitionLine";

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
		StringBuffer sb = new StringBuffer("X_M_RequisitionLine[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** C_Charge_ID AD_Reference_ID=200 */
	public static final int C_CHARGE_ID_AD_Reference_ID = 200;

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
			set_Value("C_OrderLine_ID", null);
		else
			set_Value("C_OrderLine_ID", new Integer(C_OrderLine_ID));
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
	 * Set Line Amount. Line Extended Amount (Quantity * Actual Price) without
	 * Freight and Charges
	 */
	public void setLineNetAmt(BigDecimal LineNetAmt) {
		if (LineNetAmt == null)
			throw new IllegalArgumentException("LineNetAmt is mandatory.");
		set_Value("LineNetAmt", LineNetAmt);
	}

	/**
	 * Get Line Amount. Line Extended Amount (Quantity * Actual Price) without
	 * Freight and Charges
	 */
	public BigDecimal getLineNetAmt() {
		BigDecimal bd = (BigDecimal) get_Value("LineNetAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Attribute Set Instance. Product Attribute Set Instance
	 */
	public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
		if (M_AttributeSetInstance_ID <= 0)
			set_Value("M_AttributeSetInstance_ID", null);
		else
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
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID <= 0)
			set_Value("M_Product_ID", null);
		else
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
	 * Set Requisition Line. Material Requisition Line
	 */
	public void setM_RequisitionLine_ID(int M_RequisitionLine_ID) {
		if (M_RequisitionLine_ID < 1)
			throw new IllegalArgumentException(
					"M_RequisitionLine_ID is mandatory.");
		set_ValueNoCheck("M_RequisitionLine_ID", new Integer(
				M_RequisitionLine_ID));
	}

	/**
	 * Get Requisition Line. Material Requisition Line
	 */
	public int getM_RequisitionLine_ID() {
		Integer ii = (Integer) get_Value("M_RequisitionLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Requisition. Material Requisition
	 */
	public void setM_Requisition_ID(int M_Requisition_ID) {
		if (M_Requisition_ID < 1)
			throw new IllegalArgumentException("M_Requisition_ID is mandatory.");
		set_ValueNoCheck("M_Requisition_ID", new Integer(M_Requisition_ID));
	}

	/**
	 * Get Requisition. Material Requisition
	 */
	public int getM_Requisition_ID() {
		Integer ii = (Integer) get_Value("M_Requisition_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Unit Price. Actual Price
	 */
	public void setPriceActual(BigDecimal PriceActual) {
		if (PriceActual == null)
			throw new IllegalArgumentException("PriceActual is mandatory.");
		set_Value("PriceActual", PriceActual);
	}

	/**
	 * Get Unit Price. Actual Price
	 */
	public BigDecimal getPriceActual() {
		BigDecimal bd = (BigDecimal) get_Value("PriceActual");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
