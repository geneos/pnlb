/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_EDI_Info
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.734
 */
public class X_M_EDI_Info extends PO {
	/** Standard Constructor */
	public X_M_EDI_Info(Properties ctx, int M_EDI_Info_ID, String trxName) {
		super(ctx, M_EDI_Info_ID, trxName);
		/**
		 * if (M_EDI_Info_ID == 0) { setInfo (null); setM_EDI_ID (0);
		 * setM_EDI_Info_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_M_EDI_Info(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_EDI_Info */
	public static final String Table_Name = "M_EDI_Info";

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
		StringBuffer sb = new StringBuffer("X_M_EDI_Info[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Info. Information
	 */
	public void setInfo(String Info) {
		if (Info == null)
			throw new IllegalArgumentException("Info is mandatory.");
		if (Info.length() > 4000) {
			log.warning("Length > 4000 - truncated");
			Info = Info.substring(0, 3999);
		}
		set_Value("Info", Info);
	}

	/**
	 * Get Info. Information
	 */
	public String getInfo() {
		return (String) get_Value("Info");
	}

	/** Set EDI Transaction */
	public void setM_EDI_ID(int M_EDI_ID) {
		if (M_EDI_ID < 1)
			throw new IllegalArgumentException("M_EDI_ID is mandatory.");
		set_ValueNoCheck("M_EDI_ID", new Integer(M_EDI_ID));
	}

	/** Get EDI Transaction */
	public int getM_EDI_ID() {
		Integer ii = (Integer) get_Value("M_EDI_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getM_EDI_ID()));
	}

	/** Set EDI Log */
	public void setM_EDI_Info_ID(int M_EDI_Info_ID) {
		if (M_EDI_Info_ID < 1)
			throw new IllegalArgumentException("M_EDI_Info_ID is mandatory.");
		set_ValueNoCheck("M_EDI_Info_ID", new Integer(M_EDI_Info_ID));
	}

	/** Get EDI Log */
	public int getM_EDI_Info_ID() {
		Integer ii = (Integer) get_Value("M_EDI_Info_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
