/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Product
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-01-22 12:09:23.328
 */
public class X_M_Product extends PO {
	/** Standard Constructor */
	public X_M_Product(Properties ctx, int M_Product_ID, String trxName) {
		super(ctx, M_Product_ID, trxName);
		/**
		 * if (M_Product_ID == 0) { setC_TaxCategory_ID (0); // 1000019
		 * setC_UOM_ID (0); setIsBOM (false); // N setIsDropShip (false);
		 * setIsExcludeAutoDelivery (false); // N setIsInvoicePrintDetails
		 * (false); setIsPickListPrintDetails (false); setIsPurchased (true); //
		 * Y setIsSelfService (true); // Y setIsSold (true); // Y setIsStocked
		 * (true); // Y setIsSummary (false); setIsVerified (false); // N
		 * setIsWebStoreFeatured (false); setM_AttributeSetInstance_ID (0);
		 * setM_Product_Category_ID (0); setM_Product_ID (0); setName (null);
		 * setProductType (null); // I setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_M_Product(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Product */
	public static final String Table_Name = "M_Product";

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
		StringBuffer sb = new StringBuffer("X_M_Product[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Revenue Recognition. Method for recording revenue
	 */
	public void setC_RevenueRecognition_ID(int C_RevenueRecognition_ID) {
		if (C_RevenueRecognition_ID <= 0)
			set_Value("C_RevenueRecognition_ID", null);
		else
			set_Value("C_RevenueRecognition_ID", new Integer(
					C_RevenueRecognition_ID));
	}

	/**
	 * Get Revenue Recognition. Method for recording revenue
	 */
	public int getC_RevenueRecognition_ID() {
		Integer ii = (Integer) get_Value("C_RevenueRecognition_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Subscription Type. Type of subscription
	 */
	public void setC_SubscriptionType_ID(int C_SubscriptionType_ID) {
		if (C_SubscriptionType_ID <= 0)
			set_Value("C_SubscriptionType_ID", null);
		else
			set_Value("C_SubscriptionType_ID", new Integer(
					C_SubscriptionType_ID));
	}

	/**
	 * Get Subscription Type. Type of subscription
	 */
	public int getC_SubscriptionType_ID() {
		Integer ii = (Integer) get_Value("C_SubscriptionType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Tax Category. Tax Category
	 */
	public void setC_TaxCategory_ID(int C_TaxCategory_ID) {
		if (C_TaxCategory_ID < 1)
			throw new IllegalArgumentException("C_TaxCategory_ID is mandatory.");
		set_Value("C_TaxCategory_ID", new Integer(C_TaxCategory_ID));
	}

	/**
	 * Get Tax Category. Tax Category
	 */
	public int getC_TaxCategory_ID() {
		Integer ii = (Integer) get_Value("C_TaxCategory_ID");
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
	 * Set Guarantee Days. Number of days the product is guaranteed or available
	 */
	public void setGuaranteeDays(int GuaranteeDays) {
		set_Value("GuaranteeDays", new Integer(GuaranteeDays));
	}

	/**
	 * Get Guarantee Days. Number of days the product is guaranteed or available
	 */
	public int getGuaranteeDays() {
		Integer ii = (Integer) get_Value("GuaranteeDays");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Min Guarantee Days. Minumum number of guarantee days
	 */
	public void setGuaranteeDaysMin(int GuaranteeDaysMin) {
		set_Value("GuaranteeDaysMin", new Integer(GuaranteeDaysMin));
	}

	/**
	 * Get Min Guarantee Days. Minumum number of guarantee days
	 */
	public int getGuaranteeDaysMin() {
		Integer ii = (Integer) get_Value("GuaranteeDaysMin");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Bill of Materials. Bill of Materials
	 */
	public void setIsBOM(boolean IsBOM) {
		set_Value("IsBOM", new Boolean(IsBOM));
	}

	/**
	 * Get Bill of Materials. Bill of Materials
	 */
	public boolean isBOM() {
		Object oo = get_Value("IsBOM");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Drop Shipment. Drop Shipments are sent from the Vendor directly to
	 * the Customer
	 */
	public void setIsDropShip(boolean IsDropShip) {
		set_Value("IsDropShip", new Boolean(IsDropShip));
	}

	/**
	 * Get Drop Shipment. Drop Shipments are sent from the Vendor directly to
	 * the Customer
	 */
	public boolean isDropShip() {
		Object oo = get_Value("IsDropShip");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Exclude Auto Delivery. Exclude from automatic Delivery
	 */
	public void setIsExcludeAutoDelivery(boolean IsExcludeAutoDelivery) {
		set_Value("IsExcludeAutoDelivery", new Boolean(IsExcludeAutoDelivery));
	}

	/**
	 * Get Exclude Auto Delivery. Exclude from automatic Delivery
	 */
	public boolean isExcludeAutoDelivery() {
		Object oo = get_Value("IsExcludeAutoDelivery");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Print detail records on invoice . Print detail BOM elements on the
	 * invoice
	 */
	public void setIsInvoicePrintDetails(boolean IsInvoicePrintDetails) {
		set_Value("IsInvoicePrintDetails", new Boolean(IsInvoicePrintDetails));
	}

	/**
	 * Get Print detail records on invoice . Print detail BOM elements on the
	 * invoice
	 */
	public boolean isInvoicePrintDetails() {
		Object oo = get_Value("IsInvoicePrintDetails");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Print detail records on pick list. Print detail BOM elements on the
	 * pick list
	 */
	public void setIsPickListPrintDetails(boolean IsPickListPrintDetails) {
		set_Value("IsPickListPrintDetails", new Boolean(IsPickListPrintDetails));
	}

	/**
	 * Get Print detail records on pick list. Print detail BOM elements on the
	 * pick list
	 */
	public boolean isPickListPrintDetails() {
		Object oo = get_Value("IsPickListPrintDetails");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Purchased. Organization purchases this product
	 */
	public void setIsPurchased(boolean IsPurchased) {
		set_Value("IsPurchased", new Boolean(IsPurchased));
	}

	/**
	 * Get Purchased. Organization purchases this product
	 */
	public boolean isPurchased() {
		Object oo = get_Value("IsPurchased");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Self-Service. This is a Self-Service entry or this entry can be
	 * changed via Self-Service
	 */
	public void setIsSelfService(boolean IsSelfService) {
		set_Value("IsSelfService", new Boolean(IsSelfService));
	}

	/**
	 * Get Self-Service. This is a Self-Service entry or this entry can be
	 * changed via Self-Service
	 */
	public boolean isSelfService() {
		Object oo = get_Value("IsSelfService");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Sold. Organization sells this product
	 */
	public void setIsSold(boolean IsSold) {
		set_Value("IsSold", new Boolean(IsSold));
	}

	/**
	 * Get Sold. Organization sells this product
	 */
	public boolean isSold() {
		Object oo = get_Value("IsSold");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Stocked. Organization stocks this product
	 */
	public void setIsStocked(boolean IsStocked) {
		set_Value("IsStocked", new Boolean(IsStocked));
	}

	/**
	 * Get Stocked. Organization stocks this product
	 */
	public boolean isStocked() {
		Object oo = get_Value("IsStocked");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Summary Level. This is a summary entity
	 */
	public void setIsSummary(boolean IsSummary) {
		set_Value("IsSummary", new Boolean(IsSummary));
	}

	/**
	 * Get Summary Level. This is a summary entity
	 */
	public boolean isSummary() {
		Object oo = get_Value("IsSummary");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsToFormule */
	public void setIsToFormule(boolean IsToFormule) {
		set_Value("IsToFormule", new Boolean(IsToFormule));
	}

	/** Get IsToFormule */
	public boolean isToFormule() {
		Object oo = get_Value("IsToFormule");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Verified. The BOM configuration has been verified
	 */
	public void setIsVerified(boolean IsVerified) {
		set_ValueNoCheck("IsVerified", new Boolean(IsVerified));
	}

	/**
	 * Get Verified. The BOM configuration has been verified
	 */
	public boolean isVerified() {
		Object oo = get_Value("IsVerified");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Featured in Web Store. If selected, the product is displayed in the
	 * inital or any empy search
	 */
	public void setIsWebStoreFeatured(boolean IsWebStoreFeatured) {
		set_Value("IsWebStoreFeatured", new Boolean(IsWebStoreFeatured));
	}

	/**
	 * Get Featured in Web Store. If selected, the product is displayed in the
	 * inital or any empy search
	 */
	public boolean isWebStoreFeatured() {
		Object oo = get_Value("IsWebStoreFeatured");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Low Level */
	public void setLowLevel(int LowLevel) {
		set_ValueNoCheck("LowLevel", new Integer(LowLevel));
	}

	/** Get Low Level */
	public int getLowLevel() {
		Integer ii = (Integer) get_Value("LowLevel");
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

	/**
	 * Set Attribute Set. Product Attribute Set
	 */
	public void setM_AttributeSet_ID(int M_AttributeSet_ID) {
		if (M_AttributeSet_ID <= 0)
			set_Value("M_AttributeSet_ID", null);
		else
			set_Value("M_AttributeSet_ID", new Integer(M_AttributeSet_ID));
	}

	/**
	 * Get Attribute Set. Product Attribute Set
	 */
	public int getM_AttributeSet_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSet_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Freight Category. Category of the Freight
	 */
	public void setM_FreightCategory_ID(int M_FreightCategory_ID) {
		if (M_FreightCategory_ID <= 0)
			set_Value("M_FreightCategory_ID", null);
		else
			set_Value("M_FreightCategory_ID", new Integer(M_FreightCategory_ID));
	}

	/**
	 * Get Freight Category. Category of the Freight
	 */
	public int getM_FreightCategory_ID() {
		Integer ii = (Integer) get_Value("M_FreightCategory_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Locator. Warehouse Locator
	 */
	public void setM_Locator_ID(int M_Locator_ID) {
		if (M_Locator_ID <= 0)
			set_Value("M_Locator_ID", null);
		else
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

	/** M_PRODUCT_CORTANTE_ID AD_Reference_ID=1000056 */
	public static final int M_PRODUCT_CORTANTE_ID_AD_Reference_ID = 1000056;

	/** Set M_PRODUCT_CORTANTE_ID */
	public void setM_PRODUCT_CORTANTE_ID(int M_PRODUCT_CORTANTE_ID) {
		if (M_PRODUCT_CORTANTE_ID <= 0)
			set_Value("M_PRODUCT_CORTANTE_ID", null);
		else
			set_Value("M_PRODUCT_CORTANTE_ID", new Integer(
					M_PRODUCT_CORTANTE_ID));
	}

	/** Get M_PRODUCT_CORTANTE_ID */
	public int getM_PRODUCT_CORTANTE_ID() {
		Integer ii = (Integer) get_Value("M_PRODUCT_CORTANTE_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** M_PRODUCT_FAMILY_ID AD_Reference_ID=1000047 */
	public static final int M_PRODUCT_FAMILY_ID_AD_Reference_ID = 1000047;

	/**
	 * Set M_PRODUCT_FAMILY_ID. M_PRODUCT_FAMILY_ID
	 */
	public void setM_PRODUCT_FAMILY_ID(int M_PRODUCT_FAMILY_ID) {
		if (M_PRODUCT_FAMILY_ID <= 0)
			set_Value("M_PRODUCT_FAMILY_ID", null);
		else
			set_Value("M_PRODUCT_FAMILY_ID", new Integer(M_PRODUCT_FAMILY_ID));
	}

	/**
	 * Get M_PRODUCT_FAMILY_ID. M_PRODUCT_FAMILY_ID
	 */
	public int getM_PRODUCT_FAMILY_ID() {
		Integer ii = (Integer) get_Value("M_PRODUCT_FAMILY_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** M_PRODUCT_MANLEVEL_ID AD_Reference_ID=1000048 */
	public static final int M_PRODUCT_MANLEVEL_ID_AD_Reference_ID = 1000048;

	/** Set M_PRODUCT_MANLEVEL_ID */
	public void setM_PRODUCT_MANLEVEL_ID(int M_PRODUCT_MANLEVEL_ID) {
		if (M_PRODUCT_MANLEVEL_ID <= 0)
			set_Value("M_PRODUCT_MANLEVEL_ID", null);
		else
			set_Value("M_PRODUCT_MANLEVEL_ID", new Integer(
					M_PRODUCT_MANLEVEL_ID));
	}

	/** Get M_PRODUCT_MANLEVEL_ID */
	public int getM_PRODUCT_MANLEVEL_ID() {
		Integer ii = (Integer) get_Value("M_PRODUCT_MANLEVEL_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** M_PRODUCT_MARKET_ID AD_Reference_ID=1000057 */
	public static final int M_PRODUCT_MARKET_ID_AD_Reference_ID = 1000057;

	/** Set M_PRODUCT_MARKET_ID */
	public void setM_PRODUCT_MARKET_ID(int M_PRODUCT_MARKET_ID) {
		if (M_PRODUCT_MARKET_ID <= 0)
			set_Value("M_PRODUCT_MARKET_ID", null);
		else
			set_Value("M_PRODUCT_MARKET_ID", new Integer(M_PRODUCT_MARKET_ID));
	}

	/** Get M_PRODUCT_MARKET_ID */
	public int getM_PRODUCT_MARKET_ID() {
		Integer ii = (Integer) get_Value("M_PRODUCT_MARKET_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** M_PRODUCT_MARK_ID AD_Reference_ID=1000049 */
	public static final int M_PRODUCT_MARK_ID_AD_Reference_ID = 1000049;

	/** Set M_PRODUCT_MARK_ID */
	public void setM_PRODUCT_MARK_ID(int M_PRODUCT_MARK_ID) {
		if (M_PRODUCT_MARK_ID <= 0)
			set_Value("M_PRODUCT_MARK_ID", null);
		else
			set_Value("M_PRODUCT_MARK_ID", new Integer(M_PRODUCT_MARK_ID));
	}

	/** Get M_PRODUCT_MARK_ID */
	public int getM_PRODUCT_MARK_ID() {
		Integer ii = (Integer) get_Value("M_PRODUCT_MARK_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** M_PRODUCT_PRESENTATION_ID AD_Reference_ID=1000050 */
	public static final int M_PRODUCT_PRESENTATION_ID_AD_Reference_ID = 1000050;

	/** Set M_PRODUCT_PRESENTATION_ID */
	public void setM_PRODUCT_PRESENTATION_ID(int M_PRODUCT_PRESENTATION_ID) {
		if (M_PRODUCT_PRESENTATION_ID <= 0)
			set_Value("M_PRODUCT_PRESENTATION_ID", null);
		else
			set_Value("M_PRODUCT_PRESENTATION_ID", new Integer(
					M_PRODUCT_PRESENTATION_ID));
	}

	/** Get M_PRODUCT_PRESENTATION_ID */
	public int getM_PRODUCT_PRESENTATION_ID() {
		Integer ii = (Integer) get_Value("M_PRODUCT_PRESENTATION_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** M_PRODUCT_SPECIALTIES_ID AD_Reference_ID=1000051 */
	public static final int M_PRODUCT_SPECIALTIES_ID_AD_Reference_ID = 1000051;

	/**
	 * Set M_PRODUCT_SPECIALTIES_ID. M_PRODUCT_SPECIALTIES_ID
	 */
	public void setM_PRODUCT_SPECIALTIES_ID(int M_PRODUCT_SPECIALTIES_ID) {
		if (M_PRODUCT_SPECIALTIES_ID <= 0)
			set_Value("M_PRODUCT_SPECIALTIES_ID", null);
		else
			set_Value("M_PRODUCT_SPECIALTIES_ID", new Integer(
					M_PRODUCT_SPECIALTIES_ID));
	}

	/**
	 * Get M_PRODUCT_SPECIALTIES_ID. M_PRODUCT_SPECIALTIES_ID
	 */
	public int getM_PRODUCT_SPECIALTIES_ID() {
		Integer ii = (Integer) get_Value("M_PRODUCT_SPECIALTIES_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** M_Product_Category_ID AD_Reference_ID=163 */
	public static final int M_PRODUCT_CATEGORY_ID_AD_Reference_ID = 163;

	/**
	 * Set Product Category. Category of a Product
	 */
	public void setM_Product_Category_ID(int M_Product_Category_ID) {
		if (M_Product_Category_ID < 1)
			throw new IllegalArgumentException(
					"M_Product_Category_ID is mandatory.");
		set_Value("M_Product_Category_ID", new Integer(M_Product_Category_ID));
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
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 60) {
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
		if (ProductType == null)
			throw new IllegalArgumentException("ProductType is mandatory");
		if (ProductType.equals("E") || ProductType.equals("I")
				|| ProductType.equals("O") || ProductType.equals("R")
				|| ProductType.equals("S"))
			;
		else
			throw new IllegalArgumentException("ProductType Invalid value - "
					+ ProductType + " - Reference_ID=270 - E - I - O - R - S");
		if (ProductType.length() > 1) {
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
	 * Set Mail Template. Text templates for mailings
	 */
	public void setR_MailText_ID(int R_MailText_ID) {
		if (R_MailText_ID <= 0)
			set_Value("R_MailText_ID", null);
		else
			set_Value("R_MailText_ID", new Integer(R_MailText_ID));
	}

	/**
	 * Get Mail Template. Text templates for mailings
	 */
	public int getR_MailText_ID() {
		Integer ii = (Integer) get_Value("R_MailText_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Expense Type. Expense report type
	 */
	public void setS_ExpenseType_ID(int S_ExpenseType_ID) {
		if (S_ExpenseType_ID <= 0)
			set_ValueNoCheck("S_ExpenseType_ID", null);
		else
			set_ValueNoCheck("S_ExpenseType_ID", new Integer(S_ExpenseType_ID));
	}

	/**
	 * Get Expense Type. Expense report type
	 */
	public int getS_ExpenseType_ID() {
		Integer ii = (Integer) get_Value("S_ExpenseType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Resource. Resource
	 */
	public void setS_Resource_ID(int S_Resource_ID) {
		if (S_Resource_ID <= 0)
			set_ValueNoCheck("S_Resource_ID", null);
		else
			set_ValueNoCheck("S_Resource_ID", new Integer(S_Resource_ID));
	}

	/**
	 * Get Resource. Resource
	 */
	public int getS_Resource_ID() {
		Integer ii = (Integer) get_Value("S_Resource_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** SalesRep_ID AD_Reference_ID=190 */
	public static final int SALESREP_ID_AD_Reference_ID = 190;

	/**
	 * Set Sales Representative. Sales Representative or Company Agent
	 */
	public void setSalesRep_ID(int SalesRep_ID) {
		if (SalesRep_ID <= 0)
			set_Value("SalesRep_ID", null);
		else
			set_Value("SalesRep_ID", new Integer(SalesRep_ID));
	}

	/**
	 * Get Sales Representative. Sales Representative or Company Agent
	 */
	public int getSalesRep_ID() {
		Integer ii = (Integer) get_Value("SalesRep_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 40) {
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
	 * Set Version No. Version Number
	 */
	public void setVersionNo(String VersionNo) {
		if (VersionNo != null && VersionNo.length() > 20) {
			log.warning("Length > 20 - truncated");
			VersionNo = VersionNo.substring(0, 19);
		}
		set_Value("VersionNo", VersionNo);
	}

	/**
	 * Get Version No. Version Number
	 */
	public String getVersionNo() {
		return (String) get_Value("VersionNo");
	}

	/**
	 * Set Volume. Volume of a product
	 */
	public void setVolume(BigDecimal Volume) {
		set_Value("Volume", Volume);
	}

	/**
	 * Get Volume. Volume of a product
	 */
	public BigDecimal getVolume() {
		BigDecimal bd = (BigDecimal) get_Value("Volume");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Weight. Weight of a product
	 */
	public void setWeight(BigDecimal Weight) {
		set_Value("Weight", Weight);
	}

	/**
	 * Get Weight. Weight of a product
	 */
	public BigDecimal getWeight() {
		BigDecimal bd = (BigDecimal) get_Value("Weight");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
