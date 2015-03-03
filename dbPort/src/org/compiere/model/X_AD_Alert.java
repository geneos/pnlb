/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Alert
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.031
 */
public class X_AD_Alert extends PO {
	/** Standard Constructor */
	public X_AD_Alert(Properties ctx, int AD_Alert_ID, String trxName) {
		super(ctx, AD_Alert_ID, trxName);
		/**
		 * if (AD_Alert_ID == 0) { setAD_AlertProcessor_ID (0); setAD_Alert_ID
		 * (0); setAlertMessage (null); setAlertSubject (null);
		 * setEnforceClientSecurity (true); // Y setEnforceRoleSecurity (true); //
		 * Y setIsValid (true); // Y setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Alert(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Alert */
	public static final String Table_Name = "AD_Alert";

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
		StringBuffer sb = new StringBuffer("X_AD_Alert[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Alert Processor. Alert Processor/Server Parameter
	 */
	public void setAD_AlertProcessor_ID(int AD_AlertProcessor_ID) {
		if (AD_AlertProcessor_ID < 1)
			throw new IllegalArgumentException(
					"AD_AlertProcessor_ID is mandatory.");
		set_Value("AD_AlertProcessor_ID", new Integer(AD_AlertProcessor_ID));
	}

	/**
	 * Get Alert Processor. Alert Processor/Server Parameter
	 */
	public int getAD_AlertProcessor_ID() {
		Integer ii = (Integer) get_Value("AD_AlertProcessor_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Alert. Compiere Alert
	 */
	public void setAD_Alert_ID(int AD_Alert_ID) {
		if (AD_Alert_ID < 1)
			throw new IllegalArgumentException("AD_Alert_ID is mandatory.");
		set_ValueNoCheck("AD_Alert_ID", new Integer(AD_Alert_ID));
	}

	/**
	 * Get Alert. Compiere Alert
	 */
	public int getAD_Alert_ID() {
		Integer ii = (Integer) get_Value("AD_Alert_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Alert Message. Message of the Alert
	 */
	public void setAlertMessage(String AlertMessage) {
		if (AlertMessage == null)
			throw new IllegalArgumentException("AlertMessage is mandatory.");
		if (AlertMessage.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			AlertMessage = AlertMessage.substring(0, 1999);
		}
		set_Value("AlertMessage", AlertMessage);
	}

	/**
	 * Get Alert Message. Message of the Alert
	 */
	public String getAlertMessage() {
		return (String) get_Value("AlertMessage");
	}

	/**
	 * Set Alert Subject. Subject of the Alert
	 */
	public void setAlertSubject(String AlertSubject) {
		if (AlertSubject == null)
			throw new IllegalArgumentException("AlertSubject is mandatory.");
		if (AlertSubject.length() > 60) {
			log.warning("Length > 60 - truncated");
			AlertSubject = AlertSubject.substring(0, 59);
		}
		set_Value("AlertSubject", AlertSubject);
	}

	/**
	 * Get Alert Subject. Subject of the Alert
	 */
	public String getAlertSubject() {
		return (String) get_Value("AlertSubject");
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
	 * Set Enforce Client Security. Send alerts to recipient only if the client
	 * security rules of the role allows
	 */
	public void setEnforceClientSecurity(boolean EnforceClientSecurity) {
		set_Value("EnforceClientSecurity", new Boolean(EnforceClientSecurity));
	}

	/**
	 * Get Enforce Client Security. Send alerts to recipient only if the client
	 * security rules of the role allows
	 */
	public boolean isEnforceClientSecurity() {
		Object oo = get_Value("EnforceClientSecurity");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Enforce Role Security. Send alerts to recipient only if the data
	 * security rules of the role allows
	 */
	public void setEnforceRoleSecurity(boolean EnforceRoleSecurity) {
		set_Value("EnforceRoleSecurity", new Boolean(EnforceRoleSecurity));
	}

	/**
	 * Get Enforce Role Security. Send alerts to recipient only if the data
	 * security rules of the role allows
	 */
	public boolean isEnforceRoleSecurity() {
		Object oo = get_Value("EnforceRoleSecurity");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Comment/Help. Comment or Hint
	 */
	public void setHelp(String Help) {
		if (Help != null && Help.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Help = Help.substring(0, 1999);
		}
		set_Value("Help", Help);
	}

	/**
	 * Get Comment/Help. Comment or Hint
	 */
	public String getHelp() {
		return (String) get_Value("Help");
	}

	/**
	 * Set Valid. Element is valid
	 */
	public void setIsValid(boolean IsValid) {
		set_Value("IsValid", new Boolean(IsValid));
	}

	/**
	 * Get Valid. Element is valid
	 */
	public boolean isValid() {
		Object oo = get_Value("IsValid");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
