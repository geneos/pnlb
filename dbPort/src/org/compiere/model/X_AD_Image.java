/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Image
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.484
 */
public class X_AD_Image extends PO {
	/** Standard Constructor */
	public X_AD_Image(Properties ctx, int AD_Image_ID, String trxName) {
		super(ctx, AD_Image_ID, trxName);
		/**
		 * if (AD_Image_ID == 0) { setAD_Image_ID (0); setEntityType (null);
		 * setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Image(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Image */
	public static final String Table_Name = "AD_Image";

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
		StringBuffer sb = new StringBuffer("X_AD_Image[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Image. System Image or Icon
	 */
	public void setAD_Image_ID(int AD_Image_ID) {
		if (AD_Image_ID < 1)
			throw new IllegalArgumentException("AD_Image_ID is mandatory.");
		set_ValueNoCheck("AD_Image_ID", new Integer(AD_Image_ID));
	}

	/**
	 * Get Image. System Image or Icon
	 */
	public int getAD_Image_ID() {
		Integer ii = (Integer) get_Value("AD_Image_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set BinaryData. Binary Data
	 */
	public void setBinaryData(byte[] BinaryData) {
		set_Value("BinaryData", BinaryData);
	}

	/**
	 * Get BinaryData. Binary Data
	 */
	public byte[] getBinaryData() {
		return (byte[]) get_Value("BinaryData");
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
	 * Set Image URL. URL of image
	 */
	public void setImageURL(String ImageURL) {
		if (ImageURL != null && ImageURL.length() > 120) {
			log.warning("Length > 120 - truncated");
			ImageURL = ImageURL.substring(0, 119);
		}
		set_Value("ImageURL", ImageURL);
	}

	/**
	 * Get Image URL. URL of image
	 */
	public String getImageURL() {
		return (String) get_Value("ImageURL");
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
