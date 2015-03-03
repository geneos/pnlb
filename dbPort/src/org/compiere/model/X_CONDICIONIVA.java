/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for CONDICIONIVA
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.375
 */
public class X_CONDICIONIVA extends PO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Standard Constructor */
	public X_CONDICIONIVA(Properties ctx, int CONDICIONIVA_ID, String trxName) {
		super(ctx, CONDICIONIVA_ID, trxName);
		/**
		 * if (CONDICIONIVA_ID == 0) { setCONDICIONIVA_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_CONDICIONIVA(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=CONDICIONIVA */
	public static final String Table_Name = "CONDICIONIVA";

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
		StringBuffer sb = new StringBuffer("X_CONDICIONIVA[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** Set CONDICIONIVA_ID */
	public void setCONDICIONIVA_ID(int CONDICIONIVA_ID) {
		if (CONDICIONIVA_ID < 1)
			throw new IllegalArgumentException("CONDICIONIVA_ID is mandatory.");
		set_ValueNoCheck("CONDICIONIVA_ID", new Integer(CONDICIONIVA_ID));
	}

	/** Get CONDICIONIVA_ID */
	public int getCONDICIONIVA_ID() {
		Integer ii = (Integer) get_Value("CONDICIONIVA_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 120) {
			log.warning("Length > 120 - truncated");
			Name = Name.substring(0, 119);
		}
		set_Value("Name", Name);
	}

	/**
	 * Get Name. Alphanumeric identifier of the entity
	 */
	public String getName() {
		return (String) get_Value("Name");
	}

	/**
	 * Set IVACODE. Alphanumeric identifier of the entity
	 */
	public void setIVACode(String code) {
		if (code == null)
			throw new IllegalArgumentException("IVACode is mandatory.");
		set_Value("IVACODE", code);
	}

	/**
	 * Get IVACODE. Alphanumeric identifier of the entity
	 */
	public String getIVACode() {
		return (String) get_Value("IVACODE");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
	}
}
