/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_TreeBar
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.484
 */
public class X_AD_TreeBar extends PO {
	/** Standard Constructor */
	public X_AD_TreeBar(Properties ctx, int AD_TreeBar_ID, String trxName) {
		super(ctx, AD_TreeBar_ID, trxName);
		/**
		 * if (AD_TreeBar_ID == 0) { setAD_Tree_ID (0); setAD_User_ID (0);
		 * setNode_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_TreeBar(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_TreeBar */
	public static final String Table_Name = "AD_TreeBar";

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
		StringBuffer sb = new StringBuffer("X_AD_TreeBar[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Tree. Identifies a Tree
	 */
	public void setAD_Tree_ID(int AD_Tree_ID) {
		if (AD_Tree_ID < 1)
			throw new IllegalArgumentException("AD_Tree_ID is mandatory.");
		set_ValueNoCheck("AD_Tree_ID", new Integer(AD_Tree_ID));
	}

	/**
	 * Get Tree. Identifies a Tree
	 */
	public int getAD_Tree_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID < 1)
			throw new IllegalArgumentException("AD_User_ID is mandatory.");
		set_ValueNoCheck("AD_User_ID", new Integer(AD_User_ID));
	}

	/**
	 * Get User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public int getAD_User_ID() {
		Integer ii = (Integer) get_Value("AD_User_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Node_ID */
	public void setNode_ID(int Node_ID) {
		if (Node_ID < 0)
			throw new IllegalArgumentException("Node_ID is mandatory.");
		set_Value("Node_ID", new Integer(Node_ID));
	}

	/** Get Node_ID */
	public int getNode_ID() {
		Integer ii = (Integer) get_Value("Node_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getNode_ID()));
	}
}
