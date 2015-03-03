/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for K_EntryRelated
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.671
 */
public class X_K_EntryRelated extends PO {
	/** Standard Constructor */
	public X_K_EntryRelated(Properties ctx, int K_EntryRelated_ID,
			String trxName) {
		super(ctx, K_EntryRelated_ID, trxName);
		/**
		 * if (K_EntryRelated_ID == 0) { setK_EntryRelated_ID (0); setK_Entry_ID
		 * (0); }
		 */
	}

	/** Load Constructor */
	public X_K_EntryRelated(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=K_EntryRelated */
	public static final String Table_Name = "K_EntryRelated";

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
		StringBuffer sb = new StringBuffer("X_K_EntryRelated[")
				.append(get_ID()).append("]");
		return sb.toString();
	}

	/** K_EntryRelated_ID AD_Reference_ID=285 */
	public static final int K_ENTRYRELATED_ID_AD_Reference_ID = 285;

	/**
	 * Set Related Entry. Related Entry for this Enntry
	 */
	public void setK_EntryRelated_ID(int K_EntryRelated_ID) {
		if (K_EntryRelated_ID < 1)
			throw new IllegalArgumentException(
					"K_EntryRelated_ID is mandatory.");
		set_ValueNoCheck("K_EntryRelated_ID", new Integer(K_EntryRelated_ID));
	}

	/**
	 * Get Related Entry. Related Entry for this Enntry
	 */
	public int getK_EntryRelated_ID() {
		Integer ii = (Integer) get_Value("K_EntryRelated_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getK_EntryRelated_ID()));
	}

	/**
	 * Set Entry. Knowledge Entry
	 */
	public void setK_Entry_ID(int K_Entry_ID) {
		if (K_Entry_ID < 1)
			throw new IllegalArgumentException("K_Entry_ID is mandatory.");
		set_ValueNoCheck("K_Entry_ID", new Integer(K_Entry_ID));
	}

	/**
	 * Get Entry. Knowledge Entry
	 */
	public int getK_Entry_ID() {
		Integer ii = (Integer) get_Value("K_Entry_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name != null && Name.length() > 60) {
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
}
