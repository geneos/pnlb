/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_AttributeSetExclude
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.421
 */
public class X_M_AttributeSetExclude extends PO {
	/** Standard Constructor */
	public X_M_AttributeSetExclude(Properties ctx,
			int M_AttributeSetExclude_ID, String trxName) {
		super(ctx, M_AttributeSetExclude_ID, trxName);
		/**
		 * if (M_AttributeSetExclude_ID == 0) { setAD_Table_ID (0); setIsSOTrx
		 * (false); setM_AttributeSetExclude_ID (0); setM_AttributeSet_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_M_AttributeSetExclude(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_AttributeSetExclude */
	public static final String Table_Name = "M_AttributeSetExclude";

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
		StringBuffer sb = new StringBuffer("X_M_AttributeSetExclude[").append(
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
	 * Set Exclude Attribute Set. Exclude the ability to enter Attribute Sets
	 */
	public void setM_AttributeSetExclude_ID(int M_AttributeSetExclude_ID) {
		if (M_AttributeSetExclude_ID < 1)
			throw new IllegalArgumentException(
					"M_AttributeSetExclude_ID is mandatory.");
		set_ValueNoCheck("M_AttributeSetExclude_ID", new Integer(
				M_AttributeSetExclude_ID));
	}

	/**
	 * Get Exclude Attribute Set. Exclude the ability to enter Attribute Sets
	 */
	public int getM_AttributeSetExclude_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSetExclude_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Attribute Set. Product Attribute Set
	 */
	public void setM_AttributeSet_ID(int M_AttributeSet_ID) {
		if (M_AttributeSet_ID < 0)
			throw new IllegalArgumentException(
					"M_AttributeSet_ID is mandatory.");
		set_ValueNoCheck("M_AttributeSet_ID", new Integer(M_AttributeSet_ID));
	}

	/**
	 * Get Attribute Set. Product Attribute Set
	 */
	public int getM_AttributeSet_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSet_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
