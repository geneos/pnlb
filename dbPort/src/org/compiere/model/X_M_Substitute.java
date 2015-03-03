/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Substitute
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.703
 */
public class X_M_Substitute extends PO {
	/** Standard Constructor */
	public X_M_Substitute(Properties ctx, int M_Substitute_ID, String trxName) {
		super(ctx, M_Substitute_ID, trxName);
		/**
		 * if (M_Substitute_ID == 0) { setM_Product_ID (0); setName (null);
		 * setSubstitute_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_M_Substitute(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Substitute */
	public static final String Table_Name = "M_Substitute";

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
		StringBuffer sb = new StringBuffer("X_M_Substitute[").append(get_ID())
				.append("]");
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

	/** Substitute_ID AD_Reference_ID=162 */
	public static final int SUBSTITUTE_ID_AD_Reference_ID = 162;

	/**
	 * Set Substitute. Entity which can be used in place of this entity
	 */
	public void setSubstitute_ID(int Substitute_ID) {
		if (Substitute_ID < 1)
			throw new IllegalArgumentException("Substitute_ID is mandatory.");
		set_ValueNoCheck("Substitute_ID", new Integer(Substitute_ID));
	}

	/**
	 * Get Substitute. Entity which can be used in place of this entity
	 */
	public int getSubstitute_ID() {
		Integer ii = (Integer) get_Value("Substitute_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
