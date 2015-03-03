/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_BOM
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.453
 */
public class X_M_BOM extends PO {
	/** Standard Constructor */
	public X_M_BOM(Properties ctx, int M_BOM_ID, String trxName) {
		super(ctx, M_BOM_ID, trxName);
		/**
		 * if (M_BOM_ID == 0) { setBOMType (null); // A setBOMUse (null); // A
		 * setM_BOM_ID (0); setM_Product_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_M_BOM(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_BOM */
	public static final String Table_Name = "M_BOM";

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
		StringBuffer sb = new StringBuffer("X_M_BOM[").append(get_ID()).append(
				"]");
		return sb.toString();
	}

	/** BOMType AD_Reference_ID=347 */
	public static final int BOMTYPE_AD_Reference_ID = 347;

	/** Current Active = A */
	public static final String BOMTYPE_CurrentActive = "A";

	/** Future = F */
	public static final String BOMTYPE_Future = "F";

	/** Maintenance = M */
	public static final String BOMTYPE_Maintenance = "M";

	/** Make-To-Order = O */
	public static final String BOMTYPE_Make_To_Order = "O";

	/** Previous = P */
	public static final String BOMTYPE_Previous = "P";

	/** Repair = R */
	public static final String BOMTYPE_Repair = "R";

	/** Previous, Spare = S */
	public static final String BOMTYPE_PreviousSpare = "S";

	/**
	 * Set BOM Type. Type of BOM
	 */
	public void setBOMType(String BOMType) {
		if (BOMType == null)
			throw new IllegalArgumentException("BOMType is mandatory");
		if (BOMType.equals("A") || BOMType.equals("F") || BOMType.equals("M")
				|| BOMType.equals("O") || BOMType.equals("P")
				|| BOMType.equals("R") || BOMType.equals("S"))
			;
		else
			throw new IllegalArgumentException("BOMType Invalid value - "
					+ BOMType
					+ " - Reference_ID=347 - A - F - M - O - P - R - S");
		if (BOMType.length() > 1) {
			log.warning("Length > 1 - truncated");
			BOMType = BOMType.substring(0, 0);
		}
		set_Value("BOMType", BOMType);
	}

	/**
	 * Get BOM Type. Type of BOM
	 */
	public String getBOMType() {
		return (String) get_Value("BOMType");
	}

	/** BOMUse AD_Reference_ID=348 */
	public static final int BOMUSE_AD_Reference_ID = 348;

	/** Master = A */
	public static final String BOMUSE_Master = "A";

	/** Engineering = E */
	public static final String BOMUSE_Engineering = "E";

	/** Manufacturing = M */
	public static final String BOMUSE_Manufacturing = "M";

	/** Planning = P */
	public static final String BOMUSE_Planning = "P";

	/** Quality = Q */
	public static final String BOMUSE_Quality = "Q";

	/**
	 * Set BOM Use. The use of the Bill of Material
	 */
	public void setBOMUse(String BOMUse) {
		if (BOMUse == null)
			throw new IllegalArgumentException("BOMUse is mandatory");
		if (BOMUse.equals("A") || BOMUse.equals("E") || BOMUse.equals("M")
				|| BOMUse.equals("P") || BOMUse.equals("Q"))
			;
		else
			throw new IllegalArgumentException("BOMUse Invalid value - "
					+ BOMUse + " - Reference_ID=348 - A - E - M - P - Q");
		if (BOMUse.length() > 1) {
			log.warning("Length > 1 - truncated");
			BOMUse = BOMUse.substring(0, 0);
		}
		set_Value("BOMUse", BOMUse);
	}

	/**
	 * Get BOM Use. The use of the Bill of Material
	 */
	public String getBOMUse() {
		return (String) get_Value("BOMUse");
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
	 * Set BOM. Bill of Material
	 */
	public void setM_BOM_ID(int M_BOM_ID) {
		if (M_BOM_ID < 1)
			throw new IllegalArgumentException("M_BOM_ID is mandatory.");
		set_ValueNoCheck("M_BOM_ID", new Integer(M_BOM_ID));
	}

	/**
	 * Get BOM. Bill of Material
	 */
	public int getM_BOM_ID() {
		Integer ii = (Integer) get_Value("M_BOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Change Notice. Bill of Materials (Engineering) Change Notice
	 * (Version)
	 */
	public void setM_ChangeNotice_ID(int M_ChangeNotice_ID) {
		if (M_ChangeNotice_ID <= 0)
			set_Value("M_ChangeNotice_ID", null);
		else
			set_Value("M_ChangeNotice_ID", new Integer(M_ChangeNotice_ID));
	}

	/**
	 * Get Change Notice. Bill of Materials (Engineering) Change Notice
	 * (Version)
	 */
	public int getM_ChangeNotice_ID() {
		Integer ii = (Integer) get_Value("M_ChangeNotice_ID");
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
