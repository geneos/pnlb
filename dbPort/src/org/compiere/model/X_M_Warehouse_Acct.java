/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_Warehouse_Acct
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.796
 */
public class X_M_Warehouse_Acct extends PO {
	/** Standard Constructor */
	public X_M_Warehouse_Acct(Properties ctx, int M_Warehouse_Acct_ID,
			String trxName) {
		super(ctx, M_Warehouse_Acct_ID, trxName);
		/**
		 * if (M_Warehouse_Acct_ID == 0) { setC_AcctSchema_ID (0);
		 * setM_Warehouse_ID (0); setW_Differences_Acct (0);
		 * setW_InvActualAdjust_Acct (0); setW_Inventory_Acct (0);
		 * setW_Revaluation_Acct (0); }
		 */
	}

	/** Load Constructor */
	public X_M_Warehouse_Acct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_Warehouse_Acct */
	public static final String Table_Name = "M_Warehouse_Acct";

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
		StringBuffer sb = new StringBuffer("X_M_Warehouse_Acct[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Accounting Schema. Rules for accounting
	 */
	public void setC_AcctSchema_ID(int C_AcctSchema_ID) {
		if (C_AcctSchema_ID < 1)
			throw new IllegalArgumentException("C_AcctSchema_ID is mandatory.");
		set_ValueNoCheck("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
	}

	/**
	 * Get Accounting Schema. Rules for accounting
	 */
	public int getC_AcctSchema_ID() {
		Integer ii = (Integer) get_Value("C_AcctSchema_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Warehouse. Storage Warehouse and Service Point
	 */
	public void setM_Warehouse_ID(int M_Warehouse_ID) {
		if (M_Warehouse_ID < 1)
			throw new IllegalArgumentException("M_Warehouse_ID is mandatory.");
		set_ValueNoCheck("M_Warehouse_ID", new Integer(M_Warehouse_ID));
	}

	/**
	 * Get Warehouse. Storage Warehouse and Service Point
	 */
	public int getM_Warehouse_ID() {
		Integer ii = (Integer) get_Value("M_Warehouse_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Warehouse Differences. Warehouse Differences Account
	 */
	public void setW_Differences_Acct(int W_Differences_Acct) {
		set_Value("W_Differences_Acct", new Integer(W_Differences_Acct));
	}

	/**
	 * Get Warehouse Differences. Warehouse Differences Account
	 */
	public int getW_Differences_Acct() {
		Integer ii = (Integer) get_Value("W_Differences_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Inventory Adjustment. Account for Inventory value adjustments for
	 * Actual Costing
	 */
	public void setW_InvActualAdjust_Acct(int W_InvActualAdjust_Acct) {
		set_Value("W_InvActualAdjust_Acct", new Integer(W_InvActualAdjust_Acct));
	}

	/**
	 * Get Inventory Adjustment. Account for Inventory value adjustments for
	 * Actual Costing
	 */
	public int getW_InvActualAdjust_Acct() {
		Integer ii = (Integer) get_Value("W_InvActualAdjust_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set (Not Used). Warehouse Inventory Asset Account - Currently not used
	 */
	public void setW_Inventory_Acct(int W_Inventory_Acct) {
		set_Value("W_Inventory_Acct", new Integer(W_Inventory_Acct));
	}

	/**
	 * Get (Not Used). Warehouse Inventory Asset Account - Currently not used
	 */
	public int getW_Inventory_Acct() {
		Integer ii = (Integer) get_Value("W_Inventory_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Inventory Revaluation. Account for Inventory Revaluation
	 */
	public void setW_Revaluation_Acct(int W_Revaluation_Acct) {
		set_Value("W_Revaluation_Acct", new Integer(W_Revaluation_Acct));
	}

	/**
	 * Get Inventory Revaluation. Account for Inventory Revaluation
	 */
	public int getW_Revaluation_Acct() {
		Integer ii = (Integer) get_Value("W_Revaluation_Acct");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
