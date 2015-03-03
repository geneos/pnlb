/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_Hierarchy
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.906
 */
public class X_PA_Hierarchy extends PO {
	/** Standard Constructor */
	public X_PA_Hierarchy(Properties ctx, int PA_Hierarchy_ID, String trxName) {
		super(ctx, PA_Hierarchy_ID, trxName);
		/**
		 * if (PA_Hierarchy_ID == 0) { setAD_Tree_Account_ID (0);
		 * setAD_Tree_Activity_ID (0); setAD_Tree_BPartner_ID (0);
		 * setAD_Tree_Campaign_ID (0); setAD_Tree_Org_ID (0);
		 * setAD_Tree_Product_ID (0); setAD_Tree_Project_ID (0);
		 * setAD_Tree_SalesRegion_ID (0); setName (null); setPA_Hierarchy_ID
		 * (0); }
		 */
	}

	/** Load Constructor */
	public X_PA_Hierarchy(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_Hierarchy */
	public static final String Table_Name = "PA_Hierarchy";

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
		StringBuffer sb = new StringBuffer("X_PA_Hierarchy[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AD_Tree_Account_ID AD_Reference_ID=184 */
	public static final int AD_TREE_ACCOUNT_ID_AD_Reference_ID = 184;

	/**
	 * Set Account Tree. Tree for Natural Account Tree
	 */
	public void setAD_Tree_Account_ID(int AD_Tree_Account_ID) {
		if (AD_Tree_Account_ID < 1)
			throw new IllegalArgumentException(
					"AD_Tree_Account_ID is mandatory.");
		set_Value("AD_Tree_Account_ID", new Integer(AD_Tree_Account_ID));
	}

	/**
	 * Get Account Tree. Tree for Natural Account Tree
	 */
	public int getAD_Tree_Account_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Account_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_Activity_ID AD_Reference_ID=184 */
	public static final int AD_TREE_ACTIVITY_ID_AD_Reference_ID = 184;

	/**
	 * Set Activity Tree. Tree to determine activity hierarchy
	 */
	public void setAD_Tree_Activity_ID(int AD_Tree_Activity_ID) {
		if (AD_Tree_Activity_ID < 1)
			throw new IllegalArgumentException(
					"AD_Tree_Activity_ID is mandatory.");
		set_Value("AD_Tree_Activity_ID", new Integer(AD_Tree_Activity_ID));
	}

	/**
	 * Get Activity Tree. Tree to determine activity hierarchy
	 */
	public int getAD_Tree_Activity_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Activity_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_BPartner_ID AD_Reference_ID=184 */
	public static final int AD_TREE_BPARTNER_ID_AD_Reference_ID = 184;

	/**
	 * Set BPartner Tree. Tree to determine business partner hierarchy
	 */
	public void setAD_Tree_BPartner_ID(int AD_Tree_BPartner_ID) {
		if (AD_Tree_BPartner_ID < 1)
			throw new IllegalArgumentException(
					"AD_Tree_BPartner_ID is mandatory.");
		set_Value("AD_Tree_BPartner_ID", new Integer(AD_Tree_BPartner_ID));
	}

	/**
	 * Get BPartner Tree. Tree to determine business partner hierarchy
	 */
	public int getAD_Tree_BPartner_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_Campaign_ID AD_Reference_ID=184 */
	public static final int AD_TREE_CAMPAIGN_ID_AD_Reference_ID = 184;

	/**
	 * Set Campaign Tree. Tree to determine marketing campaign hierarchy
	 */
	public void setAD_Tree_Campaign_ID(int AD_Tree_Campaign_ID) {
		if (AD_Tree_Campaign_ID < 1)
			throw new IllegalArgumentException(
					"AD_Tree_Campaign_ID is mandatory.");
		set_Value("AD_Tree_Campaign_ID", new Integer(AD_Tree_Campaign_ID));
	}

	/**
	 * Get Campaign Tree. Tree to determine marketing campaign hierarchy
	 */
	public int getAD_Tree_Campaign_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Campaign_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_Org_ID AD_Reference_ID=184 */
	public static final int AD_TREE_ORG_ID_AD_Reference_ID = 184;

	/**
	 * Set Organization Tree. Tree to determine organizational hierarchy
	 */
	public void setAD_Tree_Org_ID(int AD_Tree_Org_ID) {
		if (AD_Tree_Org_ID < 1)
			throw new IllegalArgumentException("AD_Tree_Org_ID is mandatory.");
		set_Value("AD_Tree_Org_ID", new Integer(AD_Tree_Org_ID));
	}

	/**
	 * Get Organization Tree. Tree to determine organizational hierarchy
	 */
	public int getAD_Tree_Org_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Org_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_Product_ID AD_Reference_ID=184 */
	public static final int AD_TREE_PRODUCT_ID_AD_Reference_ID = 184;

	/**
	 * Set Product Tree. Tree to determine product hierarchy
	 */
	public void setAD_Tree_Product_ID(int AD_Tree_Product_ID) {
		if (AD_Tree_Product_ID < 1)
			throw new IllegalArgumentException(
					"AD_Tree_Product_ID is mandatory.");
		set_Value("AD_Tree_Product_ID", new Integer(AD_Tree_Product_ID));
	}

	/**
	 * Get Product Tree. Tree to determine product hierarchy
	 */
	public int getAD_Tree_Product_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Product_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_Project_ID AD_Reference_ID=184 */
	public static final int AD_TREE_PROJECT_ID_AD_Reference_ID = 184;

	/**
	 * Set Project Tree. Tree to determine project hierarchy
	 */
	public void setAD_Tree_Project_ID(int AD_Tree_Project_ID) {
		if (AD_Tree_Project_ID < 1)
			throw new IllegalArgumentException(
					"AD_Tree_Project_ID is mandatory.");
		set_Value("AD_Tree_Project_ID", new Integer(AD_Tree_Project_ID));
	}

	/**
	 * Get Project Tree. Tree to determine project hierarchy
	 */
	public int getAD_Tree_Project_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_Project_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Tree_SalesRegion_ID AD_Reference_ID=184 */
	public static final int AD_TREE_SALESREGION_ID_AD_Reference_ID = 184;

	/**
	 * Set Sales Region Tree. Tree to determine sales regional hierarchy
	 */
	public void setAD_Tree_SalesRegion_ID(int AD_Tree_SalesRegion_ID) {
		if (AD_Tree_SalesRegion_ID < 1)
			throw new IllegalArgumentException(
					"AD_Tree_SalesRegion_ID is mandatory.");
		set_Value("AD_Tree_SalesRegion_ID", new Integer(AD_Tree_SalesRegion_ID));
	}

	/**
	 * Get Sales Region Tree. Tree to determine sales regional hierarchy
	 */
	public int getAD_Tree_SalesRegion_ID() {
		Integer ii = (Integer) get_Value("AD_Tree_SalesRegion_ID");
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
	 * Set Comment/Help. Comment or Hint
	 */
	public void setHelp(String Help) {
		if (Help != null && Help.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Help = Help.substring(0, 1999);
		}
		set_Value("Help", Help);
	}

	/**
	 * Get Comment/Help. Comment or Hint
	 */
	public String getHelp() {
		return (String) get_Value("Help");
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 120) {
			log.warning("Length > 120 - truncated");
			Name = Name.substring(0, 119);
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

	/**
	 * Set Reporting Hierarchy. Optional Reporting Hierarchy - If not selected
	 * the default hierarchy trees are used.
	 */
	public void setPA_Hierarchy_ID(int PA_Hierarchy_ID) {
		if (PA_Hierarchy_ID < 1)
			throw new IllegalArgumentException("PA_Hierarchy_ID is mandatory.");
		set_ValueNoCheck("PA_Hierarchy_ID", new Integer(PA_Hierarchy_ID));
	}

	/**
	 * Get Reporting Hierarchy. Optional Reporting Hierarchy - If not selected
	 * the default hierarchy trees are used.
	 */
	public int getPA_Hierarchy_ID() {
		Integer ii = (Integer) get_Value("PA_Hierarchy_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
