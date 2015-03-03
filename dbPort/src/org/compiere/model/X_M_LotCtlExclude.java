/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_LotCtlExclude
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.093
 */
public class X_M_LotCtlExclude extends PO {
	/** Standard Constructor */
	public X_M_LotCtlExclude(Properties ctx, int M_LotCtlExclude_ID,
			String trxName) {
		super(ctx, M_LotCtlExclude_ID, trxName);
		/**
		 * if (M_LotCtlExclude_ID == 0) { setAD_Table_ID (0); setIsSOTrx
		 * (false); setM_LotCtlExclude_ID (0); setM_LotCtl_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_M_LotCtlExclude(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_LotCtlExclude */
	public static final String Table_Name = "M_LotCtlExclude";

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
		StringBuffer sb = new StringBuffer("X_M_LotCtlExclude[").append(
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
	 * Set Exclude Lot. Exclude the ability to create Lots in Attribute Sets
	 */
	public void setM_LotCtlExclude_ID(int M_LotCtlExclude_ID) {
		if (M_LotCtlExclude_ID < 1)
			throw new IllegalArgumentException(
					"M_LotCtlExclude_ID is mandatory.");
		set_ValueNoCheck("M_LotCtlExclude_ID", new Integer(M_LotCtlExclude_ID));
	}

	/**
	 * Get Exclude Lot. Exclude the ability to create Lots in Attribute Sets
	 */
	public int getM_LotCtlExclude_ID() {
		Integer ii = (Integer) get_Value("M_LotCtlExclude_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Lot Control. Product Lot Control
	 */
	public void setM_LotCtl_ID(int M_LotCtl_ID) {
		if (M_LotCtl_ID < 1)
			throw new IllegalArgumentException("M_LotCtl_ID is mandatory.");
		set_ValueNoCheck("M_LotCtl_ID", new Integer(M_LotCtl_ID));
	}

	/**
	 * Get Lot Control. Product Lot Control
	 */
	public int getM_LotCtl_ID() {
		Integer ii = (Integer) get_Value("M_LotCtl_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
