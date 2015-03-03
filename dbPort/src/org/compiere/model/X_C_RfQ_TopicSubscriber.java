/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_RfQ_TopicSubscriber
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.515
 */
public class X_C_RfQ_TopicSubscriber extends PO {
	/** Standard Constructor */
	public X_C_RfQ_TopicSubscriber(Properties ctx,
			int C_RfQ_TopicSubscriber_ID, String trxName) {
		super(ctx, C_RfQ_TopicSubscriber_ID, trxName);
		/**
		 * if (C_RfQ_TopicSubscriber_ID == 0) { setC_BPartner_ID (0);
		 * setC_BPartner_Location_ID (0); setC_RfQ_TopicSubscriber_ID (0);
		 * setC_RfQ_Topic_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_C_RfQ_TopicSubscriber(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_RfQ_TopicSubscriber */
	public static final String Table_Name = "C_RfQ_TopicSubscriber";

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
		StringBuffer sb = new StringBuffer("X_C_RfQ_TopicSubscriber[").append(
				get_ID()).append("]");
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
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID < 1)
			throw new IllegalArgumentException("C_BPartner_ID is mandatory.");
		set_Value("C_BPartner_ID", new Integer(C_BPartner_ID));
	}

	/**
	 * Get Business Partner . Identifies a Business Partner
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public void setC_BPartner_Location_ID(int C_BPartner_Location_ID) {
		if (C_BPartner_Location_ID < 1)
			throw new IllegalArgumentException(
					"C_BPartner_Location_ID is mandatory.");
		set_Value("C_BPartner_Location_ID", new Integer(C_BPartner_Location_ID));
	}

	/**
	 * Get Partner Location. Identifies the (ship to) address for this Business
	 * Partner
	 */
	public int getC_BPartner_Location_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_Location_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set RfQ Subscriber. Request for Quotation Topic Subscriber
	 */
	public void setC_RfQ_TopicSubscriber_ID(int C_RfQ_TopicSubscriber_ID) {
		if (C_RfQ_TopicSubscriber_ID < 1)
			throw new IllegalArgumentException(
					"C_RfQ_TopicSubscriber_ID is mandatory.");
		set_ValueNoCheck("C_RfQ_TopicSubscriber_ID", new Integer(
				C_RfQ_TopicSubscriber_ID));
	}

	/**
	 * Get RfQ Subscriber. Request for Quotation Topic Subscriber
	 */
	public int getC_RfQ_TopicSubscriber_ID() {
		Integer ii = (Integer) get_Value("C_RfQ_TopicSubscriber_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set RfQ Topic. Topic for Request for Quotations
	 */
	public void setC_RfQ_Topic_ID(int C_RfQ_Topic_ID) {
		if (C_RfQ_Topic_ID < 1)
			throw new IllegalArgumentException("C_RfQ_Topic_ID is mandatory.");
		set_ValueNoCheck("C_RfQ_Topic_ID", new Integer(C_RfQ_Topic_ID));
	}

	/**
	 * Get RfQ Topic. Topic for Request for Quotations
	 */
	public int getC_RfQ_Topic_ID() {
		Integer ii = (Integer) get_Value("C_RfQ_Topic_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_RfQ_Topic_ID()));
	}

	/**
	 * Set Opt-out Date. Date the contact opted out
	 */
	public void setOptOutDate(Timestamp OptOutDate) {
		set_Value("OptOutDate", OptOutDate);
	}

	/**
	 * Get Opt-out Date. Date the contact opted out
	 */
	public Timestamp getOptOutDate() {
		return (Timestamp) get_Value("OptOutDate");
	}

	/**
	 * Set Subscribe Date. Date the contact actively subscribed
	 */
	public void setSubscribeDate(Timestamp SubscribeDate) {
		set_Value("SubscribeDate", SubscribeDate);
	}

	/**
	 * Get Subscribe Date. Date the contact actively subscribed
	 */
	public Timestamp getSubscribeDate() {
		return (Timestamp) get_Value("SubscribeDate");
	}
}
