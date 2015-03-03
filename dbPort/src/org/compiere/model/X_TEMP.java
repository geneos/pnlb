/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for TEMP
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.843
 */
public class X_TEMP extends PO {
	/** Standard Constructor */
	public X_TEMP(Properties ctx, int TEMP_ID, String trxName) {
		super(ctx, TEMP_ID, trxName);
		/**
		 * if (TEMP_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_TEMP(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=TEMP */
	public static final String Table_Name = "TEMP";

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
		StringBuffer sb = new StringBuffer("X_TEMP[").append(get_ID()).append(
				"]");
		return sb.toString();
	}

	/** Set NRO_HOJA */
	public void setNRO_HOJA(BigDecimal NRO_HOJA) {
		set_Value("NRO_HOJA", NRO_HOJA);
	}

	/** Get NRO_HOJA */
	public BigDecimal getNRO_HOJA() {
		BigDecimal bd = (BigDecimal) get_Value("NRO_HOJA");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set TEMPDATE */
	public void setTEMPDATE(Timestamp TEMPDATE) {
		set_Value("TEMPDATE", TEMPDATE);
	}

	/** Get TEMPDATE */
	public Timestamp getTEMPDATE() {
		return (Timestamp) get_Value("TEMPDATE");
	}
}
