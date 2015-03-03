/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_ClientShare
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.281
 */
public class X_AD_ClientShare extends PO {
	/** Standard Constructor */
	public X_AD_ClientShare(Properties ctx, int AD_ClientShare_ID,
			String trxName) {
		super(ctx, AD_ClientShare_ID, trxName);
		/**
		 * if (AD_ClientShare_ID == 0) { setAD_ClientShare_ID (0);
		 * setAD_Table_ID (0); setName (null); setShareType (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_ClientShare(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_ClientShare */
	public static final String Table_Name = "AD_ClientShare";

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
		StringBuffer sb = new StringBuffer("X_AD_ClientShare[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Client Share. Force (not) sharing of client/org entities
	 */
	public void setAD_ClientShare_ID(int AD_ClientShare_ID) {
		if (AD_ClientShare_ID < 1)
			throw new IllegalArgumentException(
					"AD_ClientShare_ID is mandatory.");
		set_ValueNoCheck("AD_ClientShare_ID", new Integer(AD_ClientShare_ID));
	}

	/**
	 * Get Client Share. Force (not) sharing of client/org entities
	 */
	public int getAD_ClientShare_ID() {
		Integer ii = (Integer) get_Value("AD_ClientShare_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID < 1)
			throw new IllegalArgumentException("AD_Table_ID is mandatory.");
		set_Value("AD_Table_ID", new Integer(AD_Table_ID));
	}

	/**
	 * Get Table. Table for the Fields
	 */
	public int getAD_Table_ID() {
		Integer ii = (Integer) get_Value("AD_Table_ID");
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
		if (Name.length() > 120) {
			log.warning("Length > 120 - truncated");
			Name = Name.substring(0, 119);
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

	/** ShareType AD_Reference_ID=365 */
	public static final int SHARETYPE_AD_Reference_ID = 365;

	/** Client (all shared) = C */
	public static final String SHARETYPE_ClientAllShared = "C";

	/** Org (not shared) = O */
	public static final String SHARETYPE_OrgNotShared = "O";

	/** Client or Org = x */
	public static final String SHARETYPE_ClientOrOrg = "x";

	/**
	 * Set Share Type. Type of sharing
	 */
	public void setShareType(String ShareType) {
		if (ShareType == null)
			throw new IllegalArgumentException("ShareType is mandatory");
		if (ShareType.equals("C") || ShareType.equals("O")
				|| ShareType.equals("x"))
			;
		else
			throw new IllegalArgumentException("ShareType Invalid value - "
					+ ShareType + " - Reference_ID=365 - C - O - x");
		if (ShareType.length() > 1) {
			log.warning("Length > 1 - truncated");
			ShareType = ShareType.substring(0, 0);
		}
		set_Value("ShareType", ShareType);
	}

	/**
	 * Get Share Type. Type of sharing
	 */
	public String getShareType() {
		return (String) get_Value("ShareType");
	}
}
