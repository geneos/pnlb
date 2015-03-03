/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for MPC_Order_Node_Asset
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.078
 */
public class X_MPC_Order_Node_Asset extends PO {
	/** Standard Constructor */
	public X_MPC_Order_Node_Asset(Properties ctx, int MPC_Order_Node_Asset_ID,
			String trxName) {
		super(ctx, MPC_Order_Node_Asset_ID, trxName);
		/**
		 * if (MPC_Order_Node_Asset_ID == 0) { setA_Asset_ID (0);
		 * setMPC_Order_ID (0); setMPC_Order_Node_Asset_ID (0);
		 * setMPC_Order_Node_ID (0); setMPC_Order_Workflow_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_MPC_Order_Node_Asset(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=MPC_Order_Node_Asset */
	public static final String Table_Name = "MPC_Order_Node_Asset";

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
		StringBuffer sb = new StringBuffer("X_MPC_Order_Node_Asset[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Asset. Asset used internally or by customers
	 */
	public void setA_Asset_ID(int A_Asset_ID) {
		if (A_Asset_ID < 1)
			throw new IllegalArgumentException("A_Asset_ID is mandatory.");
		set_Value("A_Asset_ID", new Integer(A_Asset_ID));
	}

	/**
	 * Get Asset. Asset used internally or by customers
	 */
	public int getA_Asset_ID() {
		Integer ii = (Integer) get_Value("A_Asset_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** MPC_Order_ID AD_Reference_ID=1000045 */
	public static final int MPC_ORDER_ID_AD_Reference_ID = 1000045;

	/**
	 * Set Manufacturing Order. Manufacturing Order
	 */
	public void setMPC_Order_ID(int MPC_Order_ID) {
		if (MPC_Order_ID < 1)
			throw new IllegalArgumentException("MPC_Order_ID is mandatory.");
		set_ValueNoCheck("MPC_Order_ID", new Integer(MPC_Order_ID));
	}

	/**
	 * Get Manufacturing Order. Manufacturing Order
	 */
	public int getMPC_Order_ID() {
		Integer ii = (Integer) get_Value("MPC_Order_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Order Node Asset ID */
	public void setMPC_Order_Node_Asset_ID(int MPC_Order_Node_Asset_ID) {
		if (MPC_Order_Node_Asset_ID < 1)
			throw new IllegalArgumentException(
					"MPC_Order_Node_Asset_ID is mandatory.");
		set_ValueNoCheck("MPC_Order_Node_Asset_ID", new Integer(
				MPC_Order_Node_Asset_ID));
	}

	/** Get Order Node Asset ID */
	public int getMPC_Order_Node_Asset_ID() {
		Integer ii = (Integer) get_Value("MPC_Order_Node_Asset_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Order Node ID */
	public void setMPC_Order_Node_ID(int MPC_Order_Node_ID) {
		if (MPC_Order_Node_ID < 1)
			throw new IllegalArgumentException(
					"MPC_Order_Node_ID is mandatory.");
		set_ValueNoCheck("MPC_Order_Node_ID", new Integer(MPC_Order_Node_ID));
	}

	/** Get Order Node ID */
	public int getMPC_Order_Node_ID() {
		Integer ii = (Integer) get_Value("MPC_Order_Node_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Order Workflow */
	public void setMPC_Order_Workflow_ID(int MPC_Order_Workflow_ID) {
		if (MPC_Order_Workflow_ID < 1)
			throw new IllegalArgumentException(
					"MPC_Order_Workflow_ID is mandatory.");
		set_ValueNoCheck("MPC_Order_Workflow_ID", new Integer(
				MPC_Order_Workflow_ID));
	}

	/** Get Order Workflow */
	public int getMPC_Order_Workflow_ID() {
		Integer ii = (Integer) get_Value("MPC_Order_Workflow_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
