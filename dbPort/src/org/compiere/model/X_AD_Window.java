/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Window
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.937
 */
public class X_AD_Window extends PO {
	/** Standard Constructor */
	public X_AD_Window(Properties ctx, int AD_Window_ID, String trxName) {
		super(ctx, AD_Window_ID, trxName);
		/**
		 * if (AD_Window_ID == 0) { setAD_Window_ID (0); setEntityType (null); //
		 * U setIsBetaFunctionality (false); setIsDefault (false); setIsSOTrx
		 * (true); // Y setName (null); setWindowType (null); // M }
		 */
	}

	/** Load Constructor */
	public X_AD_Window(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Window */
	public static final String Table_Name = "AD_Window";

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

	protected BigDecimal accessLevel = new BigDecimal(4);

	/** AccessLevel 4 - System */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_Window[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set System Color. Color for backgrounds or indicators
	 */
	public void setAD_Color_ID(int AD_Color_ID) {
		if (AD_Color_ID <= 0)
			set_Value("AD_Color_ID", null);
		else
			set_Value("AD_Color_ID", new Integer(AD_Color_ID));
	}

	/**
	 * Get System Color. Color for backgrounds or indicators
	 */
	public int getAD_Color_ID() {
		Integer ii = (Integer) get_Value("AD_Color_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Image. System Image or Icon
	 */
	public void setAD_Image_ID(int AD_Image_ID) {
		if (AD_Image_ID <= 0)
			set_Value("AD_Image_ID", null);
		else
			set_Value("AD_Image_ID", new Integer(AD_Image_ID));
	}

	/**
	 * Get Image. System Image or Icon
	 */
	public int getAD_Image_ID() {
		Integer ii = (Integer) get_Value("AD_Image_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Window. Data entry or display window
	 */
	public void setAD_Window_ID(int AD_Window_ID) {
		if (AD_Window_ID < 1)
			throw new IllegalArgumentException("AD_Window_ID is mandatory.");
		set_ValueNoCheck("AD_Window_ID", new Integer(AD_Window_ID));
	}

	/**
	 * Get Window. Data entry or display window
	 */
	public int getAD_Window_ID() {
		Integer ii = (Integer) get_Value("AD_Window_ID");
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

	/** EntityType AD_Reference_ID=245 */
	public static final int ENTITYTYPE_AD_Reference_ID = 245;

	/** Applications = A */
	public static final String ENTITYTYPE_Applications = "A";

	/** Compiere = C */
	public static final String ENTITYTYPE_Compiere = "C";

	/** Customization = CUST */
	public static final String ENTITYTYPE_Customization = "CUST";

	/** Dictionary = D */
	public static final String ENTITYTYPE_Dictionary = "D";

	/** User maintained = U */
	public static final String ENTITYTYPE_UserMaintained = "U";

	/**
	 * Set Entity Type. Dictionary Entity Type; Determines ownership and
	 * synchronization
	 */
	public void setEntityType(String EntityType) {
		if (EntityType == null)
			throw new IllegalArgumentException("EntityType is mandatory");
		if (EntityType.length() > 4) {
			log.warning("Length > 4 - truncated");
			EntityType = EntityType.substring(0, 3);
		}
		set_Value("EntityType", EntityType);
	}

	/**
	 * Get Entity Type. Dictionary Entity Type; Determines ownership and
	 * synchronization
	 */
	public String getEntityType() {
		return (String) get_Value("EntityType");
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
	 * Set Beta Functionality. This functionality is considered Beta
	 */
	public void setIsBetaFunctionality(boolean IsBetaFunctionality) {
		set_Value("IsBetaFunctionality", new Boolean(IsBetaFunctionality));
	}

	/**
	 * Get Beta Functionality. This functionality is considered Beta
	 */
	public boolean isBetaFunctionality() {
		Object oo = get_Value("IsBetaFunctionality");
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

	/**
	 * Set Sales Transaction. This is a Sales Transaction
	 */
	public void setIsSOTrx(boolean IsSOTrx) {
		set_Value("IsSOTrx", new Boolean(IsSOTrx));
	}

	/**
	 * Get Sales Transaction. This is a Sales Transaction
	 */
	public boolean isSOTrx() {
		Object oo = get_Value("IsSOTrx");
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

	/** Set Window Height */
	public void setWinHeight(int WinHeight) {
		set_Value("WinHeight", new Integer(WinHeight));
	}

	/** Get Window Height */
	public int getWinHeight() {
		Integer ii = (Integer) get_Value("WinHeight");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Window Width */
	public void setWinWidth(int WinWidth) {
		set_Value("WinWidth", new Integer(WinWidth));
	}

	/** Get Window Width */
	public int getWinWidth() {
		Integer ii = (Integer) get_Value("WinWidth");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** WindowType AD_Reference_ID=108 */
	public static final int WINDOWTYPE_AD_Reference_ID = 108;

	/** Maintain = M */
	public static final String WINDOWTYPE_Maintain = "M";

	/** Query Only = Q */
	public static final String WINDOWTYPE_QueryOnly = "Q";

	/** Single Record = S */
	public static final String WINDOWTYPE_SingleRecord = "S";

	/** Transaction = T */
	public static final String WINDOWTYPE_Transaction = "T";

	/**
	 * Set WindowType. Type or classification of a Window
	 */
	public void setWindowType(String WindowType) {
		if (WindowType == null)
			throw new IllegalArgumentException("WindowType is mandatory");
		if (WindowType.equals("M") || WindowType.equals("Q")
				|| WindowType.equals("S") || WindowType.equals("T"))
			;
		else
			throw new IllegalArgumentException("WindowType Invalid value - "
					+ WindowType + " - Reference_ID=108 - M - Q - S - T");
		if (WindowType.length() > 1) {
			log.warning("Length > 1 - truncated");
			WindowType = WindowType.substring(0, 0);
		}
		set_Value("WindowType", WindowType);
	}

	/**
	 * Get WindowType. Type or classification of a Window
	 */
	public String getWindowType() {
		return (String) get_Value("WindowType");
	}
}
