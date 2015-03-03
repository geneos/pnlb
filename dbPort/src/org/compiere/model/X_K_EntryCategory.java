/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for K_EntryCategory
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.671
 */
public class X_K_EntryCategory extends PO {
	/** Standard Constructor */
	public X_K_EntryCategory(Properties ctx, int K_EntryCategory_ID,
			String trxName) {
		super(ctx, K_EntryCategory_ID, trxName);
		/**
		 * if (K_EntryCategory_ID == 0) { setK_CategoryValue_ID (0);
		 * setK_Category_ID (0); setK_Entry_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_K_EntryCategory(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=K_EntryCategory */
	public static final String Table_Name = "K_EntryCategory";

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
		StringBuffer sb = new StringBuffer("X_K_EntryCategory[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Category Value. The value of the category
	 */
	public void setK_CategoryValue_ID(int K_CategoryValue_ID) {
		if (K_CategoryValue_ID < 1)
			throw new IllegalArgumentException(
					"K_CategoryValue_ID is mandatory.");
		set_Value("K_CategoryValue_ID", new Integer(K_CategoryValue_ID));
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getK_CategoryValue_ID()));
	}

	/**
	 * Set Knowledge Category. Knowledge Category
	 */
	public void setK_Category_ID(int K_Category_ID) {
		if (K_Category_ID < 1)
			throw new IllegalArgumentException("K_Category_ID is mandatory.");
		set_Value("K_Category_ID", new Integer(K_Category_ID));
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
	 * Set Entry. Knowledge Entry
	 */
	public void setK_Entry_ID(int K_Entry_ID) {
		if (K_Entry_ID < 1)
			throw new IllegalArgumentException("K_Entry_ID is mandatory.");
		set_ValueNoCheck("K_Entry_ID", new Integer(K_Entry_ID));
	}

	/**
	 * Get Entry. Knowledge Entry
	 */
	public int getK_Entry_ID() {
		Integer ii = (Integer) get_Value("K_Entry_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
