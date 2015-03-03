/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Ref_List
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.046
 */
public class X_AD_Ref_List extends PO {
	/** Standard Constructor */
	public X_AD_Ref_List(Properties ctx, int AD_Ref_List_ID, String trxName) {
		super(ctx, AD_Ref_List_ID, trxName);
		/**
		 * if (AD_Ref_List_ID == 0) { setAD_Ref_List_ID (0); setAD_Reference_ID
		 * (0); setEntityType (null); // U setName (null); setValue (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Ref_List(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Ref_List */
	public static final String Table_Name = "AD_Ref_List";

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
		StringBuffer sb = new StringBuffer("X_AD_Ref_List[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Reference List. Reference List based on Table
	 */
	public void setAD_Ref_List_ID(int AD_Ref_List_ID) {
		if (AD_Ref_List_ID < 1)
			throw new IllegalArgumentException("AD_Ref_List_ID is mandatory.");
		set_ValueNoCheck("AD_Ref_List_ID", new Integer(AD_Ref_List_ID));
	}

	/**
	 * Get Reference List. Reference List based on Table
	 */
	public int getAD_Ref_List_ID() {
		Integer ii = (Integer) get_Value("AD_Ref_List_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Reference. System Reference (Pick List)
	 */
	public void setAD_Reference_ID(int AD_Reference_ID) {
		if (AD_Reference_ID < 1)
			throw new IllegalArgumentException("AD_Reference_ID is mandatory.");
		set_ValueNoCheck("AD_Reference_ID", new Integer(AD_Reference_ID));
	}

	/**
	 * Get Reference. System Reference (Pick List)
	 */
	public int getAD_Reference_ID() {
		Integer ii = (Integer) get_Value("AD_Reference_ID");
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

	/**
	 * Set Valid from. Valid from including this date (first day)
	 */
	public void setValidFrom(Timestamp ValidFrom) {
		set_Value("ValidFrom", ValidFrom);
	}

	/**
	 * Get Valid from. Valid from including this date (first day)
	 */
	public Timestamp getValidFrom() {
		return (Timestamp) get_Value("ValidFrom");
	}

	/**
	 * Set Valid to. Valid to including this date (last day)
	 */
	public void setValidTo(Timestamp ValidTo) {
		set_Value("ValidTo", ValidTo);
	}

	/**
	 * Get Valid to. Valid to including this date (last day)
	 */
	public Timestamp getValidTo() {
		return (Timestamp) get_Value("ValidTo");
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 60) {
			log.warning("Length > 60 - truncated");
			Value = Value.substring(0, 59);
		}
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public String getValue() {
		return (String) get_Value("Value");
	}
}
