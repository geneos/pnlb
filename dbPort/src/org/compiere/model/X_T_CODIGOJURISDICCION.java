/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for T_CODIGOJURISDICCION
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:04.921
 */
public class X_T_CODIGOJURISDICCION extends PO {
	/** Standard Constructor */
	public X_T_CODIGOJURISDICCION(Properties ctx, int T_CODIGOJURISDICCION_ID,
			String trxName) {
		super(ctx, T_CODIGOJURISDICCION_ID, trxName);
		/**
		 * if (T_CODIGOJURISDICCION_ID == 0) { }
		 */
	}

	/** Load Constructor */
	public X_T_CODIGOJURISDICCION(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=T_CODIGOJURISDICCION */
	public static final String Table_Name = "T_CODIGOJURISDICCION";

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
		StringBuffer sb = new StringBuffer("X_T_CODIGOJURISDICCION[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** Set T_CODIGOJURISDICCION_ID */
	public void setT_CODIGOJURISDICCION_ID(int T_CODIGOJURISDICCION_ID) {
		if (T_CODIGOJURISDICCION_ID <= 0)
			set_ValueNoCheck("T_CODIGOJURISDICCION_ID", null);
		else
			set_ValueNoCheck("T_CODIGOJURISDICCION_ID", new Integer(
					T_CODIGOJURISDICCION_ID));
	}

	/** Get T_CODIGOJURISDICCION_ID */
	public int getT_CODIGOJURISDICCION_ID() {
		Integer ii = (Integer) get_Value("T_CODIGOJURISDICCION_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(BigDecimal Value) {
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public BigDecimal getValue() {
		BigDecimal bd = (BigDecimal) get_Value("Value");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
