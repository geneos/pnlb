/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_PrintLabel
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.875
 */
public class X_AD_PrintLabel extends PO {
	/** Standard Constructor */
	public X_AD_PrintLabel(Properties ctx, int AD_PrintLabel_ID, String trxName) {
		super(ctx, AD_PrintLabel_ID, trxName);
		/**
		 * if (AD_PrintLabel_ID == 0) { setAD_LabelPrinter_ID (0);
		 * setAD_PrintLabel_ID (0); setAD_Table_ID (0); setIsLandscape (false);
		 * setLabelHeight (0); setLabelWidth (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_PrintLabel(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_PrintLabel */
	public static final String Table_Name = "AD_PrintLabel";

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
		StringBuffer sb = new StringBuffer("X_AD_PrintLabel[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Label printer. Label Printer Definition
	 */
	public void setAD_LabelPrinter_ID(int AD_LabelPrinter_ID) {
		if (AD_LabelPrinter_ID < 1)
			throw new IllegalArgumentException(
					"AD_LabelPrinter_ID is mandatory.");
		set_Value("AD_LabelPrinter_ID", new Integer(AD_LabelPrinter_ID));
	}

	/**
	 * Get Label printer. Label Printer Definition
	 */
	public int getAD_LabelPrinter_ID() {
		Integer ii = (Integer) get_Value("AD_LabelPrinter_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Print Label. Label Format to print
	 */
	public void setAD_PrintLabel_ID(int AD_PrintLabel_ID) {
		if (AD_PrintLabel_ID < 1)
			throw new IllegalArgumentException("AD_PrintLabel_ID is mandatory.");
		set_ValueNoCheck("AD_PrintLabel_ID", new Integer(AD_PrintLabel_ID));
	}

	/**
	 * Get Print Label. Label Format to print
	 */
	public int getAD_PrintLabel_ID() {
		Integer ii = (Integer) get_Value("AD_PrintLabel_ID");
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
		set_Value("AD_Table_ID", new Integer(AD_Table_ID));
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
	 * Set Label Height. Height of the label
	 */
	public void setLabelHeight(int LabelHeight) {
		set_Value("LabelHeight", new Integer(LabelHeight));
	}

	/**
	 * Get Label Height. Height of the label
	 */
	public int getLabelHeight() {
		Integer ii = (Integer) get_Value("LabelHeight");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Label Width. Width of the Label
	 */
	public void setLabelWidth(int LabelWidth) {
		set_Value("LabelWidth", new Integer(LabelWidth));
	}

	/**
	 * Get Label Width. Width of the Label
	 */
	public int getLabelWidth() {
		Integer ii = (Integer) get_Value("LabelWidth");
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
}
