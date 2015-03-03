/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_WF_Node_Para
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.89
 */
public class X_AD_WF_Node_Para extends PO {
	/** Standard Constructor */
	public X_AD_WF_Node_Para(Properties ctx, int AD_WF_Node_Para_ID,
			String trxName) {
		super(ctx, AD_WF_Node_Para_ID, trxName);
		/**
		 * if (AD_WF_Node_Para_ID == 0) { setAD_WF_Node_ID (0);
		 * setAD_WF_Node_Para_ID (0); setEntityType (null); // U }
		 */
	}

	/** Load Constructor */
	public X_AD_WF_Node_Para(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_WF_Node_Para */
	public static final String Table_Name = "AD_WF_Node_Para";

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
		StringBuffer sb = new StringBuffer("X_AD_WF_Node_Para[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** Set Process Parameter */
	public void setAD_Process_Para_ID(int AD_Process_Para_ID) {
		if (AD_Process_Para_ID <= 0)
			set_Value("AD_Process_Para_ID", null);
		else
			set_Value("AD_Process_Para_ID", new Integer(AD_Process_Para_ID));
	}

	/** Get Process Parameter */
	public int getAD_Process_Para_ID() {
		Integer ii = (Integer) get_Value("AD_Process_Para_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Node. Workflow Node (activity), step or process
	 */
	public void setAD_WF_Node_ID(int AD_WF_Node_ID) {
		if (AD_WF_Node_ID < 1)
			throw new IllegalArgumentException("AD_WF_Node_ID is mandatory.");
		set_ValueNoCheck("AD_WF_Node_ID", new Integer(AD_WF_Node_ID));
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getAD_WF_Node_ID()));
	}

	/**
	 * Set Workflow Node Parameter. Workflow Node Execution Parameter
	 */
	public void setAD_WF_Node_Para_ID(int AD_WF_Node_Para_ID) {
		if (AD_WF_Node_Para_ID < 1)
			throw new IllegalArgumentException(
					"AD_WF_Node_Para_ID is mandatory.");
		set_ValueNoCheck("AD_WF_Node_Para_ID", new Integer(AD_WF_Node_Para_ID));
	}

	/**
	 * Get Workflow Node Parameter. Workflow Node Execution Parameter
	 */
	public int getAD_WF_Node_Para_ID() {
		Integer ii = (Integer) get_Value("AD_WF_Node_Para_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Attribute Name. Name of the Attribute
	 */
	public void setAttributeName(String AttributeName) {
		if (AttributeName != null && AttributeName.length() > 60) {
			log.warning("Length > 60 - truncated");
			AttributeName = AttributeName.substring(0, 59);
		}
		set_Value("AttributeName", AttributeName);
	}

	/**
	 * Get Attribute Name. Name of the Attribute
	 */
	public String getAttributeName() {
		return (String) get_Value("AttributeName");
	}

	/**
	 * Set Attribute Value. Value of the Attribute
	 */
	public void setAttributeValue(String AttributeValue) {
		if (AttributeValue != null && AttributeValue.length() > 60) {
			log.warning("Length > 60 - truncated");
			AttributeValue = AttributeValue.substring(0, 59);
		}
		set_Value("AttributeValue", AttributeValue);
	}

	/**
	 * Get Attribute Value. Value of the Attribute
	 */
	public String getAttributeValue() {
		return (String) get_Value("AttributeValue");
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

	/** EntityType AD_Reference_ID=245 */
	public static final int ENTITYTYPE_AD_Reference_ID = 245;

	/** Applications = A */
	public static final String ENTITYTYPE_Applications = "A";

	/** Compiere = C */
	public static final String ENTITYTYPE_Compiere = "C";

	/** Customization = CUST */
	public static final String ENTITYTYPE_Customization = "CUST";

	/** Dictionary = D */
	public static final String ENTITYTYPE_Dictionary = "D";

	/** User maintained = U */
	public static final String ENTITYTYPE_UserMaintained = "U";

	/**
	 * Set Entity Type. Dictionary Entity Type; Determines ownership and
	 * synchronization
	 */
	public void setEntityType(String EntityType) {
		if (EntityType == null)
			throw new IllegalArgumentException("EntityType is mandatory");
		if (EntityType.length() > 4) {
			log.warning("Length > 4 - truncated");
			EntityType = EntityType.substring(0, 3);
		}
		set_Value("EntityType", EntityType);
	}

	/**
	 * Get Entity Type. Dictionary Entity Type; Determines ownership and
	 * synchronization
	 */
	public String getEntityType() {
		return (String) get_Value("EntityType");
	}
}
