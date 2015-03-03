/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Greeting
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.421
 */
public class X_C_Greeting extends PO {
	/** Standard Constructor */
	public X_C_Greeting(Properties ctx, int C_Greeting_ID, String trxName) {
		super(ctx, C_Greeting_ID, trxName);
		/**
		 * if (C_Greeting_ID == 0) { setC_Greeting_ID (0); setIsDefault (false);
		 * setIsFirstNameOnly (false); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_C_Greeting(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Greeting */
	public static final String Table_Name = "C_Greeting";

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
		StringBuffer sb = new StringBuffer("X_C_Greeting[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Greeting. Greeting to print on correspondence
	 */
	public void setC_Greeting_ID(int C_Greeting_ID) {
		if (C_Greeting_ID < 1)
			throw new IllegalArgumentException("C_Greeting_ID is mandatory.");
		set_ValueNoCheck("C_Greeting_ID", new Integer(C_Greeting_ID));
	}

	/**
	 * Get Greeting. Greeting to print on correspondence
	 */
	public int getC_Greeting_ID() {
		Integer ii = (Integer) get_Value("C_Greeting_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Greeting. For letters, e.g. "Dear { 0} " or "Dear Mr. { 0} " - At
	 * runtime, " { 0} " is replaced by the name
	 */
	public void setGreeting(String Greeting) {
		if (Greeting != null && Greeting.length() > 60) {
			log.warning("Length > 60 - truncated");
			Greeting = Greeting.substring(0, 59);
		}
		set_Value("Greeting", Greeting);
	}

	/**
	 * Get Greeting. For letters, e.g. "Dear { 0} " or "Dear Mr. { 0} " - At
	 * runtime, " { 0} " is replaced by the name
	 */
	public String getGreeting() {
		return (String) get_Value("Greeting");
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
	 * Set First name only. Print only the first name in greetings
	 */
	public void setIsFirstNameOnly(boolean IsFirstNameOnly) {
		set_Value("IsFirstNameOnly", new Boolean(IsFirstNameOnly));
	}

	/**
	 * Get First name only. Print only the first name in greetings
	 */
	public boolean isFirstNameOnly() {
		Object oo = get_Value("IsFirstNameOnly");
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
