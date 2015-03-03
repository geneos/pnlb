/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Form
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.453
 */
public class X_AD_Form extends PO {
	/** Standard Constructor */
	public X_AD_Form(Properties ctx, int AD_Form_ID, String trxName) {
		super(ctx, AD_Form_ID, trxName);
		/**
		 * if (AD_Form_ID == 0) { setAD_Form_ID (0); setAccessLevel (null);
		 * setEntityType (null); // U setIsBetaFunctionality (false); setName
		 * (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Form(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Form */
	public static final String Table_Name = "AD_Form";

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
		StringBuffer sb = new StringBuffer("X_AD_Form[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Special Form. Special Form
	 */
	public void setAD_Form_ID(int AD_Form_ID) {
		if (AD_Form_ID < 1)
			throw new IllegalArgumentException("AD_Form_ID is mandatory.");
		set_ValueNoCheck("AD_Form_ID", new Integer(AD_Form_ID));
	}

	/**
	 * Get Special Form. Special Form
	 */
	public int getAD_Form_ID() {
		Integer ii = (Integer) get_Value("AD_Form_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AccessLevel AD_Reference_ID=5 */
	public static final int ACCESSLEVEL_AD_Reference_ID = 5;

	/** Organization = 1 */
	public static final String ACCESSLEVEL_Organization = "1";

	/** Client only = 2 */
	public static final String ACCESSLEVEL_ClientOnly = "2";

	/** Client+Organization = 3 */
	public static final String ACCESSLEVEL_ClientPlusOrganization = "3";

	/** System only = 4 */
	public static final String ACCESSLEVEL_SystemOnly = "4";

	/** System+Client = 6 */
	public static final String ACCESSLEVEL_SystemPlusClient = "6";

	/** All = 7 */
	public static final String ACCESSLEVEL_All = "7";

	/**
	 * Set Data Access Level. Access Level required
	 */
	public void setAccessLevel(String AccessLevel) {
		if (AccessLevel == null)
			throw new IllegalArgumentException("AccessLevel is mandatory");
		if (AccessLevel.equals("1") || AccessLevel.equals("2")
				|| AccessLevel.equals("3") || AccessLevel.equals("4")
				|| AccessLevel.equals("6") || AccessLevel.equals("7"))
			;
		else
			throw new IllegalArgumentException("AccessLevel Invalid value - "
					+ AccessLevel + " - Reference_ID=5 - 1 - 2 - 3 - 4 - 6 - 7");
		if (AccessLevel.length() > 1) {
			log.warning("Length > 1 - truncated");
			AccessLevel = AccessLevel.substring(0, 0);
		}
		set_Value("AccessLevel", AccessLevel);
	}

	/**
	 * Get Data Access Level. Access Level required
	 */
	public String getAccessLevel() {
		return (String) get_Value("AccessLevel");
	}

	/**
	 * Set Classname. Java Classname
	 */
	public void setClassname(String Classname) {
		if (Classname != null && Classname.length() > 60) {
			log.warning("Length > 60 - truncated");
			Classname = Classname.substring(0, 59);
		}
		set_Value("Classname", Classname);
	}

	/**
	 * Get Classname. Java Classname
	 */
	public String getClassname() {
		return (String) get_Value("Classname");
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
	 * Set Beta Functionality. This functionality is considered Beta
	 */
	public void setIsBetaFunctionality(boolean IsBetaFunctionality) {
		set_Value("IsBetaFunctionality", new Boolean(IsBetaFunctionality));
	}

	/**
	 * Get Beta Functionality. This functionality is considered Beta
	 */
	public boolean isBetaFunctionality() {
		Object oo = get_Value("IsBetaFunctionality");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set jsp URL. Web URL of the jsp function
	 */
	public void setJSPURL(String JSPURL) {
		if (JSPURL != null && JSPURL.length() > 120) {
			log.warning("Length > 120 - truncated");
			JSPURL = JSPURL.substring(0, 119);
		}
		set_Value("JSPURL", JSPURL);
	}

	/**
	 * Get jsp URL. Web URL of the jsp function
	 */
	public String getJSPURL() {
		return (String) get_Value("JSPURL");
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
