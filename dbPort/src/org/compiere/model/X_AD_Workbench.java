/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Workbench
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.968
 */
public class X_AD_Workbench extends PO {
	/** Standard Constructor */
	public X_AD_Workbench(Properties ctx, int AD_Workbench_ID, String trxName) {
		super(ctx, AD_Workbench_ID, trxName);
		/**
		 * if (AD_Workbench_ID == 0) { setAD_Column_ID (0); setAD_Workbench_ID
		 * (0); setEntityType (null); // U setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Workbench(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Workbench */
	public static final String Table_Name = "AD_Workbench";

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
		StringBuffer sb = new StringBuffer("X_AD_Workbench[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set System Color. Color for backgrounds or indicators
	 */
	public void setAD_Color_ID(Object AD_Color_ID) {
		set_Value("AD_Color_ID", AD_Color_ID);
	}

	/**
	 * Get System Color. Color for backgrounds or indicators
	 */
	public Object getAD_Color_ID() {
		return get_Value("AD_Color_ID");
	}

	/** AD_Column_ID AD_Reference_ID=244 */
	public static final int AD_COLUMN_ID_AD_Reference_ID = 244;

	/**
	 * Set Column. Column in the table
	 */
	public void setAD_Column_ID(int AD_Column_ID) {
		if (AD_Column_ID < 1)
			throw new IllegalArgumentException("AD_Column_ID is mandatory.");
		set_Value("AD_Column_ID", new Integer(AD_Column_ID));
	}

	/**
	 * Get Column. Column in the table
	 */
	public int getAD_Column_ID() {
		Integer ii = (Integer) get_Value("AD_Column_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Image. System Image or Icon
	 */
	public void setAD_Image_ID(byte[] AD_Image_ID) {
		set_Value("AD_Image_ID", AD_Image_ID);
	}

	/**
	 * Get Image. System Image or Icon
	 */
	public byte[] getAD_Image_ID() {
		return (byte[]) get_Value("AD_Image_ID");
	}

	/**
	 * Set Workbench. Collection of windows, reports
	 */
	public void setAD_Workbench_ID(int AD_Workbench_ID) {
		if (AD_Workbench_ID < 1)
			throw new IllegalArgumentException("AD_Workbench_ID is mandatory.");
		set_ValueNoCheck("AD_Workbench_ID", new Integer(AD_Workbench_ID));
	}

	/**
	 * Get Workbench. Collection of windows, reports
	 */
	public int getAD_Workbench_ID() {
		Integer ii = (Integer) get_Value("AD_Workbench_ID");
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
}
