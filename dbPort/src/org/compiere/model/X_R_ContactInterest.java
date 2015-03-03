/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for R_ContactInterest
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.375
 */
public class X_R_ContactInterest extends PO {
	/** Standard Constructor */
	public X_R_ContactInterest(Properties ctx, int R_ContactInterest_ID,
			String trxName) {
		super(ctx, R_ContactInterest_ID, trxName);
		/**
		 * if (R_ContactInterest_ID == 0) { setAD_User_ID (0); //
		 * 
		 * @AD_User_ID@ setR_InterestArea_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_R_ContactInterest(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=R_ContactInterest */
	public static final String Table_Name = "R_ContactInterest";

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
		StringBuffer sb = new StringBuffer("X_R_ContactInterest[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID < 1)
			throw new IllegalArgumentException("AD_User_ID is mandatory.");
		set_ValueNoCheck("AD_User_ID", new Integer(AD_User_ID));
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_User_ID()));
	}

	/**
	 * Set Opt-out Date. Date the contact opted out
	 */
	public void setOptOutDate(Timestamp OptOutDate) {
		set_ValueNoCheck("OptOutDate", OptOutDate);
	}

	/**
	 * Get Opt-out Date. Date the contact opted out
	 */
	public Timestamp getOptOutDate() {
		return (Timestamp) get_Value("OptOutDate");
	}

	/**
	 * Set Interest Area. Interest Area or Topic
	 */
	public void setR_InterestArea_ID(int R_InterestArea_ID) {
		if (R_InterestArea_ID < 1)
			throw new IllegalArgumentException(
					"R_InterestArea_ID is mandatory.");
		set_ValueNoCheck("R_InterestArea_ID", new Integer(R_InterestArea_ID));
	}

	/**
	 * Get Interest Area. Interest Area or Topic
	 */
	public int getR_InterestArea_ID() {
		Integer ii = (Integer) get_Value("R_InterestArea_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Subscribe Date. Date the contact actively subscribed
	 */
	public void setSubscribeDate(Timestamp SubscribeDate) {
		set_ValueNoCheck("SubscribeDate", SubscribeDate);
	}

	/**
	 * Get Subscribe Date. Date the contact actively subscribed
	 */
	public Timestamp getSubscribeDate() {
		return (Timestamp) get_Value("SubscribeDate");
	}
}
