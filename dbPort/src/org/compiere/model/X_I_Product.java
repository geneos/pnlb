/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for I_Product
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.531
 */
public class X_I_Product extends PO {
	/** Standard Constructor */
	public X_I_Product(Properties ctx, int I_Product_ID, String trxName) {
		super(ctx, I_Product_ID, trxName);
		/**
		 * if (I_Product_ID == 0) { setI_IsImported (false); setI_Product_ID
		 * (0); }
		 */
	}

	/** Load Constructor */
	public X_I_Product(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=I_Product */
	public static final String Table_Name = "I_Product";

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

	protected BigDecimal accessLevel = new BigDecimal(2);

	/** AccessLevel 2 - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_I_Product[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Business Partner Key. The Key of the Business Partner
	 */
	public void setBPartner_Value(String BPartner_Value) {
		if (BPartner_Value != null && BPartner_Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			BPartner_Value = BPartner_Value.substring(0, 39);
		}
		set_Value("BPartner_Value", BPartner_Value);
	}

	/**
	 * Get Business Partner Key. The Key of the Business Partner
	 */
	public String getBPartner_Value() {
		return (String) get_Value("BPartner_Value");
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID <= 0)
			set_Value("C_BPartner_ID", null);
		else
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
	 * Set Currency. The Currency for this record
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		if (C_Currency_ID <= 0)
			set_Value("C_Currency_ID", null);
		else
			set_Value("C_Currency_ID", new Integer(C_Currency_ID));
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
	 * Set UOM. Unit of Measure
	 */
	public void setC_UOM_ID(int C_UOM_ID) {
		if (C_UOM_ID <= 0)
			set_Value("C_UOM_ID", null);
		else
			set_Value("C_UOM_ID", new Integer(C_UOM_ID));
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
	 * Set Classification. Classification for grouping
	 */
	public void setClassification(String Classification) {
		if (Classification != null && Classification.length() > 1) {
			log.warning("Length > 1 - truncated");
			Classification = Classification.substring(0, 0);
		}
		set_Value("Classification", Classification);
	}

	/**
	 * Get Classification. Classification for grouping
	 */
	public String getClassification() {
		return (String) get_Value("Classification");
	}

	/**
	 * Set Cost per Order. Fixed Cost Per Order
	 */
	public void setCostPerOrder(BigDecimal CostPerOrder) {
		set_Value("CostPerOrder", CostPerOrder);
	}

	/**
	 * Get Cost per Order. Fixed Cost Per Order
	 */
	public BigDecimal getCostPerOrder() {
		BigDecimal bd = (BigDecimal) get_Value("CostPerOrder");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Promised Delivery Time. Promised days between order and delivery
	 */
	public void setDeliveryTime_Promised(int DeliveryTime_Promised) {
		set_Value("DeliveryTime_Promised", new Integer(DeliveryTime_Promised));
	}

	/**
	 * Get Promised Delivery Time. Promised days between order and delivery
	 */
	public int getDeliveryTime_Promised() {
		Integer ii = (Integer) get_Value("DeliveryTime_Promised");
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
	 * Set Description URL. URL for the description
	 */
	public void setDescriptionURL(String DescriptionURL) {
		if (DescriptionURL != null && DescriptionURL.length() > 120) {
			log.warning("Length > 120 - truncated");
			DescriptionURL = DescriptionURL.substring(0, 119);
		}
		set_Value("DescriptionURL", DescriptionURL);
	}

	/**
	 * Get Description URL. URL for the description
	 */
	public String getDescriptionURL() {
		return (String) get_Value("DescriptionURL");
	}

	/**
	 * Set Discontinued. This product is no longer available
	 */
	public void setDiscontinued(boolean Discontinued) {
		set_Value("Discontinued", new Boolean(Discontinued));
	}

	/**
	 * Get Discontinued. This product is no longer available
	 */
	public boolean isDiscontinued() {
		Object oo = get_Value("Discontinued");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Discontinued by. Discontinued By
	 */
	public void setDiscontinuedBy(Timestamp DiscontinuedBy) {
		set_Value("DiscontinuedBy", DiscontinuedBy);
	}

	/**
	 * Get Discontinued by. Discontinued By
	 */
	public Timestamp getDiscontinuedBy() {
		return (Timestamp) get_Value("DiscontinuedBy");
	}

	/**
	 * Set Document Note. Additional information for a Document
	 */
	public void setDocumentNote(String DocumentNote) {
		if (DocumentNote != null && DocumentNote.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			DocumentNote = DocumentNote.substring(0, 1999);
		}
		set_Value("DocumentNote", DocumentNote);
	}

	/**
	 * Get Document Note. Additional information for a Document
	 */
	public String getDocumentNote() {
		return (String) get_Value("DocumentNote");
	}

	/**
	 * Set Comment/Help. Comment or Hint
	 */
	public void setHelp(String Help) {
		if (Help != null && Help.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Help = Help.substring(0, 1999);
		}
		set_Value("Help", Help);
	}

	/**
	 * Get Comment/Help. Comment or Hint
	 */
	public String getHelp() {
		return (String) get_Value("Help");
	}

	/**
	 * Set ISO Currency Code. Three letter ISO 4217 Code of the Currency
	 */
	public void setISO_Code(String ISO_Code) {
		if (ISO_Code != null && ISO_Code.length() > 3) {
			log.warning("Length > 3 - truncated");
			ISO_Code = ISO_Code.substring(0, 2);
		}
		set_Value("ISO_Code", ISO_Code);
	}

	/**
	 * Get ISO Currency Code. Three letter ISO 4217 Code of the Currency
	 */
	public String getISO_Code() {
		return (String) get_Value("ISO_Code");
	}

	/**
	 * Set Import Error Message. Messages generated from import process
	 */
	public void setI_ErrorMsg(String I_ErrorMsg) {
		if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			I_ErrorMsg = I_ErrorMsg.substring(0, 1999);
		}
		set_Value("I_ErrorMsg", I_ErrorMsg);
	}

	/**
	 * Get Import Error Message. Messages generated from import process
	 */
	public String getI_ErrorMsg() {
		return (String) get_Value("I_ErrorMsg");
	}

	/**
	 * Set Imported. Has this import been processed
	 */
	public void setI_IsImported(boolean I_IsImported) {
		set_Value("I_IsImported", new Boolean(I_IsImported));
	}

	/**
	 * Get Imported. Has this import been processed
	 */
	public boolean isI_IsImported() {
		Object oo = get_Value("I_IsImported");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Import Product. Import Item or Service
	 */
	public void setI_Product_ID(int I_Product_ID) {
		if (I_Product_ID < 1)
			throw new IllegalArgumentException("I_Product_ID is mandatory.");
		set_ValueNoCheck("I_Product_ID", new Integer(I_Product_ID));
	}

	/**
	 * Get Import Product. Import Item or Service
	 */
	public int getI_Product_ID() {
		Integer ii = (Integer) get_Value("I_Product_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Image URL. URL of image
	 */
	public void setImageURL(String ImageURL) {
		if (ImageURL != null && ImageURL.length() > 120) {
			log.warning("Length > 120 - truncated");
			ImageURL = ImageURL.substring(0, 119);
		}
		set_Value("ImageURL", ImageURL);
	}

	/**
	 * Get Image URL. URL of image
	 */
	public String getImageURL() {
		return (String) get_Value("ImageURL");
	}

	/**
	 * Set Product Category. Category of a Product
	 */
	public void setM_Product_Category_ID(int M_Product_Category_ID) {
		if (M_Product_Category_ID <= 0)
			set_Value("M_Product_Category_ID", null);
		else
			set_Value("M_Product_Category_ID", new Integer(
					M_Product_Category_ID));
	}

	/**
	 * Get Product Category. Category of a Product
	 */
	public int getM_Product_Category_ID() {
		Integer ii = (Integer) get_Value("M_Product_Category_ID");
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
	 * Set Manufacturer. Manufacturer of the Product
	 */
	public void setManufacturer(String Manufacturer) {
		if (Manufacturer != null && Manufacturer.length() > 30) {
			log.warning("Length > 30 - truncated");
			Manufacturer = Manufacturer.substring(0, 29);
		}
		set_Value("Manufacturer", Manufacturer);
	}

	/**
	 * Get Manufacturer. Manufacturer of the Product
	 */
	public String getManufacturer() {
		return (String) get_Value("Manufacturer");
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name != null && Name.length() > 60) {
			log.warning("Length > 60 - truncated");
			Name = Name.substring(0, 59);
		}
		set_Value("Name", Name);
	}

	/**
	 * Get Name. Alphanumeric identifier of the entity
	 */
	public String getName() {
		return (String) get_Value("Name");
	}

	/**
	 * Set Minimum Order Qty. Minimum order quantity in UOM
	 */
	public void setOrder_Min(int Order_Min) {
		set_Value("Order_Min", new Integer(Order_Min));
	}

	/**
	 * Get Minimum Order Qty. Minimum order quantity in UOM
	 */
	public int getOrder_Min() {
		Integer ii = (Integer) get_Value("Order_Min");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Order Pack Qty. Package order size in UOM (e.g. order set of 5 units)
	 */
	public void setOrder_Pack(int Order_Pack) {
		set_Value("Order_Pack", new Integer(Order_Pack));
	}

	/**
	 * Get Order Pack Qty. Package order size in UOM (e.g. order set of 5 units)
	 */
	public int getOrder_Pack() {
		Integer ii = (Integer) get_Value("Order_Pack");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Price effective. Effective Date of Price
	 */
	public void setPriceEffective(Timestamp PriceEffective) {
		set_Value("PriceEffective", PriceEffective);
	}

	/**
	 * Get Price effective. Effective Date of Price
	 */
	public Timestamp getPriceEffective() {
		return (Timestamp) get_Value("PriceEffective");
	}

	/**
	 * Set Limit Price. Lowest price for a product
	 */
	public void setPriceLimit(BigDecimal PriceLimit) {
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
	 * Set PO Price. Price based on a purchase order
	 */
	public void setPricePO(BigDecimal PricePO) {
		set_Value("PricePO", PricePO);
	}

	/**
	 * Get PO Price. Price based on a purchase order
	 */
	public BigDecimal getPricePO() {
		BigDecimal bd = (BigDecimal) get_Value("PricePO");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Standard Price. Standard Price
	 */
	public void setPriceStd(BigDecimal PriceStd) {
		set_Value("PriceStd", PriceStd);
	}

	/**
	 * Get Standard Price. Standard Price
	 */
	public BigDecimal getPriceStd() {
		BigDecimal bd = (BigDecimal) get_Value("PriceStd");
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

	/** Set Process Now */
	public void setProcessing(boolean Processing) {
		set_Value("Processing", new Boolean(Processing));
	}

	/** Get Process Now */
	public boolean isProcessing() {
		Object oo = get_Value("Processing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Product Category Key */
	public void setProductCategory_Value(String ProductCategory_Value) {
		if (ProductCategory_Value != null
				&& ProductCategory_Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			ProductCategory_Value = ProductCategory_Value.substring(0, 39);
		}
		set_Value("ProductCategory_Value", ProductCategory_Value);
	}

	/** Get Product Category Key */
	public String getProductCategory_Value() {
		return (String) get_Value("ProductCategory_Value");
	}

	/** ProductType AD_Reference_ID=270 */
	public static final int PRODUCTTYPE_AD_Reference_ID = 270;

	/** Expense type = E */
	public static final String PRODUCTTYPE_ExpenseType = "E";

	/** Item = I */
	public static final String PRODUCTTYPE_Item = "I";

	/** Online = O */
	public static final String PRODUCTTYPE_Online = "O";

	/** Resource = R */
	public static final String PRODUCTTYPE_Resource = "R";

	/** Service = S */
	public static final String PRODUCTTYPE_Service = "S";

	/**
	 * Set Product Type. Type of product
	 */
	public void setProductType(String ProductType) {
		if (ProductType != null && ProductType.length() > 1) {
			log.warning("Length > 1 - truncated");
			ProductType = ProductType.substring(0, 0);
		}
		set_Value("ProductType", ProductType);
	}

	/**
	 * Get Product Type. Type of product
	 */
	public String getProductType() {
		return (String) get_Value("ProductType");
	}

	/**
	 * Set Royalty Amount. (Included) Amount for copyright, etc.
	 */
	public void setRoyaltyAmt(BigDecimal RoyaltyAmt) {
		set_Value("RoyaltyAmt", RoyaltyAmt);
	}

	/**
	 * Get Royalty Amount. (Included) Amount for copyright, etc.
	 */
	public BigDecimal getRoyaltyAmt() {
		BigDecimal bd = (BigDecimal) get_Value("RoyaltyAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set SKU. Stock Keeping Unit
	 */
	public void setSKU(String SKU) {
		if (SKU != null && SKU.length() > 30) {
			log.warning("Length > 30 - truncated");
			SKU = SKU.substring(0, 29);
		}
		set_Value("SKU", SKU);
	}

	/**
	 * Get SKU. Stock Keeping Unit
	 */
	public String getSKU() {
		return (String) get_Value("SKU");
	}

	/**
	 * Set Shelf Depth. Shelf depth required
	 */
	public void setShelfDepth(int ShelfDepth) {
		set_Value("ShelfDepth", new Integer(ShelfDepth));
	}

	/**
	 * Get Shelf Depth. Shelf depth required
	 */
	public int getShelfDepth() {
		Integer ii = (Integer) get_Value("ShelfDepth");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Shelf Height. Shelf height required
	 */
	public void setShelfHeight(int ShelfHeight) {
		set_Value("ShelfHeight", new Integer(ShelfHeight));
	}

	/**
	 * Get Shelf Height. Shelf height required
	 */
	public int getShelfHeight() {
		Integer ii = (Integer) get_Value("ShelfHeight");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Shelf Width. Shelf width required
	 */
	public void setShelfWidth(int ShelfWidth) {
		set_Value("ShelfWidth", new Integer(ShelfWidth));
	}

	/**
	 * Get Shelf Width. Shelf width required
	 */
	public int getShelfWidth() {
		Integer ii = (Integer) get_Value("ShelfWidth");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set UPC/EAN. Bar Code (Universal Product Code or its superset European
	 * Article Number)
	 */
	public void setUPC(String UPC) {
		if (UPC != null && UPC.length() > 30) {
			log.warning("Length > 30 - truncated");
			UPC = UPC.substring(0, 29);
		}
		set_Value("UPC", UPC);
	}

	/**
	 * Get UPC/EAN. Bar Code (Universal Product Code or its superset European
	 * Article Number)
	 */
	public String getUPC() {
		return (String) get_Value("UPC");
	}

	/**
	 * Set Units Per Pallet. Units Per Pallet
	 */
	public void setUnitsPerPallet(int UnitsPerPallet) {
		set_Value("UnitsPerPallet", new Integer(UnitsPerPallet));
	}

	/**
	 * Get Units Per Pallet. Units Per Pallet
	 */
	public int getUnitsPerPallet() {
		Integer ii = (Integer) get_Value("UnitsPerPallet");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value != null && Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value = Value.substring(0, 39);
		}
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public String getValue() {
		return (String) get_Value("Value");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getValue());
	}

	/**
	 * Set Partner Category. Product Category of the Business Partner
	 */
	public void setVendorCategory(String VendorCategory) {
		if (VendorCategory != null && VendorCategory.length() > 30) {
			log.warning("Length > 30 - truncated");
			VendorCategory = VendorCategory.substring(0, 29);
		}
		set_Value("VendorCategory", VendorCategory);
	}

	/**
	 * Get Partner Category. Product Category of the Business Partner
	 */
	public String getVendorCategory() {
		return (String) get_Value("VendorCategory");
	}

	/**
	 * Set Partner Product Key. Product Key of the Business Partner
	 */
	public void setVendorProductNo(String VendorProductNo) {
		if (VendorProductNo != null && VendorProductNo.length() > 30) {
			log.warning("Length > 30 - truncated");
			VendorProductNo = VendorProductNo.substring(0, 29);
		}
		set_Value("VendorProductNo", VendorProductNo);
	}

	/**
	 * Get Partner Product Key. Product Key of the Business Partner
	 */
	public String getVendorProductNo() {
		return (String) get_Value("VendorProductNo");
	}

	/**
	 * Set Volume. Volume of a product
	 */
	public void setVolume(int Volume) {
		set_Value("Volume", new Integer(Volume));
	}

	/**
	 * Get Volume. Volume of a product
	 */
	public int getVolume() {
		Integer ii = (Integer) get_Value("Volume");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Weight. Weight of a product
	 */
	public void setWeight(int Weight) {
		set_Value("Weight", new Integer(Weight));
	}

	/**
	 * Get Weight. Weight of a product
	 */
	public int getWeight() {
		Integer ii = (Integer) get_Value("Weight");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set UOM Code. UOM EDI X12 Code
	 */
	public void setX12DE355(String X12DE355) {
		if (X12DE355 != null && X12DE355.length() > 2) {
			log.warning("Length > 2 - truncated");
			X12DE355 = X12DE355.substring(0, 1);
		}
		set_Value("X12DE355", X12DE355);
	}

	/**
	 * Get UOM Code. UOM EDI X12 Code
	 */
	public String getX12DE355() {
		return (String) get_Value("X12DE355");
	}
}
