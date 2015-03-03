/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for A_Asset
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.062
 */
public class X_A_Asset extends PO {
	/** Standard Constructor */
	public X_A_Asset(Properties ctx, int A_Asset_ID, String trxName) {
		super(ctx, A_Asset_ID, trxName);
		/**
		 * if (A_Asset_ID == 0) { setA_Asset_Group_ID (0); setA_Asset_ID (0);
		 * setIsDepreciated (false); setIsDisposed (false);
		 * setIsFullyDepreciated (false); // N setIsInPosession (false);
		 * setIsOwned (false); setM_AttributeSetInstance_ID (0); setName (null);
		 * setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_A_Asset(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=A_Asset */
	public static final String Table_Name = "A_Asset";

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
		StringBuffer sb = new StringBuffer("X_A_Asset[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID <= 0)
			set_Value("AD_User_ID", null);
		else
			set_Value("AD_User_ID", new Integer(AD_User_ID));
	}

	/**
	 * Get User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public int getAD_User_ID() {
		Integer ii = (Integer) get_Value("AD_User_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Asset Group. Group of Assets
	 */
	public void setA_Asset_Group_ID(int A_Asset_Group_ID) {
		if (A_Asset_Group_ID < 1)
			throw new IllegalArgumentException("A_Asset_Group_ID is mandatory.");
		set_Value("A_Asset_Group_ID", new Integer(A_Asset_Group_ID));
	}

	/**
	 * Get Asset Group. Group of Assets
	 */
	public int getA_Asset_Group_ID() {
		Integer ii = (Integer) get_Value("A_Asset_Group_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Asset. Asset used internally or by customers
	 */
	public void setA_Asset_ID(int A_Asset_ID) {
		if (A_Asset_ID < 1)
			throw new IllegalArgumentException("A_Asset_ID is mandatory.");
		set_ValueNoCheck("A_Asset_ID", new Integer(A_Asset_ID));
	}

	/**
	 * Get Asset. Asset used internally or by customers
	 */
	public int getA_Asset_ID() {
		Integer ii = (Integer) get_Value("A_Asset_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Asset Depreciation Date. Date of last depreciation
	 */
	public void setAssetDepreciationDate(Timestamp AssetDepreciationDate) {
		set_Value("AssetDepreciationDate", AssetDepreciationDate);
	}

	/**
	 * Get Asset Depreciation Date. Date of last depreciation
	 */
	public Timestamp getAssetDepreciationDate() {
		return (Timestamp) get_Value("AssetDepreciationDate");
	}

	/**
	 * Set Asset Disposal Date. Date when the asset is/was disposed
	 */
	public void setAssetDisposalDate(Timestamp AssetDisposalDate) {
		set_Value("AssetDisposalDate", AssetDisposalDate);
	}

	/**
	 * Get Asset Disposal Date. Date when the asset is/was disposed
	 */
	public Timestamp getAssetDisposalDate() {
		return (Timestamp) get_Value("AssetDisposalDate");
	}

	/**
	 * Set In Service Date. Date when Asset was put into service
	 */
	public void setAssetServiceDate(Timestamp AssetServiceDate) {
		set_Value("AssetServiceDate", AssetServiceDate);
	}

	/**
	 * Get In Service Date. Date when Asset was put into service
	 */
	public Timestamp getAssetServiceDate() {
		return (Timestamp) get_Value("AssetServiceDate");
	}

	/** C_BPartnerSR_ID AD_Reference_ID=353 */
	public static final int C_BPARTNERSR_ID_AD_Reference_ID = 353;

	/**
	 * Set BPartner (Agent). Business Partner (Agent or Sales Rep)
	 */
	public void setC_BPartnerSR_ID(int C_BPartnerSR_ID) {
		if (C_BPartnerSR_ID <= 0)
			set_Value("C_BPartnerSR_ID", null);
		else
			set_Value("C_BPartnerSR_ID", new Integer(C_BPartnerSR_ID));
	}

	/**
	 * Get BPartner (Agent). Business Partner (Agent or Sales Rep)
	 */
	public int getC_BPartnerSR_ID() {
		Integer ii = (Integer) get_Value("C_BPartnerSR_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public void setC_BPartner_Location_ID(int C_BPartner_Location_ID) {
		if (C_BPartner_Location_ID <= 0)
			set_Value("C_BPartner_Location_ID", null);
		else
			set_Value("C_BPartner_Location_ID", new Integer(
					C_BPartner_Location_ID));
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
	 * Set Address. Location or Address
	 */
	public void setC_Location_ID(int C_Location_ID) {
		if (C_Location_ID <= 0)
			set_Value("C_Location_ID", null);
		else
			set_Value("C_Location_ID", new Integer(C_Location_ID));
	}

	/**
	 * Get Address. Location or Address
	 */
	public int getC_Location_ID() {
		Integer ii = (Integer) get_Value("C_Location_ID");
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
	 * Set Guarantee Date. Date when guarantee expires
	 */
	public void setGuaranteeDate(Timestamp GuaranteeDate) {
		set_Value("GuaranteeDate", GuaranteeDate);
	}

	/**
	 * Get Guarantee Date. Date when guarantee expires
	 */
	public Timestamp getGuaranteeDate() {
		return (Timestamp) get_Value("GuaranteeDate");
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
	 * Set Depreciate. The asset will be depreciated
	 */
	public void setIsDepreciated(boolean IsDepreciated) {
		set_Value("IsDepreciated", new Boolean(IsDepreciated));
	}

	/**
	 * Get Depreciate. The asset will be depreciated
	 */
	public boolean isDepreciated() {
		Object oo = get_Value("IsDepreciated");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Disposed. The asset is disposed
	 */
	public void setIsDisposed(boolean IsDisposed) {
		set_Value("IsDisposed", new Boolean(IsDisposed));
	}

	/**
	 * Get Disposed. The asset is disposed
	 */
	public boolean isDisposed() {
		Object oo = get_Value("IsDisposed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Fully depreciated. The asset is fully depreciated
	 */
	public void setIsFullyDepreciated(boolean IsFullyDepreciated) {
		set_ValueNoCheck("IsFullyDepreciated", new Boolean(IsFullyDepreciated));
	}

	/**
	 * Get Fully depreciated. The asset is fully depreciated
	 */
	public boolean isFullyDepreciated() {
		Object oo = get_Value("IsFullyDepreciated");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set In Possession. The asset is in the possession of the organization
	 */
	public void setIsInPosession(boolean IsInPosession) {
		set_Value("IsInPosession", new Boolean(IsInPosession));
	}

	/**
	 * Get In Possession. The asset is in the possession of the organization
	 */
	public boolean isInPosession() {
		Object oo = get_Value("IsInPosession");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Owned. The asset is owned by the organization
	 */
	public void setIsOwned(boolean IsOwned) {
		set_Value("IsOwned", new Boolean(IsOwned));
	}

	/**
	 * Get Owned. The asset is owned by the organization
	 */
	public boolean isOwned() {
		Object oo = get_Value("IsOwned");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Last Maintenance. Last Maintenance Date
	 */
	public void setLastMaintenanceDate(Timestamp LastMaintenanceDate) {
		set_Value("LastMaintenanceDate", LastMaintenanceDate);
	}

	/**
	 * Get Last Maintenance. Last Maintenance Date
	 */
	public Timestamp getLastMaintenanceDate() {
		return (Timestamp) get_Value("LastMaintenanceDate");
	}

	/**
	 * Set Last Note. Last Maintenance Note
	 */
	public void setLastMaintenanceNote(String LastMaintenanceNote) {
		if (LastMaintenanceNote != null && LastMaintenanceNote.length() > 60) {
			log.warning("Length > 60 - truncated");
			LastMaintenanceNote = LastMaintenanceNote.substring(0, 59);
		}
		set_Value("LastMaintenanceNote", LastMaintenanceNote);
	}

	/**
	 * Get Last Note. Last Maintenance Note
	 */
	public String getLastMaintenanceNote() {
		return (String) get_Value("LastMaintenanceNote");
	}

	/**
	 * Set Last Unit. Last Maintenance Unit
	 */
	public void setLastMaintenanceUnit(int LastMaintenanceUnit) {
		set_Value("LastMaintenanceUnit", new Integer(LastMaintenanceUnit));
	}

	/**
	 * Get Last Unit. Last Maintenance Unit
	 */
	public int getLastMaintenanceUnit() {
		Integer ii = (Integer) get_Value("LastMaintenanceUnit");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Lease Termination. Lease Termination Date
	 */
	public void setLeaseTerminationDate(Timestamp LeaseTerminationDate) {
		set_Value("LeaseTerminationDate", LeaseTerminationDate);
	}

	/**
	 * Get Lease Termination. Lease Termination Date
	 */
	public Timestamp getLeaseTerminationDate() {
		return (Timestamp) get_Value("LeaseTerminationDate");
	}

	/** Lease_BPartner_ID AD_Reference_ID=192 */
	public static final int LEASE_BPARTNER_ID_AD_Reference_ID = 192;

	/**
	 * Set Lessor. The Business Partner who rents or leases
	 */
	public void setLease_BPartner_ID(int Lease_BPartner_ID) {
		if (Lease_BPartner_ID <= 0)
			set_Value("Lease_BPartner_ID", null);
		else
			set_Value("Lease_BPartner_ID", new Integer(Lease_BPartner_ID));
	}

	/**
	 * Get Lessor. The Business Partner who rents or leases
	 */
	public int getLease_BPartner_ID() {
		Integer ii = (Integer) get_Value("Lease_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Life use. Units of use until the asset is not usable anymore
	 */
	public void setLifeUseUnits(int LifeUseUnits) {
		set_Value("LifeUseUnits", new Integer(LifeUseUnits));
	}

	/**
	 * Get Life use. Units of use until the asset is not usable anymore
	 */
	public int getLifeUseUnits() {
		Integer ii = (Integer) get_Value("LifeUseUnits");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Location comment. Additional comments or remarks concerning the
	 * location
	 */
	public void setLocationComment(String LocationComment) {
		if (LocationComment != null && LocationComment.length() > 255) {
			log.warning("Length > 255 - truncated");
			LocationComment = LocationComment.substring(0, 254);
		}
		set_Value("LocationComment", LocationComment);
	}

	/**
	 * Get Location comment. Additional comments or remarks concerning the
	 * location
	 */
	public String getLocationComment() {
		return (String) get_Value("LocationComment");
	}

	/**
	 * Set Lot No. Lot number (alphanumeric)
	 */
	public void setLot(String Lot) {
		if (Lot != null && Lot.length() > 255) {
			log.warning("Length > 255 - truncated");
			Lot = Lot.substring(0, 254);
		}
		set_Value("Lot", Lot);
	}

	/**
	 * Get Lot No. Lot number (alphanumeric)
	 */
	public String getLot() {
		return (String) get_Value("Lot");
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

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID <= 0)
			set_ValueNoCheck("M_Product_ID", null);
		else
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
	}

	/**
	 * Set Next Maintenence. Next Maintenence Date
	 */
	public void setNextMaintenenceDate(Timestamp NextMaintenenceDate) {
		set_Value("NextMaintenenceDate", NextMaintenenceDate);
	}

	/**
	 * Get Next Maintenence. Next Maintenence Date
	 */
	public Timestamp getNextMaintenenceDate() {
		return (Timestamp) get_Value("NextMaintenenceDate");
	}

	/**
	 * Set Next Unit. Next Maintenence Unit
	 */
	public void setNextMaintenenceUnit(int NextMaintenenceUnit) {
		set_Value("NextMaintenenceUnit", new Integer(NextMaintenenceUnit));
	}

	/**
	 * Get Next Unit. Next Maintenence Unit
	 */
	public int getNextMaintenenceUnit() {
		Integer ii = (Integer) get_Value("NextMaintenenceUnit");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Quantity. Quantity
	 */
	public void setQty(BigDecimal Qty) {
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

	/**
	 * Set Serial No. Product Serial Number
	 */
	public void setSerNo(String SerNo) {
		if (SerNo != null && SerNo.length() > 255) {
			log.warning("Length > 255 - truncated");
			SerNo = SerNo.substring(0, 254);
		}
		set_Value("SerNo", SerNo);
	}

	/**
	 * Get Serial No. Product Serial Number
	 */
	public String getSerNo() {
		return (String) get_Value("SerNo");
	}

	/**
	 * Set Usable Life - Months. Months of the usable life of the asset
	 */
	public void setUseLifeMonths(int UseLifeMonths) {
		set_Value("UseLifeMonths", new Integer(UseLifeMonths));
	}

	/**
	 * Get Usable Life - Months. Months of the usable life of the asset
	 */
	public int getUseLifeMonths() {
		Integer ii = (Integer) get_Value("UseLifeMonths");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Usable Life - Years. Years of the usable life of the asset
	 */
	public void setUseLifeYears(int UseLifeYears) {
		set_Value("UseLifeYears", new Integer(UseLifeYears));
	}

	/**
	 * Get Usable Life - Years. Years of the usable life of the asset
	 */
	public int getUseLifeYears() {
		Integer ii = (Integer) get_Value("UseLifeYears");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Use units. Currently used units of the assets
	 */
	public void setUseUnits(int UseUnits) {
		set_ValueNoCheck("UseUnits", new Integer(UseUnits));
	}

	/**
	 * Get Use units. Currently used units of the assets
	 */
	public int getUseUnits() {
		Integer ii = (Integer) get_Value("UseUnits");
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
}
