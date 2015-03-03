/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_PrintPaper
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.906
 */
public class X_AD_PrintPaper extends PO {
	/** Standard Constructor */
	public X_AD_PrintPaper(Properties ctx, int AD_PrintPaper_ID, String trxName) {
		super(ctx, AD_PrintPaper_ID, trxName);
		/**
		 * if (AD_PrintPaper_ID == 0) { setAD_PrintPaper_ID (0); setCode (null); //
		 * iso-a4 setIsDefault (false); setIsLandscape (true); // Y
		 * setMarginBottom (0); // 36 setMarginLeft (0); // 36 setMarginRight
		 * (0); // 36 setMarginTop (0); // 36 setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_PrintPaper(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_PrintPaper */
	public static final String Table_Name = "AD_PrintPaper";

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

	protected BigDecimal accessLevel = new BigDecimal(6);

	/** AccessLevel 6 - System - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_PrintPaper[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Print Paper. Printer paper definition
	 */
	public void setAD_PrintPaper_ID(int AD_PrintPaper_ID) {
		if (AD_PrintPaper_ID < 1)
			throw new IllegalArgumentException("AD_PrintPaper_ID is mandatory.");
		set_ValueNoCheck("AD_PrintPaper_ID", new Integer(AD_PrintPaper_ID));
	}

	/**
	 * Get Print Paper. Printer paper definition
	 */
	public int getAD_PrintPaper_ID() {
		Integer ii = (Integer) get_Value("AD_PrintPaper_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Validation code. Validation Code
	 */
	public void setCode(String Code) {
		if (Code == null)
			throw new IllegalArgumentException("Code is mandatory.");
		if (Code.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Code = Code.substring(0, 1999);
		}
		set_Value("Code", Code);
	}

	/**
	 * Get Validation code. Validation Code
	 */
	public String getCode() {
		return (String) get_Value("Code");
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
	 * Set Landscape. Landscape orientation
	 */
	public void setIsLandscape(boolean IsLandscape) {
		set_Value("IsLandscape", new Boolean(IsLandscape));
	}

	/**
	 * Get Landscape. Landscape orientation
	 */
	public boolean isLandscape() {
		Object oo = get_Value("IsLandscape");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Bottom Margin. Bottom Space in 1/72 inch
	 */
	public void setMarginBottom(int MarginBottom) {
		set_Value("MarginBottom", new Integer(MarginBottom));
	}

	/**
	 * Get Bottom Margin. Bottom Space in 1/72 inch
	 */
	public int getMarginBottom() {
		Integer ii = (Integer) get_Value("MarginBottom");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Left Margin. Left Space in 1/72 inch
	 */
	public void setMarginLeft(int MarginLeft) {
		set_Value("MarginLeft", new Integer(MarginLeft));
	}

	/**
	 * Get Left Margin. Left Space in 1/72 inch
	 */
	public int getMarginLeft() {
		Integer ii = (Integer) get_Value("MarginLeft");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Right Margin. Right Space in 1/72 inch
	 */
	public void setMarginRight(int MarginRight) {
		set_Value("MarginRight", new Integer(MarginRight));
	}

	/**
	 * Get Right Margin. Right Space in 1/72 inch
	 */
	public int getMarginRight() {
		Integer ii = (Integer) get_Value("MarginRight");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Top Margin. Top Space in 1/72 inch
	 */
	public void setMarginTop(int MarginTop) {
		set_Value("MarginTop", new Integer(MarginTop));
	}

	/**
	 * Get Top Margin. Top Space in 1/72 inch
	 */
	public int getMarginTop() {
		Integer ii = (Integer) get_Value("MarginTop");
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
}
