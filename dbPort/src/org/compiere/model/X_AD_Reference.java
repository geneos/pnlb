/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Reference
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.093
 */
public class X_AD_Reference extends PO {
	/** Standard Constructor */
	public X_AD_Reference(Properties ctx, int AD_Reference_ID, String trxName) {
		super(ctx, AD_Reference_ID, trxName);
		/**
		 * if (AD_Reference_ID == 0) { setAD_Reference_ID (0); setEntityType
		 * (null); // U setName (null); setValidationType (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Reference(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Reference */
	public static final String Table_Name = "AD_Reference";

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
		StringBuffer sb = new StringBuffer("X_AD_Reference[").append(get_ID())
				.append("]");
		return sb.toString();
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
	 * Set Value Format. Format of the value; Can contain fixed format elements,
	 * Variables: "_lLoOaAcCa09"
	 */
	public void setVFormat(String VFormat) {
		if (VFormat != null && VFormat.length() > 40) {
			log.warning("Length > 40 - truncated");
			VFormat = VFormat.substring(0, 39);
		}
		set_Value("VFormat", VFormat);
	}

	/**
	 * Get Value Format. Format of the value; Can contain fixed format elements,
	 * Variables: "_lLoOaAcCa09"
	 */
	public String getVFormat() {
		return (String) get_Value("VFormat");
	}

	/** ValidationType AD_Reference_ID=2 */
	public static final int VALIDATIONTYPE_AD_Reference_ID = 2;

	/** DataType = D */
	public static final String VALIDATIONTYPE_DataType = "D";

	/** List Validation = L */
	public static final String VALIDATIONTYPE_ListValidation = "L";

	/** Table Validation = T */
	public static final String VALIDATIONTYPE_TableValidation = "T";

	/**
	 * Set Validation type. Different method of validating data
	 */
	public void setValidationType(String ValidationType) {
		if (ValidationType == null)
			throw new IllegalArgumentException("ValidationType is mandatory");
		if (ValidationType.equals("D") || ValidationType.equals("L")
				|| ValidationType.equals("T"))
			;
		else
			throw new IllegalArgumentException(
					"ValidationType Invalid value - " + ValidationType
							+ " - Reference_ID=2 - D - L - T");
		if (ValidationType.length() > 1) {
			log.warning("Length > 1 - truncated");
			ValidationType = ValidationType.substring(0, 0);
		}
		set_Value("ValidationType", ValidationType);
	}

	/**
	 * Get Validation type. Different method of validating data
	 */
	public String getValidationType() {
		return (String) get_Value("ValidationType");
	}
}
