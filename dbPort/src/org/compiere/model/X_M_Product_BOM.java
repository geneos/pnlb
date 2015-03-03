/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Product_BOM
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.437
 */
public class X_M_Product_BOM extends PO {
	/** Standard Constructor */
	public X_M_Product_BOM(Properties ctx, int M_Product_BOM_ID, String trxName) {
		super(ctx, M_Product_BOM_ID, trxName);
		/**
		 * if (M_Product_BOM_ID == 0) { setBOMQty (Env.ZERO); // 1 setLine (0); //
		 * 
		 * @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_Product_BOM
		 *             WHERE M_Product_ID=@M_Product_ID@ setM_ProductBOM_ID (0);
		 *             setM_Product_BOM_ID (0); setM_Product_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_M_Product_BOM(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Product_BOM */
	public static final String Table_Name = "M_Product_BOM";

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
		StringBuffer sb = new StringBuffer("X_M_Product_BOM[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set BOM Quantity. Bill of Materials Quantity
	 */
	public void setBOMQty(BigDecimal BOMQty) {
		if (BOMQty == null)
			throw new IllegalArgumentException("BOMQty is mandatory.");
		set_Value("BOMQty", BOMQty);
	}

	/**
	 * Get BOM Quantity. Bill of Materials Quantity
	 */
	public BigDecimal getBOMQty() {
		BigDecimal bd = (BigDecimal) get_Value("BOMQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** BOMType AD_Reference_ID=279 */
	public static final int BOMTYPE_AD_Reference_ID = 279;

	/** In alternative Group 1 = 1 */
	public static final String BOMTYPE_InAlternativeGroup1 = "1";

	/** In alternative Group 2 = 2 */
	public static final String BOMTYPE_InAlternativeGroup2 = "2";

	/** In alternaltve Group 3 = 3 */
	public static final String BOMTYPE_InAlternaltveGroup3 = "3";

	/** In alternative Group 4 = 4 */
	public static final String BOMTYPE_InAlternativeGroup4 = "4";

	/** In alternative Group 5 = 5 */
	public static final String BOMTYPE_InAlternativeGroup5 = "5";

	/** In alternative Group 6 = 6 */
	public static final String BOMTYPE_InAlternativeGroup6 = "6";

	/** In alternative Group 7 = 7 */
	public static final String BOMTYPE_InAlternativeGroup7 = "7";

	/** In alternative Group 8 = 8 */
	public static final String BOMTYPE_InAlternativeGroup8 = "8";

	/** In alternative Group 9 = 9 */
	public static final String BOMTYPE_InAlternativeGroup9 = "9";

	/** Optional Part = O */
	public static final String BOMTYPE_OptionalPart = "O";

	/** Standard Part = P */
	public static final String BOMTYPE_StandardPart = "P";

	/**
	 * Set BOM Type. Type of BOM
	 */
	public void setBOMType(String BOMType) {
		if (BOMType != null && BOMType.length() > 1) {
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
	 * Set Line No. Unique line for this document
	 */
	public void setLine(int Line) {
		set_Value("Line", new Integer(Line));
	}

	/**
	 * Get Line No. Unique line for this document
	 */
	public int getLine() {
		Integer ii = (Integer) get_Value("Line");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** M_ProductBOM_ID AD_Reference_ID=162 */
	public static final int M_PRODUCTBOM_ID_AD_Reference_ID = 162;

	/**
	 * Set BOM Product. Bill of Material Component Product
	 */
	public void setM_ProductBOM_ID(int M_ProductBOM_ID) {
		if (M_ProductBOM_ID < 1)
			throw new IllegalArgumentException("M_ProductBOM_ID is mandatory.");
		set_Value("M_ProductBOM_ID", new Integer(M_ProductBOM_ID));
	}

	/**
	 * Get BOM Product. Bill of Material Component Product
	 */
	public int getM_ProductBOM_ID() {
		Integer ii = (Integer) get_Value("M_ProductBOM_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getM_ProductBOM_ID()));
	}

	/** Set BOM Line */
	public void setM_Product_BOM_ID(int M_Product_BOM_ID) {
		if (M_Product_BOM_ID < 1)
			throw new IllegalArgumentException("M_Product_BOM_ID is mandatory.");
		set_ValueNoCheck("M_Product_BOM_ID", new Integer(M_Product_BOM_ID));
	}

	/** Get BOM Line */
	public int getM_Product_BOM_ID() {
		Integer ii = (Integer) get_Value("M_Product_BOM_ID");
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
}
