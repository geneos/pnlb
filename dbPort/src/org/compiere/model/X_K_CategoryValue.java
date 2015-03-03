/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for K_CategoryValue
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.609
 */
public class X_K_CategoryValue extends PO {
	/** Standard Constructor */
	public X_K_CategoryValue(Properties ctx, int K_CategoryValue_ID,
			String trxName) {
		super(ctx, K_CategoryValue_ID, trxName);
		/**
		 * if (K_CategoryValue_ID == 0) { setK_CategoryValue_ID (0);
		 * setK_Category_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_K_CategoryValue(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=K_CategoryValue */
	public static final String Table_Name = "K_CategoryValue";

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
		StringBuffer sb = new StringBuffer("X_K_CategoryValue[").append(
				get_ID()).append("]");
		return sb.toString();
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
	 * Set Category Value. The value of the category
	 */
	public void setK_CategoryValue_ID(int K_CategoryValue_ID) {
		if (K_CategoryValue_ID < 1)
			throw new IllegalArgumentException(
					"K_CategoryValue_ID is mandatory.");
		set_ValueNoCheck("K_CategoryValue_ID", new Integer(K_CategoryValue_ID));
	}

	/**
	 * Get Category Value. The value of the category
	 */
	public int getK_CategoryValue_ID() {
		Integer ii = (Integer) get_Value("K_CategoryValue_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Knowledge Category. Knowledge Category
	 */
	public void setK_Category_ID(int K_Category_ID) {
		if (K_Category_ID < 1)
			throw new IllegalArgumentException("K_Category_ID is mandatory.");
		set_ValueNoCheck("K_Category_ID", new Integer(K_Category_ID));
	}

	/**
	 * Get Knowledge Category. Knowledge Category
	 */
	public int getK_Category_ID() {
		Integer ii = (Integer) get_Value("K_Category_ID");
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
}
