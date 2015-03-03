/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_PRODUCT_MARK
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.234
 */
public class X_M_PRODUCT_MARK extends PO {
	/** Standard Constructor */
	public X_M_PRODUCT_MARK(Properties ctx, int M_PRODUCT_MARK_ID,
			String trxName) {
		super(ctx, M_PRODUCT_MARK_ID, trxName);
		/**
		 * if (M_PRODUCT_MARK_ID == 0) { setDescription (null);
		 * setM_PRODUCT_MARK_ID (0); setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_M_PRODUCT_MARK(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_PRODUCT_MARK */
	public static final String Table_Name = "M_PRODUCT_MARK";

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
		StringBuffer sb = new StringBuffer("X_M_PRODUCT_MARK[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description == null)
			throw new IllegalArgumentException("Description is mandatory.");
		if (Description.length() > 255) {
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

	/** Set M_PRODUCT_MARK_ID */
	public void setM_PRODUCT_MARK_ID(int M_PRODUCT_MARK_ID) {
		if (M_PRODUCT_MARK_ID < 1)
			throw new IllegalArgumentException(
					"M_PRODUCT_MARK_ID is mandatory.");
		set_ValueNoCheck("M_PRODUCT_MARK_ID", new Integer(M_PRODUCT_MARK_ID));
	}

	/** Get M_PRODUCT_MARK_ID */
	public int getM_PRODUCT_MARK_ID() {
		Integer ii = (Integer) get_Value("M_PRODUCT_MARK_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
