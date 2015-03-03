/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Preference
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.703
 */
public class X_AD_Preference extends PO {
	/** Standard Constructor */
	public X_AD_Preference(Properties ctx, int AD_Preference_ID, String trxName) {
		super(ctx, AD_Preference_ID, trxName);
		/**
		 * if (AD_Preference_ID == 0) { setAD_Preference_ID (0); setAttribute
		 * (null); setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Preference(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Preference */
	public static final String Table_Name = "AD_Preference";

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
		StringBuffer sb = new StringBuffer("X_AD_Preference[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Preference. Personal Value Preference
	 */
	public void setAD_Preference_ID(int AD_Preference_ID) {
		if (AD_Preference_ID < 1)
			throw new IllegalArgumentException("AD_Preference_ID is mandatory.");
		set_ValueNoCheck("AD_Preference_ID", new Integer(AD_Preference_ID));
	}

	/**
	 * Get Preference. Personal Value Preference
	 */
	public int getAD_Preference_ID() {
		Integer ii = (Integer) get_Value("AD_Preference_ID");
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
	 * Set Window. Data entry or display window
	 */
	public void setAD_Window_ID(int AD_Window_ID) {
		if (AD_Window_ID <= 0)
			set_Value("AD_Window_ID", null);
		else
			set_Value("AD_Window_ID", new Integer(AD_Window_ID));
	}

	/**
	 * Get Window. Data entry or display window
	 */
	public int getAD_Window_ID() {
		Integer ii = (Integer) get_Value("AD_Window_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Attribute */
	public void setAttribute(String Attribute) {
		if (Attribute == null)
			throw new IllegalArgumentException("Attribute is mandatory.");
		if (Attribute.length() > 60) {
			log.warning("Length > 60 - truncated");
			Attribute = Attribute.substring(0, 59);
		}
		set_Value("Attribute", Attribute);
	}

	/** Get Attribute */
	public String getAttribute() {
		return (String) get_Value("Attribute");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getAttribute());
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 60) {
			log.warning("Length > 60 - truncated");
			Value = Value.substring(0, 59);
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
