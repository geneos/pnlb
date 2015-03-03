/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_OrgType
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.656
 */
public class X_AD_OrgType extends PO {
	/** Standard Constructor */
	public X_AD_OrgType(Properties ctx, int AD_OrgType_ID, String trxName) {
		super(ctx, AD_OrgType_ID, trxName);
		/**
		 * if (AD_OrgType_ID == 0) { setAD_OrgType_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_OrgType(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_OrgType */
	public static final String Table_Name = "AD_OrgType";

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
		StringBuffer sb = new StringBuffer("X_AD_OrgType[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Organization Type. Organization Type allows you to categorize your
	 * organizations
	 */
	public void setAD_OrgType_ID(int AD_OrgType_ID) {
		if (AD_OrgType_ID < 1)
			throw new IllegalArgumentException("AD_OrgType_ID is mandatory.");
		set_ValueNoCheck("AD_OrgType_ID", new Integer(AD_OrgType_ID));
	}

	/**
	 * Get Organization Type. Organization Type allows you to categorize your
	 * organizations
	 */
	public int getAD_OrgType_ID() {
		Integer ii = (Integer) get_Value("AD_OrgType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
