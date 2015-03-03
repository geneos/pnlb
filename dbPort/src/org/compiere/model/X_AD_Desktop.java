/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Desktop
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.359
 */
public class X_AD_Desktop extends PO {
	/** Standard Constructor */
	public X_AD_Desktop(Properties ctx, int AD_Desktop_ID, String trxName) {
		super(ctx, AD_Desktop_ID, trxName);
		/**
		 * if (AD_Desktop_ID == 0) { setAD_Desktop_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_Desktop(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Desktop */
	public static final String Table_Name = "AD_Desktop";

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
		StringBuffer sb = new StringBuffer("X_AD_Desktop[").append(get_ID())
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

	/**
	 * Set Desktop. Collection of Workbenches
	 */
	public void setAD_Desktop_ID(int AD_Desktop_ID) {
		if (AD_Desktop_ID < 1)
			throw new IllegalArgumentException("AD_Desktop_ID is mandatory.");
		set_ValueNoCheck("AD_Desktop_ID", new Integer(AD_Desktop_ID));
	}

	/**
	 * Get Desktop. Collection of Workbenches
	 */
	public int getAD_Desktop_ID() {
		Integer ii = (Integer) get_Value("AD_Desktop_ID");
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
