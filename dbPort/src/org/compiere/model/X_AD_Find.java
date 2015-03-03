/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Find
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.437
 */
public class X_AD_Find extends PO {
	/** Standard Constructor */
	public X_AD_Find(Properties ctx, int AD_Find_ID, String trxName) {
		super(ctx, AD_Find_ID, trxName);
		/**
		 * if (AD_Find_ID == 0) { setAD_Column_ID (0); setAD_Find_ID (0);
		 * setAndOr (null); // A setFind_ID (Env.ZERO); setOperation (null); // ==
		 * setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Find(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Find */
	public static final String Table_Name = "AD_Find";

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
	
	protected BigDecimal accessLevel = new BigDecimal(7);

	/** AccessLevel 7 - System - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_Find[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AD_Column_ID AD_Reference_ID=251 */
	public static final int AD_COLUMN_ID_AD_Reference_ID = 251;

	/**
	 * Set Column. Column in the table
	 */
	public void setAD_Column_ID(int AD_Column_ID) {
		if (AD_Column_ID < 1)
			throw new IllegalArgumentException("AD_Column_ID is mandatory.");
		set_Value("AD_Column_ID", new Integer(AD_Column_ID));
	}

	/**
	 * Get Column. Column in the table
	 */
	public int getAD_Column_ID() {
		Integer ii = (Integer) get_Value("AD_Column_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Find */
	public void setAD_Find_ID(int AD_Find_ID) {
		if (AD_Find_ID < 1)
			throw new IllegalArgumentException("AD_Find_ID is mandatory.");
		set_ValueNoCheck("AD_Find_ID", new Integer(AD_Find_ID));
	}

	/** Get Find */
	public int getAD_Find_ID() {
		Integer ii = (Integer) get_Value("AD_Find_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_Find_ID()));
	}

	/** AndOr AD_Reference_ID=204 */
	public static final int ANDOR_AD_Reference_ID = 204;

	/** And = A */
	public static final String ANDOR_And = "A";

	/** Or = O */
	public static final String ANDOR_Or = "O";

	/**
	 * Set And/Or. Logical operation: AND or OR
	 */
	public void setAndOr(String AndOr) {
		if (AndOr == null)
			throw new IllegalArgumentException("AndOr is mandatory");
		if (AndOr.equals("A") || AndOr.equals("O"))
			;
		else
			throw new IllegalArgumentException("AndOr Invalid value - " + AndOr
					+ " - Reference_ID=204 - A - O");
		if (AndOr.length() > 1) {
			log.warning("Length > 1 - truncated");
			AndOr = AndOr.substring(0, 0);
		}
		set_Value("AndOr", AndOr);
	}

	/**
	 * Get And/Or. Logical operation: AND or OR
	 */
	public String getAndOr() {
		return (String) get_Value("AndOr");
	}

	/** Set Find_ID */
	public void setFind_ID(BigDecimal Find_ID) {
		if (Find_ID == null)
			throw new IllegalArgumentException("Find_ID is mandatory.");
		set_Value("Find_ID", Find_ID);
	}

	/** Get Find_ID */
	public BigDecimal getFind_ID() {
		BigDecimal bd = (BigDecimal) get_Value("Find_ID");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Operation AD_Reference_ID=205 */
	public static final int OPERATION_AD_Reference_ID = 205;

	/** != = != */
	public static final String OPERATION_NotEq = "!=";

	/** < = << */
	public static final String OPERATION_Le = "<<";

	/** <= = <= */
	public static final String OPERATION_LeEq = "<=";

	/** = = == */
	public static final String OPERATION_Eq = "==";

	/** >= = >= */
	public static final String OPERATION_GtEq = ">=";

	/** > = >> */
	public static final String OPERATION_Gt = ">>";

	/** |<x>| = AB */
	public static final String OPERATION_X = "AB";

	/** sql = SQ */
	public static final String OPERATION_Sql = "SQ";

	/** ~ = ~~ */
	public static final String OPERATION_Like = "~~";

	/**
	 * Set Operation. Compare Operation
	 */
	public void setOperation(String Operation) {
		if (Operation == null)
			throw new IllegalArgumentException("Operation is mandatory");
		if (Operation.equals("!=") || Operation.equals("<<")
				|| Operation.equals("<=") || Operation.equals("==")
				|| Operation.equals(">=") || Operation.equals(">>")
				|| Operation.equals("AB") || Operation.equals("SQ")
				|| Operation.equals("~~"))
			;
		else
			throw new IllegalArgumentException(
					"Operation Invalid value - "
							+ Operation
							+ " - Reference_ID=205 - != - << - <= - == - >= - >> - AB - SQ - ~~");
		if (Operation.length() > 2) {
			log.warning("Length > 2 - truncated");
			Operation = Operation.substring(0, 1);
		}
		set_Value("Operation", Operation);
	}

	/**
	 * Get Operation. Compare Operation
	 */
	public String getOperation() {
		return (String) get_Value("Operation");
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value = Value.substring(0, 39);
		}
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public String getValue() {
		return (String) get_Value("Value");
	}

	/**
	 * Set Value To. Value To
	 */
	public void setValue2(String Value2) {
		if (Value2 != null && Value2.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value2 = Value2.substring(0, 39);
		}
		set_Value("Value2", Value2);
	}

	/**
	 * Get Value To. Value To
	 */
	public String getValue2() {
		return (String) get_Value("Value2");
	}
}
