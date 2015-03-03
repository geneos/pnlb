/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for K_Source
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.687
 */
public class X_K_Source extends PO {
	/** Standard Constructor */
	public X_K_Source(Properties ctx, int K_Source_ID, String trxName) {
		super(ctx, K_Source_ID, trxName);
		/**
		 * if (K_Source_ID == 0) { setK_Source_ID (0); setName (null); }
		 */
	}

	/** Load Constructor */
	public X_K_Source(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=K_Source */
	public static final String Table_Name = "K_Source";

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
		StringBuffer sb = new StringBuffer("X_K_Source[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Description URL. URL for the description
	 */
	public void setDescriptionURL(String DescriptionURL) {
		if (DescriptionURL != null && DescriptionURL.length() > 120) {
			log.warning("Length > 120 - truncated");
			DescriptionURL = DescriptionURL.substring(0, 119);
		}
		set_Value("DescriptionURL", DescriptionURL);
	}

	/**
	 * Get Description URL. URL for the description
	 */
	public String getDescriptionURL() {
		return (String) get_Value("DescriptionURL");
	}

	/**
	 * Set Knowledge Source. Source of a Knowledge Entry
	 */
	public void setK_Source_ID(int K_Source_ID) {
		if (K_Source_ID < 1)
			throw new IllegalArgumentException("K_Source_ID is mandatory.");
		set_ValueNoCheck("K_Source_ID", new Integer(K_Source_ID));
	}

	/**
	 * Get Knowledge Source. Source of a Knowledge Entry
	 */
	public int getK_Source_ID() {
		Integer ii = (Integer) get_Value("K_Source_ID");
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
