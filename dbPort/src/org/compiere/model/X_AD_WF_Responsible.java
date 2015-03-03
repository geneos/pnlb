/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_WF_Responsible
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.921
 */
public class X_AD_WF_Responsible extends PO {
	/** Standard Constructor */
	public X_AD_WF_Responsible(Properties ctx, int AD_WF_Responsible_ID,
			String trxName) {
		super(ctx, AD_WF_Responsible_ID, trxName);
		/**
		 * if (AD_WF_Responsible_ID == 0) { setAD_Role_ID (0);
		 * setAD_WF_Responsible_ID (0); setEntityType (null); // U setName
		 * (null); setResponsibleType (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_WF_Responsible(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_WF_Responsible */
	public static final String Table_Name = "AD_WF_Responsible";

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
		StringBuffer sb = new StringBuffer("X_AD_WF_Responsible[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Role. Responsibility Role
	 */
	public void setAD_Role_ID(int AD_Role_ID) {
		if (AD_Role_ID < 0)
			throw new IllegalArgumentException("AD_Role_ID is mandatory.");
		set_Value("AD_Role_ID", new Integer(AD_Role_ID));
	}

	/**
	 * Get Role. Responsibility Role
	 */
	public int getAD_Role_ID() {
		Integer ii = (Integer) get_Value("AD_Role_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_User_ID AD_Reference_ID=286 */
	public static final int AD_USER_ID_AD_Reference_ID = 286;

	/**
	 * Set User/Contact. User within the system - Internal or Business Partner
	 * Contact
	 */
	public void setAD_User_ID(int AD_User_ID) {
		if (AD_User_ID <= 0)
			set_Value("AD_User_ID", null);
		else
			set_Value("AD_User_ID", new Integer(AD_User_ID));
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

	/**
	 * Set Workflow Responsible. Responsible for Workflow Execution
	 */
	public void setAD_WF_Responsible_ID(int AD_WF_Responsible_ID) {
		if (AD_WF_Responsible_ID < 1)
			throw new IllegalArgumentException(
					"AD_WF_Responsible_ID is mandatory.");
		set_ValueNoCheck("AD_WF_Responsible_ID", new Integer(
				AD_WF_Responsible_ID));
	}

	/**
	 * Get Workflow Responsible. Responsible for Workflow Execution
	 */
	public int getAD_WF_Responsible_ID() {
		Integer ii = (Integer) get_Value("AD_WF_Responsible_ID");
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

	/** ResponsibleType AD_Reference_ID=304 */
	public static final int RESPONSIBLETYPE_AD_Reference_ID = 304;

	/** Human = H */
	public static final String RESPONSIBLETYPE_Human = "H";

	/** Organization = O */
	public static final String RESPONSIBLETYPE_Organization = "O";

	/** Role = R */
	public static final String RESPONSIBLETYPE_Role = "R";

	/** System Resource = S */
	public static final String RESPONSIBLETYPE_SystemResource = "S";

	/**
	 * Set Responsible Type. Type of the Responsibility for a workflow
	 */
	public void setResponsibleType(String ResponsibleType) {
		if (ResponsibleType == null)
			throw new IllegalArgumentException("ResponsibleType is mandatory");
		if (ResponsibleType.equals("H") || ResponsibleType.equals("O")
				|| ResponsibleType.equals("R") || ResponsibleType.equals("S"))
			;
		else
			throw new IllegalArgumentException(
					"ResponsibleType Invalid value - " + ResponsibleType
							+ " - Reference_ID=304 - H - O - R - S");
		if (ResponsibleType.length() > 1) {
			log.warning("Length > 1 - truncated");
			ResponsibleType = ResponsibleType.substring(0, 0);
		}
		set_Value("ResponsibleType", ResponsibleType);
	}

	/**
	 * Get Responsible Type. Type of the Responsibility for a workflow
	 */
	public String getResponsibleType() {
		return (String) get_Value("ResponsibleType");
	}
}
