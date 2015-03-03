/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_User_Substitute
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.656
 */
public class X_AD_User_Substitute extends PO {
	/** Standard Constructor */
	public X_AD_User_Substitute(Properties ctx, int AD_User_Substitute_ID,
			String trxName) {
		super(ctx, AD_User_Substitute_ID, trxName);
		/**
		 * if (AD_User_Substitute_ID == 0) { setAD_User_ID (0);
		 * setAD_User_Substitute_ID (0); setName (null); setSubstitute_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_User_Substitute(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	
	/** TableName=AD_User_Substitute */
	public static final String Table_Name = "AD_User_Substitute";
	
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
		StringBuffer sb = new StringBuffer("X_AD_User_Substitute[").append(
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

	/**
	 * Set User Substitute. Substitute of the user
	 */
	public void setAD_User_Substitute_ID(int AD_User_Substitute_ID) {
		if (AD_User_Substitute_ID < 1)
			throw new IllegalArgumentException(
					"AD_User_Substitute_ID is mandatory.");
		set_ValueNoCheck("AD_User_Substitute_ID", new Integer(
				AD_User_Substitute_ID));
	}

	/**
	 * Get User Substitute. Substitute of the user
	 */
	public int getAD_User_Substitute_ID() {
		Integer ii = (Integer) get_Value("AD_User_Substitute_ID");
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

	/** Substitute_ID AD_Reference_ID=110 */
	public static final int SUBSTITUTE_ID_AD_Reference_ID = 110;

	/**
	 * Set Substitute. Entity which can be used in place of this entity
	 */
	public void setSubstitute_ID(int Substitute_ID) {
		if (Substitute_ID < 1)
			throw new IllegalArgumentException("Substitute_ID is mandatory.");
		set_Value("Substitute_ID", new Integer(Substitute_ID));
	}

	/**
	 * Get Substitute. Entity which can be used in place of this entity
	 */
	public int getSubstitute_ID() {
		Integer ii = (Integer) get_Value("Substitute_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Valid from. Valid from including this date (first day)
	 */
	public void setValidFrom(Timestamp ValidFrom) {
		set_Value("ValidFrom", ValidFrom);
	}

	/**
	 * Get Valid from. Valid from including this date (first day)
	 */
	public Timestamp getValidFrom() {
		return (Timestamp) get_Value("ValidFrom");
	}

	/**
	 * Set Valid to. Valid to including this date (last day)
	 */
	public void setValidTo(Timestamp ValidTo) {
		set_Value("ValidTo", ValidTo);
	}

	/**
	 * Get Valid to. Valid to including this date (last day)
	 */
	public Timestamp getValidTo() {
		return (Timestamp) get_Value("ValidTo");
	}
}
