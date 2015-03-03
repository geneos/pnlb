/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_SubscriptionType
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.593
 */
public class X_C_SubscriptionType extends PO {
	/** Standard Constructor */
	public X_C_SubscriptionType(Properties ctx, int C_SubscriptionType_ID,
			String trxName) {
		super(ctx, C_SubscriptionType_ID, trxName);
		/**
		 * if (C_SubscriptionType_ID == 0) { setC_SubscriptionType_ID (0);
		 * setFrequency (0); setFrequencyType (null); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_C_SubscriptionType(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_SubscriptionType */
	public static final String Table_Name = "C_SubscriptionType";

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
		StringBuffer sb = new StringBuffer("X_C_SubscriptionType[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Subscription Type. Type of subscription
	 */
	public void setC_SubscriptionType_ID(int C_SubscriptionType_ID) {
		if (C_SubscriptionType_ID < 1)
			throw new IllegalArgumentException(
					"C_SubscriptionType_ID is mandatory.");
		set_ValueNoCheck("C_SubscriptionType_ID", new Integer(
				C_SubscriptionType_ID));
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
	 * Set Frequency. Frequency of events
	 */
	public void setFrequency(int Frequency) {
		set_Value("Frequency", new Integer(Frequency));
	}

	/**
	 * Get Frequency. Frequency of events
	 */
	public int getFrequency() {
		Integer ii = (Integer) get_Value("Frequency");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** FrequencyType AD_Reference_ID=221 */
	public static final int FREQUENCYTYPE_AD_Reference_ID = 221;

	/** Day = D */
	public static final String FREQUENCYTYPE_Day = "D";

	/** Hour = H */
	public static final String FREQUENCYTYPE_Hour = "H";

	/** Minute = M */
	public static final String FREQUENCYTYPE_Minute = "M";

	/**
	 * Set Frequency Type. Frequency of event
	 */
	public void setFrequencyType(String FrequencyType) {
		if (FrequencyType == null)
			throw new IllegalArgumentException("FrequencyType is mandatory");
		if (FrequencyType.equals("D") || FrequencyType.equals("H")
				|| FrequencyType.equals("M"))
			;
		else
			throw new IllegalArgumentException("FrequencyType Invalid value - "
					+ FrequencyType + " - Reference_ID=221 - D - H - M");
		if (FrequencyType.length() > 1) {
			log.warning("Length > 1 - truncated");
			FrequencyType = FrequencyType.substring(0, 0);
		}
		set_Value("FrequencyType", FrequencyType);
	}

	/**
	 * Get Frequency Type. Frequency of event
	 */
	public String getFrequencyType() {
		return (String) get_Value("FrequencyType");
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
