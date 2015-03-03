/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Val_Rule
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.671
 */
public class X_AD_Val_Rule extends PO {
	/** Standard Constructor */
	public X_AD_Val_Rule(Properties ctx, int AD_Val_Rule_ID, String trxName) {
		super(ctx, AD_Val_Rule_ID, trxName);
		/**
		 * if (AD_Val_Rule_ID == 0) { setAD_Val_Rule_ID (0); setEntityType
		 * (null); // U setName (null); setType (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Val_Rule(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Val_Rule */
	public static final String Table_Name = "AD_Val_Rule";

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
		StringBuffer sb = new StringBuffer("X_AD_Val_Rule[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Dynamic Validation. Dynamic Validation Rule
	 */
	public void setAD_Val_Rule_ID(int AD_Val_Rule_ID) {
		if (AD_Val_Rule_ID < 1)
			throw new IllegalArgumentException("AD_Val_Rule_ID is mandatory.");
		set_ValueNoCheck("AD_Val_Rule_ID", new Integer(AD_Val_Rule_ID));
	}

	/**
	 * Get Dynamic Validation. Dynamic Validation Rule
	 */
	public int getAD_Val_Rule_ID() {
		Integer ii = (Integer) get_Value("AD_Val_Rule_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Validation code. Validation Code
	 */
	public void setCode(String Code) {
		if (Code != null && Code.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Code = Code.substring(0, 1999);
		}
		set_Value("Code", Code);
	}

	/**
	 * Get Validation code. Validation Code
	 */
	public String getCode() {
		return (String) get_Value("Code");
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

	/** Type AD_Reference_ID=101 */
	public static final int TYPE_AD_Reference_ID = 101;

	/** Java Script = E */
	public static final String TYPE_JavaScript = "E";

	/** Java Language = J */
	public static final String TYPE_JavaLanguage = "J";

	/** SQL = S */
	public static final String TYPE_SQL = "S";

	/**
	 * Set Type. Type of Validation (SQL, Java Script, Java Language)
	 */
	public void setType(String Type) {
		if (Type == null)
			throw new IllegalArgumentException("Type is mandatory");
		if (Type.equals("E") || Type.equals("J") || Type.equals("S"))
			;
		else
			throw new IllegalArgumentException("Type Invalid value - " + Type
					+ " - Reference_ID=101 - E - J - S");
		if (Type.length() > 1) {
			log.warning("Length > 1 - truncated");
			Type = Type.substring(0, 0);
		}
		set_Value("Type", Type);
	}

	/**
	 * Get Type. Type of Validation (SQL, Java Script, Java Language)
	 */
	public String getType() {
		return (String) get_Value("Type");
	}
}
