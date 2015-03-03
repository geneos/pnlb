/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_SerNoCtlExclude
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.687
 */
public class X_M_SerNoCtlExclude extends PO {
	/** Standard Constructor */
	public X_M_SerNoCtlExclude(Properties ctx, int M_SerNoCtlExclude_ID,
			String trxName) {
		super(ctx, M_SerNoCtlExclude_ID, trxName);
		/**
		 * if (M_SerNoCtlExclude_ID == 0) { setAD_Table_ID (0); setIsSOTrx
		 * (false); setM_SerNoCtlExclude_ID (0); setM_SerNoCtl_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_M_SerNoCtlExclude(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_SerNoCtlExclude */
	public static final String Table_Name = "M_SerNoCtlExclude";

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

	protected BigDecimal accessLevel = new BigDecimal(2);

	/** AccessLevel 2 - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_M_SerNoCtlExclude[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID < 1)
			throw new IllegalArgumentException("AD_Table_ID is mandatory.");
		set_Value("AD_Table_ID", new Integer(AD_Table_ID));
	}

	/**
	 * Get Table. Table for the Fields
	 */
	public int getAD_Table_ID() {
		Integer ii = (Integer) get_Value("AD_Table_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Sales Transaction. This is a Sales Transaction
	 */
	public void setIsSOTrx(boolean IsSOTrx) {
		set_Value("IsSOTrx", new Boolean(IsSOTrx));
	}

	/**
	 * Get Sales Transaction. This is a Sales Transaction
	 */
	public boolean isSOTrx() {
		Object oo = get_Value("IsSOTrx");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Exclude SerNo. Exclude the ability to create Serial Numbers in
	 * Attribute Sets
	 */
	public void setM_SerNoCtlExclude_ID(int M_SerNoCtlExclude_ID) {
		if (M_SerNoCtlExclude_ID < 1)
			throw new IllegalArgumentException(
					"M_SerNoCtlExclude_ID is mandatory.");
		set_ValueNoCheck("M_SerNoCtlExclude_ID", new Integer(
				M_SerNoCtlExclude_ID));
	}

	/**
	 * Get Exclude SerNo. Exclude the ability to create Serial Numbers in
	 * Attribute Sets
	 */
	public int getM_SerNoCtlExclude_ID() {
		Integer ii = (Integer) get_Value("M_SerNoCtlExclude_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Serial No Control. Product Serial Number Control
	 */
	public void setM_SerNoCtl_ID(int M_SerNoCtl_ID) {
		if (M_SerNoCtl_ID < 1)
			throw new IllegalArgumentException("M_SerNoCtl_ID is mandatory.");
		set_ValueNoCheck("M_SerNoCtl_ID", new Integer(M_SerNoCtl_ID));
	}

	/**
	 * Get Serial No Control. Product Serial Number Control
	 */
	public int getM_SerNoCtl_ID() {
		Integer ii = (Integer) get_Value("M_SerNoCtl_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
