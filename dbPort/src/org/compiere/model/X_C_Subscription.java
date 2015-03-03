/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Subscription
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.578
 */
public class X_C_Subscription extends PO {
	/** Standard Constructor */
	public X_C_Subscription(Properties ctx, int C_Subscription_ID,
			String trxName) {
		super(ctx, C_Subscription_ID, trxName);
		/**
		 * if (C_Subscription_ID == 0) { setC_BPartner_ID (0);
		 * setC_SubscriptionType_ID (0); setC_Subscription_ID (0); setIsDue
		 * (false); setM_Product_ID (0); setName (null); setPaidUntilDate (new
		 * Timestamp(System.currentTimeMillis())); setRenewalDate (new
		 * Timestamp(System.currentTimeMillis())); setStartDate (new
		 * Timestamp(System.currentTimeMillis())); }
		 */
	}

	/** Load Constructor */
	public X_C_Subscription(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Subscription */
	public static final String Table_Name = "C_Subscription";

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
		StringBuffer sb = new StringBuffer("X_C_Subscription[")
				.append(get_ID()).append("]");
		return sb.toString();
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
	 * Set Subscription Type. Type of subscription
	 */
	public void setC_SubscriptionType_ID(int C_SubscriptionType_ID) {
		if (C_SubscriptionType_ID < 1)
			throw new IllegalArgumentException(
					"C_SubscriptionType_ID is mandatory.");
		set_Value("C_SubscriptionType_ID", new Integer(C_SubscriptionType_ID));
	}

	/**
	 * Get Subscription Type. Type of subscription
	 */
	public int getC_SubscriptionType_ID() {
		Integer ii = (Integer) get_Value("C_SubscriptionType_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Subscription. Subscription of a Business Partner of a Product to
	 * renew
	 */
	public void setC_Subscription_ID(int C_Subscription_ID) {
		if (C_Subscription_ID < 1)
			throw new IllegalArgumentException(
					"C_Subscription_ID is mandatory.");
		set_ValueNoCheck("C_Subscription_ID", new Integer(C_Subscription_ID));
	}

	/**
	 * Get Subscription. Subscription of a Business Partner of a Product to
	 * renew
	 */
	public int getC_Subscription_ID() {
		Integer ii = (Integer) get_Value("C_Subscription_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Due. Subscription Renewal is Due
	 */
	public void setIsDue(boolean IsDue) {
		set_Value("IsDue", new Boolean(IsDue));
	}

	/**
	 * Get Due. Subscription Renewal is Due
	 */
	public boolean isDue() {
		Object oo = get_Value("IsDue");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID < 1)
			throw new IllegalArgumentException("M_Product_ID is mandatory.");
		set_Value("M_Product_ID", new Integer(M_Product_ID));
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

	/**
	 * Set Paid Until. Subscription is paid/valid until this date
	 */
	public void setPaidUntilDate(Timestamp PaidUntilDate) {
		if (PaidUntilDate == null)
			throw new IllegalArgumentException("PaidUntilDate is mandatory.");
		set_Value("PaidUntilDate", PaidUntilDate);
	}

	/**
	 * Get Paid Until. Subscription is paid/valid until this date
	 */
	public Timestamp getPaidUntilDate() {
		return (Timestamp) get_Value("PaidUntilDate");
	}

	/** Set Renewal Date */
	public void setRenewalDate(Timestamp RenewalDate) {
		if (RenewalDate == null)
			throw new IllegalArgumentException("RenewalDate is mandatory.");
		set_Value("RenewalDate", RenewalDate);
	}

	/** Get Renewal Date */
	public Timestamp getRenewalDate() {
		return (Timestamp) get_Value("RenewalDate");
	}

	/**
	 * Set Start Date. First effective day (inclusive)
	 */
	public void setStartDate(Timestamp StartDate) {
		if (StartDate == null)
			throw new IllegalArgumentException("StartDate is mandatory.");
		set_Value("StartDate", StartDate);
	}

	/**
	 * Get Start Date. First effective day (inclusive)
	 */
	public Timestamp getStartDate() {
		return (Timestamp) get_Value("StartDate");
	}
}
