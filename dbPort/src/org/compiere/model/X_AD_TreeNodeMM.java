/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_TreeNodeMM
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.515
 */
public class X_AD_TreeNodeMM extends PO {
	/** Standard Constructor */
	public X_AD_TreeNodeMM(Properties ctx, int AD_TreeNodeMM_ID, String trxName) {
		super(ctx, AD_TreeNodeMM_ID, trxName);
		/**
		 * if (AD_TreeNodeMM_ID == 0) { setAD_Tree_ID (0); setNode_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_TreeNodeMM(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_TreeNodeMM */
	public static final String Table_Name = "AD_TreeNodeMM";

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
		StringBuffer sb = new StringBuffer("X_AD_TreeNodeMM[").append(get_ID())
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

	/** Set Node_ID */
	public void setNode_ID(int Node_ID) {
		if (Node_ID < 0)
			throw new IllegalArgumentException("Node_ID is mandatory.");
		set_ValueNoCheck("Node_ID", new Integer(Node_ID));
	}

	/** Get Node_ID */
	public int getNode_ID() {
		Integer ii = (Integer) get_Value("Node_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Parent. Parent of Entity
	 */
	public void setParent_ID(int Parent_ID) {
		if (Parent_ID <= 0)
			set_Value("Parent_ID", null);
		else
			set_Value("Parent_ID", new Integer(Parent_ID));
	}

	/**
	 * Get Parent. Parent of Entity
	 */
	public int getParent_ID() {
		Integer ii = (Integer) get_Value("Parent_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Sequence. Method of ordering records; lowest number comes first
	 */
	public void setSeqNo(int SeqNo) {
		set_Value("SeqNo", new Integer(SeqNo));
	}

	/**
	 * Get Sequence. Method of ordering records; lowest number comes first
	 */
	public int getSeqNo() {
		Integer ii = (Integer) get_Value("SeqNo");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
