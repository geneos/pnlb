/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for K_Topic
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.703
 */
public class X_K_Topic extends PO {
	/** Standard Constructor */
	public X_K_Topic(Properties ctx, int K_Topic_ID, String trxName) {
		super(ctx, K_Topic_ID, trxName);
		/**
		 * if (K_Topic_ID == 0) { setIsPublic (true); // Y setIsPublicWrite
		 * (true); // Y setK_Topic_ID (0); setK_Type_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_K_Topic(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=K_Topic */
	public static final String Table_Name = "K_Topic";

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

	protected BigDecimal accessLevel = new BigDecimal(3);

	/** AccessLevel 3 - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_K_Topic[").append(get_ID())
				.append("]");
		return sb.toString();
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
	 * Set Public. Public can read entry
	 */
	public void setIsPublic(boolean IsPublic) {
		set_Value("IsPublic", new Boolean(IsPublic));
	}

	/**
	 * Get Public. Public can read entry
	 */
	public boolean isPublic() {
		Object oo = get_Value("IsPublic");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Public Write. Public can write entries
	 */
	public void setIsPublicWrite(boolean IsPublicWrite) {
		set_Value("IsPublicWrite", new Boolean(IsPublicWrite));
	}

	/**
	 * Get Public Write. Public can write entries
	 */
	public boolean isPublicWrite() {
		Object oo = get_Value("IsPublicWrite");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Knowledge Topic. Knowledge Topic
	 */
	public void setK_Topic_ID(int K_Topic_ID) {
		if (K_Topic_ID < 1)
			throw new IllegalArgumentException("K_Topic_ID is mandatory.");
		set_ValueNoCheck("K_Topic_ID", new Integer(K_Topic_ID));
	}

	/**
	 * Get Knowledge Topic. Knowledge Topic
	 */
	public int getK_Topic_ID() {
		Integer ii = (Integer) get_Value("K_Topic_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Knowldge Type. Knowledge Type
	 */
	public void setK_Type_ID(int K_Type_ID) {
		if (K_Type_ID < 1)
			throw new IllegalArgumentException("K_Type_ID is mandatory.");
		set_ValueNoCheck("K_Type_ID", new Integer(K_Type_ID));
	}

	/**
	 * Get Knowldge Type. Knowledge Type
	 */
	public int getK_Type_ID() {
		Integer ii = (Integer) get_Value("K_Type_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
