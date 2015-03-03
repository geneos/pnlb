/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_UserQuery
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.64
 */
public class X_AD_UserQuery extends PO {
	/** Standard Constructor */
	public X_AD_UserQuery(Properties ctx, int AD_UserQuery_ID, String trxName) {
		super(ctx, AD_UserQuery_ID, trxName);
		/**
		 * if (AD_UserQuery_ID == 0) { setAD_Table_ID (0); setAD_UserQuery_ID
		 * (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_UserQuery(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_UserQuery */
	public static final String Table_Name = "AD_UserQuery";

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

	protected BigDecimal accessLevel = new BigDecimal(7);

	/** AccessLevel 7 - System - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_UserQuery[").append(get_ID())
				.append("]");
		return sb.toString();
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
	 * Set User Query. Saved User Query
	 */
	public void setAD_UserQuery_ID(int AD_UserQuery_ID) {
		if (AD_UserQuery_ID < 1)
			throw new IllegalArgumentException("AD_UserQuery_ID is mandatory.");
		set_ValueNoCheck("AD_UserQuery_ID", new Integer(AD_UserQuery_ID));
	}

	/**
	 * Get User Query. Saved User Query
	 */
	public int getAD_UserQuery_ID() {
		Integer ii = (Integer) get_Value("AD_UserQuery_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID <= 0)
			set_Value("AD_User_ID", null);
		else
			set_Value("AD_User_ID", new Integer(AD_User_ID));
	}

	/**
	 * Get User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public int getAD_User_ID() {
		Integer ii = (Integer) get_Value("AD_User_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Validation code. Validation Code
	 */
	public void setCode(String Code) {
		if (Code != null && Code.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Code = Code.substring(0, 1999);
		}
		set_Value("Code", Code);
	}

	/**
	 * Get Validation code. Validation Code
	 */
	public String getCode() {
		return (String) get_Value("Code");
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
}
