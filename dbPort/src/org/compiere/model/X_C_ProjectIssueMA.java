/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_ProjectIssueMA
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:00.265
 */
public class X_C_ProjectIssueMA extends PO {
	/** Standard Constructor */
	public X_C_ProjectIssueMA(Properties ctx, int C_ProjectIssueMA_ID,
			String trxName) {
		super(ctx, C_ProjectIssueMA_ID, trxName);
		/**
		 * if (C_ProjectIssueMA_ID == 0) { setC_ProjectIssue_ID (0);
		 * setM_AttributeSetInstance_ID (0); setMovementQty (Env.ZERO); }
		 */
	}

	/** Load Constructor */
	public X_C_ProjectIssueMA(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_ProjectIssueMA */
	public static final String Table_Name = "C_ProjectIssueMA";

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

	protected BigDecimal accessLevel = new BigDecimal(1);

	/** AccessLevel 1 - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_ProjectIssueMA[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Project Issue. Project Issues (Material, Labor)
	 */
	public void setC_ProjectIssue_ID(int C_ProjectIssue_ID) {
		if (C_ProjectIssue_ID < 1)
			throw new IllegalArgumentException(
					"C_ProjectIssue_ID is mandatory.");
		set_ValueNoCheck("C_ProjectIssue_ID", new Integer(C_ProjectIssue_ID));
	}

	/**
	 * Get Project Issue. Project Issues (Material, Labor)
	 */
	public int getC_ProjectIssue_ID() {
		Integer ii = (Integer) get_Value("C_ProjectIssue_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_ProjectIssue_ID()));
	}

	/**
	 * Set Attribute Set Instance. Product Attribute Set Instance
	 */
	public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
		if (M_AttributeSetInstance_ID < 0)
			throw new IllegalArgumentException(
					"M_AttributeSetInstance_ID is mandatory.");
		set_ValueNoCheck("M_AttributeSetInstance_ID", new Integer(
				M_AttributeSetInstance_ID));
	}

	/**
	 * Get Attribute Set Instance. Product Attribute Set Instance
	 */
	public int getM_AttributeSetInstance_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSetInstance_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Movement Quantity. Quantity of a product moved.
	 */
	public void setMovementQty(BigDecimal MovementQty) {
		if (MovementQty == null)
			throw new IllegalArgumentException("MovementQty is mandatory.");
		set_Value("MovementQty", MovementQty);
	}

	/**
	 * Get Movement Quantity. Quantity of a product moved.
	 */
	public BigDecimal getMovementQty() {
		BigDecimal bd = (BigDecimal) get_Value("MovementQty");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
