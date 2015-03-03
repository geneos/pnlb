/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Subscription_Delivery
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.609
 */
public class X_C_Subscription_Delivery extends PO {
	/** Standard Constructor */
	public X_C_Subscription_Delivery(Properties ctx,
			int C_Subscription_Delivery_ID, String trxName) {
		super(ctx, C_Subscription_Delivery_ID, trxName);
		/**
		 * if (C_Subscription_Delivery_ID == 0) { setC_Subscription_Delivery_ID
		 * (0); setC_Subscription_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_C_Subscription_Delivery(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Subscription_Delivery */
	public static final String Table_Name = "C_Subscription_Delivery";

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
		StringBuffer sb = new StringBuffer("X_C_Subscription_Delivery[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Subscription Delivery. Optional Delivery Record for a Subscription
	 */
	public void setC_Subscription_Delivery_ID(int C_Subscription_Delivery_ID) {
		if (C_Subscription_Delivery_ID < 1)
			throw new IllegalArgumentException(
					"C_Subscription_Delivery_ID is mandatory.");
		set_ValueNoCheck("C_Subscription_Delivery_ID", new Integer(
				C_Subscription_Delivery_ID));
	}

	/**
	 * Get Subscription Delivery. Optional Delivery Record for a Subscription
	 */
	public int getC_Subscription_Delivery_ID() {
		Integer ii = (Integer) get_Value("C_Subscription_Delivery_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getC_Subscription_Delivery_ID()));
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
}
