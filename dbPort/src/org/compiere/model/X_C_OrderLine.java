/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_OrderLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.812
 */
public class X_C_OrderLine extends PO {
	/** Standard Constructor */
	public X_C_OrderLine(Properties ctx, int C_OrderLine_ID, String trxName) {
		super(ctx, C_OrderLine_ID, trxName);
		/**
		 * if (C_OrderLine_ID == 0) { setC_BPartner_Location_ID (0); //
		 * 
		 * @C_BPartner_Location_ID@ setC_Currency_ID (0); //
		 * @C_Currency_ID@ setC_OrderLine_ID (0); setC_Order_ID (0); setC_Tax_ID
		 *                 (0); setC_UOM_ID (0); //
		 * @#C_UOM_ID@ setDateOrdered (new
		 *             Timestamp(System.currentTimeMillis())); //
		 * @DateOrdered@ setFreightAmt (Env.ZERO); setIsDescription (false); //
		 *               N setLine (0); //
		 * @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_OrderLine
		 *             WHERE C_Order_ID=@C_Order_ID@ setLineNetAmt (Env.ZERO);
		 *             setM_AttributeSetInstance_ID (0); setM_Warehouse_ID (0); //
		 * @M_Warehouse_ID@ setPriceActual (Env.ZERO); setPriceEntered
		 *                  (Env.ZERO); setPriceLimit (Env.ZERO); setPriceList
		 *                  (Env.ZERO); setProcessed (false); setQtyDelivered
		 *                  (Env.ZERO); setQtyEntered (Env.ZERO); // 1
		 *                  setQtyInvoiced (Env.ZERO); setQtyLostSales
		 *                  (Env.ZERO); setQtyOrdered (Env.ZERO); // 1
		 *                  setQtyReserved (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_OrderLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_OrderLine */
	public static final String Table_Name = "C_OrderLine";

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
		StringBuffer sb = new StringBuffer("X_C_OrderLine[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID <= 0)
			set_ValueNoCheck("C_BPartner_ID", null);
		else
			set_ValueNoCheck("C_BPartner_ID", new Integer(C_BPartner_ID));
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
	 * Set Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public void setC_BPartner_Location_ID(int C_BPartner_Location_ID) {
		if (C_BPartner_Location_ID < 1)
			throw new IllegalArgumentException(
					"C_BPartner_Location_ID is mandatory.");
		set_Value("C_BPartner_Location_ID", new Integer(C_BPartner_Location_ID));
	}

	/**
	 * Get Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public int getC_BPartner_Location_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_Location_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Currency. The Currency for this record
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		if (C_Currency_ID < 1)
			throw new IllegalArgumentException("C_Currency_ID is mandatory.");
		set_ValueNoCheck("C_Currency_ID", new Integer(C_Currency_ID));
	}

	/**
	 * Get Currency. The Currency for this record
	 */
	public int getC_Currency_ID() {
		Integer ii = (Integer) get_Value("C_Currency_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Sales Order Line. Sales Order Line
	 */
	public void setC_OrderLine_ID(int C_OrderLine_ID) {
		if (C_OrderLine_ID < 1)
			throw new IllegalArgumentException("C_OrderLine_ID is mandatory.");
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
	 * Set Order. Order
	 */
	public void setC_Order_ID(int C_Order_ID) {
		if (C_Order_ID < 1)
			throw new IllegalArgumentException("C_Order_ID is mandatory.");
		set_ValueNoCheck("C_Order_ID", new Integer(C_Order_ID));
	}

	/**
	 * Get Order. Order
	 */
	public int getC_Order_ID() {
		Integer ii = (Integer) get_Value("C_Order_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_Order_ID()));
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
	 * Set Tax. Tax identifier
	 */
	public void setC_Tax_ID(int C_Tax_ID) {
		if (C_Tax_ID < 1)
			throw new IllegalArgumentException("C_Tax_ID is mandatory.");
		set_Value("C_Tax_ID", new Integer(C_Tax_ID));
	}

	/**
	 * Get Tax. Tax identifier
	 */
	public int getC_Tax_ID() {
		Integer ii = (Integer) get_Value("C_Tax_ID");
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
	 * Set Date Delivered. Date when the product was delivered
	 */
	public void setDateDelivered(Timestamp DateDelivered) {
		set_ValueNoCheck("DateDelivered", DateDelivered);
	}

	/**
	 * Get Date Delivered. Date when the product was delivered
	 */
	public Timestamp getDateDelivered() {
		return (Timestamp) get_Value("DateDelivered");
	}

	/**
	 * Set Date Invoiced. Date printed on Invoice
	 */
	public void setDateInvoiced(Timestamp DateInvoiced) {
		set_ValueNoCheck("DateInvoiced", DateInvoiced);
	}

	/**
	 * Get Date Invoiced. Date printed on Invoice
	 */
	public Timestamp getDateInvoiced() {
		return (Timestamp) get_Value("DateInvoiced");
	}

	/**
	 * Set Date Ordered. Date of Order
	 */
	public void setDateOrdered(Timestamp DateOrdered) {
		if (DateOrdered == null)
			throw new IllegalArgumentException("DateOrdered is mandatory.");
		set_Value("DateOrdered", DateOrdered);
	}

	/**
	 * Get Date Ordered. Date of Order
	 */
	public Timestamp getDateOrdered() {
		return (Timestamp) get_Value("DateOrdered");
	}

	/**
	 * Set Date Promised. Date Order was promised
	 */
	public void setDatePromised(Timestamp DatePromised) {
		set_Value("DatePromised", DatePromised);
	}

	/**
	 * Get Date Promised. Date Order was promised
	 */
	public Timestamp getDatePromised() {
		return (Timestamp) get_Value("DatePromised");
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
	 * Set Discount %. Discount in percent
	 */
	public void setDiscount(BigDecimal Discount) {
		set_Value("Discount", Discount);
	}

	/**
	 * Get Discount %. Discount in percent
	 */
	public BigDecimal getDiscount() {
		BigDecimal bd = (BigDecimal) get_Value("Discount");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Freight Amount. Freight Amount
	 */
	public void setFreightAmt(BigDecimal FreightAmt) {
		if (FreightAmt == null)
			throw new IllegalArgumentException("FreightAmt is mandatory.");
		set_Value("FreightAmt", FreightAmt);
	}

	/**
	 * Get Freight Amount. Freight Amount
	 */
	public BigDecimal getFreightAmt() {
		BigDecimal bd = (BigDecimal) get_Value("FreightAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
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

	/**
	 * Set Line Amount. Line Extended Amount (Quantity * Actual Price) without
	 * Freight and Charges
	 */
	public void setLineNetAmt(BigDecimal LineNetAmt) {
		if (LineNetAmt == null)
			throw new IllegalArgumentException("LineNetAmt is mandatory.");
		set_ValueNoCheck("LineNetAmt", LineNetAmt);
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
	 * Set Shipper. Method or manner of product delivery
	 */
	public void setM_Shipper_ID(int M_Shipper_ID) {
		if (M_Shipper_ID <= 0)
			set_Value("M_Shipper_ID", null);
		else
			set_Value("M_Shipper_ID", new Integer(M_Shipper_ID));
	}

	/**
	 * Get Shipper. Method or manner of product delivery
	 */
	public int getM_Shipper_ID() {
		Integer ii = (Integer) get_Value("M_Shipper_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** M_Warehouse_ID AD_Reference_ID=197 */
	public static final int M_WAREHOUSE_ID_AD_Reference_ID = 197;

	/**
	 * Set Warehouse. Storage Warehouse and Service Point
	 */
	public void setM_Warehouse_ID(int M_Warehouse_ID) {
		if (M_Warehouse_ID < 1)
			throw new IllegalArgumentException("M_Warehouse_ID is mandatory.");
		set_Value("M_Warehouse_ID", new Integer(M_Warehouse_ID));
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
	 * Set Unit Price. Actual Price
	 */
	public void setPriceActual(BigDecimal PriceActual) {
		if (PriceActual == null)
			throw new IllegalArgumentException("PriceActual is mandatory.");
		set_ValueNoCheck("PriceActual", PriceActual);
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
	 * Set Cost Price. Price per Unit of Measure including all indirect costs
	 * (Freight, etc.)
	 */
	public void setPriceCost(BigDecimal PriceCost) {
		set_Value("PriceCost", PriceCost);
	}

	/**
	 * Get Cost Price. Price per Unit of Measure including all indirect costs
	 * (Freight, etc.)
	 */
	public BigDecimal getPriceCost() {
		BigDecimal bd = (BigDecimal) get_Value("PriceCost");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Price. Price Entered - the price based on the selected/base UoM
	 */
	public void setPriceEntered(BigDecimal PriceEntered) {
		if (PriceEntered == null)
			throw new IllegalArgumentException("PriceEntered is mandatory.");
		set_Value("PriceEntered", PriceEntered);
	}

	/**
	 * Get Price. Price Entered - the price based on the selected/base UoM
	 */
	public BigDecimal getPriceEntered() {
		BigDecimal bd = (BigDecimal) get_Value("PriceEntered");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Limit Price. Lowest price for a product
	 */
	public void setPriceLimit(BigDecimal PriceLimit) {
		if (PriceLimit == null)
			throw new IllegalArgumentException("PriceLimit is mandatory.");
		set_Value("PriceLimit", PriceLimit);
	}

	/**
	 * Get Limit Price. Lowest price for a product
	 */
	public BigDecimal getPriceLimit() {
		BigDecimal bd = (BigDecimal) get_Value("PriceLimit");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set List Price. List Price
	 */
	public void setPriceList(BigDecimal PriceList) {
		if (PriceList == null)
			throw new IllegalArgumentException("PriceList is mandatory.");
		set_Value("PriceList", PriceList);
	}

	/**
	 * Get List Price. List Price
	 */
	public BigDecimal getPriceList() {
		BigDecimal bd = (BigDecimal) get_Value("PriceList");
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

	/** Set QTYADVANTAGE */
	public void setQTYADVANTAGE(BigDecimal QTYADVANTAGE) {
		set_Value("QTYADVANTAGE", QTYADVANTAGE);
	}

	/** Get QTYADVANTAGE */
	public BigDecimal getQTYADVANTAGE() {
		BigDecimal bd = (BigDecimal) get_Value("QTYADVANTAGE");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set QTYCALC */
	public void setQTYCALC(BigDecimal QTYCALC) {
		set_Value("QTYCALC", QTYCALC);
	}

	/** Get QTYCALC */
	public BigDecimal getQTYCALC() {
		BigDecimal bd = (BigDecimal) get_Value("QTYCALC");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Delivered Quantity. Delivered Quantity
	 */
	public void setQtyDelivered(BigDecimal QtyDelivered) {
		if (QtyDelivered == null)
			throw new IllegalArgumentException("QtyDelivered is mandatory.");
		set_ValueNoCheck("QtyDelivered", QtyDelivered);
	}

	/**
	 * Get Delivered Quantity. Delivered Quantity
	 */
	public BigDecimal getQtyDelivered() {
		BigDecimal bd = (BigDecimal) get_Value("QtyDelivered");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
		
		BigDecimal bd = null;
		try{
			bd = (BigDecimal) get_Value("QtyEntered");
		
		}
		catch(ClassCastException e){
			if (get_Value("QtyEntered") instanceof Integer)
				bd=new BigDecimal(((Integer) get_Value("QtyEntered")).intValue());
				
		}
			
		
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Quantity Invoiced. Invoiced Quantity
	 */
	public void setQtyInvoiced(BigDecimal QtyInvoiced) {
		if (QtyInvoiced == null)
			throw new IllegalArgumentException("QtyInvoiced is mandatory.");
		set_ValueNoCheck("QtyInvoiced", QtyInvoiced);
	}

	/**
	 * Get Quantity Invoiced. Invoiced Quantity
	 */
	public BigDecimal getQtyInvoiced() {
		BigDecimal bd = (BigDecimal) get_Value("QtyInvoiced");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Lost Sales Qty. Quantity of potential sales
	 */
	public void setQtyLostSales(BigDecimal QtyLostSales) {
		if (QtyLostSales == null)
			throw new IllegalArgumentException("QtyLostSales is mandatory.");
		set_Value("QtyLostSales", QtyLostSales);
	}

	/**
	 * Get Lost Sales Qty. Quantity of potential sales
	 */
	public BigDecimal getQtyLostSales() {
		BigDecimal bd = (BigDecimal) get_Value("QtyLostSales");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Ordered Quantity. Ordered Quantity
	 */
	public void setQtyOrdered(BigDecimal QtyOrdered) {
		if (QtyOrdered == null)
			throw new IllegalArgumentException("QtyOrdered is mandatory.");
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
		if (QtyReserved == null)
			throw new IllegalArgumentException("QtyReserved is mandatory.");
		set_ValueNoCheck("QtyReserved", QtyReserved);
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

	/** Ref_OrderLine_ID AD_Reference_ID=271 */
	public static final int REF_ORDERLINE_ID_AD_Reference_ID = 271;

	/**
	 * Set Referenced Order Line. Reference to corresponding Sales/Purchase
	 * Order
	 */
	public void setRef_OrderLine_ID(int Ref_OrderLine_ID) {
		if (Ref_OrderLine_ID <= 0)
			set_Value("Ref_OrderLine_ID", null);
		else
			set_Value("Ref_OrderLine_ID", new Integer(Ref_OrderLine_ID));
	}

	/**
	 * Get Referenced Order Line. Reference to corresponding Sales/Purchase
	 * Order
	 */
	public int getRef_OrderLine_ID() {
		Integer ii = (Integer) get_Value("Ref_OrderLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Resource Assignment. Resource Assignment
	 */
	public void setS_ResourceAssignment_ID(int S_ResourceAssignment_ID) {
		if (S_ResourceAssignment_ID <= 0)
			set_Value("S_ResourceAssignment_ID", null);
		else
			set_Value("S_ResourceAssignment_ID", new Integer(
					S_ResourceAssignment_ID));
	}

	/**
	 * Get Resource Assignment. Resource Assignment
	 */
	public int getS_ResourceAssignment_ID() {
		Integer ii = (Integer) get_Value("S_ResourceAssignment_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
