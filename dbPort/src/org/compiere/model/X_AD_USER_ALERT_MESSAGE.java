/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_USER_ALERT_MESSAGE
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.531
 */
public class X_AD_USER_ALERT_MESSAGE extends PO {
	/** Standard Constructor */
	public X_AD_USER_ALERT_MESSAGE(Properties ctx,
			int AD_USER_ALERT_MESSAGE_ID, String trxName) {
		super(ctx, AD_USER_ALERT_MESSAGE_ID, trxName);
		/**
		 * if (AD_USER_ALERT_MESSAGE_ID == 0) { setAD_ALERT_MESSAGE_ID (0);
		 * setAD_USER_ALERT_MESSAGE_ID (0); setAD_User_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_USER_ALERT_MESSAGE(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_USER_ALERT_MESSAGE */
	public static final String Table_Name = "AD_USER_ALERT_MESSAGE";

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
		StringBuffer sb = new StringBuffer("X_AD_USER_ALERT_MESSAGE[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** Set AD_ALERT_MESSAGE_ID */
	public void setAD_ALERT_MESSAGE_ID(int AD_ALERT_MESSAGE_ID) {
		if (AD_ALERT_MESSAGE_ID < 1)
			throw new IllegalArgumentException(
					"AD_ALERT_MESSAGE_ID is mandatory.");
		set_Value("AD_ALERT_MESSAGE_ID", new Integer(AD_ALERT_MESSAGE_ID));
	}

	/** Get AD_ALERT_MESSAGE_ID */
	public int getAD_ALERT_MESSAGE_ID() {
		Integer ii = (Integer) get_Value("AD_ALERT_MESSAGE_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set AD_USER_ALERT_MESSAGE_ID */
	public void setAD_USER_ALERT_MESSAGE_ID(int AD_USER_ALERT_MESSAGE_ID) {
		if (AD_USER_ALERT_MESSAGE_ID < 1)
			throw new IllegalArgumentException(
					"AD_USER_ALERT_MESSAGE_ID is mandatory.");
		set_ValueNoCheck("AD_USER_ALERT_MESSAGE_ID", new Integer(
				AD_USER_ALERT_MESSAGE_ID));
	}

	/** Get AD_USER_ALERT_MESSAGE_ID */
	public int getAD_USER_ALERT_MESSAGE_ID() {
		Integer ii = (Integer) get_Value("AD_USER_ALERT_MESSAGE_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID < 1)
			throw new IllegalArgumentException("AD_User_ID is mandatory.");
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
}
