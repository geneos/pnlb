/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Warehouse
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.781
 */
public class X_M_Warehouse extends PO {
	/** Standard Constructor */
	public X_M_Warehouse(Properties ctx, int M_Warehouse_ID, String trxName) {
		super(ctx, M_Warehouse_ID, trxName);
		/**
		 * if (M_Warehouse_ID == 0) { setC_Location_ID (0); setM_Warehouse_ID
		 * (0); setName (null); setSeparator (null); // * setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_M_Warehouse(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Warehouse */
	public static final String Table_Name = "M_Warehouse";

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
		StringBuffer sb = new StringBuffer("X_M_Warehouse[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Address. Location or Address
	 */
	public void setC_Location_ID(int C_Location_ID) {
		if (C_Location_ID < 1)
			throw new IllegalArgumentException("C_Location_ID is mandatory.");
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

	/** Set ISPRODUCTIONISSUE */
	public void setISPRODUCTIONISSUE(boolean ISPRODUCTIONISSUE) {
		set_Value("ISPRODUCTIONISSUE", new Boolean(ISPRODUCTIONISSUE));
	}

	/** Get ISPRODUCTIONISSUE */
	public boolean isPRODUCTIONISSUE() {
		Object oo = get_Value("ISPRODUCTIONISSUE");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
        
        /** Set ISRELEASE */
	public void setISRELEASE(boolean ISRELEASE) {
		set_Value("ISRELEASE", new Boolean(ISRELEASE));
	}

	/** Get ISRELEASE */
	public boolean isRELEASE() {
		Object oo = get_Value("ISRELEASE");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IS_REJECTION */
	public void setIS_REJECTION(boolean IS_REJECTION) {
		set_Value("IS_REJECTION", new Boolean(IS_REJECTION));
	}

	/** Get IS_REJECTION */
	public boolean is_REJECTION() {
		Object oo = get_Value("IS_REJECTION");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Is Required MRP */
	public void setIsRequiredMRP(boolean IsRequiredMRP) {
		set_Value("IsRequiredMRP", new Boolean(IsRequiredMRP));
	}

	/** Get Is Required MRP */
	public boolean isRequiredMRP() {
		Object oo = get_Value("IsRequiredMRP");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
	 * Set Replenishment Class. Custom class to calculate Quantity to Order
	 */
	public void setReplenishmentClass(String ReplenishmentClass) {
		if (ReplenishmentClass != null && ReplenishmentClass.length() > 60) {
			log.warning("Length > 60 - truncated");
			ReplenishmentClass = ReplenishmentClass.substring(0, 59);
		}
		set_Value("ReplenishmentClass", ReplenishmentClass);
	}

	/**
	 * Get Replenishment Class. Custom class to calculate Quantity to Order
	 */
	public String getReplenishmentClass() {
		return (String) get_Value("ReplenishmentClass");
	}

	/**
	 * Set Element Separator. Element Separator
	 */
	public void setSeparator(String Separator) {
		if (Separator == null)
			throw new IllegalArgumentException("Separator is mandatory.");
		if (Separator.length() > 1) {
			log.warning("Length > 1 - truncated");
			Separator = Separator.substring(0, 0);
		}
		set_Value("Separator", Separator);
	}

	/**
	 * Get Element Separator. Element Separator
	 */
	public String getSeparator() {
		return (String) get_Value("Separator");
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
}
