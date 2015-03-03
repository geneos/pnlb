/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Product_Category
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.453
 */
public class X_M_Product_Category extends PO {
	/** Standard Constructor */
	public X_M_Product_Category(Properties ctx, int M_Product_Category_ID,
			String trxName) {
		super(ctx, M_Product_Category_ID, trxName);
		/**
		 * if (M_Product_Category_ID == 0) { setIsDefault (false);
		 * setIsSelfService (true); // Y setMMPolicy (null); // F
		 * setM_Product_Category_ID (0); setName (null); setPlannedMargin
		 * (Env.ZERO); setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_M_Product_Category(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Product_Category */
	public static final String Table_Name = "M_Product_Category";

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
		StringBuffer sb = new StringBuffer("X_M_Product_Category[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Print Color. Color used for printing and display
	 */
	public void setAD_PrintColor_ID(int AD_PrintColor_ID) {
		if (AD_PrintColor_ID <= 0)
			set_Value("AD_PrintColor_ID", null);
		else
			set_Value("AD_PrintColor_ID", new Integer(AD_PrintColor_ID));
	}

	/**
	 * Get Print Color. Color used for printing and display
	 */
	public int getAD_PrintColor_ID() {
		Integer ii = (Integer) get_Value("AD_PrintColor_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Asset Group. Group of Assets
	 */
	public void setA_Asset_Group_ID(int A_Asset_Group_ID) {
		if (A_Asset_Group_ID <= 0)
			set_Value("A_Asset_Group_ID", null);
		else
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
	 * Set isFinal. isFinal value
	 */
	public void setIsFinal(boolean IsFinal) {
		set_Value("ISFINAL", new Boolean(IsFinal));
	}

	/**
	 * Get isFinal. isFinal value
	 */
	public boolean isFinal() {
		Object oo = get_Value("ISFINAL");
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

	/** MMPolicy AD_Reference_ID=335 */
	public static final int MMPOLICY_AD_Reference_ID = 335;

	/** FiFo = F */
	public static final String MMPOLICY_FiFo = "F";

	/** LiFo = L */
	public static final String MMPOLICY_LiFo = "L";

	/**
	 * Set Material Policy. Material Movement Policy
	 */
	public void setMMPolicy(String MMPolicy) {
		if (MMPolicy == null)
			throw new IllegalArgumentException("MMPolicy is mandatory");
		if (MMPolicy.equals("F") || MMPolicy.equals("L"))
			;
		else
			throw new IllegalArgumentException("MMPolicy Invalid value - "
					+ MMPolicy + " - Reference_ID=335 - F - L");
		if (MMPolicy.length() > 1) {
			log.warning("Length > 1 - truncated");
			MMPolicy = MMPolicy.substring(0, 0);
		}
		set_Value("MMPolicy", MMPolicy);
	}

	/**
	 * Get Material Policy. Material Movement Policy
	 */
	public String getMMPolicy() {
		return (String) get_Value("MMPolicy");
	}

	/**
	 * Set Product Category. Category of a Product
	 */
	public void setM_Product_Category_ID(int M_Product_Category_ID) {
		if (M_Product_Category_ID < 1)
			throw new IllegalArgumentException(
					"M_Product_Category_ID is mandatory.");
		set_ValueNoCheck("M_Product_Category_ID", new Integer(
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
	 * Set Planned Margin %. Project's planned margin as a percentage
	 */
	public void setPlannedMargin(BigDecimal PlannedMargin) {
		if (PlannedMargin == null)
			throw new IllegalArgumentException("PlannedMargin is mandatory.");
		set_Value("PlannedMargin", PlannedMargin);
	}

	/**
	 * Get Planned Margin %. Project's planned margin as a percentage
	 */
	public BigDecimal getPlannedMargin() {
		BigDecimal bd = (BigDecimal) get_Value("PlannedMargin");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
