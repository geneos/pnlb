/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for A_RegistrationValue
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.265
 */
public class X_A_RegistrationValue extends PO {
	/** Standard Constructor */
	public X_A_RegistrationValue(Properties ctx, int A_RegistrationValue_ID,
			String trxName) {
		super(ctx, A_RegistrationValue_ID, trxName);
		/**
		 * if (A_RegistrationValue_ID == 0) { setA_RegistrationAttribute_ID (0);
		 * setA_Registration_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_A_RegistrationValue(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=A_RegistrationValue */
	public static final String Table_Name = "A_RegistrationValue";

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
		StringBuffer sb = new StringBuffer("X_A_RegistrationValue[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Registration Attribute. Asset Registration Attribute
	 */
	public void setA_RegistrationAttribute_ID(int A_RegistrationAttribute_ID) {
		if (A_RegistrationAttribute_ID < 1)
			throw new IllegalArgumentException(
					"A_RegistrationAttribute_ID is mandatory.");
		set_ValueNoCheck("A_RegistrationAttribute_ID", new Integer(
				A_RegistrationAttribute_ID));
	}

	/**
	 * Get Registration Attribute. Asset Registration Attribute
	 */
	public int getA_RegistrationAttribute_ID() {
		Integer ii = (Integer) get_Value("A_RegistrationAttribute_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getA_RegistrationAttribute_ID()));
	}

	/**
	 * Set Registration. User Asset Registration
	 */
	public void setA_Registration_ID(int A_Registration_ID) {
		if (A_Registration_ID < 1)
			throw new IllegalArgumentException(
					"A_Registration_ID is mandatory.");
		set_ValueNoCheck("A_Registration_ID", new Integer(A_Registration_ID));
	}

	/**
	 * Get Registration. User Asset Registration
	 */
	public int getA_Registration_ID() {
		Integer ii = (Integer) get_Value("A_Registration_ID");
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
}
