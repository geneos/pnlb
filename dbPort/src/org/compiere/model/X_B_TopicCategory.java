/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for B_TopicCategory
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.359
 */
public class X_B_TopicCategory extends PO {
	/** Standard Constructor */
	public X_B_TopicCategory(Properties ctx, int B_TopicCategory_ID,
			String trxName) {
		super(ctx, B_TopicCategory_ID, trxName);
		/**
		 * if (B_TopicCategory_ID == 0) { setB_TopicCategory_ID (0);
		 * setB_TopicType_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_B_TopicCategory(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=B_TopicCategory */
	public static final String Table_Name = "B_TopicCategory";

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
		StringBuffer sb = new StringBuffer("X_B_TopicCategory[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Topic Category. Auction Topic Category
	 */
	public void setB_TopicCategory_ID(int B_TopicCategory_ID) {
		if (B_TopicCategory_ID < 1)
			throw new IllegalArgumentException(
					"B_TopicCategory_ID is mandatory.");
		set_ValueNoCheck("B_TopicCategory_ID", new Integer(B_TopicCategory_ID));
	}

	/**
	 * Get Topic Category. Auction Topic Category
	 */
	public int getB_TopicCategory_ID() {
		Integer ii = (Integer) get_Value("B_TopicCategory_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Topic Type. Auction Topic Type
	 */
	public void setB_TopicType_ID(int B_TopicType_ID) {
		if (B_TopicType_ID < 1)
			throw new IllegalArgumentException("B_TopicType_ID is mandatory.");
		set_ValueNoCheck("B_TopicType_ID", new Integer(B_TopicType_ID));
	}

	/**
	 * Get Topic Type. Auction Topic Type
	 */
	public int getB_TopicType_ID() {
		Integer ii = (Integer) get_Value("B_TopicType_ID");
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
