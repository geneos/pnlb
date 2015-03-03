/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_Dunning
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.328
 */
public class X_C_Dunning extends PO {
	/** Standard Constructor */
	public X_C_Dunning(Properties ctx, int C_Dunning_ID, String trxName) {
		super(ctx, C_Dunning_ID, trxName);
		/**
		 * if (C_Dunning_ID == 0) { setC_Dunning_ID (0); setIsDefault (false);
		 * setName (null); setSendDunningLetter (false); }
		 */
	}

	/** Load Constructor */
	public X_C_Dunning(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_Dunning */
	public static final String Table_Name = "C_Dunning";

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
		StringBuffer sb = new StringBuffer("X_C_Dunning[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Dunning. Dunning Rules for overdue invoices
	 */
	public void setC_Dunning_ID(int C_Dunning_ID) {
		if (C_Dunning_ID < 1)
			throw new IllegalArgumentException("C_Dunning_ID is mandatory.");
		set_ValueNoCheck("C_Dunning_ID", new Integer(C_Dunning_ID));
	}

	/**
	 * Get Dunning. Dunning Rules for overdue invoices
	 */
	public int getC_Dunning_ID() {
		Integer ii = (Integer) get_Value("C_Dunning_ID");
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

	/**
	 * Set Default. Default value
	 */
	public void setIsDefault(boolean IsDefault) {
		set_Value("IsDefault", new Boolean(IsDefault));
	}

	/**
	 * Get Default. Default value
	 */
	public boolean isDefault() {
		Object oo = get_Value("IsDefault");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
	 * Set Send dunning letters. Indicates if dunning letters will be sent
	 */
	public void setSendDunningLetter(boolean SendDunningLetter) {
		set_Value("SendDunningLetter", new Boolean(SendDunningLetter));
	}

	/**
	 * Get Send dunning letters. Indicates if dunning letters will be sent
	 */
	public boolean isSendDunningLetter() {
		Object oo = get_Value("SendDunningLetter");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
}
