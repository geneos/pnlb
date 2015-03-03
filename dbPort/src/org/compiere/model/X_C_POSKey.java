/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_POSKey
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.921
 */
public class X_C_POSKey extends PO {
	/** Standard Constructor */
	public X_C_POSKey(Properties ctx, int C_POSKey_ID, String trxName) {
		super(ctx, C_POSKey_ID, trxName);
		/**
		 * if (C_POSKey_ID == 0) { setC_POSKeyLayout_ID (0); setC_POSKey_ID (0);
		 * setM_Product_ID (0); setName (null); setQty (Env.ZERO); setSeqNo (0); }
		 */
	}

	/** Load Constructor */
	public X_C_POSKey(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_POSKey */
	public static final String Table_Name = "C_POSKey";

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
		StringBuffer sb = new StringBuffer("X_C_POSKey[").append(get_ID())
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
	 * Set POS Key Layout. POS Function Key Layout
	 */
	public void setC_POSKeyLayout_ID(int C_POSKeyLayout_ID) {
		if (C_POSKeyLayout_ID < 1)
			throw new IllegalArgumentException(
					"C_POSKeyLayout_ID is mandatory.");
		set_ValueNoCheck("C_POSKeyLayout_ID", new Integer(C_POSKeyLayout_ID));
	}

	/**
	 * Get POS Key Layout. POS Function Key Layout
	 */
	public int getC_POSKeyLayout_ID() {
		Integer ii = (Integer) get_Value("C_POSKeyLayout_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set POS Key. POS Function Key
	 */
	public void setC_POSKey_ID(int C_POSKey_ID) {
		if (C_POSKey_ID < 1)
			throw new IllegalArgumentException("C_POSKey_ID is mandatory.");
		set_ValueNoCheck("C_POSKey_ID", new Integer(C_POSKey_ID));
	}

	/**
	 * Get POS Key. POS Function Key
	 */
	public int getC_POSKey_ID() {
		Integer ii = (Integer) get_Value("C_POSKey_ID");
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
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID < 1)
			throw new IllegalArgumentException("M_Product_ID is mandatory.");
		set_Value("M_Product_ID", new Integer(M_Product_ID));
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

	/**
	 * Set Quantity. Quantity
	 */
	public void setQty(BigDecimal Qty) {
		if (Qty == null)
			throw new IllegalArgumentException("Qty is mandatory.");
		set_Value("Qty", Qty);
	}

	/**
	 * Get Quantity. Quantity
	 */
	public BigDecimal getQty() {
		BigDecimal bd = (BigDecimal) get_Value("Qty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Sequence. Method of ordering records; lowest number comes first
	 */
	public void setSeqNo(int SeqNo) {
		set_Value("SeqNo", new Integer(SeqNo));
	}

	/**
	 * Get Sequence. Method of ordering records; lowest number comes first
	 */
	public int getSeqNo() {
		Integer ii = (Integer) get_Value("SeqNo");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
