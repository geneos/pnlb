/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_SchedulerRecipient
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.296
 */
public class X_AD_SchedulerRecipient extends PO {
	/** Standard Constructor */
	public X_AD_SchedulerRecipient(Properties ctx,
			int AD_SchedulerRecipient_ID, String trxName) {
		super(ctx, AD_SchedulerRecipient_ID, trxName);
		/**
		 * if (AD_SchedulerRecipient_ID == 0) { setAD_SchedulerRecipient_ID (0);
		 * setAD_Scheduler_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_SchedulerRecipient(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_SchedulerRecipient */
	public static final String Table_Name = "AD_SchedulerRecipient";

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
		StringBuffer sb = new StringBuffer("X_AD_SchedulerRecipient[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Role. Responsibility Role
	 */
	public void setAD_Role_ID(int AD_Role_ID) {
		if (AD_Role_ID <= 0)
			set_Value("AD_Role_ID", null);
		else
			set_Value("AD_Role_ID", new Integer(AD_Role_ID));
	}

	/**
	 * Get Role. Responsibility Role
	 */
	public int getAD_Role_ID() {
		Integer ii = (Integer) get_Value("AD_Role_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Scheduler Recipient. Recipient of the Scheduler Notification
	 */
	public void setAD_SchedulerRecipient_ID(int AD_SchedulerRecipient_ID) {
		if (AD_SchedulerRecipient_ID < 1)
			throw new IllegalArgumentException(
					"AD_SchedulerRecipient_ID is mandatory.");
		set_ValueNoCheck("AD_SchedulerRecipient_ID", new Integer(
				AD_SchedulerRecipient_ID));
	}

	/**
	 * Get Scheduler Recipient. Recipient of the Scheduler Notification
	 */
	public int getAD_SchedulerRecipient_ID() {
		Integer ii = (Integer) get_Value("AD_SchedulerRecipient_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Scheduler. Schedule Processes
	 */
	public void setAD_Scheduler_ID(int AD_Scheduler_ID) {
		if (AD_Scheduler_ID < 1)
			throw new IllegalArgumentException("AD_Scheduler_ID is mandatory.");
		set_ValueNoCheck("AD_Scheduler_ID", new Integer(AD_Scheduler_ID));
	}

	/**
	 * Get Scheduler. Schedule Processes
	 */
	public int getAD_Scheduler_ID() {
		Integer ii = (Integer) get_Value("AD_Scheduler_ID");
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_User_ID()));
	}
}
