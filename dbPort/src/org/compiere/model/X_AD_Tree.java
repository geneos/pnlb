/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Tree
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.468
 */
public class X_AD_Tree extends PO {
	/** Standard Constructor */
	public X_AD_Tree(Properties ctx, int AD_Tree_ID, String trxName) {
		super(ctx, AD_Tree_ID, trxName);
		/**
		 * if (AD_Tree_ID == 0) { setAD_Tree_ID (0); setIsAllNodes (false);
		 * setIsDefault (false); // N setName (null); setTreeType (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Tree(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Tree */
	public static final String Table_Name = "AD_Tree";

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

	protected BigDecimal accessLevel = new BigDecimal(6);

	/** AccessLevel 6 - System - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_Tree[").append(get_ID())
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
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description != null && Description.length() > 255) {
			log.warning("Length > 255 - truncated");
			Description = Description.substring(0, 254);
		}
		set_Value("Description", Description);
	}

	/**
	 * Get Description. Optional short description of the record
	 */
	public String getDescription() {
		return (String) get_Value("Description");
	}

	/**
	 * Set All Nodes. All Nodes are included (Complete Tree)
	 */
	public void setIsAllNodes(boolean IsAllNodes) {
		set_Value("IsAllNodes", new Boolean(IsAllNodes));
	}

	/**
	 * Get All Nodes. All Nodes are included (Complete Tree)
	 */
	public boolean isAllNodes() {
		Object oo = get_Value("IsAllNodes");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Default. Default value
	 */
	public void setIsDefault(boolean IsDefault) {
		set_Value("IsDefault", new Boolean(IsDefault));
	}

	/**
	 * Get Default. Default value
	 */
	public boolean isDefault() {
		Object oo = get_Value("IsDefault");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 60) {
			log.warning("Length > 60 - truncated");
			Name = Name.substring(0, 59);
		}
		set_Value("Name", Name);
	}

	/**
	 * Get Name. Alphanumeric identifier of the entity
	 */
	public String getName() {
		return (String) get_Value("Name");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
	}

	/** Set Process Now */
	public void setProcessing(boolean Processing) {
		set_Value("Processing", new Boolean(Processing));
	}

	/** Get Process Now */
	public boolean isProcessing() {
		Object oo = get_Value("Processing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** TreeType AD_Reference_ID=120 */
	public static final int TREETYPE_AD_Reference_ID = 120;

	/** Activity = AY */
	public static final String TREETYPE_Activity = "AY";

	/** BoM = BB */
	public static final String TREETYPE_BoM = "BB";

	/** BPartner = BP */
	public static final String TREETYPE_BPartner = "BP";

	/** Element Value = EV */
	public static final String TREETYPE_ElementValue = "EV";

	/** Campaign = MC */
	public static final String TREETYPE_Campaign = "MC";

	/** Menu = MM */
	public static final String TREETYPE_Menu = "MM";

	/** Organization = OO */
	public static final String TREETYPE_Organization = "OO";

	/** Product Category = PC */
	public static final String TREETYPE_ProductCategory = "PC";

	/** Project = PJ */
	public static final String TREETYPE_Project = "PJ";

	/** Product = PR */
	public static final String TREETYPE_Product = "PR";

	/** Sales Region = SR */
	public static final String TREETYPE_SalesRegion = "SR";

	/**
	 * Set Type | Area. Element this tree is built on (i.e Product, Business
	 * Partner)
	 */
	public void setTreeType(String TreeType) {
		if (TreeType == null)
			throw new IllegalArgumentException("TreeType is mandatory");
		if (TreeType.equals("AY") || TreeType.equals("BB")
				|| TreeType.equals("BP") || TreeType.equals("EV")
				|| TreeType.equals("MC") || TreeType.equals("MM")
				|| TreeType.equals("OO") || TreeType.equals("PC")
				|| TreeType.equals("PJ") || TreeType.equals("PR")
				|| TreeType.equals("SR"))
			;
		else
			throw new IllegalArgumentException(
					"TreeType Invalid value - "
							+ TreeType
							+ " - Reference_ID=120 - AY - BB - BP - EV - MC - MM - OO - PC - PJ - PR - SR");
		if (TreeType.length() > 2) {
			log.warning("Length > 2 - truncated");
			TreeType = TreeType.substring(0, 1);
		}
		set_ValueNoCheck("TreeType", TreeType);
	}

	/**
	 * Get Type | Area. Element this tree is built on (i.e Product, Business
	 * Partner)
	 */
	public String getTreeType() {
		return (String) get_Value("TreeType");
	}
}
