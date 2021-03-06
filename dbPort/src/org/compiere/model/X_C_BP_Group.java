/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BP_Group
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.656
 */
public class X_C_BP_Group extends PO {
	/** Standard Constructor */
	public X_C_BP_Group(Properties ctx, int C_BP_Group_ID, String trxName) {
		super(ctx, C_BP_Group_ID, trxName);
		/**
		 * if (C_BP_Group_ID == 0) { setC_BP_Group_ID (0); setIsConfidentialInfo
		 * (false); // N setIsDefault (false); setName (null); setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_C_BP_Group(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_BP_Group */
	public static final String Table_Name = "C_BP_Group";

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
		StringBuffer sb = new StringBuffer("X_C_BP_Group[").append(get_ID())
				.append("]");
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
	 * Set Business Partner Group. Business Partner Group
	 */
	public void setC_BP_Group_ID(int C_BP_Group_ID) {
		if (C_BP_Group_ID < 1)
			throw new IllegalArgumentException("C_BP_Group_ID is mandatory.");
		set_ValueNoCheck("C_BP_Group_ID", new Integer(C_BP_Group_ID));
	}

	/**
	 * Get Business Partner Group. Business Partner Group
	 */
	public int getC_BP_Group_ID() {
		Integer ii = (Integer) get_Value("C_BP_Group_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Credit Watch %. Credit Watch - Percent of Credit Limit when OK
	 * switches to Watch
	 */
	public void setCreditWatchPercent(BigDecimal CreditWatchPercent) {
		set_Value("CreditWatchPercent", CreditWatchPercent);
	}

	/**
	 * Get Credit Watch %. Credit Watch - Percent of Credit Limit when OK
	 * switches to Watch
	 */
	public BigDecimal getCreditWatchPercent() {
		BigDecimal bd = (BigDecimal) get_Value("CreditWatchPercent");
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
	 * Set Confidential Info. Can enter confidential information
	 */
	public void setIsConfidentialInfo(boolean IsConfidentialInfo) {
		set_Value("IsConfidentialInfo", new Boolean(IsConfidentialInfo));
	}

	/**
	 * Get Confidential Info. Can enter confidential information
	 */
	public boolean isConfidentialInfo() {
		Object oo = get_Value("IsConfidentialInfo");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/** M_DiscountSchema_ID AD_Reference_ID=325 */
	public static final int M_DISCOUNTSCHEMA_ID_AD_Reference_ID = 325;

	/**
	 * Set Discount Schema. Schema to calculate the trade discount percentage
	 */
	public void setM_DiscountSchema_ID(int M_DiscountSchema_ID) {
		if (M_DiscountSchema_ID <= 0)
			set_Value("M_DiscountSchema_ID", null);
		else
			set_Value("M_DiscountSchema_ID", new Integer(M_DiscountSchema_ID));
	}

	/**
	 * Get Discount Schema. Schema to calculate the trade discount percentage
	 */
	public int getM_DiscountSchema_ID() {
		Integer ii = (Integer) get_Value("M_DiscountSchema_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Price List. Unique identifier of a Price List
	 */
	public void setM_PriceList_ID(int M_PriceList_ID) {
		if (M_PriceList_ID <= 0)
			set_Value("M_PriceList_ID", null);
		else
			set_Value("M_PriceList_ID", new Integer(M_PriceList_ID));
	}

	/**
	 * Get Price List. Unique identifier of a Price List
	 */
	public int getM_PriceList_ID() {
		Integer ii = (Integer) get_Value("M_PriceList_ID");
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

	/** PO_DiscountSchema_ID AD_Reference_ID=325 */
	public static final int PO_DISCOUNTSCHEMA_ID_AD_Reference_ID = 325;

	/**
	 * Set PO Discount Schema. Schema to calculate the purchase trade discount
	 * percentage
	 */
	public void setPO_DiscountSchema_ID(int PO_DiscountSchema_ID) {
		if (PO_DiscountSchema_ID <= 0)
			set_Value("PO_DiscountSchema_ID", null);
		else
			set_Value("PO_DiscountSchema_ID", new Integer(PO_DiscountSchema_ID));
	}

	/**
	 * Get PO Discount Schema. Schema to calculate the purchase trade discount
	 * percentage
	 */
	public int getPO_DiscountSchema_ID() {
		Integer ii = (Integer) get_Value("PO_DiscountSchema_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** PO_PriceList_ID AD_Reference_ID=166 */
	public static final int PO_PRICELIST_ID_AD_Reference_ID = 166;

	/**
	 * Set Purchase Pricelist. Price List used by this Business Partner
	 */
	public void setPO_PriceList_ID(int PO_PriceList_ID) {
		if (PO_PriceList_ID <= 0)
			set_Value("PO_PriceList_ID", null);
		else
			set_Value("PO_PriceList_ID", new Integer(PO_PriceList_ID));
	}

	/**
	 * Get Purchase Pricelist. Price List used by this Business Partner
	 */
	public int getPO_PriceList_ID() {
		Integer ii = (Integer) get_Value("PO_PriceList_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Price Match Tolerance. PO-Invoice Match Price Tolerance in percent of
	 * the purchase price
	 */
	public void setPriceMatchTolerance(BigDecimal PriceMatchTolerance) {
		set_Value("PriceMatchTolerance", PriceMatchTolerance);
	}

	/**
	 * Get Price Match Tolerance. PO-Invoice Match Price Tolerance in percent of
	 * the purchase price
	 */
	public BigDecimal getPriceMatchTolerance() {
		BigDecimal bd = (BigDecimal) get_Value("PriceMatchTolerance");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** PriorityBase AD_Reference_ID=350 */
	public static final int PRIORITYBASE_AD_Reference_ID = 350;

	/** Higher = H */
	public static final String PRIORITYBASE_Higher = "H";

	/** Lower = L */
	public static final String PRIORITYBASE_Lower = "L";

	/** Same = S */
	public static final String PRIORITYBASE_Same = "S";

	/**
	 * Set Priority Base. Base of Priority
	 */
	public void setPriorityBase(String PriorityBase) {
		if (PriorityBase != null && PriorityBase.length() > 1) {
			log.warning("Length > 1 - truncated");
			PriorityBase = PriorityBase.substring(0, 0);
		}
		set_Value("PriorityBase", PriorityBase);
	}

	/**
	 * Get Priority Base. Base of Priority
	 */
	public String getPriorityBase() {
		return (String) get_Value("PriorityBase");
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
