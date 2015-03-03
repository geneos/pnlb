/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_PrintFont
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.718
 */
public class X_AD_PrintFont extends PO {
	/** Standard Constructor */
	public X_AD_PrintFont(Properties ctx, int AD_PrintFont_ID, String trxName) {
		super(ctx, AD_PrintFont_ID, trxName);
		/**
		 * if (AD_PrintFont_ID == 0) { setAD_PrintFont_ID (0); setCode (null);
		 * setIsDefault (false); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_PrintFont(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_PrintFont */
	public static final String Table_Name = "AD_PrintFont";

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
		StringBuffer sb = new StringBuffer("X_AD_PrintFont[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Print Font. Maintain Print Font
	 */
	public void setAD_PrintFont_ID(int AD_PrintFont_ID) {
		if (AD_PrintFont_ID < 1)
			throw new IllegalArgumentException("AD_PrintFont_ID is mandatory.");
		set_ValueNoCheck("AD_PrintFont_ID", new Integer(AD_PrintFont_ID));
	}

	/**
	 * Get Print Font. Maintain Print Font
	 */
	public int getAD_PrintFont_ID() {
		Integer ii = (Integer) get_Value("AD_PrintFont_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Validation code. Validation Code
	 */
	public void setCode(String Code) {
		if (Code == null)
			throw new IllegalArgumentException("Code is mandatory.");
		if (Code.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Code = Code.substring(0, 1999);
		}
		set_Value("Code", Code);
	}

	/**
	 * Get Validation code. Validation Code
	 */
	public String getCode() {
		return (String) get_Value("Code");
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
