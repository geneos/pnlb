/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for R_IssueUser
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.468
 */
public class X_R_IssueUser extends PO {
	/** Standard Constructor */
	public X_R_IssueUser(Properties ctx, int R_IssueUser_ID, String trxName) {
		super(ctx, R_IssueUser_ID, trxName);
		/**
		 * if (R_IssueUser_ID == 0) { setR_IssueUser_ID (0); setUserName (null); }
		 */
	}

	/** Load Constructor */
	public X_R_IssueUser(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=R_IssueUser */
	public static final String Table_Name = "R_IssueUser";

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
		StringBuffer sb = new StringBuffer("X_R_IssueUser[").append(get_ID())
				.append("]");
		return sb.toString();
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
	 * Set IssueUser. User who reported issues
	 */
	public void setR_IssueUser_ID(int R_IssueUser_ID) {
		if (R_IssueUser_ID < 1)
			throw new IllegalArgumentException("R_IssueUser_ID is mandatory.");
		set_ValueNoCheck("R_IssueUser_ID", new Integer(R_IssueUser_ID));
	}

	/**
	 * Get IssueUser. User who reported issues
	 */
	public int getR_IssueUser_ID() {
		Integer ii = (Integer) get_Value("R_IssueUser_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Registered EMail. Email of the responsible for the System
	 */
	public void setUserName(String UserName) {
		if (UserName == null)
			throw new IllegalArgumentException("UserName is mandatory.");
		if (UserName.length() > 60) {
			log.warning("Length > 60 - truncated");
			UserName = UserName.substring(0, 59);
		}
		set_ValueNoCheck("UserName", UserName);
	}

	/**
	 * Get Registered EMail. Email of the responsible for the System
	 */
	public String getUserName() {
		return (String) get_Value("UserName");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getUserName());
	}
}
