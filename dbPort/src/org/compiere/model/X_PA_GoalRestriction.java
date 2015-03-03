/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_GoalRestriction
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.89
 */
public class X_PA_GoalRestriction extends PO {
	/** Standard Constructor */
	public X_PA_GoalRestriction(Properties ctx, int PA_GoalRestriction_ID,
			String trxName) {
		super(ctx, PA_GoalRestriction_ID, trxName);
		/**
		 * if (PA_GoalRestriction_ID == 0) { setGoalRestrictionType (null);
		 * setName (null); setPA_GoalRestriction_ID (0); setPA_Goal_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_PA_GoalRestriction(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_GoalRestriction */
	public static final String Table_Name = "PA_GoalRestriction";

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
		StringBuffer sb = new StringBuffer("X_PA_GoalRestriction[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Business Partner Group. Business Partner Group
	 */
	public void setC_BP_Group_ID(int C_BP_Group_ID) {
		if (C_BP_Group_ID <= 0)
			set_Value("C_BP_Group_ID", null);
		else
			set_Value("C_BP_Group_ID", new Integer(C_BP_Group_ID));
	}

	/**
	 * Get Business Partner Group. Business Partner Group
	 */
	public int getC_BP_Group_ID() {
		Integer ii = (Integer) get_Value("C_BP_Group_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID <= 0)
			set_Value("C_BPartner_ID", null);
		else
			set_Value("C_BPartner_ID", new Integer(C_BPartner_ID));
	}

	/**
	 * Get Business Partner . Identifies a Business Partner
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** GoalRestrictionType AD_Reference_ID=368 */
	public static final int GOALRESTRICTIONTYPE_AD_Reference_ID = 368;

	/** Business Partner = B */
	public static final String GOALRESTRICTIONTYPE_BusinessPartner = "B";

	/** Product Category = C */
	public static final String GOALRESTRICTIONTYPE_ProductCategory = "C";

	/** Bus.Partner Group = G */
	public static final String GOALRESTRICTIONTYPE_BusPartnerGroup = "G";

	/** Organization = O */
	public static final String GOALRESTRICTIONTYPE_Organization = "O";

	/** Product = P */
	public static final String GOALRESTRICTIONTYPE_Product = "P";

	/**
	 * Set Restriction Type. Goal Restriction Type
	 */
	public void setGoalRestrictionType(String GoalRestrictionType) {
		if (GoalRestrictionType == null)
			throw new IllegalArgumentException(
					"GoalRestrictionType is mandatory");
		if (GoalRestrictionType.equals("B") || GoalRestrictionType.equals("C")
				|| GoalRestrictionType.equals("G")
				|| GoalRestrictionType.equals("O")
				|| GoalRestrictionType.equals("P"))
			;
		else
			throw new IllegalArgumentException(
					"GoalRestrictionType Invalid value - "
							+ GoalRestrictionType
							+ " - Reference_ID=368 - B - C - G - O - P");
		if (GoalRestrictionType.length() > 1) {
			log.warning("Length > 1 - truncated");
			GoalRestrictionType = GoalRestrictionType.substring(0, 0);
		}
		set_Value("GoalRestrictionType", GoalRestrictionType);
	}

	/**
	 * Get Restriction Type. Goal Restriction Type
	 */
	public String getGoalRestrictionType() {
		return (String) get_Value("GoalRestrictionType");
	}

	/**
	 * Set Product Category. Category of a Product
	 */
	public void setM_Product_Category_ID(int M_Product_Category_ID) {
		if (M_Product_Category_ID <= 0)
			set_Value("M_Product_Category_ID", null);
		else
			set_Value("M_Product_Category_ID", new Integer(
					M_Product_Category_ID));
	}

	/**
	 * Get Product Category. Category of a Product
	 */
	public int getM_Product_Category_ID() {
		Integer ii = (Integer) get_Value("M_Product_Category_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID <= 0)
			set_Value("M_Product_ID", null);
		else
			set_Value("M_Product_ID", new Integer(M_Product_ID));
	}

	/**
	 * Get Product. Product, Service, Item
	 */
	public int getM_Product_ID() {
		Integer ii = (Integer) get_Value("M_Product_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/** Org_ID AD_Reference_ID=322 */
	public static final int ORG_ID_AD_Reference_ID = 322;

	/**
	 * Set Organization. Organizational entity within client
	 */
	public void setOrg_ID(int Org_ID) {
		if (Org_ID <= 0)
			set_Value("Org_ID", null);
		else
			set_Value("Org_ID", new Integer(Org_ID));
	}

	/**
	 * Get Organization. Organizational entity within client
	 */
	public int getOrg_ID() {
		Integer ii = (Integer) get_Value("Org_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Goal Restriction. Performance Goal Restriction
	 */
	public void setPA_GoalRestriction_ID(int PA_GoalRestriction_ID) {
		if (PA_GoalRestriction_ID < 1)
			throw new IllegalArgumentException(
					"PA_GoalRestriction_ID is mandatory.");
		set_ValueNoCheck("PA_GoalRestriction_ID", new Integer(
				PA_GoalRestriction_ID));
	}

	/**
	 * Get Goal Restriction. Performance Goal Restriction
	 */
	public int getPA_GoalRestriction_ID() {
		Integer ii = (Integer) get_Value("PA_GoalRestriction_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Goal. Performance Goal
	 */
	public void setPA_Goal_ID(int PA_Goal_ID) {
		if (PA_Goal_ID < 1)
			throw new IllegalArgumentException("PA_Goal_ID is mandatory.");
		set_Value("PA_Goal_ID", new Integer(PA_Goal_ID));
	}

	/**
	 * Get Goal. Performance Goal
	 */
	public int getPA_Goal_ID() {
		Integer ii = (Integer) get_Value("PA_Goal_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
