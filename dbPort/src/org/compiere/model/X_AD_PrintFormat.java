/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_PrintFormat
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.765
 */
public class X_AD_PrintFormat extends PO {
	/** Standard Constructor */
	public X_AD_PrintFormat(Properties ctx, int AD_PrintFormat_ID,
			String trxName) {
		super(ctx, AD_PrintFormat_ID, trxName);
		/**
		 * if (AD_PrintFormat_ID == 0) { setAD_PrintColor_ID (0);
		 * setAD_PrintFont_ID (0); setAD_PrintFormat_ID (0); // 0
		 * setAD_PrintPaper_ID (0); setAD_Table_ID (0); setFooterMargin (0);
		 * setHeaderMargin (0); setIsDefault (false); setIsForm (false);
		 * setIsStandardHeaderFooter (true); // Y setIsTableBased (true); // Y
		 * setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_PrintFormat(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_PrintFormat */
	public static final String Table_Name = "AD_PrintFormat";

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
	
	protected BigDecimal accessLevel = new BigDecimal(7);

	/** AccessLevel 7 - System - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_PrintFormat[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Print Color. Color used for printing and display
	 */
	public void setAD_PrintColor_ID(int AD_PrintColor_ID) {
		if (AD_PrintColor_ID < 1)
			throw new IllegalArgumentException("AD_PrintColor_ID is mandatory.");
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
	 * Set Print Font. Maintain Print Font
	 */
	public void setAD_PrintFont_ID(int AD_PrintFont_ID) {
		if (AD_PrintFont_ID < 1)
			throw new IllegalArgumentException("AD_PrintFont_ID is mandatory.");
		set_Value("AD_PrintFont_ID", new Integer(AD_PrintFont_ID));
	}

	/**
	 * Get Print Font. Maintain Print Font
	 */
	public int getAD_PrintFont_ID() {
		Integer ii = (Integer) get_Value("AD_PrintFont_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Print Format. Data Print Format
	 */
	public void setAD_PrintFormat_ID(int AD_PrintFormat_ID) {
		if (AD_PrintFormat_ID < 1)
			throw new IllegalArgumentException(
					"AD_PrintFormat_ID is mandatory.");
		set_ValueNoCheck("AD_PrintFormat_ID", new Integer(AD_PrintFormat_ID));
	}

	/**
	 * Get Print Format. Data Print Format
	 */
	public int getAD_PrintFormat_ID() {
		Integer ii = (Integer) get_Value("AD_PrintFormat_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Print Paper. Printer paper definition
	 */
	public void setAD_PrintPaper_ID(int AD_PrintPaper_ID) {
		if (AD_PrintPaper_ID < 1)
			throw new IllegalArgumentException("AD_PrintPaper_ID is mandatory.");
		set_Value("AD_PrintPaper_ID", new Integer(AD_PrintPaper_ID));
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
	 * Set Print Table Format. Table Format in Reports
	 */
	public void setAD_PrintTableFormat_ID(int AD_PrintTableFormat_ID) {
		if (AD_PrintTableFormat_ID <= 0)
			set_Value("AD_PrintTableFormat_ID", null);
		else
			set_Value("AD_PrintTableFormat_ID", new Integer(
					AD_PrintTableFormat_ID));
	}

	/**
	 * Get Print Table Format. Table Format in Reports
	 */
	public int getAD_PrintTableFormat_ID() {
		Integer ii = (Integer) get_Value("AD_PrintTableFormat_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Report View. View used to generate this report
	 */
	public void setAD_ReportView_ID(int AD_ReportView_ID) {
		if (AD_ReportView_ID <= 0)
			set_ValueNoCheck("AD_ReportView_ID", null);
		else
			set_ValueNoCheck("AD_ReportView_ID", new Integer(AD_ReportView_ID));
	}

	/**
	 * Get Report View. View used to generate this report
	 */
	public int getAD_ReportView_ID() {
		Integer ii = (Integer) get_Value("AD_ReportView_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID < 1)
			throw new IllegalArgumentException("AD_Table_ID is mandatory.");
		set_ValueNoCheck("AD_Table_ID", new Integer(AD_Table_ID));
	}

	/**
	 * Get Table. Table for the Fields
	 */
	public int getAD_Table_ID() {
		Integer ii = (Integer) get_Value("AD_Table_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Create Copy */
	public void setCreateCopy(String CreateCopy) {
		if (CreateCopy != null && CreateCopy.length() > 1) {
			log.warning("Length > 1 - truncated");
			CreateCopy = CreateCopy.substring(0, 0);
		}
		set_Value("CreateCopy", CreateCopy);
	}

	/** Get Create Copy */
	public String getCreateCopy() {
		return (String) get_Value("CreateCopy");
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
	 * Set Footer Margin. Margin of the Footer in 1/72 of an inch
	 */
	public void setFooterMargin(int FooterMargin) {
		set_Value("FooterMargin", new Integer(FooterMargin));
	}

	/**
	 * Get Footer Margin. Margin of the Footer in 1/72 of an inch
	 */
	public int getFooterMargin() {
		Integer ii = (Integer) get_Value("FooterMargin");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Header Margin. Margin of the Header in 1/72 of an inch
	 */
	public void setHeaderMargin(int HeaderMargin) {
		set_Value("HeaderMargin", new Integer(HeaderMargin));
	}

	/**
	 * Get Header Margin. Margin of the Header in 1/72 of an inch
	 */
	public int getHeaderMargin() {
		Integer ii = (Integer) get_Value("HeaderMargin");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Form. If Selected, a Form is printed, if not selected a columnar List
	 * report
	 */
	public void setIsForm(boolean IsForm) {
		set_Value("IsForm", new Boolean(IsForm));
	}

	/**
	 * Get Form. If Selected, a Form is printed, if not selected a columnar List
	 * report
	 */
	public boolean isForm() {
		Object oo = get_Value("IsForm");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Standard Header/Footer. The standard Header and Footer is used
	 */
	public void setIsStandardHeaderFooter(boolean IsStandardHeaderFooter) {
		set_Value("IsStandardHeaderFooter", new Boolean(IsStandardHeaderFooter));
	}

	/**
	 * Get Standard Header/Footer. The standard Header and Footer is used
	 */
	public boolean isStandardHeaderFooter() {
		Object oo = get_Value("IsStandardHeaderFooter");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Table Based. Table based List Reporting
	 */
	public void setIsTableBased(boolean IsTableBased) {
		set_ValueNoCheck("IsTableBased", new Boolean(IsTableBased));
	}

	/**
	 * Get Table Based. Table based List Reporting
	 */
	public boolean isTableBased() {
		Object oo = get_Value("IsTableBased");
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

	/**
	 * Set Printer Name. Name of the Printer
	 */
	public void setPrinterName(String PrinterName) {
		if (PrinterName != null && PrinterName.length() > 40) {
			log.warning("Length > 40 - truncated");
			PrinterName = PrinterName.substring(0, 39);
		}
		set_Value("PrinterName", PrinterName);
	}

	/**
	 * Get Printer Name. Name of the Printer
	 */
	public String getPrinterName() {
		return (String) get_Value("PrinterName");
	}

	/** Set VIEWPARAMETERS */
	public void setVIEWPARAMETERS(boolean VIEWPARAMETERS) {
		set_Value("VIEWPARAMETERS", new Boolean(VIEWPARAMETERS));
	}

	/** Get VIEWPARAMETERS */
	public boolean isVIEWPARAMETERS() {
		Object oo = get_Value("VIEWPARAMETERS");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
}
