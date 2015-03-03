/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_UserDef_Win
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.609
 */
public class X_AD_UserDef_Win extends PO {
	/** Standard Constructor */
	public X_AD_UserDef_Win(Properties ctx, int AD_UserDef_Win_ID,
			String trxName) {
		super(ctx, AD_UserDef_Win_ID, trxName);
		/**
		 * if (AD_UserDef_Win_ID == 0) { setAD_Language (null);
		 * setAD_UserDef_Win_ID (0); setAD_Window_ID (0); setIsDefault (false);
		 * setIsReadOnly (false); setIsUserUpdateable (false); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_UserDef_Win(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_UserDef_Win */
	public static final String Table_Name = "AD_UserDef_Win";

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
		StringBuffer sb = new StringBuffer("X_AD_UserDef_Win[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/** AD_Language AD_Reference_ID=106 */
	public static final int AD_LANGUAGE_AD_Reference_ID = 106;

	/**
	 * Set Language. Language for this entity
	 */
	public void setAD_Language(String AD_Language) {
		if (AD_Language.length() > 6) {
			log.warning("Length > 6 - truncated");
			AD_Language = AD_Language.substring(0, 5);
		}
		set_Value("AD_Language", AD_Language);
	}

	/**
	 * Get Language. Language for this entity
	 */
	public String getAD_Language() {
		return (String) get_Value("AD_Language");
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

	/** Set User defined Window */
	public void setAD_UserDef_Win_ID(int AD_UserDef_Win_ID) {
		if (AD_UserDef_Win_ID < 1)
			throw new IllegalArgumentException(
					"AD_UserDef_Win_ID is mandatory.");
		set_ValueNoCheck("AD_UserDef_Win_ID", new Integer(AD_UserDef_Win_ID));
	}

	/** Get User defined Window */
	public int getAD_UserDef_Win_ID() {
		Integer ii = (Integer) get_Value("AD_UserDef_Win_ID");
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
		if (AD_Window_ID < 1)
			throw new IllegalArgumentException("AD_Window_ID is mandatory.");
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
	 * Set Default. Default value
	 */
	public void setIsDefault(boolean IsDefault) {
		set_Value("IsDefault", new Boolean(IsDefault));
	}

	/**
	 * Get Default. Default value
	 */
	public boolean isDefault() {
		Object oo = get_Value("IsDefault");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Read Only. Field is read only
	 */
	public void setIsReadOnly(boolean IsReadOnly) {
		set_Value("IsReadOnly", new Boolean(IsReadOnly));
	}

	/**
	 * Get Read Only. Field is read only
	 */
	public boolean isReadOnly() {
		Object oo = get_Value("IsReadOnly");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set User updateable. The field can be updated by the user
	 */
	public void setIsUserUpdateable(boolean IsUserUpdateable) {
		set_Value("IsUserUpdateable", new Boolean(IsUserUpdateable));
	}

	/**
	 * Get User updateable. The field can be updated by the user
	 */
	public boolean isUserUpdateable() {
		Object oo = get_Value("IsUserUpdateable");
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
