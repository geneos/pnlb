/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_FieldGroup
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.421
 */
public class X_AD_FieldGroup extends PO {
	/** Standard Constructor */
	public X_AD_FieldGroup(Properties ctx, int AD_FieldGroup_ID, String trxName) {
		super(ctx, AD_FieldGroup_ID, trxName);
		/**
		 * if (AD_FieldGroup_ID == 0) { setAD_FieldGroup_ID (0); setEntityType
		 * (null); // U setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_FieldGroup(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_FieldGroup */
	public static final String Table_Name = "AD_FieldGroup";

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

	protected BigDecimal accessLevel = new BigDecimal(4);

	/** AccessLevel 4 - System */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_FieldGroup[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Field Group. Logical grouping of fields
	 */
	public void setAD_FieldGroup_ID(int AD_FieldGroup_ID) {
		if (AD_FieldGroup_ID < 1)
			throw new IllegalArgumentException("AD_FieldGroup_ID is mandatory.");
		set_ValueNoCheck("AD_FieldGroup_ID", new Integer(AD_FieldGroup_ID));
	}

	/**
	 * Get Field Group. Logical grouping of fields
	 */
	public int getAD_FieldGroup_ID() {
		Integer ii = (Integer) get_Value("AD_FieldGroup_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	/** Set Is Tab */
	public void setIsTab(boolean IsTab) {
		set_Value("IsTab", new Boolean(IsTab));
	}

	/** Get Is Tab */
	public boolean isTab() {
		Object oo = get_Value("IsTab");
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
}
