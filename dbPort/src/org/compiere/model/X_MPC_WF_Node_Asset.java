/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for MPC_WF_Node_Asset
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.343
 */
public class X_MPC_WF_Node_Asset extends PO {
	/** Standard Constructor */
	public X_MPC_WF_Node_Asset(Properties ctx, int MPC_WF_Node_Asset_ID,
			String trxName) {
		super(ctx, MPC_WF_Node_Asset_ID, trxName);
		/**
		 * if (MPC_WF_Node_Asset_ID == 0) { setAD_WF_Node_ID (0);
		 * setAD_Workflow_ID (0); setA_Asset_ID (0); setMPC_WF_Node_Asset_ID
		 * (0); }
		 */
	}

	/** Load Constructor */
	public X_MPC_WF_Node_Asset(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=MPC_WF_Node_Asset */
	public static final String Table_Name = "MPC_WF_Node_Asset";

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
		StringBuffer sb = new StringBuffer("X_MPC_WF_Node_Asset[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Node. Workflow Node (activity), step or process
	 */
	public void setAD_WF_Node_ID(int AD_WF_Node_ID) {
		if (AD_WF_Node_ID < 1)
			throw new IllegalArgumentException("AD_WF_Node_ID is mandatory.");
		set_Value("AD_WF_Node_ID", new Integer(AD_WF_Node_ID));
	}

	/**
	 * Get Node. Workflow Node (activity), step or process
	 */
	public int getAD_WF_Node_ID() {
		Integer ii = (Integer) get_Value("AD_WF_Node_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Workflow. Workflow or combination of tasks
	 */
	public void setAD_Workflow_ID(int AD_Workflow_ID) {
		if (AD_Workflow_ID < 1)
			throw new IllegalArgumentException("AD_Workflow_ID is mandatory.");
		set_ValueNoCheck("AD_Workflow_ID", new Integer(AD_Workflow_ID));
	}

	/**
	 * Get Workflow. Workflow or combination of tasks
	 */
	public int getAD_Workflow_ID() {
		Integer ii = (Integer) get_Value("AD_Workflow_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/** Set Node Asset */
	public void setMPC_WF_Node_Asset_ID(int MPC_WF_Node_Asset_ID) {
		if (MPC_WF_Node_Asset_ID < 1)
			throw new IllegalArgumentException(
					"MPC_WF_Node_Asset_ID is mandatory.");
		set_ValueNoCheck("MPC_WF_Node_Asset_ID", new Integer(
				MPC_WF_Node_Asset_ID));
	}

	/** Get Node Asset */
	public int getMPC_WF_Node_Asset_ID() {
		Integer ii = (Integer) get_Value("MPC_WF_Node_Asset_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
