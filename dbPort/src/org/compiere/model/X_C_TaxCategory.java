/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_TaxCategory
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.656
 */
public class X_C_TaxCategory extends PO {
	/** Standard Constructor */
	public X_C_TaxCategory(Properties ctx, int C_TaxCategory_ID, String trxName) {
		super(ctx, C_TaxCategory_ID, trxName);
		/**
		 * if (C_TaxCategory_ID == 0) { setC_TaxCategory_ID (0); setIsDefault
		 * (false); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_C_TaxCategory(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_TaxCategory */
	public static final String Table_Name = "C_TaxCategory";

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
		StringBuffer sb = new StringBuffer("X_C_TaxCategory[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Tax Category. Tax Category
	 */
	public void setC_TaxCategory_ID(int C_TaxCategory_ID) {
		if (C_TaxCategory_ID < 1)
			throw new IllegalArgumentException("C_TaxCategory_ID is mandatory.");
		set_ValueNoCheck("C_TaxCategory_ID", new Integer(C_TaxCategory_ID));
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
	 * Set Commodity Code. Commodity code used for tax calculation
	 */
	public void setCommodityCode(String CommodityCode) {
		if (CommodityCode != null && CommodityCode.length() > 20) {
			log.warning("Length > 20 - truncated");
			CommodityCode = CommodityCode.substring(0, 19);
		}
		set_Value("CommodityCode", CommodityCode);
	}

	/**
	 * Get Commodity Code. Commodity code used for tax calculation
	 */
	public String getCommodityCode() {
		return (String) get_Value("CommodityCode");
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
	 * Set Default. Default value
	 */
	public void setIsDefault(boolean IsDefault) {
		set_Value("IsDefault", new Boolean(IsDefault));
	}

	/**
	 * Get Default. Default value
	 */
	public boolean isDefault() {
		Object oo = get_Value("IsDefault");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
}
