/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Error
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.39
 */
public class X_AD_Error extends PO {
	/** Standard Constructor */
	public X_AD_Error(Properties ctx, int AD_Error_ID, String trxName) {
		super(ctx, AD_Error_ID, trxName);
		/**
		 * if (AD_Error_ID == 0) { setAD_Error_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Error(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Error */
	public static final String Table_Name = "AD_Error";

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
		StringBuffer sb = new StringBuffer("X_AD_Error[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** Set Error */
	public void setAD_Error_ID(int AD_Error_ID) {
		if (AD_Error_ID < 1)
			throw new IllegalArgumentException("AD_Error_ID is mandatory.");
		set_ValueNoCheck("AD_Error_ID", new Integer(AD_Error_ID));
	}

	/** Get Error */
	public int getAD_Error_ID() {
		Integer ii = (Integer) get_Value("AD_Error_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Language AD_Reference_ID=106 */
	public static final int AD_LANGUAGE_AD_Reference_ID = 106;

	/**
	 * Set Language. Language for this entity
	 */
	public void setAD_Language(String AD_Language) {
		if (AD_Language != null && AD_Language.length() > 6) {
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
	 * Set Validation code. Validation Code
	 */
	public void setCode(String Code) {
		if (Code != null && Code.length() > 2000) {
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
